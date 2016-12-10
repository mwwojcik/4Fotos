package mw.uslugi;

import mw.wspolne.model.GalerieWWW;
import mw.wspolne.model.TypPublikacjiEnum;

/**
 * Created by mw on 01.02.16.
 */
public interface PublikacjaGaleriiWWWUsluga {
    void publikujGalerie(GalerieWWW aGalerie,String aHost,String aUri,String aLogin,String aHaslo,TypPublikacjiEnum typPublikacji);
}
