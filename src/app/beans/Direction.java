/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

/**
 *
 * @author GumyJ01
 */
public class Direction {
    
    public static int getValX(Nom nom) {
        return getVal(nom)[0];
    }
    
    public static int getValY(Nom nom) {
        return getVal(nom)[1];
    }
    
    public static int[] getVal(Nom nom) {
        int[] val = new int[2];
        int valX = 0;
        int valY = 0;
        
        switch(nom) {
            case HAUT:
                valY = -1;
                break;
            case BAS:
                valY = 1;
                break;
            case DROITE:
                valX = 1;
                break;
            case GAUCHE:
                valX = -1;
                break;
            case HAUT_GAUCHE:
                valX = -1;
                valY = -1;
                break;
            case HAUT_DROITE:
                valX = 1;
                valY = -1;
                break;
            case BAS_GAUCHE:
                valX = -1;
                valY = 1;
                break;
            case BAS_DROITE:
                valX = 1;
                valY = 1;
                break;
        }
        
        val[0] = valX;
        val[1] = valY;
        return val;
    }

    public static enum Nom {
        HAUT,
        BAS,
        DROITE,
        GAUCHE,
        HAUT_GAUCHE,
        HAUT_DROITE,
        BAS_GAUCHE,
        BAS_DROITE
    }
}
