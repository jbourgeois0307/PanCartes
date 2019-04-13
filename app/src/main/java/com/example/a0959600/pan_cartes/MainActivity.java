package com.example.a0959600.pan_cartes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    Ecouteur ec;
    Button btnJouer;
    TextView tvScore;
    Vector<Integer> vectMeilleurScore;
    int meilleurScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJouer = findViewById(R.id.btnJouer);
        tvScore = findViewById(R.id.tvHighscore);

        vectMeilleurScore = DatabaseHelper.getInstance(MainActivity.this).trouverMeilleurScore();
        for(Integer i: vectMeilleurScore){
            meilleurScore = i;
        }
        tvScore.setText(String.valueOf(meilleurScore));


        ec = new Ecouteur();

        btnJouer.setOnClickListener(ec);
    }
    public class Ecouteur implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v==btnJouer){
                Intent i = new Intent(MainActivity.this,JeuActivity.class);
                startActivity(i);
            }
        }
    }
}
