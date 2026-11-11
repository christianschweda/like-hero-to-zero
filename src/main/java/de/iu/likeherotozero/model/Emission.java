package de.iu.likeherotozero.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emission",
        uniqueConstraints = @UniqueConstraint(columnNames = {"land_id", "jahr"}))  // pro Land und Jahr nur ein Wert
public class Emission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)           // viele Emissionen gehören zu einem Land
    @JoinColumn(name = "land_id")          // Fremdschlüssel-Spalte
    private Land land;

    @Column(nullable = false)
    private int jahr;

    @Column(name = "co2_mt", nullable = false)
    private double co2Mt;                  // Millionen Tonnen CO2

    @Column(nullable = false, length = 50)
    private String quelle;

    protected Emission() {
    }

    public Emission(Land land, int jahr, double co2Mt, String quelle) {
        this.land = land;
        this.jahr = jahr;
        this.co2Mt = co2Mt;
        this.quelle = quelle;
    }

    public Long getId() { return id; }
    public Land getLand() { return land; }
    public void setLand(Land land) { this.land = land; }
    public int getJahr() { return jahr; }
    public void setJahr(int jahr) { this.jahr = jahr; }
    public double getCo2Mt() { return co2Mt; }
    public void setCo2Mt(double co2Mt) { this.co2Mt = co2Mt; }
    public String getQuelle() { return quelle; }
    public void setQuelle(String quelle) { this.quelle = quelle; }
}