package com.myvetpath.myvetpath;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;


public class ViewSubsActivity extends BaseActivity {

    Intent create_sub_activity;
    Intent sub_details_activity;
    MyDBHandler dbHandler;
    boolean subTableExists;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Submission[] submissions;

    public void createDeleteDialog(final int selectedSubmissionPosition){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewSubsActivity.this);
        dialog.setCancelable(true);
        String title = getString(R.string.action_delete_confirmation_prompt_first_part)
                + submissions[selectedSubmissionPosition].getTitle()
                + getString(R.string.action_delete_confirmation_second_part);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ViewSubsActivity.this, R.string.deleted_message + submissions[selectedSubmissionPosition].getTitle(),
                        Toast.LENGTH_LONG).show();
                dbHandler.deleteSubmission(submissions[selectedSubmissionPosition].getInternalID());
                mAdapter.notifyItemRemoved(selectedSubmissionPosition);
            }
        }).setNegativeButton(R.string.action_no, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    /*Fills View with Submission elements*/
    public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.MyViewHolder> {
        private Submission[] mSubmissions;
        CustomSubClickListener clickListener;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titleTextView;
            public TextView dateTextView;
            public TextView caseTextView;
            public MyViewHolder(View v) {
                super(v);
                this.titleTextView = (TextView) v.findViewById(R.id.subHeader);
                this.dateTextView = (TextView) v.findViewById(R.id.subDate);
                this.caseTextView = (TextView) v.findViewById(R.id.subCaseID);
            }
        }

        public SubsAdapter(Submission[] mySubmissions, CustomSubClickListener listener) {
            mSubmissions = mySubmissions;
            clickListener = listener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public SubsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subrow, parent, false);
            final MyViewHolder myViewHolder = new MyViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onSubClick(view, mSubmissions[myViewHolder.getAdapterPosition()].getInternalID());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() { //Enable long click on a case entry
                @Override
                public boolean onLongClick(View view) {
                    clickListener.onSubLongClick(view, myViewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
            });

            return myViewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.titleTextView.setText(mSubmissions[position].getTitle());
            calendar.setTimeInMillis(mSubmissions[position].getDateOfCreation());
            holder.dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
            if(mSubmissions[position].getCaseID() == NULL){
                holder.caseTextView.setText(R.string.pending);
            } else {
                holder.caseTextView.setText(String.valueOf(mSubmissions[position].getCaseID()));
            }
        }

        @Override
        public int getItemCount() {
            return dbHandler.getNumberOfSubmissions();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setMenuOptionItemToRemove(this);
        toolbar.setTitle("Submissions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        create_sub_activity = new Intent(this, CreateSubActivity.class);
        sub_details_activity = new Intent(this, SubDetailsActivity.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.subsRecyclerView);

        dbHandler = new MyDBHandler(this);

        submissions = dbHandler.getSubmissions();

        mAdapter = new SubsAdapter(submissions, new CustomSubClickListener() {
            @Override
            public void onSubClick(View v, int caseID) {
                sub_details_activity.putExtra("internalID", caseID);
                startActivity(sub_details_activity);
            }

            @Override
            public boolean onSubLongClick(View v, int position) {
                //Set it so long clicking on an entry shows the dialog
                createDeleteDialog(position);
                return true;
            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }



}
