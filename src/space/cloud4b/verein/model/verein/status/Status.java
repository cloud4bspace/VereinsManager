package space.cloud4b.verein.model.verein.status;

import space.cloud4b.verein.daten.mysql.service.DatenLieferant;

import java.util.ArrayList;
import java.util.HashMap;

public class Status {

    private int statusId;
    private String statusTextLang;
    private String statusTextKurz;
    private String statusSymbol;
    private HashMap<Integer, StatusElement> statusElemente;

    public Status(int statusId) {
        System.out.println("Status " + statusId + " wird erstellt");
        this.statusId = statusId;
        DatenLieferant.statusInfosSetzen(this);
        statusElemente = new HashMap<>(DatenLieferant.statusHashMapLaden(statusId));
    }

    public HashMap<Integer, StatusElement> getStatusElemente() {
        return statusElemente;
    }

    public ArrayList<StatusElement> getElementsAsArrayList() {
        return new ArrayList<StatusElement>(statusElemente.values());
    }
    public void setStatusTextLang(String statusTextLang){ this.statusTextLang = statusTextLang; }
    public void setStatusTextKurz(String statusTextKurz) {this.statusTextKurz = statusTextKurz; }
    public void setStatusSymbol(String statusSymbol) {this.statusSymbol = statusSymbol; }
    public int getStatusId(){ return statusId;}
}
