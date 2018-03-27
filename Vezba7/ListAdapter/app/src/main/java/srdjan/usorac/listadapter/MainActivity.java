package srdjan.usorac.listadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharacterAdapter adapter = new CharacterAdapter(this);
        adapter.addCharacter(new Custom(getString(R.string.yes), getResources().getDrawable(R.drawable.butters_stotch)));

        ListView list = (ListView) findViewById(R.id.list_view);
        list.setAdapter(adapter);
    }
}
