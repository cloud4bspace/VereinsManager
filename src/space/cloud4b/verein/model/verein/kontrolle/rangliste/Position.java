package space.cloud4b.verein.model.verein.kontrolle.rangliste;

import javafx.beans.property.*;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

public class Position {

    private Rangliste rangliste;
    private Mitglied mitglied;
    private String mitgliedKurzbezeichnung;
    private int anzahlTermine;
    private int anzahlAnwesenheiten;
    private double anwesenheitsAnteil;
    private int rangYTD;
    private int rangVorjahr;

    public Position(Rangliste rangliste, Mitglied mitglied, String mitgliedKurzbezeichnung, int anzahlTermine, int anzahlAnwesenheiten, double anwesenheitsAnteil) {
        this.rangliste = rangliste;
        this.mitglied = mitglied;
        this.mitgliedKurzbezeichnung = mitgliedKurzbezeichnung;
        this.anzahlTermine = anzahlTermine;
        this.anzahlAnwesenheiten = anzahlAnwesenheiten;
        this.anwesenheitsAnteil = anwesenheitsAnteil;
        this.rangYTD = 9999;
        this.rangVorjahr = 9999;
        System.out.println("neue Position erstellt für " + mitglied);

    }

    public IntegerProperty getRangProperty() {
        return new SimpleIntegerProperty(this.rangYTD);
    }

    public StringProperty getKurzbezeichnungProperty() {
        return new SimpleStringProperty(this.mitgliedKurzbezeichnung);
    }

    public IntegerProperty getAnzahlTermineProperty() {
        return new SimpleIntegerProperty(this.anzahlTermine);
    }

    public IntegerProperty getAnzahlAnwesenheitenProperty() {
        return new SimpleIntegerProperty(this.anzahlAnwesenheiten);
    }

    public DoubleProperty getAnwesenheitsAnteilProperty() {
        return new SimpleDoubleProperty(this.anwesenheitsAnteil);
    }

    public double getAnwesenheitsAnteil(){
        return this.anwesenheitsAnteil;
    }

    public void setRangYTD(int rangYTD){
        this.rangYTD = rangYTD;
    }

}
