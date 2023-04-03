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
    int blackwidows[] = {R.drawable.blackwidow1, R.drawable.blackwidow2, R.drawable.blackwidow3, R.drawable.blackwidow4, R.drawable.blackwidow5, R.drawable.blackwidow6, R.drawable.blackwidow7,
        R.drawable.blackwidow8, R.drawable.blackwidow9, R.drawable.blackwidow10, R.drawable.blackwidow11, R.drawable.blackwidow12};
    int wandas[] = {R.drawable.wanda1, R.drawable.wanda2, R.drawable.wanda3, R.drawable.wanda4, R.drawable.wanda5, R.drawable.wanda6, R.drawable.wanda7, R.drawable.wanda8,
        R.drawable.wanda9, R.drawable.wanda10, R.drawable.wanda11, R.drawable.wanda12};
    int captains[] = {R.drawable.captain1, R.drawable.captain2, R.drawable.captain3, R.drawable.captain4, R.drawable.captain5, R.drawable.captain6, R.drawable.captain7,
        R.drawable.captain8, R.drawable.captain9, R.drawable.captain10, R.drawable.captain11, R.drawable.captain12};
    int irons[] = {R.drawable.iron1, R.drawable.iron2, R.drawable.iron3, R.drawable.iron4, R.drawable.iron5, R.drawable.iron6, R.drawable.iron7, R.drawable.iron8,
        R.drawable.iron9, R.drawable.iron10, R.drawable.iron11, R.drawable.iron12};
    int hulks[] = {R.drawable.hulk1, R.drawable.hulk2, R.drawable.hulk3, R.drawable.hulk4, R.drawable.hulk5, R.drawable.hulk6, R.drawable.hulk7, R.drawable.hulk8,
        R.drawable.hulk9, R.drawable.hulk10, R.drawable.hulk11, R.drawable.hulk12};
    int thors[] = {R.drawable.thor1, R.drawable.thor2, R.drawable.thor3, R.drawable.thor4, R.drawable.thor5, R.drawable.thor6, R.drawable.thor7, R.drawable.thor8,
        R.drawable.thor9, R.drawable.thor10, R.drawable.thor11, R.drawable.thor12};
    int panthers[] = {R.drawable.panther1, R.drawable.panther2, R.drawable.panther3, R.drawable.panther4, R.drawable.panther5, R.drawable.panther6, R.drawable.panther7,
        R.drawable.panther8, R.drawable.panther9, R.drawable.panther10, R.drawable.panther11, R.drawable.panther12};
    int thanoss[] = {R.drawable.thanos1, R.drawable.thanos2, R.drawable.thanos3, R.drawable.thanos4, R.drawable.thanos5, R.drawable.thanos6, R.drawable.thanos7,
        R.drawable.thanos8, R.drawable.thanos9, R.drawable.thanos10, R.drawable.thanos11, R.drawable.thanos12};
    int supergirls[] = {R.drawable.supergirl1, R.drawable.supergirl2, R.drawable.supergirl3, R.drawable.supergirl4, R.drawable.supergirl5, R.drawable.supergirl6, R.drawable.supergirl7,
        R.drawable.supergirl8, R.drawable.supergirl9, R.drawable.supergirl10, R.drawable.supergirl11, R.drawable.supergirl12};
    int supermans[] = {R.drawable.superman1, R.drawable.superman2, R.drawable.superman3, R.drawable.superman4, R.drawable.superman5, R.drawable.superman6, R.drawable.superman7,
        R.drawable.superman8, R.drawable.superman9, R.drawable.superman10, R.drawable.superman11, R.drawable.superman12};

    int sources[];
    int targets[];
    int gameID=0;
    Random random = new Random();

    public GameActivity(){
        gameID = random.nextInt(15);
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
            case 5:
                sources = GameUtils.copyArray(blackwidows);
                targets = GameUtils.copyArray(blackwidows);
                question = "The Avengers: A normal girl with extraordinary abilities";
                answer = "Black Widow";
                break;
            case 6:
                sources = GameUtils.copyArray(wandas);
                targets = GameUtils.copyArray(wandas);
                question = "A beautiful woman with the superpower of control";
                answer = "Wanda Maximoff";
                break;
            case 7:
                sources = GameUtils.copyArray(captains);
                targets = GameUtils.copyArray(captains);
                question = "Leader of the Avengers";
                answer = "Captain America";
                break;
            case 8:
                sources = GameUtils.copyArray(irons);
                targets = GameUtils.copyArray(irons);
                question = "Genius, Billionaire, Playboy, Philanthropist";
                answer = "Iron man";
                break;
            case 9:
                sources = GameUtils.copyArray(hulks);
                targets = GameUtils.copyArray(hulks);
                question = "The green giant of the Avengers";
                answer = "Hulk";
                break;
            case 10:
                sources = GameUtils.copyArray(thors);
                targets = GameUtils.copyArray(thors);
                question = "God who controls the thunder";
                answer = "Thor";
                break;
            case 11:
                sources = GameUtils.copyArray(panthers);
                targets = GameUtils.copyArray(panthers);
                question = "The King of Wakanda";
                answer = "Black Panther";
                break;
            case 12:
                sources = GameUtils.copyArray(thanoss);
                targets = GameUtils.copyArray(thanoss);
                question = "With a snap of her fingers, half the world's population disappeared";
                answer = "Thanos";
                break;
            case 13:
                sources = GameUtils.copyArray(supergirls);
                targets = GameUtils.copyArray(supergirls);
                question = "The strongest woman in the world";
                answer = "Supergirl";
                break;
            case 14:
                sources = GameUtils.copyArray(supermans);
                targets = GameUtils.copyArray(supermans);
                question = "The strongest man in the world";
                answer = "Superman";
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

