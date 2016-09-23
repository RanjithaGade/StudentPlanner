package planner.school.com.myschoolplanner.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import planner.school.com.myschoolplanner.R;

/**
 * Created by Ranjitha on 12/1/2015.
 */
public class AddExamsActivity extends Activity implements View.OnClickListener{

    private EditText mTitleEt;
    private EditText mDescEt;
    private TextView mDateVal;
    private TextView mTimeVal;
    private EditText mDurationET;
    private ToggleButton mReminderToggle;
    private TextView mWhenTv;
    private EditText mLocEt;
    private Spinner mReminderSpinner;
    private Button mSave;


    private int startHr = 9;
    private int startMin = 30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exam_layout);
        initializeViews();

    }

    private void initializeViews() {
        mTitleEt = (EditText)findViewById(R.id.title_et);
        mDescEt = (EditText)findViewById(R.id.desc_et);
        mDateVal = (TextView)findViewById(R.id.date_val_tv);
        mTimeVal = (TextView)findViewById(R.id.time_val_tv);
        mLocEt = (EditText)findViewById(R.id.loc_et);
        mDurationET = (EditText)findViewById(R.id.duration_et);
        mReminderToggle = (ToggleButton)findViewById(R.id.reminder_toggle);
        mWhenTv = (TextView)findViewById(R.id.when_tv);
        mReminderSpinner = (Spinner)findViewById(R.id.reminder_time_spinner);
        mSave = (Button)findViewById(R.id.save);

        List<String> list = new ArrayList<String>();
        list.add("before 5 min");
        list.add("before 15 min");
        list.add("before 30 min");
        list.add("before 1 hr");
        list.add("before 4 hr");
        list.add("before 12 hr");
        list.add("before 1 day");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReminderSpinner.setAdapter(dataAdapter);


        mDateVal.setOnClickListener(this);
        mTimeVal.setOnClickListener(this);
        mReminderToggle.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.date_val_tv:
                DateDialog();
                break;
            case R.id.time_val_tv:
                TimeDialog();
                break;
            case R.id.reminder_toggle:
                if(mReminderToggle.isChecked()){
                    mWhenTv.setVisibility(View.VISIBLE);
                    mReminderSpinner.setVisibility(View.VISIBLE);
                }else{
                    mWhenTv.setVisibility(View.GONE);
                    mReminderSpinner.setVisibility(View.GONE);
                }
            case R.id.save:
                save();
                break;
        }

    }

    private void save() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject taskJson = new JSONObject();
        try {
            taskJson.put("name",mTitleEt.getText().toString());
            taskJson.put("desc",mDescEt.getText().toString());
            taskJson.put("date",mDateVal.getText().toString());
            taskJson.put("time",mTimeVal.getText().toString());
            taskJson.put("location",mLocEt.getText().toString());
            taskJson.put("duration",mDurationET.getText().toString());
            Date date = dateFormat.parse(mDateVal.getText().toString());
            if(mReminderToggle.isChecked()){

            }
            addTask(taskJson, date);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void DateDialog(){


        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {

                mDateVal.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);


            }};
        Calendar cal = Calendar.getInstance();


        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpDialog.show();

    }


    public void TimeDialog(){


        TimePickerDialog.OnTimeSetListener listener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {

                String AM_PM = "AM";
                if(hr > 12){
                    hr = hr -12;
                    AM_PM = "PM";
                }

                mTimeVal.setText(hr+":"+min+" "+AM_PM);
                startHr = hr;
                startMin = min;
                if(AM_PM.equals("PM")){
                    startHr = startHr +12;
                }

            }
        };
        Calendar cal = Calendar.getInstance();


        TimePickerDialog tpDialog=new TimePickerDialog(this,listener,startHr,startMin,cal.get(Calendar.AM_PM)!=0);
        tpDialog.show();

    }

    private void addTask(JSONObject newClass, Date date) throws  Exception{
        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_RED);
        values.put(CalendarProvider.DESCRIPTION, newClass.toString());
        values.put(CalendarProvider.LOCATION, newClass.getString("name"));
        values.put(CalendarProvider.EVENT, "EXAM");
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

        cal.set(year, month, day, startHr+1, startMin);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
    }
}
