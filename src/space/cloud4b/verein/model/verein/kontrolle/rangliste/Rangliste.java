package space.cloud4b.verein.model.verein.kontrolle.rangliste;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import space.cloud4b.verein.daten.mysql.service.DatenLieferant;
import space.cloud4b.verein.model.verein.Verein;

import java.util.ArrayList;

public class Rangliste {

    private String bezeichnung;
    private ArrayList<Position> rangliste;
    private Verein verein;

    public Rangliste(String bezeichnung, Verein verein) {
        this.verein = verein;
        this.bezeichnung = bezeichnung;
        //this.rangliste = new ArrayList<>();
        System.out.println("neue Rangliste erstellt: " + this.bezeichnung);
        this.rangliste = DatenLieferant.fuelleRangliste(this.verein);
    }

    public ObservableList<Position> getRanglistenListe() {
        return FXCollections.observableArrayList(rangliste);
    }

}
