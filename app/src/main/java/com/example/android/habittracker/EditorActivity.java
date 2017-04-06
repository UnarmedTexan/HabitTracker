package com.example.android.habittracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Mark on 4/2/2017.
 */

public class EditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // EditText field to enter the duration of the habit activity
    private EditText mDurationEditText;

    // Spinner to select the type of habit activity
    private Spinner mHabitSpinner;

    // TextView field used to display date selected from DatePicker
    private TextView mDateText;

    // String to store the entry from EditText mDurationEditText.
    private String durationString;

    /**
     * Actual habit activity being performed.
     * Only possible values in the HabitContract.java file: {@link HabitEntry#HABIT_UNKNOWN},
     * {@link HabitEntry#HABIT_EXERCISE}, {@link HabitEntry#HABIT_GOAL_TRACKING},
     * {@link HabitEntry#HABIT_MEDITATE}, {@link HabitEntry#HABIT_NETWORKING},
     * {@link HabitEntry#HABIT_SELF_DEVELOPMENT}, or {@link HabitEntry#HABIT_WATCHING_TV}.
     */
    private String mHabitType = HabitEntry.HABIT_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mHabitSpinner = (Spinner) findViewById(R.id.spinner_habit);
        mDurationEditText = (EditText) findViewById(R.id.edit_habit_duration);
        mDateText = (TextView) findViewById(R.id.habit_date);

        setupSpinner();
    }

    // Setup the dropdown spinner that allows the user to select the type of habit.
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter habitSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_habit_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        habitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mHabitSpinner.setAdapter(habitSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mHabitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.habit_exercise))) {
                        mHabitType = HabitEntry.HABIT_EXERCISE;
                    } else if (selection.equals(getString(R.string.habit_goalTracking))) {
                        mHabitType = HabitEntry.HABIT_GOAL_TRACKING;
                    } else if (selection.equals(getString(R.string.habit_meditate))) {
                        mHabitType = HabitEntry.HABIT_MEDITATE;
                    } else if (selection.equals(getString(R.string.habit_networking))) {
                        mHabitType = HabitEntry.HABIT_NETWORKING;
                    } else if (selection.equals(getString(R.string.habit_selfDevelopment))) {
                        mHabitType = HabitEntry.HABIT_SELF_DEVELOPMENT;
                    } else if (selection.equals(getString(R.string.habit_watchingTV))) {
                        mHabitType = HabitEntry.HABIT_WATCHING_TV;
                    } else {
                        mHabitType = HabitEntry.HABIT_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mHabitType = HabitEntry.HABIT_UNKNOWN;
            }
        });
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the calendar date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // format the date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String formattedDate = sdf.format(c.getTime());

        // Set the selected habit activity date to the TextView "habit_date"
        mDateText.setText(formattedDate);
    }

    // Verify habit duration has been entered.
    private boolean durationEntered() {
        // Retrieve the entry from the EditText field for duration and store in String.
        durationString = mDurationEditText.getText().toString().trim();

        // Check string to verify user has made an entry.
        if (durationString.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // Get user input from editor and save new habits to database.
    private void insertHabit() {

        // Retrieve date input and convert to a string.
        String dateString = mDateText.getText().toString();

        // Convert duration string to an int.
        int duration = Integer.parseInt(durationString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_DATE, dateString);
        values.put(HabitEntry.COLUMN_HABIT_HABIT, mHabitType);
        values.put(HabitEntry.COLUMN_HABIT_DURATION, duration);

        // Insert a new row for a habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if (item.getItemId() == R.id.action_save) {

            // Warn user that no entry has been made for duration
            if (!durationEntered()) {
                Toast.makeText(EditorActivity.this, getString(R.string.no_duration_entered),
                        Toast.LENGTH_SHORT).show();
            } else {
                insertHabit();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}