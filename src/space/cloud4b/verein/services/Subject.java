package space.cloud4b.verein.services;

public interface Subject {
    void Attach(Observer o);
    void Dettach(Observer o);
    void Notify();
}
