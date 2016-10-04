package com.tokenautocomplete.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokenautocomplete.DataAPI;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.R;
import com.tokenautocomplete.TokenCompleteTextView;
import com.tokenautocomplete.entity.SalesData;
import com.tokenautocomplete.helper.APIManager;
import com.tokenautocomplete.helper.ServiceCenterAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends Activity implements TokenCompleteTextView.TokenListener<DataAPI> {
    //the text view of TokenAutoComplete
    TokenCompleteTextView namaSales, namaFrontliner, namaPromotor, hargaJual;

    //list data of TokenAutoComplete
    ArrayList<DataAPI> listSales = new ArrayList<>();
    ArrayList<DataAPI> listFrontliner = new ArrayList<>();
    ArrayList<DataAPI> listPromotor = new ArrayList<>();
    ArrayList<DataAPI> listHarga = new ArrayList<>();

    //array adapter of TokenAutocomplete
    ArrayAdapter<DataAPI> adapterSales;
    ArrayAdapter<DataAPI> adapterPromotor;
    ArrayAdapter<DataAPI> adapterFrontliner;
    ArrayAdapter<DataAPI> adapterHarga;

    //variable to parse while register button clicked
    String parseSales, parseFrontliner, parsePromotor, parseHarga;

    //temporary variable to get API data
    protected List<SalesData.DataSales> saleslist;

    //UI variables
    private RadioButton radiovalue;
    private RadioGroup rg;
    private ProgressDialog dialog;

    private String message_error;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize content if not using drawer
        setContentView(R.layout.activity_main);

        String idToko = "20160703211408000000000000000";


        //get data from API to autocomplete text view
        getSales(idToko);


    }


    protected void getSales(final String idSales){
        System.out.println("id toko di getSales: "+idSales);
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(APIManager.urlKata)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ServiceCenterAPI interfaceAPI = retrofit.create(ServiceCenterAPI.class);
        final Call<SalesData> sales = interfaceAPI.getSales(idSales);
        sales.enqueue(new Callback<SalesData>() {
            @Override
            public void onResponse(Call<SalesData> call, Response<SalesData> response) {
                if (response.isSuccessful()) {
                    saleslist = response.body().dataSales;
                    if (saleslist != null) {

                        for (SalesData.DataSales data : saleslist) {
                            listSales.add(new DataAPI(data.id,data.name));
                        }
                    }
                } else {
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                Log.d("Error Register", t.getMessage());
            }
        });

        final Call<SalesData> frontliner = interfaceAPI.getFrontliner(idSales);
        frontliner.enqueue(new Callback<SalesData>() {
            @Override
            public void onResponse(Call<SalesData> call, Response<SalesData> response) {
                if (response.isSuccessful()) {
                    saleslist = response.body().dataSales;
                    if (saleslist != null) {
//                        people = new ArrayList<DataAPI>();

                        for (SalesData.DataSales data : saleslist) {
                            listFrontliner.add(new DataAPI(data.id,data.name));
                        }
                    }
                } else {
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                Log.d("Error Register", t.getMessage());
            }
        });

        final Call<SalesData> promotor = interfaceAPI.getPromotor(idSales);
        promotor.enqueue(new Callback<SalesData>() {
            @Override
            public void onResponse(Call<SalesData> call, Response<SalesData> response) {
                if (response.isSuccessful()) {
                    saleslist = response.body().dataSales;
                    if (saleslist != null) {
                        for (SalesData.DataSales data : saleslist) {
                            listPromotor.add(new DataAPI(data.id,data.name));
                        }
                    }
                } else {
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                Log.d("Error Register", t.getMessage());
            }
        });

        final Call<SalesData> harga = interfaceAPI.getHarga(idSales);
        harga.enqueue(new Callback<SalesData>() {
            @Override
            public void onResponse(Call<SalesData> call, Response<SalesData> response) {
                if (response.isSuccessful()) {
                    saleslist = response.body().dataSales;
                    if (saleslist != null) {
                        for (SalesData.DataSales data : saleslist) {
//                            listHarga.add(new DataAPI(data.id,data.type,data.harga));
                            listHarga.add(new DataAPI(data.id,data.type));
                        }
                    }
                } else {
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                Log.d("Error Register", t.getMessage());
            }
        });

        //create adapter sales
        adapterSales = new FilteredArrayAdapter<DataAPI>(this, R.layout.person_layout, listSales) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                DataAPI p = getItem(position);
                ((TextView)convertView.findViewById(R.id.idData)).setText(p.getId());
                ((TextView)convertView.findViewById(R.id.nameData)).setText(p.getNama());

                return convertView;
            }

            @Override
            protected boolean keepObject(DataAPI person, String mask) {
                mask = mask.toLowerCase();
                return person.getId().toLowerCase().startsWith(mask) || person.getNama().toLowerCase().startsWith(mask);
            }
        };

        //create adapter frontliner
        adapterFrontliner = new FilteredArrayAdapter<DataAPI>(this, R.layout.person_layout, listFrontliner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                DataAPI p = getItem(position);
                ((TextView)convertView.findViewById(R.id.idData)).setText(p.getId());
                ((TextView)convertView.findViewById(R.id.nameData)).setText(p.getNama());

                return convertView;
            }

            @Override
            protected boolean keepObject(DataAPI person, String mask) {
                mask = mask.toLowerCase();
                return person.getId().toLowerCase().startsWith(mask) || person.getNama().toLowerCase().startsWith(mask);
            }
        };

        //create adapter promotor
        adapterPromotor = new FilteredArrayAdapter<DataAPI>(this, R.layout.person_layout, listPromotor) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                DataAPI p = getItem(position);
                ((TextView)convertView.findViewById(R.id.idData)).setText(p.getId());
                ((TextView)convertView.findViewById(R.id.nameData)).setText(p.getNama());

                return convertView;
            }

            @Override
            protected boolean keepObject(DataAPI person, String mask) {
                mask = mask.toLowerCase();
                return person.getId().toLowerCase().startsWith(mask) || person.getNama().toLowerCase().startsWith(mask);
            }
        };

        //create adapter harga
        adapterHarga = new FilteredArrayAdapter<DataAPI>(this, R.layout.person_layout, listHarga) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                DataAPI p = getItem(position);
                ((TextView)convertView.findViewById(R.id.idData)).setText(p.getId());
                ((TextView)convertView.findViewById(R.id.nameData)).setText(p.getNama());
                System.out.println("id harga di adapter: "+p.getId());
                System.out.println("tipe harga di adapter: "+p.getNama());

                return convertView;
            }

            @Override
            protected boolean keepObject(DataAPI person, String mask) {
                mask = mask.toLowerCase();
                return person.getId().toLowerCase().startsWith(mask) || person.getNama().toLowerCase().startsWith(mask);
            }
        };


        namaSales = (TokenCompleteTextView) findViewById(R.id.namaSales);
        namaSales.setAdapter(adapterSales);
        namaSales.setTokenListener(this);
        namaSales.setThreshold(1);
        namaSales.setTokenLimit(1);

        namaFrontliner = (TokenCompleteTextView) findViewById(R.id.namaFrontliner);
        namaFrontliner.setAdapter(adapterFrontliner);
        namaFrontliner.setTokenListener(this);
        namaFrontliner.setThreshold(1);
        namaFrontliner.setTokenLimit(1);

        namaPromotor = (TokenCompleteTextView) findViewById(R.id.namaPromotor);
        namaPromotor.setAdapter(adapterPromotor);
        namaPromotor.setTokenListener(this);
        namaPromotor.setThreshold(1);
        namaPromotor.setTokenLimit(1);

        hargaJual = (TokenCompleteTextView) findViewById(R.id.harga_jual);
        hargaJual.setAdapter(adapterHarga);
        hargaJual.setTokenListener(this);
        hargaJual.setThreshold(1);
        hargaJual.setTokenLimit(1);

    }

    //method for get ID for each TokenAutoComplete
    private void updateTokenConfirmation() {
        StringBuilder sb = new StringBuilder("Current tokens:\n");

        for (Object tokenSales : namaSales.getObjects()) {
            sb.append("idSales: "+((DataAPI) tokenSales).getId());
            parseSales = ((DataAPI) tokenSales).getId();
        }

        for (Object tokenFrontliner : namaFrontliner.getObjects()) {
            sb.append("idFrontliner: "+((DataAPI) tokenFrontliner).getId());
            parseFrontliner = ((DataAPI) tokenFrontliner).getId();
        }

        for (Object tokenPromotor : namaPromotor.getObjects()) {
            sb.append("idPromotor: "+((DataAPI) tokenPromotor).getId());
            parsePromotor = ((DataAPI) tokenPromotor).getId();
        }
        for (Object tokenHarga : hargaJual.getObjects()) {
            sb.append("idHarga: "+((DataAPI) tokenHarga).getId());
            parseHarga = ((DataAPI) tokenHarga).getId();
        }
        ((TextView)findViewById(R.id.tokens)).setText(sb);
    }


    @Override
    public void onTokenAdded(DataAPI token) {
//        ((TextView)findViewById(R.id.lastEvent)).setText("Added: " + token);
        updateTokenConfirmation();
    }
    @Override
    public void onTokenRemoved(DataAPI token) {
//        ((TextView)findViewById(R.id.lastEvent)).setText("Removed: " + token);
        updateTokenConfirmation();
    }
}
