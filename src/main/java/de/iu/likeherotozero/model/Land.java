package de.iu.likeherotozero.model;

import jakarta.persistence.*;

@Entity                      // Diese Klasse wird als Tabelle gespeichert
@Table(name = "land")        // Name der Tabelle in MySQL
public class Land {

    @Id                                                   // Primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // MySQL vergibt die Nummer automatisch
    private Long id;

    @Column(name = "iso_code", nullable = false, unique = true, length = 3)
    private String isoCode;

    @Column(nullable = false)
    private String name;

    protected Land() {
        // Leerer Konstruktor: Pflicht für JPA (wie im Skript)
    }

    public Land(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getIsoCode() { return isoCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}