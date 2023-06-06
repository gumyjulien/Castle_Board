/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.ennemi;

import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Pharaon extends Perso {
    
    public static final String titreSuper = "Sceptre héqa";
    public static final String description = "Sorti de son tombeau, le Pharaon est prêt à se venger : il tire des projectiles avec son oeil et attire les ennemis avec son sceptre";
    public static final String[] titreAmeliorations = new String[] {"Oeil maudit", titreSuper};
    public static final String[] descAmeliorations = new String[] {"Les projectiles de l'oeil empoisonnent la cible pendant 2 tours", titreSuper + ": inflige 3 dégâts et étourdit la cible"};
    
    public Pharaon(int camp){
        super(32, 1, 1, 3, TypeDeplacement.CLASSIQUE, TypeAttaque.A_DISTANCE, camp, "Pharaon", false, true, 9, new ArrayList<TypeAction>(), true, 0, titreSuper, "Après avoir touché 9 ennemis, il attire l'ennemi le plus faible jusqu'à lui", titreAmeliorations, descAmeliorations, description);
        ajouterEvenementPuissance(TypeAction.ATTAQUE);
    }
    
    @Override
    public Perso ameliorer() {
        int niveau = getNmbrEtoiles() + 1;
        switch(niveau) {
            case 3:
                setDg(getDg() + 1);
                break;
            case 4:
                setPvMax(getPvMax() + 2);
                break;
            default:
                setPvMax(getPvMax() + 1);
        }
        setPv(getPvMax());
        setNmbrEtoiles(niveau);
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        return new Pharaon(getCamp());
    }
}
