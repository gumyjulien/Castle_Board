/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.Compte;
import app.beans.Costume;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TirageCoffre;
import app.workers.WrkTirages;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author gumyj
 */
public class RecupRecompenseCtrl {

    private Assets assets;
    private WrkTirages wrkTirages;
    private Compte compteConnecte;
    private Pane pnRecupRecompense;
    private MainCtrl mainCtrl;
    private ImageView imgFond;
    private ImageView imgRecompense;
    private ImageView imgCoffre;
    private Label lblQuantiteRecompense;
    private Label lblNomRecompense;
    private ImageView imgTirages;
    private Label lblTiragesRestants;
    private ProgressBar pgbRecompense;
    private Label lblResultat;
    private ImageView imgNivRecompense;
    private Label lblDebloque;
    private ImageView imgCoffreProba;
    private ImageView imgFondQuitterProba;
    private GridPane gpProbaCoffre;
    private Label lblProbaTitreDoublon;
    private Label lblProbaChancePersoDoublon;
    private Label lblProbaChanceHerosDoublon;
    private Label lblProbaTitreNouveau;
    private Label lblProbaChanceNouvPerso;
    private Label lblProbaChanceNouvHeros;
    private Label lblInfosContenuCoffre;
    private boolean animationEnCours;
    private boolean probaAffichees;

//    private int nombreRecompensesDifferentes;
    private ArrayList<Recompense> toutesLesRecompenses;
    private int idRecompenseAffichee;
    private ArrayList<TirageCoffre> tousLesTirages;
    private int idTirageAffiche;

    public RecupRecompenseCtrl(MainCtrl mainCtrl, Pane pnRecupRecompense, Assets assets) {
        this.assets = assets;
        this.wrkTirages = new WrkTirages(mainCtrl, this);
        this.pnRecupRecompense = pnRecupRecompense;
        this.mainCtrl = mainCtrl;

        // recuperer les elements graphiques
        this.imgFond = (ImageView) this.pnRecupRecompense.getChildren().get(0);
        this.imgTirages = (ImageView) this.pnRecupRecompense.getChildren().get(1);
        this.lblTiragesRestants = (Label) this.pnRecupRecompense.getChildren().get(2);
        VBox vbRec = (VBox) this.pnRecupRecompense.getChildren().get(3);
        this.lblNomRecompense = (Label) vbRec.getChildren().get(0);
        this.imgRecompense = (ImageView) vbRec.getChildren().get(1);
        Pane pn = (Pane) vbRec.getChildren().get(2);
        this.pgbRecompense = (ProgressBar) pn.getChildren().get(0);
        this.lblResultat = (Label) pn.getChildren().get(1);
        this.imgNivRecompense = (ImageView) pn.getChildren().get(2);
        this.lblQuantiteRecompense = (Label) this.pnRecupRecompense.getChildren().get(4);
        this.lblDebloque = (Label) this.pnRecupRecompense.getChildren().get(5);
        this.imgCoffreProba = (ImageView) this.pnRecupRecompense.getChildren().get(6);
        this.imgFondQuitterProba = (ImageView) this.pnRecupRecompense.getChildren().get(7);
        this.gpProbaCoffre = (GridPane) this.pnRecupRecompense.getChildren().get(8);
        this.lblProbaTitreDoublon = (Label) this.gpProbaCoffre.getChildren().get(2);
        this.lblProbaChancePersoDoublon = (Label) this.gpProbaCoffre.getChildren().get(4);
        this.lblProbaChanceHerosDoublon = (Label) this.gpProbaCoffre.getChildren().get(6);
        this.lblProbaTitreNouveau = (Label) this.gpProbaCoffre.getChildren().get(7);
        this.lblProbaChanceNouvPerso = (Label) this.gpProbaCoffre.getChildren().get(9);
        this.lblProbaChanceNouvHeros = (Label) this.gpProbaCoffre.getChildren().get(11);
        this.lblInfosContenuCoffre = (Label) this.gpProbaCoffre.getChildren().get(12);

        this.pnRecupRecompense.setOnMouseClicked(e -> {
            if (!probaAffichees) {
                prochainTirage();
            }
        });

        this.imgCoffreProba.setOnMouseClicked(e -> {
            if (!animationEnCours) {
                montrerCacherProba(true);
            }
        });

        this.imgFondQuitterProba.setOnMouseClicked(e -> {
            montrerCacherProba(false);
        });

        montrerCacherProba(false);

    }

    public void afficheRecompense(ArrayList<Recompense> toutesLesRecompenses, Compte compte) {

        tousLesTirages = new ArrayList<>();
        this.toutesLesRecompenses = toutesLesRecompenses;
        this.compteConnecte = compte;

        // afficher la premiere recompense
        idRecompenseAffichee = -1;
        prochaineRecompense();

    }

    public void mettreAJourProbabilites(int[] nbrDoublons, int[] nbrNouveaux, double[] doublons, double[] nouveaux) {
        this.lblProbaTitreDoublon.setText("Persos et héros déjà débloqués : " + nbrDoublons[0] + " perso(s) et " + nbrDoublons[1] + " héros");
        this.lblProbaTitreNouveau.setText("Persos et héros déjà à trouver : " + nbrNouveaux[0] + " perso(s) et " + nbrNouveaux[1] + " héros");
        this.lblProbaChancePersoDoublon.setText(doublons[0] + " %");
        this.lblProbaChanceHerosDoublon.setText(doublons[1] + " %");
        this.lblProbaChanceNouvPerso.setText(nouveaux[0] + " %");
        this.lblProbaChanceNouvHeros.setText(nouveaux[1] + " %");
    }

    private void montrerCacherProba(boolean ouverture) {
        this.imgFondQuitterProba.setVisible(ouverture);
        this.gpProbaCoffre.setVisible(ouverture);
        if (!ouverture) {
            Thread th = new Thread(() -> {
                try {
                    Thread.sleep(200);
                    this.probaAffichees = false;
                } catch (InterruptedException ex) {
                }
            });
            th.start();
        } else {
            this.probaAffichees = true;
        }
    }

    private void mettreAJourTirage(TirageCoffre rec, boolean debloquee) {
        // s'il s'agit d'une ressource
        toutMontrer();
        if (rec.getRessource() != null) {
//            imgFond.setImage(assets.getImage("texturesRecompenses/fondRec/" + rec.getRessource().toString() + ".png"));
            Image imageRec = assets.getImage("texturesRecompenses/img" + rec.getRessource().toString() + ".png");
            if (rec.getRessource().equals(TirageCoffre.Ressource.POINT_DE_MAITRISE)) {
                imageRec = assets.getImage("texturesRecompenses/imgPointDeMaitrise.png");
            }
            imgRecompense.setVisible(false);
            imgRecompense.setImage(imageRec);
            lblNomRecompense.setText(rec.getRessource().toString().toUpperCase());
            animationRessource(rec, imageRec);
            if (rec.getRessource().equals(TirageCoffre.Ressource.POINT_DE_MAITRISE)) {
                imageRec = assets.getImage("texturesBadges/img" + wrkTirages.getBadgeCompte(compteConnecte, rec).getRarete() + ".png");
            }
            imgNivRecompense.setImage(imageRec);
        } else if (rec.getCostume() != null) {
            Costume costume = rec.getCostume();
            Image imageRec = assets.getImage("texturesPersos/" + costume.getPerso().getNom() + "/" + costume.getNom() + "/poseStatique.png");
            toutCacher(false);
            imgRecompense.setImage(imageRec);
            lblNomRecompense.setText(costume.getNom());
            lblDebloque.setText("NOUVEAU COSTUME DÉBLOQUÉ !");
            animationRessource(rec, imageRec);
        } else {
            Perso perso = rec.getCartePerso().getPerso();
//            imgFond.setImage(assets.getImage("texturesRecompenses/fondRecupRecompense.png"));
            imgRecompense.setImage(assets.getImage("texturesPersos/" + perso.getNom() + "/" + perso.getCostumeSelec().getNom() + "/carte.png"));
            lblNomRecompense.setText(perso.getNom());
            if (perso.isHeros()) {
                imgNivRecompense.setImage(assets.getImage("texturesAttributs/imgCouronne.png"));
            } else {
                imgNivRecompense.setImage(assets.getImage("texturesAttributs/imgEtoile" + rec.getCartePerso().getNiveau() + ".png"));
            }
            if (debloquee) {
                lblDebloque.setText("NOUVEAU " + (perso.isHeros() ? "HÉROS" : "PERSO") + " DÉBLOQUÉ !");
            }
            lblDebloque.setVisible(debloquee);
        }
        if (!tousLesTirages.isEmpty()) {
            imgTirages.setVisible(true);
            lblTiragesRestants.setVisible(true);
            imgCoffreProba.setVisible((tousLesTirages.size() > 1));
        } else {
            imgTirages.setVisible(false);
            lblTiragesRestants.setVisible(false);
            imgCoffreProba.setVisible(false);
        }
        lblTiragesRestants.setText("" + (tousLesTirages.size() - (idTirageAffiche + 1)));
        imgTirages.toFront();
        lblTiragesRestants.toFront();
        imgFondQuitterProba.toFront();
        gpProbaCoffre.toFront();
        lblQuantiteRecompense.setText("x" + rec.getQuantite());
    }

    private void prochaineRecompense() {
        // mettre a jour le compte s il y avait une recompense avant
        if (idRecompenseAffichee >= 0) {
            Recompense rec = toutesLesRecompenses.get(idRecompenseAffichee);
            if (!rec.estUnCoffre()) {
                TirageCoffre ress;
                switch (rec.getObjetRec()) {
                    case PIECES:
                        ress = new TirageCoffre(TirageCoffre.Ressource.PIECES, rec.getQuantite());
                        break;
                    case RUBIS:
                        ress = new TirageCoffre(TirageCoffre.Ressource.RUBIS, rec.getQuantite());
                        break;
                    case POINT_DE_MAITRISE:
                        ress = new TirageCoffre(TirageCoffre.Ressource.POINT_DE_MAITRISE, rec.getCartePerso(), rec.getQuantite());
                        break;
                    case COSTUME:
                        ress = new TirageCoffre(rec.getCostume());
                        break;
                    default:
                        ress = new TirageCoffre(TirageCoffre.Ressource.RUBIS, 0);
                        break;
                }
                ArrayList<TirageCoffre> tirage = new ArrayList<>();
                tirage.add(ress);
                wrkTirages.appliqueRecompensesCompte(compteConnecte, tirage);
            } else {
                wrkTirages.appliqueRecompensesCompte(compteConnecte, tousLesTirages);
            }
        }

        if (!(idRecompenseAffichee + 1 >= toutesLesRecompenses.size())) {
            idRecompenseAffichee++;
            Recompense rec = toutesLesRecompenses.get(idRecompenseAffichee);
            toutCacher(true);
            // s'il s'agit d'une ressource
            if (!rec.estUnCoffre()) {
                tousLesTirages.clear();
                TirageCoffre ress;
                switch (rec.getObjetRec()) {
                    case PIECES:
                        ress = new TirageCoffre(TirageCoffre.Ressource.PIECES, rec.getQuantite());
                        break;
                    case RUBIS:
                        ress = new TirageCoffre(TirageCoffre.Ressource.RUBIS, rec.getQuantite());
                        break;
                    case POINT_DE_MAITRISE:
                        ress = new TirageCoffre(TirageCoffre.Ressource.POINT_DE_MAITRISE, rec.getCartePerso(), rec.getQuantite());
                        break;
                    case COSTUME:
                        ress = new TirageCoffre(rec.getCostume());
                        break;
                    default:
                        ress = new TirageCoffre(TirageCoffre.Ressource.RUBIS, 0);
                        break;
                }
                boolean costume = false;
                if (rec.getObjetRec().equals(Recompense.ObjRec.COSTUME)) {
                    Perso p = rec.getCostume().getPerso();
                    animationRessource(ress, assets.getImage("texturesPersos/" + p.getNom() + rec.getCostume().getNom() + "/poseStatique.png"));
                    costume = true;
                } else {
                    animationRessource(ress, assets.getImage("texturesRecompenses/img" + ress.getRessource().toString() + ".png"));
                }
                imgTirages.setVisible(false);
                lblTiragesRestants.setVisible(false);
                mettreAJourTirage(ress, costume);
            } else {
                // si c'est un coffre
                // definir tous les tirages de celui-ci
                if (rec.getObjetRec() == Recompense.ObjRec.CARTE) {
                    tousLesTirages = wrkTirages.genereRecCarte(compteConnecte, rec.getCartePerso(), rec.getQuantite());
                } else {
                    String infoCoffre = rec.getObjetRec().getNom(true) + " peut contenir n'importe quelle carte";
                    if (rec.isRegionFixe() || rec.getRegion().getId() == 1) {
                        infoCoffre += " de la région " + rec.getRegion().getAdjectif() + " (Région " + rec.getRegion().getId() + ").";
                    } else {
                        infoCoffre += " des régions 1 à " + rec.getRegion().getId() + ".";
                    }
                    infoCoffre += " Contient " + rec.getNombreTirages() + " tirages";
                    this.lblInfosContenuCoffre.setText(infoCoffre);
                    tousLesTirages = wrkTirages.genereTiragesCoffre(compteConnecte, rec.getNombreTirages(), rec.getRegion(), rec.isRegionFixe());
                }

                idTirageAffiche = -1;
                animationCoffre(assets.getImage("texturesRecompenses/img" + rec.getObjetRec().toString() + ".png"));
            }
        } else {
            pnRecupRecompense.setVisible(false);
            mainCtrl.mettreAJourRessources();
            mainCtrl.mettreAJourToutesLesCartes();
            mainCtrl.mettreAJourMissions();
            mainCtrl.mettreAJourBadges();
        }
    }

    private void prochainTirage() {
        if (!animationEnCours) {
            if (!tousLesTirages.isEmpty() && !(idTirageAffiche + 1 >= tousLesTirages.size())) {
                idTirageAffiche++;
                TirageCoffre tirage = tousLesTirages.get(idTirageAffiche);
                toutCacher(false);
                if (tirage.getCartePerso() != null) {
                    lblTiragesRestants.setText("" + (tousLesTirages.size() - (idTirageAffiche + 1)));
                    animationCarte(tirage, tirage.getCartePerso().getPerso().isHeros(), tirage.getCartePerso().isDebloque());
                } else {
                    mettreAJourTirage(tirage, false);
                }
            } else {
                prochaineRecompense();
            }
        }
    }

    private void toutCacher(boolean controleCoffre) {
        imgRecompense.setVisible(false);
        lblNomRecompense.setVisible(false);
        lblQuantiteRecompense.setVisible(false);
        lblResultat.setVisible(false);
        pgbRecompense.setVisible(false);
        imgNivRecompense.setVisible(false);
        lblDebloque.setVisible(false);
        if (controleCoffre) {
            lblTiragesRestants.toBack();
            imgTirages.toBack();
            imgCoffreProba.toBack();
            gpProbaCoffre.toBack();
            imgFondQuitterProba.toBack();
            if (pnRecupRecompense.getChildren().size() > 9) {
                do {
                    pnRecupRecompense.getChildren().remove(pnRecupRecompense.getChildren().size() - 1);
                } while (pnRecupRecompense.getChildren().size() > 9);
            }
        }
    }

    private void toutMontrer() {
        imgRecompense.setVisible(true);
        lblNomRecompense.setVisible(true);
        lblQuantiteRecompense.setVisible(true);
        lblResultat.setVisible(true);
        pgbRecompense.setVisible(true);
        imgNivRecompense.setVisible(true);
        imgTirages.toFront();
        lblTiragesRestants.toFront();
        imgCoffreProba.toFront();
        imgFondQuitterProba.toFront();
        gpProbaCoffre.toFront();
    }

    private void animationRessource(TirageCoffre rec, Image imageRec) {
        TranslateTransition transition;
        final boolean animationPgb;
        String txtLabel = "";
        pgbRecompense.setProgress(1.0);
        if (rec.getRessource() != null) {
            switch (rec.getRessource()) {
                case PIECES:
                    transition = wrkTirages.createAnimationPieces(pnRecupRecompense, imageRec);
                    txtLabel = compteConnecte.getNombrePieces() + "";
                    break;
                case RUBIS:
                    transition = wrkTirages.createAnimationRubis(pnRecupRecompense, imageRec);
                    txtLabel = compteConnecte.getNombreRubis() + "";
                    break;
                case POINT_DE_MAITRISE:
                    transition = wrkTirages.createAnimationRubis(pnRecupRecompense, imageRec);
                    if (!wrkTirages.getBadgeCompte(compteConnecte, rec).isMax()) {
                        txtLabel = compteConnecte.getBadge(rec.getCartePerso()).getPoints() + " / " + compteConnecte.getBadge(rec.getCartePerso()).getPointsRequis();
                        pgbRecompense.setProgress((double) compteConnecte.getBadge(rec.getCartePerso()).getPoints() / (double) compteConnecte.getBadge(rec.getCartePerso()).getPointsRequis());
                    } else {
                        txtLabel = compteConnecte.getBadge(rec.getCartePerso()).getPoints() + "";
                        pgbRecompense.setProgress(1.0);
                    }
                    break;
                default:
                    transition = wrkTirages.createAnimationPieces(pnRecupRecompense, imageRec);
            }
            animationPgb = true;
        } else {
            transition = wrkTirages.createAnimationCostume(pnRecupRecompense, imageRec);
            animationPgb = false;
        }
        lblResultat.setFont(Font.font("System", FontWeight.BOLD, (txtLabel.length() > 9 ? 16 : (txtLabel.length() > 7 ? 20 : 25))));
        lblResultat.setText(txtLabel);
        animationEnCours = true;
        transition.setOnFinished(e -> {
            imgRecompense.setVisible(true);
            animationEnCours = false;
            if (animationPgb) {
                animationPgbStock(compteConnecte, rec);
            } else {
                toutCacher(false);
                lblNomRecompense.setVisible(true);
                lblDebloque.setVisible(true);
            }
        });
    }

    private void animationCoffre(Image imageRec) {
        TranslateTransition transition;
        transition = wrkTirages.createAnimationCoffre(pnRecupRecompense, imageRec);
        animationEnCours = true;
        transition.setOnFinished(e -> {
            animationEnCours = false;
            prochainTirage();
        });
    }

    private void animationCarte(TirageCoffre tirage, boolean isHeros, boolean dejaDebloquee) {
        ArrayList<Image> images = new ArrayList<>();
        String img;
        if (isHeros) {
            img = "Heros";
        } else {
            img = "Perso";
        }
        images.add(assets.getImage("texturesCartes/Vide" + img + ".png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "2.png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "3.png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "4.png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "5.png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "6.png"));
        images.add(assets.getImage("texturesCartes/Vide" + img + "7.png"));
        if (!dejaDebloquee) {
            Perso p = tirage.getCartePerso().getPerso();
            images.add(assets.getImage("texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/carte.png"));
        }
        animationEnCours = true;
        TranslateTransition transition = wrkTirages.createAnimationCarte(pnRecupRecompense, images, dejaDebloquee);
        transition.setOnFinished(e -> {
            animationEnCours = false;
            mettreAJourTirage(tirage, !dejaDebloquee);
            animationPgbStock(compteConnecte, tirage);
        });
    }

    private void animationPgbStock(Compte compte, TirageCoffre tirage) {
        animationEnCours = true;
        TranslateTransition transition = wrkTirages.createAnimationPgbStock(lblResultat, pgbRecompense, compte, tirage);
        transition.setOnFinished(e -> {
            animationEnCours = false;
        });
    }

}
