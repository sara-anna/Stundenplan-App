package de.hs_esslingen.stundenplanapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.style.Widget;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyScheduleFragment.OnFragmentInteractionListener,
        SemesterListFragment.OnFragmentInteractionListener,
        SemesterScheduleFragment.OnFragmentInteractionListener,
        LectureDetailsFragment.OnFragmentInteractionListener{

    // Instances of DbHelper and database itself
    ScheduleDbHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Test, ob die App frisch gestartet wurde.
        // Verhindert ein Neuladen des initial fragments beim Drehen des Geräts.
        if(null == savedInstanceState) {
            // Initial fragment
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = MyScheduleFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // initialize DbHelper and get the database
        mDbHelper = new ScheduleDbHelper(this);
        db = mDbHelper.getReadableDatabase();
    }

    // Hide the three dots options menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle option item clicks here.
        int id = item.getItemId();

        //If the "belegen/abmelden" button is pressed
        if (id == R.id.action_take_course) {

            // Prepare the text for notification
            CharSequence notificationText = "";

            // Get the currently displayed fragment instance
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
            if (f instanceof LectureDetailsFragment){
                LectureDetailsFragment currentFragment = (LectureDetailsFragment) f;
                int lectureId = currentFragment.getLectureId();

                String[] projection = {
                        ScheduleContract.TableLecture._ID,
                        ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE
                };

                // Filter results WHERE "title" = 'My Title'
                String selection = ScheduleContract.TableLecture._ID + " = ?";
                String[] selectionArgs = {String.valueOf(lectureId)};

                Cursor cursor = db.query(
                        ScheduleContract.TableLecture.TABLE_NAME,// The table to query
                        projection,                              // The columns to return
                        selection,                               // The columns for the WHERE clause
                        selectionArgs,                           // The values for the WHERE clause
                        null,                                    // don't group the rows
                        null,                                    // don't filter by row groups
                        null                                     // don't sort
                );

                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        // Check if course is taken by student
                        int lectureIsTaken = cursor.getInt(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE));

                        if (lectureIsTaken == 0)
                        {
                            String strFilter = "_id=" + lectureId;
                            ContentValues args = new ContentValues();
                            args.put(ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE, 1);
                            db.update(ScheduleContract.TableLecture.TABLE_NAME, args, strFilter, null);
                            notificationText = "Vorlesung ist jetzt belegt.";
                        }
                        else if (lectureIsTaken == 1)
                        {
                            String strFilter = "_id=" + lectureId;
                            ContentValues args = new ContentValues();
                            args.put(ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE, 0);
                            db.update(ScheduleContract.TableLecture.TABLE_NAME, args, strFilter, null);
                            notificationText = "Vorlesung ist jetzt abgemeldet.";
                        }
                    }
                }

                // Reload fragment to see changes
                currentFragment.getFragmentManager().beginTransaction().detach(currentFragment).commit();
                currentFragment.getFragmentManager().beginTransaction().attach(currentFragment).commit();

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, notificationText, duration);
                toast.show();
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_my_schedule) {
            fragment = MyScheduleFragment.newInstance(null, null);
        } else if (id == R.id.subitem_swb) {
            fragment = SemesterListFragment.newInstance("SWB");
        } else if (id == R.id.subitem_tib) {
            fragment = SemesterListFragment.newInstance("TIB");
        } else if (id == R.id.subitem_wkb) {
            fragment = SemesterListFragment.newInstance("WKB");
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void queryDbAndaddDbEntriesAsButtonToView(String[] projection, String selection, String[] selectionArgs){
        // Query the database
        Cursor cursor = db.query(
                ScheduleContract.TableLecture.TABLE_NAME, // The table to query
                projection,                               // The columns to return
                selection,                             // The columns for the WHERE clause
                selectionArgs,                        // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        // Loop through all found entries
        while(cursor.moveToNext()) {
            // Store the lecture Id in a variable
            final long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture._ID));
            // Store the lecture time in a variable
            String itemTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_TIME));
            // Store the lecture day in a variable
            String itemDay = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_DAY_OF_WEEK));
            // Store the lecture desctription in a variable
            String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_NAME));
            // Store the lecture lecturer in a variable
            String itemLecturer = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_LECTURER));

            // Parse time to startTime and endTime
            String[] times = itemTime.split(" - ");
            String startTime = times[0];
            String endTime = times[1];

            // Initialize a new Button. This Button will represent a lecture in the schedule
            Button btnLecture = new Button(this);

            // Set Id of Lecture as Id of Button
            btnLecture.setId((int) itemId);
            // Set the text of Lecture as Text of Button
            btnLecture.setText(itemTime + "\n\n" + itemDescription + "\n" + itemLecturer);
            btnLecture.setAllCaps(false);
            btnLecture.setTextSize(11);

            // Calculate top margin of Lecture Element depending on the start time
            int topMarginToSet = calculateMarginFromTime(startTime);
            // Set top margin of Lecture Element
            RelativeLayout.LayoutParams paramsOfButton = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsOfButton.setMargins(0,topMarginToSet,0,0);
            btnLecture.setLayoutParams(paramsOfButton);
            btnLecture.requestLayout();

            // Calculate height of Lecture Element depending on the start time
            int heightToSet = 0;
            heightToSet = calculateHeightFromTimes(startTime, endTime);
            // Set height of Lecture Element
            btnLecture.setHeight(heightToSet);

            // Add onClickListener to Button
            btnLecture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newFragment = LectureDetailsFragment.newInstance((int) itemId);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, newFragment)
                            .addToBackStack(null).commit();
                }
            });

            // Select the right RelativeView representing a day of week to put the lecture into
            RelativeLayout mainLayout = calculateHorizontalPositionOfLectureButton(itemDay);
            paramsOfButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            // Add the button for the lecture at the right place in the view
            mainLayout.addView(btnLecture, paramsOfButton);
        }
        cursor.close();
    }

    private RelativeLayout calculateHorizontalPositionOfLectureButton(String day){
        RelativeLayout relativeLayoutElement = new RelativeLayout(this);
        switch (day) {
            case "Mo":
                relativeLayoutElement = (RelativeLayout) findViewById(R.id.mondayRelativeLayout);
                break;
            case "Di":
                relativeLayoutElement = (RelativeLayout) findViewById(R.id.tuesdayRelativeLayout);
                break;
            case "Mi":
                relativeLayoutElement = (RelativeLayout) findViewById(R.id.wednesdayRelativeLayout);
                break;
            case "Do":
                relativeLayoutElement = (RelativeLayout) findViewById(R.id.thursdayRelativeLayout);
                break;
            case "Fr":
                relativeLayoutElement = (RelativeLayout) findViewById(R.id.fridayRelativeLayout);
                break;
        }
        return relativeLayoutElement;
    }

    private int calculateMarginFromTime(String time){
        int margin = 0;

        if (time.equals("07:35")) {
            margin = (int) (findViewById(R.id.textView_ersteStunde)).getTop();
        }
        else if(time.equals("09:30")){
            margin = (int) (findViewById(R.id.textView_zweiteStunde)).getTop();
        }
        else if(time.equals("11:15")){
            margin = (int) (findViewById(R.id.textView_dritteStunde)).getTop();
        }
        else if(time.equals("14:00")){
            margin = (int) (findViewById(R.id.textView_vierteStunde)).getTop();
        }
        else if(time.equals("15:45")){
            margin = (int) (findViewById(R.id.textView_fuenfteStunde)).getTop();
        }
        return margin;
    }

    private int calculateHeightFromTimes (String startTime, String endTime) {

        // Get the position of the start time
        int yPositionStartTime = 0;

        if (startTime.equals("07:35")) {
            yPositionStartTime = (int) (findViewById(R.id.textView_ersteStunde)).getTop();
        }
        else if(startTime.equals("09:30")){
            yPositionStartTime = (int) (findViewById(R.id.textView_zweiteStunde)).getTop();
        }
        else if(startTime.equals("11:15")){
            yPositionStartTime = (int) (findViewById(R.id.textView_dritteStunde)).getTop();
        }
        else if(startTime.equals("14:00")){
            yPositionStartTime = (int) (findViewById(R.id.textView_vierteStunde)).getTop();
        }
        else if(startTime.equals("15:45")){
            yPositionStartTime = (int) (findViewById(R.id.textView_fuenfteStunde)).getTop();
        }

        // Get the position of the end time
        int yPositionEndTime = 0;
        if (endTime.equals("09:05")) {
            yPositionEndTime = (int) (findViewById(R.id.textView_zweiteStunde)).getTop();
        }
        else if(endTime.equals("11:00")){
            yPositionEndTime = (int) (findViewById(R.id.textView_dritteStunde)).getTop();
        }
        else if(endTime.equals("12:45")){
            yPositionEndTime = (int) (findViewById(R.id.textView_Mittagspause)).getTop();
        }
        else if(endTime.equals("15:30")){
            yPositionEndTime = (int) (findViewById(R.id.textView_fuenfteStunde)).getTop();
        }
        else if(endTime.equals("17:15")){
            yPositionEndTime = (int) (findViewById(R.id.textView_fuenfteStunde)).getTop()+
                    (int) (findViewById(R.id.textView_fuenfteStunde)).getHeight()+
                    (int) ((ConstraintLayout.LayoutParams)(findViewById(R.id.textView_fuenfteStunde)).getLayoutParams()).bottomMargin;
        }

        int height = yPositionEndTime - yPositionStartTime;
        return height;
    }



}
