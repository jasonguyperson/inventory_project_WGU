package com.example.jasonloutensockscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.fragments.DatePickerFragment;
import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.utils.Converters;
import com.example.jasonloutensockscheduler.utils.NotificationBroadcastReceiver;
import com.example.jasonloutensockscheduler.viewmodels.MainActivityViewModel;

import java.time.LocalDate;

import static android.content.Intent.ACTION_SEND;

public class AddEditAssessmentActivity extends AppCompatActivity  implements DatePickerFragment.DatePickerFragmentListener {
    //track where from
    private int thisCourseId = -3;
    private int thisAssessmentId = -3;

    //ViewModel
    private MainActivityViewModel addEditAssessmentViewModel;

    //UI
    EditText editTextAssessmentTitle;
    TextView textViewAssessmentDueDate;
    EditText editTextAssessmentStatus;
    EditText editTextAssessmentNote;
    EditText editTextAssessmentType;
    Button buttonShareAssessmentNote;

    //DatePickerFragment support
    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_assessment);

        //UI
        editTextAssessmentTitle = (EditText) findViewById(R.id.edit_text_assessment_title);
        textViewAssessmentDueDate = (TextView) findViewById(R.id.text_view_assessment_due_date);
        editTextAssessmentStatus = (EditText) findViewById(R.id.edit_text_assessment_status);
        editTextAssessmentNote = (EditText) findViewById(R.id.edit_text_assessment_note);
        editTextAssessmentType = (EditText) findViewById(R.id.edit_text_assessment_type);
        buttonShareAssessmentNote = (Button) findViewById(R.id.button_share_assessment_note);

        //retrieve Assessment object through intent and populate fields
        getAssessmentAndPopulateFields();

        //get ViewModel
        addEditAssessmentViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //make due date clickable
        textViewAssessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerFragment();
            }
        });

        //share notes
        buttonShareAssessmentNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNote();
            }
        });
    }//end of onCreate()


    //determines if we're adding a new assessment or updating an existing assessment
    private void addOrUpdateAssessment() {
        if (thisAssessmentId < 0) {
            addNewAssessment();
        } else {
            updateAssessment();
        }
    }//end of addOrUpdateAssessment;


    //add new assessment
    private void addNewAssessment() {
        //instantiate converters object for LocalDate conversion
        Converters c = new Converters();

        //get UI fields
        int courseId = thisCourseId;
        String title = editTextAssessmentTitle.getText().toString();
        LocalDate dueDate = c.stringToLocalDate(textViewAssessmentDueDate.getText().toString());
        String status = editTextAssessmentStatus.getText().toString();
        String note = editTextAssessmentNote.getText().toString();
        String type = editTextAssessmentType.getText().toString();

        if (title.trim().isEmpty() || dueDate == null || status.trim().isEmpty() || type.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //instantiate Assessment object
        Assessment assessment = new Assessment(courseId, title, dueDate, status, note, type);

        //add new assessment
        addEditAssessmentViewModel.insert(assessment);

        //notification
        Long longDue = c.localDateToMilliseconds(dueDate);
        makeAlarm(title+" is due today.", longDue);

        finish();
    }

    private void updateAssessment() {
        //instantiate converters object for LocalDate conversion
        Converters c = new Converters();

        //get UI fields
        int courseId = thisCourseId;
        String title = editTextAssessmentTitle.getText().toString();
        LocalDate dueDate = c.stringToLocalDate(textViewAssessmentDueDate.getText().toString());
        String status = editTextAssessmentStatus.getText().toString();
        String note = editTextAssessmentNote.getText().toString();
        String type = editTextAssessmentType.getText().toString();

        if (title.trim().isEmpty() || dueDate == null || status.trim().isEmpty() || type.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //instantiate Assessment object
        Assessment assessment = new Assessment(courseId, title, dueDate, status, note, type);
        //since we're updating, add the assessmentId to the assessment object
        assessment.setAssessmentId(thisAssessmentId);


        //add new assessment
        addEditAssessmentViewModel.update(assessment);
        finish();
    }

    //use custom menu layout (for updating assessment)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_assessment_menu, menu);
        return true;
    }

    //handle menu selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_assessment:
                addOrUpdateAssessment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //opens DatePickerFragment
    public void openDatePickerFragment() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(fragmentManager, "Start Date");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        month++;

        //Create LocalDate object, convert to string using converter
        LocalDate ld = LocalDate.of(year, month, day);
        Converters c = new Converters();
        String dateAsString = c.localDateToString(ld);

        //update textView
        textViewAssessmentDueDate.setText(dateAsString);
    }//end of onDateSet()

    public void getAssessmentAndPopulateFields() {
        Intent intent = getIntent();

        //get Assessment object info from previous activity
        int courseId = intent.getIntExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_COURSE_ID, -1);
        int assessmentId = intent.getIntExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_ID, -1);
        String assessmentTitle = intent.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_TITLE);
        String assessmentDueDate = intent.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_DUE_DATE);
        String assessmentStatus = intent.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_STATUS);
        String assessmentNote = intent.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_NOTE);
        String assessmentType = intent.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_TYPE);

        //check to see if intent was actually sent from AllAssessmentsActivity
        if (courseId < 0 && assessmentId < 0) {
            courseId = intent.getIntExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_COURSE_ID, -1);
            assessmentId = intent.getIntExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_ID, -1);
            assessmentTitle = intent.getStringExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_TITLE);
            assessmentDueDate = intent.getStringExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_DUE_DATE);
            assessmentStatus = intent.getStringExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_STATUS);
            assessmentNote = intent.getStringExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_NOTE);
            assessmentType = intent.getStringExtra(AllAssessmentsActivity.EXTRA_ALLASSESSMENTS_TYPE);
        }

        //global for class
        thisCourseId = courseId;
        thisAssessmentId = assessmentId;

        //set activity title to be term title
        setTitle(assessmentTitle);

        //populate UI fields
        editTextAssessmentTitle.setText(assessmentTitle);
        textViewAssessmentDueDate.setText(assessmentDueDate);
        editTextAssessmentStatus.setText(assessmentStatus);
        editTextAssessmentNote.setText(assessmentNote);
        editTextAssessmentType.setText(assessmentType);
    }//end of getCourseAndPopulateFields

    public void shareNote() {
        String noteToShare = editTextAssessmentNote.getText().toString();
        String assessmentTitleToShare = editTextAssessmentTitle.getText().toString();

        Intent intent = new Intent();
        intent.setAction(ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, noteToShare);
        intent.putExtra(Intent.EXTRA_TITLE, assessmentTitleToShare);
        intent.setType("text/plain");

        Intent share = Intent.createChooser(intent, null);
        startActivity(intent);
    }

    public void makeAlarm(String message, Long longDue) {
        Intent intent = new Intent(AddEditAssessmentActivity.this, NotificationBroadcastReceiver.class);
        intent.putExtra("key", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditAssessmentActivity.this, 2, intent, 0);
        AlarmManager am2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am2.set(AlarmManager.RTC_WAKEUP, longDue, pendingIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}//end of AddEditAssessmentActivity
