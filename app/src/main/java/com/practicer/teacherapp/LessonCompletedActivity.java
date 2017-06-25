package com.practicer.teacherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import custom.Constants;
import custom.CustomListAdapter;
import custom.DBController;
import custom.ReportCustomListAdapter;
import custom.WDT;

public class LessonCompletedActivity extends AppCompatActivity {
    private ListView reportList;
    private DBController db;
    private ReportCustomListAdapter adapter;
    private ArrayList<WDT> report= new ArrayList<WDT>();
    private TextView body_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_completed);
        Button startNew = (Button) findViewById(R.id.start_new);
        body_title = (TextView) findViewById(R.id.body_title);
        reportList = (ListView) findViewById(R.id.reportList);
        db = new DBController(this);

        startNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LessonCompletedActivity.this, MainActivity.class));
            }
        });

        int weekValue = getIntent().getIntExtra(Constants.KEY_WEEK_ID,0);
        body_title.setText(String.format("%s - %s - %s - %s - %s - %s",
                getIntent().getStringExtra(Constants.KEY_SUBJECT), getIntent().getStringExtra(Constants.KEY_CLASS), getIntent().getStringExtra(Constants.KEY_TERM),
                getWeekSummaryValue(weekValue), getIntent().getStringExtra(Constants.KEY_WEEK), getIntent().getStringExtra(Constants.KEY_DAY)));


        //These are created statically, please the table

        //get current report list
        ArrayList<HashMap<String, String>> reports = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra(Constants.REPORT);
        for (int i=0; i<reports.size(); i++){
            WDT wdt = new WDT();
            wdt.setTitle(reports.get(i).get(Constants.STUDY_MATERIAL));
            wdt.setExpectedTime(reports.get(i).get(Constants.EXPECTED_TIME));
            wdt.setActualTime(reports.get(i).get(Constants.ACTUAL_TIME));
            report.add(wdt);
        }
        //set report adapter
        adapter = new ReportCustomListAdapter(LessonCompletedActivity.this, report);
        reportList.setAdapter(adapter);
    }
    public String getWeekSummaryValue(int value){
        String weekSummary = "";
        if(value <= 5){
            weekSummary = "1ST HALF";
        }
        else if(value > 5 && value <= 10){
            weekSummary = "2ND HALF";
        }
        else if(value >10){
            weekSummary = "3RD HALF";
        }
        return weekSummary;
    }
}