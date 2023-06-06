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
public class LeCoureur extends Perso {
    
    public static final String description = "Un sportif surentraîné, pouvant courir sur tout le plateau lorsque l'envie lui prend. Il partage même un peu de son talent aux alliés à proximité.";
    public static final String[] titreAmeliorations = new String[] {"Sprint", "Déplacement statégique", "Expérience"};
    public static final String[] descAmeliorations = new String[] {"Débloque sa capacité " + titreAmeliorations[0], "Les alliés proches peuvent désormais se déplacer en diagonale", "Gagne un jeton de puissance à chaque fois qu'il se déplace"};
    
    public LeCoureur(int camp){
        super(12, 1, 1, 1, camp, "Le Coureur", false, true, 3, new ArrayList<TypeAction>(), false, 0, titreAmeliorations[0], "Peut se déplacer sur tout le plateau après avoir attaqué, ou été attaqué 3 fois", titreAmeliorations, descAmeliorations, description);
        super.ajouterEvenementPuissance(TypeAction.ATTAQUE);
        super.ajouterEvenementPuissance(TypeAction.DEFENSE);
        super.setNmbrEtoiles(1);
        super.ajouteCostume(new Costume(this, "Astronaute", 0, Costume.Rarete.MYTHIQUE));
    }


    
    @Override
    public Perso ameliorer() {
        int niveau = getNmbrEtoiles() + 1;
        switch(niveau) {
            case 2:
            case 5:
                setVitesse(getVitesse() + 1);
                break;
            case 3:
                setPvMax(getPvMax() + 2);
                break;
            case 4:
                setPvMax(getPvMax() + 1);
                break;
            case 6:
                ajouterEvenementPuissance(TypeAction.DEPLACER);
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
        Perso p = new LeCoureur(getCamp());
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
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 15));
        Mission m = new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.PERSO, 6, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 50));
        m = new Mission(Mission.TypeMission.ACTIVER_CAPACITE, Mission.Portee.PERSO, 2, recompenses, this, true);
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
        res[1] = new Recompense(Recompense.ObjRec.COFFRET, 2, null, true);
        res[2] = new Recompense(Recompense.ObjRec.PIECES, 1000);
        res[3] = new Recompense(Recompense.ObjRec.GRAND_COFFRE, null, true);
        res[4] = new Recompense(Recompense.ObjRec.RUBIS, 250);
        res[5] = new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, true);
        return res;
    }
    
    
}
