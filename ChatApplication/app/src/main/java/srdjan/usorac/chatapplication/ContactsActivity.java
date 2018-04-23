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
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        chat = findViewById(R.id.imageView);

        adapter = new CharacterAdapter(this);

        /*adapter.addCharacter(new Custom((Character)getString(R.string.jovana_milosevic).charAt(0),
                                        getString(R.string.jovana_milosevic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.marko_markovic).charAt(0),
                                        getString(R.string.marko_markovic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.srdjan_usorac).charAt(0),
                                        getString(R.string.srdjan_usorac),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.igor_ilic).charAt(0),
                                        getString(R.string.igor_ilic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.savo_dragovic).charAt(0),
                                        getString(R.string.savo_dragovic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.moja_mama).charAt(0),
                                        getString(R.string.moja_mama),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.ruzica_mitric).charAt(0),
                                        getString(R.string.ruzica_mitric),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.nikola_percevic).charAt(0),
                                        getString(R.string.nikola_percevic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
        adapter.addCharacter(new Custom((Character)getString(R.string.vladimir_spasojevic).charAt(0),
                                        getString(R.string.vladimir_spasojevic),
                                        getResources().getDrawable(R.drawable.new_messages_red)));
*/
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
                    /*preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(contact.);*/

                    adapter.addCharacter(new Custom(contact.getFirstName().charAt(0),
                            contact.getFirstName() + " " + contact.getLastName(),
                            getResources().getDrawable(R.drawable.new_messages_red)));
                }
            }
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        Contact[] contacts = mDbHelper.readContacts();
        Custom[] custom = new Custom[contacts.length]

        for (int i = 0; i < custom.length; i++) {
            custom[i].setImage(
                    getResources().getDrawable(R.drawable.new_messages_red));
            custom[i].setName(
                    contacts[i].getFirstName() + " " + contacts[i].getLastName());
            custom[i].setFirst_letter(
                    custom[i].getName().charAt(0));
        }

        adapter.update(custom);
    }*/

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
