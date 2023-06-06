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
public class Objectif implements Serializable {

    public enum TypeObjectif {
        FLAWLESS_VICTORY,
        SEUIL_DG_SUBIS,
        CIBLE_HEROS,
        CIBLE_SPECIALE,
        DEGATS_AVEC,
        DEGATS_TOTAUX,
        DEGATS_AVEC_HEROS,
        RECUPERER_RESSOURCE,
        ATTEINDRE_LIGNE
    }

    public enum DifficulteObjectif {
        FACILE,
        MOYEN,
        DIFFICILE,
        EXPERT;

        @Override
        public String toString() {
            return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        }
    }

    public Objectif(DifficulteObjectif difficulte, TypeObjectif typeObjectif, int nombre, Recompense recompense) {
        this(difficulte, typeObjectif, recompense);
        this.nombre = nombre;
    }

    public Objectif(DifficulteObjectif difficulte, TypeObjectif typeObjectif, Recompense recompense) {
        this.difficulte = difficulte;
        this.typeObjectif = typeObjectif;
        this.accompli = false;
        this.recompenseRecup = false;
        this.recompense = recompense;
        this.perso = null;
        if (typeObjectif.equals(TypeObjectif.CIBLE_HEROS) || typeObjectif.equals(TypeObjectif.CIBLE_SPECIALE)) {
            this.nombre = 1;
        } else {
            this.nombre = 0;
        }
    }

    public Objectif(DifficulteObjectif difficulte, TypeObjectif typeObjectif, Perso perso, Recompense recompense) {
        this(difficulte, typeObjectif, recompense);
        this.perso = perso;
    }

    public Objectif(DifficulteObjectif difficulte, TypeObjectif typeObjectif, Perso perso, int nombre, Recompense recompense) {
        this(difficulte, typeObjectif, recompense);
        this.perso = perso;
        this.nombre = nombre;
    }

    public TypeObjectif getTypeObjectif() {
        return typeObjectif;
    }

    public boolean isAccompli() {
        return accompli;
    }

    public boolean isRecompenseRecup() {
        return recompenseRecup;
    }

    public void setRecompenseRecup(boolean recompenseRecup) {
        this.recompenseRecup = recompenseRecup;
    }

    public void setAccompli(boolean accompli) {
        this.accompli = accompli;
    }

    public DifficulteObjectif getDifficulte() {
        return difficulte;
    }

    public Perso getPerso() {
        return perso;
    }

    public int getNombre() {
        return nombre;
    }

    public Recompense getRecompense() {
        return recompense;
    }

    public Objectif copierObjectif() {
        return new Objectif(difficulte, typeObjectif, perso, nombre, recompense);
    }

    @Override
    public String toString() {
        String titre = "";
        switch (typeObjectif) {
            case CIBLE_HEROS:
                titre = "Éliminer le héros adverse";
                break;
            case CIBLE_SPECIALE:
                titre = "Éliminer le " + perso.getNom() + " adverse";
                break;
            case DEGATS_AVEC:
                titre = "Infliger " + nombre + " dégâts avec votre " + perso.getNom();
                break;
            case FLAWLESS_VICTORY:
                titre = "Gagner la partie sans subir de dégâts";
                break;
            case DEGATS_TOTAUX:
                titre = "Infliger un total de " + nombre + " dégâts";
                break;
            case DEGATS_AVEC_HEROS:
                titre = "Infliger " + nombre + " dégâts avec votre héros";
                break;
            case SEUIL_DG_SUBIS:
                titre = "Gagner la partie en subissant moins de " + nombre + " dégâts";
                break;
        }
        return titre;
    }

    /* si le type est CIBLE_SPECIALE, le perso est la cible (l'ennemi a vaincre)
       si le type est DEGATS_AVEC, le perso est la source (l'allie qui doit faire des degats)
     */
    private DifficulteObjectif difficulte;
    private TypeObjectif typeObjectif;
    private Perso perso;
    private int nombre;
    private Recompense recompense;
    private boolean accompli;
    private boolean recompenseRecup;

}
