/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.io.Serializable;

/**
 *
 * @author GumyJ01
 */
public class Recompense implements Serializable {
    
    public enum ObjRec {
        PIECES,
        RUBIS,
        POINT_DE_MAITRISE,
        CARTE,
        COSTUME,
        SACOCHE,
        COFFRET,
        GRAND_COFFRE,
        COFFRE_ROYAL,
        CHARRETTE_A_BUTIN;
        
        @Override        
        public String toString() {
            String res = "";
            String[] split = name().split("_");
            for (String string : split) {
                res += string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
            }
            return res;
        }
        
        public String getNom(boolean determinant) {
            String res = (determinant ? "Ce" : "");
            switch(this) {
                case SACOCHE:
                    res += (determinant ? "tte " : "") + "Sacoche";
                    break;
                case COFFRET:
                    res += (determinant ? " " : "") + "Coffret";
                    break;
                case GRAND_COFFRE:
                    res += (determinant ? " " : "") + "Grand Coffre";
                    break;
                case COFFRE_ROYAL:
                    res += (determinant ? " " : "") + "Coffre Royal";
                    break;
                case CHARRETTE_A_BUTIN:
                    res += (determinant ? "tte " : "") + "Charrette à butin";
                    break;
            }
            return res;
        }
    }

    public Recompense(ObjRec objet) {
        this.objetRec = objet;
        this.quantite = 1;
    }
    
    public Recompense(Costume c) {
        this(ObjRec.COSTUME);
        this.costume = c;
    }
    
    public Recompense(ObjRec objet, Region region, boolean regionFixe) {
        this(objet);
        this.region = region;
        this.regionFixe = regionFixe;
    }
    
    public Recompense(ObjRec objet, int quantite) {
        this.objetRec = objet;
        this.quantite = quantite;
    }
    
    public Recompense(ObjRec objet, CartePerso carte, int quantite) {
        this(objet, quantite);
        this.cartePerso = carte;
    }
    
    
    public Recompense(ObjRec objet, int quantite, Region region, boolean regionFixe) {
        this.objetRec = objet;
        this.quantite = quantite;
        this.region = region;
        this.regionFixe = regionFixe;
    }

    public ObjRec getObjetRec() {
        return objetRec;
    }

    public int getQuantite() {
        return quantite;
    }

    public void coeffQuantite(int coeff) {
        this.quantite *= coeff;
    }

    public CartePerso getCartePerso() {
        return cartePerso;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isRegionFixe() {
        return regionFixe;
    }

    public void setRegionFixe(boolean regionFixe) {
        this.regionFixe = regionFixe;
    }

    public Costume getCostume() {
        return costume;
    }

    public void setCostume(Costume costume) {
        this.costume = costume;
    }
    
    public boolean estUnCoffre() {
        boolean res = false;
        switch(objetRec) {
            case CARTE:
            case SACOCHE:
            case COFFRET:
            case GRAND_COFFRE:
            case COFFRE_ROYAL:
            case CHARRETTE_A_BUTIN:
                res = true;
        }
        return res;
    }
    
    public int getNombreTirages() {
        int res = 0;
        switch(objetRec) {
            case SACOCHE:
                res = 1;
                break;
            case COFFRET:
                res = 3;
                break;
            case GRAND_COFFRE:
                res = 5;
                break;
            case COFFRE_ROYAL:
                res = 7;
                break;
            case CHARRETTE_A_BUTIN:
                res = 13;
                break;
        }
        return res;
    }
    
    private ObjRec objetRec;
    private int quantite;
    private Region region;
    private boolean regionFixe;
    private CartePerso cartePerso;
    private Costume costume;
}
