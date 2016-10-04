package com.tokenautocomplete.helper;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by iosdev on 5/6/16.
 */
public class APIManager {

//    public static String urlKata ="http://softwareqta.com/service-center/api/";
    public static String urlKata ="http://center.kataindonesia.co.id/api/";
    public static String urlLoginEPayment = "https://spektrumdevel.ddns.net:14554/";
    public static AsyncHttpClient client = new AsyncHttpClient();
    public Context context;

    public APIManager(Context context){
        this.context = context;
    }


    public static void get(String controller, Map params, final APIManagerInterface responseHandler) {
        responseHandler.onStart();
        String param = "/";
        int i = 0;
        Object[] tempParam = params.values().toArray();
        for ( Object key : params.keySet()) {
            param = param + key.toString() + "/"+ tempParam[i].toString() + "/";
            i++;
        }
        String a = urlKata +controller+param;
        client.setMaxRetriesAndTimeout(5, 10);
        client.get(urlKata + controller + param, new JsonHttpResponseHandler() {
            //client.get("http://pdam.voxteneo.co.id/api/pengaduan/id_users/1/status/CS/APIKEY/48c652359808ef54567c431a1781dfae/offset/0/limit/0/wilayah/b/", new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                responseHandler.onSuccess(statusCode, responseBody);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                responseHandler.onFailure(statusCode, errorResponse, error);
            }
        });
    }

    public static void post(final String controller, Map params, final APIManagerInterface responseHandler) {
        responseHandler.onStart();
        final RequestParams param = new RequestParams();
        int i = 0;
        Object[] tempParam = params.values().toArray();
        for (Object key : params.keySet()) {
            if (tempParam[i] instanceof File){
                try {
                    param.put(key.toString(), (File) tempParam[i]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            param.put(key.toString(), tempParam[i]);
            i++;
        }

        client.setMaxRetriesAndTimeout(5, 10);
        client.post(urlKata + controller, param, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                responseHandler.onSuccess(statusCode, responseBody);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                responseHandler.onFailure(statusCode, errorResponse, error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailureString(statusCode, responseString, throwable);
            }
        });
    }




}
