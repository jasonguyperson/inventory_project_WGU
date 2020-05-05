package com.example.jasonloutensockscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.utils.Converters;

import java.time.LocalDate;

public class AddTermActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.jasonloutensockscheduler.activities.EXTRA_TITLE";
    public static final String EXTRA_START = "com.example.jasonloutensockscheduler.activities.EXTRA_START";
    public static final String EXTRA_END = "com.example.jasonloutensockscheduler.activities.EXTRA_END";
    private EditText editTextTitle;
    private DatePicker datePickerStart;
    private DatePicker datePickerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        editTextTitle = findViewById(R.id.edit_text_title);
        datePickerStart = findViewById(R.id.date_picker_start);
        datePickerEnd = findViewById(R.id.date_picker_end);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Term");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_term_menu, menu);
        return true;
    }

    private void saveTerm() {
        Converters c = new Converters();

        String title = editTextTitle.getText().toString();
        String start = c.localDateToString(LocalDate.of(datePickerStart.getYear(), datePickerStart.getMonth()+1, datePickerStart.getDayOfMonth())); //have to convert to string to send data back
        String end = c.localDateToString(LocalDate.of(datePickerEnd.getYear(), datePickerEnd.getMonth()+1, datePickerEnd.getDayOfMonth()));

        if (title.trim().isEmpty() || datePickerStart == null || datePickerEnd == null) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_START, start);
        data.putExtra(EXTRA_END, end);

        setResult(RESULT_OK, data);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
