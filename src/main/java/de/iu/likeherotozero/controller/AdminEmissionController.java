package de.iu.likeherotozero.controller;

import de.iu.likeherotozero.dto.EmissionFormular;
import de.iu.likeherotozero.model.Emission;
import de.iu.likeherotozero.service.Co2Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/emissionen")   // alle Adressen dieser Klasse beginnen mit /admin/emissionen
public class AdminEmissionController {

    private final Co2Service co2Service;

    public AdminEmissionController(Co2Service co2Service) {
        this.co2Service = co2Service;
    }

    // Übersicht: /admin/emissionen?land=DEU
    @GetMapping
    public String uebersicht(@RequestParam(name = "land", required = false) String isoCode, Model model) {
        model.addAttribute("laender", co2Service.alleLaender());
        if (isoCode != null && !isoCode.isBlank()) {
            co2Service.findeLand(isoCode).ifPresent(land -> {
                model.addAttribute("ausgewaehltesLand", land);
                model.addAttribute("emissionen", co2Service.alleEmissionen(land));
            });
        }
        return "admin/emissionen";
    }

    // Leeres Formular: /admin/emissionen/neu?land=DEU
    @GetMapping("/neu")
    public String neu(@RequestParam(name = "land", required = false) String isoCode, Model model) {
        EmissionFormular formular = new EmissionFormular();
        formular.setLandIsoCode(isoCode);   // Land schon vorauswählen, falls bekannt
        return zeigeFormular(formular, model);
    }

    // Formular mit vorhandenen Werten: /admin/emissionen/42/bearbeiten
    @GetMapping("/{id}/bearbeiten")
    public String bearbeiten(@PathVariable Long id, Model model) {
        Emission emission = co2Service.findeEmission(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return zeigeFormular(EmissionFormular.aus(emission), model);
    }

    // Formular abschicken
    @PostMapping("/speichern")
    public String speichern(@ModelAttribute("formular") EmissionFormular formular,
                            Model model, RedirectAttributes redirectAttributes) {
        try {
            co2Service.speichern(formular);
        } catch (IllegalArgumentException e) {
            model.addAttribute("fehler", e.getMessage());
            return zeigeFormular(formular, model);   // Formular mit Eingaben und Fehlermeldung erneut zeigen
        }
        redirectAttributes.addFlashAttribute("meldung", "Der Wert wurde gespeichert.");
        return "redirect:/admin/emissionen?land=" + formular.getLandIsoCode();
    }

    // Hilfsmethode: Formularseite vorbereiten
    private String zeigeFormular(EmissionFormular formular, Model model) {
        model.addAttribute("formular", formular);
        model.addAttribute("laender", co2Service.alleLaender());
        return "admin/formular";
    }
}