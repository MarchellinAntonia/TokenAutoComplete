package com.tokenautocomplete.helper;

import org.json.JSONObject;

/**
 * Created by iosdev on 5/6/16.
 */
public interface APIManagerInterface {
    public abstract void onSuccess(int statusCode, JSONObject responseJSON);
    public abstract void onFailure(int statusCode, JSONObject responseJSON, Throwable error);
    public abstract void onFailureString(int satatusCode, String responseString, Throwable error);
    public abstract void onStart();
}
