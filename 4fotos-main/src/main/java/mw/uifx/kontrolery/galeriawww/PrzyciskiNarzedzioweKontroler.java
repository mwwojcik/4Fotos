package mw.uifx.kontrolery.galeriawww;

import javafx.fxml.FXML;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import mw.uslugi.GaleriaFasadaUslug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller(value = "galerieWWWPrzyciski")
public class PrzyciskiNarzedzioweKontroler extends KontrolerBazowyImpl{


    @FXML
    public void initialize() {
        System.out.println("stworzono" + this.getClass().getSimpleName());
    }

    @FXML
    public void generujGalerieWWWAkcjaKlik(){
        System.out.println("GenerujWWW");
    }

    @FXML
    public void publikujGalerieWWWAkcjaKlik(){
        System.out.println("PublikujWWW");
    }



    @FXML
    public void zamknijAplikacjeAkcjaKlik(){
        zamknijAplikacje();
    }

}
