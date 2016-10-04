package com.tokenautocomplete.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokenautocomplete.entity.LoginResponse;
import com.tokenautocomplete.entity.ResponseLoginPayment;

/**
 * Created by iosdev on 5/3/16.
 */
public class MasterPreference {
    protected SharedPreferences sharedPreferences;
    protected static final String MY_PREFERENCES = "MyPrefs";
    protected SharedPreferences.Editor editor;
    protected Context context;

    public MasterPreference(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void savePreferencesLogin(LoginResponse loginResponse){
        editor.putString("roleID", loginResponse.roleID);
        editor.putString("ID", loginResponse.ID);
        editor.putString("imei1", loginResponse.imei1);
        editor.putString("imei2", loginResponse.immei2);
        editor.putString("token", loginResponse.token);
        editor.putString("uname", loginResponse.uname);
        editor.putString("password", loginResponse.password);
        editor.putString("phone", loginResponse.phone);
        editor.putBoolean("isLogin", true);
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public String getStringRoleID(){
        return sharedPreferences.getString("roleID", "");
    }

    public String getStringID(){
        return sharedPreferences.getString("ID", "");
    }

    public String getStringImei(){
        return sharedPreferences.getString("imei1","");
    }

    public String getStringUname() {
        return sharedPreferences.getString("uname", "");
    }

    public String getStringPassword(){
        return sharedPreferences.getString("password", "");
    }

    public String getStringPhone(){
        return sharedPreferences.getString("phone", "");
    }

    public void removePreferences(){
        editor.clear();
        editor.commit();
    }

    public void savePreferenceLoginPayment(ResponseLoginPayment responseLoginPayment){
        editor.putString("paymentSession", responseLoginPayment.last_update);
        editor.putString("paymentSessionKey",responseLoginPayment.ses_key);
    }

    public void setUsername(String uname){
        editor.putString("uname", uname);
        editor.commit();
    }

    public void setNotifMsg(String msg){
        editor.putString("notifMsg", msg);
        editor.commit();
    }

    public String getNotifMsg() {
        return sharedPreferences.getString("notifMsg", "");
    }

    public void notifExist(Boolean exist){
        editor.putBoolean("notifExist", exist);
        editor.commit();
    }

    public boolean isNotifExist() {
        return sharedPreferences.getBoolean("notifExist", false);
    }
}
