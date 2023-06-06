/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;

/**
 *
 * @author gumyj
 */
public class Costume implements Serializable {

    public Costume(Perso perso, String nom) {
        this(perso, nom, 0, Rarete.DEFAULT);
    }
    
    public Costume(Perso perso, String nom, int prix, Rarete rarete) {
        this.perso = perso;
        this.nom = nom;
        this.prix = prix;
        this.rarete = rarete;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Rarete getRarete() {
        return rarete;
    }

    public void setRarete(Rarete rarete) {
        this.rarete = rarete;
    }

    public Perso getPerso() {
        return perso;
    }

    public void setPerso(Perso perso) {
        this.perso = perso;
    }
    
    private Perso perso;
    private String nom;
    private int prix;
    private Rarete rarete;
    
    public enum Rarete {
        DEFAULT,
        SPECIAL,
        EPIQUE,
        MYTHIQUE;
        
        @Override
        public String toString() {
            String res;
            switch(this) {
                case DEFAULT:
                    res = "Par défaut";
                    break;
                case SPECIAL:
                    res = "Spécial";
                    break;
                case EPIQUE:
                    res = "Épique";
                    break;
                case MYTHIQUE:
                    res = "Mythique";
                    break;
                default:
                    res = "Non classé";
            }
            return res;
        }
    }
}
