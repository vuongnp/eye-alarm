package com.example.eyealarm.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eyealarm.R;
import com.example.eyealarm.data.Alarm;
import com.example.eyealarm.service.AlarmService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RingActivity extends AppCompatActivity {
    @BindView(R.id.activity_ring_dismiss) Button dismiss;
    @BindView(R.id.activity_ring_snooze) Button snooze;
    @BindView(R.id.activity_ring_clock) ImageView clock;
    @BindView(R.id.activity_ring_txtRing) TextView txtRing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.royal_blue));

        actionBar.setBackgroundDrawable(colorDrawable);

        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = "" + mdformat.format(calendar.getTime());
        txtRing.setText(strDate);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
//                getApplicationContext().stopService(intentService);
//                finish();
                gotoCameraActivity();
            }
        });

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 10); // add 10 minutes

                Alarm alarm = new Alarm(
                        new Random().nextInt(Integer.MAX_VALUE),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        "Snooze",
                        System.currentTimeMillis(),
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false
                );

                alarm.schedule(getApplicationContext());

                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });
        animateClock();
        animateTextView();
    }

    private void animateClock(){
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }

    private void animateTextView(){
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(txtRing, "translationX", -300f, 300f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(1000);
        rotateAnimation.start();
    }

    private void gotoCameraActivity(){
        Intent gotoCameraActivityIntent = new Intent(this, CameraActivity.class);
        startActivity(gotoCameraActivityIntent);
    }
}
