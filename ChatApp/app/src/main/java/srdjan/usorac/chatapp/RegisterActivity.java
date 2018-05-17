package srdjan.usorac.chatapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private Button register_now;
    private EditText username, password, email, firstName, lastName;
    private DatePicker date_picker;
    //private DbHelper mDbHelper;
    private Contact contact;
    private Handler handler;
    private HttpHelper httpHelper;
    public static String REGISTER_URL = HttpHelper.BASE_URL + "/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_now = findViewById(R.id.register_register);

        register_now.setEnabled(false);
        register_now.setOnClickListener(this);

        username = (EditText) findViewById(R.id.username_reg);
        password = (EditText) findViewById(R.id.password_reg);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email_reg);

        username.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);

        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        firstName.addTextChangedListener(this);
        lastName.addTextChangedListener(this);
        email.addTextChangedListener(this);

        date_picker = (DatePicker) findViewById(R.id.date_picker);
        Date time = Calendar.getInstance().getTime();
        date_picker.setMaxDate(time.getTime());

        //mDbHelper = DbHelper.getInstance(this);
        handler = new Handler();
        httpHelper = new HttpHelper();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_register) {

            /*contact = new Contact(0, username.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString());

            mDbHelper.insertContact(contact);*/
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HttpHelper.USERNAME, username.getText().toString());
                        jsonObject.put(HttpHelper.PASSWORD, password.getText().toString());
                        jsonObject.put(HttpHelper.EMAIL, email.getText().toString());

                        final HttpHelper.Responce success = httpHelper.postJSONObjectFromURL(RegisterActivity.REGISTER_URL, jsonObject);

                        if (success.responceCode == HttpHelper.SUCCESS) {
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "Added new contact!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "ERROR " + success.responceCode + ": " + success.responceMessage, Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (username.getText().toString().length() == 0){
            username.setError("Mandatory field !!!");
        }
        if (password.getText().toString().length() == 0){
            password.setError("Mandatory (min 6 characters) !!!");
        }
        if (email.getText().toString().length() == 0){
            email.setError("Mandatory field !!!");
        }
        if (firstName.getText().toString().length() == 0){
            firstName.setError("Mandatory field !!!");
        }
        if (lastName.getText().toString().length() == 0){
            lastName.setError("Mandatory field !!!");
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
                    if (firstName.getText().toString().length() > 0) {
                        if (lastName.getText().toString().length() > 0) {
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
            else {
                register_now.setEnabled(false);
            }
        }
        else {
            register_now.setEnabled(false);
        }

    }
}
