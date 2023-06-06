/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class AbeilleCostaude extends Perso {
    
    public static final String description = "Bzzz costaud";
    public static final String[] titreAmeliorations = new String[] {"", "", ""};
    public static final String[] descAmeliorations = new String[] {"", "", ""};
    
    public AbeilleCostaude(int camp){
        super(4, 2, 2, 1, camp, "Abeille costaude", false, false, titreAmeliorations, descAmeliorations, description);
    }
    
    @Override
    public Perso ameliorer() {
        return this;
    }

    @Override
    public Perso copierPerso() {
        return new AbeilleCostaude(getCamp());
    }
}
