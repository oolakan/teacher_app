package excel.helper;
/**
 * Created by HighStrit on 11/04/2017.
 */

import android.content.ContentValues;
import android.util.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.Iterator;
import java.util.Locale;

import custom.Constants;
import custom.DBController;

public class Excel2SQLiteHelper {

    //insert into class table
    public static void insertClasses(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {

                  ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.KEY_ID, formatter.formatCellValue(row.getCell(0)));
                    contentValues.put(Constants.KEY_CATEGORY,formatter.formatCellValue(row.getCell(1)));
                    contentValues.put(Constants.KEY_NAME, formatter.formatCellValue(row.getCell(2)));
                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
                }
            }
    }

    //insert into day table
    public static void insertSubjectOrTermOrDayWeekOrType(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {
                ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.KEY_ID, formatter.formatCellValue(row.getCell(0)));
                    contentValues.put(Constants.KEY_NAME, formatter.formatCellValue(row.getCell(1)));
                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
                }
            }
    }

    //insert into term table
    public static void insertHalfTerms(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {
                ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.KEY_ID, formatter.formatCellValue(row.getCell(0)));
                    contentValues.put(Constants.KEY_HALF_TERM_SUMMARY_TITLE,  formatter.formatCellValue(row.getCell(1)));
                    contentValues.put(Constants.KEY_HALF_TERM_SUMMARY_CONTENT, formatter.formatCellValue(row.getCell(2)));
                    contentValues.put(Constants.KEY_HALF_TERM_NO,  formatter.formatCellValue(row.getCell(3)));
                    contentValues.put(Constants.KEY_SUBJECT_ID,  formatter.formatCellValue(row.getCell(4)));
                    contentValues.put(Constants.KEY_CLASS_ID,  formatter.formatCellValue(row.getCell(6)));
                    contentValues.put(Constants.KEY_TERM_ID,  formatter.formatCellValue(row.getCell(8)));
                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
                }
            }
    }

    //insert into reports table
    public static void insertReports(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.KEY_ID, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.STUDY_MATERIAL, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.EXPECTED_TIME, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.ACTUAL_TIME, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.KEY_SUBJECT_ID, row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.KEY_CLASS_ID, row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.KEY_TERM_ID, row.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.KEY_DAY_ID, row.getCell(7, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    contentValues.put(Constants.KEY_WEEK_ID, row.getCell(8, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
                }

        }
    }

    //insert into subject material table
    public static void insertStudyMaterials(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {

                ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.KEY_ID, formatter.formatCellValue(row.getCell(0)));
                    contentValues.put(Constants.STUDY_TITLE, formatter.formatCellValue(row.getCell(1)));
                    contentValues.put(Constants.BODY_NO, formatter.formatCellValue(row.getCell(2)));
                    contentValues.put(Constants.STUDY_NO,  formatter.formatCellValue(row.getCell(3)));
                    contentValues.put(Constants.BODY_TITLE,  formatter.formatCellValue(row.getCell(4)));
                    contentValues.put(Constants.BODY_CONTENT,  formatter.formatCellValue(row.getCell(5)));
                    contentValues.put(Constants.BODY_TYPE,  formatter.formatCellValue(row.getCell(6)));
                    contentValues.put(Constants.EXPECTED_TIME,  formatter.formatCellValue(row.getCell(7)));
                    contentValues.put(Constants.KEY_DAY_ID,  formatter.formatCellValue(row.getCell(8)));
                    contentValues.put(Constants.KEY_WEEK_ID,  formatter.formatCellValue(row.getCell(10)));
                    contentValues.put(Constants.KEY_TERM_ID,  formatter.formatCellValue(row.getCell(12)));
                    contentValues.put(Constants.KEY_SUBJECT_ID,  formatter.formatCellValue(row.getCell(14)));
                    contentValues.put(Constants.KEY_CLASS_ID,  formatter.formatCellValue(row.getCell(16)));
                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
            }
        }
    }

    //insert into reports table
    public static void insertWeekSummaries(DBController dbAdapter, Sheet sheet) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();
            if (row.getRowNum() > 0 && sheet.getRow(row.getRowNum()) != null) {

                ContentValues contentValues = new ContentValues();
                    //put week summaries content
                    contentValues.put(Constants.KEY_ID,  formatter.formatCellValue(row.getCell(0)));
                    contentValues.put(Constants.SUMMARY_TITLE,  formatter.formatCellValue(row.getCell(1)));
                    contentValues.put(Constants.SUMMARY_NO,  formatter.formatCellValue(row.getCell(2)));
                    contentValues.put(Constants.SUMMARY_HEADING,  formatter.formatCellValue(row.getCell(3)));
                    contentValues.put(Constants.SUMMARY_CONTENT,  formatter.formatCellValue(row.getCell(4)));
                    contentValues.put(Constants.KEY_TERM_ID,  formatter.formatCellValue(row.getCell(5)));
                    contentValues.put(Constants.KEY_WEEK_ID,  formatter.formatCellValue(row.getCell(7)));
                    contentValues.put(Constants.KEY_CLASS_ID,  formatter.formatCellValue(row.getCell(9)));
                    contentValues.put(Constants.KEY_SUBJECT_ID, formatter.formatCellValue(row.getCell(11)));

                    try {
                        if (dbAdapter.insert(sheet.getSheetName(), contentValues) < 0) {
                            return;
                        }
                    } catch (Exception ex) {
                        Log.d("Exception in importing", ex.getMessage());
                    }
                }
            }
    }
}