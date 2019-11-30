package space.cloud4b.verein.model.verein.kalender;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import space.cloud4b.verein.daten.mysql.service.DatenLieferant;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Kalender {
    String kalenderName;
    ArrayList<Termin> terminListe;
    ArrayList<Termin> kommendeTermineListe = new ArrayList<>();
    ArrayList<Jubilaeum> jubilaeumsListe;

    public Kalender(String kalenderName) {
        this.kalenderName = kalenderName;
        System.out.println("Kalender " + kalenderName + " wurde erstellt");
        terminListe = DatenLieferant.termineLaden();
        jubilaeumsListe = DatenLieferant.jubilaenLaden();
        this.setKommendeTermine();
    }

    public ObservableList<Termin> getTerminListe() {
        return FXCollections.observableArrayList(terminListe);
    }

    public ArrayList<Termin> getTerminListeAsArrayList() {
        return terminListe;
    }

    public ObservableList<Termin> getNaechsteTerminListe() {
        return FXCollections.observableArrayList(kommendeTermineListe);
    }

    public ObservableList<Jubilaeum> getJubilaeumsListe() {
        Collections.sort(jubilaeumsListe, new Sortbydate());
        return FXCollections.observableArrayList(jubilaeumsListe);
    }

    public void setKommendeTermine() {
        for(Termin t : terminListe) {
            if(t.getDatum().isAfter(LocalDate.now().minusDays(1))){
                kommendeTermineListe.add(t);
            }
        }
    }

}
