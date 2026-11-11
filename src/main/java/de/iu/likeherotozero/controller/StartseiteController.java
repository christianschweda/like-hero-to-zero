package de.iu.likeherotozero.controller;

import de.iu.likeherotozero.service.Co2Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller   // diese Klasse beantwortet Anfragen aus dem Browser
public class StartseiteController {

    private final Co2Service co2Service;

    public StartseiteController(Co2Service co2Service) {
        this.co2Service = co2Service;
    }

    @GetMapping("/")   // zuständig für die Adresse localhost:8080/
    public String startseite(@RequestParam(name = "land", required = false) String isoCode,
                             Model model) {

        // 1. Länderliste immer mitgeben (für das Auswahlfeld)
        model.addAttribute("laender", co2Service.alleLaender());

        // 2. Wurde ein Land gewählt? Dann neuesten Wert suchen
        if (isoCode != null && !isoCode.isBlank()) {
            co2Service.findeLand(isoCode).ifPresent(land -> {
                model.addAttribute("ausgewaehltesLand", land);
                co2Service.neuesteEmission(land)
                        .ifPresent(emission -> model.addAttribute("emission", emission));
            });
        }

        // 3. Name des Templates: Spring zeigt templates/index.html an
        return "index";
    }
}