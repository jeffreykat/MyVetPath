package com.myvetpath.myvetpath;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//This screen will show the draft that the user was was previously working on
public class ViewDraftsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    MyDBHandler dbHandler;
    boolean subTableExists;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int selectedSubmissionPosition; //keeps track of what entry was selected for long press
    private Submission[] drafts;

    //This method is used to show the delete popup option when the user long clicks on a submission.
    //Parameters: the view and the position of the entry that was clicked on
    public void showPopup(View v, int pos){
        PopupMenu popup = new PopupMenu(this, v);
        selectedSubmissionPosition = pos;
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.view_submission_delete_menu);
        popup.show();

    }

    //This function shows the dialog box that confirms with the user if they want to delete the submission.
    //It is called whenever the user clicks on the delete option from the popup menu
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            default: //right now we have plans to only include a "Delete" option. If we ever add more, we will need to add more switch cases
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewDraftsActivity.this);

                String confirmationMessage = getString(R.string.action_delete_confirmation_prompt_first_part) + drafts[selectedSubmissionPosition].getTitle()
                        + getString(R.string.action_delete_confirmation_second_part); //Create confirmation message by including case title

                dialogBuilder.setMessage(confirmationMessage).setCancelable(false).setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    //This function sets what happens when user clicks on the "yes" button in the dialog box. It should delete the submission from the SQLite database and show a message to the user saying that the entry was deleted
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ViewDraftsActivity.this, R.string.deleted_message,
                                Toast.LENGTH_LONG).show();
                        //TODO: Delete submission from SQLite database
                    }
                })
                        .setNegativeButton(R.string.action_no, null); //Create "No" option. Set it to null to just dismiss the dialog button
                AlertDialog alert = dialogBuilder.create();
                alert.setTitle(getString(R.string.action_delete_confirmation));
                alert.show();
                return true;
        }

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
            holder.caseTextView.setText(String.valueOf(drafts[position].getInternalID()));
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
                showPopup(v, position);
                return true;
            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_info) {
            return true;
        }
        if (id == R.id.action_submission) {
            return true;
        }
        if (id == R.id.action_viewsubs) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
