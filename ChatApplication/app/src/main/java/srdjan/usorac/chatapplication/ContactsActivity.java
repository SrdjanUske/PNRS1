package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    public SharedPreferences preferences;
    private Button logout, refresh;
    private ImageView chat;
    private CharacterAdapter adapter;
    private Handler handler;
    private HttpHelper httpHelper;
    public static String BASE_URL = "http://18.205.194.168";
    public static String CONTACTS_URL = BASE_URL + "/contacts/";
    private User[] users;
    private String username;
    private String sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logout = findViewById(R.id.logout);
        refresh = findViewById(R.id.refresh);
        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);

        chat = findViewById(R.id.imageView);

        adapter = new CharacterAdapter(this);

        ListView list = (ListView) findViewById(R.id.contacts_list);
        list.setAdapter(adapter);

        //Bundle bundle = getIntent().getExtras();
        //String username = bundle.getString("UserName");

        /*DbHelper mDbHelper = DbHelper.getInstance(this);
        Contact[] contacts = mDbHelper.readContacts();

        if (contacts == null) {
            Toast.makeText(getApplicationContext(), "No contacts!", Toast.LENGTH_SHORT).show();
        }
        else {

            for (Contact contact : contacts) {
                if (!(contact.getUserName().equals(username))) {

                    adapter.addCharacter(contact);
                }
            }
        }*/
        handler = new Handler();
        httpHelper = new HttpHelper(this);
        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
        sessionID = preferences.getString("sessionID", "");
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        if (view.getId() == R.id.logout) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.refresh) {

            new Thread(new Runnable() {
                public void run() {
                    try {
                        JSONArray jsonArray = httpHelper.getJSONArrayFromURL(ContactsActivity.CONTACTS_URL + "?" + sessionID);
                        if(jsonArray != null) {
                            users = new User[jsonArray.length()];
                            for(int i = 0; i < jsonArray.length(); i++) {
                                final int index = i;
                                JSONObject jsonobject = jsonArray.getJSONObject(i);
                                username = jsonobject.getString("username");
                                User user = new User(sessionID, username);
                                users[i] = user;
                                handler.post(new Runnable(){
                                    public void run() {
                                        adapter.addCharacter(users[index]);
                                        Toast.makeText(ContactsActivity.this,"Got contact: " + index, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        handler.post(new Runnable(){
                            public void run() {
                                Toast.makeText(ContactsActivity.this,"No contacts!", Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
