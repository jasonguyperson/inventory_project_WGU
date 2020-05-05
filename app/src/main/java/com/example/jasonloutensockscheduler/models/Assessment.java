package com.example.jasonloutensockscheduler.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE))
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;

    private int courseId;

    private String title;

    private LocalDate dueDate;

    private String status;

    private String note;

    private String type;


    //CONSTRUCTOR
    public Assessment(int courseId, String title, LocalDate dueDate, String status, String note, String type) {
        this.courseId = courseId;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
        this.note = note;
        this.type = type;
    }


    //SETTERS
    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }


    //GETTERS
    public int getAssessmentId() {
        return assessmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public String getType() {
        return type;
    }
}//end of Assessment class
