package com.mffa.dev.issbid.Model;

public class Babak {
    private String id,nmTim,lawanTim,uploadAt,updateAt;

    public Babak() {
    }

    public Babak(String id, String nmTim, String lawanTim, String uploadAt, String updateAt) {
        this.id = id;
        this.nmTim = nmTim;
        this.lawanTim = lawanTim;
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

    public String getLawanTim() {
        return lawanTim;
    }

    public void setLawanTim(String lawanTim) {
        this.lawanTim = lawanTim;
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
