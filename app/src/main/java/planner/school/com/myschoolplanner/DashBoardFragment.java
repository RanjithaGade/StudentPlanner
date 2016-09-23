package planner.school.com.myschoolplanner;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import planner.school.com.myschoolplanner.activity.MainActivity;
import planner.school.com.myschoolplanner.adapter.EventsAdapter;
import planner.school.com.myschoolplanner.adapter.GenricAdapter;
import planner.school.com.myschoolplanner.utils.Utils;

/**
 * Created by Ranjitha on 11/25/2015.
 */
public class DashBoardFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTodayClsTv;
    private TextView mTodayTv2;

    private TextView mTodayTaskTv;
    private TextView mTomorrowTaskTv;

    private TextView mTodayEventsTv;
    private TextView mTomorrowEventsTv;

    private TextView mTodayExamsTV;
    private TextView mWeekExamsTv;

    private TextView mNoClassesTV;
    private TextView mNoTaskTV;

    private ListView mClsExamListView;
    private ListView mTaskEventsListView;

    private ArrayList<Event> clsXamList = new ArrayList<>();
    private ArrayList<Event> taskEventList = new ArrayList<>();


    private int tdyT1;
    private int tdyT2;

    private int taskT1;
    private int taskT2;

    private int eventT1;
    private int eventT2;

    private int examT1;
    private int examT2;

    private int todayDate;
    private int twoDaysLater;
    private int oneWeekLater;

    public static DashBoardFragment newInstance(int sectionNumber) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DashBoardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        initializeView(rootView);
        String banner = getBannerString();
        TextView tv = (TextView)rootView.findViewById(R.id.section_label);
        tv.setText(banner);
        return rootView;
    }

    private void initializeView(View v) {

        mTodayClsTv = (TextView)v.findViewById(R.id.today_txt_1);
        mTodayTv2  = (TextView)v.findViewById(R.id.today_txt_2);

        mTodayTaskTv  = (TextView)v.findViewById(R.id.tasks_txt_1);
        mTomorrowTaskTv  = (TextView)v.findViewById(R.id.tasks_txt_2);

        mTodayEventsTv  = (TextView)v.findViewById(R.id.events_txt_1);
        mTomorrowEventsTv  = (TextView)v.findViewById(R.id.events_txt_2);

        mTodayExamsTV  = (TextView)v.findViewById(R.id.exams_txt_1);
        mWeekExamsTv  = (TextView)v.findViewById(R.id.exams_txt_2);

        mNoClassesTV = (TextView)v.findViewById(R.id.no_classes_tv);
        mNoTaskTV = (TextView)v.findViewById(R.id.no_tasks_tv);

        mClsExamListView = (ListView)v.findViewById(R.id.classes_list);
        mTaskEventsListView = (ListView)v.findViewById(R.id.tasks_list);

        TimeZone tz = TimeZone.getDefault();
        Calendar cTemp = Calendar.getInstance();
        todayDate = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));
        cTemp.add(Calendar.DAY_OF_YEAR,1);
        twoDaysLater = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));
        cTemp.add(Calendar.DAY_OF_YEAR,6);
        oneWeekLater = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));



    }

    @Override
    public void onResume() {
        super.onResume();
        GetEvents getEvents = new GetEvents();
        getEvents.execute();

    }

    private void updateDashBoardValues(){
        mTodayClsTv.setText(tdyT1+" classes today");
        mTodayTv2.setText(tdyT2+" exams today");

        mTodayTaskTv.setText(taskT1+" tasks due today");
        mTomorrowTaskTv.setText(taskT2+" tasks due tomorrow");

        mTodayEventsTv.setText(eventT1+" events today");
        mTomorrowEventsTv.setText(eventT2+" events tomorrow");

        mTodayExamsTV.setText(examT1+" exams today");
        mWeekExamsTv.setText(examT2+" exams this week");



    }

    private String getBannerString() {
        String str = "Today - ";
        Calendar cal = Calendar.getInstance();
        int val = cal.get(Calendar.DAY_OF_WEEK);
        switch (val){
            case 1:
                str = str + "Sunday,";
                break;
            case 2:
                str = str + "Monday,";
                break;
            case 3:
                str = str + "Tuesday,";
                break;
            case 4:
                str = str + "Wednesday,";
                break;
            case 5:
                str = str + "Thursday,";
                break;
            case 6:
                str = str + "Friday,";
                break;
            case 7:
                str = str + "Saturday,";
                break;

        }
        int monthVal = cal.get(Calendar.MONTH);
        switch (monthVal){
            case 0:
                str = str + " January ";
                break;
            case 1:
                str = str + " February ";
                break;
            case 2:
                str = str + " March ";
                break;
            case 3:
                str = str + " April ";
                break;
            case 4:
                str = str + " May ";
                break;
            case 5:
                str = str + " June ";
                break;
            case 6:
                str = str + " July ";
                break;
            case 7:
                str = str + " August ";
                break;
            case 8:
                str = str + " September ";
                break;
            case 9:
                str = str + " October ";
                break;
            case 10:
                str = str + " November ";
                break;
            case 11:
                str = str + " December ";
                break;
        }

        int day = cal.get(Calendar.DAY_OF_MONTH);
        str = str+day+", ";
        int year = cal.get(Calendar.YEAR);
        str = str+year;

        return str;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    private class GetEvents extends AsyncTask<Void,Void,Void> {
        ArrayList<Event> events = new ArrayList<Event>();
        @Override
        protected Void doInBackground(Void... params) {

            clsXamList.clear();
            taskEventList.clear();

            Cursor c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI,new String[] {CalendarProvider.ID,CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION,CalendarProvider.LOCATION,CalendarProvider.START,CalendarProvider.END,CalendarProvider.COLOR},"?="+CalendarProvider.EVENT +" AND ?>="+CalendarProvider.START_DAY+" AND "+ CalendarProvider.END_DAY+">=?",
                    new String[] {"CLASS",String.valueOf(todayDate),String.valueOf(todayDate)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        clsXamList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            tdyT1 = events.size();
            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI,new String[] {CalendarProvider.ID,CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION,CalendarProvider.LOCATION,CalendarProvider.START,CalendarProvider.END,CalendarProvider.COLOR},"?="+CalendarProvider.EVENT +" AND ?>="+CalendarProvider.START_DAY+" AND "+ CalendarProvider.END_DAY+">=?",
                    new String[] {"EXAM",String.valueOf(todayDate),String.valueOf(todayDate)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        clsXamList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            tdyT2 = events.size();
            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?=" + CalendarProvider.EVENT + " AND ?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                    new String[]{"EXAM", String.valueOf(oneWeekLater), String.valueOf(twoDaysLater)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        clsXamList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            examT1 = tdyT2;
            examT2 = examT1+events.size();
            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?=" + CalendarProvider.EVENT + " AND ?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                    new String[]{"TASK", String.valueOf(todayDate), String.valueOf(todayDate)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        taskEventList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            taskT1 = events.size();

            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?=" + CalendarProvider.EVENT + " AND ?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                    new String[]{"TASK", String.valueOf(twoDaysLater), String.valueOf(twoDaysLater)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        taskEventList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            taskT2= events.size();
            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?=" + CalendarProvider.EVENT + " AND ?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                    new String[]{"EVENT", String.valueOf(todayDate), String.valueOf(todayDate)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        taskEventList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            eventT1 = events.size();

            events.clear();

            c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                            CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?=" + CalendarProvider.EVENT + " AND ?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                    new String[]{"EVENT", String.valueOf(twoDaysLater), String.valueOf(twoDaysLater)}, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                        event.setName(c.getString(1));
                        event.setDescription(c.getString(2));
                        event.setLocation(c.getString(3));
                        event.setColor(c.getInt(6));
                        events.add(event);
                        taskEventList.add(event);

                    }while(c.moveToNext());
                }
                c.close();
            }
            eventT2= events.size();
            events.clear();



            return null;
        }

        protected void onPostExecute(Void par){
            if (taskEventList.size() != 0) {
                GenricAdapter adapter = new GenricAdapter(getContext(), taskEventList);
                mTaskEventsListView.setAdapter(adapter);
                Utils.setListViewHeightBasedOnChildren(mTaskEventsListView);
                mNoTaskTV.setVisibility(View.INVISIBLE);

            }

            if (clsXamList.size() != 0) {
                GenricAdapter adapter = new GenricAdapter(getContext(), clsXamList);
                mClsExamListView.setAdapter(adapter);
                Utils.setListViewHeightBasedOnChildren(mClsExamListView);
                mNoClassesTV.setVisibility(View.INVISIBLE);

            }

            updateDashBoardValues();
        }

    }



}
