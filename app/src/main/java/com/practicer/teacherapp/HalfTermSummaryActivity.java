package com.practicer.teacherapp;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class HalfTermSummaryActivity extends AppCompatActivity {
    private TextView halfTermView;
    private TextView halfTerm;
    private Button goToWeekSummaryButton;
    private ListView halfSummary;
    private DBController db;
    private CustomListAdapter adapter;
    private ArrayList<WDT> halfTermList = new ArrayList<WDT>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_term_summary);
        db = new DBController(this);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        toolBar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.back_icon,null));
        int week = Integer.parseInt(getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        toolBar.setTitle(String.format("%s %s", getWeekSummaryValue(week).toUpperCase(),getString(R.string.summary).toUpperCase()));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        halfTermView = (TextView) findViewById(R.id.half_term_title);
        halfTerm = (TextView) findViewById(R.id.half_title);
        halfSummary =(ListView) findViewById(R.id.half_term_list);

        //get week number
        halfTermView.setText(String.format("%s - %s - %s - %s - %s\n%s",
                getIntent().getStringExtra(Constants.KEY_SUBJECT), getIntent().getStringExtra(Constants.KEY_CLASS), getIntent().getStringExtra(Constants.TERM),
                 getIntent().getStringExtra(Constants.KEY_WEEK), getIntent().getStringExtra(Constants.KEY_DAY), getIntent().getStringExtra(Constants.STUDY_TITLE)));
        halfTerm.setText(String.format("%s %s", getWeekSummaryValue(week),getString(R.string.summary)));

        //get week summary for the selected options subject, String term, String clas, String week_no
        ArrayList<WDT> wdt = db.getHalfTerm(getIntent().getStringExtra(Constants.KEY_SUBJECT_ID),
                getIntent().getStringExtra(Constants.KEY_TERM_ID),getIntent().getStringExtra(Constants.KEY_CLASS_ID),getWeekSummaryValue(week));

        if(wdt.size() > 0) {
            for (int i = 0; i < wdt.size(); i++) {
                WDT wdt1 = new WDT();
                wdt1.setContent(wdt.get(i).getContent());
                if(wdt1.getContent().equals(Constants.ALPHABETS)){
                    for(int j =1; j<=12; j++){
                        WDT alphabet = new WDT();
                        alphabet.setTitle(Constants.ALPHABETS);
                        alphabet.setContent(String.format("h%s",Integer.toString(j)));
                        halfTermList.add(alphabet);
                    }
                }
                else{
                    wdt1.setTitle(wdt.get(i).getTitle());
                    wdt1.setContent(wdt.get(i).getContent());
                    halfTermList.add(wdt1);
                }
            }
            adapter = new CustomListAdapter(HalfTermSummaryActivity.this, halfTermList);
            halfSummary.setAdapter(adapter);
        }else {
            halfTerm.setText(getString(R.string.no_data_found));
        }
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