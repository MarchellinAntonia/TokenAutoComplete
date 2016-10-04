package com.tokenautocomplete.helper;


import com.google.gson.JsonObject;
import com.tokenautocomplete.entity.AllResponse;
import com.tokenautocomplete.entity.LoginResponse;
import com.tokenautocomplete.entity.ResponseLoginPayment;
import com.tokenautocomplete.entity.SalesData;
import com.tokenautocomplete.entity.Toko;
import com.tokenautocomplete.entity.UniversalResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by iosdev on 5/2/16.
 */
public interface ServiceCenterAPI {

    @POST("service/pesan")
    Call<UniversalResponse> sendPesan(@Query("immei") String immei, @Query("email") String email, @Query("subject") String subject, @Query("pesan") String pesan);

//    @GET("getsales")
//    Call<APIData>getSales(@Query("idToko") String id);

    @GET("getsales")
    Call<SalesData>getSales(@Query("idToko") String id);

    @GET("GetFrontliner")
    Call<SalesData>getFrontliner(@Query("idToko") String id);

    @GET("getPromotor")
    Call<SalesData>getPromotor(@Query("idToko") String id);

    @GET("getHarga")
    Call<SalesData>getHarga(@Query("idToko") String id);

    @POST("Service/tokoinfo")
    Call<Toko>getToko(@Query("idtoko") String id);


    @Multipart
    @POST("service/keluhan")
    //Call<ResponseBody> uploadKeluhan(@Part("immei") RequestBody kode, @Part("foto") RequestBody file, @Part("keluhan") RequestBody keluhan, @Part("tanggal") RequestBody tanggal);
    Call<ResponseBody> uploadKeluhan(@Part("immei") RequestBody kode, @Part MultipartBody.Part file, @Part("keluhan") RequestBody keluhan, @Part("tanggal") RequestBody tanggal);

    //@Query("imei1") String immei, @Query("imei2") String immei2, @Query("username") String username,@Query("password")String password
    //@Field("imei1") String immei, @Field("imei2") String immei2, @Field("username") String username,@Field("password")String password
    //@Query("imei1") String immei, @Query("imei2") String immei2, @Field("username") String username,@Field("password")String password


    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    //@FieldMap Map<String, String> parameters
    //@FormUrlEncoded
    //@FormUrlEncoded
    @Multipart
    @POST("Login")
    Call<LoginResponse> login(@Part("imei1") RequestBody immei, @Part("imei2") RequestBody immei2, @Part("username") RequestBody username, @Part("password") RequestBody password);


    @Multipart
    @POST("registrasi")
    Call<AllResponse> register(@Part("imeipendaftar") RequestBody imeipendaftar,
                               @Part("imeicust") RequestBody imecust,
                               @Part("imeicust2") RequestBody imecust2,
                               @Part("alamat") RequestBody alamat,
                               @Part("provinsi") RequestBody provinsi,
                               @Part("kota") RequestBody kota,
                               @Part("kecamatan") RequestBody kecamatan,
                               @Part("kelurahan") RequestBody kelurahan,
                               @Part("notelp") RequestBody notelp,
                               @Part("nohp") RequestBody nohp,
                               @Part("noktp") RequestBody noktp,
                               @Part("hargajual") RequestBody hargajual,
                               @Part("reseller_status") RequestBody reseler_status,
                               @Part("email") RequestBody email,
                               @Part("roleID") RequestBody roleID,
                               @Part("salesID") RequestBody salesID,
                               @Part("fname") RequestBody fname,
                               @Part("lname") RequestBody lname,
                               @Part("regform") RequestBody reform,
                               @Part("refID") RequestBody refID,
                               @Part("reselerID") RequestBody reselerID,
                               @Part("no_rek") RequestBody no_rek,
                               @Part("kode_bank") RequestBody kode_bank,
                               @Part("username") RequestBody username);
//                               @Part("filephoto") RequestBody file);

    @Headers("Content-Type: application/json" )
    @POST("services.rgb")
    Call<ResponseBody> loginEPayment(@Body JsonObject body, @Header("Content-Type") String type, @Header("Accept") String accept, @Header("Accept-Encoding") String encoding);

    @Headers("Content-Type: application/json" )
    @POST("services.rgb")
    Call<ResponseLoginPayment> loginEPayment2(@Body JsonObject body);

    @Headers("Content-Type: application/json" )
    @POST("services.rgb")
    Call<ResponseBody> loginEPayment3(@Body RequestBody body);

    @POST("regService")
    Call<ResponseBody> registerEPayment(@Header("action") String action, @Header("user_akses") String user_akses, @Header("user_passwd") String user_passwd, @Header("mem_name") String mem_name, @Header("nomor_member") String nomor_member, @Header("username") String username, @Header("imei") String imei);

    @Headers("Content-Type: application/json" )
    @POST("services.rgb")
    Call<ResponseBody> allTransaksi(@Body JsonObject body);


}
