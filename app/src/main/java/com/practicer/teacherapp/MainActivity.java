package com.practicer.teacherapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import custom.Constants;
import custom.DBController;
import custom.SpinnerCustomList;
import custom.StdClass;
import custom.WDT;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DBController db;
    private Spinner week, day, term, clas, subject, type_of_activity;
    private Button start_activity_button;
    private ArrayList<WDT> weekItems = new ArrayList<WDT>();
    private ArrayList<WDT> dayItems = new ArrayList<WDT>();
    private ArrayList<WDT> termItems = new ArrayList<WDT>();
    private ArrayList<WDT> clasItems = new ArrayList<WDT>();
    private ArrayList<WDT> subjectItems = new ArrayList<WDT>();
    private ArrayList<WDT> activityItems = new ArrayList<WDT>();

    private Button start_activity, dateView, timeView;
    private EditText dateTextView, timeTextView;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String timeString;
    private String dateString;

    private String subjectString, classString, termString, weekString, dayString, activityTypeString;
    private String subjectId, classId, termId, weekId, dayId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBController(this);
//        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        toolbar.setTitle(getString(R.string.app_name));
        //inflate menu on toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.upload) {
                    startActivity(new Intent(MainActivity.this,UploadExcel.class));
                    return true;
                }
                return onMenuItemClick(item);
            }
        });
        //Views
        //Spinner views
        subject = (Spinner) findViewById(R.id.subject);
        clas = (Spinner) findViewById(R.id.clas);
        term = (Spinner) findViewById(R.id.term);
        week = (Spinner) findViewById(R.id.week);
        day = (Spinner) findViewById(R.id.day);
        type_of_activity = (Spinner) findViewById(R.id.type_of_activity);
        dateView = (Button) findViewById(R.id.date_button);
        timeView = (Button) findViewById(R.id.time_button);
        dateTextView = (EditText) findViewById(R.id.date);
        timeTextView = (EditText) findViewById(R.id.time);

        dateTextView.setFocusable(false);
        timeTextView.setFocusable(false);

        start_activity_button = (Button) findViewById(R.id.start_activity_button);
        //set date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dateString = mDay + "-" + (mMonth + 1) + "-" + mYear;
        dateTextView.setText(dateString);
        //set time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        timeString = mHour + ":" + mMinute;
        timeTextView.setText(timeString);

        try {
            //set subject adapter
            WDT wdt = new WDT();
            wdt.setName(getString(R.string.subject));
            subjectItems.add(wdt);
            ArrayList<WDT> subjectList = db.getAllRows(Constants.SUBJECT_TABLE);
            for (int i = 0; i < subjectList.size(); i++) {
                WDT wdt1 = new WDT();
                wdt1.setName(subjectList.get(i).getName());
                wdt1.setId(subjectList.get(i).getId());
                subjectItems.add(wdt1);
            }
            subject.setOnItemSelectedListener(this);
            SpinnerCustomList subjectAdapter = new SpinnerCustomList(MainActivity.this, subjectItems);
            subject.setAdapter(subjectAdapter);

            //set class items
            WDT wdtc = new WDT();
            wdtc.setName(getString(R.string.stdclass));
            clasItems.add(wdtc);

            ArrayList<WDT> stdClas = db.getClasses(Constants.CLASS_TABLE);
            for (int i = 0; i < stdClas.size(); i++) {
                WDT wdt1 = new WDT();
                wdt1.setId(stdClas.get(i).getId());
                wdt1.setName(stdClas.get(i).getName());
                clasItems.add(wdt1);
            }
            clas.setOnItemSelectedListener(this);
            SpinnerCustomList clasAdapter = new SpinnerCustomList(MainActivity.this, clasItems);
            clas.setAdapter(clasAdapter);

            //set term adapter
            WDT wdtt = new WDT();
            wdtt.setName(getString(R.string.term));
            termItems.add(wdtt);
            ArrayList<WDT> termList = db.getAllRows(Constants.TERM_TABLE);
            for (int i = 0; i < termList.size(); i++) {
                WDT wdt2 = new WDT();
                wdt2.setId(termList.get(i).getId());
                wdt2.setName(termList.get(i).getName());
                termItems.add(wdt2);
            }
            term.setOnItemSelectedListener(this);
            SpinnerCustomList termAdapter = new SpinnerCustomList(MainActivity.this, termItems);
            term.setAdapter(termAdapter);

            //Set week adapter
            WDT wdtw = new WDT();
            wdtw.setName(getString(R.string.week));
            ArrayList<WDT> weekList = db.getAllRows(Constants.WEEK_TABLE);
            weekItems.add(wdtw);
            for (int i = 0; i < weekList.size(); i++) {
                WDT wdt3 = new WDT();
                wdt3.setId(weekList.get(i).getId());
                wdt3.setName(weekList.get(i).getName());
                weekItems.add(wdt3);
            }
            week.setOnItemSelectedListener(this);
            SpinnerCustomList customAdapter = new SpinnerCustomList(getApplicationContext(), weekItems);
            week.setAdapter(customAdapter);

            //set day adaper
            WDT wdtd = new WDT();
            wdtd.setName(getString(R.string.day));
            dayItems.add(wdtd);
            ArrayList<WDT> allDays = db.getAllRows(Constants.DAY_TABLE);
            for (int i = 0; i < allDays.size(); i++) {
                WDT wdt4 = new WDT();
                wdt4.setId(allDays.get(i).getId());
                wdt4.setName(allDays.get(i).getName());
                dayItems.add(wdt4);
            }
            day.setOnItemSelectedListener(this);
            SpinnerCustomList dayAdapter = new SpinnerCustomList(MainActivity.this, dayItems);
            day.setAdapter(dayAdapter);

            //Type of activity
            WDT wdttype = new WDT();
            wdttype.setName(getString(R.string.activity_type));
            activityItems.add(wdttype);
            ArrayList<WDT> allTypes = db.getAllRows(Constants.ACTIVITY_TYPE_TABLE);
            for (int i = 0; i < allTypes.size(); i++) {
                WDT wdt5 = new WDT();
                wdt5.setId(allTypes.get(i).getId());
                wdt5.setName(allTypes.get(i).getName());
                activityItems.add(wdt5);
            }
            type_of_activity.setOnItemSelectedListener(this);
            SpinnerCustomList activityAdapter = new SpinnerCustomList(MainActivity.this, activityItems);
            type_of_activity.setAdapter(activityAdapter);
        }catch (IllegalStateException ex){}
        //set onclick listener
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                dateTextView.setText(dateString);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                timeTextView.setText(hourOfDay + ":" + minute);
                                timeString = hourOfDay + ":" + minute;
                                mHour = hourOfDay;
                                mMinute = minute;
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }
        });


        start_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               validateFields();
            }
        });
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Spinner spinner = (Spinner)arg0;
        if(spinner.getId() == R.id.subject){
            subjectId = subjectItems.get(position).getId();
            subjectString = subjectItems.get(position).getName();

        }
        else if (spinner.getId() == R.id.clas){
            classId = clasItems.get(position).getId();
            classString = clasItems.get(position).getName();

        }
        else if (spinner.getId() == R.id.week){
            weekId = weekItems.get(position).getId();
            weekString = weekItems.get(position).getName();

        }
        else if(spinner.getId() == R.id.term){
            termId = termItems.get(position).getId();
            termString = termItems.get(position).getName();

        }
        else if(spinner.getId() == R.id.day){
            dayId = dayItems.get(position).getId();
            dayString = dayItems.get(position).getName();
        }
        else if(spinner.getId() == R.id.type_of_activity){
            activityTypeString = activityItems.get(position).getName();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    //validate fields
    public void validateFields(){
        boolean cancel = false;
        View focusView = null;
        if(subjectString.equalsIgnoreCase(getString(R.string.subject))){
            cancel = true;
            focusView = subject;
            Toast.makeText(getApplicationContext(), getString(R.string.select_subject), Toast.LENGTH_SHORT).show();
        }
        if(classString.equalsIgnoreCase(getString(R.string.stdclass))){
            cancel = true;
            focusView = clas;
            Toast.makeText(getApplicationContext(), getString(R.string.select_class), Toast.LENGTH_SHORT).show();
        }
        if(termString.equalsIgnoreCase(getString(R.string.term))){
            cancel = true;
            focusView = term;
            Toast.makeText(getApplicationContext(), getString(R.string.select_term), Toast.LENGTH_SHORT).show();
        }
        if(weekString.equalsIgnoreCase(getString(R.string.week))){
            cancel = true;
            focusView = week;
            Toast.makeText(getApplicationContext(), getString(R.string.select_week), Toast.LENGTH_SHORT).show();
        }
        if(dayString.equalsIgnoreCase(getString(R.string.day))){
            cancel = true;
            focusView = day;
            Toast.makeText(getApplicationContext(), getString(R.string.select_day), Toast.LENGTH_SHORT).show();
        }
        if(activityTypeString.equals(getString(R.string.activity_type))){
            cancel = true;
            focusView = type_of_activity;
            Toast.makeText(getApplicationContext(), getString(R.string.select_activity_type), Toast.LENGTH_SHORT).show();
        }
        if(cancel){
            focusView.requestFocus();
        }
        else{
            Intent intent = new Intent(MainActivity.this, WeekContentActivity.class);
            intent.putExtra(Constants.KEY_SUBJECT, subjectString);
            intent.putExtra(Constants.KEY_CLASS, classString);
            intent.putExtra(Constants.KEY_TERM, termString);
            intent.putExtra(Constants.KEY_WEEK, weekString);
            intent.putExtra(Constants.KEY_DAY, dayString);

            intent.putExtra(Constants.KEY_SUBJECT_ID, subjectId);
            intent.putExtra(Constants.KEY_CLASS_ID, classId);
            intent.putExtra(Constants.KEY_TERM_ID, termId);
            intent.putExtra(Constants.KEY_WEEK_ID, weekId);
            intent.putExtra(Constants.KEY_DAY_ID, dayId);
            startActivity(intent);
        }
    }
}