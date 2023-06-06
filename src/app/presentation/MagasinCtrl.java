/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.Compte;
import app.beans.OffreMagasin;
import app.beans.Perso;
import app.beans.Recompense;
import app.helpers.EncryptionHelper;
import app.workers.WrkMagasin;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author gumyj
 */
public class MagasinCtrl {

    public MagasinCtrl(MainCtrl mainCtrl, Assets assets, WrkMagasin wrkMagasin, Pane pnMagasin) {
        this.mainCtrl = mainCtrl;
        this.assets = assets;
        this.wrkMagasin = wrkMagasin;
        this.pnMagasin = pnMagasin;

        this.sclListeOffres = (ScrollPane) pnMagasin.getChildren().get(1);
        this.vbMagasin = (VBox) sclListeOffres.getContent();
        GridPane gp = (GridPane) vbMagasin.getChildren().get(1);
        this.vbListeOffresSpeciales = (VBox) vbMagasin.getChildren().get(3);
        this.listePnOffresVrac = new Pane[8];
        this.listeImgRessPrix = new ImageView[8];
        for (int i = 0; i < listePnOffresVrac.length; i++) {
            Pane pn = (Pane) gp.getChildren().get(i);
            listePnOffresVrac[i] = pn;
            HBox hb = (HBox) pn.getChildren().get(2);
            listeImgRessPrix[i] = (ImageView) hb.getChildren().get(1);
        }

        Pane pnTresor = (Pane) vbMagasin.getChildren().get(5);
        this.pswCodeTresorerie = (PasswordField) pnTresor.getChildren().get(1);
        this.btnValiderTresorerie = (Button) pnTresor.getChildren().get(2);

    }

    public void remettreEnHaut() {
        this.sclListeOffres.setVvalue(0);
    }

    public void mettreAJourMagasin(Compte c) {
        this.vbListeOffresSpeciales.getChildren().clear();

        OffreMagasin[] offresEnVrac = new OffreMagasin[8];

        ArrayList<OffreMagasin> toutesLesOffres = c.getToutesLesOffresMagasin();
        // 650 + vb
        double vbOffresTaille = 0;

        // OFFRES SPECIALES
        int n = 0;
        for (OffreMagasin o : toutesLesOffres) {
            Pane pnOffre;
            switch (o.getTypeOffre()) {
                case CARTE_COSTUME:
                    pnOffre = createOffreCostume(o);
                    pnOffre.setOnMouseClicked(e -> {
                        if (wrkMagasin.acheterOffre(o, c)) {
                            mainCtrl.recupererRecompense(o.getRecompenses());
                            toutesLesOffres.remove(o);
                            mainCtrl.mettreAJourMagasin();
                        }
                    });
                    vbOffresTaille += pnOffre.getPrefHeight() + 20;
                    vbListeOffresSpeciales.getChildren().add(pnOffre);
                    break;
                case EN_VRAC:
                    offresEnVrac[n] = o;
                    n++;
                    break;
            }
        }
        vbListeOffresSpeciales.setPrefHeight(vbOffresTaille);

        // OFFRES EN VRAC
        for (int i = 0; i < offresEnVrac.length; i++) {
            OffreMagasin o = offresEnVrac[i];

            // maj de la region
            if (o.getRecompenses().get(0).estUnCoffre()) {
                o.getRecompenses().get(0).setRegion(c.getPlusHauteRegionDecouverte());
                o.getRecompenses().get(0).setRegionFixe(false);
            }

            // recuperer les pn de chaque offre
            Pane pn = listePnOffresVrac[i];
            ImageView img = (ImageView) pn.getChildren().get(0);
            Label lbl = (Label) pn.getChildren().get(1);
            HBox hb = (HBox) pn.getChildren().get(2);
            Label lblPrix = (Label) hb.getChildren().get(0);
            if (hb.getChildren().size() < 2) {
                hb.getChildren().add(listeImgRessPrix[i]);
            }
            ImageView imgRessPrix = (ImageView) hb.getChildren().get(1);

            // mettre a jour
            img.setImage(assets.getImageRec(o.getRecompenses().get(0)));
            lbl.setText("x" + o.getRecompenses().get(0).getQuantite());
            if (o.getPrix() == 0) {
                lblPrix.setText("GRATUIT");
                hb.getChildren().remove(imgRessPrix);
            } else {
                lblPrix.setText(o.getPrix() + "");
                imgRessPrix.setImage(assets.getImage("texturesRecompenses/img" + o.getRessPrix().toString() + ".png"));
            }

            pn.setOnMouseClicked(e -> {
                if (wrkMagasin.acheterOffre(o, c)) {
                    mainCtrl.recupererRecompense(o.getRecompenses());
                    toutesLesOffres.set(toutesLesOffres.indexOf(o), wrkMagasin.genereOffreEnVrac(c));
                    mainCtrl.mettreAJourMagasin();
                }
            });
        }

        this.btnValiderTresorerie.setOnAction(e -> {
            validerCodeTresorerie(c);
        });

        this.vbMagasin.setPrefHeight(650 + vbOffresTaille);

    }

    private Pane createOffreCostume(OffreMagasin o) {
        Recompense rec1 = o.getRecompenses().get(0);
        Recompense rec2 = o.getRecompenses().get(1);

        Perso p = rec1.getCartePerso().getPerso();
        Perso sousForme = p.getSousForme();

        Pane pn = new Pane();
        pn.setPrefSize(478, 214);
        pn.setMinSize(478, 214);
        pn.setMaxSize(478, 214);
        pn.getStylesheets().add("resources/css/mesStyles.css");
        pn.getStyleClass().add("pnOffreCostume");
        pn.setCursor(Cursor.HAND);

        Label lblTitre = new Label(o.getNom());
        lblTitre.setPrefSize(407, 36);
        lblTitre.setLayoutX(36);
        lblTitre.setLayoutY(7);
        lblTitre.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitre.setTextFill(Color.WHITE);
        lblTitre.setAlignment(Pos.CENTER);
        pn.getChildren().add(lblTitre);

        Label lblPlus = new Label("+");
        lblPlus.setLayoutX(229);
        lblPlus.setLayoutY(76);
        lblPlus.setFont(Font.font("System", FontWeight.BOLD, 29));
        lblPlus.setTextFill(Color.WHITE);
        pn.getChildren().add(lblPlus);

        Label lblNomCarte = new Label(rec1.getCartePerso().getPerso().getNom());
        lblNomCarte.setPrefSize(123, 21);
        lblNomCarte.setLayoutX(61);
        lblNomCarte.setLayoutY(161);
        lblNomCarte.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblNomCarte.setTextFill(Color.WHITE);
        lblNomCarte.setAlignment(Pos.CENTER);
        pn.getChildren().add(lblNomCarte);

        Label lblQuantite = new Label("x" + rec1.getQuantite());
        lblQuantite.setPrefSize(123, 21);
        lblQuantite.setLayoutX(61);
        lblQuantite.setLayoutY(180);
        lblQuantite.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblQuantite.setTextFill(Color.WHITE);
        lblQuantite.setAlignment(Pos.CENTER);
        pn.getChildren().add(lblQuantite);

        Label lblNomCostume = new Label("Costume " + rec2.getCostume().getNom());
        lblNomCostume.setPrefSize(133, 21);
        lblNomCostume.setLayoutX(308);
        lblNomCostume.setLayoutY(161);
        lblNomCostume.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblNomCostume.setTextFill(Color.WHITE);
        lblNomCostume.setAlignment(Pos.CENTER);
        pn.getChildren().add(lblNomCostume);

        for (int i = 0; i < 3; i++) {
            ImageView imgCarte = new ImageView(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/carte.png"));
            imgCarte.setFitWidth(83);
            imgCarte.setFitHeight(96);
            imgCarte.setPreserveRatio(true);
            imgCarte.setLayoutX(100 - (20 * i));
            imgCarte.setLayoutY(50 + (5 * i));
            imgCarte.setRotate(15.0 - (7.5 * (double) i));
            pn.getChildren().add(imgCarte);
        }

        ImageView imgSousForme1 = new ImageView();
        imgSousForme1.setFitWidth(61);
        imgSousForme1.setFitHeight(63);
        imgSousForme1.setPreserveRatio(true);
        imgSousForme1.setLayoutX(300);
        imgSousForme1.setLayoutY(100);
        pn.getChildren().add(imgSousForme1);

        ImageView imgSousForme2 = new ImageView();
        imgSousForme2.setFitWidth(60);
        imgSousForme2.setFitHeight(63);
        imgSousForme2.setPreserveRatio(true);
        imgSousForme2.setLayoutX(391);
        imgSousForme2.setLayoutY(26);
        pn.getChildren().add(imgSousForme2);

        ImageView imgCostume = new ImageView(assets.getImageCostume(rec2.getCostume()));
        imgCostume.setFitWidth(100);
        imgCostume.setFitHeight(112);
        imgCostume.setPreserveRatio(true);
        imgCostume.setLayoutX(326);
        imgCostume.setLayoutY(42);
        pn.getChildren().add(imgCostume);

        HBox hbPrix = new HBox();
        hbPrix.setPrefSize(100, 45);
        hbPrix.setLayoutX(190);
        hbPrix.setLayoutY(168);
        hbPrix.setAlignment(Pos.CENTER);
        hbPrix.getStylesheets().add("resources/css/mesStyles.css");
        hbPrix.getStyleClass().add("hbOffrePrix");

        Label lblPrix = new Label(o.getPrix() + "");
        lblPrix.setFont(Font.font("System", FontWeight.BOLD, 19));
        lblPrix.setTextFill(Color.WHITE);
        lblPrix.setAlignment(Pos.CENTER_RIGHT);
        lblPrix.setPrefSize(47, 49);
        hbPrix.getChildren().add(lblPrix);

        ImageView imgRessPrix = new ImageView(assets.getImage("texturesRecompenses/img" + o.getRessPrix().toString() + ".png"));
        imgRessPrix.setFitWidth(32);
        imgRessPrix.setFitHeight(37);
        imgRessPrix.setPreserveRatio(true);
        if (o.getPrix() > 0) {
            hbPrix.getChildren().add(imgRessPrix);
        } else {
            lblPrix.setText("GRATUIT");
            lblPrix.setPrefSize(99, 49);
        }

        pn.getChildren().add(hbPrix);

        // gauche : privilegier sous forme
        // droite : projectile ou sous forme
        Image img = assets.getImage("texturesPersos/" + p.getNom() + "/" + rec2.getCostume().getNom() + "/projectileSuper.png");
        if (img != null) {
            imgSousForme2.setImage(img);
            imgSousForme2.setRotate(30.7);
        } else {
            img = assets.getImage("texturesPersos/" + p.getNom() + "/" + rec2.getCostume().getNom() + "/projectile.png");
            if (img != null) {
                imgSousForme2.setImage(img);
                imgSousForme2.setRotate(30.7);
            }
        }

        // si pas de projectile : sous forme les deux
        if (img == null) {
            if (sousForme != null) {
                img = assets.getImage("texturesPersos/" + sousForme.getNom() + "/" + rec2.getCostume().getNom() + "/poseStatique1.png");
                imgSousForme1.setImage(img);
                Perso sousForme2 = sousForme.getSousForme();
                if (sousForme2 != null) {
                    img = assets.getImage("texturesPersos/" + sousForme2.getNom() + "/" + rec2.getCostume().getNom() + "/poseStatique1.png");
                    imgSousForme2.setImage(img);
                }
            }

        }

        return pn;
    }

    private void validerCodeTresorerie(Compte c) {
        try {
            ArrayList<Recompense> recs = wrkMagasin.verifierCode(c, EncryptionHelper.encrypte("SHA-256", pswCodeTresorerie.getText()));

            if (!recs.isEmpty()) {
                mainCtrl.recupererRecompense(recs);
            }
            pswCodeTresorerie.clear();

        } catch (NoSuchAlgorithmException ex) {
        }
    }

    private MainCtrl mainCtrl;
    private Assets assets;
    private WrkMagasin wrkMagasin;
    private Pane pnMagasin;
    private ScrollPane sclListeOffres;
    private Pane[] listePnOffresVrac;
    private ImageView[] listeImgRessPrix;
    private VBox vbMagasin;
    private VBox vbListeOffresSpeciales;
    private PasswordField pswCodeTresorerie;
    private Button btnValiderTresorerie;

}
