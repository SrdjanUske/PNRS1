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

    public static final String CONTACTS_URL = HttpHelper.BASE_URL + "/contacts";
    public static final String LOGOUT_URL = HttpHelper.BASE_URL + "/logout";

    public SharedPreferences preferences;
    private Button logout, refresh;
    private ImageView chat;
    private CharacterAdapter adapter;
    private Handler handler;
    private HttpHelper httpHelper;
    private Contact[] contacts;
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

        ListView list = findViewById(R.id.contacts_list);
        list.setAdapter(adapter);

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

    @Override
    protected void onResume() {
        super.onResume();
        //getContacts();
    }

    public void getContacts() {
        adapter.clear();
        new Thread(new Runnable() {
            public void run() {
                try {
                    final JSONArray jsonArray = httpHelper.getJSONArrayFromURL(CONTACTS_URL, sessionID);

                    if (jsonArray == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ContactsActivity.this, "Unknown error, returning to MainActivity!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        contacts = new Contact[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final int index = i;
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String user = jsonobject.getString(HttpHelper.USERNAME);
                            Contact contact = new Contact(user);
                            contacts[i] = contact;
                                if (!username.equals(user)) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.addCharacter(contacts[index]);
                                        }
                                    });
                                }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
