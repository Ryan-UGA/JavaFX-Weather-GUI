package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
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
    Button button;
    /** Below the top hbox will be a label with instructions and then space from cc's. */
    Label instructions;
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
    CustomComponent cc11;
    CustomComponent cc12;
    CustomComponent cc13;
    CustomComponent cc14;

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
        search = new TextField("Type the city here.");
        button = new Button("Get Weather");
        instructions = new Label("Type a city, state to get the weather for that area.");
        blank = new Label();
        openCageResponse = new OpenCageResponse();
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
        cc11 = new CustomComponent(11);
        cc12 = new CustomComponent(12);
        cc13 = new CustomComponent(13);
        cc14 = new CustomComponent(14);
    } // ApiApp

    /** {@inheritDoc} */
    public void init() {
        top.getChildren().addAll(search, button);
        root.getChildren().add(top);
        root.getChildren().addAll(instructions, blank, cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8,
            cc9, cc10, cc11, cc12, cc13, cc14);
        root.setSpacing(10);
        /** First, getLatLong occurs. Then, getForecastLink occurs. Lastly, getWeather occurs. */
        EventHandler<ActionEvent> weather = ae -> getWeather();
        button.setOnAction(weather);
    } // init

    /** This method gets the latitude and longitude of an inputted city using OpenCage API. */
    private void getLatLong() {
        try {
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
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        } // try
    } // getLatLong

    /**
     * This method will get the weather at specified latitude and longitude using
     * the National Weather Service API.
     */
    private void getForecastLink() {
        getLatLong();
        try {
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
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        } // try
    } // getForecastLink

    /** This is the final response that will get the weather with the updated forecastLink. */
    private void getWeather() {
        getForecastLink();
        try {
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
            cc1.setComponents(forecastResponse);
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        } // try
    } // getWeather

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
        stage.setTitle("Weatherman Barnes");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start
} // ApiApp
