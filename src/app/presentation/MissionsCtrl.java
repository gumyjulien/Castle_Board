/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.Region;
import app.workers.WrkCartes;
import app.workers.WrkMissions;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author gumyj
 */
public class MissionsCtrl {

    public MissionsCtrl(MainCtrl mainCtrl, Pane pnMissions, WrkMissions wrkMissions, WrkCartes wrkCartes, ImageView imgFondQuitter, Assets assets) {
        this.mainCtrl = mainCtrl;
        this.pnMissions = pnMissions;
        this.wrkMissions = wrkMissions;
        this.wrkCartes = wrkCartes;
        this.imgFondQuitter = imgFondQuitter;
        this.assets = assets;
        this.missionsGlobalesAffichees = new ArrayList<>();
        this.pnMissionsGlobalesAffiches = new ArrayList<>();
        this.missionsDeRegionAffichees = new HashMap<>();
        this.pnMissionsDeRegionAffiches = new HashMap<>();
        this.missionsDePersoAffichees = new HashMap<>();
        this.pnMissionsDePersoAffiches = new HashMap<>();

        // recuperer les elements graphiques
        imgFond = (ImageView) pnMissions.getChildren().get(0);
        lblGlobales = (Label) pnMissions.getChildren().get(1);
        lblRegion = (Label) pnMissions.getChildren().get(2);
        lblPerso = (Label) pnMissions.getChildren().get(3);
        btnQuitter = (Button) pnMissions.getChildren().get(4);
        this.sclMissions = (ScrollPane) pnMissions.getChildren().get(6);
        this.btnNotifsGlobales = (Button) pnMissions.getChildren().get(7);
        this.btnNotifsRegion = (Button) pnMissions.getChildren().get(8);
        this.btnNotifsPerso = (Button) pnMissions.getChildren().get(9);
        vbMissionsGlobales = (VBox) sclMissions.getContent();
        vbMissionsGlobales.getChildren().clear();

        this.vbMissionsDeRegion = creerVb(true);
        this.vbMissionsDePerso = creerVb(true);

        btnQuitter.setOnMouseClicked(e -> {
            retourMenu();
        });

        lblRegion.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsDeRegion);
            this.sclMissions.setVvalue(0);
        });
        btnNotifsRegion.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsDeRegion);
            this.sclMissions.setVvalue(0);
        });
        

        lblGlobales.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsGlobales);
            this.sclMissions.setVvalue(0);
        });
        btnNotifsGlobales.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsGlobales);
            this.sclMissions.setVvalue(0);
        });

        lblPerso.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsDePerso);
            this.sclMissions.setVvalue(0);
        });
        btnNotifsPerso.setOnMouseClicked(e -> {
            this.sclMissions.setContent(vbMissionsDePerso);
            this.sclMissions.setVvalue(0);
        });

    }

    public void mettreAJourMissions(Compte compte, ArrayList<Region> toutesLesRegions) {

        ArrayList<Mission> toutesLesMissions = compte.getToutesLesMissions();

        this.toutesLesMissionsCompte = toutesLesMissions;
        ArrayList<CartePerso> toutesLesCartesDeblo = wrkCartes.trieCartes(compte.getToutesLesCartes());

        ArrayList<Mission> toutesLesMissionsGlobales = new ArrayList<>();
        ArrayList<Mission> toutesLesMissionsDeRegion = new ArrayList<>();
        ArrayList<Mission> toutesLesMissionsDePerso = new ArrayList<>();

        // classer les missions selon leur portée : globale, région, perso
        for (Mission m : toutesLesMissions) {
            switch (m.getPorteeMission()) {
                case GLOBALE:
                    toutesLesMissionsGlobales.add(m);
                    if (m.getTypeMission().equals(Mission.TypeMission.DEBLOQUER_PERSO)) {
                        m.setProgression(wrkCartes.getNombreDeCartesDebloquees(compte.getToutesLesCartes()));
                    }
                    break;
                case REGION:
                    toutesLesMissionsDeRegion.add(m);
                    break;
                case PERSO:
                    toutesLesMissionsDePerso.add(m);
                    break;
            }
        }

        // MISSIONS GLOBALES
        controleNombreMissionsAff(vbMissionsGlobales, toutesLesMissionsGlobales, missionsGlobalesAffichees, pnMissionsGlobalesAffiches);
        mettreAJourMissions(pnMissionsGlobalesAffiches, toutesLesMissionsGlobales, compte);

        // MISSIONS DE REGION
        HashMap<Region, ArrayList<Mission>> missionsClasseesRegion = new HashMap<>();
        for (Mission m : toutesLesMissionsDeRegion) {
            Region r = m.getRegion();
            if (!missionsClasseesRegion.containsKey(r)) {
                missionsClasseesRegion.put(r, new ArrayList<>());
            }
            ArrayList<Mission> miss = missionsClasseesRegion.get(r);
            miss.add(m);
        }

        for (Region reg : toutesLesRegions) {
            if (!missionsClasseesRegion.containsKey(reg) && reg.getId() <= compte.getPlusHauteRegionDecouverte().getId()) {
                missionsClasseesRegion.put(reg, new ArrayList<>());
            }
        }

        double tailleVb = 100;

        for (Region region : missionsClasseesRegion.keySet()) {
            ArrayList<Mission> listeMissionsParRegion = missionsClasseesRegion.get(region);
            if (!missionsDeRegionAffichees.containsKey(region)) {
                missionsDeRegionAffichees.put(region, new ArrayList<>());
                ajouterSection(vbMissionsDeRegion, assets.getImage("texturesRegions/" + region.getNom() + "/logo.png"), "Missions de la région " + region.getAdjectif());
            }
            ArrayList<Mission> listeMissionsAfficheeParRegion = missionsDeRegionAffichees.get(region);
            if (!this.pnMissionsDeRegionAffiches.containsKey(region)) {
                pnMissionsDeRegionAffiches.put(region, creerVb(false));
                vbMissionsDeRegion.getChildren().add(pnMissionsDeRegionAffiches.get(region));
            }
            VBox vb = this.pnMissionsDeRegionAffiches.get(region);
            ArrayList<Pane> listePn = new ArrayList<>();
            for (Node n : vb.getChildren()) {
                if (n instanceof Pane) {
                    listePn.add((Pane) n);
                }
            }

            controleNombreMissionsAff(vb, listeMissionsParRegion, listeMissionsAfficheeParRegion, listePn);
            mettreAJourMissions(listePn, listeMissionsParRegion, compte);

            tailleVb += vb.getHeight() + 50;

        }

        vbMissionsDeRegion.setPrefHeight(tailleVb);

        // MISSIONS DE PERSO
        HashMap<Integer, ArrayList<Mission>> missionsClasseesPerso = new HashMap<>();
        for (Mission m : toutesLesMissionsDePerso) {
            Perso p = m.getPerso();
            CartePerso cartePerso = null;
            // retrouver le perso dans les cartes debloquees
            int i = 0;
            int indexCarte = -1;
            for (CartePerso carte : toutesLesCartesDeblo) {
                if (carte.getPerso().equals(p)) {
                    cartePerso = carte;
                    indexCarte = i;
                    break;
                }
                i++;
            }
            if (!missionsClasseesPerso.containsKey(indexCarte)) {
                missionsClasseesPerso.put(indexCarte, new ArrayList<>());
            }
            ArrayList<Mission> miss = missionsClasseesPerso.get(indexCarte);
            miss.add(m);
        }

        for (CartePerso carte : toutesLesCartesDeblo) {
            int indexCarte = toutesLesCartesDeblo.indexOf(carte);
            if (!missionsClasseesPerso.containsKey(indexCarte) && carte.isDebloque()) {
                missionsClasseesPerso.put(indexCarte, new ArrayList<>());
            }
        }

        // mettre a jour l'ordre des missions affichees
        HashMap<Integer, ArrayList<Mission>> lesMissionsDePerso = new HashMap<>();
        HashMap<Integer, VBox> lesMissionsDePersoAffichees = new HashMap<>();

//        for (Integer cle : missionsClasseesPerso.keySet()) {
//            
//        }
        for (Integer cle : missionsDePersoAffichees.keySet()) {
            if (!missionsDePersoAffichees.get(cle).isEmpty()) {
                int index = 0;
                // retrouver la position du perso dans le classement
                for (Integer clef : missionsClasseesPerso.keySet()) {
                    if (!missionsClasseesPerso.get(clef).isEmpty()) {
                        if (missionsClasseesPerso.get(clef).get(0).getPerso().equals(missionsDePersoAffichees.get(cle).get(0).getPerso())) {
                            lesMissionsDePerso.put(index, missionsDePersoAffichees.get(cle));
                            lesMissionsDePersoAffichees.put(index, pnMissionsDePersoAffiches.get(cle));
                        }
                    }
                    index++;
                }
            }
        }
        missionsDePersoAffichees = lesMissionsDePerso;
        pnMissionsDePersoAffiches = lesMissionsDePersoAffichees;

        tailleVb = 50;

        vbMissionsDePerso.getChildren().clear();

        for (Integer index : missionsClasseesPerso.keySet()) {
            ArrayList<Mission> listeMissionsParPerso = missionsClasseesPerso.get(index);
            missionsDePersoAffichees.put(index, new ArrayList<>());
            Perso p = toutesLesCartesDeblo.get(index).getPerso();
            ajouterSection(vbMissionsDePerso, assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/logo.png"), "Missions avec votre " + p.getNom());
            ArrayList<Mission> listeMissionsAfficheeParPerso = missionsDePersoAffichees.get(index);
            pnMissionsDePersoAffiches.put(index, creerVb(false));
            vbMissionsDePerso.getChildren().add(pnMissionsDePersoAffiches.get(index));
            VBox vb = this.pnMissionsDePersoAffiches.get(index);
            ArrayList<Pane> listePn = new ArrayList<>();
            for (Node n : vb.getChildren()) {
                if (n instanceof Pane) {
                    listePn.add((Pane) n);
                }
            }

            controleNombreMissionsAff(vb, listeMissionsParPerso, listeMissionsAfficheeParPerso, listePn);
            mettreAJourMissions(listePn, listeMissionsParPerso, compte);

            tailleVb += vb.getHeight() + 50;

        }

        vbMissionsDePerso.setPrefHeight(tailleVb);

        toutesLesMissions.clear();
        toutesLesMissions.addAll(toutesLesMissionsGlobales);
        toutesLesMissions.addAll(toutesLesMissionsDeRegion);
        toutesLesMissions.addAll(toutesLesMissionsDePerso);
        
        int nbr = 0;
        for (Mission m : toutesLesMissionsGlobales) {
            if(m.isTerminee()) {
                nbr++;
            }
        }
        this.btnNotifsGlobales.setText(nbr + "");
        this.btnNotifsGlobales.setFont(Font.font("System", FontWeight.BOLD, (nbr < 10 ? 21 : 16)));
        this.btnNotifsGlobales.setVisible(nbr > 0);
        
        nbr = 0;
        for (Mission m : toutesLesMissionsDeRegion) {
            if(m.isTerminee()) {
                nbr++;
            }
        }
        this.btnNotifsRegion.setText(nbr + "");
        this.btnNotifsRegion.setFont(Font.font("System", FontWeight.BOLD, (nbr < 10 ? 21 : 16)));
        this.btnNotifsRegion.setVisible(nbr > 0);
        
        nbr = 0;
        for (Mission m : toutesLesMissionsDePerso) {
            if(m.isTerminee()) {
                nbr++;
            }
        }
        this.btnNotifsPerso.setText(nbr + "");
        this.btnNotifsPerso.setFont(Font.font("System", FontWeight.BOLD, (nbr < 10 ? 21 : 16)));
        this.btnNotifsPerso.setVisible(nbr > 0);

    }

    private void controleNombreMissionsAff(VBox vb, ArrayList<Mission> toutesLesMissions, ArrayList<Mission> toutesLesMissionsAff, ArrayList<Pane> tousLesPnMissionsAff) {
        int nbrMissionsAff = tousLesPnMissionsAff.size();
        int nbrToutesLesMissions = toutesLesMissions.size();
        if (nbrMissionsAff != nbrToutesLesMissions) {
            int diff;
            if (nbrMissionsAff > nbrToutesLesMissions) {
                diff = nbrMissionsAff - nbrToutesLesMissions;
                for (int j = 0; j < diff; j++) {
                    vb.getChildren().remove((nbrMissionsAff - 1) - j);
                }
            } else {
                diff = nbrToutesLesMissions - nbrMissionsAff;
                for (int j = 0; j < diff; j++) {
                    ajouterPnMission(vb);
                }
            }

            // procéder à la mise à jour des missions
            toutesLesMissionsAff.clear();
            for (Mission laMission : toutesLesMissions) {
                toutesLesMissionsAff.add(laMission.copierMission());
            }

            tousLesPnMissionsAff.clear();
            for (Node n : vb.getChildren()) {
                if (n instanceof Pane) {
                    tousLesPnMissionsAff.add((Pane) n);
                }
            }
        }

        double tailleVb = nbrToutesLesMissions * 120 + 20;

        boolean contientLbl = false;
        int indexLbl = -1;
        for (Node n : vb.getChildren()) {
            if (n instanceof Label) {
                contientLbl = true;
                indexLbl = vb.getChildren().indexOf(n);
                break;
            }
        }
        if (tousLesPnMissionsAff.isEmpty()) {
            if (!contientLbl) {
                ajouterLabelToutTermine(vb);
            }
            tailleVb += 81;
        } else {
            if (contientLbl) {
                vb.getChildren().remove(indexLbl);
            }
        }

        vb.setPrefHeight(tailleVb);
        vb.setMinHeight(tailleVb);
    }

    private void mettreAJourMissions(ArrayList<Pane> tousLesPn, ArrayList<Mission> toutesLesMissions, Compte compte) {
        int x = 0;
        for (Mission m : toutesLesMissions) {
            // récupérer les pn de toutes les missions
            Pane pn = tousLesPn.get(x);
            Label lblTitre = (Label) pn.getChildren().get(0);
            ProgressBar pgb = (ProgressBar) pn.getChildren().get(1);
            Label lblEtape = (Label) pn.getChildren().get(2);
            Label lblProgress = (Label) pn.getChildren().get(3);
            HBox hb = (HBox) pn.getChildren().get(4);

            // maj
            lblTitre.setText(m.toString());
            pgb.setProgress(m.getProgressionPourcentage());
            if (m.getEtapeMax() > 1) {
                lblEtape.setText("Étape " + m.getNumEtape() + " sur " + m.getEtapeMax());
            } else {
                lblEtape.setText("");
            }
            if (!m.isTerminee()) {
                lblProgress.setText(m.getProgression() + " / " + m.getNbrObjectif());
                pgb.getStyleClass().clear();
                pgb.getStyleClass().add("pgbMissionEnCours");
                pn.getStyleClass().clear();
                pn.getStyleClass().add("pnMissionEnCours");
                pn.setCursor(Cursor.DEFAULT);
                pn.setOnMouseClicked(e -> {
                });
            } else {
                lblProgress.setText("Terminée");
                pgb.getStyleClass().clear();
                pgb.getStyleClass().add("pgbMissionTerminee");
                pgb.setProgress(1.0);
                pn.getStyleClass().clear();
                pn.getStyleClass().add("pnMissionTerminee");
                pn.setCursor(Cursor.HAND);
                pn.setOnMouseClicked(e -> {
                    wrkMissions.terminerMission(toutesLesMissionsCompte, m);
                });
            }

            ArrayList<Recompense> listeRec = m.getRecompenses();
            for (int j = 0; j < 3; j++) {
                Pane pnRec = (Pane) hb.getChildren().get(2 - j);
                ImageView img = (ImageView) pnRec.getChildren().get(0);
                Label lblQuantite = (Label) pnRec.getChildren().get(1);
                Label lblInfoRegion = (Label) pnRec.getChildren().get(2);

                if (listeRec.size() > j) {
                    Recompense rec = listeRec.get(j);
                    img.setImage(assets.getImageRec(rec));
                    lblQuantite.setText(rec.getQuantite() + "");
                    String region = "";
                    if(m.getPorteeMission().equals(Mission.Portee.GLOBALE)) {
                        rec.setRegion(compte.getPlusHauteRegionDecouverte());
                    }
                    
                    if (rec.estUnCoffre() && rec.getCartePerso() == null) {
                        region = "Région ";
                        if (!rec.isRegionFixe() && rec.getRegion().getId() > 1) {
                            region += "1-" + rec.getRegion().getId();
                        } else {
                            region += rec.getRegion().getId();
                        }
                    }
                    lblInfoRegion.setText(region);
                    if (!m.isTerminee()) {
                        pnRec.getStyleClass().clear();
                        pnRec.getStyleClass().add("pnRecompenseMissionEnCours");
                    } else {
                        pnRec.getStyleClass().clear();
                        pnRec.getStyleClass().add("pnRecompenseMissionTerminee");
                    }
                    pnRec.setVisible(true);
                } else {
                    pnRec.setVisible(false);
                }
            }

            x++;
        }
        mainCtrl.mettreAJourNotifs();
    }

    private VBox creerVb(boolean marges) {
        VBox vb = new VBox();
        vb.setPrefSize(vbMissionsGlobales.getPrefWidth(), vbMissionsGlobales.getPrefHeight());
        vb.setMinSize(vbMissionsGlobales.getMinWidth(), vbMissionsGlobales.getMinHeight());
        vb.setMaxSize(vbMissionsGlobales.getMaxWidth(), vbMissionsGlobales.getMaxHeight());
        vb.setAlignment(vbMissionsGlobales.getAlignment());
        if (marges) {
            vb.setPadding(vbMissionsGlobales.getPadding());
        }
        vb.setSpacing(vbMissionsGlobales.getSpacing());

        return vb;
    }

    private void ajouterPnMission(VBox vb) {
        Pane pnMiss = new Pane();
        pnMiss.setPrefSize(290, 118);
        pnMiss.setMinSize(290, 118);
        pnMiss.setMaxSize(290, 118);
        pnMiss.getStylesheets().add("resources/css/mesStyles.css");
        pnMiss.getStyleClass().add("pnMissionEnCours");

        Label lblTitre = new Label();
        lblTitre.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblTitre.setTextFill(Color.WHITE);
        lblTitre.setWrapText(true);
        lblTitre.setAlignment(Pos.CENTER);
        lblTitre.setTextAlignment(TextAlignment.CENTER);
        lblTitre.setPadding(new Insets(15, 0, 0, 0));
        lblTitre.setPrefSize(142, 79);
        lblTitre.setLayoutX(6);
        pnMiss.getChildren().add(lblTitre);

        ProgressBar pgb = new ProgressBar();
        pgb.setPrefSize(272, 18);
        pgb.setLayoutX(14);
        pgb.setLayoutY(86);
        pgb.getStylesheets().add("resources/css/mesStyles.css");
        pgb.getStyleClass().add("pgbMissionEnCours");
        pnMiss.getChildren().add(pgb);

        Label lblEtape = new Label();
        lblEtape.setFont(Font.font("System", 12));
        lblEtape.setTextFill(Color.WHITE);
        lblEtape.setTextAlignment(TextAlignment.CENTER);
        lblEtape.setLayoutX(217);
        lblEtape.setLayoutY(6);
        pnMiss.getChildren().add(lblEtape);

        Label lblProgress = new Label();
        lblProgress.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblProgress.setTextFill(Color.WHITE);
        lblProgress.setAlignment(Pos.CENTER);
        lblProgress.setTextAlignment(TextAlignment.CENTER);
        lblProgress.setPrefSize(261, 17);
        lblProgress.setLayoutX(19);
        lblProgress.setLayoutY(87);
        pnMiss.getChildren().add(lblProgress);

        HBox hbRec = new HBox();
        hbRec.setPrefSize(148, 42);
        hbRec.setSpacing(2);
        hbRec.setLayoutX(146);
        hbRec.setLayoutY(38);

        for (int i = 0; i < 3; i++) {
            Pane pnRec = new Pane();
            pnRec.setPrefSize(46, 40);
            pnRec.getStylesheets().add("resources/css/mesStyles.css");
            pnRec.getStyleClass().add("pnRecompenseMissionEnCours");

            ImageView imgRec = new ImageView();
            imgRec.setPreserveRatio(true);
            imgRec.setFitWidth(35);
            imgRec.setFitHeight(35);
            imgRec.setLayoutX(2);
            imgRec.setLayoutY(1);
            pnRec.getChildren().add(imgRec);

            Label lblQuantite = new Label();
            lblQuantite.setFont(Font.font("System", FontWeight.BOLD, 14));
            lblQuantite.setTextFill(Color.WHITE);
            lblQuantite.setWrapText(true);
            lblQuantite.setAlignment(Pos.CENTER_RIGHT);
            lblQuantite.setTextAlignment(TextAlignment.RIGHT);
            lblQuantite.setPrefSize(47, 25);
            lblQuantite.setLayoutX(-4);
            lblQuantite.setLayoutY(21);
            pnRec.getChildren().add(lblQuantite);

            Label lblInfoRegion = new Label();
            lblInfoRegion.setFont(Font.font("System", FontWeight.BOLD, 9));
            lblInfoRegion.setTextFill(Color.WHITE);
            lblInfoRegion.setWrapText(true);
            lblInfoRegion.setAlignment(Pos.CENTER);
            lblInfoRegion.setPrefSize(54, 15);
            lblInfoRegion.setLayoutX(-5);
            lblInfoRegion.setLayoutY(-13);
            pnRec.getChildren().add(lblInfoRegion);

            hbRec.getChildren().add(pnRec);
        }
        pnMiss.getChildren().add(hbRec);

        vb.getChildren().add(pnMiss);

    }

    private void ajouterSection(VBox vb, Image imgLogo, String texte) {
        Pane pnMiss = new Pane();
        pnMiss.setPrefSize(290, 30);
        pnMiss.setMinSize(290, 30);
        pnMiss.setMaxSize(290, 30);
        pnMiss.getStylesheets().add("resources/css/mesStyles.css");
        pnMiss.getStyleClass().add("pnSectionMission");

        ImageView img = new ImageView(imgLogo);
        img.setFitWidth(35);
        img.setFitHeight(35);
        img.setLayoutX(14);
        img.setLayoutY(-2);
        img.setPreserveRatio(true);
        pnMiss.getChildren().add(img);

        Label lbl = new Label(texte);
        lbl.setFont(Font.font("System", FontWeight.BOLD, (texte.length() > 35 ? 12 : 14)));
        lbl.setTextFill(Color.WHITE);
        lbl.setAlignment(Pos.CENTER_LEFT);
        lbl.setLayoutX(56);
        lbl.setLayoutY(6);
        pnMiss.getChildren().add(lbl);
        vb.getChildren().add(pnMiss);
    }

    private void ajouterLabelToutTermine(VBox vb) {
        Label lbl = new Label("Vous avez complété toutes les missions pour l'instant");
        lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.ORANGE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setWrapText(true);
        lbl.setPrefSize(263, 81);
        lbl.setMinSize(263, 81);
        lbl.setMaxSize(263, 81);
        vb.getChildren().add(lbl);
    }

    private void retourMenu() {
        pnMissions.setVisible(false);
        imgFondQuitter.setVisible(false);
    }

    private MainCtrl mainCtrl;

    private Pane pnMissions;
    private Assets assets;
    private WrkMissions wrkMissions;
    private WrkCartes wrkCartes;
    private ScrollPane sclMissions;

    private ImageView imgFondQuitter;
    private ImageView imgFond;
    private Label lblGlobales;
    private Label lblRegion;
    private Label lblPerso;
    private Button btnQuitter;
    private VBox vbMissionsGlobales;
    private VBox vbMissionsDeRegion;
    private VBox vbMissionsDePerso;
    private Button btnNotifsGlobales;
    private Button btnNotifsRegion;
    private Button btnNotifsPerso;

    private ArrayList<Mission> toutesLesMissionsCompte;

    private ArrayList<Mission> missionsGlobalesAffichees;
    private ArrayList<Pane> pnMissionsGlobalesAffiches;
    private HashMap<Region, ArrayList<Mission>> missionsDeRegionAffichees;
    private HashMap<Region, VBox> pnMissionsDeRegionAffiches;
    private HashMap<Integer, ArrayList<Mission>> missionsDePersoAffichees;
    private HashMap<Integer, VBox> pnMissionsDePersoAffiches;
}
