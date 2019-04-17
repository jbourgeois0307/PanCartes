package com.example.a0959600.pan_cartes;

import android.content.Intent;
import android.provider.ContactsContract;
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
    DatabaseHelper dbh;
    int meilleurScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbh = DatabaseHelper.getInstance(this);

        btnJouer = findViewById(R.id.btnJouer);
        tvScore = findViewById(R.id.tvHighscore);

        vectMeilleurScore = dbh.trouverMeilleurScore();
        if(!vectMeilleurScore.isEmpty()){
            tvScore.setText(String.valueOf(vectMeilleurScore.get(0)));
        }
        else{
            tvScore.setText(String.valueOf(0));
        }
        ec = new Ecouteur();

        btnJouer.setOnClickListener(ec);
    }
    public class Ecouteur implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v==btnJouer){
                Intent i = new Intent(MainActivity.this,JeuActivity.class);
                startActivityForResult(i,0);
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int nouveauScore = data.getIntExtra("nouveauScore",0);
            if(nouveauScore!=0){
                tvScore.setText(String.valueOf(nouveauScore));
            }
            dbh.fermerBD();
            dbh = DatabaseHelper.getInstance(getApplicationContext());
        }
    }
}
