package com.benlewis.vitamintrackerapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating the database
        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("sql_lite_test", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("DROP TABLE test");
        sqLiteDatabase.execSQL("CREATE TABLE test(id INTEGER, name TEXT)");

        sqLiteDatabase.execSQL("INSERT INTO test VALUES('23','tim');");
        sqLiteDatabase.execSQL("INSERT INTO test VALUES('46','jason');");

        //Show the data
        Cursor query = sqLiteDatabase.rawQuery("SELECT * from test", null);

        //Loop through the records as long as there is a first record.
        //Then if there is a 'next' record keep looping until no
        //next record is found as it returns false.
        if(query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                String name = query.getString(1);

                Toast.makeText(getBaseContext(), "ID = " + id + " name = " + name, Toast.LENGTH_LONG).show();
            } while (query.moveToNext());
        } else {
            Toast.makeText(getBaseContext(), "Error getting data", Toast.LENGTH_LONG).show();
        }

        //Finished with the database
        sqLiteDatabase.close();
    }
}
