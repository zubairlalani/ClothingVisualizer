package com.example.reshma.realfashionproject;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.reshma.realfashionproject.DisplayImage.PICK_IMAGE;
import static com.example.reshma.realfashionproject.DisplayImage.currentPhotoPath;
import static com.example.reshma.realfashionproject.MainActivity.CAM_REQUEST;

public class ShowPopUp extends Activity {

    Button camOption;
    Button galleryOption;
    String currentPath;
    static Boolean pickedCam = false;
    static Boolean pickedGallery = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);


        camOption = findViewById(R.id.id_camOption);
        galleryOption = findViewById(R.id.id_galleryOption);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.4));
        // creates the window

        camOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                       //if the camera button is picked in the option window

                pickedCam = true;
                pickedGallery = false;                              //the camera button was picked and the gallery wasn't picked

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);         //creates the built-in camera intent


                if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();                                      //creates a new file
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),              //Creates a Uri
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);                  //send in the photo uri
                        takePictureIntent.putExtra("hello", currentPath);
                        currentPhotoPath = currentPath;                                                 //currentPhotoPath is accessible by all classes --
                                                                                                              // set the currentPhotoPath to the path before the path is reset
                                                                                                                //when you go back to the original intent

                        startActivityForResult(takePictureIntent, CAM_REQUEST);                 //opens built-in camera

                    }
                }

            }
        });

        galleryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //if gallery button is clicked
                pickedCam = false;
                pickedGallery = true;                                                //camera button was NOT picked and the Gallery Button was picked in the popup window
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);                         //open built-in gallery app


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAM_REQUEST){                     //if coming back from the built-in camera
            if (resultCode == RESULT_OK){

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK , returnIntent);               //close the pop up window and go back to the app
                finish();

            }
            else{
                Toast.makeText(ShowPopUp.this, ""+resultCode, Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == PICK_IMAGE){                     //if an image is picked from the built-in Gallery
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();                                //get the URI because data holds the information from the gallery intent at this point
                    Intent goBack = new Intent();
                    goBack.putExtra("uri", imageUri.toString());                        //send uri back to the app
                    setResult(Activity.RESULT_OK, goBack);
                    finish();                                                               //close pop-up window and go back to app

                }
                catch (Exception e){
                    Toast.makeText(this, "ERROR "+e, Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(ShowPopUp.this, "oops", Toast.LENGTH_SHORT).show();
            }
        }
    }


         private File createImageFile() throws IOException  {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //File storageDir = Environment.getExternalStorageDirectory();
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents

            currentPath = image.getAbsolutePath();                      //get the path of the image

            return image;                           //return the file//
         }


}


