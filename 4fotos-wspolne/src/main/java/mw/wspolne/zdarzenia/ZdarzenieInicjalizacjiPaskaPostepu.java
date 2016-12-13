package mw.wspolne.zdarzenia;

/**
 * Created by mw on 13.12.16.
 */
public class ZdarzenieInicjalizacjiPaskaPostepu extends ZdarzenieInkrementacjiPaskaPostepu {
    public ZdarzenieInicjalizacjiPaskaPostepu(Object source, long maxStep, int getActStep, String komunikat, String przetwarzanyElement) {
        super(source, maxStep, getActStep, komunikat, przetwarzanyElement);
    }
}
