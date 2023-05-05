package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;

/**
 * This class is a custom component that will be used to display the detailed weather forecast.
 * Each component is a hbox of two labels, one for the day (during or not during the day) and
 * one for the detailed forecast.
 */
public class CustomComponent extends HBox {
    Label nameLabel;
    Label summary;
    int index; // index of the Periods array that information will be pulled from

     /**
     * This constructs a new {@code CustomComponent} for the ApiApp.
     *
     * @param n the nth custom component
     */
    public CustomComponent(int n) {
        index = n - 1;
        nameLabel = new Label("");
        nameLabel.setMinWidth(125.0);
        summary = new Label("");
        this.getChildren().addAll(nameLabel, summary);
        this.setSpacing(20);
        this.setHgrow(nameLabel, Priority.ALWAYS);
    } // CustomComponent

    /**
     * This method sets the detailed forecast of the custom component.
     * It will be called in the ApiApp class after there is a forecastResponse generated.
     *
     * @param forecastResponse the final response that gets the weather in ApiApp
     */
    public void setDetailedForecast(ForecastResponse forecastResponse) {
        nameLabel.setText(forecastResponse.properties.periods[index].name);
        summary.setText(forecastResponse.properties.periods[index].detailedForecast);
    } // setDetailedForecast
} // CutomComponent
