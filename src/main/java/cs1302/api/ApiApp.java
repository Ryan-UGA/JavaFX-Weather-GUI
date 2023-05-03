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
    /** Variables used below. */
    Stage stage;
    Scene scene;
    VBox root;
    /** Root consists of this hbox of items on the top. */
    HBox hbox;
    TextField search;
    Button button;
    /** This is the variable storing the downloads from the OpenCage API. */
    private OpenCageResponse openCageResponse;
    /** This is the city, state that the user will enter. */
    private String place;
    /** This will be the latitude and longitude of the city the user inputs. */
    private double latitude;
    private double longitude;
    /** This is the variable storing the downloads from the NWS API. */
    private NWSResponse nwsResponse;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        hbox = new HBox(8);
        search = new TextField("Type the city here.");
        button = new Button("Get Weather");
        openCageResponse = new OpenCageResponse();
    } // ApiApp

    /** {@inheritDoc} */
    public void init() {
        hbox.getChildren().addAll(search, button);
        root.getChildren().add(hbox);
        EventHandler<ActionEvent> weather = ae -> getForecastLink();
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
            printOpenCageResponse(openCageResponse);
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
            System.out.println("uri = " + uri);
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
            printNWSResponse(nwsResponse);
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        } //
    } // getForecastLink

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
        System.out.println("forecast link = " + nwsResponse.properties.forecast);
    } // printNWSResponse

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        // demonstrate how to load local asset using "file:resources/"
        scene = new Scene(root);
        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start
} // ApiApp
