package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iosdev on 5/31/16.
 */
public class ResponseLoginPayment {
    @SerializedName("last_update")
    public String last_update;
    @SerializedName("status")
    public String status;
    @SerializedName("sess_key")
    public String ses_key;
    @SerializedName("message")
    public String message;
}
