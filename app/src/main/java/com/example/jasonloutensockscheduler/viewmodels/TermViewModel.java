package com.example.jasonloutensockscheduler.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasonloutensockscheduler.models.Term;
import com.example.jasonloutensockscheduler.repository.SchedulerRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private SchedulerRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allTerms = repository.getAllTerms();
    }

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
}
