package com.nanostuffs.maimai.activity;

import static com.nanostuffs.maimai.notification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.nanostuffs.maimai.notification.CommonUtilities.EXTRA_MESSAGE;
import static com.nanostuffs.maimai.notification.CommonUtilities.SENDER_ID;

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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.PlaceJSONParser;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.CountrySimpleAdapter;
import com.nanostuffs.maimai.adapter.LocationsAdapter;
import com.nanostuffs.maimai.model.SearchItem;
import com.nanostuffs.maimai.notification.WakeLocker;

public class SignUpActivity extends Activity implements OnClickListener,
		OnLocationChangedListener {
	protected static final int CAMERA_ACTIVITY = 0;
	protected static final int RESULT_LOAD_IMAGE = 1;
	private static final int PIC_CROP = 2;
	private Typeface mActionBarTypeface;
	private Button mSignUp;
	private EditText mEmail;
	private EditText mPassword;
	private EditText mUsername;
	private EditText mMyCity;
	private LinearLayout mLin;
	private ImageView mProfilePic;
	private Dialog dialog;
	private String imageFilePath;
	private Bitmap bm;
	private byte[] bb;
	private String response;
	private String mGoogleName;
	private String mGooglePhotoUrl;
	private String mGoogleEmail;
	private String mGoogleLocation;
	AsyncTask<Void, Void, Void> mRegisterTask;
	public static String regId;
	private String mRegID;
	private ArrayList<SearchItem> mItemList;
	private SearchItem mSItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		try {
			initializeComponents();
		} catch (Exception e) {
		}

	}

	public class GCMTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public GCMTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
		
			GCMRegistrar.checkDevice(SignUpActivity.this);

			// Make sure the manifest was properly set - comment out this
			// line
			// while developing the app, then uncomment it when it's ready.
			// GCMRegistrar.checkManifest(SignUpActivity.this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					DISPLAY_MESSAGE_ACTION));

			// Get GCM registration id
			regId = GCMRegistrar.getRegistrationId(SignUpActivity.this);

			// Check if regid already presents
			if (regId.equals("")) {
				// Registration is not present, register now with GCM
				GCMRegistrar.register(SignUpActivity.this, SENDER_ID);
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(SignUpActivity.this)) {
					// Skips registration.
					GCMRegistrar.unregister(SignUpActivity.this);
					GCMRegistrar.register(SignUpActivity.this, SENDER_ID);
					// Toast.makeText(getApplicationContext(),
					// "Already registered with GCM",
					// Toast.LENGTH_LONG).show();
				} else {
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					final Context context = SignUpActivity.this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user
							// ServerUtilities.register(context, name,
							// amount, regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					mRegisterTask.execute(null, null, null);
				}
			}
			int flag = 0;
			while (regId.equals("")) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			
			} catch (Exception e) {
			}
			return regId;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			Log.e("", "regId : " + regId);
			if (regId != null) {
				mRegID = regId;
				savePreferences("regId", regId);
			}

		}
	}

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	private AutoCompleteTextView mMyCityAutocomplete;
	private LocationManager locationManager;
	private double longitudeE6;
	private double latitudeE6;
	private Uri picUri;
	protected PlacesTask placesTask;
	private AlertDialog alert;

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();

	}

	private void initializeComponents() {
		LocationManager lm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Location location = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			Log.e("", "lat : " + lat + "lng : " + lng);

		}

		mItemList = new ArrayList<SearchItem>();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SignUpActivity.this);
		mRegID = sharedPreferences.getString("regId", "");
		Log.e("", "preferences mRegID : " + mRegID);
		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");

		mLin = (LinearLayout) findViewById(R.id.lin);
		mSignUp = (Button) findViewById(R.id.sign_up);
		mEmail = (EditText) findViewById(R.id.email);
		mPassword = (EditText) findViewById(R.id.password);
		mUsername = (EditText) findViewById(R.id.username);
		// mMyCity = (EditText) findViewById(R.id.my_city);
		mMyCityAutocomplete = (AutoCompleteTextView) findViewById(R.id.my_city_autocomplete);

		// get current location
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		String bestprovider = locationManager.getBestProvider(criteria, false);
		Location lastknownlocation = locationManager
				.getLastKnownLocation(bestprovider);
		if (lastknownlocation != null) {
			longitudeE6 = lastknownlocation.getLongitude() * 1e6;
			latitudeE6 = lastknownlocation.getLatitude() * 1e6;

			Geocoder geocoder = new Geocoder(this);
			List<Address> addresses;
			try {
				addresses = geocoder.getFromLocation(
						lastknownlocation.getLatitude(),
						lastknownlocation.getLongitude(), 1);

				for (Address address : addresses) {
					String Country = address.getCountryName();
					// locationdetails +=
					// "Address is "+address.getAddressLine(0)+" ,"+address.getCountryName();
					Log.e("", "longitudeE6 : " + longitudeE6 + "latitudeE6 : "
							+ latitudeE6 + "Country : " + Country);
					mMyCityAutocomplete.setText(Country);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String countryCode = tm.getSimCountryIso();
			String countryname = getCountryName(countryCode);
			mMyCityAutocomplete.setText(countryname);

		}
		mProfilePic = (ImageView) findViewById(R.id.profile_pic);

		mLin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mUsername.getWindowToken(), 0);
			}
		});
		mProfilePic.setOnClickListener(this);
		mSignUp.setOnClickListener(this);

		mSignUp.setTypeface(mActionBarTypeface);
		mEmail.setTypeface(mActionBarTypeface);
		mPassword.setTypeface(mActionBarTypeface);
		mUsername.setTypeface(mActionBarTypeface);
		mMyCityAutocomplete.setTypeface(mActionBarTypeface);

		if (getIntent().getStringExtra("flag") != null) {
			if (getIntent().getStringExtra("flag").equals("1")) {
				mGoogleName = getIntent().getStringExtra("personName");
				mGooglePhotoUrl = getIntent().getStringExtra("personPhotoUrl");
				mGoogleEmail = getIntent().getStringExtra("email");
				mGoogleLocation = getIntent().getStringExtra("location");

				if (mGoogleName != null) {
					mUsername.setText(mGoogleName);
				}
				if (mGoogleEmail != null) {
					mEmail.setText(mGoogleEmail);
				}
				if (mGoogleLocation != null) {
					mMyCity.setText(mGoogleLocation);
				}
				if (!mGooglePhotoUrl.equalsIgnoreCase("null")) {
					new LoadProfileImage(mProfilePic).execute(mGooglePhotoUrl);
				}
			}

			if (getIntent().getStringExtra("flag").equals("2")) {
				mGoogleName = getIntent().getStringExtra("personName");
				mGooglePhotoUrl = getIntent().getStringExtra("personPhotoUrl");

				if (mGoogleName != null) {
					mUsername.setText(mGoogleName);
				}

				if (!mGooglePhotoUrl.equalsIgnoreCase("null")) {
					new LoadProfileImage(mProfilePic).execute(mGooglePhotoUrl);
				}
			}
		}
		// if (checkInternetConnection()) {
		// ProgressDialog progress = new ProgressDialog(SignUpActivity.this);
		// progress.setMessage("Please wait..");
		// new GCMTask(progress).execute();
		// }
		mMyCityAutocomplete.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mMyCityAutocomplete.length() >= 1) {
					// if (checkInternetConnection()) {
					// new RetriveLocationListTask().execute(mMyCityAutocomplete
					// .getText().toString());
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
		mMyCityAutocomplete
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									mUsername.getWindowToken(), 0);

							return true;
						}
						return false;
					}
				});

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

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
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
			mMyCityAutocomplete.setAdapter(adapter);

			// adapter.notifyDataSetChanged();
		}
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
				Log.e("", "result : " + result);

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
								SignUpActivity.this, listWithoutDuplicates1);
						mMyCityAutocomplete.setAdapter(adapter);
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
			params.add(new BasicNameValuePair("param", mMyCityAutocomplete
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

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(getCroppedBitmap(result));
			BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					.getDrawable();
			Bitmap bmap = drawable.getBitmap();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmap.compress(CompressFormat.PNG, 100, bos);
			bb = bos.toByteArray();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_up:
			signup();
			break;
		case R.id.profile_pic:
			openGalleryOrCamera();
			break;
		default:
			break;
		}
	}

	private void openGalleryOrCamera() {
		dialog = new Dialog(SignUpActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mProfilePic.getBottom() + 10;
		Button camera = (Button) dialog.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent cameraIntent = new Intent(
				// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(cameraIntent, CAMERA_REQUEST);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				Uri fileUri = getOutputMediaFileUri(); // create a file
														// to save the
														// image
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set
																	// the
																	// image
																	// file
																	// name

				startActivityForResult(intent, CAMERA_ACTIVITY);
			}
		});
		Button gallery = (Button) dialog.findViewById(R.id.gallery);
		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent i = new Intent(
				// Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//
				// startActivityForResult(i, RESULT_LOAD_IMAGE);

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

	private void signup() {
		if (mUsername.length() == 0 && mEmail.length() == 0
				&& mPassword.length() == 0) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter all the details")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mUsername.setFocusableInTouchMode(true);
									mUsername.requestFocus();
									mUsername.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mUsername.length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter Username")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mUsername.setFocusableInTouchMode(true);
									mUsername.requestFocus();
									mUsername.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mEmail.length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter Email")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mEmail.setFocusableInTouchMode(true);
									mEmail.requestFocus();
									mEmail.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mPassword.length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter Password")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mPassword.setFocusableInTouchMode(true);
									mPassword.requestFocus();
									mPassword.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else {
			if (isValidEmail(mEmail.getText().toString())) {
				if (checkInternetConnection()) {
					ProgressDialog progress = new ProgressDialog(
							SignUpActivity.this);
					progress.setMessage("Signing up..");
					int corePoolSize = 60;
					int maximumPoolSize = 80;
					int keepAliveTime = 10;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
							maximumPoolSize);
					Executor threadPoolExecutor = new ThreadPoolExecutor(
							corePoolSize, maximumPoolSize, keepAliveTime,
							TimeUnit.SECONDS, workQueue);
					new RegisterTask(progress)
							.executeOnExecutor(threadPoolExecutor);
				}
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				alertDialogBuilder
						.setTitle("Warning !")
						.setIcon(R.drawable.alert)
						.setMessage("Please enter valid Email")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										mEmail.setFocusableInTouchMode(true);
										mEmail.requestFocus();
										mEmail.setHintTextColor(Color.RED);
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}

	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public class RegisterTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public RegisterTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = registerUserToWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			Log.e("", "result : " + result);
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SignUpActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								signup();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				try {
					Log.e("", "result :" + result);
					JSONObject responseObj = new JSONObject(result);
					JSONArray ja = responseObj.getJSONArray("data");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						if (jo.getString("response_msg").contains(
								"Login successfully")) {

							setPreferences(LoginActivity.PREFS_NAME);

							SharedPreferences prefs = getSharedPreferences(
									SplashActivity.PREFS_UID,
									Context.MODE_PRIVATE);
							prefs.edit()
									.putString(SplashActivity.PREFS_UID,
											jo.getString("uid")).commit();

							startActivity(new Intent(SignUpActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
							finish();
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									SignUpActivity.this);
							alertDialogBuilder
									.setMessage(jo.getString("response_msg"))
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder
									.create();
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
	}

	private void setPreferences(String prefsName) {
		SharedPreferences settings = getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(LoginActivity.PREFS_LOGIN, "SUCCESS");
		editor.commit();
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
						SignUpActivity.this);
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

	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras() != null) {
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					// we're connected
					try {
						initializeComponents();
					} catch (Exception e) {
					}
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkReceiver);
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

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (dialog != null) {
	// dialog.dismiss();
	// }
	// try {
	// if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
	// && null != data) {
	// Uri selectedImage = data.getData();
	// if (Build.VERSION.SDK_INT >= 19) {
	// String imguri = selectedImage.toString();
	// if (imguri.substring(0, 21).equals("content://com.android")) {
	// String[] photo_split = imguri.split("%3A");
	// imguri = "content://media/external/images/media/"
	// + photo_split[1];
	// }
	// selectedImage = Uri.parse(imguri);
	// }
	//
	// String[] filePathColumn = { MediaStore.Images.Media.DATA };
	//
	// Cursor cursor = getContentResolver().query(selectedImage,
	// filePathColumn, null, null, null);
	// cursor.moveToFirst();
	//
	// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	// String picturePath = cursor.getString(columnIndex);
	// cursor.close();
	// Log.e("", "picturePath : " + picturePath);
	// // mProfilePic.setImageBitmap(BitmapFactory
	// // .decodeFile(picturePath));
	// mProfilePic.setImageBitmap(getCroppedBitmap(BitmapFactory
	// .decodeFile(picturePath)));
	// } else if (requestCode == CAMERA_ACTIVITY
	// && resultCode == RESULT_OK) {
	// // Bitmap photo = (Bitmap) data.getExtras().get("data");
	//
	// imageFilePath = getOutputMediaFileUri().getPath();
	// // bitmap = BitmapFactory.decodeFile(imageFilePath);
	// Display mDisplay = getWindowManager().getDefaultDisplay();
	// int width1 = mDisplay.getWidth();
	//
	// int height1 = mDisplay.getHeight();
	//
	// // bm = decodeScaledBitmapFromSdCard(imageFilePath, width1,
	// // height1);
	// bm = BitmapFactory.decodeFile(imageFilePath);
	//
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// Matrix matrix = new Matrix();
	//
	// // matrix.postRotate(90);
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix, true);
	//
	// ExifInterface ei = new ExifInterface(imageFilePath);
	// int orientation = ei.getAttributeInt(
	// ExifInterface.TAG_ORIENTATION,
	// ExifInterface.ORIENTATION_NORMAL);
	//
	// switch (orientation) {
	// case ExifInterface.ORIENTATION_ROTATE_90:
	// Matrix matrix2 = new Matrix();
	//
	// matrix2.postRotate(90);
	//
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix2, true);
	//
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_180:
	// Matrix matrix4 = new Matrix();
	// matrix4.postRotate(180);
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix4, true);
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_270:
	// Matrix matrix3 = new Matrix();
	// matrix3.postRotate(270);
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix3, true);
	// break;
	// case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
	// Matrix matrix6 = new Matrix();
	// matrix6.preScale(-1.0f, 1.0f);
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix6, true);
	// break;
	// case ExifInterface.ORIENTATION_FLIP_VERTICAL:
	// Matrix matrix7 = new Matrix();
	// matrix7.preScale(1.0f, -1.0f);
	// // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
	// // bm.getHeight(), true);
	//
	// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
	// bm.getHeight(), matrix7, true);
	// break;
	// // etc.
	// }
	// mProfilePic.setImageBitmap(getCroppedBitmap(bm));
	// }
	//
	// BitmapDrawable drawable = (BitmapDrawable) mProfilePic
	// .getDrawable();
	// Bitmap bmap = drawable.getBitmap();
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// bmap.compress(CompressFormat.PNG, 100, bos);
	// bb = bos.toByteArray();
	// } catch (Exception e) {
	// }
	// }

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
					mProfilePic.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mProfilePic.setImageBitmap(getCroppedBitmap(photo));
					}
				}

			} else if (requestCode == CAMERA_ACTIVITY
					&& resultCode == RESULT_OK) {
				if (Build.VERSION.SDK_INT >= 19) {
					imageFilePath = getOutputMediaFileUri().getPath();
					bm = BitmapFactory.decodeFile(imageFilePath);
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
					mProfilePic.setImageBitmap(getCroppedBitmap(bm));
					BitmapDrawable drawable = (BitmapDrawable) mProfilePic
							.getDrawable();
					Bitmap bmap = drawable.getBitmap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bmap.compress(CompressFormat.PNG, 100, bos);
					bb = bos.toByteArray();
				} else {
					picUri = getOutputMediaFileUri();
					performCrop();
				}

			}
			if (requestCode == PIC_CROP && resultCode == RESULT_OK) { 
				if (Build.VERSION.SDK_INT >= 19) {
					Uri selectedImage = data.getData();
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
							.decodeFile(picturePath)));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");
					mProfilePic.setImageBitmap(getCroppedBitmap(thePic));
					BitmapDrawable drawable = (BitmapDrawable) mProfilePic
							.getDrawable();
					Bitmap bmap = drawable.getBitmap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bmap.compress(CompressFormat.PNG, 100, bos);
					bb = bos.toByteArray();
				}
			}
		} catch (Exception e) {
		}
	}

	private void performCrop() {
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
	}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

	private String registerUserToWeb() {
		Log.e("", " bb :" + bb);
		String postURL = "http://54.149.99.130/ws/signup.php?username=sfssss&password=&usertype=&email=&city=&country=&token=aa";
		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", mUsername.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", mPassword.getText()
					.toString()));
			params.add(new BasicNameValuePair("email", mEmail.getText()
					.toString()));
			// params.add(new BasicNameValuePair("country", mMyCity.getText()
			// .toString()));
			params.add(new BasicNameValuePair("city", mMyCityAutocomplete
					.getText().toString()));
			params.add(new BasicNameValuePair("usertype", ""));
			params.add(new BasicNameValuePair("token", mRegID));
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

	@Override
	public void onLocationChanged(Location loc) {
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
