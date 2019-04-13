package com.example.a0959600.pan_cartes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static DatabaseHelper ourInstance;
    private SQLiteDatabase database;

    public static DatabaseHelper getInstance(Context c) {
        if(ourInstance==null) {
            ourInstance = new DatabaseHelper(c.getApplicationContext());
        }
        return ourInstance;
    }

    private DatabaseHelper(Context c) {
        super(c,"db",null,1);
        ouvrirBD();
    }

    private void ouvrirBD() {
        //On peut ouvrir en lecture ou en écriture
        database=this.getWritableDatabase();
    }

    private void fermerBD(){
        database.close();
    }

    public void ajouterMeilleurScore(int score){
        ContentValues cv = new ContentValues();
        cv.put("highscore",score);
        database.insert("SCORE",null,cv);
    }

    public Vector<Integer> trouverMeilleurScore(){
        Vector<Integer> resultat = new Vector<>();
        Cursor c = database.query("SCORE",new String[]{"highscore"},null,null,null,null,null);
        while(c.moveToNext()){
            resultat.add(c.getInt(0));
        }
        return resultat;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SCORE( _id INTEGER PRIMARY KEY AUTOINCREMENT, highscore REAL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

