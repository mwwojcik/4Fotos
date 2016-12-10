package mw.uifx.parametry;

import mw.uifx.parametry.bazowe.BazowyParam;
import mw.uifx.wspolne.IElementOdswiezany;

/**
 * Created by Mariusz.Wojcik on 2016-08-31.
 */
public class ElementOdswiezanyParam extends BazowyParam {
    private IElementOdswiezany elementOdswiezany;

    public ElementOdswiezanyParam(IElementOdswiezany rodzic) {
        this.elementOdswiezany = rodzic;
    }

    public IElementOdswiezany getElementOdswiezany() {
        return elementOdswiezany;
    }

    public void setElementOdswiezany(IElementOdswiezany elementOdswiezany) {
        this.elementOdswiezany = elementOdswiezany;
    }
}
