package de.iu.likeherotozero.service;

import de.iu.likeherotozero.model.Emission;
import de.iu.likeherotozero.model.Land;
import de.iu.likeherotozero.repository.EmissionRepository;
import de.iu.likeherotozero.repository.LandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import de.iu.likeherotozero.dto.EmissionFormular;
import java.time.Year;

@Service                          // Spring erzeugt automatisch ein Objekt (Bean) dieser Klasse
@Transactional(readOnly = true)   // alle Methoden lesen nur, schreiben nichts
public class Co2Service {

    private static final String QUELLE_EINGABE = "Eingabe Wissenschaftler:in";

    private final LandRepository landRepository;
    private final EmissionRepository emissionRepository;

    public Co2Service(LandRepository landRepository, EmissionRepository emissionRepository) {
        this.landRepository = landRepository;
        this.emissionRepository = emissionRepository;
    }

    // Alle Länder alphabetisch, für die Auswahlliste
    public List<Land> alleLaender() {
        return landRepository.findAllByOrderByNameAsc();
    }

    // Ein Land über seinen ISO-Code finden, z. B. "DEU"
    public Optional<Land> findeLand(String isoCode) {
        return landRepository.findByIsoCode(isoCode);
    }

    // Den neuesten CO2-Wert eines Landes finden
    public Optional<Emission> neuesteEmission(Land land) {
        return emissionRepository.findFirstByLandOrderByJahrDesc(land);
    }

    // Alle Werte eines Landes, neueste zuerst (für die Tabelle)
    public List<Emission> alleEmissionen(Land land) {
        return emissionRepository.findByLandOrderByJahrDesc(land);
    }

    // Einen einzelnen Wert über seine id finden (zum Bearbeiten)
    public Optional<Emission> findeEmission(Long id) {
        return emissionRepository.findById(id);
    }

    // Neuen Wert anlegen oder vorhandenen ändern
    @Transactional   // überschreibt readOnly: diese Methode darf schreiben
    public Emission speichern(EmissionFormular formular) {

        if (formular.getJahr() == null || formular.getCo2Mt() == null) {
            throw new IllegalArgumentException("Bitte Jahr und CO₂-Wert angeben.");
        }
        if (formular.getJahr() > Year.now().getValue()) {
            throw new IllegalArgumentException("Das Jahr darf nicht in der Zukunft liegen.");
        }

        Land land = landRepository.findByIsoCode(formular.getLandIsoCode())
                .orElseThrow(() -> new IllegalArgumentException("Bitte ein gültiges Land auswählen."));

        Optional<Emission> vorhanden = emissionRepository.findByLandAndJahr(land, formular.getJahr());
        Emission emission;

        if (formular.getId() == null) {
            // Fall 1: neuer Eintrag
            if (vorhanden.isPresent()) {
                throw new IllegalArgumentException("Für " + land.getName() + " gibt es im Jahr "
                        + formular.getJahr() + " bereits einen Wert. Bitte den vorhandenen Eintrag bearbeiten.");
            }
            emission = new Emission(land, formular.getJahr(), formular.getCo2Mt(), QUELLE_EINGABE);

        } else {
            // Fall 2: vorhandenen Eintrag ändern
            emission = emissionRepository.findById(formular.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Der Eintrag existiert nicht mehr."));
            if (vorhanden.isPresent() && !vorhanden.get().getId().equals(emission.getId())) {
                throw new IllegalArgumentException("Für dieses Jahr gibt es bereits einen anderen Eintrag.");
            }
            emission.setJahr(formular.getJahr());
            emission.setCo2Mt(formular.getCo2Mt());
            emission.setQuelle(QUELLE_EINGABE);
        }

        return emissionRepository.save(emission);
    }
}