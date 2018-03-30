package srdjan.usorac.chatapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    Button register_now;
    EditText username,password,email;
    DatePicker date_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_now = findViewById(R.id.register_register);

        register_now.setEnabled(false);
        register_now.setOnClickListener(this);

        username = (EditText) findViewById(R.id.username_reg);
        password = (EditText) findViewById(R.id.password_reg);
        email = (EditText) findViewById(R.id.email_reg);

        username.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);

        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);

        date_picker = (DatePicker) findViewById(R.id.date_picker);
        Date time = Calendar.getInstance().getTime();
        date_picker.setMaxDate(time.getTime());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_register) {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (username.getText().toString().length() == 0){

            username.setError("Mandatory field");

        }
        if (password.getText().toString().length() == 0){

            password.setError("Mandatory (min 6 characters)");

        }
        if (email.getText().toString().length() == 0){

            email.setError("Mandatory field");

        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String mail = email.getText().toString();


        if (username.getText().toString().length() > 0){
            if (password.getText().toString().length() >= 6) {
                if (mail.length() !=0 && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {

                    register_now.setEnabled(true);

                }
                else {
                    register_now.setEnabled(false);
                }
            }
            else {
                register_now.setEnabled(false);
            }
        }
        else {
            register_now.setEnabled(false);
        }

    }
}
