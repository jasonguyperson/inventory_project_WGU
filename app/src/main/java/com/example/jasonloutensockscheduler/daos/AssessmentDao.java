package com.example.jasonloutensockscheduler.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.jasonloutensockscheduler.models.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    //custom query:
    @Query("SELECT * FROM assessment_table ORDER BY assessmentId DESC")
    LiveData<List<Assessment>> getAllAssessments();

}
