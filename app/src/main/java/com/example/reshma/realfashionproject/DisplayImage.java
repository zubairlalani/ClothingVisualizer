package com.example.reshma.realfashionproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.reshma.realfashionproject.MainActivity.pantPaths;
import static com.example.reshma.realfashionproject.MainActivity.shirtPaths;
import static com.example.reshma.realfashionproject.MainActivity.shoePaths;
import static com.example.reshma.realfashionproject.ShowPopUp.pickedCam;
import static com.example.reshma.realfashionproject.ShowPopUp.pickedGallery;

public class DisplayImage extends AppCompatActivity {

    public static String info = "";

    ImageButton iv;
    ImageButton iv2;
    ImageButton iv3;

    ImageButton savedFits;   //button to go back to main menu
    ImageButton createFits;

    ImageButton saveButton;
    int numb;
    File file;

    public static final int PICK_IMAGE = 3;
    public static final int REQUEST_IMAGE = 2;

    Bitmap bm;

    static String currentPhotoPath;
    String galleryPath;
    private String shirt,pants,shoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_image);
        currentPhotoPath = "HEHE";

        iv = findViewById(R.id.id_imageView);
        iv2 = findViewById(R.id.id_imageView2);         //set ID's
        iv3 = findViewById(R.id.id_imageView3);

        savedFits = findViewById(R.id.id_oldFits2);
        createFits = findViewById(R.id.id_createFit2);

        iv.setImageResource(R.drawable.shirticon);
        iv.setTag("1");
        iv2.setImageResource(R.drawable.panticon);
        iv2.setTag("1");
        iv3.setImageResource(R.drawable.shoeicon);
        iv3.setTag("1");

        saveButton = findViewById(R.id.id_saveButton);



        if(OpenCVLoader.initDebug())
        {
            //Toast.makeText(getApplicationContext(),"OpenCV loaded successfully", Toast.LENGTH_SHORT).show();   //check if the openCV loader has loaded properly
        }
        else
            //Toast.makeText(getApplicationContext(),"Could not load openCV", Toast.LENGTH_SHORT).show();


        //if click on imagebutton open pop up that asks take photo or go to gallery
                 //based on answer either open camera or open gallery
                        //either option should automatically lower resolution and remove the background of the image
                //once all the images are set click save button


        iv.setClickable(true);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(DisplayImage.this, "clicked2", Toast.LENGTH_SHORT).show();

                numb =1;
                Intent popUpIntent = new Intent(getApplicationContext(), ShowPopUp.class);
                //Toast.makeText(DisplayImage.this, "clicked1", Toast.LENGTH_SHORT).show();
                startActivityForResult(popUpIntent, REQUEST_IMAGE);


            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numb =2;
                Intent popUpIntent = new Intent(getApplicationContext(), ShowPopUp.class);          //sets up three imageButtons and opens an option menu when you click on them
                startActivityForResult(popUpIntent, REQUEST_IMAGE);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numb =3;
                Intent popUpIntent = new Intent(getApplicationContext(), ShowPopUp.class);      //Toast.makeText(DisplayImage.this, "clicked3", Toast.LENGTH_SHORT).show();
                startActivityForResult(popUpIntent, REQUEST_IMAGE);
            }
        });

        savedFits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        createFits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayImage.class);
                startActivity(intent);
            }
        });

    }
    Bitmap ShrinkBitmap(String file, int width, int height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }                         //changes the resolution of an image/bitmap to make grabcut algorithm work faster

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)              //occurs when the pop-up option menu intent finishes
    {
        switch (requestCode) {          //request code changes based on which intent you are dealing with (gallery or camera)

            case REQUEST_IMAGE: {                           //when pop up image is done

                if(resultCode == RESULT_OK) {

                    if(pickedCam && !pickedGallery)                 //if the camera option was chosen
                    {
                        bm = ShrinkBitmap(currentPhotoPath, 150, 150);
                    }
                    else if(!pickedCam && pickedGallery)            //if the gallery option was chosen
                    {
                        String uriString = data.getStringExtra("uri");                                  //gets the URI from the intent where the gallery intent was called
                        final Uri imageUri = Uri.parse(uriString);

                        try
                        {
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);          //all of this code is in order to get the path from the gallery image
                            bm = BitmapFactory.decodeStream(imageStream);
                        }
                        catch(IOException e)
                        {
                            Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show();
                        }


                        galleryPath = getPath(getApplicationContext(), imageUri);
                        bm = ShrinkBitmap(galleryPath, 150, 150);

                    }


                    bm = removeBackground(bm);              //removes background and sets image to the image button based on which one was clicked

                    if (numb == 1) {
                        iv.setImageBitmap(bm);
                        try {
                            file = createImageFile(1);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else if (numb == 2) {
                        iv2.setImageBitmap(bm);
                        try {
                            file = createImageFile(2);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else if (numb == 3) {
                        iv3.setImageBitmap(bm);
                        try {
                            file = createImageFile(3);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    try{
                        FileOutputStream out = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, out);    // bm is the Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info = "";
                            shirtPaths.add(shirt);
                            pantPaths.add(pants);
                            shoePaths.add(shoe);

                            for (String arr: shirtPaths) {
                                info += arr+" ";
                            }
                            info += "\n";
                            for (String arr: pantPaths) {
                                info += arr+" ";
                            }
                            info += "\n";
                            for (String arr: shoePaths) {
                                info += arr+" ";
                            }

                            try {
                                OutputStreamWriter writer = new OutputStreamWriter(openFileOutput("pat.json", MODE_PRIVATE));

                                if(info!=null)
                                    writer.write(info);

                                writer.close();
                            }catch (Exception e){
                                Log.d("TAG", "Error: "+e);
                            }
                    }});

                }
            }

        }

    }

    public static String getPath(Context context, Uri uri ) {
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
    }               //given the context and the uri it gets the path of the image

    public Bitmap removeBackground(Bitmap bitmap) {
        //GrabCut part
        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);


        int r = img.rows();
        int c = img.cols();
        Point p1 = new Point(c / 100, r / 100);
        Point p2 = new Point(c - c / 100, r - r / 100);
        Rect rect = new Rect(p1, p2);

        Mat mask = new Mat();
        Mat fgdModel = new Mat();
        Mat bgdModel = new Mat();

        Mat imgC3 = new Mat();
        Imgproc.cvtColor(img, imgC3, Imgproc.COLOR_RGBA2RGB);

        Imgproc.grabCut(imgC3, mask, rect, bgdModel, fgdModel, 10, Imgproc.
                GC_INIT_WITH_RECT);

        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3.0));
        Core.compare(mask, source, mask, Core.CMP_EQ);

        //This is important. You must use Scalar(255,255, 255,255), not Scalar(255,255,255)

        Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255,
                255, 255,255));
        img.copyTo(foreground, mask);

        // convert matrix to output bitmap
        bitmap = Bitmap.createBitmap((int) foreground.size().width,
                (int) foreground.size().height,
                Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(foreground, bitmap);

        return bitmap;
    }                       //removebackground algorithm from openCV
    private File createImageFile( int numb) throws IOException  {
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

        //get the path of the image
        if(numb == 1){
            shirt = image.getAbsolutePath();
        }
        else if(numb == 2){
            pants = image.getAbsolutePath();
        }
        else if(numb ==3){
            shoe = image.getAbsolutePath();
        }

        return image;                           //return the file//
    }


}

