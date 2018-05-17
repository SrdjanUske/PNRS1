package srdjan.usorac.chatapp;

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
    public static String CONTACTS_URL = HttpHelper.BASE_URL + "/contacts";
    public static String LOGOUT_URL = HttpHelper.BASE_URL + "/logout";
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
        httpHelper = new HttpHelper();
        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
        sessionID = preferences.getString("sessionID", null);
        username = preferences.getString("logged_in", null);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.logout) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HttpHelper.Responce success = httpHelper.postJSONObjectFromURL(LOGOUT_URL, new JSONObject(), sessionID);
                        if (success.responceCode == HttpHelper.SUCCESS) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Logging out!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ContactsActivity.this, "ERROR " + success.responceCode + ": " + success.responceMessage, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        else if (view.getId() == R.id.refresh) {
            getContacts();
        }
    }

    public void getContacts() {
        adapter.clear();
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONArray jsonArray = httpHelper.getJSONArrayFromURL(CONTACTS_URL, sessionID);

                    if (jsonArray == null) {
                        Toast.makeText(ContactsActivity.this, "Unknown error, returning to MainActivity!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String user = jsonobject.getString(HttpHelper.USERNAME);
                            Contact contact = new Contact(username);

                            if (!username.equals(user)) {
                                adapter.addCharacter(contact);
                            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
