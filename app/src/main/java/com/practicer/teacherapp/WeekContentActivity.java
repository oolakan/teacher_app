package com.practicer.teacherapp;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import custom.Constants;
import custom.CustomListAdapter;
import custom.DBController;
import custom.WDT;
import custom.WeekContentListAdapter;
import multimedia.SongsManager;


public class WeekContentActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, WeekContentListAdapter.customButtonListener ,
        SeekBar.OnSeekBarChangeListener {

    //WEEK SUMMARY VIEWS
    private ValueAnimator timer;
    private int secondsToRun;
    private ListView weekSummary;
    private DBController db;
    private CustomListAdapter adapter;
    private ArrayList<WDT> weekSummaryList = new ArrayList<WDT>();
    private String study_Title;
    private TextView weekSummaryView;
    private Button goToWeekSummaryButton, goToHalfTermSummaryButton;
    private TextView body_title, body_content, timerCounter, timerValue;
    private ImageView bodyImage;
    private Button next, previous;
    ArrayList<WDT> studyMaterialList;
    private FrameLayout musicFrame;
    private int mid = 0;
    WDT currentM;
    private int PREVIOUS_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private String dateTimetoMillis;
    private int mYear, mDay, mMonth, mHour, mMinute;
    private ImageView bodyContentImage;
    private ListView multimediaView;
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private SongsManager songManager;
    private custom.Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;

    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private Button pop, party, rap;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    private ArrayList<HashMap<String, String>> reportObjects = new ArrayList<HashMap<String, String>>();
    private Dialog dialog;
    ImageButton btnPlay;
    ImageButton btnBackward;
    ImageButton btnForward;
    TextView songCurrentDurationLabel;
    TextView songTotalDurationLabel;
    SeekBar songProgressBar;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_content);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        toolBar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.back_icon, null));
        toolBar.setTitle(String.format("%s - %s", getIntent().getStringExtra(Constants.KEY_WEEK).toUpperCase().toUpperCase(),
                getIntent().getStringExtra(Constants.KEY_SUBJECT).toUpperCase()));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db = new DBController(this);
        weekSummaryView = (TextView) findViewById(R.id.week_summary_title);
        goToWeekSummaryButton = (Button) findViewById(R.id.week_summary_button);
        goToHalfTermSummaryButton = (Button) findViewById(R.id.half_term_summary_button);
        body_title = (TextView) findViewById(R.id.body_title);
        body_content = (TextView) findViewById(R.id.body_content);
        timerCounter = (TextView) findViewById(R.id.timerCounter);
        timerValue = (TextView) findViewById(R.id.timerValue);
        bodyImage = (ImageView) findViewById(R.id.body_content_image);
        next = (Button) findViewById(R.id.next);
        multimediaView = (ListView) findViewById(R.id.multimedia);
        musicFrame = (FrameLayout) findViewById(R.id.musicFrame);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        previous = (Button) findViewById(R.id.previous);

        int week = Integer.parseInt(getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        if (mid == 0) {
            previous.setVisibility(View.GONE);
        } else {
            previous.setVisibility(View.VISIBLE);
        }

        int weekValue = Integer.parseInt(getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        goToWeekSummaryButton.setText(String.format("%s %s %s", getString(R.string.view), getIntent().getStringExtra(Constants.KEY_WEEK), getString(R.string.summary)));
        goToHalfTermSummaryButton.setText(String.format("%s %s %s", getString(R.string.view), getWeekSummaryValue(weekValue), getString(R.string.summary)));

        //set timer and study material details
        studyMaterialList = db.getStudyMaterial(
                getIntent().getStringExtra(Constants.KEY_DAY_ID),
                getIntent().getStringExtra(Constants.KEY_WEEK_ID),
                getIntent().getStringExtra(Constants.KEY_TERM_ID),
                getIntent().getStringExtra(Constants.KEY_SUBJECT_ID),
                getIntent().getStringExtra(Constants.KEY_CLASS_ID));

        if (studyMaterialList.size() > 0) {
            currentM = studyMaterialList.get(mid);
            study_Title = currentM.getStudyTitle();
            setStudyMaterialView();
        } else {
            body_content.setText(getString(R.string.no_data_found));
            body_content.setGravity(Gravity.CENTER);
            next.setVisibility(View.GONE);
            study_Title = "";
        }
        weekSummaryView.setText(String.format("%s - %s - %s - %s - %s\n%s",
                getIntent().getStringExtra(Constants.KEY_SUBJECT), getIntent().getStringExtra(Constants.KEY_CLASS), getIntent().getStringExtra(Constants.KEY_TERM),
                getIntent().getStringExtra(Constants.KEY_WEEK), getIntent().getStringExtra(Constants.KEY_DAY), study_Title));

        //display week summary
        goToWeekSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeekContentActivity.this, WeekSummaryActivity.class);
                intent.putExtra(Constants.KEY_SUBJECT, getIntent().getStringExtra(Constants.KEY_SUBJECT));
                intent.putExtra(Constants.KEY_CLASS, getIntent().getStringExtra(Constants.KEY_CLASS));
                intent.putExtra(Constants.KEY_TERM, getIntent().getStringExtra(Constants.KEY_TERM));
                intent.putExtra(Constants.KEY_WEEK, getIntent().getStringExtra(Constants.KEY_WEEK));
                intent.putExtra(Constants.KEY_DAY, getIntent().getStringExtra(Constants.KEY_DAY));

                intent.putExtra(Constants.KEY_SUBJECT_ID, getIntent().getStringExtra(Constants.KEY_SUBJECT_ID));
                intent.putExtra(Constants.KEY_CLASS_ID, getIntent().getStringExtra(Constants.KEY_CLASS_ID));
                intent.putExtra(Constants.KEY_TERM_ID, getIntent().getStringExtra(Constants.KEY_TERM_ID));
                intent.putExtra(Constants.KEY_WEEK_ID, getIntent().getStringExtra(Constants.KEY_WEEK_ID));
                intent.putExtra(Constants.KEY_DAY_ID, getIntent().getStringExtra(Constants.KEY_DAY_ID));
                intent.putExtra(Constants.STUDY_TITLE, study_Title);
                startActivity(intent);
            }
        });
        //go to selected half term summary
        goToHalfTermSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeekContentActivity.this, HalfTermSummaryActivity.class);
                intent.putExtra(Constants.KEY_SUBJECT, getIntent().getStringExtra(Constants.KEY_SUBJECT));
                intent.putExtra(Constants.KEY_CLASS, getIntent().getStringExtra(Constants.KEY_CLASS));
                intent.putExtra(Constants.KEY_TERM, getIntent().getStringExtra(Constants.KEY_TERM));
                intent.putExtra(Constants.KEY_WEEK, getIntent().getStringExtra(Constants.KEY_WEEK));
                intent.putExtra(Constants.KEY_DAY, getIntent().getStringExtra(Constants.KEY_DAY));

                intent.putExtra(Constants.KEY_SUBJECT_ID, getIntent().getStringExtra(Constants.KEY_SUBJECT_ID));
                intent.putExtra(Constants.KEY_CLASS_ID, getIntent().getStringExtra(Constants.KEY_CLASS_ID));
                intent.putExtra(Constants.KEY_TERM_ID, getIntent().getStringExtra(Constants.KEY_TERM_ID));
                intent.putExtra(Constants.KEY_WEEK_ID, getIntent().getStringExtra(Constants.KEY_WEEK_ID));
                intent.putExtra(Constants.KEY_DAY_ID, getIntent().getStringExtra(Constants.KEY_DAY_ID));
                intent.putExtra(Constants.STUDY_TITLE, study_Title);
                startActivity(intent);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mid++;
                    if (mid < studyMaterialList.size()) {
                        if (mid != 0) previous.setVisibility(View.VISIBLE);
                        currentM = studyMaterialList.get(mid);
                        //if user is not on the current page decrement value
                        if(PREVIOUS_PAGE != 0) PREVIOUS_PAGE--;
                        //if user is on the current page increment current page
                        if(PREVIOUS_PAGE == 0) CURRENT_PAGE++;
                        setStudyMaterialView();
                        if (!currentM.getExpectedTime().isEmpty()) {
                            //save previous time and content
                            timerCounter.setText("");
                            String time = currentM.getExpectedTime().replaceAll("\\D+", "");
                            secondsToRun = Integer.parseInt(time) * 1000 * 60;
                            //run new timer when user is on the next page
                            if(CURRENT_PAGE > PREVIOUS_PAGE) {
                                if (timer != null && timer.isStarted()) {
                                    //save the previous data
                                    currentM = studyMaterialList.get(mid-1);
                                    utils = new custom.Utilities();
                                    String used_time = utils.milliSecondsToTimer(timer.getCurrentPlayTime()) + "";
                                    saveReport(currentM.getBodyTitle(), currentM.getExpectedTime().replaceAll("\\D+", ""), String.format("%s mins", used_time));
                                }
                                runTimer(secondsToRun);
                            }
                            //reinitialize current page to 0
                            CURRENT_PAGE = 0;
                        }
                    } else if (mid > studyMaterialList.size()) {
                        next.setText(getString(R.string.finish));
                        previous.setVisibility(View.GONE);
                        Intent intent = new Intent(WeekContentActivity.this, LessonCompletedActivity.class);
                        intent.putExtra(Constants.KEY_SUBJECT, getIntent().getStringExtra(Constants.KEY_SUBJECT));
                        intent.putExtra(Constants.KEY_CLASS, getIntent().getStringExtra(Constants.KEY_CLASS));
                        intent.putExtra(Constants.KEY_TERM, getIntent().getStringExtra(Constants.KEY_TERM));
                        intent.putExtra(Constants.KEY_WEEK, getIntent().getStringExtra(Constants.KEY_WEEK));
                        intent.putExtra(Constants.KEY_DAY, getIntent().getStringExtra(Constants.KEY_DAY));
                        intent.putExtra(Constants.REPORT, reportObjects);
                        startActivity(intent);
                    } else {
                        next.setText(getString(R.string.finish));
                        previous.setVisibility(View.GONE);
                        if (!currentM.getExpectedTime().isEmpty()) {
                            //save previous time and content
                            if (timer != null && timer.isStarted()) {
                                //save the previous data
                                currentM = studyMaterialList.get(mid-1);
                                utils = new custom.Utilities();
                                String used_time = utils.milliSecondsToTimer(timer.getCurrentPlayTime()) + "";
                                saveReport(currentM.getBodyTitle(), currentM.getExpectedTime().replaceAll("\\D+", ""), String.format("%s mins", used_time));
                            }
                            timerCounter.setText("");
                            secondsToRun = Integer.parseInt(currentM.getExpectedTime().replaceAll("\\D+", "")) * 1000 * 60;
                            runTimer(secondsToRun);
                        }
                    }
                } catch (NullPointerException ex) {
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(mid > 0) {
                        mid--;
                        if (mid != 0)
                            previous.setVisibility(View.VISIBLE);
                        else
                            previous.setVisibility(View.GONE);
                        currentM = studyMaterialList.get(mid);
                        PREVIOUS_PAGE++;
                        if (PREVIOUS_PAGE > 2) {
                            mid++;
                            Toast.makeText(getApplicationContext(), getString(R.string.back_not_allowed), Toast.LENGTH_SHORT).show();
                        }
                        else setPreviousStudyMaterialView();
                    } else {
                            next.setText(getString(R.string.next));
                            previous.setVisibility(View.GONE);
                    }
                }
                catch (NullPointerException ex){

                }
            }
        });
    }

    /**
     * set study material
     */
    private void setStudyMaterialView() {
        getContent();
    }


    private void setPreviousStudyMaterialView() {
        getContent();
    }

    public void getContent(){
        if (!currentM.getExpectedTime().isEmpty()) {
            timerCounter.setText(currentM.getExpectedTime().replaceAll("\\D+", "") + getString(R.string.minutes));
            timerValue.setText(currentM.getExpectedTime().replaceAll("\\D+", "") + getString(R.string.minutes));
            timerCounter.setVisibility(View.VISIBLE);
            timerValue.setVisibility(View.VISIBLE);
        }
        else {
            timerCounter.setVisibility(View.GONE);
            timerValue.setVisibility(View.GONE);
        }
        body_title.setText(currentM.getBodyTitle());
        if (currentM.getBodyType().equalsIgnoreCase(Constants.TEXT)) {
            musicFrame.setVisibility(View.GONE);
            bodyImage.setVisibility(View.GONE);
            multimediaView.setVisibility(View.GONE);
            body_content.setText(currentM.getBodyContent());
            body_content.setVisibility(View.VISIBLE);
        } else if (currentM.getBodyType().equalsIgnoreCase(Constants.MULTIMEDIA)) {
                body_content.setVisibility(View.GONE);
                bodyImage.setVisibility(View.GONE);
                musicFrame.setVisibility(View.GONE);
                multimediaView.setVisibility(View.VISIBLE);
                WeekContentListAdapter adapter = null;
                String[] contents = currentM.getBodyContent().split(":-");
                ArrayList<WDT> multimediaList = new ArrayList<WDT>();
                multimediaList.clear();
                multimediaView.setAdapter(null);
                for (int i = 0; i < contents.length; i++) {
                    WDT wdt = new WDT();
                    if (contents[i].startsWith("s:")){
                        wdt.setBodyType(Constants.SONG);
                        String[] doc = contents[i].split(":");
                        String music_name = doc[1];
                        wdt.setBodyContent(music_name);
                        multimediaList.add(wdt);
                    }
                    else if (contents[i].startsWith("i:")){
                        wdt.setBodyType(Constants.IMAGE);
                        String[] doc = contents[i].split(":");
                        String image_name = doc[1];
                        wdt.setBodyContent(image_name);
                        multimediaList.add(wdt);
                    }
                    else if (contents[i].startsWith("v:")) {
                        wdt.setBodyType(Constants.VIDEO);
                        String[] doc = contents[i].split(":");
                        String video_name = doc[1];
                        wdt.setBodyContent(video_name);
                        multimediaList.add(wdt);
                    }
                    else if(contents[i].contains(" ")){
                        wdt.setBodyType(Constants.TEXT);
                        String text = contents[i];
                        wdt.setBodyContent(text);
                        multimediaList.add(wdt);
                    }
            }
            adapter = new WeekContentListAdapter(WeekContentActivity.this, multimediaList);
            adapter.setCustomButtonListner(WeekContentActivity.this);
            multimediaView.setAdapter(adapter);
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(WeekContentActivity.this, MainActivity.class));
        finish();
    }
    public String getWeekSummaryValue(int value) {
        String weekSummary = "";
        if (value <= 5) {
            weekSummary = "1ST HALF";
        } else {
            weekSummary = "2ND HALF";
        }
        return weekSummary;
    }

    public void runTimer(int secondsToRun) {
        timer = ValueAnimator.ofInt(secondsToRun);
        timer.setDuration(secondsToRun * 1000).setInterpolator(new LinearInterpolator());
        timer.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int elapsedSeconds = (int) animation.getAnimatedValue();
                int minutes = elapsedSeconds / 60;
                int seconds = elapsedSeconds % 60;
                timerCounter.setText(String.format("%d:%2d", minutes, seconds));
            }
        });
        timer.start();
    }

    //convert datetime to timestamp
    //convertdatetime to timestamp
    public int componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (int) (c.getTimeInMillis() / 1000L);
    }

    //save timestamp
    public void saveReport(String title, String expectedTime, String actualTime) {
        //get current timestamp
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mDay = c.get(Calendar.DAY_OF_WEEK);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);
        dateTimetoMillis = Integer.toString(componentTimeToTimestamp(mYear, mMonth, mDay, mHour, mMinute));
        //save report to db
        ContentValues values = new ContentValues();
        values.put(Constants.STUDY_MATERIAL, title);
        values.put(Constants.EXPECTED_TIME, expectedTime);
        values.put(Constants.ACTUAL_TIME, actualTime);
        values.put(Constants.KEY_SUBJECT_ID, getIntent().getStringExtra(Constants.KEY_SUBJECT_ID));
        values.put(Constants.KEY_CLASS_ID, getIntent().getStringExtra(Constants.KEY_CLASS_ID));
        values.put(Constants.KEY_TERM_ID, getIntent().getStringExtra(Constants.KEY_TERM_ID));
        values.put(Constants.KEY_DAY_ID, getIntent().getStringExtra(Constants.KEY_DAY_ID));
        values.put(Constants.KEY_WEEK_ID, getIntent().getStringExtra(Constants.KEY_WEEK_ID));
        values.put(Constants.TIMESTAMP, dateTimetoMillis);
        //save report in arraylist
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.STUDY_MATERIAL, title);
        map.put(Constants.EXPECTED_TIME, expectedTime);
        map.put(Constants.ACTUAL_TIME, actualTime);
        reportObjects.add(map);
        db.insert(Constants.REPORT_TABLE, values);

    }
    /**
     * Function to play a song
     */
    public void playSong(AssetFileDescriptor afd) {
        // Play song
        try {
            mp.reset();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);
            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mp.release();
        } catch (NullPointerException ex) {
        }
    }

    @Override
    public void onButtonClickListner(int position, final String value, String buttonClicked, WeekContentListAdapter.ViewHolder holder) {
        if(buttonClicked.equals(Constants.SONG)) {
            //Create music dialog
            dialog = new Dialog(WeekContentActivity.this);
            dialog.setContentView(R.layout.music_layout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setTitle(getString(R.string.audio_title));
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                    if(mp != null) {
                        mp.release();
                        mp = null;
                    }
                    //reinitialize media player
                }
            });
            //define views in dialog
            final ImageButton btnPlay = (ImageButton) dialog.findViewById(R.id.btnPlay);
            final ImageButton btnBackward = (ImageButton) dialog.findViewById(R.id.btnBackward);
            final ImageButton btnForward = (ImageButton) dialog.findViewById(R.id.btnForward);
            Button closeDialog = (Button) dialog.findViewById(R.id.close);
            songCurrentDurationLabel = (TextView) dialog.findViewById(R.id.songCurrentDurationLabel);
            songTotalDurationLabel = (TextView) dialog.findViewById(R.id.songTotalDurationLabel);
            songProgressBar = (SeekBar) dialog.findViewById(R.id.songProgressBar);
            utils = new custom.Utilities();
            // Listeners
            mp = new MediaPlayer();
            songProgressBar.setOnSeekBarChangeListener(this); // Important
            mp.setOnCompletionListener(this); // Important
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    mp.release();
                    //reinitialize media player
                    mp = null;
                }
            });
            /**
             * Play button click event
             * plays a song and changes button to pause image
             * pauses a song and changes button to play image
             * */
            btnPlay.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // check for already playing
                            try {
                                if (mp.isPlaying()) {
                                    if (mp != null) {
                                        mp.pause();
                                        // Changing button image to play button
                                        btnPlay.setImageResource(R.drawable.img_btn_play);
                                    }
                                } else {
                                    // Resume song
                                    if (mp != null) {
                                        AssetFileDescriptor afd = getApplicationContext().getAssets().openFd(Constants.CONTENT_AUDIO_FOLDER+value);
                                        mp.reset();
                                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                        mp.prepare();
                                        mp.start();
                                        // Changing Button Image to pause image
                                        btnPlay.setImageResource(R.drawable.btn_pause);
                                        // set Progress bar values
                                        songProgressBar.setProgress(0);
                                        songProgressBar.setMax(100);
                                        // Updating progress bar
                                        updateProgressBar();
                                    }
                                }
                            } catch (IOException ex) {
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Resources.NotFoundException ex) {
                                Toast.makeText(getApplicationContext(), "Resource not found in this device", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            /**
             * Forward button click event
             * Forwards song specified seconds
             * */
            btnForward.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // get current song position
                    int currentPosition = mp.getCurrentPosition();
                    // check if seekForward time is lesser than song duration
                    if (currentPosition + seekForwardTime <= mp.getDuration()) {
                        // forward song
                        mp.seekTo(currentPosition + seekForwardTime);
                    } else {
                        // forward to end position
                        mp.seekTo(mp.getDuration());
                    }
                }
            });

            /**
             * Backward button click event
             * Backward song to specified seconds
             * */
            btnBackward.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // get current song position
                    int currentPosition = mp.getCurrentPosition();
                    // check if seekBackward time is greater than 0 sec
                    if (currentPosition - seekBackwardTime >= 0) {
                        // forward song
                        mp.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        // backward to starting position
                        mp.seekTo(0);
                    }
                }
            });
            dialog.show();
        }
        else if(buttonClicked.equals(Constants.VIDEO)){
            final Dialog dialog = new Dialog(WeekContentActivity.this);
            dialog.setContentView(R.layout.video_layout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle(getString(R.string.video_player));
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            videoView = (VideoView) dialog.findViewById(R.id.video);
            final ImageButton btnPlay = (ImageButton) dialog.findViewById(R.id.btnPlay);
            final ImageButton btnForward = (ImageButton) dialog.findViewById(R.id.btnForward);
            final ImageButton btnBackward = (ImageButton) dialog.findViewById(R.id.btnBackward);
            Button close = (Button) dialog.findViewById(R.id.close);
            try {
                mp = new MediaPlayer();
                SurfaceHolder _holder = videoView.getHolder();
                _holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                _holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {
                        mp.setDisplay(surfaceHolder);
                    }
                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    }
                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        if(mp != null)
                             mp.setDisplay(null);
                    }
                });
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd(Constants.CONTENT_VIDEO_FOLDER+value);
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mp.prepareAsync();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            songCurrentDurationLabel = (TextView) dialog.findViewById(R.id.songCurrentDurationLabel);
            songTotalDurationLabel = (TextView) dialog.findViewById(R.id.songTotalDurationLabel);
            songProgressBar = (SeekBar) dialog.findViewById(R.id.songProgressBar);
            utils = new custom.Utilities();
            // Listeners
            songProgressBar.setOnSeekBarChangeListener(this); // Important
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mp.stop();
                    if(mp != null)
                        mp.setDisplay(null);
                    dialog.cancel();
                }
            });
            btnBackward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get current song position
                    int currentPosition = mp.getCurrentPosition();
                    // check if seekBackward time is greater than 0 sec
                    if (currentPosition - seekBackwardTime >= 0) {
                        // forward song
                        mp.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        // backward to starting position
                        mp.seekTo(0);
                    }
                }
            });
            btnForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get current song position
                    int currentPosition = mp.getCurrentPosition();
                    // check if seekForward time is lesser than song duration
                    if(currentPosition + seekForwardTime <= mp.getDuration()) {
                        // forward song
                        mp.seekTo(currentPosition + seekForwardTime);
                    } else {
                        // forward to end position
                        mp.seekTo(mp.getDuration());
                    }
                }
            });
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mp.isPlaying()) {
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.img_btn_play);
                    } else {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.btn_pause);
                        songProgressBar.setProgress(0);
                        songProgressBar.setMax(100);
                        updateProgressBar();
                    }
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {}

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = 0;
        if(mp != null){
            totalDuration = mp.getDuration();
        }
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
        // forward or backward to certain seconds
        if(mp != null) {
            mp.seekTo(currentPosition);
        }
        // update timer progress again
        updateProgressBar();
    }
    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = 0;
                long currentDuration = 0;
                if(mp != null){
                    totalDuration = mp.getDuration();
                    currentDuration = mp.getCurrentPosition();
                }
                // Displaying Total Duration time
                songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setProgress(progress);
                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }catch (IllegalStateException ex){}
        }
    };
}
