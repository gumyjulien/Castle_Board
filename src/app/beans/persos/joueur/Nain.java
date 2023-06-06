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
public class Nain extends Perso {

    public static final String description = "Un guerrier un peu timide derrière son bouclier. Du fait qu'il soit protégé, il ne se déplace que très lentement. Son point faible : il subit 1.5x les dégâts à chaque attaque contre lui.";
    public static final String[] titreAmeliorations = new String[]{"Protection", "Furie", "Mur de boucliers"};
    public static final String[] descAmeliorations = new String[]{"Reçoit un bouclier permanent de 1", "+1 dégât", "UP: déclenche la capacité " + titreAmeliorations[2]};

    public Nain(int camp) {
        super(12, 1, 1, 1, camp, "Nain", false, false, -1, new ArrayList<TypeAction>(), true, 3, titreAmeliorations[2], "Confère 3 de bouclier à tous ses alliés proches",titreAmeliorations, descAmeliorations, description);
    }

    @Override
    public Perso ameliorer() {
        super.setNmbrEtoiles(super.getNmbrEtoiles() + 1);

        switch (super.getNmbrEtoiles()) {
            case 1:
                super.setProtection(1);
                break;
            case 2:
                super.setDg(super.getDg() + 1);
                break;
            case 3:
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Nain(getCamp());
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
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 30));
        m = new Mission(Mission.TypeMission.DEGATS_SUBIS, Mission.Portee.PERSO, 6, recompenses, this, false);
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
