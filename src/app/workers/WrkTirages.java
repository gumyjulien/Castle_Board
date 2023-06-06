/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.BadgeMaitrise;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Costume;
import app.beans.Region;
import app.beans.TirageCoffre;
import app.presentation.MainCtrl;
import app.presentation.RecupRecompenseCtrl;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 *
 * @author gumyj
 */
public class WrkTirages {

    private MainCtrl mainCtrl;
    private RecupRecompenseCtrl recupRecompenseCtrl;

    public WrkTirages(MainCtrl mainCtrl, RecupRecompenseCtrl recupRecompenseCtrl) {
        this.mainCtrl = mainCtrl;
        this.recupRecompenseCtrl = recupRecompenseCtrl;
    }
    
    public BadgeMaitrise getBadgeCompte(Compte compte, TirageCoffre tirage) {
        return compte.getBadge(tirage.getCartePerso());
    }

    public ArrayList<TirageCoffre> genereRecCarte(Compte compte, CartePerso carte, int quantite) {
        ArrayList<TirageCoffre> resultat = new ArrayList<>();

        ArrayList<CartePerso> toutesLesCartesCompte = compte.getToutesLesCartes();

        for (CartePerso laCarte : toutesLesCartesCompte) {
            if (WrkCartes.comparerCartes(laCarte, carte)) {
                resultat.add(new TirageCoffre(laCarte, quantite));
            }
        }

        return resultat;
    }

    public ArrayList<TirageCoffre> genereTiragesCoffre(Compte compte, int nombreTirages, Region region, boolean regionPrecise) {
        ArrayList<TirageCoffre> resultat = new ArrayList<>();
        ArrayList<TirageCoffre> ressources = new ArrayList<>();
        ArrayList<TirageCoffre> cartes = new ArrayList<>();
        ArrayList<TirageCoffre> nouvCartes = new ArrayList<>();
        ArrayList<TirageCoffre> bonus = new ArrayList<>();

        // generer tous les tirages
        for (int i = 0; i < nombreTirages; i++) {
            // ajouter entre 15 et 30 pieces par tirage
            int minPieces = 15;
            int maxPieces = 30;
            int pieces = (int) (Math.random() * (maxPieces - minPieces + 1) + minPieces);
            ressources.add(new TirageCoffre(TirageCoffre.Ressource.PIECES, pieces));
            /*
                Chances d'obtention :
                  * Doublons :                  -
                        - Perso (x1) -> 60%      |
                        - Heros (x5) -> 25%      |
                  * Nouveau :                    |- Selon persos debloques et pas encore trouves
                        - Perso (x1) -> 10%      |
                        - Heros (x1) -> 5%       |
                                                -
                  * Choix :
                        - Choisir entre 2 cartes -> 10%
                  * Bonus :
                        - Rubis (x3)  -> 10%
                        - Rubis (x10) ->  7%
                        - Rubis (x16) ->  4%
             */

            // recuperer toutes les cartes possibles et celles du compte
            ArrayList<CartePerso> toutesLesCartes = WrkCartes.getToutesLesCartes(region.getId(), regionPrecise);
            ArrayList<CartePerso> toutesLesCartesCompte = compte.getToutesLesCartes();
            ArrayList<CartePerso> toutesLesCartesDebloquees = new ArrayList<>();
            ArrayList<CartePerso> toutesLesCartesATrouver = new ArrayList<>();
            // separer les cartes debloquees aux non trouvees du compte
            for (CartePerso laCarte : toutesLesCartesCompte) {
                boolean controleRegion;
                if (regionPrecise) {
                    controleRegion = (laCarte.getRegion().getId() == region.getId());
                } else {
                    controleRegion = (laCarte.getRegion().getId() <= region.getId());
                }
                if (controleRegion) {
                    if (laCarte.isDebloque()) {
                        toutesLesCartesDebloquees.add(laCarte);
                    } else {
                        // rechercher la carte dans la banque de cartes
                        for (CartePerso carte : toutesLesCartes) {
                            if (WrkCartes.comparerCartes(laCarte, carte)) {
                                toutesLesCartesATrouver.add(laCarte);
                                break;
                            }
                        }
                    }
                }
            }

            TirageCoffre tirage;
            boolean correct = false;
            do {
                tirage = procederTirage(toutesLesCartes, toutesLesCartesDebloquees, toutesLesCartesATrouver);
                if (tirage.getCartePerso() != null) {
                    if (tirage.getCartePerso().getPerso().isHeros()) {
                        correct = true;
                    } else {
                        if (tirage.getCartePerso().getNombreDeCartes() < 15) {
                            correct = true;
                        }
                    }
                }
            } while (!correct);
            if (tirage.getCartePerso() != null) {
                if (toutesLesCartesDebloquees.contains(tirage.getCartePerso())) {
                    cartes.add(tirage);
                } else {
                    nouvCartes.add(tirage);
                }
            } else {
                ressources.add(tirage);
            }
        }

        // controler si l'on a obtenu deux fois la meme carte (ou ressources) et les mettre ensemble
        // ressources
        HashMap<TirageCoffre.Ressource, Integer> ressourcesPresentes = new HashMap<>();
        for (TirageCoffre tirage : ressources) {
            if (!ressourcesPresentes.containsKey(tirage.getRessource())) {
                ressourcesPresentes.put(tirage.getRessource(), tirage.getQuantite());
            } else {
                int quantite = tirage.getQuantite();
                quantite += ressourcesPresentes.get(tirage.getRessource());
                ressourcesPresentes.replace(tirage.getRessource(), quantite);
            }
        }

        ressources.clear();
        for (TirageCoffre.Ressource ressource : ressourcesPresentes.keySet()) {
            int quantite = ressourcesPresentes.get(ressource);
            ressources.add(new TirageCoffre(ressource, quantite));
        }

        // cartes
        HashMap<CartePerso, Integer> cartesPresentes = new HashMap<>();
        for (TirageCoffre tirage : cartes) {
            if (!cartesPresentes.containsKey(tirage.getCartePerso())) {
                cartesPresentes.put(tirage.getCartePerso(), tirage.getQuantite());
            } else {
                int quantite = tirage.getQuantite();
                quantite += cartesPresentes.get(tirage.getCartePerso());
                cartesPresentes.replace(tirage.getCartePerso(), quantite);
            }
        }

        cartes.clear();
        for (CartePerso carte : cartesPresentes.keySet()) {
            int quantite = cartesPresentes.get(carte);
            cartes.add(new TirageCoffre(carte, quantite));
        }

        // nouvelles cartes
        HashMap<CartePerso, Integer> nouvCartesPresentes = new HashMap<>();
        for (TirageCoffre tirage : nouvCartes) {
            if (!nouvCartesPresentes.containsKey(tirage.getCartePerso())) {
                nouvCartesPresentes.put(tirage.getCartePerso(), tirage.getQuantite());
            } else {
                int quantite = tirage.getQuantite();
                quantite += nouvCartesPresentes.get(tirage.getCartePerso());
                nouvCartesPresentes.replace(tirage.getCartePerso(), quantite);
            }
        }

        nouvCartes.clear();
        for (CartePerso carte : nouvCartesPresentes.keySet()) {
            int quantite = nouvCartesPresentes.get(carte);
            nouvCartes.add(new TirageCoffre(carte, quantite));
        }

        resultat.addAll(ressources);
        resultat.addAll(cartes);
        resultat.addAll(nouvCartes);

        return resultat;
    }

    private TirageCoffre procederTirage(ArrayList<CartePerso> toutesLesCartes, ArrayList<CartePerso> toutesLesCartesDebloquees, ArrayList<CartePerso> toutesLesCartesATrouver) {
        TirageCoffre resultat = null;

        double nbrCartesTtl = toutesLesCartes.size();
        double nbrCartesDeblo = toutesLesCartesDebloquees.size();
        double nbrCartesHerosDeblo = 0;
        double nbrCartesPersoDeblo = 0;
        for (CartePerso carte : toutesLesCartesDebloquees) {
            if (carte.getPerso().isHeros()) {
                nbrCartesHerosDeblo++;
            } else {
                nbrCartesPersoDeblo++;
            }
        }
        double nbrCartesATrouver = toutesLesCartesATrouver.size();
        double nbrCartesHerosATrouver = 0;
        double nbrCartesPersoATrouver = 0;
        for (CartePerso carte : toutesLesCartesATrouver) {
            if (carte.getPerso().isHeros()) {
                nbrCartesHerosATrouver++;
            } else {
                nbrCartesPersoATrouver++;
            }
        }

        int[] nbrCartesDebloquees = new int[]{(int) nbrCartesPersoDeblo, (int) nbrCartesHerosDeblo};
        int[] nbrNouvCartes = new int[]{(int) nbrCartesPersoATrouver, (int) nbrCartesHerosATrouver};

        double probaHerosDoublon = 0.35;
        double probaPersoDoublon = 0.5;
        double probaNouvHeros = 0.05;
        double probaNouvPerso = 0.1;

        // s il n y a pas de heros ou de perso debloque ou a trouver de cette region, repartir les probabilites
        if (nbrCartesDeblo + nbrCartesATrouver > 0) {
            if ((nbrCartesHerosATrouver + nbrCartesHerosDeblo) == 0) {
                probaHerosDoublon = 0.0;
                probaNouvHeros = 0.0;
                probaPersoDoublon += 0.15;
                probaNouvPerso += 0.05;
            } else if ((nbrCartesPersoATrouver + nbrCartesPersoDeblo) == 0) {
                probaHerosDoublon += 0.7;
                probaNouvHeros += 0.1;
                probaPersoDoublon = 0.0;
                probaNouvPerso = 0.0;
            }
        } else {
            probaHerosDoublon = 0.0;
            probaNouvHeros = 0.0;
            probaPersoDoublon = 0.0;
            probaNouvPerso = 0.00;
        }

        nbrCartesHerosATrouver *= probaNouvHeros;
        nbrCartesHerosDeblo *= probaHerosDoublon;
        nbrCartesPersoATrouver *= probaNouvPerso;
        nbrCartesPersoDeblo *= probaPersoDoublon;

        double proportionHerosDeblo = 0.5;
        double proportionHerosATrouver = 0.5;
        if (nbrCartesHerosATrouver != nbrCartesHerosDeblo) {
            double proportion;
            if (nbrCartesHerosDeblo > nbrCartesHerosATrouver) {
                proportion = (nbrCartesHerosDeblo / (nbrCartesHerosDeblo + nbrCartesHerosATrouver));
                proportionHerosDeblo = proportion;
                proportionHerosATrouver = 1 - proportion;
            } else {
                proportion = (nbrCartesHerosATrouver / (nbrCartesHerosDeblo + nbrCartesHerosATrouver));
                proportionHerosATrouver = proportion;
                proportionHerosDeblo = 1 - proportion;
            }
        }

        double proportionPersoDeblo = 0.5;
        double proportionPersoATrouver = 0.5;
        if (nbrCartesPersoATrouver != nbrCartesPersoDeblo) {
            double proportion;
            if (nbrCartesPersoDeblo > nbrCartesPersoATrouver) {
                proportion = (nbrCartesPersoDeblo / (nbrCartesPersoDeblo + nbrCartesPersoATrouver));
                proportionPersoDeblo = proportion;
                proportionPersoATrouver = 1 - proportion;
            } else {
                proportion = (nbrCartesPersoATrouver / (nbrCartesPersoDeblo + nbrCartesPersoATrouver));
                proportionPersoATrouver = proportion;
                proportionPersoDeblo = 1 - proportion;
            }
        }

        double chanceDoublonPerso = Math.round((probaNouvPerso + probaPersoDoublon) * proportionPersoDeblo * 100);
        double chanceDoublonHeros = Math.round((probaNouvHeros + probaHerosDoublon) * proportionHerosDeblo * 100);
        double chanceNouveauPerso = Math.round((probaNouvPerso + probaPersoDoublon) * proportionPersoATrouver * 100);
        double chanceNouveauHeros = Math.round((probaNouvHeros + probaHerosDoublon) * proportionHerosATrouver * 100);

        double[] probaDoublons = new double[]{chanceDoublonPerso, chanceDoublonHeros};
        double[] probaNouveaux = new double[]{chanceNouveauPerso, chanceNouveauHeros};

        recupRecompenseCtrl.mettreAJourProbabilites(nbrCartesDebloquees, nbrNouvCartes, probaDoublons, probaNouveaux);

        // proceder au tirage
        double min = 0.0;
        double max = 100.0;
        double chance = (Math.random() * (max - min + 1)) + min;
        // selon le resultat, ajouter la carte tiree
        if (chance < chanceDoublonPerso) {
            // recuperer tous les persos doublons
            ArrayList<CartePerso> tousLesPersosDoublon = new ArrayList<>();
            for (CartePerso carte : toutesLesCartesDebloquees) {
                if (!carte.getPerso().isHeros()) {
                    tousLesPersosDoublon.add(carte);
                }
            }
            // definir les limites
            min = 0;
            max = tousLesPersosDoublon.size() - 1;
            int tirage = (int) ((Math.random() * (max - min + 1)) + min);
            // ajouter la carte tiree
            resultat = new TirageCoffre(tousLesPersosDoublon.get(tirage), 1);
        } else if (chance < (chanceDoublonPerso + chanceDoublonHeros)) {
            // recuperer tous les heros doublons
            ArrayList<CartePerso> tousLesHerosDoublon = new ArrayList<>();
            for (CartePerso carte : toutesLesCartesDebloquees) {
                if (carte.getPerso().isHeros()) {
                    tousLesHerosDoublon.add(carte);
                }
            }
            // definir les limites
            min = 0;
            max = tousLesHerosDoublon.size() - 1;
            int tirage = (int) ((Math.random() * (max - min + 1)) + min);
            // ajouter la carte tiree
            resultat = new TirageCoffre(tousLesHerosDoublon.get(tirage), 5);
        } else if (chance < (chanceDoublonPerso + chanceDoublonHeros + chanceNouveauPerso)) {
            // recuperer tous les persos a trouver
            ArrayList<CartePerso> tousLesPersosATrouver = new ArrayList<>();
            for (CartePerso carte : toutesLesCartesATrouver) {
                if (!carte.getPerso().isHeros()) {
                    tousLesPersosATrouver.add(carte);
                }
            }
            // definir les limites
            min = 0;
            max = tousLesPersosATrouver.size() - 1;
            int tirage = (int) ((Math.random() * (max - min + 1)) + min);
            // ajouter la carte tiree
            resultat = new TirageCoffre(tousLesPersosATrouver.get(tirage), 1);
        } else if (chance < (chanceDoublonPerso + chanceDoublonHeros + chanceNouveauPerso + chanceNouveauHeros)) {
            // recuperer tous les heros a trouver
            ArrayList<CartePerso> tousLesPersosATrouver = new ArrayList<>();
            for (CartePerso carte : toutesLesCartesATrouver) {
                if (carte.getPerso().isHeros()) {
                    tousLesPersosATrouver.add(carte);
                }
            }
            // definir les limites
            min = 0;
            max = tousLesPersosATrouver.size() - 1;
            int tirage = (int) ((Math.random() * (max - min + 1)) + min);
            // ajouter la carte tiree
            resultat = new TirageCoffre(tousLesPersosATrouver.get(tirage), 5);
        } else {
            resultat = new TirageCoffre(TirageCoffre.Ressource.PIECES, 50);
        }

        return resultat;
    }

    public void appliqueRecompensesCompte(Compte compte, ArrayList<TirageCoffre> tirages) {
        // ajouter toutes les ressources/cartes au compte
        for (TirageCoffre tirage : tirages) {
            if (tirage.getRessource() != null) {
                switch (tirage.getRessource()) {
                    case PIECES:
                        compte.ajouterPieces(tirage.getQuantite());
                        break;
                    case RUBIS:
                        compte.ajouterRubis(tirage.getQuantite());
                        break;
                    case POINT_DE_MAITRISE:
                        compte.getBadge(tirage.getCartePerso()).addPoints(tirage.getQuantite());
                        break;
                }
            } else if (tirage.getCartePerso() != null) {
                CartePerso laCarte = tirage.getCartePerso();
                ArrayList<CartePerso> toutesLesCartesDuCompte = compte.getToutesLesCartes();
                for (CartePerso uneCarte : toutesLesCartesDuCompte) {
                    if (WrkCartes.comparerCartes(laCarte, uneCarte)) {
                        boolean debloquee = uneCarte.ajouteDesCartes(tirage.getQuantite());
                        if (debloquee) {
                            mainCtrl.debloquerCarte(uneCarte);
                        }
                    }
                }
            } else if (tirage.getCostume() != null) {
                Costume costume = tirage.getCostume();
                ArrayList<CartePerso> toutesLesCartesDuCompte = compte.getToutesLesCartes();
                for (CartePerso uneCarte : toutesLesCartesDuCompte) {
                    if (uneCarte.getPerso().equals(costume.getPerso())) {
                        uneCarte.getPerso().setCostumeSelec(costume);
                    }
                }
            }
        }
    }

    public TranslateTransition createAnimationPieces(Pane pnRecupRecompense, Image img) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(148);
        imageView.setFitHeight(168);
        imageView.setLayoutY(218);
        imageView.setPreserveRatio(true);
        imageView.setImage(img);

        TranslateTransition transitionX = new TranslateTransition();
        transitionX.setToX(235);
        transitionX.setDuration(Duration.seconds(0.5));
        transitionX.setNode(imageView);

        RotateTransition transitionRotation = new RotateTransition();
        transitionRotation.setToAngle(360);
        transitionRotation.setDuration(Duration.seconds(0.5));
        transitionRotation.setNode(imageView);

        pnRecupRecompense.getChildren().add(imageView);

        transitionX.play();
        transitionRotation.play();

        transitionRotation.setOnFinished(e -> {
            pnRecupRecompense.getChildren().remove(imageView);
        });

        return transitionX;
    }

    public TranslateTransition createAnimationRubis(Pane pnRecupRecompense, Image img) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(148);
        imageView.setFitHeight(168);
        imageView.setLayoutX(234);
        imageView.setLayoutY(-19);
        imageView.setPreserveRatio(true);
        imageView.setImage(img);

        TranslateTransition transitionX = new TranslateTransition();
        transitionX.setToY(237);
        transitionX.setDuration(Duration.seconds(0.5));
        transitionX.setNode(imageView);

        RotateTransition transitionRotation = new RotateTransition();
        transitionRotation.setToAngle(360);
        transitionRotation.setDuration(Duration.seconds(0.5));
        transitionRotation.setNode(imageView);

        pnRecupRecompense.getChildren().add(imageView);

        transitionX.play();
        transitionRotation.play();

        transitionRotation.setOnFinished(e -> {
            pnRecupRecompense.getChildren().remove(imageView);
        });

        return transitionX;
    }

    public TranslateTransition createAnimationCostume(Pane pnRecupRecompense, Image img) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(148);
        imageView.setFitHeight(168);
        imageView.setLayoutX(234);
        imageView.setLayoutY(266);
        imageView.setPreserveRatio(true);
        imageView.setImage(img);

        TranslateTransition transitionX = new TranslateTransition();
        transitionX.setToY(0);
        transitionX.setDuration(Duration.seconds(0.5));
        transitionX.setNode(imageView);

        ScaleTransition transition3 = new ScaleTransition();
        transition3.setByX(-0.5);
        transition3.setByY(-0.5);
        transition3.setDuration(Duration.seconds(0.0001));
        transition3.setNode(imageView);
        transition3.play();

        ScaleTransition transition2 = new ScaleTransition();
        transition2.setByX(0.5);
        transition2.setByY(0.5);
        transition2.setDuration(Duration.seconds(0.5));
        transition2.setNode(imageView);

        pnRecupRecompense.getChildren().add(imageView);

        transitionX.play();
        transition2.play();

        return transitionX;
    }

    public TranslateTransition createAnimationCoffre(Pane pnRecupRecompense, Image img) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(198);
        imageView.setFitHeight(218);
        imageView.setLayoutX(214);
        imageView.setLayoutY(100);
        imageView.setPreserveRatio(true);
        imageView.setImage(img);

        TranslateTransition transitionX = new TranslateTransition();
        transitionX.setToY(416);
        transitionX.setDuration(Duration.seconds(0.5));
        transitionX.setNode(imageView);

        pnRecupRecompense.getChildren().add(imageView);

        transitionX.play();

        return transitionX;
    }

    public TranslateTransition createAnimationCarte(Pane pnRecupRecompense, ArrayList<Image> images, boolean dejaDebloquee) {
        Image imgFincarte = null;
        if (!dejaDebloquee) {
            imgFincarte = images.get(images.size() - 1);
            images.remove(images.size() - 1);
        }

        final Image imgFinCarte = imgFincarte;

        ImageView imageView = new ImageView();
        imageView.setFitWidth(148);
        imageView.setFitHeight(168);
        imageView.setLayoutX(237);
        imageView.setLayoutY(516);
        imageView.setPreserveRatio(true);
        imageView.setImage(images.get(0));

        TranslateTransition transitionX = new TranslateTransition();
        double duree;
        if (dejaDebloquee) {
            duree = 0.5;
        } else {
            duree = 4;
        }
        transitionX.setDuration(Duration.seconds(duree));
        transitionX.setNode(imageView);

        TranslateTransition transitionY = new TranslateTransition();
        transitionY.setToY(-298);
        if (dejaDebloquee) {
            duree = 0.5;
        } else {
            duree = 3;
        }
        transitionY.setDuration(Duration.seconds(duree));
        transitionY.setNode(imageView);

        if (!dejaDebloquee) {
            ScaleTransition transition2 = new ScaleTransition();
            transition2.setByX(0.3);
            transition2.setByY(0.3);
            transition2.setDuration(Duration.seconds(2.5));
            transition2.setNode(imageView);

            ScaleTransition transition3 = new ScaleTransition();
            transition3.setByX(-0.3);
            transition3.setByY(-0.3);
            transition3.setDelay(Duration.seconds(1));
            transition3.setDuration(Duration.seconds(0.5));
            transition3.setNode(imageView);

            transition2.play();

            transition2.setOnFinished(e -> {
                transition3.play();
            });
            transition3.setOnFinished(e -> {
                pnRecupRecompense.getChildren().remove(imageView);
            });

        } else {
            transitionY.setOnFinished(e -> {
                pnRecupRecompense.getChildren().remove(imageView);
            });
        }

        pnRecupRecompense.getChildren().add(imageView);

        transitionX.play();
        transitionY.play();

        if (!dejaDebloquee) {

        } else {

        }

        Thread th = new Thread(() -> {
            if (dejaDebloquee) {
                for (int i = 0; i < images.size(); i++) {
                    try {
                        imageView.setImage(images.get(i));
                        Thread.sleep(500 / images.size());
                    } catch (InterruptedException ex) {
                    }
                }
            } else {
                for (int j = 0; j < 6; j++) {
                    for (int i = 0; i < images.size(); i++) {
                        try {
                            imageView.setImage(images.get(i));
                            Thread.sleep(250 / images.size());
                        } catch (InterruptedException ex) {
                        }
                    }
                    for (int i = images.size() - 1; i >= 0; i--) {
                        try {
                            imageView.setImage(images.get(i));
                            Thread.sleep(250 / images.size());
                        } catch (InterruptedException ex) {
                        }
                    }
                }
                imageView.setImage(imgFinCarte);
            }
        });

        th.start();

        return transitionX;
    }

    public TranslateTransition createAnimationPgbStock(Label lbl, ProgressBar pgb, Compte compte, TirageCoffre tirage) {

        boolean isRessource = false;
        if (tirage.getRessource() != null) {
            isRessource = true;
        }

        int valInit = 0;
        int valFin = 0;
        if (isRessource) {
            switch (tirage.getRessource()) {
                case PIECES:
                    valInit = compte.getNombrePieces();
                    break;
                case RUBIS:
                    valInit = compte.getNombreRubis();
                    break;
                case POINT_DE_MAITRISE:
                    valInit = compte.getBadge(tirage.getCartePerso()).getPoints();
                    break;
            }
            if (tirage.getRessource().equals(TirageCoffre.Ressource.POINT_DE_MAITRISE) && !compte.getBadge(tirage.getCartePerso()).isMax()) {
                valFin = compte.getBadge(tirage.getCartePerso()).getPointsRequis();
                isRessource = false;
            } else {
                valFin = valInit + tirage.getQuantite();
            }
        } else {
            CartePerso laCarte = null;
            for (CartePerso uneCarteCompte : compte.getToutesLesCartes()) {
                if (WrkCartes.comparerCartes(tirage.getCartePerso(), uneCarteCompte)) {
                    laCarte = uneCarteCompte;
                    break;
                }
            }
            if (laCarte != null) {
                valInit = laCarte.getNombreDeCartes();
                valFin = laCarte.getNombreDeCartesRequises();
            }
        }

        final int valInitiale = valInit;
        final int valFinale = valFin;
        final int quantite = tirage.getQuantite();
        final boolean estRessource = isRessource;

        Thread th = new Thread(() -> {
            String texte;
            double valeur = 0.0;
            int diff;
            if (estRessource) {
                double facteur = 1;
                diff = valFinale - valInitiale;
                if (diff > 500) {
                    facteur = 2 * (diff / 500);
                }
                for (int i = 1; i <= diff; i += facteur) {
                    try {
                        int val = valInitiale + i;
                        texte = val + "";
                        valeur = 1;
                        final String txtLabel = texte;
                        final double valPgb = valeur;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lbl.setText(txtLabel);
                                lbl.setFont(Font.font("System", FontWeight.BOLD, (txtLabel.length() > 9 ? 16 : (txtLabel.length() > 7 ? 20 : 25))));
                                pgb.setProgress(valPgb);
                            }
                        });
                        Thread.sleep(500l / (long) diff);
                    } catch (InterruptedException ex) {
                    }
                }
            } else {
                diff = quantite;
                for (int i = 1; i <= diff; i++) {
                    int frames = 20;
                    if (diff >= 5) {
                        frames = 1;
                    }
                    int val = valInitiale + i;
                    texte = val + " / " + valFinale;
                    for (int j = 1; j <= frames; j++) {
                        try {
                            if (val < valFinale) {
                                double valPgbStart = valInitiale / valFinale;
                                double valPgbEnd = (double) val / valFinale;
                                valeur = valPgbStart + (double) j * (valPgbEnd / (double) frames);
                                if (valeur > 1) {
                                    valeur = 1;
                                }
                            } else {
                                valeur = 1;
                            }
                            final String txtLabel = texte;
                            final double valPgb = valeur;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl.setText(txtLabel);
                                    lbl.setFont(Font.font("System", FontWeight.BOLD, (txtLabel.length() > 9 ? 16 : (txtLabel.length() > 7 ? 20 : 25))));
                                    pgb.setProgress(valPgb);
                                }
                            });
                            Thread.sleep((long) (250d / (double) diff / (double) frames));
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        }
        );

        th.start();

        TranslateTransition transitionX = new TranslateTransition();
        if (isRessource) {
            transitionX.setDuration(Duration.seconds(0.5));
        } else {
            transitionX.setDuration(Duration.seconds(0.25));
        }
        transitionX.setNode(lbl);
        transitionX.play();
        return transitionX;
    }

}
