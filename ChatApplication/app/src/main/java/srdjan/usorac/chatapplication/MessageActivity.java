package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
    private Handler handler;
    private HttpHelper httpHelper;
    public static String BASE_URL = "http://18.205.194.168";
    //public static String LOGIN_URL = BASE_URL + "/login/";

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

            Toast.makeText(getApplicationContext(), "Message is sent", Toast.LENGTH_SHORT).show();
            message.setText("");

        }
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
