package com.mffa.dev.issbid.Model;

public class Klasemen {
    private String id,sistemKomp,nmKomp,uploadAt,updateAt;

    public Klasemen() {
    }

    public Klasemen(String id, String sistemKomp, String nmKomp, String uploadAt, String updateAt) {
        this.id = id;
        this.sistemKomp = sistemKomp;
        this.nmKomp = nmKomp;
        this.uploadAt = uploadAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSistemKomp() {
        return sistemKomp;
    }

    public void setSistemKomp(String sistemKomp) {
        this.sistemKomp = sistemKomp;
    }

    public String getNmKomp() {
        return nmKomp;
    }

    public void setNmKomp(String nmKomp) {
        this.nmKomp = nmKomp;
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
