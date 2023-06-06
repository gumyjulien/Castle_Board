/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.Assets;
import app.beans.BadgeMaitrise;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.Mission;
import app.beans.Niveau;
import app.beans.Objectif;
import app.beans.Perso;
import app.beans.Recompense;
import app.presentation.JeuCtrl;
import app.presentation.MainCtrl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javax.swing.GroupLayout.Alignment;

/**
 *
 * @author GumyJ01
 */
public class WrkMenu {

    public WrkMenu(Assets assets) {
        this.assets = assets;
    }

    public TranslateTransition createAnimationDetailsNiveau(Pane pnMenu, boolean aller) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(pnMenu);
        if (aller) {
            transition.setToY(600);
        } else {
            transition.setToY(-600);
        }
        transition.setDuration(Duration.seconds(0.2));
        return transition;
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

    public void afficherResultatsPartie(MainCtrl mainCtrl, Compte compteConnecte, NiveauxWrk niveauxWrk, WrkMissions wrkMissions, WrkBadges wrkBadges, Niveau niveauJoue, HashMap<Perso, Compteur> tousLesCompteurs, Pane pnResultats, ImageView[] imgEtoiles) {
        resultatsTermine = false;
        ongletActuel = 0;
        this.mainCtrl = mainCtrl;
        this.imgEtoiles = imgEtoiles;

        ArrayList<Objectif> objectifsInitiaux = new ArrayList<>();
        for (Objectif obj : niveauJoue.getListeObjectifs()) {
            objectifsInitiaux.add(obj.copierObjectif());
        }
        
        for (int i = 0; i < objectifsInitiaux.size(); i++) {
            imgEtoiles[i].setImage(assets.getImage("texturesNiveaux/etoile" + (objectifsInitiaux.get(i).isAccompli() ? "Pleine" : "Vide") + ".png"));
        }

        ArrayList<Mission> missionsInitiales = new ArrayList<>();
        for (Mission m : compteConnecte.getToutesLesMissions()) {
            missionsInitiales.add(m.copierMission());
        }

        ScrollPane scl = (ScrollPane) pnResultats.getChildren().get(3);
        this.pnFond = (Pane) scl.getContent();
        this.gpOnglets = (GridPane) pnFond.getChildren().get(0);

        creerPnFonds();

        pnAfficheObjectifs.getChildren().clear();
        this.pnLesObjectifs = creerFondsObj(3);
        this.lblLesLbl = creerLabelObj(3);
        mettreAJourObjectifs(objectifsInitiaux);

//        this.pnLesMissions = creerMissions(missionsInitiales);
//        mettreAJourMissions(missionsInitiales);

        mettreAJourTableauComplet(tousLesCompteurs);

        boolean victoire = false;
        ArrayList<Integer> valObtenuesObj = niveauxWrk.gererObjectifsNiveau(compteConnecte.getNiveauDansProgressionDesNiveauxJoues(niveauJoue.getIdNiveau()).getListeObjectifs(), tousLesCompteurs);
        if (compteConnecte.getNiveauDansProgressionDesNiveauxJoues(niveauJoue.getIdNiveau()).getNombreObjectifsAccomplis() > 0) {
            if (niveauxWrk.getIdNiveaumax() != niveauJoue.getIdNiveau() && compteConnecte.getPlusHautNiveau().getIdNiveau() < niveauxWrk.getProchainNiveau(niveauJoue.getIdNiveau()).getIdNiveau()) {
                compteConnecte.ajouterNiveauJoue(niveauxWrk.getProchainNiveau(niveauJoue.getIdNiveau()));
                victoire = true;
                if(!niveauJoue.getRegion().equals(niveauxWrk.getProchainNiveau(niveauJoue.getIdNiveau()).getRegion())) {
                    mainCtrl.decouvrirNouvelleRegion(niveauxWrk.getProchainNiveau(niveauJoue.getIdNiveau()).getRegion());
                }
            }
        }
        
        HashMap<BadgeMaitrise, HashMap<Mission, Integer>> missionsBadgesCompletees = wrkBadges.gererMissionsBadges(compteConnecte.getTousLesBadges(), tousLesCompteurs, victoire);
        mainCtrl.afficheProgressionBadges(missionsBadgesCompletees);

        ArrayList<Mission> missionsTraitees = wrkMissions.gererMissions(niveauJoue, compteConnecte.getToutesLesMissions(), tousLesCompteurs, victoire);
        this.pnLesMissions = creerMissions(missionsTraitees);
        mettreAJourMissions(missionsTraitees);

        animationChangerOnglet(0, false).play();

        animationObjectifs(valObtenuesObj, objectifsInitiaux, niveauJoue.getListeObjectifs());

    }

    public boolean suiteResultats() {
        if (!resultatsTermine) {
            switch (ongletActuel) {
                case 0:
                    animationChangerOnglet(1, true).play();
                    break;
                case 1:
                    animationChangerOnglet(2, true).play();
                    break;
            }
        } else {
            mainCtrl.afficherMenu();
        }
        if (ongletActuel >= 1) {
            resultatsTermine = true;
        }
        return resultatsTermine;
    }

    private void creerPnFonds() {
        this.gpAfficheComplet = (GridPane) gpOnglets.getChildren().get(2);

        gpOnglets.getChildren().clear();
        gpOnglets.setLayoutX(0);
        pnAfficheObjectifs = new Pane();

        this.gpOnglets.add(pnAfficheObjectifs, 0, 0);

        ScrollPane sclMissions = new ScrollPane();
        sclMissions.getStylesheets().add("resources/css/mesStyles.css");
        sclMissions.getStyleClass().clear();
        sclMissions.getStyleClass().add("sclMissions");
        sclMissions.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sclMissions.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Pane pnMissions = new Pane();
        pnMissions.setPrefSize(320, 455);
        pnMissions.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        pnMissions.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        sclMissions.setContent(pnMissions);

        VBox vbMissions = new VBox();
        vbMissions.setAlignment(Pos.TOP_CENTER);
        vbMissions.setSpacing(5);
        vbMissions.setPrefWidth(281);
        vbMissions.setLayoutX(16);

        pnMissions.getChildren().add(vbMissions);

        this.vbMissions = vbMissions;

        this.gpOnglets.add(sclMissions, 1, 0);

        if (gpAfficheComplet.getChildren().size() < 7) {
            for (int i = 0; i < 4; i++) {
                ImageView imgLogo = new ImageView();
                imgLogo.setFitWidth(35);
                imgLogo.setFitHeight(40);
                imgLogo.setPreserveRatio(true);

                gpAfficheComplet.add(imgLogo, 0, i + 1);

                for (int j = 0; j < 5; j++) {
                    Label lbl = new Label();
                    lbl.setFont(Font.font("System", FontWeight.BOLD, 19));
                    lbl.setTextFill(Color.WHITE);
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPrefSize(62, 39);

                    gpAfficheComplet.add(lbl, j + 1, i + 1);
                }

            }

            for (int j = 0; j < 5; j++) {
                Label lbl = new Label();
                lbl.setFont(Font.font("System", FontWeight.BOLD, 22));
                lbl.setTextFill(Color.WHITE);
                lbl.setAlignment(Pos.CENTER);
                lbl.setPrefSize(62, 39);

                gpAfficheComplet.add(lbl, j + 1, 5);
            }
        }

        this.gpOnglets.add(gpAfficheComplet, 2, 0);

    }

    private Pane[] creerFondsObj(int nbr) {
        Pane[] res = new Pane[nbr];

        for (int i = 0; i < nbr; i++) {
            Pane pn = new Pane();
            pn.setPrefSize(231, 79);
            pn.getStylesheets().add("resources/css/mesStyles.css");
            pn.setLayoutX(37);
            pn.setLayoutY(i * 85);
            pn.setVisible(false);

            ProgressBar pgb = new ProgressBar();
            pgb.setPrefSize(231, 79);
            pgb.getStylesheets().add("resources/css/mesStyles.css");
            pgb.getStyleClass().add("pgbObjectifTermine");
            pn.getChildren().add(pgb);

            Label lbl = new Label();
            lbl.setFont(Font.font("System", FontWeight.BOLD, 15));
            lbl.setTextFill(Color.WHITE);
            lbl.setAlignment(Pos.CENTER);
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setWrapText(true);
            lbl.setPrefSize(206, 63);
            lbl.setLayoutX(14);
            lbl.setLayoutY(8);
            pn.getChildren().add(lbl);

            ImageView img = new ImageView();
            img.setPreserveRatio(true);
            img.setFitHeight(54);
            img.setFitWidth(54);
            img.setLayoutX(203);
            img.setLayoutY(11);
            img.setImage(assets.getImage("texturesRecompenses/imgAccompli.png"));
            pn.getChildren().add(img);

            res[i] = pn;
            pnAfficheObjectifs.getChildren().add(pn);
        }

        return res;
    }

    private Label[] creerLabelObj(int nbr) {
        Label[] res = new Label[nbr];

        for (int i = 0; i < nbr; i++) {
            Label lbl = new Label();
            lbl.setFont(Font.font("System", FontWeight.BOLD, 16));
            lbl.setTextFill(Color.WHITE);
            lbl.setAlignment(Pos.CENTER);
            lbl.setPrefSize(83, 25);
            lbl.setLayoutX(5);
            lbl.setLayoutY(50 + i * 84);
            res[i] = lbl;
            pnAfficheObjectifs.getChildren().add(lbl);
        }

        return res;
    }

    private Pane[] creerMissions(ArrayList<Mission> toutesLesMissions) {
        Pane[] res = new Pane[toutesLesMissions.size()];
        int i = 0;
        for (Mission m : toutesLesMissions) {
            Pane pn = new Pane();
            pn.setPrefSize(261, 87);
            pn.setMaxSize(261, 87);
            pn.getStylesheets().add("resources/css/mesStyles.css");
            pn.getStyleClass().add("pnMissionEnCours");

            ProgressBar pgb = new ProgressBar();
            pgb.setPrefSize(261, 87);
            pgb.getStylesheets().add("resources/css/mesStyles.css");
            pgb.getStyleClass().add("pgbFondMissionTerminee");
            pn.getChildren().add(pgb);

            Label lbl = new Label();
            lbl.setFont(Font.font("System", FontWeight.BOLD, 15));
            lbl.setTextFill(Color.WHITE);
            lbl.setWrapText(true);
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setPrefSize(231, 42);
            lbl.setLayoutX(14);
            lbl.setLayoutY(8);
            pn.getChildren().add(lbl);

            ProgressBar pgbProgression = new ProgressBar();
            pgbProgression.setPrefSize(231, 15);
            pgbProgression.getStylesheets().add("resources/css/mesStyles.css");
            pgbProgression.getStyleClass().add("pgbMissionEnCours");
            pgbProgression.setLayoutX(15);
            pgbProgression.setLayoutY(56);
            pn.getChildren().add(pgbProgression);

            Label lblProgression = new Label();
            lblProgression.setFont(Font.font("System", FontWeight.BOLD, 14));
            lblProgression.setAlignment(Pos.CENTER);
            lblProgression.setPrefSize(231, 17);
            lblProgression.setLayoutX(15);
            lblProgression.setLayoutY(55);
            pn.getChildren().add(lblProgression);

            ImageView imgLogo = new ImageView();
            imgLogo.setPreserveRatio(true);
            imgLogo.setFitHeight(44);
            imgLogo.setFitWidth(47);
            imgLogo.setLayoutX(-19);
            imgLogo.setLayoutY(21);
            pn.getChildren().add(imgLogo);

            ImageView imgReussi = new ImageView();
            imgReussi.setPreserveRatio(true);
            imgReussi.setFitHeight(57);
            imgReussi.setFitWidth(60);
            imgReussi.setLayoutX(238);
            imgReussi.setLayoutY(18);
            imgReussi.setImage(assets.getImage("texturesRecompenses/imgAccompli.png"));
            pn.getChildren().add(imgReussi);

            this.vbMissions.getChildren().add(pn);
            res[i] = pn;
            i++;
        }

        Pane pnFondMissions = (Pane) ((ScrollPane) this.gpOnglets.getChildren().get(1)).getContent();
        pnFondMissions.setPrefHeight(toutesLesMissions.size() * 93);
        this.vbMissions.setPrefHeight(toutesLesMissions.size() * 93);

        return res;
    }

    private void mettreAJourObjectifs(ArrayList<Objectif> objectifs) {
        for (int i = 0; i < objectifs.size(); i++) {
            mettreAJourObjectif(objectifs.get(i), i);
            pnLesObjectifs[i].setLayoutX(37);
            pnLesObjectifs[i].setLayoutY(i * 85);
        }
    }

    private void mettreAJourObjectif(Objectif obj, int i) {
        Pane pn = pnLesObjectifs[i];
        ProgressBar pgb = (ProgressBar) pn.getChildren().get(0);
        Label lbl = (Label) pn.getChildren().get(1);
        ImageView img = (ImageView) pn.getChildren().get(2);

        pn.getStyleClass().clear();
        img.setVisible(false);
        pgb.setProgress(0.0);
        if (obj.isAccompli()) {
            pgb.setProgress(1.0);
            img.setVisible(true);
        } else {
            pn.getStyleClass().add("pnObj" + obj.getDifficulte());
        }

        lbl.setText(obj.toString());
    }

    private void mettreAJourMissions(ArrayList<Mission> toutesLesMissions) {
        int i = 0;
        for (Mission m : toutesLesMissions) {
            Pane pn = pnLesMissions[i];
            ProgressBar pgb = (ProgressBar) pn.getChildren().get(0);
            Label lbl = (Label) pn.getChildren().get(1);
            ProgressBar pgbProgression = (ProgressBar) pn.getChildren().get(2);
            Label lblProgression = (Label) pn.getChildren().get(3);
            ImageView imgLogo = (ImageView) pn.getChildren().get(4);
            ImageView imgAccompli = (ImageView) pn.getChildren().get(5);

            pgb.setProgress(0.0);
            lbl.setText(m.toString());
            pgbProgression.setProgress(m.getProgressionPourcentage());
            lblProgression.setText(m.getProgression() + " / " + m.getNbrObjectif());
            lblProgression.setTextFill(Color.WHITE);
            if (m.isTerminee()) {
                lblProgression.setText("Terminée");
                lblProgression.setTextFill(Color.WHITE);
                pgbProgression.getStyleClass().clear();
                pgbProgression.getStyleClass().add("pgbMissionTerminee");
                pn.getStyleClass().clear();
                pn.getStyleClass().add("pnMissionTerminee");
            }
            switch (m.getPorteeMission()) {
                case PERSO:
                    imgLogo.setImage(assets.getImage("texturesPersos/" + m.getPerso().getNom() + "/" + m.getPerso().getCostumeSelec().getNom() + "/logo.png"));
                    break;
                case REGION:
                    imgLogo.setImage(assets.getImage("texturesNiveaux/" + m.getRegion().getNom() + "/logo.png"));
                    break;
                default:
                    imgLogo.setImage(null);
            }
            imgAccompli.setVisible(false);
            i++;
        }
    }

    private void mettreAJourTableauComplet(HashMap<Perso, Compteur> tousLesCompteurs) {

        int[] valsTtl = new int[]{0, 0, 0, 0, 0};

        int i = 0;
        for (Perso p : tousLesCompteurs.keySet()) {
            ImageView img = (ImageView) gpAfficheComplet.getChildren().get(6 + 6 * i);
            img.setImage(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/logo.png"));

            Compteur c = tousLesCompteurs.get(p);

            Integer[] vals = new Integer[]{c.getNombreDgSubis(), c.getNombreDgInfliges(), c.getNombreSoinProcure(), c.getNombreSoinRecu(), c.getNombreCasesParcourues()};

            int j = 1;
            for (Integer val : vals) {
                Label lbl = (Label) gpAfficheComplet.getChildren().get(6 + 6 * i + j);
                lbl.setText(val + "");
                valsTtl[j - 1] += val;
                j++;
            }
            i++;
        }

        int j = 0;
        for (int val : valsTtl) {
            Label lbl = (Label) gpAfficheComplet.getChildren().get(6 + 6 * i + j);
            lbl.setText(val + "");
            j++;
        }

    }

    private void animationObjectifs(ArrayList<Integer> compteurs, ArrayList<Objectif> objectifsInit, ArrayList<Objectif> objectifsFin) {
        for (int i = 0; i < pnLesObjectifs.length; i++) {
            final int x = i;
            int nbr = compteurs.get(i);
            final Objectif obj = objectifsInit.get(i);
            final Objectif objFin = objectifsFin.get(i);
            pnLesObjectifs[i].setLayoutX(-74);

            TranslateTransition transition = animationMouvementObj(pnLesObjectifs[i], i);
            final Label lbl = lblLesLbl[i];
            transition.setOnFinished(e -> {
                ScaleTransition transitionLbl = animationLabelObjectif(lbl, nbr, obj.getNombre());
                transitionLbl.setOnFinished(e2 -> {
                    if (!obj.isAccompli()) {
                        if (objFin.isAccompli()) {
                            Thread th = animationPgbObjectifs(x);
                            th.start();
                        } else {
                            ImageView img = (ImageView) pnLesObjectifs[x].getChildren().get(2);
                            img.setImage(assets.getImage("texturesRecompenses/imgÉchec.png"));
                            img.setVisible(true);
                        }
                    } else {
                        mettreAJourObjectif(obj, x);
                    }
                });
                transitionLbl.play();
            });

            transition.play();
        }
    }

    private TranslateTransition animationMouvementObj(Pane pn, int i) {
        TranslateTransition transition = new TranslateTransition();

        transition.setNode(pn);
        transition.setDuration(Duration.seconds(0.3));
        transition.setDelay(Duration.seconds(0.3 * i));
        transition.setToX(100);

        pn.setVisible(true);

        return transition;
    }

    private ScaleTransition animationLabelObjectif(Label lbl, int nbrObtenu, int nbrTotal) {
        ScaleTransition transition = new ScaleTransition();

        transition.setNode(lbl);
        transition.setDuration(Duration.seconds(2));
        transition.setFromX(0.1);
        transition.setFromY(0.1);
        transition.setToX(1);
        transition.setToY(1);

        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                lbl.setText(0 + " / " + nbrTotal);
            });
            for (int i = 0; i < nbrObtenu; i++) {
                try {
                    final int x = i;
                    Platform.runLater(() -> {
                        lbl.setText(x + " / " + nbrTotal);
                    });
                    Thread.sleep(1500 / nbrObtenu);
                } catch (InterruptedException ex) {
                }
            }
            Platform.runLater(() -> {
                lbl.setText(nbrObtenu + " / " + nbrTotal);
            });
        });

        th.start();
        return transition;
    }

    private Thread animationPgbObjectifs(int x) {

        Thread th = new Thread(() -> {
            int frames = 100;
            for (int i = 0; i < frames; i++) {
                try {
                    ProgressBar pgb = (ProgressBar) pnLesObjectifs[x].getChildren().get(0);
                    final double val = (int) i / 10.0;
                    Platform.runLater(() -> {
                        pgb.setProgress(val);
                        this.imgEtoiles[x].setImage(assets.getImage("texturesNiveaux/etoilePleine.png"));
                    });
                    Thread.sleep(200 / frames);
                } catch (InterruptedException ex) {
                }
            }
            ImageView img = (ImageView) pnLesObjectifs[x].getChildren().get(2);
            img.setImage(assets.getImage("texturesRecompenses/imgAccompli.png"));
            img.setVisible(true);
        });

        return th;

    }

    private TranslateTransition animationChangerOnglet(int onglet, boolean animation) {
        TranslateTransition transition = new TranslateTransition();
        transition.setToX(onglet * -320);
        if (animation) {
            transition.setDuration(Duration.seconds(0.5));
        } else {
            transition.setDuration(Duration.seconds(0.0001));
        }
        transition.setNode(gpOnglets);

        transition.setOnFinished(e -> {
            this.ongletActuel = onglet;
        });

        return transition;
    }

    private MainCtrl mainCtrl;
    private Assets assets;
    private Pane pnFond;
    private GridPane gpOnglets;
    private GridPane gpAfficheComplet;
    private Pane pnAfficheObjectifs;
    private ImageView[] imgEtoiles;
    private Pane[] pnLesObjectifs;
    private Label[] lblLesLbl;
    private Pane[] pnLesMissions;
    private Pane pnAfficheMissions;

    private Pane pnAfficheComplet;

    private HBox hbOnglets;
    private VBox vbMissions;

    private int ongletActuel;
    private boolean resultatsTermine;

}
