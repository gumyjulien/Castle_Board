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
public class Demon extends Perso {
    
    public static final String titreSuper = "Appel des enfers";
    public static final String description = "Tout juste sorti des enfers, le démon enflamme, après 5 attaques, tous les ennemis de trouvant dans un rayon autour de lui. Ces cases brûlent à la fin du tour et infligent 1 dégât.";
    public static final String[] titreAmeliorations = new String[] {"Infernal", "Vengeance diabolique", "Force des enfers"};
    public static final String[] descAmeliorations = new String[] {titreSuper + ": +1 de rayon", "Gagne 1 jeton de puissance à chaque fois qu'il est attaqué", titreSuper + ": +1 d'attaque"};
    
    public Demon(int camp){
        super(8, 1, 1, 1, camp, "Démon", false, false, 5, new ArrayList<TypeAction>(), true, 0, titreSuper, "Après 5 attaques, enflamme tous les ennemis de trouvant dans un rayon autour de lui", titreAmeliorations, descAmeliorations, description);
        super.ajouterEvenementPuissance(TypeAction.ATTAQUE);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(super.getNmbrEtoiles()){
            case 2:
                super.ajouterEvenementPuissance(TypeAction.DEFENSE);
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Demon(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 25));
        Mission m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 6, recompenses, this, false);
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
