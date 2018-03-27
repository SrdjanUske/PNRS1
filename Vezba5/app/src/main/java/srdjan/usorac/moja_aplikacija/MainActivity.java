package srdjan.usorac.moja_aplikacija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    ArrayAdapter<String> mAdapter;
    ListView list;
    Button button;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list_view_id);
        button = (Button) findViewById(R.id.button_id);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        edit = (EditText) findViewById(R.id.edit_id);
        edit.addTextChangedListener(this);

        button.setEnabled(false);
        button.setOnClickListener(this);

        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_id) {
            String item = edit.getText().toString();
            mAdapter.add(item);
            edit.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (edit.getText().toString().length() > 0) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (edit.getText().toString().length() > 0) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (edit.getText().toString().length() > 0) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           // Intent intent = new Intent(this, ListActivity.class);
          //  startActivity(intent);
            mAdapter.remove(mAdapter.getItem(i));
    }
}
