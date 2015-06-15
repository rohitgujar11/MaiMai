package com.nanostuffs.maimai.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.ChatAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.model.Category;
import com.nanostuffs.maimai.model.Chat;
import com.nanostuffs.maimai.model.SearchItem;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView;

public class MessagingSellerDirectly extends Activity implements
		OnClickListener {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private XListView mCategoryListView;
	private Handler mHandler;
	private ArrayList<Category> mCategory;
	private String mCatID;
	private String mCatName;
	private AutoCompleteTextView actionBarSearch;
	private ArrayList<SearchItem> mItemList;
	private String list;
	private ProgressDialog progress;
	private SearchItem mItem;
	private TextView mName;
	private TextView mTimeAgo;
	private ImageView mUserImage;
	private String mNameStr;
	private String mTimeAgoStr;
	private String mUserImageStr;
	private String mFromID;
	private String mToID;
	private AQuery androidAQuery;
	private ArrayList<Chat> mChatList;
	private ListView mChatListView;
	private ImageButton mSendButton;
	// private ImageButton mAudioButton;
	private EditText mEditMessage;
	private Chat mChat;
	private ChatAdapter mChatAdapter;
	private Handler mHandler1 = new Handler();
	private Handler mHandler2 = new Handler();
	private TextView mNoConv;
	// private CheckBox mAudioButton;
	private String outputFile;
	private MediaRecorder myAudioRecorder;

	private byte[] mAudioByteArr;
	private String response;
	private RelativeLayout mRel;
	private String mTest;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_GALLERY = 2;
	private long mTime;
	protected String mRectime;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private Handler mHandler111 = null;
	private static int count = 0;
	private boolean isPause = false;
	private boolean isStop = true;
	private static int delay = 1000; // 1s
	private static int period = 1000; // 1s
	private long lastDown = System.currentTimeMillis();

	private long lastDuration = 0;
	private Button mAudioButton1;
	private Button mAttachButton;
	public String mTo,send_fromid,send_toid;

	private int seconds;
	private int minutes;
	private int hours;
	protected boolean mLongClick;
	private static final int WIDTH = 200;
	private static final int HEIGHT = 200;
	private static final int RADIUS = 13;
	// private Recorder mRecorder;
	private File f;
	ArrayList<String> store_data_chat_id = new ArrayList<String>();
	ArrayList<String> temp_store_data_chat_id = new ArrayList<String>();
	Options options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_seller_directly);
		
		actionBarDetails();
		try {
			options = new BitmapFactory.Options();
		    options.inScaled = false;
			initializeComponents();
			getChatMessages();

			mHandler1.postDelayed(mRunnable, 1000);
		} catch (Exception e) {
		}

	}

	/*
	 * private Handler mRecordingHandler = new Handler(new Handler.Callback() {
	 * public boolean handleMessage(Message m) { switch (m.what) { case
	 * FLACRecorder.MSG_AMPLITUDES: FLACRecorder.Amplitudes amp =
	 * (FLACRecorder.Amplitudes) m.obj;
	 * 
	 * break;
	 * 
	 * case FLACRecorder.MSG_OK: // Ignore break;
	 * 
	 * case Recorder.MSG_END_OF_RECORDING:
	 * 
	 * break;
	 * 
	 * default: mRecorder.stop(); // mErrorCode = m.what; //
	 * showDialog(DIALOG_RECORDING_ERROR); break; }
	 * 
	 * return true; } });
	 */

	// // DOWN handler
	// Handler messageHandler = new Handler() {
	//
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case 1: // GET DOWNSTREAM json id="@+id/comment"
	// // String mtxt = msg.getData().getString("text");
	// // if (mtxt.length() > 20) {
	// // final String f_msg = mtxt;
	// // handler.post(new Runnable() { // This thread runs in the UI
	// // // TREATMENT FOR GOOGLE RESPONSE
	// // @Override
	// // public void run() {
	// //// transcibe_done = 1;
	// //// System.out.println("111111 " + f_msg);
	// //// txtView.setText(f_msg);
	// //
	// // }
	// // });
	// }
	// break;
	// case 2:
	// break;
	// }
	// }
	// };

	private final Runnable mRunnable = new Runnable() {

		public void run() {
			getChatMessages();
			mHandler1.postDelayed(mRunnable, 1000);
		}
	};
	private final Runnable mRunnable1 = new Runnable() {

		public void run() {
			mTime = mTime + 1000;
			mHandler2.postDelayed(mRunnable, 30000);
		}
	};
	private int width;
	private int height;
	private int radius;
	private String mToID1;
	private File audiofile;
	private String name_uid;

	private void getChatMessages() {
		try {

			if (checkInternetConnection()) {
				ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage("Please wait..");
				progress.setCanceledOnTouchOutside(false);
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				new RetriveChatTask(progress)
						.executeOnExecutor(threadPoolExecutor);
			}

		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}
	public class RetriveChatTask1 extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String ago;
		private String resultdata;
		
		public RetriveChatTask1(ProgressDialog progress) {
			this.progress = progress;
			
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = anyweb();
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			System.out.println(""+result);
		}
	}
	private String anyweb() {
	try {
		String postURL = "http://54.149.99.130/ws/update_chat_read_flag.php?";
		String result = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postURL);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		System.out.println("from id:- "+send_fromid+"to id:- "+send_toid);
		params.add(new BasicNameValuePair("fromid",send_fromid));
		params.add(new BasicNameValuePair("toid",send_toid));	
		params.add(new BasicNameValuePair("loginid",mUIDStr));
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
				HTTP.UTF_8);
		post.setEntity(ent);
		HttpResponse responsePOST = client.execute(post);
		HttpEntity resEntity = responsePOST.getEntity();
	String get_update = EntityUtils.toString(resEntity);
	System.out.println("re"+get_update);
		if (resEntity != null) {
			String get;
		    int cnt = responsePOST.getStatusLine().getStatusCode();
			get= EntityUtils.toString(resEntity);
			System.out.println("re"+cnt);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "done";
	}
	public class RetriveChatTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public RetriveChatTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = getChatsFromWeb();
				
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress.dismiss();
		
			try {
				store_data_chat_id.clear();
				
				JSONObject responseObj = new JSONObject(result);
			

			
				JSONArray ja = responseObj.getJSONArray("data");
			
			if (!(ja.length() == 0)) {
				
			
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					store_data_chat_id.add(i,jo.getString("ChatId"));
				}
			}
		}
		 catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
			int flag = 0;
			
			if(temp_store_data_chat_id.size()!=0){
				flag=0;
				System.out.println("temp store chat id is :- "+temp_store_data_chat_id);
				System.out.println("store chat id is :- "+store_data_chat_id);
				for (int i = 0; i < store_data_chat_id.size(); i++) {
				
					for (int j = 0; j < temp_store_data_chat_id.size(); j++) {
						if((store_data_chat_id.get(j).toString()).equalsIgnoreCase((temp_store_data_chat_id.get(i).toString())))
						{	
							flag=0;
							break;
						}
						else{
							flag=1;
						}
					}
					if(flag==1)
						break;
				}
			}
			else
				flag=1;
			if(flag==0)
				Log.i("Allchatacivity","data is same");
			else
			{
				try {
					
					temp_store_data_chat_id.clear();
					for (int i = 0; i < store_data_chat_id.size(); i++) {
						temp_store_data_chat_id.add(i, store_data_chat_id.get(i));
					}
					System.out.println("result"+result);
				JSONObject responseObj = new JSONObject(result);

				JSONArray ja = responseObj.getJSONArray("data");
				
				if (!(ja.length() == 0)) {
					mNoConv.setVisibility(View.GONE);

					if (mChatList.size() != 0) {
						mChatList.clear();
					}
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						mChat = new Chat();
						mChat.setFromImage(jo.getString("From_image"));
						mChat.setToImage(jo.getString("To_image"));
						mChat.setFromId(jo.getString("From_id"));
						mChat.setToId(jo.getString("To_id"));
						mChat.setMessage(jo.getString("Message"));
						mChat.setThumb(jo.getString("thumb"));
						mChat.setRecTime(jo.getString("AudioTime"));
						mChatList.add(0, mChat);
					}
					new RetriveChatTask1(MessagingSellerDirectly.this.progress).execute();
					// String from_imagePath =
					// mChat.getFromImage().replace("\\/", "/")
					// .replace("[", "").replace("]", "")
					// .replace("\"", "");
					// String to_imagePath = mChat.getToImage().replace("\\/",
					// "/")
					// .replace("[", "").replace("]", "")
					// .replace("\"", "");
					String from_imagePath = mChat.getFromImage().replace("\\/",
							"/");
					String to_imagePath = mChat.getToImage()
							.replace("\\/", "/");
					Log.e("", "bbbbbbbbbfrom_imagePath " + from_imagePath);
					Log.e("", "bbbbbbbbbto_imagePath " + to_imagePath);

					androidAQuery.id(mUserImage).image(to_imagePath, false,
							false, 0, R.drawable.user_photo);
					mTo = mChat.getFromId();
				mChatAdapter = new ChatAdapter(
						MessagingSellerDirectly.this, mChatList);
				mChatListView.setAdapter(mChatAdapter);
				scrollMyListViewToBottom();
				} else {
					mNoConv.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			}
			
				
					/*Log.e("", "gggg get from id " + mChat.getFromId());
					Log.e("", "gggg get to id " + mChat.getToId());*/
					if (mTest.contains("1")) {
						mRel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);

							}
						});
					}
						mName.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String chat_pass = mChatList.get(0).getFromId();
								if(mChatList.get(0).getFromId().equalsIgnoreCase(mUIDStr))
									chat_pass = mChatList.get(0).getToId();
								savePreferences("name_uid",chat_pass);
								Intent intent = new Intent(MessagingSellerDirectly.this, NameItemsTabActivity.class);
								startActivity(intent);
							/*	intent.overridePendingTransition(
										R.anim.slide_in_right, R.anim.slide_out_left);*/
							}
						});
					

					
				

			
		}
	}

	private String getChatsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		Log.e("", "1111111getChatsFromWeb mFromID : " + mUIDStr + " mToID : "
				+ mToID + " mFromID : " + mFromID + " tooo : " + mTo);
		String postURL = "http://54.149.99.130/ws/get_messages.php?fromid=&toid=&login_user";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("fromid", mUIDStr));
			
			if (mUIDStr.equalsIgnoreCase(mToID)) {
				params.add(new BasicNameValuePair("toid", mToID1));
			} else {
				params.add(new BasicNameValuePair("toid", mToID));
			}
			params.add(new BasicNameValuePair("login_user", mUIDStr));
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
		// myAudioRecorder = new MediaRecorder();
		// outputFile = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/myrecording.wav";
		// myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		// myAudioRecorder.setOutputFile(outputFile);
		// myAudioRecorder.setMaxDuration(300000);
		/*
		 * mRecorder = new Recorder(MessagingSellerDirectly.this,
		 * mRecordingHandler);
		 */
		mRel = (RelativeLayout) findViewById(R.id.rel_brown);

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
		mName = (TextView) findViewById(R.id.name);
		mName.setTypeface(mActionBarTypeface);
		mNoConv = (TextView) findViewById(R.id.no_conv);
		mNoConv.setTypeface(mActionBarTypeface);
		mTimeAgo = (TextView) findViewById(R.id.hour_ago);
		mTimeAgo.setTypeface(mActionBarTypeface);
		mEditMessage = (EditText) findViewById(R.id.edit_message);
		mEditMessage.setTypeface(mActionBarTypeface);
		mUserImage = (ImageView) findViewById(R.id.sell_image);
		mSendButton = (ImageButton) findViewById(R.id.send_btn);
		mSendButton.setOnClickListener(this);
		// mAudioButton = (ImageButton) findViewById(R.id.audio_btn);
		// mAudioButton.setOnClickListener(this);
		mAudioButton1 = (Button) findViewById(R.id.audio_btn1);
		mAttachButton = (Button) findViewById(R.id.plus_btn);
		mAttachButton.setOnClickListener(this);
		mChatListView = (ListView) findViewById(R.id.list_chats);
		androidAQuery = new AQuery(this);
		mChatList = new ArrayList<Chat>();
		if (getIntent().getExtras() != null) {
			mNameStr = getIntent().getExtras().getString("name");
			mTimeAgoStr = getIntent().getExtras().getString("ago");
			mUserImageStr = getIntent().getExtras().getString("userImage");
			send_fromid = getIntent().getExtras().getString("fromid");
			send_toid = getIntent().getExtras().getString("toid");
			Log.e("", "mUserImageStr : " + mUserImageStr);
			mFromID = getIntent().getExtras().getString("from");
			mToID = getIntent().getExtras().getString("to");
			mToID1 = getIntent().getExtras().getString("to1");
			if (getIntent().getExtras().getString("to1") == null) {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(this);
				name_uid = sharedPreferences.getString("name_uid", "");
				mToID1 = name_uid;
			}
			Log.e("", " gggggggggmToIDmToID : " + mToID);
			Log.e("", " gggggggggmFromIDmFromID : " + mFromID);
			Log.e("", " gggggggggmmToIDmToID11111 : " + mToID1);
			mTest = getIntent().getExtras().getString("test");
			mName.setText(mNameStr);
			mTimeAgo.setText("Last active: " + mTimeAgoStr);
			// androidAQuery.id(mUserImage).image(mUserImageStr, true, true, 0,
			// R.drawable.user_photo);
		}
		// outputFile = Environment.getExternalStorageDirectory()
		// + "/recording.flac";
		outputFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/myrecording.mp3";

		// outputFile = Environment.getRootDirectory().getAbsolutePath() +
		// "/myrecording.wav";

		// File sampleDir = Environment.getExternalStorageDirectory();
		// try {
		// audiofile = File.createTempFile("sound", ".3gp", sampleDir);
		// } catch (IOException e) {
		// Log.e("", "sdcard access error");
		// return;
		// }
		mAudioButton1.setOnLongClickListener(speakHoldListener);
		mAudioButton1.setOnTouchListener(speakTouchListener);
		mAudioButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("", "33333 on click");
				mLongClick = false;
			}
		});

	}

	// private View.OnLongClickListener speakHoldListener = new
	// View.OnLongClickListener() {
	//
	// private boolean mStartRecording;
	//
	// @Override
	// public boolean onLongClick(View pView) {
	// Log.e("", "33333 on onLongClick");
	//
	// mLongClick = true;
	// try {
	// // mHandler2.postDelayed(mRunnable1, 1000);
	// lastDown = System.currentTimeMillis();
	//
	// // f = new File(
	// // android.os.Environment.getExternalStorageDirectory(),
	// // "aud.mp3");
	// // try {
	// // f.createNewFile();
	// //
	// myAudioRecorder = new MediaRecorder();
	//
	// myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// myAudioRecorder
	// .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	// myAudioRecorder
	// .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	//
	// myAudioRecorder.setOutputFile(outputFile);
	//
	// try {
	// myAudioRecorder.prepare();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// myAudioRecorder.start();
	// }
	// // mRecorder.start(outputFile);
	//
	// catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	// // Toast.makeText(getApplicationContext(),
	// // "Recording started", Toast.LENGTH_LONG).show();
	// showCustomToast("Recording started");
	// return true;
	// }
	//
	// };
	//
	// private View.OnTouchListener speakTouchListener = new
	// View.OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View pView, MotionEvent pEvent) {
	// pView.onTouchEvent(pEvent);
	// // We're only interested in when the button is released.
	//
	// // mAudioByteArr = new byte[(int) file.length()];
	// try {
	// if (pEvent.getAction() == MotionEvent.ACTION_UP) {
	//
	// if (mLongClick) {
	// Log.e("", "33333 on onTouch");
	// mLongClick = false;
	// lastDuration = 0;
	// lastDuration = System.currentTimeMillis() - lastDown;
	// seconds = (int) (lastDuration / 1000) % 60;
	// minutes = (int) ((lastDuration / (1000 * 60)) % 60);
	// hours = (int) ((lastDuration / (1000 * 60 * 60)) % 24);
	//
	// Log.e("", "lastDuration : " + lastDuration + " ::: "
	// + hours + ":" + minutes + " :" + seconds);
	// mRectime = hours + "h " + minutes + "m " + seconds
	// + " s";
	// Log.e("", "999lastDuration : " + lastDuration);
	//
	// if (myAudioRecorder != null) {
	// // try {
	// // Thread.sleep(500);
	// // } catch (InterruptedException e) {
	// // e.printStackTrace();
	// // }
	// myAudioRecorder.stop();
	// // myAudioRecorder.reset();
	// // myAudioRecorder.release();
	// // myAudioRecorder = null;
	// }
	// // mRecorder.stop();
	// showCustomToast("Audio recorded successfully");
	//
	// File file = new File(outputFile);
	// Log.e("", "file : " + outputFile);
	// mAudioByteArr = read(file);
	//
	// sendMessage();
	//
	// }
	//
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (NullPointerException e) {
	// e.printStackTrace();
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	// }
	// };
	private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

		private boolean mStartRecording;

		@Override
		public boolean onLongClick(View pView) {
			Log.e("", "33333 on onLongClick");
			mLongClick = true;
			// try {
			// if (myAudioRecorder == null) {
			// myAudioRecorder = new MediaRecorder();
			// myAudioRecorder
			// .setAudioSource(MediaRecorder.AudioSource.MIC);
			// myAudioRecorder
			// .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// //
			// myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			// myAudioRecorder
			// .setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			// myAudioRecorder.setOutputFile(outputFile);
			// }
			// if (!mStartRecording) {
			// try {
			// myAudioRecorder.prepare();
			// myAudioRecorder.start();
			// mStartRecording = true;
			// } catch (IOException e) {
			// e.printStackTrace();
			// }// catch
			// } else {
			// try {
			// mStartRecording = false;
			// myAudioRecorder.stop();
			// myAudioRecorder.reset();
			// myAudioRecorder.release();
			// myAudioRecorder = null;
			// } catch (RuntimeException e) {
			// e.printStackTrace();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// } catch (IllegalStateException e) {
			// e.printStackTrace();
			// }
			// showCustomToast("Recording started");
			// return true;
			// }
			try {

				if (myAudioRecorder == null) {
					myAudioRecorder = new MediaRecorder();
					myAudioRecorder
							.setAudioSource(MediaRecorder.AudioSource.MIC);
					myAudioRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					// //working on canvas

					myAudioRecorder
							.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					myAudioRecorder.setOutputFile(outputFile);
					myAudioRecorder.prepare();
					lastDown = System.currentTimeMillis();
					// myAudioRecorder.setMaxDuration(300000);

					myAudioRecorder.start();
				}
			} catch (Exception e) {
			}

			showCustomToast("Recording started");
			return true;
		}

	};

	private View.OnTouchListener speakTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View pView, MotionEvent pEvent) {
			pView.onTouchEvent(pEvent);
			// We're only interested in when the button is released.

			// mAudioByteArr = new byte[(int) file.length()];
			try {
				if (pEvent.getAction() == MotionEvent.ACTION_UP) {
					if (mLongClick) {
						Log.e("", "33333 on onTouch");
						mLongClick = false;
						lastDuration = 0;
						lastDuration = System.currentTimeMillis() - lastDown;
						seconds = (int) (lastDuration / 1000) % 60;
						minutes = (int) ((lastDuration / (1000 * 60)) % 60);
						hours = (int) ((lastDuration / (1000 * 60 * 60)) % 24);

						Log.e("", "lastDuration : " + lastDuration + " ::: "
								+ hours + ":" + minutes + " :" + seconds);
						mRectime = hours + "h " + minutes + "m " + seconds
								+ " s";
						Log.e("", "999lastDuration : " + lastDuration);

						if (myAudioRecorder != null) {
							myAudioRecorder.stop();
							myAudioRecorder.release();
							// myAudioRecorder = null;
						}
						showCustomToast("Audio recorded successfully");
						File file = new File(outputFile);
						mAudioByteArr = read(file);

						sendMessage();

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			return false;
		}
	};

	private Dialog dialog;
	private String imageFilePath;
	private Bitmap bm;
	private Bitmap mainBitmap;
	private byte[] byteArr;
	private byte[] byteArr1;
	private Bitmap thumb;
	private AlertDialog alert;

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
		// toast.setDuration(Toast.LENGTH_SHORT);
		// toast.setView(layout);
		// toast.show();
		Toast.makeText(MessagingSellerDirectly.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public byte[] read(File file) throws IOException {

		// if ( file.length() > MAX_FILE_SIZE ) {
		// throw new FileTooBigException(file);
		// }
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
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
									MessagingSellerDirectly.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(
									MessagingSellerDirectly.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(
									MessagingSellerDirectly.this,
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
												MessagingSellerDirectly.this,
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
				startActivity(new Intent(MessagingSellerDirectly.this,
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
				startActivity(new Intent(MessagingSellerDirectly.this,
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
							getChatMessages();

							mHandler1.postDelayed(mRunnable, 1000);
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

				if (result.length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MessagingSellerDirectly.this);
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
										MessagingSellerDirectly.this,
										listWithoutDuplicates);
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

			} catch (Exception e) {
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
			ProgressDialog progress = new ProgressDialog(
					MessagingSellerDirectly.this);
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
						MessagingSellerDirectly.this);
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

	private void showConnectionTimeoutMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MessagingSellerDirectly.this);
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
						startActivity(new Intent(MessagingSellerDirectly.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								MessagingSellerDirectly.this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			lastDuration = 0;
			seconds = 0;
			minutes = 0;
			hours = 0;
			mAudioByteArr = null;
			if (mEditMessage.getText().toString().trim().length() == 0) {
				Log.e("", "111111111111111111111");
			} else {
				if (mEditMessage.getText().toString().trim().length() != 0
						|| (lastDuration <= 999)) {
					lastDuration = 0;
					sendMessage();
				}
			}
			break;
		case R.id.plus_btn:
			dialog = new Dialog(MessagingSellerDirectly.this,
					R.style.DialogSlideAnim);
			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(getLayoutInflater().inflate(
					R.layout.attach_file, null));
			dialog.getWindow().getAttributes().y = mRel.getBottom() + 140;
			Button camera = (Button) dialog.findViewById(R.id.camera);
			Button gallery = (Button) dialog.findViewById(R.id.gallery);
			Button location = (Button) dialog.findViewById(R.id.location);

			camera.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog != null) {
						dialog.dismiss();
					}
					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					Uri fileUri = getOutputMediaFileUri();
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, PICK_FROM_CAMERA);

				}
			});
			gallery.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (dialog != null) {
						dialog.dismiss();
					}
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.putExtra("return-data", true);
					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_GALLERY);

				}
			});

			location.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog != null) {
						dialog.dismiss();
					}
					Intent intent = new Intent(MessagingSellerDirectly.this,
							CurrentLocationActivity.class);
					Log.e("", "mToID : " + mToID + " mToID1 : " + mToID1);
					intent.putExtra("mToID", mToID);
					intent.putExtra("mToID1", mToID1);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}

	private static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	private static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				android.os.Environment
						.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES),
				"MaiMai");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1111) {
			// Audio

			File folder = new File(Environment.getExternalStorageDirectory(),
					"/Sounds");
			long folderModi = folder.lastModified();

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith("3ga"));
				}
			};

			File[] folderList = folder.listFiles(filter);

			String recentName = "";

			for (int i = 0; i < folderList.length; i++) {
				long fileModi = folderList[i].lastModified();

				if (folderModi == fileModi) {
					recentName = folderList[i].getName();
				}
			}
		}
		// Camera
		if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
			imageFilePath = getOutputMediaFileUri().getPath();
			bm = BitmapFactory.decodeFile((imageFilePath),options);
			Matrix matrix = new Matrix();
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					matrix, true);
			ExifInterface ei = null;
			try {
				ei = new ExifInterface(imageFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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
			mainBitmap = bm;
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// mainBitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

			// mainBitmap = codec(bm, Bitmap.CompressFormat.JPEG, 30);
			//
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// mainBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			// Bitmap decoded = BitmapFactory.decodeStream(new
			// ByteArrayInputStream(out.toByteArray()));
			//
			// Log.e("Original   dimensions",
			// "zzzzzzzzzzzz org :"+mainBitmap.getWidth()+" "+mainBitmap.getHeight());
			// Log.e("Compressed dimensions", "zzzzzz compr " +
			// decoded.getWidth()+" "+decoded.getHeight());
			//
			// // mainBitmap = bm;
			// mainBitmap = decoded;
		}

		// Gallery
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
				mainBitmap = BitmapFactory.decodeFile((picturePath),options);
			} else {
				Log.e("", "testttttttttttttt");
				// Bundle extras2 = data.getExtras();
				// if (extras2 != null) {
				// Bitmap photo = extras2.getParcelable("data");
				// mainBitmap = photo;
				// Log.e("", "testttttttttttttt mainBitmap" +mainBitmap);
				// } else {
				// Uri selectedImage = data.getData();
				// String[] filePathColumn = { MediaStore.Images.Media.DATA };
				//
				// Cursor cursor = getContentResolver().query(selectedImage,
				// filePathColumn, null, null, null);
				// cursor.moveToFirst();
				//
				// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// String picturePath = cursor.getString(columnIndex);
				// cursor.close();
				// mainBitmap = BitmapFactory.decodeFile(picturePath);
				// Log.e("", "testttttttttttttt mainBitmap 2" +mainBitmap);
				// }
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				mainBitmap = BitmapFactory.decodeFile((picturePath),options);
				Log.e("", "testttttttttttttt mainBitmap 2" + mainBitmap);
			}
		}
		if (mainBitmap != null) {
			mainBitmap = codec(mainBitmap, Bitmap.CompressFormat.JPEG, 15);
			sendMessage();
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
		canvas.drawRoundRect(rectF, 20, 20, paint);

		// canvas.drawRoundRect(rectF, radius, radius, paint);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		// bitmap.getWidth() / 2, paint);
		canvas.drawRect(rect, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 120, 120, false);
		Bitmap _bmp = Bitmap.createScaledBitmap(output, bitmap.getWidth() / 2,
				bitmap.getHeight() / 2, false);
		return _bmp;
		// return output;
	}

	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
			int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);

		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	private void sendMessage() {
		mSendButton.setEnabled(true);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Sending..");
			// progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new SendMsgTask(progress).executeOnExecutor(threadPoolExecutor);
		}

	}

	public class SendMsgTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public SendMsgTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = sendMsgToWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				try {
					progress.dismiss();
					Log.e("", "lllllllllllllllllllllllll " +result);
				} catch (Exception e) {
				}
				if (result.contains("Successfully inserted")) {
					getChatMessages();
					mEditMessage.setText("");
				}
			} catch (Exception e) {
			}
		}
	}

	private void scrollMyListViewToBottom() {
		mChatListView.setSelection(mChatAdapter.getCount() - 1);
		// mChatListView.post(new Runnable() {
		// @Override
		// public void run() {
		// // Select the last row so it will scroll into view...
		// mChatListView.setSelection(mChatAdapter.getCount() - 1);
		// }
		// });
	}

	private String sendMsgToWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");

		String postURL = "http://54.149.99.130/ws/send_message.php?fromid=&toid=&msg=&audiotime=";
		String result = "";
		try {
			Log.e("", "111111from : " + mUIDStr + "mToID : " + mToID + " ");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("fromid", mUIDStr));
			if (mUIDStr.equalsIgnoreCase(mToID)) {
				Log.e("", "111111from : " + mUIDStr + "mToID1 : " + mToID1 + " ");
				params.add(new BasicNameValuePair("toid", mToID1));
			} else {
				Log.e("", "111111from : " + mUIDStr + "mToID : " + mToID + " ");
				params.add(new BasicNameValuePair("toid", mToID));
			}

			params.add(new BasicNameValuePair("msg", mEditMessage.getText()
					.toString()));

			if (mAudioByteArr == null && mainBitmap == null) {
				response = CustomHttpClient.executeHttpPost(postURL, params);
			} else if (mAudioByteArr != null) {
				params.add(new BasicNameValuePair("audiotime", mRectime));
				response = CustomHttpClient.executeHttpPostForAudio(postURL,
						params, "audio", mAudioByteArr);
				lastDuration = 0;
				seconds = 0;
				minutes = 0;
				hours = 0;
				mAudioByteArr = null;
			} else if (mainBitmap != null) {

				// ByteArrayOutputStream bos = new ByteArrayOutputStream();
				// mainBitmap.compress(CompressFormat.PNG, 100, bos);
				// byteArr = bos.toByteArray();
				// Log.e("", "byteArrbyteArr " + byteArr);
				// response =
				// CustomHttpClient.executeHttpPostForImgChat(postURL,
				// params, "chatimage", byteArr);

				// original bitmap
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				mainBitmap.compress(CompressFormat.JPEG, 100, bos);
				byteArr = bos.toByteArray();

				// thumbnail bitmap
				try {
					final int THUMBNAIL_SIZE = 200;
					thumb = Bitmap.createScaledBitmap(mainBitmap,
							THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					thumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byteArr1 = baos.toByteArray();
				} catch (Exception ex) {
				}

				Log.e("", "byteArrbyteArr " + byteArr);
				response = CustomHttpClient.executeHttpPostForImgChat1(postURL,
						params, "chatimage", byteArr, "imagethumb", byteArr1);
				mainBitmap = null;
			}
			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("", "llllllllllllllllllastDuration : " + lastDuration
				+ " mAudioByteArr : " + mAudioByteArr + " seconds : " + seconds);
		mAudioByteArr = null;
		return result;
	}

	// private String sendMsgToWeb() {
	// SharedPreferences prefs = this.getSharedPreferences(
	// SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
	// mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
	// Log.e("", "comment  : " + mEditMessage.getText().toString());
	// String postURL =
	// "http://54.149.99.130/ws/send_message.php?fromid=&toid=&msg=";
	// String result = "";
	// try {
	// DefaultHttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(postURL);
	// ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("fromid", mUIDStr));
	// params.add(new BasicNameValuePair("toid", mToID));
	// params.add(new BasicNameValuePair("msg", mEditMessage.getText()
	// .toString()));
	//
	// UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
	// HTTP.UTF_8);
	// post.setEntity(ent);
	// HttpResponse responsePOST = client.execute(post);
	// HttpEntity resEntity = responsePOST.getEntity();
	// if (resEntity != null) {
	// result = EntityUtils.toString(resEntity);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }
}
