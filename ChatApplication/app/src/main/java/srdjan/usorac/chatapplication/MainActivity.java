package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    Button login_button, register_button;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = findViewById(R.id.login);
        register_button = findViewById(R.id.register);

        register_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        login_button.setEnabled(false);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        password.setOnFocusChangeListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.login) {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (password.getText().toString().length() < 6) {
            password.setError("Password min 6 characters");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (username.getText().toString().length() > 0 && password.getText().toString().length()>=6) {
            login_button.setEnabled(true);
        }
        else {
            login_button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}