/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;

/**
 *
 * @author gumyj
 */
public class Region implements Serializable, Comparable<Region> {

    public Region(int id, String nom, String adjectif) {
        this.id = id;
        this.nom = nom;
        this.adjectif = adjectif;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdjectif() {
        return adjectif;
    }
    
    @Override
    public int compareTo(Region o) {
        int res = 0;
        if(id < o.getId()) {
            res = -1;
        } else if(id > o.getId()) {
            res = 1;
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        Region r = (Region) o;
        if (r.getId() == this.id) {
            res = true;
        }
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.id;
        return hash;
    }
    
    private final int id;
    private final String nom;
    private final String adjectif;
}
