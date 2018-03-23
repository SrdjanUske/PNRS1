package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button logout, send;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        logout = findViewById(R.id.logout_from_chat);
        logout.setOnClickListener(this);

        send = findViewById(R.id.send);
        send.setEnabled(false);
        send.setOnClickListener(this);

        message = findViewById(R.id.message);
        message.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if (view.getId() == R.id.logout_from_chat) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.send){

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