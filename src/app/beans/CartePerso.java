/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author gumyj
 */
public class CartePerso implements Serializable, Comparable<CartePerso> {

    public CartePerso(Perso perso, Region region) {
        this.perso = perso;
        this.region = region;
        this.debloque = false;
        if (perso.isHeros()) {
            this.niveau = 1;
        } else {
            this.niveau = 0;
        }
        this.nombreDeCartes = 0;
    }

    public CartePerso(Perso perso, Region region, boolean debloque, int niveau, int nombreDeCartes) {
        this.perso = perso;
        this.region = region;
        this.debloque = debloque;
        this.niveau = niveau;
        this.nombreDeCartes = nombreDeCartes;
    }

    public boolean isDebloque() {
        return debloque;
    }

    public void setDebloque(boolean debloque) {
        this.debloque = debloque;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getNiveauMax() {
        int nvMax = 3;
        if (perso.isHeros()) {
            nvMax = 10;
        }
        return nvMax;
    }

    public void niveauSuperieur() {
        if (perso.isHeros()) {
            nombreDeCartes -= getNombreDeCartesRequises();
            perso.ameliorer();
        }
        this.niveau++;
    }

    public int getNombreDeCartes() {
        return nombreDeCartes;
    }

    public boolean ajouteDesCartes(int nombreDeCartes) {
        boolean deblo = false;
        if (!debloque) {
            debloque = true;
            deblo = true;
        }
        this.nombreDeCartes += nombreDeCartes;
        return deblo;
    }

    public int getNombreDeCartesRequises() {
        int nombre = 0;
        if (perso.isHeros()) {
            if (niveau < 3) {
                nombre = niveau * 25;
            } else {
                nombre = (niveau - 1) * 50;
            }
        } else {
            switch (niveau + 1) {
                case 1:
                    nombre = 3;
                    break;
                case 2:
                    nombre = 8;
                    break;
                case 3:
                    nombre = 15;
                    break;
            }
        }
        return nombre;
    }

    public int getPrixAmel() {
        int nombre = 1;
        if (perso.isHeros()) {
            nombre = getNombreDeCartesRequises() * 2;
        } else {
            switch (niveau + 1) {
                case 1:
                    nombre = 100;
                    break;
                case 2:
                    nombre = 400;
                    break;
                case 3:
                    nombre = 1000;
                    break;
            }
        }
        return nombre;
    }

    public Perso getPerso() {
        return perso;
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public int compareTo(CartePerso c) {
        Perso p = c.getPerso();
        int res = 0;

        if (p.isHeros() && !this.perso.isHeros()) {
            res = -1;
        } else if (!p.isHeros() && this.perso.isHeros()) {
            res = 1;
        }

        return res;
    }

    private final Perso perso;
    private final Region region;
    private boolean debloque;
    private int niveau;
    private int nombreDeCartes;

}
