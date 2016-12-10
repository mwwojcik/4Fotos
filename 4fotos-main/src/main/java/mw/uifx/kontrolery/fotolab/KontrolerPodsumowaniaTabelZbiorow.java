package mw.uifx.kontrolery.fotolab;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mw.wspolne.model.MiernikZajetosci;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Created by mw on 18.08.16.
 */
@Controller
public class KontrolerPodsumowaniaTabelZbiorow {

    @FXML
    private TableView<MiernikZajetosci> miernikiPlikowTabela;

    @FXML
    protected TableColumn<MiernikZajetosci,String> typ;

    @FXML
    protected TableColumn<MiernikZajetosci,String> rozmiar;

    @FXML
    protected TableColumn<MiernikZajetosci,String> liczbaPlikow;

    public void aktualizuj(List<MiernikZajetosci>aListaPlikow){
        miernikiPlikowTabela.getItems().clear();

        if(aListaPlikow==null){
            return;
        }

        miernikiPlikowTabela.getItems().addAll(FXCollections.observableArrayList(aListaPlikow));
    }

    @FXML
    public void initialize(){
        typ.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRodzaj()));
        rozmiar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRozmiarMB()+ "MB"));
        liczbaPlikow.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLiczbaPlikow())));
    }
}
