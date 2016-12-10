package org.controlsfx.control.images;

import impl.org.controlsfx.behavior.ClickItemBehavior;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import mw.wspolne.model.Obrazek;
import org.controlsfx.control.Rating;
import org.controlsfx.control.SmallRating;
import org.controlsfx.control.button.FontAwesomeButton;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Mariusz.Wojcik on 2016-08-18.
 */
public class SmallGalleryImageWithRating extends VBox {
    private int DEFAULT_THUMBNAIL_WIDTH = 300;

    private ClickItemBehavior<Obrazek> onClickCallback;
    private ImageView imageView;
    private SmallRating rating;
    private Label name;
    private Obrazek obrazek;
    private Button przyciskUsun;

    public SmallGalleryImageWithRating(Obrazek aObrazek, ClickItemBehavior aOnClickCallback) {
        super();

        obrazek = aObrazek;
        getStyleClass().add("grid-cell-image");

        name = new Label(aObrazek.getMiniatura().getSciezka().getFileName().toString());
        imageView = createImageView(aObrazek.getMiniatura().getSciezka(), aOnClickCallback);
        rating = new SmallRating();
        rating.setRating(obrazek.getOcena());


        rating.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                obrazek.setOcena(rating.getRating());
            }
        });
        przyciskUsun = new FontAwesomeButton(12, FontAwesome.Glyph.TRASH);//new Button("", new Glyph("FontAwesome", "TRASH_ALT"));


        przyciskUsun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rating.setRating(0);
                obrazek.setOcena(0);
            }
        });

        this.setSpacing(5);
        this.getChildren().add(imageView);


        this.getChildren().add(rating);
        this.getChildren().add(przyciskUsun);

        this.getChildren().add(name);

    }

    private SmallGalleryImageWithRating getImageWithRating() {
        return this;
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
                       /*     try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(Files.newInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");

                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                            } catch (FileNotFoundException e) {
                                throw new IllegalArgumentException(e);
                            } catch (IOException e) {
                                throw new IllegalArgumentException(e);
                            }
*/
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
