package rtrk.pnrs.databaseexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private StudentDbHelper mDbHelper;
    private StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAdd = (Button)findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);

        mAdapter = new StudentAdapter(this);
        ListView list = (ListView) findViewById(R.id.list_students);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);

        mDbHelper = new StudentDbHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Student[] students = mDbHelper.readStudents();
        mAdapter.update(students);
    }

    @Override
    public void onClick(View v) {
        Student student = new Student(getInput(R.id.firstname), getInput(R.id.lastname),
                getInput(R.id.indexnumber));
        mDbHelper.insert(student);

        Student[] students = mDbHelper.readStudents();
        mAdapter.update(students);

    }

    private String getInput(int id) {
        EditText edit = (EditText) findViewById(id);
        return edit.getText().toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student student = (Student) mAdapter.getItem(position);
        mDbHelper.deleteStudent(student.getIndex());

        Student[] students = mDbHelper.readStudents();
        mAdapter.update(students);
    }
}
