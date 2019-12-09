package space.cloud4b.verein.model.verein.adressbuch;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Mitglied extends  Kontakt {

    private LocalDate eintrittsDatum;
    private LocalDate austrittsDatum;
    private StatusElement kategorieIStatus;
    private StatusElement kategorieIIStatus;
    private boolean istVorstandsmitglied;

    public Mitglied(int kontaktId, String nachName, String vorName) {

        super(kontaktId, nachName, vorName);
        System.out.println("Konstruktor Mitglied");
    }
    public Mitglied(int kontaktId, String nachName, String vorName, String eintrittsDatum) {

        super(kontaktId, nachName, vorName);
        this.eintrittsDatum = Date.valueOf(eintrittsDatum).toLocalDate();
        System.out.println("Konstruktor Mitglied");
    }

    public void setEintrittsDatum(LocalDate eintrittsDatum) {
        this.eintrittsDatum = eintrittsDatum;
    }

    public String getEintrittsDatumAsISOString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        return this.eintrittsDatum.format(formatter);
    }

    public LocalDate getEintrittsdatum() {
        return this.eintrittsDatum;
    }

    public String getKurzbezeichnung() {
        return super.getKurzbezeichnung();
        //TODO evtl. erweitern mit Mitglied-Typ
    }

    // Kategorie I
    public ObjectProperty<StatusElement> getKategorieIElementProperty() { return new SimpleObjectProperty<StatusElement>(kategorieIStatus); }
    public void setKategorieIStatus(StatusElement kategorieIStatus) {this.kategorieIStatus = kategorieIStatus; }
    public StatusElement getKategorieIElement() {
        return kategorieIStatus;
    }
    public ObjectProperty<StatusElement> getKategorieIProperty() { return new SimpleObjectProperty<StatusElement>(this.kategorieIStatus); }

    // Kategorie II
    public ObjectProperty<StatusElement> getKategorieIIElementProperty() { return new SimpleObjectProperty<StatusElement>(kategorieIIStatus); }
    public void setKategorieIIStatus(StatusElement kategorieIIStatus) {this.kategorieIIStatus = kategorieIIStatus; }
    public StatusElement getKategorieIIElement() {
        return kategorieIIStatus;
    }
    public ObjectProperty<StatusElement> getKategorieIIProperty() { return new SimpleObjectProperty<StatusElement>(this.kategorieIIStatus); }

    // Vorstandsmitglied
    public void setIstVorstandsmitglied(boolean istVorstandsmitglied) {
        this.istVorstandsmitglied = istVorstandsmitglied;
    }
    public BooleanProperty getIstVorstandsmitglied() { return new SimpleBooleanProperty(istVorstandsmitglied);}
}
