package com.tokenautocomplete;

import java.io.Serializable;

/**
 * Simple container object for contact data
 *
 * Created by mgod on 9/12/13.
 * @author mgod
 */
public class DataAPI implements Serializable{
    public String id;
    public String nama;
    public String type;
    public String harga;

    public DataAPI(String id, String nama, String type, String harga) {
        this.id = id;
        this.nama = nama;
        this.type = type;
        this.harga = harga;
    }

    public DataAPI(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public DataAPI(String id, String type, String harga) {
        this.id = id;
        this.type = type;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    @Override
    public String toString() { return nama; }
}
