package com.example.a0959600.pan_cartes;

import java.util.Vector;

public class Suite {

    private Vector<Carte> pile;

    public enum Ordre{
        CROISSANT,
        DECROISSANT
    }
    private Ordre ordre;
    private int initialVal;

    public Suite(Ordre ordre){
        this.pile = new Vector<Carte>();
        this.ordre = ordre;
        if(ordre==Ordre.CROISSANT)
            initialVal=Constantes.MIN_VAL-1;
        else{
            initialVal=Constantes.MAX_VAL+1;
        }
    }

    public int getInitialVal() { return initialVal; }

    public boolean testAjouterCarteASuite(Carte c) {
        boolean ajoutValide = false;
        int valCarteAjoutee = c.getValeur();
        int valDerniereCartePile = pile.lastElement().getValeur();
        if(!pile.isEmpty()){
            switch (ordre) {
                case CROISSANT:
                    if (pile.isEmpty() && valCarteAjoutee > initialVal) {
                        ajoutValide = true;
                    }
                    if (valCarteAjoutee > pile.lastElement().getValeur()) {
                        ajoutValide = true;
                    }
                    //RÈGLE DE 10
                    if (valCarteAjoutee == (valDerniereCartePile - 10))
                        ajoutValide = true;
                    break;
                case DECROISSANT:
                    if (pile.isEmpty() && valCarteAjoutee < initialVal) {
                        ajoutValide = true;
                    }
                    if (valCarteAjoutee < pile.lastElement().getValeur()) {
                        ajoutValide = true;
                    }
                    //RÈGLE DE 10
                    if (valCarteAjoutee == (valDerniereCartePile + 10))
                        ajoutValide = true;
                    break;
            }
        }
        else{
            return pile.add(c);
        }
        return ajoutValide;
    }

    public boolean ajouterCarteASuite(Carte c) {
        boolean ajoutValide = false;
        int valCarteAjoutee = c.getValeur();
        if(!pile.isEmpty()){
            int valDerniereCartePile = pile.lastElement().getValeur();
            switch (ordre) {
                case CROISSANT:
                    if (pile.isEmpty() && valCarteAjoutee > initialVal) {
                        ajoutValide = true;
                    }
                    if (valCarteAjoutee > pile.lastElement().getValeur()) {
                        ajoutValide = true;
                    }
                    //RÈGLE DE 10
                    if (valCarteAjoutee == (valDerniereCartePile - 10))
                        ajoutValide = true;
                    break;
                case DECROISSANT:
                    if (pile.isEmpty() && valCarteAjoutee < initialVal) {
                        ajoutValide = true;
                    }
                    if (valCarteAjoutee < pile.lastElement().getValeur()) {
                        ajoutValide = true;
                    }
                    //RÈGLE DE 10
                    if (valCarteAjoutee == (valDerniereCartePile + 10))
                        ajoutValide = true;
                    break;
            }
            if(ajoutValide){
                return pile.add(c);
            }
        }
        else{
            return pile.add(c);
        }
        return ajoutValide;
    }

}
