package mw.uifx.kontrolery.fotolab;

import impl.org.controlsfx.behavior.ClickItemBehavior;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import mw.uifx.parametry.ElementOdswiezanyParam;
import mw.uifx.wspolne.IElementOdswiezany;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.wspolne.model.Obrazek;
import org.controlsfx.control.LargeImageBox;
import org.springframework.stereotype.Controller;

/**
 * Created by mw on 24.08.16.
 */
@Controller
public class PodgladZdjeciaKontroler extends KontrolerBazowyImpl<ElementOdswiezanyParam> implements ClickItemBehavior<Obrazek>{

    private Obrazek obrazek;
    @FXML
    private HBox zdjecieKontener;

    private IElementOdswiezany rodzic;

    public void przygotujDoWyswietlenia(Obrazek aObrazek){
        obrazek=aObrazek;
    }

    @FXML
    public void initialize(){
        LargeImageBox pElement=new LargeImageBox(obrazek,this);
        zdjecieKontener.getChildren().add(pElement);
    }
    @FXML
    public  void onZamknijAkcjaKlik(){
        rodzic.przeladuj();
        zamknijOknoDialogowe();
    };

    @Override
    public void clickItem(Obrazek item) {

    }

    @Override
    protected void ustawParametry(ElementOdswiezanyParam param) {
        super.ustawParametry(param);
        rodzic=param.getElementOdswiezany();
    }
}
