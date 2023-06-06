/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans.persos.ennemi;

import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author GumyJ01
 */
public class HommeCible extends Perso {
    public static final String description = "Votre cible principale...";
    public static final String[] titreAmeliorations = new String[]{"", "", ""};
    public static final String[] descAmeliorations = new String[]{"", "", ""};

    public HommeCible(int camp, int id) {
        super(1, 0, 0, 0, camp, "Homme-cible", false, false, titreAmeliorations, descAmeliorations, description);
        this.id = id;
    }
    
    public HommeCible(int camp) {
        this(camp, -1);
    }

    public int getId() {
        return id;
    }
    
    @Override
    public Perso ameliorer() {
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso perso;
        if (this.id != -1) {
            perso = new HommeCible(getCamp(), this.id);
        } else {
            perso = new HommeCible(getCamp());
        }
        return perso;
    }
    
    @Override 
    public boolean equals(Object o) {
        boolean res = false;
        if(o instanceof HommeCible) {
            HommeCible p = (HommeCible) o;
            if(p.getId() == this.id) {
                res = true;
            }
        }
        return res;
    }
    
    private int id;
}
