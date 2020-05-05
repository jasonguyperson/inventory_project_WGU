package com.example.jasonloutensockscheduler.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.adapters.CoursesAdapter;
import com.example.jasonloutensockscheduler.fragments.DatePickerFragment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.utils.Converters;
import com.example.jasonloutensockscheduler.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class DetailedTermActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener {
    public static final int ADD_EDIT_COURSE_REQUEST = 7;
    public static final String EXTRA_COURSE_TERM_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_TERM_ID";
    public static final String EXTRA_COURSE_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_START = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_START";
    public static final String EXTRA_COURSE_END = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_END";
    public static final String EXTRA_COURSE_STATUS = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_NOTE = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_NOTE";
    public static final String EXTRA_COURSE_MENTOR_NAME = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_MENTOR_NAME";
    public static final String EXTRA_COURSE_MENTOR_PHONE = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_MENTOR_PHONE";
    public static final String EXTRA_COURSE_MENTOR_EMAIL = "com.example.jasonloutensockscheduler.activities.EXTRA_COURSE_MENTOR_EMAIL";

    private MainActivityViewModel detailedTermViewModel;
    private int thisTermId;

    EditText editTextTermTitle;
    TextView textViewTermStart;
    TextView textViewTermEnd;

    FragmentManager fragmentManager = getSupportFragmentManager();
    int dateFromDialog = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);

        //UI
        editTextTermTitle = (EditText) findViewById(R.id.edit_text_term_title);
        textViewTermStart = (TextView) findViewById(R.id.text_view_term_start);
        textViewTermEnd = (TextView) findViewById(R.id.text_view_term_end);

        //get selected term and populate fields
        getTermAndPopulateFields();

        //floating action button to open AddEditCourseActivity
        FloatingActionButton buttonAddCourse = findViewById(R.id.button_add_course);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedTermActivity.this, AddEditCourseActivity.class);
                intent.putExtra(EXTRA_COURSE_TERM_ID, thisTermId);
                startActivityForResult(intent, ADD_EDIT_COURSE_REQUEST);
            }
        });

        //set up recycler view
        RecyclerView recyclerView = findViewById(R.id.detailed_term_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CoursesAdapter adapter = new CoursesAdapter();

        //handle course click
        adapter.setOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //get current course object
                Course clickedCourse = adapter.getCourseAt(position);
                //launch DetailedTermActivity
                openAddEditCourseActivity(clickedCourse);
            }
        });
        recyclerView.setAdapter(adapter);

        detailedTermViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        try {
            detailedTermViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> courses) {
                    //filter by termId
                    List<Course> filteredCourses = new CopyOnWriteArrayList<>(courses);
                    for (Course c : filteredCourses) {
                        if (c.getTermId() != thisTermId) {
                            filteredCourses.remove(c);
                        }
                    }
                    adapter.setCourses(filteredCourses);
                }
            });
        } catch (ConcurrentModificationException e) {
            if (thisTermId == -1) {
                Toast.makeText(this, "thisTermId is -1", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "thisTermId is not -1", Toast.LENGTH_SHORT).show();
            }
        }

        //make start date clickable
        textViewTermStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFromDialog = 1;
                openDatePickerFragment();

            }
        });

        //make end date clickable
        textViewTermEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFromDialog = 2;
                openDatePickerFragment();
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
                detailedTermViewModel.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(DetailedTermActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }// end of onCreate()


    //handle back button pressed (add course canceled)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //repopulate term if back button was pressed
        if (requestCode == ADD_EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {

            //get term id back
            final int termId = Integer.parseInt(data.getStringExtra(AddEditCourseActivity.EXTRA_ASSESSMENT_TERM_ID));
            thisTermId = termId;


            //get term by Id and populate fields
            detailedTermViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
            detailedTermViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
                @Override
                public void onChanged(List<Term> terms) {
                    List<Term> allTerms = new CopyOnWriteArrayList<>(terms);
                    for (Term t : allTerms) {
                        if (t.getTermId() == termId) {
                            //populate fields
                            editTextTermTitle.setText(t.getTitle());
                            textViewTermStart.setText(t.getStart().toString());
                            textViewTermEnd.setText(t.getEnd().toString());
                            Toast.makeText(DetailedTermActivity.this, "Term ID match found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(DetailedTermActivity.this, "term id match not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //update term -----------------------------
    private void updateTerm() {
        //get UI fields
        String title = editTextTermTitle.getText().toString();
        String start = textViewTermStart.getText().toString();
        String end = textViewTermEnd.getText().toString();

        //make sure all fields are complete
        if (title.trim().isEmpty() || start.trim().isEmpty() || end.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //convert start and end to LocalDate
        Converters c = new Converters();

        //create term object
        Term term = new Term(title, c.stringToLocalDate(start), c.stringToLocalDate(end));
        term.setTermId(thisTermId);

        detailedTermViewModel.update(term);
        finish();
    }


    //use custom menu layout (for updating term)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detailed_term_menu, menu);
        return true;
    }

    //handle menu selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_term:
                updateTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
            textViewTermStart.setText(dateAsString);

        } else if (dateFromDialog == 2) {
            textViewTermEnd.setText(dateAsString);
        }
    }//end of onDateSet()


    //get term information from MainActivity
    public void getTermAndPopulateFields() {
        Intent intent = getIntent();

        int termId = intent.getIntExtra(MainActivity.EXTRA_TERM_ID, -1);
        String termTitle = intent.getStringExtra(MainActivity.EXTRA_TERM_TITLE);
        String startDate = intent.getStringExtra(MainActivity.EXTRA_TERM_START);
        String endDate = intent.getStringExtra(MainActivity.EXTRA_TERM_END);

        thisTermId = termId;

        //set activity title to be term title
        setTitle(termTitle);

        //populate fields
        editTextTermTitle.setText(termTitle);
        textViewTermStart.setText(startDate);
        textViewTermEnd.setText(endDate);
    }

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
        intent.putExtra(EXTRA_COURSE_TERM_ID, termId);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        intent.putExtra(EXTRA_COURSE_TITLE, courseTitle);
        intent.putExtra(EXTRA_COURSE_START, courseStart);
        intent.putExtra(EXTRA_COURSE_END, courseEnd);
        intent.putExtra(EXTRA_COURSE_STATUS, courseStatus);
        intent.putExtra(EXTRA_COURSE_NOTE, courseNote);
        intent.putExtra(EXTRA_COURSE_MENTOR_NAME, courseMentorName);
        intent.putExtra(EXTRA_COURSE_MENTOR_PHONE, courseMentorPhone);
        intent.putExtra(EXTRA_COURSE_MENTOR_EMAIL, courseMentorEmail);

        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}//end of DetailedTermActivity