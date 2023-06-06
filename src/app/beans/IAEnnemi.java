/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import app.workers.WorkerJeu;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author GumyJ01
 */
public class IAEnnemi {

    public enum Difficulte {
        TUTORIEL,
        FACILE,
        MOYEN,
        DIFFICILE
    }

    public IAEnnemi(Difficulte difficulte) {
        this.difficulte = difficulte;
        wrk = (WorkerJeu) WorkerJeu.getInstance();
    }

    public ActionIA getAction(Perso[][] plateau, ArrayList<Perso> equipe, int etoilesRestantes, Perso persoDejaJoue) {
        ActionIA resultat = null;
        ActionIA actionDeSecours = null;
        ActionIA attaqueDeSecours = null;
        Perso persoAtqDeSecours = null;
        // recuperer toutes les actions de tous les persos de l'equipe
        HashMap<Perso, ArrayList<TypeAction>> toutesLesActions = new HashMap<>();
        for (Perso perso : equipe) {
            int[] pos = wrk.getPosition(plateau, perso);
            if (pos != null) {
                toutesLesActions.put(perso, wrk.getToutesLesActions(plateau, perso, pos[0], pos[1]));
            }
        }

        switch (difficulte) {
            case TUTORIEL:
                // parcourir toutes les actions et obtenir la premiere "Bouger"
                for (Perso perso : equipe) {
                    ArrayList<TypeAction> actionsPersos = toutesLesActions.get(perso);
                    ArrayList<Integer> nombresAleatoires = new ArrayList<>();
                    if (actionsPersos != null && perso != persoDejaJoue) {
                        for (int a = 0; a < actionsPersos.size(); a++) {
                            int i;
                            do {
                                i = (int) (Math.random() * (actionsPersos.size()));
                            } while (nombresAleatoires.contains(i));
                            nombresAleatoires.add(i);
                            TypeAction action = actionsPersos.get(i);
                            int y = (i - (i % plateau.length)) / plateau.length;
                            int x = i - plateau.length * y;
                            int[] pos = new int[]{x, y};
                            if (TypeAction.DEPLACER.equals(action)) {
                                if (dernierePosition == null || (pos[0] != dernierePosition[0] && pos[1] != dernierePosition[1])) {
                                    resultat = new ActionIA(action, pos, i + 1, perso);
                                    dernierePosition = wrk.getPosition(plateau, perso);
                                    break;
                                } else {
                                    actionDeSecours = new ActionIA(action, pos, i + 1, perso);
                                }
                            } else if (TypeAction.ATTAQUE.equals(action)) {
                                if (persoAtqDeSecours == null || (plateau[x][y].getPv() < persoAtqDeSecours.getPv())) {
                                    attaqueDeSecours = new ActionIA(action, pos, i + 1, perso);
                                    persoAtqDeSecours = plateau[x][y];
                                }
                            }

                        }
                    }
                }
                break;
            case FACILE:
                // parcourir toutes les actions des persos
                for (Perso perso : equipe) {
                    ArrayList<TypeAction> actionsPerso = toutesLesActions.get(perso);
                    int[] posPerso = wrk.getPosition(plateau, perso);
                    int minDistancePersoProche = 1000;
                    int maxDistancePersoProche = 1000;
                    ArrayList<Integer> nombresAleatoires = new ArrayList<>();
                    if (actionsPerso != null && perso != persoDejaJoue) {
                        int[] posPersoLePlusProche = wrk.getPosition(plateau, wrk.getPersosLesPlusProches(plateau, posPerso[0], posPerso[0], 1, 1).get(0));
                        for (int a = 0; a < actionsPerso.size(); a++) {
                            int i;
                            do {
                                i = (int) (Math.random() * (actionsPerso.size()));
                            } while (nombresAleatoires.contains(i));
                            nombresAleatoires.add(i);
                            TypeAction action = actionsPerso.get(i);
                            int y = (i - (i % plateau.length)) / plateau.length;
                            int x = i - plateau.length * y;
                            int[] pos = new int[]{x, y};
                            if (TypeAction.ATTAQUE.equals(action)) {
                                if (perso.getPvPourcentage() > 30.0) {
                                    if (persoAtqDeSecours == null || (plateau[x][y].getPv() < persoAtqDeSecours.getPv())) {
                                        resultat = new ActionIA(action, pos, i + 1, perso);
                                        break;
                                    }
                                }
                            } else if (TypeAction.DEPLACER.equals(action)) {
                                if (perso.getPvPourcentage() > 30.0) {
                                    int dist = wrk.getDistanceTo(x, y, posPersoLePlusProche[0], posPersoLePlusProche[1]);
                                    if (dist < minDistancePersoProche) {
                                        resultat = new ActionIA(action, pos, i + 1, perso);
                                        minDistancePersoProche = dist;
                                    }
                                } else {
                                    int dist = wrk.getDistanceTo(x, y, posPersoLePlusProche[0], posPersoLePlusProche[1]);
                                    if (dist > maxDistancePersoProche) {
                                        resultat = new ActionIA(action, pos, i + 1, perso);
                                        maxDistancePersoProche = dist;
                                    }
                                }
                                if (dernierePosition == null || (pos[0] != dernierePosition[0] && pos[1] != dernierePosition[1])) {
                                    actionDeSecours = new ActionIA(action, pos, i + 1, perso);
                                    dernierePosition = wrk.getPosition(plateau, perso);
                                    break;
                                } else {
                                    actionDeSecours = new ActionIA(action, pos, i + 1, perso);
                                }
                            }

                        }
                    }
                }
        }
        if (resultat == null) {
            if (actionDeSecours != null) {
                resultat = actionDeSecours;
            } else {
                resultat = attaqueDeSecours;
            }
        }
        return resultat;
    }

    private Difficulte difficulte;
    private WorkerJeu wrk;
    private int[] dernierePosition;
}
