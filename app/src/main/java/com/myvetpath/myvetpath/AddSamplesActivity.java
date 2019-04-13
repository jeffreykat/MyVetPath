package com.myvetpath.myvetpath;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.SampleTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddSamplesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ArrayList<SampleTable> samples = new ArrayList<SampleTable>();
    private ArrayList<Button> buttons = new ArrayList<Button>();

    LinearLayout myLayout;
    private Button add_sample_button;
    private NumberPicker number_of_samples_NP;
    private final int MAX_NUMBER_OF_SAMPLES = 20; //might want to change this later
    private final int MIN_NUMBER_OF_SAMPLES = 1;
    private ImageButton collection_date_button;
    private Date collectionDate = null;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    MyVetPathViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_samples);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Samples");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        samples =  (ArrayList<SampleTable>) intent.getSerializableExtra("samplesList");

        myLayout = findViewById(R.id.sampleLinearLayout); //this is the linear layout that we will programatically add to

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "" + Integer.toString(samples.size()) + " Samples Added", Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("sampleResults", samples);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        //set up number picker
        number_of_samples_NP = findViewById(R.id.number_of_samples_SP);
        number_of_samples_NP.setMaxValue(MAX_NUMBER_OF_SAMPLES);
        number_of_samples_NP.setMinValue(MIN_NUMBER_OF_SAMPLES);

        //set up add sample button
        add_sample_button = findViewById(R.id.add_new_sample_bttn);
        add_sample_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //collect sample data and store it into arraylist, and then update view
                String sampleName = ((EditText) findViewById(R.id.name_of_samples_ET)).getText().toString();
                String sampleLocation = ((EditText) findViewById(R.id.location_sample_ET)).getText().toString();
                int numberOfSamples = number_of_samples_NP.getValue();

                if(collectionDate == null || sampleName.equals("") || sampleLocation.equals("")){ //check user input information
                    Toast.makeText(AddSamplesActivity.this, "You are missing a sample field.",
                            Toast.LENGTH_LONG).show();
                }else{
                    SampleTable tempSample = new SampleTable();
                    tempSample.NameOfSample = sampleName;
                    tempSample.LocationOfSample = sampleLocation;
                    tempSample.NumberOfSample = numberOfSamples;
                    tempSample.SampleCollectionDate = collectionDate.getTime();
                    samples.add(tempSample);
                    Snackbar.make(v, "Sample added: " + tempSample.NameOfSample, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    updateSamplesView();
                }
            }
        });

        //Set up collection date button
        collection_date_button = (ImageButton) findViewById(R.id.CollectionDateBTTN);
        collection_date_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

     updateSamplesView();
    }

//This is called whenever the user selects a date. It updates a textview and variable
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = "Collected On " + DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dateSelectedTV = (TextView) findViewById(R.id.CollectionDateTV);
        dateSelectedTV.setText(currentDateString);
        collectionDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

    }

    //This is what dynamically adds views to the screen. There is probably a more efficient way of doing this, but it should work for now
    private void updateSamplesView(){
        myLayout.removeAllViews();
        int index = 0;
        for(SampleTable tempSample: samples){

            TextView sampleTitleTV = new TextView(AddSamplesActivity.this);
            sampleTitleTV.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            sampleTitleTV.setText("Sample " + (index + 1) + ":");

            final Button myButton = new Button(AddSamplesActivity.this);
            myButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            myButton.setId(index);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSampleDialog(myButton.getId());
                }
            });


            calendar.setTimeInMillis(tempSample.SampleCollectionDate);
            String tempSampleDate = simpleDateFormat.format(calendar.getTime());
            myButton.setText(tempSample.NameOfSample + ": " + tempSample.NumberOfSample +
                    " sample(s) collected in " + tempSample.LocationOfSample + " on " + tempSampleDate);


            myLayout.addView(sampleTitleTV);
            myLayout.addView(myButton);
            index++;
        }

    }

//This function creates a popup dialog that asks the user if they want to delete the selected sample
    private void createSampleDialog(final int id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddSamplesActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Delete this sample?");
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { //user said yes, so delete sample and update view
                samples.remove(id);
                updateSamplesView();

            }
        })
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Close
                    }
                });
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}
