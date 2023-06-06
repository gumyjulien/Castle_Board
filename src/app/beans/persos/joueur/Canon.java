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
public class Canon extends Perso {
    
    public static final String description = "";
    public static final String[] titreAmeliorations = new String[] {"Poudre à canon", "Onde de choc", "Explosion finale"};
    public static final String[] descAmeliorations = new String[] {"-1 tour requis pour tirer", "Les boulets repoussent la cible", "UP: Explose et inflige le nombre de dégâts équivalent à son nombre de points de vie restants aux ennemis autour de lui"};
    
    public Canon(int camp){
        super(7, 1, 0, 2, TypeDeplacement.CLASSIQUE, TypeAttaque.A_DISTANCE, camp, "Canon", false, false, 3, new ArrayList<TypeAction>(), true, 0, "Boulet de canon", "Tire un boulet de canon. Temps de recharge : 3 tours", titreAmeliorations, descAmeliorations, description);
        super.ajouterEvenementPuissance(TypeAction.GO);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(super.getNmbrEtoiles()){
            case 1:
                super.setJetonsPuissanceRequis(2);
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        return new Canon(getCamp());
    }
}
