package com.example.student.zadatak1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button1 = findViewById(R.id.dugme2);
        button2 = findViewById(R.id.dugme3);
        button3 = findViewById(R.id.dugme4);
        button3.setEnabled(false);

        button1.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dugme2) {
            button2.setEnabled(false);
        }
    }
}
