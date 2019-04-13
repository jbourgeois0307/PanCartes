package com.example.a0959600.pan_cartes;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

public class JeuActivity extends AppCompatActivity {
    TextView tvObjCartesRestantes;
    TextView tvObjScore;
    TextView tvObjSuite1
            ,tvObjSuite2
            ,tvObjSuite3
            ,tvObjSuite4;
    ConstraintLayout zoneCarte0
            ,zoneCarte1
            ,zoneCarte2
            ,zoneCarte3
            ,zoneCarte4
            ,zoneCarte5
            ,zoneCarte6
            ,zoneCarte7;
    Jeu jeu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        jeu = Jeu.getInstance();

        jeu.redemarrerPartie();

        tvObjCartesRestantes=findViewById(R.id.tvCartesRestantes);
        tvObjScore=findViewById(R.id.tvScore);

        tvObjSuite1 = findViewById(R.id.tvSuite1);
        tvObjSuite2 = findViewById(R.id.tvSuite2);
        tvObjSuite3 = findViewById(R.id.tvSuite3);
        tvObjSuite4 = findViewById(R.id.tvSuite4);

        zoneCarte0 = findViewById(R.id.zoneCartePresente0);
        zoneCarte1 = findViewById(R.id.zoneCartePresente1);
        zoneCarte2 = findViewById(R.id.zoneCartePresente2);
        zoneCarte3 = findViewById(R.id.zoneCartePresente3);
        zoneCarte4 = findViewById(R.id.zoneCartePresente4);
        zoneCarte5 = findViewById(R.id.zoneCartePresente5);
        zoneCarte6 = findViewById(R.id.zoneCartePresente6);
        zoneCarte7 = findViewById(R.id.zoneCartePresente7);


        this.placerCartesInitiales();
        this.setTextViewSuites();
        this.mettreAJourInterface();


    }

    public void mettreAJourInterface(){
        /*if(jeu.partieTerminee()){
            //INSERT EN BD
            jeu.setPointageMaximal(jeu.getPointageCourant());
            finish();
        }*/
        //ENTÊTE
        tvObjCartesRestantes.setText(
                String.valueOf(jeu.getCartesRestantesPilePrincipale())
        );

        tvObjScore.setText(
                String.valueOf(jeu.getPointageCourant())
        );

    }

    public void placerCartesInitiales(){
        for(int i =0; i<8; i+=2){
            jeu.ajoutCartesPresentes(i,i+1);
        }

        creerTextViewCarte(jeu.getCartesPresentes().get(0).getValeur(),zoneCarte0);
        creerTextViewCarte(jeu.getCartesPresentes().get(1).getValeur(),zoneCarte1);
        creerTextViewCarte(jeu.getCartesPresentes().get(2).getValeur(),zoneCarte2);
        creerTextViewCarte(jeu.getCartesPresentes().get(3).getValeur(),zoneCarte3);
        creerTextViewCarte(jeu.getCartesPresentes().get(4).getValeur(),zoneCarte4);
        creerTextViewCarte(jeu.getCartesPresentes().get(5).getValeur(),zoneCarte5);
        creerTextViewCarte(jeu.getCartesPresentes().get(6).getValeur(),zoneCarte6);
        creerTextViewCarte(jeu.getCartesPresentes().get(7).getValeur(),zoneCarte7);

    }

    public void setTextViewSuites(){
        tvObjSuite1.setText(String.valueOf(jeu.getHtSuites().get("croissant1").getInitialVal()));
        tvObjSuite2.setText(String.valueOf(jeu.getHtSuites().get("croissant2").getInitialVal()));
        tvObjSuite3.setText(String.valueOf(jeu.getHtSuites().get("decroissant1").getInitialVal()));
        tvObjSuite4.setText(String.valueOf(jeu.getHtSuites().get("decroissant2").getInitialVal()));
    }

    //Créer une carte en TextView et la position là demandée
    public void creerTextViewCarte(int val, ConstraintLayout cl){
        TextView tv = new TextView(this);
        tv.setText(String.valueOf(val));
        float fWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 59, getResources().getDisplayMetrics());
        float fHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 86, getResources().getDisplayMetrics());
        tv.setWidth((int)fWidth);
        tv.setId(val);
        tv.setHeight((int)fHeight);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setTextSize(25);
        tv.setBackground(getResources().getDrawable(R.drawable.custom_card));
        cl.addView(tv);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cs.centerHorizontally(tv.getId(),cl.getId());
        cs.centerVertically(tv.getId(),cl.getId());
        cs.applyTo(cl);
    }
}
