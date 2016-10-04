package com.tokenautocomplete.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tokenautocomplete.DataAPI;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.R;
import com.tokenautocomplete.TokenCompleteTextView;
import com.tokenautocomplete.entity.SalesData;
import com.tokenautocomplete.helper.APIManager;
import com.tokenautocomplete.helper.AlertDialogHelper;
import com.tokenautocomplete.helper.ServiceCenterAPI;
import com.tokenautocomplete.util.MasterPreference;
import com.tokenautocomplete.util.TelephonyInfo;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends BaseActivity implements TokenCompleteTextView.TokenListener<DataAPI> {
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
    protected TextView logout;
    private EditText namaPelanggan, handphone, email, immei1, immei2;
    private RadioButton radiovalue;
    private RadioGroup rg;
    private ProgressDialog dialog;

    private String message_error;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    private AlertDialogHelper alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize content if not using drawer
//        setContentView(R.layout.activity_main);

        //inflater layout from drawer
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        alert = new AlertDialogHelper(Register.this);
        logout = (TextView) findViewById(R.id.logout_button);

        final MasterPreference masterPreference = new MasterPreference(Register.this);
        String imeiCust = masterPreference.getStringImei();
        String idToko = masterPreference.getStringID();
        System.out.println("idtoko: "+idToko);

        immei1 = (EditText) findViewById(R.id.immei1);
        immei2 = (EditText)findViewById(R.id.immei2);

        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        boolean isDualSIM = telephonyInfo.isDualSIM();
        String imsiSIM1 = telephonyInfo.getImsiSIM1();

        if(masterPreference.getStringRoleID().equals("6")) {
            immei1.setEnabled(true);
            immei2.setEnabled(true);
        }else{
            immei1.setEnabled(true);
        }

        if(isDualSIM) {
            String imsiSIM2 = telephonyInfo.getImsiSIM2();
            //immei2.setText(imsiSIM2);
        }


        //get data from API to autocomplete text view
        getSales(idToko);


        Button register_button = (Button) findViewById(R.id.register_button);
        if (register_button != null) {
            register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(Register.this, "", "tunggu sebentar");

//                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    handphone = ((EditText) findViewById(R.id.handphone));
                    rg = (RadioGroup) findViewById(R.id.radio_group);
                    radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId()));
                    email = ((EditText) findViewById(R.id.email));
                    namaPelanggan = ((EditText) findViewById(R.id.namaPelanggan));


                    final String imeiPhone = TelephonyInfo.getInstance(Register.this).getImsiSIM1();
                    final String imeiVal1 = immei1.getText().toString();
                    final String imeiVal2 = immei2.getText().toString();

                    new UploadProcess().execute(
                            imeiPhone,
                            imeiVal1,
                            imeiVal2,
                            namaPelanggan.getText().toString(),
                            handphone.getText().toString(),
                            parseHarga,
                            radiovalue.getText().toString(),
                            email.getText().toString(),
                            masterPreference.getStringRoleID(),
                            parseSales,parseFrontliner,parsePromotor,
                            "android", masterPreference.getStringID(),
                            masterPreference.getStringID()
//							pathPhoto

                    );
                }
            });
        }



        //logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //masterLogin.removePreferences();
                Log.w("kata", "logout on click main menu");
                masterPreference.removePreferences();
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });
    }

    private class UploadProcess extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] param) {
            URL url = null;
            JSONObject jsonObject = null;
            try {
                url = new URL("http://center.kataindonesia.co.id/api/registrasi");
                String charset = "UTF-8";

                try {

                    MultipartUtility multipart = new MultipartUtility(url.toString(), charset);
                    multipart.addFormField("imeipendaftar", param[0].toString());
                    multipart.addFormField("imeicust", param[1].toString());
                    multipart.addFormField("imeicust2", param[2].toString());
                    multipart.addFormField("namaPelanggan", param[3].toString());
                    multipart.addFormField("nohp", param[4].toString());
                    multipart.addFormField("hargajual", param[5].toString());
                    multipart.addFormField("reseller_status", param[6].toString());
                    multipart.addFormField("email", param[7].toString());
                    multipart.addFormField("roleID", param[8].toString());
                    multipart.addFormField("salesID", param[9].toString());
                    multipart.addFormField("frontlinerID", param[10].toString());
                    multipart.addFormField("promotorID", param[11].toString());
                    multipart.addFormField("regform", param[12].toString());
                    multipart.addFormField("refID", param[13].toString());
                    multipart.addFormField("reselerID", param[14].toString());

//					multipart.addFilePart("filephoto", po);
                    // multipart.addFilePart("fileUpload", uploadFile2);
                    List<String> response = multipart.finish();

                    System.out.println("imeipendaftar"+param[0].toString());
                    System.out.println("imei cust"+param[1].toString());
                    System.out.println("imei cust 2"+param[2].toString());
                    System.out.println("nama pelanggan"+param[3].toString());
                    System.out.println("no hp"+param[4].toString());
                    System.out.println("harga jual"+param[5].toString());
                    System.out.println("reseller status"+param[6].toString());
                    System.out.println("email"+param[7].toString());
                    System.out.println("role id"+param[8].toString());
                    System.out.println("sales id"+param[9].toString());
                    System.out.println("frontliner id"+param[10].toString());
                    System.out.println("promotor id"+param[11].toString());
                    System.out.println("regform"+param[12].toString());
                    System.out.println("ref id"+param[13].toString());
                    System.out.println("reseller id"+param[14].toString());


                    System.out.println("SERVER REPLIED:");

                    for (String line : response) {
                        System.out.println(line);
                    }

                    JSONObject jObj = new JSONObject(response.get(0).toString());
                    Message msg = handler.obtainMessage();

                    if(jObj.getString("status").equalsIgnoreCase("NOK")){
                        dialog.dismiss();
                        msg.arg1 = 0;
                        message_error = jObj.getString("pesan");
                    }else{
                        dialog.dismiss();
                        msg.arg1 = 1;
                    }
                    handler.sendMessage(msg);

                } catch (Exception er) {
                    dialog.dismiss();
                    System.out.println("Errro car aabruuuuu " + er.getMessage());
                }
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1) {
                Toast.makeText(Register.this, "Sukses", Toast.LENGTH_LONG).show();
                //noKTP.setText("");
                //namaBelakang.setText("");
                namaPelanggan.setText("");
                //telepon.setText("");
                handphone.setText("");
                hargaJual.setText("");
                //alamat.setText("");
                email.setText("");
            }
            else if(msg.arg1 == 0)
                Toast.makeText(Register.this, message_error, Toast.LENGTH_LONG).show();
        }
    };


    protected void getSales(final String idSales){
        System.out.println("id toko di getSales: "+idSales);
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(APIManager.urlKata)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        //get data from API http://center.kataindonesia.co.id/api/getsales
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
                    alert.show("Terjadi Kesalahan: "+response.message(), Register.class);
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                alert.show("Terjadi Kesalahan: "+t.getMessage(), Register.class);
                Log.d("Error Register", t.getMessage());
            }
        });

        //get data from API http://center.kataindonesia.co.id/api/getFrontliner
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
                    alert.show("Terjadi Kesalahan: "+response.message(), Register.class);
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                alert.show("Terjadi Kesalahan: "+t.getMessage(), Register.class);
                Log.d("Error Register", t.getMessage());
            }
        });

        //get data from API http://center.kataindonesia.co.id/api/getPromotor
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
                    alert.show("Terjadi Kesalahan: "+response.message(), Register.class);
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                alert.show("Terjadi Kesalahan: "+t.getMessage(), Register.class);
                Log.d("Error Register", t.getMessage());
            }
        });

        //get data from API http://center.kataindonesia.co.id/api/getHarga
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
                    alert.show("Terjadi Kesalahan: "+response.message(), Register.class);
                    Log.d("Terjadi Error Sales", response.message());
                }
            }
            @Override
            public void onFailure(Call<SalesData> call, Throwable t) {
                alert.show("Terjadi Kesalahan: "+t.getMessage(), Register.class);
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
