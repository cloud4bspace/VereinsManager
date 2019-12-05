package space.cloud4b.verein.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;
import space.cloud4b.verein.view.dashboard.DashBoardController;
import space.cloud4b.verein.view.mainframe.MainFrameController;
import space.cloud4b.verein.view.mitglieder.MitgliedViewController;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AdressController implements Subject {

    private int anzahlMitglieder;
    private Timestamp timestamp = null;
    private DashBoardController dashBoardController;
    private MitgliedViewController mitgliedViewController;
    private ArrayList<Observer> observerList;

    public AdressController() {
        System.out.println("AdressController erzeugt");
        observerList = new ArrayList<>();
        startTimerActor();
    }
    public AdressController(MitgliedViewController mitgliedViewController) {
        System.out.println("AdressController erzeugt");
        this.mitgliedViewController = mitgliedViewController;
        observerList = new ArrayList<>();
        startTimerActor();
    }
    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }

    public void updateAnzahlMitglieder(int anzahlMitglieder) {
        this.anzahlMitglieder = anzahlMitglieder;
        System.out.println("Anzahl Mitglieder geändert auf " + anzahlMitglieder);
        Notify();
    }

    public void updateLetzeAenderung(Timestamp neuerZeitstempel){
        this.timestamp = neuerZeitstempel;
        System.out.println("Aenderungen bei den Mitgliedern mit neuem Zeitstempel (" + neuerZeitstempel + ") festgestellt");
        Notify();
    }

    public int getAnzahlMitglieder() {
        return anzahlMitglieder;
    }
    public ObservableList<Mitglied> getMitgliederListe() {
        return FXCollections.observableArrayList(DatabaseReader.getMitgliederAsArrayList());
    }
    public ObservableList<Jubilaeum> getJubilaeumsListe() {
        return FXCollections.observableArrayList(DatabaseReader.getJubilaeenAsArrayList());
    }

    private void startTimerActor() {
        Runnable blinkRunner = () -> {
            int zaehler = 0;
            while (true) {
                // hat sich die Anzahl der Einträge in der Tabelle Kontakt verändert
                if(DatabaseReader.readAnzahlMitglieder() != anzahlMitglieder) {
                    updateAnzahlMitglieder(DatabaseReader.readAnzahlMitglieder());
                    return;
                }
                // hat sich der Zeitstempel der letzten Äenderung verändert?
                if(this.timestamp == null) {
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    return;
                } else if (DatabaseReader.readLetzteAenderung().after(this.timestamp)){
                    updateLetzeAenderung(DatabaseReader.readLetzteAenderung());
                    return;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                };
            }
        };
        Thread thread = new Thread(blinkRunner);
        thread.setDaemon(true);
        thread.start();
    }
    @Override
    public void Attach(Observer o) {
        observerList.add(o);
    }

    @Override
    public void Dettach(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void Notify() {
        for(int i = 0; i <observerList.size(); i++)
        {
            observerList.get(i).update(this);
        }
    }
}