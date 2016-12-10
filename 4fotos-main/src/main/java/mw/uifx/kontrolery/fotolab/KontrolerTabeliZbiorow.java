package mw.uifx.kontrolery.fotolab;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mw.wspolne.model.io.ZbiorDyskowy;
import org.springframework.stereotype.Controller;




/**
 * Created by mw on 18.08.16.
 */
@Controller
public class KontrolerTabeliZbiorow {

    @FXML
    protected TableView<ZbiorDyskowy> listaPlikowTabela;

    @FXML
    protected TableColumn<ZbiorDyskowy,String> nazwa;

    @FXML
    protected TableColumn<ZbiorDyskowy,String> katalog;

    @FXML
    protected TableColumn<ZbiorDyskowy,String> rozmiar;


    @FXML
    public void initialize(){
        nazwa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNazwa()));
        katalog.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNazwaKatalogu()));
        rozmiar.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRozmiarMB())+" MB"));
    }
}
