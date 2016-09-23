package planner.school.com.myschoolplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import planner.school.com.myschoolplanner.R;

/**
 * Created by Ranjitha on 12/1/2015.
 */
public class GenricAdapter extends BaseAdapter {

    ArrayList events;
    private Context mContext;

    public GenricAdapter(Context context, ArrayList events){

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

        Event event = (Event)events.get(position);
        if(event.getTitle().equals("CLASS")){
            return getClassView(position);
        }else if(event.getTitle().equals("TASK")){
            return getTaskView(position);
        }else if(event.getTitle().equals("EVENT")){
            return getEventsView(position);
        }else if(event.getTitle().equals("EXAM")){
            return getExamsView(position);
        }

        return null;
    }

    private View getClassView(int position){
        LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.class_list_item,null);

        TextView nameTV = (TextView)v.findViewById(R.id.class_name_tv);
        TextView repeaterTV = (TextView)v.findViewById(R.id.repeater_tv);
        TextView timeTV = (TextView)v.findViewById(R.id.time_tv);
        TextView descTV = (TextView)v.findViewById(R.id.desc_tv);

        try {
            Event event = (Event) events.get(position);
            JSONObject classJson = new JSONObject(event.getDescription());
            nameTV.setText(classJson.getString("name")+":");
            repeaterTV.setText("("+classJson.getString("repeat")+")");
            timeTV.setText(classJson.getString("start_time") + " - "+classJson.getString("end_time"));
            descTV.setText(classJson.getString("desc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    private View getTaskView(int position){
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

    private View getExamsView(int position){
        LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.exams_list_item,null);

        TextView nameTV = (TextView)v.findViewById(R.id.class_name_tv);
        TextView dateTV = (TextView)v.findViewById(R.id.date_tv);
        TextView timeTV = (TextView)v.findViewById(R.id.time_tv);
        TextView locationTv = (TextView)v.findViewById(R.id.location_tv);
        TextView descTV = (TextView)v.findViewById(R.id.desc_tv);
        TextView durationTV = (TextView)v.findViewById(R.id.interested_tv);

        try {
            Event event = (Event) events.get(position);
            JSONObject eventJson = new JSONObject(event.getDescription());
            nameTV.setText(eventJson.getString("name")+":");
            dateTV.setText(eventJson.getString("date"));
            timeTV.setText(eventJson.getString("time"));
            descTV.setText(eventJson.getString("desc"));
            locationTv.setText(eventJson.getString("location"));
            durationTV.setText(eventJson.getString("duration") + " mins");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    private View getEventsView(int position){
        LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.events_list_item,null);

        TextView nameTV = (TextView)v.findViewById(R.id.class_name_tv);
        TextView dateTV = (TextView)v.findViewById(R.id.date_tv);
        TextView timeTV = (TextView)v.findViewById(R.id.time_tv);
        TextView locationTv = (TextView)v.findViewById(R.id.location_tv);
        TextView descTV = (TextView)v.findViewById(R.id.desc_tv);
        TextView rsvpText = (TextView)v.findViewById(R.id.interested_tv);

        try {
            Event event = (Event) events.get(position);
            JSONObject eventJson = new JSONObject(event.getDescription());
            nameTV.setText(eventJson.getString("name")+":");
            dateTV.setText(eventJson.getString("date"));
            timeTV.setText(eventJson.getString("time"));
            descTV.setText(eventJson.getString("desc"));
            locationTv.setText(eventJson.getString("location"));
            if(eventJson.getBoolean("rsvp")) {
                rsvpText.setText("Going");
            }else{
                rsvpText.setText("Not Going");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}

