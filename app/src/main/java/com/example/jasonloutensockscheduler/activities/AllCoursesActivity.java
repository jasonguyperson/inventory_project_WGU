package com.example.jasonloutensockscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.adapters.CoursesAdapter;
import com.example.jasonloutensockscheduler.adapters.TermsAdapter;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.viewmodels.CourseViewModel;

import java.util.List;

public class AllCoursesActivity extends AppCompatActivity {
    private CourseViewModel courseViewModel;

    public static final String EXTRA_ALLCOURSES_TERM_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_TERM_ID";
    public static final String EXTRA_ALLCOURSES_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_ID";
    public static final String EXTRA_ALLCOURSES_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_TITLE";
    public static final String EXTRA_ALLCOURSES_START = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_START";
    public static final String EXTRA_ALLCOURSES_END = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_END";
    public static final String EXTRA_ALLCOURSES_STATUS = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_STATUS";
    public static final String EXTRA_ALLCOURSES_NOTE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_NOTE";
    public static final String EXTRA_ALLCOURSES_MENTOR_NAME = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_MENTOR_NAME";
    public static final String EXTRA_ALLCOURSES_MENTOR_PHONE = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_MENTOR_PHONE";
    public static final String EXTRA_ALLCOURSES_MENTOR_EMAIL = "com.example.jasonloutensockscheduler.activities.EXTRA_ALLCOURSES_MENTOR_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        RecyclerView recyclerView = findViewById(R.id.courses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CoursesAdapter adapter = new CoursesAdapter();
        recyclerView.setAdapter(adapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });

        //handle course click
        adapter.setOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //get current course object
                Course clickedCourse = adapter.getCourseAt(position);
                openAddEditCourseActivity(clickedCourse);
            }
        });
        recyclerView.setAdapter(adapter);

    }//end of onCreate()


    //method to launch AddEditCourseActivity
    public void openAddEditCourseActivity(Course clickedCourse) {
        //When clicked, send intent to AddEditCourseActivity
        int termId = clickedCourse.getTermId();
        int courseId = clickedCourse.getCourseId();
        String courseTitle = clickedCourse.getTitle();
        String courseStart = clickedCourse.getStart().toString();
        String courseEnd = clickedCourse.getEnd().toString();
        String courseStatus = clickedCourse.getStatus();
        String courseNote = clickedCourse.getNote();
        String courseMentorName = clickedCourse.getMentorName();
        String courseMentorPhone = clickedCourse.getMentorPhone();
        String courseMentorEmail = clickedCourse.getMentorEmail();

        Intent intent = new Intent(this, AddEditCourseActivity.class);
        intent.putExtra(EXTRA_ALLCOURSES_TERM_ID, termId);
        intent.putExtra(EXTRA_ALLCOURSES_ID, courseId);
        intent.putExtra(EXTRA_ALLCOURSES_TITLE, courseTitle);
        intent.putExtra(EXTRA_ALLCOURSES_START, courseStart);
        intent.putExtra(EXTRA_ALLCOURSES_END, courseEnd);
        intent.putExtra(EXTRA_ALLCOURSES_STATUS, courseStatus);
        intent.putExtra(EXTRA_ALLCOURSES_NOTE, courseNote);
        intent.putExtra(EXTRA_ALLCOURSES_MENTOR_NAME, courseMentorName);
        intent.putExtra(EXTRA_ALLCOURSES_MENTOR_PHONE, courseMentorPhone);
        intent.putExtra(EXTRA_ALLCOURSES_MENTOR_EMAIL, courseMentorEmail);

        startActivity(intent);
    }

}//end of activity
