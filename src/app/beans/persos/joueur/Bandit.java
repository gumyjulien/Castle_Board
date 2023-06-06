/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Bandit extends Perso {
    
    public static final String description = "Le bandit a plus d'un tour dans son sac (enfin, si celui-ci n'est pas dèjà rempli de vos bijoux et de votre argent); il est agile et futé, parfait pour mettre des coups sans se faire remarquer.";
    public static final String[] titreAmeliorations = new String[] {"Sandales de cuir", "Hit'n'run", "Agilité"};
    public static final String[] descAmeliorations = new String[] {"+2 de vitesse", "Peut se déplacer après avoir attaqué", "Esquive les attaques de moins de 2 dégâts"};
    
    public Bandit(int camp){
        super(5, 1, 1, 1, camp, "Bandit", false, false, titreAmeliorations, descAmeliorations, description);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(super.getNmbrEtoiles()){
            case 1:
                super.setVitesse(getVitesse() + 2);
                break;
            case 2:
                super.setActionSuivante(TypeAction.DEPLACER, TypeAction.ATTAQUE);
                break;
            case 3:
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Bandit(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 60));
        Mission m = new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.PERSO, 8, recompenses, this, true);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }
}
