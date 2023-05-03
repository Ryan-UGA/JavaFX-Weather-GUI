package cs1302.api;

/**
 * Represents a response from the OpenCage API. The API will conduct forward geolocating,
 * the process of converting a location into latitude and longitude. This will be used by Gson
 * in the ApiApp class.
 */
public class OpenCageResponse {
    OpenCageResult[] results;
} // OpenCageResponse
