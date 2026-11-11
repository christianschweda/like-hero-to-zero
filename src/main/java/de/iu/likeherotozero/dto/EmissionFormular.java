package de.iu.likeherotozero.dto;

import de.iu.likeherotozero.model.Emission;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class EmissionFormular {

    private Long id;              // leer = neuer Eintrag, gefüllt = Bearbeiten

    @NotBlank(message = "Bitte ein Land auswählen.")
    private String landIsoCode;

    @NotNull(message = "Bitte ein Jahr angeben.")
    @Min(value = 1750, message = "Das Jahr muss 1750 oder später sein.")
    private Integer jahr;

    @NotNull(message = "Bitte einen CO₂-Wert angeben.")
    @PositiveOrZero(message = "Der CO₂-Wert darf nicht negativ sein.")
    @Max(value = 100000, message = "Der Wert ist unplausibel hoch (maximal 100.000 Mio. t).")
    private Double co2Mt;


    // Formular mit den Werten eines vorhandenen Eintrags füllen (zum Bearbeiten)
    public static EmissionFormular aus(Emission emission) {
        EmissionFormular formular = new EmissionFormular();
        formular.setId(emission.getId());
        formular.setLandIsoCode(emission.getLand().getIsoCode());
        formular.setJahr(emission.getJahr());
        formular.setCo2Mt(emission.getCo2Mt());
        return formular;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLandIsoCode() { return landIsoCode; }
    public void setLandIsoCode(String landIsoCode) { this.landIsoCode = landIsoCode; }
    public Integer getJahr() { return jahr; }
    public void setJahr(Integer jahr) { this.jahr = jahr; }
    public Double getCo2Mt() { return co2Mt; }
    public void setCo2Mt(Double co2Mt) { this.co2Mt = co2Mt; }
}
