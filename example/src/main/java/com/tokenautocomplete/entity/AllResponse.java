package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iosdev on 5/22/16.
 */
public class AllResponse {
    @SerializedName("kode")
    public String kode;
    @SerializedName("status")
    public String status;
    @SerializedName("pesan")
    public String pesan;
}
