package space.cloud4b.verein.model.verein.adressbuch;

import space.cloud4b.verein.model.verein.kalender.Termin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Mitglied extends  Kontakt {

    private LocalDate eintrittsDatum;
    private LocalDate austrittsDatum;

    public Mitglied(int kontaktId, String nachName, String vorName) {

        super(kontaktId, nachName, vorName);
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
}
