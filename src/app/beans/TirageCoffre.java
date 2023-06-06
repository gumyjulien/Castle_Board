/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

/**
 *
 * @author gumyj
 */
public class TirageCoffre {
    
    public enum Ressource {
        PIECES,
        RUBIS,
        POINT_DE_MAITRISE;
        
        @Override        
        public String toString() {
            String res = "";
            String[] split = name().split("_");
            int i = split.length;
            for (String string : split) {
                res += string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase() + (i > 1 ? " " : "");
            }
            return res;
        }
    }
    
    public TirageCoffre(Costume c) {
        this.costume = c;
    }
    
    public TirageCoffre(Ressource ress, int quantite) {
        this.ressource = ress;
        this.quantite = quantite;
    }
    
    public TirageCoffre(CartePerso carte, int quantite) {
        this.cartePerso = carte;
        this.quantite = quantite;
    }
    
    public TirageCoffre(Ressource ress, CartePerso carte, int quantite) {
        this.ressource = ress;
        this.cartePerso = carte;
        this.quantite = quantite;
    }

    public int getQuantite() {
        return quantite;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public CartePerso getCartePerso() {
        return cartePerso;
    }

    public Costume getCostume() {
        return costume;
    }
    
    
    private int quantite;
    private Ressource ressource;
    private CartePerso cartePerso;
    private Costume costume;
}
