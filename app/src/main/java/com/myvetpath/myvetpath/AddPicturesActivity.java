package com.myvetpath.myvetpath;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.PictureTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//This is for the screen where users can add pictures. It is called when people click the camera icon on the CreateSubActivity screen
public class AddPicturesActivity extends AppCompatActivity {

    String LOGTAG = AddPicturesActivity.class.getSimpleName();

    private static final int GALLERY_PICTURE = 1;
    private int chosen_method;
    private int PICK_IMAGE_REQUEST = 1;

    private GridLayout mainGrid;

    private ImageButton[] images;
    private int selectedImageView;
    private int defaultAddPictureImageRESID;

    String [] longitudes = {"", "", "", "", ""};
    String [] latitudes = {"", "", "", "", ""};
    String [] imageNames = {null, null, null, null, null};
    Long [] imageDates = {null, null, null, null, null};
    Bitmap [] imageBitmaps = {null, null, null, null, null};
    String[] picturePaths = {"", "", "", "", ""};

    private LocationManager locationManager;
    private LocationListener listener;
    Boolean shouldCheckPhoneCoordinates = false;

    private ArrayList<PictureTable> picturesList;

    private boolean didUploadImages[] = {false, false, false, false, false};

    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;

    MyVetPathViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pictures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_addpictures);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        picturesList = (ArrayList<PictureTable>) intent.getSerializableExtra("pictureList");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storePicturesInDB();
                Log.d(LOGTAG, "");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pictureResults", picturesList);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

        });

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        images = new ImageButton[]{findViewById(R.id.firstImageBttn), findViewById(R.id.secondImageBttn),
                findViewById(R.id.thirdImageBttn), findViewById(R.id.fourthImageBttn)//,
                //findViewById(R.id.fifthImageBttn)
        };

        defaultAddPictureImageRESID = R.drawable.ic_action_add_photo;


         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         listener = new LocationListener() {

             //Autogenerated code stub...needed to use locationManager
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
             //Autogenerated code stub...needed to use locationManager
            @Override
            public void onProviderEnabled(String provider) {

            }

            //If the app doesn't have permission to use gps, then ask the user if it can get permission
            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

            //get the GPS coordinates if the user uploaded a picture using the camera
            @Override
            public void onLocationChanged(Location location) {
                if(shouldCheckPhoneCoordinates){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    longitudes[selectedImageView] = Double.toString(longitude);
                    latitudes[selectedImageView] = Double.toString(latitude);
//                    imageNames[selectedImageView] = "IMG_FROM_CAMERA_" + selectedImageView + ".jpg";
                }

            }
        };

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

         initializeImages();
    }




//Purpose: Set the images to whatever the user set earlier. Initializes all the data based on prior inputs
    private void initializeImages(){
        for(int i = 0; i < picturesList.size(); i++){
            PictureTable tempPicture = picturesList.get(i);
            if(tempPicture != null){
                imageNames[i] = tempPicture.Title;
                longitudes[i] = tempPicture.Longitude;
                latitudes[i] = tempPicture.Latitude;
                imageDates[i] = tempPicture.DateTaken;
                picturePaths[i] = tempPicture.ImagePath;

                Bitmap bmp = null;

                String filename = picturesList.get(i).ImagePath;
                try { //try to get the bitmap and set the image button to it
                    FileInputStream is = this.openFileInput(filename);
                    bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    images[i].setImageBitmap(bmp);
                    imageBitmaps[i] = bmp;
                    didUploadImages[i] = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

//Purpose: Stores the data gathered from this screen into the db. This is called whenever the user selects the floating action button
    private void storePicturesInDB(){
        ArrayList<PictureTable> tempList = new ArrayList<PictureTable>(5); //create a temporary list so that it is easier to deal with cases where users skip picture slots to upload

        for(int i = 0; i < 5; i++){
            if(imageNames[i] != null){ //if user uploaded image in this slot, collect all image data and add it to array list
                PictureTable tempPicture = new PictureTable();
                tempPicture.Title = imageNames[i];
                tempPicture.Longitude = longitudes[i];
                tempPicture.Latitude = latitudes[i];
                tempPicture.ImagePath = picturePaths[i];
                tempPicture.DateTaken = imageDates[i];
               if(didUploadImages[i] == false){
                   try {
                       //Write file
                       Bitmap bmp = imageBitmaps[i];
                       String filename = i + "bitmap.png";
                       FileOutputStream stream = AddPicturesActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                       bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                       //Cleanup
                       stream.close();
                       bmp.recycle();
                       tempPicture.ImagePath = filename;

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }else{
                   tempPicture.ImagePath = (i + "bitmap.png");
               }

                tempList.add(tempPicture);

            }else{ //if image not uploaded, upload a "null" picture
                Log.d("pass", "storePicturesInDB: about to set to null ");
                PictureTable emptyPicture = new PictureTable();
                emptyPicture.Title = null;
                emptyPicture.DateTaken = 0;
                emptyPicture.ImagePath = null;
                emptyPicture.Latitude = null;
                emptyPicture.Longitude = null;
                tempList.add(emptyPicture);

            }
        }
        picturesList = tempList;

    }

public String mCurrentPhotoPath;

    //Purpose: Set a click event for each of the imagebuttons. Clicking on the button should prompt the user to upload a picture
    private void setSingleEvent(GridLayout mainGrid) {
        //Loop through all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            ImageButton currentImageBttn = (ImageButton) mainGrid.getChildAt(i);
            final int index = i;
            currentImageBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //open the menu that asks if the user wants to use camera or gallery
                    selectedImageView = index;
                    UploadPicturesPrompt();
                }
            });

            currentImageBttn.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) { //asks the user if they want to delete the picture
                    startDeletePictureDialog(index);
                    return false;
                }
            });

        }
    }

    //Purpose: Ask the user if they want to delete the selected picture
    private void startDeletePictureDialog(final int selectedDeleteIndex) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Delete Confirmation");
        myAlertDialog.setMessage("Are you sure you want to delete picture #" + selectedDeleteIndex + "?");

        myAlertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) { //delete the picture by setting everything to the default values
                        Toast.makeText(AddPicturesActivity.this, "Deleted picture #" + selectedDeleteIndex, Toast.LENGTH_SHORT).show();
                        images[selectedDeleteIndex].setImageResource(defaultAddPictureImageRESID);
                        imageBitmaps[selectedDeleteIndex] = null;
                        imageNames[selectedDeleteIndex] = null;
                        latitudes[selectedDeleteIndex] = "";
                        longitudes[selectedDeleteIndex] = "";
                        imageDates[selectedDeleteIndex] = null;
                        picturePaths[selectedDeleteIndex] = null;
                    }
                });

        myAlertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() { //if the user selects no, don't do anything
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        myAlertDialog.show();
    }

public String testPath;
    //Purpose: Asks the user if they want to use the camera or gallery to upload a picture. Starts the corresponding intents
    private void UploadPicturesPrompt() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) { //open up the gallery
                        chosen_method = 0;
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) { //open up the camera
                        chosen_method = 1;

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(AddPicturesActivity.this,
                                        "com.example.android.fileprovider",
                                        photoFile);

                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            }
                        }

//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);

                    }
                });
        myAlertDialog.show();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        imageNames[selectedImageView] = imageFileName + ".jpg";


        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        if(orientation == 6){ //picture came in rotated, so rotate it by 90 degrees to make it look correct
            return rotate(bitmap, 90);
        }else if(orientation ==  3 || orientation == 1){ //phone flipped the picture horizontally, so flip it again to make it correct
            return flip(bitmap, false, true);
        }else{ //picture came in fine, so don't do anything
            return bitmap;
        }
    }


    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //purpose: this method is called automatically after the app gets a picture from the UploadPicturesPrompt
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (chosen_method == 1) { //user uploaded a picture using the camera
            File file = new File(mCurrentPhotoPath);
            picturePaths [selectedImageView] = mCurrentPhotoPath;

            Bitmap bitmap = null; //get the actual picture
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                bitmap = modifyOrientation(bitmap, mCurrentPhotoPath.substring(5));

            } catch (IOException e) {
                e.printStackTrace();
            }
            images[selectedImageView].setImageBitmap(bitmap);
            imageBitmaps[selectedImageView] = bitmap;
            imageDates[selectedImageView] = Calendar.getInstance().getTime().getTime(); //get the current date
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //Check for permissions
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                            ,10);
                    return;
                    
                }
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener); //Get the coordinates of the phone. Won't be called if the phone doesn't have permission
            shouldCheckPhoneCoordinates = true;
            Log.d("filepath", "onActivityResult: ");
        }else if(chosen_method == 0){ //The user chose to upload a picture using the gallery
            shouldCheckPhoneCoordinates = false;
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) { //check if the data is valid
                Uri uri = data.getData();
                String picturePath = getPath( this.getApplicationContext( ), uri ); //this is the path of the file in the SD card
                picturePaths[selectedImageView] = picturePath;
                //Get and store the image name
                File f = new File(picturePath);
                imageNames[selectedImageView] = f.getName();

                //Get and store the GPS coordinates
                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(uri);
                    String col = MediaStore.Images.ImageColumns.DATA;
                    Cursor c = getApplicationContext().getContentResolver().query(uri,
                            new String[]{col},
                            null, null, null);
                    ExifInterface exifInterface = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        exifInterface = new ExifInterface(in);
                    } else {
                        if (c != null && c.moveToFirst()) {
                            exifInterface = new ExifInterface(c.getString(c.getColumnIndex(col)));
                            c.close();
                        } else {
                            exifInterface = new ExifInterface(uri.getPath());
                        }
                    }

                    String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    String dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

                    //The returned date comes in as a string, so convert it into date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                    Date convertedDate = new Date();
                    try {
                        if(dateString != null) {
                            convertedDate = dateFormat.parse(dateString);
                        }
                        imageDates[selectedImageView] = convertedDate.getTime();
                    } catch (ParseException e) {
                        // Auto-generated exception
                        e.printStackTrace();
                    } catch (java.text.ParseException e) {
                        //Auto-generated exception
                        e.printStackTrace();
                    }

                    if(latitudeRef != null) {
                        if (latitudeRef.equals("N")) { //if the degrees should be positive
                            latitude = Float.toString(convertToDegree(latitude));
                        } else { //If the degrees should be negative
                            latitude = Float.toString(0 - convertToDegree(latitude));
                        }
                        if (longitudeRef.equals("N")) { //if the degrees should be positive
                            longitude = Float.toString(convertToDegree(longitude));
                        } else { //else the degrees should be negative
                            longitude = Float.toString(0 - convertToDegree(longitude));
                        }
                    } else {
                        latitude = "";
                        longitude = "";
                    }

                    longitudes[selectedImageView] = longitude;
                    latitudes[selectedImageView] = latitude;
                } catch (IOException e) {
                    // Handle any errors
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {}
                    }
                }

                try { //set and save the actual image

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //bitmap = modifyOrientation(bitmap, mCurrentPhotoPath.substring(5));
                    images[selectedImageView].setImageBitmap(bitmap);
                    imageBitmaps[selectedImageView] = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //This is a helper function that converts the longitude/latitude strings to degrees. It initially comes out with a weird format,
    //so this function turns it into a more useful, helpful form
    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    };

    //This function is used to get the path of the image selected from the gallery
    private String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
}
