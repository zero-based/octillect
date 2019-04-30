package octillect.models;

public interface IObservable<ObserverType> {
    void addObserver(ObserverType observer);
    void removeObserver(ObserverType observer);
    void notifyObservers();
}
