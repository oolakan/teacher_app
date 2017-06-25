/**
 * 
 */
package custom;
/**
 * @author JOSEPH
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBController extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // tasks table name
    private SQLiteDatabase dbase;
	public DBController(Context applicationcontext) {
        super(applicationcontext, Constants.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        dbase = database;
        //create class table
        String class_sql = "CREATE TABLE IF NOT EXISTS " + Constants.CLASS_TABLE + " ( "
                + Constants.KEY_AUTO_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "+
                Constants.KEY_CATEGORY + " TEXT, " +
                Constants.KEY_NAME+ " TEXT)";
        dbase.execSQL(class_sql);

        //create subject table
        String sub_sql = "CREATE TABLE IF NOT EXISTS " + Constants.SUBJECT_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Constants.KEY_ID + " TEXT, "
                + Constants.KEY_NAME+" TEXT)";
        dbase.execSQL(sub_sql);

        //create term table
        String term_sql = "CREATE TABLE IF NOT EXISTS " + Constants.TERM_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "
                +Constants.KEY_NAME + " TEXT)";
        dbase.execSQL(term_sql);

        //create day table
        String day_sql = "CREATE TABLE IF NOT EXISTS " + Constants.DAY_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "+
                Constants.KEY_NAME
                + " TEXT)";
        dbase.execSQL(day_sql);

        //create week table
        String week_sql = "CREATE TABLE IF NOT EXISTS " + Constants.WEEK_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "+
                Constants.KEY_NAME
                + " TEXT)";
        dbase.execSQL(week_sql);

        //create week table
        String activity_type_sql = "CREATE TABLE IF NOT EXISTS " + Constants.ACTIVITY_TYPE_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "+
                Constants.KEY_NAME
                + " TEXT)";
        dbase.execSQL(activity_type_sql);

        //create half term table
        String half_term_sql = "CREATE TABLE IF NOT EXISTS " + Constants.HALF_TERM_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_ID + " TEXT, "+
                Constants.KEY_HALF_TERM_SUMMARY_TITLE + " TEXT, "
                + Constants.KEY_HALF_TERM_SUMMARY_CONTENT + " TEXT, "
                +  Constants.KEY_HALF_TERM_NO + " TEXT, "
                +Constants.KEY_SUBJECT_ID +" TEXT, "
                +Constants.KEY_CLASS_ID + " TEXT, "
                +Constants.KEY_TERM_ID+ " TEXT)";
        dbase.execSQL(half_term_sql);

        //create half term table
        String report_sql = "CREATE TABLE IF NOT EXISTS " + Constants.REPORT_TABLE + " ( "

                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.STUDY_MATERIAL + " TEXT, "
                + Constants.EXPECTED_TIME + " TEXT, "
                + Constants.ACTUAL_TIME + " TEXT, "
                + Constants.KEY_SUBJECT_ID +" TEXT, "
                + Constants.KEY_CLASS_ID +" TEXT, "
                + Constants.KEY_TERM_ID+ " TEXT, "
                + Constants.KEY_DAY_ID+ " TEXT, "
                + Constants.KEY_WEEK_ID+ " TEXT, "
                + Constants.TIMESTAMP+ " TEXT )";

        dbase.execSQL(report_sql);
        //create study material table
        String study_material_sql = "CREATE TABLE IF NOT EXISTS "

                + Constants.STUDY_MATERIALS_TABLE + " ( "
                + Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.KEY_ID + " TEXT, "
                + Constants.STUDY_TITLE + " TEXT, "
                + Constants.BODY_NO + " TEXT, "
                + Constants.STUDY_NO + " TEXT, "
                + Constants.BODY_TITLE +" TEXT, "
                + Constants.BODY_CONTENT + " TEXT, "
                + Constants.BODY_TYPE + " TEXT, "
                +Constants.EXPECTED_TIME+ " TEXT, "
                +Constants.KEY_DAY_ID +" TEXT, "
                +Constants.KEY_WEEK_ID +" TEXT, "
                +Constants.KEY_TERM_ID +" TEXT, "
                +Constants.KEY_SUBJECT_ID +" TEXT, "
                +Constants.KEY_CLASS_ID + " TEXT)";

        dbase.execSQL(study_material_sql);

        //create weekly summary table
        String week_summary_sql = "CREATE TABLE IF NOT EXISTS "
                +Constants.WEEK_SUMMARIES_TABLE + " ( "
                +Constants.KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Constants.KEY_ID + " TEXT, "
                +Constants.SUMMARY_TITLE +" TEXT, "
                +Constants.SUMMARY_NO + " TEXT, "
                +Constants.SUMMARY_HEADING + " TEXT, "
                +Constants.SUMMARY_CONTENT +" TEXT, "
                +Constants.KEY_TERM_ID +" TEXT, "
                +Constants.KEY_WEEK_ID +" TEXT, "
                +Constants.KEY_CLASS_ID + " TEXT, "
                +Constants.KEY_SUBJECT_ID +" TEXT)";
        dbase.execSQL(week_summary_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String classQuery = "DROP TABLE IF EXISTS "+Constants.CLASS_TABLE;
        database.execSQL(classQuery);
        String subjectQuery = "DROP TABLE IF EXISTS "+Constants.SUBJECT_TABLE;
        database.execSQL(subjectQuery);
        String termQuery = "DROP TABLE IF EXISTS "+Constants.TERM_TABLE;
        database.execSQL(termQuery);
        String weekQuery = "DROP TABLE IF EXISTS "+Constants.WEEK_TABLE;
        database.execSQL(weekQuery);
        String dayQuery = "DROP TABLE IF EXISTS "+Constants.DAY_TABLE;
        database.execSQL(dayQuery);
        String activityQuery = "DROP TABLE IF EXISTS "+Constants.ACTIVITY_TYPE_TABLE;
        database.execSQL(activityQuery);
        String halfTermQuery = "DROP TABLE IF EXISTS "+Constants.HALF_TERM_TABLE;
        database.execSQL(halfTermQuery);
        String materialQuery = "DROP TABLE IF EXISTS "+Constants.STUDY_MATERIALS_TABLE;
        database.execSQL(materialQuery);
        String summaryQuery = "DROP TABLE IF EXISTS "+Constants.WEEK_SUMMARIES_TABLE;
        database.execSQL(summaryQuery);
        String reportQuery = "DROP TABLE IF EXISTS "+Constants.REPORT_TABLE;
        database.execSQL(reportQuery);
        onCreate(database);
    }
    public void open() {
        if (null == dbase || !dbase.isOpen()) {
            try {
                dbase = this.getWritableDatabase();
            } catch (SQLiteException sqLiteException) {
            }
        }
    }

    public void close() {
        if (dbase != null) {
            dbase.close();
        }
    }

    public int insert(String table, ContentValues values) {
        dbase =this.getWritableDatabase();
        return (int) dbase.insert(table, null, values);
    }

    public void delete(String tableName) {
        dbase.execSQL("delete from " + tableName);
    }

    public Cursor getAllRow(String table, String id) {
        return dbase.query(table, null, null, null, null, null, id);
    }

    /**
     * Get all table data with similar fields
     * @return
     */
    public ArrayList<WDT> getAllRows(String tableName) {
        ArrayList<WDT> allList = new ArrayList<WDT>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableName;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setId(cursor.getString(1));
                wdt.setName(cursor.getString(2));
                allList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return sound list
        return allList;
    }

    /**
     * Get all table data with similar fields
     * @return
     */
    public ArrayList<WDT> getClasses(String tableName) {
        ArrayList<WDT> allList = new ArrayList<WDT>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableName;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setId(cursor.getString(1));
                wdt.setName(cursor.getString(3));
                allList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return sound list
        return allList;
    }
    /*** Get all student lass name
     * @return*/
    //GET ALL HALF TERM VALUES
    public ArrayList<WDT> getHalfTerm(String subject, String term, String clas, String week_no) {
        ArrayList<WDT> halfTermList = new ArrayList<WDT>();
        // Select All Query
       // String selectQuery = "SELECT  * FROM " + Constants.HALF_TERM_TABLE;

        String selectQuery = "SELECT  * FROM " + Constants.HALF_TERM_TABLE
                +" WHERE "
                +Constants.KEY_TERM_ID+ "= '"+term+"' AND "+Constants.KEY_SUBJECT_ID + " = '"+subject+"' AND "
                +Constants.KEY_CLASS_ID+ "= '"+clas+"' AND "+Constants.KEY_HALF_TERM_NO + "= '"+week_no+"'";

        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setId(cursor.getString(1));
                wdt.setTitle(cursor.getString(2));
                wdt.setContent(cursor.getString(3));
                wdt.setHalfTermNo(cursor.getString(4));
                wdt.setSubjectsId(cursor.getString(5));
                wdt.setClassesId(cursor.getString(6));
                wdt.setTermsId(cursor.getString(7));
                halfTermList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return sound list
        return halfTermList;
    }
    /*** Get all student lass name
     * @return*/
    //GET ALL HALF TERM VALUES
    public ArrayList<WDT> getStudyMaterial(String days_id, String weeks_id, String terms_id, String subjects_id, String class_id) {
        ArrayList<WDT> materialList = new ArrayList<WDT>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.STUDY_MATERIALS_TABLE
                +" WHERE "+Constants.KEY_DAY_ID
                +" = '"+days_id+"' AND "+Constants.KEY_WEEK_ID + " = '"+weeks_id+"' AND "
                +Constants.KEY_TERM_ID + " = '"+terms_id+"' AND "+Constants.KEY_SUBJECT_ID + " = '"+subjects_id+"' AND "
                +Constants.KEY_CLASS_ID + " = '"+class_id+"' ORDER BY "+Constants.BODY_NO+ " ASC";

        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setId(cursor.getString(1));
                wdt.setStudyTitle(cursor.getString(2));
                wdt.setBodyNo(cursor.getString(3));
                wdt.setStudyNo(cursor.getString(4));
                wdt.setBodyTitle(cursor.getString(5));
                wdt.setBodyContent(cursor.getString(6));
                wdt.setBodyType(cursor.getString(7));
                wdt.setExpectedTime(cursor.getString(8));
                materialList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return sound list
        return materialList;
    }

    /*** Get all student lass name
     * @return*/
    //GET ALL HALF TERM VALUES
    public ArrayList<WDT> getWeekSummary(String subject_id, String term_id, String class_id, String weeks_id) {
        ArrayList<WDT> weekSummaryList = new ArrayList<WDT>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.WEEK_SUMMARIES_TABLE
        +" WHERE "+Constants.KEY_WEEK_ID+ "= '"+weeks_id+"' AND "
                +Constants.KEY_TERM_ID+ "= '"+term_id+"' AND "+Constants.KEY_SUBJECT_ID + " = '"+subject_id+"' AND "
                +Constants.KEY_CLASS_ID+ "= '"+class_id+"'";

        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setId(cursor.getString(1));
                wdt.setSummaryTitle(cursor.getString(2));
                wdt.setBodyTitle(cursor.getString(4));
                wdt.setContent(cursor.getString(5));
                weekSummaryList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return sound list
        return weekSummaryList;
    }
    /*** Get all student lass name
     * @return*/
    //Get report summary
    public ArrayList<WDT> getReport(String subject_id, String term_id, String class_id, String weeks_id,String day_id) {
        ArrayList<WDT> reportSummaryList = new ArrayList<WDT>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Constants.REPORT_TABLE;
//                +" WHERE "+Constants.KEY_WEEK_ID+ "= '"+weeks_id+"' AND "
//                +Constants.KEY_TERM_ID+ "= '"+term_id+"' AND "+Constants.KEY_SUBJECT_ID + " = '"+subject_id+"' AND "
//                +Constants.KEY_CLASS_ID+ "= '"+class_id+"'AND "+Constants.KEY_DAY_ID+" = "+day_id;

        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WDT wdt = new WDT();
                wdt.setTitle(cursor.getString(1));
                wdt.setExpectedTime(cursor.getString(2));
                wdt.setActualTime(cursor.getString(3));
                reportSummaryList.add(wdt);
            } while (cursor.moveToNext());
        }
        // return report list
        return reportSummaryList;
    }

    public int rowcount(String tablename)
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }

    //add to report table
    public void insertReport(ContentValues values){
    SQLiteDatabase db=this.getWritableDatabase();
    try{
            db.insert(Constants.REPORT_TABLE, null, values);
            db.close();
        }
    catch (SQLException ex){
        ex.printStackTrace();
    }
}


}