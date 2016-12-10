package mw.uslugi;

import mw.wspolne.model.Galeria;

/**
 * Created with IntelliJ IDEA.
 * User: mw
 * Date: 18.03.14
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public interface SegregacjaZdjecUsluga {
    boolean czyMiniaturyPrzygotowane(String aRootGaleriiWejsciowej);
    void wygenerujMiniatury(Galeria aRootGaleriiWejsciowej);
    void podajMiniatury(Galeria aRootGaleriiWejsciowej);
    void zapiszOceny(Galeria aGaleria);
    void przenosWybraneDoUsuniecia(Galeria aGaleria);
}
