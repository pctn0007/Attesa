package com.attesa;

public class Visit {

    public String cname;
    public String uhc;
    public String uvote;


    public Visit(){

    }

    public Visit(String cname, String uhc, String uvote) {
        this.cname = cname;
        this.uhc = uhc;
        this.uvote = uvote;


    }

    public String getCname() {
        return cname;
    }

    public String getUhc() {
        return uhc;
    }

    public String getUvote() {
        return uvote;
    }

}
