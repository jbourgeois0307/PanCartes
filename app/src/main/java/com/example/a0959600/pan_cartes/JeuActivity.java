package com.example.a0959600.pan_cartes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
    Chronometer chrono;
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

        tvObjSuite1 = findViewById(R.id.zoneSuite1);
        tvObjSuite2 = findViewById(R.id.zoneSuite2);
        tvObjSuite3 = findViewById(R.id.zoneSuite3);
        tvObjSuite4 = findViewById(R.id.zoneSuite4);

        rangee1 = findViewById(R.id.zoneBasRangee1);
        rangee2 = findViewById(R.id.zoneBasRangee2);

        tvObjCartesRestantes=findViewById(R.id.tvCartesRestantes);

        tvObjScore=findViewById(R.id.tvScore);

        ec = new Ecouteur();

        tvObjSuite1.setOnDragListener(ec);
        tvObjSuite2.setOnDragListener(ec);
        tvObjSuite3.setOnDragListener(ec);
        tvObjSuite4.setOnDragListener(ec);



        long tempsCourant = 0;
        chrono = findViewById(R.id.chrono);
        try{
            tempsCourant=recupererTemps();
        }
        catch (Exception e){
            if ((savedInstanceState != null) && (savedInstanceState.getSerializable("temps") != null)) {
                tempsCourant = (long) savedInstanceState.getSerializable("temps");
                chrono.setBase(SystemClock.elapsedRealtime() - tempsCourant);
            }
        }
        Vector<Integer> v= dbh.trouverMeilleurScore();
        if(!v.isEmpty()){
            jeu.setPointageMaximal(v.get(0));
        }
        else{
            jeu.setPointageMaximal(0);
        }
        tvObjCartesRestantes.setText(String.valueOf(jeu.getCartesRestantesPilePrincipale()));

        chrono.start();

        this.placerCartesInitiales();
        this.setTextViewSuites();

    }

    @Override
    public void onBackPressed(){
        this.enregistrerScore();
        dbh.reOuvrirBD();
        finish();
    }

    protected void onSaveInstanceState(Bundle infos) {
        super.onSaveInstanceState(infos);
        infos.putSerializable("temps", chrono.getBase());
    }

    public void mettreAJourInterface(){
        //Test de condition de fin de partie
        if(jeu.partieTerminee()){
            this.enregistrerScore();
            finish();
        }

        jeu.calculerScore();

        if(vectCarteRetiree.size()>=2){
            boucle:
            for(Carte c: vectCarteRetiree){
                int valCarteSortie= jeu.ajoutCartesPresentes();
                boolean carteCreee = false;
                for(int i=0; i<rangee1.getVirtualChildCount();i++){
                    if(((ConstraintLayout)rangee1.getVirtualChildAt(i)).getChildAt(0)==null){
                        creerTextViewCarte(valCarteSortie,(ConstraintLayout)rangee1.getVirtualChildAt(i));
                        carteCreee=true;
                        continue boucle;
                    }
                }
                if(!carteCreee) {
                    for (int i = 0; i < rangee2.getVirtualChildCount(); i++) {
                        if (((ConstraintLayout) rangee2.getVirtualChildAt(i)).getChildAt(0) == null) {
                            creerTextViewCarte(valCarteSortie, (ConstraintLayout) rangee2.getVirtualChildAt(i));
                            continue boucle;
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
        tv.setMinHeight((int)fHeight);
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

                        if(jeu.ajoutASuite(Constantes.nomsSuite[0],carteDeplacee)){
                            dropValide = true;
                        }
                    }
                    if(container==findViewById(R.id.zoneSuite2)){
                        if(jeu.ajoutASuite(Constantes.nomsSuite[1],carteDeplacee)){
                            dropValide = true;
                        }
                    }
                    if(container==findViewById(R.id.zoneSuite3)){
                        if(jeu.ajoutASuite(Constantes.nomsSuite[2],carteDeplacee)){
                            dropValide = true;
                        }
                    }
                    if(container==findViewById(R.id.zoneSuite4)){
                        if(jeu.ajoutASuite(Constantes.nomsSuite[3],carteDeplacee)){
                            dropValide=true;
                        }
                    }
                    if(dropValide){
                        vectCarteRetiree.add(carteDeplacee);
                        jeu.retirerCartePresente(carteDeplacee);

                        parent.removeView(view);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        ConstraintSet cs = new ConstraintSet();
                        cs.clone(container);
                        cs.centerHorizontally(view.getId(),container.getId());
                        cs.centerVertically(view.getId(),container.getId());
                        cs.applyTo(container);

                        //Elles seront immuables
                        ((TextView) view).setOnDragListener(null);
                        ((TextView) view).setOnTouchListener(null);

                        //Donne un léger angle
                        float randAngle = (float)(Math.random()*((30-(-30)+1))+(-30));
                        ((TextView) view).setRotation(randAngle);
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

    public void enregistrerScore(){
        Intent i = new Intent();

        if(jeu.comparerScore()){

            jeu.setPointageMaximal(jeu.getPointageCourant());
            dbh.ajouterMeilleurScore(jeu.getPointageCourant());
            i.putExtra("nouveauScore",jeu.getPointageCourant());

        }
        else{
            i.putExtra("nouveauScore",0);
        }
        setResult(RESULT_OK,i);
    }

    protected void onStop(){
        dbh.reOuvrirBD();
        super.onStop();
        sauvegarderPartie(chrono.getBase());
    }

    public long recupererTemps() throws Exception{
        long t = 0;
        FileInputStream fis = openFileInput("fichier.ser"); // flux de communication
        ObjectInputStream ois = new ObjectInputStream(fis); // flux de traitement
        t = (long)ois.readObject();
        ois.close();

        return t;
    }

    public void sauvegarderPartie(Long temps){
        try
        {
            FileOutputStream fos = openFileOutput("fichier.ser", Context.MODE_PRIVATE); //f lux de communication, MODE_APPEND contraire MODE_PRIVATE
            ObjectOutputStream oos = new ObjectOutputStream(fos); // flux de traitement
            oos.writeObject(temps);
            oos.flush();
            oos.close();
        }
        catch(Exception ex)
        {
            Log.v("erreur : ",ex.getMessage());
            ex.printStackTrace();
        }
    }

}
