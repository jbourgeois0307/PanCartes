package com.example.a0959600.pan_cartes;

import java.util.Random;
import java.util.Vector;

public class Pile {
    Vector<Carte> pile;

    public Pile(){
        pile  = new Vector<Carte>();
        genererPileCarte();
    }

    public void ajouterCarte(Carte c){
        pile.add(c);
    }

    public void genererPileCarte(){
        for(int i=1;i<Constantes.MAX_VAL+1;++i){
            pile.add(new Carte(i));
        }
    }

    public Carte trouverCarte(int valeur) {
        for(Carte c: pile){
            if(c.getValeur()==valeur){
                return c;
            }
        }
        return null;
    }

    public boolean isPileVide(){
        return pile.isEmpty();
    }

    public Carte sortirCarteAleatoire(){
        Random generator = new Random();
        int rnd = generator.nextInt(pile.size()-1);
        Carte randCarte =  this.pile.get(rnd);
        pile.remove(this.pile.get(rnd));
        return randCarte;
    }

    public int getPileSize(){
        return pile.size();
    }

}
