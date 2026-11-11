package de.iu.likeherotozero.security;

import de.iu.likeherotozero.model.Benutzer;
import de.iu.likeherotozero.repository.BenutzerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoBenutzerAnlage implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoBenutzerAnlage.class);

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoBenutzerAnlage(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (benutzerRepository.count() > 0) {
            return;   // es gibt schon Benutzer, nichts tun
        }
        String hash = passwordEncoder.encode("klima2026");   // Passwort hashen, bevor es gespeichert wird
        benutzerRepository.save(new Benutzer("forscherin", hash, "WISSENSCHAFTLER"));
        log.info("Demo-Benutzer 'forscherin' wurde angelegt.");
    }
}