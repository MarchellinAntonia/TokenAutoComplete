package com.tokenautocomplete.helper;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tokenautocomplete.R;
import com.tokenautocomplete.entity.ResponseLoginPayment;
import com.tokenautocomplete.util.MasterPreference;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iosdev on 5/28/16.
 */
public class DialogHelper {



    public void ShowDialogHelper(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_login_epayment);
        //dialog.setTitle("Login Kata E-Payment");

        EditText username = (EditText) dialog.findViewById(R.id.username_epayment);
        EditText password = (EditText)dialog.findViewById(R.id.password_epayment);

        TextView btnlogin = (TextView)dialog.findViewById(R.id.login_btn_epayment);
        TextView btnCancel = (TextView)dialog.findViewById(R.id.cancel_btn_epayment);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    OkHttpClient okHttpClient = new OkHttpClient();
                    /*
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    InputStream instream = context.getResources().openRawResource(R.raw.katars);
                    Certificate ca;
                    try {
                        ca = cf.generateCertificate(instream);
                        System.out.println("ca Value =" + ((X509Certificate) ca).getSubjectDN());
                    }finally{
                        instream.close();
                    }

                    KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    kStore.load(null, null);
                    kStore.setCertificateEntry("ca", ca);

                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(kStore);
                    */


                    final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] cArrr = new X509Certificate[0];
                            return cArrr;
                        }

                        @Override
                        public void checkServerTrusted(final X509Certificate[] chain,
                                                       final String authType) throws CertificateException {
                        }

                        @Override
                        public void checkClientTrusted(final X509Certificate[] chain,
                                                       final String authType) throws CertificateException {
                        }
                    }};


                    final SSLContext contextSSL = SSLContext.getInstance("SSL");
                    //contextSSL.init(null, tmf.getTrustManagers(), null);
                    contextSSL.init(null, trustAllCerts,new SecureRandom());

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.sslSocketFactory(contextSSL.getSocketFactory());
                    builder.hostnameVerifier(new HostnameVerifier() {

                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

                    Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                            .baseUrl(APIManager.urlLoginEPayment)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final ServiceCenterAPI interfaceAPI = retrofit.create(ServiceCenterAPI.class);

                    JsonObject req = new JsonObject();
                    JsonObject require = new JsonObject();
                    require.addProperty("hp_utama", "082115506569");
                    require.addProperty("device_id", "112233445511111");
                    require.addProperty("service", 1);
                    require.addProperty("uname", "tantansupriadi");
                    require.addProperty("upass", "523563");
                    req.add("REQ", require);

                    //Log.d("String processing ", req.toString());
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(req.toString())).toString());
                    Call<ResponseLoginPayment> callBack = interfaceAPI.loginEPayment2(req);
                            //loginEPayment(req, "application/json", "application/json", "gzip");

                    callBack.enqueue(new retrofit2.Callback<ResponseLoginPayment>() {
                        @Override
                        public void onResponse(Call<ResponseLoginPayment> call, Response<ResponseLoginPayment> response) {

                            if (response.isSuccessful()) {
                                MasterPreference master = new MasterPreference(context);
                                master.savePreferenceLoginPayment(response.body());
                                Toast.makeText(context, response.code() + "Berhasil Login ", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    Toast.makeText(context, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLoginPayment> call, Throwable t) {

                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
                }catch(Exception e){
                    Log.d("Error Sertificate", e.getMessage());
                }


                /*
                JsonObject req = new JsonObject();
                JsonObject require = new JsonObject();
                require.addProperty("hp_utama", "0821115506569");
                require.addProperty("device_id", "112233445511111");
                require.addProperty("service", 1);
                require.addProperty("uname", "tantansupriadi");
                require.addProperty("upass", "213143");
                req.add("REQ", require);

                APIManager apiManager = new APIManager(context);
                try {
                    apiManager.invokeWS(req);
                }catch(Exception e){
                   Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                */
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
