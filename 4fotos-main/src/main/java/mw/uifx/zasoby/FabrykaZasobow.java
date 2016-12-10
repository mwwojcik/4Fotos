package mw.uifx.zasoby;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mw.uifx.kontekst.KontekstAplikacji;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by mw on 16.08.16.
 */
@Component
public class FabrykaZasobow {

    private AnnotationConfigApplicationContext kontekstSpringowy;

    public void otworzOknoDialogowe(LokalizacjeFXMLEnum aZasob, Stage aMainStage, KontekstAplikacji aKontekst) {
        Stage stage = new Stage();
        stage.setScene(new Scene(podajObiektParentDlaZasobu(aZasob)));
        dodajStyleCSS(stage.getScene());
        stage.setTitle(aZasob.getTytul());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(aMainStage.getScene().getWindow());
        aKontekst.setStageOknaDialogowego(stage);
        stage.show();


    }

    public void zamknijOknoDialogowe(Stage aStage) {
        aStage.close();
    }


    public void dodajStyleCSS(Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/mw/uifx/css/4fotos.css").toExternalForm());
    }

    public Parent podajObiektParentDlaZasobu(LokalizacjeFXMLEnum aZasob) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(aZasob.getUrl()), null, null,
                    new Callback<Class<?>, Object>() {
                        @Override
                        public Object call(Class<?> param) {
                            // OK but doesn't work when multiple instances controller of same type
                            return kontekstSpringowy.getBean(param);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return root;
    }

    public AnnotationConfigApplicationContext getKontekstSpringowy() {
        return kontekstSpringowy;
    }

    public void setKontekstSpringowy(AnnotationConfigApplicationContext kontekstSpringowy) {
        this.kontekstSpringowy = kontekstSpringowy;
    }
}
