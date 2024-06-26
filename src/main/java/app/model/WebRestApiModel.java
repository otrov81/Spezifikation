package app.model;

import lombok.Data;

@Data
public class WebRestApiModel {
    private int id;
    private String plz;
    private String ort;
    private String gesellschaft; // Add this field
    private String gf1;
    private String gf2;
    private String gf3;
    private String gewerbe;
    private String uidAut;
    private String uid;
    private String firmenbuchNummer;
    private String steuerNummer;
    private String kur;
    private String gln;
    private String dgn;
    private String eori;
    private String rex;
    private String onace;
    private String onaceBeschreibung;
    private String dvr;
    private String ara;
    private String bank;
    private String iban;
    private String bic_swift;
    private String betriebs;
    private String versicherungssumme;
    private String giftbezugs;
    private String strasse;
}
