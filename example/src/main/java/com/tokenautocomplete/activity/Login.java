package com.tokenautocomplete.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokenautocomplete.R;
import com.tokenautocomplete.TesRegister;
import com.tokenautocomplete.entity.LoginResponse;
import com.tokenautocomplete.helper.APIManager;
import com.tokenautocomplete.helper.APIManagerInterface;
import com.tokenautocomplete.helper.AlertDialogHelper;
import com.tokenautocomplete.helper.ServiceCenterAPI;
import com.tokenautocomplete.util.MasterPreference;
import com.tokenautocomplete.util.TelephonyInfo;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends Activity {
    protected LoginResponse logindRespone;
    protected MasterPreference masterPreference;
    protected APIManager amp;
    private Gson gson;
    protected ProgressDialog dialog;
    private AlertDialogHelper alertDialog;
    private String fcmId = "";
    private String statusToken = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kata_login);
        //TextView register = (TextView) findViewById(R.id.register);
//        getSupportActionBar().hide();

        masterPreference = new MasterPreference(Login.this);
        if(masterPreference.isNotifExist()){
            masterPreference.notifExist(false);
            Intent intent = new Intent(this, TesRegister.class);
            intent.putExtra("msg", masterPreference.getNotifMsg());
            startActivity(intent);
            masterPreference.notifExist(false);
            finish();
        }else if(masterPreference.isLogin()){
            startActivity(new Intent(Login.this, Register.class));
            finish();
        }

        dialog = new ProgressDialog(Login.this, ProgressDialog.STYLE_SPINNER);
        alertDialog = new AlertDialogHelper(Login.this);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        final TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        final String immei = telephonyInfo.getImsiSIM1();

        //generate firebase token
//        fcmId = getFirebaseToken();

        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        final Retrofit retrofit = new Retrofit.Builder().
                baseUrl(APIManager.urlKata)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ServiceCenterAPI interfaceAPI = retrofit.create(ServiceCenterAPI.class);
        final MasterPreference masterPref = new MasterPreference(Login.this);

        final ProgressBar progressBar = new ProgressBar(Login.this);

//button login
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("statustoken di buttonLogin: "+statusToken);
//                if(statusToken == "0"){
//                    fcmId = getFirebaseToken();
//                    getStatusToken(fcmId);
//                }

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("fcmid",fcmId);
                params.put("imei1", immei);
                params.put("imei2", immei);

                amp.post("Login", params, new APIManagerInterface() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject responseJSON) {
                        try {
                            //LoginResponse loginResponse = gson.fromJson(responseJSON.toString(), LoginResponse.class);

                            //Toast.makeText(Login.this, ((LoginResponse) gson.fromJson(responseJSON.toString(), LoginResponse.class)).toString(), Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(responseJSON.toString());

                            //Log.d("Response Server", responseJSON.toString());
                            dialog.dismiss();
                            Toast.makeText(Login.this, jsonObject.getString("pesan"), Toast.LENGTH_LONG).show();
                            System.out.println("masuk if onSuccess Login");
                            //Toast.makeText(Login.this, jsonObject.getString("pesan"), Toast.LENGTH_LONG).show();
                            //Log.d("Kode", jsonObject.getString("kode"));
                            //Log.d("Status", jsonObject.getString("status"));
                            //Log.d("Pesan", jsonObject.getString("pesan"));


                            //Log.d("Role ID", jsonObject.getString("roleID"));
                            //Log.d("ID",jsonObject.getString("ID"));
                            //Log.d("Imei",jsonObject.getString("imei"));
                            if(jsonObject.getString("kode").equalsIgnoreCase("00")){
                                System.out.println("masuk if sukses login");
                                masterPref.savePreferencesLogin(new LoginResponse(jsonObject.getString("kode"), jsonObject.getString("status"), jsonObject.getString("pesan"), jsonObject.getString("roleID"), jsonObject.getString("ID"), jsonObject.getString("imei1"), jsonObject.getString("imei2"), jsonObject.getString("token"), jsonObject.getString("refID"), username.getText().toString(), password.getText().toString(), jsonObject.getString("phone")));
                                startActivity(new Intent(Login.this, Register.class));
                                finish();
                            }


                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Maaf anda gagal login silahkan coba lagi", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, JSONObject responseJSON, Throwable error) {
                        dialog.dismiss();
//                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Terjadi Error : "+statusCode+" ", error.getMessage());
                        alertDialog.show("Koneksi Jaringan Bermasalah", Login.class);
                    }

                    @Override
                    public void onFailureString(int satatusCode, String responseString, Throwable error) {
                        dialog.dismiss();
//                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Terjadi Error : "+satatusCode+"", error.getMessage());
                        alertDialog.show("Koneksi Jaringan Bermasalah", Login.class);
                    }

                    @Override
                    public void onStart() {
                        dialog.setMessage("Tunggu Sebentar..");
                        dialog.show();
                    }
                });

            }
        });

    }

//    private String getFirebaseToken(){
//        System.out.println("masuk method getFirebaseToken");
//        String firebasetoken;
//        firebasetoken = FirebaseInstanceId.getInstance().getToken();
//        System.out.println("firebase token di methodnya: "+firebasetoken);
//        return firebasetoken;
//    }

//    private void getStatusToken(String token){
//
//        System.out.println("masuk method getStatusToken");
//            HashMap<String, String> params = new HashMap<String, String>();
//            params.put("token", token);
////            params.put("username", "testingkata1");
//            amp.post("service/checkingfirebase", params, new APIManagerInterface() {
//                @Override
//                public void onSuccess(int statusCode, JSONObject responseJSON) {
//                    try {
//                        //Toast.makeText(Login.this, ((LoginResponse) gson.fromJson(responseJSON.toString(), LoginResponse.class)).toString(), Toast.LENGTH_LONG).show();
//                        JSONObject jsonObject = new JSONObject(responseJSON.toString());
//
//                        dialog.dismiss();
//                        statusToken = jsonObject.getString("success");
//                        Toast.makeText(Login.this, statusToken, Toast.LENGTH_LONG).show();
//                        System.out.println("masuk try");
//                        System.out.println("status token di getStatusToken: "+statusToken);
//
//                    } catch (Exception e) {
//                        System.out.println("masuk catch");
////                            Toast.makeText(Login.this, "Maaf anda gagal login silahkan coba lagi", Toast.LENGTH_LONG).show();
//                        Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, JSONObject responseJSON, Throwable error) {
//                    System.out.println("masuk onFailure");
//                    dialog.dismiss();
////                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.d("Terjadi Error : "+statusCode+" ", error.getMessage());
////                        alertDialog.show("Koneksi Jaringan Bermasalah", Login.class);
//                    alertDialog.show(error.getMessage(), Login.class);
//                }
//
//                @Override
//                public void onFailureString(int satatusCode, String responseString, Throwable error) {
//                    System.out.println("masuk onFailureString");
//                    dialog.dismiss();
////                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.d("Terjadi Error : "+satatusCode+"", error.getMessage());
////                        alertDialog.show("Koneksi Jaringan Bermasalah", Login.class);
//                    alertDialog.show(error.getMessage(), Login.class);
//                }
//
//                @Override
//                public void onStart() {
//                    System.out.println("masuk onStart");
//                    dialog.setMessage("Tunggu Sebentar..");
//                    dialog.show();
//                }
//            });
//    }


}
