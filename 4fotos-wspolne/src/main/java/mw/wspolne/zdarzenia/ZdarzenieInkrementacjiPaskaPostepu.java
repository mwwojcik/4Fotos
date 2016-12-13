package mw.wspolne.zdarzenia;

import lombok.Getter;
import lombok.Setter;
import mw.wspolne.zdarzenia.bazowe.ZdarzenieBazowe;

/**
 * Created by mw on 19.08.16.
 */
@Getter
@Setter
public class ZdarzenieInkrementacjiPaskaPostepu extends ZdarzenieBazowe {
    private long maxStep;
    private int getActStep;
    private String komunikat;
    private String przetwarzanyElement;


    public ZdarzenieInkrementacjiPaskaPostepu(Object source, long maxStep, int getActStep, String komunikat, String przetwarzanyElement) {
        super(source);
        this.maxStep = maxStep;
        this.getActStep = getActStep;
        this.komunikat = komunikat;
        this.przetwarzanyElement = przetwarzanyElement;
    }

}
