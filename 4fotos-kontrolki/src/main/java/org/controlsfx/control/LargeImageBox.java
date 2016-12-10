package org.controlsfx.control;

import impl.org.controlsfx.behavior.ClickItemBehavior;
import javafx.scene.layout.VBox;
import mw.wspolne.model.Obrazek;
import org.controlsfx.control.images.LargeImageWithRating;

/**
 * Created by mw on 24.08.16.
 */
public class LargeImageBox extends VBox {

    private LargeImageWithRating imageWithRating;

    public LargeImageBox(Obrazek aObrazek,ClickItemBehavior aClickListener) {
        this.imageWithRating = new LargeImageWithRating(aObrazek,aClickListener);

        this.getStylesheets().add(this.getClass().getResource("/org/controlsfx/control/large-image-with-rating.css").toExternalForm());
        getStyleClass().add("large-image-box");

        this.setPrefWidth(800);

        this.getChildren().add(imageWithRating);
    }
}
