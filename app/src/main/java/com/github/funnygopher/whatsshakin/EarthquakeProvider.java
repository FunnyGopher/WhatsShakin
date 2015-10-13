package com.github.funnygopher.whatsshakin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class EarthquakeProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://gov.usgs.earthquake/earthquakes");

    // Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MAGNITUDE = "magnitude";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static final class EarthquakeDatabaseHelper extends SQLiteOpenHelper {

        private static final String TAG = "EarthquakeProvider";
        private static final String DATABASE_NAME = "earthquakes.db";
        private static final int DATABASE_VERSION = 1;
        private static final String EARTHQUAKE_TABLE = "earthquakes";
        private static final String DATABASE_CREATE = "CREATE TABLE" + EARTHQUAKE_TABLE + " (" +
                " integer primary key autoincrement, ";

        public EarthquakeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
