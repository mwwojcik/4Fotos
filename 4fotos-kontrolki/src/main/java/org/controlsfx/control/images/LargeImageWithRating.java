package org.controlsfx.control.images;

import impl.org.controlsfx.behavior.ClickItemBehavior;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mw.wspolne.model.Obrazek;
import org.controlsfx.control.Rating;
import org.controlsfx.control.SmallRating;
import org.controlsfx.control.button.FontAwesomeButton;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mw on 24.08.16.
 */
public class LargeImageWithRating extends VBox {
    private int DEFAULT_THUMBNAIL_WIDTH = 600;

    private ClickItemBehavior<Obrazek> onClickCallback;
    private ImageView imageView;
    private Rating rating;
    private Label name;
    private Obrazek obrazek;
    private Button przyciskUsun;

    public LargeImageWithRating(Obrazek aObrazek, ClickItemBehavior aOnClickCallback) {
        super();

        obrazek = aObrazek;


        getStyleClass().add("large-image");

        name = new Label(aObrazek.getSciezka().getFileName().toString());
        imageView = createImageView(aObrazek.getSciezka(), aOnClickCallback);

        rating = new SmallRating();
        rating.setRating(obrazek.getOcena());
        rating.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                obrazek.setOcena(rating.getRating());
            }
        });


        przyciskUsun = new FontAwesomeButton(24, FontAwesome.Glyph.TRASH);
        przyciskUsun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rating.setRating(0);
                obrazek.setOcena(0);
            }
        });

        this.setSpacing(20);
        this.getChildren().add(imageView);


        this.getChildren().add(rating);
        this.getChildren().add(przyciskUsun);


        this.getChildren().add(name);
    }


    private ImageView createImageView(final Path imageFile, ClickItemBehavior aOnClickCallback) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        onClickCallback = aOnClickCallback;

        ImageView imageView = null;
        try {
            final Image image = new Image(Files.newInputStream(imageFile));
            imageView = new ImageView(image);
            if (image.getHeight() > image.getWidth()) {
                imageView.setFitHeight(DEFAULT_THUMBNAIL_WIDTH);
            } else {
                imageView.setFitWidth(DEFAULT_THUMBNAIL_WIDTH);
            }

            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 2) {
                            onClickCallback.clickItem(obrazek);
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            new IllegalArgumentException(e);
        }
        return imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Rating getRating() {
        return rating;
    }
}
