package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

/**
 * The ApiApp includes two API's, OpenCage and the National Weather Service.
 * OpenCage is the API that will convert a city entered by the user into latitude and
 * longitude. Then, these coordinates are inputted into the National Weather Service's uri
 * to get the weather forecast.
 * <p>
 * However, the National Weather Service's API requires two different responses before reaching
 * the data giving the weather's weekly forecast. The first response, indicated by the variable
 * openCageResponse, will get a variety of data, including links to the weekly and hourly weather
 * forecast. I pulled the link giving the weekly weather forecast, indicated by the variable
 * forecastLink. Then, this link is the uri for the final response, indicated by the variable
 * forecastResponse.
 */
public class ApiApp extends Application {
    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    /** This is the base of the OpenCage API. */
    private static final String OPENCAGE_API = "https://api.opencagedata.com/geocode/v1/json?q=";

    /** This is the base of the National Weather Service API. */
    private static final String NWS_API = "https://api.weather.gov/points/";

    /** Stage, scene, and components in app. */
    Stage stage;
    Scene scene;
    VBox root;
    /** Root consists of this hbox of items on the top. */
    HBox top;
    TextField search;
    Button getDetailedForecasts;
    /** Below the top hbox will be a label with instructions and then space from cc's. */
    Label format;
    Label instructions;
    Label extraNote;
    Label blank;
    /** Root will also consist of the custom components (name of day, forecast). */
    CustomComponent cc1;
    CustomComponent cc2;
    CustomComponent cc3;
    CustomComponent cc4;
    CustomComponent cc5;
    CustomComponent cc6;
    CustomComponent cc7;
    CustomComponent cc8;
    CustomComponent cc9;
    CustomComponent cc10;

    /** This is the variable storing the downloads from the OpenCage API. */
    OpenCageResponse openCageResponse;

    /** This is the city, state that the user will enter. */
    String place;

    /** This will be the latitude and longitude of the city the user inputs. */
    double latitude;
    double longitude;

    /** This is the variable storing the downloads from the NWS API. */
    NWSResponse nwsResponse;

    /** This is the link to the weekly weather forecast for some area found with vars above. */
    String forecastLink;

    /** This will be the final response from Json. */
    ForecastResponse forecastResponse;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        top = new HBox(10);
        search = new TextField("Type the city and state here.");
        getDetailedForecasts = new Button("Get Forecasts");
        format = new Label("Format of input can be city OR city, state");
        instructions = new Label("Type the city and press the button to get the forecasts"
            + " for the next 10 time periods.");
        String extra = "Warning: If the city entered is entered with the wrong state, weather " +
            "at some location in said state might be the result.";
        extraNote = new Label(extra);
        blank = new Label();
        cc1 = new CustomComponent(1);
        cc2 = new CustomComponent(2);
        cc3 = new CustomComponent(3);
        cc4 = new CustomComponent(4);
        cc5 = new CustomComponent(5);
        cc6 = new CustomComponent(6);
        cc7 = new CustomComponent(7);
        cc8 = new CustomComponent(8);
        cc9 = new CustomComponent(9);
        cc10 = new CustomComponent(10);
    } // ApiApp

    /** {@inheritDoc} */
    public void init() {
        top.getChildren().addAll(search, getDetailedForecasts);
        root.getChildren().add(top);
        root.getChildren().addAll(format, instructions, extraNote, blank);
        root.getChildren().addAll(cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9, cc10);
        root.setSpacing(10);
        top.setHgrow(search, Priority.ALWAYS);
        Runnable df = () -> {
            ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
            Dialog<String> dialogBox = new Dialog<>();
            dialogBox.getDialogPane().getButtonTypes().add(ok);
            dialogBox.setTitle("Error");
            String message = "";
            try {
                setDetailedForecasts();
                cc1.fixDetailedForecast(forecastResponse.properties.periods);
                cc2.fixDetailedForecast(forecastResponse.properties.periods);
                cc3.fixDetailedForecast(forecastResponse.properties.periods);
                cc4.fixDetailedForecast(forecastResponse.properties.periods);
                cc5.fixDetailedForecast(forecastResponse.properties.periods);
                cc6.fixDetailedForecast(forecastResponse.properties.periods);
                cc7.fixDetailedForecast(forecastResponse.properties.periods);
                cc8.fixDetailedForecast(forecastResponse.properties.periods);
                cc9.fixDetailedForecast(forecastResponse.properties.periods);
                cc10.fixDetailedForecast(forecastResponse.properties.periods);
            } catch (IOException | InterruptedException e) {
                message = "I'm sorry, but there was a problem loading the webpage. There " +
                    "might be multiple of the same city or this city might be in a country " +
                    "besides the United States. Please make sure to enter a valid city " +
                    "in the United States.";
                dialogBox.setContentText(message);
                dialogBox.showAndWait();
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                message = "I'm sorry, but this is not a valid input. Please try " +
                    "again, and make sure to enter a city in the United States.";
                dialogBox.setContentText(message);
                dialogBox.showAndWait();
            } catch (NullPointerException npe) {
                message = npe.getMessage();
                message += "\n\nThe input was invalid. Please make sure to enter a valid city " +
                    "in the United States.";
                dialogBox.setContentText(message);
                dialogBox.showAndWait();
            } // try
        }; // weather lambda
        getDetailedForecasts.setOnAction(event -> runInNewThread(df));
    } // init

    /**
     * This method gets the latitude and longitude of an inputted city using OpenCage API.
     *
     * @throws IOException when the site doesn't pull up
     * @throws InterruptedException when the site doesn't pull up
     */
    private void getLatLong() throws IOException, InterruptedException {
        place = URLEncoder.encode(search.getText(), StandardCharsets.UTF_8);
        String temp = "054c04b850b6435fb6a4726fd811c928";
        String apikey = URLEncoder.encode(temp, StandardCharsets.UTF_8);
        String query = String.format("?q=%s&key=%s", place, apikey);
        String uri = OPENCAGE_API + query;
        // building the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        // send the request and receive response in the form of a String
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        // ensure request is okay
        if (response.statusCode() != 200) {

            throw new IOException(response.toString());
        } // if
        // get request body (the content we requested minus excess
        String jsonString = response.body();
        // parse the JSON-formatted string using GSON
        openCageResponse = GSON
            .fromJson(jsonString, OpenCageResponse.class);
        int index = openCageResponse.results.length - 1;
        double neLatitude = openCageResponse.results[index].bounds.northeast.lat;
        double neLongitude = openCageResponse.results[index].bounds.northeast.lng;
        double swLatitude = openCageResponse.results[index].bounds.southwest.lat;
        double swLongitude = openCageResponse.results[index].bounds.southwest.lng;
        latitude = (neLatitude + swLatitude) / 2;
        longitude = (neLongitude + swLongitude) / 2;
    } // getLatLong

    /**
     * This method will get the weather at specified latitude and longitude using
     * the National Weather Service API.
     *
     * @throws IOException when the site doesn't pull up
     * @throws InterruptedException when the site doesn't pull up
     */
    private void getForecastLink() throws IOException, InterruptedException {
        getLatLong();
        String uri = NWS_API + latitude + "," + longitude;
        // building the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        // send the request and receive response in the form of a String
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        // ensure request is okay
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if
        // get request body (the content we requested minus excess
        String jsonString = response.body();
        // parse the JSON-formatted string using GSON
        nwsResponse = GSON
            .fromJson(jsonString, NWSResponse.class);
        forecastLink = nwsResponse.properties.forecast;
        printNWSResponse(nwsResponse);
    } // getForecastLink

    /**
     * This is the final response that will get the weather with the updated forecastLink.
     *
     * @throws IOException when the site doesn't pull up
     * @throws InterruptedException when the site doesn't pull up
     */
    private void getWeather() throws IOException, InterruptedException {
        getForecastLink();
        String uri = forecastLink;
        // building the request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        // send the request and receive response in the form of a String
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        // ensure request is okay
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if
        // get request body (the content we requested minus excess
        String jsonString = response.body();
        // parse the JSON-formatted string using GSON
        forecastResponse = GSON
            .fromJson(jsonString, ForecastResponse.class);
    } // getWeather

    /**
     * This will run said runnable in a new thread.
     *
     * @param target contains what the thread will do when started
     */
    private void runInNewThread(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runInNewThread

    /**
     * This method will set detailed forecasts for every custom component.
     *
     * @throws IOException when the site doesn't pull up
     * @throws InterruptedException when the site doesn't pull up
     */
    private void setDetailedForecasts() throws IOException, InterruptedException {
        getWeather();
        Platform.runLater(() -> cc1.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc2.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc3.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc4.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc5.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc6.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc7.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc8.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc9.setDetailedForecast(forecastResponse));
        Platform.runLater(() -> cc10.setDetailedForecast(forecastResponse));
    } // setDetailedForecasts

    /**
     * Print a response from the OpenCage API.
     *
     * @param openCageResponse the response object
     */
    private void printOpenCageResponse(OpenCageResponse openCageResponse) {
        System.out.println();
        System.out.println("********* PRETTY JSON STRING FOR " + place + ": *********");
        System.out.println(GSON.toJson(openCageResponse));
        System.out.println();
        System.out.println("********** PARSED RESULTS: **********");
        System.out.println("latitude = " + latitude);
        System.out.println("longitude = " + longitude);
    } // printOpenCageResponse

    /**
     * Print a response from the NWS API.
     *
     * @param nwsResponse the response object
     */
    private void printNWSResponse(NWSResponse nwsResponse) {
        System.out.println();
        System.out.println("********* PRETTY JSON STRING FOR NWSResponse : *********");
        System.out.println(GSON.toJson(nwsResponse));
        System.out.println();
        System.out.println("********** PARSED RESULTS: **********\n");
        System.out.println("forecast link actually = " + forecastLink);
    } // printNWSResponse

    /**
     * Print a response from the NWS API (forecast response).
     *
     * @param forecastResponse the final response that got the weekly weather forecast
     */
    private void printForecastResponse(ForecastResponse forecastResponse) {
        System.out.println();
        System.out.println("********* PRETTY JSON STRING FOR ForecastResponse : *********");
        System.out.println(GSON.toJson(forecastResponse));
        System.out.println();
        System.out.println("********** PARSED RESULTS: **********\n");
        System.out.println("Printing periods array below");
        for (int i = 0; i < forecastResponse.properties.periods.length; i++) {
            System.out.println("Name = " + forecastResponse.properties.periods[i].name);
            System.out.println("dF = " + forecastResponse.properties.periods[i].detailedForecast);
        } // for
    } // printForecastResponse

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        // demonstrate how to load local asset using "file:resources/"
        scene = new Scene(root);
        // setup stage
        stage.setTitle("Weather in US Cities");
        stage.setMaxWidth(1250.0);
        stage.setMaxHeight(700.0);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start
} // ApiApp
