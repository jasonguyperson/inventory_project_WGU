package com.example.jasonloutensockscheduler.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.adapters.TermsAdapter;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.utils.Converters;
import com.example.jasonloutensockscheduler.viewmodels.AssessmentViewModel;
import com.example.jasonloutensockscheduler.viewmodels.CourseViewModel;
import com.example.jasonloutensockscheduler.viewmodels.MainActivityViewModel;
import com.example.jasonloutensockscheduler.viewmodels.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final String EXTRA_TERM_ID = "com.example.jasonloutensockscheduler.activities.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START = "com.example.jasonloutensockscheduler.activities.EXTRA_TERM_START";
    public static final String EXTRA_TERM_END = "com.example.jasonloutensockscheduler.activities.EXTRA_TERM_END";


    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("All Terms");

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTermActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });

        //Build RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.terms_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TermsAdapter adapter = new TermsAdapter();

        //handle term click
        adapter.setOnItemClickListener(new TermsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //get current term object
                Term clickedTerm = adapter.getTermAt(position);
                //launch DetailedTermActivity
                openDetailedTermActivity(clickedTerm);
            }
        });
        recyclerView.setAdapter(adapter);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                adapter.setTerms(terms);
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

                //get swiped term
                Term swipedTerm = adapter.getTermAt(viewHolder.getAdapterPosition());

                //delete term if no courses found
                termContainsCourse(swipedTerm);

                mainActivityViewModel.getAllTerms().observe(MainActivity.this, new Observer<List<Term>>() {
                    @Override
                    public void onChanged(List<Term> terms) {
                        adapter.setTerms(terms);
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle add term (insert)
        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {

            Converters c = new Converters();

            String title = data.getStringExtra(AddTermActivity.EXTRA_TITLE);
            LocalDate start = c.stringToLocalDate(data.getStringExtra(AddTermActivity.EXTRA_START));
            LocalDate end = c.stringToLocalDate(data.getStringExtra(AddTermActivity.EXTRA_END));

            Term term = new Term(title, start, end);
            mainActivityViewModel.insert(term);

            Toast.makeText(this, "New term added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to add new term", Toast.LENGTH_SHORT).show();
        }
    }

    //method to launch Detailed Term Activity
    public void openDetailedTermActivity(Term clickedTerm) {

        //When clicked, send the term ID to DetailedTermActivity
        int termId = clickedTerm.getTermId();
        String termTitle = clickedTerm.getTitle();
        String termStart = clickedTerm.getStart().toString();
        String termEnd = clickedTerm.getEnd().toString();


        Intent intent = new Intent(this, DetailedTermActivity.class);
        intent.putExtra(EXTRA_TERM_ID, termId);
        intent.putExtra(EXTRA_TERM_TITLE, termTitle);
        intent.putExtra(EXTRA_TERM_START, termStart);
        intent.putExtra(EXTRA_TERM_END, termEnd);

        startActivity(intent);
    }

    public void termContainsCourse(final Term swipedTerm) {

        mainActivityViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                //filter by termId
                List<Course> filteredCourses = new CopyOnWriteArrayList<>(courses);
                for (Course c : filteredCourses) {
                    if (c.getTermId() != swipedTerm.getTermId()) {
                        filteredCourses.remove(c);
                    }
                }
                if (filteredCourses.size() == 0) {
                    mainActivityViewModel.delete(swipedTerm);
                    Toast.makeText(MainActivity.this, "Term removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Contains courses!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}//end of MainActivity
