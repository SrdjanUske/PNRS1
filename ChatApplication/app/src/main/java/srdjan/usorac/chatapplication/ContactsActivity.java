package srdjan.usorac.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout;
    TextView my_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        my_friend = (TextView) findViewById(R.id.my_friend);
        my_friend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        if (view.getId() == R.id.logout) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.my_friend) {
            intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }
    }
}