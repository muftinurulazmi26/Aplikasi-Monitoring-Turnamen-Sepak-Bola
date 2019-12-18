package com.mffa.dev.issbid.Model;

public class Player {
    private String id,image,terdaftar,update,nama,tmpLahir,tglLahir,alamat,posisi,noPunggung,tim;

    public Player() {
    }

    public Player(String id, String image, String terdaftar, String update, String nama, String tmpLahir, String tglLahir, String alamat, String posisi, String noPunggung, String tim) {
        this.id = id;
        this.image = image;
        this.terdaftar = terdaftar;
        this.update = update;
        this.nama = nama;
        this.tmpLahir = tmpLahir;
        this.tglLahir = tglLahir;
        this.alamat = alamat;
        this.posisi = posisi;
        this.noPunggung = noPunggung;
        this.tim = tim;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTmpLahir() {
        return tmpLahir;
    }

    public void setTmpLahir(String tmpLahir) {
        this.tmpLahir = tmpLahir;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getNoPunggung() {
        return noPunggung;
    }

    public void setNoPunggung(String noPunggung) {
        this.noPunggung = noPunggung;
    }

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }
}
