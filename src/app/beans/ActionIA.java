/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

/**
 *
 * @author GumyJ01
 */
public class ActionIA {

    public ActionIA(TypeAction action, int[] position, int numCase, Perso perso) {
        this.action = action;
        this.position = position;
        this.numCase = numCase;
        this.perso = perso;
    }

    public TypeAction getAction() {
        return action;
    }

    public int[] getPosition() {
        return position;
    }

    public int getNumCase() {
        return numCase;
    }

    public Perso getPerso() {
        return perso;
    }
    
    
    private TypeAction action;
    private int[] position;
    private int numCase;
    private Perso perso;
}
