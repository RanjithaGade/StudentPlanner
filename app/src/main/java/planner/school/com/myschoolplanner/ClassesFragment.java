package planner.school.com.myschoolplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import planner.school.com.myschoolplanner.activity.AddClassActivity;
import planner.school.com.myschoolplanner.activity.MainActivity;
import planner.school.com.myschoolplanner.adapter.ClassesAdapter;
import planner.school.com.myschoolplanner.utils.AppSharedPreference;


/**
 * Created by Ranjitha on 11/29/2015.
 */
public class ClassesFragment extends Fragment implements  View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int ADD_CLASS_ACTIVITY_CODE = 10;
    private ImageView addIcon;
    private ListView mListView;
    private TextView mNoClassTV;

    public static ClassesFragment newInstance(int sectionNumber) {
        ClassesFragment fragment = new ClassesFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ClassesFragment() {
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
        try {
            String jsonArray = AppSharedPreference.getClassesArray(getContext());
            JSONArray classesArray = new JSONArray(jsonArray);
            if (classesArray.length() != 0) {
                ClassesAdapter adapter = new ClassesAdapter(getContext(), classesArray);
                mListView.setAdapter(adapter);
                mNoClassTV.setVisibility(View.INVISIBLE);

            }
        }catch (Exception e){
            e.printStackTrace();

        }
//        mListView.setAdapter();

        addIcon.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String jsonArray = AppSharedPreference.getClassesArray(getContext());
            JSONArray classesArray = new JSONArray(jsonArray);
            if (classesArray.length() != 0) {
                ClassesAdapter adapter = new ClassesAdapter(getContext(), classesArray);
                mListView.setAdapter(adapter);
                mNoClassTV.setVisibility(View.INVISIBLE);

            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_icon:
                showAddClassActivity();
                break;
        }
    }

    private void showAddClassActivity() {

        Intent addClassActivity = new Intent(ClassesFragment.this.getActivity(),AddClassActivity.class);
        getActivity().startActivityForResult(addClassActivity, ADD_CLASS_ACTIVITY_CODE);
    }
}
