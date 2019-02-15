package com.myvetpath.myvetpath;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;

//This screen will show the draft that the user was was previously working on
public class ViewDraftsActivity extends BaseActivity {

    MyDBHandler dbHandler;
    boolean subTableExists;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Submission[] drafts;

    public void createDeleteDialog(final int selectedSubmissionPosition){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewDraftsActivity.this);
        dialog.setCancelable(true);
        String title = getString(R.string.action_delete_confirmation_prompt_first_part)
                + drafts[selectedSubmissionPosition].getTitle()
                + getString(R.string.action_delete_confirmation_second_part);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ViewDraftsActivity.this,  R.string.deleted_message + drafts[selectedSubmissionPosition].getTitle(),
                        Toast.LENGTH_LONG).show();
                dbHandler.deleteSubmission(drafts[selectedSubmissionPosition].getInternalID());
                mAdapter.notifyItemRemoved(selectedSubmissionPosition);
            }
        }).setNegativeButton(R.string.action_no, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.MyViewHolder> {
        private Submission[] mDrafts;
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

        public DraftsAdapter(Submission[] myDrafts, CustomSubClickListener listener){
            mDrafts = myDrafts;
            subClickListener = listener;
        }

        public DraftsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subrow, parent, false);
            final DraftsAdapter.MyViewHolder myViewHolder = new DraftsAdapter.MyViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subClickListener.onSubClick(view, mDrafts[myViewHolder.getAdapterPosition()].getInternalID());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() { //Enable long click on a case entry
                @Override
                public boolean onLongClick(View view) {
                    subClickListener.onSubLongClick(view, myViewHolder.getAdapterPosition());
                    return true;
                }
            });

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if(drafts[position].getCaseID() == NULL){
                holder.caseTextView.setText(R.string.pending);
            } else {
                holder.caseTextView.setText(String.valueOf(drafts[position].getCaseID()));
            }
            holder.titleTextView.setText(drafts[position].getTitle());
            calendar.setTimeInMillis(drafts[position].getDateOfCreation());
            holder.dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
        }

        @Override
        public int getItemCount() {
            return dbHandler.getNumberOfDrafts();
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

        dbHandler = new MyDBHandler(this);
        subTableExists = dbHandler.doesTableExist(Submission.TABLE_NAME);

        drafts = dbHandler.getDrafts();

        mAdapter = new DraftsAdapter(drafts, new CustomSubClickListener() {
            @Override
            public void onSubClick(View v, int caseID) {

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
    }


}
