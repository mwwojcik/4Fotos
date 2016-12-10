package mw.uslugi;

import mw.wspolne.model.GalerieWWW;
import mw.wspolne.model.Zasob;
import mw.wspolne.model.io.Zbior;
import mw.wspolne.model.io.ZbiorDyskowy;

import java.util.List;

/**
 * Created by mw on 14.01.16.
 */
public interface ZarzadzanieGaleriaWWWUsluga {
    public void aktualizujPlikStruktury();
    public void generujGalerieWWW();
    public GalerieWWW podajGalerieWWW();
    public List<ZbiorDyskowy> przekonwertujDoZbiorowDyskowych(Zasob aZasob);
}
