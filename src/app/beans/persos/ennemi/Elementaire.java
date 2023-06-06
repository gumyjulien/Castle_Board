/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans.persos.ennemi;

import app.beans.Perso;
import app.beans.TypeAction;
import java.util.ArrayList;

/**
 *
 * @author GumyJ01
 */
public class Elementaire extends Perso {

    public Elementaire(int pv, int dg, int vitesse, int portee, Perso.TypeDeplacement typeDeplacement, Perso.TypeAttaque typeAttaque, String element, int jetonsPuissancesRequis, ArrayList<TypeAction> evenementsPuissance, boolean instantSuper, String titreSuper, String texteSuper, String[] titreAmeliorations, String[] descAmeliorations, String description) {
        super(pv, dg, vitesse, portee, typeDeplacement, typeAttaque, 2, "Élémentaire de " + element, false, true, jetonsPuissancesRequis, evenementsPuissance, instantSuper, 0, titreSuper, texteSuper, titreAmeliorations, descAmeliorations, description);
    }
    
    @Override
    public Perso ameliorer() {
        return this;
    }
    
    @Override
    public Perso copierPerso() {
        return new Elementaire(getPv(), getDg(), getVitesse(), getPortee(), getTypeDeplacement(), getTypeAttaque(), getNom().substring(15), getJetonsPuissanceRequis(), getEvenementsPuissance(), isInstantSuper(), getTitreSuper(), getTexteSuper(), getDescAmeliorations(), getTitreAmeliorations(), getDescription());
    }
}
