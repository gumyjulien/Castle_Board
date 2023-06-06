/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gumyj
 */
public class Compteur {

    public Compteur() {
        this.nombreDeplacements = new HashMap<>();
        nombreDeplacements.put(Source.CLASSIQUE, 0);
        nombreDeplacements.put(Source.SUPER, 0);
        nombreDeplacements.put(Source.SOUS_FORME, 0);
        this.nombreCasesParcourues = new HashMap<>();
        nombreCasesParcourues.put(Source.CLASSIQUE, 0);
        nombreCasesParcourues.put(Source.SUPER, 0);
        nombreCasesParcourues.put(Source.SOUS_FORME, 0);
        this.nombreAmel = 0;
        this.nombreCapaActivees = 0;
        this.nombreDgInfliges = new HashMap<>();
        nombreDgInfliges.put(Source.CLASSIQUE, 0);
        nombreDgInfliges.put(Source.SUPER, 0);
        nombreDgInfliges.put(Source.SOUS_FORME, 0);
        this.nombreDgSubis = new HashMap<>();
        nombreDgSubis.put(Source.CLASSIQUE, 0);
        nombreDgSubis.put(Source.SUPER, 0);
        nombreDgSubis.put(Source.SOUS_FORME, 0);
        this.nombreSoinRecu = new HashMap<>();
        nombreSoinRecu.put(Source.CLASSIQUE, 0);
        nombreSoinRecu.put(Source.SUPER, 0);
        nombreSoinRecu.put(Source.SOUS_FORME, 0);
        this.nombreSoinProcure = new HashMap<>();
        nombreSoinProcure.put(Source.CLASSIQUE, 0);
        nombreSoinProcure.put(Source.SUPER, 0);
        nombreSoinProcure.put(Source.SOUS_FORME, 0);
        this.nombreEnchantements = new HashMap<>();
        nombreEnchantements.put(Source.CLASSIQUE, 0);
        nombreEnchantements.put(Source.SUPER, 0);
        nombreEnchantements.put(Source.SOUS_FORME, 0);
        this.ennemisVaincus = new HashMap<>();
        ennemisVaincus.put(Source.CLASSIQUE, new ArrayList<>());
        ennemisVaincus.put(Source.SUPER, new ArrayList<>());
        ennemisVaincus.put(Source.SOUS_FORME, new ArrayList<>());
    }
    
    public int getSelonType(Mission.TypeMission type) {
        int nbr = 0;
        switch(type) {
            case ACTIVER_CAPACITE:
                nbr = getNombreCapaActivees();
                break;
            case CASES_PARCOURUES:
                nbr = getNombreCasesParcourues();
                break;
            case DEGATS:
                nbr = getNombreDgInfliges();
                break;
            case DEGATS_SUBIS:
                nbr = getNombreDgSubis();
                break;
            case ELIMINATIONS:
                nbr = getEnnemisVaincus().size();
                break;
            case ENCHANTEMENT:
                nbr = getNombreEnchantements();
                break;
            case NBR_DEPLACEMENTS:
                nbr = getNombreDeplacements();
                break;
            case SOIN:
                nbr = getNombreSoinProcure();
                break;
        }
        return nbr;
    }
    
    public int getSelonType(Mission.TypeMission type, Source src) {
        int nbr = 0;
        switch(type) {
            case ACTIVER_CAPACITE:
                nbr = getNombreCapaActivees();
                break;
            case CASES_PARCOURUES:
                nbr = getNombreCasesParcourues(src);
                break;
            case DEGATS:
                nbr = getNombreDgInfliges(src);
                break;
            case DEGATS_SUBIS:
                nbr = getNombreDgSubis(src);
                break;
            case ELIMINATIONS:
                nbr = getEnnemisVaincus(src).size();
                break;
            case ENCHANTEMENT:
                nbr = getNombreEnchantements(src);
                break;
            case NBR_DEPLACEMENTS:
                nbr = getNombreDeplacements(src);
                break;
            case SOIN:
                nbr = getNombreSoinProcure(src);
                break;
        }
        return nbr;
    }

    public int getNombreDeplacements() {
        int res = 0;
        for (int nbr : nombreDeplacements.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreDeplacements(Source src) {
        return nombreDeplacements.get(src);
    }

    public void ajouteNombreDeplacements(int nbr, Source src) {
        this.nombreDeplacements.replace(src, this.nombreDeplacements.get(src) + nbr);
    }

    public int getNombreCasesParcourues() {
        int res = 0;
        for (int nbr : nombreCasesParcourues.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreCasesParcourues(Source src) {
        return nombreCasesParcourues.get(src);
    }

    public void ajouteNombreCasesParcourues(int nbr, Source src) {
        this.nombreCasesParcourues.replace(src, this.nombreCasesParcourues.get(src) + nbr);
    }

    public int getNombreAmel() {
        return nombreAmel;
    }

    public void ajouteNombreAmel(int nombreAmel) {
        this.nombreAmel += nombreAmel;
    }

    public int getNombreCapaActivees() {
        return nombreCapaActivees;
    }

    public void ajouteCapaActivees(int nbr) {
        this.nombreCapaActivees += nbr;
    }

    public int getNombreDgInfliges() {
        int res = 0;
        for (int nbr : nombreDgInfliges.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreDgInfliges(Source src) {
        return nombreDgInfliges.get(src);
    }

    public void ajouteNombreDgInfliges(int nbr, Source src) {
        this.nombreDgInfliges.replace(src, this.nombreDgInfliges.get(src) + nbr);
    }

    public int getNombreDgSubis() {
        int res = 0;
        for (int nbr : nombreDgSubis.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreDgSubis(Source src) {
        return nombreDgSubis.get(src);
    }

    public void ajouteNombreDgSubis(int nbr, Source src) {
        if (src != null) {
            this.nombreDgSubis.replace(src, this.nombreDgSubis.get(src) + nbr);
        }
    }

    public int getNombreSoinRecu() {
        int res = 0;
        for (int nbr : nombreSoinRecu.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreSoinRecu(Source src) {
        return nombreSoinRecu.get(src);
    }

    public void ajouteNombreSoinRecu(int nbr, Source src) {
        if (src != null) {
            this.nombreSoinRecu.replace(src, this.nombreSoinRecu.get(src) + nbr);
        }
    }

    public int getNombreSoinProcure() {
        int res = 0;
        for (int nbr : nombreSoinProcure.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreSoinProcure(Source src) {
        return nombreSoinProcure.get(src);
    }

    public void ajouteNombreSoinProcure(int nbr, Source src) {
        this.nombreSoinProcure.replace(src, this.nombreSoinProcure.get(src) + nbr);
    }

    public int getNombreEnchantements() {
        int res = 0;
        for (int nbr : nombreEnchantements.values()) {
            res += nbr;
        }
        return res;
    }

    public int getNombreEnchantements(Source src) {
        return nombreEnchantements.get(src);
    }

    public void ajouteNombreEnchantements(int nbr, Source src) {
        this.nombreEnchantements.replace(src, this.nombreEnchantements.get(src) + nbr);
    }

    public ArrayList<Perso> getEnnemisVaincus() {
        ArrayList<Perso> res = new ArrayList<>();
        for (ArrayList<Perso> val : ennemisVaincus.values()) {
            res.addAll(val);
        }
        return res;
    }

    public ArrayList<Perso> getEnnemisVaincus(Source src) {
        ArrayList<Perso> res = new ArrayList<>();
        res.addAll(ennemisVaincus.get(src));
        return res;
    }

    public void ajouteEnnemiVaincu(Perso ennemiVaincu, Source sourceUtilisee) {
        this.ennemisVaincus.get(sourceUtilisee).add(ennemiVaincu);
    }

    private HashMap<Source, Integer> nombreCasesParcourues;
    private HashMap<Source, Integer> nombreDeplacements;
    private int nombreAmel;
    private int nombreCapaActivees;
    private HashMap<Source, Integer> nombreDgInfliges;
    private HashMap<Source, Integer> nombreDgSubis;
    private HashMap<Source, Integer> nombreSoinRecu;
    private HashMap<Source, Integer> nombreSoinProcure;
    private HashMap<Source, Integer> nombreEnchantements;
    private HashMap<Source, ArrayList<Perso>> ennemisVaincus;
    

    public enum Source {
        CLASSIQUE,
        SUPER,
        SOUS_FORME
    }

}
