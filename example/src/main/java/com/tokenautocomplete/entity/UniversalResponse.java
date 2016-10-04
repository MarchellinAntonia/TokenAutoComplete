package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iosdev on 5/2/16.
 */
public class UniversalResponse {
    @SerializedName("kode")
    public String kode;
    @SerializedName("status")
    public String status;
    @SerializedName("messageRegister")
    public String messageRegister;
    @SerializedName("pesan")
    public Pesan pesan;

    public class Pesan{
        @SerializedName("immei")
        public String immei;
        @SerializedName("email")
        public String email;
        @SerializedName("subject")
        public String subject;
        @SerializedName("pesan")
        public String pesan;
    }
}
