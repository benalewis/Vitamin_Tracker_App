package com.benlewis.vitamintrackerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Ben on 13/04/2016.
 */
public class VitaminDatabase extends SQLiteOpenHelper {

    //Error logging name
    private static final String TAG = VitaminDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "vitamin.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    //Code to create the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the table
        db.execSQL("CREATE TABLE " + Tables.VITAMIN + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VitaminContract.VitaminColumns.VITAMIN_NAME + " TEXT NOT NULL,"
                + VitaminContract.VitaminColumns.VITAMIN_DOSE_AMOUNT + " TEXT NOT NULL,"
                + VitaminContract.VitaminColumns.VITAMIN_DOSE_MULTIPLE + " TEXT NOT NULL)");
        }

    //When adding additional functionality to a db, append
    //the database for new version

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            //add some fields for upgrades
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            //drop old db, create new one
            db.execSQL("DROP TABLE IF EXISTS " + Tables.VITAMIN);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    interface Tables {
        String VITAMIN = "vitamin";
    }

    public VitaminDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


}
