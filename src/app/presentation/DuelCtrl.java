/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Assets;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Region;
import app.workers.NiveauxWrk;
import app.workers.WrkCartes;
import app.workers.WrkDuel;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author gumyj
 */
public class DuelCtrl {

    public DuelCtrl(Pane pnDuel, Pane pnEquipe, Pane pnJeu, MainCtrl mainCtrl, EquipeCtrl equipeCtrl, JeuCtrl jeuCtrl, Assets assets) {
        this.pnDuel = pnDuel;
        this.pnEquipe = pnEquipe;
        this.pnJeu = pnJeu;
        this.mainCtrl = mainCtrl;
        this.equipeCtrl = equipeCtrl;
        this.jeuCtrl = jeuCtrl;
        this.assets = assets;

        wrk = new WrkDuel();

        listeBtnModifierEq = new Button[2];
        listeBtnEqAlea = new Button[2];

        // recuperer les elements graphiques
        Pane pnA = (Pane) pnDuel.getChildren().get(1);
        HBox hbCartes = (HBox) pnA.getChildren().get(1);
        listeImgCartesA = new ImageView[hbCartes.getChildren().size()];
        for (int i = 0; i < listeImgCartesA.length; i++) {
            listeImgCartesA[i] = (ImageView) hbCartes.getChildren().get(i);
        }
        VBox vbBtnEq = (VBox) pnA.getChildren().get(2);
        listeBtnModifierEq[0] = (Button) vbBtnEq.getChildren().get(0);
        listeBtnEqAlea[0] = (Button) vbBtnEq.getChildren().get(1);
        HBox hbDisposition = (HBox) pnA.getChildren().get(3);
        lblDispositionA = (Label) hbDisposition.getChildren().get(1);
        btnModifDispositionA = (Button) pnA.getChildren().get(4);

        Pane pnB = (Pane) pnDuel.getChildren().get(2);
        HBox hbCartesB = (HBox) pnB.getChildren().get(1);
        listeImgCartesB = new ImageView[hbCartesB.getChildren().size()];
        for (int i = 0; i < listeImgCartesB.length; i++) {
            listeImgCartesB[i] = (ImageView) hbCartesB.getChildren().get(i);
        }
        VBox vbBtnEqB = (VBox) pnB.getChildren().get(2);
        listeBtnModifierEq[1] = (Button) vbBtnEqB.getChildren().get(0);
        listeBtnEqAlea[1] = (Button) vbBtnEqB.getChildren().get(1);
        HBox hbDispositionB = (HBox) pnB.getChildren().get(3);
        lblDispositionB = (Label) hbDispositionB.getChildren().get(1);
        btnModifDispositionB = (Button) pnB.getChildren().get(4);

        listeBtnModifierEq[0].setOnMouseClicked(e -> {
            modifierEquipe(1);
        });
        listeBtnModifierEq[1].setOnMouseClicked(e -> {
            modifierEquipe(2);
        });
        listeBtnEqAlea[0].setOnMouseClicked(e -> {
            ArrayList<CartePerso> aievu = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                CartePerso c = compte1.getToutesLesCartes().get(i);
                aievu.add(c);
                compte1.setEquipeSelec(aievu);
            }
            mettreAJourEquipes();
        });
        listeBtnEqAlea[1].setOnMouseClicked(e -> {
            ArrayList<CartePerso> aievu = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                CartePerso c = compte2.getToutesLesCartes().get(i);
                aievu.add(c);
                compte2.setEquipeSelec(aievu);
            }
            mettreAJourEquipes();
        });

        btnCommencer = (Button) pnDuel.getChildren().get(5);
        btnQuitter = (Button) pnDuel.getChildren().get(6);

        btnCommencer.setOnMouseClicked(e -> {
            commencerDuel();
        });
        
        btnQuitter.setOnMouseClicked(e -> {
            mainCtrl.afficherMenu();
        });
    }

    public void creerSalonDuel() {
        compte1 = wrk.creerSuperCompte(WrkCartes.getToutesLesCartes(), 1);
        compte2 = wrk.creerSuperCompte(WrkCartes.getToutesLesCartes(), 2);

        mettreAJourEquipes();
    }

    public void mettreAJourEquipes() {
        int i = 0;
        for (CartePerso carte : compte1.getEquipeSelec()) {
            if (carte != null) {
                listeImgCartesA[i].setImage(assets.getImage("texturesPersos/" + carte.getPerso().getNom() + "/" + carte.getPerso().getCostumeSelec().getNom() + "/carte.png"));
            } else {
                listeImgCartesA[i].setImage(assets.getImage("texturesCartes/Vide" + (i == 0 ? "Heros" : "Perso") + ".png"));
            }
            i++;
        }
        i = 0;
        for (CartePerso carte : compte2.getEquipeSelec()) {
            if (carte != null) {
                listeImgCartesB[i].setImage(assets.getImage("texturesPersos/" + carte.getPerso().getNom() + "/" + carte.getPerso().getCostumeSelec().getNom() + "/carte.png"));
            } else {
                listeImgCartesB[i].setImage(assets.getImage("texturesCartes/Vide" + (i == 0 ? "Heros" : "Perso") + ".png"));
            }
            i++;
        }
    }

    private void modifierEquipe(int idJoueur) {
        if (idJoueur == 1) {
            equipeCtrl.mettreAJourToutesLesCartes(pnEquipe, compte1, WrkCartes.getToutesLesCartes());
        } else {
            equipeCtrl.mettreAJourToutesLesCartes(pnEquipe, compte2, WrkCartes.getToutesLesCartes());
        }
        pnEquipe.setVisible(true);
    }

    private void commencerDuel() {
        if (!compte1.getEquipeSelec().contains(null) && !compte2.getEquipeSelec().contains(null)) {
            jeuCtrl.setEquipe1(this.compte1.getPersosEquipeSelec());
            jeuCtrl.setCartesEq1(WrkCartes.copierEquipe(compte1.getEquipeSelec()));
            jeuCtrl.setEquipe2(this.compte2.getPersosEquipeSelec());
            jeuCtrl.setCartesEq2(WrkCartes.copierEquipe(compte2.getEquipeSelec()));
            jeuCtrl.construirePlateau(wrk.genererPlateau(compte1.getPersosEquipeSelec(), compte2.getPersosEquipeSelec(), lblDispositionA.getText(), lblDispositionB.getText()), pnJeu, assets);
            jeuCtrl.setRegion(new Region(1, "Camp d'entraînement", "d'entraînement"));
            jeuCtrl.debutPartie(null, null, JeuCtrl.TypePartie.DUEL);
            jeuCtrl.mettreAJourPlateau();
            pnDuel.setVisible(false);
            mainCtrl.afficheJeu();
        }
    }

    private WrkDuel wrk;
    private Pane pnDuel;
    private Pane pnEquipe;
    private Pane pnJeu;
    private MainCtrl mainCtrl;
    private EquipeCtrl equipeCtrl;
    private JeuCtrl jeuCtrl;
    private Assets assets;

    private Compte compte1;
    private Compte compte2;

    private ImageView[] listeImgCartesA;
    private ImageView[] listeImgCartesB;
    private Label lblDispositionA;
    private Label lblDispositionB;
    private Button btnModifDispositionA;
    private Button btnModifDispositionB;
    private Button[] listeBtnModifierEq;
    private Button[] listeBtnEqAlea;
    private Button btnCommencer;
    private Button btnQuitter;
}
