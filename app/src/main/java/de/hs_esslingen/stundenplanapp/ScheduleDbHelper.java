package de.hs_esslingen.stundenplanapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 08.06.2017.
 */

public class ScheduleDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Schedule.db";

    Context context;
    public ScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScheduleContract.TableLecture.SQL_CREATE_TABLE);
        db.execSQL(ScheduleContract.TableLecture.SQL_INSERT_INIT_ENTRIES);

       /* // Call LSF parser
        String url;
        // SWB1
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=AA8DD71E60E3181B18A524565B49F512?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=272&k_abstgv.abstgvnr=1005&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);
        // SWB2
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=7D1FEC667DAA1503475FCAC1912ED5A3?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=309&k_abstgv.abstgvnr=1061&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);
        // SWB3
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=0C32A7C83F13D7FE35C165328F08C01D?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=320&k_abstgv.abstgvnr=1069&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);
        // SWB4
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=8F5B881F05B927814E76427F26BB4832?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=387&k_abstgv.abstgvnr=1156&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);
        // SWB6
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=1A9E72A4FBB58104890A7E94B0C5EABB?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=425&k_abstgv.abstgvnr=1216&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);
        // SWB7
        url = "http://www3.hs-esslingen.de/qislsf/rds;jsessionid=4078FAF6D8281EB7EEB4B1BE748BEDBE?state=wplan&r_zuordabstgv.semvonint=&r_zuordabstgv.sembisint=&missing=allTerms&k_parallel.parallelid=52&k_abstgv.abstgvnr=504&r_zuordabstgv.phaseid=&week=-20&act=stg&pool=stg&show=plan&P.vx=lang&P.Print=";
        new LSFParser(context).execute(url);*/

        /*// TIB1
        url = "";
        new LSFParser(context).execute(url);
        // TIB2
        url = "";
        new LSFParser(context).execute(url);
        // TIB3
        url = "";
        new LSFParser(context).execute(url);
        // TIB4
        url = "";
        new LSFParser(context).execute(url);
        // TIB6
        url = "";
        new LSFParser(context).execute(url);
        // TIB7
        url = "";
        new LSFParser(context).execute(url);

        // WKB1
        url = "";
        new LSFParser(context).execute(url);
        // WKB2
        url = "";
        new LSFParser(context).execute(url);
        // WKB3
        url = "";
        new LSFParser(context).execute(url);
        // WKB4
        url = "";
        new LSFParser(context).execute(url);
        // WKB6
        url = "";
        new LSFParser(context).execute(url);
        // WKB7
        url = "";
        new LSFParser(context).execute(url);*/
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ScheduleContract.TableLecture.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
