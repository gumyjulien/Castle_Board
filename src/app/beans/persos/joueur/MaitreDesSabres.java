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
public class MaitreDesSabres extends Perso {
    
    public static final String description = "Les sabres n'ont plus de secrets pour lui; il manie épées, dagues, couteaux suisses et toute chose tranchante à la perfection.";
    public static final String[] titreAmeliorations = new String[] {"Festival de dagues", "Sabre tranchant", "Dagues pointues"};
    public static final String[] descAmeliorations = new String[] {"Débloque sa capacité " + titreAmeliorations[0], "Son attaque inflige des dégâts en arc, touchant ainsi les ennemis à côté de la cible", titreAmeliorations[0] + ": +2 dégâts"};
    
    public MaitreDesSabres(int camp){
        super(11, 1, 1, 1, camp, "Maître des sabres", false, true, 5, new ArrayList<TypeAction>(), true, 0, titreAmeliorations[0], "Après avoir touché 5 ennemis, il lance 3 dagues sur les ennemis les plus proches", titreAmeliorations, descAmeliorations, description);
        ajouterEvenementPuissance(TypeAction.ATTAQUE);
        super.ajouteCostume(new Costume(this, "Maître obscur", 2000, Costume.Rarete.MYTHIQUE));
        super.setNmbrEtoiles(1);
    }
    
    @Override
    public Perso ameliorer() {
        int niveau = getNmbrEtoiles() + 1;
        switch(niveau) {
            case 3:
            case 8:
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
        Perso p = new MaitreDesSabres(getCamp());
        for (int i = 1; i < getNmbrEtoiles(); i++) {
            p.ameliorer();
        }
        p.setCostumeSelec(super.getCostumeSelec());
        return p;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 120));
        Mission m = new Mission(Mission.TypeMission.ELIMINATIONS, Mission.Portee.PERSO, 3, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 40));
        m = new Mission(Mission.TypeMission.ACTIVER_CAPACITE, Mission.Portee.PERSO, 1, recompenses, this, false);
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
        res[5] = new Recompense(super.getCostumeParNom("Maître obscur"));
        return res;
    }
    
}
