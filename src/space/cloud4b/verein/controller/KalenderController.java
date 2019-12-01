package space.cloud4b.verein.controller;

import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.services.Subject;

import java.util.ArrayList;

public class KalenderController implements Subject  {

    int anzahlTermine;
    private ArrayList<Observer> observerList;

    public KalenderController() {
        System.out.println("KalenderController erzeugt");
        observerList = new ArrayList<>();
        startTimerActor();
    }

    public void updateAnzahlTermine(int anzahlTermine) {
        this.anzahlTermine = anzahlTermine;
        System.out.println("Anzahl Termine geändert auf " + anzahlTermine);
        Notify();
    }

    public int getAnzahlTermine() {
        return anzahlTermine;
    }

    private void startTimerActor() {
        Runnable checkMysqlRunner = () -> {
            int zaehler = 0;
            while (true) {
                if(DatabaseReader.readAnzahlTermine() != anzahlTermine) {
                    updateAnzahlTermine(DatabaseReader.readAnzahlTermine());
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                };
            }
        };
        Thread thread = new Thread(checkMysqlRunner);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void Attach(Observer o) {
        observerList.add(o);
    }

    @Override
    public void Dettach(Observer o) {

    }

    @Override
    public void Notify() {
        for(int i = 0; i <observerList.size(); i++)
        {
            observerList.get(i).update(this);
        }

    }
}
