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
public class Medecin extends Perso {

    public static final String description = "Le médecin est irremplaçable : il guérit les alliés lorsque ceux-ci manquent de vie. Ne peut pas guérir deux fois le même allié d'affilée.";
    public static final String[] titreAmeliorations = new String[]{"Premiers secours", "Pluie de médocs", "Lancer de bandages"};
    public static final String[] descAmeliorations = new String[]{"+2 de soin", "UP: déclenche la capacité " + titreAmeliorations[1], "UP: " + titreAmeliorations[1] + " ET +1 portée"};

    public Medecin(int camp) {
        super(5, -10, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.A_DISTANCE, camp, "Médecin", true, false, -1, new ArrayList<TypeAction>(), true, 2, titreAmeliorations[1], "Dépose des bandages, des vaccins et des anti-douleurs à côté de tous les persos alliés", titreAmeliorations, descAmeliorations, description);
        this.soin = 1;
        dernierPersoSoigne = null;
    }

    @Override
    public Perso ameliorer() {
        super.setNmbrEtoiles(super.getNmbrEtoiles() + 1);
        switch (super.getNmbrEtoiles()) {
            case 1:
                soin += 2;
                break;
            case 2:
                break;
            case 3:
//                super.ajouterEvenementPuissance(TypeAction.ENCHANTER);
//                super.setJetonsPuissanceRequis(4);
                super.setPortee(2);
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Medecin(getCamp());
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
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 40));
        m = new Mission(Mission.TypeMission.SOIN, Mission.Portee.PERSO, 5, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }

    public int getSoin() {
        return soin;
    }

    public Perso getDernierPersoSoigne() {
        return dernierPersoSoigne;
    }

    public void setDernierPersoSoigne(Perso dernierPersoSoigne) {
        this.dernierPersoSoigne = dernierPersoSoigne;
    }

    private int soin;
    private Perso dernierPersoSoigne;
}
