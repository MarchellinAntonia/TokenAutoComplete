package com.tokenautocomplete.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.tokenautocomplete.R;
import com.tokenautocomplete.util.MasterPreference;

/**
 * @author dipenp
 *
 */
public class WelcomeActivity extends BaseActivity {
	TextView namaUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 *  We will not use setContentView in this activty 
		 *  Rather than we will use layout inflater to add view in FrameLayout of our base activity layout*/
		
		/**
		 * Adding our layout to parent class frame layout.
		 */
		getLayoutInflater().inflate(R.layout.activity_welcome, frameLayout);
		
		/**
		 * Setting title and itemChecked  
		 */
		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);
		final MasterPreference masterPreference = new MasterPreference(WelcomeActivity.this);

		namaUser = (TextView) findViewById(R.id.namaUser);

		namaUser.setText(masterPreference.getStringUname());
//		((ImageView)findViewById(R.id.image_view)).setBackgroundResource(R.drawable.image1);
	}
}
