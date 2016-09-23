package planner.school.com.myschoolplanner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import java.util.ArrayList;

import planner.school.com.myschoolplanner.activity.AddTaskActivity;
import planner.school.com.myschoolplanner.activity.MainActivity;
import planner.school.com.myschoolplanner.adapter.TaskListAdapter;

/**
 * Created by Ranjitha on 11/29/2015.
 */
public class TasksFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int ADD_CLASS_ACTIVITY_CODE = 10;
    private ImageView addIcon;
    private ListView mListView;
    private TextView mNoClassTV;


    public static TasksFragment newInstance(int sectionNumber) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TasksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.classes_fragment, container, false);
//        TextView tv = (TextView)rootView.findViewById(R.id.section_label);
//        tv.setText("DashBoard");
        initializeView(rootView);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void initializeView(View v) {

        addIcon = (ImageView)v.findViewById(R.id.add_icon);
        mListView = (ListView)v.findViewById(R.id.classes_lv);
        mNoClassTV = (TextView)v.findViewById(R.id.no_classes_tv);
        GetEvents async = new GetEvents();
        async.execute();

        addIcon.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_icon:
                showAddTaskActivity();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GetEvents async = new GetEvents();
        async.execute();
    }


    private void showAddTaskActivity() {
        Intent addTaskActivity = new Intent(TasksFragment.this.getActivity(),AddTaskActivity.class);
        getActivity().startActivityForResult(addTaskActivity, ADD_CLASS_ACTIVITY_CODE);
    }


    private class GetEvents extends AsyncTask<Void,Void,Void> {
        ArrayList<Event> events = new ArrayList<Event>();
        @Override
        protected Void doInBackground(Void... params) {
            events.clear();
            Cursor c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI,new String[] {CalendarProvider.ID,CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION,CalendarProvider.LOCATION,CalendarProvider.START,CalendarProvider.END,CalendarProvider.COLOR},"?="+CalendarProvider.EVENT,
                    new String[] {"TASK"}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                    }while(c.moveToNext());
                }
                c.close();
            }

            return null;
        }

        protected void onPostExecute(Void par){
            if (events.size() != 0) {
                TaskListAdapter adapter = new TaskListAdapter(getContext(), events);
                mListView.setAdapter(adapter);
                mNoClassTV.setVisibility(View.INVISIBLE);

            }
        }

    }
}
