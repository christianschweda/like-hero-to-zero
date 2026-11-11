package de.iu.likeherotozero.repository;

import de.iu.likeherotozero.model.Land;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LandRepository extends JpaRepository<Land, Long> {

    Optional<Land> findByIsoCode(String isoCode);   // ein Land über den ISO-Code finden

    List<Land> findAllByOrderByNameAsc();           // alle Länder alphabetisch
}