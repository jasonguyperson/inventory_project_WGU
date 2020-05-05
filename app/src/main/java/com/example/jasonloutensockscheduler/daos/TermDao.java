package com.example.jasonloutensockscheduler.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.jasonloutensockscheduler.models.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    //custom query:
    @Query("SELECT * FROM term_table ORDER BY termId DESC")
    LiveData<List<Term>> getAllTerms();

}
