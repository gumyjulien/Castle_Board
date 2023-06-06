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
public class TourDeGuet extends Perso {

    public static final String description = "Une tour de guet qui ne manque pas à son travail : surveiller le plateau.";
    public static final String[] titreAmeliorations = new String[]{"", "", ""};
    public static final String[] descAmeliorations = new String[]{"", "", ""};

    public TourDeGuet(int camp, int id) {
        super(5, 1, 0, 2, TypeDeplacement.CLASSIQUE, TypeAttaque.A_DISTANCE, camp, "Tour de guet", false, false, titreAmeliorations, descAmeliorations, description);
        this.id = id;
    }

    public TourDeGuet(int camp) {
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
            perso = new TourDeGuet(getCamp(), this.id);
        } else {
            perso = new TourDeGuet(getCamp());
        }
        return perso;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        if (o instanceof TourDeGuet) {
            TourDeGuet p = (TourDeGuet) o;
            if (p.getId() == this.id) {
                res = true;
            }
        }
        return res;
    }

    private int id;

}
