package com.example.jasonloutensockscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;
import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.adapters.AssessmentsAdapter;
import com.example.jasonloutensockscheduler.fragments.DatePickerFragment;
import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.utils.Converters;
import com.example.jasonloutensockscheduler.utils.NotificationBroadcastReceiver;
import com.example.jasonloutensockscheduler.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Intent.ACTION_SEND;
import static com.example.jasonloutensockscheduler.R.id.save_course;


public class AddEditCourseActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener {
    public static final String EXTRA_ASSESSMENT_TERM_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_TERM_ID";
    public static final String EXTRA_ASSESSMENT_COURSE_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_COURSE_ID";
    public static final String EXTRA_ASSESSMENT_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_DUE_DATE = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_DUE_DATE";
    public static final String EXTRA_ASSESSMENT_STATUS = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_STATUS";
    public static final String EXTRA_ASSESSMENT_NOTE = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_NOTE";
    public static final String EXTRA_ASSESSMENT_TYPE = "com.example.jasonloutensockscheduler.activities.EXTRA_ASSESSMENT_TYPE";

    //keep track of course and term
    private int thisTermId = -2;
    private int thisCourseId = -2;

    //ViewModel
    private MainActivityViewModel addEditCourseViewModel;

    //UI
    EditText editTextCourseTitle;
    TextView textViewCourseStart;
    TextView textViewCourseEnd;
    EditText editTextCourseStatus;
    EditText editTextCourseNote;
    Button buttonShareCourseNote;
    EditText editTextCourseMentorName;
    EditText editTextCourseMentorPhone;
    EditText editTextCourseMentorEmail;

    FragmentManager fragmentManager = getSupportFragmentManager();
    int dateFromDialog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        //UI
        editTextCourseTitle = (EditText) findViewById(R.id.edit_text_course_title);
        textViewCourseStart = (TextView) findViewById(R.id.text_view_course_start);
        textViewCourseEnd = (TextView) findViewById(R.id.text_view_course_end);
        editTextCourseStatus = (EditText) findViewById(R.id.edit_text_course_status);
        editTextCourseNote = (EditText) findViewById(R.id.edit_text_course_note);
        editTextCourseMentorName = (EditText) findViewById(R.id.edit_text_mentor_name);
        editTextCourseMentorPhone = (EditText) findViewById(R.id.edit_text_mentor_phone);
        editTextCourseMentorEmail = (EditText) findViewById(R.id.edit_text_mentor_email);
        buttonShareCourseNote = (Button) findViewById(R.id.button_share_course_note);

        //get selected course and populate fields
        getCourseAndPopulateFields();

        //floating action button to open AddEditCourseActivity
        //if adding new course, cannot add new assessment (must save first)
        FloatingActionButton buttonAddAssessment = findViewById(R.id.button_add_assessment);
        if (thisTermId > -1 && thisCourseId > -1) {
            buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddEditCourseActivity.this, AddEditAssessmentActivity.class);
                    intent.putExtra(EXTRA_ASSESSMENT_COURSE_ID, thisCourseId);
                    startActivity(intent);
                }
            });
        } else {buttonAddAssessment.hide();}

        //set up recycler view
        RecyclerView recyclerView = findViewById(R.id.add_edit_course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //instantiate assessment adapter
        final AssessmentsAdapter adapter = new AssessmentsAdapter();

        //handle assessments click ****************************************************
        //@Override// <-- this IS over-ridden in DetailedTermActivity
        adapter.setOnItemClickListener(new AssessmentsAdapter.OnItemClickListener() {
            public void onItemClick(int position) {
                //get current assessment object
                Assessment clickedAssessment = adapter.getAssessmentAt(position);
                //launch AddEditAssessmentActivity
                openAddEditAssessmentActivity(clickedAssessment);
            }
        });
        recyclerView.setAdapter(adapter); //*******************************************

        addEditCourseViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        addEditCourseViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                //filter by courseId
                List<Assessment> filteredAssessments = new CopyOnWriteArrayList<>(assessments);
                for (Assessment a : filteredAssessments) {
                    if (a.getCourseId() != thisCourseId) {
                        filteredAssessments.remove(a);
                    }
                }
                adapter.setAssessments(filteredAssessments);
            }
        });

        //make start date clickable
        textViewCourseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFromDialog = 1;
                openDatePickerFragment();
            }
        });

        //make end date clickable
        textViewCourseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFromDialog = 2;
                openDatePickerFragment();
            }
        });

        //share notes
        buttonShareCourseNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNote();
            }
        });

        //for swipe-to-delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                addEditCourseViewModel.delete(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AddEditCourseActivity.this, "Assessment deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }//end of onCreate

    private void addOrUpdateCourse() {
        //if adding new course
        if (thisCourseId == -1) {
            addNewCourse();
        } else {     //if updating existing course
            updateCourse();
        }
    }//end of addOrUpdateTerm;

    private void addNewCourse() {
        //instantiate converters object for LocalDate conversion
        Converters c = new Converters();

        //get UI fields
        int termId = thisTermId;
        String title = editTextCourseTitle.getText().toString();
        LocalDate start = c.stringToLocalDate(textViewCourseStart.getText().toString());
        LocalDate end = c.stringToLocalDate(textViewCourseEnd.getText().toString());
        String status = editTextCourseStatus.getText().toString();
        String note = editTextCourseNote.getText().toString();
        String mentorName = editTextCourseMentorName.getText().toString();
        String mentorPhone = editTextCourseMentorPhone.getText().toString();
        String mentorEmail = editTextCourseMentorEmail.getText().toString();


        if (title.trim().isEmpty() || start == null || end == null || status.trim().isEmpty() ||
            mentorName.trim().isEmpty() || mentorPhone.trim().isEmpty() || mentorEmail.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //create course object
        Course course = new Course(termId, title, start, end, status, note, mentorName, mentorPhone, mentorEmail);

        //add new course
        addEditCourseViewModel.insert(course);

        //notification
        Long longStart = c.localDateToMilliseconds(start);
        Long longEnd = c.localDateToMilliseconds(end);

        makeAlarmStart(title+" begins today.", longStart);
        makeAlarmEnd(title+" ends today.", longEnd);

        finish();
    }

    private void updateCourse() {
        //instantiate converters object for LocalDate conversion
        Converters c = new Converters();

        //get UI fields
        int termId = thisTermId;
        String title = editTextCourseTitle.getText().toString();
        LocalDate start = c.stringToLocalDate(textViewCourseStart.getText().toString());
        LocalDate end = c.stringToLocalDate(textViewCourseEnd.getText().toString());
        String status = editTextCourseStatus.getText().toString();
        String note = editTextCourseNote.getText().toString();
        String mentorName = editTextCourseMentorName.getText().toString();
        String mentorPhone = editTextCourseMentorPhone.getText().toString();
        String mentorEmail = editTextCourseMentorEmail.getText().toString();

        if (title.trim().isEmpty() || start == null || end == null || status.trim().isEmpty() ||
                mentorName.trim().isEmpty() || mentorPhone.trim().isEmpty() || mentorEmail.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //create course object
        Course course = new Course(termId, title, start, end, status, note, mentorName, mentorPhone, mentorEmail);
        //since we're updating, add the courseId to the course object
        course.setCourseId(thisCourseId);

        //update course
        addEditCourseViewModel.update(course);
        finish();
    }


    //use custom menu layout (for updating course)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_course_menu, menu);
        return true;
    }

    //handle menu selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case save_course:
                addOrUpdateCourse();
                return true;
            case R.id.home:
                Intent data = new Intent();
                data.putExtra(EXTRA_ASSESSMENT_TERM_ID, thisTermId);
                setResult(RESULT_OK, data);
                finish();
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

    //this is where the magic happens: depending on which TextView was selected -> update Activity!
    @Override
    public void onDateSet(int year, int month, int day) {
        month++;

        //Create LocalDate object, convert to string using converter
        LocalDate ld = LocalDate.of(year, month, day);
        Converters c = new Converters();
        String dateAsString = c.localDateToString(ld);

        if (dateFromDialog == 1) {
            textViewCourseStart.setText(dateAsString);

        } else if (dateFromDialog == 2) {
            textViewCourseEnd.setText(dateAsString);
        }
    }//end of onDateSet()

    public void getCourseAndPopulateFields() {
        //add if/else logic to handle source of course object (could be from DetailedTermActivity OR AllCoursesActivity)
        Intent intent = getIntent();

        //check to see if the intent is from DetailedTermActivity or AllCoursesActivity
        //and get course object info from previous activity

        int courseTermId = intent.getIntExtra(DetailedTermActivity.EXTRA_COURSE_TERM_ID, -1);
        int courseId = intent.getIntExtra(DetailedTermActivity.EXTRA_COURSE_ID, -1);
        String courseTitle = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_TITLE);
        String courseStart = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_START);
        String courseEnd = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_END);
        String courseStatus = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_STATUS);
        String courseNote = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_NOTE);
        String courseMentorName = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_MENTOR_NAME);
        String courseMentorPhone = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_MENTOR_PHONE);
        String courseMentorEmail = intent.getStringExtra(DetailedTermActivity.EXTRA_COURSE_MENTOR_EMAIL);

        if (courseTermId < 0 && courseId < 0) {
            courseTermId = intent.getIntExtra(AllCoursesActivity.EXTRA_ALLCOURSES_TERM_ID, -1);
            courseId = intent.getIntExtra(AllCoursesActivity.EXTRA_ALLCOURSES_ID, -1);
            courseTitle = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_TITLE);
            courseStart = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_START);
            courseEnd = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_END);
            courseStatus = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_STATUS);
            courseNote = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_NOTE);
            courseMentorName = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_MENTOR_NAME);
            courseMentorPhone = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_MENTOR_PHONE);
            courseMentorEmail = intent.getStringExtra(AllCoursesActivity.EXTRA_ALLCOURSES_MENTOR_EMAIL);
        }

        //global for class
        thisTermId = courseTermId;
        thisCourseId = courseId;

        //set activity title to be term title
        setTitle(courseTitle);

        //populate UI fields
        editTextCourseTitle.setText(courseTitle);
        textViewCourseStart.setText(courseStart);
        textViewCourseEnd.setText(courseEnd);
        editTextCourseStatus.setText(courseStatus);
        editTextCourseNote.setText(courseNote);
        editTextCourseMentorName.setText(courseMentorName);
        editTextCourseMentorPhone.setText(courseMentorPhone);
        editTextCourseMentorEmail.setText(courseMentorEmail);
    }//end of getCourseAndPopulateFields


    //method to launch AddEditAssessmentActivity
    public void openAddEditAssessmentActivity(Assessment clickedAssessment) {

        //When clicked, send intent to AddEditAssessmentActivity
        int termId = thisTermId;
        int courseId = clickedAssessment.getCourseId();
        int assessmentId = clickedAssessment.getAssessmentId();
        String assessmentTitle = clickedAssessment.getTitle();
        String assessmentDueDate = clickedAssessment.getDueDate().toString();
        String assessmentStatus = clickedAssessment.getStatus();
        String assessmentNote = clickedAssessment.getNote();
        String assessmentType = clickedAssessment.getType();

        Intent intent = new Intent(this, AddEditAssessmentActivity.class);
        intent.putExtra(EXTRA_ASSESSMENT_TERM_ID, termId);
        intent.putExtra(EXTRA_ASSESSMENT_COURSE_ID, courseId);
        intent.putExtra(EXTRA_ASSESSMENT_ID, assessmentId);
        intent.putExtra(EXTRA_ASSESSMENT_TITLE, assessmentTitle);
        intent.putExtra(EXTRA_ASSESSMENT_DUE_DATE, assessmentDueDate);
        intent.putExtra(EXTRA_ASSESSMENT_STATUS, assessmentStatus);
        intent.putExtra(EXTRA_ASSESSMENT_NOTE, assessmentNote);
        intent.putExtra(EXTRA_ASSESSMENT_TYPE, assessmentType);

        startActivity(intent);
    }

    //share note
    public void shareNote() {
        String noteToShare = editTextCourseNote.getText().toString();
        String courseTitleToShare = editTextCourseTitle.getText().toString();

        Intent intent = new Intent();
        intent.setAction(ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, noteToShare);
        intent.putExtra(Intent.EXTRA_TITLE, courseTitleToShare);
        intent.setType("text/plain");

        Intent share = Intent.createChooser(intent, null);
        startActivity(intent);
    }

    public void makeAlarmStart(String message, Long longDay) {
        Intent intent = new Intent(AddEditCourseActivity.this, NotificationBroadcastReceiver.class);
        intent.putExtra("key", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditCourseActivity.this, 0, intent, 0);
        AlarmManager am1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am1.set(AlarmManager.RTC_WAKEUP, longDay, pendingIntent);
    }

    public void makeAlarmEnd(String message, Long longDay) {
        Intent intent = new Intent(AddEditCourseActivity.this, NotificationBroadcastReceiver.class);
        intent.putExtra("key", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditCourseActivity.this, 1, intent, 0);
        AlarmManager am1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am1.set(AlarmManager.RTC_WAKEUP, longDay, pendingIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}//end of AddEditCourseActivity
