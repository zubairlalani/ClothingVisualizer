package com.example.reshma.realfashionproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static com.example.reshma.realfashionproject.DisplayImage.currentPhotoPath;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyckerViewAdapter";
    private ArrayList<String> shPaths = new ArrayList<>();
    private ArrayList<String> ptPaths = new ArrayList<>();
    private ArrayList<String> shoPaths = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<String> shirtPaths, ArrayList<String> pantPaths, ArrayList<String> shoePaths, Context context) {
        this.shPaths = shirtPaths;
        this.ptPaths = pantPaths;
        this.shoPaths = shoePaths;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap shirtBm = ShrinkBitmap(shPaths.get(i), 150, 150);
        Bitmap pantBm = ShrinkBitmap(ptPaths.get(i), 150, 150);
        Bitmap shoeBm = ShrinkBitmap(shoPaths.get(i), 150, 150);
        //shirtBm = removeBackground(shirtBm);
        //pantBm = removeBackground(pantBm);
        //shoeBm = removeBackground(shoeBm);
        viewHolder.shirt.setImageBitmap(shirtBm);
        viewHolder.pants.setImageBitmap(pantBm);
        viewHolder.shoes.setImageBitmap(shoeBm);


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
    }

    @Override
    public int getItemCount() {
        return shPaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView shirt;
        ImageView pants;
        ImageView shoes;

        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shirt = itemView.findViewById(R.id.id_im1);
            pants = itemView.findViewById(R.id.id_im2);
            shoes = itemView.findViewById(R.id.id_im3);
        }
    }

}

