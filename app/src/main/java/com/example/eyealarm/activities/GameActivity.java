package com.example.eyealarm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eyealarm.R;
import com.example.eyealarm.service.AlarmService;
import com.example.eyealarm.utils.GameUtils;
import com.example.eyealarm.utils.ImageAdapter;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    GridView gridView;
    int bigheros[] = {R.drawable.bighero1, R.drawable.bighero2, R.drawable.bighero3, R.drawable.bighero4, R.drawable.bighero5, R.drawable.bighero6, R.drawable.bighero7,
            R.drawable.bighero8, R.drawable.bighero9, R.drawable.bighero10, R.drawable.bighero11, R.drawable.bighero12};
    int doraemons[] = {R.drawable.doremon1, R.drawable.doremon2, R.drawable.doremon3, R.drawable.doremon4, R.drawable.doremon5, R.drawable.doremon6, R.drawable.doremon7,
        R.drawable.doremon8, R.drawable.doremon9, R.drawable.doremon10, R.drawable.doremon11, R.drawable.doremon12};
    int spidermans[] = {R.drawable.spiderman1, R.drawable.spiderman2, R.drawable.spiderman3, R.drawable.spiderman4, R.drawable.spiderman5, R.drawable.spiderman6, R.drawable.spiderman7,
        R.drawable.spiderman8, R.drawable.spiderman9, R.drawable.spiderman10, R.drawable.spiderman11, R.drawable.spiderman12};
    int messis[] = {R.drawable.messi1, R.drawable.messi2, R.drawable.messi3, R.drawable.messi4, R.drawable.messi5, R.drawable.messi6, R.drawable.messi7,
        R.drawable.messi8, R.drawable.messi9, R.drawable.messi10, R.drawable.messi11, R.drawable.messi12};
    int ronaldos[] = {R.drawable.ronaldo1, R.drawable.ronaldo2, R.drawable.ronaldo3, R.drawable.ronaldo4, R.drawable.ronaldo5, R.drawable.ronaldo6, R.drawable.ronaldo7,
        R.drawable.ronaldo8, R.drawable.ronaldo9, R.drawable.ronaldo10, R.drawable.ronaldo11, R.drawable.ronaldo12};
    int sources[];
    int targets[];
    int gameID=0;
    Random random = new Random();

    public GameActivity(){
        gameID = random.nextInt(5);
    }

    int pos1 = 0;
    int pos2 = 0;
    boolean isClicked = false;

    Button btnBackToCamera;
    Button btnAnswer;

    TextView txtQuestion;

    String question="";
    String answer="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.royal_blue));

        actionBar.setBackgroundDrawable(colorDrawable);

        gridView  =  (GridView) findViewById(R.id.activity_game_gridview);
        txtQuestion = (TextView) findViewById(R.id.activity_game_question);
        btnAnswer = (Button) findViewById(R.id.activity_game_answer);

        switch (gameID){
            case 0:
                sources = GameUtils.copyArray(doraemons);
                targets = GameUtils.copyArray(doraemons);
                question = "Your friend from the future";
                answer = "Doraemon";
                break;
            case 1:
                sources = GameUtils.copyArray(bigheros);
                targets = GameUtils.copyArray(bigheros);
                question = "Who will always be your protector";
                answer = "Big hero";
                break;
            case 2:
                sources = GameUtils.copyArray(spidermans);
                targets = GameUtils.copyArray(spidermans);
                question = "Hero clings to ceiling and protects you";
                answer = "Spider man";
                break;
            case 3:
                sources = GameUtils.copyArray(messis);
                targets = GameUtils.copyArray(messis);
                question = "Greatest of All Time player";
                answer = "Messi";
                break;
            case 4:
                sources = GameUtils.copyArray(ronaldos);
                targets = GameUtils.copyArray(ronaldos);
                question = "The most scored player in the world";
                answer = "Ronaldo";
                break;
        }
        txtQuestion.setText(question);

        sources = GameUtils.shuffleArray(sources);
        ImageAdapter gridAdapter =(new ImageAdapter(this, sources));
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isClicked == false){
                    pos1 = position;
                    isClicked = true;
                }else{
                    pos2 = position;
                    sources = GameUtils.swapArray(sources, pos1, pos2);
                    ImageAdapter gridAdapter2 =(new ImageAdapter(getApplicationContext(), sources));
                    gridView.setAdapter(gridAdapter2);
                    isClicked = false;
                    if(GameUtils.checkArrayEqual(sources, targets)){
                        btnAnswer.setText(answer);
                        btnAnswer.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Ping Pong!", Toast.LENGTH_SHORT).show();
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
//                                Toast.makeText(getApplicationContext(), "Ping Pong!", Toast.LENGTH_SHORT).show();
                            }
                            public void onFinish() {
                                dismissAlarm();
                            }
                        }.start();
                    }else{
                        Toast.makeText(getApplicationContext(), "Come on", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnBackToCamera = (Button) findViewById(R.id.activity_game_back_to_camera);
        btnBackToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void dismissAlarm(){
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
        finishAffinity();
//        System.exit(0);
    }
}

