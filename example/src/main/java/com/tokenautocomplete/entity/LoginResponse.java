package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iosdev on 5/3/16.
 */
public class LoginResponse {

    @SerializedName("kode")
    public String kode;
    @SerializedName("status")
    public String status;
    @SerializedName("pesan")
    public String pesan;
    @SerializedName("roleID")
    public String roleID;
    @SerializedName("ID")
    public String ID;
    @SerializedName("imei1")
    public String imei1;
    @SerializedName("imei2")
    public String immei2;
    @SerializedName("token")
    public String token;
    @SerializedName("refID")
    public String refID;
    @SerializedName("password")
    public String password;
    @SerializedName("uname")
    public String uname;
    @SerializedName("phone")
    public String phone;

    public LoginResponse() {
    }

    public LoginResponse(String kode, String status, String pesan, String roleID, String ID, String imei1, String imei2, String token, String refID, String uname, String password, String phone) {
        this.kode = kode;
        this.status = status;
        this.pesan = pesan;
        this.roleID = roleID;
        this.ID = ID;
        this.imei1 = imei1;
        this.immei2 = imei2;
        this.token = token;
        this.refID = refID;
        this.uname = uname;
        this.password = password;
        this.phone = phone;
    }
}
