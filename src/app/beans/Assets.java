/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author GumyJ01
 */
public class Assets {

    public Assets() {
        listeImgs = new ArrayList<>();
        listePaths = new ArrayList<>();
    }

    public Image getImage(String path) {
        Image imgRetour;
        if (listePaths.contains(path)) {
            int index = listePaths.indexOf(path);
            imgRetour = listeImgs.get(index);
        } else {
            imgRetour = createImage(path);
            if (imgRetour != null) {
                listeImgs.add(imgRetour);
                listePaths.add(path);
            }
        }
        return imgRetour;
    }

    public Image getImageRec(Recompense rec) {
        Image imgRetour;
        String path = "texturesRecompenses/img" + rec.getObjetRec().toString() + ".png";
        if (rec.getCartePerso() != null && rec.estUnCoffre()) {
            Perso p = rec.getCartePerso().getPerso();
            path = "texturesPersos/" + p.getNom() + "/" + p.getCostumeSelec().getNom() + "/carte.png";
        } else if(rec.getCostume() != null) {
            Perso p = rec.getCostume().getPerso();
            path = "texturesPersos/" + p.getNom() + "/" + rec.getCostume().getNom() + "/logo.png";
        }
        imgRetour = getImage(path);
        return imgRetour;
    }
    
    public Image getImageCostume(Costume c) {
        String path = "texturesPersos/" + c.getPerso().getNom() + "/" + c.getNom() + "/poseStatique1.png";
        return getImage(path);
    }

    private Image createImage(String path) {
        Image resultat;
        try {
            resultat = new Image("resources/images/" + path);
        } catch (Exception e) {
            resultat = null;
        }
        return resultat;
    }

    private ArrayList<Image> listeImgs;
    private ArrayList<String> listePaths;

}
