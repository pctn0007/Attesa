package com.attesa;

public class Review {

    public String cid;
    public String uemail;
    public String uvote;
    public String vcomment;


    public Review(){

    }

    public Review(String cid, String uemail, String uvote, String vcomment) {
        this.cid = cid;
        this.uemail = uemail;
        this.uvote = uvote;
        this.vcomment = vcomment;


    }

    public String getCid() {
        return cid;
    }

    public String getUemail() {
        return uemail;
    }

    public String getUvote() {
        return uvote;
    }

    public String getVcomment() {
        return vcomment;
    }
}
