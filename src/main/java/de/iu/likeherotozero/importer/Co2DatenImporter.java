package de.iu.likeherotozero.importer;

import de.iu.likeherotozero.model.Emission;
import de.iu.likeherotozero.model.Land;
import de.iu.likeherotozero.repository.EmissionRepository;
import de.iu.likeherotozero.repository.LandRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component   // Spring erzeugt automatisch ein Objekt dieser Klasse (eine "Bean")
public class Co2DatenImporter implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Co2DatenImporter.class);
    private static final String DATEI = "data/owid-co2-data.csv";
    private static final int AB_JAHR = 1990;
    private static final String QUELLE = "Our World in Data";

    private final LandRepository landRepository;
    private final EmissionRepository emissionRepository;

    // Spring übergibt die Repositories automatisch (Dependency Injection, wie @Inject im Skript)
    public Co2DatenImporter(LandRepository landRepository, EmissionRepository emissionRepository) {
        this.landRepository = landRepository;
        this.emissionRepository = emissionRepository;
    }

    @Override
    @Transactional   // alles in einer Transaktion: ganz oder gar nicht (AKID aus dem Skript)
    public void run(String... args) throws Exception {

        // 1. Nur importieren, wenn die Datenbank noch leer ist
        if (emissionRepository.count() > 0) {
            log.info("CO2-Daten sind bereits vorhanden, Import wird übersprungen.");
            return;
        }

        Map<String, Land> laender = new HashMap<>();   // bereits angelegte Länder merken
        List<Emission> emissionen = new ArrayList<>();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()                 // erste Zeile enthält die Spaltennamen
                .setSkipHeaderRecord(true)
                .build();

        // 2. Datei Zeile für Zeile lesen
        try (Reader reader = new InputStreamReader(
                new ClassPathResource(DATEI).getInputStream(), StandardCharsets.UTF_8)) {

            for (CSVRecord zeile : format.parse(reader)) {
                String isoCode = zeile.get("iso_code");
                String co2Text = zeile.get("co2");
                int jahr = Integer.parseInt(zeile.get("year"));

                // 3. Filtern: keine Regionen, keine leeren Werte, nur ab 1990
                if (isoCode.isBlank() || isoCode.startsWith("OWID")
                        || co2Text.isBlank() || jahr < AB_JAHR) {
                    continue;
                }

                // 4. Land nur beim ersten Auftreten anlegen
                Land land = laender.computeIfAbsent(isoCode,
                        code -> landRepository.save(new Land(code, zeile.get("country"))));

                emissionen.add(new Emission(land, jahr, Double.parseDouble(co2Text), QUELLE));
            }
        }

        // 5. Alle Werte speichern
        emissionRepository.saveAll(emissionen);
        log.info("Import fertig: {} Länder, {} Emissionswerte.", laender.size(), emissionen.size());
    }
}