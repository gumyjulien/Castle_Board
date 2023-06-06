/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.BadgeMaitrise;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Mission;
import app.beans.Objectif;
import app.beans.Perso;
import app.beans.Recompense;
import app.workers.WrkBadges;
import app.workers.WrkCartes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author gumyj
 */
public class BadgesCtrl {

    public BadgesCtrl(MainCtrl mainCtrl, WrkBadges wrkBadges, Assets assets, Pane pnBadges, Pane pnInfosBadges, ImageView imgFondQuitter) {
        this.assets = assets;
        this.mainCtrl = mainCtrl;
        this.wrkBadges = wrkBadges;
        this.pnBadges = pnBadges;
        this.pnInfosBadges = pnInfosBadges;
        this.lblTitre = (Label) this.pnBadges.getChildren().get(0);
        this.sclBadges = (ScrollPane) this.pnBadges.getChildren().get(1);
        this.imgFondQuitter = imgFondQuitter;

        // recuperer les elements graphiques
        this.lblInfosTitre = (Label) pnInfosBadges.getChildren().get(0);
        Pane pnContenu = (Pane) pnInfosBadges.getChildren().get(1);
        this.imgInfosBadge = (ImageView) pnContenu.getChildren().get(0);
        this.imgInfosLogo = (ImageView) pnContenu.getChildren().get(1);
        this.pgbInfosPoints = (ProgressBar) pnContenu.getChildren().get(2);
        this.lblInfosPoints = (Label) pnContenu.getChildren().get(4);
        GridPane gpMethodes = (GridPane) pnContenu.getChildren().get(6);
        this.lblInfosMethodesNom = new Label[3];
        this.lblInfosMethodesPts = new Label[3];
        this.imgInfosLogoPts = new ImageView[3];
        this.pnInfosRecompenses = new Pane[6];
        for (int i = 0; i < 3; i++) {
            this.lblInfosMethodesNom[i] = (Label) gpMethodes.getChildren().get(i);
        }
        for (int i = 0; i < 3; i++) {
            this.lblInfosMethodesPts[i] = (Label) gpMethodes.getChildren().get(3 + i);
        }
        for (int i = 0; i < 3; i++) {
            this.imgInfosLogoPts[i] = (ImageView) gpMethodes.getChildren().get(6 + i);
        }
        this.btnAmeliorer = (Button) pnInfosBadges.getChildren().get(2);
        Pane pnEtapes = (Pane) pnInfosBadges.getChildren().get(3);
        this.pnInfosFondActuel = (Pane) pnEtapes.getChildren().get(0);
        GridPane gpEtapes = (GridPane) pnEtapes.getChildren().get(1);
        for (int i = 0; i < 6; i++) {
            this.pnInfosRecompenses[i] = (Pane) gpEtapes.getChildren().get(7 + i);
        }
    }

    public void construirePnBadges(ArrayList<BadgeMaitrise> tousLesBadges) {
        VBox vbInventaire = (VBox) sclBadges.getContent();
        int nombreDeBadges = tousLesBadges.size();
        int nombreLignes = (nombreDeBadges + (3 - (nombreDeBadges % 3))) / 3;
        if (nombreDeBadges % 3 == 0) {
            nombreLignes--;
        }

        if (nombreLignes < 3) {
            vbInventaire.setPrefSize(512, 533);
        } else {
            vbInventaire.setPrefSize(512, nombreLignes * 178 + 20);
        }
        vbInventaire.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        vbInventaire.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        for (int i = 0; i < nombreLignes; i++) {
            HBox hbLigneBadges = new HBox();
            hbLigneBadges.setPrefSize(500, 159);
            hbLigneBadges.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            hbLigneBadges.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            for (int j = 0; j < 3; j++) {
                Pane pnBadge = new Pane();
                pnBadge.setPrefSize(167, 155);
                pnBadge.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pnBadge.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

                ImageView imgBadge = new ImageView();
                imgBadge.setFitWidth(123);
                imgBadge.setFitHeight(124);
                imgBadge.setLayoutX(28);
                imgBadge.setPreserveRatio(true);
                pnBadge.getChildren().add(imgBadge);

                ProgressBar pgbNmbrPoints = new ProgressBar();
                pgbNmbrPoints.setPrefSize(95, 36);
                pgbNmbrPoints.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pgbNmbrPoints.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pgbNmbrPoints.setLayoutX(44);
                pgbNmbrPoints.setLayoutY(119);
                pgbNmbrPoints.getStylesheets().add("resources/css/mesStyles.css");
                pgbNmbrPoints.getStyleClass().add("pgbCartes");
                pnBadge.getChildren().add(pgbNmbrPoints);

                ImageView imgPoint = new ImageView();
                imgPoint.setFitWidth(47);
                imgPoint.setFitHeight(45);
                imgPoint.setLayoutX(20);
                imgPoint.setLayoutY(115);
                imgPoint.setPreserveRatio(true);
                pnBadge.getChildren().add(imgPoint);

                ImageView imgLogo = new ImageView();
                imgLogo.setFitWidth(40);
                imgLogo.setFitHeight(40);
                imgLogo.setLayoutX(66);
                imgLogo.setLayoutY(52);
                imgLogo.setPreserveRatio(true);
                imgLogo.setOpacity(0.5);
                pnBadge.getChildren().add(imgLogo);

                Label lblNmbrPoints = new Label();
                lblNmbrPoints.setFont(Font.font("System", FontWeight.BOLD, 16));
                lblNmbrPoints.setTextFill(Color.WHITE);
                lblNmbrPoints.setWrapText(true);
                lblNmbrPoints.setAlignment(Pos.CENTER);
                lblNmbrPoints.setPrefSize(93, 40);
                lblNmbrPoints.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblNmbrPoints.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblNmbrPoints.setLayoutX(47);
                lblNmbrPoints.setLayoutY(118);
                pnBadge.getChildren().add(lblNmbrPoints);
                
                Button btnNotif = new Button();
                btnNotif.setPrefSize(29, 31);
                btnNotif.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                btnNotif.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                btnNotif.setEllipsisString("99");
                btnNotif.setFont(Font.font("System", FontWeight.BOLD, 15));
                btnNotif.setTextFill(Color.WHITE);
                btnNotif.setStyle("-fx-background-color: red;");
                btnNotif.setCursor(Cursor.HAND);
                btnNotif.setLayoutX(117);
                btnNotif.setLayoutY(14);
                pnBadge.getChildren().add(btnNotif);

                hbLigneBadges.getChildren().add(pnBadge);
            }
            vbInventaire.getChildren().add(hbLigneBadges);
        }
    }
    
    public void remettreEnHaut() {
        this.sclBadges.setVvalue(0);
    }

    public void mettreAJourBadges(ArrayList<BadgeMaitrise> tousLesBadges, Compte compte) {
        
        this.lblTitre.setText("Badges (" + WrkBadges.getNbrBadges(tousLesBadges) + "/" + WrkBadges.getTousLesBadges().size() + ")");

        Collections.sort(tousLesBadges);

        VBox vbInventaire = (VBox) sclBadges.getContent();
        for (int i = 0; i < vbInventaire.getChildren().size(); i++) {
            HBox hbLigne = (HBox) vbInventaire.getChildren().get(i);

            for (int j = 0; j < 3; j++) {
                BadgeMaitrise badge = null;
                if (i * 3 + j < tousLesBadges.size()) {
                    badge = tousLesBadges.get(i * 3 + j);
                }

                // recuperer les elements graphiques de la carte
                Pane pnBadge = (Pane) hbLigne.getChildren().get(j);
                ImageView imgBadge = (ImageView) pnBadge.getChildren().get(0);
                ProgressBar pgbNmbrPoints = (ProgressBar) pnBadge.getChildren().get(1);
                ImageView imgPoint = (ImageView) pnBadge.getChildren().get(2);
                ImageView imgLogo = (ImageView) pnBadge.getChildren().get(3);
                Label lblNmbrPoints = (Label) pnBadge.getChildren().get(4);
                Button btnNotif = (Button) pnBadge.getChildren().get(5);

                if (badge != null) {
                    imgBadge.setVisible(true);
                    imgBadge.setCursor(Cursor.HAND);
                    imgLogo.setCursor(Cursor.HAND);

                    final BadgeMaitrise leBadge = badge;

                    imgBadge.setOnMouseClicked(e -> {
                        afficherBadge(tousLesBadges, leBadge, compte);
                    });
                    imgLogo.setOnMouseClicked(e -> {
                        afficherBadge(tousLesBadges, leBadge, compte);
                    });
                    btnNotif.setOnMouseClicked(e -> {
                        afficherBadge(tousLesBadges, leBadge, compte);
                    });

                    imgBadge.setImage(assets.getImage("texturesBadges/img" + badge.getRarete() + ".png"));
                    if (badge.getType().equals(BadgeMaitrise.Type.PERSO)) {
                        Perso p = badge.getCarte().getPerso();
                        imgLogo.setImage(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/logo.png"));
                    } else {
                        imgLogo.setImage(assets.getImage("texturesBadges/" + badge.getNom() + ".png"));
                    }
                    if (badge.getNiveau() == 6) {
                        imgLogo.setLayoutX(70);
                        imgLogo.setLayoutY(48);
                    } else {
                        imgLogo.setLayoutX(66);
                        imgLogo.setLayoutY(52);
                    }

                    imgPoint.setImage(assets.getImage("texturesRecompenses/imgPointDeMaitrise.png"));
                    if (badge.getNiveau() == badge.getNiveauMax()) {
                        pgbNmbrPoints.setProgress(1);
                        lblNmbrPoints.setText(badge.getPoints() + "");
                    } else {
                        pgbNmbrPoints.setProgress((double) badge.getPoints() / (double) badge.getPointsRequis());
                        lblNmbrPoints.setText(badge.getPoints() + " / " + badge.getPointsRequis());
                        if (badge.getPoints() >= badge.getPointsRequis()) {
                            pgbNmbrPoints.getStyleClass().clear();
                            pgbNmbrPoints.getStyleClass().add("pgbCartesAmel");
                        } else {
                            pgbNmbrPoints.getStyleClass().clear();
                            pgbNmbrPoints.getStyleClass().add("pgbCartes");
                        }
                    }
                    if (lblNmbrPoints.getText().length() > 9) {
                        lblNmbrPoints.setFont(Font.font("System", FontWeight.BOLD, 11));
                    } else if (lblNmbrPoints.getText().length() > 7) {
                        lblNmbrPoints.setFont(Font.font("System", FontWeight.BOLD, 13));
                    } else {
                        lblNmbrPoints.setFont(Font.font("System", FontWeight.BOLD, 15));
                    }
                    int nbrRecs = 0;
                    for (int lvl = 0; lvl < badge.getNiveau(); lvl++) {
                        if(!badge.getRecsRecuperees()[lvl]) {
                            nbrRecs++;
                        }
                    }
                    btnNotif.setText(nbrRecs + "");
                    btnNotif.setVisible(nbrRecs > 0);
                    imgBadge.setVisible(true);
                    imgPoint.setVisible(true);
                    pgbNmbrPoints.setVisible(true);
                    lblNmbrPoints.setVisible(true);
                    imgLogo.setVisible(true);
                } else {
                    imgBadge.setVisible(false);
                    imgPoint.setVisible(false);
                    pgbNmbrPoints.setVisible(false);
                    lblNmbrPoints.setVisible(false);
                    imgLogo.setVisible(false);
                    btnNotif.setVisible(false);
                }

            }
        }
    }

    private void afficherBadge(ArrayList<BadgeMaitrise> tousLesBadges, BadgeMaitrise b, Compte compte) {
        this.pnInfosBadges.setVisible(true);
        this.imgFondQuitter.setVisible(true);

        // mettre a jour
        this.lblInfosTitre.setText(b.getNom());
        this.lblInfosTitre.setFont(Font.font("System", FontWeight.BOLD, (b.getNom().length() > 30 ? 24 : 30)));
        this.imgInfosBadge.setImage(assets.getImage("texturesBadges/img" + b.getRarete() + ".png"));
        if (b.getType().equals(BadgeMaitrise.Type.PERSO)) {
            imgInfosLogo.setImage(assets.getImage("texturesPersos/" + b.getCarte().getPerso().getNom() + "/" + b.getCarte().getPerso().getCostumeSelec().getNom() + "/logo.png"));
        } else {
            imgInfosLogo.setImage(assets.getImage("texturesBadges/" + b.getNom() + ".png"));
        }
        if (b.getNiveau() == 6) {
            this.imgInfosLogo.setLayoutX(75);
        } else {
            this.imgInfosLogo.setLayoutX(70);
        }
        if (b.getNiveau() == b.getNiveauMax()) {
            this.pgbInfosPoints.setProgress(1);
            this.lblInfosPoints.setText(b.getPoints() + "");
        } else {
            this.pgbInfosPoints.setProgress((double) b.getPoints() / (double) b.getPointsRequis());
            this.lblInfosPoints.setText(b.getPoints() + " / " + b.getPointsRequis());
        }

        TreeMap<Mission, Integer> methPtsTriees = b.getMethodesPtsTriees();
        ArrayList<Mission> toutesLesClefs = new ArrayList<>();
        for (Mission m : methPtsTriees.keySet()) {
            toutesLesClefs.add(m);
        }
        for (int i = 0; i < imgInfosLogoPts.length; i++) {
            if (i < toutesLesClefs.size()) {
                Mission m = toutesLesClefs.get(i);
                this.lblInfosMethodesNom[i].setText(m.miniToString());
                int taille = m.miniToString().length();
                this.lblInfosMethodesNom[i].setFont(Font.font("System", FontWeight.BOLD, (taille < 25 ? 12 : (taille < 35 ? 10 : 8))));
                this.lblInfosMethodesPts[i].setText(m.getRecompenses().get(0).getQuantite() + "");
                if (m.getRecompenses().get(0).getQuantite() == 999) {
                    this.lblInfosMethodesPts[i].setText("???");
                } else if (m.getRecompenses().get(0).getQuantite() == 0) {
                    this.lblInfosMethodesPts[i].setText("X");
                }
                this.lblInfosMethodesNom[i].setVisible(true);
                this.lblInfosMethodesPts[i].setVisible(true);
                this.imgInfosLogoPts[i].setVisible(true);
            } else {
                this.lblInfosMethodesNom[i].setVisible(false);
                this.lblInfosMethodesPts[i].setVisible(false);
                this.imgInfosLogoPts[i].setVisible(false);
            }
        }

        int indexRec = 0;
        for (Pane pn : pnInfosRecompenses) {
            Pane pnFond = pn;
            ImageView imgRec = (ImageView) pn.getChildren().get(0);
            Label lblQuantite = (Label) pn.getChildren().get(1);
            Label lblRegion = (Label) pn.getChildren().get(2);
            if (b.getNiveau() <= indexRec) {
                Recompense rec = b.getRecompenses()[indexRec];
                String color = "AAAAAA";
                if(rec.getCostume() != null) {
                    switch(rec.getCostume().getRarete()) {
                        case SPECIAL:
                            color = "FFC95C";
                            break;
                        case EPIQUE:
                            color = "C500E0";
                            break;
                        case MYTHIQUE:
                            color = "FF0202";
                            break;
                    }
                }
                pnFond.setStyle("-fx-background-color: #" + color + "; -fx-background-radius: 15");
                pnFond.setCursor(Cursor.DEFAULT);
                pnFond.setOnMouseClicked(e -> {
                });
            } else {
                if (!b.getRecsRecuperees()[indexRec]) {
                    pnFond.setStyle("-fx-background-color: #FFF002; -fx-background-radius: 15");
                } else {
                    pnFond.setStyle("-fx-background-color: #8BDD15; -fx-background-radius: 15");
                }
            }

            // gerer les recompenses
            String quantite = "";
            String region = "";
            if (!b.getRecsRecuperees()[indexRec]) {
                Recompense recompense = b.getRecompenses()[indexRec];
                quantite = recompense.getQuantite() + "";
                if (recompense.estUnCoffre() && recompense.getCartePerso() == null) {
                    recompense.setRegion(compte.getPlusHauteRegionDecouverte());
                    region = "Région ";
                    if (!recompense.isRegionFixe() && recompense.getRegion().getId() > 1) {
                        region += "1-" + recompense.getRegion().getId();
                    } else {
                        region += recompense.getRegion().getId();
                    }
                }
                imgRec.setImage(assets.getImageRec(recompense));
                if (b.getNiveau() > indexRec) {
                    pn.setCursor(Cursor.HAND);
                    final int i = indexRec;
                    pn.setOnMouseClicked(e -> {
                        ArrayList<Recompense> rec = new ArrayList<>();
                        rec.add(b.getRecompenses()[i]);
                        mainCtrl.recupererRecompense(rec);
                        b.recupRecompense(i);
                        afficherBadge(tousLesBadges, b, compte);
                    });
                } else {
                    pn.setCursor(Cursor.DEFAULT);
                    pn.setOnMouseClicked(e -> {
                    });
                }
            } else if (b.getNiveau() > indexRec && b.getRecsRecuperees()[indexRec]) {
                imgRec.setImage(assets.getImage("texturesRecompenses/imgAccompli.png"));
                pn.setCursor(Cursor.DEFAULT);
                pn.setOnMouseClicked(e -> {
                });
            }

            lblQuantite.setText(quantite);
            lblRegion.setText(region);
            indexRec++;
        }

        this.pnInfosFondActuel.setLayoutX((b.getNiveau() - 1) * 78);
        this.pnInfosFondActuel.setVisible(true);
        if (b.getNiveau() == 0) {
            this.pnInfosFondActuel.setVisible(false);
        }

        this.btnAmeliorer.setOnMouseClicked(e -> {
            ameliorerBadge(tousLesBadges, compte, b);
        });

    }

    public void cacherInfos() {
        this.pnInfosBadges.setVisible(false);
        this.imgFondQuitter.setVisible(false);
    }

    public void ameliorerBadge(ArrayList<BadgeMaitrise> tousLesBadges, Compte compte, BadgeMaitrise b) {
        wrkBadges.ameliorerBadge(b);
        mettreAJourBadges(tousLesBadges, compte);
        afficherBadge(tousLesBadges, b, compte);
    }

    private Assets assets;
    private MainCtrl mainCtrl;
    private WrkBadges wrkBadges;
    private Pane pnBadges;
    private Pane pnInfosBadges;
    private Label lblTitre;
    private ScrollPane sclBadges;
    private ImageView imgFondQuitter;

    private Label lblInfosTitre;
    private ImageView imgInfosBadge;
    private ImageView imgInfosLogo;
    private ProgressBar pgbInfosPoints;
    private Label lblInfosPoints;
    private ImageView[] imgInfosLogoPts;
    private Label[] lblInfosMethodesNom;
    private Label[] lblInfosMethodesPts;
    private Pane[] pnInfosRecompenses;
    private Pane pnInfosFondActuel;
    private Button btnAmeliorer;

}
