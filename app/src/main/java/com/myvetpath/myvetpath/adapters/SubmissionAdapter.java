package com.myvetpath.myvetpath.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myvetpath.myvetpath.R;
import com.myvetpath.myvetpath.ViewSubsActivity;
import com.myvetpath.myvetpath.data.SubmissionTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static java.sql.Types.NULL;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubViewHolder> {
    private List<SubmissionTable> mSubmissions;
    OnSubmissionClickListener clickListener;

    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public interface OnSubmissionClickListener{
        void onSubmissionClick(SubmissionTable sub);
        void onSubmissionLongClick(SubmissionTable sub);
    }

    public SubmissionAdapter(OnSubmissionClickListener clickListenerArg){
        clickListener = clickListenerArg;
    }

    public void updateSubs(List<SubmissionTable> submissions){
        mSubmissions = submissions;
        notifyDataSetChanged();
    }

    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subrow, parent, false);
        return new SubViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubmissionAdapter.SubViewHolder holder, int position) {
        holder.bind(mSubmissions.get(position));
    }

    @Override
    public int getItemCount() {
        if(mSubmissions != null) {
            return mSubmissions.size();
        } else {
            return 0;
        }
    }

    public class SubViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView caseTextView;

        public SubViewHolder(View v) {
            super(v);
            this.titleTextView = (TextView) v.findViewById(R.id.subHeader);
            this.dateTextView = (TextView) v.findViewById(R.id.subDate);
            this.caseTextView = (TextView) v.findViewById(R.id.subCaseID);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onSubmissionClick(mSubmissions.get(getAdapterPosition()));
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    clickListener.onSubmissionLongClick(mSubmissions.get(getAdapterPosition()));
                    return true;
                }
            });
        }

        public void bind(SubmissionTable submissionTable){
            titleTextView.setText(submissionTable.Title);
            calendar.setTimeInMillis(submissionTable.DateOfCreation);
            dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
            if(submissionTable.Case_ID == NULL){
                caseTextView.setText(R.string.pending);
            } else {
                caseTextView.setText(String.valueOf(submissionTable.Case_ID));
            }
        }
    }
}
