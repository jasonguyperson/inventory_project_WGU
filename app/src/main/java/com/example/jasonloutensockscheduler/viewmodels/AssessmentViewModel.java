package com.example.jasonloutensockscheduler.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasonloutensockscheduler.models.Assessment;
import com.example.jasonloutensockscheduler.repository.SchedulerRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private SchedulerRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allAssessments = repository.getAllAssessments();
    }

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
