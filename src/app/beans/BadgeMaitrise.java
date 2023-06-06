/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javafx.scene.paint.Color;

/**
 *
 * @author GumyJ01
 */
public class BadgeMaitrise implements Serializable, Comparable<BadgeMaitrise> {

    public BadgeMaitrise(CartePerso carte) {
        this(carte, 0, 0);
    }

    public BadgeMaitrise(CartePerso carte, int niveau, int points) {
        this.carte = carte;
        this.niveau = niveau;
        this.points = points;
        this.nom = "Maîtrise de votre " + carte.getPerso().getNom();
        this.type = Type.PERSO;
        this.methodesPts = new HashMap<>();
        this.recsRecuperees = new boolean[]{false, false, false, false, false, false};
    }

    public BadgeMaitrise(String nom) {
        this(0, 0, nom);
    }

    public BadgeMaitrise(int niveau, int points, String nom) {
        this.nom = nom;
        this.niveau = niveau;
        this.points = points;
        this.type = Type.GLOBAL;
        this.methodesPts = new HashMap<>();
        this.recsRecuperees = new boolean[]{false, false, false, false, false, false};
    }

    public CartePerso getCarte() {
        return carte;
    }

    public void setCarte(CartePerso carte) {
        this.carte = carte;
    }

    public int getNiveau() {
        return niveau;
    }

    public void ameliorer() {
        if (niveau < getNiveauMax()) {
            this.niveau++;
        }
    }

    public int getPoints() {
        return points;
    }

    public int getPointsRequis() {
        if (pointsRequis == null) {
            pointsRequis = new int[]{10, 50, 200, 500, 1000, 3000, 10000};
        }
        return pointsRequis[niveau];
    }
    
    public boolean isMax() {
        boolean res = false;
        if(niveau >= getNiveauMax()) {
            res = true;
        }
        return res;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getNiveauMax() {
        return 6;
    }

    public String getRarete() {
        String[] raretes = new String[]{"Manquant", "Bronze", "Argent", "Or", "Diamant", "Rubis", "Obsidienne"};
        return raretes[niveau];
    }

    public Color getColor() {
        Color[] raretes = new Color[]{Color.GREY, Color.BROWN, Color.LIGHTGRAY, Color.GOLD, Color.LIGHTBLUE, Color.ORANGERED, Color.DARKSLATEBLUE};
        return raretes[niveau];
    }

    public void setPointsRequis(int[] ptsReq) {
        this.pointsRequis = ptsReq;
    }

    public Recompense[] getRecompenses() {
        return recompenses;
    }

    public void setRecompenses(Recompense[] recompenses) {
        this.recompenses = recompenses;
    }

    public void recupRecompense(int i) {
        this.recsRecuperees[i] = true;
    }

    public boolean[] getRecsRecuperees() {
        return recsRecuperees;
    }

    public void ajouteMethodePts(Mission nom, int pts) {
        if (methodesPts == null) {
            methodesPts = new HashMap<>();
        }
        this.methodesPts.put(nom, pts);
    }

    public void modifieMethodePts(Mission ancien, Mission nouv, int pts) {
        this.methodesPts.remove(ancien);
        this.methodesPts.put(nouv, pts);
    }

    public void clearMethodesPts() {
        this.methodesPts.clear();
    }

    public TreeMap<Mission, Integer> getMethodesPtsTriees() {
        TreeMap<Mission, Integer> res = new TreeMap<>();
        for (Mission cle : this.methodesPts.keySet()) {
            res.put(cle, this.methodesPts.get(cle));
        }
        return res;
    }

    public ArrayList<Mission> getListeMissions() {
        ArrayList<Mission> res = new ArrayList<>();
        for (Mission m : this.methodesPts.keySet()) {
            res.add(m);
        }
        return res;
    }

    public int getValSelonMission(Mission m) {
        int res = 0;
        for (Mission mi : this.methodesPts.keySet()) {
            if (m.equals(mi)) {
                res = this.methodesPts.get(mi);
            }
        }
        return res;
    }

    public String getNom() {
        return nom;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setRecsRecuperees(boolean[] recsRecuperees) {
        this.recsRecuperees = recsRecuperees;
    }

    public BadgeMaitrise copierBadge() {
        BadgeMaitrise b;
        if (this.type.equals(Type.PERSO)) {
            b = new BadgeMaitrise(getCarte(), getNiveau(), getPoints());
        } else {
            b = new BadgeMaitrise(getNiveau(), getPoints(), getNom());
        }
        b.setRecompenses(this.recompenses);
        b.setPointsRequis(pointsRequis);
        for (Mission m : methodesPts.keySet()) {
            b.ajouteMethodePts(m, this.methodesPts.get(m));
        }
        b.setRecsRecuperees(recsRecuperees);
        return b;
    }

    private CartePerso carte;
    private int niveau;
    private int points;
    private String nom;

    private int[] pointsRequis;
    private Type type;
    private Recompense[] recompenses;
    private boolean[] recsRecuperees;

    private HashMap<Mission, Integer> methodesPts;

    @Override
    public int compareTo(BadgeMaitrise b) {
        int res = 0;
        if (b.getNiveau() < this.niveau) {
            res = -1;
        } else if (b.getNiveau() > this.niveau) {
            res = 1;
        } else {
            if (((double) b.getPoints() / (double) b.getPointsRequis()) < ((double) getPoints() / (double) getPointsRequis())) {
                res = -1;
            } else if (((double) b.getPoints() / (double) b.getPointsRequis()) > ((double) getPoints() / (double) getPointsRequis())) {
                res = 1;
            }
        }
        return res;
    }

    public enum Type {
        PERSO,
        GLOBAL
    }
}
