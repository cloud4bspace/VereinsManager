package space.cloud4b.verein.model.verein.kalender;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.StatusElement;

public class Teilnehmer {
    private Mitglied mitglied;
    private StatusElement anmeldeStatus;
    private String anmeldungText;
    private StatusElement teilnahmeStatus;
    private String teilnahmeText;

    public Teilnehmer(Mitglied mitglied){
        this.mitglied = mitglied;
    }

    public Mitglied getMitglied(){
        return mitglied;
    }

    public void setAnmeldeStatus(StatusElement anmeldeStatus){
        this.anmeldeStatus = anmeldeStatus;
    }
    public ObjectProperty<StatusElement> getAnmeldungProperty() { return new SimpleObjectProperty<StatusElement>(this.anmeldeStatus); }

    public void setTeilnahmeStatus(StatusElement teilnahmeStatus){
        this.teilnahmeStatus = teilnahmeStatus;
    }

    public ObjectProperty<StatusElement> getTeilnahmeProperty() { return new SimpleObjectProperty<StatusElement>(this.teilnahmeStatus); }
    public int getAnmeldungWert() {
        if(this.anmeldeStatus != null) {
            return anmeldeStatus.getStatusElementKey();
        } else {
            return 0;
        }
    }
}
