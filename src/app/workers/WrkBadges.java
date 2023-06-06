/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.BadgeMaitrise;
import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.Compteur;
import app.beans.Mission;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.persos.joueur.Medecin;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gumyj
 */
public class WrkBadges {

    public WrkBadges(ArrayList<CartePerso> toutesLesCartes) {
        this.tousLesBadges = new ArrayList<>();
        int i = 0;
        for (CartePerso c : toutesLesCartes) {
            BadgeMaitrise b = creerBadge(c);
            Perso p = c.getPerso();
            for (Mission m : p.getMissionsBadge()) {
                b.ajouteMethodePts(m, m.getRecompenses().get(0).getQuantite());
            }
            b.setRecompenses(p.getRecsBadge());
            tousLesBadges.add(b);
        }

        // badge Gros Degats (degats max en 1 partie)
        BadgeMaitrise badge = new BadgeMaitrise("Gros Dégâts");
        ArrayList<Recompense> recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 0));
        Mission m = new Mission(Mission.TypeMission.RECORD_DEGATS, Mission.Portee.GLOBALE, -1, recs);
        badge.ajouteMethodePts(m, 0);
        badge.setPointsRequis(new int[]{5, 8, 15, 35, 60, 100, 999});
        badge.setRecompenses(new Recompense[]{
            new Recompense(Recompense.ObjRec.SACOCHE, null, false),
            new Recompense(Recompense.ObjRec.COFFRET, null, false),
            new Recompense(Recompense.ObjRec.PIECES, 750),
            new Recompense(Recompense.ObjRec.COFFRET, 3, null, false),
            new Recompense(Recompense.ObjRec.RUBIS, 250),
            new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN, null, false)
        }
        );
        tousLesBadges.add(badge);

        // badge Explorateur
        badge = new BadgeMaitrise("Explorateur");
        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 10));
        m = new Mission(Mission.TypeMission.COMPLETER_NIV, Mission.Portee.GLOBALE, 1, recs);
        badge.ajouteMethodePts(m, 10);
        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 125));
        m = new Mission(Mission.TypeMission.DECOUVRIR_REGION, Mission.Portee.GLOBALE, 1, recs);
        badge.ajouteMethodePts(m, 125);
        badge.setPointsRequis(new int[]{20, 50, 90, 150, 300, 600, 999});
        badge.setRecompenses(new Recompense[]{
            new Recompense(Recompense.ObjRec.PIECES, 200),
            new Recompense(Recompense.ObjRec.RUBIS, 50),
            new Recompense(Recompense.ObjRec.PIECES, 750),
            new Recompense(Recompense.ObjRec.RUBIS, 200),
            new Recompense(Recompense.ObjRec.PIECES, 1500),
            new Recompense(Recompense.ObjRec.RUBIS, 500)
        }
        );
        tousLesBadges.add(badge);
        
        // badge Secouriste (soin total)
        badge = new BadgeMaitrise("Secouriste");
        recs = new ArrayList<>();
        recs.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, 0));
        m = new Mission(Mission.TypeMission.SOIN, Mission.Portee.GLOBALE, 1, recs);
        badge.ajouteMethodePts(m, 0);
        badge.setPointsRequis(new int[]{15, 50, 100, 300, 500, 1200, 10000});
        Perso p = new Medecin(1);
        badge.setRecompenses(new Recompense[]{
            new Recompense(Recompense.ObjRec.PIECES, 200),
            new Recompense(Recompense.ObjRec.SACOCHE, null, false),
            new Recompense(Recompense.ObjRec.PIECES, 750),
            new Recompense(Recompense.ObjRec.GRAND_COFFRE, null, false),
            new Recompense(Recompense.ObjRec.RUBIS, 250),
            new Recompense(Recompense.ObjRec.PIECES, 1500)
        }
        );
        tousLesBadges.add(badge);

    }

    public BadgeMaitrise creerBadge(CartePerso carte) {
        ArrayList<CartePerso> cartes = new ArrayList<>();
        cartes.add(carte);
        return creerBadges(cartes).get(0);
    }

    public ArrayList<BadgeMaitrise> creerBadges(ArrayList<CartePerso> cartes) {
        ArrayList<BadgeMaitrise> res = new ArrayList<>();
        for (CartePerso carte : cartes) {
            CartePerso c2 = new CartePerso(carte.getPerso(), carte.getRegion(), carte.isDebloque(), carte.getNiveau(), carte.getNombreDeCartes());
            res.add(new BadgeMaitrise(c2));
        }
        return res;
    }

    public ArrayList<BadgeMaitrise> mettreAJourPersosBadges(Compte compte) {
        ArrayList<BadgeMaitrise> badges = compte.getTousLesBadges();
        for (BadgeMaitrise b : badges) {
            CartePerso c = b.getCarte();
            if (c != null) {
                for (CartePerso c2 : compte.getToutesLesCartes()) {
                    if (c.getPerso().equals(c2.getPerso())) {
                        b.setCarte(new CartePerso(c2.getPerso().copierPerso(), c2.getRegion(), c2.isDebloque(), c2.getNiveau(), c2.getNombreDeCartes()));
                        Perso p = b.getCarte().getPerso();
                        b.clearMethodesPts();
                        for (Mission m : p.getMissionsBadge()) {
                            b.ajouteMethodePts(m, m.getRecompenses().get(0).getQuantite());
                        }
                        b.setRecompenses(p.getRecsBadge());
                        p.setCostumeSelec(p.getCostumeParNom("Default"));
                    }
                }
            }
        }
        return badges;
    }

    public boolean ameliorerBadge(BadgeMaitrise b) {
        boolean res = false;
        if (b.getPoints() >= b.getPointsRequis()) {
            b.ameliorer();
            res = true;
        }
        return res;
    }
    
    public void badgeDecouvrirRegion(ArrayList<BadgeMaitrise> tousLesBadges) {
        for (BadgeMaitrise b : tousLesBadges) {
            for (Mission m : b.getMethodesPtsTriees().keySet()) {
                if(m.getTypeMission().equals(Mission.TypeMission.DECOUVRIR_REGION)) {
                    b.addPoints(b.getValSelonMission(m));
                }
            }
        }
    }

    public HashMap<BadgeMaitrise, HashMap<Mission, Integer>> gererMissionsBadges(ArrayList<BadgeMaitrise> tousLesBadges, HashMap<Perso, Compteur> tousLesCompteurs, boolean victoire) {
        HashMap<BadgeMaitrise, HashMap<Mission, Integer>> res = new HashMap<>();
        int nbr;
        boolean dejaTraite;

        for (BadgeMaitrise b : tousLesBadges) {
            for (Mission m : b.getListeMissions()) {
                dejaTraite = false;
                nbr = 0;
                switch (b.getType()) {
                    case GLOBAL:
                        switch (m.getTypeMission()) {
                            case COMPLETER_NIV:
                                if(victoire) {
                                    nbr += 1;
                                }
                                break;
                            case RECORD_DEGATS:
                                for (Perso p : tousLesCompteurs.keySet()) {
                                    nbr += tousLesCompteurs.get(p).getSelonType(Mission.TypeMission.DEGATS);
                                }
                                if (nbr > b.getPoints()) {
                                    nbr -= b.getPoints();
                                    b.addPoints(nbr);
                                } else {
                                    nbr = -2;
                                }
                                dejaTraite = true;
                                break;
                            case SOIN:
                                for (Perso p : tousLesCompteurs.keySet()) {
                                    nbr += tousLesCompteurs.get(p).getSelonType(Mission.TypeMission.SOIN);
                                }
                                b.addPoints(nbr);
                                dejaTraite = true;
                                break;
                            default:
                                for (Perso p : tousLesCompteurs.keySet()) {
                                    nbr += tousLesCompteurs.get(p).getSelonType(m.getTypeMission());
                                }
                        }
                        break;
                    case PERSO:
                        if (tousLesCompteurs.containsKey(m.getPerso())) {
                            switch (m.getTypeMission()) {
                                case JOUER_NIV:
                                    nbr = 1;
                                    break;
                                case COMPLETER_NIV:
                                    if (victoire) {
                                        nbr = 1;
                                    }
                                    break;
                                default:
                                    if (m.isAvecSuper()) {
                                        nbr = tousLesCompteurs.get(m.getPerso()).getSelonType(m.getTypeMission(), Compteur.Source.SUPER);
                                    } else if (m.isAvecSousForme()) {
                                        nbr = tousLesCompteurs.get(m.getPerso()).getSelonType(m.getTypeMission(), Compteur.Source.SOUS_FORME);
                                    } else {
                                        nbr = tousLesCompteurs.get(m.getPerso()).getSelonType(m.getTypeMission());
                                    }
                                    break;
                            }
                        }
                        break;
                }

                if (nbr >= m.getNbrObjectif()) {
                    if (!dejaTraite) {
                        b.addPoints(b.getValSelonMission(m));
                    }
                    HashMap<Mission, Integer> missionsDejaLa = new HashMap<>();
                    if (res.containsKey(b)) {
                        missionsDejaLa = res.get(b);
                    }
                    missionsDejaLa.put(m, (dejaTraite ? nbr : m.getRecompenses().get(0).getQuantite()));
                    res.put(b, missionsDejaLa);
                }
            }
        }

        return res;
    }

    public static int getNbrBadges(ArrayList<BadgeMaitrise> tousLesBadges) {
        int res = 0;
        for (BadgeMaitrise b : tousLesBadges) {
            if (b.getNiveau() > 0) {
                res++;
            }
        }
        return res;
    }

    public static ArrayList<BadgeMaitrise> getTousLesBadges() {
        ArrayList<BadgeMaitrise> res = new ArrayList<>();

        for (BadgeMaitrise b : tousLesBadges) {
            res.add(b.copierBadge());
        }

        return res;
    }

    private static ArrayList<BadgeMaitrise> tousLesBadges;
}
