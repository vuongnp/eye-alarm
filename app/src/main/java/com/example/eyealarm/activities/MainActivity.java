package com.example.eyealarm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.eyealarm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CAMERA_STORAGE = 401;
//    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.royal_blue));

        actionBar.setBackgroundDrawable(colorDrawable);

        copyAssets(this);

        requestCameraStorage();
    }

    private void requestCameraStorage(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CAMERA_STORAGE);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CAMERA_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA_STORAGE) {
            if (grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }

                if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyAssets(Context context) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
//                Log.d("assetManager is: ", assetManager.toString());
                Log.d("file name is: ", String.valueOf(filename));

//                File outDir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/assets");
                File outDir = new File (context.getFilesDir().getAbsolutePath() + "/assets");
                if (!outDir.isDirectory()){
                    Log.d("mkdir", "made");
                    outDir.mkdirs();
                }
                Log.d("assets dir: ", String.valueOf(outDir));
//                String outDir = String.valueOf(context.getFilesDir());

                File outFile = new File( outDir, filename);
                Log.d("file: ", String.valueOf(outFile));

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}