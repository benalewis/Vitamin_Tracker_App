package com.benlewis.vitamintrackerapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Ben on 13/04/2016.
 */
public class VitaminProvider extends ContentProvider {
    private VitaminDatabase mOpenHelper;

    private static String TAG = VitaminProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int VITAMIN = 100;
    private static final int VITAMIN_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VitaminContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "vitamin", VITAMIN);
        matcher.addURI(authority, "vitamin/*", VITAMIN_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new VitaminDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(VitaminDatabase.Tables.VITAMIN);

        switch(match) {
            case VITAMIN:
                //do nothing
                break;
            case VITAMIN_ID:
                String id = VitaminContract.Vitamin.getVitaminId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        VitaminDatabase.deleteDatabase(getContext());
        mOpenHelper = new VitaminDatabase(getContext());
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VITAMIN:
                return VitaminContract.Vitamin.CONTENT_TYPE;
            case VITAMIN_ID:
                return VitaminContract.Vitamin.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
    

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case VITAMIN:
                //create new record, return the ID. Append the Uri path
                long recordId = db.insertOrThrow(VitaminDatabase.Tables.VITAMIN, null, values);
                return VitaminContract.Vitamin.buildVitaminUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch(match) {
            case VITAMIN:
                //do nothing
                break;

            case VITAMIN_ID:
                String id = VitaminContract.Vitamin.getVitaminId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        //Return the number of records that were updated
        return db.update(VitaminDatabase.Tables.VITAMIN, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        if(uri.equals(VitaminContract.BASE_CONTENT_URI)) {
            deleteDatabase();
            return 0;
        }

        switch(match) {
                case VITAMIN_ID:
                    String id = VitaminContract.Vitamin.getVitaminId(uri);
                    String selectionCriteria = BaseColumns._ID + "=" + id
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                    return db.delete(VitaminDatabase.Tables.VITAMIN, selectionCriteria, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
}
