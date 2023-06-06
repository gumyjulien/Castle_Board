/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans.persos.ennemi;

import app.beans.Perso;

/**
 *
 * @author GumyJ01
 */
public class HommeDePaille extends Perso {

    public static final String description = "Juste un homme de paille...";
    public static final String[] titreAmeliorations = new String[]{"", "", ""};
    public static final String[] descAmeliorations = new String[]{"", "", ""};

    public HommeDePaille(int camp, int id) {
        super(3, 0, 0, 0, camp, "Homme de paille", false, false, titreAmeliorations, descAmeliorations, description);
        this.id = id;
    }

    public HommeDePaille(int camp) {
        this(camp, -1);
    }

    public int getId() {
        return id;
    }

    @Override
    public Perso ameliorer() {
        return this;
    }

    @Override
    public Perso copierPerso() {
        Perso perso;
        if (this.id != -1) {
            perso = new HommeDePaille(getCamp(), this.id);
        } else {
            perso = new HommeDePaille(getCamp());
        }
        return perso;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        if (o instanceof HommeDePaille) {
            HommeDePaille p = (HommeDePaille) o;
            if (p.getId() == this.id) {
                res = true;
            }
        }
        return res;
    }

    private int id;

}
