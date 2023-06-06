package app.workers;

import app.beans.CartePerso;
import app.beans.CaseItem;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.Direction;
import app.beans.persos.joueur.Abeille;
import app.beans.persos.joueur.AbeilleCostaude;
import app.beans.persos.joueur.ApiculteurRoyal;
import app.beans.persos.joueur.Archere;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.Belier;
import app.beans.persos.joueur.Demon;
import app.beans.Enchantement;
import app.beans.persos.joueur.LeCoureur;
import app.beans.persos.joueur.MaitreDesSabres;
import app.beans.persos.joueur.Medecin;
import app.beans.persos.joueur.MiniBIG;
import app.beans.persos.joueur.Nain;
import app.beans.Perso;
import app.beans.persos.joueur.Princesse;
import app.beans.persos.joueur.Ruche;
import app.beans.TypeAction;
import app.beans.persos.ennemi.Cactus;
import app.beans.persos.joueur.Scribe;
import app.beans.persos.joueur.Vampire;
import app.presentation.JeuCtrl;
import java.util.ArrayList;
import java.util.TreeMap;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Implémentation de la couche "métier" de l'application.
 *
 * @author gumyj01
 */
public class WorkerJeu {

    // static variable instancen 
    private static WorkerJeu instance = null;

    // relation avec l'usager
    private static JeuCtrl jeuCtrl = null;

    // private constructor  
    private WorkerJeu() {
        persosAToucherSuperArchere = new ArrayList<>();
    }

    // static method to create instance of WorkerJeu class 
    public static WorkerJeu getInstance() {
        if (instance == null) {
            instance = new WorkerJeu();
        }

        return instance;
    }

    public void setJeuCtrl(JeuCtrl jeuCtrl) {
        WorkerJeu.jeuCtrl = jeuCtrl;
    }

    public JeuCtrl getJeuCtrl() {
        return jeuCtrl;
    }

    /**
     * Génère la liste de toutes les actions possibles (déplacement, attaque,
     * enchantement, ...) avec le personnage placé en paramètre, selon sa
     * position sur le plateau.
     *
     * @param plateau -> le plateau avec lequel on va travailler
     * @param perso -> le personnage sélectionné pour l'action
     * @param indexI -> la position x du personnage sélectionné
     * @param indexJ -> la position y du personnage sélectionné
     * @param actionsConcernees
     * @return liste -> liste de toutes les actions possibles du personnage
     */
    public ArrayList<TypeAction> getToutesLesActions(Perso[][] plateau, Perso perso, int indexI, int indexJ, ArrayList<TypeAction> actionsConcernees) {
        // determiner les actions a scanner selon indique
        boolean scannerDeplacement = true;
        boolean scannerAttaque = true;
        boolean scannerEnchantement = true;

        if (!actionsConcernees.isEmpty()) {
            scannerAttaque = actionsConcernees.contains(TypeAction.ATTAQUE);
            scannerDeplacement = actionsConcernees.contains(TypeAction.DEPLACER);
            scannerEnchantement = actionsConcernees.contains(TypeAction.ENCHANTER);
        }

        // récupérer la portée et la vitesse du personnage
        int portee = perso.getPortee();
        int vitesse = perso.getVitesse() + 1;

        // créer la structure de données pour stocker les actions possibles
        ArrayList<TypeAction> listeActions = new ArrayList<>(plateau.length * plateau[0].length);

        // définir toutes les cases en tant que null
        for (int i = 0; i < plateau.length * plateau[0].length + 1; i++) {
            listeActions.add(null);
        }

        // déclarer les variables utiles pour la suite
        boolean peutEnHaut = true;
        boolean peutEnBas = true;
        boolean peutAGauche = true;
        boolean peutADroite = true;
        boolean peutCoinHautGauche;
        boolean peutCoinHautDroite;
        boolean peutCoinBasGauche;
        boolean peutCoinBasDroite;
        TypeAction attaque = TypeAction.ATTAQUE; // Attaquer si c'est une attaque normale, TypeAction.ATTAQUESPEC si c'en est une spéciale

        // déterminer le type d'attaque
        if (perso instanceof Barbare && perso.getNmbrEtoiles() >= 3) {
            attaque = TypeAction.ATTAQUESPEC;
        }

        // si le perso a 0 dégâts, il ne peut pas attaquer
        if (perso.getDg() <= 0) {
            attaque = null;
        }

        // définir si Le Coureur est à proximité, et donc que le perso peut se déplacer en diagonale
        boolean voisinCoureur = leCoureurEstMonVoisin(plateau, perso, indexI, indexJ);
        peutCoinHautGauche = voisinCoureur;
        peutCoinHautDroite = voisinCoureur;
        peutCoinBasGauche = voisinCoureur;
        peutCoinBasDroite = voisinCoureur;

        switch (perso.getTypeDeplacement()) {
            case COINS:
                peutEnHaut = false;
                peutEnBas = false;
                peutADroite = false;
                peutAGauche = false;
                peutCoinHautGauche = true;
                peutCoinHautDroite = true;
                peutCoinBasGauche = true;
                peutCoinBasDroite = true;
                break;
            case COTES:
                peutEnHaut = false;
                peutEnBas = false;
                peutADroite = true;
                peutAGauche = true;
                peutCoinHautGauche = false;
                peutCoinHautDroite = false;
                peutCoinBasGauche = false;
                peutCoinBasDroite = false;
                break;
        }

//        boolean[][] possibilites = new boolean[][]{{peutCoinHautGauche, peutEnHaut, peutCoinHautDroite}, {peutAGauche, false, peutADroite}, {peutCoinBasGauche, peutEnBas, peutCoinBasDroite}};
        boolean[][] possibilites = new boolean[][]{{peutCoinHautGauche, peutAGauche, peutCoinBasGauche}, {peutEnHaut, false, peutEnBas}, {peutCoinHautDroite, peutADroite, peutCoinBasDroite}};
        /*   -1  0  +1
          -1  -  X  -
           0  X  O  X
          +1  -  X  -
         */
        ArrayList<Integer> plagePoss = new ArrayList<>();
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            int caseJ = (i - (i % plateau.length)) / plateau.length;
            int caseI = i - caseJ * plateau.length;
            if ((caseI >= indexI - portee) && (caseI <= indexI + portee) && (caseJ >= indexJ - portee) && (caseJ <= indexJ + portee)) {
                if (!(caseI == indexI && caseJ == indexJ)) {
                    plagePoss.add(i);
                }
            }
        }

        // selon sa vitesse, determiner les deplacements possibles
        if (scannerDeplacement) {
            for (int vit = 1; vit < vitesse; vit++) {
                for (int i = -vit; i <= vit; i += vit) {
                    for (int j = -vit; j <= vit; j += vit) {
                        int indX = indexI + i;
                        int indY = indexJ + j;

                        int indexIPoss = i / vit + 1;
                        int indexJPoss = j / vit + 1;

                        // verifier si les valeurs sont sur le plateau
                        if (verifieCoordonees(plateau, indX, indY)) {
                            // verifier si le perso peut se rendre sur la case
                            if (possibilites[indexIPoss][indexJPoss]) {
                                // si la case est vide
                                if (plateau[indX][indY] == null) {
                                    // le perso peut donc s y rendre
                                    listeActions.set(indX + indY * plateau.length, TypeAction.DEPLACER);
                                } else {
                                    // sinon, empecher le perso d aller plus loin
                                    possibilites[indexIPoss][indexJPoss] = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // selon la portée, determiner les persos qu il peut attaquer
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                if (plagePoss.contains(i + plateau.length * j) && plateau[i][j] != null) {
                    // si le perso est de son camp
                    if (plateau[i][j].getCamp() == perso.getCamp()) {
                        // le perso peut donc l enchanter (uniquement s il en a la capacite)
                        if (perso.canEffetAmi()) {
                            if (scannerEnchantement) {
                                listeActions.set(i + j * plateau.length, TypeAction.ENCHANTER);
                            }
                        }
                    } else {
                        // sinon le perso peut l attaquer
                        if (scannerAttaque) {
                            listeActions.set(i + j * plateau.length, attaque);
                        }
                    }
                }
            }
        }

        // si le perso est Le Coureur et qu'il peut se déplacer partout, ajouter toutes les cases de libre pour y bouger
        if (perso instanceof LeCoureur) {
            if (perso.isSuperReady()) {
                for (int i = 0; i < plateau.length; i++) {
                    for (int j = 0; j < plateau[0].length; j++) {
                        if (plateau[i][j] == null) {
                            listeActions.remove(i + plateau.length * j);
                            listeActions.add(i + plateau.length * j, TypeAction.DEPLACER);
                        }
                    }
                }
            }
        }

        // si le perso est un Médecin, vérifier que la cible ne soit pas sa dernière cible qu'il a soigné
        if (perso instanceof Medecin) {
            for (int a = 0; a < listeActions.size(); a++) {
                if (TypeAction.ENCHANTER.equals(listeActions.get(a))) {
                    int i = a % plateau.length;
                    int j = (a - (a % plateau.length)) / plateau.length;
                    if (plateau[i][j] != null) {
                        if (plateau[i][j] == ((Medecin) perso).getDernierPersoSoigne() && !plateau[i][j].equals(perso)) {
                            listeActions.remove(a);
                            listeActions.add(a, null);
                        }
                    }
                }
            }
        }

        // si le perso est un Bélier, scan vers le haut et vers le bas pour détecter des potentiels ennemis sur la même ligne
        if (perso instanceof Belier) {
            boolean peutVersLeBas = true;
            boolean peutVersLeHaut = true;
            for (int i = 1; i < plateau[0].length; i++) {
                // scan vers le haut
                if (indexJ - i >= 0 && peutVersLeHaut) {
                    if (plateau[indexI][indexJ - i] != null) {
                        if (plateau[indexI][indexJ - i].getCamp() != perso.getCamp()) {
                            listeActions.remove(indexI + plateau.length * (indexJ - i));
                            listeActions.add(indexI + plateau.length * (indexJ - i), TypeAction.ATTAQUESPEC);
                            ((Belier) perso).setDgChargeEnHaut(perso.getDg() + i - 1);
                        }
                        peutVersLeHaut = false;
                    }
                }
                // scan vers le bas
                if (indexJ + i < plateau[0].length && peutVersLeBas) {
                    if (plateau[indexI][indexJ + i] != null) {
                        if (plateau[indexI][indexJ + i].getCamp() != perso.getCamp()) {
                            listeActions.remove(indexI + plateau.length * (indexJ + i));
                            listeActions.add(indexI + plateau.length * (indexJ + i), TypeAction.ATTAQUESPEC);
                            ((Belier) perso).setDgChargeEnBas(perso.getDg() + i - 1);
                        }
                        peutVersLeBas = false;
                    }
                }
            }

            // charger vers le bout de la ligne s'il n'y a personne
            if (peutVersLeHaut && indexJ != 0) {
                listeActions.remove(indexI + plateau.length * 0);
                listeActions.add(indexI + plateau.length * 0, TypeAction.ATTAQUESPEC);
            }
            if (peutVersLeBas && indexJ != (plateau[0].length - 1)) {
                listeActions.remove(indexI + plateau.length * (plateau[0].length - 1));
                listeActions.add(indexI + plateau.length * (plateau[0].length - 1), TypeAction.ATTAQUESPEC);
            }

        }

        // si le perso est un Apiculteur Royal enchante, et qu'il est a proximite de sa ruche, il peut modifier son mode
        if (perso instanceof ApiculteurRoyal) {
            if (perso.getEnchantement() == Enchantement.MIELLEUX && ((ApiculteurRoyal) perso).isRucheExistante()) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (verifieCoordonees(plateau, indexI + i, indexJ + j)) {
                            Perso caseAnalysee = plateau[indexI + i][indexJ + j];
                            if (caseAnalysee != null) {
                                if (caseAnalysee instanceof Ruche && caseAnalysee.getCamp() == perso.getCamp()) {
                                    int caseI = indexI + i;
                                    int caseJ = indexJ + j;
                                    listeActions.remove(caseI + plateau.length * caseJ);
                                    listeActions.add(caseI + plateau.length * caseJ, TypeAction.ENCHANTER);
                                }
                            }
                        }
                    }
                }
            }
        }

        // si le perso est un Scribe et qu'il n'observe pas encore de perso
        if (perso instanceof Scribe) {
            if (((Scribe) perso).getPersoObserve() == null) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int x = indexI + i;
                        int y = indexJ + j;
                        if (verifieCoordonees(plateau, x, y) && !(i == 0 && j == 0)) {
                            Perso p = plateau[x][y];
                            if (p != null) {
                                if (p.getCamp() == perso.getCamp()) {
                                    listeActions.set(x + plateau.length * y, TypeAction.ENCHANTER);
                                }
                            }
                        }
                    }
                }
            }
        }

        // enlever toutes les actions que l on aurait pas du scanner
        int taille = listeActions.size() - 1;
        int indexAct = 0;
        for (int i = 0; i < taille; i++) {
            TypeAction act = listeActions.get(indexAct);
            if (act != null) {
                switch (act) {
                    case ATTAQUE:
                    case ATTAQUESPEC:
                        if (!scannerAttaque) {
                            listeActions.set(indexAct, null);
                        } else {
                            indexAct++;
                        }
                        break;
                    case DEPLACER:
                        if (!scannerDeplacement) {
                            listeActions.set(indexAct, null);
                        } else {
                            indexAct++;
                        }
                        break;
                    case ENCHANTER:
                        if (!scannerEnchantement) {
                            listeActions.set(indexAct, null);
                        } else {
                            indexAct++;
                        }
                        break;
                    default:
                        indexAct++;
                }

            }
        }

        return listeActions;
    }

    public ArrayList<TypeAction> getToutesLesActions(Perso[][] plateau, Perso perso, int indexI, int indexJ) {
        return getToutesLesActions(plateau, perso, indexI, indexJ, new ArrayList<>());
    }

    public boolean verifieCoordonees(Perso[][] plateau, int x, int y) {
        boolean res = false;
        if ((x >= 0) && (x < plateau.length) && (y >= 0) && (y < plateau[0].length)) {
            res = true;
        }
        return res;
    }

    /**
     * Cette méthode permet de contrôler si le perso est à côté du héros allié
     * Le Coureur
     *
     * @param plateau
     * @param perso
     * @param indexI
     * @param indexJ
     * @return
     */
    public boolean leCoureurEstMonVoisin(Perso[][] plateau, Perso perso, int indexI, int indexJ) {
        // création du retour et des variables
        boolean retour = false;
        Perso caseAnalysee;

        // on recherche dans toutes les cases environnantes s'il y a un coureur, et s'il est dans la même équipe
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((indexI + i >= 0 && indexI + i < plateau.length) && (indexJ + j >= 0 && indexJ + j < plateau[0].length)) {
                    caseAnalysee = plateau[indexI + i][indexJ + j];
                    if (caseAnalysee != null) {
                        if (caseAnalysee instanceof LeCoureur && caseAnalysee.getCamp() == perso.getCamp() && caseAnalysee.getNmbrEtoiles() >= 3) {
                            retour = true;
                        }
                    }
                }
            }
        }

        // si c'est un Coureur, il est forcement a cote...
        if (perso instanceof LeCoureur) {
            retour = true;
        }

        return retour;
    }

    public Perso[][] deplacerPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ) {
        return deplacerPerso(plateau, posI, posJ, destI, destJ, plateau[posI][posJ], Compteur.Source.CLASSIQUE);
    }

    public Perso[][] deplacerPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ, Perso persoSource, Compteur.Source src) {
        plateau[destI][destJ] = plateau[posI][posJ];
        Perso perso = plateau[destI][destJ];
        if (perso != null) {
            if (perso instanceof LeCoureur && perso.isSuperReady()) {
                src = Compteur.Source.SUPER;
                perso.reinitialiserJetonsPuissance();
            }

            if (perso.getCamp() == 1 && (perso.isFormeInitiale())) {
                jeuCtrl.getCompteurPerso(persoSource).ajouteNombreDeplacements(1, src);
                int casesParcourues = getDistanceTo(posI, posJ, destI, destJ);
                jeuCtrl.getCompteurPerso(persoSource).ajouteNombreCasesParcourues(casesParcourues, src);
            }
            if (perso.getEvenementsPuissance().contains(TypeAction.DEPLACER)) {
                perso.ajouteJetonsPuissance(1);
                if (perso.getJetonsPuissance() >= perso.getJetonsPuissanceRequis()) {
                    perso.setSuperReady(true);
                }
            }
            plateau[posI][posJ] = null;

            // si c'est un apiculteur, et qu'il doit placer sa ruche, la placer sur la case ou il etait avant
            if (perso instanceof ApiculteurRoyal) {
                if (perso.isSuperReady() && !((ApiculteurRoyal) perso).isRucheExistante()) {
                    plateau[posI][posJ] = perso.getSousForme().copierPerso();
                    jeuCtrl.ajouterPersoSurPlateau(plateau[posI][posJ]);
                    ((ApiculteurRoyal) perso).setRucheExistante(true);
                    jeuCtrl.mettreAJourAttributs(posI, posJ);
                }
            }
            if ("Saignement".equals(perso.getEnchantementFormat())) {
                perso.subirDG(1, null, null);
                controlePersosVivants(plateau, posI, posJ, null, null);
            }
        }

        return plateau;
    }

    public Perso[][] enchanterPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ) {
        return enchanterPerso(plateau, posI, posJ, destI, destJ, Compteur.Source.CLASSIQUE);
    }

    public Perso[][] enchanterPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ, Compteur.Source src) {
        Perso persoSource = plateau[posI][posJ];
        Perso persoDest = plateau[destI][destJ];

        if (persoSource != null && persoDest != null) {
            if (persoSource instanceof Medecin) {
                persoDest.guerirPV(((Medecin) persoSource).getSoin(), persoSource, Compteur.Source.CLASSIQUE);
                ((Medecin) persoSource).setDernierPersoSoigne(persoDest);
                if (herosEstToutSeulExcept(plateau, persoSource.getCamp(), persoSource)) {
                    ((Medecin) persoSource).setDernierPersoSoigne(null);
                }
            } else if (persoSource instanceof ApiculteurRoyal) {
                if (persoDest instanceof Ruche) {
                    ((Ruche) persoDest).changeAbeillesDeSoin();
                }
            } else if (persoSource instanceof Abeille) {
                persoDest.guerirPV(1, persoSource, Compteur.Source.CLASSIQUE);
            } else if (persoSource instanceof Scribe) {
                Scribe p = (Scribe) persoSource;
                p.commencerObservation(persoDest);

            }

            // ajouter un jeton de puissance si le perso source doit en recevoir un
            if (persoSource.getEvenementsPuissance().contains(TypeAction.ENCHANTER)) {
                if (!persoSource.isSuperReady()) {
                    persoSource.ajouteJetonsPuissance(1);
                    if (persoSource.getJetonsPuissance() >= persoSource.getJetonsPuissanceRequis()) {
                        if (persoSource.isInstantSuper()) {
                            activerSuper(plateau, persoSource, posI, posJ);
                        } else {
                            persoSource.setSuperReady(true);
                        }
                    }
                }
            }

            if (persoSource instanceof Abeille) {
                persoSource = null;
            }

            Perso p = persoSource;
            boolean sousForme = false;
            if (p != null) {
                while (!p.isFormeInitiale()) {
                    p = p.getSuperForme();
                    sousForme = true;
                }
                if (sousForme) {
                    jeuCtrl.getCompteurPerso(p).ajouteNombreEnchantements(1, Compteur.Source.SOUS_FORME);
                } else {
                    jeuCtrl.getCompteurPerso(p).ajouteNombreEnchantements(1, src);
                }
            }

            plateau[posI][posJ] = persoSource;
            plateau[destI][destJ] = persoDest;
        }

        return plateau;
    }

    public Perso[][] attaquerPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ) {
        // obtenir les perso (attaquant et attaqué)
        Perso persoAttaque = plateau[posI][posJ];
        Perso persoDefense = plateau[destI][destJ];

        if (persoAttaque != null && persoDefense != null) {

            // attaque du perso attaquant
            // cas speciaux
            if (persoAttaque instanceof Vampire) {
                // vampire -> se soigne en meme temps d'attaquer
                if (!(persoDefense instanceof Vampire)) {
                    ((Vampire) persoAttaque).attaque();
                }
                persoDefense.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
                if (persoAttaque.getNmbrEtoiles() >= 2) {
                    persoDefense.setEnchantement(Enchantement.SAIGNEMENT, 4 + 1);
                }
            } else if (persoAttaque instanceof MiniBIG) {
                if (persoDefense.getPv() > ((MiniBIG) persoAttaque).getPvMinRequis()) {
                    persoDefense.subirDG(((MiniBIG) persoAttaque).getDegatsCritique(), persoAttaque, Compteur.Source.SUPER);
                    createAnimationImpact(createImage("resources/images/texturesPersos/" + persoAttaque.getNom() + "/" + persoAttaque.getCostumeSelec().getNom() + "/impact.png"), "petit", destI, destJ, destI, destJ, false, jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 0, false, false, null, 0).play();
                    if (persoAttaque.getNmbrEtoiles() >= 2 && persoAttaque.getProtection() < 1) {
                        persoAttaque.setProtection(1);
                    }
                } else {
                    persoDefense.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
                }
            } else if (persoAttaque instanceof Belier) {
                // belier -> inflige des degats selon la distance parcourue
                if (posJ > destJ) {
                    persoDefense.subirDG(((Belier) persoAttaque).getDgChargeEnHaut(), persoAttaque, Compteur.Source.CLASSIQUE);
                    // étourdir l'ennemi touché uniquement si la charge est supérieure à 1 case
                    if (((Belier) persoAttaque).getDgChargeEnHaut() - persoAttaque.getDg() > 0) {
                        persoDefense.setEnchantement(Enchantement.ETOURDI, 1 + 1);
                    }
                } else {
                    persoDefense.subirDG(((Belier) persoAttaque).getDgChargeEnBas(), persoAttaque, Compteur.Source.CLASSIQUE);
                    // étourdir l'ennemi touché uniquement si la charge est supérieure à 1 case
                    if (((Belier) persoAttaque).getDgChargeEnBas() - persoAttaque.getDg() > 0) {
                        persoDefense.setEnchantement(Enchantement.ETOURDI, 1 + 1);
                    }
                }
            } else if (persoAttaque instanceof MaitreDesSabres && persoAttaque.getNmbrEtoiles() >= 3) {
                // maitre des sabres -> frappe en arc
                // animation arc de degats
                // a faire

                // infliger des degats en arc selon la direction
                Direction.Nom direction = getDirection(posI, posJ, destI, destJ);
                Perso perso1;
                Perso perso2;
                int i1;
                int i2;
                int j1;
                int j2;
                switch (direction) {
                    case DROITE:
                        i1 = destI;
                        i2 = destI;
                        j1 = destJ - 1;
                        j2 = destJ + 1;
                        break;
                    case GAUCHE:
                        i1 = destI;
                        i2 = destI;
                        j1 = destJ - 1;
                        j2 = destJ + 1;
                        break;
                    case HAUT:
                        i1 = destI - 1;
                        i2 = destI + 1;
                        j1 = destJ;
                        j2 = destJ;
                        break;
                    case BAS:
                        i1 = destI - 1;
                        i2 = destI + 1;
                        j1 = destJ;
                        j2 = destJ;
                        break;
                    case HAUT_GAUCHE:
                        i1 = destI;
                        i2 = destI + 1;
                        j1 = destJ + 1;
                        j2 = destJ;
                        break;
                    case HAUT_DROITE:
                        i1 = destI;
                        i2 = destI - 1;
                        j1 = destJ + 1;
                        j2 = destJ;
                        break;
                    case BAS_GAUCHE:
                        i1 = destI;
                        i2 = destI + 1;
                        j1 = destJ - 1;
                        j2 = destJ;
                        break;
                    case BAS_DROITE:
                        i1 = destI;
                        i2 = destI - 1;
                        j1 = destJ - 1;
                        j2 = destJ;
                        break;
                    default:
                        i1 = destI;
                        i2 = destI;
                        j1 = destJ;
                        j2 = destJ;
                }
                if ((i1 >= 0 && i1 < plateau.length) && (j1 >= 0 && j1 < plateau[0].length)) {
                    perso1 = plateau[i1][j1];
                    if (perso1 != null) {
                        if (perso1.getCamp() != persoAttaque.getCamp()) {
                            perso1.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
                            persoAttaque.ajouteJetonsPuissance(1);
                        }
                    }
                }
                if ((i2 >= 0 && i2 < plateau.length) && (j2 >= 0 && j2 < plateau[0].length)) {
                    perso2 = plateau[i2][j2];
                    if (perso2 != null) {
                        if (perso2.getCamp() != persoAttaque.getCamp()) {
                            perso2.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
                            persoAttaque.ajouteJetonsPuissance(1);
                        }
                    }
                }
                persoDefense.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
                TranslateTransition transition = createAnimationImpact(createImage("resources/images/texturesPersos/" + persoAttaque.getNom() + "/" + persoAttaque.getCostumeSelec().getNom() + "/impact.png"), "énorme", posI, posJ, destI, destJ, true, jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 0, false, true, null, 0);
                transition.play();
            } else {
                persoDefense.subirDG(persoAttaque.getDg(), persoAttaque, Compteur.Source.CLASSIQUE);
            }

            // ajouter un jeton de puissance si le perso attaquant doit en recevoir un
            if (persoAttaque.getEvenementsPuissance().contains(TypeAction.ATTAQUE)) {
                if (!persoAttaque.isSuperReady()) {
                    persoAttaque.ajouteJetonsPuissance(1);
                    if (persoAttaque.getJetonsPuissance() >= persoAttaque.getJetonsPuissanceRequis()) {
                        if (persoAttaque.isInstantSuper()) {
                            activerSuper(plateau, persoAttaque, posI, posJ);
                        } else {
                            persoAttaque.setSuperReady(true);
                        }
                    }
                }
            }

            // réaction du perso défenseur
            if (persoDefense instanceof Cactus) {
                persoAttaque.subirDG(1, null, null);
            }

            // ajouter un jeton de puissance si le perso defenseur doit en recevoir un
            if (persoDefense.getEvenementsPuissance().contains(TypeAction.DEFENSE)) {
                if (!persoDefense.isSuperReady()) {
                    persoDefense.ajouteJetonsPuissance(1);
                    if (persoDefense.getJetonsPuissance() >= persoDefense.getJetonsPuissanceRequis()) {
                        if (persoDefense.isInstantSuper()) {
                            activerSuper(plateau, persoDefense, destI, destJ);
                        } else {
                            persoDefense.setSuperReady(true);
                        }
                    }
                }
            }
            if (persoAttaque instanceof Abeille) {
                persoAttaque = null;
            }
            // mettre à jour les cases du plateau
            plateau[posI][posJ] = persoAttaque;
            plateau[destI][destJ] = persoDefense;
        } else if (persoAttaque != null && persoDefense == null) {
            if (persoAttaque instanceof Belier) {
                persoAttaque.setEnchantement(Enchantement.ETOURDI, 1 + 1);
                deplacerPerso(plateau, posI, posJ, destI, destJ);
                persoAttaque.subirDG(1, null, Compteur.Source.CLASSIQUE);
            }
        }
        // si le perso attaqué est éliminé (qui a des points négatifs ou égaux à 0)
        plateau = controlePersosVivants(plateau, destI, destJ, persoAttaque, Compteur.Source.SUPER);

        return plateau;
    }

    public Perso[][] attaquerPersoSpec(Perso[][] plateau, int posI, int posJ, int destI, int destJ
    ) {

        // déterminer la direction du perso
        Direction.Nom direction = getDirection(posI, posJ, destI, destJ);

        // selon le perso exécuter la spécialité de celui-ci
        if (plateau[posI][posJ] instanceof Barbare) {

            int[] posFinale = getPositionSelonDirection(plateau, direction, new int[]{destI, destJ});

            if (posFinale != null) {
                deplacerPerso(plateau, posI, posJ, posFinale[0], posFinale[1]);
            }
            derniereCibleBarbare = plateau[destI][destJ];
            nombreCiblesBarbare++;
            // sinon ne pas bouger ...

        } else if (plateau[posI][posJ] instanceof Belier) {
            if ((posJ != destJ + 1) && (posJ != destJ - 1)) {
                if (Direction.Nom.BAS.equals(direction) && (destJ - 1 > 0)) {
                    deplacerPerso(plateau, posI, posJ, destI, destJ - 1);
                    if (!(plateau[destI][destJ - 1].getNmbrEtoiles() >= 2 && plateau[destI][destJ - 1].getPv() == 1)) {
                        plateau[destI][destJ - 1].subirDG(1, plateau[posI][posJ], Compteur.Source.CLASSIQUE);
                    }
                } else {
                    if ((destJ + 1 < plateau.length - 1)) {
                        deplacerPerso(plateau, posI, posJ, destI, destJ + 1);
                        if (!(plateau[destI][destJ + 1].getNmbrEtoiles() >= 2 && plateau[destI][destJ + 1].getPv() == 1)) {
                            plateau[destI][destJ + 1].subirDG(1, plateau[posI][posJ], Compteur.Source.CLASSIQUE);
                        }
                    }
                }
            } else {
                if (!(plateau[posI][posJ].getNmbrEtoiles() >= 2 && plateau[posI][posJ].getPv() == 1)) {
                    plateau[posI][posJ].subirDG(1, null, Compteur.Source.CLASSIQUE);
                }
            }

            // si la place est libre, y aller
            if (plateau[destI][destJ] == null) {
                deplacerPerso(plateau, posI, posJ, destI, destJ);
            }
        }

        return plateau;
    }

    public Perso[][] activerSuper(Perso[][] plateau, Perso perso, int posI, int posJ) {
        if (perso != null) {
            jeuCtrl.setActionEnCours(true);
            if (perso.isFormeInitiale()) {
                jeuCtrl.getCompteurPerso(perso).ajouteCapaActivees(1);
            }
            if (perso instanceof Barbare) {
                enchanterPersoAutour(plateau, perso, "Force", 1, Compteur.Source.SUPER);
            } else if (perso instanceof Demon) {
                if (perso.getNmbrEtoiles() >= 1) {
                    demonGenererCasesEnFeu(plateau, 2, posI, posJ);
                    if (perso.getNmbrEtoiles() >= 3) {
                        perso.setDg(perso.getDg() + 1);
                    }
                } else {
                    demonGenererCasesEnFeu(plateau, 1, posI, posJ);
                }
                jeuCtrl.setActionEnCours(false);
            } else if (perso instanceof Medecin) {
                ArrayList<CaseItem> listeCasesMedicaments = vagueDeMedicamentsMedecin(plateau, perso);
                jeuCtrl.appliquerChangementsMedic(listeCasesMedicaments);
            } else if (perso instanceof MaitreDesSabres) {
                ArrayList<Perso> persoLesPlusProches;
                if (perso.getCamp() == 1) {
                    persoLesPlusProches = getPersosLesPlusProches(plateau, posI, posJ, 2, 3);
                } else {
                    persoLesPlusProches = getPersosLesPlusProches(plateau, posI, posJ, 1, 3);
                }
                for (Perso lePerso : persoLesPlusProches) {
                    int[] pos = getPosition(plateau, lePerso);
                    TranslateTransition transition = createAnimationProjectile(Compteur.Source.SUPER, perso, createImage("resources/images/texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/projectile.png"), createImage("resources/images/texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/impactSuper.png"), "moyen", "lent", posI, posJ, pos[0], pos[1], jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), (perso.getNmbrEtoiles() >= 6 ? 4 : 2), false, true, Enchantement.SAIGNEMENT, 3 + 1);
                    if (transition != null) {
                        transition.play();
                        jeuCtrl.animationEnCours.add("attaque_x=" + posI + "_y=" + posJ + "_x2=" + pos[0] + "_y2=" + pos[1]);
                    }
                }
            } else if (perso instanceof Archere) {
                ArrayList<Perso> persoLesPlusProches;
                // recuperer le perso le plus proche
                if (perso.getCamp() == 1) {
                    persoLesPlusProches = getPersosLesPlusProches(plateau, posI, posJ, 2, 1);
                } else {
                    persoLesPlusProches = getPersosLesPlusProches(plateau, posI, posJ, 1, 1);
                }
                Perso persoLePlusProche = persoLesPlusProches.get(0);
                int[] pos = getPosition(plateau, persoLePlusProche);
                TranslateTransition transition = createAnimationProjectile(Compteur.Source.SUPER, perso, createImage("resources/images/texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/projectileSuper.png"), "moyen", "rapide", posI, posJ, pos[0], pos[1], jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 2, false, true, null, 99);

                boolean termine = false;
                int departX = pos[0];
                int departY = pos[1];

                // la fleche se repercute sur l'ennemi le plus proche, si celui-ci est a portee
                do {
                    // rechercher les ennemis les plus proches de la derniere cible
                    if (perso.getCamp() == 1) {
                        persoLesPlusProches = getPersosLesPlusProches(plateau, departX, departY, 2, 99);
                    } else {
                        persoLesPlusProches = getPersosLesPlusProches(plateau, departX, departY, 1, 99);
                    }
                    // parcourir tous les ennemis les plus proches
                    termine = true;
                    if (!persoLesPlusProches.isEmpty()) {
                        for (Perso lePersoLePlusProche : persoLesPlusProches) {
                            if (lePersoLePlusProche != null) {
                                int[] posPerso = getPosition(plateau, lePersoLePlusProche);
                                int cibleX = posPerso[0];
                                int cibleY = posPerso[1];
                                // le perso est a portee ?
                                if (getDistanceTo(departX, departY, cibleX, cibleY) <= 2) {
                                    // si la liste ne le contient pas
                                    if (!persosAToucherSuperArchere.contains(lePersoLePlusProche) && (lePersoLePlusProche != persoLePlusProche)) {
                                        // ajouter le perso a la liste de persos a toucher
                                        persosAToucherSuperArchere.add(lePersoLePlusProche);

                                        // stocker la position de l'ennemi
                                        departX = cibleX;
                                        departY = cibleY;
                                        // seulement si un ennemi a ete trouve, continuer
                                        termine = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } while (!termine);
                nombreCiblesSuperArchere = persosAToucherSuperArchere.size();

                transition.play();
            } else if (perso instanceof Ruche) {
                // parcourir, dans l'ordre logique, toutes les cases ou une abeille pourrait apparaitre (une case vide)
                int[] positions = new int[]{0, -1, 1};
                boolean termine = false;
                for (int x = 0; x < positions.length; x++) {
                    for (int y = 0; y < positions.length; y++) {
                        int i = positions[x];
                        int j = positions[y];
                        // controler si les valeurs sont dans les limites du plateau
                        if (verifieCoordonees(plateau, posI + i, posJ + j)) {
                            if (plateau[posI + i][posJ + j] == null) {
                                createAnimationProjectile(createImage("resources/images/texturesPersos/Abeille/" + perso.getCostumeSelec().getNom() + "/projectile.png"), "moyen", "rapide", posI, posJ, posI + i, posJ + j, jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 0, false, true, null, 98).play();
                                termine = true;
                                break;
                            }
                        }
                    }
                    if (termine) {
                        break;
                    }

                }
                if (!termine) {
                    jeuCtrl.setActionEnCours(false);
                }
            } else if (perso instanceof Nain) {
                enchanterPersoAutour(plateau, perso, "Bouclier", 3, Compteur.Source.SUPER);
            } else {
                jeuCtrl.setActionEnCours(false);
            }
            perso.reinitialiserJetonsPuissance();

        }

        return plateau;
    }

    public ArrayList<CaseItem> vagueDeMedicamentsMedecin(Perso[][] plateau, Perso medecin) {

        int[] posMedecin = getPosition(plateau, medecin);
        int posI = posMedecin[0];
        int posJ = posMedecin[1];
        int camp = medecin.getCamp();

        ArrayList<CaseItem> listeCasesMedicaments = new ArrayList<>();
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            listeCasesMedicaments.add(null);
        }
        ArrayList<Perso> tousLesPersosAllies = new ArrayList<>();
        // reperer tous les persos du meme camp
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                Perso perso = plateau[i][j];
                if (perso != null) {
                    if (perso.getCamp() == camp && !perso.equals(medecin)) {
                        tousLesPersosAllies.add(perso);
                    }
                }
            }
        }

        // parcourir tous les persos trouves
        for (Perso perso : tousLesPersosAllies) {
            int[] pos = getPosition(plateau, perso);
            int indexI = pos[0];
            int indexJ = pos[1];
            CaseItem[] listeMedic = new CaseItem[]{CaseItem.BANDAGE, CaseItem.ANTI_DOULEURS, CaseItem.VACCIN};
            // meidcs : bandage (+2pv), un anti-douleur (+50% vie -> max: 4) ou un vaccin (enleve enchantement et +1pv)
            int indexMedic = 0;
            double delai = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((indexI + i >= 0 && indexI + i < plateau.length) && (indexJ + j >= 0 && indexJ + j < plateau[0].length)) {
                        // si la case est vide
                        if (plateau[indexI + i][indexJ + j] == null && jeuCtrl.listeCasesMedicaments.get((indexI + i) + (indexJ + j) * plateau.length) == null && listeCasesMedicaments.get((indexI + i) + (indexJ + j) * plateau.length) == null) {
                            CaseItem medic = listeMedic[indexMedic];
                            // ajouter un medic
                            listeCasesMedicaments.remove((indexI + i) + (indexJ + j) * plateau.length);
                            listeCasesMedicaments.add((indexI + i) + (indexJ + j) * plateau.length, medic);
                            // creer et jouer l'animation
                            jeuCtrl.animationEnCours.add("enchanter_x=" + posI + "_y=" + posJ + "_x2=" + (indexI + i) + "_y2=" + (indexJ + j));
                            TranslateTransition transition = createAnimationProjectile(createImage("resources/images/texturesCaseItem/" + listeMedic[indexMedic] + ".png"), "petit", "rapide", posI, posJ, indexI + i, indexJ + j, jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), -1, false, false, null, 97);
                            transition.setDelay(Duration.seconds(delai));
                            transition.play();
                            delai += 0.2;
                            // passer au prochain
                            if (indexMedic >= listeMedic.length - 1) {
                                indexMedic = 0;
                            } else {
                                indexMedic++;
                            }
                        }
                    }
                }
            }
        }

        return listeCasesMedicaments;
    }

    public Perso[][] debutDuTour(Perso[][] plateau, int camp
    ) {
        Perso perso;

        // trouver les persos avec une compétence "GO"
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                // retrouver tous les perso du joueur
                if (plateau[i][j] != null) {
                    if (plateau[i][j].getCamp() == camp) {
                        perso = plateau[i][j];
                        if (perso.getEvenementsPuissance().contains(TypeAction.GO)) {
                            perso.ajouteJetonsPuissance(1);
                            if (perso.getJetonsPuissance() >= perso.getJetonsPuissanceRequis()) {
                                activerSuper(plateau, perso, i, j);
                            }
                        }
                        if (perso instanceof Princesse && perso.getNmbrEtoiles() >= 1) {
                            perso.setEnchantement(Enchantement.COURONNE, 1 + 1);
                            if (perso.getNmbrEtoiles() >= 3) {
                                ArrayList<Perso> persosLesPlusProches = getPersosLesPlusProches(plateau, i, j, camp, 1);
                                for (Perso lePerso : persosLesPlusProches) {
                                    int[] pos = getPosition(plateau, lePerso);
                                    TranslateTransition transition = createAnimationProjectile(createImage("resources/images/imgEnchantementCouronne.png"), "moyen", "rapide", i, j, pos[0], pos[1], jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 0, false, false, Enchantement.COURONNE, 1);
                                    transition.play();
                                    jeuCtrl.animationEnCours.add("enchanter_x=" + i + "_y=" + j + "_x2=" + pos[0] + "_y2=" + pos[1]);
                                }
                            }
                        } else if (perso instanceof ApiculteurRoyal) {
                            if (((ApiculteurRoyal) perso).isPotDeMielActif()) {
                                perso.setEnchantement(Enchantement.MIELLEUX, 100);
                            }
                        }
                    }
                }
            }
        }
        return plateau;
    }

    public Perso[][] cestLaFinDuTour(Perso[][] plateau, int camp
    ) {
        // parcourir tous les persos du plateau
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                Perso perso = plateau[i][j];
                if (perso != null) {
                    // effets des enchantements
                    if (perso.getEnchantement() != null) {
                        switch (perso.getEnchantement()) {
                            case POISON:
                                perso.subirDG(1, null, null);
                                break;
                            case REGENERATION:
                                perso.guerirPV(1, null, null);
                                break;
                        }
                    }
                    // diminuer les durées restantes des enchantements
                    perso.finDuTour();
                    // si le perso est un nain, lui octroyer un bouclier s'il n'en a pas déjà un
                    if (perso instanceof Nain && perso.getNmbrEtoiles() >= 1 && perso.getCamp() == camp) {
                        if (!(perso.getProtection() > 1)) {
                            perso.setProtection(1);
                        }
                    }

                    plateau = controlePersosVivants(plateau, i, j, null, null);

                    plateau[i][j] = perso;
                }
            }
        }
        return plateau;
    }

    private void demonGenererCasesEnFeu(Perso[][] plateau, int portee, int posI, int posJ) {
        Perso perso = plateau[posI][posJ];
        ArrayList<Integer> resultat = new ArrayList<>();
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            int caseJ = (i - (i % plateau.length)) / plateau.length;
            int caseI = i - caseJ * plateau.length;
            if ((caseI >= posI - portee) && (caseI <= posI + portee) && (caseJ >= posJ - portee) && (caseJ <= posJ + portee)) {
                if (plateau[caseI][caseJ] != null) {
                    if (plateau[caseI][caseJ].getCamp() != perso.getCamp()) {
                        resultat.add(i);
                    }
                }
            }
        }
        jeuCtrl.setCasesEnFeu(resultat);
    }

    public void enflammerCases(Perso[][] plateau, ArrayList<Integer> casesEnFeu) {
        if (casesEnFeu != null) {
            for (Integer index : casesEnFeu) {
                int i = index % plateau.length;
                int j = (index - (index % plateau.length)) / plateau.length;
                if (plateau[i][j] != null) {
                    plateau[i][j].subirDG(1, null, null);
                    if (plateau[i][j] instanceof Demon && plateau[i][j].getNmbrEtoiles() >= 3) {
                        plateau[i][j].setDg(plateau[i][j].getDg() + 2);
                    }
                    if (plateau[i][j].getPv() < 1) {
                        plateau[i][j] = null;
                    }
                }
            }
        }
    }

    public boolean cLaFinDeLActionOuPas(Perso[][] plateau, Perso perso, int posI, int posJ, TypeAction action) {
        // création du retour
        boolean retour = true;

        // création des variables
        ArrayList<TypeAction> listePoss;

        // contrôle selon le type du personnage
        if (perso != null) {
            if (perso.getActionSuivante() != null) {
                if (action.equals(perso.getActionRequise())) {
                    listePoss = finDeLAction(plateau, perso, posI, posJ);

                    // si la liste n'est pas vide et que le nombre de cibles est inférieur à 3, ce n'est pas la fin de l'action
                    if (listePoss != null) {
                        retour = false;
                        if (perso instanceof Barbare && perso.getNmbrEtoiles() >= ((Barbare) perso).getCiblesMax()) {
                            if (nombreCiblesBarbare >= 3) {
                                retour = true;
                            } else {
                                listePoss = gererPossBarbare(plateau, listePoss);
                                retour = (listePoss == null);
                            }
                        }
                    }

                }
            }
        }

        return retour;
    }

    private ArrayList<TypeAction> gererPossBarbare(Perso[][] plateau, ArrayList<TypeAction> listePoss) {
        int[] posDerCibleBrb = getPosition(plateau, derniereCibleBarbare);
        if (posDerCibleBrb != null) {
            if (TypeAction.ATTAQUESPEC.equals(listePoss.get(posDerCibleBrb[0] + posDerCibleBrb[1] * plateau.length))) {
                listePoss.set(posDerCibleBrb[0] + posDerCibleBrb[1] * plateau.length, null);
            }
        }
        boolean vide = true;
        for (TypeAction p : listePoss) {
            if (p != null) {
                vide = false;
            }
        }
        if (vide) {
            listePoss = null;
        }
        return listePoss;
    }

    public ArrayList<TypeAction> finDeLAction(Perso[][] plateau, Perso perso, int indexI, int indexJ) {
        // création du retour
        ArrayList<TypeAction> listeActions = new ArrayList<>();
        ArrayList<TypeAction> actionsConcernees = new ArrayList<>();
        actionsConcernees.add(perso.getActionSuivante());

        listeActions = execActionSuivante(plateau, perso, indexI, indexJ, actionsConcernees);
        return listeActions;
    }

    private ArrayList<TypeAction> execActionSuivante(Perso[][] plateau, Perso perso, int indexI, int indexJ, ArrayList<TypeAction> actionsConcernees) {
        // création du retour
        ArrayList<TypeAction> listeActions = getToutesLesActions(plateau, perso, indexI, indexJ, actionsConcernees);
        if (perso instanceof Barbare && perso.getNmbrEtoiles() >= 3) {
            listeActions = gererPossBarbare(plateau, listeActions);
        }

        // s'il n'y a aucune possibilité, retourner null
        boolean vide = true;
        if (listeActions != null) {
            for (TypeAction poss : listeActions) {
                if (poss != null) {
                    vide = false;
                    break;
                }
            }
        }

        if (vide) {
            listeActions = null;
        }

        return listeActions;
    }

    public int cestLaFinDeLaction(Perso[][] plateau, ArrayList<Perso> tousLesApiculteurs, ArrayList<Perso> equipe1, ArrayList<Perso> equipe2, Perso persoCible) {
        int partieToujoursEnCours = 0;
        derniereCibleBarbare = null;
        nombreCiblesBarbare = 0;

        // parcourir la liste des apiculteurs et verifier si un Apiculteur royal doit manger son pot de miel ou non
        for (Perso perso : tousLesApiculteurs) {
            if (perso.getPv() < 6 && !(((ApiculteurRoyal) perso).isPotDeMielActif())) {
                ((ApiculteurRoyal) perso).setPotDeMielActif(true);
            }
        }

        // verifier si un perso se trouve sur un medicament, et si oui, appliquer son effet
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                if (plateau[i][j] != null && jeuCtrl.listeCasesMedicaments.get(i + plateau.length * j) != null) {
                    switch (jeuCtrl.listeCasesMedicaments.get(i + plateau.length * j)) {
                        case BANDAGE:
                            plateau[i][j].guerirPV(2, null, null);
                            break;
                        case ANTI_DOULEURS:
                            int pvManquants = plateau[i][j].getPvMax() - plateau[i][j].getPv();
                            if (pvManquants > 12) {
                                pvManquants = 12;
                            }
                            pvManquants++;
                            plateau[i][j].guerirPV(pvManquants / 2, null, null);
                            break;
                        case VACCIN:
                            plateau[i][j].setEnchantement("", 1);
                            plateau[i][j].guerirPV(1, null, null);
                    }
                    jeuCtrl.listeCasesMedicaments.set(i + plateau.length * j, null);
                    jeuCtrl.getPnPlateau().getChildren().remove(jeuCtrl.listeImagesMedicaments.get(i + plateau.length * j));
                }
            }
        }

        if (persoCible == null) {
            persoCible = equipe2.get(0);
        }

        // controler si un des deux heros est vaincu
        if ((getPosition(plateau, equipe1.get(0)) == null)) {
            partieToujoursEnCours = 2;
        } else if ((getPosition(plateau, persoCible) == null)) {
            partieToujoursEnCours = 1;
        }

        return partieToujoursEnCours;
    }

    public ArrayList<Integer> genererMortSubite(ArrayList<Integer> casesEnFeu, int noTour, int nombreDeTourAvantMortSubite) {
        int[] casesPave;
        casesEnFeu = new ArrayList<>();

        switch ((noTour - nombreDeTourAvantMortSubite + 2) % 6) {
            case 2:
                casesPave = new int[]{0, 2, 5, 7, 8, 10, 13, 15, 16, 18, 21, 23};
                break;
            case 5:
                casesPave = new int[]{1, 3, 4, 6, 9, 11, 12, 14, 17, 19, 20, 22};
                break;
            default:
                casesPave = new int[0];
        }
        for (int i = 0; i < casesPave.length; i++) {
            casesEnFeu.add(casesPave[i]);
        }
        return casesEnFeu;
    }

    public boolean herosEstToutSeul(Perso[][] plateau, int camp) {
        boolean resultat = true;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                if (plateau[i][j] != null) {
                    if (plateau[i][j].getCamp() == camp && !plateau[i][j].isHeros()) {
                        resultat = false;
                        break;
                    }
                }
            }
            if (!resultat) {
                break;
            }
        }
        return resultat;
    }

    public boolean herosEstToutSeulExcept(Perso[][] plateau, int camp, Perso perso) {
        boolean resultat = true;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                if (plateau[i][j] != null && perso != null) {
                    if ((plateau[i][j].getCamp() == camp && !plateau[i][j].isHeros()) && !(plateau[i][j].getNom().equals(perso.getNom()))) {
                        resultat = false;
                        break;
                    }
                }
            }
            if (!resultat) {
                break;
            }
        }
        return resultat;
    }

    private Perso[][] controlePersosVivants(Perso[][] plateau, int x, int y, Perso persoSource, Compteur.Source src) {
        // parcourt les cases adjacentes a la position du perso et elimine tous les persos en-dessous de 1 pv
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((x + i >= 0 && x + i < plateau.length) && (y + j >= 0 && y + j < plateau[0].length)) {
                    if (plateau[x + i][y + j] != null) {
                        if (plateau[x + i][y + j].getPv() < 1) {
                            // si le perso source n'est pas null
                            if (persoSource != null && jeuCtrl.getCompteurPerso(persoSource) != null) {
                                // ajouter le perso defunt dans sa liste d'ennemis vaincus
                                jeuCtrl.getCompteurPerso(persoSource).ajouteEnnemiVaincu(plateau[x + i][y + j].copierPerso(), src);
                            }

                            if (plateau[x + i][y + j] instanceof Abeille) {
                                ((Abeille) plateau[x + i][y + j]).setDejaJoue(true);
                                plateau[x + i][y + j] = null;
                            } else if (plateau[x + i][y + j] instanceof Ruche) {
                                plateau[x + i][y + j] = new AbeilleCostaude(plateau[x + i][y + j].getCamp());
                                jeuCtrl.ajouterPersoSurPlateau(plateau[x + i][y + j]);
                            } else {
                                plateau[x + i][y + j] = null;
                            }
                            // reaction du perso elimine

                        }
                    }
                }
            }
        }
        return plateau;
    }

    public ArrayList<Perso> getPersosLesPlusProches(Perso[][] plateau, int posI, int posJ, int campAChercher, int nombre) {
        int nombreTrouves = 0;
        ArrayList<Perso> resultat = new ArrayList<>();
        TreeMap<Integer, ArrayList<Perso>> persoSelonPositionParRapportALaSource = new TreeMap<>();

        for (int rayon = 1; rayon < plateau[0].length; rayon++) {
            for (int i = -rayon; i <= rayon; i++) {
                for (int j = -rayon; j <= rayon; j++) {
                    if ((posI + i >= 0 && posI + i < plateau.length) && (posJ + j >= 0 && posJ + j < plateau[0].length)) {
                        Perso perso = plateau[posI + i][posJ + j];
                        // s'il y a un perso sur la case
                        if (perso != null && perso != plateau[posI][posJ]) {
                            // controler si le perso est du camp recherche, et qu'il est toujours en vie
                            if (perso.getCamp() == campAChercher && perso.getPv() > 0) {
                                int score = getDistanceTo(posI, posJ, posI + i, posJ + j);
                                ArrayList<Perso> listePersoDejaLa = persoSelonPositionParRapportALaSource.get(score);
                                if (listePersoDejaLa == null) {
                                    listePersoDejaLa = new ArrayList<>();
                                }
                                if (!listePersoDejaLa.contains(perso)) {
                                    listePersoDejaLa.add(perso);
                                }
                                persoSelonPositionParRapportALaSource.put(score, listePersoDejaLa);
                            }
                        }
                    }
                }
            }
        }

        for (ArrayList<Perso> laListeDePersos : persoSelonPositionParRapportALaSource.values()) {
            if (nombreTrouves < nombre) {
                for (Perso lePerso : laListeDePersos) {
                    if (nombreTrouves < nombre) {
                        resultat.add(lePerso);
                        nombreTrouves++;
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        return resultat;
    }

    public int getDistanceTo(int posI, int posJ, int destI, int destJ) {
        int score = 0;
        if (posI > destI) {
            score += (posI - destI);
        } else {
            score += (destI - posI);
        }

        if (posJ > destJ) {
            score += (posJ - destJ);
        } else {
            score += (destJ - posJ);
        }
        return score;
    }

    public TranslateTransition createAnimationDeplacement(ImageView imgABouger, int x, int y, double[] positionsX, double[] positionsY, double tailleDuneCase
    ) {
        // creer la transition avec duree et noeud
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.2));
        transition.setNode(imgABouger);

        // definir le point d'arrivee
        transition.setToX(positionsX[x] - imgABouger.getX() + (tailleDuneCase - imgABouger.getFitWidth()) / 2 + 7);
        transition.setToY(positionsY[y] - imgABouger.getY() + (tailleDuneCase - imgABouger.getFitHeight()) - 5);

        return transition;
    }

    public TranslateTransition createAnimationAttaque(ImageView imgABouger, int x, int y, int cibleX, int cibleY, double[] positionsX, double[] positionsY, double tailleDuneCase, boolean attaqueAFaire
    ) {
        // creer la transition aller avec duree et noeud
        TranslateTransition transitionAller = new TranslateTransition();
        transitionAller.setDuration(Duration.seconds(0.2));
        transitionAller.setNode(imgABouger);

        // creer la transition retour avec duree et noeud
        TranslateTransition transitionRetour = new TranslateTransition();
        transitionRetour.setDuration(Duration.seconds(0.2));
        transitionRetour.setNode(imgABouger);

        int xEnMoins = 0;
        int yEnMoins = 0;
        switch (getDirection(x, y, cibleX, cibleY)) {
            case HAUT:
                yEnMoins = (int) (-0.45 * tailleDuneCase);
                break;
            case BAS:
                yEnMoins = (int) (0.45 * tailleDuneCase);
                break;
            case DROITE:
                xEnMoins = (int) (0.45 * tailleDuneCase);
                break;
            case GAUCHE:
                xEnMoins = (int) (-0.45 * tailleDuneCase);
                break;
            case HAUT_GAUCHE:
                yEnMoins = (int) (-0.45 * tailleDuneCase);
                xEnMoins = (int) (-0.45 * tailleDuneCase);
                break;
            case HAUT_DROITE:
                yEnMoins = (int) (-0.45 * tailleDuneCase);
                xEnMoins = (int) (0.45 * tailleDuneCase);
                break;
            case BAS_GAUCHE:
                yEnMoins = (int) (0.45 * tailleDuneCase);
                xEnMoins = (int) (-0.45 * tailleDuneCase);
                break;
            case BAS_DROITE:
                yEnMoins = (int) (0.45 * tailleDuneCase);
                xEnMoins = (int) (0.45 * tailleDuneCase);
                break;
        }
        transitionAller.setToX(positionsX[cibleX] - imgABouger.getX() + (tailleDuneCase - imgABouger.getFitWidth()) / 2 + 0.07 * tailleDuneCase - xEnMoins);
        transitionAller.setToY(positionsY[cibleY] - imgABouger.getY() + (tailleDuneCase - imgABouger.getFitHeight()) - 0.05 * tailleDuneCase - yEnMoins);
        transitionRetour.setToX(positionsX[x] - imgABouger.getX() + (tailleDuneCase - imgABouger.getFitWidth()) / 2 + 0.07 * tailleDuneCase);
        transitionRetour.setToY(positionsY[y] - imgABouger.getY() + (tailleDuneCase - imgABouger.getFitHeight()) - 0.05 * tailleDuneCase);

        transitionAller.setOnFinished(e -> {
            if (attaqueAFaire) {
                attaquerPerso(jeuCtrl.getPlateau(), x, y, cibleX, cibleY);
            } else {
                enchanterPerso(jeuCtrl.getPlateau(), x, y, cibleX, cibleY);
            }
            jeuCtrl.mettreAJourAttributs(cibleX, cibleY);
            if (jeuCtrl.getPlateau()[x][y] == null) {
                imgABouger.setVisible(false);
                transitionRetour.setDuration(Duration.seconds(0.01));
            }
            transitionRetour.play();
        });

        transitionRetour.setOnFinished(e -> {
            if (attaqueAFaire) {
                jeuCtrl.animationEnCours.remove("attaque_x=" + x + "_y=" + y + "_x2=" + cibleX + "_y2=" + cibleY);
            } else {
                jeuCtrl.animationEnCours.remove("enchanter_x=" + x + "_y=" + y + "_x2=" + cibleX + "_y2=" + cibleY);
            }
            jeuCtrl.controleFinDuTour();
        });

        return transitionAller;
    }

    public TranslateTransition createAnimationLabel(Perso perso, Perso persoSource, Compteur.Source src, double[] positionsX, double[] positionsY, int tailleDUneCase, Color color,
            int valeur
    ) {
        tailleDUneCase = jeuCtrl.getTailleDUneCase();
        Label lblDegats = new Label();
        lblDegats.setTextFill(color);
        lblDegats.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 0.5 * tailleDUneCase));
        lblDegats.setText(valeur + "");

        int[] pos = getPosition(jeuCtrl.getPlateau(), perso);
        int x;
        int y;
        if (pos != null) {
            x = pos[0];
            y = pos[1];
        } else {
            x = 0;
            y = 0;
        }

        // gerer les compteurs des persos du joueur
        // s'il s'agit d'un label de degats 
        if (color == Color.RED) {
            // si le perso est adverse
            if (perso.getCamp() != 1) {
                // regarder s'il y a une source
                if (persoSource != null) {
                    // si c'est le cas, il s'agit de degats infliges par le joueur
                    Perso p = persoSource;
                    boolean sousForme = false;
                    while (!p.isFormeInitiale()) {
                        p = p.getSuperForme();
                        sousForme = true;
                    }
                    if (sousForme) {
                        jeuCtrl.getCompteurPerso(p).ajouteNombreDgInfliges(valeur, Compteur.Source.SOUS_FORME);
                    } else {
                        jeuCtrl.getCompteurPerso(p).ajouteNombreDgInfliges(valeur, src);
                    }
                }
            } else {
                // si le perso est allie, il s'agit de degats subis par le joueur
                if (perso.isFormeInitiale()) {
                    jeuCtrl.getCompteurPerso(perso).ajouteNombreDgSubis(valeur, src);
                }
            }
        } else if (color == Color.LIGHTGREEN) {
            // si le perso est allie
            if (perso.getCamp() == 1 && perso.isFormeInitiale()) {
                // s'il y a une source
                if (persoSource != null && persoSource.isFormeInitiale()) {
                    jeuCtrl.getCompteurPerso(persoSource).ajouteNombreSoinProcure(valeur, src);
                }
                jeuCtrl.getCompteurPerso(perso).ajouteNombreSoinRecu(valeur, src);
            }
        }

        if (positionsX == null) {
            positionsX = jeuCtrl.getPositionsX();
        }
        if (positionsY == null) {
            positionsY = jeuCtrl.getPositionsY();
        }

        TranslateTransition transitionLabelDg = new TranslateTransition();
        transitionLabelDg.setDuration(Duration.seconds(0.3));
        transitionLabelDg.setNode(lblDegats);
        lblDegats.setLayoutX(positionsX[x] + (tailleDUneCase - lblDegats.getWidth()) / 2 - 0.1 * tailleDUneCase);
        lblDegats.setLayoutY(positionsY[y] + (tailleDUneCase - lblDegats.getHeight()) / 2);
        lblDegats.setVisible(true);
        transitionLabelDg.setToX(0);
        transitionLabelDg.setToY(-0.75 * tailleDUneCase);

        transitionLabelDg.setOnFinished(e -> {
            lblDegats.setVisible(false);
            jeuCtrl.getPnPlateau().getChildren().remove(lblDegats);
            jeuCtrl.animationEnCours.remove("label_x=" + x + "_y=" + y);
            jeuCtrl.mettreAJourPlateau();
            jeuCtrl.controleFinDuTour();
        });

        jeuCtrl.getPnPlateau().getChildren().add(lblDegats);

        transitionLabelDg.play();
        jeuCtrl.animationEnCours.add("label_x=" + x + "_y=" + y);

        return transitionLabelDg;
    }

    public TranslateTransition createAnimationLeCoureurAstronaute(ImageView imgABouger, int[] posSource, int[] posDest) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.7));

        // creer la transition de deplacement
        TranslateTransition transitionDeplacement = new TranslateTransition();
        transitionDeplacement.setDuration(Duration.seconds(0.0001));
        transitionDeplacement.setNode(imgABouger);

        // definir le point d'arrivee
        transitionDeplacement.setToX(jeuCtrl.getPositionsX()[posDest[0]] - imgABouger.getX() + (jeuCtrl.getTailleDUneCase() - imgABouger.getFitWidth()) / 2 + 7);
        transitionDeplacement.setToY(jeuCtrl.getPositionsY()[posDest[1]] - imgABouger.getY() + (jeuCtrl.getTailleDUneCase() - imgABouger.getFitHeight()) - 5);

        // animation du perso
        ScaleTransition transitionSource = new ScaleTransition();
        transitionSource.setNode(imgABouger);
        transitionSource.setDuration(Duration.seconds(0.3));
        transitionSource.setFromX(1);
        transitionSource.setFromY(1);
        transitionSource.setToX(0.1);
        transitionSource.setToY(0.1);

        ScaleTransition transitionDest = new ScaleTransition();
        transitionDest.setNode(imgABouger);
        transitionDest.setDuration(Duration.seconds(0.3));
        transitionDest.setFromX(0.1);
        transitionDest.setFromY(0.1);
        transitionDest.setToX(1);
        transitionDest.setToY(1);
        
        transitionSource.setOnFinished(e -> {
            transitionDeplacement.play();
            transitionDest.play();
        });
        
        transitionDest.setOnFinished(e -> {
            jeuCtrl.animationEnCours.remove("deplacement_x=" + posSource[0] + "_y=" + posSource[1] + "_x2=" + posDest[0] + "_y2=" + posDest[1]);
            jeuCtrl.mettreAJourPlateau();
            jeuCtrl.controleFinDuTour();
        });

        // portail depart
        createAnimationRotationParticule(createImage("resources/images/texturesPersos/Le Coureur/Astronaute/imgSuper.png"), 0.6, "grand", posSource[0], posSource[1], jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase()).play();
        transitionSource.play();
        // portail arrivee
        createAnimationRotationParticule(createImage("resources/images/texturesPersos/Le Coureur/Astronaute/imgSuper.png"), 0.6, "grand", posDest[0], posDest[1], jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase()).play();
        jeuCtrl.animationEnCours.add("deplacement_x=" +  posSource[0] + "_y=" + posSource[1] + "_x2=" + posDest[0] + "_y2=" + posDest[1]);
        return transition;
    }

    public TranslateTransition createAnimationProjectile(Image img, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree) {
        return createAnimationProjectile(Compteur.Source.CLASSIQUE, null, img, null, grandeurProjectile, vitesseProjectile, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, degats, attaquerApres, orienter, enchantement, duree);
    }

    public TranslateTransition createAnimationProjectile(Image img, Image imgProjectile, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree) {
        return createAnimationProjectile(Compteur.Source.CLASSIQUE, null, img, imgProjectile, grandeurProjectile, vitesseProjectile, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, degats, attaquerApres, orienter, enchantement, duree);
    }

    public TranslateTransition createAnimationProjectile(Compteur.Source src, Perso psrc, Image img, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree
    ) {
        return createAnimationProjectile(src, psrc, img, null, grandeurProjectile, vitesseProjectile, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, degats, attaquerApres, orienter, enchantement, duree);
    }

    public TranslateTransition createAnimationProjectile(Compteur.Source src, Perso persoSource, Image img, Image imgImpact, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree) {
        ImageView imgProjectile = new ImageView();
        imgProjectile.setImage(img);

        Direction.Nom direction = getDirection(sourceX, sourceY, cibleX, cibleY);
        if (orienter) {
            switch (direction) {
                case DROITE:
                    imgProjectile.setRotate(0);
                    break;
                case BAS_DROITE:
                    imgProjectile.setRotate(45);
                    break;
                case BAS:
                    imgProjectile.setRotate(90);
                    break;
                case BAS_GAUCHE:
                    imgProjectile.setRotate(135);
                    break;
                case GAUCHE:
                    imgProjectile.setRotate(180);
                    break;
                case HAUT_GAUCHE:
                    imgProjectile.setRotate(-135);
                    break;
                case HAUT:
                    imgProjectile.setRotate(-90);
                    break;
                case HAUT_DROITE:
                    imgProjectile.setRotate(-45);
                    break;
                default:
                    imgProjectile.setRotate(0);
            }
        }

        switch (grandeurProjectile.toLowerCase()) {
            case "petit":
                imgProjectile.setFitHeight(0.3 * tailleDUneCase);
                imgProjectile.setFitWidth(0.3 * tailleDUneCase);
                break;
            case "moyen":
                imgProjectile.setFitHeight(0.6 * tailleDUneCase);
                imgProjectile.setFitWidth(0.6 * tailleDUneCase);
                break;
            case "grand":
                imgProjectile.setFitHeight(tailleDUneCase);
                imgProjectile.setFitWidth(tailleDUneCase);
                break;
            case "énorme":
                imgProjectile.setFitHeight(1.5 * tailleDUneCase);
                imgProjectile.setFitWidth(1.5 * tailleDUneCase);
                break;
        }

        TranslateTransition transition = new TranslateTransition();
        double secondsPer100Pixels = 0;
        switch (vitesseProjectile.toLowerCase()) {
            case "lent":
                secondsPer100Pixels = 1.5;
                break;
            case "moyen":
                secondsPer100Pixels = 0.5;
                break;
            case "rapide":
                secondsPer100Pixels = 0.3;
                break;
        }
        transition.setNode(imgProjectile);
        imgProjectile.setX(positionsX[sourceX] + (tailleDUneCase - imgProjectile.getFitWidth()) / 2 - 10);
        imgProjectile.setY(positionsY[sourceY] + (tailleDUneCase - imgProjectile.getFitHeight()) / 2);
        imgProjectile.setVisible(true);
        transition.setToX(positionsX[cibleX] - imgProjectile.getX() + (tailleDUneCase - imgProjectile.getFitWidth()) / 2);
        transition.setToY(positionsY[cibleY] - imgProjectile.getY() + (tailleDUneCase - imgProjectile.getFitHeight()) / 2);
        transition.setDuration(Duration.seconds((Math.sqrt(Math.pow(transition.getToX(), 2) + (Math.pow(transition.getToY(), 2))) / 100) * secondsPer100Pixels));

        transition.setOnFinished(e -> {
            Perso[][] plateau = jeuCtrl.getPlateau();
            Perso persoSrc = persoSource;
            if (persoSrc == null) {
                persoSrc = plateau[sourceX][sourceY];
            }
            Perso persoCible = plateau[cibleX][cibleY];
            if (persoCible != null) {
                if (enchantement != null) {
                    persoCible.setEnchantement(enchantement, duree);
                }
                if (degats > 0) {
                    persoCible.subirDG(degats, persoSrc, src);
                    controlePersosVivants(plateau, cibleX, cibleY, persoSource, src);
                } else if (attaquerApres) {
                    attaquerPerso(plateau, sourceX, sourceY, cibleX, cibleY);
                } else if (degats == -1) {
                    enchanterPerso(plateau, sourceX, sourceY, cibleX, cibleY);
                }

                if (imgImpact != null) {
                    createAnimationImpact(imgImpact, "moyen", cibleX, cibleY, cibleX, cibleY, false, jeuCtrl.getPositionsX(), jeuCtrl.getPositionsY(), jeuCtrl.getTailleDUneCase(), 0, false, true, null, 0).play();
                }

                // s'il reste des persos a toucher
                if (!persosAToucherSuperArchere.isEmpty() && duree == 99) {
                    int dg = degats + 2;
//                    int dg = (int) (Math.pow(2.0, (double) (nombreCiblesSuperArchere - persosAToucherSuperArchere.size())));
//                    int dg = (int) Math.pow(degats, (nombreCiblesSuperArchere - persosAToucherSuperArchere.size()));
                    Perso perso = persosAToucherSuperArchere.get(0);
                    int[] pos = getPosition(plateau, perso);
                    createAnimationProjectile(src, persoSrc, img, grandeurProjectile, vitesseProjectile, cibleX, cibleY, pos[0], pos[1], positionsX, positionsY, tailleDUneCase, dg, attaquerApres, orienter, enchantement, duree).play();
                    persosAToucherSuperArchere.remove(perso);
                }
            } else {
                // s'il s'agit de l'apparition d'une abeille
                if (enchantement == null && duree == 98) {
                    if (persoSource.getSousForme() != null) {
                        plateau[cibleX][cibleY] = persoSource.getSousForme().copierPerso();
                    }
                    ((Ruche) persoSource).incrementeId();
                    jeuCtrl.ajouterPersoSurPlateau(plateau[cibleX][cibleY]);
                }
            }
            jeuCtrl.animationEnCours.remove("attaque_x=" + sourceX + "_y=" + sourceY + "_x2=" + cibleX + "_y2=" + cibleY);
            jeuCtrl.animationEnCours.remove("enchanter_x=" + sourceX + "_y=" + sourceY + "_x2=" + cibleX + "_y2=" + cibleY);
            if (duree != 97) {
                imgProjectile.setVisible(false);
                jeuCtrl.mettreAJourAttributs(cibleX, cibleY);
                jeuCtrl.getPnPlateau().getChildren().remove(imgProjectile);
            }
            if (duree == 99) {
                if (persosAToucherSuperArchere.isEmpty()) {
                    jeuCtrl.setActionEnCours(false);
                }
            } else {
                jeuCtrl.setActionEnCours(false);
            }
            jeuCtrl.controleFinDuTour();
        });

        if (duree == 97) {
            jeuCtrl.getPnPlateau().getChildren().add(24, imgProjectile);
            jeuCtrl.listeImagesMedicaments.remove(cibleX + 4 * cibleY);
            jeuCtrl.listeImagesMedicaments.add(cibleX + 4 * cibleY, imgProjectile);
        } else {
            jeuCtrl.getPnPlateau().getChildren().add(imgProjectile);
        }

        return transition;
    }

    public TranslateTransition createAnimationImpact(Image img, String grandeurImpact,
            int sourceX, int sourceY, int cibleX, int cibleY, boolean decalerSelonDirection, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement,
            int duree
    ) {
        ImageView imgProjectile = new ImageView();
        imgProjectile.setImage(img);
        int xEnMoins = 0;
        int yEnMoins = 0;

        Direction.Nom direction = getDirection(sourceX, sourceY, cibleX, cibleY);
        if (orienter) {
            switch (direction) {
                case DROITE:
                    imgProjectile.setRotate(0);
                    break;
                case BAS_DROITE:
                    imgProjectile.setRotate(45);
                    break;
                case BAS:
                    imgProjectile.setRotate(90);
                    break;
                case BAS_GAUCHE:
                    imgProjectile.setRotate(135);
                    break;
                case GAUCHE:
                    imgProjectile.setRotate(180);
                    break;
                case HAUT_GAUCHE:
                    imgProjectile.setRotate(-135);
                    break;
                case HAUT:
                    imgProjectile.setRotate(-90);
                    break;
                case HAUT_DROITE:
                    imgProjectile.setRotate(-45);
                    break;
                default:
                    imgProjectile.setRotate(0);
            }
        }

        switch (grandeurImpact.toLowerCase()) {
            case "petit":
                imgProjectile.setFitHeight(0.3 * tailleDUneCase);
                imgProjectile.setFitWidth(0.3 * tailleDUneCase);
                break;
            case "moyen":
                imgProjectile.setFitHeight(0.6 * tailleDUneCase);
                imgProjectile.setFitWidth(0.6 * tailleDUneCase);
                break;
            case "grand":
                imgProjectile.setFitHeight(tailleDUneCase);
                imgProjectile.setFitWidth(tailleDUneCase);
                break;
            case "énorme":
                imgProjectile.setFitHeight(1.5 * tailleDUneCase);
                imgProjectile.setFitWidth(1.5 * tailleDUneCase);
                break;
        }

        if (decalerSelonDirection) {
            switch (getDirection(sourceX, sourceY, cibleX, cibleY)) {
                case HAUT:
                    yEnMoins = -45;
                    break;
                case BAS:
                    yEnMoins = 45;
                    break;
                case DROITE:
                    xEnMoins = 45;
                    break;
                case GAUCHE:
                    xEnMoins = -45;
                    break;
                case HAUT_GAUCHE:
                    yEnMoins = -45;
                    xEnMoins = -45;
                    break;
                case HAUT_DROITE:
                    yEnMoins = -45;
                    xEnMoins = 45;
                    break;
                case BAS_GAUCHE:
                    yEnMoins = 45;
                    xEnMoins = -45;
                    break;
                case BAS_DROITE:
                    yEnMoins = 45;
                    xEnMoins = 45;
                    break;
            }
        }

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(imgProjectile);
        imgProjectile.setX(positionsX[sourceX] + (tailleDUneCase - imgProjectile.getFitWidth()) / 2 - 0.1 * tailleDUneCase + xEnMoins);
        imgProjectile.setY(positionsY[sourceY] + (tailleDUneCase - imgProjectile.getFitHeight()) / 2 + yEnMoins);
        imgProjectile.setVisible(true);
        transition.setToX(0);
        transition.setToY(0);

        transition.setOnFinished(e -> {
            Perso[][] plateau = jeuCtrl.getPlateau();
            if (plateau[cibleX][cibleY] != null) {
                if (degats > 0) {
                    plateau[cibleX][cibleY].subirDG(degats, plateau[sourceX][sourceY], Compteur.Source.CLASSIQUE);
                    if (enchantement != null) {
                        plateau[cibleX][cibleY].setEnchantement(enchantement, duree);
                    }
                    if (plateau[cibleX][cibleY].getPv() < 1) {
                        plateau[cibleX][cibleY] = null;
                    }
                    jeuCtrl.animationEnCours.remove("attaque_x=" + sourceX + "_y=" + sourceY + "_x2=" + cibleX + "_y2=" + cibleY);
                } else if (attaquerApres) {
                    attaquerPerso(plateau, sourceX, sourceY, cibleX, cibleY);
                } else if (enchantement != null) {
                    enchanterPerso(plateau, sourceX, sourceY, cibleX, cibleY);
                    jeuCtrl.animationEnCours.remove("enchanter_x=" + sourceX + "_y=" + sourceY + "_x2=" + cibleX + "_y2=" + cibleY);
                }
            }
            imgProjectile.setVisible(false);
            jeuCtrl.getPnPlateau().getChildren().remove(imgProjectile);
            jeuCtrl.controleFinDuTour();
        });

        jeuCtrl.getPnPlateau().getChildren().add(imgProjectile);

        return transition;
    }

    public RotateTransition createAnimationRotationParticule(Image img, double duree, String grandeurImpact, int x, int y, double[] positionsX, double[] positionsY, int tailleDUneCase) {
        ImageView imgParticule = new ImageView();
        imgParticule.setImage(img);
        switch (grandeurImpact.toLowerCase()) {
            case "petit":
                imgParticule.setFitHeight(0.3 * tailleDUneCase);
                imgParticule.setFitWidth(0.3 * tailleDUneCase);
                break;
            case "moyen":
                imgParticule.setFitHeight(0.6 * tailleDUneCase);
                imgParticule.setFitWidth(0.6 * tailleDUneCase);
                break;
            case "grand":
                imgParticule.setFitHeight(1.2 * tailleDUneCase);
                imgParticule.setFitWidth(1.2 * tailleDUneCase);
                break;
            case "énorme":
                imgParticule.setFitHeight(1.5 * tailleDUneCase);
                imgParticule.setFitWidth(1.5 * tailleDUneCase);
                break;
        }

        imgParticule.setX(positionsX[x] + (tailleDUneCase - imgParticule.getFitWidth()) / 2);
        imgParticule.setY(positionsY[y] + (tailleDUneCase - imgParticule.getFitHeight()) / 2);
        imgParticule.setVisible(true);
        jeuCtrl.getPnPlateau().getChildren().add(positionsX.length * positionsY.length, imgParticule);

        RotateTransition transition = new RotateTransition();
        transition.setDuration(Duration.seconds(duree));
        transition.setNode(imgParticule);
        transition.setToAngle(180);

        transition.setOnFinished(e -> {
            imgParticule.setVisible(false);
            jeuCtrl.getPnPlateau().getChildren().remove(imgParticule);
        });

        return transition;
    }

    public RotateTransition createAnimationAmelioration(Image img, int x, int y, Perso[][] plateau, Perso perso, double[] positionsX, double[] positionsY, int tailleDUneCase) {
        ImageView imgEtoile = new ImageView();
        imgEtoile.setImage(img);
        imgEtoile.setFitHeight(0.7 * tailleDUneCase);
        imgEtoile.setFitWidth(0.7 * tailleDUneCase);
        imgEtoile.setOpacity(0.8);
        imgEtoile.setX(positionsX[x] + (tailleDUneCase - imgEtoile.getFitWidth()) / 2);
        imgEtoile.setY(positionsY[y] + (tailleDUneCase - imgEtoile.getFitHeight()) / 2);
        imgEtoile.setVisible(true);

        RotateTransition transition = new RotateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(imgEtoile);
        transition.setToAngle(360);

        transition.setOnFinished(e -> {
            ameliorerPerso(plateau, perso);
            imgEtoile.setVisible(false);
            jeuCtrl.getPnPlateau().getChildren().remove(imgEtoile);
            jeuCtrl.animationEnCours.remove("ameliorer_nom=" + perso.getNom() + "_camp=" + perso.getCamp());
            jeuCtrl.controleFinDuTour();
            jeuCtrl.mettreAJourPlateau();
        });

        jeuCtrl.getPnPlateau().getChildren().add(imgEtoile);

        return transition;
    }

    public TranslateTransition createAnimationDebutTour(Pane pnDebutTour, boolean affichageReduit, Perso[][] plateau, int tourDuJoueur) {
        pnDebutTour.setLayoutX(600);
        pnDebutTour.setLayoutY(250);
        pnDebutTour.setVisible(true);

        TranslateTransition transitionAller = new TranslateTransition();
        transitionAller.setDuration(Duration.seconds(0.3));
        transitionAller.setToX(-500);
        transitionAller.setNode(pnDebutTour);

        TranslateTransition transitionAller2 = new TranslateTransition();
        transitionAller2.setDuration(Duration.seconds(0.3));
        transitionAller2.setToX(-1000);
        transitionAller2.setDelay(Duration.seconds(0.8));
        transitionAller2.setNode(pnDebutTour);

        TranslateTransition transitionRetour = new TranslateTransition();
        transitionRetour.setDuration(Duration.seconds(0.01));
        transitionRetour.setToX(0);
        transitionRetour.setNode(pnDebutTour);

        transitionAller.setOnFinished(e -> {
            transitionAller2.play();
        });

        transitionAller2.setOnFinished(e1 -> {
            pnDebutTour.setVisible(false);
            transitionRetour.play();
            debutDuTour(plateau, tourDuJoueur);
            jeuCtrl.mettreAJourPlateau();
        });

        transitionRetour.setOnFinished(e -> {
            jeuCtrl.controleFinDuTour();
            jeuCtrl.tourDeLEnnemi();
        });

        return transitionAller;
    }

    public TranslateTransition determinerTransitionSelonPerso(Perso perso, ImageView imgABouger, Image img, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, boolean attaque) {
        TranslateTransition transition = null;
        Perso persoCible = jeuCtrl.getPlateau()[cibleX][cibleY];
        if (perso != null && persoCible != null) {
            if (perso.getTypeAttaque().equals(Perso.TypeAttaque.A_DISTANCE)) {
                if (perso.canEffetAmi() && persoCible.getCamp() == perso.getCamp()) {
                    transition = createAnimationProjectile(Compteur.Source.CLASSIQUE, perso, img, grandeurProjectile, vitesseProjectile, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, -1, false, false, null, 0);
                } else {
                    transition = createAnimationProjectile(Compteur.Source.CLASSIQUE, perso, img, grandeurProjectile, vitesseProjectile, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, 0, true, true, null, 0);
                }
            } else {
                transition = createAnimationAttaque(imgABouger, sourceX, sourceY, cibleX, cibleY, positionsX, positionsY, tailleDUneCase, attaque);
            }
        }
        return transition;
    }

    public TranslateTransition createAnimationBarreDeroulante(Pane pnMenu, boolean aller, double duree) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(pnMenu);
        if (aller) {
            transition.setToY(-220);
        } else {
            transition.setToY(0);
        }
        transition.setDuration(Duration.seconds(duree));
        return transition;
    }

    public Image createImage(String path) {
        Image resultat;
        try {
            resultat = new Image(path);
        } catch (Exception e) {
            resultat = null;
        }
        return resultat;
    }

    public int trouveDansLEquipe(Perso perso, ArrayList<Perso> equipe) {
        int index = -1;
        if (equipe != null) {
            for (int i = 0; i < equipe.size(); i++) {
                Perso lePerso = equipe.get(i);
                if (lePerso != null && perso != null) {
                    if (lePerso.equals(perso)) {
                        index = i;
                        break;
                    }
                }

            }
        }
        return index;
    }

    /**
     * Vérifie si le joueur peut améliorer la carte fournie.
     *
     * @param cout
     * @param nbrEtoiles
     * @param carte
     * @return 1 si OK, 0 s'il lui manque des étoiles et -1 si le niveau de la
     * carte est trop bas
     */
    public int peutAmelOuPas(int cout, int nbrEtoiles, CartePerso carte) {
        int resultat = 1;
        // si le joueur a assez d'étoiles et que la carte est assez amelioree
        if (nbrEtoiles < cout) {
            resultat = 0;
        }
        if (carte.getNiveau() < cout) {
            resultat = -1;
        }
        return resultat;
    }

    public Perso[][] ameliorerPerso(Perso[][] plateau, Perso persoAAmel) {
        // trouver le perso à améliorer sur le plateau
        int[] pos = getPosition(plateau, persoAAmel);
        // améliorer le perso en question
        persoAAmel.ameliorer();
        if (persoAAmel instanceof Nain) {
            if (persoAAmel.getNmbrEtoiles() >= 3) {
                activerSuper(plateau, persoAAmel, pos[0], pos[1]);
            }
        } else if (persoAAmel instanceof Medecin) {
            if (persoAAmel.getNmbrEtoiles() >= 2) {
                activerSuper(plateau, persoAAmel, pos[0], pos[1]);
            }
        }
        return plateau;
    }

    public Perso[][] enchanterPersoAutour(Perso[][] plateau, Perso perso, String enchantement, int duree, Compteur.Source src) {
        int[] pos = getPosition(plateau, perso);
        // enchanter tous les perso autour
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (!(pos[0] + x < 0 || pos[0] + x >= plateau.length) && !(pos[1] + y < 0 || pos[1] + y >= plateau[0].length)) {
                    if (plateau[pos[0] + x][pos[1] + y] != null && perso.getCamp() == plateau[pos[0] + x][pos[1] + y].getCamp()) {
                        switch (enchantement) {
                            case "Bouclier":
                                plateau[pos[0] + x][pos[1] + y].setProtection(plateau[pos[0] + x][pos[1] + y].getProtection() + duree);
                                break;
                            case "Soin":
                                plateau[pos[0] + x][pos[1] + y].guerirPV(duree, perso, src);
                                break;
                            default:
                                plateau[pos[0] + x][pos[1] + y].setEnchantement(enchantement, duree);
                        }
                        if (perso.isFormeInitiale()) {
                            jeuCtrl.getCompteurPerso(perso).ajouteNombreEnchantements(1, src);
                        }
                    }
                }
            }
        }
        jeuCtrl.setActionEnCours(false);
        return plateau;
    }

    /**
     * Cette méthode permet de trouver et de renvoyer la position du perso placé
     * en paramètre sur le plateau.
     *
     * @param plateau
     * @param perso
     * @return la position du perso, ou null sinon
     */
    public int[] getPosition(Perso[][] plateau, Perso perso) {

        int[] positions = null;
        boolean trouve = false;

        // recherche du perso dans le plateau
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] != null) {
                    if (plateau[i][j].equals(perso)) {
                        positions = new int[2];
                        positions[0] = i;
                        positions[1] = j;
                        trouve = true;
                        break;
                    }
                }
            }
            if (trouve) {
                break;
            }
        }
        return positions;
    }

    public Direction.Nom getDirection(int posI, int posJ, int destI, int destJ) {
        Direction.Nom direction;
        // déterminer la direction de l'attaque
        if (posI < destI) {
            if (posJ > destJ) {
                direction = Direction.Nom.HAUT_DROITE;
            } else if (posJ == destJ) {
                direction = Direction.Nom.DROITE;
            } else {
                direction = Direction.Nom.BAS_DROITE;
            }
        } else if (posI > destI) {
            if (posJ > destJ) {
                direction = Direction.Nom.HAUT_GAUCHE;
            } else if (posJ == destJ) {
                direction = Direction.Nom.GAUCHE;
            } else {
                direction = Direction.Nom.BAS_GAUCHE;
            }
        } else {
            if (posJ > destJ) {
                direction = Direction.Nom.HAUT;
            } else {
                direction = Direction.Nom.BAS;
            }
        }
        return direction;
    }

    public int[] getPositionInverseSelonDirection(Direction.Nom direction, int x, int y) {
        int xInverse = -Direction.getValX(direction);
        int yInverse = -Direction.getValY(direction);
        int[] pos = new int[]{xInverse, yInverse};
        return pos;
    }

    public ArrayList<Direction.Nom> getCheminLePlusCourt(Perso[][] plateau, int[] posA, int[] posB, boolean peutDiago) {
        ArrayList<Direction.Nom> resultat = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int indexI = posA[0] + i;
                int indexJ = posA[1] + j;
                if (verifieCoordonees(plateau, j, j)) {

                }
            }
        }

        return resultat;
    }

    public int[] getPositionSelonDirection(Perso[][] plateau, Direction.Nom direction, int[] pos) {
        // déclarer les variables utiles pour la suite
        int[] res = null;
        boolean trouve = false;

        int i = pos[0];
        int j = pos[1];

        int premPosI;
        int deuxPosI;
        int troisPosI;
        int quatrPosI;
        int derPosI;
        int premPosJ;
        int deuxPosJ;
        int troisPosJ;
        int quatrPosJ;
        int derPosJ;

        // définir l'ordre des places selon la direction du perso
        switch (direction) {
            case DROITE:
                premPosI = i + 1;
                premPosJ = j;
                deuxPosI = i + 1;
                deuxPosJ = j + 1;
                troisPosI = i + 1;
                troisPosJ = j - 1;
                quatrPosI = i;
                quatrPosJ = j - 1;
                derPosI = i;
                derPosJ = j + 1;
                break;
            case GAUCHE:
                premPosI = i - 1;
                premPosJ = j;
                deuxPosI = i - 1;
                deuxPosJ = j + 1;
                troisPosI = i - 1;
                troisPosJ = j - 1;
                quatrPosI = i;
                quatrPosJ = j - 1;
                derPosI = i;
                derPosJ = j + 1;
                break;
            case HAUT:
                premPosI = i;
                premPosJ = j - 1;
                deuxPosI = i + 1;
                deuxPosJ = j - 1;
                troisPosI = i - 1;
                troisPosJ = j - 1;
                quatrPosI = i - 1;
                quatrPosJ = j;
                derPosI = i + 1;
                derPosJ = j;
                break;
            case BAS:
                premPosI = i;
                premPosJ = j + 1;
                deuxPosI = i + 1;
                deuxPosJ = j + 1;
                troisPosI = i - 1;
                troisPosJ = j + 1;
                quatrPosI = i - 1;
                quatrPosJ = j;
                derPosI = i + 1;
                derPosJ = j;
                break;
            case HAUT_GAUCHE:
                premPosI = i - 1;
                premPosJ = j - 1;
                deuxPosI = i;
                deuxPosJ = j - 1;
                troisPosI = i - 1;
                troisPosJ = j;
                quatrPosI = i;
                quatrPosJ = j + 1;
                derPosI = i + 1;
                derPosJ = j;
                break;
            case HAUT_DROITE:
                premPosI = i + 1;
                premPosJ = j - 1;
                deuxPosI = i;
                deuxPosJ = j - 1;
                troisPosI = i + 1;
                troisPosJ = j;
                quatrPosI = i;
                quatrPosJ = j + 1;
                derPosI = i - 1;
                derPosJ = j;
                break;
            case BAS_GAUCHE:
                premPosI = i - 1;
                premPosJ = j + 1;
                deuxPosI = i;
                deuxPosJ = j + 1;
                troisPosI = i - 1;
                troisPosJ = j;
                quatrPosI = i;
                quatrPosJ = j - 1;
                derPosI = i + 1;
                derPosJ = j;
                break;
            case BAS_DROITE:
                premPosI = i + 1;
                premPosJ = j + 1;
                deuxPosI = i;
                deuxPosJ = j + 1;
                troisPosI = i + 1;
                troisPosJ = j;
                quatrPosI = i;
                quatrPosJ = j - 1;
                derPosI = i - 1;
                derPosJ = j;
                break;
            default:
                premPosI = i;
                premPosJ = j;
                deuxPosI = i;
                deuxPosJ = j;
                troisPosI = i;
                troisPosJ = j;
                quatrPosI = i;
                quatrPosJ = j;
                derPosI = i;
                derPosJ = j;
                break;
        }
        int[] listePositionsI = new int[]{premPosI, deuxPosI, troisPosI, quatrPosI, derPosI};
        int[] listePositionsJ = new int[]{premPosJ, deuxPosJ, troisPosJ, quatrPosJ, derPosJ};
        for (int x = 0; x < listePositionsI.length; x++) {
            if (!(listePositionsI[x] < 0 || listePositionsI[x] >= plateau.length) && !(listePositionsJ[x] < 0 || listePositionsJ[x] >= plateau[0].length)) {
                if (plateau[listePositionsI[x]][listePositionsJ[x]] == null) {
                    res = new int[]{listePositionsI[x], listePositionsJ[x]};
                    trouve = true;
                }
            }
            if (trouve) {
                break;
            }

        }
        return res;
    }

    public int getTailleCaracteresSelonLongueur(String texte) {
        int resultat = 0;
        if (texte != null) {
            resultat = 16 - (Math.round(texte.length() / 10));
            if (resultat < 9) {
                resultat = 9;
            }
        }
        return resultat;
    }

    private Perso derniereCibleBarbare;
    private int nombreCiblesBarbare;
    private ArrayList<Perso> persosAToucherSuperArchere;
    private int nombreCiblesSuperArchere;
}
