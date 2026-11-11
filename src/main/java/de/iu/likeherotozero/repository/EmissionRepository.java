package de.iu.likeherotozero.repository;

import de.iu.likeherotozero.model.Emission;
import de.iu.likeherotozero.model.Land;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmissionRepository extends JpaRepository<Emission, Long> {

    Optional<Emission> findFirstByLandOrderByJahrDesc(Land land);  // neuester Wert eines Landes

    List<Emission> findByLandOrderByJahrDesc(Land land);           // alle Werte, neueste zuerst
}