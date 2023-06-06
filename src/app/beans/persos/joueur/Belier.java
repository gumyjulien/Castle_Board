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
public class Belier extends Perso {
    
    public static final String description = "Cette machine ne peut uniquement attaquer en ligne droite, en haut ou en bas, et ne peut se déplacer qu'à droite ou à gauche. Les dégâts d'impact sont en fonction de la distance parcourue.";
    public static final String[] titreAmeliorations = new String[] {"Chargez!", "Bois d'ébène", "Réparations immédiates"};
    public static final String[] descAmeliorations = new String[] {"+1 dégât minimum", "Ne peut plus se casser au moment de l'impact", "UP : répare entièrement le Bélier"};
    
    public Belier(int camp){
        super(10, 1, 1, 0, Perso.TypeDeplacement.COTES, Perso.TypeAttaque.SPECIALE, camp, "Bélier", false, false, titreAmeliorations, descAmeliorations, description);
        this.setEnchantement("Etourdi", 1 + 1);
    }
    
    @Override
    public Perso ameliorer(){
        super.setNmbrEtoiles(super.getNmbrEtoiles()+1);
        switch(super.getNmbrEtoiles()){
            case 1:
                super.setDg(super.getDg() + 1);
                break;
            case 2:
                break;
            case 3:
                super.guerirPV(super.getPvMax(), null, null);
                break;
        }
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Belier(getCamp());
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
        m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 7, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }

    public int getDgChargeEnBas() {
        return dgChargeEnBas;
    }

    public void setDgChargeEnBas(int dgChargeEnBas) {
        this.dgChargeEnBas = dgChargeEnBas;
    }
    
    public int getDgChargeEnHaut() {
        return dgChargeEnHaut;
    }

    public void setDgChargeEnHaut(int dgChargeEnHaut) {
        this.dgChargeEnHaut = dgChargeEnHaut;
    }
    
    private int dgChargeEnHaut;
    private int dgChargeEnBas;
}
