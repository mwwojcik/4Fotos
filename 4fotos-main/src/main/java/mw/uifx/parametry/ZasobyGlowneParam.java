package mw.uifx.parametry;

import mw.uifx.parametry.bazowe.BazowyParam;
import mw.uifx.wspolne.IWyborKatalogu;

import java.util.Optional;

/**
 * Created by mw on 18.08.16.
 */
public class ZasobyGlowneParam extends BazowyParam {

    private Optional<IWyborKatalogu> obslugaDrzewka;

    public ZasobyGlowneParam(Optional<IWyborKatalogu> obslugaDrzewka) {
        this.obslugaDrzewka = obslugaDrzewka;
    }

    public Optional<IWyborKatalogu> getObslugaDrzewka() {
        return obslugaDrzewka;
    }
}
