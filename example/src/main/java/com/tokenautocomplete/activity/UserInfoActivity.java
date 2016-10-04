package com.tokenautocomplete.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tokenautocomplete.R;
import com.tokenautocomplete.entity.Toko;
import com.tokenautocomplete.helper.APIManager;
import com.tokenautocomplete.helper.ServiceCenterAPI;
import com.tokenautocomplete.util.MasterPreference;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dipenp
 *
 */
public class UserInfoActivity extends BaseActivity {

	TextView namaToko, alamatToko, emailToko, bonus, telpToko;
	private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS)
			.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//initialize layout inflater for drawer
		getLayoutInflater().inflate(R.layout.activity_user_info, frameLayout);
		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);

		namaToko = (TextView) findViewById(R.id.namaToko);
		telpToko = (TextView) findViewById(R.id.telpToko);
		alamatToko = (TextView) findViewById(R.id.alamatToko);
		emailToko = (TextView) findViewById(R.id.emailToko);
		bonus = (TextView) findViewById(R.id.bonus);

		final MasterPreference masterPreference = new MasterPreference(UserInfoActivity.this);
		String idToko = masterPreference.getStringID();

		Retrofit retrofit = new Retrofit.Builder().
				baseUrl(APIManager.urlKata)
				.addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
				.build();

		//get data from API http://center.kataindonesia.co.id/api/Service/tokoinfo
		ServiceCenterAPI interfaceAPI = retrofit.create(ServiceCenterAPI.class);
		System.out.println("idtoko di retrofit: "+idToko);
		final Call<Toko> toko = interfaceAPI.getToko(idToko);
		toko.enqueue(new Callback<Toko>() {
			@Override
			public void onResponse(Call<Toko> call, Response<Toko> response) {
				if (response.isSuccessful()) {
					namaToko.setText(response.body().getNamatoko());
					emailToko.setText(response.body().getEmail());
					telpToko.setText(response.body().getTelp());
					alamatToko.setText(response.body().getAlamat());
					bonus.setText(response.body().getBonus());
				} else {
//					alert.show("Terjadi Kesalahan: "+response.message(), RegisterActivity.class);
					Log.d("Terjadi Error Sales", response.message());
				}
			}
			@Override
			public void onFailure(Call<Toko> call, Throwable t) {
//				alert.show("Terjadi Kesalahan: "+t.getMessage(), RegisterActivity.class);
				Log.d("Error Register", t.getMessage());
			}
		});

	}
}
