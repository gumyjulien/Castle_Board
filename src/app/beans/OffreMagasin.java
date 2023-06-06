/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class OffreMagasin implements Serializable {

    public OffreMagasin(TypeOffre typeOffre, ArrayList<Recompense> recompenses, int prix, TirageCoffre.Ressource ressPrix) {
        this.typeOffre = typeOffre;
        this.recompenses = recompenses;
        this.prix = prix;
        this.ressPrix = ressPrix;
    }

    public OffreMagasin(Region region, ArrayList<Recompense> recompenses, int prix, TirageCoffre.Ressource ressPrix) {
        this(TypeOffre.OFFRE_REGION, recompenses, prix, ressPrix);
        this.region = region;
    }

    public TypeOffre getTypeOffre() {
        return typeOffre;
    }

    public void setTypeOffre(TypeOffre typeOffre) {
        this.typeOffre = typeOffre;
    }

    public ArrayList<Recompense> getRecompenses() {
        return recompenses;
    }

    public void setRecompenses(ArrayList<Recompense> recompenses) {
        this.recompenses = recompenses;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public TirageCoffre.Ressource getRessPrix() {
        return ressPrix;
    }

    public void setRessPrix(TirageCoffre.Ressource ressPrix) {
        this.ressPrix = ressPrix;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getNom() {
        String res = nom;
        if(nom == null) {
            res = typeOffre.toString();
        }
        return res;
    }
    
    private TypeOffre typeOffre;
    private ArrayList<Recompense> recompenses;
    private int prix;
    private TirageCoffre.Ressource ressPrix;
    private Region region;
    private String nom;
    
    public enum TypeOffre {
        OFFRE_REGION,
        CARTE_COSTUME,
        EN_VRAC;
        
        @Override
        public String toString() {
            String res = "";
            switch(this) {
                case CARTE_COSTUME:
                    res = "Carte & Costume";
                    break;
                case OFFRE_REGION:
                    res = "Offre de la région ";
                    break;
            }
            return res;
        }
    }
}
