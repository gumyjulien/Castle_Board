/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author GumyJ01
 */
public class Compte implements Serializable {

    public Compte(String pseudo, ArrayList<Niveau> progressionDesNiveaux, ArrayList<CartePerso> toutesLesCartes, ArrayList<Mission> toutesLesMissions, ArrayList<BadgeMaitrise> tousLesBadges) {
        this.pseudo = pseudo;
        this.progressionDesNiveauxJoues = progressionDesNiveaux;
        nombrePieces = 200;
        nombreRubis = 50;
        this.toutesLesCartes = toutesLesCartes;
        this.tousLesBadges = tousLesBadges;
        this.equipeSelec = new ArrayList<>();
        for (CartePerso carte : toutesLesCartes) {
            if (carte.isDebloque()) {
                equipeSelec.add(carte);
            }
        }
        this.toutesLesMissions = toutesLesMissions;
        this.tousLesBadges = tousLesBadges;
        this.codesUtilises = new ArrayList<>();
    }

    public Compte(ArrayList<CartePerso> toutesLesCartes, int camp) {
        this.toutesLesCartes = toutesLesCartes;
        for (CartePerso c : this.toutesLesCartes) {
            c.getPerso().setCamp(camp);
        }
        this.equipeSelec = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            equipeSelec.add(null);
        }
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getNombrePieces() {
        return nombrePieces;
    }

    public void setNombrePieces(int nombrePieces) {
        this.nombrePieces = nombrePieces;
    }

    public int getNombreRubis() {
        return nombreRubis;
    }

    public void setNombreRubis(int nombreRubis) {
        this.nombreRubis = nombreRubis;
    }

    public void ajouterPieces(int nombre) {
        this.nombrePieces += nombre;
    }

    public void ajouterRubis(int nombre) {
        this.nombreRubis += nombre;
    }

    public ArrayList<Niveau> getProgressionDesNiveauxJoues() {
        return progressionDesNiveauxJoues;
    }

    public Niveau getNiveauDansProgressionDesNiveauxJoues(int idNiveau) {
        Niveau resultat = null;
        for (Niveau niv : progressionDesNiveauxJoues) {
            if (niv.getIdNiveau() == idNiveau) {
                resultat = niv;
            }
        }
        return resultat;
    }

    public Niveau getPlusHautNiveau() {
        return progressionDesNiveauxJoues.get(progressionDesNiveauxJoues.size() - 1);
    }

    public Region getPlusHauteRegionDecouverte() {
        return getPlusHautNiveau().getRegion();
    }

    public void setProgressionDesNiveauxJoues(ArrayList<Niveau> progressionDesNiveaux) {
        this.progressionDesNiveauxJoues = progressionDesNiveaux;
    }

    public void ajouterNiveauJoue(Niveau niv) {
        Niveau niveau = getNiveauDansProgressionDesNiveauxJoues(niv.getIdNiveau());
        if (niveau == null) {
            this.progressionDesNiveauxJoues.add(niv);
        } else {
            this.progressionDesNiveauxJoues.set(progressionDesNiveauxJoues.indexOf(niveau), niv);
        }
    }

    public ArrayList<Perso> getPersosEquipeSelec() {
        ArrayList<Perso> resultat = new ArrayList<>();
        for (CartePerso carte : equipeSelec) {
            Perso p = carte.getPerso().copierPerso();
            resultat.add(p);
        }
        return resultat;
    }

    public ArrayList<CartePerso> getEquipeSelec() {
        return equipeSelec;
    }

    public void setEquipeSelec(ArrayList<CartePerso> equipeSelec) {
        this.equipeSelec = equipeSelec;
    }

    public ArrayList<CartePerso> getToutesLesCartes() {
        return toutesLesCartes;
    }

    public void setToutesLesCartes(ArrayList<CartePerso> toutesLesCartes) {
        this.toutesLesCartes = toutesLesCartes;
    }

    public ArrayList<Mission> getToutesLesMissions() {
        return toutesLesMissions;
    }

    public void setToutesLesMissions(ArrayList<Mission> toutesLesMissions) {
        this.toutesLesMissions = toutesLesMissions;
    }

    public ArrayList<BadgeMaitrise> getTousLesBadges() {
        return tousLesBadges;
    }

    public BadgeMaitrise getBadge(CartePerso c) {
        BadgeMaitrise res = null;
        for (BadgeMaitrise b : tousLesBadges) {
            if (b.getType().equals(BadgeMaitrise.Type.PERSO)) {
                if (b.getCarte().getPerso().equals(c.getPerso())) {
                    res = b;
                }
            }
        }
        return res;
    }

    public void setTousLesBadges(ArrayList<BadgeMaitrise> tousLesBadges) {
        this.tousLesBadges = tousLesBadges;
    }

    public ArrayList<OffreMagasin> getToutesLesOffresMagasin() {
        return toutesLesOffresMagasin;
    }

    public void setToutesLesOffresMagasin(ArrayList<OffreMagasin> toutesLesOffresMagasin) {
        this.toutesLesOffresMagasin = toutesLesOffresMagasin;
    }

    public ArrayList<String> getCodesUtilises() {
        return codesUtilises;
    }

    public void addCodeUtilise(String code) {
        this.codesUtilises.add(code);
    }

    private String pseudo;
    private int nombrePieces;
    private int nombreRubis;
    private ArrayList<Niveau> progressionDesNiveauxJoues;
    private ArrayList<CartePerso> equipeSelec;
    private ArrayList<CartePerso> toutesLesCartes;
    private ArrayList<Mission> toutesLesMissions;
    private ArrayList<BadgeMaitrise> tousLesBadges;
    private ArrayList<OffreMagasin> toutesLesOffresMagasin;
    
    private ArrayList<String> codesUtilises;
}
