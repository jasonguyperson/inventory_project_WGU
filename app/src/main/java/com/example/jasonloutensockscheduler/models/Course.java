package com.example.jasonloutensockscheduler.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "course_table",
        foreignKeys = @ForeignKey(entity = Term.class,
        parentColumns = "termId",
        childColumns = "termId",
        onDelete = ForeignKey.CASCADE))
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int courseId;

    private int termId;

    private String title;

    private LocalDate start;

    private LocalDate end;

    private String status;

    private String note;

    private String mentorName;

    private String mentorPhone;

    private String mentorEmail;


    //CONSTRUCTOR  ----  auto generated "termId" is omitted in constructor (correct??)
    public Course(int termId, String title, LocalDate start, LocalDate end, String status, String note, String mentorName, String mentorPhone, String mentorEmail) {
        this.termId = termId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = status;
        this.note = note;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
    }


    //SETTERS
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }


    //GETTERS

    public int getCourseId() {
        return courseId;
    }

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

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }
}//end of Course class
