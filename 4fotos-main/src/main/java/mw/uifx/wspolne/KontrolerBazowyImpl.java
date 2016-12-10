package mw.uifx.wspolne;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import mw.uifx.kontekst.KontekstAplikacji;
import mw.uifx.parametry.bazowe.BazowyParam;
import mw.uifx.zasoby.FabrykaZasobow;
import mw.uslugi.GaleriaFasadaUslug;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller
public abstract class KontrolerBazowyImpl<P extends BazowyParam> {

    @Autowired
    private GaleriaFasadaUslug galeriaUsluga;

    @Autowired
    private KontekstAplikacji kontekstAplikacji;

    @Autowired
    private FabrykaZasobow fabrykaZasobow;

    @Autowired
    private ZarzadcaWlasnosciUzytkownika zarzadcaWlasnosci;

    @Autowired
    protected ExecutorService exec;


    public GaleriaFasadaUslug getGaleriaUsluga() {
        return galeriaUsluga;
    }

    public KontekstAplikacji getKontekstAplikacji() {
        return kontekstAplikacji;
    }


    protected void ustawParametry(P param){}

    protected void uruchomZadanieAsynchroniczne(Callable<Boolean> aCallable){
        if (getKontekstAplikacji().czyIstniejeAktywneZadanie()){
            return;
        }
        kontekstAplikacji.inicjalizujZadanie(exec.submit(aCallable));
    }

    protected void otworzOknoDialogowe(LokalizacjeFXMLEnum aZasob){
        fabrykaZasobow.otworzOknoDialogowe(aZasob, kontekstAplikacji.getMainStage(), kontekstAplikacji);
    }

    protected void zamknijOknoDialogowe(){
        fabrykaZasobow.zamknijOknoDialogowe(getKontekstAplikacji().getStageOknaDialogowego());
    }

    protected void przeladujScene(Scene aScena,LokalizacjeFXMLEnum aNowyZasob){
        aScena.setRoot(fabrykaZasobow.podajObiektParentDlaZasobu(aNowyZasob));
    }


    protected Optional<String> wyswietlDialogTypuInput(String aTytulOkna, String aEtykietaPolaTekstowego){
        TextInputDialog dlg = new TextInputDialog("");
        dlg.setHeaderText("Potwierdzenie");
        dlg.setTitle(aTytulOkna);
        dlg.getDialogPane().setContentText(aEtykietaPolaTekstowego);
        return dlg.showAndWait();
    }

    protected void wyswietlOknoDialogoweOstrzezenie(String aTresc){
        Alert alert = new Alert(Alert.AlertType.WARNING, aTresc);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText("Uwaga!");
        alert.showAndWait();
    }

    protected void wyswietlOknoDialogoweInformacja(String aTresc){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, aTresc);
        alert.setTitle("Informacja");
        alert.setHeaderText("");
        alert.showAndWait();
    }

    protected void zamknijAplikacje(){
        fabrykaZasobow.zamknijOknoDialogowe(kontekstAplikacji.getMainStage());
    }

    @FXML
    public void zamknijAplikacjeAkcjaKlik() {
        zamknijAplikacje();
    }
}
