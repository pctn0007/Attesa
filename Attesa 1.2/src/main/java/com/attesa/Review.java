package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
public class Review {

    public String cid;
    public String uhc;
    public String uvote;
    public String vcomment;


    public Review(){

    }

    public Review(String cid, String uhc, String uvote, String vcomment) {
        this.cid = cid;
        this.uhc = uhc;
        this.uvote = uvote;
        this.vcomment = vcomment;


    }

    public String getCid() {
        return cid;
    }

    public String getUhc() {
        return uhc;
    }

    public String getUvote() {
        return uvote;
    }

    public String getUvcomment() {
        return vcomment;
    }
}
