package srdjan.usorac.chatapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    public static final String CONTACTS_URL = HttpHelper.BASE_URL + "/contacts";
    public static final String LOGOUT_URL = HttpHelper.BASE_URL + "/logout";
    public static final String GET_NEW_MESSAGE_URL = HttpHelper.BASE_URL + "/getfromservice";
    public static final int UNIQUE_ID_NEW_MESSAGE = 1234;

    public SharedPreferences preferences;
    private Button logout, refresh;
    private ImageView chat;
    private CharacterAdapter adapter;
    private Handler handler;
    private HttpHelper httpHelper;
    private Contact[] contacts;
    private String username;
    private String sessionID;
    NotificationBinder mService = null;

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

        bindService(new Intent(ContactsActivity.this, NotificationService.class), this, Context.BIND_AUTO_CREATE);
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
        getContacts();
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
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = (NotificationBinder) NotificationBinder.Stub.asInterface(service);
        try {
            mService.setCallback(new Callback());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private class Callback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {
            final HttpHelper httpHelper = new HttpHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getString(R.string.new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.d("TAG", "Hello from Callback");
                    try {
                        final boolean response = httpHelper.getFromServiceFromURL(GET_NEW_MESSAGE_URL, sessionID);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("TAG", "New message -> " + response);
                                if (response) {
                                    managerCompat.notify(UNIQUE_ID_NEW_MESSAGE, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
