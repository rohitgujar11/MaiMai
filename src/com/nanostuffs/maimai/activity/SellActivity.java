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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.PlaceJSONParser;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.CountrySimpleAdapter;
import com.nanostuffs.maimai.adapter.LocationsAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.adapter.SpinnerCategoryAdapter;
import com.nanostuffs.maimai.model.Category;
import com.nanostuffs.maimai.model.SearchItem;

@SuppressWarnings("unused")
public class SellActivity extends Activity {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private static final int WIDTH = 80;
	private static final int HEIGHT = 80;
	private static final int RADIUS = 13;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private EditText mItem;
	private EditText mDescription;
	private TextView mMaimaiNetworkText;
	private Button mDone;
	private CheckBox mCheckBoxNetwork;
	private EditText mPrice;
	private ArrayList<Category> mCategory;
	private Spinner mCategorySpinner;
	private ImageButton mSellImage;
	private Dialog dialog;
	private Dialog dialog1;
	private AlertDialog alert;
	private int mTotalBytes;

	private String imageFilePath;
	private Bitmap bm;
	private static ImageView mDownArrowImg;
	// protected static final int CAMERA_ACTIVITY = 0;
	// protected static final int RESULT_LOAD_IMAGE = 1;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_GALLERY = 2;
	private static final int PICK_FROM_CAMERA1 = 3;
	private static final int PICK_FROM_GALLERY1 = 4;
	private static final int PICK_FROM_CAMERA2 = 5;
	private static final int PICK_FROM_GALLERY2 = 6;
	private static final int PICK_FROM_CAMERA3 = 7;
	private static final int PICK_FROM_GALLERY3 = 8;
	private static final int PICK_FROM_CAMERA4 = 19;
	private static final int PICK_FROM_GALLERY4 = 20;
	private static final int PICK_FROM_CAMERA5 = 21;
	private static final int PICK_FROM_GALLERY5 = 22;
	private static final int PICK_FROM_CAMERA6 = 23;
	private static final int PICK_FROM_GALLERY6 = 24;
	private static final int PICK_FROM_CAMERA7 = 28;
	private static final int PICK_FROM_GALLERY7 = 29;
	private static final int PICK_FROM_CAMERA8 = 30;
	private static final int PICK_FROM_GALLERY8 = 31;
	private static final int PICK_FROM_CAMERA9 = 32;
	private static final int PICK_FROM_GALLERY9 = 33;
	private static final int PIC_CROP = 9;
	private static final int PIC_CROP1 = 10;
	private static final int PIC_CROP2 = 11;
	private static final int PIC_CROP3 = 12;
	private static final int PIC_CROP4 = 34;
	private static final int PIC_CROP5 = 35;
	private static final int PIC_CROP6 = 36;
	private static final int PIC_CROP7 = 37;
	private static final int PIC_CROP8 = 39;
	private static final int PIC_CROP9 = 40;
	private static final int PICK_FROM_GALLERY_VIDEO1 = 13;
	private static final int PICK_FROM_GALLERY_VIDEO2 = 14;
	private static final int PICK_FROM_GALLERY_VIDEO3 = 15;
	private static final int PICK_FROM_CAMERA_VIDEO1 = 16;
	private static final int PICK_FROM_CAMERA_VIDEO2 = 17;
	private static final int PICK_FROM_CAMERA_VIDEO3 = 18;
	private static final int PICK_FROM_GALLERY_VIDEO4 = 41;
	private static final int PICK_FROM_GALLERY_VIDEO5 = 42;
	private static final int PICK_FROM_GALLERY_VIDEO6 = 43;
	private static final int PICK_FROM_CAMERA_VIDEO4 = 44;
	private static final int PICK_FROM_CAMERA_VIDEO5 = 45;
	private static final int PICK_FROM_CAMERA_VIDEO6 = 46;
	private static final int PICK_FROM_GALLERY_VIDEO7 = 47;
	private static final int PICK_FROM_GALLERY_VIDEO8 = 48;
	private static final int PICK_FROM_GALLERY_VIDEO9 = 49;
	private static final int PICK_FROM_CAMERA_VIDEO7 = 50;
	private static final int PICK_FROM_CAMERA_VIDEO8 = 51;
	private static final int PICK_FROM_CAMERA_VIDEO9 = 52;
	private static final int MEDIA_TYPE_VIDEO = 53;
	private Uri picUri;
	private RelativeLayout mLinear;
	private ImageView imageView;
	private int addNew;
	private ImageButton mSellImage1;
	private ImageButton mSellImage2;
	private ImageButton mSellImage3;
	private RelativeLayout mRel;
	private String response;
	private byte[] bb1;
	private byte[] bb2;
	private byte[] bb3;
	private byte[] bb4;
	private int width;
	private int height;
	private ImageButton mNavMenu;
	private int radius;
	private ImageButton mSellVideo1;
	private String videoPath = "";
	private MediaController mediaController;
	private String videoPath1;
	private ImageButton mCrossImage;
	private ImageButton mCrossImage1;
	private ImageButton mCrossImage2;
	private ImageButton mCrossImage3;
	private Dialog dialog2;
	private Bitmap mainBitmap;
	private Dialog dialog3;
	private ImageButton mSellVideo2;
	private ImageButton mSellVideo3;
	private Bitmap mainBitmap1;
	private Bitmap mainBitmap2;
	private Bitmap mainBitmap3;
	private Bitmap mainBitmap4;
	private Bitmap mainBitmap5;
	private Bitmap mainBitmap6;
	private Bitmap mainBitmap7;
	private Bitmap mainBitmap8;
	private Bitmap mainBitmap9;
	private Bitmap mainBitmap10;
	private Bitmap mainBitmap11;
	private Bitmap mainBitmap12;
	private Bitmap mainBitmap13;
	private Bitmap mainBitmap14;
	private Bitmap mainBitmap15;
	private Bitmap mainBitmap16;
	private Bitmap mainBitmap17;
	private Bitmap mainBitmap18;
	private ImageButton mCrossImage4;
	private ImageButton mCrossImage5;
	private ImageButton mCrossImage6;
	private ImageButton mCrossImage7;
	private ImageButton mCrossImage8;
	private ImageButton mCrossImage9;
	private ImageButton mSellImage4;
	private ImageButton mSellVideo4;
	private ImageButton mSellImage5;
	private ImageButton mSellVideo5;
	private ImageButton mSellImage6;
	private ImageButton mSellVideo6;
	private ImageButton mSellImage7;
	private ImageButton mSellVideo7;
	private ImageButton mSellImage8;
	private ImageButton mSellVideo8;
	private ImageButton mSellImage9;
	private ImageButton mSellVideo9;
	private byte[] byteArr0;
	private byte[] byteArr1;
	private byte[] byteArr2;
	private byte[] byteArr3;
	private byte[] byteArr4;
	private byte[] byteArr5;
	private byte[] byteArr6;
	private byte[] byteArr7;
	private byte[] byteArr8;
	private byte[] byteArr9;
	private byte[] byteArr10;
	private byte[] byteArr11;
	private byte[] byteArr12;
	private byte[] byteArr13;
	private byte[] byteArr14;
	private byte[] byteArr15;
	private byte[] byteArr16;
	private byte[] byteArr17;
	private byte[] byteArr18;
	private byte[] byteArr19;
	private int mImageCount = 0;
	private String mCatName;
	private String itemimg0;
	private String itemimg1;
	private String itemimg2;
	private String itemimg3;
	private String itemimg4;
	private String itemimg5;
	private String itemimg6;
	private String itemimg7;
	private String itemimg8;
	private String itemimg9;
	private String itemimg10;
	private String itemimg11;
	private String itemimg12;
	private String itemimg13;
	private String itemimg14;
	private String itemimg15;
	private String itemimg16;
	private String itemimg17;
	private String itemimg18;
	private String itemimg19;
	private String mExtension;
	private String mExtension9;
	private String mExtension8;
	private String mExtension7;
	private String mExtension6;
	private String mExtension5;
	private String mExtension4;
	private String mExtension3;
	private String mExtension2;
	private String mExtension1;
	private String mExtension0;
	private String mExtension19;
	private String mExtension18;
	private String mExtension17;
	private String mExtension16;
	private String mExtension15;
	private String mExtension14;
	private String mExtension13;
	private String mExtension12;
	private String mExtension11;
	private String mExtension10;
	public AsyncTask<String, Integer, String> mSelltask;
	private Session session;
	private String access_token;
	private String link;
	private AutoCompleteTextView actionBarSearch;
	private SearchItem mSItem;
	private ArrayList<SearchItem> mItemList;
	private AutoCompleteTextView mCity;
	private Dialog custom_dialog1;
	private Handler mHandler = new Handler();
	private Handler mHandler1 = new Handler();
	private long mStartRX = 0;
	private long mStartTX = 0;
	private String mUploadSpeed;
	private long mUploadSpeedLong;
	private Typeface mSymbolTypeface;
	private String mFileSize;
	private long mTime;
	private static final int EXPECTED_SIZE_IN_BYTES = 1048576; // 1MB 1024*1024
	private static final double EDGE_THRESHOLD = 176.0;
	private static final double BYTE_TO_KILOBIT = 0.0078125;
	private static final double KILOBIT_TO_MEGABIT = 0.0009765625;
	private TextView percentage;
	private int count = 0;
	private Typeface mActionBarTypeface1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell);
		actionBarDetails();
		try {
			initializeComponents();
			getCategories();
		} catch (Exception e) {
		}

		// To calculate upload and download speed of internet
		// mStartRX = TrafficStats.getTotalRxBytes();
		// mStartTX = TrafficStats.getTotalTxBytes();
		// if (mStartRX == TrafficStats.UNSUPPORTED
		// || mStartTX == TrafficStats.UNSUPPORTED) {
		// AlertDialog.Builder alert = new AlertDialog.Builder(this);
		// alert.setTitle("Uh Oh!");
		// alert.setMessage("Your device does not support traffic stat monitoring.");
		// alert.show();
		// } else {
		// mHandler.postDelayed(mRunnable, 1000);
		// }
	}

	private SpeedInfo calculate(final long downloadTime, final long bytesIn) {
		SpeedInfo info = new SpeedInfo();
		// from mil to sec
		long bytespersecond = (bytesIn / downloadTime) * 1000;
		double kilobits = bytespersecond * BYTE_TO_KILOBIT;
		double megabits = kilobits * KILOBIT_TO_MEGABIT;
		info.downspeed = bytespersecond;
		info.kilobits = kilobits;
		info.megabits = megabits;

		return info;
	}

	private static class SpeedInfo {
		public double kilobits = 0;
		public double megabits = 0;
		public double downspeed = 0;
	}

	private final Runnable mRunnable = new Runnable() {

		public void run() {

			long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
			long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
			// Log.e("", "aaaaaa mUploadSpeed: " + getFileSize(txBytes));
			mUploadSpeedLong = txBytes;
			mUploadSpeed = getFileSize(txBytes);
			mHandler.postDelayed(mRunnable, 1000);
		}
	};
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;
	private TextView textView8;
	private TextView textView9;
	private TextView textView10;
	private TextView mDollar;

	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1024, digitGroups))
				+ " " + units[digitGroups];
	}

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
		// showCustomToast(string);
	}

	private void getCategories() {
		mCategory = new ArrayList<Category>();
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new RetriveCategoriesTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	public class RetriveCategoriesTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private Category category;
		private Category category1;

		public RetriveCategoriesTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = getCategoriesFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			// try {
			// progress.dismiss();
			// } catch (Exception e) {
			// }

			Log.e("test", "result  : " + result);
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SellActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getCategories();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				try {
					JSONObject responseObj = new JSONObject(result);

					JSONArray ja = responseObj.getJSONArray("data");
					category1 = new Category();
					category1.setCategoryName("Pick a Category");
					if (!mCategory.contains(category1)) {
						mCategory.add(0, category1);
					}

					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						category = new Category();
						category.setCategoryName(jo.getString("Name"));
						if (!mCategory.contains(category)) {
							mCategory.add(category);
						}

					}
					if (mCategory.size() != 0) {
						final SpinnerCategoryAdapter adapter = new SpinnerCategoryAdapter(
								SellActivity.this,
								R.layout.category_spinner_listitem, mCategory);
						mCategorySpinner.setAdapter(adapter);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private String getCategoriesFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/get_category.php?flag=all&userid=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mUIDStr));
			params.add(new BasicNameValuePair("flag", ""));
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

	private void initializeComponents() {
		mItemList = new ArrayList<SearchItem>();
		int density = getResources().getDisplayMetrics().densityDpi;

		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			width = (int) (WIDTH * 0.75);
			height = (int) (HEIGHT * 0.75);
			radius = (int) (RADIUS * 0.75);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			width = WIDTH;
			height = HEIGHT;
			radius = RADIUS;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			width = (int) (WIDTH * 1.5);
			height = (int) (HEIGHT * 1.5);
			radius = (int) (RADIUS * 1.5);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			width = WIDTH * 2;
			height = HEIGHT * 2;
			radius = (int) (RADIUS * 2);
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			width = WIDTH * 3;
			height = HEIGHT * 3;
			radius = (int) (RADIUS * 3);
			break;
		// case DisplayMetrics.DENSITY_XXXHIGH:
		// width = WIDTH * 5;
		// height = HEIGHT * 5;
		// radius = (int) (RADIUS * 5);
		// break;
		}
		mRel = (RelativeLayout) findViewById(R.id.rel);
		mLinear = (RelativeLayout) findViewById(R.id.lin);
		mItem = (EditText) findViewById(R.id.item_edit);
		mRel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mItem.getWindowToken(), 0);
			}
		});
		mItem.setTypeface(mActionBarTypeface);
		mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
		mDownArrowImg = (ImageView) findViewById(R.id.down_arrow_img);
		mDescription = (EditText) findViewById(R.id.desc_edit);
		mDescription.setTypeface(mActionBarTypeface);
		mPrice = (EditText) findViewById(R.id.price_edit);
		mPrice.setTypeface(mActionBarTypeface);
		mDollar = (TextView) findViewById(R.id.dollar_sign);
		mDollar.setTypeface(mActionBarTypeface);
		mCity = (AutoCompleteTextView) findViewById(R.id.city_autocomplete);
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

		mCheckBoxNetwork = (CheckBox) findViewById(R.id.checkbox_network);
		mMaimaiNetworkText = (TextView) findViewById(R.id.mai_mai_network);
		mMaimaiNetworkText.setTypeface(mActionBarTypeface);
		mMaimaiNetworkText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCheckBoxNetwork.isChecked()) {
					mCheckBoxNetwork.setChecked(false);
				} else {
					mCheckBoxNetwork.setChecked(true);
				}
			}
		});
		mDone = (Button) findViewById(R.id.done);
		mDone.setTypeface(mActionBarTypeface);
		mCrossImage = (ImageButton) findViewById(R.id.cross_img);
		mCrossImage1 = (ImageButton) findViewById(R.id.cross_img1);
		mCrossImage2 = (ImageButton) findViewById(R.id.cross_img2);
		mCrossImage3 = (ImageButton) findViewById(R.id.cross_img3);
		mCrossImage4 = (ImageButton) findViewById(R.id.cross_img4);
		mCrossImage5 = (ImageButton) findViewById(R.id.cross_img5);
		mCrossImage6 = (ImageButton) findViewById(R.id.cross_img6);
		mCrossImage7 = (ImageButton) findViewById(R.id.cross_img7);
		mCrossImage8 = (ImageButton) findViewById(R.id.cross_img8);
		mCrossImage9 = (ImageButton) findViewById(R.id.cross_img9);

		mSellImage = (ImageButton) findViewById(R.id.sell_image);
		mSellImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage.getDrawable() == null) {
					openGalleryOrCamera();
				} else {
					viewImage(mainBitmap);
				}
			}
		});
		mSellImage1 = (ImageButton) findViewById(R.id.sell_image1);
		mSellVideo1 = (ImageButton) findViewById(R.id.sell_video1);
		mSellImage1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage1.getDrawable() == null) {
					openGalleryOrCamera1();
				} else {
					viewImage(mainBitmap1);
				}
			}

		});
		mSellImage2 = (ImageButton) findViewById(R.id.sell_image2);
		mSellVideo2 = (ImageButton) findViewById(R.id.sell_video2);
		mSellImage2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage2.getDrawable() == null) {
					openGalleryOrCamera2();
				} else {
					viewImage(mainBitmap2);
				}
			}
		});
		mSellImage3 = (ImageButton) findViewById(R.id.sell_image3);
		mSellVideo3 = (ImageButton) findViewById(R.id.sell_video3);
		mSellImage3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage3.getDrawable() == null) {
					openGalleryOrCamera3();
				} else {
					viewImage(mainBitmap3);
				}
			}
		});
		mSellImage4 = (ImageButton) findViewById(R.id.sell_image4);
		mSellVideo4 = (ImageButton) findViewById(R.id.sell_video4);
		mSellImage4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage4.getDrawable() == null) {
					openGalleryOrCamera4();
				} else {
					viewImage(mainBitmap4);
				}
			}
		});
		mSellImage5 = (ImageButton) findViewById(R.id.sell_image5);
		mSellVideo5 = (ImageButton) findViewById(R.id.sell_video5);
		mSellImage5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage5.getDrawable() == null) {
					openGalleryOrCamera5();
				} else {
					viewImage(mainBitmap5);
				}
			}
		});
		mSellImage6 = (ImageButton) findViewById(R.id.sell_image6);
		mSellVideo6 = (ImageButton) findViewById(R.id.sell_video6);
		mSellImage6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage6.getDrawable() == null) {
					openGalleryOrCamera6();
				} else {
					viewImage(mainBitmap6);
				}
			}
		});
		mSellImage7 = (ImageButton) findViewById(R.id.sell_image7);
		mSellVideo7 = (ImageButton) findViewById(R.id.sell_video7);
		mSellImage7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage7.getDrawable() == null) {
					openGalleryOrCamera7();
				} else {
					viewImage(mainBitmap7);
				}
			}
		});
		mSellImage8 = (ImageButton) findViewById(R.id.sell_image8);
		mSellVideo8 = (ImageButton) findViewById(R.id.sell_video8);
		mSellImage8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage8.getDrawable() == null) {
					openGalleryOrCamera8();
				} else {
					viewImage(mainBitmap8);
				}
			}
		});
		mSellImage9 = (ImageButton) findViewById(R.id.sell_image9);
		mSellVideo9 = (ImageButton) findViewById(R.id.sell_video9);
		mSellImage9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSellImage9.getDrawable() == null) {
					openGalleryOrCamera9();
				} else {
					viewImage(mainBitmap9);
				}
			}
		});
		mCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						String name = mCategorySpinner.getSelectedItem()
								.toString();
						String arr[] = name.split("=");
						for (int i = 0; i < arr.length; i++) {
							Log.e("", "i ======" + arr[i]);
						}
						String arr1[] = arr[3].split(",");
						mCatName = arr1[0].trim();
						Log.e("", "mCatName 1 :" + mCatName);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		mDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				done();
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
			mCity.setAdapter(adapter);

			// adapter.notifyDataSetChanged();
		}
	}

	private void done() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mItem.getWindowToken(), 0);
		Log.e("", "mCatName  2:" + mCatName);
		if (mCatName.equalsIgnoreCase("null")
				|| mCatName.equalsIgnoreCase("Pick a Category")
				&& mItem.getText().toString().trim().length() == 0
				&& mDescription.getText().toString().trim().length() == 0
				&& mPrice.getText().toString().trim().length() == 0
				&& mCity.getText().toString().trim().length() == 0) {
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
									// mEmail.setFocusableInTouchMode(true);
									// mEmail.requestFocus();
									// mEmail.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mCatName.equalsIgnoreCase("Pick a Category")) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please select a category")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									// mEmail.setFocusableInTouchMode(true);
									// mEmail.requestFocus();
									// mEmail.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mItem.getText().toString().trim().length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter item")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mItem.setFocusableInTouchMode(true);
									mItem.requestFocus();
									mItem.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mDescription.getText().toString().trim().length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter description")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mDescription.setFocusableInTouchMode(true);
									mDescription.requestFocus();
									mDescription.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mPrice.getText().toString().trim().length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter price")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mPrice.setFocusableInTouchMode(true);
									mPrice.requestFocus();
									mPrice.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mCity.getText().toString().trim().length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter location")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mCity.setFocusableInTouchMode(true);
									mCity.requestFocus();
									mCity.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mainBitmap == null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please upload atleast 1 image at the first position")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();

								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else {
			mDone.setBackgroundResource(R.drawable.btn_login_p);
			mDone.setEnabled(false);

			if (checkInternetConnection()) {
				// createCustomDialog();

				// ProgressDialog progress = new
				// ProgressDialog(SellActivity.this);
				// progress.setMessage("Uploading your post ...");
				// progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				// progress.setIndeterminate(false);
				// progress.setProgress(0);
				// progress.setMax(100);
				custom_dialog1 = new Dialog(SellActivity.this);
				custom_dialog1.getWindow().requestFeature(
						Window.FEATURE_NO_TITLE);
				custom_dialog1.setContentView(getLayoutInflater().inflate(
						R.layout.progressdialog_custom, null));

				percentage = (TextView) custom_dialog1
						.findViewById(R.id.percentage);
				textView1 = (TextView) custom_dialog1
						.findViewById(R.id.textView1);
				textView2 = (TextView) custom_dialog1
						.findViewById(R.id.textView2);
				textView3 = (TextView) custom_dialog1
						.findViewById(R.id.textView3);
				textView4 = (TextView) custom_dialog1
						.findViewById(R.id.textView4);
				textView5 = (TextView) custom_dialog1
						.findViewById(R.id.textView5);
				textView6 = (TextView) custom_dialog1
						.findViewById(R.id.textView6);
				textView7 = (TextView) custom_dialog1
						.findViewById(R.id.textView7);
				textView8 = (TextView) custom_dialog1
						.findViewById(R.id.textView8);
				textView9 = (TextView) custom_dialog1
						.findViewById(R.id.textView9);
				textView10 = (TextView) custom_dialog1
						.findViewById(R.id.textView10);

				textView4.setTypeface(mSymbolTypeface);
				textView9.setTypeface(mSymbolTypeface);

				textView1.setTypeface(mActionBarTypeface);
				textView2.setTypeface(mActionBarTypeface);
				textView3.setTypeface(mActionBarTypeface1);
				textView5.setTypeface(mActionBarTypeface);
				textView6.setTypeface(mActionBarTypeface1);
				textView10.setTypeface(mActionBarTypeface);
				textView8.setTypeface(mActionBarTypeface1);

				// mHandler1.postDelayed(mRunnable1, 1000);
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				mSelltask = new SellItemTask(custom_dialog1)
						.executeOnExecutor(threadPoolExecutor);
			}
		}
	}

	private void createCustomDialog() {

	}

	private void shareOnFacebook(String itemid) {
		if (findFacebookClient(getApplicationContext())) {
			link = "http://54.149.99.130/ws/fb_sharing.php?itemid=" + itemid;
			Log.e("", "link : " + link);
			postToWall(link);
			// Intent shareIntent = new
			// Intent(android.content.Intent.ACTION_SEND);
			// shareIntent.setType("text/plain");
			// shareIntent
			// .putExtra(android.content.Intent.EXTRA_TEXT, link.trim());
			// //
			// shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,catname);
			// PackageManager pm = this.getPackageManager();
			// List<ResolveInfo> activityList = pm.queryIntentActivities(
			// shareIntent, 0);
			// for (final ResolveInfo app : activityList) {
			// if ((app.activityInfo.name).contains("facebook")) {
			// final ActivityInfo activity = app.activityInfo;
			// final ComponentName name = new ComponentName(
			// activity.applicationInfo.packageName, activity.name);
			// shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			// shareIntent.setComponent(name);
			// this.startActivity(shareIntent);
			// break;
			// }
			// }

		}

		else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SellActivity.this);
			alertDialogBuilder
					.setMessage(
							"Sorry you need to have Facebook app installed your device to Share")
					.setTitle("Error")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

	}

	private void postToWall(String link) {
		try {
			Log.e("", "postToWall");
			session = Session.getActiveSession();
			if (session == null) {
				// Log.e("", "aaaaaaa");
				session = new Session(SellActivity.this);
				Session.setActiveSession(session);
				if (session.getState()
						.equals(SessionState.CREATED_TOKEN_LOADED)) {
					Log.e("", "bbbbbbb");
					session.openForRead(new Session.OpenRequest(
							SellActivity.this).setCallback(statusCallback));
					access_token = session.getAccessToken();
				}
			}
			if (!session.isOpened() && !session.isClosed()) {
				Log.e("", "cccccccc");
				session.openForRead(new Session.OpenRequest(SellActivity.this)
						.setCallback(statusCallback));
				access_token = session.getAccessToken();
			} else {
				Log.e("", "dddddd");
				Session.openActiveSession(SellActivity.this, true,
						statusCallback);
			}

		} catch (Exception ex) {
			Log.e("", "Exception : " + ex);
		}

	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {

		@SuppressWarnings("deprecation")
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.e("", "statusCallback");
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
										Log.e("", "111111111111");
										new MyAsyncTask().execute(link);
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

								if (user != null) {
									user.getId();
									user.getUsername();
									Log.e("", "2222222222222");
									new MyAsyncTask().execute(link);
									// Log.e("", "Response : " + response);
									// Log.e("useried", user.getId());
									// Log.e("", "Name : " + user.getName());
									// Log.e("",
									// "Username : " + user.getUsername());
									// String profilePic =
									// " https://graph.facebook.com/"
									// + user.getId()
									// + "/picture?type=large";
									// Log.e("", "profilePic : " + profilePic);
									// // String email =
									// user.getProperty("email");
									//
									// // // String safeEmail = user.asMap()
									// // .get("email").toString();
									// Log.e("",
									// "email : "
									// + user.getProperty("email"));
									// Log.e("", "safeEmail : "
									// + user.asMap().get("email"));
									//
									// // Intent intent = new Intent(
									// // LoginActivity.this,
									// // SignUpActivity.class);
									// // intent.putExtra("flag", "2");
									// // intent.putExtra("personPhotoUrl",
									// // profilePic);
									// // intent.putExtra("personName",
									// // user.getName());
									// // startActivity(intent);
									// // overridePendingTransition(
									// // R.anim.slide_in_right,
									// // R.anim.slide_out_left);
									//
									// mFBPersonName = user.getName();
									// mFBPhotoURL = profilePic;
									// mFBEmail = user.getId();
									// // if
									// //
									// (!mFBPhotoURL.equalsIgnoreCase("null")) {
									// new LoadProfileImage(mProfilePic)
									// .execute(mFBPhotoURL);
									// // }
									// signupWithFB();
								}
							}

						});

			}
		}

	};

	class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
		private Facebook facebook;

		@SuppressWarnings("deprecation")
		public Boolean doInBackground(String... message) {
			Log.e("", "33333333333333");
			facebook = new Facebook("1381695872099156");
			Bundle parameters = new Bundle();
			parameters.putString("link", message[0]);
			// parameters.putString("message", message[0]);
			// parameters.putString("description", "topic share");
			try {
				facebook.request("me");
				Log.e("", "TESTTTTTTTTTTTTTTTT");
				String response = facebook.request(
						"758118237599718/feed?access_token=" + access_token,
						parameters, "POST");
				Log.e("Tests", "got response: " + response);
				if (response == null || response.equals("")
						|| response.equals("false")) {
					return Boolean.FALSE;
				} else {
					return Boolean.TRUE;
				}
			} catch (Exception e) {

				Log.e("", "errorrrrrrrrrr : " + e);
				return Boolean.FALSE;
			}
		}

		public void onPostExecute(Boolean result) {
			if (result == Boolean.TRUE) {
				// showToast("Posted on facebook successfully");
				showCustomToast("Posted on facebook successfully");
			} else {
				// showToast("Couldn't post to FB.");
				showCustomToast("Couldn't post on facebook");
			}
			finish();
		}

		private void showToast(String string) {
			Toast.makeText(SellActivity.this, "" + string, Toast.LENGTH_SHORT)
					.show();

		}
	}

	public static Boolean findFacebookClient(Context con) {
		final String[] FacebookApps = { "com.facebook.android",
				"com.facebook.katana" };
		Intent facebookIntent = new Intent();
		facebookIntent.setType("text/plain");
		final PackageManager packageManager = con.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				facebookIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < FacebookApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(FacebookApps[i])) {
					facebookIntent.setPackage(p);
					return true;
				}
			}
		}
		return false;
	}

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public class SellItemTask extends AsyncTask<String, Integer, String> {
		private Dialog progress1;
		private Typeface mActionBarTypeface1;
		private int count;

		public SellItemTask(Dialog progressDialog) {
			this.progress1 = progressDialog;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		public void onPreExecute() {
			if (progress1 != null) {
				progress1.show();
			}

			// setProgressBarIndeterminateVisibility(true);
			savePreferences("progress", "1");

		}

		@Override
		protected String doInBackground(String... urls) {
			// for (int progressValue = 0; progressValue < 100; progressValue++)
			// {
			// percentage.setText(progressValue + "%");
			// }

			String resultdata = sellItemToWeb();

			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress1.dismiss();
			} catch (Exception e) {
			}
			savePreferences("progress", "0");

			try {
				Log.e("", "result :" + result);

				JSONObject responseObj = new JSONObject(result);
				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg")
							.equalsIgnoreCase("Success")) {
						if (mCheckBoxNetwork.isChecked()) {
							shareOnFacebook(jo.getString("id"));
						}
						LayoutInflater inflater = getLayoutInflater();
						View layout = inflater
								.inflate(
										R.layout.toast,
										(ViewGroup) findViewById(R.id.toast_layout_root));
						mActionBarTypeface1 = Typeface.createFromAsset(
								getAssets(), "fonts/verdana_bold.ttf");
						ImageView image = (ImageView) layout
								.findViewById(R.id.image);
						TextView text1 = (TextView) layout
								.findViewById(R.id.textView1);
						TextView text2 = (TextView) layout
								.findViewById(R.id.textView2);
						TextView text3 = (TextView) layout
								.findViewById(R.id.textView3);
						text1.setTypeface(mActionBarTypeface1);
						text2.setTypeface(mActionBarTypeface);
						text3.setTypeface(mActionBarTypeface);

						Toast toast = new Toast(getApplicationContext());
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.setDuration(Toast.LENGTH_LONG);
						toast.setView(layout);
						toast.show();
						// Toast.makeText(SellActivity.this,
						// "Item sold successfully", Toast.LENGTH_SHORT)
						// .show();
						finish();
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								SellActivity.this);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String convertSecondToHHMMString(long mTime2) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		String time = df.format(new Date(mTime2 * 1000L));

		return time;

	}

	// private final Runnable mRunnable1 = new Runnable() {
	//
	// public void run() {
	//
	// long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
	// long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
	// Log.e("", "aaaaaa mUploadSpeed: " + getFileSize(txBytes));
	// mUploadSpeedLong = txBytes;
	// mUploadSpeed = getFileSize(txBytes);
	// mHandler.postDelayed(mRunnable, 1000);
	// percentage.setText(count + 10 + "%");
	// }
	// };
	private final Runnable mRunnable2 = new Runnable() {

		@Override
		public void run() {
			try {

				long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
				long txBytes = TrafficStats.getTotalTxBytes() - mStartTX - 50;
				Log.e("", "aaaaaa mFileSize: " + mFileSize);
				mUploadSpeedLong = txBytes;
				mUploadSpeed = getFileSize(txBytes);
				mHandler.postDelayed(mRunnable2, 1000);

				Log.e("", "4444444444 getFileSize(txBytes) : "
						+ getFileSize(txBytes) + " mFileSize : " + mFileSize);
				textView7.setText(getFileSize(txBytes) + "/"
						+ getFileSize(mTotalBytes * 2));
				WifiManager wifiManager = (WifiManager) SellActivity.this
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (wifiInfo != null) {
					long linkSpeed = wifiInfo.getLinkSpeed(); // measured using
																// WifiInfo.LINK_SPEED_UNITS
					textView10.setText(getFileSize(linkSpeed));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	private String sellItemToWeb() {
		// mHandler1.postDelayed(mRunnable1, 1000);
		// long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
		// long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
		// Log.e("", "aaaaaa mUploadSpeed: " + getFileSize(txBytes));
		// mUploadSpeedLong = txBytes;
		// mUploadSpeed = getFileSize(txBytes);
		// mHandler.postDelayed(mRunnable, 1000);
		// textView10.setText(getFileSize(txBytes));

		convertImagesAndVideosToByteArray();

		if (byteArr0 == null) {
			byteArr0 = new byte[0];
			itemimg0 = "";
		} else {
			itemimg0 = "itemimg0";
		}
		if (byteArr1 == null) {
			byteArr1 = new byte[0];
			itemimg1 = "";
		} else {
			itemimg1 = "itemimg1";
		}
		if (byteArr2 == null) {
			byteArr2 = new byte[0];
			itemimg2 = "";
		} else {
			itemimg2 = "itemimg2";
		}
		if (byteArr3 == null) {
			byteArr3 = new byte[0];
			itemimg3 = "";
		} else {
			itemimg3 = "itemimg3";
		}
		if (byteArr4 == null) {
			byteArr4 = new byte[0];
			itemimg4 = "";
		} else {
			itemimg4 = "itemimg4";
		}
		if (byteArr5 == null) {
			byteArr5 = new byte[0];
			itemimg5 = "";
		} else {
			itemimg5 = "itemimg5";
		}
		if (byteArr6 == null) {
			byteArr6 = new byte[0];
			itemimg6 = "";
		} else {
			itemimg6 = "itemimg6";
		}
		if (byteArr7 == null) {
			byteArr7 = new byte[0];
			itemimg7 = "";
		} else {
			itemimg7 = "itemimg7";
		}
		if (byteArr8 == null) {
			byteArr8 = new byte[0];
			itemimg8 = "";
		} else {
			itemimg8 = "itemimg8";
		}
		if (byteArr9 == null) {
			byteArr9 = new byte[0];
			itemimg9 = "";
		} else {
			itemimg9 = "itemimg9";
		}
		/**********/
		// /////************/
		if (byteArr10 == null) {
			byteArr10 = new byte[0];
			itemimg10 = "";
		} else {
			itemimg10 = "thumb0";
		}
		if (byteArr11 == null) {
			byteArr11 = new byte[0];
			itemimg11 = "";
		} else {
			itemimg11 = "thumb1";
		}
		if (byteArr12 == null) {
			byteArr12 = new byte[0];
			itemimg12 = "";
		} else {
			itemimg12 = "thumb2";
		}
		if (byteArr13 == null) {
			byteArr13 = new byte[0];
			itemimg13 = "";
		} else {
			itemimg13 = "thumb3";
		}
		if (byteArr14 == null) {
			byteArr14 = new byte[0];
			itemimg14 = "";
		} else {
			itemimg14 = "thumb4";
		}
		if (byteArr15 == null) {
			byteArr15 = new byte[0];
			itemimg15 = "";
		} else {
			itemimg15 = "thumb5";
		}
		if (byteArr16 == null) {
			byteArr16 = new byte[0];
			itemimg16 = "";
		} else {
			itemimg16 = "thumb6";
		}
		if (byteArr17 == null) {
			byteArr17 = new byte[0];
			itemimg17 = "";
		} else {
			itemimg17 = "thumb7";
		}
		if (byteArr18 == null) {
			byteArr18 = new byte[0];
			itemimg18 = "";
		} else {
			itemimg18 = "thumb8";
		}

		mTotalBytes = byteArr0.length
				+ byteArr1.length
				+ byteArr2.length
				+ byteArr3.length
				+ byteArr4.length
				+ byteArr5.length
				+ byteArr6.length
				+ byteArr7.length
				+ byteArr8.length
				+ byteArr9.length
				+ byteArr10.length
				+ byteArr11.length
				+ byteArr12.length
				+ byteArr13.length
				+ byteArr14.length
				+ byteArr15.length
				+ byteArr16.length
				+ byteArr17.length
				+ byteArr18.length
				+ mCatName.getBytes().length
				+ mPrice.getText().toString().getBytes().length
				+ mDescription.getText().toString().getBytes().length
				+ mItem.getText().toString().getBytes().length
				+ mCity.getText().toString().getBytes().length
				+ String.valueOf(mCheckBoxNetwork.isChecked()).getBytes().length;

		Log.e("", "aaaa mFileSize : " + getFileSize(mTotalBytes));
		mFileSize = getFileSize(mTotalBytes);

		// To calculate upload and download speed of internet
		mStartRX = TrafficStats.getTotalRxBytes();
		mStartTX = TrafficStats.getTotalTxBytes();
		if (mStartRX == TrafficStats.UNSUPPORTED
				|| mStartTX == TrafficStats.UNSUPPORTED) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Uh Oh!");
			alert.setMessage("Your device does not support traffic stat monitoring.");
			alert.show();
		} else {
			mHandler.postDelayed(mRunnable2, 1000);
		}

		// mTime = mTotalBytes / mUploadSpeedLong;
		// Log.e("", "aaaa mTime : " + convertSecondToHHMMString(mTime));

		Log.e("", "mExtension0 : " + mExtension0 + "   mExtension1: "
				+ mExtension1 + "mExtension2 : " + mExtension2
				+ "   mExtension3: " + mExtension3 + "mExtension4 : "
				+ mExtension4 + "   mExtension5: " + mExtension5
				+ "mExtension6 : " + mExtension6 + "   mExtension7: "
				+ mExtension7 + "mExtension8 : " + mExtension8
				+ "   mExtension9: " + mExtension9 + "mExtension10 : "
				+ mExtension10 + "   mExtension11: " + mExtension11
				+ "mExtension12 : " + mExtension12 + "   mExtension13: "
				+ mExtension13 + "mExtension14 : " + mExtension14
				+ "   mExtension15: " + mExtension15 + "mExtension16 : "
				+ mExtension16 + "   mExtension17: " + mExtension17
				+ "mExtension18 : " + mExtension18);
		Log.e("",
				"mCatName : " + mCatName + "imgcnt : "
						+ String.valueOf(mImageCount) + "locationnnnnnnn:"
						+ mCity.getText().toString());

		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/add-item.php?name=sf&desc=dsdf&uid=1&price=23&catename=HER&network=true&location=";

		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", mUIDStr));
			params.add(new BasicNameValuePair("catename", mCatName));
			params.add(new BasicNameValuePair("price", mPrice.getText()
					.toString()));
			params.add(new BasicNameValuePair("desc", mDescription.getText()
					.toString()));
			params.add(new BasicNameValuePair("name", mItem.getText()
					.toString()));
			params.add(new BasicNameValuePair("network", String
					.valueOf(mCheckBoxNetwork.isChecked())));
			params.add(new BasicNameValuePair("location", mCity.getText()
					.toString()));

			// File file = new File(postURL);
			// FileInputStream fileInputStream = new FileInputStream(file);
			// byte[] bytes = new byte[(int) file.length()];
			// fileInputStream.read(bytes);
			// fileInputStream.close();
			//
			// URL url = new URL(postURL);
			// HttpURLConnection connection = (HttpURLConnection) url
			// .openConnection();
			// OutputStream outputStream = connection.getOutputStream();
			// int bufferLength = 1024;
			// for (int i = 0; i < bytes.length; i += bufferLength) {
			// int progress = (int) ((i / (float) bytes.length) * 100);
			// // setProgress(progress);
			// percentage.setText(progress + "");
			// if (bytes.length - i >= bufferLength) {
			// outputStream.write(bytes, i, bufferLength);
			// } else {
			// outputStream.write(bytes, i, bytes.length - i);
			// }
			// }
			// percentage.setText(100 + "");
			//
			// outputStream.close();
			// outputStream.flush();
			//
			// InputStream inputStream = connection.getInputStream();
			// // read the response
			// inputStream.close();

			response = CustomHttpClient.executeHttpPostForImg10(postURL,
					params, itemimg0, byteArr0, itemimg1, byteArr1, itemimg2,
					byteArr2, itemimg3, byteArr3, itemimg4, byteArr4, itemimg5,
					byteArr5, itemimg6, byteArr6, itemimg7, byteArr7, itemimg8,
					byteArr8, itemimg9, byteArr9, itemimg10, byteArr10,
					itemimg11, byteArr11, itemimg12, byteArr12, itemimg13,
					byteArr13, itemimg14, byteArr14, itemimg15, byteArr15,
					itemimg16, byteArr16, itemimg17, byteArr17, itemimg18,
					byteArr18, mExtension0, mExtension1, mExtension2,
					mExtension3, mExtension4, mExtension5, mExtension6,
					mExtension7, mExtension8, mExtension9, mExtension10,
					mExtension11, mExtension12, mExtension13, mExtension14,
					mExtension15, mExtension16, mExtension17, mExtension18,
					mExtension19);

			// response = CustomHttpClient.executeHttpPostForImg10(postURL,
			// params, itemimg0, byteArr0, itemimg1, byteArr1, itemimg2,
			// byteArr2, itemimg3, byteArr3, itemimg4, byteArr4, itemimg5,
			// byteArr5, itemimg6, byteArr6, itemimg7, byteArr7, itemimg8,
			// byteArr8, itemimg9, byteArr9, mExtension0, mExtension1,
			// mExtension2, mExtension3, mExtension4, mExtension5,
			// mExtension6, mExtension7, mExtension8, mExtension9);

			// response = CustomHttpClient.executeHttpPostForImg(postURL,
			// params,
			// itemimg0, byteArr0);
			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void convertImagesAndVideosToByteArray() {
		if (mainBitmap != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap.compress(CompressFormat.PNG, 100, bos);
			byteArr0 = bos.toByteArray();
			mExtension0 = ".jpg";
		}
		if (mainBitmap1 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap1.compress(CompressFormat.PNG, 100, bos);
			byteArr1 = bos.toByteArray();
			mExtension1 = ".jpg";
		}
		if (mainBitmap2 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap2.compress(CompressFormat.PNG, 100, bos);
			byteArr2 = bos.toByteArray();
			mExtension2 = ".jpg";
		}
		if (mainBitmap3 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap3.compress(CompressFormat.PNG, 100, bos);
			byteArr3 = bos.toByteArray();
			mExtension3 = ".jpg";
		}
		if (mainBitmap4 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap4.compress(CompressFormat.PNG, 100, bos);
			byteArr4 = bos.toByteArray();
			mExtension4 = ".jpg";

		}
		if (mainBitmap5 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap5.compress(CompressFormat.PNG, 100, bos);
			byteArr5 = bos.toByteArray();
			mExtension5 = ".jpg";

		}
		if (mainBitmap6 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap6.compress(CompressFormat.PNG, 100, bos);
			byteArr6 = bos.toByteArray();
			mExtension6 = ".jpg";

		}

		if (mainBitmap7 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap7.compress(CompressFormat.PNG, 100, bos);
			byteArr7 = bos.toByteArray();
			mExtension7 = ".jpg";

		}
		if (mainBitmap8 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap8.compress(CompressFormat.PNG, 100, bos);
			byteArr8 = bos.toByteArray();
			mExtension8 = ".jpg";

		}

		if (mainBitmap9 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap9.compress(CompressFormat.PNG, 100, bos);
			byteArr9 = bos.toByteArray();
			mExtension9 = ".jpg";

		}

		if (mainBitmap10 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap10.compress(CompressFormat.PNG, 100, bos);
			byteArr10 = bos.toByteArray();
			mExtension10 = ".png";
		}
		if (mainBitmap11 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap11.compress(CompressFormat.PNG, 100, bos);
			byteArr11 = bos.toByteArray();
			mExtension11 = ".png";
		}
		if (mainBitmap12 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap12.compress(CompressFormat.PNG, 100, bos);
			byteArr12 = bos.toByteArray();
			mExtension12 = ".png";
		}
		if (mainBitmap13 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap13.compress(CompressFormat.PNG, 100, bos);
			byteArr13 = bos.toByteArray();
			mExtension13 = ".png";
		}
		if (mainBitmap14 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap14.compress(CompressFormat.PNG, 100, bos);
			byteArr14 = bos.toByteArray();
			mExtension14 = ".png";

		}
		if (mainBitmap15 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap15.compress(CompressFormat.PNG, 100, bos);
			byteArr15 = bos.toByteArray();
			mExtension15 = ".png";

		}
		if (mainBitmap16 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap16.compress(CompressFormat.PNG, 100, bos);
			byteArr16 = bos.toByteArray();
			mExtension16 = ".png";

		}

		if (mainBitmap17 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap17.compress(CompressFormat.PNG, 100, bos);
			byteArr17 = bos.toByteArray();
			mExtension17 = ".png";

		}
		if (mainBitmap18 != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mainBitmap18.compress(CompressFormat.PNG, 100, bos);
			byteArr18 = bos.toByteArray();
			mExtension18 = ".png";

		}

	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Uri uri, final Context context) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
		Runtime.getRuntime().gc();
	}

	private void openGalleryOrCamera() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Log.e("", "openGalleryOrCamera");
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(false);
		video_camera.setPressed(true);
		video_camera.setEnabled(false);

		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(false);
		video_gallery.setPressed(true);
		video_gallery.setEnabled(false);

		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button camera = (Button) dialog.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				Uri fileUri = getOutputMediaFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, PICK_FROM_CAMERA);

				// if (dialog != null) {
				// dialog.dismiss();
				// }
				// dialog1 = new Dialog(SellActivity.this);
				// dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				// dialog1.setContentView(getLayoutInflater().inflate(
				// R.layout.dialog_take, null));
				// Button take_photo = (Button) dialog1
				// .findViewById(R.id.take_photo);
				// take_photo.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (dialog1 != null) {
				// dialog1.dismiss();
				// }
				// Intent intent = new Intent(
				// "android.media.action.IMAGE_CAPTURE");
				// Uri fileUri = getOutputMediaFileUri();
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				// startActivityForResult(intent, PICK_FROM_CAMERA);
				// }
				// });
				// Button take_video = (Button) dialog1
				// .findViewById(R.id.take_video);
				// take_video.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (dialog1 != null) {
				// dialog1.dismiss();
				// }
				// }
				// });
				// take_photo.setTypeface(mActionBarTypeface);
				// take_video.setTypeface(mActionBarTypeface);
				//
				// dialog1.show();
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
						PICK_FROM_GALLERY);
				//
				// dialog1 = new Dialog(SellActivity.this);
				// dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				// dialog1.setContentView(getLayoutInflater().inflate(
				// R.layout.dialog_choose, null));
				// Button choose_photo = (Button) dialog1
				// .findViewById(R.id.choose_photo);
				// choose_photo.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (dialog1 != null) {
				// dialog1.dismiss();
				// }
				// Intent intent = new Intent();
				// intent.setType("image/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// intent.putExtra("crop", "true");
				// intent.putExtra("aspectX", 1);
				// intent.putExtra("aspectY", 1);
				// intent.putExtra("outputX", 400);
				// intent.putExtra("outputY", 400);
				// intent.putExtra("return-data", true);
				// startActivityForResult(Intent.createChooser(intent,
				// "Complete action using"), PICK_FROM_GALLERY);
				// }
				// });
				// Button choose_video = (Button) dialog1
				// .findViewById(R.id.choose_video);
				// choose_video.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// if (dialog1 != null) {
				// dialog1.dismiss();
				// }
				// }
				// });
				// choose_photo.setTypeface(mActionBarTypeface);
				// choose_video.setTypeface(mActionBarTypeface);
				// dialog1.show();
			}
		});
		dialog.show();

	}

	private void openGalleryOrCamera1() {
		Log.e("", "openGalleryOrCamera1");
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO1);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA1);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO1);
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
						PICK_FROM_GALLERY1);
			}
		});
		dialog.show();
	}

	public Uri getOutputMediaFileUri1(int type) {
		// To be safe, you should check that the SDCard is mounted

		if (Environment.getExternalStorageState() != null) {
			// this works for Android 2.2 and above
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
					"SMW_VIDEO");

			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d("", "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "VID_" + timeStamp + ".3gp");
			} else {
				return null;
			}

			return Uri.fromFile(mediaFile);
		}

		return null;
	}

	private void openGalleryOrCamera2() {
		Log.e("", "openGalleryOrCamera2");
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO2);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA2);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO2);
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
						PICK_FROM_GALLERY2);
			}
		});
		dialog.show();

	}

	private void openGalleryOrCamera3() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO3);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA3);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO3);
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
						PICK_FROM_GALLERY3);
			}
		});
		dialog.show();

	}

	private void openGalleryOrCamera4() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO4);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA4);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO4);
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
						PICK_FROM_GALLERY4);
			}
		});
		dialog.show();

	}

	private void openGalleryOrCamera5() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO5);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA5);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO5);
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
						PICK_FROM_GALLERY5);
			}
		});
		dialog.show();
	}

	private void openGalleryOrCamera6() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO6);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA6);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO6);
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
						PICK_FROM_GALLERY6);
			}
		});
		dialog.show();
	}

	private void openGalleryOrCamera7() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO7);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA7);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO7);
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
						PICK_FROM_GALLERY7);
			}
		});
		dialog.show();
	}

	private void openGalleryOrCamera8() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO8);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA8);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO8);
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
						PICK_FROM_GALLERY8);
			}
		});
		dialog.show();

	}

	private void openGalleryOrCamera9() {
		dialog = new Dialog(SellActivity.this, R.style.DialogSlideAnim);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_btn,
				null));
		dialog.getWindow().getAttributes().y = mPrice.getBottom() + 10;
		Button video_camera = (Button) dialog.findViewById(R.id.video_camera);
		video_camera.setSelected(true);
		video_camera.setPressed(false);
		video_camera.setEnabled(true);

		video_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File mediaFile = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/myvideo.mp4");

				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA_VIDEO9);
			}
		});
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
				startActivityForResult(intent, PICK_FROM_CAMERA9);
			}
		});
		Button video_gallery = (Button) dialog.findViewById(R.id.video_gallery);
		video_gallery.setSelected(true);
		video_gallery.setPressed(false);
		video_gallery.setEnabled(true);

		video_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*");
				startActivityForResult(galleryIntent, PICK_FROM_GALLERY_VIDEO9);
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
						PICK_FROM_GALLERY9);
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (dialog != null) {
			dialog.dismiss();
		}
		try {
			/********************** mSellImage *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);

			if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
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
					picUri = getOutputMediaFileUri();
					performCrop_gallary(picUri,PIC_CROP);
					//mainBitmap = getCroppedBitmap(bm);
					//mSellImage.setImageBitmap(getCroppedBitmap(bm));

					// addNewImageView();
					Log.e("", "cfksew7fejfwefkjwec ");

					mCrossImage.setVisibility(View.VISIBLE);
					mCrossImage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Log.e("", "goneeeeeeeeee ");
							mCrossImage.setVisibility(View.GONE);
							mSellImage.setImageBitmap(null);
							mSellImage
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage.setImageBitmap(null);
											mainBitmap = null;
											openGalleryOrCamera();
										}
									});
						}
					});
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
					mSellImage.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap = BitmapFactory.decodeFile(picturePath);
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");
					mSellImage.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap = thePic;
				}
				mSellImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Log.e("", "PIC_CROP : " + mSellImage.getDrawable());
						if (data == null) {
							openGalleryOrCamera();
						} else {
							if (mSellImage.getDrawable() == null) {
								openGalleryOrCamera();
							} else {
								viewImage(mainBitmap);
							}
						}

					}
				});

				mCrossImage.setVisibility(View.VISIBLE);
				mCrossImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage.setVisibility(View.GONE);
						mSellImage.setImageBitmap(null);
						mSellImage.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage.setImageBitmap(null);
								mainBitmap = null;
								openGalleryOrCamera();
							}
						});
					}
				});

			}
			if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP);
					mainBitmap = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
					mSellImage.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));

					mSellImage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Log.e("",
									"PICK_FROM_GALLERY : "
											+ mSellImage.getDrawable());
							if (data == null) {
								openGalleryOrCamera();
							} else {
								if (mSellImage.getDrawable() == null) {
									openGalleryOrCamera();
								} else {
									viewImage(mainBitmap);
								}
							}

						}
					});

					mCrossImage.setVisibility(View.VISIBLE);
					mCrossImage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage.setVisibility(View.GONE);
							mSellImage.setImageBitmap(null);
							mainBitmap = null;
							mSellImage
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											openGalleryOrCamera();
										}
									});
						}
					});
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mainBitmap = photo;
						mSellImage.setImageBitmap(getCroppedBitmap(photo));
						mCrossImage.setVisibility(View.VISIBLE);
						mCrossImage.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCrossImage.setVisibility(View.GONE);
								mSellImage.setImageBitmap(null);
								mainBitmap = null;
								mSellImage
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												openGalleryOrCamera();
											}
										});
							}
						});
					}
				}
			}
			// BitmapDrawable drawable = (BitmapDrawable)
			// mSellImage.getDrawable();
			// Bitmap bmap = drawable.getBitmap();
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// bmap.compress(CompressFormat.PNG, 100, bos);
			// bb1 = bos.toByteArray();
			if (Build.VERSION.SDK_INT >= 19) {
				mSellImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Log.e("", "5465465464 : " + mSellImage.getDrawable());

						if (mSellImage.getDrawable() == null) {
							openGalleryOrCamera();
						} else {
							viewImage(mainBitmap);
						}

					}
				});
			} else {
				mSellImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Log.e("", "000000000 : " + mSellImage.getDrawable());
						if (data == null) {
							openGalleryOrCamera();
						} else {
							if (mSellImage.getDrawable() == null) {
								openGalleryOrCamera();
							} else {
								viewImage(mainBitmap);
							}
						}

					}
				});
			}

			/********************** mSellImage1 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA1 && resultCode == RESULT_OK) {
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP1);
					//mSellImage1.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap1 = getCroppedBitmap(bm);
					mCrossImage1.setVisibility(View.VISIBLE);
					mCrossImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage1.setVisibility(View.GONE);
							mSellImage1.setImageBitmap(null);
							mSellImage1
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage1.setImageBitmap(null);
											mainBitmap1 = null;
											openGalleryOrCamera1();
										}
									});
						}
					});
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage1.getDrawable() == null) {
								openGalleryOrCamera1();
							} else {
								viewImage(mainBitmap1);
							}

						}
					});
				} else {
					picUri = getOutputMediaFileUri();
					performCrop1();
				}
			}
			if (requestCode == PIC_CROP1 && resultCode == RESULT_OK) {
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
					mSellImage1.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap1 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");
					mSellImage1.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap1 = thePic;
				}
				mCrossImage1.setVisibility(View.VISIBLE);
				mCrossImage1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage1.setVisibility(View.GONE);
						mSellImage1.setImageBitmap(null);
						mSellImage1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage1.setImageBitmap(null);
								mainBitmap1 = null;
								openGalleryOrCamera1();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage1.getDrawable() == null) {
								openGalleryOrCamera1();
							} else {
								viewImage(mainBitmap1);
							}

						}
					});
				} else {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera();
							} else {
								if (mSellImage1.getDrawable() == null) {
									openGalleryOrCamera1();
								} else {
									viewImage(mainBitmap1);
								}
							}

						}
					});
				}

			}

			if (requestCode == PICK_FROM_GALLERY1 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP1);
					mSellImage1.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap1 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
					mCrossImage1.setVisibility(View.VISIBLE);
					mCrossImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage1.setVisibility(View.GONE);
							mSellImage1.setImageBitmap(null);
							mSellImage1
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage1.setImageBitmap(null);
											mainBitmap1 = null;
											openGalleryOrCamera1();
										}
									});
						}
					});
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage1.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap1 = photo;
					}
				}
				mCrossImage1.setVisibility(View.VISIBLE);
				mCrossImage1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage1.setVisibility(View.GONE);
						mSellImage1.setImageBitmap(null);
						mSellImage1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage1.setImageBitmap(null);
								mainBitmap1 = null;
								openGalleryOrCamera1();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage1.getDrawable() == null) {
								openGalleryOrCamera1();
							} else {
								viewImage(mainBitmap1);
							}

						}
					});
				} else {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera();
							} else {
								if (mSellImage1.getDrawable() == null) {
									openGalleryOrCamera1();
								} else {
									viewImage(mainBitmap1);
								}
							}

						}
					});
				}
			}

			if (requestCode == PICK_FROM_GALLERY_VIDEO1
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap10 = bMap;
				mSellImage1.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo1.setVisibility(View.VISIBLE);
				mCrossImage1.setVisibility(View.VISIBLE);
				mCrossImage1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage1.setVisibility(View.GONE);
						mSellVideo1.setVisibility(View.GONE);
						mSellImage1.setImageBitmap(null);
						mSellImage1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mainBitmap10 = null;
								mSellImage1.setImageBitmap(null);
								openGalleryOrCamera1();
							}
						});
					}
				});
				mSellImage1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera();
						} else {
							if (mSellImage1.getDrawable() == null) {
								openGalleryOrCamera1();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});

				try {
					byteArr1 = readBytes(vid);
					mExtension1 = ".3gp";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO1
					&& resultCode == RESULT_OK && null != data) {
				final Uri vid = data.getData();
				Log.e("", "vid : " + vid);
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap10 = bMap;
				mSellImage1.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo1.setVisibility(View.VISIBLE);
				mCrossImage1.setVisibility(View.VISIBLE);
				mCrossImage1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage1.setVisibility(View.GONE);
						mSellVideo1.setVisibility(View.GONE);
						mSellImage1.setImageBitmap(null);
						mSellImage1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage1.setImageBitmap(null);

								openGalleryOrCamera1();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Log.e("",
									"5465465464 : " + mSellImage.getDrawable());

							if (mSellImage1.getDrawable() == null) {
								openGalleryOrCamera1();
							} else {
								playVideo(vid);
							}

						}
					});
				} else {
					mSellImage1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera();
							} else {
								if (mSellImage1.getDrawable() == null) {
									openGalleryOrCamera1();
								} else {
									playVideo(vid);
								}
							}

						}
					});
				}

				mSellVideo1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr1 = readBytes(vid);
					mExtension1 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// BitmapDrawable drawable1 = (BitmapDrawable)
			// mSellImage1.getDrawable();
			// Bitmap bmap1 = drawable1.getBitmap();
			// ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			// bmap1.compress(CompressFormat.PNG, 100, bos1);
			// bb2 = bos1.toByteArray();

			/********************** mSellImage2 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA2 && resultCode == RESULT_OK) {
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP2);
					//mSellImage2.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap2 = getCroppedBitmap(bm);
					mCrossImage2.setVisibility(View.VISIBLE);
					mCrossImage2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage2.setVisibility(View.GONE);
							mSellImage2.setImageBitmap(null);
							mSellImage2
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage2.setImageBitmap(null);
											mainBitmap2 = null;
											openGalleryOrCamera2();
										}
									});
						}
					});
					mSellImage2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage2.getDrawable() == null) {
								openGalleryOrCamera2();
							} else {
								viewImage(mainBitmap2);
							}
						}
					});
				} else {
					picUri = getOutputMediaFileUri();
					performCrop2();
				}
			}
			if (requestCode == PIC_CROP2 && resultCode == RESULT_OK) {
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
					mSellImage2.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap2 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage2.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap2 = thePic;
				}
				mCrossImage2.setVisibility(View.VISIBLE);
				mCrossImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage2.setVisibility(View.GONE);
						mSellImage2.setImageBitmap(null);
						mSellImage2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage2.setImageBitmap(null);
								mainBitmap2 = null;
								openGalleryOrCamera2();
							}
						});
					}
				});
				mSellImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera2();
						} else {
							if (mSellImage2.getDrawable() == null) {
								openGalleryOrCamera2();
							} else {
								viewImage(mainBitmap2);
							}
						}

					}
				});
			}

			if (requestCode == PICK_FROM_GALLERY2 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP2);
					mSellImage2.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap2 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage2.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap2 = photo;
					}
				}
				mCrossImage2.setVisibility(View.VISIBLE);
				mCrossImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage2.setVisibility(View.GONE);
						mSellImage2.setImageBitmap(null);
						mSellImage2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage2.setImageBitmap(null);
								mainBitmap2 = null;
								openGalleryOrCamera2();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage2.getDrawable() == null) {
								openGalleryOrCamera2();
							} else {
								viewImage(mainBitmap2);
							}
						}
					});
				} else {
					mSellImage2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera2();
							} else {
								if (mSellImage2.getDrawable() == null) {
									openGalleryOrCamera2();
								} else {
									viewImage(mainBitmap2);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO2
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 =getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap11 = bMap;
				mSellImage2.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo2.setVisibility(View.VISIBLE);
				mCrossImage2.setVisibility(View.VISIBLE);
				mCrossImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage2.setVisibility(View.GONE);
						mSellVideo2.setVisibility(View.GONE);
						mSellImage2.setImageBitmap(null);
						mSellImage2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage2.setImageBitmap(null);
								mainBitmap11 = null;
								openGalleryOrCamera2();
							}
						});
					}
				});
				mSellImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera2();
						} else {
							if (mSellImage2.getDrawable() == null) {
								openGalleryOrCamera2();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr2 = readBytes(vid);
					mExtension2 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO2
					&& resultCode == RESULT_OK && null != data) {
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap11 = bMap;
				mSellImage2.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo2.setVisibility(View.VISIBLE);
				mCrossImage2.setVisibility(View.VISIBLE);
				mCrossImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage2.setVisibility(View.GONE);
						mSellVideo2.setVisibility(View.GONE);
						mSellImage2.setImageBitmap(null);
						mSellImage2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage2.setImageBitmap(null);
								mainBitmap11 = null;
								openGalleryOrCamera2();
							}
						});
					}
				});
				mSellImage2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera2();
						} else {
							if (mSellImage2.getDrawable() == null) {
								openGalleryOrCamera2();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr2 = readBytes(vid);
					mExtension2 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// BitmapDrawable drawable2 = (BitmapDrawable)
			// mSellImage2.getDrawable();
			// Bitmap bmap2 = drawable2.getBitmap();
			// ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
			// bmap2.compress(CompressFormat.PNG, 100, bos2);
			// bb3 = bos2.toByteArray();

			/********************** mSellImage3 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA3 && resultCode == RESULT_OK) {
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP3);
					//mSellImage3.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap3 = getCroppedBitmap(bm);
					// addNewImageView();
					mCrossImage3.setVisibility(View.VISIBLE);
					mCrossImage3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage3.setVisibility(View.GONE);
							mSellImage3.setImageBitmap(null);
							mSellImage3
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage3.setImageBitmap(null);
											mainBitmap3 = null;
											openGalleryOrCamera3();
										}
									});
						}
					});
				} else {
					picUri = getOutputMediaFileUri();
					performCrop3();
				}
			}
			if (requestCode == PIC_CROP3 && resultCode == RESULT_OK) {
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
					mSellImage3.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap3 = BitmapFactory.decodeFile(picturePath);
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage3.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap3 = thePic;
				}
				mCrossImage3.setVisibility(View.VISIBLE);
				mCrossImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage3.setVisibility(View.GONE);
						mSellImage3.setImageBitmap(null);
						mSellImage3.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage3.setImageBitmap(null);
								mainBitmap3 = null;
								openGalleryOrCamera3();
							}
						});
					}
				});
				mSellImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera3();
						} else {
							if (mSellImage3.getDrawable() == null) {
								openGalleryOrCamera3();
							} else {
								viewImage(mainBitmap3);
							}
						}

					}
				});
			}

			if (requestCode == PICK_FROM_GALLERY3 && resultCode == RESULT_OK
					&& null != data) {
				Log.e("", "PICK_FROM_GALLERY3");
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP3);
					mSellImage3.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap3 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage3.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap3 = photo;
					}
				}
				mCrossImage3.setVisibility(View.VISIBLE);
				mCrossImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage3.setVisibility(View.GONE);
						mSellImage3.setImageBitmap(null);
						mSellImage3.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage3.setImageBitmap(null);
								mainBitmap3 = null;

								openGalleryOrCamera3();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage3.getDrawable() == null) {
								openGalleryOrCamera3();
							} else {
								viewImage(mainBitmap3);
							}

						}
					});
				} else {
					mSellImage3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera3();
							} else {
								if (mSellImage3.getDrawable() == null) {
									openGalleryOrCamera3();
								} else {
									viewImage(mainBitmap3);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO3
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap12 = bMap;
				mSellImage3.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo3.setVisibility(View.VISIBLE);
				mCrossImage3.setVisibility(View.VISIBLE);
				mCrossImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage3.setVisibility(View.GONE);
						mSellVideo3.setVisibility(View.GONE);
						mSellImage3.setImageBitmap(null);
						mSellImage3.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage3.setImageBitmap(null);
								mainBitmap12 = null;
								openGalleryOrCamera3();
							}
						});
					}
				});
				mSellImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera3();
						} else {
							if (mSellImage3.getDrawable() == null) {
								openGalleryOrCamera3();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr3 = readBytes(vid);
					mExtension3 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO3
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO3");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap12 = bMap;
				mSellImage3.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo3.setVisibility(View.VISIBLE);
				mCrossImage3.setVisibility(View.VISIBLE);
				mCrossImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage3.setVisibility(View.GONE);
						mSellVideo3.setVisibility(View.GONE);
						mSellImage3.setImageBitmap(null);
						mSellImage3.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage3.setImageBitmap(null);
								mainBitmap12 = null;
								openGalleryOrCamera3();
							}
						});
					}
				});
				mSellImage3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera3();
						} else {
							if (mSellImage3.getDrawable() == null) {
								openGalleryOrCamera3();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr3 = readBytes(vid);
					mExtension3 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/********************** mSellImage4 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA4 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA4");
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP4);
					//mSellImage4.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap4 = getCroppedBitmap(bm);

				} else {
					picUri = getOutputMediaFileUri();
					performCrop4();
				}
				mCrossImage4.setVisibility(View.VISIBLE);
				mCrossImage4.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mCrossImage4.setVisibility(View.GONE);
						mSellImage4.setImageBitmap(null);
						mSellImage4.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage4.setImageBitmap(null);
								mainBitmap4 = null;

								openGalleryOrCamera4();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage4.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage4.getDrawable() == null) {
								openGalleryOrCamera4();
							} else {
								viewImage(mainBitmap4);
							}
						}
					});
				} else {
					mSellImage4.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera4();
							} else {
								if (mSellImage4.getDrawable() == null) {
									openGalleryOrCamera4();
								} else {
									viewImage(mainBitmap4);
								}
							}

						}
					});
				}

			}
			if (requestCode == PIC_CROP4 && resultCode == RESULT_OK) {
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
					mSellImage4.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap4 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage4.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap4 = thePic;
				}
				mCrossImage4.setVisibility(View.VISIBLE);
				mCrossImage4.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mCrossImage4.setVisibility(View.GONE);
						mSellImage4.setImageBitmap(null);
						mSellImage4.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage4.setImageBitmap(null);
								mainBitmap4 = null;

								openGalleryOrCamera4();
							}
						});
					}
				});
				mSellImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera4();
						} else {
							if (mSellImage4.getDrawable() == null) {
								openGalleryOrCamera4();
							} else {
								viewImage(mainBitmap4);
							}
						}

					}
				});
			}

			if (requestCode == PICK_FROM_GALLERY4 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP4);
					mSellImage4.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap4 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage4.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap4 = photo;
					}
				}
				mCrossImage4.setVisibility(View.VISIBLE);
				mCrossImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage4.setVisibility(View.GONE);
						mSellImage4.setImageBitmap(null);
						mSellImage4.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage4.setImageBitmap(null);
								mainBitmap4 = null;

								openGalleryOrCamera4();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage4.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage4.getDrawable() == null) {
								openGalleryOrCamera4();
							} else {
								viewImage(mainBitmap4);
							}

						}
					});
				} else {
					mSellImage4.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera4();
							} else {
								if (mSellImage4.getDrawable() == null) {
									openGalleryOrCamera4();
								} else {
									viewImage(mainBitmap4);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO4
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap13 = bMap;
				mSellImage4.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo4.setVisibility(View.VISIBLE);
				mCrossImage4.setVisibility(View.VISIBLE);
				mCrossImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage4.setVisibility(View.GONE);
						mSellVideo4.setVisibility(View.GONE);
						mSellImage4.setImageBitmap(null);
						mSellImage4.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage4.setImageBitmap(null);
								mainBitmap13 = null;
								openGalleryOrCamera4();
							}
						});
					}
				});
				mSellImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera4();
						} else {
							if (mSellImage4.getDrawable() == null) {
								openGalleryOrCamera4();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr4 = readBytes(vid);
					mExtension4 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO4
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO4");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap13 = bMap;
				mSellImage4.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo4.setVisibility(View.VISIBLE);
				mCrossImage4.setVisibility(View.VISIBLE);
				mCrossImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage4.setVisibility(View.GONE);
						mSellVideo4.setVisibility(View.GONE);
						mSellImage4.setImageBitmap(null);
						mSellImage4.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage4.setImageBitmap(null);
								mainBitmap13 = null;
								openGalleryOrCamera4();
							}
						});
					}
				});
				mSellImage4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera4();
						} else {
							if (mSellImage4.getDrawable() == null) {
								openGalleryOrCamera4();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr4 = readBytes(vid);
					mExtension4 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/********************** mSellImage5 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA5 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA5");
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP5);
					//mSellImage5.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap5 = getCroppedBitmap(bm);
				} else {
					picUri = getOutputMediaFileUri();
					performCrop5();
				}

				mCrossImage5.setVisibility(View.VISIBLE);
				mCrossImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage5.setVisibility(View.GONE);
						mSellImage5.setImageBitmap(null);
						mSellImage5.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage5.setImageBitmap(null);
								mainBitmap5 = null;

								openGalleryOrCamera5();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSellImage5.getDrawable() == null) {
								openGalleryOrCamera5();
							} else {
								viewImage(mainBitmap5);
							}
						}
					});
				} else {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera5();
							} else {
								if (mSellImage5.getDrawable() == null) {
									openGalleryOrCamera5();
								} else {
									viewImage(mainBitmap5);
								}
							}

						}
					});
				}

			}
			if (requestCode == PIC_CROP5 && resultCode == RESULT_OK) {
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
					mSellImage5.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap5 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage5.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap5 = thePic;
				}
				mCrossImage5.setVisibility(View.VISIBLE);
				mCrossImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage5.setVisibility(View.GONE);
						mSellImage5.setImageBitmap(null);
						mSellImage5.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage5.setImageBitmap(null);
								mainBitmap5 = null;

								openGalleryOrCamera5();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSellImage5.getDrawable() == null) {
								openGalleryOrCamera5();
							} else {
								viewImage(mainBitmap5);
							}
						}
					});
				} else {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera5();
							} else {
								if (mSellImage5.getDrawable() == null) {
									openGalleryOrCamera5();
								} else {
									viewImage(mainBitmap5);
								}
							}

						}
					});
				}

			}

			if (requestCode == PICK_FROM_GALLERY5 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP5);
					mSellImage5.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap5 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage5.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap5 = photo;
					}
				}
				mCrossImage5.setVisibility(View.VISIBLE);
				mCrossImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage5.setVisibility(View.GONE);
						mSellImage5.setImageBitmap(null);
						mSellImage5.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage5.setImageBitmap(null);
								mainBitmap5 = null;

								openGalleryOrCamera5();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage5.getDrawable() == null) {
								openGalleryOrCamera5();
							} else {
								viewImage(mainBitmap5);
							}

						}
					});
				} else {
					mSellImage5.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera4();
							} else {
								if (mSellImage5.getDrawable() == null) {
									openGalleryOrCamera5();
								} else {
									viewImage(mainBitmap5);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO5
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap14 = bMap;
				mSellImage5.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo5.setVisibility(View.VISIBLE);
				mCrossImage5.setVisibility(View.VISIBLE);
				mCrossImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage5.setVisibility(View.GONE);
						mSellVideo5.setVisibility(View.GONE);
						mSellImage5.setImageBitmap(null);
						mSellImage5.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage5.setImageBitmap(null);
								mainBitmap14 = null;
								openGalleryOrCamera5();
							}
						});
					}
				});
				mSellImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera5();
						} else {
							if (mSellImage5.getDrawable() == null) {
								openGalleryOrCamera5();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr5 = readBytes(vid);
					mExtension5 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO5
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO5");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap14 = bMap;
				mSellImage5.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo5.setVisibility(View.VISIBLE);
				mCrossImage5.setVisibility(View.VISIBLE);
				mCrossImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage5.setVisibility(View.GONE);
						mSellVideo5.setVisibility(View.GONE);
						mSellImage5.setImageBitmap(null);
						mSellImage5.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage5.setImageBitmap(null);
								mainBitmap14 = null;
								openGalleryOrCamera5();
							}
						});
					}
				});
				mSellImage5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera5();
						} else {
							if (mSellImage5.getDrawable() == null) {
								openGalleryOrCamera5();
							} else {
								playVideo(vid);
							}
						}

					}

				});
				mSellVideo5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr5 = readBytes(vid);
					mExtension5 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/********************** mSellImage6 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA6 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA6");
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP6);
					//mSellImage6.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap6 = getCroppedBitmap(bm);
				} else {
					picUri = getOutputMediaFileUri();
					performCrop6();
				}
				mCrossImage6.setVisibility(View.VISIBLE);
				mCrossImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage6.setVisibility(View.GONE);
						mSellImage6.setImageBitmap(null);
						mSellImage6.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage6.setImageBitmap(null);
								mainBitmap6 = null;

								openGalleryOrCamera6();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage6.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage6.getDrawable() == null) {
								openGalleryOrCamera6();
							} else {
								viewImage(mainBitmap6);
							}

						}
					});
				} else {
					mSellImage6.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera6();
							} else {
								if (mSellImage6.getDrawable() == null) {
									openGalleryOrCamera6();
								} else {
									viewImage(mainBitmap6);
								}
							}

						}
					});
				}
			}
			if (requestCode == PIC_CROP6 && resultCode == RESULT_OK) {
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
					mSellImage6.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap6 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage6.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap6 = thePic;
				}

			}

			if (requestCode == PICK_FROM_GALLERY6 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP6);
					mSellImage6.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap6 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage6.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap6 = photo;
					}
				}
				mCrossImage6.setVisibility(View.VISIBLE);
				mCrossImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage6.setVisibility(View.GONE);
						mSellImage6.setImageBitmap(null);
						mSellImage6.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage6.setImageBitmap(null);
								mainBitmap6 = null;
								openGalleryOrCamera6();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage6.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSellImage6.getDrawable() == null) {
								openGalleryOrCamera6();
							} else {
								viewImage(mainBitmap6);
							}

						}
					});
				} else {
					mSellImage6.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera6();
							} else {
								if (mSellImage6.getDrawable() == null) {
									openGalleryOrCamera6();
								} else {
									viewImage(mainBitmap6);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO6
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap15 = bMap;
				mSellImage6.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo6.setVisibility(View.VISIBLE);
				mCrossImage6.setVisibility(View.VISIBLE);
				mCrossImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage6.setVisibility(View.GONE);
						mSellVideo6.setVisibility(View.GONE);
						mSellImage6.setImageBitmap(null);
						mSellImage6.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage6.setImageBitmap(null);
								mainBitmap15 = null;
								openGalleryOrCamera6();
							}
						});
					}
				});
				mSellImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera6();
						} else {
							if (mSellImage6.getDrawable() == null) {
								openGalleryOrCamera6();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr6 = readBytes(vid);
					mExtension6 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO6
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO6");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap15 = bMap;
				mSellImage6.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo6.setVisibility(View.VISIBLE);
				mCrossImage6.setVisibility(View.VISIBLE);
				mCrossImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage6.setVisibility(View.GONE);
						mSellVideo6.setVisibility(View.GONE);
						mSellImage6.setImageBitmap(null);
						mSellImage6.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage6.setImageBitmap(null);
								mainBitmap15 = null;
								openGalleryOrCamera6();
							}
						});
					}
				});
				mSellImage6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera6();
						} else {
							if (mSellImage6.getDrawable() == null) {
								openGalleryOrCamera6();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr6 = readBytes(vid);
					mExtension6 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/********************** mSellImage7 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA7 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA7");
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
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP7);
					//mSellImage7.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap7 = getCroppedBitmap(bm);

					mCrossImage7.setVisibility(View.VISIBLE);
					mCrossImage7.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage7.setVisibility(View.GONE);
							mSellImage7.setImageBitmap(null);
							mSellImage7
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage7.setImageBitmap(null);
											mainBitmap7 = null;

											openGalleryOrCamera7();
										}
									});
						}
					});
					if (Build.VERSION.SDK_INT >= 19) {
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (mSellImage7.getDrawable() == null) {
									openGalleryOrCamera7();
								} else {
									viewImage(mainBitmap7);
								}

							}
						});
					} else {
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (data == null) {
									openGalleryOrCamera7();
								} else {
									if (mSellImage7.getDrawable() == null) {
										openGalleryOrCamera7();
									} else {
										viewImage(mainBitmap7);
									}
								}

							}
						});
					}
				} else {
					picUri = getOutputMediaFileUri();
					performCrop7();
				}

			}
			if (requestCode == PIC_CROP7 && resultCode == RESULT_OK) {
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
					mSellImage7.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap7 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage7.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap7 = thePic;
				}
				mCrossImage7.setVisibility(View.VISIBLE);
				mCrossImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage7.setVisibility(View.GONE);
						mSellImage7.setImageBitmap(null);
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage7.setImageBitmap(null);
								mainBitmap7 = null;

								openGalleryOrCamera7();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage7.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage7.getDrawable() == null) {
								openGalleryOrCamera7();
							} else {
								viewImage(mainBitmap7);
							}

						}
					});
				} else {
					mSellImage7.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera7();
							} else {
								if (mSellImage7.getDrawable() == null) {
									openGalleryOrCamera7();
								} else {
									viewImage(mainBitmap7);
								}
							}

						}
					});
				}

			}

			if (requestCode == PICK_FROM_GALLERY7 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP7);
					mSellImage7.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap7 = BitmapFactory.decodeFile(picturePath);
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage7.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap7 = photo;
					}
				}
				mCrossImage7.setVisibility(View.VISIBLE);
				mCrossImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage7.setVisibility(View.GONE);
						mSellImage7.setImageBitmap(null);
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage7.setImageBitmap(null);
								mainBitmap7 = null;

								openGalleryOrCamera7();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage7.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage7.getDrawable() == null) {
								openGalleryOrCamera7();
							} else {
								viewImage(mainBitmap7);
							}
						}
					});
				} else {
					mSellImage7.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera7();
							} else {
								if (mSellImage7.getDrawable() == null) {
									openGalleryOrCamera7();
								} else {
									viewImage(mainBitmap7);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO7
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap16 = bMap;
				mSellImage7.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo7.setVisibility(View.VISIBLE);
				mCrossImage7.setVisibility(View.VISIBLE);
				mCrossImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage7.setVisibility(View.GONE);
						mSellVideo7.setVisibility(View.GONE);
						mSellImage7.setImageBitmap(null);
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage7.setImageBitmap(null);
								mainBitmap7 = null;

								openGalleryOrCamera7();
							}
						});

					}
				});
				mSellImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera7();
						} else {
							if (mSellImage7.getDrawable() == null) {
								openGalleryOrCamera7();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr7 = readBytes(vid);
					mExtension7 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO7
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO7");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap16 = bMap;
				mSellImage7.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo7.setVisibility(View.VISIBLE);
				mCrossImage7.setVisibility(View.VISIBLE);
				mCrossImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage7.setVisibility(View.GONE);
						mSellVideo7.setVisibility(View.GONE);
						mSellImage7.setImageBitmap(null);
						mSellImage7.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage7.setImageBitmap(null);
								mainBitmap16 = null;
								openGalleryOrCamera7();
							}
						});
					}
				});
				mSellImage7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera7();
						} else {
							if (mSellImage7.getDrawable() == null) {
								openGalleryOrCamera7();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr7 = readBytes(vid);
					mExtension7 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/********************** mSellImage8 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA8 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA8");
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
					//mSellImage8.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap8 = getCroppedBitmap(bm);
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP8);
					mCrossImage8.setVisibility(View.VISIBLE);
					mCrossImage8.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage8.setVisibility(View.GONE);
							mSellImage8.setImageBitmap(null);
							mSellImage8
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mSellImage8.setImageBitmap(null);
											mainBitmap8 = null;

											openGalleryOrCamera8();
										}
									});
						}
					});
					if (Build.VERSION.SDK_INT >= 19) {
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (mSellImage8.getDrawable() == null) {
									openGalleryOrCamera8();
								} else {
									viewImage(mainBitmap8);
								}
							}
						});
					} else {
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (data == null) {
									openGalleryOrCamera8();
								} else {
									if (mSellImage8.getDrawable() == null) {
										openGalleryOrCamera8();
									} else {
										viewImage(mainBitmap8);
									}
								}

							}
						});
					}
				} else {
					picUri = getOutputMediaFileUri();
					performCrop8();
				}

			}
			if (requestCode == PIC_CROP8 && resultCode == RESULT_OK) {
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
					mSellImage8.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap8 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage8.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap8 = thePic;
				}

				mCrossImage8.setVisibility(View.VISIBLE);
				mCrossImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage8.setVisibility(View.GONE);
						mSellImage8.setImageBitmap(null);
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage8.setImageBitmap(null);
								mainBitmap8 = null;

								openGalleryOrCamera8();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage8.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage8.getDrawable() == null) {
								openGalleryOrCamera8();
							} else {
								viewImage(mainBitmap8);
							}
						}
					});
				} else {
					mSellImage8.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera8();
							} else {
								if (mSellImage8.getDrawable() == null) {
									openGalleryOrCamera8();
								} else {
									viewImage(mainBitmap8);
								}
							}

						}
					});
				}
			}
			if (requestCode == PICK_FROM_GALLERY8 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP8);
					mSellImage8.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap8 = BitmapFactory.decodeFile(picturePath);
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage8.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap8 = photo;
					}
				}
				mCrossImage8.setVisibility(View.VISIBLE);
				mCrossImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage8.setVisibility(View.GONE);
						mSellImage8.setImageBitmap(null);
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage8.setImageBitmap(null);
								mainBitmap8 = null;

								openGalleryOrCamera8();
							}
						});
					}
				});

			}
			if (Build.VERSION.SDK_INT >= 19) {
				mSellImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mSellImage8.getDrawable() == null) {
							openGalleryOrCamera8();
						} else {
							viewImage(mainBitmap8);
						}
					}
				});
			} else {
				mSellImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera8();
						} else {
							if (mSellImage8.getDrawable() == null) {
								openGalleryOrCamera8();
							} else {
								viewImage(mainBitmap8);
							}
						}

					}
				});
			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO8
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mSellImage8.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo8.setVisibility(View.VISIBLE);
				mCrossImage8.setVisibility(View.VISIBLE);
				mCrossImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage8.setVisibility(View.GONE);
						mSellVideo8.setVisibility(View.GONE);
						mSellImage8.setImageBitmap(null);
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage8.setImageBitmap(null);

								openGalleryOrCamera8();
							}
						});
					}
				});
				mSellImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera8();
						} else {
							if (mSellImage8.getDrawable() == null) {
								openGalleryOrCamera8();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr8 = readBytes(vid);
					mExtension8 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO8
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO8");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap17 = bMap;
				mSellImage8.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo8.setVisibility(View.VISIBLE);
				mCrossImage8.setVisibility(View.VISIBLE);
				mCrossImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage8.setVisibility(View.GONE);
						mSellVideo8.setVisibility(View.GONE);
						mSellImage8.setImageBitmap(null);
						mSellImage8.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage8.setImageBitmap(null);
								mainBitmap17 = null;
								openGalleryOrCamera8();
							}
						});
					}
				});
				mSellImage8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera8();
						} else {
							if (mSellImage8.getDrawable() == null) {
								openGalleryOrCamera8();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr8 = readBytes(vid);
					mExtension8 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/********************** mSellImage9 *******************************/
			Log.e("", "requestCode :  " + requestCode + " resultCode : "
					+ resultCode + "data : " + data);
			if (requestCode == PICK_FROM_CAMERA9 && resultCode == RESULT_OK) {
				Log.e("", "PICK_FROM_CAMERA9");
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
					//mSellImage9.setImageBitmap(getCroppedBitmap(bm));
					//mainBitmap9 = getCroppedBitmap(bm);
					picUri = getOutputMediaFileUri();

					performCrop_gallary(picUri,PIC_CROP9);
					mCrossImage9.setVisibility(View.VISIBLE);
					mCrossImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCrossImage9.setVisibility(View.GONE);
							mSellImage9.setImageBitmap(null);
							mSellImage9
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Log.e("",
													"testtttttttttttttttttttttttttst");
											mSellImage9.setImageBitmap(null);
											mainBitmap9 = null;

											openGalleryOrCamera9();
										}
									});
						}
					});
					if (Build.VERSION.SDK_INT >= 19) {

						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mSellImage9.getDrawable() == null) {
									openGalleryOrCamera9();
								} else {
									viewImage(mainBitmap9);
								}
							}
						});
					} else {
						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (data == null) {
									openGalleryOrCamera9();
								} else {
									if (mSellImage9.getDrawable() == null) {
										openGalleryOrCamera9();
									} else {
										viewImage(mainBitmap9);
									}
								}

							}
						});
					}

				} else {
					picUri = getOutputMediaFileUri();
					performCrop9();
				}
			}
			if (requestCode == PIC_CROP9 && resultCode == RESULT_OK) {
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
					mSellImage9.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap9 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras = data.getExtras();
					Bitmap thePic = extras.getParcelable("data");

					mSellImage9.setImageBitmap(getCroppedBitmap(thePic));
					mainBitmap9 = thePic;
				}
				mCrossImage9.setVisibility(View.VISIBLE);
				mCrossImage9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage9.setVisibility(View.GONE);
						mSellImage9.setImageBitmap(null);
						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage9.setImageBitmap(null);
								mainBitmap9 = null;

								openGalleryOrCamera9();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {

					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSellImage9.getDrawable() == null) {
								openGalleryOrCamera9();
							} else {
								viewImage(mainBitmap9);
							}
						}
					});
				} else {
					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera9();
							} else {
								if (mSellImage9.getDrawable() == null) {
									openGalleryOrCamera9();
								} else {
									viewImage(mainBitmap9);
								}
							}

						}
					});
				}

			}

			if (requestCode == PICK_FROM_GALLERY9 && resultCode == RESULT_OK
					&& null != data) {
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
					Log.e("", "picturePath : " + picturePath);
					performCrop_gallary(selectedImage,PIC_CROP9);
					mSellImage9.setImageBitmap(getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath)));
					mainBitmap9 = getCroppedBitmap(BitmapFactory
							.decodeFile(picturePath));
				} else {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						mSellImage9.setImageBitmap(getCroppedBitmap(photo));
						mainBitmap9 = photo;
					}
				}
				mCrossImage9.setVisibility(View.VISIBLE);
				mCrossImage9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage9.setVisibility(View.GONE);
						mSellImage9.setImageBitmap(null);
						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage9.setImageBitmap(null);
								mainBitmap9 = null;
								openGalleryOrCamera9();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSellImage9.getDrawable() == null) {
								openGalleryOrCamera9();
							} else {
								viewImage(mainBitmap9);
							}
						}
					});
				} else {
					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera9();
							} else {
								if (mSellImage9.getDrawable() == null) {
									openGalleryOrCamera9();
								} else {
									viewImage(mainBitmap9);
								}
							}

						}
					});
				}

			}
			if (requestCode == PICK_FROM_GALLERY_VIDEO9
					&& resultCode == RESULT_OK && null != data) {

				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap18 = bMap;
				mSellImage9.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo9.setVisibility(View.VISIBLE);
				mCrossImage9.setVisibility(View.VISIBLE);
				mCrossImage9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage9.setVisibility(View.GONE);
						mSellVideo9.setVisibility(View.GONE);
						mSellImage9.setImageBitmap(null);
						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage9.setImageBitmap(null);
								mainBitmap18 = null;
								openGalleryOrCamera9();
							}
						});
					}
				});
				if (Build.VERSION.SDK_INT >= 19) {
					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (mSellImage9.getDrawable() == null) {
								openGalleryOrCamera9();
							} else {
								playVideo(vid);
							}

						}
					});
				} else {
					mSellImage9.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (data == null) {
								openGalleryOrCamera9();
							} else {
								if (mSellImage9.getDrawable() == null) {
									openGalleryOrCamera9();
								} else {
									playVideo(vid);
								}
							}

						}
					});
				}

				mSellVideo9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr9 = readBytes(vid);
					mExtension9 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (requestCode == PICK_FROM_CAMERA_VIDEO9
					&& resultCode == RESULT_OK && null != data) {
				Log.e("", "PICK_FROM_CAMERA_VIDEO9");
				final Uri vid = data.getData();
				videoPath1 = getPath(vid, SellActivity.this);
				Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath1,
						MediaStore.Video.Thumbnails.MINI_KIND);
				mainBitmap18 = bMap;
				mSellImage9.setImageBitmap(getCroppedBitmap(bMap));
				mSellVideo9.setVisibility(View.VISIBLE);
				mCrossImage9.setVisibility(View.VISIBLE);
				mCrossImage9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCrossImage9.setVisibility(View.GONE);
						mSellVideo9.setVisibility(View.GONE);
						mSellImage9.setImageBitmap(null);
						mSellImage9.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mSellImage9.setImageBitmap(null);
								mainBitmap18 = null;
								openGalleryOrCamera9();
							}
						});
					}
				});
				mSellImage9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (data == null) {
							openGalleryOrCamera9();
						} else {
							if (mSellImage9.getDrawable() == null) {
								openGalleryOrCamera9();
							} else {
								playVideo(vid);
							}
						}

					}
				});
				mSellVideo9.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						playVideo(vid);
					}
				});
				try {
					byteArr9 = readBytes(vid);
					mExtension9 = ".3gp";

				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.e("", "test act " + Session.getActiveSession());

				if (Session.getActiveSession() != null) {
					Session.getActiveSession().onActivityResult(this,
							requestCode, resultCode, data);
				}
			}
		} catch (OutOfMemoryError e) {
			Toast.makeText(SellActivity.this,
					"Please select a lower resolution image",
					Toast.LENGTH_SHORT).show();
		}
	}

	public byte[] readBytes(Uri uri) throws IOException {
		// this dynamically extends to take the bytes you read
		InputStream inputStream = getContentResolver().openInputStream(uri);
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	protected void viewImage(Bitmap mainBitmap) {
		dialog2 = new Dialog(SellActivity.this);
		dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(getLayoutInflater().inflate(
				R.layout.dialog_sell_image, null));

		TouchImageView sell_image = (TouchImageView) dialog2
				.findViewById(R.id.sell_image);
		sell_image.setImageBitmap(mainBitmap);
		dialog2.show();
	}

	private void viewImage1(Drawable drawable) {
		dialog2 = new Dialog(SellActivity.this);
		dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(getLayoutInflater().inflate(
				R.layout.dialog_sell_image, null));
		TouchImageView sell_image = (TouchImageView) dialog2
				.findViewById(R.id.sell_image);
		sell_image.setImageDrawable(drawable);
		dialog2.show();
	}

	protected void playVideo(Uri uri) {
		dialog3 = new Dialog(SellActivity.this);
		dialog3.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog3.setContentView(getLayoutInflater().inflate(
				R.layout.dialog_sell_video, null));

		VideoView sell_video = (VideoView) dialog3
				.findViewById(R.id.sell_video);
		sell_video.setVideoURI(uri);
		sell_video.requestFocus();
		// mediaController = new MediaController(this, true);
		// mediaController.setAnchorView(sell_video);
		// sell_video.setMediaController(mediaController);
		sell_video.start();
		dialog3.show();
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void performCrop() {
		try {
			
			
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
	}
	private void performCrop_gallary(Uri uri,Integer temp) {
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
            startActivityForResult(cropIntent, temp);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	private void performCrop1() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP1);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop2() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP2);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop3() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP3);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop4() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP4);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop5() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP5);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop6() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP6);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop7() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP7);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop8() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP8);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop9() {
		try {
			
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
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
            startActivityForResult(cropIntent, PIC_CROP9);
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
		// final Rect rect = new Rect(0, 0, bitmap.getWidth(),
		// bitmap.getHeight());
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		final RectF rectF = new RectF(0, 0, bitmap.getWidth(),
				bitmap.getHeight());
		// canvas.drawRoundRect(rectF, 20, 20, paint);

		canvas.drawRoundRect(rectF, radius, radius, paint);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		// bitmap.getWidth() / 2, paint);
		// canvas.drawRect(rect, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 120, 120, false);
		Bitmap _bmp = Bitmap.createScaledBitmap(output, width, height, false);
		return _bmp;
		// return output;
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

	public void actionBarDetails() {
		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");
		mActionBarTypeface1 = Typeface.createFromAsset(getAssets(),
				"fonts/verdana_bold.ttf");
		mSymbolTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/modernpics.otf");

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
							startActivity(new Intent(SellActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(SellActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(SellActivity.this,
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
												SellActivity.this,
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
		searchClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBarSearch.setText("");
			}
		});
		ImageView message = (ImageView) addView.findViewById(R.id.message);
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				startActivity(new Intent(SellActivity.this,
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
					// try {
					// // getCategories();
					// } catch (Exception e) {
					// }
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
						mSItem = new SearchItem();
						mSItem.setItemName(jo.getString("Name"));
						mItemList.add(mSItem);
					}
					if (mItemList.size() != 0) {
						List<SearchItem> listWithoutDuplicates = new ArrayList<SearchItem>(
								mItemList);
						final SearchAdapter adapter = new SearchAdapter(
								SellActivity.this, listWithoutDuplicates);
						actionBarSearch.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
								SellActivity.this, listWithoutDuplicates1);
						mCity.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
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

	private void logout() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(SellActivity.this);
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
						SellActivity.this);
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
						SharedPreferences settings = getSharedPreferences(
								LoginActivity.PREFS_NAME, 0);
						settings.edit()
								.putString(LoginActivity.PREFS_LOGIN, "FAILED")
								.commit();
						startActivity(new Intent(SellActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								SellActivity.this);
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
			} catch (Exception e) {
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
	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  setContentView(R.layout.sell);
	}*/

}
