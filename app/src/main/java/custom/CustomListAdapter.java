package custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.practicer.teacherapp.R;

import java.util.ArrayList;

/**
 * Created by HighStrit on 18/03/2017.
 */
public class CustomListAdapter extends ArrayAdapter<WDT> {
    private final Activity context;
    private ArrayList<WDT> wdt;

    public CustomListAdapter(Activity context, ArrayList<WDT> wdt) {
        // TODO Auto-generated constructor stub
        super(context, R.layout.week_summary_list, wdt);
        this.context=context;
        this.wdt = wdt;
    }

    @SuppressLint({ "InflateParams", "ViewHolder" })
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.week_summary_list, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.week_summary_title);
        TextView body = (TextView) rowView.findViewById(R.id.week_summary_content);
        ImageView image = (ImageView) rowView.findViewById(R.id.week_summary_image);
        if(wdt.get(position).getTitle().equals(Constants.ALPHABETS)){
            title.setText("");
            image.setVisibility(View.VISIBLE);
            String name = wdt.get(position).getContent();
            int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
           // Drawable drawable = context.getResources().getDrawable(id, null);
            image.setImageResource(id);
        }
        else{
            title.setText(wdt.get(position).getBodyTitle());
            body.setText(wdt.get(position).getContent());
        }
        return rowView;
    }
}
