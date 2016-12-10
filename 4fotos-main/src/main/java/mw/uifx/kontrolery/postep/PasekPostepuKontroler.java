package mw.uifx.kontrolery.postep;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import mw.uifx.wspolne.KontrolerBazowyImpl;
import mw.wspolne.zdarzenia.ProgressEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * Created by mw on 19.08.16.
 */
@Component
@Controller
public class PasekPostepuKontroler extends KontrolerBazowyImpl {

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void onZamknijAkcjaKlik() {
        zamknijOknoDialogowe();
    }

    @FXML
    private Label akcja;

    @FXML
    private Label krok;

    @FXML
    private Label log;


    private int max=0;


    private void initProgressBar(int aMax){
        max=aMax;
    }

    private void finishTask(ProgressEvent event){
        Platform.runLater(() -> super.wyswietlOknoDialogoweInformacja("Zadanie=>" + event.getKomunikat() + " zostało zakończone"));
    }

    @EventListener
    public void handleProgressEvent(ProgressEvent event) {
        System.out.println("Odebralem zdarzenie=>"+event.getGetActStep());
        if(event.getGetActStep()==0){
            Platform.runLater(() -> initProgressBar(event.getMaxStep()));
        }

        Platform.runLater(() -> akcja.textProperty().setValue(event.getKomunikat()));

        Platform.runLater(() -> progressBar.setProgress(1.0 * event.getGetActStep() / max));
        Platform.runLater(() -> krok.textProperty().setValue(event.getGetActStep() + "/" + event.getMaxStep()));

        Platform.runLater(() -> log.textProperty().setValue(event.getPrzetwarzanyElement()));

        if(event.getGetActStep()==event.getMaxStep()){
            finishTask(event);
        }
    }
}
