package mmilica.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    private static final int SUCCESS = 200;

    /*HTTP get json Array*/
    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        // TODO: Implement HTTP GET request for retrieving JSON Array that contains array of movies.
        //    - method - GET
        //    - headers - Accept: application/json
        //    - read timeout - 1 second
        //    - connect timeout - 15 seconds
        // Check response code - if it is 200, return new JSONArray, otherwise return null

        return null;
    }

    /*HTTP get json object*/
    public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        // TODO: Implement HTTP GET request for retrieving Movie whose id is forwarded.
        //    - method - GET
        //    - headers - Accept: application/json
        //    - read timeout - 1 second
        //    - connect timeout - 15 seconds
        // Check response code - if it is 200, return new JSONObject, otherwise return null

        return null;
    }

    /*HTTP post*/
    public boolean postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        // TODO: Implement HTTP POST request for sending new Movie object to server.
        //    - method - POST
        //    - headers - Accept: application/json
        //              - Content-Type", application/json;charset=UTF-8
        //    - read timeout - 1 second
        //    - connect timeout - 15 seconds
        // Check response code - if it is 200, return true, otherwise return false

        return false;
    }

    /*HTTP delete*/
    public boolean httpDelete(String urlString) throws IOException, JSONException {
        // TODO: Implement HTTP DELETE request for removing Movie from server.
        //    - method - DELETE
        //    - headers - Accept: application/json
        //              - Content-Type", application/json; charset=UTF-8
        // Check response code - if it is 200, return true, otherwise return false

        return false;
    }
}
