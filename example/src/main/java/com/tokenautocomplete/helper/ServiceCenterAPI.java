package com.tokenautocomplete.helper;


import com.tokenautocomplete.entity.SalesData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iosdev on 5/2/16.
 */
public interface ServiceCenterAPI {

    @GET("getsales")
    Call<SalesData>getSales(@Query("idToko") String id);

    @GET("GetFrontliner")
    Call<SalesData>getFrontliner(@Query("idToko") String id);

    @GET("getPromotor")
    Call<SalesData>getPromotor(@Query("idToko") String id);

    @GET("getHarga")
    Call<SalesData>getHarga(@Query("idToko") String id);

}
