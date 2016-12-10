package mw.uifx.kontrolery.fotolab;


import impl.org.controlsfx.behavior.ClickItemBehavior;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mw.uifx.parametry.ElementOdswiezanyParam;
import mw.uifx.wspolne.IElementOdswiezany;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import mw.wspolne.model.KryteriaFiltrowania;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.Rating;
import org.controlsfx.control.images.SmallGalleryImageWithRating;
import org.controlsfx.control.button.FontAwesomeButton;
import org.controlsfx.control.cell.ImageWithRatingGridCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.util.Callback;
import mw.uifx.parametry.ZasobyGlowneParam;
import mw.uifx.wspolne.IWyborKatalogu;
import mw.uifx.wspolne.KontrolerBazowyImpl;

import mw.wspolne.model.Galeria;
import mw.wspolne.model.Obrazek;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller
@Component
public class SegregacjaZdjecKontroler extends KontrolerBazowyImpl<ZasobyGlowneParam> implements ClickItemBehavior<Obrazek>,IElementOdswiezany {

    @FXML
    private HBox przyciskiKontener;

    @FXML
    private GridView<SmallGalleryImageWithRating> colorGrid;

    @FXML
    private Button przyciskUsun;

    @FXML
    private HBox kontenerKryteriow;

    @FXML
    private RadioButton kryteriaDokladnieRowneRadio;

    @FXML
    private RadioButton kryteriaMniejszeRowneRadio;

    @FXML
    private RadioButton kryteriaWiekszeRowneRadio;

    @FXML
    private Rating kryteriaRating;

    private ToggleGroup kryteriaWyszukiwaniaGrupa;

    private IWyborKatalogu obslugaDrzewka;

    private KryteriaFiltrowania kryteriaFiltrowania=new KryteriaFiltrowania();

    private Galeria galeria;

    private ObservableList<SmallGalleryImageWithRating> kolekcjaMiniatur=FXCollections.<SmallGalleryImageWithRating>observableArrayList();;

    @Autowired
    private PodgladZdjeciaKontroler podgladZdjeciaKontroler;

    public GridView<SmallGalleryImageWithRating> getColorGrid() {
        return colorGrid;
    }

    /***********************************
     ***********************************
     * Inicjalizacja
     ***********************************
     * *********************************
     */
    public void przygotujGalerie() {
        if (!czyMiniaturyPrzygotowane()) {
            uruchomZadanieAsynchroniczne(() -> wygenerujGalerie());
        }
    }


    public boolean czyMiniaturyPrzygotowane() {
        return getGaleriaUsluga().czyMiniaturyPrzygotowane(obslugaDrzewka.podajWybranyWezel().get());
    }

    private Boolean wygenerujGalerie() {
        getGaleriaUsluga().wygenerujMiniatury(obslugaDrzewka.podajWybranyWezel().get());
        return true;
    }


    @FXML
    public void initialize() {

        podgladZdjeciaKontroler.ustawParametry(new ElementOdswiezanyParam(this));
        przyciskiKontener.setSpacing(5);

        przyciskUsun =new FontAwesomeButton(14, FontAwesome.Glyph.TRASH);
        kontenerKryteriow.getChildren().add(przyciskUsun);

        kryteriaMniejszeRowneRadio.setUserData(KryteriaFiltrowania.OperatorFiltrowaniaEnum.MNIEJSZE_ROWNE);
        kryteriaDokladnieRowneRadio.setUserData(KryteriaFiltrowania.OperatorFiltrowaniaEnum.ROWNE);
        kryteriaWiekszeRowneRadio.setUserData(KryteriaFiltrowania.OperatorFiltrowaniaEnum.WIEKSZE_ROWNE);


        kryteriaWyszukiwaniaGrupa=new ToggleGroup();
        kryteriaWyszukiwaniaGrupa.getToggles().add(kryteriaDokladnieRowneRadio);
        kryteriaWyszukiwaniaGrupa.getToggles().add(kryteriaMniejszeRowneRadio);
        kryteriaWyszukiwaniaGrupa.getToggles().add(kryteriaWiekszeRowneRadio);

        kryteriaWyszukiwaniaGrupa.selectToggle(kryteriaMniejszeRowneRadio);

        kryteriaWyszukiwaniaGrupa.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (kryteriaWyszukiwaniaGrupa.getSelectedToggle() != null) {
                    kryteriaFiltrowania.setOperatorFiltrowaniaEnum((KryteriaFiltrowania.OperatorFiltrowaniaEnum)kryteriaWyszukiwaniaGrupa.getSelectedToggle().getUserData());
                    System.out.println(kryteriaFiltrowania.getOperatorFiltrowaniaEnum());
                    przeladujWidok();
                }
            }
        });

        kryteriaRating.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //todo niezgodnosc typow
                kryteriaFiltrowania.setOcena((int)kryteriaRating.getRating());
                przeladujWidok();
            }
        });

        przyciskUsun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kryteriaRating.setRating(0);
                kryteriaFiltrowania.setOcena(0);
                przeladujWidok();
            }
        });


        inicjalizujWidok();
    }




    private void inicjalizujWidok() {
        galeria = getGaleriaUsluga().podajMiniatury(obslugaDrzewka.podajWybranyWezel().get());

        if (galeria == null || galeria.getObrazki() == null) {
            return;
        }

        colorGrid.setCellFactory(new Callback<GridView<SmallGalleryImageWithRating>, GridCell<SmallGalleryImageWithRating>>() {
            @Override
            public GridCell<SmallGalleryImageWithRating> call(GridView<SmallGalleryImageWithRating> arg0) {
                return new ImageWithRatingGridCell();
            }
        });


        kryteriaRating.setRating(kryteriaFiltrowania.getOcena());

       przeladujWidok();
    }

    private List<SmallGalleryImageWithRating> konwertujObrazkiNaGalerie(List<Obrazek> aObrazki){
        return aObrazki.stream().map(i->new SmallGalleryImageWithRating(i,this)).collect(Collectors.toList());
    }

    /***********************************
     ***********************************
     * Przyciski
     ***********************************
     * *********************************
     */
    @FXML
    public void onZamknijAkcjaKlik() {
        zamknijOknoDialogowe();
    }

    public void onZapiszOcenyAkcjaKlik(){
        getGaleriaUsluga().zapiszOceny(galeria);
        wyswietlOknoDialogoweOstrzezenie("Oceny zostały zapisane.");
    }

    public void onPrzeniesKlik(){
        getGaleriaUsluga().przenosWybraneDoUsuniecia(galeria);
    }


    /***********************************
     ***********************************
     * Start okna szczegolow
     ***********************************
     * *********************************
     */

    @Override
    public void clickItem(Obrazek item) {
        System.out.println("Kliknieto=>"+item);

        podgladZdjeciaKontroler.przygotujDoWyswietlenia(item);
        otworzOknoDialogowe(LokalizacjeFXMLEnum.PODGLAD_DJECIA_OKNO_MODALNE);
    }


    /***********************************
     ***********************************
     * Metody pozostale
     ***********************************
     * *********************************
     */
    private void przeladujWidok(){
        System.out.println("=====> czyszczenie widoku");
        kolekcjaMiniatur= FXCollections.<SmallGalleryImageWithRating>observableArrayList(konwertujObrazkiNaGalerie(galeria.filtrujWedlugKryteriow(kryteriaFiltrowania)));
        colorGrid.setItems(kolekcjaMiniatur);
    }


    @Override
    protected void ustawParametry(ZasobyGlowneParam param) {
        super.ustawParametry(param);

        if (param.getObslugaDrzewka().isPresent()) {
            obslugaDrzewka = param.getObslugaDrzewka().get();
        }
    }

    @Override
    public void przeladuj() {
        przeladujWidok();
    }
}
