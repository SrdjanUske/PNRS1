package rtrk.pnrs.adapterexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharacterAdapter adapter = new CharacterAdapter(this);
        adapter.addCharacter(new Character(getString(R.string.stan),
                getResources().getDrawable(R.drawable.stan_marsh)));
        adapter.addCharacter(new Character(getString(R.string.kyle),
                getResources().getDrawable(R.drawable.kyle_broflovski)));
        adapter.addCharacter(new Character(getString(R.string.eric),
                getResources().getDrawable(R.drawable.eric_cartman)));
        adapter.addCharacter(new Character(getString(R.string.kenny),
                getResources().getDrawable(R.drawable.kenny_mccormick)));
        adapter.addCharacter(new Character(getString(R.string.ike),
                getResources().getDrawable(R.drawable.ike)));
        adapter.addCharacter(new Character(getString(R.string.jimmy),
                getResources().getDrawable(R.drawable.jimmy_valmer)));
        adapter.addCharacter(new Character(getString(R.string.butters),
                getResources().getDrawable(R.drawable.butters_stotch)));
        adapter.addCharacter(new Character(getString(R.string.mr_garrison),
                getResources().getDrawable(R.drawable.mr_garrison)));
        adapter.addCharacter(new Character(getString(R.string.mr_mackey),
                getResources().getDrawable(R.drawable.mr_mackey)));
        adapter.addCharacter(new Character(getString(R.string.randy),
                getResources().getDrawable(R.drawable.randy_marsh)));
        adapter.addCharacter(new Character(getString(R.string.timmy),
                getResources().getDrawable(R.drawable.timmy)));

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }
}