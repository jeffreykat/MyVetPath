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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.adapters.SubmissionAdapter;
import com.myvetpath.myvetpath.data.MyVetPathAPI;
import com.myvetpath.myvetpath.data.SubmissionTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.Types.NULL;


public class ViewSubsActivity extends BaseActivity implements SubmissionAdapter.OnSubmissionClickListener {

    private MyVetPathViewModel viewModel;

    Intent create_sub_activity;
    Intent sub_details_activity;
    boolean subTableExists;

    private RecyclerView mRecyclerView;
    private SubmissionAdapter mAdapter;
    private List<SubmissionTable> submissions;

    public void createDeleteDialog(final SubmissionTable submission){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewSubsActivity.this);
        dialog.setCancelable(true);
        String title = getString(R.string.action_delete_confirmation_prompt_first_part)
                + submission.Title
                + getString(R.string.action_delete_confirmation_second_part);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ViewSubsActivity.this, getString(R.string.deleted_message) + submission.Title,
                        Toast.LENGTH_LONG).show();
                viewModel.deleteSubmission(submission);
            }
        }).setNegativeButton(R.string.action_no, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void onSubmissionClick(SubmissionTable sub) {
        sub_details_activity.putExtra("submission", sub);
        Log.d("ViewSubsActivity", "Master_ID in list: "+ Long.toString(sub.Master_ID));
        startActivity(sub_details_activity);
    }

    @Override
    public void onSubmissionLongClick(SubmissionTable sub) {
        createDeleteDialog(sub);
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SubmissionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        viewModel.getSubmissions().observe(this, new Observer<List<SubmissionTable>>() {
            @Override
            public void onChanged(@Nullable List<SubmissionTable> submissionTables) {
                mAdapter.updateSubs(submissionTables);
            }
        });
    }



}
