/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

/**
 *
 * @author gumyj
 */
public enum CaseItem {

    BANDAGE,
    VACCIN,
    ANTI_DOULEURS;

    @Override
    public String toString() {
        String res = "";
        String[] split = name().split("_");
        for (String string : split) {
            res += string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }
        return res;
    }

}
