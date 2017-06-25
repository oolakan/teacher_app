package custom;

import android.annotation.SuppressLint;
import android.app.Activity;
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
public class ReportCustomListAdapter extends ArrayAdapter<WDT> {
    private final Activity context;
    private ArrayList<WDT> wdt;

    public ReportCustomListAdapter(Activity context, ArrayList<WDT> wdt) {
        // TODO Auto-generated constructor stub
        super(context, R.layout.report_list, wdt);
        this.context=context;
        this.wdt = wdt;
    }

    @SuppressLint({ "InflateParams", "ViewHolder" })
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.report_list, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.content_title);
        TextView expected_time = (TextView) rowView.findViewById(R.id.expected_time);
        TextView actual_time = (TextView) rowView.findViewById(R.id.actual_time);
        title.setText(wdt.get(position).getTitle());
        expected_time.setText(wdt.get(position).getExpectedTime());
        actual_time.setText(wdt.get(position).getActualTime());
        return rowView;
    }
}
