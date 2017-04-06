package com.example.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Mark on 4/2/2017.
 */


// API Contract for the Habit Tracker app.
public final class HabitContract {

    // Empty constructor to prevent someone from accidentally instantiating the contract class.
    private HabitContract(){}

    /**
     * Inner class that defines constant values for the Habit Tracker database table.
     * Each entry in the table represents time spent on a single habit activity.
     */
    public static final class HabitEntry implements BaseColumns{

        // Name of the database table
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number assigned to each habit activity entry
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Date of the habit activity being performed
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_DATE = "date";

        /**
         *  Actual habit activity being performed
         *
         *  Only possible values are {@link #HABIT_UNKNOWN}, {@link #HABIT_EXERCISE},
         *  {@link #HABIT_GOAL_TRACKING}, {@link #HABIT_MEDITATE}, {@link #HABIT_NETWORKING},
         *  {@link #HABIT_SELF_DEVELOPMENT}, or {@link #HABIT_WATCHING_TV}.
         *
         *  TYPE: INTEGER
         */
        public final static String COLUMN_HABIT_HABIT = "habit";

        /**
         *  Duration of time spent performing individual habit activity.
         *
         *  TYPE: INTEGER
         */
        public final static String COLUMN_HABIT_DURATION = "duration";

        /**
         * Possible values for the habit activities.
         */
        public static final String HABIT_UNKNOWN = "Unkown";
        public static final String HABIT_EXERCISE = "Exercise";
        public static final String HABIT_GOAL_TRACKING = "Goal Tracking";
        public static final String HABIT_MEDITATE = "Meditate";
        public static final String HABIT_NETWORKING = "Networking";
        public static final String HABIT_SELF_DEVELOPMENT = "Self-Development";
        public static final String HABIT_WATCHING_TV = "Watching TV";
    }
}
