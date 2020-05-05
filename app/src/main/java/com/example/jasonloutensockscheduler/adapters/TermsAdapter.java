package com.example.jasonloutensockscheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasonloutensockscheduler.R;
import com.example.jasonloutensockscheduler.models.Term;

import java.util.ArrayList;
import java.util.List;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.TermsHolder> {
    private List<Term> terms = new ArrayList<>();

    // START --- to make cards clickable --- //
    private OnItemClickListener tListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        tListener = listener;
    }
    // END --- to make cards clickable --- //

    @NonNull
    @Override
    public TermsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermsHolder(itemView, tListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsHolder holder, int position) {
        Term currentTerm = terms.get(position);
        holder.textViewTitle.setText(currentTerm.getTitle());
        holder.textViewStart.setText(currentTerm.getStart().toString());
        holder.textViewEnd.setText(currentTerm.getEnd().toString());
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        notifyDataSetChanged(); //update later
    }

    public Term getTermAt(int position) {
        return terms.get(position);
    }

    class TermsHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewStart;
        private TextView textViewEnd;

        public TermsHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_term_title);
            textViewStart = itemView.findViewById(R.id.text_view_term_start);
            textViewEnd = itemView.findViewById(R.id.text_view_term_end);

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
} //end of TermsAdapter class
