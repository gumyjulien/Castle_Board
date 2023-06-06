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
public class PantinArme extends Perso {
    public static final String description = "Juste un pantin... mais armé...";
    public static final String[] titreAmeliorations = new String[]{"", "", ""};
    public static final String[] descAmeliorations = new String[]{"", "", ""};

    public PantinArme(int camp, int id) {
        super(6, 2, 2, 1, camp, "Pantin armé", false, false, titreAmeliorations, descAmeliorations, description);
        this.id = id;
    }
    
    public PantinArme(int camp) {
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
            perso = new PantinArme(getCamp(), this.id);
        } else {
            perso = new PantinArme(getCamp());
        }
        return perso;
    }
    
    @Override 
    public boolean equals(Object o) {
        boolean res = false;
        if(o instanceof PantinArme) {
            PantinArme p = (PantinArme) o;
            if(p.getId() == this.id) {
                res = true;
            }
        }
        return res;
    }
    
    private int id;
}
