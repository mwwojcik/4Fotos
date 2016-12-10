package org.controlsfx.control.button;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import org.controlsfx.glyphfont.FontAwesome;


/**
 * Created by Mariusz.Wojcik on 2016-08-25.
 */
public class FontAwesomeButton extends Button {
    public FontAwesomeButton(int size, FontAwesome.Glyph icon) {
        super();
        this.getStylesheets().add(this.getClass().getResource("/org/controlsfx/control/button/font-awesome.css").toExternalForm());
        this.setGraphic(createIconLabel(icon.getChar(), size));
    }

    private static javafx.scene.control.Label createIconLabel(char aChar, int iconSize) {
        Label pLabel = new Label();
        pLabel.setText(String.valueOf(aChar));
        pLabel.getStyleClass().add("icons");
        pLabel.setStyle("-fx-font-size: " + iconSize + "px;");
        return pLabel;
    }
}
