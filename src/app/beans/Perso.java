/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import app.beans.persos.joueur.Nain;
import app.beans.persos.joueur.Abeille;
import app.beans.persos.joueur.AbeilleCostaude;
import app.beans.persos.joueur.Archere;
import app.beans.persos.joueur.Bandit;
import app.beans.persos.joueur.LeCoureur;
import app.workers.WorkerItf;
import app.workers.WorkerJeu;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;

/**
 *
 * @author gumyj
 */
public abstract class Perso implements Serializable, Cloneable {

    /**
     * Constrcteur utilisé pour un perso classique, sans super.
     */
    public Perso(int pv, int dg, int vitesse, int portee, TypeDeplacement typeDeplacement, TypeAttaque typeAttaque, int camp, String nom, boolean effetAmi, boolean isHeros, String[] titreAmel, String[] descAmel, String description) {
        this.wrk = WorkerJeu.getInstance();
        this.pv = pv;
        this.pvMax = pv;
        this.dg = dg;
        this.vitesse = vitesse;
        this.portee = portee;
        this.typeDeplacement = typeDeplacement;
        this.typeAttaque = typeAttaque;
        this.protection = 0;
        this.nmbrEtoiles = 0;
        this.jetonsPuissance = 0;
        this.superReady = false;
        this.enchantement = null;
        this.dureeEnchRestante = 0;
        this.camp = camp;
        this.nom = nom;
        this.effetAmi = effetAmi;
        this.isHeros = isHeros;
        this.jetonsPuissanceRequis = -1;
        this.evenementsPuissance = new ArrayList<>();
        this.instantSuper = false;
        this.lvlRequisSuper = -1;
        this.titreSuper = null;
        this.texteSuper = null;
        this.description = description;
        this.titreAmeliorations = titreAmel;
        this.descAmeliorations = descAmel;
        this.formeInitiale = true;
        this.tousLesCostumes = new ArrayList<>();
        this.tousLesCostumes.add(new Costume(this, "Default"));
        this.costumeSelec = this.tousLesCostumes.get(0);
    }

    /**
     * Constructeur utilisé pour les persos sans super avec déplacement et
     * attaque banales.
     */
    public Perso(int pv, int dg, int vitesse, int portee, int camp, String nom, boolean effetAmi, boolean isHeros, String[] titreAmel, String[] descAmel, String description) {
        this(pv, dg, vitesse, portee, TypeDeplacement.CLASSIQUE, TypeAttaque.MELEE, camp, nom, effetAmi, isHeros, titreAmel, descAmel, description);
    }

    /**
     * Constructeur utilisé pour un perso avec un super.
     */
    public Perso(int pv, int dg, int vitesse, int portee, TypeDeplacement typeDeplacement, TypeAttaque typeAttaque, int camp, String nom, boolean effetAmi, boolean isHeros, int jetonsPuissanceRequis, ArrayList<TypeAction> evenementsPuissance, boolean instantSuper, int nivDeblSuper, String titreSuper, String texteSuper, String[] titreAmel, String[] descAmel, String description) {
        this(pv, dg, vitesse, portee, typeDeplacement, typeAttaque, camp, nom, effetAmi, isHeros, titreAmel, descAmel, description);
        this.jetonsPuissanceRequis = jetonsPuissanceRequis;
        this.evenementsPuissance = evenementsPuissance;
        this.instantSuper = instantSuper;
        this.lvlRequisSuper = nivDeblSuper;
        this.titreSuper = titreSuper;
        this.texteSuper = texteSuper;
    }

    /**
     * Constructeur utilisé pour les persos avec super avec déplacement et
     * attaque banales.
     */
    public Perso(int pv, int dg, int vitesse, int portee, int camp, String nom, boolean effetAmi, boolean isHeros, int jetonsPuissanceRequis, ArrayList<TypeAction> evenementsPuissance, boolean instantSuper, int nivDeblSuper, String titreSuper, String texteSuper, String[] titreAmel, String[] descAmel, String description) {
        this(pv, dg, vitesse, portee, TypeDeplacement.CLASSIQUE, TypeAttaque.MELEE, camp, nom, effetAmi, isHeros, jetonsPuissanceRequis, evenementsPuissance, instantSuper, nivDeblSuper, titreSuper, texteSuper, titreAmel, descAmel, description);
    }

    public abstract Perso copierPerso();

    public void guerirPV(int nbr, Perso source, Compteur.Source src) {
        TranslateTransition transition;
        if (wrk == null) {
            wrk = WorkerJeu.getInstance();
        }
        // si la guérison dépasse le maximum de pv, il s'arrête au max
        if (pv + nbr > pvMax) {
            transition = wrk.createAnimationLabel(this, source, src, null, null, 100, Color.LIGHTGREEN, (pvMax - pv));
            pv = pvMax;
        } else {
            // sinon, il guérit tout simplement
            pv += nbr;
            transition = wrk.createAnimationLabel(this, source, src, null, null, 100, Color.LIGHTGREEN, nbr);
        }
        transition.play();
    }

    public void subirDG(int nbr, Perso source, Compteur.Source src) {
        if (this instanceof Nain) {
            nbr *= 1.5;
        } else if(this instanceof Bandit && nmbrEtoiles >= 3) {
            if(nbr == 1) {
                nbr = 0;
            }
        }

        if (protection >= nbr) {
            // affaiblir la protection
            protection -= nbr;
        } else {
            // subir les dégâts moins la protection
            pv -= (nbr - protection);
            protection = 0;
        }
        if (wrk == null) {
            wrk = WorkerJeu.getInstance();
        }
        wrk.createAnimationLabel(this, source, src, null, null, 100, Color.RED, nbr);
    }

    public int getPv() {
        return pv;
    }

    public double getPvPourcentage() {
        return (((double) pv / (double) pvMax) * (double) 100);
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getDg() {
        return dg;
    }

    public void setDg(int dg) {
        this.dg = dg;
    }

    public int getVitesse() {
        return vitesse;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    public int getPortee() {
        return portee;
    }

    public void setPortee(int portee) {
        this.portee = portee;
    }

    public int getPvMax() {
        return pvMax;
    }

    public void setPvMax(int pvMax) {
        this.pvMax = pvMax;
    }

    public int getProtection() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public int getJetonsPuissance() {
        int retour = -1;
        if (this.jetonsPuissanceRequis != -1) {
            retour = this.jetonsPuissance;
        }
        return retour;
    }

    public void ajouteJetonsPuissance(int jetonsPuissance) {
        this.jetonsPuissance += jetonsPuissance;
    }

    public int getJetonsPuissanceRequis() {
        return jetonsPuissanceRequis;
    }

    public void setJetonsPuissanceRequis(int jetonsPuissanceRequis) {
        this.jetonsPuissanceRequis = jetonsPuissanceRequis;
    }

    public boolean isSuperReady() {
        return superReady;
    }

    public void setSuperReady(boolean superReady) {
        this.superReady = superReady;
    }

    public WorkerJeu getWrk() {
        return wrk;
    }

    public int getLvlRequisSuper() {
        return lvlRequisSuper;
    }

    public String getTitreSuper() {
        return titreSuper;
    }

    public void reinitialiserJetonsPuissance() {
        this.jetonsPuissance = 0;
        this.superReady = false;
    }

    public ArrayList<TypeAction> getEvenementsPuissance() {
        return evenementsPuissance;
    }

    public boolean isInstantSuper() {
        return instantSuper;
    }

    public String getTexteSuper() {
        return texteSuper;
    }

    public void ajouterEvenementPuissance(TypeAction action) {
        evenementsPuissance.add(action);
    }

    public void viderEvenementPuissance() {
        evenementsPuissance.clear();
    }

    public String getEnchantementFormat() {
        String retour = "";
        if (this.enchantement != null) {
            retour = this.enchantement.name().substring(0, 1).toUpperCase() + this.enchantement.name().substring(1).toLowerCase();
        }
        return retour;
    }

    public Enchantement getEnchantement() {
        return enchantement;
    }

    public void setEnchantement(Enchantement enchantement, int duree) {
        if (this.enchantement == Enchantement.FORCE) {
            this.dg--;
        }
        this.enchantement = enchantement;
        this.dureeEnchRestante = duree;
        if (this.enchantement == Enchantement.FORCE) {
            this.dg++;
        }
    }

    public void setEnchantement(String enchantement, int duree) {
        Enchantement ench = null;
        switch (enchantement) {
            case "Etourdi":
                ench = Enchantement.ETOURDI;
                break;
            case "Saignement":
                ench = Enchantement.SAIGNEMENT;
                break;
            case "Couronne":
                ench = Enchantement.COURONNE;
                break;
            case "Poison":
                ench = Enchantement.POISON;
                break;
            case "Force":
                ench = Enchantement.FORCE;
                break;
        }
        this.setEnchantement(ench, duree);
    }

    public int getNmbrEtoiles() {
        return nmbrEtoiles;
    }

    public void setNmbrEtoiles(int nmbrEtoiles) {
        this.nmbrEtoiles = nmbrEtoiles;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public String getNom() {
        return nom;
    }

    public boolean canEffetAmi() {
        return effetAmi;
    }

    public void setCanEffetAmi(boolean effetAmi) {
        this.effetAmi = effetAmi;
    }

    public boolean isHeros() {
        return isHeros;
    }

    public int getDureeEnchRestante() {
        return dureeEnchRestante;
    }

    public String getDescription() {
        return description;
    }

    public String getTitreAmel(int index) {
        String retour = "Niveau max.";
        if (index < titreAmeliorations.length) {
            retour = titreAmeliorations[index];
        }
        return retour;
    }

    public String getDescAmel(int index) {
        String retour = "Niveau max.";
        if (index < descAmeliorations.length) {
            retour = descAmeliorations[index];
        }
        return retour;
    }

    public String[] getTitreAmeliorations() {
        return titreAmeliorations;
    }

    public String[] getDescAmeliorations() {
        return descAmeliorations;
    }

    public TypeDeplacement getTypeDeplacement() {
        return typeDeplacement;
    }

    public void setTypeDeplacement(TypeDeplacement typeDeplacement) {
        this.typeDeplacement = typeDeplacement;
    }

    public TypeAttaque getTypeAttaque() {
        return typeAttaque;
    }

    public void setTypeAttaque(TypeAttaque typeAttaque) {
        this.typeAttaque = typeAttaque;
    }
    
    public void setActionSuivante(TypeAction actionSuivante, TypeAction actionRequise) {
        this.actionSuivante = actionSuivante;
        this.actionRequise = actionRequise;
    }

    public TypeAction getActionSuivante() {
        return actionSuivante;
    }

    public TypeAction getActionRequise() {
        return actionRequise;
    }

    public boolean isFormeInitiale() {
        return formeInitiale;
    }

    public void setFormeInitiale(boolean formeInitiale) {
        this.formeInitiale = formeInitiale;
    }

    public Perso getSousForme() {
        return sousForme;
    }

    public void setSousForme(Perso sousForme) {
        this.sousForme = sousForme;
    }

    public Perso getSuperForme() {
        return superForme;
    }

    public void setSuperForme(Perso superForme) {
        this.superForme = superForme;
    }

    public void finDuTour() {
        // s'il y a un enchantement, diminuer sa durée restante
        if (enchantement != null) {
            this.dureeEnchRestante--;
        }
        // si la durée restante est égale à 0, on enlève l'enchantement
        if (this.dureeEnchRestante == 0) {
            if (enchantement == Enchantement.FORCE) {
                this.dg--;
            }
            this.enchantement = null;
        }

        // si c'est une abeille, lui permettre de jouer au prochain tour
        if (this instanceof Abeille) {
            ((Abeille) this).setDejaJoue(false);
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        if (o != null) {
            Perso p = (Perso) o;
            if (p.getNom().equals(this.nom) && p.getCamp() == this.camp) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.camp;
        hash = 73 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    public abstract Perso ameliorer();

    public ArrayList<Costume> getTousLesCostumes() {
        return tousLesCostumes;
    }
    
    public Costume getCostumeParNom(String nom) {
        Costume res = tousLesCostumes.get(0);
        for (Costume c : tousLesCostumes) {
            if(c.getNom().equals(nom)) {
                res = c;
                break;
            }
        }
        return res;
    }

    public void ajouteCostume(Costume c) {
        this.tousLesCostumes.add(c);
    }

    public Costume getCostumeSelec() {
        return costumeSelec;
    }

    public void setCostumeSelec(Costume costumeSelec) {
        this.costumeSelec = costumeSelec;
    }
    public void majSousForme() {
    }
    
    public ArrayList<Mission> getMissionsBadge() {
        ArrayList<Mission> res = new ArrayList<>();
        return res;
    }
    
    public Recompense[] getRecsBadge() {
        Recompense[] res = new Recompense[6];
        res[0] = new Recompense(Recompense.ObjRec.PIECES, 200);
        res[1] = new Recompense(Recompense.ObjRec.COFFRET, 2, null, false);
        res[2] = new Recompense(Recompense.ObjRec.PIECES, 500);
        res[3] = new Recompense(Recompense.ObjRec.GRAND_COFFRE, null, false);
        res[4] = new Recompense(Recompense.ObjRec.RUBIS, 250);
        res[5] = new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, false);
        return res;
    }

    private transient WorkerJeu wrk;
    private int pv;
    private int pvMax;
    private int dg;
    private int vitesse;
    private int portee;
    private int protection;
    private int nmbrEtoiles;
    private int camp;
    private int jetonsPuissance;
    private boolean superReady;
    private Enchantement enchantement;
    private int dureeEnchRestante;
    private final String nom;
    private boolean effetAmi;
    private final boolean isHeros;
    private transient final String description;
    private transient final String[] titreAmeliorations;
    private transient final String[] descAmeliorations;
    private ArrayList<TypeAction> evenementsPuissance;
    private boolean instantSuper;
    private int lvlRequisSuper;
    private transient String titreSuper;
    private transient String texteSuper;
    private int jetonsPuissanceRequis;
    private TypeDeplacement typeDeplacement;
    private TypeAttaque typeAttaque;
    private TypeAction actionSuivante;
    private TypeAction actionRequise;
    
    private ArrayList<Costume> tousLesCostumes;
    private Costume costumeSelec;
    
    private boolean formeInitiale;
    private Perso sousForme;
    private Perso superForme;

    public enum TypeDeplacement {
        CLASSIQUE,
        COTES,
        COINS,
        LOSANGES
    }

    public enum TypeAttaque {
        MELEE,
        A_DISTANCE,
        SPECIALE
    }
}
