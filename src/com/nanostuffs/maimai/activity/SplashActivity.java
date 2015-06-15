package com.nanostuffs.maimai.activity;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.nanostuffs.maimai.R;

public class SplashActivity extends Activity {
	public static final String PREFS_UID = "UID";
	public static ArrayList<String>  view_count_array = new ArrayList<String>();
	public static int view_count;
	public static String url = "http://2mai.mobi/maimai/ws/"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity);
		WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
		attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		this.getWindow().setAttributes(attrs);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences settings = getSharedPreferences(
						LoginActivity.PREFS_NAME, 0);
				String value = settings.getString(LoginActivity.PREFS_LOGIN,
						"FAILED");
				if (value.equalsIgnoreCase("SUCCESS")) {
					startActivity(new Intent(SplashActivity.this,
							HomeActivity.class));
					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}

			}
		}, 1500);
	}
}
