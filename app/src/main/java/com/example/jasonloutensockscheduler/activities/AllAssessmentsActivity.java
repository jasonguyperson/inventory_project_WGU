package com.example.jasonloutensockscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.adapters.AssessmentsAdapter;
import com.example.jasonloutensockscheduler.adapters.CoursesAdapter;
import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.viewmodels.AssessmentViewModel;


import java.util.List;

public class AllAssessmentsActivity extends AppCompatActivity {
    private AssessmentViewModel assessmentViewModel;

    public static final String EXTRA_ALLASSESSMENTS_TERM_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_TERM_ID";
    public static final String EXTRA_ALLASSESSMENTS_COURSE_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_COURSE_ID";
    public static final String EXTRA_ALLASSESSMENTS_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_ID";
    public static final String EXTRA_ALLASSESSMENTS_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_TITLE";
    public static final String EXTRA_ALLASSESSMENTS_DUE_DATE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_DUE_DATE";
    public static final String EXTRA_ALLASSESSMENTS_STATUS = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_STATUS";
    public static final String EXTRA_ALLASSESSMENTS_NOTE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_NOTE";
    public static final String EXTRA_ALLASSESSMENTS_TYPE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLASSESSMENTS_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assessments);

        RecyclerView recyclerView = findViewById(R.id.assessments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AssessmentsAdapter adapter = new AssessmentsAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                adapter.setAssessments(assessments);
            }
        });

        //handle assessment click
        adapter.setOnItemClickListener(new AssessmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //get current assessment object
                Assessment clickedAssessment = adapter.getAssessmentAt(position);
                openAddEditAssessmentActivity(clickedAssessment);
            }
        });
        recyclerView.setAdapter(adapter);

    }//end of onCreate

    //method to launch AddEditAssessmentActivity
    public void openAddEditAssessmentActivity(Assessment clickedAssessment) {
        //When clicked, send intent to AddEditAssessmentActivity
        int courseId = clickedAssessment.getCourseId();
        int assessmentId = clickedAssessment.getAssessmentId();
        String assessmentTitle = clickedAssessment.getTitle();
        String assessmentDueDate = clickedAssessment.getDueDate().toString();
        String assessmentStatus = clickedAssessment.getStatus();
        String assessmentNote = clickedAssessment.getNote();
        String assessmentType = clickedAssessment.getType();

        Intent intent = new Intent(this, AddEditAssessmentActivity.class);
        intent.putExtra(EXTRA_ALLASSESSMENTS_COURSE_ID, courseId);
        intent.putExtra(EXTRA_ALLASSESSMENTS_ID, assessmentId);
        intent.putExtra(EXTRA_ALLASSESSMENTS_TITLE, assessmentTitle);
        intent.putExtra(EXTRA_ALLASSESSMENTS_DUE_DATE, assessmentDueDate);
        intent.putExtra(EXTRA_ALLASSESSMENTS_STATUS, assessmentStatus);
        intent.putExtra(EXTRA_ALLASSESSMENTS_NOTE, assessmentNote);
        intent.putExtra(EXTRA_ALLASSESSMENTS_TYPE, assessmentType);

        startActivity(intent);
    }

}//end of activity
