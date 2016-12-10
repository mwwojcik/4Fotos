package mw.uifx.kontrolery.fotolab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import mw.uifx.kontrolery.TrybDzialaniaAplikacjiKontroler;
import mw.uifx.parametry.ZasobyGlowneParam;
import mw.uifx.wspolne.IWyborKatalogu;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.model.io.ZbiorDyskowy;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
@Controller
public class AplikacjaGlownaKontroler extends KontrolerBazowyImpl implements IWyborKatalogu {

    @FXML
    private HBox przyciskiNarzedzioweWidok;

    @FXML
    private PrzyciskiNarzedzioweKontroler przyciskiNarzedzioweWidokController;

    @FXML
    private HBox trybDzialaniaAplikacjiWidok;

    @FXML
    private TrybDzialaniaAplikacjiKontroler trybDzialaniaAplikacjiWidokController;

    @FXML
    private TreeView<String>drzewoKatalogow;


    @FXML
    private TableView listaPlikowJPGWidok;
    @FXML
    private KontrolerTabeliZbiorowJPG listaPlikowJPGWidokController;
    @FXML
    private TableView listaPlikowRAWWidok;
    @FXML
    private KontrolerTabeliZbiorowRAW listaPlikowRAWWidokController;
    @FXML
    private TableView listaPlikowXCFWidok;
    @FXML
    private KontrolerTabeliZbiorowXCF listaPlikowXCFWidokController;

    @FXML
    private TableView podsumowanieListPlikowWidok;
    @FXML
    private KontrolerPodsumowaniaTabelZbiorow podsumowanieListPlikowWidokController;

    @FXML
    public void initialize() {
        inicjalizujDrzewoKatalogow(getGaleriaUsluga().podajListeKatalogowGalerii());
    }

    private void inicjalizujDrzewoKatalogow(List<String> aKatalogi){
        drzewoKatalogow.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        drzewoKatalogow.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                ObservableList<TreeItem<String>> wezly=drzewoKatalogow.getSelectionModel().getSelectedItems();
                List<String>pZaznaczone=wezly.stream().map(t->t.getValue()).collect(Collectors.toList());

                Map<TypyPlikowEnum,List<ZbiorDyskowy>> pMapaPlikow=getGaleriaUsluga().podajListePlikow(Arrays.asList(TypyPlikowEnum.JPG,TypyPlikowEnum.RAW,TypyPlikowEnum.XCF), pZaznaczone);

                listaPlikowJPGWidokController.aktualizuj(pMapaPlikow.get(TypyPlikowEnum.JPG));
                listaPlikowRAWWidokController.aktualizuj(pMapaPlikow.get(TypyPlikowEnum.RAW));
                listaPlikowXCFWidokController.aktualizuj(pMapaPlikow.get(TypyPlikowEnum.XCF));

                podsumowanieListPlikowWidokController.aktualizuj(getGaleriaUsluga().obliczRozmiarPlikow(pMapaPlikow));
            }

        });

        TreeItem<String> rootItem = new TreeItem<String> ("Galerie");
        rootItem.setExpanded(true);
        aKatalogi.stream().forEach(
                kat -> {
                    rootItem.getChildren().add(new TreeItem<>(kat));
                }
        );
        drzewoKatalogow.setRoot(rootItem);

        przyciskiNarzedzioweWidokController.ustawParametry(new ZasobyGlowneParam(Optional.of(this)));
    }

    @Override
    public Optional<String> podajWybranyWezel() {
        return (drzewoKatalogow.getSelectionModel().getSelectedItem() != null) ?
                Optional.of(drzewoKatalogow.getSelectionModel().getSelectedItem().getValue())
                :Optional.empty();
    }
}
