/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.CartePerso;
import app.beans.Compte;
import app.beans.OffreMagasin;
import app.beans.Perso;
import app.beans.Recompense;
import app.beans.TirageCoffre;
import app.beans.persos.joueur.ApiculteurRoyal;
import app.beans.persos.joueur.Archere;
import app.beans.persos.joueur.Barbare;
import app.beans.persos.joueur.LeCoureur;
import app.beans.persos.joueur.MaitreDesSabres;
import app.beans.persos.joueur.Medecin;
import app.beans.persos.joueur.MiniBIG;
import app.beans.persos.joueur.Nain;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gumyj
 */
public class WrkMagasin {

    public WrkMagasin(NiveauxWrk wrkNiveaux) {
        this.wrkNiveaux = wrkNiveaux;

        genererToutesLesOffresEnVrac();
        genererRecsTresorerie();

        toutesLesOffres = new ArrayList<>();

        Perso p = new LeCoureur(1);
        p.setCostumeSelec(p.getCostumeParNom("Astronaute"));
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, wrkNiveaux.getToutesLesRegions().get(0)), 25));
        recompenses.add(new Recompense(p.getCostumeParNom("Astronaute")));
        OffreMagasin o = new OffreMagasin(OffreMagasin.TypeOffre.CARTE_COSTUME, recompenses, 0, TirageCoffre.Ressource.RUBIS);
        o.setNom("Cadeau pour les beta testeurs ;)");
        toutesLesOffres.add(o);

        recompenses = new ArrayList<>();
        p = new ApiculteurRoyal(1);
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, wrkNiveaux.getToutesLesRegions().get(8)), 100));
        recompenses.add(new Recompense(p.getCostumeParNom("Plongeur")));
        o = new OffreMagasin(OffreMagasin.TypeOffre.CARTE_COSTUME, recompenses, 1200, TirageCoffre.Ressource.RUBIS);
        toutesLesOffres.add(o);

        p = new Archere(1);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, wrkNiveaux.getToutesLesRegions().get(8)), 11));
        recompenses.add(new Recompense(p.getCostumeParNom("Sirène")));
        o = new OffreMagasin(OffreMagasin.TypeOffre.CARTE_COSTUME, recompenses, 1200, TirageCoffre.Ressource.RUBIS);
        toutesLesOffres.add(o);
    }

    public ArrayList<OffreMagasin> getOffresDeDepart(Compte c) {
        ArrayList<OffreMagasin> res = new ArrayList<>();

        res.addAll(toutesLesOffres);

        for (int i = 0; i < 8; i++) {
            res.add(genereOffreEnVrac(c));
        }

        return res;
    }

    public boolean acheterOffre(OffreMagasin o, Compte c) {
        boolean res = false;

        switch (o.getRessPrix()) {
            case PIECES:
                if (c.getNombrePieces() >= o.getPrix()) {
                    res = true;
                    c.setNombrePieces(c.getNombrePieces() - o.getPrix());
                }
                break;
            case RUBIS:
                if (c.getNombreRubis() >= o.getPrix()) {
                    res = true;
                    c.setNombreRubis(c.getNombreRubis() - o.getPrix());
                }
                break;
        }

        return res;
    }

    public ArrayList<Recompense> verifierCode(Compte c, String code) {
        ArrayList<Recompense> res = new ArrayList<>();
        if (code != null) {
            for (String unCode : recsTresorerie.keySet()) {
                if (code.equals(unCode) && !c.getCodesUtilises().contains(unCode)) {
                    res = recsTresorerie.get(unCode);
                    c.addCodeUtilise(unCode);
                }
            }
        }
        return res;
    }

    public OffreMagasin genereOffreEnVrac(Compte c) {
        int min = 0;
        int max = toutesLesOffresEnVrac.size() - 1;
        int chance;
        OffreMagasin o;
        chance = (int) ((Math.random() * (max - min + 1)) + min);
        o = toutesLesOffresEnVrac.get(chance);
        if (o.getRecompenses().get(0).getObjetRec().equals(Recompense.ObjRec.CARTE)) {
            // generer une offre de carte selon celles du compte
            o = genererOffreEnVracCarte(c, (o.getPrix() == 0));
        } else if (o.getRecompenses().get(0).estUnCoffre()) {
            o.getRecompenses().get(0).setRegion(c.getPlusHauteRegionDecouverte());
            o.getRecompenses().get(0).setRegionFixe(false);
        }
        return o;
    }

    private void genererRecsTresorerie() {
        recsTresorerie = new HashMap<>();

        ArrayList<Recompense> rec;
        Perso p;

        p = new MaitreDesSabres(1);
        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 75));
        rec.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 500));
        recsTresorerie.put("723fc300b93ed87c048754475e2e40837117d8e482874a09240881e14c1e7c78", rec);

        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.PIECES, 500));
        rec.add(new Recompense(Recompense.ObjRec.RUBIS, 200));
        recsTresorerie.put("2a81c7633bcade574008001cf940b80fd76648f8bbf6158feafc4cc05016d70e", rec);

        rec = new ArrayList<>();
        p = new Barbare(1);
        rec.add(new Recompense(p.getCostumeParNom("Sombrero")));
        p = new MiniBIG(1);
        rec.add(new Recompense(p.getCostumeParNom("C.A.T")));
        p = new Archere(1);
        rec.add(new Recompense(p.getCostumeParNom("Sirène")));
        p = new ApiculteurRoyal(1);
        rec.add(new Recompense(p.getCostumeParNom("Plongeur")));
        p = new MaitreDesSabres(1);
        rec.add(new Recompense(p.getCostumeParNom("Maître obscur")));
        p = new LeCoureur(1);
        rec.add(new Recompense(p.getCostumeParNom("Astronaute")));
        recsTresorerie.put("5265e9d7fa8d2c08db927a5e0fba111e8a68e0d0b409d64f7f4c944e473c2569", rec);

        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.COFFRE_ROYAL, wrkNiveaux.getToutesLesRegions().get(1), false));
        rec.add(new Recompense(Recompense.ObjRec.RUBIS, 100));
        recsTresorerie.put("e23c3d7ff76f6e6235ce091f2fcd5fd35748677799d1637acf5ba2bca350e258", rec);

        p = new Barbare(1);
        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 11));
        recsTresorerie.put("9fd984f672d47fe3c3c50ed9de376c7d7f1adf1906beec17051f5f65b79eae3c", rec);

        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.RUBIS, 1));
        recsTresorerie.put("6675dba42e1cb8ce5d02b12c520fa32af5dca471e23c28d0b46e32a8451c25da", rec);

        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.PIECES, 500));
        rec.add(new Recompense(Recompense.ObjRec.RUBIS, 200));
        recsTresorerie.put("2a81c7633bcade574008001cf940b80fd76648f8bbf6158feafc4cc05016d70e", rec);

        p = new ApiculteurRoyal(1);
        rec = new ArrayList<>();
        rec.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 5));
        rec.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 400));
        p = new Archere(1);
        rec.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 1));
        rec.add(new Recompense(Recompense.ObjRec.POINT_DE_MAITRISE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 400));
        recsTresorerie.put("42d2b8496338da14f96f3cfd86816a016bb42fe88ae423200a2981d5ecbe8d5c", rec);

    }

    private OffreMagasin genererOffreEnVracCarte(Compte c, boolean gratuite) {
        ArrayList<CartePerso> toutesLesCartes = new ArrayList<>();
        for (CartePerso carte : c.getToutesLesCartes()) {
            boolean cartesManquantes = (!carte.getPerso().isHeros() ? carte.getNombreDeCartes() < 11 : true);
            if (carte.getRegion().getId() <= c.getPlusHauteRegionDecouverte().getId() && cartesManquantes) {
                toutesLesCartes.add(carte);
            }
        }
        // determiner la carte 
        int min = 0;
        int max = toutesLesCartes.size() - 1;
        int chance = (int) ((Math.random() * (max - min + 1)) + min);

        CartePerso carte = toutesLesCartes.get(chance);

        min = 0;
        max = 10;
        chance = (int) ((Math.random() * (max - min + 1)) + min);

        int prix = 1;
        if (gratuite) {
            prix = 0;
        }

        if (chance > 8) {
            if (!carte.isDebloque()) {
                min = 1;
                max = 3;
                prix = 3;
            } else {
                min = 1;
                max = 6;
            }
        } else {
            if (!carte.isDebloque()) {
                min = 1;
                max = 2;
                prix = 3;
            } else {
                min = 1;
                max = 3;
            }
        }
        // determiner la quantite et le prix
        int quantite = (int) ((Math.random() * (max - min + 1)) + min);
        prix *= quantite * 150;
        TirageCoffre.Ressource ress = TirageCoffre.Ressource.PIECES;
        min = 1;
        max = 5;
        chance = (int) ((Math.random() * (max - min + 1)) + min);
        if (chance > 3) {
            prix = quantite * 10;
            ress = TirageCoffre.Ressource.RUBIS;
        }

        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, carte, (carte.getPerso().isHeros() ? quantite * 5 : quantite)));
        return new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, prix, ress);
    }

    private void genererToutesLesOffresEnVrac() {
        toutesLesOffresEnVrac = new ArrayList<>();

        // Offre avec ressources uniquement
        ArrayList<Recompense> recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 120));
        OffreMagasin o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 2, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        for (int i = 0; i < 3; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 2, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 300));
        for (int i = 0; i < 3; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 5, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 500));
        for (int i = 0; i < 2; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 8, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 700));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 10, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 900));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 15, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.RUBIS, 5));
        for (int i = 0; i < 3; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 500, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.RUBIS, 15));
        for (int i = 0; i < 3; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 1500, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.RUBIS, 50));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 5000, TirageCoffre.Ressource.PIECES);
        toutesLesOffresEnVrac.add(o);

        // Offres avec coffres
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE));
        for (int i = 0; i < 4; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 15, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE));
        for (int i = 0; i < 5; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 20, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.SACOCHE, 2));
        for (int i = 0; i < 3; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 30, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRET));
        for (int i = 0; i < 2; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 50, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.GRAND_COFFRE));
        for (int i = 0; i < 2; i++) {
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 80, TirageCoffre.Ressource.RUBIS);
            toutesLesOffresEnVrac.add(o);
        }

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRE_ROYAL));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 120, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.COFFRE_ROYAL, 2));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 160, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CHARRETTE_A_BUTIN));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 160, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        // Offres avec cartes
        for (int i = 0; i < 30; i++) {
            recompenses = new ArrayList<>();
            recompenses.add(new Recompense(Recompense.ObjRec.CARTE));
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 100, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }

        Perso p = new ApiculteurRoyal(1);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 5));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 800, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        p = new Archere(1);
        recompenses = new ArrayList<>();
        recompenses.add(new Recompense(Recompense.ObjRec.CARTE, new CartePerso(p, WrkCartes.getRegionAvecPerso(p, wrkNiveaux.getToutesLesRegions())), 5));
        o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 700, TirageCoffre.Ressource.RUBIS);
        toutesLesOffresEnVrac.add(o);

        // offres gratuites
        // avec cartes
        for (int i = 0; i < 5; i++) {
            recompenses = new ArrayList<>();
            recompenses.add(new Recompense(Recompense.ObjRec.CARTE));
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 0, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }
        for (int i = 0; i < 5; i++) {
            recompenses = new ArrayList<>();
            recompenses.add(new Recompense(Recompense.ObjRec.PIECES, 100));
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 0, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }
        for (int i = 0; i < 3; i++) {
            recompenses = new ArrayList<>();
            recompenses.add(new Recompense(Recompense.ObjRec.RUBIS, 15));
            o = new OffreMagasin(OffreMagasin.TypeOffre.EN_VRAC, recompenses, 0, TirageCoffre.Ressource.PIECES);
            toutesLesOffresEnVrac.add(o);
        }

    }

    private ArrayList<OffreMagasin> toutesLesOffres;
    private ArrayList<OffreMagasin> toutesLesOffresEnVrac;

    private HashMap<String, ArrayList<Recompense>> recsTresorerie;

    private NiveauxWrk wrkNiveaux;
}
