package de.iu.likeherotozero.service;

import de.iu.likeherotozero.model.Emission;
import de.iu.likeherotozero.model.Land;
import de.iu.likeherotozero.repository.EmissionRepository;
import de.iu.likeherotozero.repository.LandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service                          // Spring erzeugt automatisch ein Objekt (Bean) dieser Klasse
@Transactional(readOnly = true)   // alle Methoden lesen nur, schreiben nichts
public class Co2Service {

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
}