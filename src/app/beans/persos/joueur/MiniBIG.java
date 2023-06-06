/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Costume;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class MiniBIG extends Perso {
    
    public static final String titreSuper = "Epée de crystal";
    public static final String description = "Un robot programmé pour trancher les ennemis. Très efficace contre les ennemis avec beaucoup de points de vie.";
    public static final String[] titreAmeliorations = new String[] {"Coup fatal", "Armure de métal", "Travail minutieux"};
    public static final String[] descAmeliorations = new String[] {titreSuper + ": +1 dégât", titreSuper + ": reçoit un bouclier lorsque sa capacité s'active", titreSuper + " s'active pour les cibles de 5pv ou plus"};
    
    public MiniBIG(int camp){
        super(6, 2, 2, 1, camp, "Mini B.I.G", false, false, -1, new ArrayList<>(), false, 0, titreSuper, "Les attaques infligent +1 dégât si la cible a plus de 6pv", titreAmeliorations, descAmeliorations, description);
        super.ajouteCostume(new Costume(this, "C.A.T", 600, Costume.Rarete.EPIQUE));
        dgSupplementaires = 1;
        pvMinRequis = 6;
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(getNmbrEtoiles()) {
            case 1:
                dgSupplementaires++;
                break;
            case 2:
                break;
            case 3:
                pvMinRequis--;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new MiniBIG(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 25));
        Mission m = new Mission(Mission.TypeMission.COMPLETER_NIV, Mission.Portee.GLOBALE, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 100));
        m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 10, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }
    
    @Override
    public Recompense[] getRecsBadge() {
        Recompense[] res = new Recompense[6];
        res[0] = new Recompense(Recompense.ObjRec.PIECES, 200);
        res[1] = new Recompense(super.getCostumeParNom("C.A.T"));
        res[2] = new Recompense(Recompense.ObjRec.PIECES, 1000);
        res[3] = new Recompense(Recompense.ObjRec.GRAND_COFFRE, null, false);
        res[4] = new Recompense(Recompense.ObjRec.RUBIS, 250);
        res[5] = new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, false);
        return res;
    }
    
    public int getDegatsCritique() {
        return getDg() + dgSupplementaires;
    }

    public int getPvMinRequis() {
        return pvMinRequis;
    }
    
    private int dgSupplementaires;
    private int pvMinRequis;
}
