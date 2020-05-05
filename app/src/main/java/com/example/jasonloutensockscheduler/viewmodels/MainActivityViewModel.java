package com.example.jasonloutensockscheduler.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.models.Course;
import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.repository.SchedulerRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private SchedulerRepository repository;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Assessment>> allAssessments;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allTerms = repository.getAllTerms();
        allCourses = repository.getAllCourses();
        allAssessments = repository.getAllAssessments();
    }

    //Term methods
    public void insert(Term term) {
        repository.insert(term);
    }

    public void update(Term term) {
        repository.update(term);
    }

    public void delete(Term term) {
        repository.delete(term);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }


    //Course methods
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


    //Assessment methods
    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

}
