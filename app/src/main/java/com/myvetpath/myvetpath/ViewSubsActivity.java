package com.myvetpath.myvetpath;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class ViewSubsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    Intent create_sub_activity;
    Intent sub_details_activity;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int selectedSubmissionPosition; //keeps track of what entry was selected for long press
    private String[] subsTitles;
    private String[] subsDates;
    private String[] subsCaseID;

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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewSubsActivity.this);
                String confirmationMessage = getString(R.string.action_delete_confirmation_prompt_first_part) + subsTitles[selectedSubmissionPosition]
                                            + getString(R.string.action_delete_confirmation_second_part); //Create confirmation message by including case title
                dialogBuilder.setMessage(confirmationMessage).setCancelable(false).setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    //This function sets what happens when user clicks on the "yes" button in the dialog box. It should delete the submission from the SQLite database
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ViewSubsActivity.this, R.string.deleted_message,
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



    public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.MyViewHolder> {
        private String[] mDataset;
        private String[] mDatesset;
        private String[] mCaseset;

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

        public SubsAdapter(String[] myDataset, String[] myDatesset, String[] myCaseset, CustomSubClickListener listener) {
            mDataset = myDataset;
            mDatesset = myDatesset;
            mCaseset = myCaseset;
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
                    clickListener.onSubClick(view, myViewHolder.getAdapterPosition());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() { //Enable long click on a case entry
                @Override
                public boolean onLongClick(View view) {
                    clickListener.onSubLongClick(view, myViewHolder.getAdapterPosition());
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
            holder.titleTextView.setText(mDataset[position]);
            holder.dateTextView.setText(mDatesset[position]);
            holder.caseTextView.setText(mCaseset[position]);

        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Submissions");
        setSupportActionBar(toolbar);

        create_sub_activity = new Intent(this, CreateSubActivity.class);
        sub_details_activity = new Intent(this, SubDetailsActivity.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.subsRecyclerView);

        subsTitles = getResources().getStringArray(R.array.subsTitles);
        subsDates = getResources().getStringArray(R.array.subsDates);
        subsCaseID = getResources().getStringArray(R.array.subsCaseIDs);

        mAdapter = new SubsAdapter(subsTitles, subsDates, subsCaseID, new CustomSubClickListener() {
            @Override
            public void onSubClick(View v, int position) {
                sub_details_activity.putExtra("pos", position);
                startActivity(sub_details_activity);
            }

            @Override
            public boolean onSubLongClick(View v, int position) { //Set it so long clicking on an entry shows the popoup menu
                showPopup(v, position);
                return true;
            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            startActivity(create_sub_activity);
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
