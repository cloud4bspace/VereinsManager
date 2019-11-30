package space.cloud4b.verein.model.verein.kalender;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kontrolle.Meldung;

import javax.swing.text.DateFormatter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class Termin {

    private int terminId;
    private LocalDate terminDatum;
    private LocalDateTime terminZeit;
    private LocalDateTime terminZeitBis;
    private String terminDateAsLocalString;
    private String terminText;
    private String zeitText;
    private ArrayList<Meldung> anmeldungen;
    private ArrayList<Meldung> abmeldungen;
    private ArrayList<Meldung> teilnahmen;
    private ArrayList<Meldung> abwesenheiten;

    public Termin(int terminId, LocalDate terminDatum, String terminText) {
        this.terminId = terminId;
        this.terminDatum = terminDatum;
        this.terminText = terminText;
        System.out.println("Termin wurde erzeugt");
    }


    public ObservableValue<String> getTextProperty() {
        return new SimpleStringProperty(this.terminText);
    }


    public ObjectProperty<LocalDate> getDatumProperty() {
        return new SimpleObjectProperty<LocalDate>(this.terminDatum);
    }

    public LocalDate getDatum(){
        return terminDatum;
    }

    public StringProperty getDateAsLocalStringMedium() {
        return new SimpleStringProperty(terminDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    public StringProperty getDateAsLocalStringLong() {
        return new SimpleStringProperty(terminDatum.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
    }

    public StringProperty getDateAsNiceString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E. dd.MM.yyyy");
        return new SimpleStringProperty(terminDatum.format(formatter));
        }

    public void setZeit(LocalDateTime terminZeit) {
        this.terminZeit = terminZeit;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.zeitText = terminZeit.format(formatter);
        if(this.terminZeitBis!=null) {
            this.zeitText += ":" + terminZeitBis.format(formatter);
        }

    }

    public void setZeitBis(LocalDateTime terminZeitBis) {
        this.terminZeitBis = terminZeitBis;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.zeitText = terminZeit.format(formatter) + "-" + terminZeitBis.format(formatter);

    }

    public StringProperty getZeitTextProperty() {
        return new SimpleStringProperty(zeitText);
    }
}
