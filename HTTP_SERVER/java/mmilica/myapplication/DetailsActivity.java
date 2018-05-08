package mmilica.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class DetailsActivity extends AppCompatActivity {

    private HttpHelper httpHelper;
    private String movieId;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        httpHelper = new HttpHelper();
        textView = (TextView) findViewById(R.id.movie_text);

        Bundle intent = getIntent().getExtras();
        if(intent!=null) {
            movieId = (String) intent.get("movieId");
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject jsonObj = httpHelper.getJSONObjectFromURL(MainActivity.MOVIE_URL + movieId);
                    textView.setText(formMovieTextFromJson(jsonObj));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String formMovieTextFromJson(JSONObject movieJson) throws JSONException {
        Iterator<?> keys = movieJson.keys();
        String str = "Movie: \n";

        // TODO: Parse response from server
        // Parsed string should look like:
        //
        //  key: value
        // -----------------------------
        //  name: movieName
        //  genre: movieGenre
        //  year: 9999
        //  duration: 9999
        //  director: FirstName LastName

        // Note: value can be JSONArray, too!

        while(keys.hasNext()) {
            String key = (String)keys.next();
            if (!(movieJson.get(key) instanceof JSONArray)) {

            } else {

            }
        }
        return str;
    }
}
