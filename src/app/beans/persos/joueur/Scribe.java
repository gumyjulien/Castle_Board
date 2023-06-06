/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans.persos.joueur;

import app.beans.ActionPerso;
import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author GumyJ01
 */
public class Scribe extends Perso {
    
    public static final String description = "Avec sa tablette de pierre toujours à portée de main, le Scribe prend note de toutes les actions d'un perso allié, pour ensuite les reproduire.";
    public static final String[] titreAmeliorations = new String[] {"Biographie", "Sacrement brisé", "Intelligence"};
    public static final String[] descAmeliorations = new String[] {"Débloque sa capacité " + titreAmeliorations[0], "Dès 3 actions écrites, la prochaine attaque brise la tablette sur la cible qui inflige 3 dégâts et étourdit celle-ci", titreAmeliorations[0] + ": +1 action effectuée ET -2 jetons de puissance requis"};
    

    public Scribe(int camp) {
        super(11, 1, 1, 1, camp, "Scribe", false, true, -1, new ArrayList<TypeAction>(), true, 0, titreAmeliorations[0], "Observe un perso allié et reproduit la première et dernière action après en avoir effectué 5", titreAmeliorations, descAmeliorations, description);
        actionsEcrites = new ArrayList<>();
        persoObserve = null;
    }

    public ArrayList<ActionPerso> getActionsEcrites() {
        return actionsEcrites;
    }
    
    public ArrayList<ActionPerso> getPremEtDerAction() {
        ArrayList<ActionPerso> res = new ArrayList<>();
        res.add(actionsEcrites.get(0));
        res.add(actionsEcrites.get(actionsEcrites.size() - 1));
        return res;
    }

    public void ajouteActionEcrite(ActionPerso action) {
        this.actionsEcrites.add(action);
    }

    public Perso getPersoObserve() {
        return persoObserve;
    }

    public void setPersoObserve(Perso persoObserve) {
        this.persoObserve = persoObserve;
    }
    
    public void commencerObservation(Perso p) {
        this.persoObserve = p;
        actionsEcrites = new ArrayList<>();
        setJetonsPuissanceRequis(5);
    }

    @Override
    public Perso copierPerso() {
        Perso p = new Scribe(getCamp());
        for (int i = 1; i < getNmbrEtoiles(); i++) {
            p.ameliorer();
        }
        return p;
    }

    @Override
    public Perso ameliorer() {
        int niveau = getNmbrEtoiles() + 1;
        switch(niveau) {
            case 3:
                setPvMax(getPvMax() + 2);
                break;
            case 4:
                setPvMax(getPvMax() + 1);
                break;
            case 10:
                setJetonsPuissanceRequis(3);
                break;
            default:
                setPvMax(getPvMax() + 1);
        }
        setPv(getPvMax());
        setNmbrEtoiles(niveau);
        return this;
    }
    
    private ArrayList<ActionPerso> actionsEcrites;
    private Perso persoObserve;
}
