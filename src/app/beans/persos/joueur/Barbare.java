/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.BadgeMaitrise;
import app.beans.Costume;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TypeAction;
import app.workers.NiveauxWrk;
import app.workers.WrkCartes;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Barbare extends Perso {

    public static final String description = "Un vaillant guerrier prêt à sacrifier sa vie au combat. Il paraît que son épée est tellement tranchante qu'elle pourrait traverser les ennemis.";
    public static final String[] titreAmeliorations = new String[]{"Barbarie", "Bon casque", "Epée tranchante"};
    public static final String[] descAmeliorations = new String[]{"Débloque sa capacité " + titreAmeliorations[0], "+3 points de vie", "Passe a travers les ennemis et peut attaquer à nouveau"};

    public Barbare(int camp) {
        super(5, 1, 2, 1, camp, "Barbare", false, false, -1, new ArrayList<TypeAction>(), true, 1, titreAmeliorations[0], "Chaque 4 tours, procure Force à tous ses alliés proches", titreAmeliorations, descAmeliorations, description);
        ciblesMax = 0;
        super.ajouteCostume(new Costume(this, "Sombrero", 0, Costume.Rarete.SPECIAL));
    }

    @Override
    public Perso ameliorer() {
        super.setNmbrEtoiles(super.getNmbrEtoiles() + 1);
        switch (super.getNmbrEtoiles()) {
            case 1:
                super.ajouterEvenementPuissance(TypeAction.GO);
                super.setJetonsPuissanceRequis(4);
                break;
            case 2:
                super.setPvMax(super.getPvMax() + 3);
                super.guerirPV(3, null, null);
                break;
            case 3:
                super.setActionSuivante(TypeAction.ATTAQUE, TypeAction.ATTAQUESPEC);
                ciblesMax = 3;
                break;
        }
        return this;
    }

    @Override
    public Perso copierPerso() {
        Perso res = new Barbare(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }

    public int getCiblesMax() {
        return ciblesMax;
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
        m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 5, recompenses, this, false);
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
        res[1] = new Recompense(super.getCostumeParNom("Sombrero"));
        res[2] = new Recompense(Recompense.ObjRec.PIECES, 1000);
        res[3] = new Recompense(Recompense.ObjRec.GRAND_COFFRE, null, true);
        res[4] = new Recompense(Recompense.ObjRec.RUBIS, 250);
        res[5] = new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, true);
        return res;
    }

    private int ciblesMax;
}
