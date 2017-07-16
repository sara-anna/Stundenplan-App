package de.hs_esslingen.stundenplanapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 13.06.2017.
 */

public class LSFParser extends AsyncTask<String, Void, Void> {
    private String url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=AA8DD71E60E3181B18A524565B49F512?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=272&k_abstgv.abstgvnr=1005&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
    private ArrayList<LectureEntity> lectureEntities = new ArrayList<LectureEntity>();

    // Instances of DbHelper and database itself
    private ScheduleDbHelper mDbHelper;
    private SQLiteDatabase db;

    private Context mContext;

    public LSFParser (Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String url = params[0];
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        try {
            // Connect to the web site
            Document document = Jsoup.connect(url).get();
            // Find all lectures
            Elements parsedLectures = document
                    .getElementsByClass("plan2");
            for (Element parsedLecture : parsedLectures) {
                //lectureEntities.add(new LectureEntity(
                String name =  parsedLecture.getElementsByClass("klein").first().text()
                        .substring(5, parsedLecture.getElementsByClass("klein").first().text().length());
                Element lecturerElement = parsedLecture.getElementsByClass("notiz")
                        .select("td:contains(Dozent)").first();
                String lecturer = "";
                if (lecturerElement != null) {
                    lecturer = lecturerElement.text().substring(
                            (lecturerElement.text().indexOf("Dozent"))+ 8, lecturerElement.text().length());
                }
                String roomText = parsedLecture.getElementsByClass("ver")
                        .select("a:contains(Gebäude)").first().text();
                String room = roomText.substring(
                        (roomText.indexOf(" - "))+ 3, roomText.length());
                String time =  parsedLecture.getElementsByClass("notiz").first().text()
                        .substring(0, 13);
                String courseOfStudies = document.getElementsByTag("h4").first().text()
                        .substring(1,4);
                String semester = document.getElementsByTag("h4").first().text()
                        .substring(4,5);

                // Parse day
                String day = "";
                Element row = parsedLecture.parent();
                int i = 0;
                Elements scheduleDays = row.children();
                for (Element scheduleDay : scheduleDays) {
                    if(scheduleDays.size() == 7 && i == 0)
                    {
                        continue;
                    }
                    if (scheduleDay.text().equals(parsedLecture.text())){
                        break;
                    }
                    i++;
                }
                switch (i){
                    case 1:
                        day = "Mo";
                        break;
                    case 2:
                        day = "Di";
                        break;
                    case 3:
                        day = "Mi";
                        break;
                    case 4:
                        day = "Do";
                        break;
                    case 5:
                        day = "Fr";
                        break;
                    default:
                        break;
                }
                lectureEntities.add(new LectureEntity(
                        name, lecturer, room, day, time, "", courseOfStudies, semester, 0
                ));
            }
            System.out.print("hkj");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // initialize DbHelper and get the database
        mDbHelper = new ScheduleDbHelper(mContext);
        db = mDbHelper.getReadableDatabase();

        // Insert data in database
        ContentValues values = new ContentValues();
        for (LectureEntity lectureEntity :lectureEntities) {
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_NAME, lectureEntity.getName());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_LECTURER, lectureEntity.getLecturer());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_ROOM, lectureEntity.getRoom());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_DAY_OF_WEEK, lectureEntity.getDayOfWeek());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_TIME, lectureEntity.getTime());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_CANCELLED_ON, lectureEntity.getCancelledOn());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_COURSE_OF_STUDIES, lectureEntity.getCourseOfStudies());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_SEMESTER, lectureEntity.getSemester());
            values.put(ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE, lectureEntity.getStudentTakesCourse());
            db.insert(ScheduleContract.TableLecture.TABLE_NAME, null, values);
        }
    }
}
