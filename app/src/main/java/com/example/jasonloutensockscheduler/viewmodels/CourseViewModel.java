package com.example.jasonloutensockscheduler.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.repository.SchedulerRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private SchedulerRepository repository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void delete(Course course) {
        repository.delete(course);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }


}
