package com.attesa;

public class Clinic {

    public String cid;
    public String cname;
    public String ctype;
    public String cphone;
    public String clive;
    public int cstatus;

    public Clinic(){

    }

    public Clinic(String cid, String cname, String ctype, String cphone, String clive, int cstatus) {
        this.cid = cid;
        this.cname = cname;
        this.ctype = ctype;
        this.cphone = cphone;
        this.clive = clive;
        this.cstatus = cstatus;

    }


    public String getCid() {
        return cid;
    }

    public String getCname() {
        return cname;
    }

    public String getCtype() {
        return ctype;
    }

    public String getCphone() {
        return cphone;
    }

    public String getClive() {
        return clive;
    }

    public int getCstatus() {
        return cstatus;
    }


}
