package com.practicer.teacherapp;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import custom.Constants;
import custom.CustomListAdapter;
import custom.DBController;
import custom.WDT;

public class WeekSummaryActivity extends AppCompatActivity {
private TextView weekSumamryView;
    private TextView weekSumamry;
    private Button goToWeekSummaryButton;
    private ListView weekSummaryListview;
    private DBController db;
    private CustomListAdapter adapter;
    private ArrayList<WDT> weekSummaryItems = new ArrayList<WDT>();
    private String summaryHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_summary);
        db = new DBController(this);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        toolBar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.back_icon,null));
        toolBar.setTitle(String.format("%s %s",getIntent().getStringExtra(Constants.KEY_WEEK).toUpperCase(), getString(R.string.summary).toUpperCase()));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        weekSumamryView = (TextView) findViewById(R.id.week_summary_title);
        weekSumamry = (TextView) findViewById(R.id.week_title);
        goToWeekSummaryButton = (Button) findViewById(R.id.week_summary_button);
        weekSummaryListview =(ListView) findViewById(R.id.week_summary_list);

        //get week number
        int week = Integer.parseInt(getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        weekSumamryView.setText(String.format("%s - %s - %s - %s - %s\n%s",
                getIntent().getStringExtra(Constants.KEY_SUBJECT), getIntent().getStringExtra(Constants.KEY_CLASS), getIntent().getStringExtra(Constants.TERM),
                 getIntent().getStringExtra(Constants.KEY_WEEK), getIntent().getStringExtra(Constants.KEY_DAY), getIntent().getStringExtra(Constants.STUDY_TITLE)));

        //get week summary for the selected options
        ArrayList<WDT> wdt = db.getWeekSummary(getIntent().getStringExtra(Constants.KEY_SUBJECT_ID),
                getIntent().getStringExtra(Constants.KEY_TERM_ID),getIntent().getStringExtra(Constants.KEY_CLASS_ID),getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        if(wdt.size() > 0) {
            for (int i = 0; i < wdt.size(); i++) {
                WDT wdt1 = new WDT();
                wdt1.setBodyTitle(wdt.get(i).getBodyTitle());
                wdt1.setContent(wdt.get(i).getContent());
                weekSummaryItems.add(wdt1);
                summaryHead = wdt.get(i).getSummaryTitle();
            }
            adapter = new CustomListAdapter(WeekSummaryActivity.this, weekSummaryItems);
            weekSummaryListview.setAdapter(adapter);
        }else {
            weekSumamry.setText(getString(R.string.no_data_found));
            weekSumamry.setGravity(Gravity.CENTER);
        }
        weekSumamry.setText(String.format("%s %s - %s", getIntent().getStringExtra(Constants.KEY_WEEK), getString(R.string.summary), summaryHead));
    }
    //get half term number from the selected term
    public String getWeekSummaryValue(int value){
        String weekSummary = "";
        if(value <= 5){
            weekSummary = "1st half";
        }
        else if(value > 5 && value <= 10){
            weekSummary = "2nd half";
        }
        else if(value >10){
            weekSummary = "3rd half";
        }
        return weekSummary;
    }
}