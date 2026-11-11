package de.iu.likeherotozero.model;

import jakarta.persistence.*;

@Entity
@Table(name = "benutzer")
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String benutzername;

    @Column(name = "passwort_hash", nullable = false, length = 100)
    private String passwortHash;      // NICHT das Passwort selbst, sondern ein "Fingerabdruck" davon

    @Column(nullable = false, length = 30)
    private String rolle;             // z. B. "WISSENSCHAFTLER"

    protected Benutzer() {
    }

    public Benutzer(String benutzername, String passwortHash, String rolle) {
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
        this.rolle = rolle;
    }

    public Long getId() { return id; }
    public String getBenutzername() { return benutzername; }
    public String getPasswortHash() { return passwortHash; }
    public String getRolle() { return rolle; }
}