package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chellin on 29/09/16.
 */

public class Toko {

    String idToko;
    @SerializedName("namatoko")
    String namatoko;
    @SerializedName("email")
    String email;
    @SerializedName("telp")
    String telp;
    @SerializedName("Alamat")
    String alamat;
    @SerializedName("Bonus")
    String bonus;

    public String getIdToko() {
        return idToko;
    }

    public void setIdToko(String idToko) {
        this.idToko = idToko;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
