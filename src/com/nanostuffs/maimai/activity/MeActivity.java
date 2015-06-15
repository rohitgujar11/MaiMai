package com.nanostuffs.maimai.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.PlaceJSONParser;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.CountrySimpleAdapter;
import com.nanostuffs.maimai.adapter.LocationsAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.adapter.SpinnerCountryAdapter;
import com.nanostuffs.maimai.model.SearchItem;
import com.nanostuffs.maimai.model.UserDetails;

public class MeActivity extends Activity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener,
		OnLocationChangedListener {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private ImageView mProfilePic;
	private TextView mName;
	private TextView mPersonalInfo;
	private ImageButton mEditIcon;
	private ImageButton mSaveIcon;
	private EditText mUsernameEdit;
	private TextView mUsername;
	private TextView mUsernameText;
	private TextView mAge;
	private TextView mAgeEdit;
	private TextView mAgeText;
	private TextView mGenderText;
	private TextView mGenderTextView;
	private TextView mLocation;
	// private Spinner mCountrySpinner;
	private Spinner mGenderSpinner;
	private LinearLayout mLin;
	private RelativeLayout mRel;
	private TextView mMobile;
	private EditText mMobileEdit;
	private TextView mMobileText;
	private TextView mEmail;
	private EditText mEmailEdit;
	private TextView mEmailText;
	private TextView mReceiveNotification;
	private CheckBox mReceiveNotificationCheckBox;
	private TextView mLocationText;
	private Button mMyPostingsBtn;
	private ImageButton mHelpBtn;
	private RatingBar ratingBar;
	private AQuery androidAQuery;
	private String mCountryByWeb;
	private String mGenderByWeb;
	private UserDetails userDetails;
	private ArrayAdapter<String> myAdap;
	private SpinnerCountryAdapter adapter;
	private Dialog dialog;
	private String imageFilePath;
	private Bitmap bm;
	private byte[] bb;
	private String response;
	private ImageButton mForgotPwd;
	private ImageButton mChangePwd;
	protected static final int CAMERA_ACTIVITY = 0;
	protected static final int RESULT_LOAD_IMAGE = 1;
	private String mOldPassword;
	private String mNewPassword;
	private ImageButton mFBConnect;
	private ImageButton mGConnect;
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private int flag;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private Session session;
	private String access_token;
	private static final int RC_SIGN_IN = 0;
	private AutoCompleteTextView actionBarSearch;
	private SearchItem mItem;
	private ArrayList<SearchItem> mItemList;
	private LocationManager locationManager;
	private double longitudeE6;
	private double latitudeE6;
	private AutoCompleteTextView mCity;
	private SearchItem mSItem;
	final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
	final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;
	private ImageView mEditPen;
	public String mOldEmail;
	private static final int PIC_CROP = 2;
	private int mEditEmailCount = 0;
	private AlertDialog alert;
Options options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		actionBarDetails();
		try {
			options = new BitmapFactory.Options();
		    options.inScaled = false;
			initializeComponents();
			getUserDetails();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	private double[] getGPS() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location l = null;

		for (int i = providers.size() - 1; i >= 0; i--) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}

		double[] gps = new double[2];
		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
			Log.e("", " gps[0] : " + gps[1] + " gps[0] : " + gps[1]);
		}
		return gps;
	}

	private void getUserDetails() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Please wait..");
			progress.setCancelable(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new RetriveUserDetailsTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	private void showConnectionTimeoutMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
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
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}
	public class RetriveUserDetailsTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public RetriveUserDetailsTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				 resultdata = getUserDetailsFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {

				progress.dismiss();

				if (result.length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MeActivity.this);
					builder.setMessage("Connection Timeout ! Please try again.");
					builder.setTitle("Warning !");
					builder.setIcon(R.drawable.alert);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getUserDetails();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();

				} else {

					Log.e("test", "result  : " + result);
					saveLogcatToFile(getApplicationContext());
					try {
						JSONObject responseObj = new JSONObject(result);

						JSONArray ja = responseObj.getJSONArray("data");
						if (!(ja.length() == 0)) {
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo = (JSONObject) ja.get(i);
								userDetails = new UserDetails();
								userDetails.setUid(jo.getString("Id"));
								userDetails.setUsername(jo
										.getString("Username"));
								userDetails.setPassword(jo
										.getString("Password"));
								userDetails.setEmail(jo.getString("Email"));
								userDetails.setImage(jo.getString("Image"));
								userDetails.setAge(jo.getString("Age"));
								userDetails.setMobile(jo.getString("Mobile"));
								userDetails.setGender(jo.getString("Gender"));
								userDetails.setRating(jo.getString("Rating"));
								userDetails.setLocation(jo.getString("City"));
								userDetails.setDateCreated(jo
										.getString("Datecreated"));
								userDetails.setReceiveNotification(jo
										.getString("Notification_flag"));
							}
							Log.e("userDetails.getRating()", "imageeeeeeeee : " +userDetails.getRating());
							if(userDetails.getRating().length()==0)
								Log.i("","");
							else
								ratingBar.setRating(Float.parseFloat(userDetails.getRating()));
							ratingBar.setEnabled(false);

							mOldEmail = userDetails.getEmail();

							String imagePath = userDetails.getImage();
							imagePath = imagePath.replace("\\/", "/");
							Log.e("", "imageeeeeeeee : " + imagePath);
							androidAQuery.id(mProfilePic).image(imagePath,
									false, false, 0, R.drawable.samplephoto);

							if (userDetails.getReceiveNotification().contains(
									"0")) {
								mReceiveNotificationCheckBox.setChecked(true);
							} else {
								mReceiveNotificationCheckBox.setChecked(false);
							}
							if (userDetails.getUsername().length() == 0) {
								mUsernameText.setText("");
							} else {
								mUsernameText
										.setText(userDetails.getUsername());
								mUsernameEdit
										.setText(userDetails.getUsername());
								mName.setText(userDetails.getUsername());
							}
							if (userDetails.getAge().equalsIgnoreCase("null")
									|| userDetails.getAge().equalsIgnoreCase(
											"0")) {
								mAgeText.setText("");
							} else {
								mAgeText.setText(userDetails.getAge());
								mAgeEdit.setText(userDetails.getAge());
							}
							if (userDetails.getEmail().length() == 0) {
								mEmailText.setText("");
							} else {
								if (userDetails.getEmail().contains("@")) {
									mEmailEdit.setText(userDetails.getEmail());
									mEmailText.setText(userDetails.getEmail());
								} else {
									mEmailEdit.setText("");
									mEmailText.setText("");
								}
							}
							if (userDetails.getMobile().length() == 0) {
								mMobileText.setText("");
							} else {
								mMobileEdit.setText(userDetails.getMobile());
								mMobileText.setText(userDetails.getMobile());
							}
							if (userDetails.getLocation().length() != 0) {
								mLocationText
										.setText(userDetails.getLocation());
								mCity.setText(userDetails.getLocation());

								// mCountryByWeb = userDetails.getCountry();
								// int spinnerPosition =
								// myAdap.getPosition(mCountryByWeb);
								// Log.e("", "Country : " +
								// userDetails.getLocation()
								// + "spinner position : " + spinnerPosition);
								// mCountrySpinner.setSelection(spinnerPosition,
								// false);
								// mLocationText.setText(mCountryByWeb);
							} else {
								getCurrentLocation(mCity, mLocationText);
							}
							if (userDetails.getGender().length() != 0) {
								Log.e("",
										"Genderrrrrrrrrrr1 : "
												+ userDetails.getGender());

								mGenderTextView
										.setText(userDetails.getGender());
								mGenderByWeb = userDetails.getGender();
								int spinnerPosition = adapter
										.getPosition(mGenderByWeb);
								Log.e("", "Gender : " + userDetails.getGender()
										+ "spinner position : "
										+ spinnerPosition);
								mGenderSpinner.setSelection(spinnerPosition,
										false);
							} else {
								Log.e("",
										"Genderrrrrrrrrrr2 : "
												+ userDetails.getGender());
								mGenderTextView.setVisibility(View.INVISIBLE);
							}
							mOldPassword = userDetails.getPassword();

						} else {
							showCustomToast("No details present !");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
			}
		}

		private void getCurrentLocation(AutoCompleteTextView mCity,
				TextView mLocationText) {
			// get current location
			LocationManager locationManager = (LocationManager) MeActivity.this
					.getSystemService(Context.LOCATION_SERVICE);

			Location lastKnownLocation_byGps = locationManager
					.getLastKnownLocation(gpsLocationProvider);
			Location lastKnownLocation_byNetwork = locationManager
					.getLastKnownLocation(networkLocationProvider);

			Criteria criteria = new Criteria();
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			String bestprovider = locationManager.getBestProvider(criteria,
					false);
			Location lastknownlocation = locationManager
					.getLastKnownLocation(bestprovider);
			if (lastknownlocation != null) {
				longitudeE6 = lastknownlocation.getLongitude() * 1e6;
				latitudeE6 = lastknownlocation.getLatitude() * 1e6;

				Geocoder geocoder = new Geocoder(MeActivity.this);
				List<Address> addresses;
				try {
					addresses = geocoder.getFromLocation(
							lastknownlocation.getLatitude(),
							lastknownlocation.getLongitude(), 1);

					for (Address address : addresses) {
						// String Country = address.getCountryName() + " "
						// + address.getFeatureName() + " "
						// + address.getLocality() + " "
						// + address.getSubLocality();
						String Country = address.getCountryName();
						// locationdetails +=
						// "Address is "+address.getAddressLine(0)+" ,"+address.getCountryName();
						Log.e("", "longitudeE6 : " + longitudeE6
								+ "latitudeE6 : " + latitudeE6 + "Country : "
								+ Country);
						mCity.setText(Country);
						mLocationText.setText(Country);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (lastKnownLocation_byGps != null) {
				longitudeE6 = lastKnownLocation_byGps.getLongitude() * 1e6;
				latitudeE6 = lastKnownLocation_byGps.getLatitude() * 1e6;

				Geocoder geocoder = new Geocoder(MeActivity.this);
				List<Address> addresses;
				try {
					addresses = geocoder.getFromLocation(
							lastKnownLocation_byGps.getLatitude(),
							lastKnownLocation_byGps.getLongitude(), 1);

					for (Address address : addresses) {
						// String Country = address.getCountryName() + " "
						// + address.getFeatureName() + " "
						// + address.getLocality() + " "
						// + address.getSubLocality();
						String Country = address.getCountryName();
						// locationdetails +=
						// "Address is "+address.getAddressLine(0)+" ,"+address.getCountryName();
						Log.e("", "longitudeE6 : " + longitudeE6
								+ "latitudeE6 : " + latitudeE6 + "Country : "
								+ Country);
						mCity.setText(Country);
						mLocationText.setText(Country);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (lastKnownLocation_byNetwork != null) {
				longitudeE6 = lastKnownLocation_byNetwork.getLongitude() * 1e6;
				latitudeE6 = lastKnownLocation_byNetwork.getLatitude() * 1e6;

				Geocoder geocoder = new Geocoder(MeActivity.this);
				List<Address> addresses;
				try {
					addresses = geocoder.getFromLocation(
							lastKnownLocation_byNetwork.getLatitude(),
							lastKnownLocation_byNetwork.getLongitude(), 1);

					for (Address address : addresses) {
						// String Country = address.getCountryName() + " "
						// + address.getFeatureName() + " "
						// + address.getLocality() + " "
						// + address.getSubLocality();
						String Country = address.getCountryName();
						// locationdetails +=
						// "Address is "+address.getAddressLine(0)+" ,"+address.getCountryName();
						Log.e("", "longitudeE6 : " + longitudeE6
								+ "latitudeE6 : " + latitudeE6 + "Country : "
								+ Country);
						mCity.setText(Country);
						mLocationText.setText(Country);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String countryCode = tm.getSimCountryIso();
				String countryname = getCountryName(countryCode);
				mCity.setText(countryname);
			}
		}
	}

	private String getUserDetailsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		Log.e("", "mUIDStr : " + mUIDStr);
		String postURL = "http://54.149.99.130/ws/get_users.php?uid=40";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
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
	public static void saveLogcatToFile(Context context) {    
	    String fileName = "logcat_"+System.currentTimeMillis()+".txt";
	    File outputFile = new File(context.getExternalCacheDir(),fileName);
	    try {
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initializeComponents() {
		mItemList = new ArrayList<SearchItem>();
		try {
			mGoogleApiClient = new GoogleApiClient.Builder(MeActivity.this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this).addApi(Plus.API, null)
			.addScope(Plus.SCOPE_PLUS_LOGIN).build();
			mGoogleApiClient.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		mProfilePic = (ImageView) findViewById(R.id.profile_pic);
		androidAQuery = new AQuery(this);
		mName = (TextView) findViewById(R.id.name);
		mName.setTypeface(mActionBarTypeface);
		mName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				Intent intent = new Intent(MeActivity.this,
						MeItemsTabActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mPersonalInfo = (TextView) findViewById(R.id.personal_info);
		mPersonalInfo.setTypeface(mActionBarTypeface);
		mUsername = (TextView) findViewById(R.id.username);
		mUsername.setTypeface(mActionBarTypeface);
		mUsernameEdit = (EditText) findViewById(R.id.username_edit);
		mUsernameEdit.setTypeface(mActionBarTypeface);
		mRel = (RelativeLayout) findViewById(R.id.rel);
		mRel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mUsernameEdit.getWindowToken(), 0);
			}
		});
		mUsernameText = (TextView) findViewById(R.id.username_text);
		mUsernameText.setTypeface(mActionBarTypeface);
		mAge = (TextView) findViewById(R.id.age);
		mAge.setTypeface(mActionBarTypeface);
		mAgeEdit = (EditText) findViewById(R.id.age_edit);
		mAgeEdit.setTypeface(mActionBarTypeface);
		mAgeText = (TextView) findViewById(R.id.age_text);
		mAgeText.setTypeface(mActionBarTypeface);
		mGenderText = (TextView) findViewById(R.id.gender);
		mGenderText.setTypeface(mActionBarTypeface);
		mGenderTextView = (TextView) findViewById(R.id.gender_text);
		mGenderTextView.setTypeface(mActionBarTypeface);
		mLocation = (TextView) findViewById(R.id.location);
		mLocation.setTypeface(mActionBarTypeface);
		mLocationText = (TextView) findViewById(R.id.location_text);
		mLocationText.setTypeface(mActionBarTypeface);
		mCity = (AutoCompleteTextView) findViewById(R.id.my_city_autocomplete);
		mCity.setTypeface(mActionBarTypeface);
		mCity.addTextChangedListener(new TextWatcher() {

			private PlacesTask placesTask;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mCity.length() >= 1) {
					// if (checkInternetConnection()) {
					// new RetriveLocationListTask().execute(mCity.getText()
					// .toString());
					// }
					placesTask = new PlacesTask();
					placesTask.execute(s.toString());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mMobile = (TextView) findViewById(R.id.mobile);
		mMobile.setTypeface(mActionBarTypeface);
		mMobileEdit = (EditText) findViewById(R.id.mobile_edit);
		mMobileEdit.setTypeface(mActionBarTypeface);
		mMobileText = (TextView) findViewById(R.id.mobile_text);
		mMobileText.setTypeface(mActionBarTypeface);
		mEmail = (TextView) findViewById(R.id.email);
		mEmail.setTypeface(mActionBarTypeface);
		mEmailEdit = (EditText) findViewById(R.id.email_edit);
		mEmailEdit.setTypeface(mActionBarTypeface);

		mEmailEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Log.e("", "onFocusChange");
					mEmailEdit.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {
							Log.e("", "afterTextChanged");
							mEditEmailCount = 1;
						}
					});
				}
			}
		});
		mEmailText = (TextView) findViewById(R.id.email_text);
		mEmailText.setTypeface(mActionBarTypeface);
		mReceiveNotification = (TextView) findViewById(R.id.receive_notification);
		mReceiveNotification.setTypeface(mActionBarTypeface);

		mReceiveNotificationCheckBox = (CheckBox) findViewById(R.id.check_box_id);

		mReceiveNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mReceiveNotificationCheckBox.isChecked()) {
					mReceiveNotificationCheckBox.setChecked(false);
				} else {
					mReceiveNotificationCheckBox.setChecked(true);
				}

			}
		});

		mReceiveNotificationCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (checkInternetConnection()) {
							ProgressDialog progress = new ProgressDialog(
									MeActivity.this);
							progress.setMessage("Please wait..");
							progress.setCanceledOnTouchOutside(false);
							int corePoolSize = 60;
							int maximumPoolSize = 80;
							int keepAliveTime = 10;
							BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
									maximumPoolSize);
							Executor threadPoolExecutor = new ThreadPoolExecutor(
									corePoolSize, maximumPoolSize,
									keepAliveTime, TimeUnit.SECONDS, workQueue);
							new ReceiveNotificationTask(progress)
									.executeOnExecutor(threadPoolExecutor);
						}
					}
				});

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setNumStars(5);
		ratingBar.setEnabled(false);

		mGenderSpinner = (Spinner) findViewById(R.id.genderSpinner);
		ArrayList<String> gender = new ArrayList<String>();
		// gender.add("Select gender");
		gender.add("");
		gender.add("Male");
		gender.add("Female");
		adapter = new SpinnerCountryAdapter(this, R.layout.spinner11, gender);
		mGenderSpinner.setAdapter(adapter);
		mGenderTextView.setText(mGenderSpinner.getSelectedItem().toString());
		mEditPen = (ImageView) findViewById(R.id.edit_img);
		mEditIcon = (ImageButton) findViewById(R.id.edit_icon);
		mSaveIcon = (ImageButton) findViewById(R.id.save_icon);
		mEditIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mProfilePic.setOnClickListener(MeActivity.this);
				mEditPen.setVisibility(View.VISIBLE);
				mEditIcon.setVisibility(View.GONE);
				mSaveIcon.setVisibility(View.VISIBLE);
				// mUsernameEdit.setVisibility(View.VISIBLE);
				// mUsernameText.setVisibility(View.GONE);
				mAgeEdit.setVisibility(View.VISIBLE);
				mAgeText.setVisibility(View.GONE);
				mMobileEdit.setVisibility(View.VISIBLE);
				mMobileText.setVisibility(View.GONE);
				mEmailEdit.setVisibility(View.VISIBLE);
				mEmailText.setVisibility(View.GONE);
				mCity.setVisibility(View.VISIBLE);
				// mCountrySpinner.setEnabled(true);
				mLocationText.setVisibility(View.GONE);
				mGenderSpinner.setVisibility(View.VISIBLE);
				mGenderSpinner.setEnabled(true);
				mGenderTextView.setVisibility(View.GONE);
			}
		});

		mSaveIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mUsernameEdit.getWindowToken(), 0);
				mProfilePic.setOnClickListener(null);
				mEditPen.setVisibility(View.GONE);
				mEditPen.setVisibility(View.GONE);
				mSaveIcon.setVisibility(View.GONE);
				mEditIcon.setVisibility(View.VISIBLE);
				// mUsernameText.setVisibility(View.VISIBLE);
				// mUsernameEdit.setVisibility(View.GONE);
				mAgeText.setVisibility(View.VISIBLE);
				mAgeEdit.setVisibility(View.GONE);
				mMobileText.setVisibility(View.VISIBLE);
				mMobileEdit.setVisibility(View.GONE);
				mEmailText.setVisibility(View.VISIBLE);
				mEmailEdit.setVisibility(View.GONE);
				mLocationText.setVisibility(View.VISIBLE);
				mCity.setVisibility(View.GONE);
				// mCountrySpinner.setEnabled(false);
				mGenderTextView.setVisibility(View.VISIBLE);
				mGenderSpinner.setVisibility(View.GONE);
				mGenderSpinner.setEnabled(false);

				mGenderTextView.setText(mGenderSpinner.getSelectedItem()
						.toString());
				mLocationText.setText(mCity.getText().toString());
				if (mMobileEdit.length() == 0) {
					mMobileText.setText("");
				} else {
					mMobileText.setText(mMobileEdit.getText().toString());
				}
				if (mEmailEdit.length() == 0) {
					mEmailText.setText("");
				} else {
					mEmailText.setText(mEmailEdit.getText().toString());
				}
				if (mAgeEdit.getText().toString().equalsIgnoreCase("null")) {
					mAgeText.setText("");
				} else {
					mAgeText.setText(mAgeEdit.getText().toString());
				}
				if (mUsernameEdit.length() == 0) {
					mUsernameText.setText("");
				} else {
					mUsernameText.setText(mUsernameEdit.getText().toString());
					mName.setText(mUsernameEdit.getText().toString());
				}

				saveUserDetails();

			}

		});

		mMyPostingsBtn = (Button) findViewById(R.id.my_postings);
		mMyPostingsBtn.setOnClickListener(this);
		mMyPostingsBtn.setTypeface(mActionBarTypeface);
		mMyPostingsBtn.setOnClickListener(this);
		mHelpBtn = (ImageButton) findViewById(R.id.help_btn);
		mHelpBtn.setOnClickListener(this);
		mChangePwd = (ImageButton) findViewById(R.id.change_pwd_btn);
		mChangePwd.setOnClickListener(this);
		mFBConnect = (ImageButton) findViewById(R.id.fb_btn);
		mFBConnect.setOnClickListener(this);
		mGConnect = (ImageButton) findViewById(R.id.google_btn);
		mGConnect.setOnClickListener(this);
	}

	private class PlacesTask extends AsyncTask<String, Void, String> {

		private ParserTask parserTask;

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";
			Log.e("", "test PlacesTask");
			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyCU5VmPXZ67EoqBTinGw0c6UHxPWfE7Tb4";

			String input = "";
			String plac = null;
			for (int i = 0; i < place.length; i++) {
				plac = place[0];
			}
			Log.v("ADDRESS::", plac);
			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=establishment";

			// Sensor enabled
			String sensor = "sensor=false";

			String radius = "radius=500";

			String components = "country:in";
			// Building the parameters to the web service
			// String parameters = input + "&" + radius + "&" + sensor + "&" +
			// key;
			String parameters = input + "&" + components + "&" + sensor + "&"
					+ key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			// new
			// String url =
			// "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=swargate&sensor=false&key=AIzaSyCU5VmPXZ67EoqBTinGw0c6UHxPWfE7Tb4&components=country:in";

			try {
				// Fetching the data from web service in background
				data = downloadUrl(url);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("", "result :" + result);
			try {

				// Creating ParserTask
				parserTask = new ParserTask();

				// Starting Parsing the JSON string returned by Web Service
				parserTask.execute(result);

			} catch (Exception e) {
			}
		}
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { R.id.tv };

			// Creating a SimpleAdapter for the AutoCompleteTextView

			CountrySimpleAdapter adapter = new CountrySimpleAdapter(
					getBaseContext(), result, R.layout.loc, from, to);

			// Setting the adapter
			mCity.setAdapter(adapter);

			// adapter.notifyDataSetChanged();
		}
	}

	public class RetriveLocationListTask extends
			AsyncTask<String, Void, String> {

		public RetriveLocationListTask() {
		}

		public void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = getLocationsFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {

			try {
				Log.e("", "result location: " + result);

				JSONObject responseObj = new JSONObject(result);
				if (result.contains("No data available")) {

				} else {

					JSONArray ja = responseObj.getJSONArray("data");

					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);

						JSONArray ja1 = jo.getJSONArray("response_msg");
						Log.e("", "ja1 : " + ja1);
						for (int j = 0; j < ja1.length(); j++) {
							if (mItemList != null) {
								mItemList.clear();
							}
							Log.e("", "jjjjj : " + ja1.get(i).toString());
							mSItem = new SearchItem();
							mSItem.setItemName(ja1.get(i).toString());
							if (!mItemList.contains(mSItem)) {
								mItemList.add(mSItem);
							}

						}

					}
					if (mItemList.size() != 0) {

						HashSet<SearchItem> listWithoutDuplicates = new HashSet<SearchItem>(
								mItemList);
						List<SearchItem> listWithoutDuplicates1 = new ArrayList<SearchItem>(
								listWithoutDuplicates);
						final LocationsAdapter adapter = new LocationsAdapter(
								MeActivity.this, listWithoutDuplicates1);
						mCity.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getLocationsFromWeb() {
		String postURL = "http://54.149.99.130/ws/find_cities.php?param=bo";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("param", mCity.getText()
					.toString()));
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

	protected void onStart() {
		super.onStart();
		Log.e("", "onStart");
		try {
			mGoogleApiClient.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	protected void onStop() {
		super.onStop();
		Log.e("", "onStop");
		try {
			if (mGoogleApiClient.isConnected()) {
				mGoogleApiClient.disconnect();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		Log.e("", "res");
		flag = 1;
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public class ReceiveNotificationTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public ReceiveNotificationTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = receiveNotificationFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress.dismiss();
		}
	}

	private String receiveNotificationFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/receive_notify.php?uid=1&receive_notify=1";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (mReceiveNotificationCheckBox.isChecked()) {
				params.add(new BasicNameValuePair("receive_notify", "0"));
			} else {
				params.add(new BasicNameValuePair("receive_notify", "1"));
			}
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

	private String getCountryName(String countryCode) {
		HashMap<String, String> countryLookupMap = null;
		countryLookupMap = new HashMap<String, String>();

		countryLookupMap.put("AD", "Andorra");
		countryLookupMap.put("AE", "United Arab Emirates");
		countryLookupMap.put("AF", "Afghanistan");
		countryLookupMap.put("AG", "Antigua and Barbuda");
		countryLookupMap.put("AI", "Anguilla");
		countryLookupMap.put("AL", "Albania");
		countryLookupMap.put("AM", "Armenia");
		countryLookupMap.put("AN", "Netherlands Antilles");
		countryLookupMap.put("AO", "Angola");
		countryLookupMap.put("AQ", "Antarctica");
		countryLookupMap.put("AR", "Argentina");
		countryLookupMap.put("AS", "American Samoa");
		countryLookupMap.put("AT", "Austria");
		countryLookupMap.put("AU", "Australia");
		countryLookupMap.put("AW", "Aruba");
		countryLookupMap.put("AZ", "Azerbaijan");
		countryLookupMap.put("BA", "Bosnia and Herzegovina");
		countryLookupMap.put("BB", "Barbados");
		countryLookupMap.put("BD", "Bangladesh");
		countryLookupMap.put("BE", "Belgium");
		countryLookupMap.put("BF", "Burkina Faso");
		countryLookupMap.put("BG", "Bulgaria");
		countryLookupMap.put("BH", "Bahrain");
		countryLookupMap.put("BI", "Burundi");
		countryLookupMap.put("BJ", "Benin");
		countryLookupMap.put("BM", "Bermuda");
		countryLookupMap.put("BN", "Brunei");
		countryLookupMap.put("BO", "Bolivia");
		countryLookupMap.put("BR", "Brazil");
		countryLookupMap.put("BS", "Bahamas");
		countryLookupMap.put("BT", "Bhutan");
		countryLookupMap.put("BV", "Bouvet Island");
		countryLookupMap.put("BW", "Botswana");
		countryLookupMap.put("BY", "Belarus");
		countryLookupMap.put("BZ", "Belize");
		countryLookupMap.put("CA", "Canada");
		countryLookupMap.put("CC", "Cocos (Keeling) Islands");
		countryLookupMap.put("CD", "Congo, The Democratic Republic of the");
		countryLookupMap.put("CF", "Central African Republic");
		countryLookupMap.put("CG", "Congo");
		countryLookupMap.put("CH", "Switzerland");
		countryLookupMap.put("CI", "Côte d?Ivoire");
		countryLookupMap.put("CK", "Cook Islands");
		countryLookupMap.put("CL", "Chile");
		countryLookupMap.put("CM", "Cameroon");
		countryLookupMap.put("CN", "China");
		countryLookupMap.put("CO", "Colombia");
		countryLookupMap.put("CR", "Costa Rica");
		countryLookupMap.put("CU", "Cuba");
		countryLookupMap.put("CV", "Cape Verde");
		countryLookupMap.put("CX", "Christmas Island");
		countryLookupMap.put("CY", "Cyprus");
		countryLookupMap.put("CZ", "Czech Republic");
		countryLookupMap.put("DE", "Germany");
		countryLookupMap.put("DJ", "Djibouti");
		countryLookupMap.put("DK", "Denmark");
		countryLookupMap.put("DM", "Dominica");
		countryLookupMap.put("DO", "Dominican Republic");
		countryLookupMap.put("DZ", "Algeria");
		countryLookupMap.put("EC", "Ecuador");
		countryLookupMap.put("EE", "Estonia");
		countryLookupMap.put("EG", "Egypt");
		countryLookupMap.put("EH", "Western Sahara");
		countryLookupMap.put("ER", "Eritrea");
		countryLookupMap.put("ES", "Spain");
		countryLookupMap.put("ET", "Ethiopia");
		countryLookupMap.put("FI", "Finland");
		countryLookupMap.put("FJ", "Fiji Islands");
		countryLookupMap.put("FK", "Falkland Islands");
		countryLookupMap.put("FM", "Micronesia, Federated States of");
		countryLookupMap.put("FO", "Faroe Islands");
		countryLookupMap.put("FR", "France");
		countryLookupMap.put("GA", "Gabon");
		countryLookupMap.put("GB", "United Kingdom");
		countryLookupMap.put("GD", "Grenada");
		countryLookupMap.put("GE", "Georgia");
		countryLookupMap.put("GF", "French Guiana");
		countryLookupMap.put("GH", "Ghana");
		countryLookupMap.put("GI", "Gibraltar");
		countryLookupMap.put("GL", "Greenland");
		countryLookupMap.put("GM", "Gambia");
		countryLookupMap.put("GN", "Guinea");
		countryLookupMap.put("GP", "Guadeloupe");
		countryLookupMap.put("GQ", "Equatorial Guinea");
		countryLookupMap.put("GR", "Greece");
		countryLookupMap.put("GS",
				"South Georgia and the South Sandwich Islands");
		countryLookupMap.put("GT", "Guatemala");
		countryLookupMap.put("GU", "Guam");
		countryLookupMap.put("GW", "Guinea-Bissau");
		countryLookupMap.put("GY", "Guyana");
		countryLookupMap.put("HK", "Hong Kong");
		countryLookupMap.put("HM", "Heard Island and McDonald Islands");
		countryLookupMap.put("HN", "Honduras");
		countryLookupMap.put("HR", "Croatia");
		countryLookupMap.put("HT", "Haiti");
		countryLookupMap.put("HU", "Hungary");
		countryLookupMap.put("ID", "Indonesia");
		countryLookupMap.put("IE", "Ireland");
		countryLookupMap.put("IL", "Israel");
		countryLookupMap.put("IN", "India");
		countryLookupMap.put("IO", "British Indian Ocean Territory");
		countryLookupMap.put("IQ", "Iraq");
		countryLookupMap.put("IR", "Iran");
		countryLookupMap.put("IS", "Iceland");
		countryLookupMap.put("IT", "Italy");
		countryLookupMap.put("JM", "Jamaica");
		countryLookupMap.put("JO", "Jordan");
		countryLookupMap.put("JP", "Japan");
		countryLookupMap.put("KE", "Kenya");
		countryLookupMap.put("KG", "Kyrgyzstan");
		countryLookupMap.put("KH", "Cambodia");
		countryLookupMap.put("KI", "Kiribati");
		countryLookupMap.put("KM", "Comoros");
		countryLookupMap.put("KN", "Saint Kitts and Nevis");
		countryLookupMap.put("KP", "North Korea");
		countryLookupMap.put("KR", "South Korea");
		countryLookupMap.put("KW", "Kuwait");
		countryLookupMap.put("KY", "Cayman Islands");
		countryLookupMap.put("KZ", "Kazakstan");
		countryLookupMap.put("LA", "Laos");
		countryLookupMap.put("LB", "Lebanon");
		countryLookupMap.put("LC", "Saint Lucia");
		countryLookupMap.put("LI", "Liechtenstein");
		countryLookupMap.put("LK", "Sri Lanka");
		countryLookupMap.put("LR", "Liberia");
		countryLookupMap.put("LS", "Lesotho");
		countryLookupMap.put("LT", "Lithuania");
		countryLookupMap.put("LU", "Luxembourg");
		countryLookupMap.put("LV", "Latvia");
		countryLookupMap.put("LY", "Libyan Arab Jamahiriya");
		countryLookupMap.put("MA", "Morocco");
		countryLookupMap.put("MC", "Monaco");
		countryLookupMap.put("MD", "Moldova");
		countryLookupMap.put("MG", "Madagascar");
		countryLookupMap.put("MH", "Marshall Islands");
		countryLookupMap.put("MK", "Macedonia");
		countryLookupMap.put("ML", "Mali");
		countryLookupMap.put("MM", "Myanmar");
		countryLookupMap.put("MN", "Mongolia");
		countryLookupMap.put("MO", "Macao");
		countryLookupMap.put("MP", "Northern Mariana Islands");
		countryLookupMap.put("MQ", "Martinique");
		countryLookupMap.put("MR", "Mauritania");
		countryLookupMap.put("MS", "Montserrat");
		countryLookupMap.put("MT", "Malta");
		countryLookupMap.put("MU", "Mauritius");
		countryLookupMap.put("MV", "Maldives");
		countryLookupMap.put("MW", "Malawi");
		countryLookupMap.put("MX", "Mexico");
		countryLookupMap.put("MY", "Malaysia");
		countryLookupMap.put("MZ", "Mozambique");
		countryLookupMap.put("NA", "Namibia");
		countryLookupMap.put("NC", "New Caledonia");
		countryLookupMap.put("NE", "Niger");
		countryLookupMap.put("NF", "Norfolk Island");
		countryLookupMap.put("NG", "Nigeria");
		countryLookupMap.put("NI", "Nicaragua");
		countryLookupMap.put("NL", "Netherlands");
		countryLookupMap.put("NO", "Norway");
		countryLookupMap.put("NP", "Nepal");
		countryLookupMap.put("NR", "Nauru");
		countryLookupMap.put("NU", "Niue");
		countryLookupMap.put("NZ", "New Zealand");
		countryLookupMap.put("OM", "Oman");
		countryLookupMap.put("PA", "Panama");
		countryLookupMap.put("PE", "Peru");
		countryLookupMap.put("PF", "French Polynesia");
		countryLookupMap.put("PG", "Papua New Guinea");
		countryLookupMap.put("PH", "Philippines");
		countryLookupMap.put("PK", "Pakistan");
		countryLookupMap.put("PL", "Poland");
		countryLookupMap.put("PM", "Saint Pierre and Miquelon");
		countryLookupMap.put("PN", "Pitcairn");
		countryLookupMap.put("PR", "Puerto Rico");
		countryLookupMap.put("PS", "Palestine");
		countryLookupMap.put("PT", "Portugal");
		countryLookupMap.put("PW", "Palau");
		countryLookupMap.put("PY", "Paraguay");
		countryLookupMap.put("QA", "Qatar");
		countryLookupMap.put("RE", "Réunion");
		countryLookupMap.put("RO", "Romania");
		countryLookupMap.put("RU", "Russian Federation");
		countryLookupMap.put("RW", "Rwanda");
		countryLookupMap.put("SA", "Saudi Arabia");
		countryLookupMap.put("SB", "Solomon Islands");
		countryLookupMap.put("SC", "Seychelles");
		countryLookupMap.put("SD", "Sudan");
		countryLookupMap.put("SE", "Sweden");
		countryLookupMap.put("SG", "Singapore");
		countryLookupMap.put("SH", "Saint Helena");
		countryLookupMap.put("SI", "Slovenia");
		countryLookupMap.put("SJ", "Svalbard and Jan Mayen");
		countryLookupMap.put("SK", "Slovakia");
		countryLookupMap.put("SL", "Sierra Leone");
		countryLookupMap.put("SM", "San Marino");
		countryLookupMap.put("SN", "Senegal");
		countryLookupMap.put("SO", "Somalia");
		countryLookupMap.put("SR", "Suriname");
		countryLookupMap.put("ST", "Sao Tome and Principe");
		countryLookupMap.put("SV", "El Salvador");
		countryLookupMap.put("SY", "Syria");
		countryLookupMap.put("SZ", "Swaziland");
		countryLookupMap.put("TC", "Turks and Caicos Islands");
		countryLookupMap.put("TD", "Chad");
		countryLookupMap.put("TF", "French Southern territories");
		countryLookupMap.put("TG", "Togo");
		countryLookupMap.put("TH", "Thailand");
		countryLookupMap.put("TJ", "Tajikistan");
		countryLookupMap.put("TK", "Tokelau");
		countryLookupMap.put("TM", "Turkmenistan");
		countryLookupMap.put("TN", "Tunisia");
		countryLookupMap.put("TO", "Tonga");
		countryLookupMap.put("TP", "East Timor");
		countryLookupMap.put("TR", "Turkey");
		countryLookupMap.put("TT", "Trinidad and Tobago");
		countryLookupMap.put("TV", "Tuvalu");
		countryLookupMap.put("TW", "Taiwan");
		countryLookupMap.put("TZ", "Tanzania");
		countryLookupMap.put("UA", "Ukraine");
		countryLookupMap.put("UG", "Uganda");
		countryLookupMap.put("UM", "United States Minor Outlying Islands");
		countryLookupMap.put("US", "United States");
		countryLookupMap.put("UY", "Uruguay");
		countryLookupMap.put("UZ", "Uzbekistan");
		countryLookupMap.put("VA", "Holy See (Vatican City State)");
		countryLookupMap.put("VC", "Saint Vincent and the Grenadines");
		countryLookupMap.put("VE", "Venezuela");
		countryLookupMap.put("VG", "Virgin Islands, British");
		countryLookupMap.put("VI", "Virgin Islands, U.S.");
		countryLookupMap.put("VN", "Vietnam");
		countryLookupMap.put("VU", "Vanuatu");
		countryLookupMap.put("WF", "Wallis and Futuna");
		countryLookupMap.put("WS", "Samoa");
		countryLookupMap.put("YE", "Yemen");
		countryLookupMap.put("YT", "Mayotte");
		countryLookupMap.put("YU", "Yugoslavia");
		countryLookupMap.put("ZA", "South Africa");
		countryLookupMap.put("ZM", "Zambia");
		countryLookupMap.put("ZW", "Zimbabwe");

		return (String) countryLookupMap.get(countryCode.toUpperCase());
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
							startActivity(new Intent(MeActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:

							break;
						case 2:
							startActivity(new Intent(MeActivity.this,
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
									if (actionBarSearch.length() >= 1) {
										startActivity(new Intent(
												MeActivity.this,
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
				startActivity(new Intent(MeActivity.this, SellActivity.class)
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
				startActivity(new Intent(MeActivity.this,
						AllChatsActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		getActionBar().setCustomView(addView);
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

		public RetriveSearchListTask() {
		}

		public void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = getSearchDetailsFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {

			try {
				Log.e("", "result getSearchDetailsFromWeb: " + result);

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
								MeActivity.this, listWithoutDuplicates);
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

	private void logout() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(MeActivity.this);
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
						MeActivity.this);
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});

				 alert = builder.create();
				alert.show();

			} catch (Exception e) {
			}
			return false;
		}
	}


	public class LogoutTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public LogoutTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = logoutFromWeb();
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
						startActivity(new Intent(MeActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								MeActivity.this);
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
		String postURL = "http://54.149.99.130/ws/logout.php?uid=" + mUIDStr;
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

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
		switch (v.getId()) {
		case R.id.help_btn:
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
					Uri.fromParts("mailto", "Alanat30@gmail.com", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
			startActivity(Intent.createChooser(emailIntent, "Send email..."));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.profile_pic:
			openGalleryOrCamera();
			break;
		case R.id.change_pwd_btn:
			if (mOldPassword.length() != 0) {
				changePassword();
			}
			break;
		case R.id.my_postings:
			Intent intent = new Intent(MeActivity.this,
					MeItemsTabActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.fb_btn:
			fbConnect();
			break;
		case R.id.google_btn:
			try {
				Log.e("", "is...." + mGoogleApiClient.isConnected());
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInError();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			break;
		default:
			break;
		}
	}

	private void fbConnect() {
		try {

			session = Session.getActiveSession();
			if (session == null) {
				session = new Session(MeActivity.this);
				Session.setActiveSession(session);
				if (session.getState()
						.equals(SessionState.CREATED_TOKEN_LOADED)) {
					session.openForRead(new Session.OpenRequest(MeActivity.this)
							.setCallback(statusCallback));
					access_token = session.getAccessToken();
				}
			}
			if (!session.isOpened() && !session.isClosed()) {

				session.openForRead(new Session.OpenRequest(MeActivity.this)
						.setCallback(statusCallback));
				access_token = session.getAccessToken();
			} else {
				Session.openActiveSession(MeActivity.this, true, statusCallback);
			}
		} catch (Exception ex) {

		}

	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {

		@SuppressWarnings("deprecation")
		public void call(Session session, SessionState state,
				Exception exception) {
			final Session session2 = Session.getActiveSession();
			if (session2 != null && session2.isOpened()) {
				// If the session is open, make an API call to get user data
				// and define a new callback to handle the response
				Request request = Request.newMeRequest(session2,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								// If the response is successful
								if (session2 == Session.getActiveSession()) {
									if (user != null) {
										String user_ID = user.getId();// user id
										String profileName = user.getName();// user's
																			// profile
																			// name
										// String
										// userNameViewsetText(user.getName());
									}
								}
							}
						});
				Request.executeBatchAsync(request);
			}

			if (session.isOpened()) {

				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {

							// callback after Graph API response with user
							// object

							public void onCompleted(GraphUser user,
									Response response) {
								user.getId();
								user.getUsername();
								if (user != null) {
									Log.e("", "Response : " + response);
									Log.e("useried", user.getId());
									Log.e("", "Name : " + user.getName());
									Log.e("",
											"Username : " + user.getUsername());
									String profilePic = " https://graph.facebook.com/"
											+ user.getId()
											+ "/picture?type=large";
									Log.e("", "profilePic : " + profilePic);
									// String email = user.getProperty("email");

									// // String safeEmail = user.asMap()
									// .get("email").toString();
									Log.e("",
											"email : "
													+ user.getProperty("email"));
									Log.e("", "safeEmail : "
											+ user.asMap().get("email"));

									// Intent intent = new Intent(
									// LoginActivity.this,
									// SignUpActivity.class);
									// intent.putExtra("flag", "2");
									// intent.putExtra("personPhotoUrl",
									// profilePic);
									// intent.putExtra("personName",
									// user.getName());
									// startActivity(intent);
									// overridePendingTransition(
									// R.anim.slide_in_right,
									// R.anim.slide_out_left);
									//
									// mFBPersonName = user.getName();
									// mFBPhotoURL = profilePic;
									// mFBEmail = user.getId();
									// // if
									// (!mFBPhotoURL.equalsIgnoreCase("null")) {
									// new LoadProfileImage(mProfilePic)
									// .execute(mFBPhotoURL);
									// }
									// Toast.makeText(MeActivity.this,
									// "Connected", Toast.LENGTH_SHORT)
									// .show();
									showCustomToast("Connected");

								}
							}

						});

			}
		}

	};
	private Uri picUri;
	private String mNewEmail;

	private void showCustomToast(String string) {
		// LayoutInflater inflater = getLayoutInflater();
		// View layout = inflater.inflate(R.layout.toast_background,
		// (ViewGroup) findViewById(R.id.toast_layout_root));
		//
		// TextView toast_text = (TextView)
		// layout.findViewById(R.id.toast_text);
		// toast_text.setTypeface(mActionBarTypeface);
		// toast_text.setText(string);
		// Toast toast = new Toast(getApplicationContext());
		// toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 15);
		// toast.setDuration(Toast.LENGTH_LONG);
		// toast.setView(layout);
		// toast.show();
		Toast.makeText(MeActivity.this, string, Toast.LENGTH_SHORT).show();
	}

	private void changePassword() {
		dialog = new Dialog(MeActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.change_pwd,
				null));
		final EditText oldPassword = (EditText) dialog
				.findViewById(R.id.old_pwd);
		oldPassword.setTypeface(mActionBarTypeface);
		oldPassword.setText(mOldPassword);
		final EditText newPassword = (EditText) dialog
				.findViewById(R.id.new_pwd);
		newPassword.setTypeface(mActionBarTypeface);
		final TextView oldPasswordText = (TextView) dialog
				.findViewById(R.id.old_pwd_text);
		oldPasswordText.setTypeface(mActionBarTypeface);
		final TextView newPasswordText = (TextView) dialog
				.findViewById(R.id.new_pwd_text);
		newPasswordText.setTypeface(mActionBarTypeface);

		Button changePwd = (Button) dialog.findViewById(R.id.change_pwd);
		changePwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (newPassword.length() == 0) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							MeActivity.this);
					alertDialogBuilder
							.setTitle("Warning !")
							.setIcon(R.drawable.alert)
							.setMessage("Please enter a password")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog1, int id) {
											dialog1.dismiss();
										}
									});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				} else {
					dialog.dismiss();
					mNewPassword = newPassword.getText().toString();
					changePasswordWeb();
					getUserDetails();
				}
			}

		});
		changePwd.setTypeface(mActionBarTypeface);

		dialog.show();

	}

	private void changePasswordWeb() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Please wait..");
			progress.setCancelable(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new ChangePasswordTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	private void openGalleryOrCamera() {
		dialog = new Dialog(MeActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mMobile.getBottom() + 10;
		Button camera = (Button) dialog.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				Uri fileUri = getOutputMediaFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, CAMERA_ACTIVITY);
				// performCrop();
			}
		});
		Button gallery = (Button) dialog.findViewById(R.id.gallery);
		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 400);
				intent.putExtra("outputY", 400);
				intent.putExtra("return-data", true);
				startActivityForResult(
						Intent.createChooser(intent, "Complete action using"),
						RESULT_LOAD_IMAGE);
			}
		});
		dialog.show();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
		Runtime.getRuntime().gc();
	}

	private static Uri getOutputMediaFileUri() {
		Log.e("", "getOutputMediaFile : " + getOutputMediaFile());
		return Uri.fromFile(getOutputMediaFile());
	}

	private static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				android.os.Environment
						.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES),
				"MaiMai");
		Log.e("", "mediaStorageDir : " + mediaStorageDir.exists());
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		// Create a media file name
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "image.jpg");
		Log.e("", "mediaFile  :" + mediaFile);
		return mediaFile;
	}

	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
			int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);

		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (dialog != null) {
			dialog.dismiss();
		}

		try {
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
					&& null != data) {
				Uri selectedImage = data.getData();
				if (Build.VERSION.SDK_INT >= 19) {
					Uri selectedImage1 = data.getData();
					String imguri = selectedImage1.toString();
					if (imguri.substring(0, 21).equals("content://com.android")) {
						String[] photo_split = imguri.split("%3A");
						imguri = "content://media/external/images/media/"
								+ photo_split[1];
					}
					selectedImage1 = Uri.parse(imguri);
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(selectedImage1,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
					Log.e("", "picturePath : " + picturePath);
					picUri = getOutputMediaFileUri();
					performCrop(selectedImage1);
					//mProfilePic.setImageBitmap(getCroppedBitmap(BitmapFactory
						//	.decodeFile(picturePath)));
//					BitmapDrawable drawable = (BitmapDrawable) mProfilePic
//							.getDrawable();
//					Bitmap bmap = drawable.getBitmap();
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					bmap.compress(CompressFormat.PNG, 50, bos);
//					bb = bos.toByteArray();
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mProfilePic.setImageBitmap(getCroppedBitmap(photo));
						// BitmapDrawable drawable = (BitmapDrawable)
						// mProfilePic
						// .getDrawable();
						// Bitmap bmap = drawable.getBitmap();
						// ByteArrayOutputStream bos = new
						// ByteArrayOutputStream();
						// bmap.compress(CompressFormat.PNG, 50, bos);
						// bb = bos.toByteArray();
					}
				}

			} else if (requestCode == CAMERA_ACTIVITY
					&& resultCode == RESULT_OK) {
				if (Build.VERSION.SDK_INT >= 19) {
					imageFilePath = getOutputMediaFileUri().getPath();
					bm = BitmapFactory.decodeFile((imageFilePath),options);
					Matrix matrix = new Matrix();
					bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
							bm.getHeight(), matrix, true);
					ExifInterface ei = null;
					try {
						ei = new ExifInterface(imageFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					int orientation = ei.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						Matrix matrix2 = new Matrix();
						matrix2.postRotate(90);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix2, true);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						Matrix matrix4 = new Matrix();
						matrix4.postRotate(180);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix4, true);
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						Matrix matrix3 = new Matrix();
						matrix3.postRotate(270);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix3, true);
						break;
					case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
						Matrix matrix6 = new Matrix();
						matrix6.preScale(-1.0f, 1.0f);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix6, true);
						break;
					case ExifInterface.ORIENTATION_FLIP_VERTICAL:
						Matrix matrix7 = new Matrix();
						matrix7.preScale(1.0f, -1.0f);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix7, true);
						break;
					}
					picUri = getOutputMediaFileUri();
					performCrop(picUri);
				//	mProfilePic.setImageBitmap(getCroppedBitmap(bm));
					// BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					// .getDrawable();
					// Bitmap bmap = drawable.getBitmap();
					// ByteArrayOutputStream bos = new ByteArrayOutputStream();
					// bmap.compress(CompressFormat.PNG, 50, bos);
					// bb = bos.toByteArray();
				} else {
					picUri = getOutputMediaFileUri();
					performCrop(picUri);
				}

			}
			if (requestCode == PIC_CROP && resultCode == RESULT_OK) {
				if (Build.VERSION.SDK_INT >= 19) {
					Bundle extras = data.getExtras();
	    			Bitmap b = extras.getParcelable("data");
	    			mProfilePic.setImageBitmap(getCroppedBitmap(b));
	    			
				/*	Uri selectedImage = data.getData();
					String imguri = selectedImage.toString();
					if (imguri.substring(0, 21).equals("content://com.android")) {
						String[] photo_split = imguri.split("%3A");
						imguri = "content://media/external/images/media/"
								+ photo_split[1];
					}
					selectedImage = Uri.parse(imguri);
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
					mProfilePic.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile((picturePath),options)));*/
					// BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					// .getDrawable();
					// Bitmap bmap = drawable.getBitmap();
					// ByteArrayOutputStream bos = new ByteArrayOutputStream();
					// bmap.compress(CompressFormat.PNG, 50, bos);
					// bb = bos.toByteArray();
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");
					mProfilePic.setImageBitmap(getCroppedBitmap(thePic));
					// BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					// .getDrawable();
					// Bitmap bmap = drawable.getBitmap();
					// ByteArrayOutputStream bos = new ByteArrayOutputStream();
					// bmap.compress(CompressFormat.PNG, 50, bos);
					// bb = bos.toByteArray();
				}
			}
			BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					.getDrawable();
			Bitmap bmap = drawable.getBitmap();
			bmap = getCroppedBitmap(codec(bmap, Bitmap.CompressFormat.JPEG, 15));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmap.compress(CompressFormat.PNG,0, bos);
			bb = bos.toByteArray();
		} catch (Exception e) {
		}
		try {

			if (Session.getActiveSession() != null) {
				Session.getActiveSession().onActivityResult(this, requestCode,
						resultCode, data);
			}
			if (requestCode == RC_SIGN_IN) {
				if (resultCode != RESULT_OK) {
					mSignInClicked = false;
				}

				mIntentInProgress = false;

				try {
					if (!mGoogleApiClient.isConnecting()) {
						mGoogleApiClient.connect();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		} catch (OutOfMemoryError e) {
			// Toast.makeText(MeActivity.this,
			// "Please select a lower resolution image",
			// Toast.LENGTH_SHORT).show();
			showCustomToast("Please select a lower resolution image");
		}
	}

	private void performCrop(Uri uri) {

		try {
			
			
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	/*
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 400);
			cropIntent.putExtra("outputY", 400);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	*/}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
/*		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, 150, 150);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				75, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;*/

        int targetWidth = 150;
        int targetHeight = 150;
        Bitmap output = Bitmap.createBitmap(targetWidth, 
                            targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth), 
            ((float) targetHeight)) / 2),
            Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = bitmap;
        canvas.drawBitmap(sourceBitmap, 
            new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()), 
            new Rect(0, 0, targetWidth, targetHeight), null);
        return output;
    
	}

	private void saveUserDetails() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Please wait..");
			progress.setCancelable(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new SaveUserDetailsTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	public class SaveUserDetailsTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public SaveUserDetailsTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				 resultdata = getSaveDetailsFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {

			Log.e("test", "result savvvvvvvvve : " + result);
			try {
				JSONObject responseObj = new JSONObject(result);

				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					// if (jo.getString("response_msg").equalsIgnoreCase(
					// "Successfully updated") ||
					// (jo.getString("response_msg").equalsIgnoreCase(
					// "User Name already present"))
					// ||(jo.getString("response_msg").equalsIgnoreCase(
					// "Successfully updated"))) {
					if (jo.getString("response_msg").equalsIgnoreCase(
							"Successfully updated")) {
						// Toast.makeText(MeActivity.this,
						// "Successfully updated",
						// Toast.LENGTH_SHORT).show();
						showCustomToast("Successfully updated");
					}
				}

				progress.dismiss();

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class ChangePasswordTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public ChangePasswordTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = getChangePasswordToWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {

			Log.e("test", "result  : " + result);
			try {
				JSONObject responseObj = new JSONObject(result);

				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg").equalsIgnoreCase(
							"Password save successfully")) {
						// Toast.makeText(MeActivity.this,
						// "Password changed successfully",
						// Toast.LENGTH_SHORT).show();
						showCustomToast("Password changed successfully");
					}
				}

				progress.dismiss();

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getSaveDetailsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		mNewEmail = mEmailText.getText().toString();
		Log.e("", "mUIDStr : " + mUIDStr);
		String postURL = "http://54.149.99.130/ws/edit_user.php?uid=&email=&city=&gender=&mobile=&age=&country=";
		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", mUIDStr));
			if (mEditEmailCount == 1) {
				mEditEmailCount = 0;
				params.add(new BasicNameValuePair("email", mEmailText.getText()
						.toString()));
				Log.e("", "mNewEmail not equal");
			} else {
				Log.e("", "email not change");
				params.add(new BasicNameValuePair("email", ""));
			}
			params.add(new BasicNameValuePair("gender", mGenderTextView
					.getText().toString()));
			params.add(new BasicNameValuePair("mobile", mMobileText.getText()
					.toString()));
			params.add(new BasicNameValuePair("age", mAgeText.getText()
					.toString()));
			params.add(new BasicNameValuePair("city", mLocationText.getText()
					.toString()));

			if (bb == null) {
				Log.e("", "1");
				response = CustomHttpClient.executeHttpPost(postURL, params);
			} else {
				Log.e("", "2");
				response = CustomHttpClient.executeHttpPostForImg(postURL,
						params, "userfile", bb);
			}
			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getChangePasswordToWeb() {
		Log.e("", " bb :" + bb);
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/change_password.php?pass=12345&uid=51";
		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", mUIDStr));
			params.add(new BasicNameValuePair("pass", mNewPassword));

			response = CustomHttpClient.executeHttpPost(postURL, params);

			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				// personName = currentPerson.getDisplayName();
				// personPhotoUrl = currentPerson.getImage().getUrl();
				// String personGooglePlusProfile = currentPerson.getUrl();
				// email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				// location = currentPerson.getCurrentLocation();
				//
				// Log.e("", "Name: " + personName + ", plusProfile: "
				// + personGooglePlusProfile + ", email: " + email
				// + ", Image: " + personPhotoUrl + "Location : "
				// + location);
				//
				// // txtName.setText(personName);
				// // txtEmail.setText(email);
				//
				// // by default the profile url gives 50x50 px image only
				// // we can replace the value with whatever dimension we want
				// by
				// // replacing sz=X
				// personPhotoUrl = personPhotoUrl.substring(0,
				// personPhotoUrl.length() - 2)
				// + PROFILE_PIC_SIZE;

				if (flag == 1) {
					// Intent intent = new Intent(LoginActivity.this,
					// SignUpActivity.class);
					// intent.putExtra("flag", "1");
					// intent.putExtra("personPhotoUrl", personPhotoUrl);
					// intent.putExtra("personName", personName);
					// intent.putExtra("email", email);
					// intent.putExtra("location", location);
					// startActivity(intent);
					// overridePendingTransition(R.anim.slide_in_right,
					// R.anim.slide_out_left);
					// mGooglePhotoURL = personPhotoUrl;
					// if (mGooglePersonName != null) {
					// mGooglePersonName = personName;
					// }
					// if (mGoogleEmail != null) {
					// mGoogleEmail = email;
					// }
					// if (mGoogleLocation != null) {
					// mGoogleLocation = location;
					// }
					// // if (!mGooglePhotoURL.equalsIgnoreCase("null")) {
					// new LoadProfileImage(mProfilePic)
					// .execute(mGooglePhotoURL);
					// // }
					// signupWithGoogle();
					// Toast.makeText(MeActivity.this, "Connected",
					// Toast.LENGTH_SHORT).show();
					showCustomToast("Connected");
					flag = 0;

				}
				// new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
				mGConnect.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent intent = new Intent(LoginActivity.this,
						// SignUpActivity.class);
						// intent.putExtra("flag", "1");
						// intent.putExtra("personPhotoUrl", personPhotoUrl);
						// intent.putExtra("personName", personName);
						// intent.putExtra("email", email);
						// intent.putExtra("location", location);
						// startActivity(intent);
						// overridePendingTransition(R.anim.slide_in_right,
						// R.anim.slide_out_left);
						// mGooglePhotoURL = personPhotoUrl;
						// if (mGooglePersonName != null) {
						// mGooglePersonName = personName;
						// }
						// if (mGoogleEmail != null) {
						// mGoogleEmail = email;
						// }
						// if (mGoogleLocation != null) {
						// mGoogleLocation = location;
						// }
						// // if (!mGooglePhotoURL.equalsIgnoreCase("null")) {
						// new LoadProfileImage(mProfilePic)
						// .execute(mGooglePhotoURL);
						// // }
						// signupWithGoogle();
						// Toast.makeText(MeActivity.this, "Connected",
						// Toast.LENGTH_SHORT).show();
						showCustomToast("Connected");
					}
				});

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		try {
			mGoogleApiClient.connect();
			updateUI(false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			// btnSignIn.setVisibility(View.GONE);
			// btnSignOut.setVisibility(View.VISIBLE);
			// btnRevokeAccess.setVisibility(View.VISIBLE);
			// llProfileLayout.setVisibility(View.VISIBLE);
		} else {
			// btnSignIn.setVisibility(View.VISIBLE);
			// btnSignOut.setVisibility(View.GONE);
			// btnRevokeAccess.setVisibility(View.GONE);
			// llProfileLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLocationChanged(Location loc) {
		// Toast.makeText(
		// getBaseContext(),
		// "Location changed : Lat: " + loc.getLatitude() + " Lng: "
		// + loc.getLongitude(), Toast.LENGTH_SHORT).show();
		String longitude = "Longitude: " + loc.getLongitude();
		Log.v("", longitude);
		String latitude = "Latitude: " + loc.getLatitude();
		Log.v("", latitude);

		/*----------to get City-Name from coordinates ------------- */
		String cityName = null;
		Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
			if (addresses.size() > 0)
				System.out.println(addresses.get(0).getLocality());
			cityName = addresses.get(0).getLocality();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String s = longitude + "\n" + latitude + "\n\nMy Currrent City is: "
				+ cityName;
		// editLocation.setText(s);
		Log.e("", "LOCATION : " + s);

	}
}
