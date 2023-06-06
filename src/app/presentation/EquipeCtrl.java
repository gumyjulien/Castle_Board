/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Perso;
import app.beans.persos.joueur.Medecin;
import app.workers.WrkCartes;
import app.workers.WrkEquipe;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author gumyj
 */
public class EquipeCtrl {

    public EquipeCtrl(MainCtrl mainCtrl, WrkCartes wrkCartes, Pane pnInfos, ImageView imgFondQuitter) {
        this.mainCtrl = mainCtrl;
        this.wrkCartes = wrkCartes;
        this.pnInfosPerso = pnInfos;
        this.imgFondQuitter = imgFondQuitter;
        this.wrkEquipe = new WrkEquipe();

        // recuperer les elements graphiques
//        int oui = 0;
//        for (Node node : pnInfos.getChildren()) {
//            System.out.println(oui + ": " + node.getClass());
//            if(node instanceof GridPane) {
//                int non = 0;
//                for (Node nodes : ((GridPane) node).getChildren()) {
//                    System.out.println("  " + non + ": " + nodes.getClass());
//                    non++;
//                }
//            }
//            
//            oui++;
//        }
        this.lblInfoNom = (Label) pnInfos.getChildren().get(0);
        this.imgInfoCarte = (ImageView) pnInfos.getChildren().get(1);
        this.lblInfoNiv = (Label) pnInfos.getChildren().get(2);
        this.lblInfoDesc = (Label) pnInfos.getChildren().get(3);
        this.pgbInfoCartes = (ProgressBar) pnInfos.getChildren().get(4);
        this.imgInfoEtoileNiv = (ImageView) pnInfos.getChildren().get(5);
        this.lblInfoCartes = (Label) pnInfos.getChildren().get(6);
        // pause - fonds x 3
        this.listeImgAttr = new ImageView[5];
        GridPane gpAttr = (GridPane) pnInfos.getChildren().get(10);
        for (int i = 0; i < listeImgAttr.length; i++) {
            this.listeImgAttr[i] = (ImageView) gpAttr.getChildren().get(i);
        }
        VBox vb = (VBox) gpAttr.getChildren().get(5);
        this.lblInfoSuperBloque = (Label) vb.getChildren().get(0);
        this.lblInfoTitreSuper = (Label) vb.getChildren().get(1);
        this.lblInfoSuper = (Label) vb.getChildren().get(2);
        this.vbSuper = vb;
        this.listeLblValAttr = new Label[4];
        this.listeLblValPlus = new Label[4];
        for (int i = 0; i < listeLblValAttr.length; i++) {
            HBox hb = (HBox) gpAttr.getChildren().get(6 + i);
            this.listeLblValAttr[i] = (Label) hb.getChildren().get(0);
            this.listeLblValPlus[i] = (Label) hb.getChildren().get(1);
        }
        // pause - fond
        this.listeFondsAmel = new Pane[3];
        for (int i = 0; i < listeFondsAmel.length; i++) {
            this.listeFondsAmel[i] = (Pane) pnInfos.getChildren().get(13 + i);
        }
        GridPane gpAmel = (GridPane) pnInfos.getChildren().get(16);
        this.listeInfoEtoiles = new ImageView[3];
        for (int i = 0; i < listeInfoEtoiles.length; i++) {
            this.listeInfoEtoiles[i] = (ImageView) gpAmel.getChildren().get(i);
        }
        this.listeTitresAmel = new Label[3];
        this.listeDescAmel = new Label[3];
        for (int i = 0; i < listeDescAmel.length; i++) {
            VBox vb2 = (VBox) gpAmel.getChildren().get(3 + i);
            this.listeTitresAmel[i] = (Label) vb2.getChildren().get(0);
            this.listeDescAmel[i] = (Label) vb2.getChildren().get(1);
        }
        this.btnInfoAmeliorer = (Button) pnInfos.getChildren().get(17);
        HBox hb = (HBox) pnInfos.getChildren().get(18);
        this.lblInfoPrix = (Label) hb.getChildren().get(0);
        this.imgInfoRessPrix = (ImageView) hb.getChildren().get(1);
        this.btnInfoAjout = (Button) pnInfos.getChildren().get(19);

        btnInfoAjout.getStylesheets().add("resources/css/mesStyles.css");
    }

    public void construirePnEquipe(Pane pnEquipe, ArrayList<CartePerso> toutesLesCartes, Assets assets) {
        this.assets = assets;
        this.pnEquipe = pnEquipe;
        this.pnChoixPlace = (Pane) this.pnEquipe.getChildren().get(3);

        Label lblNombreCartes = (Label) pnEquipe.getChildren().get(0);
        lblNombreCartes.setText("Équipe (" + wrkCartes.getNombreDeCartesDebloquees(toutesLesCartes) + "/" + toutesLesCartes.size() + ")");

        HBox hbEquipe = (HBox) pnEquipe.getChildren().get(1);
        // creer les 4 cartes de l'equipe
        for (int i = 0; i < 4; i++) {
            Pane pnCarte = new Pane();
            pnCarte.setPrefSize(125, 155);
            pnCarte.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            pnCarte.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            ImageView imgCarte = new ImageView();
            imgCarte.setFitWidth(100);
            imgCarte.setFitHeight(112);
            imgCarte.setLayoutX(16);
            pnCarte.getChildren().add(imgCarte);

            ImageView imgEtoile = new ImageView();
            imgEtoile.setFitWidth(29);
            imgEtoile.setFitHeight(31);
            imgEtoile.setLayoutX(12);
            imgEtoile.setLayoutY(114);
            pnCarte.getChildren().add(imgEtoile);

            ProgressBar pgbNmbrCartes = new ProgressBar();
            pgbNmbrCartes.setPrefSize(67, 31);
            pgbNmbrCartes.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            pgbNmbrCartes.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            pgbNmbrCartes.setLayoutX(41);
            pgbNmbrCartes.setLayoutY(114);
            pgbNmbrCartes.getStylesheets().add("resources/css/mesStyles.css");
            pgbNmbrCartes.getStyleClass().add("pgbCartes");
            pnCarte.getChildren().add(pgbNmbrCartes);

            Label lblNmbrCartePerso = new Label();
            lblNmbrCartePerso.setFont(Font.font("System", FontWeight.BOLD, 15));
            lblNmbrCartePerso.setTextFill(Color.WHITE);
            lblNmbrCartePerso.setWrapText(true);
            lblNmbrCartePerso.setAlignment(Pos.CENTER);
            lblNmbrCartePerso.setPrefSize(60, 21);
            lblNmbrCartePerso.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            lblNmbrCartePerso.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            lblNmbrCartePerso.setLayoutX(45);
            lblNmbrCartePerso.setLayoutY(119);
            pnCarte.getChildren().add(lblNmbrCartePerso);

            hbEquipe.getChildren().add(pnCarte);
        }

        this.sclInventaire = (ScrollPane) pnEquipe.getChildren().get(2);
        VBox vbInventaire = (VBox) sclInventaire.getContent();
        int nombreDeCartes = toutesLesCartes.size();
        int nombreLignes = (nombreDeCartes + (4 - (nombreDeCartes % 4))) / 4;
        if (nombreDeCartes % 4 == 0) {
            nombreLignes--;
        }

        if (nombreLignes < 3) {
            vbInventaire.setPrefSize(499, 350);
        } else {
            vbInventaire.setPrefSize(499, nombreLignes * 155 + 20);
        }
        vbInventaire.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        vbInventaire.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // creer l'inventaire de toutes les cartes
        for (int i = 0; i < nombreLignes; i++) {
            HBox hbLigneCartes = new HBox();
            hbLigneCartes.setPrefSize(500, 159);
            hbLigneCartes.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            hbLigneCartes.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            for (int j = 0; j < 4; j++) {
                Pane pnCarte = new Pane();
                pnCarte.setPrefSize(125, 155);
                pnCarte.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pnCarte.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

                ImageView imgCarte = new ImageView();
                imgCarte.setFitWidth(95);
                imgCarte.setFitHeight(112);
                imgCarte.setLayoutX(16);
                pnCarte.getChildren().add(imgCarte);

                ImageView imgEtoile = new ImageView();
                imgEtoile.setFitWidth(29);
                imgEtoile.setFitHeight(31);
                imgEtoile.setLayoutX(12);
                imgEtoile.setLayoutY(114);
                pnCarte.getChildren().add(imgEtoile);

                ProgressBar pgbNmbrCartes = new ProgressBar();
                pgbNmbrCartes.setPrefSize(67, 31);
                pgbNmbrCartes.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pgbNmbrCartes.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                pgbNmbrCartes.setLayoutX(41);
                pgbNmbrCartes.setLayoutY(114);
                pgbNmbrCartes.getStylesheets().add("resources/css/mesStyles.css");
                pgbNmbrCartes.getStyleClass().add("pgbCartes");
                pnCarte.getChildren().add(pgbNmbrCartes);

                ImageView imgBloque = new ImageView(this.assets.getImage("texturesCartes/fondBloque.png"));
                imgBloque.setFitWidth(95);
                imgBloque.setFitHeight(112);
                imgBloque.setLayoutX(16);
                imgBloque.setOpacity(0.4);
                pnCarte.getChildren().add(imgBloque);

                Label lblBloqueRegion = new Label();
                lblBloqueRegion.setFont(Font.font("System", FontWeight.BOLD, 15));
                lblBloqueRegion.setTextFill(Color.WHITE);
                lblBloqueRegion.setWrapText(true);
                lblBloqueRegion.setTextAlignment(TextAlignment.CENTER);
                lblBloqueRegion.setPrefSize(82, 90);
                lblBloqueRegion.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblBloqueRegion.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblBloqueRegion.setLayoutX(23);
                lblBloqueRegion.setLayoutY(9);
                pnCarte.getChildren().add(lblBloqueRegion);

                Label lblNmbrCartePerso = new Label();
                lblNmbrCartePerso.setFont(Font.font("System", FontWeight.BOLD, 15));
                lblNmbrCartePerso.setTextFill(Color.WHITE);
                lblNmbrCartePerso.setWrapText(true);
                lblNmbrCartePerso.setAlignment(Pos.CENTER);
                lblNmbrCartePerso.setPrefSize(60, 21);
                lblNmbrCartePerso.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblNmbrCartePerso.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                lblNmbrCartePerso.setLayoutX(45);
                lblNmbrCartePerso.setLayoutY(119);
                pnCarte.getChildren().add(lblNmbrCartePerso);

                hbLigneCartes.getChildren().add(pnCarte);
            }

            vbInventaire.getChildren().add(hbLigneCartes);

        }

    }

    public void mettreAJourToutesLesCartes(Pane pnEquipe, Compte compte, ArrayList<CartePerso> toutesLesCartes) {
        this.pnChoixPlace.setVisible(false);
        this.sclInventaire.setVvalue(0);
        ArrayList<CartePerso> equipeSelec = compte.getEquipeSelec();
        ScrollPane scrollPane = (ScrollPane) pnEquipe.getChildren().get(2);
        Label lblNombreCartes = (Label) pnEquipe.getChildren().get(0);
        lblNombreCartes.setText("Équipe (" + wrkCartes.getNombreDeCartesDebloquees(compte.getToutesLesCartes()) + "/" + toutesLesCartes.size() + ")");
        ArrayList<CartePerso> persosDisponibles = wrkEquipe.getCartesDisponibles(equipeSelec, compte.getToutesLesCartes());
        persosDisponibles = wrkCartes.trieCartes(persosDisponibles);
        HBox hbEquipe = (HBox) pnEquipe.getChildren().get(1);

        for (int i = 0; i < 4; i++) {
            CartePerso carte = null;
            if (i < equipeSelec.size()) {
                carte = equipeSelec.get(i);
            }

            // recuperer les elements graphiques de la carte
            Pane pnCarte = (Pane) hbEquipe.getChildren().get(i);
            ImageView imgCarte = (ImageView) pnCarte.getChildren().get(0);
            ImageView imgEtoile = (ImageView) pnCarte.getChildren().get(1);
            ProgressBar pgbNmbrCartes = (ProgressBar) pnCarte.getChildren().get(2);
            Label lblNmbrCartes = (Label) pnCarte.getChildren().get(3);

            if (carte != null) {
                imgCarte.setCursor(Cursor.HAND);

                final CartePerso laCarte = carte;
                final CartePerso laCarteARemplacer = this.carteARemplacer;
                final ArrayList<CartePerso> lEquipeSelec = equipeSelec;
                final ArrayList<CartePerso> lesPersosDispos = persosDisponibles;

                if (choixPlaceEnCours) {
                    imgCarte.setOnMouseClicked(e -> {
                        wrkEquipe.remplacerCarte(equipeSelec, lesPersosDispos, laCarte, laCarteARemplacer);
                        choixPlaceEnCours = false;
                        mettreAJourToutesLesCartes(pnEquipe, compte, toutesLesCartes);
                    });
                } else {
                    imgCarte.setOnMouseClicked(e -> {
                        afficherPerso(laCarte, compte);
                    });
                }
                imgCarte.setImage(assets.getImage("texturesPersos/" + carte.getPerso().getNom() + "/" + carte.getPerso().getCostumeSelec().getNom() + "/carte.png"));
                if (carte.getPerso().isHeros()) {
                    imgEtoile.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
                } else {
                    imgEtoile.setImage(assets.getImage("texturesAttributs/imgEtoile" + carte.getNiveau() + ".png"));
                }
                if (carte.getNiveau() == carte.getNiveauMax()) {
                    pgbNmbrCartes.setProgress(1);
                    lblNmbrCartes.setText("Max");
                    pgbNmbrCartes.getStyleClass().clear();
                    pgbNmbrCartes.getStyleClass().add("pgbCartes");
                } else {
                    pgbNmbrCartes.setProgress((double) carte.getNombreDeCartes() / (double) carte.getNombreDeCartesRequises());
                    lblNmbrCartes.setText(carte.getNombreDeCartes() + " / " + carte.getNombreDeCartesRequises());
                    if (carte.getNombreDeCartes() >= carte.getNombreDeCartesRequises()) {
                        pgbNmbrCartes.getStyleClass().clear();
                        pgbNmbrCartes.getStyleClass().add("pgbCartesAmel");
                    } else {
                        pgbNmbrCartes.getStyleClass().clear();
                        pgbNmbrCartes.getStyleClass().add("pgbCartes");
                    }
                }
                if (lblNmbrCartes.getText().length() > 9) {
                    lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 11));
                } else if (lblNmbrCartes.getText().length() > 7) {
                    lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 13));
                } else {
                    lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 15));
                }
                imgEtoile.setVisible(true);
                pgbNmbrCartes.setVisible(true);
                lblNmbrCartes.setVisible(true);
            } else {
                if (i == 0) {
                    imgCarte.setImage(assets.getImage("texturesCartes/VideHeros.png"));
                } else {
                    imgCarte.setImage(assets.getImage("texturesCartes/VidePerso.png"));
                }
                imgEtoile.setVisible(false);
                pgbNmbrCartes.setVisible(false);
                lblNmbrCartes.setVisible(false);
                imgCarte.setOnMouseClicked(e -> {
                });
            }
        }

        VBox vbInventaire = (VBox) scrollPane.getContent();
        for (int i = 0; i < vbInventaire.getChildren().size(); i++) {
            HBox hbLigne = (HBox) vbInventaire.getChildren().get(i);

            for (int j = 0; j < 4; j++) {
                CartePerso carte = null;
                if (i * 4 + j < persosDisponibles.size()) {
                    carte = persosDisponibles.get(i * 4 + j);
                }

                // recuperer les elements graphiques de la carte
                Pane pnCarte = (Pane) hbLigne.getChildren().get(j);
                ImageView imgCarte = (ImageView) pnCarte.getChildren().get(0);
                ImageView imgEtoile = (ImageView) pnCarte.getChildren().get(1);
                ProgressBar pgbNmbrCartes = (ProgressBar) pnCarte.getChildren().get(2);
                ImageView imgBloque = (ImageView) pnCarte.getChildren().get(3);
                Label lblBloque = (Label) pnCarte.getChildren().get(4);
                Label lblNmbrCartes = (Label) pnCarte.getChildren().get(5);

                if (carte != null) {
                    imgCarte.setVisible(true);
                    imgCarte.setCursor(Cursor.HAND);
                    imgBloque.setCursor(Cursor.HAND);
                    lblBloque.setCursor(Cursor.HAND);

                    final CartePerso laCarte = carte;

                    imgCarte.setOnMouseClicked(e -> {
                        afficherPerso(laCarte, compte);
                    });
                    imgBloque.setOnMouseClicked(e -> {
                        afficherPerso(laCarte, compte);
                    });
                    lblBloque.setOnMouseClicked(e -> {
                        afficherPerso(laCarte, compte);
                    });

                    imgCarte.setImage(assets.getImage("texturesPersos/" + carte.getPerso().getNom() + "/" + carte.getPerso().getCostumeSelec().getNom() + "/carte.png"));
                    if (carte.getPerso().isHeros()) {
                        imgEtoile.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
                    } else {
                        imgEtoile.setImage(assets.getImage("texturesAttributs/imgEtoile" + carte.getNiveau() + ".png"));
                    }
                    if (carte.getNiveau() == carte.getNiveauMax()) {
                        pgbNmbrCartes.setProgress(1);
                        lblNmbrCartes.setText("Max");
                        pgbNmbrCartes.getStyleClass().clear();
                        pgbNmbrCartes.getStyleClass().add("pgbCartes");
                    } else {
                        pgbNmbrCartes.setProgress((double) carte.getNombreDeCartes() / (double) carte.getNombreDeCartesRequises());
                        lblNmbrCartes.setText(carte.getNombreDeCartes() + " / " + carte.getNombreDeCartesRequises());
                        if (carte.getNombreDeCartes() >= carte.getNombreDeCartesRequises()) {
                            pgbNmbrCartes.getStyleClass().clear();
                            pgbNmbrCartes.getStyleClass().add("pgbCartesAmel");
                        } else {
                            pgbNmbrCartes.getStyleClass().clear();
                            pgbNmbrCartes.getStyleClass().add("pgbCartes");
                        }
                    }
                    if (lblNmbrCartes.getText().length() > 9) {
                        lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 11));
                    } else if (lblNmbrCartes.getText().length() > 7) {
                        lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 13));
                    } else {
                        lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 15));
                    }
                    if (carte.isDebloque()) {
                        imgEtoile.setVisible(true);
                        pgbNmbrCartes.setVisible(true);
                        lblNmbrCartes.setVisible(true);
                        lblBloque.setVisible(false);
                        imgBloque.setVisible(false);
                    } else {
                        imgEtoile.setVisible(false);
                        pgbNmbrCartes.setVisible(false);
                        lblNmbrCartes.setVisible(false);
                        imgBloque.setVisible(true);
                        if (compte.getPlusHautNiveau().getRegion().getId() < carte.getRegion().getId()) {
                            lblBloque.setText("À partir de la région " + carte.getRegion().getId() + ": Région " + carte.getRegion().getAdjectif());
                        } else {
                            lblBloque.setText("Pas encore trouvée");
                        }
                        lblBloque.setVisible(true);
                    }
                } else {
                    imgCarte.setVisible(false);
                    imgEtoile.setVisible(false);
                    pgbNmbrCartes.setVisible(false);
                    lblNmbrCartes.setVisible(false);
                    imgBloque.setVisible(false);
                    lblBloque.setVisible(false);
                }

            }
        }
        Pane pnCarte = (Pane) pnChoixPlace.getChildren().get(1);
        ImageView imgCarte = (ImageView) pnCarte.getChildren().get(0);
        ImageView imgEtoile = (ImageView) pnCarte.getChildren().get(1);
        ProgressBar pgbNmbrCartes = (ProgressBar) pnCarte.getChildren().get(2);
        Label lblNmbrCartes = (Label) pnCarte.getChildren().get(3);

        pnChoixPlace.setOnMouseClicked(e -> {
            choixPlaceEnCours = false;
            mettreAJourToutesLesCartes(pnEquipe, compte, toutesLesCartes);
        });
        if (carteARemplacer != null) {

            imgCarte.setImage(assets.getImage("texturesPersos/" + carteARemplacer.getPerso().getNom() + "/" + carteARemplacer.getPerso().getCostumeSelec().getNom() + "/carte.png"));
            if (carteARemplacer.getPerso().isHeros()) {
                imgEtoile.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
            } else {
                imgEtoile.setImage(assets.getImage("texturesAttributs/imgEtoile" + carteARemplacer.getNiveau() + ".png"));
            }
            if (carteARemplacer.getNiveau() == carteARemplacer.getNiveauMax()) {
                pgbNmbrCartes.setProgress(1);
                lblNmbrCartes.setText("Max");
                pgbNmbrCartes.getStyleClass().clear();
                pgbNmbrCartes.getStyleClass().add("pgbCartes");
            } else {
                pgbNmbrCartes.setProgress((double) carteARemplacer.getNombreDeCartes() / (double) carteARemplacer.getNombreDeCartesRequises());
                lblNmbrCartes.setText(carteARemplacer.getNombreDeCartes() + " / " + carteARemplacer.getNombreDeCartesRequises());
                if (carteARemplacer.getNombreDeCartes() >= carteARemplacer.getNombreDeCartesRequises()) {
                    pgbNmbrCartes.getStyleClass().clear();
                    pgbNmbrCartes.getStyleClass().add("pgbCartesAmel");
                } else {
                    pgbNmbrCartes.getStyleClass().clear();
                    pgbNmbrCartes.getStyleClass().add("pgbCartes");
                }
            }
            if (lblNmbrCartes.getText().length() > 9) {
                lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 11));
            } else if (lblNmbrCartes.getText().length() > 7) {
                lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 13));
            } else {
                lblNmbrCartes.setFont(Font.font("System", FontWeight.BOLD, 15));
            }
        }

        scrollPane.setVisible(!choixPlaceEnCours);
        this.pnChoixPlace.setVisible(choixPlaceEnCours);

    }

    public void afficherPerso(CartePerso cartePerso, Compte compte) {
        mainCtrl.mettreAJourRessources();
        pnInfosPerso.setVisible(true);
        imgFondQuitter.setVisible(true);
        Perso perso = cartePerso.getPerso();
        imgInfoCarte.setImage(assets.getImage("texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/carte.png"));
        lblInfoNom.setText(perso.getNom());
        lblInfoNiv.setText("Niveau " + cartePerso.getNiveau());
        lblInfoDesc.setText(perso.getDescription());
        if (cartePerso.getNiveau() == cartePerso.getNiveauMax()) {
            pgbInfoCartes.setProgress(1);
            lblInfoCartes.setText("Max");
            pgbInfoCartes.getStyleClass().clear();
            pgbInfoCartes.getStyleClass().add("pgbCartes");
        } else {
            pgbInfoCartes.setProgress((double) cartePerso.getNombreDeCartes() / (double) cartePerso.getNombreDeCartesRequises());
            lblInfoCartes.setText(cartePerso.getNombreDeCartes() + " / " + cartePerso.getNombreDeCartesRequises());
            if (cartePerso.getNombreDeCartes() >= cartePerso.getNombreDeCartesRequises()) {
                pgbInfoCartes.getStyleClass().clear();
                pgbInfoCartes.getStyleClass().add("pgbCartesAmel");
            } else {
                pgbInfoCartes.getStyleClass().clear();
                pgbInfoCartes.getStyleClass().add("pgbCartes");
            }
        }
        if (lblInfoCartes.getText().length() > 7) {
            lblInfoCartes.setFont(Font.font("System", FontWeight.BOLD, 18));
        } else {
            lblInfoCartes.setFont(Font.font("System", FontWeight.BOLD, 21));
        }

        listeLblValAttr[0].setText("" + perso.getPvMax());
        listeLblValAttr[1].setText("" + perso.getDg());
        listeLblValAttr[2].setText("" + perso.getVitesse());
        listeLblValAttr[3].setText("" + perso.getPortee());
        if (perso.getDg() < 0) {
            listeImgAttr[1].setImage(assets.getImage("texturesAttributs/imgSoin.png"));
            if (perso instanceof Medecin) {
                listeLblValAttr[1].setText("" + ((Medecin) perso).getSoin());
            } else {
                listeLblValAttr[1].setText("0");
            }
        } else {
            listeImgAttr[1].setImage(assets.getImage("texturesAttributs/imgEpeeDg.png"));
        }
        if (perso.isHeros()) {
            imgInfoEtoileNiv.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
            Perso p2 = perso.copierPerso();
            p2.ameliorer();
            listeLblValPlus[0].setText("   ");
            listeLblValPlus[1].setText("   ");
            listeLblValPlus[2].setText("   ");
            listeLblValPlus[3].setText("   ");
            if (cartePerso.getNombreDeCartes() >= cartePerso.getNombreDeCartesRequises()) {
                if (p2.getPvMax() != perso.getPvMax()) {
                    listeLblValPlus[0].setText("+" + (p2.getPvMax() - perso.getPvMax()));
                }
                if (p2.getDg() != perso.getDg()) {
                    listeLblValPlus[1].setText("+" + (p2.getDg() - perso.getDg()));
                }
                if (p2.getVitesse() != perso.getVitesse()) {
                    listeLblValPlus[2].setText("+" + (p2.getVitesse() - perso.getVitesse()));
                }
                if (p2.getPortee() != perso.getPortee()) {
                    listeLblValPlus[3].setText("+" + (p2.getPortee() - perso.getPortee()));
                }
            }
            for (int i = 0; i < listeInfoEtoiles.length; i++) {
                if (cartePerso.getNiveau() < (i * 3)) {
                    listeInfoEtoiles[i].setImage(assets.getImage("imgCadenas.png"));
                } else {
                    listeInfoEtoiles[i].setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
                }
            }
        } else {
            imgInfoEtoileNiv.setImage(assets.getImage("texturesAttributs/imgEtoile" + cartePerso.getNiveau() + ".png"));
            listeLblValPlus[0].setText("   ");
            listeLblValPlus[1].setText("   ");
            listeLblValPlus[2].setText("   ");
            listeLblValPlus[3].setText("   ");
            for (int i = 0; i < listeInfoEtoiles.length; i++) {
                if (cartePerso.getNiveau() < (i + 1)) {
                    listeInfoEtoiles[i].setImage(assets.getImage("imgCadenas.png"));
                } else {
                    listeInfoEtoiles[i].setImage(assets.getImage("texturesAttributs/imgEtoile" + (i + 1) + ".png"));
                }
            }
        }
        vbSuper.getChildren().remove(lblInfoSuper);
        vbSuper.getChildren().remove(lblInfoTitreSuper);
        vbSuper.getChildren().remove(lblInfoSuperBloque);
        lblInfoSuper.setText("Ce perso n'a pas de super");
        if (perso.getTexteSuper() != null) {
            if (cartePerso.getNiveau() < perso.getLvlRequisSuper()) {
                lblInfoSuperBloque.setText("Débloquée au niveau " + perso.getLvlRequisSuper());
                lblInfoSuperBloque.setTextFill(Color.RED);
            } else {
                lblInfoSuperBloque.setText("Dès le niveau " + perso.getLvlRequisSuper());
                lblInfoSuperBloque.setTextFill(Color.GREY);
            }
            vbSuper.getChildren().add(lblInfoSuperBloque);
            if (perso.getLvlRequisSuper() == 0) {
                vbSuper.getChildren().remove(lblInfoSuperBloque);
            }
            lblInfoTitreSuper.setText(perso.getTitreSuper());
            lblInfoSuper.setText(perso.getTexteSuper());
            vbSuper.getChildren().add(lblInfoTitreSuper);
        }
        vbSuper.getChildren().add(lblInfoSuper);
        lblInfoSuper.setFont(Font.font("System", FontWeight.BOLD, wrkEquipe.getTailleCaracteresSelonLongueur(lblInfoSuper.getText(), vbSuper.getChildren().contains(lblInfoSuperBloque))));
        for (int i = 0; i < 3; i++) {
            listeTitresAmel[i].setText(perso.getTitreAmel(i));
            listeDescAmel[i].setText(perso.getDescAmel(i));
            listeDescAmel[i].setFont(Font.font(wrkEquipe.getTailleCaracteresSelonLongueur(perso.getDescAmel(i), vbSuper.getChildren().contains(lblInfoSuperBloque))));
            if (cartePerso.getNiveau() >= ((perso.isHeros() ? (i * 3) : i + 1))) {
                listeFondsAmel[i].setStyle("-fx-background-color: gold; -fx-background-radius: 15;");
            } else {
                listeFondsAmel[i].setStyle("-fx-background-color: grey; -fx-background-radius: 15;");
            }
        }
        if (compte.getEquipeSelec().contains(cartePerso)) {
            btnInfoAjout.setText("Retirer");
            btnInfoAjout.getStyleClass().clear();
            btnInfoAjout.getStyleClass().add("btnRefuser");
            btnInfoAjout.setOnMouseClicked(e -> {
                ArrayList<CartePerso> equipe = compte.getEquipeSelec();
                wrkEquipe.retirerPerso(equipe, wrkEquipe.getCartesDisponibles(equipe, compte.getToutesLesCartes()), cartePerso);
                retourEquipe();
                mettreAJourToutesLesCartes(pnEquipe, compte, WrkCartes.getToutesLesCartes());

            });
        } else {
            btnInfoAjout.setText("Ajouter");
            btnInfoAjout.getStyleClass().clear();
            btnInfoAjout.getStyleClass().add("btnAccepter");
            btnInfoAjout.setOnMouseClicked(e -> {
                ArrayList<CartePerso> equipe = compte.getEquipeSelec();
                if (wrkEquipe.peutAjouterEquipe(cartePerso, equipe)) {
                    boolean ajouterPerso = wrkEquipe.ajouterPerso(equipe, wrkEquipe.getCartesDisponibles(equipe, compte.getToutesLesCartes()), cartePerso);
                    if (!ajouterPerso) {
                        choixPlaceEnCours = true;
                        carteARemplacer = cartePerso;
                    }
                    retourEquipe();
                    mettreAJourToutesLesCartes(pnEquipe, compte, WrkCartes.getToutesLesCartes());
                }
            });
        }

        lblInfoPrix.setText(cartePerso.getPrixAmel() + "");
        btnInfoAmeliorer.setOnMouseClicked(e -> {
            wrkEquipe.ameliorerPerso(cartePerso, compte);
            mettreAJourToutesLesCartes(pnEquipe, compte, WrkCartes.getToutesLesCartes());
            mainCtrl.mettreAJourRessources();
            afficherPerso(cartePerso, compte);
        });
        lblInfoPrix.setOnMouseClicked(e -> {
            wrkEquipe.ameliorerPerso(cartePerso, compte);
            mettreAJourToutesLesCartes(pnEquipe, compte, WrkCartes.getToutesLesCartes());
            mainCtrl.mettreAJourRessources();
            afficherPerso(cartePerso, compte);
        });
        imgInfoRessPrix.setOnMouseClicked(e -> {
            wrkEquipe.ameliorerPerso(cartePerso, compte);
            mettreAJourToutesLesCartes(pnEquipe, compte, WrkCartes.getToutesLesCartes());
            mainCtrl.mettreAJourRessources();
            afficherPerso(cartePerso, compte);
        });
        btnInfoAjout.setVisible(cartePerso.isDebloque());
        btnInfoAmeliorer.setVisible((cartePerso.getNiveau() < cartePerso.getNiveauMax()) && cartePerso.isDebloque());
        lblInfoPrix.setVisible((cartePerso.getNiveau() < cartePerso.getNiveauMax()) && cartePerso.isDebloque());
        imgInfoRessPrix.setVisible((cartePerso.getNiveau() < cartePerso.getNiveauMax()) && cartePerso.isDebloque());
    }

    public void retourEquipe() {
        pnInfosPerso.setVisible(false);
        imgFondQuitter.setVisible(false);
    }

    private MainCtrl mainCtrl;
    private WrkCartes wrkCartes;
    private WrkEquipe wrkEquipe;
    private Assets assets;
    private Pane pnEquipe;
    private Pane pnChoixPlace;
    private boolean choixPlaceEnCours;
    private CartePerso carteARemplacer;

    private ScrollPane sclInventaire;
    private ImageView imgFondQuitter;
    private Pane pnInfosPerso;
    private Label lblInfoNom;
    private ImageView imgInfoCarte;
    private Label lblInfoNiv;
    private Label lblInfoDesc;
    private ProgressBar pgbInfoCartes;
    private ImageView imgInfoEtoileNiv;
    private Label lblInfoCartes;
    private ImageView[] listeImgAttr;
    private Label lblInfoSuper;
    private Label lblInfoTitreSuper;
    private Label lblInfoSuperBloque;
    private Label[] listeLblValAttr;
    private Label[] listeLblValPlus;
    private Pane[] listeFondsAmel;
    private ImageView[] listeInfoEtoiles;
    private Label[] listeTitresAmel;
    private Label[] listeDescAmel;
    private Label lblInfoPrix;
    private ImageView imgInfoRessPrix;
    private Button btnInfoAjout;
    private Button btnInfoAmeliorer;
    private VBox vbSuper;
}
