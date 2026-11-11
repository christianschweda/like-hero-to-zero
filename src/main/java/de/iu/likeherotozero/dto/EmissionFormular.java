package de.iu.likeherotozero.dto;

import de.iu.likeherotozero.model.Emission;

public class EmissionFormular {

    private Long id;              // leer = neuer Eintrag, gefüllt = Bearbeiten
    private String landIsoCode;
    private Integer jahr;         // Integer statt int: darf leer sein, wenn nichts eingegeben wurde
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
