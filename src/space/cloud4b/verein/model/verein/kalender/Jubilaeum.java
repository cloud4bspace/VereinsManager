package space.cloud4b.verein.model.verein.kalender;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Observable;

public class Jubilaeum extends Termin {

    public Jubilaeum(int terminId, LocalDate terminDatum, String terminText) {
        super(terminId, terminDatum, terminText);
        System.out.println("neues Jubiläum angelegt: " + terminText + "/" + terminDatum);

    }



}
