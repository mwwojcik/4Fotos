package org.controlsfx.control;

/**
 * Created by Mariusz.Wojcik on 2016-08-18.
 */
public class SmallRating extends Rating {

    public SmallRating() {

    }

    @Override public String getUserAgentStylesheet() {
        return this.getClass().getResource("/org/controlsfx/control/small-rating.css").toExternalForm();
    }
}
