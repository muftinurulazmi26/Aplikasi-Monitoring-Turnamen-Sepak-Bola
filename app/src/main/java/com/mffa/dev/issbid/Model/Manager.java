package com.mffa.dev.issbid.Model;

public class Manager {
    private String idManager,namaManager,tinggalManager,alamatManager,timManager,profil,terdaftar,update;

    public Manager() {
    }

    public Manager(String idManager, String namaManager, String tinggalManager, String alamatManager, String timManager, String profil, String terdaftar, String update) {
        this.idManager = idManager;
        this.namaManager = namaManager;
        this.tinggalManager = tinggalManager;
        this.alamatManager = alamatManager;
        this.timManager = timManager;
        this.profil = profil;
        this.terdaftar = terdaftar;
        this.update = update;
    }

    public String getIdManager() {
        return idManager;
    }

    public void setIdManager(String idManager) {
        this.idManager = idManager;
    }

    public String getNamaManager() {
        return namaManager;
    }

    public void setNamaManager(String namaManager) {
        this.namaManager = namaManager;
    }

    public String getTinggalManager() {
        return tinggalManager;
    }

    public void setTinggalManager(String tinggalManager) {
        this.tinggalManager = tinggalManager;
    }

    public String getAlamatManager() {
        return alamatManager;
    }

    public void setAlamatManager(String alamatManager) {
        this.alamatManager = alamatManager;
    }

    public String getTimManager() {
        return timManager;
    }

    public void setTimManager(String timManager) {
        this.timManager = timManager;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getTerdaftar() {
        return terdaftar;
    }

    public void setTerdaftar(String terdaftar) {
        this.terdaftar = terdaftar;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
