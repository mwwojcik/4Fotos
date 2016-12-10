package mw.uslugi.bazowe;

import mw.uslugi.stan.ZarzadcaStanu;
import mw.wspolne.zdarzenia.publikacja.PublikujacyZdarzeniaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
@Component
public class UslugaBazowa {
    @Autowired
    protected ZarzadcaStanu zarzadcaStanu;

    @Autowired
    protected PublikujacyZdarzeniaBean publikujacy;


    protected ZarzadcaStanu getZarzadcaStanu() {
        return zarzadcaStanu;
    }

    protected PublikujacyZdarzeniaBean getPublikujacy() {
        return publikujacy;
    }
}
