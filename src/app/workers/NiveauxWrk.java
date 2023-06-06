/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.IAEnnemi;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.LeCoureur;
import app.beans.Niveau;
import app.beans.Objectif;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.Region;
import app.beans.TypeAction;
import app.beans.persos.ennemi.Cactus;
import app.beans.persos.ennemi.Elementaire;
import app.beans.persos.ennemi.HommeCible;
import app.beans.persos.ennemi.HommeDePaille;
import app.beans.persos.ennemi.PantinArme;
import app.beans.persos.ennemi.TourDeGuet;
import app.beans.persos.joueur.Canon;
import app.beans.persos.joueur.MaitreDesSabres;
import app.beans.persos.joueur.MiniBIG;
import app.beans.persos.joueur.Nain;
import app.beans.persos.joueur.Princesse;
import app.presentation.MainCtrl;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author GumyJ01
 */
public class NiveauxWrk {

    public NiveauxWrk(MainCtrl mainCtrl) {
        tousLesNiveaux = new ArrayList<>();
        toutesLesRegions = new ArrayList<>();
        // creer les niveaux
//        for (int i = 1; i < 4; i++) {
//            tousLesNiveaux.add(null);
//            resetNiveau(i);
//        }
        // creer les regions
        toutesLesRegions.add(new Region(1, "Camp d'entraînement", "d'entraînement"));
        toutesLesRegions.add(new Region(2, "Dunes semblables", "désertique"));
        toutesLesRegions.add(new Region(3, "Coeur du volcan", "infernale"));
        toutesLesRegions.add(new Region(4, "Jungle sauvage", "tropicale"));
        toutesLesRegions.add(new Region(5, "À l'abordage !", "pirate"));
        toutesLesRegions.add(new Region(6, "Marais douteux", "sinistre"));
        toutesLesRegions.add(new Region(7, "Plaines glacées", "hivernale"));
        toutesLesRegions.add(new Region(8, "Pays des rêves", "onirique"));
        toutesLesRegions.add(new Region(9, "Cour du château", "royale"));

        Region region1 = toutesLesRegions.get(0);
        Region region2 = toutesLesRegions.get(1);
        Region region3 = toutesLesRegions.get(2);
        IAEnnemi iaEnnemi = new IAEnnemi(IAEnnemi.Difficulte.TUTORIEL);

        // NIVEAU 1
        ArrayList<Perso> equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(5, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2));
        ArrayList<Objectif> listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 20)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC, new LeCoureur(1), 2, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new LeCoureur(0), region1), 24)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense(Recompense.ObjRec.SACOCHE, 1, region1, true)));
//        for (Objectif obj : listeObjectifs) {
//            obj.setAccompli(true);
//        }
        Perso[][] plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);

        Niveau niv1 = new Niveau(region1, 1, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv1);

        // NIVEAU 2
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(6, 1, 2, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new HommeDePaille(2, 2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 20)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC, new Barbare(1), 4, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new Barbare(0), region1), 1)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense(Recompense.ObjRec.SACOCHE, 2, region1, true)));
        plateau = new Perso[4][6];
        plateau[1][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);
        plateau[0][1] = equipeAdverse.get(2);
        Niveau niv2 = new Niveau(region1, 2, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv2);

        // NIVEAU 3
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(8, 1, 2, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new HommeDePaille(2, 2));
        equipeAdverse.add(new HommeDePaille(2, 3));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 50)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_TOTAUX, 12, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new LeCoureur(0), region1), 15)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense(Recompense.ObjRec.SACOCHE, region1, true)));
        plateau = new Perso[4][6];
        plateau[1][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);
        plateau[0][1] = equipeAdverse.get(2);
        plateau[1][3] = equipeAdverse.get(3);
        Niveau niv3 = new Niveau(region1, 3, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv3);

        // NIVEAU 4
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(4, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new HommeDePaille(2, 2));
        equipeAdverse.add(new HommeDePaille(2, 3));
        equipeAdverse.add(new HommeCible(2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_SPECIALE, equipeAdverse.get(4), new Recompense(Recompense.ObjRec.PIECES, 50)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC, new Nain(1), 4, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new LeCoureur(0), region1), 25)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense(Recompense.ObjRec.SACOCHE, region1, true)));
        plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(4);
        plateau[1][0] = equipeAdverse.get(1);
        plateau[1][1] = equipeAdverse.get(2);
        plateau[0][1] = equipeAdverse.get(3);
        plateau[3][0] = equipeAdverse.get(0);
        Niveau niv4 = new Niveau(region1, 4, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv4);
        
        iaEnnemi = new IAEnnemi(IAEnnemi.Difficulte.FACILE);

        // NIVEAU 5
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(14, 1, 2, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new PantinArme(2, 1));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new PantinArme(2, 2));
        equipeAdverse.add(new HommeDePaille(2, 2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 50)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.DEGATS_AVEC_HEROS, 5, new Recompense(Recompense.ObjRec.SACOCHE, region1, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.SEUIL_DG_SUBIS, 6, new Recompense(Recompense.ObjRec.COFFRET, region1, true)));
        plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(0);
        plateau[0][1] = equipeAdverse.get(1);
        plateau[1][1] = equipeAdverse.get(2);
        plateau[2][1] = equipeAdverse.get(3);
        plateau[3][1] = equipeAdverse.get(4);
        Niveau niv5 = new Niveau(region1, 5, equipeAdverse, listeObjectifs, plateau, iaEnnemi, true);
        tousLesNiveaux.add(niv5);
        
        // NIVEAU 6
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(11, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new HommeDePaille(2, 2));
        equipeAdverse.add(new TourDeGuet(2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, equipeAdverse.get(0), new Recompense(Recompense.ObjRec.PIECES, 50)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC_HEROS, 4, new Recompense(Recompense.ObjRec.SACOCHE, region1, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.SEUIL_DG_SUBIS, 5, new Recompense(Recompense.ObjRec.COFFRET, region1, true)));
        plateau = new Perso[5][7];
        plateau[0][2] = equipeAdverse.get(1);
        plateau[4][2] = equipeAdverse.get(2);
        plateau[3][1] = equipeAdverse.get(3);
        plateau[3][0] = equipeAdverse.get(0);
        Niveau niv6 = new Niveau(region1, 6, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv6);
        
        // NIVEAU 7
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(12, 1, 2, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new TourDeGuet(2, 1));
        equipeAdverse.add(new TourDeGuet(2, 2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, equipeAdverse.get(0), new Recompense(Recompense.ObjRec.PIECES, 60)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.DEGATS_TOTAUX, 22, new Recompense(Recompense.ObjRec.SACOCHE, 2, region1, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.SEUIL_DG_SUBIS, 10, new Recompense(Recompense.ObjRec.COFFRET, region1, true)));
        plateau = new Perso[5][6];
        plateau[2][0] = equipeAdverse.get(0);
        plateau[2][2] = equipeAdverse.get(1);
        plateau[1][1] = equipeAdverse.get(2);
        plateau[3][1] = equipeAdverse.get(3);
        Niveau niv7 = new Niveau(region1, 7, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv7);
        
        // NIVEAU 8
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(9, 2, 2, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new HommeDePaille(2, 1));
        equipeAdverse.add(new TourDeGuet(2, 1));
        equipeAdverse.add(new PantinArme(2, 1));
        equipeAdverse.add(new PantinArme(2, 2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, equipeAdverse.get(0), new Recompense(Recompense.ObjRec.PIECES, 75)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.DEGATS_TOTAUX, 13, new Recompense(Recompense.ObjRec.PIECES, 100)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.DIFFICILE, Objectif.TypeObjectif.SEUIL_DG_SUBIS, 8, new Recompense(Recompense.ObjRec.SACOCHE, 2, region1, true)));
        plateau = new Perso[5][6];
        plateau[2][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);
        plateau[1][1] = equipeAdverse.get(2);
        plateau[3][1] = equipeAdverse.get(3);
        plateau[0][1] = equipeAdverse.get(4);
        Niveau niv8 = new Niveau(region1, 8, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv8);
        
        // NIVEAU 9
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(4, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new PantinArme(2, 1));
        equipeAdverse.add(new PantinArme(2, 2));
        equipeAdverse.add(new PantinArme(2, 3));
        equipeAdverse.add(new HommeCible(2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_SPECIALE, equipeAdverse.get(4), new Recompense(Recompense.ObjRec.PIECES, 50)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.DEGATS_AVEC, new Barbare(1), 6, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new Barbare(0), region1), 25)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.DIFFICILE, Objectif.TypeObjectif.DEGATS_TOTAUX, 22, new Recompense(Recompense.ObjRec.COFFRET, region1, true)));
        plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(4);
        plateau[1][0] = equipeAdverse.get(1);
        plateau[1][1] = equipeAdverse.get(2);
        plateau[0][1] = equipeAdverse.get(3);
        plateau[3][0] = equipeAdverse.get(0);
        Niveau niv9 = new Niveau(region1, 9, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv9);
        
        // NIVEAU 10
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(22, 3, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "bois", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.RUBIS, 150)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.SEUIL_DG_SUBIS, 13, new Recompense(Recompense.ObjRec.COFFRE_ROYAL, region1, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.DIFFICILE, Objectif.TypeObjectif.DEGATS_AVEC, new MiniBIG(1), 15, new Recompense(Recompense.ObjRec.CARTE, new CartePerso(new MiniBIG(0), region1), 2)));
        plateau = new Perso[3][4];
        plateau[0][0] = equipeAdverse.get(0);
        Niveau niv10 = new Niveau(region1, 10, equipeAdverse, listeObjectifs, plateau, iaEnnemi, true);
        tousLesNiveaux.add(niv10);
        
        // NIVEAU 11
        equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(6, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "sable", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new Cactus(2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 20)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC_HEROS, 4, new Recompense(Recompense.ObjRec.SACOCHE, 1, region2, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense(Recompense.ObjRec.SACOCHE, 2, region2, true)));
        plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);

        Niveau niv11 = new Niveau(region2, 11, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv11);
        
        for (int i = 0; i < 4; i++) {
            equipeAdverse = new ArrayList<>();
        equipeAdverse.add(new Elementaire(6 + i, 1, 1, 1, Perso.TypeDeplacement.CLASSIQUE, Perso.TypeAttaque.MELEE, "sable", -1, new ArrayList<TypeAction>(), true, "", "", new String[3], new String[3], "Votre pire ennemi"));
        equipeAdverse.add(new Cactus(2));
        listeObjectifs = new ArrayList<>();
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.CIBLE_HEROS, new Recompense(Recompense.ObjRec.PIECES, 20 + i * 10)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.FACILE, Objectif.TypeObjectif.DEGATS_AVEC_HEROS, 4 + i, new Recompense(Recompense.ObjRec.SACOCHE, 1, region2, true)));
        listeObjectifs.add(new Objectif(Objectif.DifficulteObjectif.MOYEN, Objectif.TypeObjectif.FLAWLESS_VICTORY, new Recompense((i < 2 ? Recompense.ObjRec.SACOCHE : Recompense.ObjRec.GRAND_COFFRE), (i < 2 ? 2 : 1), region2, true)));
        plateau = new Perso[4][6];
        plateau[0][0] = equipeAdverse.get(0);
        plateau[2][1] = equipeAdverse.get(1);

        Niveau niv = new Niveau(region2, 12 + i, equipeAdverse, listeObjectifs, plateau, iaEnnemi);
        tousLesNiveaux.add(niv);
        }
        
    }

    public Niveau getNiveau(int id) {
        Niveau niv = null;
        for (Niveau niveau : tousLesNiveaux) {
            if (niveau.getIdNiveau() == id) {
                Perso[][] plateau = new Perso[niveau.getPlateau().length][niveau.getPlateau()[0].length];
                for (int i = 0; i < plateau.length; i++) {
                    for (int j = 0; j < plateau[0].length; j++) {
                        if (niveau.getPlateau()[i][j] != null) {
                            plateau[i][j] = niveau.getPlateau()[i][j].copierPerso();
                        }
                    }
                }
                ArrayList<Perso> equipeAdverse = new ArrayList<>();
                for (Perso perso : niveau.getEquipeAdverse()) {
                    equipeAdverse.add(perso.copierPerso());
                }
                niv = new Niveau(niveau.getRegion(), niveau.getIdNiveau(), equipeAdverse, niveau.getListeObjectifs(), plateau, niveau.getIaEnnemi());
            }
        }
        return niv;
    }

    public Niveau getNiveauDuCompte(int id, Compte compte) {
        Niveau niv = null;
        for (Niveau niveau : compte.getProgressionDesNiveauxJoues()) {
            if (niveau.getIdNiveau() == id) {
                niv = niveau;
            }
        }
        return niv;
    }

    public ArrayList<Niveau> getTousLesNiveaux() {
        return (ArrayList<Niveau>) tousLesNiveaux.clone();
    }
    
    public ArrayList<Region> getToutesLesRegions() {
        return (ArrayList<Region>) toutesLesRegions.clone();
    }

    public ArrayList<Integer> gererObjectifsNiveau(ArrayList<Objectif> tousLesObjectifs, HashMap<Perso, Compteur> tousLesCompteurs) {

        ArrayList<Integer> res = new ArrayList<>();

        for (Objectif objectif : tousLesObjectifs) {
            switch (objectif.getTypeObjectif()) {
                case CIBLE_HEROS:
                    for (Compteur compteur : tousLesCompteurs.values()) {
                        for (Perso perso : compteur.getEnnemisVaincus()) {
                            if (perso.isHeros()) {
                                objectif.setAccompli(true);
                                res.add(1);
                            }
                        }
                    }
                    if (!objectif.isAccompli()) {
                        res.add(0);
                    }
                    break;
                case CIBLE_SPECIALE:
                    for (Compteur compteur : tousLesCompteurs.values()) {
                        for (Perso perso : compteur.getEnnemisVaincus()) {
                            if (perso.getNom().equals(objectif.getPerso().getNom())) {
                                objectif.setAccompli(true);
                                res.add(1);
                            }
                        }
                    }
                    if (!objectif.isAccompli()) {
                        res.add(0);
                    }
                    break;
                case FLAWLESS_VICTORY:
                    boolean flawless = true;
                    int dgSubis = 0;
                    for (Compteur compteur : tousLesCompteurs.values()) {
                        if (compteur.getNombreDgSubis() > 0) {
                            flawless = false;
                            dgSubis += compteur.getNombreDgSubis();
                        }
                    }
                    if (flawless) {
                        objectif.setAccompli(true);
                    }
                    res.add(dgSubis);
                    break;
                case SEUIL_DG_SUBIS:
                    int nbrDgSubis = 0;
                    for (Compteur compteur : tousLesCompteurs.values()) {
                        if (compteur.getNombreDgSubis() > 0) {
                            nbrDgSubis += compteur.getNombreDgSubis();
                        }
                    }
                    if(nbrDgSubis <= objectif.getNombre()) {
                        objectif.setAccompli(true);
                    }
                    res.add(nbrDgSubis);
                    break;
                case DEGATS_AVEC:
                    boolean ok = false;
                    for (Perso perso : tousLesCompteurs.keySet()) {
                        if (perso.equals(objectif.getPerso())) {
                            Compteur compteur = tousLesCompteurs.get(perso);
                            if (compteur.getNombreDgInfliges() >= objectif.getNombre()) {
                                objectif.setAccompli(true);
                            }
                            res.add(compteur.getNombreDgInfliges());
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        res.add(0);
                    }
                    break;
                case DEGATS_TOTAUX:
                    int total = 0;
                    for (Compteur compteur : tousLesCompteurs.values()) {
                        total += compteur.getNombreDgInfliges();
                    }
                    if (total >= objectif.getNombre()) {
                        objectif.setAccompli(true);
                    }
                    res.add(total);
                    break;
                case DEGATS_AVEC_HEROS:
                    boolean oke = false;
                    for (Perso perso : tousLesCompteurs.keySet()) {
                        if (perso.isHeros()) {
                            Compteur compteur = tousLesCompteurs.get(perso);
                            if (compteur.getNombreDgInfliges() >= objectif.getNombre()) {
                                objectif.setAccompli(true);
                            }
                            res.add(compteur.getNombreDgInfliges());
                            oke = true;
                            break;
                        }
                    }
                    if (!oke) {
                        res.add(0);
                    }
                    break;
                default:
                    res.add(0);
                    break;
            }
        }

        return res;
    }

    public int getIdNiveaumax() {
        return tousLesNiveaux.get(tousLesNiveaux.size() - 1).getIdNiveau();
    }

    public Niveau getProchainNiveau(int id) {
        return getNiveau(id + 1);
    }

    private ArrayList<Niveau> tousLesNiveaux;
    private ArrayList<Region> toutesLesRegions;

}
