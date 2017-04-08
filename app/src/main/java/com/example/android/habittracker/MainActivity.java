package com.example.android.habittracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {

    //Variable used to access HabitDbHelper
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup floating action button to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // Helper method to display database information
    private void displayDatabaseInfo() {

        // Instantiate subclass of SQLiteOpenHelper to access database and pass the context
        // of current activity.
        mDbHelper = new HabitDbHelper(this);

        Cursor mCursor = mDbHelper.readData();

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            // Create a header in database display TextView
            displayView.setText("The habits table contains " + mCursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_DATE + " - " +
                    HabitEntry.COLUMN_HABIT_HABIT + " - " +
                    HabitEntry.COLUMN_HABIT_DURATION + "\n");

            // Figure out the index of each column
            int idColumnIndex = mCursor.getColumnIndex(HabitEntry._ID);
            int dateColumnIndex = mCursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DATE);
            int habitColumnIndex = mCursor.getColumnIndex(HabitEntry.COLUMN_HABIT_HABIT);
            int durationColumnIndex = mCursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DURATION);

            // Iterate through all the returned rows in the cursor
            while (mCursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = mCursor.getInt(idColumnIndex);
                String currentDate = mCursor.getString(dateColumnIndex);
                String currentHabit = mCursor.getString(habitColumnIndex);
                int currentDuration = mCursor.getInt(durationColumnIndex);
                // Display the values from each column of the current row in the cursor
                // in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentDate + " - " +
                        currentHabit + " - " +
                        currentDuration + " minutes"));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            mCursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_activity.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDbHelper = new HabitDbHelper(this);

        // User clicked on a menu option in the app bar overflow menu
        if (item.getItemId() == R.id.action_insert_dummy_data) {
            mDbHelper.insertDummyHabit();
            displayDatabaseInfo();
        }
        return super.onOptionsItemSelected(item);
    }
}