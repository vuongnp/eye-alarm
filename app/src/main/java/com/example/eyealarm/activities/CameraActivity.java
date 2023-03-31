package com.example.eyealarm.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.eyealarm.R;
import com.example.eyealarm.service.AlarmService;
import com.example.eyealarm.utils.ChallengeCenter;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import static android.graphics.ImageFormat.YUV_420_888;


public class CameraActivity extends AppCompatActivity implements ImageAnalysis.Analyzer {
    static {
        System.loadLibrary("eyealarm");
    }
    public native boolean initPipeline(String asset_path );
    public native float[] processImg(Bitmap bitmapIn);
    public native void uninitPipeline();

    private PreviewView previewView;
    private SurfaceView surfaceView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

//    private Button btnTarget;
    private Button btnChallenge;
    private Button btnGoToGameScreen;

    private TextView txtNotice;
    private TextView txtGreat;

    Paint paint = new Paint();

    private int TARGET;
    private int current_count;
    private boolean passed = true;
    private int challenge_id;
//    private int pre_challenge_id=-1;

    Random random = new Random();

    public CameraActivity(){
//        TARGET = random.nextInt(5) + 5;
        TARGET = 10;
        current_count = 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

//        copyAssets(this);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.royal_blue));

        actionBar.setBackgroundDrawable(colorDrawable);

        String outDir = String.valueOf(this.getFilesDir());
        String assetsDir = outDir+"/assets/";
        initPipeline(assetsDir);

//        btnTarget = (Button) findViewById(R.id.activity_camera_target);
        btnChallenge = (Button) findViewById(R.id.activity_camera_challenge);
        btnGoToGameScreen = (Button) findViewById(R.id.activity_camera_go_to_game);

        txtNotice = (TextView) findViewById(R.id.activity_camera_text_notice);
        txtGreat = (TextView) findViewById(R.id.activity_camera_text_great);

        previewView = findViewById(R.id.activity_camera_previewView);
        surfaceView = findViewById(R.id.activity_camera_surfaceView);

        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        startCamera();

        btnGoToGameScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGameActivity();
            }
        });
    }

    private void startCamera(){
        cameraProviderFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            }catch (ExecutionException | InterruptedException e){
                Toast.makeText(this, "Error starting camera "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), this);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }

    private Bitmap decodeImageProxyToBitmap(ImageProxy image) {
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    @Override
    public void analyze(@NonNull ImageProxy image) {
        if (passed) {
            challenge_id = random.nextInt(2) + 1; //return random value from 1->3
            passed = false;
        }
        Log.d("Challenge ID", String.valueOf(challenge_id));
        switch (challenge_id){
            case 1:
                btnChallenge.setText("OPEN your eyes");
                break;
            case 2:
                btnChallenge.setText("BLINK your eyes");
                break;
            case 3:
                btnChallenge.setText("Close left eye, open right eye");
                break;
            case 4:
                btnChallenge.setText("Open left eye, close right eye ");
                break;
        }

//        String targetButtonText = String.valueOf(Math.max(TARGET - current_count,0));
//        btnTarget.setText(targetButtonText);

        if(image.getFormat() == YUV_420_888){
            Bitmap bitmapImage = decodeImageProxyToBitmap(image);
            Matrix mtx = new Matrix();
            mtx.postRotate(270); // rotate 270 degree for front camera with portrait mode
            bitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), mtx, true);
            float[] all_results = processImg(bitmapImage);
//            Log.d("Num results", String.valueOf(all_results[0]));
            Canvas canvas = surfaceView.getHolder().lockCanvas();
            int rotation = image.getImageInfo().getRotationDegrees();
            if (canvas != null  && all_results != null){
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                visualization(canvas, all_results, image.getWidth(), image.getHeight(), rotation);
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
            if (all_results[0]>1){
                txtNotice.setText("Too many faces detected");
            }else if(all_results[0]==1){
                txtNotice.setText("");
                int challenge_status = ChallengeCenter.challengeManager(all_results, challenge_id);
                Log.d("Challenge Status", String.valueOf(challenge_status));
                if (challenge_status == 1){
                    passed = true;
                    current_count += 1;
                    txtGreat.setVisibility(View.VISIBLE);
                }else{
                    txtGreat.setVisibility(View.INVISIBLE);
                }
            }else{
                txtNotice.setText("No face detected");
            }
        }
        if(current_count>=TARGET){
            txtGreat.setVisibility(View.INVISIBLE);
            btnChallenge.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Ping Pong!", Toast.LENGTH_SHORT).show();
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
//                                Toast.makeText(getApplicationContext(), "Ping Pong!", Toast.LENGTH_SHORT).show();
                }
                public void onFinish() {
                    dismissAlarm();
                }
            }.start();
        }
        image.close();
    }

    public void visualization(Canvas canvas, float[] all_results, int frameWidth, int frameHeight, int rotation){
        paint.setColor(Color.parseColor("#58e073"));
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        //Get the frame rotatiom
        int w, h;
        if (rotation == 0 || rotation == 180){
            w = frameWidth;
        }else{
            w = frameHeight;
        }
        if (rotation == 0 || rotation == 180){
            h = frameHeight;
        }else{
            h = frameWidth;
        }
        // calculate ratio between frame and screen system
        float scaleX = Float.valueOf(previewView.getWidth())/w;
        float scaleY = Float.valueOf(previewView.getHeight())/h;
//        String scalexy = String.valueOf(rotation) + " + " + String.valueOf(frameWidth) + " + " + String.valueOf(frameHeight) + " + " + String.valueOf(previewView.getWidth()) + " + " + String.valueOf(previewView.getHeight());
//        Log.d("Rotation - Frame width - Frame height - View width - View height ", scalexy);
        float scale, xoff, yoff;
        if (scaleX > scaleY) {
            scale = scaleX;
            xoff = 0f;
            yoff = (Float.valueOf(previewView.getHeight())-scale*h)/2;
        }else{
            scale = scaleY;
            yoff = 0f;
            xoff = (Float.valueOf(previewView.getWidth())-scale*w)/2;
        }

        int num_persons = (int)(all_results[0]);
        for (int i = 0; i < num_persons; i++){
            int pos = 1 + i * 42;
            float xmin = all_results[pos + 0] * scale + xoff;
            float ymin = all_results[pos + 1] * scale + yoff;
            float xmax = all_results[pos + 2] * scale + xoff;
            float ymax = all_results[pos + 3] * scale + yoff;
//            String frame = String.valueOf(all_results[pos + 0]) + " = " + String.valueOf(all_results[pos + 1]) + " = " + String.valueOf(all_results[pos + 2]) + " = "+ String.valueOf(all_results[pos + 3]);
//            String screen = String.valueOf(xmin) + " = " + String.valueOf(ymin) + " = " + String.valueOf(xmax) + " = "+ String.valueOf(ymax);
//            Log.e("Frame ", frame);
//            Log.e("Screen ", screen);
            canvas.drawRect(xmin, ymin, xmax, ymax, paint);

//            float x1_l = all_results[pos + 6] * scale + xoff;
//            float y1_l = all_results[pos + 7] * scale + yoff;
//            float x2_l = all_results[pos + 8] * scale + xoff;
//            float y2_l = all_results[pos + 9] * scale + yoff;
//            float x3_l = all_results[pos + 10] * scale + xoff;
//            float y3_l = all_results[pos + 11] * scale + yoff;
//            float x4_l = all_results[pos + 12] * scale + xoff;
//            float y4_l = all_results[pos + 13] * scale + yoff;
//            float x5_l = all_results[pos + 14] * scale + xoff;
//            float y5_l = all_results[pos + 15] * scale + yoff;
//            float x6_l = all_results[pos + 16] * scale + xoff;
//            float y6_l = all_results[pos + 17] * scale + yoff;
//            float x7_l = all_results[pos + 18] * scale + xoff;
//            float y7_l = all_results[pos + 19] * scale + yoff;
//            float x8_l = all_results[pos + 20] * scale + xoff;
//            float y8_l = all_results[pos + 21] * scale + yoff;
            float x9_l = all_results[pos + 22] * scale + xoff;
            float y9_l = all_results[pos + 23] * scale + yoff;

//            canvas.drawLine(x1_l, y1_l, x2_l, y2_l, paint);
//            canvas.drawLine(x2_l, y2_l, x3_l, y3_l, paint);
//            canvas.drawLine(x3_l, y3_l, x4_l, y4_l, paint);
//            canvas.drawLine(x4_l, y4_l, x5_l, y5_l, paint);
//            canvas.drawLine(x5_l, y5_l, x6_l, y6_l, paint);
//            canvas.drawLine(x6_l, y6_l, x7_l, y7_l, paint);
//            canvas.drawLine(x7_l, y7_l, x8_l, y8_l, paint);
//            canvas.drawLine(x1_l, y1_l, x8_l, y8_l, paint);
            canvas.drawPoint(x9_l, y9_l, paint);

//            float x1_r = all_results[pos + 24] * scale + xoff;
//            float y1_r = all_results[pos + 25] * scale + yoff;
//            float x2_r = all_results[pos + 26] * scale + xoff;
//            float y2_r = all_results[pos + 27] * scale + yoff;
//            float x3_r = all_results[pos + 28] * scale + xoff;
//            float y3_r = all_results[pos + 29] * scale + yoff;
//            float x4_r = all_results[pos + 30] * scale + xoff;
//            float y4_r = all_results[pos + 31] * scale + yoff;
//            float x5_r = all_results[pos + 32] * scale + xoff;
//            float y5_r = all_results[pos + 33] * scale + yoff;
//            float x6_r = all_results[pos + 34] * scale + xoff;
//            float y6_r = all_results[pos + 35] * scale + yoff;
//            float x7_r = all_results[pos + 36] * scale + xoff;
//            float y7_r = all_results[pos + 37] * scale + yoff;
//            float x8_r = all_results[pos + 38] * scale + xoff;
//            float y8_r = all_results[pos + 39] * scale + yoff;
            float x9_r = all_results[pos + 40] * scale + xoff;
            float y9_r = all_results[pos + 41] * scale + yoff;

//            canvas.drawLine(x1_r, y1_r, x2_r, y2_r, paint);
//            canvas.drawLine(x2_r, y2_r, x3_r, y3_r, paint);
//            canvas.drawLine(x3_r, y3_r, x4_r, y4_r, paint);
//            canvas.drawLine(x4_r, y4_r, x5_r, y5_r, paint);
//            canvas.drawLine(x5_r, y5_r, x6_r, y6_r, paint);
//            canvas.drawLine(x6_r, y6_r, x7_r, y7_r, paint);
//            canvas.drawLine(x7_r, y7_r, x8_r, y8_r, paint);
//            canvas.drawLine(x1_r, y1_r, x8_r, y8_r, paint);
            canvas.drawPoint(x9_r, y9_r, paint);
        }
    }

    private void dismissAlarm(){
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
        finishAffinity();
//        System.exit(0);
    }

    private void gotoGameActivity(){
        finish();
        Intent gotoGameActivityIntent = new Intent(this, GameActivity.class);
        startActivity(gotoGameActivityIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uninitPipeline();
    }
}

