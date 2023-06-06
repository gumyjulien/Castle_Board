/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans.persos.joueur;

import app.beans.Costume;
import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Ruche extends Perso {
    
    public static final String description = "Bzzzz";
    public static final String[] titreAmeliorations = new String[] {"", "", ""};
    public static final String[] descAmeliorations = new String[] {"", "", ""};
    
    public Ruche(int camp, ApiculteurRoyal src){
        super(2, 0, 0, 0, camp, "Ruche", false, false, 2, new ArrayList<TypeAction>(), true, 0, "", "", titreAmeliorations, descAmeliorations, description);
        super.ajouteCostume(new Costume(this, "Plongeur", 1200, Costume.Rarete.MYTHIQUE));
        super.ajouterEvenementPuissance(TypeAction.GO);
        setFormeInitiale(false);
        majSousForme();
        setSuperForme(src);
        abeillesDeSoin = false;
        idLePlusHaut = 0;
    }

    public boolean isAbeillesDeSoin() {
        return abeillesDeSoin;
    }

    public void changeAbeillesDeSoin() {
        this.abeillesDeSoin = !abeillesDeSoin;
        majSousForme();
    }

    public int getIdLePlusHaut() {
        return idLePlusHaut;
    }
    
    public void incrementeId() {
        idLePlusHaut++;
        majSousForme();
    }
    
    
    @Override
    public void majSousForme() {
        Abeille a = new Abeille(getCamp(), this, abeillesDeSoin, idLePlusHaut);
        a.setCostumeSelec(a.getCostumeParNom(getCostumeSelec().getNom()));
        setSousForme(a);
    }
    
    @Override
    public Perso ameliorer() {
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        Perso res = new Ruche(getCamp(), (ApiculteurRoyal) getSuperForme());
        res.setCostumeSelec(getCostumeSelec());
        res.majSousForme();
        return res;
    }
    
    private boolean abeillesDeSoin;
    private int idLePlusHaut;
}
