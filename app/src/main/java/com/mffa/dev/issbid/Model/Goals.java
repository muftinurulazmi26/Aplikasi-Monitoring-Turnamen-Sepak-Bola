package com.mffa.dev.issbid.Model;

public class Goals {
    private String icon,namaPlayer,menitgoals;

    public Goals() {
    }

    public Goals(String icon, String namaPlayer, String menitgoals) {
        this.icon = icon;
        this.namaPlayer = namaPlayer;
        this.menitgoals = menitgoals;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNamaPlayer() {
        return namaPlayer;
    }

    public void setNamaPlayer(String namaPlayer) {
        this.namaPlayer = namaPlayer;
    }

    public String getMenitgoals() {
        return menitgoals;
    }

    public void setMenitgoals(String menitgoals) {
        this.menitgoals = menitgoals;
    }
}
