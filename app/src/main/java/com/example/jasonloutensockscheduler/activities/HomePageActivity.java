package com.example.jasonloutensockscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jasonloutensockscheduler.R;

public class HomePageActivity extends AppCompatActivity {
    private Button buttonViewTerms;
    private Button buttonViewCourses;
    private Button buttonViewAssessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        buttonViewTerms = /*(Button)*/ findViewById(R.id.button_view_terms);
        buttonViewCourses = /*(Button)*/ findViewById(R.id.button_view_courses);
        buttonViewAssessments = /*(Button)*/ findViewById(R.id.button_view_assessments);

        buttonViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTermsActivity();
            }
        });

        buttonViewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoursesActivity();
            }
        });

        buttonViewAssessments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAssessmentsActivity();
            }
        });


    }

    public void openTermsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void openCoursesActivity() {
        Intent intent = new Intent(this, AllCoursesActivity.class);
        startActivity(intent);
    }


    public void openAssessmentsActivity() {
        Intent intent = new Intent(this, AllAssessmentsActivity.class);
        startActivity(intent);
    }
}
