package space.cloud4b.verein.model.verein.adressbuch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import space.cloud4b.verein.daten.mysql.service.DatenLieferant;
import space.cloud4b.verein.model.verein.Verein;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;

import java.util.ArrayList;
import java.util.HashMap;

public class AdressBuch {
    private Verein verein;
    private String adressBuchName;
    private Status anredeStatus;
    private Status kategorieIStatus;
    private Status kategorieIIStatus;
    private ArrayList<Mitglied> mitgliederListe;

    public AdressBuch(String adressBuchName, Verein verein) {
        DatabaseOperation.checkMitgliederStatus();
        this.verein = verein;
        this.adressBuchName = adressBuchName;
        // TODO Debug System.out
        System.out.println("Adressbuch erstellt (" + adressBuchName + ")");
        this.mitgliederListe = new ArrayList<>();
        this.anredeStatus = new Status(1);
        this.kategorieIStatus = new Status(2);
        this.kategorieIIStatus = new Status( 4);
        mitgliederListe = DatenLieferant.mitgliederLaden(this);
    }

    public ObservableList<Mitglied> getMitgliederListe() {
        return FXCollections.observableArrayList(mitgliederListe);
    }
    public ArrayList<Mitglied> getMitgliederListeAsArrayList() { return mitgliederListe; };

    public Status getAnredeStatus() {
        return anredeStatus;
    }
    public Status getKategorieIStatus() { return kategorieIStatus; }
    public Status getKategorieIIStatus() { return kategorieIIStatus; }




}
