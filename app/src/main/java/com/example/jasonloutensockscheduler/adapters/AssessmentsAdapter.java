package com.example.jasonloutensockscheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.models.Assessment;

import java.util.ArrayList;
import java.util.List;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.AssessmentHolder> {
    private List<Assessment> assessments = new ArrayList<>();

    // START --- to make cards clickable --- //
    private OnItemClickListener aListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        aListener = listener;
    }
    // END --- to make cards clickable --- //

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView, aListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        Assessment currentAssessment = assessments.get(position);
        holder.textViewTitle.setText(currentAssessment.getTitle());
        holder.textViewDueDate.setText(currentAssessment.getDueDate().toString());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public Assessment getAssessmentAt(int position) {
        return assessments.get(position);
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDueDate;

        public AssessmentHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_assessment_title);
            textViewDueDate = itemView.findViewById(R.id.text_view_assessment_due_date);

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
