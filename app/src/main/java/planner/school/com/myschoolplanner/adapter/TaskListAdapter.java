package planner.school.com.myschoolplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import planner.school.com.myschoolplanner.R;

/**
 * Created by Ranjitha on 12/1/2015.
 */
public class TaskListAdapter  extends BaseAdapter {

    ArrayList events;
    private Context mContext;

    public TaskListAdapter(Context context, ArrayList events){

        this.mContext = context;
        this.events = events;


    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
            return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.class_list_item,null);

        TextView nameTV = (TextView)v.findViewById(R.id.class_name_tv);
        TextView repeaterTV = (TextView)v.findViewById(R.id.repeater_tv);
        TextView timeTV = (TextView)v.findViewById(R.id.time_tv);
        TextView descTV = (TextView)v.findViewById(R.id.desc_tv);

        try {
            Event event = (Event) events.get(position);
            JSONObject eventJson = new JSONObject(event.getDescription());
            nameTV.setText(eventJson.getString("name")+":");
            repeaterTV.setText(eventJson.getString("time"));
            timeTV.setText(eventJson.getString("date"));
            descTV.setText(eventJson.getString("desc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}

