package mmilica.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener{

    private HttpHelper httpHelper;
    private ArrayAdapter<String> mListAdapter;
    public static String BASE_URL = "http://192.168.0.14:8080";
    public static String GET_MOVIE_LIST = BASE_URL + "/api/movies/";
    public static String MOVIE_URL = BASE_URL + "/api/movie/";
    private MovieModel [] movieModels;
    private Button addNewMovieButton, getMovies;
    private Handler handler;
    private String name, movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*used to handle UI components in separate thread*/
        handler = new Handler();

        addNewMovieButton = (Button) findViewById(R.id.addMovie);
        addNewMovieButton.setOnClickListener(this);
        getMovies = (Button) findViewById(R.id.getMovies);
        getMovies.setOnClickListener(this);

        httpHelper = new HttpHelper();
        mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mListAdapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        String movieId = movieModels[position].getId();
        intent.putExtra("movieId",  movieId);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String item = mListAdapter.getItem(position);
        final String movieId = movieModels[position].getId();
        new Thread(new Runnable() {
            public void run() {
                try {
                    final boolean success = httpHelper.httpDelete(MOVIE_URL + movieId);
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Deleting: " + success, Toast.LENGTH_LONG).show();
                            if(success) {
                                mListAdapter.remove(item);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.addMovie) {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        } else if(id == R.id.getMovies) {
            // a potentially  time consuming task
            mListAdapter.clear();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        JSONArray jsonArray = httpHelper.getJSONArrayFromURL(GET_MOVIE_LIST);
                        if(jsonArray != null) {
                            movieModels = new MovieModel[jsonArray.length()];
                            for(int i = 0; i < jsonArray.length(); i++) {
                                final int index = i;
                                // TODO: Parse response from server
                                // Create new MovieModel object (note that only movie id and name are needed for this)

                                handler.post(new Runnable(){
                                    public void run() {
                                        mListAdapter.add(movieModels[index].getName());
                                    }
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
