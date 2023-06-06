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
public class Princesse extends Perso {
    
    public static final String description = "Elle détient la couronne la plus rare et la plus désirée de tout le royaume. De temps en temps, elle la prête à ses amis pour qu'ils l'utilisent avec soin.";
    public static final String[] titreAmeliorations = new String[] {"Pouvoir royal", "Sur ses grands chevaux", "Partage de pouvoir"};
    public static final String[] descAmeliorations = new String[] {"Obtient l'enchantement permanent \"Couronne\", lui permettant ainsi de faire 2 actions au cours du même tour", "+1 vitesse", "Débloque la capacité " + titreAmeliorations[2]};
    
    public Princesse(int camp){
        super(6, 1, 1, 1, camp, "Princesse", false, false, -1, new ArrayList<TypeAction>(), true, 3, titreAmeliorations[0], "GO: Confère l'enchantement \"Couronne\" à l'allié le plus proche", titreAmeliorations, descAmeliorations, description);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(super.getNmbrEtoiles()){
            case 1:
                break;
            case 2:
                super.setVitesse(2);
                break;
            case 3:
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Princesse(getCamp());
        res.setCostumeSelec(super.getCostumeSelec());
        return res;
    }
    
    @Override
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 30));
        Mission m = new Mission(Mission.TypeMission.ACTIVER_CAPACITE, Mission.Portee.GLOBALE, 1, recompenses, this, false);
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
