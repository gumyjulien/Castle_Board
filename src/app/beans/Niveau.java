/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author GumyJ01
 */
public class Niveau implements Serializable, Cloneable {

    public Niveau(Region region, int idNiveau, ArrayList<Perso> equipeAdverse, ArrayList<Objectif> listeObjectifs, Perso[][] plateau, IAEnnemi iaEnnemi) {
        this(region, idNiveau, equipeAdverse, listeObjectifs, plateau, iaEnnemi, false);
    }

    public Niveau(Region region, int idNiveau, ArrayList<Perso> equipeAdverse, ArrayList<Objectif> listeObjectifs, Perso[][] plateau, IAEnnemi iaEnnemi, boolean isBoss) {
        this.region = region;
        this.idNiveau = idNiveau;
        this.equipeAdverse = equipeAdverse;
        this.listeObjectifs = listeObjectifs;
        this.plateau = plateau;
        this.iaEnnemi = iaEnnemi;
        this.boss = isBoss;
    }

    public int getIdNiveau() {
        return idNiveau;
    }

    public boolean isBoss() {
        return boss;
    }

    public ArrayList<Perso> getEquipeAdverse() {
        return equipeAdverse;
    }

    public void setEquipeAdverse(ArrayList<Perso> equipeAdverse) {
        this.equipeAdverse = equipeAdverse;
    }

    public ArrayList<Objectif> getListeObjectifs() {
        return listeObjectifs;
    }

    public int getNombreObjectifsAccomplis() {
        int resultat = 0;
        for (Objectif objectif : listeObjectifs) {
            if (objectif.isAccompli()) {
                resultat++;
            }
        }
        return resultat;
    }

    public int getRecompensesRestantes() {
        int resultat = 0;
        for (Objectif objectif : listeObjectifs) {
            if (objectif.isAccompli() && !objectif.isRecompenseRecup()) {
                resultat++;
            }
        }
        return resultat;
    }

    public Perso[][] getPlateau() {
        return plateau.clone();
    }

    public void setPlateau(Perso[][] plateau) {
        this.plateau = plateau;
    }

    public IAEnnemi getIaEnnemi() {
        return iaEnnemi;
    }

    public void setIaEnnemi(IAEnnemi iaEnnemi) {
        this.iaEnnemi = iaEnnemi;
    }

    public Niveau copieNiveau() {
        return new Niveau(region, idNiveau, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
    }

    public Region getRegion() {
        return region;
    }

    private final Region region;
    private final int idNiveau;
    private final ArrayList<Objectif> listeObjectifs;
    private final boolean boss;
    private transient ArrayList<Perso> equipeAdverse;
    private transient Perso[][] plateau;
    private transient IAEnnemi iaEnnemi;
}
