/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.Mission;
import app.beans.Niveau;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.Region;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.Demon;
import app.beans.persos.joueur.LeCoureur;
import app.beans.persos.joueur.MaitreDesSabres;
import app.beans.persos.joueur.Medecin;
import app.beans.persos.joueur.MiniBIG;
import app.beans.persos.joueur.Nain;
import app.beans.persos.joueur.Vampire;
import app.presentation.MainCtrl;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.Pane;

/**
 *
 * @author GumyJ01
 */
public class WrkMissions {

    public WrkMissions(MainCtrl mainCtrl, NiveauxWrk wrkNiveaux) {
        listeMissionsDidacticiel = new ArrayList<>();
        this.wrkNiveaux = wrkNiveaux;
        this.mainCtrl = mainCtrl;

        // MISSIONS DE BASE
        ArrayList<Mission> missionsDeBase = new ArrayList<>();

        ArrayList<Recompense> recs = new ArrayList<>();
        // mission de jeu
        missionsDeBase.addAll(genererMissionsGlobales(5));

        int nbreMax = WrkCartes.getToutesLesCartes().size();
        if (nbreMax % 2 != 0) {
            nbreMax--;
        }
        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 50));
        Mission missionDeblPersos = new Mission(Mission.TypeMission.DEBLOQUER_PERSO, Mission.Portee.GLOBALE, nbreMax, recs, new ArrayList<>(), (nbreMax - 4) / 2, (nbreMax - 4) / 2);
        for (int i = nbreMax - 2; i > 4; i -= 2) {
            recs = new ArrayList<>();
            recs.add(new Recompense(Recompense.ObjRec.PIECES, (i - 4) * 25));
            if (i > 6) {
                recs.add(new Recompense(Recompense.ObjRec.RUBIS, (i - 6) * 5));
            }
            ArrayList<Mission> missionSuivante = new ArrayList<>();
            missionSuivante.add(missionDeblPersos);
            Mission m = new Mission(Mission.TypeMission.DEBLOQUER_PERSO, Mission.Portee.GLOBALE, i, recs, missionSuivante, (i - 4) / 2, (nbreMax - 4) / 2);
            missionDeblPersos = m;
        }
        missionsDeBase.add(missionDeblPersos);
        missionsDeBase.addAll(genererMissionsRegion(this.wrkNiveaux.getToutesLesRegions().get(0)));
        missionsDeBase.addAll(genererMissionsPerso(new Barbare(1), 2));
        missionsDeBase.addAll(genererMissionsPerso(new LeCoureur(1), 2));
        missionsDeBase.addAll(genererMissionsPerso(new Nain(1), 2));
        missionsDeBase.addAll(genererMissionsPerso(new Medecin(1), 2));

//        recs = new ArrayList<>();
//        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 50));
//        missionsDeBase.add(new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.PERSO, 35, recs, new LeCoureur(0), true));
//        listeMissionsDidacticiel.add(new Mission(Mission.TypeMission.DIDACTITEL, Mission.Portee.GLOBALE, 1, recs, missionsDeBase, 1, 1));
//        listeMissionsDidacticiel.get(0).setTerminee(true);
        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.COFFRET, 6, this.wrkNiveaux.getToutesLesRegions().get(0), true));
        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 50));
        Mission m = new Mission(Mission.TypeMission.DIDACTITEL, Mission.Portee.GLOBALE, 1, recs, missionsDeBase, 1, 1);
        m.addProgression(1);
//        listeMissionsDidacticiel.add(m);

        listeMissionsDidacticiel.addAll(missionsDeBase);
    }

    public ArrayList<Mission> mettreAJourPersosMissions(ArrayList<Mission> toutesLesMissions) {
        for (Mission m : toutesLesMissions) {
            if (m.getPorteeMission().equals(Mission.Portee.PERSO)) {
                m.setPerso(m.getPerso().copierPerso());
            }
        }
        return toutesLesMissions;
    }

    public ArrayList<Mission> getListeMissionsDidacticiel() {
        ArrayList<Mission> res = new ArrayList<>();
        for (Mission m : listeMissionsDidacticiel) {
            res.add(m.copierMission());
        }
        return res;
    }

    public void terminerMission(ArrayList<Mission> toutesLesMissions, Mission m) {
        // controler si c'est pas une mission à étapes
        if (m.getMissionSuivante() != null) {
            if (m.getMissionSuivante().size() > 1) {
                for (Mission mission : m.getMissionSuivante()) {
                    toutesLesMissions.add(mission);
                }
            } else {
                if (!m.getMissionSuivante().isEmpty()) {
                    Mission mission = m.getMissionSuivante().get(0);
                    toutesLesMissions.set(toutesLesMissions.indexOf(m), mission);
                }
            }
        } else {
            if (m.getPorteeMission().equals(Mission.Portee.PERSO)) {
                toutesLesMissions.addAll(genererMissionsPerso(m.getPerso(), 1));
            } else if (m.getPorteeMission().equals(Mission.Portee.GLOBALE)) {
                toutesLesMissions.addAll(genererMissionsGlobales(1));
            }
        }
        toutesLesMissions.remove(m);
        mainCtrl.recupererRecompense(m.getRecompenses());
    }

    public ArrayList<Mission> genererMissionsGlobales(int nbr) {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recs;
        ArrayList<Mission> toutesLesMissions = new ArrayList<>();

        // generer le degre
        int min = 1;
        int max = 4;
        int chance = (int) ((Math.random() * (max - min + 1)) + min);

        if (chance < 4) {
            min = 1;
            max = 3;
        } else {
            min = 1;
            max = 6;
        }
        int degre = (int) ((Math.random() * (max - min + 1)) + min);

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 25 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.GLOBALE, 2 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.COFFRET, 1 + ((degre - (degre % 3)) / 3)));
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 25 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.GLOBALE, 20 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 10 * degre));
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 25 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.COMPLETER_NIV, Mission.Portee.GLOBALE, 2 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.COFFRE_ROYAL, 1 + ((degre - (degre % 3)) / 3)));
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 25 * degre));
        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 5 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.ELIMINATIONS, Mission.Portee.GLOBALE, 6 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 30 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.ENCHANTEMENT, Mission.Portee.GLOBALE, 2 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.COFFRET, 1 + ((degre - (degre % 2)) / 2)));
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 50 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.SOIN, Mission.Portee.GLOBALE, 8 * degre, recs));

        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.GRAND_COFFRE, 1 + ((degre - (degre % 2)) / 2)));
        recs.add(new Recompense(Recompense.ObjRec.PIECES, 50 * degre));
        recs.add(new Recompense(Recompense.ObjRec.RUBIS, 5 * degre));
        toutesLesMissions.add(new Mission(Mission.TypeMission.NBR_DEPLACEMENTS, Mission.Portee.GLOBALE, 9 * degre, recs));

        min = 0;
        max = toutesLesMissions.size() - 1;

        for (int i = 0; i < nbr; i++) {
            int num = (int) ((Math.random() * (max - min + 1)) + min);
            res.add(toutesLesMissions.get(num).copierMission());
        }

        return res;
    }

    public ArrayList<Mission> genererMissionsRegion(Region r) {
        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses;

        /*
            LISTE MISSIONS A ETAPES POUR REGION
                
                - Degats :       10  25  50  75  100
                - Soin :         10  25  50  75  100
                - Eliminations : 5   15  30  60  85
                - Jouer :        3   5   7   10  25
                
         */
        ArrayList<ArrayList<Recompense>> toutesLesRecs = new ArrayList<>();
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 20));
        toutesLesRecs.add(recompenses);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, r, true));
        toutesLesRecs.add(recompenses);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRET, r, true));
        toutesLesRecs.add(recompenses);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 50));
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, r, true));
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRET, r, true));
        toutesLesRecs.add(recompenses);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 50));
        recompenses.add(new Recompense(Recompense.ObjRec.RUBIS, 15));
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRE_ROYAL, r, true));
        toutesLesRecs.add(recompenses);
        int[] objDg = new int[]{10, 25, 50, 75, 100};
        int[] objSoin = new int[]{10, 25, 50, 75, 100};
        int[] objElim = new int[]{2, 5, 20, 60, 85};
        int[] objJouer = new int[]{3, 5, 7, 10, 25};

        Mission missionDg = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.REGION, objDg[4], toutesLesRecs.get(4), r);
        for (int i = 3; i >= 0; i--) {
            ArrayList<Mission> missionSuivante = new ArrayList<>();
            missionSuivante.add(missionDg);
            Mission m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.REGION, objDg[i], toutesLesRecs.get(i), r, missionSuivante, i + 1, 5);
            missionDg = m;
        }
        res.add(missionDg);

        Mission missionSoin = new Mission(Mission.TypeMission.SOIN, Mission.Portee.REGION, objSoin[4], toutesLesRecs.get(4), r);
        for (int i = 3; i >= 0; i--) {
            ArrayList<Mission> missionSuivante = new ArrayList<>();
            missionSuivante.add(missionSoin);
            Mission m = new Mission(Mission.TypeMission.SOIN, Mission.Portee.REGION, objSoin[i], toutesLesRecs.get(i), r, missionSuivante, i + 1, 5);
            missionSoin = m;
        }
        res.add(missionSoin);

        Mission missionElim = new Mission(Mission.TypeMission.ELIMINATIONS, Mission.Portee.REGION, objElim[4], toutesLesRecs.get(4), r);
        for (int i = 3; i >= 0; i--) {
            ArrayList<Mission> missionSuivante = new ArrayList<>();
            missionSuivante.add(missionElim);
            Mission m = new Mission(Mission.TypeMission.ELIMINATIONS, Mission.Portee.REGION, objElim[i], toutesLesRecs.get(i), r, missionSuivante, i + 1, 5);
            missionElim = m;
        }
        res.add(missionElim);

        Mission missionJouer = new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.REGION, objJouer[4], toutesLesRecs.get(4), r);
        for (int i = 3; i >= 0; i--) {
            ArrayList<Mission> missionSuivante = new ArrayList<>();
            missionSuivante.add(missionJouer);
            Mission m = new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.REGION, objJouer[i], toutesLesRecs.get(i), r, missionSuivante, i + 1, 5);
            missionJouer = m;
        }
        res.add(missionJouer);

        return res;
    }

    public ArrayList<Mission> genererMissionsPerso(Perso p, int nbr) {

        ArrayList<Mission> res = new ArrayList<>();
        ArrayList<Recompense> recompenses;
        Region r = WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions());

        ArrayList<Mission> listeMissions = new ArrayList<>();

        int min = 1;
        int max = 4;
        int chance = (int) ((Math.random() * (max - min + 1)) + min);

        if (chance < 4) {
            min = 1;
            max = 3;
        } else {
            min = 1;
            max = 6;
        }
        int degre = (int) ((Math.random() * (max - min + 1)) + min);

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 10 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 20 * degre));
        listeMissions.add(genereMissionDgOuSoin(p, 5 * degre, recompenses));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 15 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, 1 + ((degre - (degre % 3)) / 3), r, true));
        listeMissions.add(genereMissionDeplacement(p, 10 * degre, recompenses, Mission.TypeMission.DEGATS));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 20 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, 1 + ((degre - (degre % 3)) / 3), r, true));
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 10 * degre));
        listeMissions.add(genererMissionCapacite(p, 10 * degre, (degre % 2 == 0 ? 1 : 2), recompenses, Mission.TypeMission.DEGATS));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 20 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, 1 + ((degre - (degre % 3)) / 3), r, true));
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 10 * degre));
        listeMissions.add(genererMissionCapacite(p, 10 * degre, (degre % 2 == 0 ? 1 : 2), recompenses, Mission.TypeMission.ELIMINATIONS));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 30 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRET, 1 + ((degre - (degre % 3)) / 3), r, true));
        listeMissions.add(new Mission(Mission.TypeMission.DEGATS_SUBIS, Mission.Portee.PERSO, 4 * degre, recompenses, p, false));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 30 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRET, 1 + ((degre - (degre % 3)) / 3), r, true));
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 10 * degre));
        listeMissions.add(new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.PERSO, 3 * degre, recompenses, p, false));

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, r), 40 * degre));
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, r), (p.isHeros() ? degre * 5 : degre)));
        listeMissions.add(new Mission(Mission.TypeMission.COMPLETER_NIV, Mission.Portee.PERSO, 2 * degre, recompenses, p, false));

        min = 0;
        max = listeMissions.size() - 1;

        for (int i = 0; i < nbr; i++) {
            int num = (int) ((Math.random() * (max - min + 1)) + min);
            res.add(listeMissions.get(num).copierMission());
        }

        return res;
    }

    private Mission genereMissionDgOuSoin(Perso p, int nbrTtl, ArrayList<Recompense> recompenses) {
        Mission m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, 0, recompenses);
        if (p.getDg() > 0) {
            m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, nbrTtl * p.getDg(), recompenses, p, false);
        } else {
            if (p instanceof Medecin) {
                int soin = ((Medecin) p).getSoin();
                m = new Mission(Mission.TypeMission.SOIN, Mission.Portee.PERSO, nbrTtl * soin, recompenses, p, false);
            }
        }
        return m;
    }

    private Mission genereMissionDeplacement(Perso p, int nbrTtl, ArrayList<Recompense> recompenses, Mission.TypeMission typeAutre) {
        Mission m;
        if (p.getVitesse() <= 0) {
            m = genereAlternativeMission(p, nbrTtl, recompenses, typeAutre);
        } else {
            if (p.getVitesse() < 2) {
                m = new Mission(Mission.TypeMission.NBR_DEPLACEMENTS, Mission.Portee.PERSO, nbrTtl, recompenses, p, false);
            } else {
                m = new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.PERSO, nbrTtl * p.getVitesse(), recompenses, p, false);
            }
        }
        return m;
    }

    private Mission genererMissionCapacite(Perso p, int nbrTtl, int idCapa, ArrayList<Recompense> recompenses, Mission.TypeMission typeAutre) {
        Mission m;
        // selon le perso
        if (p instanceof LeCoureur) {
            if (idCapa == 1) {
                m = new Mission(Mission.TypeMission.CASES_PARCOURUES, Mission.Portee.PERSO, nbrTtl * 2, recompenses, p, true);
            } else {
                m = new Mission(Mission.TypeMission.NBR_DEPLACEMENTS, Mission.Portee.PERSO, nbrTtl, recompenses, p, true);
            }
        } else if (p instanceof Barbare) {
            m = new Mission(Mission.TypeMission.ENCHANTEMENT, Mission.Portee.PERSO, nbrTtl, recompenses, p, true);
        } else if (p instanceof Demon) {
            m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, nbrTtl, recompenses, p, true);
        } else if (p instanceof MiniBIG) {
            m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, nbrTtl * 2, recompenses, p, true);
        } else if (p instanceof MaitreDesSabres) {
            if (idCapa == 1) {
                m = new Mission(Mission.TypeMission.DEGATS, Mission.Portee.PERSO, nbrTtl * 2, recompenses, p, true);
            } else {
                m = new Mission(Mission.TypeMission.ELIMINATIONS, Mission.Portee.PERSO, nbrTtl, recompenses, p, true);
            }
        } else if (p instanceof Vampire) {
            m = new Mission(Mission.TypeMission.SOIN, Mission.Portee.PERSO, nbrTtl, recompenses, p, true);
        } else {
            m = genereAlternativeMission(p, nbrTtl, recompenses, typeAutre);
        }
        return m;
    }

    private Mission genereAlternativeMission(Perso p, int nbrTtl, ArrayList<Recompense> recompenses, Mission.TypeMission typeAutre) {
        Mission m;
        switch (typeAutre) {
            case CASES_PARCOURUES:
            case NBR_DEPLACEMENTS:
                m = genereMissionDeplacement(p, nbrTtl, recompenses, Mission.TypeMission.COMPLETER_NIV);
                if (p.getVitesse() < 1 && (m.getTypeMission().equals(Mission.TypeMission.NBR_DEPLACEMENTS) || m.getTypeMission().equals(Mission.TypeMission.CASES_PARCOURUES))) {
                    m = new Mission(Mission.TypeMission.COMPLETER_NIV, Mission.Portee.PERSO, (nbrTtl - (nbrTtl % 10)) / 10, recompenses, p, false);
                }
                break;
            case DEGATS:
                m = genereMissionDgOuSoin(p, nbrTtl, recompenses);
                break;
            case JOUER_NIV:
                m = new Mission(typeAutre, Mission.Portee.PERSO, (nbrTtl - (nbrTtl % 5)) / 5, recompenses, p, false);
                break;
            case ELIMINATIONS:
                if (p.getDg() < 1) {
                    m = new Mission(Mission.TypeMission.JOUER_NIV, Mission.Portee.PERSO, (nbrTtl - (nbrTtl % 6)) / 6, recompenses, p, false);
                } else {
                    m = new Mission(typeAutre, Mission.Portee.PERSO, nbrTtl, recompenses, p, false);
                }
                break;
            default:
                m = new Mission(typeAutre, Mission.Portee.PERSO, nbrTtl, recompenses, p, false);
        }
        return m;
    }

    public ArrayList<Mission> gererMissions(Niveau niveau, ArrayList<Mission> toutesLesMissions, HashMap<Perso, Compteur> tousLesCompteurs, boolean victoire) {

        ArrayList<Mission> missionsTraitees = new ArrayList<>();
        int nbr;

        for (Mission m : toutesLesMissions) {
            nbr = retrouveInfoProgression(m, niveau, tousLesCompteurs, victoire);
            if (nbr > 0) {
                m.addProgression(nbr);
                missionsTraitees.add(m);
            }
        }

        return missionsTraitees;
    }

    private int retrouveInfoProgression(Mission m, Niveau n, HashMap<Perso, Compteur> tousLesCompteurs, boolean victoire) {
        int nbr = 0;
        Mission.TypeMission type = m.getTypeMission();
        Mission.Portee portee = m.getPorteeMission();
        boolean avecSuper = m.isAvecSuper();

        // selon le type de la mission, aller chercher l'info que l'on recherche dans les compteurs
        switch (portee) {
            case REGION:
                if (n.getRegion().equals(m.getRegion())) {
                    for (Perso p : tousLesCompteurs.keySet()) {
                        nbr += tousLesCompteurs.get(p).getSelonType(type);
                    }
                }
                break;
            case GLOBALE:
                for (Perso p : tousLesCompteurs.keySet()) {
                    nbr += tousLesCompteurs.get(p).getSelonType(type);
                }
                if (type.equals(Mission.TypeMission.DIDACTITEL)) {
                    nbr = 1;
                }
                break;
            case PERSO:
                if (tousLesCompteurs.containsKey(m.getPerso())) {
                    // si la mission etait de jouer un niveau, incrementer sa progression
                    if (type.equals(Mission.TypeMission.JOUER_NIV)) {
                        nbr = 1;
                    } else if (type.equals(Mission.TypeMission.COMPLETER_NIV) && victoire) {
                        nbr = 1;
                    } else {
                        if (avecSuper) {
                            nbr = tousLesCompteurs.get(m.getPerso()).getSelonType(type, Compteur.Source.SUPER);
                        } else {
                            nbr = tousLesCompteurs.get(m.getPerso()).getSelonType(type);
                        }
                    }
                }
                break;
        }

        // si la mission etait de jouer un niveau, incrementer sa progression
        if (!portee.equals(Mission.Portee.PERSO)) {
            if (type.equals(Mission.TypeMission.JOUER_NIV)) {
                nbr = 1;
            } else if (type.equals(Mission.TypeMission.COMPLETER_NIV) && victoire) {
                nbr = 1;
            }
        }

        return nbr;
    }

    private MainCtrl mainCtrl;
    private final ArrayList<Mission> listeMissionsDidacticiel;
    private NiveauxWrk wrkNiveaux;
}
