package space.cloud4b.verein.model.verein;

import space.cloud4b.verein.model.verein.adressbuch.AdressBuch;
import space.cloud4b.verein.model.verein.kalender.Kalender;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Rangliste;

import java.util.Calendar;

public class Verein {

    String vereinsName;
    Kalender kalender;
    AdressBuch adressBuch;
    Rangliste rangliste;


    public Verein(String vereinsName) {
        System.out.println("Verein " + vereinsName + " wurde erstellt");
        this.vereinsName = vereinsName;
        this.kalender = new Kalender("Kalender des Vereins " + vereinsName);
        this.adressBuch = new AdressBuch( "Adressbuch des Vereins " + vereinsName, this);
        this.rangliste = new Rangliste("Anwesenheitsstatistik", this);

    }

    public String getVereinsName() {
        return this.vereinsName;
    }

    public Rangliste getRangliste() { return this.rangliste; }

    public AdressBuch getAdressBuch(){
        return this.adressBuch;
    }
    public Kalender getKalender() { return this.kalender; }

    public String toString() {
        return vereinsName;
    }
}
