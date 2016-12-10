package org.controlsfx.control.cell;

import javafx.geometry.Pos;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.images.SmallGalleryImageWithRating;

/**
 * Created by Mariusz.Wojcik on 2016-08-18.
 */
public class ImageWithRatingGridCell extends GridCell<SmallGalleryImageWithRating> {

    /**
     * Creates a default ImageGridCell instance, which will preserve image properties
     */
    private int DEFAULT_CELL_SIZE=400;

    public ImageWithRatingGridCell() {
        this.setAlignment(Pos.CENTER);

    }


    /**
     * {@inheritDoc}
     */
    @Override protected void updateItem(SmallGalleryImageWithRating item, boolean empty) {
        super.updateItem(item, empty);
        if(item==null||item.getImageView()==null){
            return;
        }
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(item);
        }
    }
}
