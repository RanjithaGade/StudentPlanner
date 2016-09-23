package planner.school.com.myschoolplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import planner.school.com.myschoolplanner.R;

/**
 * Created by Ranjitha on 11/30/2015.
 */
public class ClassesAdapter extends BaseAdapter {

    private JSONArray mClassArray;
    private Context mContext;

    public ClassesAdapter(Context context, JSONArray classesArray){

        this.mContext = context;
        this.mClassArray = classesArray;


    }

    @Override
    public int getCount() {
        return mClassArray.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mClassArray.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            JSONObject classJson = new JSONObject(mClassArray.get(position).toString());
            nameTV.setText(classJson.getString("name")+":");
            repeaterTV.setText("("+classJson.getString("repeat")+")");
            timeTV.setText(classJson.getString("start_time") + " - "+classJson.getString("end_time"));
            descTV.setText(classJson.getString("desc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
