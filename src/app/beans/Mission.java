/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class Mission implements Serializable, Comparable<Mission> {

    public enum TypeMission {
        DEGATS,
        RECORD_DEGATS,
        DEGATS_SUBIS,
        SOIN,
        ELIMINATIONS,
        ENCHANTEMENT,
        NBR_DEPLACEMENTS,
        CASES_PARCOURUES,
        JOUER_NIV,
        COMPLETER_NIV,
        RECUP_RESSOURCE,
        DEBLOQUER_PERSO,
        ACTIVER_CAPACITE,
        DIDACTITEL,
        TERMINER_MISSION,
        DECOUVRIR_REGION
    }

    public enum Portee {
        GLOBALE,
        REGION,
        PERSO
    }

    /**
     * Constructeur utilisé pour une mission globale.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses) {
        this.typeMission = typeMission;
        this.porteeMission = porteeMission;
        this.nbr = nbr;
        this.progression = 0;
        this.recompenses = recompenses;
        this.missionSuivante = null;
        this.numEtape = 1;
        this.etapeMax = 1;
        this.terminee = false;
        this.avecSousForme = false;
    }

    /**
     * Constructeur utilisé pour une mission globale à étapes.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, ArrayList<Mission> missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses);
        this.missionSuivante = missionSuivante;
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    /**
     * Constructeur utilisé pour une mission globale à une seule étape.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Mission missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses);
        this.missionSuivante = new ArrayList<>();
        this.missionSuivante.add(missionSuivante);
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    /**
     * Constructeur utilisé pour une mission de région.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Region region) {
        this(typeMission, porteeMission, nbr, recompenses);
        this.region = region;
    }

    /**
     * Constructeur utilisé pour une mission de région à étapes.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Region region, ArrayList<Mission> missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses, region);
        this.missionSuivante = missionSuivante;
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    /**
     * Constructeur utilisé pour une mission de région à une seule étape.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Region region, Mission missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses, region);
        this.missionSuivante = new ArrayList<>();
        this.missionSuivante.add(missionSuivante);
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    /**
     * Constructeur utilisé pour une mission de perso.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Perso perso, boolean avecSuper) {
        this(typeMission, porteeMission, nbr, recompenses);
        this.perso = perso;
        this.avecSuper = avecSuper;
    }

    /**
     * Constructeur utilisé pour une mission de perso à étapes.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Perso perso, boolean avecSuper, ArrayList<Mission> missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses, perso, avecSuper);
        this.missionSuivante = missionSuivante;
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    /**
     * Constructeur utilisé pour une mission de perso à une seule étape.
     */
    public Mission(TypeMission typeMission, Portee porteeMission, int nbr, ArrayList<Recompense> recompenses, Perso perso, boolean avecSuper, Mission missionSuivante, int numEtape, int etapeMax) {
        this(typeMission, porteeMission, nbr, recompenses, perso, avecSuper);
        this.missionSuivante = new ArrayList<>();
        this.missionSuivante.add(missionSuivante);
        this.numEtape = numEtape;
        this.etapeMax = etapeMax;
    }

    public Mission copierMission() {
        Mission m = new Mission(typeMission, porteeMission, nbr, recompenses);
        boolean aEtapes = (this.missionSuivante != null);

        switch (porteeMission) {
            case GLOBALE:
                if (aEtapes) {
                    m = new Mission(typeMission, porteeMission, nbr, recompenses, missionSuivante, numEtape, etapeMax);
                }
                break;
            case REGION:
                if (aEtapes) {
                    m = new Mission(typeMission, porteeMission, nbr, recompenses, region, missionSuivante, numEtape, etapeMax);
                } else {
                    m = new Mission(typeMission, porteeMission, nbr, recompenses, region);
                }
                break;
            case PERSO:
                if (aEtapes) {
                    m = new Mission(typeMission, porteeMission, nbr, recompenses, perso, avecSuper, missionSuivante, numEtape, etapeMax);
                } else {
                    m = new Mission(typeMission, porteeMission, nbr, recompenses, perso, avecSuper);
                }
                break;
        }

        return m;
    }

    public TypeMission getTypeMission() {
        return typeMission;
    }

    public Portee getPorteeMission() {
        return porteeMission;
    }

    public ArrayList<Mission> getMissionSuivante() {
        return missionSuivante;
    }

    public int getNumEtape() {
        return numEtape;
    }

    public int getEtapeMax() {
        return etapeMax;
    }

    public Region getRegion() {
        return region;
    }

    public int getNbrObjectif() {
        return nbr;
    }

    public void coeffNbrObjectif(int coeff) {
        this.nbr *= coeff;
    }

    public void coeffRecompenses(int coeff) {
        int coeffCoffre = (coeff - (coeff % 2)) / 2;
        for (Recompense r : recompenses) {
            if (r.estUnCoffre()) {
                r.coeffQuantite(coeffCoffre);
            } else {
                r.coeffQuantite(coeff);
            }
        }
    }

    public ArrayList<Recompense> getRecompenses() {
        return recompenses;
    }

    public Perso getPerso() {
        return perso;
    }

    public boolean isTerminee() {
        return terminee;
    }

    public void setTerminee(boolean terminee) {
        this.terminee = terminee;
    }

    public boolean isAvecSuper() {
        return avecSuper;
    }

    public void setPerso(Perso perso) {
        this.perso = perso;
    }

    public void setNumEtape(int numEtape) {
        this.numEtape = numEtape;
    }

    public void setEtapeMax(int etapeMax) {
        this.etapeMax = etapeMax;
    }

    public int getProgression() {
        return progression;
    }

    public double getProgressionPourcentage() {
        double res;
        res = (double) progression / (double) nbr;
        return res;
    }

    public void setProgression(int progression) {
        this.progression = progression;
        if (progression >= this.nbr) {
            this.terminee = true;
        }
    }

    public void addProgression(int nbr) {
        this.progression += nbr;
        if (progression >= this.nbr) {
            this.terminee = true;
        }
    }

    public boolean isPartieUnique() {
        return partieUnique;
    }

    public void setPartieUnique(boolean partieUnique) {
        this.partieUnique = partieUnique;
    }

    public boolean isAvecSousForme() {
        return avecSousForme;
    }

    public void setAvecSousForme(boolean avecSousForme) {
        this.avecSousForme = avecSousForme;
    }

    @Override
    public int compareTo(Mission m) {
        int res = 0;
        int nombreTtlRecA = 0;
        int nombreTtlRecB = 0;
        for (Recompense rec : recompenses) {
            nombreTtlRecA += rec.getQuantite();
        }
        for (Recompense rec : m.getRecompenses()) {
            nombreTtlRecB += rec.getQuantite();
        }
        if (nombreTtlRecA > nombreTtlRecB) {
            res = 1;
        } else if (nombreTtlRecA < nombreTtlRecB) {
            res = -1;
        }
        return res;
    }

    @Override
    public String toString() {
        String res = "";
        boolean globale = porteeMission == Portee.GLOBALE;
        boolean avecPerso = porteeMission == Portee.PERSO;

        switch (typeMission) {
            case DIDACTITEL:
                res = "Terminer le didactitiel";
                break;
            case DEGATS:
                res = "Infliger un total de " + nbr + " dégât(s)";
                break;
            case DEGATS_SUBIS:
                res = "Subir un total de " + nbr + " dégât(s)";
                break;
            case SOIN:
                res = "Soigner un total de " + nbr + " PV";
                break;
            case ENCHANTEMENT:
                res = "Enchanter un total de " + nbr + " persos";
                break;
            case NBR_DEPLACEMENTS:
                res = "Se déplacer un total de " + nbr + " fois sur le plateau";
                break;
            case CASES_PARCOURUES:
                res = "Parcourir un total de " + nbr + " case(s) du plateau";
                break;
            case JOUER_NIV:
                res = "Jouer " + nbr + " niveau(x)";
                break;
            case COMPLETER_NIV:
                res = "Compléter " + nbr + " niveau(x)";
                break;
            case DEBLOQUER_PERSO:
                res = "Collectionner un total de " + nbr + " carte(s)";
                break;
            case RECUP_RESSOURCE:
                if (rec.estUnCoffre()) {
                    res = "Ouvrir un total de " + nbr + " " + rec.toString();
                } else {
                    res = "Collecter un total de " + nbr + " " + rec.toString();
                }
                break;
            case ACTIVER_CAPACITE:
                String nomSuper;
                if (perso.getLvlRequisSuper() > 0) {
                    nomSuper = perso.getTitreAmel(perso.getLvlRequisSuper() - 1);
                } else {
                    nomSuper = perso.getTitreSuper();
                }
                res = "Activer la capacité " + nomSuper + " un total de " + nbr + " fois";
                break;
            case ELIMINATIONS:
                res = "Éliminer un total de " + nbr + " ennemis";
                break;
        }
        if (avecPerso) {
            if (avecSuper) {
                String nomSuper;
                if (perso.getLvlRequisSuper() > 0) {
                    nomSuper = perso.getTitreAmel(perso.getLvlRequisSuper() - 1);
                } else {
                    nomSuper = perso.getTitreSuper();
                }
                res += " avec la capacité " + nomSuper;
            } else {
                if (typeMission.equals(TypeMission.JOUER_NIV) || typeMission.equals(TypeMission.COMPLETER_NIV)) {
                    res += " en utilisant votre " + perso.getNom();
                }
            }
        }

        if (partieUnique) {
            res += " en une seule partie";
        }

        return res;
    }

    public String miniToString() {
        String res = "";
        switch (typeMission) {
            case DEGATS:
                res = nbr + " dégâts infligés";
                break;
            case RECORD_DEGATS:
                res = "Record de dégâts infligés";
                break;
            case DEGATS_SUBIS:
                res = nbr + " dégâts subis";
                break;
            case SOIN:
                if(nbr == 1) {
                    res = "X PV soignés";
                } else {
                    res = nbr + " PV soignés";
                }
                break;
            case ENCHANTEMENT:
                res = nbr + " persos enchantés";
                break;
            case NBR_DEPLACEMENTS:
                res = nbr + " déplacements";
                break;
            case CASES_PARCOURUES:
                res = nbr + " cases parcourues";
                break;
            case JOUER_NIV:
                res = "Jouer une partie";
                break;
            case COMPLETER_NIV:
                res = "Compléter un niveau";
                break;
            case TERMINER_MISSION:
                res = "Compléter une mission";
                break;
            case ACTIVER_CAPACITE:
                String nomSuper;
                if (perso.getLvlRequisSuper() > 0) {
                    nomSuper = perso.getTitreAmel(perso.getLvlRequisSuper() - 1);
                } else {
                    nomSuper = perso.getTitreSuper();
                }
                res = (nbr > 1 ? (nbr + "x ") : "") + nomSuper;
                break;
            case ELIMINATIONS:
                res = "Éliminer " + nbr + " ennemis";
                break;
            case DECOUVRIR_REGION:
                res = "Découvrir une région";
                break;
        }

        if (nbr > 1 && partieUnique && !avecSousForme && !typeMission.equals(TypeMission.JOUER_NIV) && !typeMission.equals(TypeMission.COMPLETER_NIV) && !typeMission.equals(TypeMission.TERMINER_MISSION) && !typeMission.equals(TypeMission.DECOUVRIR_REGION)) {
            res += " en une partie";
        }

        if (avecSousForme) {
            Perso p = perso;
            while (p.getSousForme() != null) {
                p = p.getSousForme();
            }
            res += " avec ses " + p.getNom() + "s";
        }

        return res;
    }

    // pour toutes les missions
    private TypeMission typeMission;
    private Portee porteeMission;
    private int nbr;
    private int progression;
    private ArrayList<Recompense> recompenses;
    private ArrayList<Mission> missionSuivante;
    private int numEtape;
    private int etapeMax;
    private boolean terminee;

    // pour les missions de région
    private Region region;

    // pour les missions de ressources
    private Recompense rec;

    // missions de perso
    private Perso perso;
    private boolean avecSuper;
    private boolean avecSousForme;

    private boolean partieUnique;

}
