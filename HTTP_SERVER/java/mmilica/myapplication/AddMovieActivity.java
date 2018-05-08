package mmilica.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddMovieActivity extends AppCompatActivity implements View.OnClickListener{

    private HttpHelper httpHelper;
    public EditText nameEdit, genreEdit, yearEdit, durationEdit, directorEdit;
    public Button addMovie;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        httpHelper = new HttpHelper();
        handler = new Handler();
        nameEdit = (EditText) findViewById(R.id.form_name);
        genreEdit = (EditText) findViewById(R.id.form_genre);
        yearEdit = (EditText) findViewById(R.id.form_year);
        durationEdit = (EditText) findViewById(R.id.form_duration);
        directorEdit = (EditText) findViewById(R.id.form_director);
        addMovie = (Button) findViewById(R.id.add_movie);
        addMovie.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_movie) {
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        // TODO: Fill in JSONObject with details about new movie

                        final boolean success = httpHelper.postJSONObjectFromURL(MainActivity.MOVIE_URL, jsonObject);
                        handler.post(new Runnable(){
                            public void run() {
                                Toast.makeText(AddMovieActivity.this, "Adding new movie: " + success, Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent intent = new Intent(AddMovieActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
