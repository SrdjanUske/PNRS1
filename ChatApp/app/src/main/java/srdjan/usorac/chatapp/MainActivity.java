package srdjan.usorac.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class  MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    public static final String LOGIN_URL = HttpHelper.BASE_URL + "/login";

    Button login_button, register_button;
    EditText username, password;
    SharedPreferences preferences;
    private Handler handler;
    private HttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = findViewById(R.id.login);
        register_button = findViewById(R.id.register);

        register_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        login_button.setEnabled(false);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        password.setOnFocusChangeListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

        handler = new Handler();
        httpHelper = new HttpHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, NotificationsService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, NotificationsService.class));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.login) {
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HttpHelper.USERNAME, username.getText().toString());
                        jsonObject.put(HttpHelper.PASSWORD, password.getText().toString());

                        final HttpHelper.Responce success = httpHelper.postJSONObjectFromURL(MainActivity.LOGIN_URL, jsonObject);

                        if (success.responceCode == HttpHelper.SUCCESS) {
                            handler.post(new Runnable(){
                                public void run() {
                                        Toast.makeText(MainActivity.this, "Login: " + success.responceMessage, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);

                                        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("sessionID", success.sessionID);
                                        editor.putString("logged_in", username.getText().toString());
                                        editor.commit();
                                        startActivity(intent);
                                }
                            });
                        }
                        else {
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(MainActivity.this, "ERROR: username or password wrong!", Toast.LENGTH_LONG).show();
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
        if (password.getText().toString().length() < 6) {
            password.setError("Password min 6 characters");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (username.getText().toString().length() > 0 && password.getText().toString().length() >= 6) {
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
