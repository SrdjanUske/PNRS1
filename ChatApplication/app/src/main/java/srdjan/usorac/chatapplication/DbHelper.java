package srdjan.usorac.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance = null;

    public static final String DATABASE_NAME = "contacts.db";
    public static final int DATABASE_VERSION = 1;

    // table of contacts on my app
    public static final String TABLE_NAME = "contacts_table";
    public static final String CONTACT_ID = "ContactID";
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";

    // table of chats with contacts
    public static final String CHAT_TABLE_NAME = "chat_table";
    public static final String MESSAGE_ID = "message_id";
    public static final String SENDER_ID = "sender_id";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String COLUMN_MESSAGE = "message";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT);" );


        db.execSQL("CREATE TABLE " + CHAT_TABLE_NAME + " (" +
                MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SENDER_ID + " INTEGER, " +
                RECEIVER_ID + " INTEGER, " +
                COLUMN_MESSAGE + " TEXT," +
                " FOREIGN KEY (" + SENDER_ID + ") REFERENCES " + DbHelper.TABLE_NAME + "(" + DbHelper.CONTACT_ID + ")," +
                " FOREIGN KEY (" + RECEIVER_ID + ") REFERENCES " + DbHelper.TABLE_NAME + "(" + DbHelper.CONTACT_ID + ")" +
                ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /* methods for contacts_table */

    private Contact createContact(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(CONTACT_ID));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
        String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));

        return new Contact(id, userName, firstName, lastName);
    }

    public void insertContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, contact.getUserName());
        values.put(COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(COLUMN_LAST_NAME, contact.getLastName());

        db.insert(TABLE_NAME, null, values);
        close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = instance.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_NAME, null,
                DbHelper.CONTACT_ID + "=?", new String[] {Integer.toString(id)},
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        db.close();

        return contact;
    }

    public Contact[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_NAME, null, null, null, null, null, null, null);

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




    /* methods for chat_table */

    private Chat createMessage(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(MESSAGE_ID));
        int senderID = cursor.getInt(cursor.getColumnIndex(SENDER_ID));
        int receiverID = cursor.getInt(cursor.getColumnIndex(RECEIVER_ID));
        String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));

        Contact sender = getContact(senderID);
        Contact receiver = getContact(receiverID);

        return new Chat(id, sender, receiver, message);
    }

    public void insertMessage(Chat chat) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SENDER_ID, chat.getmSender().getmID());
        values.put(RECEIVER_ID, chat.getmReceiver().getmID());
        values.put(COLUMN_MESSAGE, chat.getmMessage());

        db.insert(CHAT_TABLE_NAME, null, values);
        close();
    }

    public Chat[] readMessages(int ID1, int ID2) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(DbHelper.CHAT_TABLE_NAME, null,
                "(" + DbHelper.SENDER_ID + "=? AND " + DbHelper.RECEIVER_ID + "=?) OR " +
                        "(" + DbHelper.SENDER_ID + "=? AND " + DbHelper.RECEIVER_ID + "=?)",
                new String[] {Integer.toString(ID1), Integer.toString(ID2),
                        Integer.toString(ID2), Integer.toString(ID1)},
                null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Chat[] chats = new Chat[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            chats[i++] = createMessage(cursor);
        }

        close();
        return chats;
    }

    public void deleteMessage(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CHAT_TABLE_NAME, MESSAGE_ID + "=?", new String[] {String.valueOf(id)});
        close();
    }
}
