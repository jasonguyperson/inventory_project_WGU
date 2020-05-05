package com.example.jasonloutensockscheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseHolder> {
    private List<Course> courses = new ArrayList<>();


    // START --- to make cards clickable --- //
    private OnItemClickListener cListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        cListener = listener;
    }
    // END --- to make cards clickable --- //

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView, cListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course currentCourse = courses.get(position);
        holder.textViewTitle.setText(currentCourse.getTitle());
        holder.textViewStart.setText(currentCourse.getStart().toString());
        holder.textViewEnd.setText(currentCourse.getEnd().toString());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public Course getCourseAt(int position) {
        return courses.get(position);
    }

    class CourseHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewStart;
        private TextView textViewEnd;

        public CourseHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_course_title);
            textViewStart = itemView.findViewById(R.id.text_view_course_start);
            textViewEnd = itemView.findViewById(R.id.text_view_course_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
