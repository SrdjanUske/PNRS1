package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout;
    private ImageView chat;
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        chat = findViewById(R.id.imageView);

        adapter = new CharacterAdapter(this);

        ListView list = (ListView) findViewById(R.id.contacts_list);
        list.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("UserName");

        DbHelper mDbHelper = DbHelper.getInstance(this);
        Contact[] contacts = mDbHelper.readContacts();

        if (contacts == null) {
            Toast.makeText(getApplicationContext(), "No contacts!", Toast.LENGTH_SHORT).show();
        }
        else {

            for (Contact contact : contacts) {
                if (!(contact.getUserName().equals(username))) {

                    adapter.addCharacter(contact);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        if (view.getId() == R.id.logout) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
