package custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.practicer.teacherapp.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by HighStrit on 18/03/2017.
 */
public class WeekContentListAdapter extends ArrayAdapter<WDT>{
    private final Activity context;
    private ArrayList<WDT> wdt;
    customButtonListener customListner;
    public WeekContentListAdapter(Activity context, ArrayList<WDT> wdt) {
        // TODO Auto-generated constructor stub
        super(context, R.layout.week_content_list, wdt);
        this.context=context;
        this.wdt = wdt;
    }

    @SuppressLint({ "InflateParams", "ViewHolder" })
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.week_content_list, null, true);
            viewHolder = new ViewHolder();
            viewHolder.body_content = (TextView) view.findViewById(R.id.week_content_content);
            viewHolder.image = (ImageView) view.findViewById(R.id.week_content_image);
            viewHolder.frame = (FrameLayout) view.findViewById(R.id.musicFrame);
            viewHolder.videoFrame = (FrameLayout) view.findViewById(R.id.videoFrame);
            viewHolder.btnPlay = (ImageButton) view.findViewById(R.id.play);
            viewHolder.btnPlayVideo = (ImageButton) view.findViewById(R.id.playVideo);
            viewHolder.audioTitle = (TextView) view.findViewById(R.id.music_title);
            viewHolder.videoTitle = (TextView) view.findViewById(R.id.video_title);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(wdt.get(position).getBodyType().equals(Constants.IMAGE)){
            viewHolder.image.setVisibility(View.VISIBLE);
            String name = wdt.get(position).getBodyContent();
//            File mediaStorageDir = new File(
//                    Environment
//                            .getExternalStorageDirectory().getAbsolutePath()+ Constants.CONTENT_IMAGE_FOLDER);
//            // Create the storage directory if it does not exist
//            if (mediaStorageDir.exists()){
//                Bitmap b= BitmapFactory.decodeFile(mediaStorageDir.getAbsolutePath() + File.separator
//                        + name + ".png");
//                viewHolder.image.setImageBitmap(b);
//            }
           // int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            Bitmap bitmap = getBitmapFromAssets("images/t1p4w6d1maths.png");
            viewHolder.image.setImageBitmap(bitmap);
        }
        else if(wdt.get(position).getBodyType().equalsIgnoreCase(Constants.TEXT)){
            viewHolder.body_content.setText(wdt.get(position).getBodyContent());
        }

        else if(wdt.get(position).getBodyType().equalsIgnoreCase(Constants.SONG)){
            viewHolder.frame.setVisibility(View.VISIBLE);
            viewHolder.audioTitle.setText(wdt.get(position).getBodyContent());
        }
        else if(wdt.get(position).getBodyType().equalsIgnoreCase(Constants.VIDEO)){
            viewHolder.videoFrame.setVisibility(View.VISIBLE);
            viewHolder.videoTitle.setText(wdt.get(position).getBodyContent());
        }
        viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customListner.onButtonClickListner(position, wdt.get(position).getBodyContent(), Constants.SONG, viewHolder);
            }
        });

        viewHolder.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customListner.onButtonClickListner(position, wdt.get(position).getBodyContent(), Constants.VIDEO, viewHolder);
            }
        });
        return view;
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value, String buttonClicked, ViewHolder holder);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    public class ViewHolder {
        ImageButton btnPlay;
        ImageButton btnPlayVideo;
        TextView audioTitle;
        TextView videoTitle;
        TextView body_content;
        ImageView image;
        FrameLayout frame;
        FrameLayout videoFrame;
    }

    public Bitmap getBitmapFromAssets(String fileName){
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
