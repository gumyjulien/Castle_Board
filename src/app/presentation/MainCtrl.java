/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package app.presentation;

import app.workers.NiveauxWrk;
import app.beans.Assets;
import app.beans.BadgeMaitrise;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.Mission;
import app.beans.Niveau;
import app.beans.Objectif;
import app.beans.Perso;
import app.beans.Recompense;
import app.helpers.JfxPopup;
import app.helpers.file.WrkObjectFile;
import app.workers.WrkBadges;
import app.workers.WrkCartes;
import app.workers.WrkMagasin;
import app.workers.WrkMenu;
import app.workers.WrkMissions;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author GumyJ01
 */
public class MainCtrl implements Initializable {

    @FXML
    private Pane pnRoot;
    @FXML
    private Pane pnChargement;
    @FXML
    private ImageView imgFond;
    @FXML
    private ProgressBar pgbLoading;
    @FXML
    private Pane pnMenu;
    @FXML
    private Button btnEquipe;
    @FXML
    private Button btnMissions;
    @FXML
    private Button btnBadges;
    @FXML
    private Pane pnCarteLevels;
    @FXML
    private Button btnBoutique;
    @FXML
    private Button btnNotifBoutique;
    @FXML
    private Button btnNotifEquipe;
    @FXML
    private Button btnNotifBadges;
    @FXML
    private Button btnNotifMissions;
    @FXML
    private Label lblChargement;
    public boolean animationEnCours;
    @FXML
    private Pane pnJeu;
    @FXML
    private ImageView imgFondQuitter;
    @FXML
    private Label lblNumeroNiv;
    @FXML
    private Button btnJouer;
    @FXML
    private HBox hbEquipeAdverse;
    @FXML
    private Pane pnDetailsNiveau;

    private NiveauxWrk niveauxWrk;
    private JeuCtrl jeuCtrl;
    private EquipeCtrl equipeCtrl;
    private RecupRecompenseCtrl recupRecCtrl;
    private MissionsCtrl missionsCtrl;
    private DuelCtrl duelCtrl;
    private BadgesCtrl badgesCtrl;
    private MagasinCtrl magasinCtrl;
    private Assets assets;
    private WrkMenu wrkMenu;
    private WrkCartes wrkCartes;
    private WrkMissions wrkMissions;
    private WrkBadges wrkBadges;
    private WrkMagasin wrkMagasin;
    private Pane[] listeFondObjectifs;
    private Pane[] listeFondRecObjectifs;
    private Label[] listeTextesObjectifs;
    private Label[] listeTextesRecObjectifs;
    private Label[] listeTextesRegionObjectifs;
    private ImageView[] listeImgRecObjectifs;
    private Pane[] listeFondResultatsObjectifs;
    private Pane[] listeFondResultatsRecObjectifs;
    private Label[] listeTextesResultatsObjectifs;
    private Label[] listeTextesResultatsRecObjectifs;
    private ImageView[] listeImgResultatsRecObjectifs;
    private ImageView[] listeFondNiveaux;
    private HashMap<Integer, ArrayList<ImageView>> listeImagesEtoilesNiveaux;
    private Button[] listeNotifsNiveaux;
    private Niveau niveauJoue;
    private Compte compteConnecte;
    private ArrayList<Perso> equipeSelec;
    @FXML
    private Pane pnObj1;
    @FXML
    private Label lblObj1;
    @FXML
    private Pane pnRecObj1;
    @FXML
    private ImageView imgRecObj1;
    @FXML
    private Label lblRecObj1;
    @FXML
    private Pane pnObj2;
    @FXML
    private Label lblObj2;
    @FXML
    private Pane pnRecObj2;
    @FXML
    private ImageView imgRecObj2;
    @FXML
    private Label lblRecObj2;
    @FXML
    private Pane pnObj3;
    @FXML
    private Label lblObj3;
    @FXML
    private Pane pnRecObj3;
    @FXML
    private ImageView imgRecObj3;
    @FXML
    private Label lblRecObj3;
    @FXML
    private VBox vbRessources;
    @FXML
    private Label lblRessPieces;
    @FXML
    private Label lblRessRubis;
    @FXML
    private ImageView imgGererCartesJeu;
    @FXML
    private Pane pnResultats;
    @FXML
    private ImageView imgResultatsEtoile1;
    @FXML
    private ImageView imgResultatsEtoile2;
    @FXML
    private ImageView imgResultatsEtoile3;
    @FXML
    private Button btnResultats;
    private Pane pnResultatsObj1;
    private Pane pnResultatsObj2;
    private Pane pnResultatsObj3;
    private Label lblResultatsObj2;
    private Pane pnResultatsRecObj2;
    private ImageView imgResultatsRecObj2;
    private Label lblResultatsRecObj2;
    private Label lblResultatsObj3;
    private Pane pnResultatsRecObj3;
    private ImageView imgResultatsRecObj3;
    private Label lblResultatsRecObj3;
    private Label lblResultatsObj1;
    private Pane pnResultatsRecObj1;
    private ImageView imgResultatsRecObj1;
    private Label lblResultatsRecObj1;
    private boolean popupAffichee;
    @FXML
    private Pane pnEquipe;
    @FXML
    private Pane pnDebutTour;
    @FXML
    private ImageView imgBanniere;
    @FXML
    private Label lblTourDuJoueur;
    @FXML
    private Label lblNoTour;
    @FXML
    private Pane pnRecupRecompense;
    @FXML
    private Label lblRegionObj1;
    @FXML
    private Label lblRegionObj2;
    @FXML
    private Label lblRegionObj3;
    @FXML
    private ImageView imgFondQuitterInfos;
    @FXML
    private Pane pnInfos;
    @FXML
    private Pane pnMissions;
    @FXML
    private Button btnDuel;
    @FXML
    private Pane pnDuel;
    @FXML
    private Pane pnBadges;
    @FXML
    private Pane pnInfosBadge;
    @FXML
    private ImageView imgFondQuitterInfosBadges;
    @FXML
    private ImageView imgFondQuitterProgBadges;
    @FXML
    private Pane pnProgressionBadges;
    @FXML
    private VBox vbProgressionBadges;
    @FXML
    private Pane pnListeProgressionBadges;
    @FXML
    private Pane pnMagasin;
    @FXML
    private Label lblRessPiecesEquipe;
    @FXML
    private VBox vbRessources1;
    @FXML
    private Label lblRessPieces1;
    @FXML
    private Label lblRessRubis1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cacher tous les pane sauf le chargement
        pnJeu.setVisible(false);
        pnRoot.setOnKeyReleased(ev -> {
            if (pnJeu.isVisible() && !pnResultats.isVisible()) {
                if (null != ev.getCode()) {
                    switch (ev.getCode()) {
                        case E:
                            jeuCtrl.ouvrirFermerBarreDeroulante(!jeuCtrl.menuDeroulantOuvert, 0.2);
                            break;
                        case A:
                            jeuCtrl.setActionEnCours(false);
                            break;
                        case ESCAPE:
                            if (!popupAffichee) {
                                ArrayList<String> listeBtnRouges = new ArrayList<>();
                                ArrayList<String> listeBtnBleus = new ArrayList<>();
                                listeBtnBleus.add("Annuler");
                                listeBtnRouges.add("Quitter");
                                Pane popup = JfxPopup.afficherInformation(pnRoot, "Voulez-vous vraiment quitter la partie ? Toute progression sera perdue.", listeBtnBleus, listeBtnRouges);
                                pnRoot.getChildren().add(popup);
                                popupAffichee = true;
                                HBox hb = (HBox) popup.getChildren().get(1);
                                hb.getChildren().get(0).setOnMouseClicked(e -> {
                                    jeuCtrl.afficherFondNoir(false);
                                    pnRoot.getChildren().remove(popup);
                                    popupAffichee = false;
                                });
                                hb.getChildren().get(1).setOnMouseClicked(e -> {
                                    jeuCtrl.setPartieEnCours(false);
                                    afficherMenu();
                                    pnRoot.getChildren().remove(popup);
                                    popupAffichee = false;
                                });
                                jeuCtrl.afficherFondNoir(true);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        pnMenu.setVisible(false);
        pnResultats.setVisible(false);
        pnEquipe.setVisible(false);
        pnRecupRecompense.setVisible(false);
        pnInfos.setVisible(false);
        imgFondQuitter.setVisible(false);
        pnMissions.setVisible(false);
        imgFondQuitterInfos.setVisible(false);
        imgFondQuitterInfosBadges.setVisible(false);
        imgFondQuitterProgBadges.setVisible(false);
        pnProgressionBadges.setVisible(false);
        pnDuel.setVisible(false);
        pnBadges.setVisible(false);
        pnInfosBadge.setVisible(false);
        pnMagasin.setVisible(false);
        pnChargement.setVisible(true);
        imgFondQuitter.setOnMouseClicked(e -> {
            if (pnMenu.isVisible()) {
                retournerMenu();
            }
        });
        imgFondQuitterInfos.setOnMouseClicked(e -> {
            if (pnInfos.isVisible()) {
                equipeCtrl.retourEquipe();
            }
        });
        imgFondQuitterInfosBadges.setOnMouseClicked(e -> {
            if (pnBadges.isVisible()) {
                mettreAJourBadges();
                badgesCtrl.cacherInfos();
            }
        });
        imgFondQuitterProgBadges.setOnMouseClicked(e -> {
            if (pnProgressionBadges.isVisible()) {
                mettreAJourBadges();
                pnProgressionBadges.setVisible(false);
                imgFondQuitterProgBadges.setVisible(false);
            }
        });
        listeFondObjectifs = new Pane[]{pnObj1, pnObj2, pnObj3};
        listeFondRecObjectifs = new Pane[]{pnRecObj1, pnRecObj2, pnRecObj3};
        listeImgRecObjectifs = new ImageView[]{imgRecObj1, imgRecObj2, imgRecObj3};
        listeTextesObjectifs = new Label[]{lblObj1, lblObj2, lblObj3};
        listeTextesRecObjectifs = new Label[]{lblRecObj1, lblRecObj2, lblRecObj3};
        listeTextesRegionObjectifs = new Label[]{lblRegionObj1, lblRegionObj2, lblRegionObj3};
        listeFondResultatsObjectifs = new Pane[]{pnResultatsObj1, pnResultatsObj2, pnResultatsObj3};
        listeFondResultatsRecObjectifs = new Pane[]{pnResultatsRecObj1, pnResultatsRecObj2, pnResultatsRecObj3};
        listeImgResultatsRecObjectifs = new ImageView[]{imgResultatsRecObj1, imgResultatsRecObj2, imgResultatsRecObj3};
        listeTextesResultatsObjectifs = new Label[]{lblResultatsObj1, lblResultatsObj2, lblResultatsObj3};
        listeTextesResultatsRecObjectifs = new Label[]{lblResultatsRecObj1, lblResultatsRecObj2, lblResultatsRecObj3};
        listeImagesEtoilesNiveaux = new HashMap<>();
        equipeSelec = new ArrayList<>();
        popupAffichee = false;
    }

    @FXML

    public void demarrerChargement() {

        lblChargement.setVisible(false);

        // creer le conteneur des assets
        assets = new Assets();

        wrkMenu = new WrkMenu(assets);

        // creer le JeuCtrl
        jeuCtrl = new JeuCtrl(this);

        niveauxWrk = new NiveauxWrk(this);
        listeFondNiveaux = new ImageView[niveauxWrk.getTousLesNiveaux().size()];

        wrkCartes = new WrkCartes(niveauxWrk);

        wrkMissions = new WrkMissions(this, niveauxWrk);
        missionsCtrl = new MissionsCtrl(this, pnMissions, wrkMissions, wrkCartes, imgFondQuitter, assets);

        equipeCtrl = new EquipeCtrl(this, wrkCartes, pnInfos, imgFondQuitterInfos);
        equipeCtrl.construirePnEquipe(pnEquipe, wrkCartes.getToutesLesCartes(), assets);

        wrkBadges = new WrkBadges(wrkCartes.getToutesLesCartes());
        badgesCtrl = new BadgesCtrl(this, wrkBadges, assets, pnBadges, pnInfosBadge, imgFondQuitterInfosBadges);
        badgesCtrl.construirePnBadges(WrkBadges.getTousLesBadges());

        recupRecCtrl = new RecupRecompenseCtrl(this, pnRecupRecompense, assets);

        wrkMagasin = new WrkMagasin(niveauxWrk);
        magasinCtrl = new MagasinCtrl(this, assets, wrkMagasin, pnMagasin);

        btnResultats.setOnMouseClicked(e -> {
            boolean fini = wrkMenu.suiteResultats();
            if (fini) {
                btnResultats.setText("OK");
            }
        });

        duelCtrl = new DuelCtrl(pnDuel, pnEquipe, pnJeu, this, equipeCtrl, jeuCtrl, assets);

        // construire la route des niveaux
        construireRouteDesNiveaux();

        essayerRecupererCompte();

    }

    public void mettreAJourChargement(double progression) {
        pgbLoading.setProgress(progression);
        pgbLoading.setVisible(true);
        lblChargement.setText(Math.round(progression * 100) + "%");
    }

    public void quitter() {
        WrkObjectFile.serialiseObjet("compte.castleboard", compteConnecte);
        Platform.exit();
    }

    public void essayerRecupererCompte() {
        ArrayList<String> listeBtnRouges = new ArrayList<>();
        ArrayList<String> listeBtnBleus = new ArrayList<>();
        // essayer de lire le fichier "compte.castleboard"
        Compte compte = (Compte) WrkObjectFile.deserialiseObjet("compte.castleboard");

        if (compte == null) {
            listeBtnBleus.add("Redétecter");
            listeBtnRouges.add("Nouveau");
            Pane popup = JfxPopup.afficherInformation(pnRoot, "Aucun compte n'a été détecté", listeBtnBleus, listeBtnRouges);
            pnRoot.getChildren().add(popup);
            HBox hb = (HBox) popup.getChildren().get(1);
            hb.getChildren().get(0).setOnMouseClicked(e -> {
                pnRoot.getChildren().remove(popup);
                essayerRecupererCompte();
            });
            hb.getChildren().get(1).setOnMouseClicked(e -> {
                Pane popupPseudo = JfxPopup.afficherInput(pnRoot, "Veuillez choisir un pseudo", "Choisissez un pseudo");
                pnRoot.getChildren().remove(popup);
                pnRoot.getChildren().add(popupPseudo);
                HBox hbPseudo = (HBox) popupPseudo.getChildren().get(1);
                hbPseudo.getChildren().get(1).setOnMouseClicked(ex -> {
                    String pseudo = ((TextField) hbPseudo.getChildren().get(0)).getText().trim();
                    if (pseudo.length() > 2) {
                        creerNouveauCompte(pseudo);
                        pnRoot.getChildren().remove(popupPseudo);
                    }
                });
            });
        } else {
            this.compteConnecte = compte;
            listeBtnBleus.add("Confirmer");
            listeBtnRouges.add("Abandonner");
            Pane popupConfirmation = JfxPopup.afficherInformation(pnRoot, "Le compte de " + compte.getPseudo() + " au niveau " + compte.getPlusHautNiveau().getIdNiveau() + " a été détécté. S'y connecter ?", listeBtnBleus, listeBtnRouges);
            pnRoot.getChildren().add(popupConfirmation);
            HBox hb = (HBox) popupConfirmation.getChildren().get(1);
            hb.getChildren().get(0).setOnMouseClicked(e -> {
                pnRoot.getChildren().remove(popupConfirmation);
                wrkCartes.metAJourToutesLesCartes(compte.getEquipeSelec(), compte.getToutesLesCartes());
                wrkMissions.mettreAJourPersosMissions(compte.getToutesLesMissions());
                wrkBadges.mettreAJourPersosBadges(compte);
                reinitialiserPlateauxDesNiveaux();
                afficherMenu();
            });
            hb.getChildren().get(1).setOnMouseClicked(e -> {
                Pane popupPseudo = JfxPopup.afficherInput(pnRoot, "Veuillez choisir un pseudo", "Choisissez un pseudo");
                pnRoot.getChildren().remove(popupConfirmation);
                pnRoot.getChildren().add(popupPseudo);
                HBox hbPseudo = (HBox) popupPseudo.getChildren().get(1);
                hbPseudo.getChildren().get(1).setOnMouseClicked(ex -> {
                    String pseudo = ((TextField) hbPseudo.getChildren().get(0)).getText().trim();
                    if (pseudo.length() > 2) {
                        creerNouveauCompte(pseudo);
                        pnRoot.getChildren().remove(popupPseudo);
                    }
                });
            });
        }
    }

    private void creerNouveauCompte(String pseudo) {
        ArrayList<Niveau> listeNv = new ArrayList<>();
        listeNv.add(niveauxWrk.getNiveau(1));
        ArrayList<CartePerso> cartesDeDepart = wrkCartes.getCartesDeDepart();
        this.compteConnecte = new Compte(pseudo, listeNv, cartesDeDepart, wrkMissions.getListeMissionsDidacticiel(), wrkBadges.getTousLesBadges());
        this.compteConnecte.setToutesLesOffresMagasin(wrkMagasin.getOffresDeDepart(compteConnecte));
        wrkCartes.metAJourToutesLesCartes(this.compteConnecte.getEquipeSelec(), this.compteConnecte.getToutesLesCartes());
        wrkMissions.mettreAJourPersosMissions(this.compteConnecte.getToutesLesMissions());
        afficherMenu();
    }

    public void afficherDetailsNiveau(int id, boolean animation) {
        reinitialiserPlateauxDesNiveaux();
        niveauJoue = niveauxWrk.getNiveauDuCompte(id, compteConnecte);
        // mettre a jour les details du niveau
        lblNumeroNiv.setText("Niveau " + niveauJoue.getIdNiveau());
        for (Perso perso : niveauJoue.getEquipeAdverse()) {
            ImageView img = new ImageView(assets.getImage("texturesPersos/" + perso.getNom() + "/logo.png"));
            img.setFitHeight(45);
            img.setFitWidth(45);
            //hbEquipeAdverse.getChildren().add(img);
        }
        // parcourir les objectifs
        for (int i = 0; i < niveauJoue.getListeObjectifs().size(); i++) {
            Objectif objectif = niveauJoue.getListeObjectifs().get(i);
            listeFondObjectifs[i].setVisible(true);
            if (!objectif.isAccompli()) {
                String couleurFondObj = "#";
                String couleurFondRec = "#";
                switch (objectif.getDifficulte()) {
                    case FACILE:
                        couleurFondObj += "5DFF84";
                        couleurFondRec += "53DF75";
                        break;
                    case MOYEN:
                        couleurFondObj += "FFD676";
                        couleurFondRec += "FEB100";
                        break;
                    case DIFFICILE:
                        couleurFondObj += "FF9A77";
                        couleurFondRec += "FE4100";
                        break;
                    case EXPERT:
                        couleurFondObj += "808080";
                        couleurFondRec += "545454";
                        break;
                }
                listeFondObjectifs[i].setStyle("-fx-background-color: " + couleurFondObj + "; -fx-background-radius: 15");
                listeFondRecObjectifs[i].setStyle("-fx-background-color: " + couleurFondRec + "; -fx-background-radius: 15");
                listeFondObjectifs[i].setCursor(Cursor.DEFAULT);
                listeFondObjectifs[i].setOnMouseClicked(e -> {
                });
            } else {
                if (!objectif.isRecompenseRecup()) {
                    listeFondObjectifs[i].setStyle("-fx-background-color: #FFE000; -fx-background-radius: 15");
                    listeFondRecObjectifs[i].setStyle("-fx-background-color: #FFCD00; -fx-background-radius: 15");
                } else {
                    listeFondObjectifs[i].setStyle("-fx-background-color: #93FF00; -fx-background-radius: 15");
                    listeFondRecObjectifs[i].setStyle("-fx-background-color: #8BDD15; -fx-background-radius: 15");
                }
            }
            String titre = "";
            if (!objectif.isAccompli() || objectif.isRecompenseRecup()) {
                titre = objectif.toString();

                if (objectif.isRecompenseRecup()) {
                    listeFondObjectifs[i].setCursor(Cursor.DEFAULT);
                    listeFondObjectifs[i].setOnMouseClicked(e -> {
                    });
                }

            } else {
                if (!objectif.isRecompenseRecup()) {
                    listeFondObjectifs[i].setStyle("-fx-background-color: #FFE000; -fx-background-radius: 15");
                    listeFondRecObjectifs[i].setStyle("-fx-background-color: #FFCD00; -fx-background-radius: 15");
                    listeFondObjectifs[i].setCursor(Cursor.HAND);
                    titre = "Récupérer récompense";
                    listeTextesObjectifs[i].setText(titre);
                    listeTextesObjectifs[i].setFont(Font.font("System", FontWeight.BOLD, wrkMenu.getTailleCaracteresSelonLongueur(titre)));
                    listeFondObjectifs[i].setOnMouseClicked(e -> {
                        ArrayList<Recompense> rec = new ArrayList<>();
                        rec.add(objectif.getRecompense());
                        recupererRecompense(rec);
                        objectif.setRecompenseRecup(true);
                        afficherDetailsNiveau(id, false);
                    });
                }
            }
            listeTextesObjectifs[i].setText(titre);
            listeTextesObjectifs[i].setFont(Font.font("System", FontWeight.BOLD, wrkMenu.getTailleCaracteresSelonLongueur(titre)));
            // gerer les recompenses
            String quantite = "";
            String region = "";
            if (!objectif.isRecompenseRecup()) {
                Recompense recompense = objectif.getRecompense();
                quantite = recompense.getQuantite() + "";
                if (recompense.estUnCoffre() && recompense.getCartePerso() == null) {
                    region = "Région ";
                    if (!recompense.isRegionFixe() && recompense.getRegion().getId() > 1) {
                        region += "1-" + recompense.getRegion().getId();
                    } else {
                        region += recompense.getRegion().getId();
                    }
                }
                listeImgRecObjectifs[i].setImage(assets.getImageRec(recompense));
            }
            if (objectif.isAccompli() && objectif.isRecompenseRecup()) {
                listeImgRecObjectifs[i].setImage(assets.getImage("texturesRecompenses/imgAccompli.png"));
            }

            listeTextesRecObjectifs[i].setText(quantite);
            listeTextesRegionObjectifs[i].setText(region);
        }
        Perso[][] plateau = niveauJoue.getPlateau();
        jeuCtrl.setPlateau(plateau);

        imgFondQuitter.setVisible(true);
        pnDetailsNiveau.setVisible(true);
        pnDetailsNiveau.setLayoutX(55);
        pnDetailsNiveau.setLayoutY(-553);
        if (animation) {
            wrkMenu.createAnimationDetailsNiveau(pnDetailsNiveau, true).play();
        }
    }

    public void afficherMenu() {
        pnJeu.setVisible(false);
        imgFondQuitter.setVisible(false);
        pnChargement.setVisible(false);
        pnResultats.setVisible(false);
        pnEquipe.setVisible(false);
        pnDuel.setVisible(false);
        pnMenu.setVisible(true);
        pnMagasin.setVisible(false);
        mettreAJourToutesLesCartes();
        mettreAJourRessources();
        mettreAJourRouteDesNiveaux();
        mettreAJourMissions();
        mettreAJourBadges();
        mettreAJourMagasin();
    }

    public void construireRouteDesNiveaux() {
        double posX = 0;
        double posY;
        pnCarteLevels.setPrefSize(260, (niveauxWrk.getTousLesNiveaux().size() + 1) * 91 + 100);
        pnCarteLevels.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        pnCarteLevels.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        listeNotifsNiveaux = new Button[niveauxWrk.getTousLesNiveaux().size()];
        ArrayList<Niveau> tousLesNiveaux = niveauxWrk.getTousLesNiveaux();
        for (int i = 0; i < tousLesNiveaux.size(); i++) {
            final int idNiveau = tousLesNiveaux.get(i).getIdNiveau();

            if (idNiveau % 5 == 0) {
                // creer le fond de la region chaque 5 niveaux
                ImageView imgFondRegion = new ImageView();
                imgFondRegion.setImage(assets.getImage("texturesNiveaux/fondRegions/" + tousLesNiveaux.get(i).getRegion().getNom() + ".png"));
                imgFondRegion.setFitHeight(6 * 91);
                imgFondRegion.setFitWidth(260);
                imgFondRegion.setLayoutX(0);
                imgFondRegion.setLayoutY(pnCarteLevels.getPrefHeight() - (double) (i + 1) * 91);
                pnCarteLevels.getChildren().add(imgFondRegion);
                imgFondRegion.toBack();
            }

            // determiner les coordonnees x et y
            posY = pnCarteLevels.getPrefHeight() - (double) (i + 1) * 91;
            switch (idNiveau % 4) {
                case 1:
                    posX = 167;
                    break;
                case 2:
                case 0:
                    posX = 90;
                    break;
                case 3:
                    posX = 5;
                    break;
            }

            // creer les pn des niveaux
            Pane pn = new Pane();
            pn.setLayoutX(posX);
            pn.setLayoutY(posY);
            pn.setPrefSize(78, 75);
            pn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            pn.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            pn.setCursor(Cursor.HAND);
            pn.setOnMouseClicked(e -> {
                if (this.compteConnecte.getPlusHautNiveau().getIdNiveau() >= idNiveau) {
                    afficherDetailsNiveau(idNiveau, true);
                }
            });

            // image du fond
            ImageView imgFond = new ImageView();
            imgFond.setImage(assets.getImage("texturesNiveaux/fond.png"));
            imgFond.setFitWidth(68);
            imgFond.setFitHeight(68);
            imgFond.setLayoutX(5);
            imgFond.setLayoutY(4);
            listeFondNiveaux[i] = imgFond;
            pn.getChildren().add(imgFond);

            // Label id niveau
            Label lbl = new Label(idNiveau + "");
            lbl.setPrefSize(66, 36);
            lbl.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            lbl.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            lbl.setLayoutX(6);
            lbl.setLayoutY(20);
            lbl.setFont(Font.font("System", FontWeight.BOLD, 25));
            lbl.setAlignment(Pos.CENTER);
            pn.getChildren().add(lbl);

            double[] listePosX = new double[]{9, 27, 45};
            double[] listePosY = new double[]{48, 53, 48};
            ArrayList<ImageView> listeImg = new ArrayList<>();
            for (int j = 0; j < listePosX.length; j++) {
                ImageView img = new ImageView();
                img.setImage(assets.getImage("texturesNiveaux/etoileVide.png"));
                img.setFitWidth(25);
                img.setFitHeight(25);
                img.setLayoutX(listePosX[j]);
                img.setLayoutY(listePosY[j]);
                pn.getChildren().add(img);
                listeImg.add(img);
            }
            listeImagesEtoilesNiveaux.put(idNiveau, listeImg);

            // image notif
            Button btnNotif = new Button();
            btnNotif.setFont(Font.font("System", FontWeight.BOLD, 15));
            btnNotif.setTextFill(Color.WHITE);
            btnNotif.setPrefSize(29, 31);
            btnNotif.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            btnNotif.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            btnNotif.setText("1");
            btnNotif.setStyle("-fx-background-color: red");
            btnNotif.setLayoutX(59);
            btnNotif.setLayoutY(0);
            listeNotifsNiveaux[i] = btnNotif;
            pn.getChildren().add(btnNotif);

            pnCarteLevels.getChildren().add(pn);

        }
    }

    public void mettreAJourRouteDesNiveaux() {
        ArrayList<Niveau> listeNivCompte = this.compteConnecte.getProgressionDesNiveauxJoues();
        for (int i = 0; i < niveauxWrk.getTousLesNiveaux().size(); i++) {
            int idNiveau = niveauxWrk.getTousLesNiveaux().get(i).getIdNiveau();
            if (idNiveau <= this.compteConnecte.getPlusHautNiveau().getIdNiveau()) {
                Niveau nv = niveauxWrk.getTousLesNiveaux().get(i);
                listeFondNiveaux[i].setImage(assets.getImage("texturesNiveaux/fond" + (nv.isBoss() ? "Boss" : "") + ".png"));
                listeFondNiveaux[i].setCursor(Cursor.HAND);
                // recuperer l'index du niveau
                int index = listeNivCompte.indexOf(niveauxWrk.getNiveau(idNiveau));
                // mettre a jour les etoiles
                ArrayList<ImageView> listeImgs = listeImagesEtoilesNiveaux.get(idNiveau);
                for (int j = 0; j < listeImgs.size(); j++) {
                    if (this.compteConnecte.getProgressionDesNiveauxJoues().get(i).getListeObjectifs().get(j).isAccompli()) {
                        listeImgs.get(j).setImage(assets.getImage("texturesNiveaux/etoilePleine.png"));
                    } else {
                        listeImgs.get(j).setImage(assets.getImage("texturesNiveaux/etoileVide.png"));
                    }
                }
                int recRestantes = this.compteConnecte.getProgressionDesNiveauxJoues().get(i).getRecompensesRestantes();
                if (recRestantes > 0) {
                    listeNotifsNiveaux[i].setVisible(true);
                    listeNotifsNiveaux[i].setText(recRestantes + "");
                } else {
                    listeNotifsNiveaux[i].setVisible(false);
                }

            } else {
                listeFondNiveaux[i].setImage(assets.getImage("texturesNiveaux/fondBloque.png"));
                listeFondNiveaux[i].setCursor(Cursor.DEFAULT);
                listeNotifsNiveaux[i].setVisible(false);
            }
        }
    }

    public void mettreAJourRessources() {
        lblRessPieces.setText(compteConnecte.getNombrePieces() + "");
        lblRessRubis.setText(compteConnecte.getNombreRubis() + "");
        lblRessPieces1.setText(compteConnecte.getNombrePieces() + "");
        lblRessRubis1.setText(compteConnecte.getNombreRubis() + "");
        lblRessPiecesEquipe.setText(compteConnecte.getNombrePieces() + "");
    }

    public void mettreAJourToutesLesCartes() {
        equipeCtrl.mettreAJourToutesLesCartes(pnEquipe, compteConnecte, wrkCartes.getToutesLesCartes());
    }

    public void mettreAJourMagasin() {
        magasinCtrl.mettreAJourMagasin(compteConnecte);
    }

    public void mettreAJourNotifs() {
        int nbrCartesAAmel = 0;
        for (CartePerso c : compteConnecte.getToutesLesCartes()) {
            if (c.getNombreDeCartes() >= c.getNombreDeCartesRequises()) {
                nbrCartesAAmel++;
            }
        }
        btnNotifEquipe.setText(nbrCartesAAmel + "");
        btnNotifEquipe.setVisible(nbrCartesAAmel > 0);

        for (int i = 0; i < niveauxWrk.getTousLesNiveaux().size(); i++) {
            int idNiveau = niveauxWrk.getTousLesNiveaux().get(i).getIdNiveau();
            if (idNiveau <= this.compteConnecte.getPlusHautNiveau().getIdNiveau()) {
                int recRestantes = this.compteConnecte.getProgressionDesNiveauxJoues().get(i).getRecompensesRestantes();
                if (recRestantes > 0) {
                    listeNotifsNiveaux[i].setVisible(true);
                    listeNotifsNiveaux[i].setText(recRestantes + "");
                    listeNotifsNiveaux[i].setOnAction(e -> {
                        afficherDetailsNiveau(idNiveau, true);
                    });
                } else {
                    listeNotifsNiveaux[i].setVisible(false);
                }
            } else {
                listeNotifsNiveaux[i].setVisible(false);
            }
        }

        int missionsTerminees = 0;
        for (Mission m : compteConnecte.getToutesLesMissions()) {
            if (m.isTerminee()) {
                missionsTerminees++;
            }
        }
        if (missionsTerminees > 0) {
            btnNotifMissions.setText(missionsTerminees + "");
            btnNotifMissions.setVisible(true);
        } else {
            btnNotifMissions.setVisible(false);
        }

        int nbrBadgesAAmel = 0;
        for (BadgeMaitrise b : compteConnecte.getTousLesBadges()) {
            if (b.getPoints() >= b.getPointsRequis()) {
                nbrBadgesAAmel++;
            }
        }
        btnNotifBadges.setText(nbrBadgesAAmel + "");
        btnNotifBadges.setVisible(nbrBadgesAAmel > 0);
    }

    public void mettreAJourMissions() {
        missionsCtrl.mettreAJourMissions(this.compteConnecte, niveauxWrk.getToutesLesRegions());
    }

    public void mettreAJourBadges() {
        badgesCtrl.mettreAJourBadges(compteConnecte.getTousLesBadges(), compteConnecte);
    }

    public void recupererRecompense(ArrayList<Recompense> recompenses) {
        ArrayList<Recompense> oui = new ArrayList<>();
        for (Recompense rec : recompenses) {
            if (rec.estUnCoffre() && rec.getObjetRec() != Recompense.ObjRec.CARTE) {
                for (int i = 0; i < rec.getQuantite(); i++) {
                    oui.add(rec);
                }
            } else {
                oui.add(rec);
            }

        }
        pnRecupRecompense.setVisible(true);
        recupRecCtrl.afficheRecompense(oui, compteConnecte);

        mettreAJourRessources();
    }

    public void debloquerCarte(CartePerso carte) {
        compteConnecte.getToutesLesMissions().addAll(wrkMissions.genererMissionsPerso(carte.getPerso(), 2));
        mettreAJourMissions();
        mettreAJourBadges();
    }

    public void decouvrirNouvelleRegion(app.beans.Region r) {
        compteConnecte.getToutesLesMissions().addAll(wrkMissions.genererMissionsRegion(r));
        wrkBadges.badgeDecouvrirRegion(compteConnecte.getTousLesBadges());
        mettreAJourMissions();
        mettreAJourBadges();
    }

    public void reinitialiserPlateauxDesNiveaux() {
        for (Niveau niv : compteConnecte.getProgressionDesNiveauxJoues()) {
            Niveau leNiveau = niveauxWrk.getNiveau(niv.getIdNiveau());
            niv.setEquipeAdverse(leNiveau.getEquipeAdverse());
            niv.setPlateau(leNiveau.getPlateau());
            niv.setIaEnnemi(leNiveau.getIaEnnemi());
        }
    }

    @FXML
    private void ouvrirEquipe(ActionEvent event) {
        mettreAJourToutesLesCartes();
        pnEquipe.setVisible(true);
        imgFondQuitter.setVisible(true);
    }

    @FXML
    private void ouvrirMissions(ActionEvent event) {
        pnMissions.setVisible(true);
        imgFondQuitter.setVisible(true);
    }

    @FXML
    private void ouvrirBadges(ActionEvent event) {
        mettreAJourBadges();
        badgesCtrl.remettreEnHaut();
        pnBadges.setVisible(true);
        imgFondQuitter.setVisible(true);
    }

    @FXML
    private void ouvrirBoutique(ActionEvent event) {
        mettreAJourMagasin();
        magasinCtrl.remettreEnHaut();
        pnMagasin.setVisible(true);
        imgFondQuitter.setVisible(true);
    }

    private void retournerMenu() {
        if (pnDetailsNiveau.isVisible()) {
            TranslateTransition transition = wrkMenu.createAnimationDetailsNiveau(pnDetailsNiveau, false);
            transition.setOnFinished(e -> {
                pnDetailsNiveau.setVisible(false);
                imgFondQuitter.setVisible(false);
            });
            transition.play();
        } else if (pnEquipe.isVisible()) {
            pnEquipe.setVisible(false);
            imgFondQuitter.setVisible(false);
            if (pnDuel.isVisible()) {
                imgFondQuitter.setVisible(true);
                duelCtrl.mettreAJourEquipes();
            }
        } else if (pnDuel.isVisible()) {
            pnDuel.setVisible(false);
            imgFondQuitter.setVisible(false);
            mettreAJourToutesLesCartes();
        } else if (pnBadges.isVisible()) {
            pnBadges.setVisible(false);
            imgFondQuitter.setVisible(false);
        } else if (pnMagasin.isVisible()) {
            pnMagasin.setVisible(false);
            imgFondQuitter.setVisible(false);
        }
        mettreAJourNotifs();
    }

    public void afficherResultatsPartie(int gagnant, HashMap<Perso, Compteur> tousLesCompteurs) {
        pnJeu.toBack();
        imgFondQuitter.setVisible(true);
        btnResultats.setText("Suite");

        pnResultats.setVisible(true);
        wrkMenu.afficherResultatsPartie(this, compteConnecte, niveauxWrk, wrkMissions, wrkBadges, niveauJoue, tousLesCompteurs, pnResultats, new ImageView[]{imgResultatsEtoile1, imgResultatsEtoile2, imgResultatsEtoile3});

        mettreAJourMissions();
    }

    public void afficheProgressionBadges(HashMap<BadgeMaitrise, HashMap<Mission, Integer>> badges) {

        vbProgressionBadges.getChildren().clear();
        
        double tailleVb = 20;

        for (BadgeMaitrise b : badges.keySet()) {
            // creer une section
            Pane pnBadge = new Pane();
            pnBadge.setPrefSize(326, 32);
            pnBadge.setMinSize(326, 32);
            pnBadge.setMaxSize(326, 32);
            tailleVb += 42;
            pnBadge.getStylesheets().add("resources/css/mesStyles.css");
            pnBadge.getStyleClass().add("pnSectionMission");

            ImageView imgBadge = new ImageView(assets.getImage("texturesBadges/img" + b.getRarete() + ".png"));
            imgBadge.setFitWidth(42);
            imgBadge.setFitHeight(40);
            imgBadge.setLayoutX(9);
            imgBadge.setLayoutY(-6);
            imgBadge.setPreserveRatio(true);
            pnBadge.getChildren().add(imgBadge);

            ImageView imgLogo = new ImageView();
            if (b.getCarte() != null) {
                Perso p = b.getCarte().getPerso();
                imgLogo.setImage(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/logo.png"));
            } else {
                imgLogo.setImage(assets.getImage("texturesBadges/" + b.getNom() + ".png"));
            }
            imgLogo.setFitWidth(23);
            imgLogo.setFitHeight(23);
            imgLogo.setLayoutX(16);
            imgLogo.setLayoutY(5);
            imgLogo.setPreserveRatio(true);
            imgLogo.setOpacity(0.5);
            pnBadge.getChildren().add(imgLogo);

            Label lblBadge = new Label(b.getNom());
            lblBadge.setFont(Font.font("System", FontWeight.BOLD, (b.getNom().length() > 35 ? 12 : 16)));
            lblBadge.setPrefSize(261, 25);
            lblBadge.setTextFill(Color.WHITE);
            lblBadge.setAlignment(Pos.CENTER_LEFT);
            lblBadge.setLayoutX(56);
            lblBadge.setLayoutY(4);
            pnBadge.getChildren().add(lblBadge);

            vbProgressionBadges.getChildren().add(pnBadge);

            // afficher la liste des missions completees
            for (Mission m : badges.get(b).keySet()) {
                int nbr = badges.get(b).get(m);
                Pane pnMiss = new Pane();
                pnMiss.setPrefSize(326, 49);
                pnMiss.setMinSize(326, 49);
                pnMiss.setMaxSize(326, 49);
                tailleVb += 49;
                pnMiss.getStylesheets().add("resources/css/mesStyles.css");
                pnMiss.getStyleClass().add("pnMissionRecapBadges");

                ImageView imgPts = new ImageView(assets.getImage("texturesRecompenses/imgPointDeMaitrise.png"));
                imgPts.setFitWidth(43);
                imgPts.setFitHeight(40);
                imgPts.setLayoutX(299);
                imgPts.setLayoutY(5);
                imgPts.setPreserveRatio(true);
                pnMiss.getChildren().add(imgPts);

                Label lblMission = new Label(m.miniToString());
                lblMission.setFont(Font.font("System", FontWeight.BOLD, (b.getNom().length() > 9 ? 13 : (b.getNom().length() > 9 ? 15 : 17))));
                lblMission.setPrefSize(214, 20);
                lblMission.setTextFill(Color.WHITE);
                lblMission.setAlignment(Pos.CENTER_LEFT);
                lblMission.setLayoutX(14);
                lblMission.setLayoutY(15);
                pnMiss.getChildren().add(lblMission);

                Label lblNbrPts = new Label("+ " + nbr);
                lblNbrPts.setFont(Font.font("System", FontWeight.BOLD, 22));
                lblNbrPts.setPrefSize(60, 32);
                lblNbrPts.setTextFill(Color.WHITE);
                lblNbrPts.setAlignment(Pos.CENTER_RIGHT);
                lblNbrPts.setLayoutX(237);
                lblNbrPts.setLayoutY(9);
                pnMiss.getChildren().add(lblNbrPts);

                vbProgressionBadges.getChildren().add(pnMiss);
            }
        }

        if (tailleVb < 408) {
            tailleVb = 408;
        }

        pnListeProgressionBadges.setPrefHeight(tailleVb);
        vbProgressionBadges.setPrefHeight(tailleVb);

        pnProgressionBadges.setVisible(true);
        imgFondQuitterProgBadges.setVisible(true);

    }

    public void afficheJeu() {
        imgFondQuitter.setVisible(false);
        pnDetailsNiveau.setVisible(false);
        pnMenu.setVisible(false);
        pnJeu.setVisible(true);
    }

    @FXML
    private void lancerPartie(ActionEvent event) {

//        niveauJoue = niveauxWrk.getNiveau(niveauJoue.getIdNiveau()).copieNiveau();
        jeuCtrl.setEquipe1(this.compteConnecte.getPersosEquipeSelec());
        jeuCtrl.setCartesEq1(WrkCartes.copierEquipe(compteConnecte.getEquipeSelec()));
        jeuCtrl.construirePlateau(niveauJoue.getPlateau(), pnJeu, assets);
        jeuCtrl.setEquipe2(niveauJoue.getEquipeAdverse());
        jeuCtrl.setRegion(niveauJoue.getRegion());
        jeuCtrl.debutPartie(niveauJoue.getIaEnnemi(), niveauJoue.getListeObjectifs().get(0).getPerso(), JeuCtrl.TypePartie.CAMPAGNE);
        jeuCtrl.mettreAJourPlateau();
        afficheJeu();
    }

    @FXML
    private void ouvrirDuel(ActionEvent event) {
        duelCtrl.creerSalonDuel();
        pnDuel.setVisible(true);
        imgFondQuitter.setVisible(true);
    }

}
