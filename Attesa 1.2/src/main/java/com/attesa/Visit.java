package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]
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

    public boolean checkCname(String selected){
        return (selected.equals(cname));
    }
}
