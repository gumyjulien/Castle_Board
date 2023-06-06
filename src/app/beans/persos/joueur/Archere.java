/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.CartePerso;
import app.beans.Costume;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.Region;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Archere extends Perso {

    public static final String description = "L'archère répand l'amour dans tout le royaume. À la différence de Cupidon, ses flèches traversent réellement le coeur de ses cibles...";
    public static final String[] titreAmeliorations = new String[]{"Côtes de mailles", "Longue-vue", "Flèche dorée"};
    public static final String[] descAmeliorations = new String[]{"+2 points de vie", "La portée s'étend sur tout le plateau", "Débloque la capacité " + titreAmeliorations[2]};

    public Archere(int camp) {
        super(3, 1, 1, 2, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.A_DISTANCE, camp, "Archère", false, false, -1, new ArrayList<TypeAction>(), true, 3, titreAmeliorations[2], "Après 4 tirs, lance une flèche qui rebondit sur les ennemis", titreAmeliorations, descAmeliorations, description);
        super.ajouteCostume(new Costume(this, "Sirène", 300, Costume.Rarete.SPECIAL));
    }

    @Override
    public Perso ameliorer() {
        super.setNmbrEtoiles(super.getNmbrEtoiles() + 1);
        switch (super.getNmbrEtoiles()) {
            case 1:
                super.setPvMax(super.getPvMax() + 2);
                super.guerirPV(2, null, null);
                break;
            case 2:
                super.setPortee(10);
                break;
            case 3:
                super.ajouterEvenementPuissance(TypeAction.ATTAQUE);
                super.setJetonsPuissanceRequis(4);
                break;
        }
        return this;
    }

    @Override
    public Perso copierPerso() {
        Perso res = new Archere(getCamp());
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
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 200));
        m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 15, recompenses, this, false);
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
        res[1] = new Recompense(Recompense.ObjRec.COFFRET, null, false);
        res[2] = new Recompense(Recompense.ObjRec.PIECES, 500);
        res[3] = new Recompense(Recompense.ObjRec.COFFRE_ROYAL, null, false);
        res[4] = new Recompense(Recompense.ObjRec.RUBIS, 250);
        res[5] = new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, false);
        return res;
    }
}
