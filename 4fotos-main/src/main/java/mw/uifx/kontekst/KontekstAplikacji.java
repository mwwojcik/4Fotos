package mw.uifx.kontekst;

import javafx.stage.Stage;
import mw.wspolne.model.TrybAplikacjiEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Future;

/**
 * Created by mw on 16.08.16.
 */
@Component
public class KontekstAplikacji {



    private Stage mainStage;

    private Deque<Stage> stagesOkienDialogowych=new LinkedList<>();

    private TrybAplikacjiEnum trybAplikacji=TrybAplikacjiEnum.OBSLUGA_ZDJEC;

    private Future<Boolean> statusOstatniegoZadania;




    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Stage getStageOknaDialogowego() {
        return stagesOkienDialogowych.pollLast();
    }

    public void setStageOknaDialogowego(Stage stageOknaDialogowego) {
        stagesOkienDialogowych.addLast(stageOknaDialogowego);
    }

    public TrybAplikacjiEnum getTrybAplikacji() {
        return trybAplikacji;
    }

    public void setTrybAplikacji(TrybAplikacjiEnum trybAplikacji) {
        this.trybAplikacji = trybAplikacji;
    }

    public void inicjalizujZadanie(Future<Boolean>aStatus){
        statusOstatniegoZadania=aStatus;
    }

    public boolean czyIstniejeAktywneZadanie(){
        return (statusOstatniegoZadania!=null&&!statusOstatniegoZadania.isDone());
    }
}
