package srdjan.usorac.chatapp;


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

    public static final int SUCCESS = 200;
    public static final String BASE_URL = "http://18.205.194.168:80";

    /* Inicijalizacija svih polja kod json fajlova */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String SESSION_ID = "sessionid";
    public static final String RECEIVER = "receiver";
    public static final String DATA = "data";

    /*HTTP get json Array*/
    public JSONArray getJSONArrayFromURL(String urlString, String sessionID) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setRequestProperty(SESSION_ID, sessionID);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        //Log.d("HTTP GET", "JSON data- " + jsonString);

        //Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        //Log.i("MSG", urlConnection.getResponseMessage());

        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    /*HTTP post*/
    public Responce postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        Responce responce = new Responce();
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            responce.responceCode = 404;
            responce.responceMessage = "Connection error!";
            return responce;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        responce.responceCode = urlConnection.getResponseCode();
        responce.responceMessage = urlConnection.getResponseMessage();
        responce.sessionID = urlConnection.getHeaderField(SESSION_ID);

        //Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        //Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();

        /*preferences = mContext.getApplicationContext().getSharedPreferences("MyPreferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionID", urlConnection.getHeaderField(0));
        editor.commit();*/

        return responce;
    }

    public Responce postJSONObjectFromURL(String urlString, JSONObject jsonObject, String sessionID) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        Responce responce = new Responce();
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty(SESSION_ID, sessionID);
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            responce.responceCode = 404;
            responce.responceMessage = "Connection error!";
            return responce;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        responce.responceCode = urlConnection.getResponseCode();
        responce.responceMessage = urlConnection.getResponseMessage();
        responce.sessionID = urlConnection.getHeaderField(SESSION_ID);

        //Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        //Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();

        /*preferences = mContext.getApplicationContext().getSharedPreferences("MyPreferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionID", urlConnection.getHeaderField(0));
        editor.commit();*/

        return responce;
    }

    public class Responce {
        public int responceCode;
        public String responceMessage;
        public String sessionID;
    }
}