package com.example.a0959600.pan_cartes;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class Jeu {
    private int pointageCourant;
    private int pointageMaximal;
    private Pile pilePrincipale;
    private Hashtable<String,Suite> htSuites;
    private List<Carte> cartesPresentes;

    public static final Jeu instance = new Jeu();

    private Jeu(){

        pointageCourant = 0;
        pointageMaximal = 0;

        pilePrincipale = new Pile();

        htSuites = new Hashtable<String,Suite>();

        htSuites.put("croissant1",new Suite(Suite.Ordre.CROISSANT));
        htSuites.put("croissant2",new Suite(Suite.Ordre.CROISSANT));
        htSuites.put("decroissant1",new Suite(Suite.Ordre.DECROISSANT));
        htSuites.put("decroissant2",new Suite(Suite.Ordre.DECROISSANT));

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
    public void setPointageCourant(int pointageCourant){
        this.pointageCourant=pointageCourant;
    }

    public void setPointageMaximal(int pointage){
        this.pointageMaximal = pointage;
        sauvegarderPointageMaximal(pointage);
    }

    //MÉTHODES
    public void retirerCartePresente(int index){
        cartesPresentes.set(index,null);
    }

    public int calculerScore(){
        int score = 10000;
        score/=pilePrincipale.getPileSize();
        pointageCourant+=score;
        return pointageCourant;
    }

    public void ajoutCartesPresentes(int index1, int index2){
        cartesPresentes.set(index1,pilePrincipale.sortirCarteAleatoire());
        cartesPresentes.set(index2,pilePrincipale.sortirCarteAleatoire());
    }

    public boolean ajoutASuite(String nomSuite,Carte c){
        return htSuites.get(nomSuite).ajouterCarteASuite(c);
    }


    private void sauvegarderPointageMaximal(int pointage){
        //Requête SQLite
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

        htSuites.put("croissant1",new Suite(Suite.Ordre.CROISSANT));
        htSuites.put("croissant2",new Suite(Suite.Ordre.CROISSANT));
        htSuites.put("decroissant1",new Suite(Suite.Ordre.DECROISSANT));
        htSuites.put("decroissant2",new Suite(Suite.Ordre.DECROISSANT));

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
        // À FAIRE
        boolean isDeplacementValide = false;
        for(Carte c:cartesPresentes){
            if(htSuites.get("croissant1").testAjouterCarteASuite(c)
                    ||htSuites.get("croissant2").testAjouterCarteASuite(c)
                    ||htSuites.get("decroissant1").testAjouterCarteASuite(c)
                    ||htSuites.get("decroissant2").testAjouterCarteASuite(c)){
                isDeplacementValide=true;
            }
        }
        return isDeplacementValide;
    }

}
