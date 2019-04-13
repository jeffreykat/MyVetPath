package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.SubmissionTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.sql.Types.NULL;

//This screen will show the draft that the user was was previously working on
public class ViewDraftsActivity extends BaseActivity {

    MyVetPathViewModel viewModel;

    boolean subTableExists;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private RecyclerView mRecyclerView;
    private DraftsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SubmissionTable> drafts;

    public void createDeleteDialog(final int selectedSubmissionPosition){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewDraftsActivity.this);
        dialog.setCancelable(true);
        String title = getString(R.string.action_delete_confirmation_prompt_first_part)
                + drafts.get(selectedSubmissionPosition).Title
                + getString(R.string.action_delete_confirmation_second_part);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ViewDraftsActivity.this,  getString(R.string.deleted_message) + drafts.get(selectedSubmissionPosition).Title,
                        Toast.LENGTH_LONG).show();
                viewModel.deleteSubmission(drafts.get(selectedSubmissionPosition));
                mAdapter.notifyItemRemoved(selectedSubmissionPosition);
            }
        }).setNegativeButton(R.string.action_no, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.MyViewHolder> {
        private List<SubmissionTable> mDrafts;
        CustomSubClickListener subClickListener;

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

        public DraftsAdapter(CustomSubClickListener listener){
            subClickListener = listener;
        }

        public void updateDrafts(List<SubmissionTable> drafts){
            mDrafts = drafts;
            notifyDataSetChanged();
        }

        public DraftsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subrow, parent, false);
            final DraftsAdapter.MyViewHolder myViewHolder = new DraftsAdapter.MyViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subClickListener.onSubClick(view, mDrafts.get(myViewHolder.getAdapterPosition()).Master_ID);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() { //Enable long click on a case entry
                @Override
                public boolean onLongClick(View view) {
                    subClickListener.onSubLongClick(view, myViewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
            });

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if(drafts.get(position).Case_ID == NULL){
                holder.caseTextView.setText(R.string.pending);
            } else {
                holder.caseTextView.setText(String.valueOf(drafts.get(position).Case_ID));
            }
            holder.titleTextView.setText(drafts.get(position).Title);
            calendar.setTimeInMillis(drafts.get(position).DateOfCreation);
            holder.dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
        }

        @Override
        public int getItemCount() {
            if(mDrafts != null){
                return mDrafts.size();
            } else {
                return 0;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drafts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_viewdrafts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.draftsRecyclerView);
        final Intent create_sub_intent = new Intent(this, CreateSubActivity.class);

        mAdapter = new DraftsAdapter(new CustomSubClickListener() {
            @Override
            public void onSubClick(View v, long internalID) {
                create_sub_intent.putExtra("draft", internalID);
                startActivity(create_sub_intent);
            }

            @Override
            public boolean onSubLongClick(View v, int position) {
                createDeleteDialog(position);
                return true;
            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        viewModel.getDrafts().observe(this, new Observer<List<SubmissionTable>>() {
            @Override
            public void onChanged(@Nullable List<SubmissionTable> submissionTables) {
                drafts = submissionTables;
                mAdapter.updateDrafts(submissionTables);
            }
        });
    }


}
