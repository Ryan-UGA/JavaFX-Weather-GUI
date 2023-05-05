package cs1302.api;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;

/**
 * This class is a custom component that will be used to display the detailed weather forecast.
 * Each component is a hbox of two labels, one for the day (during or not during the day) and
 * one for the detailed forecast.
 */
public class CustomComponent extends VBox {
    HBox hbox1;
    Label nameLabel;
    Label summary;
    int index; // index of the Periods array that information will be pulled from
    HBox hbox2;
    Label extraBlank;
    Label extraInfo;
    String originalString;

     /**
     * This constructs a new {@code CustomComponent} for the ApiApp.
     *
     * @param n the nth custom component
     */
    public CustomComponent(int n) {
        index = n - 1;
        nameLabel = new Label();
        nameLabel.setMinWidth(125.0);
        summary = new Label();
        extraInfo = new Label();
        hbox1 = new HBox();
        extraBlank = new Label();
        extraBlank.setMinWidth(127.0);
        hbox2 = new HBox();
        hbox2.getChildren().addAll(extraBlank, extraInfo);
        hbox1.getChildren().addAll(nameLabel, summary);
        this.getChildren().addAll(hbox1, hbox2);
        hbox1.setSpacing(10);
        this.setSpacing(5);
        hbox1.setHgrow(nameLabel, Priority.ALWAYS);
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

    /**
     * This method organizes the information from the detailedForecast variable
     * to where all of the information will fit on the screen.
     *
     * @param periods where the detailedForecasts strings are stored
     */
    public void fixDetailedForecast(Periods[] periods) {
        String originalString = periods[index].detailedForecast;
        String printString = "  ";
        boolean add = true;
        int startIndex = 0;
        for (int i = 0; i < originalString.length(); i++) {
            if (originalString.charAt(i) == ' ' && i >= 140 && add == true) {
                add = false;
                startIndex = i;
            } // if
        } // for
        if (startIndex >= 140) {
            printString += "Previous line continued: ";
            printString += originalString.substring(startIndex, originalString.length());
        } // if
        this.extraInfo.setText(printString);
    } // fixDetailedForecast
} // CutomComponent
