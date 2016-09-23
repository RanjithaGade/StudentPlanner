package planner.school.com.myschoolplanner.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import planner.school.com.myschoolplanner.utils.AppSharedPreference;
import planner.school.com.myschoolplanner.R;

/**
 * Created by Ranjitha on 11/30/2015.
 */
public class AddClassActivity extends Activity implements View.OnClickListener{

    private EditText mNameET;
    private EditText mDescET;
    private CheckBox mon;
    private CheckBox tue;
    private CheckBox wed;
    private CheckBox thu;
    private CheckBox fri;
    private CheckBox sat;
    private CheckBox sun;



    private TextView mStartDateTV;
    private TextView mEndDateTV;

    private TextView mStartTimeTV;
    private TextView mEndTimeTV;

    private int startHr = 9;
    private int startMin = 30;

    private int endHr = 10;
    private int endMin = 30;

    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_class_layout);



        initializeViews();

    }

    private void initializeViews() {

        mNameET = (EditText)findViewById(R.id.name_et);
        mDescET = (EditText)findViewById(R.id.desc_et);

        mStartDateTV = (TextView)findViewById(R.id.start_date_tv);
        mEndDateTV  = (TextView)findViewById(R.id.end_date_tv);

        mStartTimeTV = (TextView)findViewById(R.id.start_time_tv);
        mEndTimeTV = (TextView)findViewById(R.id.end_time_tv);

        mSave = (Button)findViewById(R.id.save);

        mon = (CheckBox)findViewById(R.id.mon);
        tue = (CheckBox)findViewById(R.id.tue);
        wed = (CheckBox)findViewById(R.id.wed);
        thu = (CheckBox)findViewById(R.id.thu);
        fri = (CheckBox)findViewById(R.id.fri);
        sat = (CheckBox)findViewById(R.id.sat);
        sun = (CheckBox)findViewById(R.id.sun);



        mSave.setOnClickListener(this);
        mStartDateTV.setOnClickListener(this);
        mEndDateTV.setOnClickListener(this);
        mStartTimeTV.setOnClickListener(this);
        mEndTimeTV.setOnClickListener(this);
    }


    public void DateDialog(final boolean  isStart){


        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                if(isStart) {
                    mStartDateTV.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                }else{
                    mEndDateTV.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                }

            }};
        Calendar cal = Calendar.getInstance();
        if(!isStart){
            cal.add(Calendar.MONTH,4);
        }

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpDialog.show();

    }


    public void TimeDialog(final boolean  isStart){


        TimePickerDialog.OnTimeSetListener listener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {

                String AM_PM = "AM";
                    if(hr > 12){
                        hr = hr -12;
                        AM_PM = "PM";
                    }
                if(isStart) {
                    mStartTimeTV.setText(hr+":"+min+" "+AM_PM);
                    startHr = hr;
                    startMin = min;
                    if(AM_PM.equals("PM")){
                        startHr = startHr +12;
                    }
                }else{
                    mEndTimeTV.setText(hr+":"+min+" "+AM_PM);
                    endHr = hr;
                    endMin = min;
                    if(AM_PM.equals("PM")){
                        endHr = endHr +12;
                    }
                }
            }
        };
        Calendar cal = Calendar.getInstance();
        TimePickerDialog tpDialog=new TimePickerDialog(this,listener,startHr,startMin,cal.get(Calendar.AM_PM)!=0);
        if(!isStart){
            tpDialog=new TimePickerDialog(this,listener,endHr,endMin,cal.get(Calendar.AM_PM)!=0);
        }


        tpDialog.show();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.start_date_tv:
                DateDialog(true);
                break;
            case R.id.end_date_tv:
                DateDialog(false);
                break;
            case R.id.start_time_tv:
                TimeDialog(true);
                break;
            case R.id.end_time_tv:
                TimeDialog(false);
                break;
            case R.id.save:
                saveNewClass();
                break;
        }

    }

    private void saveNewClass() {
        String classesJson = AppSharedPreference.getClassesArray(this);
        try {
            JSONArray classesArray = new JSONArray(classesJson);
            JSONObject newClass = new JSONObject();
            newClass.put("name",mNameET.getText().toString());
            newClass.put("desc",mDescET.getText().toString());
            newClass.put("start_time",mStartTimeTV.getText().toString());
            newClass.put("end_time",mEndTimeTV.getText().toString());
            newClass.put("start_date",mStartDateTV.getText().toString());
            newClass.put("end_date",mEndDateTV.getText().toString());
            newClass.put("repeat",getRepeatWeeks());

            classesArray.put(newClass);



            addClassEvents(newClass);
            AppSharedPreference.setClassesArray(this, classesArray.toString());
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addClassEvents(JSONObject newClass) throws  Exception{

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(newClass.getString("start_date"));
        Date endDate = dateFormat.parse(newClass.getString("end_date"));
        String repeater = newClass.getString("repeat");
        String[] weekSplits = repeater.split(",");
        List weeksList = Arrays.asList(weekSplits);
        Date currentDate = startDate;

        while (currentDate.compareTo(endDate) < 1 ){



            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            String dayOfWeek = getDayOfWeek(day);
            if(weeksList.contains(dayOfWeek)){
                addClassEvent(newClass,currentDate);
            }
            cal.add(Calendar.DAY_OF_YEAR,1);
            currentDate = cal.getTime();
        }



    }

    private void addClassEvent(JSONObject newClass,Date date) throws  Exception{
        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_BLUE);
        values.put(CalendarProvider.DESCRIPTION, newClass.toString());
        values.put(CalendarProvider.LOCATION, newClass.getString("name"));
        values.put(CalendarProvider.EVENT, "CLASS");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        int year = cal1.get(Calendar.YEAR);
        int month = cal1.get(Calendar.MONTH);
        int day = cal1.get(Calendar.DAY_OF_MONTH);




        Calendar cal = Calendar.getInstance();
        Calendar cTemp = Calendar.getInstance();
        cTemp.set(year, month, day);
        TimeZone tz = TimeZone.getDefault();
        int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));
        cal.set(year, month, day, startHr, startMin);
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, startDay);

        cal.set(year, month, day, endHr, endMin);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
    }

    private String getDayOfWeek(int day) {
        switch (day){


            case 1:
                return "sun";
            case 2:
                return "mon";
            case 3:
                return "tue";
            case 4:
                return "wed";
            case 5:
                return "thu";
            case 6:
                return "fri";
            case 7:
                return "sat";
        }
        return "err";
    }


    private String getRepeatWeeks(){
        String str = "";
        if(mon.isChecked()){
            str = str+"mon,";
        }
        if(tue.isChecked()){
            str = str+"tue,";
        }
        if(wed.isChecked()){
            str = str+"wed,";
        }
        if(thu.isChecked()){
            str = str+"thu,";
        }if(fri.isChecked()){
            str = str+"fri,";
        }if(sat.isChecked()){
            str = str+"sat,";
        }if(sun.isChecked()){
            str = str+"sun,";
        }

        if(!str.equals("")){
            str = str.substring(0,str.length()-1);
        }
        return str;



    }
}
