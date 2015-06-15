package com.nanostuffs.maimai.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.model.SearchItem;

public class LocationActivity extends FragmentActivity implements
		LocationListener, android.location.LocationListener {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private AutoCompleteTextView actionBarSearch;
	private ArrayList<SearchItem> mItemList;
	private Button mSendCurrentLocation;
	private SearchItem mItem;
	private GoogleMap mGoogleMap;
	private LocationManager lm;
	private String mLatitudeStr;
	private String mLongitudeStr;
	private Double mLatDouble;
	private Double mLongDouble;
	private double mLatitude;
	private double mLongitude;
	private String mToName;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		actionBarDetails();
		try {
			initializeComponents();
		} catch (Exception e) {
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
		Runtime.getRuntime().gc();
	}

	private void initializeComponents() {
		SupportMapFragment myMAPF = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_all);
		mGoogleMap = myMAPF.getMap();
		mGoogleMap.clear();
		mGoogleMap.setMyLocationEnabled(true);
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		Location location = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			Log.e("", "lat : " + lat + "lng : " + lng);
			mLatitude = lat;
			mLongitude = lng;
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat, lng), 12));
		} else {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					LocationActivity.this);
		}
		if (getIntent().getExtras() != null) {
			mLatitudeStr = getIntent().getExtras().getString("latitude");
			mLongitudeStr = getIntent().getExtras().getString("longitude");
			mToName = getIntent().getExtras().getString("name");
			mLatDouble = Double.valueOf(mLatitudeStr);
			mLongDouble = Double.valueOf(mLongitudeStr);
		}

		if (mLatDouble != null && mLongDouble != null) {
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(mLatDouble, mLongDouble), 12));
			mGoogleMap.addMarker(new MarkerOptions().position(
					new LatLng(mLatDouble, mLongDouble)));
		}

	}

	public void actionBarDetails() {
		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		
		mNavMenu = (ImageButton) addView.findViewById(R.id.menu);
		TextView chat_view = (TextView) addView.findViewById(R.id.msg_count);
		if(LoginActivity.chat_cnt>0){
			String get_count = String.valueOf(LoginActivity.chat_cnt);
			chat_view.setVisibility(View.VISIBLE);
			chat_view.setText(get_count);
		}
		else if(LoginActivity.chat_cnt==0)
    		chat_view.setVisibility(View.INVISIBLE);
		NavgListAdapter adapter = new NavgListAdapter(this, navString);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		actionBarSearch = (AutoCompleteTextView) addView
				.findViewById(R.id.action_bar_search);
		final ListView navList = (ListView) findViewById(R.id.drawer);
		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int pos, long id) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

					@Override
					public void onDrawerClosed(View drawerView) {
						super.onDrawerClosed(drawerView);
						switch (pos) {
						case 0:
							startActivity(new Intent(LocationActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(LocationActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(LocationActivity.this,
									NewsActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 3:
							logout();
							break;
						default:
							break;
						}
						drawer.setDrawerListener(null);
					}
				});
				drawer.closeDrawer(navList);
			}
		});

		actionBarSearch.setTypeface(mActionBarTypeface);
		actionBarSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (actionBarSearch.length() >= 1) {
					// if (checkInternetConnection()) {
					new RetriveSearchListTask().execute(actionBarSearch
							.getText().toString());
					// }
				}

				actionBarSearch
						.setOnEditorActionListener(new OnEditorActionListener() {
							@Override
							public boolean onEditorAction(TextView v,
									int actionId, KeyEvent event) {
								if (actionId == EditorInfo.IME_ACTION_SEARCH) {
									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(
											actionBarSearch.getWindowToken(), 0);
									if (actionBarSearch.length() >= 1) {
										startActivity(new Intent(
												LocationActivity.this,
												SearchedItemListActivity.class)
												.setFlags(
														Intent.FLAG_ACTIVITY_CLEAR_TOP)
												.putExtra(
														"itemname",
														actionBarSearch
																.getText()
																.toString()));
										overridePendingTransition(
												R.anim.slide_in_right,
												R.anim.slide_out_left);
									}
								}
								return false;
							}
						});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mNavMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				if (drawer.isDrawerOpen(Gravity.LEFT)) {
					drawer.closeDrawer(Gravity.LEFT);
				} else {
					drawer.openDrawer(Gravity.LEFT);
				}
			}
		});

		ImageView searchClear = (ImageView) addView
				.findViewById(R.id.search_cancel);
		searchClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBarSearch.setText("");
			}
		});
		ImageView sell = (ImageView) addView.findViewById(R.id.camera);
		sell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				startActivity(new Intent(LocationActivity.this,
						SellActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		ImageView message = (ImageView) addView.findViewById(R.id.message);
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				startActivity(new Intent(LocationActivity.this,
						AllChatsActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		searchClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBarSearch.setText("");
			}
		});
		getActionBar().setCustomView(addView);
	}
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}
	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras() != null) {
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					// we're connected
					try {
						alert.cancel();
						try {
							initializeComponents();
						} catch (Exception e) {
						}
					} catch (Exception e) {
					}

				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		actionBarSearch.setText("");
		// getCategories();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkReceiver);
	}

	public class RetriveSearchListTask extends AsyncTask<String, Void, String> {

		private String resultdata;

		public RetriveSearchListTask() {
		}

		public void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				 resultdata = getSearchDetailsFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LocationActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				try {
					Log.e("", "result : " + result);

					JSONObject responseObj = new JSONObject(result);
					if (result.contains("No data available")) {

					} else {
						if (mItemList != null) {
							mItemList.clear();
						}
						JSONArray ja = responseObj.getJSONArray("data");

						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = (JSONObject) ja.get(i);
							mItem = new SearchItem();
							mItem.setItemName(jo.getString("Name"));
							mItemList.add(mItem);
						}
						if (mItemList.size() != 0) {
							List<SearchItem> listWithoutDuplicates = new ArrayList<SearchItem>(
									mItemList);
							final SearchAdapter adapter = new SearchAdapter(
									LocationActivity.this,
									listWithoutDuplicates);
							actionBarSearch.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}catch (NullPointerException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getSearchDetailsFromWeb() {
		String postURL = "http://54.149.99.130/ws/search_item.php?keyword=he";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("keyword", actionBarSearch
					.getText().toString()));
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	private void logout() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(LocationActivity.this);
			progress.setMessage("Logging out...");
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new LogoutTask(progress).executeOnExecutor(threadPoolExecutor);
		}
	}

	private boolean checkInternetConnection() {

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
			return haveConnectedWifi || haveConnectedMobile;
		} else {
			try {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LocationActivity.this);
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(LocationActivity.this,LocationActivity.class);
								startActivity(intent);
							}
						});

				 alert = builder.create();
				alert.show();

			} catch (Exception e) {
			}
			return false;
		}
	}

	private void showConnectionTimeoutMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LocationActivity.this);
		builder.setMessage("Connection Timeout ! Please try again.");
		builder.setTitle("Warning !");
		builder.setIcon(R.drawable.alert);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public class LogoutTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public LogoutTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				 resultdata = logoutFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			try {
				Log.e("", "result :" + result);

				JSONObject responseObj = new JSONObject(result);
				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg").equalsIgnoreCase(
							"Logout successfully")) {
						Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
						SharedPreferences settings = getSharedPreferences(
								LoginActivity.PREFS_NAME, 0);
						settings.edit()
								.putString(LoginActivity.PREFS_LOGIN, "FAILED")
								.commit();
						startActivity(new Intent(LocationActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								LocationActivity.this);
						alertDialogBuilder
								.setMessage(jo.getString("response_msg"))
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.dismiss();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String logoutFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/logout.php?uid="+mUIDStr;
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", mUIDStr));
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			Log.e("", "lat : " + lat + "lng : " + lng);
			mLatitude = lat;
			mLongitude = lng;
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(lat, lng), 12));
		}
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (drawer.isDrawerOpen(Gravity.LEFT)) {
				drawer.closeDrawer(Gravity.LEFT);
			} else {
				drawer.openDrawer(Gravity.LEFT);
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		return true;
	}
}
