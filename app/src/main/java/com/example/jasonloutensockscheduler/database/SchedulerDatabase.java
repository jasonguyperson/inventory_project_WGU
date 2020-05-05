package com.example.jasonloutensockscheduler.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jasonloutensockscheduler.daos.AssessmentDao;
import com.example.jasonloutensockscheduler.daos.CourseDao;
import com.example.jasonloutensockscheduler.daos.TermDao;
import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.utils.Converters;

import java.time.LocalDate;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class SchedulerDatabase extends RoomDatabase {

    //Database contained here
    private static SchedulerDatabase instance;

    //used to access DAOs
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    public static synchronized SchedulerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SchedulerDatabase.class, "scheduler_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private CourseDao courseDao;
        private AssessmentDao assessmentDao;

        private PopulateDbAsyncTask(SchedulerDatabase db) {  //create multiple "populate" async tasks?
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            //initialization stuff (to populate with info)
            LocalDate start1 = LocalDate.of(2020, 03, 01);
            LocalDate end1 = LocalDate.of(2020, 04, 01);

            termDao.insert(new Term("Term 1", start1, end1));
            courseDao.insert(new Course(1, "Course 1", start1, end1, "Not Started", "Tough class", "Alan Grant", "(555) 611-1993", "agrant@wgu.edu"));
            assessmentDao.insert(new Assessment(1,"Assessment 1", end1, "Not Complete", "Quis custodiet ipsos custodes?", "Objective"));

            return null;
        }
    }
}//end of SchedulerDatabase database
