package mw.uifx.wspolne;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
public interface KontrolerBazowy <StageType extends Stage> {

    StageType getStage();

    void show();

    void close();
}