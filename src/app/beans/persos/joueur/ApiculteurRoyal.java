/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Costume;
import app.beans.Enchantement;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class ApiculteurRoyal extends Perso {
    
    public static final String description = "L'apiculteur royal s'occupe du bien être de toutes les abeilles du royaume. Pour le remercier, celles-ci l'aident en cas de danger, et peuvent même soigner ses blessures graves !";
    public static final String[] titreAmeliorations = new String[] {"Ruche royale", "Pot de miel", "Abeilles en folie"};
    public static final String[] descAmeliorations = new String[] {"Débloque la capacité " + titreAmeliorations[0], "Lorsqu'il atteint 6PV ou moins, il mange un pot de miel et restitue 4PV, et obtient l'enchantement \"Mielleux\"", "Toutes les abeillent gagnent l'enchantement \"Force\" lors de leur apparition"};
    
    public ApiculteurRoyal(int camp){
        super(9, 1, 1, 1, camp, "Apiculteur Royal", false, true, 2, new ArrayList<TypeAction>(), false, 1, titreAmeliorations[0], "Après s'être déplacé deux fois, construit une ruche sur la case derrière lui", titreAmeliorations, descAmeliorations, description);
        super.ajouterEvenementPuissance(TypeAction.DEPLACER);
        super.ajouteCostume(new Costume(this, "Plongeur", 1200, Costume.Rarete.MYTHIQUE));
        super.setNmbrEtoiles(1);
        majSousForme();
        rucheExistante = false;
        potDeMielActif = false;
    }
    
    @Override
    public void majSousForme() {
        Ruche r = new Ruche(getCamp(), this);
        r.setCostumeSelec(r.getCostumeParNom(getCostumeSelec().getNom()));
        setSousForme(r);
    }

    public boolean isRucheExistante() {
        return rucheExistante;
    }

    public void setRucheExistante(boolean rucheExistante) {
        this.rucheExistante = rucheExistante;
        if(this.rucheExistante) {
            super.viderEvenementPuissance();
            super.setJetonsPuissanceRequis(-1);
        }
    }

    public boolean isPotDeMielActif() {
        return potDeMielActif;
    }

    public void setPotDeMielActif(boolean potDeMielActif) {
        this.potDeMielActif = potDeMielActif;
        if(this.potDeMielActif) {
            super.guerirPV(4, this, null);
            super.setEnchantement(Enchantement.MIELLEUX, 100);
        }
    }
    
    @Override
    public Perso ameliorer() {
        int niveau = getNmbrEtoiles() + 1;
        switch(niveau) {
            case 4:
                setVitesse(getVitesse() + 1);
                break;
            case 3:
                setPvMax(getPvMax() + 2);
                break;
            case 6:
                setPvMax(getPvMax() + 3);
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
        Perso res = new ApiculteurRoyal(getCamp());
        for (int i = 1; i < getNmbrEtoiles(); i++) {
            res.ameliorer();
        }
        res.setCostumeSelec(super.getCostumeSelec());
        res.majSousForme();
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
        m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 3, recompenses, this, false);
        m.setPartieUnique(true);
        m.setAvecSousForme(true);
        res.add(m);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 999));
        m = new Mission(Mission.TypeMission.TERMINER_MISSION, Mission.Portee.PERSO, 1, recompenses, this, false);
        m.setPartieUnique(true);
        res.add(m);
        return res;
    }
    
    private boolean rucheExistante;
    private boolean potDeMielActif;
}
