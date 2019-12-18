package com.mffa.dev.issbid.Model;

public class TimKlasemen {
    private String id,nmTim,m,k,s,mg,kg,poin,posisi,uploadAt,updateAt;

    public TimKlasemen() {
    }

    public TimKlasemen(String id, String nmTim, String m, String k, String s, String mg, String kg, String poin, String posisi, String uploadAt, String updateAt) {
        this.id = id;
        this.nmTim = nmTim;
        this.m = m;
        this.k = k;
        this.s = s;
        this.mg = mg;
        this.kg = kg;
        this.poin = poin;
        this.posisi = posisi;
        this.uploadAt = uploadAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNmTim() {
        return nmTim;
    }

    public void setNmTim(String nmTim) {
        this.nmTim = nmTim;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(String uploadAt) {
        this.uploadAt = uploadAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
