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
public class ActionPerso {

    /**
     * Constructeur utilisé pour une action concernant plusieurs persos.
     */
    public ActionPerso(Action typeAction, Perso perso, Perso persoCible, int[] posInitiale, int[] posFinale) {
        this.typeAction = typeAction;
        this.perso = perso;
        this.persoCible = persoCible;
        this.posInitiale = posInitiale;
        this.posFinale = posFinale;
    }
    
    /**
     * Constructeur utilisé pour une action concernant un seul perso.
     */
    public ActionPerso(Action typeAction, Perso perso, int[] posInitiale, int[] posFinale) {
        this.typeAction = typeAction;
        this.perso = perso;
        this.posInitiale = posInitiale;
        this.posFinale = posFinale;
    }

    public Action getTypeAction() {
        return typeAction;
    }

    public Perso getPerso() {
        return perso;
    }

    public Perso getPersoCible() {
        return persoCible;
    }

    public int[] getPosInitiale() {
        return posInitiale;
    }

    public int[] getPosFinale() {
        return posFinale;
    }
    
    public enum Action {
        DEPLACEMENT,
        ATTAQUE,
        ACTIVER_SUPER,
        AMELIORER,
        ENCHANTER
    }
    
    private Action typeAction;
    private Perso perso;
    private Perso persoCible;
    private int[] posInitiale;
    private int[] posFinale;
}
