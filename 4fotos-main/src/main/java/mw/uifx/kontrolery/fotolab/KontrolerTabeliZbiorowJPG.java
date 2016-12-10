package mw.uifx.kontrolery.fotolab;

import javafx.collections.FXCollections;
import mw.wspolne.model.io.ZbiorDyskowy;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by mw on 18.08.16.
 */
@Controller
public class KontrolerTabeliZbiorowJPG extends KontrolerTabeliZbiorow {

    public void aktualizuj(List<ZbiorDyskowy>aListaPlikow){
        listaPlikowTabela.getItems().clear();
        if(aListaPlikow==null){
            return;
        }
        listaPlikowTabela.getItems().addAll(FXCollections.observableArrayList(aListaPlikow));
    }

}
