/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.ActionIA;
import app.beans.persos.joueur.Abeille;
import app.beans.persos.joueur.ApiculteurRoyal;
import app.beans.Assets;
import app.beans.CartePerso;
import app.beans.CaseItem;
import app.beans.Compteur;
import app.beans.IAEnnemi;
import app.beans.persos.joueur.Medecin;
import app.beans.Perso;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.LeCoureur;
import app.beans.persos.joueur.Nain;
import app.beans.persos.joueur.Ruche;
import app.workers.WorkerJeu;
import app.workers.WorkerItf;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import app.beans.Region;
import app.beans.TypeAction;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author gumyj
 */
public class JeuCtrl implements Initializable {

    public enum TypePartie {
        CAMPAGNE,
        DUEL,
        ARENE
    }

    public final static int NBRE_TOURS_AVANT_MORT_SUBITE = 100;
    private TypePartie typePartie;

    private Label[] listeDesDgDesCases;
    private Label[] listeDesPvDesCases;
    private ImageView[] listeImagesDgCases;
    private ImageView[] listeImagesPvCases;
    private ImageView[] listeDesEtoilesDesCases;
    private ImageView[] listeDesImagesCases;
    private ImageView[] listeDesPossDesCases;
    private ImageView[] listeDesEnchantementsCases;
    private ImageView[] listeImagesPuissanceCases;
    private Label[] listePuissanceCases;
    private Label[] listeNomDesPersos;
    private ImageView[] listeImgPersos;
    private ImageView[] listePersoAPlateau;
    private ImageView[] listePersoBPlateau;
    private Label[] listeTitresAmel;
    private Label[] listeDescAmel;
    private Button[] listeBoutonAmel;
    private Label[] listeCoutAmel;
    private Perso[][] plateau;
    private ArrayList<Perso> equipe1;
    private ArrayList<CartePerso> cartesEq1;
    private ArrayList<Perso> equipe2;
    private ArrayList<CartePerso> cartesEq2;
    private Region region;
    private String dispositionA;
    private String dispositionB;
    private Image imageUneEtoile;
    private Image imageDeuxEtoiles;
    private Image imageTroisEtoiles;
    private Perso persoSelec;
    private Perso persoActionUne;
    private TypeAction typeDerAction;
    private int tourDuJoueur;
    private int actionsRestantes;
    private int radarEnCours;
    private int nbreEtoilesA;
    private int nbreEtoilesB;
    private int noTour;
    private boolean mortSubite;
    private boolean tourDePreparation;
    private boolean partieEnCours;
    private double[] positionsX;
    private double[] positionsY;
    public ArrayList<String> animationEnCours;
    private ArrayList<ImageView> persosAAfficher;
    public ArrayList<CaseItem> listeCasesMedicaments;
    public ArrayList<ImageView> listeImagesMedicaments;
    private int tailleDUneCase;
    private Perso persoCible;
    private Button btnFinDuTourA;
    private Label lblRessEtoilesA;
    private Label lblRessEtoilesB;
    double xGauche;
    double yHaut;
    private boolean popupAffichee;

    private MainCtrl source;
    private Pane pnMenuDeroulantJeu;
    private ImageView imgBarreMenuDeroulant;
    private Assets assets;
    private CartePerso cartePersoSelec;
    public boolean menuDeroulantOuvert;
    private ArrayList<TypeAction> possibilites;
    private boolean actionEnCours;
    private ArrayList<Integer> casesEnFeu;
    private WorkerJeu wrk;
    private IAEnnemi ia;
    private HashMap<Perso, Compteur> tousLesCompteurs;
    @FXML
    private Label lblActionsRestantes;
    @FXML
    private Label lblToursRestants;
    @FXML
    private Label lblMortSubiteDans;
    @FXML
    private Pane pnPlateau;
    private ImageView imgFondNoir;
    private Pane pnDebutTour;
    private Pane pnAmeliorerPerso;
    private Label lblTitrePnAmel;
    private Label lblTitreAmel;
    private Label lblDescAmel;
    private Button btnAmel;
    private Label lblNbrCoutAmel;
    private ImageView imgAmelBloquee;
    private ImageView imgBanniere;
    private Label lblTourDuJoueur;
    private Label lblNoTour;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public JeuCtrl(MainCtrl source) {
        animationEnCours = new ArrayList<>();
        equipe1 = new ArrayList<>();
        equipe2 = new ArrayList<>();
        wrk = WorkerJeu.getInstance();
        wrk.setJeuCtrl(this);
        this.source = source;
        persosAAfficher = new ArrayList<>();
        possibilites = new ArrayList<>();
        tousLesCompteurs = new HashMap<>();

    }

    public void setPartieEnCours(boolean oui) {
        this.partieEnCours = oui;
    }

    public void construirePlateau(Perso[][] plateau, Pane pnPlateau, Assets assets) {
        this.plateau = plateau;
        this.pnPlateau = pnPlateau;
        this.assets = assets;

        // si le pnPlateau est deja rempli de qqch d'autre que du menu deroulant
        if (pnPlateau.getChildren().size() > 1) {
            int taille = pnPlateau.getChildren().size();
//            int numeroAEviter;
//            if (pnMenuDeroulantJeu == null) {
//                numeroAEviter = (taille - 1) - plateau.length * plateau[0].length;
//            } else {
//                numeroAEviter = this.pnPlateau.getChildren().indexOf(pnMenuDeroulantJeu);
//            }
            int dejaPasse = 0;

            while (pnPlateau.getChildren().size() > 3) {
                if (pnPlateau.getChildren().get(dejaPasse) instanceof GridPane || !(pnPlateau.getChildren().get(dejaPasse) instanceof Pane)) {
                    pnPlateau.getChildren().remove(dejaPasse);
                } else {
                    dejaPasse++;
                }
            }

//            for (int i = 0; i < pnPlateau.getChildren().size(); i++) {
//                if (!(pnPlateau.getChildren().get(dejaPasse) instanceof Pane)) {
//                    pnPlateau.getChildren().remove(dejaPasse);
//                    i--;
//                } else {
//                    dejaPasse++;
//                }
//            }
//            taille = pnPlateau.getChildren().size();
//            for (int i = 0; i < taille - 2; i++) {
//                pnPlateau.getChildren().remove(0);
//            }
        }

        // creation de tous les tableaux des cases
        listeDesDgDesCases = new Label[plateau.length * plateau[0].length];
        listeDesPvDesCases = new Label[plateau.length * plateau[0].length];
        listeImagesPvCases = new ImageView[plateau.length * plateau[0].length];
        listeImagesDgCases = new ImageView[plateau.length * plateau[0].length];
        listeDesImagesCases = new ImageView[plateau.length * plateau[0].length];
        listeDesEnchantementsCases = new ImageView[plateau.length * plateau[0].length];
        listeDesEtoilesDesCases = new ImageView[plateau.length * plateau[0].length];
        listeDesPossDesCases = new ImageView[plateau.length * plateau[0].length];
        listeImagesPuissanceCases = new ImageView[plateau.length * plateau[0].length];
        listePuissanceCases = new Label[plateau.length * plateau[0].length];
        listePersoAPlateau = new ImageView[4];
        listePersoBPlateau = new ImageView[10];
        listeCasesMedicaments = new ArrayList<>();
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            listeCasesMedicaments.add(null);
        }
        listeImagesMedicaments = new ArrayList<>();
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            listeImagesMedicaments.add(null);
        }
        for (int i = 0; i < plateau.length * plateau[0].length + 1; i++) {
            possibilites.add(null);
        }

        // determiner la taille d'une case
        double tailleCase;
        if (plateau.length > plateau[0].length) {
            tailleCase = (pnPlateau.getWidth() - 50) / (double) plateau.length;
        } else if (plateau.length < plateau[0].length) {
            tailleCase = (pnPlateau.getHeight() - 50) / (double) plateau[0].length;
        } else {
            if (pnPlateau.getWidth() > pnPlateau.getHeight()) {
                tailleCase = (pnPlateau.getWidth() - 50) / (double) plateau.length;
            } else {
                tailleCase = (pnPlateau.getHeight() - 50) / (double) plateau[0].length;
            }
        }

        if (tailleCase > 100) {
            tailleCase = 100;
        }
        this.tailleDUneCase = (int) tailleCase;

        // centrer le plateau
        this.xGauche = (pnPlateau.getWidth() - (double) plateau.length * tailleCase) / 2;
        this.yHaut = (pnPlateau.getHeight() - (double) plateau[0].length * tailleCase) / 2;
//        xGauche = 0;
//        yHaut = 0;

        // definir positionsX et postionsY
        positionsX = new double[plateau.length];
        for (int x = 0; x < plateau.length; x++) {
            positionsX[x] = this.xGauche + x * tailleCase;
        }
        positionsY = new double[plateau[0].length];
        for (int y = 0; y < plateau[0].length; y++) {
            positionsY[y] = this.yHaut + y * tailleCase;
        }

        // commentaire de Jonas : caca
        // creer les cases du plateau
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {

                final int posI = i;
                final int posJ = j;
                // CREER LES CASES
                ImageView imgCase = new ImageView();
                imgCase.setImage(assets.getImage("texturesCases/herbe/caseVide.png"));
                imgCase.setFitHeight(tailleCase);
                imgCase.setFitWidth(tailleCase);
                imgCase.setX(xGauche + i * tailleCase);
                imgCase.setY(yHaut + j * tailleCase);
                imgCase.setOnMouseClicked(e -> {
                    if (!tourDePreparation) {
                        selectionnerUneCase(plateau[posI][posJ], posI + plateau[0].length * posJ);
                    }
                });

                this.pnPlateau.getChildren().add(imgCase);
                listeDesImagesCases[i + plateau.length * j] = imgCase;
            }
        }
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            int j = ((i - (i % plateau.length)) / plateau.length);
            int x = i - j * plateau.length;
            final int posI = i;
            final int posJ = j;
            // CREER LES ATTRIBUTS
            // 1 ere cellule 
            // l'image de l'attaque
            ImageView imgDg = new ImageView(assets.getImage("texturesAttributs/imgEpeeDg.png"));
            imgDg.setFitWidth(0.3 * tailleCase);
            imgDg.setFitHeight(0.27 * tailleCase);
            listeImagesDgCases[i] = imgDg;
            // l'image de sante
            ImageView imgPv = new ImageView(assets.getImage("texturesAttributs/imgCoeurPV.png"));
            imgPv.setFitWidth(0.3 * tailleCase);
            imgPv.setFitHeight(0.27 * tailleCase);
            listeImagesPvCases[i] = imgPv;

            // 2 eme cellule
            // l'image de l'etoile
            ImageView imgEtoile = new ImageView(assets.getImage("texturesAttributs/imgEtoile1.png"));
            imgEtoile.setImage(assets.getImage("texturesAttributs/imgEtoile2.png"));
            imgEtoile.setImage(assets.getImage("texturesAttributs/imgEtoile3.png"));
            imgEtoile.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
////                imgEtoile.setImage(null);
            imgEtoile.setFitWidth(0.3 * tailleCase);
            imgEtoile.setFitHeight(0.27 * tailleCase);
            listeDesEtoilesDesCases[i] = imgEtoile;

            // 3 eme cellule
            // le label de l'attaque
            Label lblDg = new Label();
            lblDg.setPrefSize(0.3 * tailleCase, 0.27 * tailleCase);
            lblDg.setMinSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblDg.setMaxSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblDg.setText("1");
            lblDg.setFont(Font.font("System", FontWeight.BOLD, 0.15 * tailleCase));
            lblDg.setAlignment(Pos.CENTER);
            listeDesDgDesCases[i] = lblDg;
            // le label de sante
            Label lblPv = new Label();
            lblPv.setPrefSize(0.3 * tailleCase, 0.27 * tailleCase);
            lblPv.setMinSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblPv.setMaxSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblPv.setText("1");
            lblPv.setFont(Font.font("System", FontWeight.BOLD, 0.15 * tailleCase));
            lblPv.setTextFill(Color.WHITE);
            lblPv.setAlignment(Pos.CENTER);
            listeDesPvDesCases[i] = lblPv;

            // creer le GridPane
            GridPane gp = new GridPane();
            // inserer les attributs
            gp.add(listeImagesDgCases[i], 0, 0);
            gp.add(listeDesDgDesCases[i], 0, 0);
            gp.add(listeDesEtoilesDesCases[i], 1, 0);
            gp.add(listeImagesPvCases[i], 2, 0);
            gp.add(listeDesPvDesCases[i], 2, 0);

            // modifier les proprietes du gp
            gp.setPrefSize(100, 27);
            gp.setMinSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            gp.setMaxSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            ColumnConstraints colonne = new ColumnConstraints();
            colonne.setHalignment(HPos.CENTER);
            colonne.setPrefWidth(0.33 * tailleCase);
            colonne.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            colonne.setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            gp.getColumnConstraints().addAll(colonne, colonne, colonne);

            // placer le gp
            gp.setLayoutX(xGauche + x * tailleCase);
            gp.setLayoutY(yHaut + j * tailleCase);

            // ajouter le gp dans le pn
            this.pnPlateau.getChildren().add(gp);

            // CREER LES ENCHANTEMENTS
            ImageView imgEnchantement = new ImageView();
            imgEnchantement.setFitWidth(0.3 * tailleCase);
            imgEnchantement.setFitHeight(0.27 * tailleCase);
            listeDesEnchantementsCases[i] = imgEnchantement;

            // placer l'enchantement
            imgEnchantement.setX(xGauche + 0.125 * tailleCase + i * tailleCase);
            imgEnchantement.setY(yHaut + 0.125 * tailleCase + j * tailleCase);
            imgEnchantement.setOnMouseClicked(e -> {
                selectionnerUneCase(plateau[posI][posJ], posI + plateau[0].length * posJ);
            });

            // ajouter l'enchantement au gp
            this.pnPlateau.getChildren().add(imgEnchantement);

            // CREER LE JETON DE PUISSANCE
            // creer l'image du jeton
            ImageView imgJeton = new ImageView();
            imgJeton.setFitWidth(0.27 * tailleCase);
            imgJeton.setFitHeight(0.27 * tailleCase);
            imgJeton.setImage(assets.getImage("texturesAttributs/jetonPuissance/100%.png"));
            listeImagesPuissanceCases[i] = imgJeton;

            // creer le label du jeton
            Label lblJeton = new Label();
            lblJeton.setPrefSize(0.3 * tailleCase, 0.27 * tailleCase);
            lblJeton.setMinSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblJeton.setMaxSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            lblJeton.setText("1");
            lblJeton.setFont(Font.font("System", FontWeight.BOLD, 0.15 * tailleCase));
            lblJeton.setAlignment(Pos.CENTER);
            lblJeton.setFocusTraversable(true);
            listePuissanceCases[i] = lblJeton;

            // creer le GridPane
            GridPane gpJeton = new GridPane();
            // inserer les attributs
            gpJeton.add(listeImagesPuissanceCases[i], 0, 0);
            gpJeton.add(listePuissanceCases[i], 0, 0);

            // modifier les proprietes du gp
            gpJeton.setPrefSize(0.3 * tailleCase, 0.27 * tailleCase);
            gpJeton.setMinSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            gpJeton.setMaxSize(javafx.scene.layout.Region.USE_PREF_SIZE, javafx.scene.layout.Region.USE_PREF_SIZE);
            ColumnConstraints colonneJeton = new ColumnConstraints();
            colonneJeton.setHalignment(HPos.CENTER);
            colonneJeton.setPrefWidth(0.33 * tailleCase);
            colonneJeton.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            colonneJeton.setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            gpJeton.getColumnConstraints().addAll(colonneJeton);

            // placer le gp
            gpJeton.setLayoutX(xGauche + 0.002 * tailleCase + x * tailleCase);
            gpJeton.setLayoutY(yHaut + 0.7 * tailleCase + j * tailleCase);

            // ajouter le gp dans le pn
            this.pnPlateau.getChildren().add(gpJeton);
        }

        // les 4 suivants sont les images des persos du joueur 1
        for (int i = 0; i < 4; i++) {
            ImageView img = new ImageView();
            img.setFitHeight(0.76 * tailleDUneCase);
            img.setFitWidth(0.76 * tailleDUneCase);

            this.pnPlateau.getChildren().add(img);
            listePersoAPlateau[i] = img;
        }

        // les 4 suivants celles du joueur 2
        for (int i = 0; i < 10; i++) {
            ImageView img = new ImageView();
            img.setFitHeight(0.76 * tailleDUneCase);
            img.setFitWidth(0.76 * tailleDUneCase);

            this.pnPlateau.getChildren().add(img);
            listePersoBPlateau[i] = img;
        }
        for (int i = 0; i < plateau.length * plateau[0].length; i++) {
            int j = ((i - (i % plateau.length)) / plateau.length);
            int x = i - j * plateau.length;
            final int posI = x;
            final int posJ = j;

            // CREER LES IMAGES DES POSSIBILITES
            ImageView imgEnchantement = new ImageView();
            imgEnchantement.setImage(assets.getImage(""));
            imgEnchantement.setFitHeight(0.75 * tailleCase);
            imgEnchantement.setFitWidth(0.75 * tailleCase);
            imgEnchantement.setOpacity(0.78);

            imgEnchantement.setX(xGauche + x * tailleCase + 0.125 * tailleCase);
            imgEnchantement.setY(yHaut + j * tailleCase + 0.125 * tailleCase);
            imgEnchantement.setOnMouseClicked(e -> {
                selectionnerUneCase(plateau[posI][posJ], posI + plateau[0].length * posJ);
            });

            this.pnPlateau.getChildren().add(imgEnchantement);
            listeDesEnchantementsCases[i] = imgEnchantement;

            // CREER LES IMAGES DES POSSIBILITES
            ImageView imgPoss = new ImageView();
            imgPoss.setImage(null);
            imgPoss.setFitHeight(0.9 * tailleCase);
            imgPoss.setFitWidth(0.9 * tailleCase);
            imgPoss.setOpacity(0.8);

            imgPoss.setX(xGauche + x * tailleCase + 0.05 * tailleCase);
            imgPoss.setY(yHaut + j * tailleCase + 0.05 * tailleCase);
            imgPoss.setOnMouseClicked(e -> {
                int[] pos = new int[]{posI, posJ};
                if (persoSelec != null) {
                    selectionnerUnePoss(pos, posI + plateau.length * posJ);
                } else {
                    placerPerso(cartePersoSelec.getPerso(), posI, posJ);
                }
            });

            this.pnPlateau.getChildren().add(imgPoss);
            listeDesPossDesCases[i] = imgPoss;
        }

//        this.pnDebutTour = new Pane();
//        
//        
//
        if (pnDebutTour == null) {
            this.pnDebutTour = (Pane) pnPlateau.getChildren().get(1);
        }
        if (pnAmeliorerPerso == null) {
            this.pnAmeliorerPerso = (Pane) pnPlateau.getChildren().get(2);
            lblTitrePnAmel = (Label) pnAmeliorerPerso.getChildren().get(0);
            Pane pn = (Pane) pnAmeliorerPerso.getChildren().get(1);
            lblTitreAmel = (Label) pn.getChildren().get(1);
            lblDescAmel = (Label) pn.getChildren().get(0);
            Button btnQuitter = (Button) pnAmeliorerPerso.getChildren().get(2);
            btnQuitter.setOnMouseClicked(e -> {
                imgFondNoir.setVisible(false);
                pnAmeliorerPerso.setVisible(false);
            });
            btnAmel = (Button) pnAmeliorerPerso.getChildren().get(3);
            HBox hb = (HBox) pnAmeliorerPerso.getChildren().get(4);
            lblNbrCoutAmel = (Label) hb.getChildren().get(1);
            imgAmelBloquee = (ImageView) pnAmeliorerPerso.getChildren().get(5);
        }
        this.imgFondNoir = new ImageView();
        imgFondNoir.setFitHeight(700);
        imgFondNoir.setFitWidth(600);
        imgFondNoir.setImage(assets.getImage("fondNoir.png"));
        imgFondNoir.setOpacity(0.2);
        this.pnPlateau.getChildren().add(imgFondNoir);

        this.imgBanniere = (ImageView) pnDebutTour.getChildren().get(0);
        this.lblTourDuJoueur = (Label) pnDebutTour.getChildren().get(1);
        this.lblNoTour = (Label) pnDebutTour.getChildren().get(2);

        if (pnMenuDeroulantJeu == null) {
            pnMenuDeroulantJeu = (Pane) pnPlateau.getChildren().get(0);
            this.imgBarreMenuDeroulant = (ImageView) pnMenuDeroulantJeu.getChildren().get(pnMenuDeroulantJeu.getChildren().size() - 1);
            imgBarreMenuDeroulant.setOnMouseClicked(e -> {
                ouvrirFermerBarreDeroulante(!menuDeroulantOuvert, 0.2);
            });
        }
        this.pnMenuDeroulantJeu.setVisible(true);
        this.pnMenuDeroulantJeu.toFront();
        this.imgFondNoir.toFront();
        this.pnAmeliorerPerso.toFront();
        this.pnAmeliorerPerso.setVisible(false);
        this.pnDebutTour.toFront();
        menuDeroulantOuvert = false;

    }

    public void quitter() {
        // faire qq chose avant de quitter
        // wrk.fermerBD();
        // System.out.println("Je vous quitte !");

        // obligatoire pour bien terminer une application JavaFX
        Platform.exit();
    }

    public void afficherFondNoir(boolean visible) {
        this.popupAffichee = visible;
        this.imgFondNoir.setVisible(visible);
        arreterRadar();
    }

    public void ouvrirFermerBarreDeroulante(boolean ouverture, double duree) {
        boolean condition;
        if(!ouverture) {
            condition = (tourDuJoueur == 1);
        } else {
            condition = tourDuJoueur == 1 && animationEnCours.isEmpty();
        }
        if (condition) {
            if (ouverture) {
                cartePersoSelec = null;
                arreterRadar();
            }
            wrk.createAnimationBarreDeroulante(pnMenuDeroulantJeu, ouverture, duree).play();
            menuDeroulantOuvert = ouverture;
            imgBarreMenuDeroulant.setImage(assets.getImage("barreMonterDescendreCartes" + menuDeroulantOuvert + ".png"));
            mettreAJourBarreDeroulante();
        }
    }

    public void afficheAmelioration() {
        Perso p = cartePersoSelec.getPerso();
        int cout = p.getNmbrEtoiles() + 1;
        this.lblTitrePnAmel.setText("Améliorer votre " + p.getNom() + " au niveau " + cout + " ?");
        this.lblTitreAmel.setText(p.getTitreAmel(p.getNmbrEtoiles()));
        this.lblDescAmel.setText(p.getDescAmel(p.getNmbrEtoiles()));
        this.lblNbrCoutAmel.setText(cout + "");
        int resAmel = wrk.peutAmelOuPas(cout, nbreEtoilesA, cartePersoSelec);
        if (resAmel != 1) {
            this.btnAmel.setOnMouseClicked(e -> {
            });
            this.btnAmel.getStyleClass().clear();
            this.btnAmel.getStyleClass().add("btnBloque");
            // si c est a cause du nombre d etoiles
            if (resAmel == 0) {
                this.lblNbrCoutAmel.setTextFill(Color.RED);
                this.imgAmelBloquee.setVisible(false);
            } else {
                this.lblNbrCoutAmel.setTextFill(Color.WHITE);
                this.imgAmelBloquee.setVisible(true);
            }
        } else {
            this.lblNbrCoutAmel.setTextFill(Color.WHITE);
            this.btnAmel.getStyleClass().clear();
            this.btnAmel.getStyleClass().add("btnAccepter");
            this.imgAmelBloquee.setVisible(false);
            this.btnAmel.setOnMouseClicked(e -> {
                ameliorerPerso(cartePersoSelec);
                ouvrirFermerBarreDeroulante(false, 0.1);
                this.pnAmeliorerPerso.setVisible(false);
                this.imgFondNoir.setVisible(false);
            });
        }
        this.pnAmeliorerPerso.setVisible(true);
        this.imgFondNoir.setVisible(true);
    }

    public void mettreAJourBarreDeroulante() {
        Pane pn = (Pane) pnMenuDeroulantJeu.getChildren().get(0);
        // recuperer la hbox
        HBox hb = (HBox) pn.getChildren().get(0);
        // mettre a jour l'equipe
        for (int i = 0; i < hb.getChildren().size(); i++) {
            final int index = i;
            ImageView img = (ImageView) hb.getChildren().get(i);
            Perso p = equipe1.get(i);
            img.setImage(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/carte.png"));
            img.setCursor(Cursor.HAND);
            img.setOnMouseClicked(e -> {
                switch (typePartie) {
                    case CAMPAGNE:
                        if (cartePersoSelec != cartesEq1.get(index)) {
                            cartePersoSelec = cartesEq1.get(index);
                        } else {
                            cartePersoSelec = null;
                        }
                }
                mettreAJourBarreDeroulante();
            });
            if (cartePersoSelec != null) {
                if (cartePersoSelec != cartesEq1.get(i)) {
                    img.setOpacity(0.4);
                } else {
                    img.setOpacity(1.0);
                }
            } else {
                img.setOpacity(1.0);
            }
        }

        boolean peutPlacer = false;
        boolean peutAmel = true;

        if (cartePersoSelec != null) {
            Perso perso = cartePersoSelec.getPerso();
            if (wrk.getPosition(plateau, perso) == null) {
                peutAmel = false;
                peutPlacer = tourDePreparation;
            } else if (perso.isHeros()) {
                peutAmel = false;
            }
        } else {
            peutPlacer = false;
            peutAmel = false;
        }
        if(!menuDeroulantOuvert) {
            peutPlacer = false;
            peutAmel = false;
        }

        VBox vb = (VBox) pn.getChildren().get(1);
        // bouton placer
        Button btn = (Button) vb.getChildren().get(0);
        btn.setOnMouseClicked(e -> {
            ouvrirFermerBarreDeroulante(false, 0.1);
            for (int i = 0; i < plateau.length * plateau[0].length; i++) {
                possibilites.set(i, null);
            }
            for (int j = plateau[0].length - 2; j < plateau[0].length; j++) {
                for (int i = 0; i < plateau.length; i++) {
                    if (plateau[i][j] == null) {
                        possibilites.set(i + j * plateau.length, TypeAction.PLACER);
                        commencerRadar(false);
                    }
                }
            }
        });
        if (peutPlacer) {
            btn.setCursor(Cursor.HAND);
        }
        btn.setDisable(!peutPlacer);

        // bouton amel
        Button btn2 = (Button) vb.getChildren().get(1);
        btn2.setOnMouseClicked(e -> {
            afficheAmelioration();
        });
        if (peutAmel) {
            btn2.setCursor(Cursor.HAND);
        }
        btn2.setDisable(!peutAmel);

        // bouton fin du tour
        Button btn3 = (Button) vb.getChildren().get(2);
        btn3.setOnMouseClicked(e -> {
            ouvrirFermerBarreDeroulante(false, 0.1);
            finDuTour();
        });
        this.btnFinDuTourA = btn3;
        btnFinDuTourA.setDisable(tourDePreparation);

        // ressources
        HBox hbRess = (HBox) pn.getChildren().get(2);
        lblRessEtoilesA = (Label) hbRess.getChildren().get(1);
        lblActionsRestantes = (Label) hbRess.getChildren().get(3);

        lblRessEtoilesA.setText(nbreEtoilesA + "");
        lblActionsRestantes.setText(actionsRestantes + " action(s) restante(s)");

    }

    public void debutPartie(IAEnnemi ia, Perso persoCible, TypePartie typePartie) {

        popupAffichee = false;
        this.typePartie = typePartie;
        tourDePreparation = true;

        this.equipe1 = new ArrayList<>();
        for (CartePerso carte : cartesEq1) {
            equipe1.add(carte.getPerso());
        }

        if (this.typePartie.equals(TypePartie.DUEL)) {
            this.equipe2 = new ArrayList<>();
            for (CartePerso carte : cartesEq2) {
                equipe2.add(carte.getPerso());
            }
            tourDePreparation = false;
        }
        this.partieEnCours = true;
        this.ia = ia;
        this.persoCible = persoCible;
        tourDuJoueur = 1;
        actionsRestantes = 2;
        nbreEtoilesA = 0;
        nbreEtoilesB = 0;
        imageUneEtoile = assets.getImage("texturesAttributs/imgEtoile1.png");
        imageDeuxEtoiles = assets.getImage("texturesAttributs/imgEtoile2.png");
        imageTroisEtoiles = assets.getImage("texturesAttributs/imgEtoile3.png");
        radarEnCours = -1;
        actionEnCours = false;
        persoActionUne = null;
        persoSelec = null;
        casesEnFeu = null;
        noTour = 1;
        mortSubite = false;

        tousLesCompteurs.clear();
        for (int i = 0; i < equipe1.size(); i++) {
            Perso perso = equipe1.get(i);
            if (typePartie.equals(TypePartie.DUEL)) {
                int[] posPerso = wrk.getPosition(plateau, perso);
                listePersoAPlateau[i].setX(positionsX[posPerso[0]] + (tailleDUneCase - listePersoAPlateau[i].getFitWidth()) / 2 + 0.07 * this.tailleDUneCase);
                listePersoAPlateau[i].setY(positionsY[posPerso[1]] + (tailleDUneCase - listePersoAPlateau[i].getFitHeight()) - 0.05 * this.tailleDUneCase);
            }
            listePersoAPlateau[i].setOnMouseClicked(e -> {
                if (!tourDePreparation) {
                    int[] pos = wrk.getPosition(plateau, perso);
                    selectionnerUneCase(perso, pos[0] + plateau.length * pos[1]);
                }
            });
            String nom = perso.getNom();
            listePersoAPlateau[i].setImage(assets.getImage("texturesPersos/" + nom + "/" + perso.getCostumeSelec().getNom() + "/poseStatique1.png"));
            tousLesCompteurs.put(perso, new Compteur());
        }
        for (int i = 0; i < equipe2.size(); i++) {
            Perso perso = equipe2.get(i);
            int[] posPerso = wrk.getPosition(plateau, perso);
            listePersoBPlateau[i].setX(positionsX[posPerso[0]] + (tailleDUneCase - listePersoBPlateau[i].getFitWidth()) / 2 + 0.07 * this.tailleDUneCase);
            listePersoBPlateau[i].setY(positionsY[posPerso[1]] + (tailleDUneCase - listePersoBPlateau[i].getFitHeight()) - 0.05 * this.tailleDUneCase);
            listePersoBPlateau[i].setOnMouseClicked(e -> {
                int[] pos = wrk.getPosition(plateau, perso);
                selectionnerUneCase(perso, pos[0] + plateau.length * pos[1]);
            });
            String nom = perso.getNom();
            if (nom.endsWith(".")) {
                nom = nom.substring(0, nom.length() - 1);
            }
            listePersoBPlateau[i].setImage(assets.getImage("texturesPersos/" + nom + "/" + perso.getCostumeSelec().getNom() + "/poseStatique2.png"));
        }

        mettreAJourPlateau();
    }

    public void setEquipe1(ArrayList<Perso> equipe1) {
        this.equipe1 = equipe1;
    }

    public void setEquipe2(ArrayList<Perso> equipe2) {
        this.equipe2 = equipe2;
    }

    public void setCartesEq1(ArrayList<CartePerso> cartesEq1) {
        this.cartesEq1 = cartesEq1;
    }

    public void setCartesEq2(ArrayList<CartePerso> cartesEq2) {
        this.cartesEq2 = cartesEq2;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setPlateau(Perso[][] plateau) {
        this.plateau = plateau;
    }

    public void setDispositionA(String dispositionA) {
        this.dispositionA = dispositionA;
    }

    public void setDispositionB(String dispositionB) {
        this.dispositionB = dispositionB;
    }

    public Perso[][] getPlateau() {
        return plateau;
    }

    public double[] getPositionsX() {
        return positionsX;
    }

    public double[] getPositionsY() {
        return positionsY;
    }

    public int getTailleDUneCase() {
        return tailleDUneCase;
    }

    public Pane getPnPlateau() {
        return pnPlateau;
    }

    public Compteur getCompteurPerso(Perso perso) {
        return this.tousLesCompteurs.get(perso);
    }

    public void appliquerChangementsMedic(ArrayList<CaseItem> listeMedic) {
        for (int i = 0; i < listeMedic.size(); i++) {
            CaseItem medicPresent = listeCasesMedicaments.get(i);
            CaseItem medicAPoser = listeMedic.get(i);
            if (medicPresent == null && medicAPoser != null) {
                listeCasesMedicaments.remove(i);
                listeCasesMedicaments.add(i, medicAPoser);
            }
        }
    }

    public void ajouterPersoSurPlateau(Perso perso) {
        if (perso != null) {
            int camp = perso.getCamp();
            if (camp == 1) {
                // ajouter le perso dans l'equipe
                equipe1.add(perso);
                // creer un nouveau tableau pour y ajouter le perso
                int ancienneTaille = listePersoAPlateau.length;
                ImageView[] listeImgPersos = new ImageView[ancienneTaille + 1];
                // copier le contenu de l'ancien tableau
                for (int i = 0; i < listePersoAPlateau.length; i++) {
                    listeImgPersos[i] = listePersoAPlateau[i];
                }
                // ajouter une imageview
                listeImgPersos[ancienneTaille] = new ImageView();
                // remplacer le tableau
                listePersoAPlateau = listeImgPersos;
                // modifier les attributs de la nouvelle image
                int[] posPerso = wrk.getPosition(plateau, perso);
                listePersoAPlateau[ancienneTaille].setFitHeight(0.76 * this.tailleDUneCase);
                listePersoAPlateau[ancienneTaille].setFitWidth(0.76 * this.tailleDUneCase);
                listePersoAPlateau[ancienneTaille].setX(positionsX[posPerso[0]] + (tailleDUneCase - listePersoAPlateau[ancienneTaille].getFitWidth()) / 2 + 0.07 * this.tailleDUneCase);
                listePersoAPlateau[ancienneTaille].setY(positionsY[posPerso[1]] + (tailleDUneCase - listePersoAPlateau[ancienneTaille].getFitHeight()) - 0.05 * this.tailleDUneCase);

                listePersoAPlateau[ancienneTaille].setImage(assets.getImage("texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/poseStatique1.png"));
                // lorsque l'on clique dessus, cela selectionne le perso
                listePersoAPlateau[ancienneTaille].setOnMouseClicked(e -> {
                    int[] pos = wrk.getPosition(plateau, perso);
                    selectionnerUneCase(perso, pos[0] + plateau.length * pos[1] + 1);
                });
                // ajouter l'imageView dans la liste a afficher sur le pnPlateau
                persosAAfficher.add(listePersoAPlateau[ancienneTaille]);
            } else {
                // ajouter le perso dans l'equipe
                equipe2.add(perso);
                // creer un nouveau tableau pour y ajouter le perso
                int ancienneTaille = listePersoBPlateau.length;
                ImageView[] listeImgPersos = new ImageView[ancienneTaille + 1];
                // copier le contenu de l'ancien tableau
                for (int i = 0; i < listePersoBPlateau.length; i++) {
                    listeImgPersos[i] = listePersoBPlateau[i];
                }
                // ajouter une imageview
                listeImgPersos[ancienneTaille] = new ImageView();
                // remplacer le tableau
                listePersoBPlateau = listeImgPersos;
                // modifier les attributs de la nouvelle image
                int[] posPerso = wrk.getPosition(plateau, perso);
                listePersoBPlateau[ancienneTaille].setFitHeight(0.76 * this.tailleDUneCase);
                listePersoBPlateau[ancienneTaille].setFitWidth(0.76 * this.tailleDUneCase);
                listePersoBPlateau[ancienneTaille].setX(positionsX[posPerso[0]] + (tailleDUneCase - listePersoBPlateau[ancienneTaille].getFitWidth()) / 2 + 0.07 * this.tailleDUneCase);
                listePersoBPlateau[ancienneTaille].setY(positionsY[posPerso[1]] + (tailleDUneCase - listePersoBPlateau[ancienneTaille].getFitHeight()) - 0.05 * this.tailleDUneCase);

                listePersoBPlateau[ancienneTaille].setImage(assets.getImage("texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/poseStatique2.png"));
                // lorsque l'on clique dessus, cela selectionne le perso
                listePersoBPlateau[ancienneTaille].setOnMouseClicked(e -> {
                    int[] pos = wrk.getPosition(plateau, perso);
                    selectionnerUneCase(perso, pos[0] + plateau.length * pos[1] + 1);
                });
                // ajouter l'imageView dans la liste a afficher sur le pnPlateau
                persosAAfficher.add(listePersoBPlateau[ancienneTaille]);
            }

        }
    }

    public void controleFinDuTour() {
        if (partieEnCours && animationEnCours.isEmpty() && !actionEnCours) {
            ArrayList<Perso> listeApi = new ArrayList<>();
            for (Perso perso : equipe1) {
                if (perso instanceof ApiculteurRoyal) {
                    listeApi.add(perso);
                }
            }
            boolean persosBougentRestants = true;
            if (tourDuJoueur == 2 && !tourDePreparation) {
                for (Perso perso : equipe2) {
                    if (perso instanceof ApiculteurRoyal) {
                        listeApi.add(perso);
                    }

                    if (typePartie == TypePartie.CAMPAGNE) {
                        persosBougentRestants = false;

                        int[] pos = wrk.getPosition(plateau, perso);
                        if (pos != null) {
                            ArrayList<TypeAction> toutesLesActionsDuPersos = wrk.getToutesLesActions(plateau, perso, pos[0], pos[1]);

                            if (perso != persoActionUne) {
                                for (TypeAction action : toutesLesActionsDuPersos) {
                                    if (action != null) {
                                        persosBougentRestants = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (persosBougentRestants) {
                            break;
                        }
                    }
                }
            }

            if (!tourDePreparation) {
                int gagnant = wrk.cestLaFinDeLaction(plateau, listeApi, equipe1, equipe2, persoCible);
                if (gagnant != 0 && partieEnCours) {
                    partieEnCours = false;
                    source.afficherResultatsPartie(gagnant, tousLesCompteurs);
                }
            } else {
                // controler si le tour de preparation est termine
                boolean fini = true;
                for (Perso perso : equipe1) {
                    if (wrk.getPosition(plateau, perso) == null) {
                        fini = false;
                        break;
                    }
                }
                if (fini) {
                    tourDePreparation = false;
                }
            }

            // controler s'il reste des abeilles non jouees
            boolean abeilleRestantes = false;
            if (tourDuJoueur == 1) {
                for (Perso perso : equipe1) {
                    if (perso instanceof Abeille) {
                        if (!(((Abeille) perso).aDejaJoue())) {
                            abeilleRestantes = true;
                            break;
                        }
                    }
                }
            } else {
                for (Perso perso : equipe2) {
                    if (perso instanceof Abeille) {
                        if (!(((Abeille) perso).aDejaJoue())) {
                            abeilleRestantes = true;
                            break;
                        }
                    }
                }
            }
            if (!persosBougentRestants || (actionsRestantes <= 0 && !abeilleRestantes)) {
                finDuTour();
            } else {
//                btnFinDuTourA.setDisable(false);
            }
        } else if (actionEnCours && typeDerAction != null) {
            int[] pos = wrk.getPosition(plateau, persoSelec);
            boolean finDuTour = true;
            if (pos != null) {
                finDuTour = wrk.cLaFinDeLActionOuPas(plateau, persoSelec, pos[0], pos[1], typeDerAction);
            }
            if (!finDuTour) {
                possibilites = wrk.finDeLAction(plateau, persoSelec, pos[0], pos[1]);
                if (possibilites != null) {
                    commencerRadar(false);
                }
            }
        }
    }

    public void setActionEnCours(boolean actionEnCours) {
        this.actionEnCours = actionEnCours;
    }

    public void mettreAJourAttributs(int i, int j) {
        // ajouter tous les persos de la liste sur le PnPlateau
        for (ImageView imageView : persosAAfficher) {
            pnPlateau.getChildren().add(25, imageView);
        }
        persosAAfficher.clear();
        if (plateau[i][j] != null) {
            // stocker le perso en question
            Perso perso = plateau[i][j];

            // mettre à jour les attributs du perso
            listeDesDgDesCases[i + plateau.length * j].setText(perso.getDg() + "");
            listeDesPvDesCases[i + plateau.length * j].setText(perso.getPv() + "");
            if (!plateau[i][j].isHeros()) {
                switch (perso.getNmbrEtoiles()) {
                    case 0:
                        listeDesEtoilesDesCases[i + plateau.length * j].setImage(null);
                        break;
                    case 1:
                        listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageUneEtoile);
                        break;

                    case 2:
                        listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageDeuxEtoiles);
                        break;

                    case 3:
                        listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageTroisEtoiles);
                        break;
                }
            } else {
                listeDesEtoilesDesCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
            }

            // montrer l'enchantement s'il y en a un
            listeDesEnchantementsCases[i + plateau.length * j].setImage(assets.getImage("texturesEnchantements/" + perso.getEnchantementFormat() + ".png"));
            listeDesEnchantementsCases[i + plateau.length * j].setVisible(true);

            // gérer les puissances
            if (perso.getJetonsPuissanceRequis() != -1) {
                if (!perso.isSuperReady()) {
                    listePuissanceCases[i + plateau.length * j].setVisible(true);
                } else {
                    listePuissanceCases[i + plateau.length * j].setVisible(false);
                }
                listeImagesPuissanceCases[i + plateau.length * j].setVisible(true);
                double pourcentage = ((double) perso.getJetonsPuissance() / (double) perso.getJetonsPuissanceRequis()) * 100;
                listeImagesPuissanceCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/jetonPuissance/" + Math.round(pourcentage / 5) * 5 + "%.png"));
                listePuissanceCases[i + plateau.length * j].setText(perso.getJetonsPuissance() + "");
            } else {
                listePuissanceCases[i + plateau.length * j].setVisible(false);
                listeImagesPuissanceCases[i + plateau.length * j].setVisible(false);
            }

            // montrer les attributs au cas où ils seraient cachés
            listeDesDgDesCases[i + plateau.length * j].setVisible(true);
            listeDesPvDesCases[i + plateau.length * j].setVisible(true);
            listeDesEtoilesDesCases[i + plateau.length * j].setVisible(true);
            listeImagesDgCases[i + plateau.length * j].setVisible(true);
            if (plateau[i][j].getProtection() > 0) {
                listeImagesPvCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgBouclier.png"));
                listeDesPvDesCases[i + plateau.length * j].setText(perso.getProtection() + "");
            } else {
                listeImagesPvCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgCoeurPV.png"));
            }
            if (plateau[i][j] instanceof Medecin) {
                listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                listeDesDgDesCases[i + plateau.length * j].setText(((Medecin) plateau[i][j]).getSoin() + "");
            } else if (plateau[i][j] instanceof Ruche) {
                if (((Ruche) plateau[i][j]).isAbeillesDeSoin()) {
                    listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                } else {
                    listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
                }
            } else if (plateau[i][j] instanceof Abeille) {
                if (plateau[i][j].canEffetAmi()) {
                    listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                    listeDesDgDesCases[i + plateau.length * j].setText(1 + "");
                } else {
                    listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
                    listeDesDgDesCases[i + plateau.length * j].setText(plateau[i][j].getDg() + "");
                }
            } else {
                listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
            }
            listeImagesPvCases[i + plateau.length * j].setVisible(true);
        } else {
            // cacher les attributs
            listeDesDgDesCases[i + plateau.length * j].setVisible(false);
            listeDesPvDesCases[i + plateau.length * j].setVisible(false);
            listeDesEtoilesDesCases[i + plateau.length * j].setVisible(false);
            listeImagesDgCases[i + plateau.length * j].setVisible(false);
            listeImagesPvCases[i + plateau.length * j].setVisible(false);
            listeDesEnchantementsCases[i + plateau.length * j].setVisible(false);
            listePuissanceCases[i + plateau.length * j].setVisible(false);
            listeImagesPuissanceCases[i + plateau.length * j].setVisible(false);
        }
    }

    public void mettreAJourPlateau() {

        // ajouter tous les persos de la liste sur le PnPlateau
        for (ImageView imageView : persosAAfficher) {
            pnPlateau.getChildren().add(25, imageView);
        }
        persosAAfficher.clear();

        // i --> Ligne
        // j |
        //   V  Colonne
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                // si la case est en feu
                boolean feu = false;
                if (casesEnFeu != null) {
                    if (casesEnFeu.contains((i + plateau.length * j))) {
                        feu = true;
                    }
                }

//                 afficher l'etat de la case et son biome
                if (!feu) {
                    listeDesImagesCases[i + plateau.length * j].setImage(assets.getImage("texturesCases/" + this.region.getNom() + ".png"));
                } else {
                    listeDesImagesCases[i + plateau.length * j].setImage(assets.getImage("texturesCases/feu.png"));
                }

                // s'il y a un perso sur la case
                if (plateau[i][j] != null) {
                    // stocker le perso en question
                    Perso perso = plateau[i][j];

                    // mettre à jour les attributs du perso
                    listeDesDgDesCases[i + plateau.length * j].setText(perso.getDg() + "");
                    listeDesPvDesCases[i + plateau.length * j].setText(perso.getPv() + "");
                    if (!plateau[i][j].isHeros()) {
                        switch (perso.getNmbrEtoiles()) {
                            case 0:
                                listeDesEtoilesDesCases[i + plateau.length * j].setImage(null);
                                break;
                            case 1:
                                listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageUneEtoile);
                                break;

                            case 2:
                                listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageDeuxEtoiles);
                                break;

                            case 3:
                                listeDesEtoilesDesCases[i + plateau.length * j].setImage(imageTroisEtoiles);
                                break;
                        }
                    } else {
                        listeDesEtoilesDesCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
                    }

                    // montrer l'enchantement s'il y en a un
                    listeDesEnchantementsCases[i + plateau.length * j].setImage(assets.getImage("texturesEnchantements/" + perso.getEnchantementFormat() + ".png"));
                    listeDesEnchantementsCases[i + plateau.length * j].setVisible(true);

                    // gérer les puissances
                    if (perso.getJetonsPuissanceRequis() != -1) {
                        if (!perso.isSuperReady()) {
                            listePuissanceCases[i + plateau.length * j].setVisible(true);
                        } else {
                            listePuissanceCases[i + plateau.length * j].setVisible(false);
                        }
                        listeImagesPuissanceCases[i + plateau.length * j].setVisible(true);
                        double pourcentage = ((double) perso.getJetonsPuissance() / (double) perso.getJetonsPuissanceRequis()) * 100;
                        listeImagesPuissanceCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/jetonPuissance/" + Math.round(pourcentage / 5) * 5 + "%.png"));
                        listePuissanceCases[i + plateau.length * j].setText(perso.getJetonsPuissance() + "");
                    } else {
                        listePuissanceCases[i + plateau.length * j].setVisible(false);
                        listeImagesPuissanceCases[i + plateau.length * j].setVisible(false);
                    }

                    // montrer les attributs au cas où ils seraient cachés
                    listeDesDgDesCases[i + plateau.length * j].setVisible(true);
                    listeDesPvDesCases[i + plateau.length * j].setVisible(true);
                    listeDesEtoilesDesCases[i + plateau.length * j].setVisible(true);
                    listeImagesDgCases[i + plateau.length * j].setVisible(true);
                    if (plateau[i][j].getProtection() > 0) {
                        listeImagesPvCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgBouclier.png"));
                        listeDesPvDesCases[i + plateau.length * j].setText(perso.getProtection() + "");
                    } else {
                        listeImagesPvCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgCoeurPV.png"));
                    }
                    if (plateau[i][j] instanceof Medecin) {
                        listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                        listeDesDgDesCases[i + plateau.length * j].setText(((Medecin) plateau[i][j]).getSoin() + "");
                    } else if (plateau[i][j] instanceof Ruche) {
                        if (((Ruche) plateau[i][j]).isAbeillesDeSoin()) {
                            listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                        } else {
                            listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
                        }
                    } else if (plateau[i][j] instanceof Abeille) {
                        if (plateau[i][j].canEffetAmi()) {
                            listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
                            listeDesDgDesCases[i + plateau.length * j].setText(1 + "");
                        } else {
                            listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
                            listeDesDgDesCases[i + plateau.length * j].setText(plateau[i][j].getDg() + "");
                        }
                    } else {
                        listeImagesDgCases[i + plateau.length * j].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
                    }
                    listeImagesPvCases[i + plateau.length * j].setVisible(true);
                } else {
                    // cacher les attributs
                    listeDesDgDesCases[i + plateau.length * j].setVisible(false);
                    listeDesPvDesCases[i + plateau.length * j].setVisible(false);
                    listeDesEtoilesDesCases[i + plateau.length * j].setVisible(false);
                    listeImagesDgCases[i + plateau.length * j].setVisible(false);
                    listeImagesPvCases[i + plateau.length * j].setVisible(false);
                    listeDesEnchantementsCases[i + plateau.length * j].setVisible(false);
                    listePuissanceCases[i + plateau.length * j].setVisible(false);
                    listeImagesPuissanceCases[i + plateau.length * j].setVisible(false);
                }

            }
        }

        // mise à jour des éléments hors-plateau
        // mettre à jour les labels correspondant aux persos des deux équipes
//        for (int i = 0; i < 6; i++) {
//            if (i < 3) {
//                listeNomDesPersos[i].setText(equipe1.get(i + 1).getNom());
//                listeImgPersos[i].setImage(assets.getImage("texturesCartes/" + equipe1.get(i + 1).getNom() + ".png"));
//                if (!"R.I.P.".equals(listeTitresAmel[i].getText())) {
//                    listeTitresAmel[i].setText(equipe1.get(i + 1).getTitreAmel(equipe1.get(i + 1).getNmbrEtoiles()));
//                    listeDescAmel[i].setText(equipe1.get(i + 1).getDescAmel(equipe1.get(i + 1).getNmbrEtoiles()));
//                    // gérer la taille des caractères selon la longueur du texte
//                    listeDescAmel[i].setFont(new Font(wrk.getTailleCaracteresSelonLongueur(listeDescAmel[i].getText())));
//                }
//            } else {
//                listeNomDesPersos[i].setText(equipe2.get(i - 2).getNom());
//                listeImgPersos[i].setImage(assets.getImage("texturesCartes/" + equipe2.get(i - 2).getNom() + ".png"));
//                if (!"R.I.P.".equals(listeTitresAmel[i].getText())) {
//                    listeTitresAmel[i].setText(equipe2.get(i - 2).getTitreAmel(equipe2.get(i - 2).getNmbrEtoiles()));
//                    listeDescAmel[i].setText(equipe2.get(i - 2).getDescAmel(equipe2.get(i - 2).getNmbrEtoiles()));
//                    // gérer la taille des caractères selon la longueur du texte
//                    listeDescAmel[i].setFont(new Font(wrk.getTailleCaracteresSelonLongueur(listeDescAmel[i].getText())));
//                }
//            }
//        }
        // mettre à jour les n° tours et les actions restantes
//        lblActionsRestantes.setText(actionsRestantes + " action(s) restante(s)");
//        if (!mortSubite) {
//            lblMortSubiteDans.setText("Mort subite dans " + (NBRE_TOURS_AVANT_MORT_SUBITE - noTour) + " tour(s)");
//            lblToursRestants.setText("Tour " + noTour + "/" + NBRE_TOURS_AVANT_MORT_SUBITE);
//        } else {
//            lblMortSubiteDans.setText("Mort subite active");
//            lblToursRestants.setText("Tour n°" + noTour);
//        }
        // gérer les améliorations
//        gererAmels();
        // cacher les persos éliminés
        for (int i = 0; i < equipe1.size(); i++) {
            Perso perso = equipe1.get(i);
            int[] posPerso = wrk.getPosition(plateau, perso);
            if (posPerso == null) {
                listePersoAPlateau[i].setVisible(false);
            }
        }
        for (int i = 0; i < equipe2.size(); i++) {
            Perso perso = equipe2.get(i);
            int[] posPerso = wrk.getPosition(plateau, perso);
            if (posPerso == null) {
                listePersoBPlateau[i].setVisible(false);
            }
        }

        // cacher le fond noir si necessaire
        if (imgFondNoir.isVisible() && !popupAffichee) {
            imgFondNoir.setVisible(false);
        }

    }

    private void commencerRadar(boolean auto) {
        ouvrirFermerBarreDeroulante(false, 0.1);
        if (auto) {
            int[] positions = wrk.getPosition(plateau, persoSelec);
            possibilites = wrk.getToutesLesActions(plateau, persoSelec, positions[0], positions[1]);
        }

        for (int i = 0; i < listeDesPossDesCases.length; i++) {
            TypeAction possibilite = possibilites.get(i);

            if (possibilite != null) {
                switch (possibilite) {
                    case DEPLACER:
                    case PLACER:
                        listeDesPossDesCases[i].setImage(assets.getImage("imagePossibiliteDeplacerCarre.png"));
                        break;
                    case ATTAQUE:
                    case ATTAQUESPEC:
                        listeDesPossDesCases[i].setImage(assets.getImage("imagePossibiliteAttaquerCarre.png"));
                        break;
                    case ENCHANTER:
                        listeDesPossDesCases[i].setImage(assets.getImage("imagePossibiliteSoignerCarre.png"));
                        break;
                }
                listeDesPossDesCases[i].setVisible(true);
                listeDesPossDesCases[i].toFront();
            } else {
                listeDesPossDesCases[i].setVisible(false);
            }
        }

    }

    private void arreterRadar() {
        radarEnCours = -1;
        for (int i = 0; i < listeDesPossDesCases.length; i++) {
            listeDesPossDesCases[i].setVisible(false);
        }
    }

    private void finDeLAction(TypeAction typeAction) {
        this.typeDerAction = null;
        arreterRadar();
        int[] pos = wrk.getPosition(plateau, persoSelec);
        boolean finDuTour = true;
        if (pos != null) {
            finDuTour = wrk.cLaFinDeLActionOuPas(plateau, persoSelec, pos[0], pos[1], typeAction);
        }
        if (!finDuTour) {
//            possibilites = wrk.finDeLAction(plateau, persoSelec, pos[0], pos[1]);
//            if (possibilites != null) {
//                commencerRadar(false);
//            }
            this.typeDerAction = typeAction;
        } else {
            actionEnCours = false;
            if (!(persoSelec instanceof Abeille)) {
                actionsRestantes--;
            } else {
                ((Abeille) persoSelec).setDejaJoue(true);
            }

            // afficher le nombre d'actions restantes
//            lblActionsRestantes.setText(actionsRestantes + " action(s) restante(s)");
            if (!wrk.herosEstToutSeul(plateau, tourDuJoueur) && !("Couronne".equals(persoSelec.getEnchantementFormat()))) {
                if (!(persoSelec instanceof Abeille)) {
                    persoActionUne = persoSelec;
                }

                // reduire l'opacite du perso deja joue
                int index;
                if (tourDuJoueur == 1) {
                    index = wrk.trouveDansLEquipe(persoSelec, equipe1);
                    listePersoAPlateau[index].setOpacity(0.7);
                } else {
                    index = wrk.trouveDansLEquipe(persoSelec, equipe2);
                    listePersoBPlateau[index].setOpacity(0.7);
                }
            }
            controleFinDuTour();
        }
    }

    private void selectionnerUneCase(Perso persoEnQuestion, int numCase) {
        // si la barre deroulante est ouverte, la fermer
        if (!menuDeroulantOuvert) {
            ouvrirFermerBarreDeroulante(false, 0.1);
        }
        // si une action est en cours, mais qu elle peut etre arretee
        if (actionEnCours && animationEnCours.isEmpty()) {
            // s il s agit du perso selectionne
            actionsRestantes--;
            persoActionUne = persoSelec;
            actionEnCours = false;
            int index;
            if (tourDuJoueur == 1) {
                index = wrk.trouveDansLEquipe(persoSelec, equipe1);
                listePersoAPlateau[index].setOpacity(0.7);
            } else {
                index = wrk.trouveDansLEquipe(persoSelec, equipe2);
                listePersoBPlateau[index].setOpacity(0.7);
            }
            persoSelec = null;
            arreterRadar();
            controleFinDuTour();
        }

        // si une action est toujours en cours, ne pas sélectionner
        if (!actionEnCours && animationEnCours.isEmpty()) {
            // s'il y a un personnage
            if (persoEnQuestion != null && !persoEnQuestion.equals(persoActionUne)) {
                boolean peutSelec = true;
                if (persoEnQuestion instanceof Abeille) {
                    if (((Abeille) persoEnQuestion).aDejaJoue()) {
                        peutSelec = false;
                    }
                } else {
                    if (actionsRestantes < 1) {
                        peutSelec = false;
                    }
                }
                if (peutSelec) {
                    // s'il appartient au camp du joueur qui a le tour et que le perso n'est pas étourdi
                    boolean peutJouer = false;
                    switch (typePartie) {
                        case CAMPAGNE:
                            peutJouer = tourDuJoueur == 1 && persoEnQuestion.getCamp() == tourDuJoueur && !"Etourdi".equals(persoEnQuestion.getEnchantementFormat());
                            break;
                        case DUEL:
                            peutJouer = persoEnQuestion.getCamp() == tourDuJoueur && !"Etourdi".equals(persoEnQuestion.getEnchantementFormat());
                            break;
                    }

                    if (peutJouer) {
                        // s'il y a aucun radar existant
                        // creer un radar
                        if (radarEnCours == numCase) {
                            // re-clic sur le radar
                            // annuler radar
                            persoSelec = null;
                            arreterRadar();
                        } else {
                            // sélectionner le perso
                            persoSelec = persoEnQuestion;
                            // commencer le radar
                            radarEnCours = numCase;
                            commencerRadar(true);
                        }
                    }
                }
            } else {
                arreterRadar();
            }
        }
    }

    private void placerPerso(Perso perso, int posI, int posJ) {
        plateau[posI][posJ] = perso;
        listePersoAPlateau[wrk.trouveDansLEquipe(perso, equipe1)].setVisible(true);
        listePersoAPlateau[wrk.trouveDansLEquipe(perso, equipe1)].setX(300);
        listePersoAPlateau[wrk.trouveDansLEquipe(perso, equipe1)].setY(650);
        TranslateTransition transition = wrk.createAnimationDeplacement(listePersoAPlateau[wrk.trouveDansLEquipe(perso, equipe1)], posI, posJ, positionsX, positionsY, tailleDUneCase);
        transition.setOnFinished(e -> {
            mettreAJourPlateau();
            controleFinDuTour();
        });
        transition.play();
        arreterRadar();
    }

    private void selectionnerUnePoss(int[] posCase, int numCase) {
        int[] posPerso = wrk.getPosition(plateau, persoSelec);
        TranslateTransition transition;
        if (possibilites != null) {
            switch (possibilites.get(numCase)) {
                case DEPLACER:
                    // persoSelec.isSuperReady() && 
                    if (persoSelec instanceof LeCoureur && persoSelec.getCostumeSelec().getNom().equals("Astronaute")) {
                        if (tourDuJoueur == 1) {
                            transition = wrk.createAnimationLeCoureurAstronaute(listePersoAPlateau[wrk.trouveDansLEquipe(persoSelec, equipe1)], posPerso, posCase);
                        } else {
                            transition = wrk.createAnimationLeCoureurAstronaute(listePersoBPlateau[wrk.trouveDansLEquipe(persoSelec, equipe2)], posPerso, posCase);                        }
                    } else {
                        if (tourDuJoueur == 1) {
                            transition = wrk.createAnimationDeplacement(listePersoAPlateau[wrk.trouveDansLEquipe(persoSelec, equipe1)], posCase[0], posCase[1], positionsX, positionsY, tailleDUneCase);
                        } else {
                            transition = wrk.createAnimationDeplacement(listePersoBPlateau[wrk.trouveDansLEquipe(persoSelec, equipe2)], posCase[0], posCase[1], positionsX, positionsY, tailleDUneCase);
                        }
                    }
                    if (transition != null) {
                        transition.setOnFinished(e -> {
                            animationEnCours.remove("deplacement_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                            mettreAJourPlateau();
                            controleFinDuTour();
                        });
                        animationEnCours.add("deplacement_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                        transition.play();
                        // bloquer tous les boutons susceptibles d'effectuer une action en plus
//                        for (Button button : listeBoutonAmel) {
//                            button.setDisable(true);
//                        }
//                        btnFinDuTourA.setDisable(true);
                        // cacher les attributs
                        listeDesDgDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesPvDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesEtoilesDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesDgCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesPvCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesEnchantementsCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listePuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesPuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                    }
                    wrk.deplacerPerso(plateau, posPerso[0], posPerso[1], posCase[0], posCase[1]);
                    break;
                case ATTAQUE:
                    Perso p = plateau[posPerso[0]][posPerso[1]];
                    if (tourDuJoueur == 1) {
                        transition = wrk.determinerTransitionSelonPerso(plateau[posPerso[0]][posPerso[1]], listePersoAPlateau[wrk.trouveDansLEquipe(persoSelec, equipe1)], assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/projectile.png"), "moyen", "moyen", posPerso[0], posPerso[1], posCase[0], posCase[1], positionsX, positionsY, (int) tailleDUneCase, true);
                    } else {
                        transition = wrk.determinerTransitionSelonPerso(plateau[posPerso[0]][posPerso[1]], listePersoBPlateau[wrk.trouveDansLEquipe(persoSelec, equipe2)], assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/projectile.png"), "moyen", "moyen", posPerso[0], posPerso[1], posCase[0], posCase[1], positionsX, positionsY, (int) tailleDUneCase, true);
                    }
                    if (transition != null) {
                        transition.play();
                        animationEnCours.add("attaque_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                        // bloquer tous les boutons susceptibles d'effectuer une action en plus
//                        for (Button button : listeBoutonAmel) {
//                            button.setDisable(true);
//                        }
//                        btnFinDuTourA.setDisable(true);
                        // cacher les attributs si le perso attaque en melee
                        if (plateau[posPerso[0]][posPerso[1]].getPortee() == 1) {
                            listeDesDgDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeDesPvDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeDesEtoilesDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeImagesDgCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeImagesPvCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeDesEnchantementsCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listePuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                            listeImagesPuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        }
                    }
                    break;
                case ATTAQUESPEC:
                    wrk.attaquerPerso(plateau, posPerso[0], posPerso[1], posCase[0], posCase[1]);
                    wrk.attaquerPersoSpec(plateau, posPerso[0], posPerso[1], posCase[0], posCase[1]);
                    int[] posPerso2 = wrk.getPosition(plateau, persoSelec);
                    if (tourDuJoueur == 1) {
                        transition = wrk.createAnimationDeplacement(listePersoAPlateau[wrk.trouveDansLEquipe(persoSelec, equipe1)], posPerso2[0], posPerso2[1], positionsX, positionsY, tailleDUneCase);
                    } else {
                        transition = wrk.createAnimationDeplacement(listePersoBPlateau[wrk.trouveDansLEquipe(persoSelec, equipe2)], posPerso2[0], posPerso2[1], positionsX, positionsY, tailleDUneCase);
                    }
                    if (transition != null) {
                        transition.setOnFinished(e -> {
                            animationEnCours.remove("attaqueSpec_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                            mettreAJourPlateau();
                            controleFinDuTour();
                        });
                        transition.play();
                        animationEnCours.add("attaqueSpec_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                        // bloquer tous les boutons susceptibles d'effectuer une action en plus
//                        for (Button button : listeBoutonAmel) {
//                            button.setDisable(true);
//                        }
//                        btnFinDuTourA.setDisable(true);
                        // cacher les attributs 
                        listeDesDgDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesPvDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesEtoilesDesCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesDgCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesPvCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeDesEnchantementsCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listePuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                        listeImagesPuissanceCases[posPerso[0] + plateau.length * posPerso[1]].setVisible(false);
                    }
                    break;
                case ENCHANTER:
                    p = plateau[posPerso[0]][posPerso[1]];
                    if (tourDuJoueur == 1) {
                        transition = wrk.determinerTransitionSelonPerso(plateau[posPerso[0]][posPerso[1]], listePersoAPlateau[wrk.trouveDansLEquipe(persoSelec, equipe1)], assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/projectile.png"), "petit", "moyen", posPerso[0], posPerso[1], posCase[0], posCase[1], positionsX, positionsY, (int) tailleDUneCase, false);
                    } else {
                        transition = wrk.determinerTransitionSelonPerso(plateau[posPerso[0]][posPerso[1]], listePersoBPlateau[wrk.trouveDansLEquipe(persoSelec, equipe2)], assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/projectile.png"), "petit", "moyen", posPerso[0], posPerso[1], posCase[0], posCase[1], positionsX, positionsY, (int) tailleDUneCase, false);
                    }
                    if (transition != null) {
                        transition.play();
                        animationEnCours.add("enchanter_x=" + posPerso[0] + "_y=" + posPerso[1] + "_x2=" + posCase[0] + "_y2=" + posCase[1]);
                        // bloquer tous les boutons susceptibles d'effectuer une action en plus
//                        for (Button button : listeBoutonAmel) {
//                            button.setDisable(true);
//                        }
//                        btnFinDuTourA.setDisable(true);
                    }
                    break;
            }
            actionEnCours = true;
            arreterRadar();
            finDeLAction(possibilites.get(numCase));
        } else {
            arreterRadar();
        }
    }

    private void ameliorerPerso(CartePerso carte) {
        if (actionsRestantes > 0) {
            int cout;
            RotateTransition transition;
            Perso perso = carte.getPerso();
            if (tourDuJoueur == 1) {
                cout = perso.getNmbrEtoiles() + 1;
                if (wrk.peutAmelOuPas(cout, nbreEtoilesA, carte) == 1) {
                    transition = wrk.createAnimationAmelioration(imageUneEtoile, wrk.getPosition(plateau, perso)[0], wrk.getPosition(plateau, perso)[1], plateau, perso, positionsX, positionsY, (int) tailleDUneCase);
                    if (transition != null) {
                        transition.play();
                        animationEnCours.add("ameliorer_nom=" + perso.getNom() + "_camp=" + perso.getCamp());
                    }
                    nbreEtoilesA -= cout;
                    actionsRestantes--;
                }
            } else if (tourDuJoueur == 2) {
                cout = perso.getNmbrEtoiles() + 1;
                if (wrk.peutAmelOuPas(cout, nbreEtoilesB, carte) == 1) {
                    transition = wrk.createAnimationAmelioration(imageUneEtoile, wrk.getPosition(plateau, perso)[0], wrk.getPosition(plateau, perso)[1], plateau, perso, positionsX, positionsY, (int) tailleDUneCase);
                    if (transition != null) {
                        transition.play();
                        animationEnCours.add("ameliorer_nom=" + perso.getNom() + "_camp=" + perso.getCamp());
                    }
                    nbreEtoilesB -= cout;
                    actionsRestantes--;
                }
            }
            arreterRadar();
            controleFinDuTour();
        }
    }

    private void gererAmels() {
        String[] tristesse = new String[7];
        tristesse[0] = "Il nous a quitté, ... trop tôt";
        tristesse[1] = "Il nous manque déjà... snif";
        tristesse[2] = "#décès";
        tristesse[3] = "Repose en paix, petit être";
        tristesse[4] = "Ce sont toujours les meilleurs qui partent en premier...";
        tristesse[5] = "Sa présence nous manquera... snif...";
        tristesse[6] = "Il est mort mdr";
        for (int i = 0; i < listeBoutonAmel.length; i++) {
            // activer tous les boutons
            listeBoutonAmel[i].setDisable(false);
            if (i < 3) {
                // si c'est le tour du joueur
                if (tourDuJoueur == 1) {
                    // si le perso n'est pas en vie
                    if (wrk.getPosition(plateau, equipe1.get(i + 1)) == null) {
                        // désactiver le bouton
                        listeBoutonAmel[i].setDisable(true);
                        // indiquer qu'il a été éliminé
                        if (!"R.I.P.".equals(listeTitresAmel[i].getText())) {
                            listeTitresAmel[i].setText("R.I.P.");
                            listeDescAmel[i].setText(tristesse[(int) (Math.random() * ((tristesse.length - 1) - 0 + 1)) + 0]);
                            listeDescAmel[i].setFont(new Font(wrk.getTailleCaracteresSelonLongueur(listeDescAmel[i].getText())));
                        }
                    }
                    // si le perso est au niveau max
                    if (equipe1.get(i + 1).getNmbrEtoiles() >= 3) {
                        listeBoutonAmel[i].setDisable(true);
                    }
                } else {
                    // sinon le désactiver
                    listeBoutonAmel[i].setDisable(true);
                }

            } else {
                // si c'est le tour du joueur
                if (tourDuJoueur == 2) {
                    // si le perso n'est pas en vie
                    if (wrk.getPosition(plateau, equipe2.get(i - 2)) == null) {
                        // désactiver le bouton
                        listeBoutonAmel[i].setDisable(true);
                        // indiquer qu'il a été éliminé
                        if (!"R.I.P.".equals(listeTitresAmel[i].getText())) {
                            listeTitresAmel[i].setText("R.I.P.");
                            listeDescAmel[i].setText(tristesse[(int) (Math.random() * (2 - 0 + 1)) + 0]);
                        }
                    }
                    // si le perso est au niveau max
                    if (equipe2.get(i - 2).getNmbrEtoiles() >= 3) {
                        listeBoutonAmel[i].setDisable(true);
                    }
                } else {
                    // sinon le désactiver
                    listeBoutonAmel[i].setDisable(true);
                }
            }
        }
    }

    public void tourDeLEnnemi() {
        if (typePartie == TypePartie.CAMPAGNE && tourDuJoueur == 2) {
//                Thread.sleep(1000);
            Thread th = new Thread(() -> {
                do {
                    try {
                        Thread.sleep(500);
                        ActionIA action = ia.getAction(plateau, equipe2, nbreEtoilesB, persoActionUne);
                        if (action != null) {
                            possibilites.set(action.getNumCase(), action.getAction());
                            persoSelec = action.getPerso();
                            Platform.runLater(() -> {
                                if (tourDuJoueur == 2) {
                                    selectionnerUnePoss(action.getPosition(), action.getNumCase());
                                }
                            });
                            Thread.sleep(500);
                        } else {
                            actionsRestantes = 0;
                        }
                    } catch (InterruptedException ex) {
                    }
                } while (actionsRestantes > 0 && tourDuJoueur == 2);
            });
            th.start();
        }
    }

    public void finDuTour() {

        // baisser la barre deroulante
        ouvrirFermerBarreDeroulante(false, 0.1);

        // reinitialiser l'opacite des persos
        for (ImageView imgPerso : listePersoAPlateau) {
            imgPerso.setOpacity(1.0);
        }
        for (ImageView imgPerso : listePersoBPlateau) {
            imgPerso.setOpacity(1.0);
        }

        // redéfinir le nombre d'actions à 2
        actionsRestantes = 2;

        // changer le joueur qui peut jouer
        if (tourDuJoueur == 1) {
            tourDuJoueur = 2;
            nbreEtoilesB++;
        } else {
            tourDuJoueur = 1;
            nbreEtoilesA++;
        }

        // incrémenter le n° du tour
        noTour++;

        // si le nombre de tours atteint la mort subite
        if (noTour >= NBRE_TOURS_AVANT_MORT_SUBITE) {
            // commencer/continuer la mort subite
            mortSubite = true;
        }

        // vider l'action 1
        persoActionUne = null;

        // brûler les cases
        wrk.enflammerCases(plateau, casesEnFeu);

        // dire au worker que c la fin du tour
        wrk.cestLaFinDuTour(plateau, tourDuJoueur);

        // supprimer les cases en feu
        casesEnFeu = null;

        // si c'est la mort subite, gérer l'enflammage des cases
        if (mortSubite) {
            casesEnFeu = wrk.genererMortSubite(casesEnFeu, noTour, NBRE_TOURS_AVANT_MORT_SUBITE);
        }

        // arreter le radar s'il y en a un
        if (radarEnCours != 0) {
            arreterRadar();
        }

        // desactiver tous les boutons
//        for (Button bouton : listeBoutonAmel) {
//            bouton.setDisable(true);
//        }
        // créer l'animation du debut du tour
        imgFondNoir.setVisible(true);
        TranslateTransition transition = wrk.createAnimationDebutTour(pnDebutTour, true, plateau, tourDuJoueur);

        // mettre a jour les elements de la banniere
        imgBanniere.setImage(assets.getImage("fondBanniereEq" + tourDuJoueur + ".png"));
        lblTourDuJoueur.setText("Tour du joueur " + tourDuJoueur);
        if (mortSubite) {
            lblNoTour.setText("Tour " + noTour);
        } else {
            lblNoTour.setText("Tour " + noTour + " / " + NBRE_TOURS_AVANT_MORT_SUBITE);
        }

        transition.play();
    }

    public void setCasesEnFeu(ArrayList<Integer> casesEnFeu) {
        if (this.casesEnFeu != null) {
            for (Integer index : casesEnFeu) {
                this.casesEnFeu.add(index);
            }
        } else {
            this.casesEnFeu = casesEnFeu;
        }
    }

}
