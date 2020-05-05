package com.example.jasonloutensockscheduler.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.jasonloutensockscheduler.models.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    //custom query:
    @Query("SELECT * FROM course_table ORDER BY courseId DESC")
    LiveData<List<Course>> getAllCourses();

}
