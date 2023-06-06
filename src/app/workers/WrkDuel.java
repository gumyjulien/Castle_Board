/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Perso;
import java.util.ArrayList;

/**
 *
 * @author gumyj
 */
public class WrkDuel {
    
    public Compte creerSuperCompte(ArrayList<CartePerso> toutesLesCartes, int camp) {
        ArrayList<CartePerso> res = new ArrayList<>();
        for (CartePerso c : toutesLesCartes) {
            res.add(new CartePerso(c.getPerso().copierPerso(), c.getRegion(), c.isDebloque(), c.getNiveau(), c.getNombreDeCartes()));
        }
        for (CartePerso c : res) {
            int nivMin = (c.getPerso().isHeros() ? 1 : 0);
            for (int i = nivMin; i < c.getNiveauMax(); i++) {
                c.ajouteDesCartes(c.getNombreDeCartesRequises() - c.getNombreDeCartes());
                c.niveauSuperieur();
            }
        }
        Compte compte = new Compte(res, camp);
        return compte;
    }
    
    public Perso[][] genererPlateau(ArrayList<Perso> equipe1, ArrayList<Perso> equipe2, String dispositionA, String dispositionB) {
        // création du plateau
        Perso[][] plateau = new Perso[4][6];

        // selon la disposition, on pose les persos au bon endroit
        switch (dispositionA) {
            case "Ligne":
                /*         0  1  2  3
                          ___________
                       0 | H  1  2  3
                       1 | -  -  -  -
                 */
                // joueur 1
                plateau[0][0] = equipe1.get(0);
                plateau[1][0] = equipe1.get(1);
                plateau[2][0] = equipe1.get(2);
                plateau[3][0] = equipe1.get(3);
                break;
            case "Coin":
                /*         0  1  2  3
                          ___________
                       0 | H  1  -  -
                       1 | 2  3  -  -
                 */
                // joueur 1
                plateau[0][0] = equipe1.get(0);
                plateau[1][0] = equipe1.get(1);
                plateau[0][1] = equipe1.get(2);
                plateau[1][1] = equipe1.get(3);
                break;
            case "Héros solitaire":
                /*         0  1  2  3
                          ___________
                       0 | 1  2  -  H
                       1 | 3  -  -  -
                 */
                // joueur 1
                plateau[3][0] = equipe1.get(0);
                plateau[0][0] = equipe1.get(1);
                plateau[1][0] = equipe1.get(2);
                plateau[0][1] = equipe1.get(3);
                break;
            case "Aléatoire":
                int posI;
                int posJ;
                // joueur 1
                for (Perso perso : equipe1) {
                    do {
                        posI = (int) (Math.random() * (3 - 0 + 1));
                        posJ = (int) (Math.random() * (1 - 0 + 1));
                    } while (plateau[posI][posJ] != null);
                    plateau[posI][posJ] = perso;
                }

        }
        switch (dispositionB) {
            case "Ligne":
                /*         0  1  2  3
                          ___________
                          ...........
                       4 | -  -  -  -
                       5 | 3  2  1  H
                 */
                // joueur 2
                plateau[3][5] = equipe2.get(0);
                plateau[2][5] = equipe2.get(1);
                plateau[1][5] = equipe2.get(2);
                plateau[0][5] = equipe2.get(3);
                break;
            case "Coin":
                /*         0  1  2  3
                          ___________
                          ...........
                       4 | -  -  3  2
                       5 | -  -  1  H
                 */
                // joueur 2
                plateau[3][5] = equipe2.get(0);
                plateau[2][5] = equipe2.get(1);
                plateau[3][4] = equipe2.get(2);
                plateau[2][4] = equipe2.get(3);
                break;
            case "Héros solitaire":
                /*         0  1  2  3
                          ___________
                          ...........
                       4 | -  -  -  3
                       5 | H  -  2  1
                 */
                // joueur 2
                plateau[0][5] = equipe2.get(0);
                plateau[3][5] = equipe2.get(1);
                plateau[2][5] = equipe2.get(2);
                plateau[3][4] = equipe2.get(3);
                break;
            case "Aléatoire":
                int posI;
                int posJ;
                // joueur 2
                for (Perso perso : equipe2) {
                    do {
                        posI = (int) (Math.random() * (3 - 0 + 1));
                        posJ = (int) (Math.random() * (5 - 4 + 1) + 4);
                    } while (plateau[posI][posJ] != null);
                    plateau[posI][posJ] = perso;
                }

        }
        return plateau;
    }
    
}
