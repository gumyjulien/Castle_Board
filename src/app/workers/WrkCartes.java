/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.BadgeMaitrise;
import app.beans.CartePerso;
import app.beans.Perso;
import app.beans.Region;
import app.beans.persos.joueur.ApiculteurRoyal;
import app.beans.persos.joueur.Archere;
import app.beans.persos.joueur.Bandit;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.Belier;
import app.beans.persos.joueur.Canon;
import app.beans.persos.joueur.Demon;
import app.beans.persos.joueur.LeCoureur;
import app.beans.persos.joueur.MaitreDesSabres;
import app.beans.persos.joueur.Medecin;
import app.beans.persos.joueur.MiniBIG;
import app.beans.persos.joueur.Nain;
import app.beans.persos.joueur.Princesse;
import app.beans.persos.joueur.Scribe;
import app.beans.persos.joueur.Vampire;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author gumyj
 */
public class WrkCartes {

    public WrkCartes(NiveauxWrk wrkNiveaux) {
        this.toutesLesCartes = new ArrayList<>();

        // Ajout de toutes les cartes
        toutesLesCartes.add(new CartePerso(new Barbare(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new LeCoureur(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new Nain(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new Medecin(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.get(0).ajouteDesCartes(1);
        toutesLesCartes.get(1).ajouteDesCartes(1);
        toutesLesCartes.get(2).ajouteDesCartes(1);
        toutesLesCartes.get(3).ajouteDesCartes(1);
        toutesLesCartes.get(0).setDebloque(true);
        toutesLesCartes.get(1).setDebloque(true);
        toutesLesCartes.get(2).setDebloque(true);
        toutesLesCartes.get(3).setDebloque(true);
        toutesLesCartes.add(new CartePerso(new Belier(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new Bandit(1), wrkNiveaux.getToutesLesRegions().get(0)));
//        toutesLesCartes.add(new CartePerso(new Scribe(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new MiniBIG(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new Princesse(1), wrkNiveaux.getToutesLesRegions().get(1)));
        toutesLesCartes.add(new CartePerso(new MaitreDesSabres(1), wrkNiveaux.getToutesLesRegions().get(1)));
        toutesLesCartes.add(new CartePerso(new Vampire(1), wrkNiveaux.getToutesLesRegions().get(0)));
        toutesLesCartes.add(new CartePerso(new Demon(1), wrkNiveaux.getToutesLesRegions().get(1)));
        toutesLesCartes.add(new CartePerso(new ApiculteurRoyal(1), wrkNiveaux.getToutesLesRegions().get(8)));
        toutesLesCartes.add(new CartePerso(new Archere(1), wrkNiveaux.getToutesLesRegions().get(8)));
        
        toutesLesCartes = trieCartes(toutesLesCartes);

    }

    public ArrayList<CartePerso> metAJourToutesLesCartes(ArrayList<CartePerso> equipeSelec, ArrayList<CartePerso> toutesLesCartesDuJoueur) {

        ArrayList<CartePerso> cartesAjoutees = new ArrayList<>();
        
        boolean trouve;
        for (CartePerso carte : toutesLesCartes) {
            trouve = false;
            for (CartePerso carteJoueur : toutesLesCartesDuJoueur) {
                if (comparerCartes(carte, carteJoueur)) {
                    CartePerso c = new CartePerso(carteJoueur.getPerso().copierPerso(), carteJoueur.getRegion(), carteJoueur.isDebloque(), carteJoueur.getNiveau(), carteJoueur.getNombreDeCartes());
                    toutesLesCartesDuJoueur.set(toutesLesCartesDuJoueur.indexOf(carteJoueur), c);
                    if(equipeSelec.contains(carteJoueur)) {
                        equipeSelec.set(equipeSelec.indexOf(carteJoueur), c);
                    }
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                toutesLesCartesDuJoueur.add(new CartePerso(carte.getPerso().copierPerso(), carte.getRegion(), carte.isDebloque(), carte.getNiveau(), carte.getNombreDeCartes()));
                cartesAjoutees.add(new CartePerso(carte.getPerso(), carte.getRegion()));
            }
        }
        
        // controler s'il n'y a pas des cartes en trop
        int i = 0;
        while(toutesLesCartesDuJoueur.size() > toutesLesCartes.size()) {
            CartePerso cartePerso = toutesLesCartesDuJoueur.get(i);
            if(!cartesAjoutees.contains(cartePerso)) {
                toutesLesCartesDuJoueur.remove(cartePerso);
            } else {
                i++;
            }
        }

        return toutesLesCartesDuJoueur;
    }

    public ArrayList<CartePerso> getCartesDeDepart() {
        ArrayList<CartePerso> listePersosDepart = new ArrayList<>();
        for (CartePerso carte : toutesLesCartes) {
            if (carte.isDebloque()) {
                listePersosDepart.add(new CartePerso(carte.getPerso(), carte.getRegion(), carte.isDebloque(), carte.getNiveau(), carte.getNombreDeCartes()));
            }
        }
        return listePersosDepart;
    }

    public ArrayList<CartePerso> trieCartes(ArrayList<CartePerso> listeDesCartes) {
        ArrayList<CartePerso> resultat = new ArrayList<>();
        ArrayList<CartePerso> cartesDeblo = new ArrayList<>();
        ArrayList<CartePerso> cartesATrouver = new ArrayList<>();
        HashMap<Integer, ArrayList<CartePerso>> cartesParRegion = new HashMap<>();

        // trier les cartes par leur region
        for (CartePerso carte : listeDesCartes) {
            if (!cartesParRegion.containsKey(carte.getRegion().getId())) {
                cartesParRegion.put(carte.getRegion().getId(), new ArrayList<>());
            }
            ArrayList<CartePerso> cartesDejaPresentes = cartesParRegion.get(carte.getRegion().getId());
            if (carte.getPerso().isHeros()) {
                Perso perso = null;
                int i = -1;
                do {
                    i++;
                    if(!cartesDejaPresentes.isEmpty()) {
                        perso = cartesDejaPresentes.get(i).getPerso();
                    }
                } while ((i+1) < cartesDejaPresentes.size() && perso.isHeros());
                cartesDejaPresentes.add(i, carte);
            } else {
                cartesDejaPresentes.add(carte);
            }
            cartesParRegion.replace(carte.getRegion().getId(), cartesDejaPresentes);
        }
        ArrayList<Integer> listeDesRegions = new ArrayList<>();
        for (Integer integer : cartesParRegion.keySet()) {
            listeDesRegions.add(integer);
        }

        Collections.sort(listeDesRegions);

        for (Integer idRegion : listeDesRegions) {
            for (CartePerso cartePerso : cartesParRegion.get(idRegion)) {
                if(cartePerso.isDebloque()) {
                    cartesDeblo.add(cartePerso);
                } else {
                    cartesATrouver.add(cartePerso);
                }
            }
        }
        resultat.addAll(cartesDeblo);
        resultat.addAll(cartesATrouver);

        return resultat;
    }

    public int getNombreDeCartesDebloquees(ArrayList<CartePerso> toutesLesCartes) {
        int nombre = 0;
        for (CartePerso carte : toutesLesCartes) {
            if (carte.isDebloque()) {
                nombre++;
            }
        }
        return nombre;
    }

    public static ArrayList<CartePerso> getToutesLesCartes() {
        ArrayList<CartePerso> toutesLesCartesCopie = new ArrayList<>();
        for (CartePerso carte : toutesLesCartes) {
            toutesLesCartesCopie.add(new CartePerso(carte.getPerso(), carte.getRegion()));
        }
        return toutesLesCartesCopie;
    }

    public static ArrayList<CartePerso> getToutesLesCartes(int idRegion, boolean regionPrecise) {
        ArrayList<CartePerso> toutesLesCartesCopie = new ArrayList<>();
        for (CartePerso carte : toutesLesCartes) {
            if (regionPrecise) {
                if (carte.getRegion().getId() == idRegion) {
                    toutesLesCartesCopie.add(new CartePerso(carte.getPerso(), carte.getRegion()));
                }
            } else {
                if (carte.getRegion().getId() <= idRegion) {
                    toutesLesCartesCopie.add(new CartePerso(carte.getPerso(), carte.getRegion()));
                }
            }
        }
        return toutesLesCartesCopie;
    }
    
    public static boolean comparerCartes(CartePerso carte1, CartePerso carte2) {
        boolean res = false;
        if(carte1.getPerso().getNom().equals(carte2.getPerso().getNom()) && carte1.getRegion().getId() == carte2.getRegion().getId()){
            res = true;
        }
        return res;
    }
    
    public static ArrayList<CartePerso> copierEquipe(ArrayList<CartePerso> liste) {
        ArrayList<CartePerso> equipe = new ArrayList<>();
        for (CartePerso carte : liste) {
            equipe.add(new CartePerso(carte.getPerso().copierPerso(), carte.getRegion(), carte.isDebloque(), carte.getNiveau(), carte.getNombreDeCartes()));
        }
        return equipe;
    }
    
    public static Region getRegionAvecPerso(Perso p, ArrayList<Region> toutesLesRegions) {
        Region r = toutesLesRegions.get(0);
        for (CartePerso carte : toutesLesCartes) {
            if(carte.getPerso().equals(p)) {
                r = carte.getRegion();
                break;
            }
        }
        return r;
    }

    private static ArrayList<CartePerso> toutesLesCartes;
}
