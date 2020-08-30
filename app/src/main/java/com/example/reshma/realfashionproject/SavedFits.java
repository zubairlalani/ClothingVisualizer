package com.example.reshma.realfashionproject;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class SavedFits extends AppCompatActivity {

    /*
    ImageButton backButton;
    LinearLayoutManager linearLayoutManager;
    Parcelable listState;
    String LIST_STATE_KEY = "key";
    int counter = 1;

    public static ArrayList<String> shirtPaths = new ArrayList<>();
    public static ArrayList<String> pantPaths = new ArrayList<>();
    public static ArrayList<String> shoePaths = new ArrayList<>();
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_saved_fits);


/*
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        */
        /*
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput("path.json")));
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
        */

    }

    /*
    private void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.id_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter( shirtPaths, pantPaths, shoePaths, this);
        recyclerView.setAdapter(adapter);
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //listState = linearLayoutManager.onSaveInstanceState();
        //outState.putParcelable(LIST_STATE_KEY, listState);
        super.onSaveInstanceState(outState);
        //Toast.makeText(getApplicationContext(),"CLOSING", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


}

