package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

/**
 * This class is a custom component that will be used to display the detailed weather forecast.
 * Each component is a hbox of two labels, one for the day (during or not during the day) and
 * one for the detailed forecast.
 */
public class CustomComponent extends HBox {
    /** These are all variables associated with the day name. */
    Label name;

    /** These are all variables associated with the detailed forecast. */
    Label summary;

    /**
     * This constructs a new {@code CustomComponent} for the ApiApp.
     *
     // @param n the nth custom component
     */
    public CustomComponent(/** int num */) {
        name = new Label("Name");
        summary = new Label("Label");
        this.getChildren().addAll(name, summary);
        this.setSpacing(20);
    } // CustomComponent
} // CustomComponent
