package space.cloud4b.verein.model.verein.adressbuch;

import javafx.beans.property.*;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Kontakt {

    private int kontaktId;
    private String nachName;
    private String vorName;
    private String adresse;
    private String adresszusatz;
    private int plz;
    private String ort;
    private LocalDate geburtsdatum;
    private StatusElement anredeStatus;
    private String bemerkungen;
    private String mobile;
    private String telefon;
    private String eMail;
    private String eMailII;
    private LocalDate austrittsDatum;
    private String letzteAenderungUser;
    private Timestamp letzteAenderungTimestamp;

    public Kontakt(int kontaktId, String nachName, String vorName) {
        this.kontaktId = kontaktId;
        this.nachName = nachName;
        this.vorName = vorName;
        System.out.println("Konstruktor Kontakt");
    }

    public String toString() {
        return "#" + kontaktId + ": " + nachName + " " + vorName + " | " + adresse + " | " + plz + " " + ort + " (" + geburtsdatum + ")";
    }

    public String getKurzbezeichnung() {
        return this.nachName + " " + this.vorName;
    }

    // id
    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(kontaktId);
    }
    public int getId() {
        return kontaktId;
    }

    // Anrede
    public ObjectProperty<StatusElement> getAnredeElementProperty() { return new SimpleObjectProperty<StatusElement>(anredeStatus); }
    public void setAnredeStatus(StatusElement anredeStatus) {this.anredeStatus = anredeStatus; }
    public StatusElement getAnredeElement() {
        return anredeStatus;
    }
    public ObjectProperty<StatusElement> getAnredeProperty() { return new SimpleObjectProperty<StatusElement>(this.anredeStatus); }

    // Nachname
    public void setNachName(String nachname) { this.nachName = nachname; }
    public StringProperty getNachnameProperty() { return new SimpleStringProperty(nachName); }
    public String getNachName() { return this.nachName; }

    // Vorname
    public void setVorName(String vorName) { this.vorName = vorName; }
    public StringProperty getVornameProperty() { return new SimpleStringProperty(vorName); }
    public String getVorname() { return this.vorName; }

    // Adresse
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getAdresse() { return this.adresse; }

    // Adresszusatz
    public void setAdresszusatz(String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }
    public String getAdresszusatz() { return this.adresszusatz; }

    // PLZ
    public void setPlz(int plz) { this.plz = plz; }
    public int getPlz() { return this.plz; }

    // Ort
    public void setOrt(String ort) { this.ort = ort;}
    public String getOrt() { return this.ort;}
    public StringProperty getOrtProperty() { return new SimpleStringProperty(ort);}

    // Bemerkungen
    public void setBemerkungen(String bemerkungen) { this.bemerkungen = bemerkungen; }
    public String getBemerkungen() { return this.bemerkungen; }
    public StringProperty getBemerkungenProperty() { return new SimpleStringProperty(bemerkungen);}

    // Mobile
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getMobile() { return this.mobile; }
    public StringProperty getMobileProperty() { return new SimpleStringProperty(mobile);}

    // Telefon
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getTelefon() { return this.telefon; }
    public StringProperty getTelefonProperty() { return new SimpleStringProperty(telefon);}

    // E-Mail
    public void setEmail(String eMail) { this.eMail = eMail; }
    public String getEmail() { return this.eMail; }
    public StringProperty getEmailProperty() { return new SimpleStringProperty(eMail);}

    // E-Mail II
    public void setEmailII(String eMailII) { this.eMailII = eMailII; }
    public String getEmailII() { return this.eMailII; }
    public StringProperty getEmailIIProperty() { return new SimpleStringProperty(eMailII);}

    // Geburtsdatum
    public void setGeburtsdatum(LocalDate geburtsdatum) { this.geburtsdatum = geburtsdatum; }
    public LocalDate getGeburtsdatum() { return this.geburtsdatum; }

    // Austrittsdatum
    public void setAustrittsDatum(LocalDate austrittsDatum) { this.austrittsDatum = austrittsDatum; }
    public LocalDate getAustrittsDatum() { return this.austrittsDatum; }

    // letzte Änderung

    public void setLetzteAenderungUser(String letzteAenderungUser) {
        this.letzteAenderungUser = letzteAenderungUser;
    }
    public void setLetzteAenderungTimestamp(Timestamp letzteAenderungTimestamp) {
        this.letzteAenderungTimestamp = letzteAenderungTimestamp;
    }

    public String getLetzteAenderung() {
        return "letzte Änderung: " + letzteAenderungTimestamp + " (" + letzteAenderungUser + ")";
    }
}


