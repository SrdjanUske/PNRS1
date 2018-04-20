package rtrk.pnrs.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StudentsProvider extends ContentProvider {

    public static final String AUTHORITY = "rtrk.pnrs.contentprovider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
            StudentDbHelper.TABLE_NAME);

    public static final String CONTENT_TYPE =
        "vnd.android.cursor.dir/vnd.rtrk.pnrs.contentprovider.student";
    public static final String CONTENT_ITEM_TYPE =
        "vnd.android.cursor.item/vnd.rtrk.pnrs.contentprovider.student";

    private static final int STUDENT = 1;
    private static final int STUDENT_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, StudentDbHelper.TABLE_NAME, STUDENT);
        sUriMatcher.addURI(AUTHORITY, StudentDbHelper.TABLE_NAME + "/#", STUDENT_ID);
    }

    private StudentDbHelper mHelper = null;

    private int delete(String selection, String[] selectionArgs) {
        int deleted = 0;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        deleted = db.delete(StudentDbHelper.TABLE_NAME, selection, selectionArgs);
        mHelper.close();

        return deleted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted = 0;

        switch (sUriMatcher.match(uri)) {
            case STUDENT:
                deleted = delete(selection, selectionArgs);
                break;
            case STUDENT_ID:
                deleted = delete("_id=?", new String[] {uri.getLastPathSegment()});
                break;
            default:
        }

        if (deleted > 0 ) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        String type = null;

        switch (sUriMatcher.match(uri)) {
            case STUDENT:
                type = CONTENT_TYPE;
                break;
            case STUDENT_ID:
                type = CONTENT_ITEM_TYPE;
                break;
            default:
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(StudentDbHelper.TABLE_NAME, null, values);
        mHelper.close();

        if (id != -1) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return Uri.withAppendedPath(uri, Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        mHelper = new StudentDbHelper(getContext());
        return true;
    }

    private Cursor query(String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.query(StudentDbHelper.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case STUDENT:
                cursor = query(projection, selection, selectionArgs, sortOrder);
                break;
            case STUDENT_ID:
                cursor = query(projection, "_id=?",
                        new String[]{uri.getLastPathSegment()}, null);
                break;
            default:
        }

        return cursor;
    }

    private int update(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updated = db.update(StudentDbHelper.TABLE_NAME, values, selection,
                selectionArgs);
        mHelper.close();

        return updated;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int updated = 0;

        switch (sUriMatcher.match(uri)) {
            case STUDENT:
                updated = update(values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                updated = update(values, "_id=?", new String[]{uri.getLastPathSegment()});
                break;
            default:
        }

        if (updated > 0) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return updated;
    }
}
