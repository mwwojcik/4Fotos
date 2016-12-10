package mw.uifx.kontrolery.fotolab;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import mw.uifx.parametry.ZasobyGlowneParam;
import mw.uifx.wspolne.IWyborKatalogu;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import mw.uslugi.GaleriaFasadaUslug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller(value = "zasobyGlownePrzyciski")
public class PrzyciskiNarzedzioweKontroler extends KontrolerBazowyImpl<ZasobyGlowneParam> {

    private String KOMUNIKAT = "W tej chwili trwa uruchomione wcześniej zadanie.";

    @FXML
    private HBox przyciskiKontener;

    @Autowired
    private GaleriaFasadaUslug galeriaUsluga;

    @Autowired
    private SegregacjaZdjecKontroler segregacjaZdjecKontroler;

    @Autowired
    private ImportZdjecKontroler importZdjecKontroler;

    private CzyszczenieUsunietychKontroler czyszczenieUsunietychKontroler;

    @FXML
    public void initialize() {
        przyciskiKontener.setSpacing(3);

    }

    @FXML
    public void importujZdjeciaAkcjaKlik() {

        if (getKontekstAplikacji().czyIstniejeAktywneZadanie()) {
            wyswietlOknoDialogoweOstrzezenie(KOMUNIKAT);
            return;
        }
        importZdjecKontroler.importujZdjecia();

    }

    @FXML
    public void segregujZdjeciaAkcjaKlik() {

        if(!obslugaDrzewka.podajWybranyWezel().isPresent()){
            wyswietlOknoDialogoweOstrzezenie("Nie wybrano galerii.");
            return;
        }

        if (getKontekstAplikacji().czyIstniejeAktywneZadanie()) {
            wyswietlOknoDialogoweOstrzezenie(KOMUNIKAT);
            return;
        }

        if (!segregacjaZdjecKontroler.czyMiniaturyPrzygotowane()) {
            segregacjaZdjecKontroler.przygotujGalerie();
        } else {
            otworzOknoDialogowe(LokalizacjeFXMLEnum.SEGREGACJA_ZDJEC_OKNO_MODALNE);
        }


    }




    private IWyborKatalogu obslugaDrzewka;


    @Override
    protected void ustawParametry(ZasobyGlowneParam param) {
        super.ustawParametry(param);

        if (param.getObslugaDrzewka().isPresent()) {
            obslugaDrzewka = param.getObslugaDrzewka().get();
        }

        segregacjaZdjecKontroler.ustawParametry(param);
       // czyszczenieUsunietychKontroler.ustawParametry(param);

    }
}
