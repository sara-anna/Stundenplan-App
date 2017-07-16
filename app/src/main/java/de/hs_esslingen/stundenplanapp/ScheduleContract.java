package de.hs_esslingen.stundenplanapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by admin on 08.06.2017.
 */

public final class ScheduleContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ScheduleContract() {}

    // Inner class that defines the table contents
    public static class TableLecture implements BaseColumns {
        public static final String TABLE_NAME = "lecture";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LECTURER = "lecturer";
        public static final String COLUMN_NAME_ROOM = "room";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "dayOfWeek";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CANCELLED_ON = "cancelledOn";
        public static final String COLUMN_NAME_COURSE_OF_STUDIES = "courseOfStudies";
        public static final String COLUMN_NAME_SEMESTER = "semester";
        public static final String COLUMN_NAME_STUDENT_TAKES_COURSE = "studentTakesCourse";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_NAME + " TEXT," +
                        COLUMN_NAME_LECTURER + " TEXT," +
                        COLUMN_NAME_ROOM + " TEXT," +
                        COLUMN_NAME_DAY_OF_WEEK + " TEXT," +
                        COLUMN_NAME_TIME + " TEXT," +
                        COLUMN_NAME_CANCELLED_ON + " TEXT," +
                        COLUMN_NAME_COURSE_OF_STUDIES + " TEXT," +
                        COLUMN_NAME_SEMESTER + " INTEGER," +
                        COLUMN_NAME_STUDENT_TAKES_COURSE + " INTEGER)";

        public static final String SQL_INSERT_INIT_ENTRIES =
                "INSERT INTO " + TABLE_NAME + " " +
                        "VALUES (0, 'Mathematik 1 A', 'Clincy', 'F 01.015', 'Mo', '09:30 - 12:45', '', 'SWB', 1,  1), " +
                        "(1, 'Informationstechnik', 'Zimmermann', 'F 01.-109', 'Mo', '14:00 - 17:15', '', 'SWB', 2, 0), " +
                        "(2, 'Betriebssysteme', 'Weber', 'F 01.-110', 'Mi', '09:30 - 12:45', '', 'SWB', 2,  1), " +
                        "(3, 'Labor Elektrotechnik', 'Hehl', 'F 01.409', 'Mo', '07:35 - 11:00', '', 'TIB', 2, 0), " +
                        "(4, 'Physik 2 Tutorium', 'Grösch ', 'F 02.227', 'Fr', '14:00 - 17:15', '', 'TIB', 2, 0), " +
                        "(5, 'Spezielle BWL 1', 'Krause', 'F 01.217', 'Mo', '09:30 - 12:45', '', 'WKB', 6, 0), " +
                        "(6, 'Informationssysteme', 'Schoop', 'F 01.410', 'Mi', '09:30 - 12:45', '', 'WKB', 6, 0), " +
                        "(7, 'Spezielle BWL 2', 'Walter-Seifart', 'F 01.309', 'Fr', '07:35 - 12:45', '', 'WKB', 6, 0)," +
                        "(8, 'Test Test Test', 'Bla', 'F 01.410', 'Mi', '09:30 - 11:00', '', 'TIB', 4, 0), " +
                        "(9, 'Testveranstaltung', 'Mustermann', 'F 01.409', 'Mi', '07:35 - 09:05', '', 'TIB', 4, 0)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
