package mw.uifx.kontrolery;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import mw.wspolne.model.TrybAplikacjiEnum;
import org.springframework.stereotype.Controller;

/**
 * Created by mw on 17.08.16.
 */
@Controller
public class TrybDzialaniaAplikacjiKontroler extends KontrolerBazowyImpl {

    @FXML
    private ComboBox<TrybAplikacjiEnum> trybAplikacjiCombo;

    @FXML
    public void initialize() {
        System.out.println("stworzono" + this.getClass().getSimpleName());
        trybAplikacjiCombo.getItems().addAll(TrybAplikacjiEnum.values());

        trybAplikacjiCombo.getSelectionModel().select(getKontekstAplikacji().getTrybAplikacji());

        trybAplikacjiCombo.valueProperty().addListener(new ChangeListener<TrybAplikacjiEnum>() {
            @Override public void changed(ObservableValue ov, TrybAplikacjiEnum aWartoscStara, TrybAplikacjiEnum aWartoscNowa) {
                if(aWartoscNowa!=null) {
                    getKontekstAplikacji().setTrybAplikacji(aWartoscNowa);
                    trybAplikacjiCombo.getSelectionModel().select(aWartoscNowa);
                    ustawWlasciwyTrybAplikacji(aWartoscNowa);
                }
            }
        });
    }

    private void ustawWlasciwyTrybAplikacji(TrybAplikacjiEnum aTrybDzialania){
        if(aTrybDzialania!=null&&aTrybDzialania==TrybAplikacjiEnum.OBSLUGA_WWW) {
            przeladujScene(getKontekstAplikacji().getMainStage().getScene(), LokalizacjeFXMLEnum.GALERIA_WWW_WIDOK);
        }else{
            przeladujScene(getKontekstAplikacji().getMainStage().getScene(), LokalizacjeFXMLEnum.ZASOBY_GLOWNE_WIDOK);
        }
    }
}
