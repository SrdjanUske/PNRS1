package srdjan.usorac.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    public static final String SEND_URL = HttpHelper.BASE_URL + "/message";
    public static final String LOGOUT_URL = HttpHelper.BASE_URL + "/logout";

    SharedPreferences preferences;
    Button logout, send, refresh;
    EditText message;
    TextView name;
    ListView list;
    private ChatAdapter chat_list;
    private Chat chat;
    private Chat[] messages;
    private String sessionID;
    private String receiver, sender;
    private Handler handler;
    private HttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        name = findViewById(R.id.message_contact);

        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
        sender = preferences.getString("logged_in", null);
        receiver = getIntent().getStringExtra(Contact.name);
        sessionID = preferences.getString("sessionID", null);
        name.setText(receiver);

        logout = findViewById(R.id.logout_from_chat);
        logout.setOnClickListener(this);
        refresh = findViewById(R.id.refresh_messages);
        refresh.setOnClickListener(this);

        list = findViewById(R.id.messages);
        chat_list = new ChatAdapter(this);
        list.setAdapter(chat_list);

        handler = new Handler();
        httpHelper = new HttpHelper();

        send = findViewById(R.id.send);
        send.setEnabled(false);
        send.setOnClickListener(this);

        message = findViewById(R.id.message);
        message.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logout_from_chat) {

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
                                    Toast.makeText(MessageActivity.this, "ERROR " + success.responceCode + ": " + success.responceMessage, Toast.LENGTH_LONG).show();
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
        else if(view.getId() == R.id.send){
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HttpHelper.RECEIVER, receiver);
                        jsonObject.put(HttpHelper.DATA, message.getText().toString());

                        final HttpHelper.Responce success = httpHelper.postJSONObjectFromURL(SEND_URL, jsonObject, sessionID);

                        if (success.responceCode == HttpHelper.SUCCESS) {
                                handler.post(new Runnable(){
                                    public void run() {

                                        Contact sender_name = new Contact(sender);
                                        Contact receiver_name = new Contact(receiver);
                                        chat = new Chat(sender_name, receiver_name, message.getText().toString());
                                        chat_list.addMessage(chat);
                                        chat_list.notifyDataSetChanged();
                                        message.setText("");
                                    }
                                });
                        }
                        else {
                            handler.post(new Runnable(){
                                public void run() {
                                    Toast.makeText(MessageActivity.this, "ERROR " + success.responceCode + ": " + success.responceMessage , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        else if (view.getId() == R.id.refresh_messages) {
            getMessages();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessages();
    }

    public void getMessages() {
        chat_list.clear();
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONArray jsonArray = httpHelper.getJSONArrayFromURL(SEND_URL + "/" + receiver, sessionID);

                    if (jsonArray == null) {
                        Toast.makeText(MessageActivity.this, "Unknown error, returning to MainActivity!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        messages = new Chat[jsonArray.length()];
                        for(int i = 0; i < jsonArray.length(); i++) {
                            final int index = i;
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String data = jsonobject.getString(HttpHelper.DATA);
                            String sender_server = jsonobject.getString(HttpHelper.SENDER);

                            Contact sender_name = new Contact(sender_server);
                            Contact receiver_name = new Contact(receiver);
                            Chat message = new Chat(sender_name, receiver_name, data);
                            messages[i] = message;
                            handler.post(new Runnable(){
                                public void run() {
                                    chat_list.addMessage(messages[index]);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    handler.post(new Runnable(){
                        public void run() {
                            chat_list.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (message.getText().toString().length() != 0){
            send.setEnabled(true);
        }
        else{
            send.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
