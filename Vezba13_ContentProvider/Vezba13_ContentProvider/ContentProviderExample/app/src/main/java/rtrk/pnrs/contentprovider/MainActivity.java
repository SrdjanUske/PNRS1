package rtrk.pnrs.contentprovider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Student[] students = readStudents();
        mAdapter.update(students);
    }

    @Override
    public void onClick(View v) {
        Student student = new Student(getInput(R.id.firstname), getInput(R.id.lastname),
                getInput(R.id.indexnumber));
        insert(student);

        Student[] students = readStudents();
        mAdapter.update(students);

    }

    private String getInput(int id) {
        EditText edit = (EditText) findViewById(id);
        return edit.getText().toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student student = (Student) mAdapter.getItem(position);
        deleteStudent(student.getIndex());

        Student[] students = readStudents();
        mAdapter.update(students);
    }

    private void insert(Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentDbHelper.COLUMN_FIRST_NAME, student.getFirstName());
        values.put(StudentDbHelper.COLUMN_LAST_NAME, student.getLastName());
        values.put(StudentDbHelper.COLUMN_INDEX, student.getIndex());

        ContentResolver resolver = getContentResolver();
        resolver.insert(StudentsProvider.CONTENT_URI, values);
    }

    public Student[] readStudents() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(StudentsProvider.CONTENT_URI, null, null, null,
                null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Student[] students = new Student[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            students[i++] = createStudent(cursor);
        }

        cursor.close();
        return students;
    }

    public void deleteStudent(String index) {
        ContentResolver resolver = getContentResolver();
        resolver.delete(StudentsProvider.CONTENT_URI, StudentDbHelper.COLUMN_INDEX + "=?",
                new String[] {index});
    }

    private Student createStudent(Cursor cursor) {
        String firstName = cursor.getString(cursor.getColumnIndex(
                StudentDbHelper.COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(
                StudentDbHelper.COLUMN_LAST_NAME));
        String index = cursor.getString(cursor.getColumnIndex(
                StudentDbHelper.COLUMN_INDEX));

        return new Student(firstName, lastName, index);
    }
}
