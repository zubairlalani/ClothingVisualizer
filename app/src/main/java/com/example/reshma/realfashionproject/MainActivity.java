package com.example.reshma.realfashionproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity{


    ImageButton backButton;
    LinearLayoutManager linearLayoutManager;
    Parcelable listState;
    String LIST_STATE_KEY = "key";
    int counter = 1;

    public static ArrayList<String> shirtPaths = new ArrayList<>();
    public static ArrayList<String> pantPaths = new ArrayList<>();
    public static ArrayList<String> shoePaths = new ArrayList<>();

    //Create a save button


    ImageButton savedFits;
    ImageButton createFit;


    String[] permissions;
    int REQUEST_PERMISSION = 1;
    String data;

    RecyclerView recyclerView;

    static final int CAM_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_saved_fits);
        permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(OpenCVLoader.initDebug())
        {
            //Toast.makeText(getApplicationContext(),"OpenCV loaded successfully", Toast.LENGTH_SHORT).show();
        }
        else
            //Toast.makeText(getApplicationContext(),"Could not load openCV", Toast.LENGTH_SHORT).show();

        if(!hasPermissions(this, permissions)){
           // Toast.makeText(this, "Doesn't have permissions", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        }

        savedFits = findViewById(R.id.id_oldFits);
        createFit = findViewById(R.id.id_createFit);


        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput("pat.json")));
            String line = bufferedReader.readLine();

            while(line!=null){
                if(counter == 1)
                    shirtPaths = new ArrayList<>(Arrays.asList(line.split(" ")));
                else if(counter == 2){
                    pantPaths = new ArrayList<>(Arrays.asList(line.split(" ")));
                }
                else if(counter == 3){
                    shoePaths = new ArrayList<>(Arrays.asList(line.split(" ")));
                }
                line = bufferedReader.readLine();
                counter++;
            }
            initRecyclerView();
        }catch (Exception e){
            Log.d("TAG", "ERROR IN READING TO DATA");
        }


        if(shirtPaths.isEmpty() || pantPaths.isEmpty() || shoePaths.isEmpty())
            savedFits.setClickable(false);
        else
            savedFits.setClickable(true);


        savedFits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        });


        createFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayImage.class);
                startActivity(intent);
            }
        });


    }


    private void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.id_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter( shirtPaths, pantPaths, shoePaths, this);
        recyclerView.setAdapter(adapter);
    }


    public static boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




}




