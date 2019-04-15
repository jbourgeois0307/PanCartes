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
import java.util.Vector;

public class JeuActivity extends AppCompatActivity {
    TextView tvObjCartesRestantes;
    TextView tvObjScore;
    Vector<Carte> vectCarteRetiree = new Vector<Carte>();
    ConstraintLayout tvObjSuite1
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

        tvObjSuite1 = findViewById(R.id.zoneSuite1);
        tvObjSuite2 = findViewById(R.id.zoneSuite2);
        tvObjSuite3 = findViewById(R.id.zoneSuite3);
        tvObjSuite4 = findViewById(R.id.zoneSuite4);

        rangee1 = findViewById(R.id.zoneBasRangee1);
        rangee2 = findViewById(R.id.zoneBasRangee2);

        ec = new Ecouteur();

        this.placerCartesInitiales();
        this.setTextViewSuites();
        this.mettreAJourInterface();

        tvObjSuite1.setOnDragListener(ec);
        tvObjSuite2.setOnDragListener(ec);
        tvObjSuite3.setOnDragListener(ec);
        tvObjSuite4.setOnDragListener(ec);


    }

    public void mettreAJourInterface(){
        /*if(jeu.partieTerminee()){
            //INSERT EN BD
            jeu.setPointageMaximal(jeu.getPointageCourant());
            finish();
        }*/
        //ENTÊTE
        jeu.calculerScore();

        if(vectCarteRetiree.size()>=2){
            for(Carte c: vectCarteRetiree){
                int valCarteSortie= jeu.ajoutCartesPresentes();

                boolean aucunEspaceTrouve=true;

                for(int i=0; i<rangee1.getVirtualChildCount();i++){
                    System.out.println(rangee1.getVirtualChildAt(i));
                    if(((ConstraintLayout)rangee1.getVirtualChildAt(i)).getChildAt(0)==null){
                        aucunEspaceTrouve=false;
                        creerTextViewCarte(valCarteSortie,(ConstraintLayout)rangee1.getVirtualChildAt(i));
                    }
                }

                if(aucunEspaceTrouve){
                    for(int i=0; i<rangee2.getVirtualChildCount();i++){
                        if(((ConstraintLayout)rangee2.getVirtualChildAt(i)).getChildAt(0)==null){
                            creerTextViewCarte(valCarteSortie,(ConstraintLayout)rangee2.getVirtualChildAt(i));
                        }
                    }
                }
            }
            vectCarteRetiree.clear();
        }
        tvObjCartesRestantes.setText(
                String.valueOf(jeu.getCartesRestantesPilePrincipale())
        );

        tvObjScore.setText(
                String.valueOf(jeu.getPointageCourant())
        );

        if(jeu.comparerScore()){
            dbh.ajouterMeilleurScore(jeu.getPointageCourant());
            jeu.setPointageMaximal(jeu.getPointageCourant());
        }

    }

    public void placerCartesInitiales(){
        for(int i = 0; i<4; ++i){
            ConstraintLayout cl = (ConstraintLayout)rangee1.getChildAt(i);
            creerTextViewCarte(jeu.getCartesPresentes().get(i).getValeur(),cl);
        }
        for(int i = 0; i<4; ++i){
            ConstraintLayout cl = (ConstraintLayout)rangee2.getChildAt(i);
            creerTextViewCarte(jeu.getCartesPresentes().get(i+4).getValeur(),cl);
        }

    }

    public void setTextViewSuites(){
        ((TextView)tvObjSuite1.getChildAt(0)).setText(String.valueOf(jeu.getHtSuites().get("croissant1").getInitialVal()));
        ((TextView)tvObjSuite2.getChildAt(0)).setText(String.valueOf(jeu.getHtSuites().get("croissant2").getInitialVal()));
        ((TextView)tvObjSuite3.getChildAt(0)).setText(String.valueOf(jeu.getHtSuites().get("decroissant1").getInitialVal()));
        ((TextView)tvObjSuite4.getChildAt(0)).setText(String.valueOf(jeu.getHtSuites().get("decroissant2").getInitialVal()));
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
        tv.setOnDragListener(ec);
        tv.setOnTouchListener(ec);
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
            final View view = (View) event.getLocalState();
            Carte carteDeplacee = new Carte(Integer.valueOf(((TextView) view).getText().toString()));
            switch(event.getAction()){
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    ConstraintLayout parent = (ConstraintLayout) view.getParent();
                    ConstraintLayout container=null;
                    boolean dropValide = false;
                    if(v instanceof TextView){
                        container = (ConstraintLayout)v.getParent();
                    }
                    else{
                        container = (ConstraintLayout)v;
                    }

                    if(container==findViewById(R.id.zoneSuite1)){

                        if(jeu.ajoutASuite("croissant1",carteDeplacee)){
                            dropValide = true;

                        }
                    }
                    if(container==findViewById(R.id.zoneSuite2)){
                        if(jeu.ajoutASuite("croissant2",carteDeplacee)){
                            dropValide = true;
                        }
                    }
                    if(container==findViewById(R.id.zoneSuite3)){
                        if(jeu.ajoutASuite("decroissant1",carteDeplacee)){
                            dropValide = true;
                        }
                    }
                    if(container==findViewById(R.id.zoneSuite4)){
                        if(jeu.ajoutASuite("decroissant2",carteDeplacee)){
                            dropValide=true;
                        }
                    }
                    if(dropValide){
                        vectCarteRetiree.add(carteDeplacee);
                        jeu.retirerCartePresente(carteDeplacee);

                        parent.removeView(view);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        //((TextView)view).setGravity(Gravity.CENTER);
                        ConstraintSet cs = new ConstraintSet();
                        cs.clone(container);
                        cs.centerHorizontally(view.getId(),container.getId());
                        cs.centerVertically(view.getId(),container.getId());
                        cs.applyTo(container);
                    }
                    else{
                        view.setVisibility(View.VISIBLE);
                    }
                    mettreAJourInterface();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setVisibility(View.VISIBLE);
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
