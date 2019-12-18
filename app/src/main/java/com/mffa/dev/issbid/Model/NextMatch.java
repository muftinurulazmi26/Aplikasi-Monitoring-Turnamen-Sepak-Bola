package com.mffa.dev.issbid.Model;

public class NextMatch {
    private String id,pertandingan,namaTim,lawanTim,tanggal,jam,remindTime,menitBermain,skorTim,skorLawan,kickOff,urlvid;

    public NextMatch() {
    }

    public NextMatch(String id, String pertandingan, String namaTim, String lawanTim, String tanggal, String jam, String remindTime, String menitBermain, String skorTim, String skorLawan, String kickOff, String urlvid) {
        this.id = id;
        this.pertandingan = pertandingan;
        this.namaTim = namaTim;
        this.lawanTim = lawanTim;
        this.tanggal = tanggal;
        this.jam = jam;
        this.remindTime = remindTime;
        this.menitBermain = menitBermain;
        this.skorTim = skorTim;
        this.skorLawan = skorLawan;
        this.kickOff = kickOff;
        this.urlvid = urlvid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPertandingan() {
        return pertandingan;
    }

    public void setPertandingan(String pertandingan) {
        this.pertandingan = pertandingan;
    }

    public String getNamaTim() {
        return namaTim;
    }

    public void setNamaTim(String namaTim) {
        this.namaTim = namaTim;
    }

    public String getLawanTim() {
        return lawanTim;
    }

    public void setLawanTim(String lawanTim) {
        this.lawanTim = lawanTim;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getMenitBermain() {
        return menitBermain;
    }

    public void setMenitBermain(String menitBermain) {
        this.menitBermain = menitBermain;
    }

    public String getSkorTim() {
        return skorTim;
    }

    public void setSkorTim(String skorTim) {
        this.skorTim = skorTim;
    }

    public String getSkorLawan() {
        return skorLawan;
    }

    public void setSkorLawan(String skorLawan) {
        this.skorLawan = skorLawan;
    }

    public String getKickOff() {
        return kickOff;
    }

    public void setKickOff(String kickOff) {
        this.kickOff = kickOff;
    }

    public String getUrlvid() {
        return urlvid;
    }

    public void setUrlvid(String urlvid) {
        this.urlvid = urlvid;
    }
}
