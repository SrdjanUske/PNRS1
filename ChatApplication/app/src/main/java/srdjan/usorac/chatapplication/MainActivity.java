package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, View.OnLongClickListener {

    Button login_button, register_button;
    EditText username, password;
    private DbHelper mDbHelper;
    private Contact[] contacts;
    SharedPreferences preferences;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = findViewById(R.id.login);
        register_button = findViewById(R.id.register);

        imageView = (ImageView) findViewById(R.id.passwordView);
        imageView.setLongClickable(true);

        register_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        login_button.setEnabled(false);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        password.setOnFocusChangeListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

        mDbHelper = DbHelper.getInstance(this);
        contacts = mDbHelper.readContacts();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.login) {
            boolean exist = false;

            if (contacts == null) {

                Toast.makeText(getApplicationContext(), "Username not recognized!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "If you're not register, click REGISTER button!", Toast.LENGTH_SHORT).show();

            } else {

                for (int i = 0; i < contacts.length; i++) {
                    if (contacts[i].getUserName().equals(username.getText().toString())) {
                        exist = true;

                        preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("senderID", contacts[i].getmID());
                        editor.commit();

                        Intent intent = new Intent(this, ContactsActivity.class);
                        intent.putExtra("UserName", contacts[i].getUserName());
                        startActivity(intent);
                    }
                }
                if (!exist) {
                    Toast.makeText(getApplicationContext(), "Username not recognized!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "If you're not register, click REGISTER button!", Toast.LENGTH_SHORT).show();
                }
            }
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
        if (password.getText().toString().length() > 0) {
            imageView.setOnLongClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.passwordView) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        return true;
    }
}
