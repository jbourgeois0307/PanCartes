package com.example.a0959600.pan_cartes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class Jeu {
    private int pointageCourant;
    private int pointageMaximal;
    private Pile pilePrincipale;
    private Hashtable<String,Suite> htSuites;
    private List<Carte> cartesPresentes;

    DatabaseHelper dbh;

    public static final Jeu instance = new Jeu();

    private Jeu(){

        pointageCourant = 0;
        pointageMaximal = 0;

        pilePrincipale = new Pile();


        htSuites = new Hashtable<String,Suite>();
        for(String s: Constantes.nomsSuite){
            if(s.equals(Constantes.nomsSuite[0])||s.equals(Constantes.nomsSuite[1])){
                htSuites.put(s,new Suite(Suite.Ordre.CROISSANT));
            }
            else{
                htSuites.put(s,new Suite(Suite.Ordre.DECROISSANT));
            }
        }

        //Liste de taille fixe de 8
        cartesPresentes = Arrays.asList(new Carte[Constantes.MAX_CARTES_PRESENTES]);

        peuplerCartesPresentes();

    }

    public Hashtable<String, Suite> getHtSuites() { return htSuites;
    }

    public static Jeu getInstance(){
        return instance;
    }



    //MÉTHODES D'ACCÈS
    public int getPointageCourant(){
        return this.pointageCourant;
    }

    public List<Carte> getCartesPresentes(){ return this.cartesPresentes; }

    public int getCartesRestantesPilePrincipale(){
        return pilePrincipale.getPileSize();
    }

    //MÉTHODES DE MUTATION
    public void setPointageMaximal(int pointage){
        this.pointageMaximal = pointage;
    }

    //MÉTHODES

    public int calculerScore(long diffTempsEntreMouvement, String nomSuite, Carte carteDeplacee){
        int score = 100000;

        //Selon le temps
        diffTempsEntreMouvement/=1000;
        diffTempsEntreMouvement = diffTempsEntreMouvement == 0 ? 1 : diffTempsEntreMouvement;
        score/=diffTempsEntreMouvement;

        //Selon le nombre de cartes restantes
        score/=pilePrincipale.getPileSize();


        //Selon la différence avec la dernière carte mise sur la suite
        Suite suiteInteragie = htSuites.get(nomSuite);
        int valCarteDeplacee = carteDeplacee.getValeur();
        int valDernierCarteSuite=0;

        if(suiteInteragie.getPileSuite().size()==1){
            valDernierCarteSuite=suiteInteragie.getInitialVal();
        }
        else{
            valDernierCarteSuite = suiteInteragie.getPileSuite()
                    .get(suiteInteragie.getPileSuite().size()-2).getValeur();
        }

        if(suiteInteragie.getOrdre()== Suite.Ordre.CROISSANT){
            int diff = valCarteDeplacee-valDernierCarteSuite;
            diff = diff <=0 ? diff*=-1 : diff;
            score/=(diff);
        }
        else{
            int diff =valDernierCarteSuite-valCarteDeplacee;
            diff = diff <=0 ? diff*=-1 : diff;
            score/=(diff);
        }

        return pointageCourant+=score;
    }

    public int ajoutCartesPresentes(){
        Carte carteAleatoireAjoutee = null;
        for(int i=0; i<cartesPresentes.size();i++){
            if(cartesPresentes.get(i)==null){
                Carte carteSortie = pilePrincipale.sortirCarteAleatoire();
                cartesPresentes.set(i,carteSortie);
                return carteSortie.getValeur();
            }
        }
        return -1;
    }

    public void retirerCartePresente(Carte c){
        for(int i=0; i<cartesPresentes.size();i++){
            if(cartesPresentes.get(i)!=null){
                if(cartesPresentes.get(i).getValeur()==c.getValeur()){
                    cartesPresentes.set(i,null);
                }
            }
        }
    }

    public boolean ajoutASuite(String nomSuite,Carte c){
        return htSuites.get(nomSuite).ajouterCarteASuite(c);
    }


    private void peuplerCartesPresentes(){
        for(int i=0; i<Constantes.MAX_CARTES_PRESENTES;++i){
            cartesPresentes.set(i,pilePrincipale.sortirCarteAleatoire());
        }
    }

    public void redemarrerPartie(){
        pointageCourant = 0;
        pointageMaximal = 0;
        pilePrincipale = new Pile();

        for(String s: Constantes.nomsSuite){
            if(s.equals(Constantes.nomsSuite[0])||s.equals(Constantes.nomsSuite[1])){
                htSuites.put(s,new Suite(Suite.Ordre.CROISSANT));
            }
            else{
                htSuites.put(s,new Suite(Suite.Ordre.DECROISSANT));
            }
        }

        //Liste de taille fixe de 8
        cartesPresentes = Arrays.asList(new Carte[Constantes.MAX_CARTES_PRESENTES]);

        peuplerCartesPresentes();
    }

    public boolean partieTerminee(){
        boolean isPartieTerminee = false;
        if(pilePrincipale.isPileVide()){
            isPartieTerminee = true;
        }
        if(!deplacementValide()){
            isPartieTerminee = true;
        }
        return isPartieTerminee;
    }

    public boolean deplacementValide(){
        boolean isDeplacementValide = false;
        for(Carte c:cartesPresentes){
            if(c!=null){
                for(int i=0; i<4; ++i){
                    if(htSuites.get(Constantes.nomsSuite[i]).testAjouterCarteASuite(c)){
                        isDeplacementValide=true;
                    }
                }
            }
        }
        return isDeplacementValide;
    }

    public boolean comparerScore(){
        if(pointageCourant>pointageMaximal){
            return true;
        }
        return false;
    }

}
