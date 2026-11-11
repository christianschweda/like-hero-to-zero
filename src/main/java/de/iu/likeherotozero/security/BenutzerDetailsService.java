package de.iu.likeherotozero.security;

import de.iu.likeherotozero.model.Benutzer;
import de.iu.likeherotozero.repository.BenutzerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BenutzerDetailsService implements UserDetailsService {

    private final BenutzerRepository benutzerRepository;

    public BenutzerDetailsService(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    // Spring Security ruft diese Methode beim Login mit dem eingegebenen Benutzernamen auf
    @Override
    public UserDetails loadUserByUsername(String benutzername) throws UsernameNotFoundException {
        Benutzer benutzer = benutzerRepository.findByBenutzername(benutzername)
                .orElseThrow(() -> new UsernameNotFoundException("Unbekannter Benutzer: " + benutzername));

        // Unseren Benutzer in das Format übersetzen, das Spring Security versteht
        return User.withUsername(benutzer.getBenutzername())
                .password(benutzer.getPasswortHash())
                .roles(benutzer.getRolle())
                .build();
    }
}