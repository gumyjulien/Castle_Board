/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.CartePerso;
import app.beans.Compte;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class WrkEquipe {

    public WrkEquipe() {
    }

    public ArrayList<CartePerso> getCartesDisponibles(ArrayList<CartePerso> equipeSelec, ArrayList<CartePerso> toutesLesCartes) {
        ArrayList<CartePerso> persosDisponibles = new ArrayList<>();
        boolean trouve;
        for (CartePerso carte : toutesLesCartes) {
            trouve = false;
            for (CartePerso carteEquipe : equipeSelec) {
                if (carteEquipe != null) {
                    if (WrkCartes.comparerCartes(carte, carteEquipe)) {
                        trouve = true;
                    }
                }
            }
            if (!trouve) {
                persosDisponibles.add(carte);
            }
        }
        return persosDisponibles;
    }

    public boolean peutAjouterEquipe(CartePerso carte, ArrayList<CartePerso> equipeSelec) {
        boolean res = false;
        if (carte.isDebloque() && !equipeSelec.contains(carte)) {
            res = true;
        }
        return res;
    }

    public boolean ajouterPerso(ArrayList<CartePerso> equipe, ArrayList<CartePerso> persoDisponibles, CartePerso carte) {
        boolean res = false;
        if (carte.getPerso().isHeros()) {
            if (equipe.get(0) == null) {
                equipe.set(0, carte);
                persoDisponibles.remove(carte);
                res = true;
            }
        } else {

            int prems = premierePosLibreEquipe(equipe);
            if (prems != -1) {
                equipe.set(prems, carte);
                persoDisponibles.remove(carte);
                res = true;
            }
        }

        return res;
    }

    public void retirerPerso(ArrayList<CartePerso> equipe, ArrayList<CartePerso> persoDisponibles, CartePerso carte) {
        if (carte.getPerso().isHeros()) {
            equipe.set(0, null);
            persoDisponibles.add(carte);
        } else {
            int index = equipe.indexOf(carte);
            equipe.set(index, null);
            persoDisponibles.add(carte);
        }
    }

    public void remplacerCarte(ArrayList<CartePerso> equipe, ArrayList<CartePerso> persoDisponibles, CartePerso carteAvant, CartePerso nouvCarte) {
        if (carteAvant.getPerso().isHeros() == nouvCarte.getPerso().isHeros()) {
            retirerPerso(equipe, persoDisponibles, carteAvant);
            ajouterPerso(equipe, persoDisponibles, nouvCarte);
        }
    }

    private int premierePosLibreEquipe(ArrayList<CartePerso> equipe) {
        int pos = -1;
        for (int i = 1; i < equipe.size(); i++) {
            if (equipe.get(i) == null) {
                pos = i;
                break;
            }
        }
        return pos;

    }

    public void ameliorerPerso(CartePerso cartePerso, Compte compte) {
        if (cartePerso.isDebloque()) {
            if (cartePerso.getNiveau() < cartePerso.getNiveauMax()) {
                if (cartePerso.getNombreDeCartes() >= cartePerso.getNombreDeCartesRequises()) {
                    if (compte.getNombrePieces() >= cartePerso.getPrixAmel()) {
                        compte.ajouterPieces(-cartePerso.getPrixAmel());
                        cartePerso.niveauSuperieur();
                    }
                }
            }
        }
    }

    public int getTailleCaracteresSelonLongueur(String texte, boolean reduit) {
        int resultat = 0;
        int max = 16;
        if (reduit) {
            max = 13;
        }
        if (texte != null) {
            resultat = max - (Math.round(texte.length() / 10));
            if (resultat < 9) {
                resultat = 9;
            }
        }
        return resultat;
    }

}
