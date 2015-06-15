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
import java.util.Collections;
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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.PlaceJSONParser;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.CommentsAdapter;
import com.nanostuffs.maimai.adapter.CountrySimpleAdapter;
import com.nanostuffs.maimai.adapter.LocationsAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.model.Comments;
import com.nanostuffs.maimai.model.Item;
import com.nanostuffs.maimai.model.SearchItem;

public class MeItemDetailsActivity extends Activity {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private ImageView mUserImage;
	private TextView mName;
	private TextView mPrice;
	private TextView mTimeAgo;
	private ImageView mImage1;
	private ImageView mImage2;
	private ImageView mImage3;
	private ImageView mImage4;
	private ImageView mImage5;
	private ImageView mImage6;
	private ImageView mImage7;
	private ImageView mImage8;
	private ImageView mImage9;
	private ImageView mImage10;
	private TextView mItemName;
	private TextView mItemDescription;
	private TextView mItemPrice;
	private TextView mItemLocation;
	private TextView mItemLikes;
	private TextView mItemViews;
	private TextView mItemReportAbuse;
	private AutoCompleteTextView actionBarSearch;
	private SearchItem mItem;
	private ArrayList<SearchItem> mItemList;
	private String mItemID;
	private String mUserID;
	private Item item;
	private AQuery androidAQuery;
	private ImageView mReportAbuseImage;
	private VideoView mVideo1;
	private VideoView mVideo2;
	private VideoView mVideo3;
	private VideoView mVideo4;
	private VideoView mVideo5;
	private VideoView mVideo6;
	private VideoView mVideo7;
	private VideoView mVideo8;
	private VideoView mVideo9;
	private VideoView mVideo10;
	private ImageButton mEditIcon;
	private ImageButton mSaveIcon;
	private EditText mEditItemName;
	private EditText mEditItemDescription;
	private String response;
	private EditText mEditItemPrice;
	// private Spinner mLocationSpinner;
	private ArrayAdapter<String> myAdap;
	private ListView mCommentsList;
	private ArrayList<Comments> mComList;
	private Comments mCom;
	private CommentsAdapter mCommentsAdapter;
	private ImageButton mDelImage1;
	private ImageButton mDelImage2;
	private ImageButton mDelImage3;
	private ImageButton mDelImage4;
	private ImageButton mDelImage5;
	private ImageButton mDelImage6;
	private ImageButton mDelImage7;
	private ImageButton mDelImage9;
	private ImageButton mDelImage8;
	private ImageButton mDelImage10;
	private ImageButton mDelVideo1;
	private ImageButton mDelVideo2;
	private ImageButton mDelVideo3;
	private ImageButton mDelVideo4;
	private ImageButton mDelVideo5;
	private ImageButton mDelVideo6;
	private ImageButton mDelVideo7;
	private ImageButton mDelVideo8;
	private ImageButton mDelVideo9;
	private ImageButton mDelVideo10;
	private String mImageName;
	private int mFlag;
	private RelativeLayout mRelThumb1;
	private RelativeLayout mRelThumb2;
	private RelativeLayout mRelThumb3;
	private RelativeLayout mRelThumb4;
	private RelativeLayout mRelThumb5;
	private RelativeLayout mRelThumb6;
	private RelativeLayout mRelThumb7;
	private RelativeLayout mRelThumb8;
	private RelativeLayout mRelThumb9;
	private RelativeLayout mRelThumb10;
	private ImageButton mThumbImage1;
	private ImageButton mThumbImage2;
	private ImageButton mThumbImage3;
	private ImageButton mThumbImage4;
	private ImageButton mThumbImage5;
	private ImageButton mThumbImage6;
	private ImageButton mThumbImage7;
	private ImageButton mThumbImage8;
	private ImageButton mThumbImage9;
	private ImageButton mThumbImage10;
	private static final int PICK_FROM_GALLERY = 1;
	private static final int PICK_FROM_GALLERY1 = 2;
	private static final int PICK_FROM_GALLERY2 = 3;
	private static final int PICK_FROM_GALLERY3 = 4;
	private static final int PICK_FROM_GALLERY4 = 5;
	private static final int PICK_FROM_GALLERY5 = 6;
	private static final int PICK_FROM_GALLERY6 = 7;
	private static final int PICK_FROM_GALLERY7 = 8;
	private static final int PICK_FROM_GALLERY8 = 9;
	private static final int PICK_FROM_GALLERY9 = 10;
	private static final int PIC_CROP = 11;
	private static final int PIC_CROP1 = 12;
	private static final int PIC_CROP2 = 13;
	private static final int PIC_CROP3 = 14;
	private static final int PIC_CROP4 = 15;
	private static final int PIC_CROP5 = 16;
	private static final int PIC_CROP6 = 17;
	private static final int PIC_CROP7 = 18;
	private static final int PIC_CROP8 = 19;
	private static final int PIC_CROP9 = 20;
	private Bitmap mainBitmap;
	private Bitmap mainBitmap1;
	private Bitmap mainBitmap2;
	private Bitmap mainBitmap3;
	private Bitmap mainBitmap4;
	private Bitmap mainBitmap5;
	private Bitmap mainBitmap6;
	private Bitmap mainBitmap7;
	private Bitmap mainBitmap8;
	private Bitmap mainBitmap9;
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
	private Uri picUri;
	private String mExtension0;
	private String mExtension1;
	private String mExtension2;
	private String mExtension3;
	private String mExtension4;
	private String mExtension5;
	private String mExtension6;
	private String mExtension7;
	private String mExtension8;
	private String mExtension9;
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
	public String mItemIDDDDDD;
	private Dialog custom_dialog1,dialog3;
	protected Uri picUri1;
	protected Uri picUri2;
	protected Uri picUri3;
	
	protected Uri picUri4;
	private AsyncTask<String, Integer, String> mSelltask;
	private TextView mDollar;
	private AutoCompleteTextView mCountryAuto;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.me_item_details);
		setContentView(R.layout.me_comment_list);
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		actionBarDetails();
		try {
			initializeComponents();
			getItemDetails();
			getComments();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	protected void playVideo(String string) {
		dialog3 = new Dialog(MeItemDetailsActivity
				.this);
		dialog3.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog3.setContentView(getLayoutInflater().inflate(
				R.layout.video_play, null));

		final VideoView sell_video = (VideoView) dialog3
				.findViewById(R.id.videoView_play);
		
		final ProgressBar mProgress;
		
		mProgress = (ProgressBar) dialog3.findViewById(R.id.progress_play);
		mProgress.setVisibility(View.VISIBLE);
		MediaController mediaController = new MediaController(
				MeItemDetailsActivity.this);
		mediaController.setAnchorView(sell_video);
		sell_video.setMediaController(mediaController);
		sell_video.requestFocus();
		sell_video.setVideoPath(string);
		Log.e("",
				"Video........................"
						+ getIntent().getStringExtra("video"));
		if (checkInternetConnection()) {
			mProgress.setVisibility(View.VISIBLE);
			sell_video.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mProgress.setVisibility(View.GONE);
					sell_video.start();
				}
			});
		}
		
		dialog3.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
				mainBitmap = BitmapFactory.decodeFile(picturePath);
				mImage1.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap = thePic;
				mImage1.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP1 && resultCode == RESULT_OK) {
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
				mainBitmap1 = BitmapFactory.decodeFile(picturePath);
				mImage2.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap1 = thePic;

				mImage2.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP2 && resultCode == RESULT_OK) {
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
				mainBitmap2 = BitmapFactory.decodeFile(picturePath);
				mImage3.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap2 = thePic;
				mImage3.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP3 && resultCode == RESULT_OK) {
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
				mainBitmap3 = BitmapFactory.decodeFile(picturePath);
				mImage4.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap3 = thePic;
				mImage4.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP4 && resultCode == RESULT_OK) {
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
				mainBitmap4 = BitmapFactory.decodeFile(picturePath);
				mImage5.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap4 = thePic;
				mImage5.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP5 && resultCode == RESULT_OK) {
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
				mainBitmap5 = BitmapFactory.decodeFile(picturePath);
				mImage6.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap5 = thePic;
				mImage6.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP6 && resultCode == RESULT_OK) {
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
				mainBitmap6 = BitmapFactory.decodeFile(picturePath);
				mImage7.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mainBitmap6 = thePic;
				mImage7.setImageBitmap(thePic);
			}
		} else if (requestCode == PIC_CROP7 && resultCode == RESULT_OK) {
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
				mainBitmap7 = BitmapFactory.decodeFile(picturePath);
				mImage8.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mImage8.setImageBitmap(thePic);
				mainBitmap7 = thePic;
			}
		} else if (requestCode == PIC_CROP8 && resultCode == RESULT_OK) {
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
				mainBitmap8 = BitmapFactory.decodeFile(picturePath);
				mImage9.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mImage9.setImageBitmap(thePic);
				mainBitmap8 = thePic;
			}
		} else if (requestCode == PIC_CROP9 && resultCode == RESULT_OK) {
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
				mainBitmap9 = BitmapFactory.decodeFile(picturePath);
				mImage10.setImageBitmap(mainBitmap);
			} else {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				mImage10.setImageBitmap(thePic);
				mainBitmap9 = thePic;
			}
		}

		// save new cropped images in database
		Log.e("", "saveImages() : " + saveImages());
		int corePoolSize = 60;
		int maximumPoolSize = 80;
		int keepAliveTime = 10;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
				maximumPoolSize);
		Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
		custom_dialog1 = new Dialog(MeItemDetailsActivity.this);
		custom_dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		custom_dialog1.setContentView(getLayoutInflater().inflate(
				R.layout.progressdialog_custom, null));

		mSelltask = new SellItemTask(custom_dialog1)
				.executeOnExecutor(threadPoolExecutor);

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}

	public class SellItemTask extends AsyncTask<String, Integer, String> {
		private Dialog progress1;
		private Typeface mActionBarTypeface1;
		private int count;
		private String resultdata;

		public SellItemTask(Dialog progressDialog) {
			this.progress1 = progressDialog;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		public void onPreExecute() {
			// progress1.show();

		}

		@Override
		protected String doInBackground(String... urls) {
			// for (int progressValue = 0; progressValue < 100; progressValue++)
			// {
			// percentage.setText(progressValue + "%");
			// }
			if (isOnline()) {
				resultdata = saveImages();
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress1.dismiss();
			Log.e("", "resulttttttttttttttttt : " + result);

		}
	}

	private String saveImages() {
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
		if (byteArr10 == null) {
			byteArr10 = new byte[0];
		} else {
		}
		if (byteArr11 == null) {
			byteArr11 = new byte[0];
		} else {
		}
		if (byteArr12 == null) {
			byteArr12 = new byte[0];
		} else {
		}
		if (byteArr13 == null) {
			byteArr13 = new byte[0];
		} else {
		}
		if (byteArr14 == null) {
			byteArr14 = new byte[0];
		} else {
		}
		if (byteArr15 == null) {
			byteArr15 = new byte[0];
		} else {
		}
		if (byteArr16 == null) {
			byteArr16 = new byte[0];
		} else {
		}
		if (byteArr17 == null) {
			byteArr17 = new byte[0];
		} else {
		}
		if (byteArr18 == null) {
			byteArr18 = new byte[0];
		} else {
		}

		String postURL = "http://54.149.99.130/ws/add_crop_item_images.php?itemid=";

		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemIDDDDDD));
			Log.e("", "postURL : " + postURL);
			response = CustomHttpClient.executeHttpPostForImg10(postURL,
					params, itemimg0, byteArr0, itemimg1, byteArr1, itemimg2,
					byteArr2, itemimg3, byteArr3, itemimg4, byteArr4, itemimg5,
					byteArr5, itemimg6, byteArr6, itemimg7, byteArr7, itemimg8,
					byteArr8, itemimg9, byteArr9, "", byteArr10, "", byteArr11,
					"", byteArr12, "", byteArr13, "", byteArr14, "", byteArr15,
					"", byteArr16, "", byteArr17, "", byteArr18, mExtension0,
					mExtension1, mExtension2, mExtension3, mExtension4,
					mExtension5, mExtension6, mExtension7, mExtension8,
					mExtension9, "", "", "", "", "", "", "", "", "", "");

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
		Log.e("", "result : " + result);
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

	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	private void performCrop(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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

	private void performCrop1(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP1);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop2(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP2);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop3(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP3);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop4(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP4);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop5(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP5);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop6(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP6);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop7(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP7);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop8(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP8);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void performCrop9(Uri uri) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(uri, "image/*");
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
			startActivityForResult(cropIntent, PIC_CROP9);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Your device doesn't support photo cropping!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
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
		Toast.makeText(MeItemDetailsActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	private void getComments() {
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
			new RetriveCommentsTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}

	}

	public class RetriveCommentsTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public RetriveCommentsTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = getCommentsFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress.dismiss();
			Log.e("test", "result newwwwwwwwwwww : " + result);
			try {
				JSONObject responseObj = new JSONObject(result);

				JSONArray ja = responseObj.getJSONArray("data");
				if (ja.length() == 0) {
					// mCommentsList.setVisibility(View.GONE);
				} else {
					// mCommentsList.setVisibility(View.VISIBLE);
					if (mComList.size() != 0) {
						mComList.clear();
					}
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						mCom = new Comments();
						mCom.setItemComment(jo.getString("comment"));
						mCom.setTimeAgo(jo.getString("datecreated"));
						JSONArray ja1 = jo.getJSONArray("UserDetail");
						for (int j = 0; j < ja1.length(); j++) {
							JSONObject jo1 = (JSONObject) ja1.get(j);
							mCom.setUserName(jo1.getString("Username"));
							mCom.setUserPic(jo1.getString("pic"));
						}
						// mCommentItem.setItemId(jo.getString("Id"));
						// mCommentItem.setItemImage(jo.getString("Image"));
						// item.setItemName(jo.getString("Name"));
						// item.setUserId(jo.getString("UserId"));
						// item.setUserName(jo.getString("Username"));
						// // item.setDescription(jo.getString("Description"));
						// item.setPrice(jo.getString("Price"));
						// item.setLocation(jo.getString("Locations"));
						// item.setDays(jo.getString("NoOfDays"));
						// item.setIsLike(jo.getString("user_likes"));

						mComList.add(mCom);
					}

				}
				Log.e("", "Com list 11 ::::::::::::::::::::::::::::::::"
						+ mComList);
				// if (mComList.size() != 0) {
				mCommentsAdapter = new CommentsAdapter(
						MeItemDetailsActivity.this, mComList);
				mCommentsList.setAdapter(mCommentsAdapter);
				// }

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private String getCommentsFromWeb() {
		String postURL = "http://54.149.99.130/ws/get_comment.php?item_id=2";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("item_id", mItemID));

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
		if (getIntent() != null) {
			mItemID = getIntent().getStringExtra("itemid");
			mUserID = getIntent().getStringExtra("userid");
		}
		mComList = new ArrayList<Comments>();

		mCommentsList = (ListView) findViewById(R.id.list_comments);
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout listHeaderView = (RelativeLayout) inflater.inflate(
				R.layout.me_comment_list_header, null);
		mCommentsList.addHeaderView(listHeaderView);

		androidAQuery = new AQuery(this);
		mItemList = new ArrayList<SearchItem>();
		mUserImage = (ImageView) listHeaderView.findViewById(R.id.sell_image);
		mRelThumb1 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb1);
		mRelThumb2 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb2);
		mRelThumb3 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb3);
		mRelThumb4 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb4);
		mRelThumb5 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb5);
		mRelThumb6 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb6);
		mRelThumb7 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb7);
		mRelThumb8 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb8);
		mRelThumb9 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb9);
		mRelThumb10 = (RelativeLayout) listHeaderView
				.findViewById(R.id.relthumb10);

		mImage1 = (ImageView) listHeaderView.findViewById(R.id.image1);
		mImage2 = (ImageView) listHeaderView.findViewById(R.id.image2);
		mImage3 = (ImageView) listHeaderView.findViewById(R.id.image3);
		mImage4 = (ImageView) listHeaderView.findViewById(R.id.image4);
		mImage5 = (ImageView) listHeaderView.findViewById(R.id.image5);
		mImage6 = (ImageView) listHeaderView.findViewById(R.id.image6);
		mImage7 = (ImageView) listHeaderView.findViewById(R.id.image7);
		mImage8 = (ImageView) listHeaderView.findViewById(R.id.image8);
		mImage9 = (ImageView) listHeaderView.findViewById(R.id.image9);
		mImage10 = (ImageView) listHeaderView.findViewById(R.id.image10);

		mVideo1 = (VideoView) listHeaderView.findViewById(R.id.video1);
		mVideo2 = (VideoView) listHeaderView.findViewById(R.id.video2);
		mVideo3 = (VideoView) listHeaderView.findViewById(R.id.video3);
		mVideo4 = (VideoView) listHeaderView.findViewById(R.id.video4);
		mVideo5 = (VideoView) listHeaderView.findViewById(R.id.video5);
		mVideo6 = (VideoView) listHeaderView.findViewById(R.id.video6);
		mVideo7 = (VideoView) listHeaderView.findViewById(R.id.video7);
		mVideo8 = (VideoView) listHeaderView.findViewById(R.id.video8);
		mVideo9 = (VideoView) listHeaderView.findViewById(R.id.video9);
		mVideo10 = (VideoView) listHeaderView.findViewById(R.id.video10);

		mThumbImage1 = (ImageButton) listHeaderView.findViewById(R.id.thumb1);
		mThumbImage2 = (ImageButton) listHeaderView.findViewById(R.id.thumb2);
		mThumbImage3 = (ImageButton) listHeaderView.findViewById(R.id.thumb3);
		mThumbImage4 = (ImageButton) listHeaderView.findViewById(R.id.thumb4);
		mThumbImage5 = (ImageButton) listHeaderView.findViewById(R.id.thumb5);
		mThumbImage6 = (ImageButton) listHeaderView.findViewById(R.id.thumb6);
		mThumbImage7 = (ImageButton) listHeaderView.findViewById(R.id.thumb7);
		mThumbImage8 = (ImageButton) listHeaderView.findViewById(R.id.thumb8);
		mThumbImage9 = (ImageButton) listHeaderView.findViewById(R.id.thumb9);
		mThumbImage10 = (ImageButton) listHeaderView.findViewById(R.id.thumb10);

		mDelImage1 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img1);
		mDelImage2 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img2);
		mDelImage3 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img3);
		mDelImage4 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img4);
		mDelImage5 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img5);
		mDelImage6 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img6);
		mDelImage7 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img7);
		mDelImage8 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img8);
		mDelImage9 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img9);
		mDelImage10 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_img10);

		mDelVideo1 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video1);
		mDelVideo2 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video2);
		mDelVideo3 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video3);
		mDelVideo4 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video4);
		mDelVideo5 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video5);
		mDelVideo6 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video6);
		mDelVideo7 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video7);
		mDelVideo8 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video8);
		mDelVideo9 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video9);
		mDelVideo10 = (ImageButton) listHeaderView
				.findViewById(R.id.delete_video10);
		mName = (TextView) listHeaderView.findViewById(R.id.name);
		mPrice = (TextView) listHeaderView.findViewById(R.id.price);
		mTimeAgo = (TextView) listHeaderView.findViewById(R.id.hour_ago);
		mItemName = (TextView) listHeaderView.findViewById(R.id.item_name);
		mItemDescription = (TextView) listHeaderView
				.findViewById(R.id.item_desc);
		mItemPrice = (TextView) listHeaderView.findViewById(R.id.item_price);
		mItemLocation = (TextView) listHeaderView
				.findViewById(R.id.item_location);
		mItemLikes = (TextView) listHeaderView.findViewById(R.id.item_like);
		mItemViews = (TextView) listHeaderView.findViewById(R.id.item_views);
		mItemReportAbuse = (TextView) listHeaderView
				.findViewById(R.id.item_report_abuse);
		mReportAbuseImage = (ImageView) listHeaderView
				.findViewById(R.id.item_report_abuse_image);

		mName.setTypeface(mActionBarTypeface);
		mPrice.setTypeface(mActionBarTypeface);
		mTimeAgo.setTypeface(mActionBarTypeface);
		mItemName.setTypeface(mActionBarTypeface);
		mItemDescription.setTypeface(mActionBarTypeface);
		mItemPrice.setTypeface(mActionBarTypeface);
		mItemLocation.setTypeface(mActionBarTypeface);
		mItemLikes.setTypeface(mActionBarTypeface);
		mItemViews.setTypeface(mActionBarTypeface);
		mItemReportAbuse.setTypeface(mActionBarTypeface);

		mName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				Intent intent = new Intent(MeItemDetailsActivity.this,
						MeItemsTabActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
			}
		});
		mEditItemName = (EditText) findViewById(R.id.item_name_edit);
		mEditItemDescription = (EditText) findViewById(R.id.item_desc_edit);
		mEditItemName.setTypeface(mActionBarTypeface);
		mEditItemDescription.setTypeface(mActionBarTypeface);
		mEditItemPrice = (EditText) findViewById(R.id.item_price_edit);
		mEditItemPrice.setTypeface(mActionBarTypeface);
		// mLocationSpinner = (Spinner) findViewById(R.id.countrySpinner);
		Locale[] locale = Locale.getAvailableLocales();
		ArrayList<String> countries = new ArrayList<String>();
		String country;
		for (Locale loc : locale) {
			country = loc.getDisplayCountry();
			if (country.length() > 0 && !countries.contains(country)) {
				countries.add(country);
			}
		}
		Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
		// mLocationSpinner.setAdapter(new SpinnerCountryAdapter(this,
		// R.layout.spinner11, countries));
		//
		// myAdap = (ArrayAdapter<String>) mLocationSpinner.getAdapter();
		mDollar = (TextView) listHeaderView.findViewById(R.id.dollar);
		mDollar.setTypeface(mActionBarTypeface);
		mCountryAuto = (AutoCompleteTextView) listHeaderView
				.findViewById(R.id.country_auto);
		mCountryAuto.setTypeface(mActionBarTypeface);

		mEditIcon = (ImageButton) listHeaderView.findViewById(R.id.edit_icon);
		if((mUserID.equalsIgnoreCase(mUIDStr)))
			mEditIcon.setVisibility(View.VISIBLE);
		else 
			mEditIcon.setVisibility(View.INVISIBLE);

		
		mSaveIcon = (ImageButton) listHeaderView.findViewById(R.id.save_icon);
		mEditIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mEditIcon.setVisibility(View.GONE);
				mSaveIcon.setVisibility(View.VISIBLE);
				mEditItemName.setVisibility(View.VISIBLE);
				mItemName.setVisibility(View.INVISIBLE);
				mEditItemDescription.setVisibility(View.VISIBLE);
				mItemDescription.setVisibility(View.INVISIBLE);
				mEditItemPrice.setVisibility(View.VISIBLE);
				mItemPrice.setVisibility(View.INVISIBLE);
				mItemLocation.setVisibility(View.INVISIBLE);
				// mLocationSpinner.setVisibility(View.VISIBLE);
				mCountryAuto.setVisibility(View.VISIBLE);

				mEditItemName.setText(mItemName.getText().toString());
				mEditItemDescription.setText(mItemDescription.getText()
						.toString());
				String price = mItemPrice.getText().toString().replace("$","");
				price = price.replace("  ", "");
				mEditItemPrice.setText(price);
				mCountryAuto.setText(mItemLocation.getText().toString());

				// int spinnerPosition = myAdap.getPosition(mItemLocation
				// .getText().toString());
				// mLocationSpinner.setSelection(spinnerPosition, false);
				mCountryAuto.addTextChangedListener(new TextWatcher() {

					private PlacesTask placesTask;

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (mCountryAuto.length() >= 1) {
							// new
							// RetriveLocationListTask().execute(mCountryAuto
							// .getText().toString());

							placesTask = new PlacesTask();
							placesTask.execute(s.toString());
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
			}
		});
		mSaveIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEditItemName.getWindowToken(), 0);

				mSaveIcon.setVisibility(View.GONE);
				mEditIcon.setVisibility(View.VISIBLE);
				mItemName.setVisibility(View.VISIBLE);
				mEditItemName.setVisibility(View.INVISIBLE);
				mItemDescription.setVisibility(View.VISIBLE);
				mEditItemDescription.setVisibility(View.INVISIBLE);
				mEditItemPrice.setVisibility(View.INVISIBLE);
				mItemPrice.setVisibility(View.VISIBLE);
				mItemLocation.setVisibility(View.VISIBLE);
				// mLocationSpinner.setVisibility(View.INVISIBLE);
				mCountryAuto.setVisibility(View.INVISIBLE);

				mItemName.setText(mEditItemName.getText().toString());
				mItemDescription.setText(mEditItemDescription.getText()
						.toString());
				System.out.println("mEditItemPrice.getText().toString()"+mEditItemPrice.getText().toString());
				mItemPrice.setText("  "+mEditItemPrice.getText().toString());
				// mItemLocation.setText(mLocationSpinner.getSelectedItem()
				// .toString());
				mItemLocation.setText(mCountryAuto.getText().toString());

				saveUserDetails();
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
			mCountryAuto.setAdapter(adapter);

			// adapter.notifyDataSetChanged();
		}
	}

	public class RetriveLocationListTask extends
			AsyncTask<String, Void, String> {

		private SearchItem mSItem;

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
								MeItemDetailsActivity.this,
								listWithoutDuplicates1);
						mCountryAuto.setAdapter(adapter);
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
			params.add(new BasicNameValuePair("param", mCountryAuto.getText()
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

			Log.e("test", "result  : " + result);
			try {
				JSONObject responseObj = new JSONObject(result);

				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg").equalsIgnoreCase(
							"Successfully updated")) {
						// Toast.makeText(MeItemDetailsActivity.this,
						// "Successfully updated", Toast.LENGTH_SHORT)
						// .show();
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

	private String getSaveDetailsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/edit_user_item.php?uid=1&itemid=2&name=PantsPants&decription=Nice&price=20&location=pune";
		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", mUIDStr));
			params.add(new BasicNameValuePair("itemid", mItemID));
			params.add(new BasicNameValuePair("name", mItemName.getText()
					.toString()));
			params.add(new BasicNameValuePair("decription", mItemDescription
					.getText().toString()));
			params.add(new BasicNameValuePair("price", mItemPrice.getText()
					.toString()));
			params.add(new BasicNameValuePair("location", mItemLocation
					.getText().toString()));

			response = CustomHttpClient.executeHttpPost(postURL, params);

			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void getItemDetails() {
		Log.e("", "getItemDetailsgetItemDetailsgetItemDetails");
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
			new RetriveItemsDetailsTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	public class RetriveItemsDetailsTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String imagePath;
		private String[] itemImageArr;
		private String videoPath;
		private String[] itemVideoArr;
		private String thumbPath;
		private String[] itemThumbArr;
		private String resultdata;

		public RetriveItemsDetailsTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = getItemsDetailsFromWeb();

			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			Log.e("test", "result  : " + result);
			progress.dismiss();
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MeItemDetailsActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getItemDetails();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				Log.e("", "result me details : " + result);
				try {
					JSONObject responseObj = new JSONObject(result);
					JSONArray ja = responseObj.getJSONArray("data");

					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						item = new Item();
						item.setItemId(jo.getString("Id"));
						item.setItemImage(jo.getString("Image"));
						item.setItemName(jo.getString("Name"));
						item.setUserId(jo.getString("UserId"));
						item.setUserName(jo.getString("Username"));
						item.setDescription(jo.getString("Description"));
						item.setPrice(jo.getString("Price"));
						item.setLocation(jo.getString("Locations"));
						item.setDays(jo.getString("NoOfDays"));
						item.setUserPic(jo.getString("Userpic"));
						item.setVideo(jo.getString("Video"));
						item.setItemTotalLikes(jo.getString("Total_likes"));
						item.setItemViewedCount(jo.getString("Viewed_count"));
						item.setThumb(jo.getString("Thumb"));
					}

					mItemIDDDDDD = item.getItemId();
					/********* User image ************/
					String userImage = item.getUserPic();
					userImage = userImage.replace("\\/", "/");
					Log.e("", "imageeeeeeeee : " + userImage);
					androidAQuery.id(mUserImage).image(userImage, false, false,
							0, R.drawable.user_photo);

					/*********** Item images *********/
					imagePath = item.getItemImage();
					if (!imagePath.equalsIgnoreCase("[]")) {
						Log.e("", "imagePath  :" + imagePath);
						if (!item.getItemImage().contains(",")) {
							imagePath = imagePath.replace("\\/", "/")
									.replace("[", "").replace("]", "")
									.replace("\"", "");
							Log.e("", "imageeeeeeeee : " + imagePath);
							mImage1.setVisibility(View.VISIBLE);
							androidAQuery.id(mImage1).image(imagePath, false,
									false, 0, R.drawable.defaultimage2);

							// crop
							mImage1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// picUri = getOutputMediaFileUri();
									// File file = new File(imagePath);
									// Uri uri = Uri.fromFile(file);
									if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(imagePath);
										performCrop(uri);
									}
									/*File imgFile = new  File(imagePath);

									if(imgFile.exists()){

									    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

									  

									    mImage1.setImageBitmap(myBitmap);

									}*/

								}
							});
							if((mUserID.equalsIgnoreCase(mUIDStr)))
								mDelImage1.setVisibility(View.VISIBLE);
							else 
								mDelImage1.setVisibility(View.INVISIBLE);
							//mDelImage1.setVisibility(View.VISIBLE);
							mDelImage1
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											mFlag = 1;
											mImageName = imagePath;
											delete();
										}
									});
						} else {
							itemImageArr = imagePath.split(",");
							switch (itemImageArr.length) {
							case 1:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// // crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										picUri = getOutputMediaFileUri();
										// Bitmap mainBitmap = BitmapFactory
										// .decodeFile(itemImageArr[0]);
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);

									}
								});

								break;
							case 2:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// // crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// picUri = getOutputMediaFileUri();
										// File file = new
										// File(itemImageArr[0]);
										// Uri uri = Uri.fromFile(file);
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);

									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									private Uri picUri1;

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
											Uri uri = Uri.parse(itemImageArr[1]);
											performCrop(uri);
										}
									}
								});

								break;
							case 3:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 0;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
											Uri uri = Uri.parse(itemImageArr[0]);
											performCrop(uri);
										}
									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								//mDelImage1.setVisibility(View.VISIBLE);
							//	mDelImage2.setVisibility(View.VISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								//mDelImage1.setVisibility(View.VISIBLE);
								//mDelImage3.setVisibility(View.VISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);
										}
									}
								});
								break;
							case 4:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// // crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);	
										}

									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}

										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);
										}
									}
								});

								break;
							case 5:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// // crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);	
										}

									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);
									}
								}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// // crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[4]);
										performCrop(uri);
										}
									}
								});

								break;
							case 6:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);
										}

									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[4]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[5] = itemImageArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[5] : "
										+ itemImageArr[5]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemImageArr[5], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemImageArr[5];
												delete();
											}
										});
								// crop
								mImage6.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[5]);
										performCrop(uri);
										}
									}
								});

								break;
							case 7:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);
									}
									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// // crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[4]);
										performCrop(uri);
										}
									}
								});

								itemImageArr[5] = itemImageArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[5] : "
										+ itemImageArr[5]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemImageArr[5], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemImageArr[5];
												delete();
											}
										});
								// // crop
								mImage6.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr)))
										{	
										Uri uri = Uri.parse(itemImageArr[5]);
										performCrop(uri);
										}
										}
								});

								itemImageArr[6] = itemImageArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[6] : "
										+ itemImageArr[6]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemImageArr[6], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemImageArr[6];
												delete();
											}
										});
								// crop
								mImage7.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[6]);
										performCrop(uri);
										}
									}
								});
								break;
							case 8:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);}
									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);}
									}
								});
								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});

								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[4]);
										performCrop(uri);}
									}
								});

								itemImageArr[5] = itemImageArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[5] : "
										+ itemImageArr[5]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemImageArr[5], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemImageArr[5];
												delete();
											}
										});
								// crop
								mImage6.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[5]);
										performCrop(uri);}

									}
								});

								itemImageArr[6] = itemImageArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[6] : "
										+ itemImageArr[6]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemImageArr[6], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemImageArr[6];
												delete();
											}
										});
								// crop
								mImage7.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[6]);
										performCrop(uri);}

									}
								});

								itemImageArr[7] = itemImageArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[7] : "
										+ itemImageArr[7]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemImageArr[7], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemImageArr[7];
												delete();
											}
										});
								// crop
								mImage8.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[7]);
										performCrop(uri);}

									}
								});

								break;
							case 9:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);}
									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);}

									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// // crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[4]);
										performCrop(uri);}

									}
								});

								itemImageArr[5] = itemImageArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[5] : "
										+ itemImageArr[5]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemImageArr[5], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemImageArr[5];
												delete();
											}
										});
								// crop
								mImage6.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[5]);
										performCrop(uri);}
									}
								});

								itemImageArr[6] = itemImageArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[6] : "
										+ itemImageArr[6]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemImageArr[6], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemImageArr[6];
												delete();
											}
										});
								// crop
								mImage7.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[6]);
										performCrop(uri);}
									}
								});

								itemImageArr[7] = itemImageArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[7] : "
										+ itemImageArr[7]);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								androidAQuery.id(mImage8).image(
										itemImageArr[7], false, false, 0,
										R.drawable.defaultimage2);
								mDelImage8.setVisibility(View.VISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemImageArr[7];
												delete();
											}
										});
								// crop
								mImage8.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[7]);
										performCrop(uri);}

									}
								});

								itemImageArr[8] = itemImageArr[8]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[8] : "
										+ itemImageArr[8]);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								androidAQuery.id(mImage9).image(
										itemImageArr[8], false, false, 0,
										R.drawable.defaultimage2);
								mDelImage9.setVisibility(View.VISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemImageArr[8];
												delete();
											}
										});
								// crop
								mImage9.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[8]);
										performCrop(uri);}

									}
								});

								break;
							case 10:
								itemImageArr[0] = itemImageArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[0] : "
										+ itemImageArr[0]);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);								androidAQuery.id(mImage1).image(
										itemImageArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mDelImage1.setVisibility(View.VISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemImageArr[0];
												delete();
											}
										});
								// crop
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);}

									}
								});

								itemImageArr[1] = itemImageArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[1] : "
										+ itemImageArr[1]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemImageArr[1], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemImageArr[1];
												delete();
											}
										});
								// // crop
								mImage2.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[1]);
										performCrop(uri);}
									}
								});

								itemImageArr[2] = itemImageArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[2] : "
										+ itemImageArr[2]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemImageArr[2], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemImageArr[2];
												delete();
											}
										});
								// crop
								mImage3.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[2]);
										performCrop(uri);}
									}
								});

								itemImageArr[3] = itemImageArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[3] : "
										+ itemImageArr[3]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemImageArr[3], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemImageArr[3];
												delete();
											}
										});
								// crop
								mImage4.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[3]);
										performCrop(uri);}
									}
								});

								itemImageArr[4] = itemImageArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[4] : "
										+ itemImageArr[4]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemImageArr[4], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);	
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemImageArr[4];
												delete();
											}
										});
								// crop
								mImage5.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[0]);
										performCrop(uri);}
									}
								});

								itemImageArr[5] = itemImageArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[5] : "
										+ itemImageArr[5]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemImageArr[5], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);							
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemImageArr[5];
												delete();
											}
										});
								// crop
								mImage6.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[5]);
										performCrop(uri);}
									}
								});

								itemImageArr[6] = itemImageArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[6] : "
										+ itemImageArr[6]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemImageArr[6], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);	
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemImageArr[6];
												delete();
											}
										});
								// crop
								mImage7.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[6]);
										performCrop(uri);}
									}
								});

								itemImageArr[7] = itemImageArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[7] : "
										+ itemImageArr[7]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemImageArr[7], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);	
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemImageArr[7];
												delete();
											}
										});
								// crop
								mImage8.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[7]);
										performCrop(uri);}

									}
								});

								itemImageArr[8] = itemImageArr[8]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[8] : "
										+ itemImageArr[8]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemImageArr[8], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);	
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemImageArr[8];
												delete();
											}
										});
								// crop
								mImage9.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[8]);
										performCrop(uri);}
									}
								});

								itemImageArr[9] = itemImageArr[9]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemImageArr[9] : "
										+ itemImageArr[9]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemImageArr[9], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
									mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemImageArr[9];
												delete();
											}
										});
								// crop
								mImage10.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if((mUserID.equalsIgnoreCase(mUIDStr))){
										Uri uri = Uri.parse(itemImageArr[9]);
										performCrop(uri);}
									}
								});

								break;
							default:
								break;
							}

						}
					}

					/************* Item Thumb images **************/
					videoPath = item.getVideo();
					Log.e("", "videoPath : " + videoPath);
					if (!videoPath.equalsIgnoreCase("[]")) {
						if (!item.getVideo().contains(",")) {
							videoPath = videoPath.replace("\\/", "/")
									.replace("[", "").replace("]", "")
									.replace("\"", "");
						} else {
							itemVideoArr = videoPath.split(",");
							switch (itemVideoArr.length) {
							case 1:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);
								break;
							case 2:
								Log.e("", " itemVideoArr[0] : "
										+ itemVideoArr[0]
										+ "itemVideoArr[1] : "
										+ itemVideoArr[1]);
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);
								break;
							case 3:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);
								break;
							case 4:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								break;
							case 5:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								itemVideoArr[4] = itemVideoArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[4] : "
										+ itemVideoArr[4]);
								break;
							case 6:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								itemVideoArr[4] = itemVideoArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[4] : "
										+ itemVideoArr[4]);

								itemVideoArr[5] = itemVideoArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[5] : "
										+ itemVideoArr[5]);
								break;
							case 7:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								itemVideoArr[4] = itemVideoArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[4] : "
										+ itemVideoArr[4]);

								itemVideoArr[5] = itemVideoArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[5] : "
										+ itemVideoArr[5]);

								itemVideoArr[6] = itemVideoArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[6] : "
										+ itemVideoArr[6]);
								break;
							case 8:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								itemVideoArr[4] = itemVideoArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[4] : "
										+ itemVideoArr[4]);

								itemVideoArr[5] = itemVideoArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[5] : "
										+ itemVideoArr[5]);

								itemVideoArr[6] = itemVideoArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[6] : "
										+ itemVideoArr[6]);

								itemVideoArr[7] = itemVideoArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[7] : "
										+ itemVideoArr[7]);
								break;
							case 9:
								itemVideoArr[0] = itemVideoArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[0] : "
										+ itemVideoArr[0]);

								itemVideoArr[1] = itemVideoArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[1] : "
										+ itemVideoArr[1]);

								itemVideoArr[2] = itemVideoArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[2] : "
										+ itemVideoArr[2]);

								itemVideoArr[3] = itemVideoArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[3] : "
										+ itemVideoArr[3]);

								itemVideoArr[4] = itemVideoArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[4] : "
										+ itemVideoArr[4]);

								itemVideoArr[5] = itemVideoArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[5] : "
										+ itemVideoArr[5]);

								itemVideoArr[6] = itemVideoArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[6] : "
										+ itemVideoArr[6]);

								itemVideoArr[7] = itemVideoArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[7] : "
										+ itemVideoArr[7]);

								itemVideoArr[8] = itemVideoArr[8]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemVideoArr[8] : "
										+ itemVideoArr[8]);
								break;

							default:
								break;
							}
						}
					}
					thumbPath = item.getThumb();
					if (!thumbPath.equalsIgnoreCase("")) {
						if (!item.getThumb().contains(",")) {
							thumbPath = thumbPath.replace("\\/", "/")
									.replace("[", "").replace("]", "")
									.replace("\"", "");
							Log.e("", "thumbPaththumbPaththumbPath : "
									+ thumbPath);
							mImage10.setVisibility(View.VISIBLE);
							androidAQuery.id(mImage10).image(thumbPath, false,
									false, 0, R.drawable.defaultimage2);
							if((mUserID.equalsIgnoreCase(mUIDStr)))
								mDelImage10.setVisibility(View.VISIBLE);
							else 
								mDelImage10.setVisibility(View.INVISIBLE);
							Log.e("", "gfvvvgb");
							mDelImage10
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Log.e("", "aaaaaaaaaaaaaa");
											mFlag = 10;
											mImageName = thumbPath;
											delete();
										}
									});
							mDelImage10
									.setOnFocusChangeListener(new OnFocusChangeListener() {

										@Override
										public void onFocusChange(View v,
												boolean hasFocus) {
											if (hasFocus) {
												Log.e("", "focussssssssss");
											}
										}
									});

							mRelThumb10.setVisibility(View.VISIBLE);
							mRelThumb10
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
										/*	Intent intent = new Intent(
													MeItemDetailsActivity.this,
													VideoActivity.class);
											intent.putExtra("video", videoPath);
											startActivity(intent);
											overridePendingTransition(
													R.anim.slide_in_right,
													R.anim.slide_out_left);*/
											playVideo(videoPath);
										}
									});
							mThumbImage10
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											/*Intent intent = new Intent(
													MeItemDetailsActivity.this,
													VideoActivity.class);
											intent.putExtra("video", videoPath);
											startActivity(intent);
											overridePendingTransition(
													R.anim.slide_in_right,
													R.anim.slide_out_left);*/
											playVideo(videoPath);
										}
									});

						} else {

							itemThumbArr = thumbPath.split(",");
							switch (itemThumbArr.length) {
							case 1:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								Log.e("", "kkkkkkkyt");
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												Log.e("", "bbbbbbbb");
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});
								mDelImage10
										.setOnTouchListener(new OnTouchListener() {

											@Override
											public boolean onTouch(View v,
													MotionEvent event) {
												Log.e("", "ccccccccccc");
												mFlag = 10;
												mImageName = thumbPath;
												delete();
												return false;
											}
										});

								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});

								break;
							case 2:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								break;
							case 3:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								break;
							case 4:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});

								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[2];
												delete();
											}
										});

								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});
								break;
							case 5:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});

								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});
								itemThumbArr[4] = itemThumbArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[4] : "
										+ itemThumbArr[4]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemThumbArr[4], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb6.setVisibility(View.VISIBLE);
								mRelThumb6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								mThumbImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemThumbArr[4];
												delete();
											}
										});
								break;
							case 6:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});

								itemThumbArr[4] = itemThumbArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[4] : "
										+ itemThumbArr[4]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemThumbArr[4], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb6.setVisibility(View.VISIBLE);
								mRelThumb6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								mThumbImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemThumbArr[4];
												delete();
											}
										});
								itemThumbArr[5] = itemThumbArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[5] : "
										+ itemThumbArr[5]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemThumbArr[5], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb5.setVisibility(View.VISIBLE);
								mRelThumb5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								mThumbImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemThumbArr[5];
												delete();
											}
										});

								itemThumbArr[6] = itemThumbArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[6] : "
										+ itemThumbArr[6]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemThumbArr[6], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb4.setVisibility(View.VISIBLE);
								mRelThumb4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								mThumbImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemThumbArr[6];
												delete();
											}
										});
								break;
							case 7:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
								/*				Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
								/*				Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});

								itemThumbArr[4] = itemThumbArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[4] : "
										+ itemThumbArr[4]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemThumbArr[4], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb6.setVisibility(View.VISIBLE);
								mRelThumb6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								mThumbImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemThumbArr[4];
												delete();
											}
										});
								itemThumbArr[5] = itemThumbArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[5] : "
										+ itemThumbArr[5]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemThumbArr[5], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb5.setVisibility(View.VISIBLE);
								mRelThumb5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								mThumbImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemThumbArr[5];
												delete();
											}
										});
								itemThumbArr[6] = itemThumbArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[6] : "
										+ itemThumbArr[6]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemThumbArr[6], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb4.setVisibility(View.VISIBLE);
								mRelThumb4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								mThumbImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemThumbArr[6];
												delete();
											}
										});
								itemThumbArr[7] = itemThumbArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[7] : "
										+ itemThumbArr[7]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemThumbArr[7], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb3.setVisibility(View.VISIBLE);
								mRelThumb3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								mThumbImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemThumbArr[7];
												delete();
											}
										});
								break;
							case 8:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
									/*			Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});

								itemThumbArr[4] = itemThumbArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[4] : "
										+ itemThumbArr[4]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemThumbArr[4], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb6.setVisibility(View.VISIBLE);
								mRelThumb6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								mThumbImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemThumbArr[4];
												delete();
											}
										});
								itemThumbArr[5] = itemThumbArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[5] : "
										+ itemThumbArr[5]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemThumbArr[5], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb5.setVisibility(View.VISIBLE);
								mRelThumb5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								mThumbImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemThumbArr[5];
												delete();
											}
										});
								itemThumbArr[6] = itemThumbArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[6] : "
										+ itemThumbArr[6]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemThumbArr[6], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb4.setVisibility(View.VISIBLE);
								mRelThumb4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								mThumbImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemThumbArr[6];
												delete();
											}
										});
								itemThumbArr[7] = itemThumbArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[7] : "
										+ itemThumbArr[7]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemThumbArr[7], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb3.setVisibility(View.VISIBLE);
								mRelThumb3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								mThumbImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemThumbArr[7];
												delete();
											}
										});

								itemThumbArr[8] = itemThumbArr[8]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[8] : "
										+ itemThumbArr[8]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemThumbArr[8], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb2.setVisibility(View.VISIBLE);
								mRelThumb2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[8]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[8]);
											}
										});
								mThumbImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[8]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[8]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemThumbArr[8];
												delete();
											}
										});

								break;
							case 9:
								itemThumbArr[0] = itemThumbArr[0]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[0] : "
										+ itemThumbArr[0]);
								mImage10.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage10).image(
										itemThumbArr[0], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb10.setVisibility(View.VISIBLE);
								mRelThumb10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								mThumbImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[0]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[0]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage10.setVisibility(View.VISIBLE);
								else 
									mDelImage10.setVisibility(View.INVISIBLE);
								mDelImage10
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 10;
												mImageName = itemThumbArr[0];
												delete();
											}
										});

								itemThumbArr[1] = itemThumbArr[1]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[1] : "
										+ itemThumbArr[1]);
								mImage9.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage9).image(
										itemThumbArr[1], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb9.setVisibility(View.VISIBLE);
								mRelThumb9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								mThumbImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[1]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[1]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage9.setVisibility(View.VISIBLE);
								else 
									mDelImage9.setVisibility(View.INVISIBLE);
								mDelImage9
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 9;
												mImageName = itemThumbArr[1];
												delete();
											}
										});
								itemThumbArr[2] = itemThumbArr[2]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[2] : "
										+ itemThumbArr[2]);
								mImage8.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage8).image(
										itemThumbArr[2], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb8.setVisibility(View.VISIBLE);
								mRelThumb8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								mThumbImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[2]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[2]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage8.setVisibility(View.VISIBLE);
								else 
									mDelImage8.setVisibility(View.INVISIBLE);
								mDelImage8
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 8;
												mImageName = itemThumbArr[2];
												delete();
											}
										});
								itemThumbArr[3] = itemThumbArr[3]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[3] : "
										+ itemThumbArr[3]);
								mImage7.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage7).image(
										itemThumbArr[3], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb7.setVisibility(View.VISIBLE);
								mRelThumb7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								mThumbImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[3]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[3]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage7.setVisibility(View.VISIBLE);
								else 
									mDelImage7.setVisibility(View.INVISIBLE);
								mDelImage7
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 7;
												mImageName = itemThumbArr[3];
												delete();
											}
										});

								itemThumbArr[4] = itemThumbArr[4]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[4] : "
										+ itemThumbArr[4]);
								mImage6.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage6).image(
										itemThumbArr[4], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb6.setVisibility(View.VISIBLE);
								mRelThumb6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								mThumbImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[4]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[4]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage6.setVisibility(View.VISIBLE);
								else 
									mDelImage6.setVisibility(View.INVISIBLE);
								mDelImage6
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 6;
												mImageName = itemThumbArr[4];
												delete();
											}
										});
								itemThumbArr[5] = itemThumbArr[5]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[5] : "
										+ itemThumbArr[5]);
								mImage5.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage5).image(
										itemThumbArr[5], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb5.setVisibility(View.VISIBLE);
								mRelThumb5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								mThumbImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[5]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[5]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage5.setVisibility(View.VISIBLE);
								else 
									mDelImage5.setVisibility(View.INVISIBLE);
								mDelImage5
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 5;
												mImageName = itemThumbArr[5];
												delete();
											}
										});
								itemThumbArr[6] = itemThumbArr[6]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[6] : "
										+ itemThumbArr[6]);
								mImage4.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage4).image(
										itemThumbArr[6], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb4.setVisibility(View.VISIBLE);
								mRelThumb4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								mThumbImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[6]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[6]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage4.setVisibility(View.VISIBLE);
								else 
									mDelImage4.setVisibility(View.INVISIBLE);
								mDelImage4
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 4;
												mImageName = itemThumbArr[6];
												delete();
											}
										});
								itemThumbArr[7] = itemThumbArr[7]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[7] : "
										+ itemThumbArr[7]);
								mImage3.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage3).image(
										itemThumbArr[7], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb3.setVisibility(View.VISIBLE);
								mRelThumb3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
								/*				Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								mThumbImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[7]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[7]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage3.setVisibility(View.VISIBLE);
								else 
									mDelImage3.setVisibility(View.INVISIBLE);
								mDelImage3
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 3;
												mImageName = itemThumbArr[7];
												delete();
											}
										});

								itemThumbArr[8] = itemThumbArr[8]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[8] : "
										+ itemThumbArr[8]);
								mImage2.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage2).image(
										itemThumbArr[8], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb2.setVisibility(View.VISIBLE);
								mRelThumb2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[8]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[8]);
											}
										});
								mThumbImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
										/*		Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[8]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[8]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage2.setVisibility(View.VISIBLE);
								else 
									mDelImage2.setVisibility(View.INVISIBLE);
								mDelImage2
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 2;
												mImageName = itemThumbArr[8];
												delete();
											}
										});

								itemThumbArr[9] = itemThumbArr[9]
										.replace("\\/", "/").replace("[", "")
										.replace("]", "").replace("\"", "");
								Log.e("", "itemThumbArr[9] : "
										+ itemThumbArr[9]);
								mImage1.setVisibility(View.VISIBLE);
								androidAQuery.id(mImage1).image(
										itemThumbArr[9], false, false, 0,
										R.drawable.defaultimage2);
								mRelThumb1.setVisibility(View.VISIBLE);
								mRelThumb1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[9]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[9]);
											}
										});
								mThumbImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
											/*	Intent intent = new Intent(
														MeItemDetailsActivity.this,
														VideoActivity.class);
												intent.putExtra("video",
														itemVideoArr[9]);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);*/
												playVideo(itemVideoArr[9]);
											}
										});
								if((mUserID.equalsIgnoreCase(mUIDStr)))
									mDelImage1.setVisibility(View.VISIBLE);
								else 
									mDelImage1.setVisibility(View.INVISIBLE);
								mDelImage1
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												mFlag = 1;
												mImageName = itemThumbArr[9];
												delete();
											}
										});
								break;
							default:
								break;
							}

						}
					}

					// /********* Item Videos *****************/
					// videoPath = item.getVideo();
					// if (!videoPath.equalsIgnoreCase("[]")) {
					// Log.e("", "videoPath  :" + videoPath);
					// if (!item.getVideo().contains(",")) {
					// videoPath = videoPath.replace("\\/", "/")
					// .replace("[", "").replace("]", "")
					// .replace("\"", "");
					// Log.e("", "videoPathvideoPathvideoPath : " + videoPath);
					// mVideo10.setVisibility(View.VISIBLE);
					// MediaController mediaController = new MediaController(
					// MeItemDetailsActivity.this);
					// mediaController.setAnchorView(mVideo10);
					//
					// mVideo10.setMediaController(mediaController);
					// mVideo10.setVideoURI(Uri.parse(videoPath));
					// mVideo10.requestFocus();
					// mVideo10.start();
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10.setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = videoPath;
					// delete();
					// }
					// });
					// } else {
					// itemVideoArr = videoPath.split(",");
					// switch (itemVideoArr.length) {
					// case 1:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// break;
					// case 2:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					//
					// mVideo10.setVisibility(View.VISIBLE);
					//
					// // Creating MediaController
					// MediaController mediaController = new MediaController(
					// MeItemDetailsActivity.this);
					// mediaController.setAnchorView(mVideo10);
					//
					// // specify the location of media file
					// Uri uri = Uri.parse(itemVideoArr[0]);
					//
					// // Setting MediaController and URI, then starting
					// // the videoView
					// mVideo10.setMediaController(mediaController);
					// mVideo10.setVideoURI(uri);
					// mVideo10.requestFocus();
					// mVideo10.setOnPreparedListener(new OnPreparedListener() {
					//
					// @Override
					// public void onPrepared(MediaPlayer mp) {
					// mp.start();
					//
					// }
					// });
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// Bitmap bMap1 = null;
					// try {
					// bMap1 = Media.getBitmap(
					// MeItemDetailsActivity.this
					// .getContentResolver(), Uri
					// .parse(itemVideoArr[1].trim()));
					// } catch (FileNotFoundException e) {
					// e.printStackTrace();
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					// mImage9.setImageBitmap(bMap1);
					// mImage9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					// break;
					// case 3:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// break;
					// case 4:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					// break;
					// case 5:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					// break;
					// case 6:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					//
					// itemVideoArr[5] = itemVideoArr[5]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
					// mVideo5.setVisibility(View.VISIBLE);
					// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[5];
					// delete();
					// }
					// });
					// break;
					// case 7:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					//
					// itemVideoArr[5] = itemVideoArr[5]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
					// mVideo5.setVisibility(View.VISIBLE);
					// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
					// mDelVideo5.setVisibility(View.VISIBLE);
					// mDelVideo5
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[5];
					// delete();
					// }
					// });
					//
					// itemVideoArr[6] = itemVideoArr[6]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
					// mVideo4.setVisibility(View.VISIBLE);
					// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
					// mDelVideo4.setVisibility(View.VISIBLE);
					// mDelVideo4
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[6];
					// delete();
					// }
					// });
					// break;
					// case 8:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					//
					// itemVideoArr[5] = itemVideoArr[5]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
					// mVideo5.setVisibility(View.VISIBLE);
					// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
					// mDelVideo5.setVisibility(View.VISIBLE);
					// mDelVideo5
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[5];
					// delete();
					// }
					// });
					//
					// itemVideoArr[6] = itemVideoArr[6]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
					// mVideo4.setVisibility(View.VISIBLE);
					// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
					// mDelVideo4.setVisibility(View.VISIBLE);
					// mDelVideo4
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[6];
					// delete();
					// }
					// });
					//
					// itemVideoArr[7] = itemVideoArr[7]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
					// mVideo3.setVisibility(View.VISIBLE);
					// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
					// mDelVideo3.setVisibility(View.VISIBLE);
					// mDelVideo3
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[7];
					// delete();
					// }
					// });
					// break;
					// case 9:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					//
					// itemVideoArr[5] = itemVideoArr[5]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
					// mVideo5.setVisibility(View.VISIBLE);
					// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
					// mDelVideo5.setVisibility(View.VISIBLE);
					// mDelVideo5
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[5];
					// delete();
					// }
					// });
					//
					// itemVideoArr[6] = itemVideoArr[6]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
					// mVideo4.setVisibility(View.VISIBLE);
					// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
					// mDelVideo4.setVisibility(View.VISIBLE);
					// mDelVideo4
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[6];
					// delete();
					// }
					// });
					//
					// itemVideoArr[7] = itemVideoArr[7]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
					// mVideo3.setVisibility(View.VISIBLE);
					// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
					// mDelVideo3.setVisibility(View.VISIBLE);
					// mDelVideo3
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[7];
					// delete();
					// }
					// });
					//
					// itemVideoArr[8] = itemVideoArr[8]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[8] : " + itemVideoArr[8]);
					// mVideo2.setVisibility(View.VISIBLE);
					// mVideo2.setVideoURI(Uri.parse(itemVideoArr[8]));
					// mDelVideo2.setVisibility(View.VISIBLE);
					// mDelVideo2
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[8];
					// delete();
					// }
					// });
					// break;
					// case 10:
					// itemVideoArr[0] = itemVideoArr[0]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
					// mVideo10.setVisibility(View.VISIBLE);
					// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
					// mDelVideo10.setVisibility(View.VISIBLE);
					// mDelVideo10
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[0];
					// delete();
					// }
					// });
					//
					// itemVideoArr[1] = itemVideoArr[1]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
					// mVideo9.setVisibility(View.VISIBLE);
					// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
					// mDelVideo9.setVisibility(View.VISIBLE);
					// mDelVideo9
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[1];
					// delete();
					// }
					// });
					//
					// itemVideoArr[2] = itemVideoArr[2]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
					// mVideo8.setVisibility(View.VISIBLE);
					// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
					// mDelVideo8.setVisibility(View.VISIBLE);
					// mDelVideo8
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[2];
					// delete();
					// }
					// });
					//
					// itemVideoArr[3] = itemVideoArr[3]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
					// mVideo7.setVisibility(View.VISIBLE);
					// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
					// mDelVideo7.setVisibility(View.VISIBLE);
					// mDelVideo7
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[3];
					// delete();
					// }
					// });
					//
					// itemVideoArr[4] = itemVideoArr[4]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
					// mVideo6.setVisibility(View.VISIBLE);
					// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
					// mDelVideo6.setVisibility(View.VISIBLE);
					// mDelVideo6
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[4];
					// delete();
					// }
					// });
					//
					// itemVideoArr[5] = itemVideoArr[5]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
					// mVideo5.setVisibility(View.VISIBLE);
					// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
					// mDelVideo5.setVisibility(View.VISIBLE);
					// mDelVideo5
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[5];
					// delete();
					// }
					// });
					//
					// itemVideoArr[6] = itemVideoArr[6]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
					// mVideo4.setVisibility(View.VISIBLE);
					// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
					// mDelVideo4.setVisibility(View.VISIBLE);
					// mDelVideo4
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[6];
					// delete();
					// }
					// });
					//
					// itemVideoArr[7] = itemVideoArr[7]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
					// mVideo3.setVisibility(View.VISIBLE);
					// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
					// mDelVideo3.setVisibility(View.VISIBLE);
					// mDelVideo3
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[7];
					// delete();
					// }
					// });
					//
					// itemVideoArr[8] = itemVideoArr[8]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[8] : " + itemVideoArr[8]);
					// mVideo2.setVisibility(View.VISIBLE);
					// mVideo2.setVideoURI(Uri.parse(itemVideoArr[8]));
					// mDelVideo2.setVisibility(View.VISIBLE);
					// mDelVideo2
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[8];
					// delete();
					// }
					// });
					//
					// itemVideoArr[9] = itemVideoArr[9]
					// .replace("\\/", "/").replace("[", "")
					// .replace("]", "").replace("\"", "");
					// Log.e("", "itemVideoArr[9] : " + itemVideoArr[9]);
					// mVideo1.setVisibility(View.VISIBLE);
					// mVideo1.setVideoURI(Uri.parse(itemVideoArr[9]));
					// mDelVideo1.setVisibility(View.VISIBLE);
					// mDelVideo1
					// .setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// mImageName = itemVideoArr[9];
					// delete();
					// }
					// });
					// break;
					// default:
					// break;
					// }
					//
					// }
					// }

					/************************************/
					mName.setText(item.getUserName());
					mPrice.setText(item.getPrice());

					String ago = item.getDays();
					if (ago.contains("d")) {
						ago = ago.replace("d", "");
						if (ago.trim().equals("1")) {
							ago = ago + "day ago";
						} else {
							ago = ago + "days ago";
						}
					} else if (ago.contains("m")) {
						ago = ago.replace("m", "");
						if (ago.trim().equals("1")) {
							ago = ago + "minute ago";
						} else {
							ago = ago + "minutes ago";
						}
					} else if (ago.contains("M")) {
						ago = ago.replace("M", "");
						if (ago.trim().equals("1")) {
							ago = ago + "month ago";
						} else {
							ago = ago + "months ago";
						}
					} else if (ago.contains("y")) {
						ago = ago.replace("y", "");
						if (ago.trim().equals("1")) {
							ago = ago + "year ago";
						} else {
							ago = ago + "years ago";
						}
					} else if (ago.contains("h")) {
						ago = ago.replace("h", "");
						if (ago.trim().equals("1")) {
							ago = ago + "hour ago";
						} else {
							ago = ago + "hours ago";
						}
					} else if (ago.contains("s")) {
						ago = "now";
					}
					mTimeAgo.setText("Posted " + ago);
					mItemName.setText(item.getItemName());
					mItemDescription.setText(item.getDescription());
					mPrice.setText(item.getPrice());
					mItemLocation.setText(item.getLocation());
					mItemPrice.setText("  "+item.getPrice().toString()
							.replace("$", ""));
					String likes = item.getItemTotalLikes();
					if (likes.equals("1")) {
						mItemLikes.setText(likes + " like");
					} else {
						mItemLikes.setText(likes + " likes");
					}
					String views = item.getItemViewedCount();
					if (views.equals("1")) {
						mItemViews.setText(views + " view");
					} else {
						mItemViews.setText(views + " views");
					}

					mItemReportAbuse.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MeItemDetailsActivity.this);
							builder.setMessage("Are you sure you want to report abuse for this item ?");
							builder.setIcon(R.drawable.alert);
							builder.setTitle("Report Abuse");
							builder.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (checkInternetConnection()) {
												ProgressDialog progress = new ProgressDialog(
														MeItemDetailsActivity.this);
												progress.setMessage("Please wait..");
												progress.setCancelable(false);
												int corePoolSize = 60;
												int maximumPoolSize = 80;
												int keepAliveTime = 10;
												BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
														maximumPoolSize);
												Executor threadPoolExecutor = new ThreadPoolExecutor(
														corePoolSize,
														maximumPoolSize,
														keepAliveTime,
														TimeUnit.SECONDS,
														workQueue);
												new ReportAbuseTask(progress)
														.executeOnExecutor(threadPoolExecutor);
											}
										}
									}).setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
						}
					});

					mReportAbuseImage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MeItemDetailsActivity.this);
							builder.setMessage("Are you sure you want to report abuse for this item ?");
							builder.setIcon(R.drawable.alert);
							builder.setTitle("Report Abuse");
							builder.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (checkInternetConnection()) {
												if (checkInternetConnection()) {
													ProgressDialog progress = new ProgressDialog(
															MeItemDetailsActivity.this);
													progress.setMessage("Please wait..");
													progress.setCancelable(false);
													int corePoolSize = 60;
													int maximumPoolSize = 80;
													int keepAliveTime = 10;
													BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
															maximumPoolSize);
													Executor threadPoolExecutor = new ThreadPoolExecutor(
															corePoolSize,
															maximumPoolSize,
															keepAliveTime,
															TimeUnit.SECONDS,
															workQueue);
													new ReportAbuseTask(
															progress)
															.executeOnExecutor(threadPoolExecutor);
												}
											}
										}
									}).setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	public class ReportAbuseTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public ReportAbuseTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = reportAbuseToWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			Log.e("test", "result  : " + result);
			if (result.contains("Success")) {
				// Toast.makeText(MeItemDetailsActivity.this,
				// "Thank you for reporting ", Toast.LENGTH_SHORT).show();
				showCustomToast("Thank you for reporting");
			} else {
				// Toast.makeText(MeItemDetailsActivity.this, "Try again",
				// Toast.LENGTH_SHORT).show();
				showCustomToast("Try again");
			}
		}

	}

	private String reportAbuseToWeb() {
		String postURL = "http://54.149.99.130/ws/report_abuse.php?itemid=2&userid=1";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mUserID));
			params.add(new BasicNameValuePair("itemid", mItemID));

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

	private String getItemsDetailsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		Log.e("", "getItemsDetailsFromWeb mUIDStr : " + mUIDStr);
		String postURL = "http://54.149.99.130/ws/get_user_item.php?userid=2&itemid=2&login_user_id=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mUserID));
			params.add(new BasicNameValuePair("itemid", mItemID));
			params.add(new BasicNameValuePair("login_user_id", mUIDStr));
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
							startActivity(new Intent(
									MeItemDetailsActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(
									MeItemDetailsActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(
									MeItemDetailsActivity.this,
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
												MeItemDetailsActivity.this,
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
				startActivity(new Intent(MeItemDetailsActivity.this,
						SellActivity.class)
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
		ImageView message = (ImageView) addView.findViewById(R.id.message);
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				startActivity(new Intent(MeItemDetailsActivity.this,
						AllChatsActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		getActionBar().setCustomView(addView);
	}

	private void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MeItemDetailsActivity.this);
		builder.setMessage("Are you sure you want to delete this item ?");
		builder.setIcon(R.drawable.alert);
		builder.setTitle("Delete");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (checkInternetConnection()) {
					ProgressDialog progress = new ProgressDialog(
							MeItemDetailsActivity.this);
					progress.setMessage("Please wait..");
					progress.setCancelable(false);
					int corePoolSize = 60;
					int maximumPoolSize = 80;
					int keepAliveTime = 10;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
							maximumPoolSize);
					Executor threadPoolExecutor = new ThreadPoolExecutor(
							corePoolSize, maximumPoolSize, keepAliveTime,
							TimeUnit.SECONDS, workQueue);
					new DeleteItemTask(progress)
							.executeOnExecutor(threadPoolExecutor);
				}
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public class DeleteItemTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public DeleteItemTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = deleteFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			Log.e("", "result delete: " + result);

			try {
				JSONObject responseObj = new JSONObject(result);
				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg").equalsIgnoreCase(
							"Deleted successfully")) {
						showCustomToast("Deleted successfully");
						switch (mFlag) {
						case 1:
							mDelImage1.setVisibility(View.GONE);
							mImage1.setVisibility(View.GONE);
							mRelThumb1.setVisibility(View.GONE);
							mThumbImage1.setVisibility(View.GONE);
							break;
						case 2:
							mDelImage2.setVisibility(View.GONE);
							mImage2.setVisibility(View.GONE);
							mRelThumb2.setVisibility(View.GONE);
							mThumbImage2.setVisibility(View.GONE);
							break;
						case 3:
							mDelImage3.setVisibility(View.GONE);
							mImage3.setVisibility(View.GONE);
							mRelThumb3.setVisibility(View.GONE);
							mThumbImage3.setVisibility(View.GONE);
							break;
						case 4:
							mDelImage4.setVisibility(View.GONE);
							mImage4.setVisibility(View.GONE);
							mRelThumb4.setVisibility(View.GONE);
							mThumbImage4.setVisibility(View.GONE);
							break;
						case 5:
							mDelImage5.setVisibility(View.GONE);
							mImage5.setVisibility(View.GONE);
							mRelThumb5.setVisibility(View.GONE);
							mThumbImage5.setVisibility(View.GONE);
							break;
						case 6:
							mDelImage6.setVisibility(View.GONE);
							mImage6.setVisibility(View.GONE);
							mRelThumb6.setVisibility(View.GONE);
							mThumbImage6.setVisibility(View.GONE);
							break;
						case 7:
							mDelImage7.setVisibility(View.GONE);
							mImage7.setVisibility(View.GONE);
							mRelThumb7.setVisibility(View.GONE);
							mThumbImage7.setVisibility(View.GONE);
							break;
						case 8:
							mDelImage8.setVisibility(View.GONE);
							mImage8.setVisibility(View.GONE);
							mRelThumb8.setVisibility(View.GONE);
							mThumbImage8.setVisibility(View.GONE);
							break;
						case 9:
							mDelImage9.setVisibility(View.GONE);
							mImage9.setVisibility(View.GONE);
							mRelThumb9.setVisibility(View.GONE);
							mThumbImage9.setVisibility(View.GONE);
							break;
						case 10:
							mDelImage10.setVisibility(View.GONE);
							mImage10.setVisibility(View.GONE);
							mRelThumb10.setVisibility(View.GONE);
							mThumbImage10.setVisibility(View.GONE);
							break;
						default:
							break;
						}
					} else {

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			// Intent intent = new Intent(MeActivity.this,
			// MeItemsTabActivity.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_left);
		}
	}

	private String deleteFromWeb() {
		Log.e("", "mImageNamemImageName : " + mImageName);
		String postURL = "http://54.149.99.130/ws/delete_item_image.php?itemid=&imagename=";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemID));
			params.add(new BasicNameValuePair("imagename", mImageName));
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
							getItemDetails();
							getComments();
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
								MeItemDetailsActivity.this,
								listWithoutDuplicates);
						actionBarSearch.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
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
			ProgressDialog progress = new ProgressDialog(
					MeItemDetailsActivity.this);
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
						MeItemDetailsActivity.this);
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
						startActivity(new Intent(MeItemDetailsActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								MeItemDetailsActivity.this);
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

}
