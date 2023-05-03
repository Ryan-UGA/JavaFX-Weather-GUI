package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

/**
 * This class is a custom component that will be used to display the detailed weather forecast.
 * Each component is a hbox of two labels, one for the day (during or not during the day) and
 * one for the detailed forecast.
 */
public class CustomComponent extends HBox {
    Label name;
    Label summary;

    /** This creates a new {@code CustomComponent} object. */
    public CustomComponent() {
        name = new Label("name");
        summary = new Label("detailedForecast");
        this.getChildren().addAll(name, summary);
    } // CustomComponent
} // CustomComponent
