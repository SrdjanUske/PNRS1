package srdjan.usorac.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts.db";
    public static final int DATABASE_VERSION = 1;

    // table of contacts on my app
    public static final String TABLE_NAME = "contacts_table";
    public static String CONTACT_ID = "ContactID";
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Contact createContact(Cursor cursor) {
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
        String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));

        return new Contact(userName, firstName, lastName);
    }

    public void insertContact(Contact student) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, student.getUserName());
        values.put(COLUMN_FIRST_NAME, student.getFirstName());
        values.put(COLUMN_LAST_NAME, student.getLastName());

        db.insert(TABLE_NAME, null, values);
        close();
    }

    public Contact[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, CONTACT_ID + "=?", new String[] {String.valueOf(id)});
        close();
    }

}
