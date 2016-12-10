package mw.uifx.kontrolery.fotolab;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller
public class ImportZdjecKontroler extends KontrolerBazowyImpl {

    @FXML
    private HBox przyciskiKontener;

    @FXML
    public void initialize(){
        przyciskiKontener.setSpacing(5);
    }

    public void importujZdjecia(){

        Optional<String>result=wyswietlDialogTypuInput("Import zdjęć","Prefiks katalogu");

        if(result.isPresent()){
            uruchomZadanieAsynchroniczne(()->uruchomImportZdjec(result.get()));
        }else{
            return;
        }

    }

    private boolean uruchomImportZdjec(String aPrefix){
        getGaleriaUsluga().importujPliki(aPrefix);
        return true;
    }



}
