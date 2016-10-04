package com.tokenautocomplete.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by iosdev on 5/4/16.
 */
public class SalesData {
    @SerializedName("kode")
    public String kode;
    @SerializedName("status")
    public String status;
    @SerializedName("dataSales")
    public List<DataSales> dataSales;
//    @SerializedName("dataSales")
//    public List<DataHarga> dataHarga;

    public class DataSales{
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;

        @SerializedName("type")
        public String type;
        @SerializedName("harga")
        public String harga;
    }

//    public class DataHarga{
//        @SerializedName("id")
//        public String id;
//        @SerializedName("type")
//        public String type;
//        @SerializedName("harga")
//        public String harga;
//    }
//
//    public List<DataSales> getDataSales() {
//        return dataSales;
//    }
    public List<DataSales> getDataSales() {
        return dataSales;
    }

    public void setDataSales(List<DataSales> dataSales) {
        this.dataSales = dataSales;
    }

//    public void setDataHarga(List<DataHarga> dataHarga) {
//        this.dataHarga = dataHarga;
//    }
}
