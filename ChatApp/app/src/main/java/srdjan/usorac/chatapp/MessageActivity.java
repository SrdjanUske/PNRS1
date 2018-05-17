package srdjan.usorac.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private int sender_id, receiver_id;
    SharedPreferences preferences;
    Button logout, send;
    EditText message;
    Bundle chat_contact;
    TextView name;
    ListView list;
    ChatAdapter chat_list;
    //DbHelper mDbHelper;
    private String sessionID;
    private String receiver, sender;
    private Handler handler;
    private HttpHelper httpHelper;
    public static String SEND_URL = HttpHelper.BASE_URL + "/message";
    public static String LOGOUT_URL = HttpHelper.BASE_URL + "/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        chat_contact = getIntent().getExtras();
        name = (TextView) findViewById(R.id.message_contact);
        name.setText(String.valueOf(chat_contact.getString("contact_name")));

        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
        sender_id = preferences.getInt("senderID", -1);
        receiver_id = chat_contact.getInt("receiverID");
        sender = preferences.getString("logged_in", null);
        receiver = chat_contact.getString("contact_name");
        sessionID = preferences.getString("sessionID", null);

        //mDbHelper = DbHelper.getInstance(this);

        logout = findViewById(R.id.logout_from_chat);
        logout.setOnClickListener(this);

        list = (ListView) findViewById(R.id.messages);
        chat_list = new ChatAdapter(this);
        list.setAdapter(chat_list);

        // initializing
        /*Chat[] chats = mDbHelper.readMessages(sender_id, receiver_id);

        if (chats != null) {
            for (Chat chat : chats) {
                chat_list.addMessage(chat);
            }
        }*/

        send = findViewById(R.id.send);
        send.setEnabled(false);
        send.setOnClickListener(this);

        message = findViewById(R.id.message);
        message.addTextChangedListener(this);
    }

    /*public void update(int sender_id, int receiver_id) {

        Chat[] chats = mDbHelper.readMessages(sender_id, receiver_id);
        chat_list.update(chats);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //update(sender_id, receiver_id);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.send){

            /*Contact sender = mDbHelper.getContact(sender_id);
            Contact receiver = mDbHelper.getContact(receiver_id);

            Chat chat = new Chat(0, sender, receiver, message.getText().toString());
            mDbHelper.insertMessage(chat);

            chat_list.addMessage(chat);
            chat_list.notifyDataSetChanged(); */
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

                                        Toast.makeText(getApplicationContext(), "Message is sent", Toast.LENGTH_SHORT).show();

                                        Contact sender_name = new Contact(sender);
                                        Contact receiver_name = new Contact(receiver);
                                        Chat chat = new Chat(sender_name, receiver_name, message.getText().toString());
                                        chat_list.addMessage(chat);
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
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String data = jsonobject.getString(HttpHelper.DATA);

                            Contact sender_name = new Contact(sender);
                            Contact receiver_name = new Contact(receiver);
                            Chat message = new Chat(sender_name, receiver_name, data);

                            chat_list.addMessage(message);
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
