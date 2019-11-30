package space.cloud4b.verein.model.verein.kontrolle;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDateTime;

public class Meldung {
    private int meldungId;
    private Mitglied mitglied;
    private Termin termin;
    private LocalDateTime zeitStempel;
    private String Bemerkung;
    private StatusElement meldungsTyp;
}
