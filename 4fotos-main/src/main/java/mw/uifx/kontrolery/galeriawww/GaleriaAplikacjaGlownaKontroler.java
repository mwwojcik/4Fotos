package mw.uifx.kontrolery.galeriawww;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Pair;
import mw.uifx.kontrolery.TrybDzialaniaAplikacjiKontroler;
import mw.uifx.kontrolery.fotolab.*;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.uslugi.PublikacjaGaleriiWWWUsluga;
import mw.uslugi.ZarzadzanieGaleriaWWWUsluga;
import mw.wspolne.model.*;
import mw.wspolne.model.io.ZbiorDyskowy;
import org.controlsfx.dialog.LoginDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mw on 17.08.16.
 */
@Controller
public class GaleriaAplikacjaGlownaKontroler extends KontrolerBazowyImpl{

    private String LOGIN = "galeria.mariuszwojcik";
    private String HOST="wojcikm.futuro.info.pl";
    private String HOSTURI="/galeria/albumy";

    @FXML
    private HBox trybDzialaniaAplikacjiWidok;

    @FXML
    private TrybDzialaniaAplikacjiKontroler trybDzialaniaAplikacjiWidokController;

    @FXML
    private TreeView<Zasob> drzewoKatalogow;

    @FXML
    private TableView podsumowanieListPlikowWidok;
    @FXML
    private KontrolerPodsumowaniaTabelZbiorow podsumowanieListPlikowWidokController;


    @FXML
    private TableView listaPlikowJPGWidok;
    @FXML
    private KontrolerTabeliZbiorowJPG listaPlikowJPGWidokController;

    @Autowired
    private ZarzadzanieGaleriaWWWUsluga zarzadzanieGaleriaWWWUsluga;

    @Autowired
    private PublikacjaGaleriiWWWUsluga publikacjaGaleriiWWWUsluga;

    GalerieWWW galerie;

    @FXML
    public void initialize() {
        System.out.println("stworzono" + this.getClass().getSimpleName());

        TreeItem<Zasob> rootItem = new TreeItem<Zasob> ();
        rootItem.setExpanded(true);

        galerie=zarzadzanieGaleriaWWWUsluga.podajGalerieWWW();

        galerie.getListaKategorii().stream().forEach(
                kat -> {
                    TreeItem pWezel=new TreeItem<>(kat);
                    if(kat instanceof KategoriaWWW){
                        KategoriaWWW pKategoria=(KategoriaWWW)kat;
                        pKategoria.getListaGalerii().forEach(g-> {
                                    pWezel.getChildren().add(new TreeItem<>(g));
                                }
                        );
                    }
                    rootItem.getChildren().add(pWezel);
                }
        );
        drzewoKatalogow.setRoot(rootItem);

        drzewoKatalogow.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                TreeItem<Zasob> wezel=drzewoKatalogow.getSelectionModel().getSelectedItem();

                if(wezel==null){
                    return;
                }

                List<ZbiorDyskowy>pPliki=zarzadzanieGaleriaWWWUsluga.przekonwertujDoZbiorowDyskowych(wezel.getValue());

                if(pPliki==null){
                    return;
                }

                listaPlikowJPGWidokController.aktualizuj(pPliki);

                Map<TypyPlikowEnum,List<ZbiorDyskowy>> pMapaPlikow=new HashMap<TypyPlikowEnum, List<ZbiorDyskowy>>();
                pMapaPlikow.put(TypyPlikowEnum.JPG,pPliki);

                podsumowanieListPlikowWidokController.aktualizuj(getGaleriaUsluga().obliczRozmiarPlikow(pMapaPlikow));
            }

        });

    }

    public void generujGalerieWWWAkcjaKlik(){
        uruchomZadanieAsynchroniczne(()->wygenerujGalerie());
    }

    private Boolean wygenerujGalerie() {
        zarzadzanieGaleriaWWWUsluga.generujGalerieWWW();
        return true;
    }

    public void publikujGalerieWWWAkcjaKlik(){
        LoginDialog pDialog=new LoginDialog(new Pair<>(LOGIN, ""), new Callback<Pair<String, String>, Void>() {
            @Override
            public Void call(Pair<String, String> param) {
                publikacjaGaleriiWWWUsluga.publikujGalerie(galerie,HOST,HOSTURI,param.getKey(),param.getValue(),TypPublikacjiEnum.WSZYSTKO);
                return null;
            }
        });
        pDialog.setHeaderText("Host:"+HOST+"\n"+"URI:"+HOSTURI);
        pDialog.showAndWait();
    }



    public void zaczytajPlikAkcjaKlik(){
        zarzadzanieGaleriaWWWUsluga.aktualizujPlikStruktury();
    }

}
