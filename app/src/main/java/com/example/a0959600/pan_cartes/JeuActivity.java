package com.example.a0959600.pan_cartes;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class JeuActivity extends AppCompatActivity {
    TextView tvObjCartesRestantes;
    TextView tvObjScore;
    TextView tvObjSuite1
            ,tvObjSuite2
            ,tvObjSuite3
            ,tvObjSuite4;
    TableRow rangee1, rangee2;
    Jeu jeu;
    DatabaseHelper dbh;
    Ecouteur ec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        dbh = DatabaseHelper.getInstance(this);
        jeu = Jeu.getInstance();

        jeu.redemarrerPartie();

        tvObjCartesRestantes=findViewById(R.id.tvCartesRestantes);
        tvObjScore=findViewById(R.id.tvScore);

        tvObjSuite1 = findViewById(R.id.tvSuite1);
        tvObjSuite2 = findViewById(R.id.tvSuite2);
        tvObjSuite3 = findViewById(R.id.tvSuite3);
        tvObjSuite4 = findViewById(R.id.tvSuite4);

        rangee1 = findViewById(R.id.zoneBasRangee1);
        rangee2 = findViewById(R.id.zoneBasRangee2);

        this.placerCartesInitiales();
        this.setTextViewSuites();
        this.mettreAJourInterface();

        ec = new Ecouteur();

        for(int i = 0; i<4; ++i){
            TextView tv = (TextView)((ConstraintLayout)rangee1.getVirtualChildAt(i)).getChildAt(0);
            tv.setOnDragListener(ec);
            tv.setOnTouchListener(ec);
        }
        for(int i = 0; i<4; ++i){
            TextView tv = (TextView)((ConstraintLayout)rangee2.getVirtualChildAt(i)).getChildAt(0);
            tv.setOnDragListener(ec);
            tv.setOnTouchListener(ec);
        }


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
        for(int i = 0; i<4; ++i){
            ConstraintLayout cl = (ConstraintLayout)rangee1.getChildAt(i);
            creerTextViewCarte(jeu.getCartesPresentes().get(i).getValeur(),cl);
        }
        for(int i = 0; i<4; ++i){
            ConstraintLayout cl = (ConstraintLayout)rangee2.getChildAt(i);
            creerTextViewCarte(jeu.getCartesPresentes().get(i).getValeur(),cl);
        }

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

    public class Ecouteur implements View.OnTouchListener, View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch(event.getAction()){
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    System.out.println("ici!");
                    View draggedTV = (View) event.getLocalState();
                    LinearLayout parent = (LinearLayout) draggedTV.getParent();
                    LinearLayout container = (LinearLayout)v;

                    /*if(container==banqueFem){
                        if(dbh.verifierGenre(((TextView)draggedTV).getText().toString(),"feminin")){
                            parent.removeView(draggedTV);
                            container.addView(draggedTV);
                        }
                    }
                    if(container==banqueMasc){
                        if(dbh.verifierGenre(((TextView)draggedTV).getText().toString().toString(),"masculin")){
                            parent.removeView(draggedTV);
                            container.addView(draggedTV);
                        }
                    }
                    else{
                        ((LinearLayout)draggedTV.getParent()).addView(draggedTV);
                    }
*/
                    draggedTV.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null,shadowBuilder,v,0);
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    protected void onStop(){
        super.onStop();
        dbh.fermerBD();
    }
}
