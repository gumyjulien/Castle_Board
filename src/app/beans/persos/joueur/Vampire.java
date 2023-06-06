/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Compteur;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Vampire extends Perso {
    
    public static final String titreSuper = "Morsures";
    public static final String description = "Le vampire mord les ennemis et se regénère des points de vie. Cette abilité ne fonctionne pas contre un autre vampire, car le sang n'y est pas assez pur.";
    public static final String[] titreAmeliorations = new String[] {"Suçon sanguin", "Dents pointues", "100% pur sang"};
    public static final String[] descAmeliorations = new String[] {titreSuper + ": la dose de soin correspond au nombre de dégâts infligés", titreSuper + ": provoque des saignements", titreSuper + ": peut dépasser le maximum de PV"};
    
    public Vampire(int camp){
        super(4, 2, 1, 1, camp, "Vampire", false, false, -1, new ArrayList<>(), false, 0, titreSuper, "Mord les ennemis et restitue 1PV à chaque attaque", titreAmeliorations, descAmeliorations, description);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Vampire(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 10));
        Mission m = new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.GLOBALE, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 50));
        m = new Mission(Mission.TypeMission.ACTIVER_CAPACITE, Mission.Portee.PERSO, 5, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }
    
    public void attaque(){
        if(super.getNmbrEtoiles() >= 1){
            if(super.getNmbrEtoiles() >= 3){
                super.setPvMax(super.getPvMax() + (super.getDg() - (super.getPvMax() - super.getPv())));
            }
            super.guerirPV(super.getDg(), this, Compteur.Source.SUPER);
        } else {
            super.guerirPV(1, this, Compteur.Source.SUPER);
        }
    }
}