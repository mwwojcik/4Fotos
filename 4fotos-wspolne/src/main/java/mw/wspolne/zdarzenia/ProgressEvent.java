package mw.wspolne.zdarzenia;

import mw.wspolne.zdarzenia.bazowe.ZdarzenieBazowe;

/**
 * Created by mw on 19.08.16.
 */
public class ProgressEvent extends ZdarzenieBazowe {
    private int maxStep;
    private int getActStep;
    private String komunikat;
    private String przetwarzanyElement;

    public ProgressEvent(Object source) {
        super(source);
    }

    public ProgressEvent(Object source, int maxStep, int getActStep, String komunikat, String przetwarzanyElement) {
        super(source);
        this.maxStep = maxStep;
        this.getActStep = getActStep;
        this.komunikat = komunikat;
        this.przetwarzanyElement = przetwarzanyElement;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public int getGetActStep() {
        return getActStep;
    }

    public String getKomunikat() {
        return komunikat;
    }

    public String getPrzetwarzanyElement() {
        return przetwarzanyElement;
    }
}
