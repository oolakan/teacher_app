package com.practicer.teacherapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import custom.Constants;
import custom.DBController;
import excel.helper.Excel2SQLiteHelper;

public class UploadExcel extends ListActivity {
    private ProgressDialog progressDialog;
    TextView message;
    DBController controller = new DBController(this);
    Button uploadExcel;
    ListView lv;
    public static final int requestcode = 1;
    static String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_excel);
        uploadExcel = (Button) findViewById(R.id.upload_excel);
        message = (TextView) findViewById(R.id.text);
        //select file from local storage
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        toolBar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.back_icon,null));
        toolBar.setTitle(getString(R.string.upload_excel));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadExcel.this, MainActivity.class));
                finish();
            }
        });

        uploadExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("gagt/sdf");
                try {
                    startActivityForResult(fileintent, requestcode);
                } catch (ActivityNotFoundException e) {
                    message.setText("No activity can handle picking a file. Showing alternatives.");
                }
            }
        });
    }

    /**
     * upload excel
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showProgress();
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String FilePath = data.getData().getPath();
                try {
                    if (resultCode == RESULT_OK) {
                        AssetManager am = this.getAssets();
                        InputStream inStream;
                        Workbook wb = null;
                        try {
                            inStream = new FileInputStream(FilePath);
                            wb = new HSSFWorkbook(inStream);
                            inStream.close();
                        } catch (IOException e) {
                            hideProgress();
                            message.setText("First "+e.getMessage());
                            Log.d("Poi", e.getMessage());
                            e.printStackTrace();
                        }
                        DBController dbAdapter = new DBController(this);
                        //get the number of sheets and iterate over
                        for(int i =0; i<wb.getNumberOfSheets(); i++){
                            Sheet sheet = wb.getSheetAt(i);
                            String tableName = wb.getSheetName(i);
                            dbAdapter.open();
                            dbAdapter.delete(wb.getSheetName(i));
                            dbAdapter.close();
                            dbAdapter.open();
                            insertSheet(tableName,dbAdapter, sheet);
                            if (sheet == null) {
                                return;
                            }
                        }
                    }
                    hideProgress();
                } catch (Exception ex) {
                    hideProgress();
                    message.setText(ex.getMessage());
                }
        }
    }
    //insert excel sheet into the sqlite database
    public static void insertSheet(String tableName, DBController dbAdapter,  Sheet sheet){

        if(tableName.equalsIgnoreCase(Constants.DAY_TABLE)
                || tableName.equalsIgnoreCase(Constants.WEEK_TABLE) ||
                tableName.equalsIgnoreCase(Constants.TERM_TABLE)
                || tableName.equalsIgnoreCase(Constants.SUBJECT_TABLE) ||
                tableName.equalsIgnoreCase(Constants.ACTIVITY_TYPE_TABLE)){
            Excel2SQLiteHelper.insertSubjectOrTermOrDayWeekOrType(dbAdapter, sheet);
        }
        else if(tableName.equalsIgnoreCase(Constants.CLASS_TABLE)){
            Excel2SQLiteHelper.insertClasses(dbAdapter, sheet);
        }
        else if(tableName.equalsIgnoreCase(Constants.HALF_TERM_TABLE)){
            Excel2SQLiteHelper.insertHalfTerms(dbAdapter, sheet);
        }
        else if(tableName.equalsIgnoreCase(Constants.STUDY_MATERIALS_TABLE)){
            Excel2SQLiteHelper.insertStudyMaterials(dbAdapter, sheet);
        }
        else if(tableName.equalsIgnoreCase(Constants.WEEK_SUMMARIES_TABLE)){
            Excel2SQLiteHelper.insertWeekSummaries(dbAdapter, sheet);
        }
    }
    //show progress bar
    public void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.uploading));
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.message));
        progressDialog.show();
    }
    //hide progress bar
    public  void hideProgress(){
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(UploadExcel.this, MainActivity.class));
        finish();
    }
}