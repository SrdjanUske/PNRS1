package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        CharacterAdapter adapter = new CharacterAdapter(this);
        adapter.addCharacter(new Custom((Character)getString(R.string.savo_dragovic).charAt(0),
                                        getString(R.string.savo_dragovic),
                                        getResources().getDrawable(R.drawable.eric_cartman)));
        adapter.addCharacter(new Custom((Character)getString(R.string.marko_markovic).charAt(0),
                                        getString(R.string.marko_markovic),
                                        getResources().getDrawable(R.drawable.eric_cartman)));
        adapter.addCharacter(new Custom((Character)getString(R.string.srdjan_usorac).charAt(0),
                                        getString(R.string.srdjan_usorac),
                                        getResources().getDrawable(R.drawable.eric_cartman)));

        ListView list = (ListView) findViewById(R.id.contacts_list);
        list.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        if (view.getId() == R.id.logout) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
