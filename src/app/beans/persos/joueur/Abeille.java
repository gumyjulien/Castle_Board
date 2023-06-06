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
import javafx.scene.image.Image;

/**
 *
 * @author gumyj
 */
public class Abeille extends Perso {

    public static final String description = "Bzzz";
    public static final String[] titreAmeliorations = new String[]{"", "", ""};
    public static final String[] descAmeliorations = new String[]{"", "", ""};

    public Abeille(int camp, Ruche src, boolean peutSoigner, int id) {
        super(1, 1, 1, 1, camp, "Abeille", peutSoigner, false, titreAmeliorations, descAmeliorations, description);
        super.ajouteCostume(new Costume(this, "Plongeur", 1200, Costume.Rarete.MYTHIQUE));
        setFormeInitiale(false);
        setSuperForme(src);
        this.id = id;
        this.dejaJoue = false;
        if (peutSoigner) {
            super.setDg(0);
        }
    }

    public int getId() {
        return id;
    }

    public void setDejaJoue(boolean dejaJoue) {
        this.dejaJoue = dejaJoue;
    }

    public boolean aDejaJoue() {
        return dejaJoue;
    }

    @Override
    public Perso ameliorer() {
        return this;
    }

    private int id;
    private boolean dejaJoue;

    @Override
    public Perso copierPerso() {
        Perso res = new Abeille(getCamp(), (Ruche) getSuperForme(), canEffetAmi(), id);
        res.setCostumeSelec(getCostumeSelec());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        if (o instanceof Abeille) {
            Abeille ab = (Abeille) o;
            if (super.equals(ab) && id == ab.getId()) {
                res = true;
            }
        }
        return res;
    }

}
