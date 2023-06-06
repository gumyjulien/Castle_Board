package app.workers;

import app.beans.Enchantement;
import app.beans.Perso;
import app.presentation.JeuCtrl;
import java.util.ArrayList;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    void setJeuCtrl(JeuCtrl jeuCtrl);
    JeuCtrl getJeuCtrl();
//    Perso[][] startNouvellePartie(ArrayList<Perso> equipe1, ArrayList<Perso> equipe2, String dispositionA, String dispositionB, Perso[][] plateau);
    ArrayList<String> getToutesLesActions(Perso[][] plateau, Perso perso, int indexI, int indexJ);
    int[] getPosition(Perso[][] plateau, Perso perso);
    Perso[][] deplacerPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ);
    Perso[][] enchanterPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ);
    Perso[][] attaquerPerso(Perso[][] plateau, int posI, int posJ, int destI, int destJ);
    Perso[][] attaquerPersoSpec(Perso[][] plateau, int posI, int posJ, int destI, int destJ);
    Perso[][] activerSuper(Perso[][] plateau, Perso perso, int posI, int posJ);
    void enflammerCases(Perso[][] plateau, ArrayList<Integer> casesEnFeu);
    boolean cLaFinDeLActionOuPas(Perso[][] plateau, Perso perso, int posI, int posJ, String deplacement);
    ArrayList<String> finDeLAction(Perso[][] plateau, Perso perso, int indexI, int indexJ);
    int cestLaFinDeLaction(Perso[][] plateau, ArrayList<Perso> tousLesApiculteurs, ArrayList<Perso> equipe1, ArrayList<Perso> equipe2, Perso persoCible);
    ArrayList<String> vagueDeMedicamentsMedecin(Perso[][] plateau, Perso medecin);
    Perso[][] debutDuTour(Perso[][] plateau, int camp);
    Perso[][] cestLaFinDuTour(Perso[][] plateau, int camp);
    ArrayList<Integer> genererMortSubite(ArrayList<Integer> casesEnFeu, int noTour, int nombreDeTourAvantMortSubite);
    Perso[][] enchanterPersoAutour(Perso[][] plateau, Perso perso, String enchantement, int duree);
    boolean herosEstToutSeul(Perso[][] plateau, int camp);
    boolean herosEstToutSeulExcept(Perso[][] plateau, int camp, Perso perso);
    Perso[][] controlePersosVivants(Perso[][] plateau, int i, int j, Perso persoSource);
    ArrayList<Perso> getPersosLesPlusProches(Perso[][] plateau, int posI, int posJ, int campAChercher, int nombre);
    int getDistanceTo(int posI, int posJ, int destI, int destJ);
    Image createImage(String path);
    TranslateTransition createAnimationDeplacement(ImageView imgPerso, int x, int y, double[] positionsX, double[] positionsY, double tailleDuneCase);
    TranslateTransition createAnimationAttaque(ImageView imgABouger, int x, int y, int cibleX, int cibleY, double[] positionsX, double[] positionsY, double tailleDuneCase, boolean attaqueAFaire);
    TranslateTransition createAnimationLabel(Perso perso, Perso persoSource, double[] positionsX, double[] positionsY, int tailleDUneCase, Color color, int valeur);
    TranslateTransition createAnimationProjectile(Image img, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree);
    TranslateTransition createAnimationImpact(Image img, String grandeurImpact, int sourceX, int sourceY, int cibleX, int cibleY, boolean decalerSelonDirection, double[] positionsX, double[] positionsY, int tailleDUneCase, int degats, boolean attaquerApres, boolean orienter, Enchantement enchantement, int duree);
    RotateTransition createAnimationAmelioration(Image img, int x, int y, Perso[][] plateau, Perso perso, double[] positionsX, double[] positionsY, int tailleDUneCase);
    TranslateTransition createAnimationDebutTour(Pane pnDebutTour, boolean affichageReduit, Perso[][] plateau, int tourDuJoueur);
    TranslateTransition determinerTransitionSelonPerso(Perso perso, ImageView imgABouger, Image img, String grandeurProjectile, String vitesseProjectile, int sourceX, int sourceY, int cibleX, int cibleY, double[] positionsX, double[] positionsY, int tailleDUneCase, boolean attaque);
    TranslateTransition createAnimationBarreDeroulante(Pane pnMenu, boolean aller, double duree);
    int trouveDansLEquipe(Perso perso, ArrayList<Perso> equipe);
    boolean peutAmelOuPas(int cout, int nbrEtoiles);
    Perso[][] ameliorerPerso(Perso[][] plateau, Perso persoAAmel);
    String getDirection(int posI, int posJ, int destI, int destJ);
    int getTailleCaracteresSelonLongueur(String texte);
}
