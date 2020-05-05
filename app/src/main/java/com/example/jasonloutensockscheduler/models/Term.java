package com.example.jasonloutensockscheduler.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;

@Entity(tableName = "term_table")
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int termId;

    private String title;

    private LocalDate start;

    private LocalDate end;

    //CONSTRUCTOR
    public Term(String title, LocalDate start, LocalDate end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    //SETTERS
    public void setTermId(int termId) {
        this.termId = termId;
    }

    //GETTERS
    public int getTermId() {
        return termId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }



} //end of Term class
