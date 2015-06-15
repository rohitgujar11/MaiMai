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
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.CommentsAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.model.Comments;
import com.nanostuffs.maimai.model.Item;
import com.nanostuffs.maimai.model.SearchItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class ItemDetailsActivity extends Activity implements OnClickListener {
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
	private EditText mComment;
	private ImageButton mSendBtn;
	private CheckBox mLikeBtn;
	private ImageButton mShareBtn;
	private RelativeLayout mRel;
	private ScrollView mScroll;
	private RelativeLayout mRel1;
	private RelativeLayout mHsv;
	private RelativeLayout mRel2;
	private ListView mCommentsList;
	private ArrayList<Comments> mComList;
	private Comments mCom;
	private String mAddComment;
	private CommentsAdapter mCommentsAdapter;
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
	private CheckBox mFollowToggle;
	private String mRepliedCount;
	private Button mMsgSeller;
	String activity;
	private Dialog dialog2,dialog3;
	private String mSharePath;
	private AlertDialog alert;
	ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.item_details);

	
		setContentView(R.layout.comment_list);
		actionBarDetails();
		try {
			initializeComponents();
			getItemDetails();
			getComments();
		} catch (Exception e) {
		}

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
		Toast.makeText(ItemDetailsActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}
/*@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	if(activity.equalsIgnoreCase("search"));
	
}*/
	private void initializeComponents() {

		if (getIntent() != null) {
			mItemID = getIntent().getStringExtra("itemid");
			mUserID = getIntent().getStringExtra("userid");
			activity = getIntent().getStringExtra("activity");
		}
		// try {
		// Uri data = getIntent().getData();
		// if (data != null) {
		// String scheme = data.getScheme();
		// String host = data.getHost();
		// List<String> params = data.getPathSegments();
		// String first = params.get(0);
		// String second = params.get(1);
		// String s =data.toString();
		// Log.e("", "stringgggggggg : " + s);
		// Log.e("", "stringgggggggg  is contains: " + s.contains("?"));
		// String dataArr[] = s.split("?");
		// Log.e("", "stringgggggggg dataArr[1]: " + dataArr[1]);
		// // String paramArr[] = dataArr[1].split("&");
		// // showCustomToast("user id : " + paramArr[0] + "item id : "
		// // + paramArr[1] + "login user id : " + paramArr[2]);
		// }
		// } catch (Exception e) {
		// }
		Uri data = getIntent().getData();
		if (data != null) {
			String scheme = data.getScheme();
			String host = data.getHost();
			List<String> params = data.getPathSegments();
			String first = params.get(0);
			String second = params.get(1);
			String s = data.toString();
			Log.e("", "stringgggggggg : " + s);
			Log.e("", "stringgggggggg  is contains: " + s.contains("?"));
			String dataArr[] = s.split("php");
			dataArr[1] = dataArr[1].replace("?userid=", "")
					.replace("&itemid=", ",").replace("&login_user_id=", ",");
			String idArr[] = dataArr[1].split(",");
			Log.e("", "stringgggggggg dataArr[1]: " + dataArr[1]);
			mItemID = idArr[1];
			mUserID = idArr[0];
			// String paramArr[] = dataArr[1].split("&");
			// showCustomToast("user id : " + paramArr[0] + "item id : "
			// + paramArr[1] + "login user id : " + paramArr[2]);
		}
		mCommentsList = (ListView) findViewById(R.id.list_comments);
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout listHeaderView = (RelativeLayout) inflater.inflate(
				R.layout.comment_list_header, null);
		mCommentsList.addHeaderView(listHeaderView);

		mComList = new ArrayList<Comments>();

		mComment = (EditText) findViewById(R.id.comment);
		mComment.setTypeface(mActionBarTypeface);
		mCommentsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("", "lnlininnnnnnnnnnnnn");

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			}
		});
		LinearLayout lin = (LinearLayout) findViewById(R.id.lin);
		lin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			}
		});
		listHeaderView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			}
		});

		mRel2 = (RelativeLayout) listHeaderView.findViewById(R.id.rel_brown);
		mRel2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			}
		});
		mHsv = (RelativeLayout) listHeaderView.findViewById(R.id.rel2222);
		mHsv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			}
		});
		androidAQuery = new AQuery(this);
		mItemList = new ArrayList<SearchItem>();
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

		mUserImage = (ImageView) listHeaderView.findViewById(R.id.sell_image);
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

		mMsgSeller = (Button) listHeaderView.findViewById(R.id.msg);
		mFollowToggle = (CheckBox) listHeaderView
				.findViewById(R.id.follow_toggle);
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");

		if (mUIDStr.equalsIgnoreCase(mUserID)) {
			mFollowToggle.setVisibility(View.GONE);
			mMsgSeller.setVisibility(View.GONE);
		} else {
			mFollowToggle.setVisibility(View.VISIBLE);
			mMsgSeller.setVisibility(View.VISIBLE);
		}

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

		mFollowToggle.setTypeface(mActionBarTypeface);
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

		mSendBtn = (ImageButton) findViewById(R.id.send_btn);
		mSendBtn.setEnabled(false);
		mSendBtn.setOnClickListener(this);
		mLikeBtn = (CheckBox) findViewById(R.id.like_button);
		mShareBtn = (ImageButton) findViewById(R.id.share_button);
		mShareBtn.setOnClickListener(this);

		mComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count != 0) {
					mSendBtn.setEnabled(true);
				} else {
					mSendBtn.setEnabled(false);
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

	}

	private void followUnfollowUser() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(
					ItemDetailsActivity.this);
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new FollowUnfollowUser(progress)
					.executeOnExecutor(threadPoolExecutor);
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

	public class FollowUnfollowUser extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public FollowUnfollowUser(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = followUnfollowFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress.dismiss();
			Log.e("", "tttttttttttt result follow : " + result);
			// viewHolder.likeCheckBox.setChecked(true);
		}
	}

	private String followUnfollowFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/following_user.php?userid1=2&userid2=4";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid1", mUIDStr));
			params.add(new BasicNameValuePair("userid2", mUserID));
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

	private void getItemDetails() {
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
						ItemDetailsActivity.this, mComList);
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

	public class LikeTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public LikeTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = likeFromWeb();

			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				// progress.dismiss();
				Log.e("", "tttttttttttt result like : " + result);
			} catch (Exception e) {
			}

			// viewHolder.likeCheckBox.setChecked(true);
		}
	}

	private String likeFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/item_like.php?itemid=1&uid=1";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemID));
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

	public class UnLikeTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public UnLikeTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = unlikeFromWeb();
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				// progress.dismiss();
				Log.e("", "tttttttttttt result unlike: " + result);
			} catch (Exception e) {
			}

			// viewHolder.likeCheckBox.setChecked(false);
		}
	}

	private String unlikeFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/item_unlike.php?itemid=1&uid=1";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemID));
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

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	protected void viewImage(String path) {
		dialog2 = new Dialog(ItemDetailsActivity.this);
		dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(getLayoutInflater().inflate(
				R.layout.temp_item_show, null));
		TouchImageView sell_image = (TouchImageView) dialog2.findViewById(R.id.sell_image);
		final TouchImageView sell_image1 = (TouchImageView) dialog2.findViewById(R.id.sell_image1);
		final ProgressBar pB = (ProgressBar) dialog2.findViewById(R.id.progress);
		pB.setVisibility(View.VISIBLE);
		ImageLoader imageLoader;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ItemDetailsActivity.this));
		
	FailReason failReason;
	imageLoader.displayImage(path, sell_image, null, new ImageLoadingListener() {
	    @Override
	    public void onLoadingStarted(String imageUri, View view) {
	    	
	    }
	    @Override
	    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
	    	
	    	pB.setVisibility(View.INVISIBLE);
	    	sell_image1.setVisibility(View.INVISIBLE);
	    }
	    @Override
	    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	    	pB.setVisibility(View.INVISIBLE);
	    	sell_image1.setVisibility(View.INVISIBLE);
	    	
	    }
	    @Override
	    public void onLoadingCancelled(String imageUri, View view) {
	    	
	    }
	}, new ImageLoadingProgressListener() {
	    @Override
	    public void onProgressUpdate(String imageUri, View view, int current, int total) {
	    	
	    }
	});
		/*androidAQuery.id(sell_image).progress(pB)
				.image(path, false, false, 0, R.drawable.default_image1);*/
		dialog2.show();

	}
	protected void playVideo(final String string) {
		try{
			
					// TODO Auto-generated method stub
					dialog3 = new Dialog(ItemDetailsActivity.this);
					dialog3.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
					dialog3.setContentView(getLayoutInflater().inflate(
							R.layout.video_play, null));

					final VideoView sell_video = (VideoView) dialog3
							.findViewById(R.id.videoView_play);
					
					final ProgressBar mProgress;
					
					mProgress = (ProgressBar) dialog3.findViewById(R.id.progress_play);
					mProgress.setVisibility(View.VISIBLE);
					MediaController mediaController = new MediaController(
							ItemDetailsActivity.this);
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

				
			
				}catch(Exception e){
			System.out.println("error");
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
		private String mShareText;
		private String userImage;
		private String ago;
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
			try {
				progress.dismiss();

				if (result.length() == 0) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							ItemDetailsActivity.this);
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

					Log.e("test", "result  : " + result);
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
							item.setIsLike(jo.getString("user_likes"));
							item.setThumb(jo.getString("Thumb"));
							item.setFollow(jo.getString("Follow"));
						}
						SharedPreferences prefs = ItemDetailsActivity.this
								.getSharedPreferences(SplashActivity.PREFS_UID,
										Context.MODE_PRIVATE);
						mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
						savePreferences("name_uid", item.getUserId());
						Log.e("",
								"clickkkkkkkkkkk image b: "
										+ item.getItemImage());
						mName.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								savePreferences("name_uid", item.getUserId());
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										actionBarSearch.getWindowToken(), 0);
								Intent intent = new Intent(
										ItemDetailsActivity.this,
										NameItemsTabActivity.class);
								startActivity(intent);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
							}
						});

						String isLike = item.getIsLike();
						if (isLike.contains("1")) {
							mLikeBtn.setChecked(true);
						} else {
							mLikeBtn.setChecked(false);

						}
						mLikeBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									Log.e("", "tttttttttttt 111");
									if (checkInternetConnection()) {
										ProgressDialog progress = new ProgressDialog(
												ItemDetailsActivity.this);
										progress.setMessage("Please wait..");
										progress.setCanceledOnTouchOutside(false);
										int corePoolSize = 60;
										int maximumPoolSize = 80;
										int keepAliveTime = 10;
										BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
												maximumPoolSize);
										Executor threadPoolExecutor = new ThreadPoolExecutor(
												corePoolSize, maximumPoolSize,
												keepAliveTime,
												TimeUnit.SECONDS, workQueue);
										new LikeTask(progress)
												.executeOnExecutor(threadPoolExecutor);
									}
								} else {
									Log.e("", "tttttttttttt 222222");
									if (checkInternetConnection()) {
										ProgressDialog progress = new ProgressDialog(
												ItemDetailsActivity.this);
										progress.setMessage("Please wait..");
										progress.setCanceledOnTouchOutside(false);
										int corePoolSize = 60;
										int maximumPoolSize = 80;
										int keepAliveTime = 10;
										BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
												maximumPoolSize);
										Executor threadPoolExecutor = new ThreadPoolExecutor(
												corePoolSize, maximumPoolSize,
												keepAliveTime,
												TimeUnit.SECONDS, workQueue);
										new UnLikeTask(progress)
												.executeOnExecutor(threadPoolExecutor);
									}
								}

							}
						});
						String isFollow = item.getFollow();
						if (isFollow.contains("1")) {
							mFollowToggle.setChecked(true);
							mFollowToggle
									.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(
												CompoundButton buttonView,
												boolean isChecked) {
											Log.e("", "onCheckedChanged");

											if (isChecked) {
												Log.e("", "isCheckedisChecked");
												showCustomToast("Follow");
												followUnfollowUser();
											} else {
												showCustomToast("Unfollow");
												followUnfollowUser();
											}

										}

									});
						} else {
							mFollowToggle.setChecked(false);
							mFollowToggle
									.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(
												CompoundButton buttonView,
												boolean isChecked) {
											Log.e("", "onCheckedChanged");

											if (isChecked) {
												Log.e("", "isCheckedisChecked");
												followUnfollowUser();
											}
										}

									});
						}

						String path = "http://54.149.99.130/ws/sharing_page.php?userid="
								+ item.getUserId()
								+ "&itemid="
								+ item.getItemId()
								+ "&login_user_id="
								+ mUIDStr;

						// String path = "http://nanostuffs.com";
						// String path = ". Check it out @ " + mSharePath;
						path = ". Check it out @ " + path;
						// mShareText = item.getItemName() + " " +
						// item.getPrice()
						// + " at " + item.getLocation()
						// + ". Check it out @MaiMai";
						// String path =
						// "https://play.google.com/store/apps/details?id=com.nanostuffs.maimai";
						mShareText = item.getItemName() + " " + item.getPrice()
								+ " at " + item.getLocation() + path;

						mShareBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										actionBarSearch.getWindowToken(), 0);
								String shareBody = mShareText;
								Intent sharingIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								sharingIntent.setType("text/plain");
								sharingIntent.putExtra(
										android.content.Intent.EXTRA_SUBJECT,
										"MaiMai");
								sharingIntent.putExtra(
										android.content.Intent.EXTRA_TEXT,
										shareBody);
								startActivity(Intent
										.createChooser(
												sharingIntent,
												getResources().getString(
														R.string.share_this)));

							}
						});

						/********* User image ************/
						userImage = item.getUserPic();
						userImage = userImage.replace("\\/", "/");
						Log.e("", "imageeeeeeeee : " + userImage);
						androidAQuery.id(mUserImage).image(userImage, false,
								false, 0, R.drawable.user_photo);

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
								androidAQuery.id(mImage1).image(imagePath,
										false, false, 0,
										R.drawable.defaultimage2);
								mImage1.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										viewImage(imagePath);

									}
								});
							} else {
								itemImageArr = imagePath.split(",");
								switch (itemImageArr.length) {
								case 1:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									viewImage(itemImageArr[0]);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});
									break;
								case 2:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									break;
								case 3:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});
									break;
								case 4:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});
									break;
								case 5:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});
									break;
								case 6:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});

									itemImageArr[5] = itemImageArr[5]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[5] : "
											+ itemImageArr[5]);
									mImage6.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage6).image(
											itemImageArr[5], false, false, 0,
											R.drawable.defaultimage2);
									mImage6.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[5]);

										}
									});
									break;
								case 7:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});

									itemImageArr[5] = itemImageArr[5]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[5] : "
											+ itemImageArr[5]);
									mImage6.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage6).image(
											itemImageArr[5], false, false, 0,
											R.drawable.defaultimage2);
									mImage6.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[5]);

										}
									});

									itemImageArr[6] = itemImageArr[6]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[6] : "
											+ itemImageArr[6]);
									mImage7.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage7).image(
											itemImageArr[6], false, false, 0,
											R.drawable.defaultimage2);
									mImage7.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[6]);

										}
									});
									break;
								case 8:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});

									itemImageArr[5] = itemImageArr[5]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[5] : "
											+ itemImageArr[5]);
									mImage6.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage6).image(
											itemImageArr[5], false, false, 0,
											R.drawable.defaultimage2);
									mImage6.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[5]);

										}
									});

									itemImageArr[6] = itemImageArr[6]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[6] : "
											+ itemImageArr[6]);
									mImage7.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage7).image(
											itemImageArr[6], false, false, 0,
											R.drawable.defaultimage2);
									mImage7.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[6]);

										}
									});

									itemImageArr[7] = itemImageArr[7]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[7] : "
											+ itemImageArr[7]);
									mImage8.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage8).image(
											itemImageArr[7], false, false, 0,
											R.drawable.defaultimage2);
									mImage8.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[7]);

										}
									});
									break;
								case 9:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});

									itemImageArr[5] = itemImageArr[5]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[5] : "
											+ itemImageArr[5]);
									mImage6.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage6).image(
											itemImageArr[5], false, false, 0,
											R.drawable.defaultimage2);
									mImage6.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[5]);

										}
									});

									itemImageArr[6] = itemImageArr[6]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[6] : "
											+ itemImageArr[6]);
									mImage7.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage7).image(
											itemImageArr[6], false, false, 0,
											R.drawable.defaultimage2);
									mImage7.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[6]);

										}
									});

									itemImageArr[7] = itemImageArr[7]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[7] : "
											+ itemImageArr[7]);
									mImage8.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage8).image(
											itemImageArr[7], false, false, 0,
											R.drawable.defaultimage2);
									mImage8.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[7]);

										}
									});

									itemImageArr[8] = itemImageArr[8]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[8] : "
											+ itemImageArr[8]);
									mImage9.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage9).image(
											itemImageArr[8], false, false, 0,
											R.drawable.defaultimage2);
									mImage9.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[8]);

										}
									});
									break;
								case 10:
									itemImageArr[0] = itemImageArr[0]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[0] : "
											+ itemImageArr[0]);
									mImage1.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage1).image(
											itemImageArr[0], false, false, 0,
											R.drawable.defaultimage2);
									mImage1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[0]);

										}
									});

									itemImageArr[1] = itemImageArr[1]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[1] : "
											+ itemImageArr[1]);
									mImage2.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage2).image(
											itemImageArr[1], false, false, 0,
											R.drawable.defaultimage2);
									mImage2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[1]);

										}
									});

									itemImageArr[2] = itemImageArr[2]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[2] : "
											+ itemImageArr[2]);
									mImage3.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage3).image(
											itemImageArr[2], false, false, 0,
											R.drawable.defaultimage2);
									mImage3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[2]);

										}
									});

									itemImageArr[3] = itemImageArr[3]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[3] : "
											+ itemImageArr[3]);
									mImage4.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage4).image(
											itemImageArr[3], false, false, 0,
											R.drawable.defaultimage2);
									mImage4.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[3]);

										}
									});

									itemImageArr[4] = itemImageArr[4]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[4] : "
											+ itemImageArr[4]);
									mImage5.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage5).image(
											itemImageArr[4], false, false, 0,
											R.drawable.defaultimage2);
									mImage5.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[4]);

										}
									});

									itemImageArr[5] = itemImageArr[5]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[5] : "
											+ itemImageArr[5]);
									mImage6.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage6).image(
											itemImageArr[5], false, false, 0,
											R.drawable.defaultimage2);
									mImage6.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[5]);

										}
									});

									itemImageArr[6] = itemImageArr[6]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[6] : "
											+ itemImageArr[6]);
									mImage7.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage7).image(
											itemImageArr[6], false, false, 0,
											R.drawable.defaultimage2);
									mImage7.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[6]);

										}
									});

									itemImageArr[7] = itemImageArr[7]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[7] : "
											+ itemImageArr[7]);
									mImage8.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage8).image(
											itemImageArr[7], false, false, 0,
											R.drawable.defaultimage2);
									mImage8.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[7]);

										}
									});

									itemImageArr[8] = itemImageArr[8]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[8] : "
											+ itemImageArr[8]);
									mImage9.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage9).image(
											itemImageArr[8], false, false, 0,
											R.drawable.defaultimage2);
									mImage9.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[9]);

										}
									});

									itemImageArr[9] = itemImageArr[9]
											.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "itemImageArr[9] : "
											+ itemImageArr[9]);
									mImage10.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage10).image(
											itemImageArr[9], false, false, 0,
											R.drawable.defaultimage2);
									mImage10.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											viewImage(itemImageArr[9]);

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
						if (videoPath.contains(".mov")) {
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
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);
										break;
									case 2:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[1]);
										break;
									case 3:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);
										break;
									case 4:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										break;
									case 5:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										itemVideoArr[4] = itemVideoArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[4] : "
												+ itemVideoArr[4]);
										break;
									case 6:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										itemVideoArr[4] = itemVideoArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[4] : "
												+ itemVideoArr[4]);

										itemVideoArr[5] = itemVideoArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[5] : "
												+ itemVideoArr[5]);
										break;
									case 7:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										itemVideoArr[4] = itemVideoArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[4] : "
												+ itemVideoArr[4]);

										itemVideoArr[5] = itemVideoArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[5] : "
												+ itemVideoArr[5]);

										itemVideoArr[6] = itemVideoArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[6] : "
												+ itemVideoArr[6]);
										break;
									case 8:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										itemVideoArr[4] = itemVideoArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[4] : "
												+ itemVideoArr[4]);

										itemVideoArr[5] = itemVideoArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[5] : "
												+ itemVideoArr[5]);

										itemVideoArr[6] = itemVideoArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[6] : "
												+ itemVideoArr[6]);

										itemVideoArr[7] = itemVideoArr[7]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[7] : "
												+ itemVideoArr[7]);
										break;
									case 9:
										itemVideoArr[0] = itemVideoArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[0] : "
												+ itemVideoArr[0]);

										itemVideoArr[1] = itemVideoArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[1] : "
												+ itemVideoArr[1]);

										itemVideoArr[2] = itemVideoArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[2] : "
												+ itemVideoArr[2]);

										itemVideoArr[3] = itemVideoArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[3] : "
												+ itemVideoArr[3]);

										itemVideoArr[4] = itemVideoArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[4] : "
												+ itemVideoArr[4]);

										itemVideoArr[5] = itemVideoArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[5] : "
												+ itemVideoArr[5]);

										itemVideoArr[6] = itemVideoArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[6] : "
												+ itemVideoArr[6]);

										itemVideoArr[7] = itemVideoArr[7]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[7] : "
												+ itemVideoArr[7]);

										itemVideoArr[8] = itemVideoArr[8]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemVideoArr[8] : "
												+ itemVideoArr[8]);
										break;

									default:
										break;
									}

								}
							}
						}
						thumbPath = item.getThumb();
						if (thumbPath.contains(".png")) {
							Log.e("", "thumbPaththumbPath : " + thumbPath);
							if (!thumbPath.equalsIgnoreCase("[]")) {
								if (!item.getThumb().contains(",")) {
									thumbPath = thumbPath.replace("\\/", "/")
											.replace("[", "").replace("]", "")
											.replace("\"", "");
									Log.e("", "thumbPaththumbPaththumbPath : "
											+ thumbPath);
									mImage10.setVisibility(View.VISIBLE);
									androidAQuery.id(mImage10).image(thumbPath,
											false, false, 0,
											R.drawable.defaultimage2);
									mRelThumb10.setVisibility(View.VISIBLE);
									mRelThumb10
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
													imm.hideSoftInputFromWindow(
															actionBarSearch
																	.getWindowToken(),
															0);
													playVideo(videoPath);
													/*Intent intent = new Intent(
															ItemDetailsActivity.this,
															VideoActivity.class);
													intent.putExtra("video",
															videoPath);
													startActivity(intent);
													overridePendingTransition(
															R.anim.slide_in_right,
															R.anim.slide_out_left);*/
												}
											});
									mThumbImage10
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
													imm.hideSoftInputFromWindow(
															actionBarSearch
																	.getWindowToken(),
															0);
													playVideo(videoPath);

													/*Intent intent = new Intent(
															ItemDetailsActivity.this,
															VideoActivity.class);
													intent.putExtra("video",
															videoPath);
													startActivity(intent);
													overridePendingTransition(
															R.anim.slide_in_right,
															R.anim.slide_out_left);*/
												}
											});
								} else {
									Log.e("", "thumbPaththumbPaththumbPath : "
											+ thumbPath);
									itemThumbArr = thumbPath.split(",");
									switch (itemThumbArr.length) {
									case 1:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");

										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);

														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										break;
									case 2:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
										/*				Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										break;
									case 3:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[2]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[2]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										break;
									case 4:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[1]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[2]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[2]);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[3]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});
										mThumbImage7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[3]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										break;
									case 5:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														playVideo(itemVideoArr[0]);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[1]);
													}
												});

										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*ntent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[2]);
													}
												});

										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[3]);
													}
												});

										itemThumbArr[4] = itemThumbArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[4] : "
												+ itemThumbArr[4]);
										mImage6.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage6).image(
												itemThumbArr[4], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb6.setVisibility(View.VISIBLE);
										mRelThumb6
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[4]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[4]);
													}
												});
										break;
									case 6:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[0]);
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[1]);
													}
												});

										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[2]);
													}
												});

										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[3]);
													}
												});

										itemThumbArr[4] = itemThumbArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[4] : "
												+ itemThumbArr[4]);
										mImage6.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage6).image(
												itemThumbArr[4], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb6.setVisibility(View.VISIBLE);
										mRelThumb6
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[4]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[4]);
													}
												});

										itemThumbArr[5] = itemThumbArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[5] : "
												+ itemThumbArr[5]);
										mImage5.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage5).image(
												itemThumbArr[5], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb5.setVisibility(View.VISIBLE);
										mRelThumb5
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[5]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[5]);
													}
												});

										itemThumbArr[6] = itemThumbArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[6] : "
												+ itemThumbArr[6]);
										mImage4.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage4).image(
												itemThumbArr[6], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb4.setVisibility(View.VISIBLE);
										mRelThumb4
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[6]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[6]);
													}
												});

										break;
									case 8:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[0]);
													}
												});

										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[1]);
													}
												});

										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[2]);
													}
												});

										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[3]);
													}
												});

										itemThumbArr[4] = itemThumbArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[4] : "
												+ itemThumbArr[4]);
										mImage6.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage6).image(
												itemThumbArr[4], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb6.setVisibility(View.VISIBLE);
										mRelThumb6
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[4]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[4]);
													}
												});

										itemThumbArr[5] = itemThumbArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[5] : "
												+ itemThumbArr[5]);
										mImage5.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage5).image(
												itemThumbArr[5], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb5.setVisibility(View.VISIBLE);
										mRelThumb5
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[5]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[5]);
													}
												});

										itemThumbArr[6] = itemThumbArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[6] : "
												+ itemThumbArr[6]);
										mImage4.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage4).image(
												itemThumbArr[6], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb4.setVisibility(View.VISIBLE);
										mRelThumb4
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[6]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[6]);
													}
												});

										itemThumbArr[7] = itemThumbArr[7]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[7] : "
												+ itemThumbArr[7]);
										mImage3.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage3).image(
												itemThumbArr[7], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb3.setVisibility(View.VISIBLE);
										mRelThumb3
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[7]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[7]);
													}
												});

										break;
									case 9:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[0]);
													}
												});
										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[1]);
													}
												});
										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[2]);
													}
												});
										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[3]);
													}
												});
										itemThumbArr[4] = itemThumbArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[4] : "
												+ itemThumbArr[4]);
										mImage6.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage6).image(
												itemThumbArr[4], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb6.setVisibility(View.VISIBLE);
										mRelThumb6
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[4]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[4]);
													}
												});
										itemThumbArr[5] = itemThumbArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[5] : "
												+ itemThumbArr[5]);
										mImage5.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage5).image(
												itemThumbArr[5], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb5.setVisibility(View.VISIBLE);
										mRelThumb5
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[5]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[5]);
													}
												});
										itemThumbArr[6] = itemThumbArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[6] : "
												+ itemThumbArr[6]);
										mImage4.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage4).image(
												itemThumbArr[6], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb4.setVisibility(View.VISIBLE);
										mRelThumb4
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[6]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[6]);
													}
												});
										itemThumbArr[7] = itemThumbArr[7]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[7] : "
												+ itemThumbArr[7]);
										mImage3.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage3).image(
												itemThumbArr[7], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb3.setVisibility(View.VISIBLE);
										mRelThumb3
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[7]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[7]);
													}
												});
										itemThumbArr[8] = itemThumbArr[8]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[8] : "
												+ itemThumbArr[8]);
										mImage2.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage2).image(
												itemThumbArr[8], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb2.setVisibility(View.VISIBLE);
										mRelThumb2
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[8]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[8]);
													}
												});
										break;
									case 10:
										itemThumbArr[0] = itemThumbArr[0]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[0] : "
												+ itemThumbArr[0]);
										mImage10.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage10).image(
												itemThumbArr[0], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb10.setVisibility(View.VISIBLE);
										mRelThumb10
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[0]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[0]);
													}
												});
										itemThumbArr[1] = itemThumbArr[1]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[1] : "
												+ itemThumbArr[1]);
										mImage9.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage9).image(
												itemThumbArr[1], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb9.setVisibility(View.VISIBLE);
										mRelThumb9
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[1]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[1]);
													}
												});
										itemThumbArr[2] = itemThumbArr[2]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[2] : "
												+ itemThumbArr[2]);
										mImage8.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage8).image(
												itemThumbArr[2], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb8.setVisibility(View.VISIBLE);
										mRelThumb8
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[2]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[2]);
													}
												});
										itemThumbArr[3] = itemThumbArr[3]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[3] : "
												+ itemThumbArr[3]);
										mImage7.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage7).image(
												itemThumbArr[3], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb7.setVisibility(View.VISIBLE);
										mRelThumb7
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
												/*		Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[3]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[3]);
													}
												});
										itemThumbArr[4] = itemThumbArr[4]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[4] : "
												+ itemThumbArr[4]);
										mImage6.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage6).image(
												itemThumbArr[4], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb6.setVisibility(View.VISIBLE);
										mRelThumb6
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[4]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[4]);
													}
												});
										itemThumbArr[5] = itemThumbArr[5]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[5] : "
												+ itemThumbArr[5]);
										mImage5.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage5).image(
												itemThumbArr[5], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb5.setVisibility(View.VISIBLE);
										mRelThumb5
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[5]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[5]);
													}
												});
										itemThumbArr[6] = itemThumbArr[6]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[6] : "
												+ itemThumbArr[6]);
										mImage4.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage4).image(
												itemThumbArr[6], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb4.setVisibility(View.VISIBLE);
										mRelThumb4
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
											/*			Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[6]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[6]);
													}
												});
										itemThumbArr[7] = itemThumbArr[7]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[7] : "
												+ itemThumbArr[7]);
										mImage3.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage3).image(
												itemThumbArr[7], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb3.setVisibility(View.VISIBLE);
										mRelThumb3
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[7]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[7]);
													}
												});
										itemThumbArr[8] = itemThumbArr[8]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[8] : "
												+ itemThumbArr[8]);
										mImage2.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage2).image(
												itemThumbArr[8], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb2.setVisibility(View.VISIBLE);
										mRelThumb2
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[8]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[8]);
													}
												});

										itemThumbArr[9] = itemThumbArr[9]
												.replace("\\/", "/")
												.replace("[", "")
												.replace("]", "")
												.replace("\"", "");
										Log.e("", "itemThumbArr[9] : "
												+ itemThumbArr[9]);
										mImage1.setVisibility(View.VISIBLE);
										androidAQuery.id(mImage1).image(
												itemThumbArr[9], false, false,
												0, R.drawable.defaultimage2);
										mRelThumb1.setVisibility(View.VISIBLE);
										mRelThumb1
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
														/*Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
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
														InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
														imm.hideSoftInputFromWindow(
																actionBarSearch
																		.getWindowToken(),
																0);
													/*	Intent intent = new Intent(
																ItemDetailsActivity.this,
																VideoActivity.class);
														intent.putExtra(
																"video",
																itemVideoArr[9]);
														startActivity(intent);
														overridePendingTransition(
																R.anim.slide_in_right,
																R.anim.slide_out_left);*/
														playVideo(itemVideoArr[9]);
													}
												});
										break;
									default:
										break;
									}

								}
							}
						}

						// /********* Item Videos *****************/
						// String videoPath = item.getVideo();
						// if (!videoPath.equalsIgnoreCase("[]")) {
						// if (!item.getVideo().contains(",")) {
						// videoPath = videoPath.replace("\\/", "/")
						// .replace("[", "").replace("]", "")
						// .replace("\"", "");
						// Log.e("", "videoPathvideoPathvideoPath : " +
						// videoPath);
						// mVideo10.setVisibility(View.VISIBLE);
						// MediaController mediaController = new
						// MediaController(
						// ItemDetailsActivity.this);
						// mediaController.setAnchorView(mVideo10);
						//
						// mVideo10.setMediaController(mediaController);
						// mVideo10.setVideoURI(Uri.parse(videoPath));
						// mVideo10.requestFocus();
						// mVideo10.start();
						// } else {
						// String[] itemVideoArr = videoPath.split(",");
						// switch (itemVideoArr.length) {
						// case 1:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
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
						// MediaController mediaController = new
						// MediaController(
						// ItemDetailsActivity.this);
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
						// mVideo10.start();
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// Bitmap bMap1 = null;
						// try {
						// bMap1 = Media.getBitmap(
						// ItemDetailsActivity.this
						// .getContentResolver(), Uri
						// .parse(itemVideoArr[1]));
						// } catch (FileNotFoundException e) {
						// e.printStackTrace();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// mImage9.setImageBitmap(bMap1);
						// mImage9.setVisibility(View.VISIBLE);
						// // mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// break;
						// case 3:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						// break;
						// case 4:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						// break;
						// case 5:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						// break;
						// case 6:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						//
						// itemVideoArr[5] = itemVideoArr[5]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
						// mVideo5.setVisibility(View.VISIBLE);
						// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
						// break;
						// case 7:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						//
						// itemVideoArr[5] = itemVideoArr[5]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
						// mVideo5.setVisibility(View.VISIBLE);
						// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
						//
						// itemVideoArr[6] = itemVideoArr[6]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
						// mVideo4.setVisibility(View.VISIBLE);
						// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
						// break;
						// case 8:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						//
						// itemVideoArr[5] = itemVideoArr[5]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
						// mVideo5.setVisibility(View.VISIBLE);
						// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
						//
						// itemVideoArr[6] = itemVideoArr[6]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
						// mVideo4.setVisibility(View.VISIBLE);
						// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
						//
						// itemVideoArr[7] = itemVideoArr[7]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
						// mVideo3.setVisibility(View.VISIBLE);
						// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
						// break;
						// case 9:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoURI(Uri.parse(itemVideoArr[0]));
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						//
						// itemVideoArr[5] = itemVideoArr[5]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
						// mVideo5.setVisibility(View.VISIBLE);
						// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
						//
						// itemVideoArr[6] = itemVideoArr[6]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
						// mVideo4.setVisibility(View.VISIBLE);
						// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
						//
						// itemVideoArr[7] = itemVideoArr[7]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
						// mVideo3.setVisibility(View.VISIBLE);
						// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
						//
						// itemVideoArr[8] = itemVideoArr[8]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[8] : " + itemVideoArr[8]);
						// mVideo2.setVisibility(View.VISIBLE);
						// mVideo2.setVideoURI(Uri.parse(itemVideoArr[8]));
						// break;
						// case 10:
						// itemVideoArr[0] = itemVideoArr[0]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[0] : " + itemVideoArr[0]);
						// mVideo10.setVisibility(View.VISIBLE);
						// mVideo10.setVideoPath(itemVideoArr[0]);
						//
						// itemVideoArr[1] = itemVideoArr[1]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[1] : " + itemVideoArr[1]);
						// mVideo9.setVisibility(View.VISIBLE);
						// mVideo9.setVideoURI(Uri.parse(itemVideoArr[1]));
						//
						// itemVideoArr[2] = itemVideoArr[2]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[2] : " + itemVideoArr[2]);
						// mVideo8.setVisibility(View.VISIBLE);
						// mVideo8.setVideoURI(Uri.parse(itemVideoArr[2]));
						//
						// itemVideoArr[3] = itemVideoArr[3]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[3] : " + itemVideoArr[3]);
						// mVideo7.setVisibility(View.VISIBLE);
						// mVideo7.setVideoURI(Uri.parse(itemVideoArr[3]));
						//
						// itemVideoArr[4] = itemVideoArr[4]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[4] : " + itemVideoArr[4]);
						// mVideo6.setVisibility(View.VISIBLE);
						// mVideo6.setVideoURI(Uri.parse(itemVideoArr[4]));
						//
						// itemVideoArr[5] = itemVideoArr[5]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[5] : " + itemVideoArr[5]);
						// mVideo5.setVisibility(View.VISIBLE);
						// mVideo5.setVideoURI(Uri.parse(itemVideoArr[5]));
						//
						// itemVideoArr[6] = itemVideoArr[6]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[6] : " + itemVideoArr[6]);
						// mVideo4.setVisibility(View.VISIBLE);
						// mVideo4.setVideoURI(Uri.parse(itemVideoArr[6]));
						//
						// itemVideoArr[7] = itemVideoArr[7]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[7] : " + itemVideoArr[7]);
						// mVideo3.setVisibility(View.VISIBLE);
						// mVideo3.setVideoURI(Uri.parse(itemVideoArr[7]));
						//
						// itemVideoArr[8] = itemVideoArr[8]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[8] : " + itemVideoArr[8]);
						// mVideo2.setVisibility(View.VISIBLE);
						// mVideo2.setVideoURI(Uri.parse(itemVideoArr[8]));
						//
						// itemVideoArr[9] = itemVideoArr[9]
						// .replace("\\/", "/").replace("[", "")
						// .replace("]", "").replace("\"", "");
						// Log.e("", "itemVideoArr[9] : " + itemVideoArr[9]);
						// mVideo1.setVisibility(View.VISIBLE);
						// mVideo1.setVideoURI(Uri.parse(itemVideoArr[9]));
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

						ago = item.getDays();
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

						mMsgSeller.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.e("",
										"11111 toid aaaaaaaaaaaaa : "
												+ item.getUserId());
								Intent intent = new Intent(
										ItemDetailsActivity.this,
										MessagingSellerDirectly.class);
								intent.putExtra("to", item.getUserId());
								intent.putExtra("from", mUIDStr);
								intent.putExtra("userImage", userImage);
								intent.putExtra("name", item.getUserName());
								intent.putExtra("ago", ago);
								intent.putExtra("test", "1");
								startActivity(intent);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
							}
						});
						mTimeAgo.setText("Posted " + ago);
						mItemName.setText(item.getItemName());
						mItemDescription.setText(item.getDescription());
						mPrice.setText(item.getPrice());
						mItemLocation.setText(item.getLocation());
						mItemPrice.setText(item.getPrice());
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

						mItemReportAbuse
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												ItemDetailsActivity.this);
										builder.setMessage("Are you sure you want to report abuse for this item ?");
										builder.setIcon(R.drawable.alert);
										builder.setTitle("Report Abuse");
										builder.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														if (checkInternetConnection()) {
															ProgressDialog progress = new ProgressDialog(
																	ItemDetailsActivity.this);
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
												})
												.setNegativeButton(
														"No",
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

						mReportAbuseImage
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												ItemDetailsActivity.this);
										builder.setMessage("Are you sure you want to report abuse for this item ?");
										builder.setIcon(R.drawable.alert);
										builder.setTitle("Report Abuse");
										builder.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														if (checkInternetConnection()) {
															if (checkInternetConnection()) {
																ProgressDialog progress = new ProgressDialog(
																		ItemDetailsActivity.this);
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
												})
												.setNegativeButton(
														"No",
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
			} catch (Exception e) {
			}
		}
	}

	private void showConnectionTimeoutMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ItemDetailsActivity.this);
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

	public class LikeUnlikeTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public LikeUnlikeTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = likeUnlikeFromWeb();

			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			// progress.dismiss();
			Log.e("", "onPostExecuteonPostExecuteonPostExecuteonPostExecute");
		}
	}

	private String likeUnlikeFromWeb() {
		String postURL = "http://54.149.99.130/ws/item_like.php?itemid=1&uid=1";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemID));
			params.add(new BasicNameValuePair("uid", mUserID));
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

	public class ReportAbuseTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public ReportAbuseTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = reportAbuseToWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			Log.e("test", "result  : " + result);
			if (result.contains("Success")) {
				// Toast.makeText(ItemDetailsActivity.this,
				// "Thank you for reporting ", Toast.LENGTH_SHORT).show();
				showCustomToast("Thank you for reporting");
			} else {
				// Toast.makeText(ItemDetailsActivity.this, "Try again",
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
		Log.e("", "getItemsDetailsFromWeb mUIDStr : " + mUIDStr);
		Log.e("", "mUserID : " + mUserID + "mItemID : " + mItemID);
		String postURL = "http://54.149.99.130/ws/get_user_item.php?userid=&login_user_id=&itemid=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mUserID));
			params.add(new BasicNameValuePair("itemid", mItemID));
			params.add(new BasicNameValuePair("login_user_id", mUIDStr));
			mSharePath = "http://54.149.99.130/ws/get_user_item.php?userid="
					+ mUserID + "&itemid=" + mItemID + "&login_user_id="
					+ mUIDStr;
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
							startActivity(new Intent(ItemDetailsActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(ItemDetailsActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(ItemDetailsActivity.this,
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
												ItemDetailsActivity.this,
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
				startActivity(new Intent(ItemDetailsActivity.this,
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
				startActivity(new Intent(ItemDetailsActivity.this,
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
						if (listWithoutDuplicates.size() != 0) {
							final SearchAdapter adapter = new SearchAdapter(
									ItemDetailsActivity.this,
									listWithoutDuplicates);
							actionBarSearch.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}

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
					ItemDetailsActivity.this);
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
						ItemDetailsActivity.this);
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
						startActivity(new Intent(ItemDetailsActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								ItemDetailsActivity.this);
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
		switch (v.getId()) {
		case R.id.send_btn:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
			if (mComment.getText().toString().trim().length() != 0) {
				mAddComment = mComment.getText().toString();
				sendComment();
				mComment.setText("");
			}

			break;

		default:
			break;
		}
	}

	private void sendComment() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Posting..");
			// progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new AddCommentsTask(progress).executeOnExecutor(threadPoolExecutor);
		}
	}

	public class AddCommentsTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public AddCommentsTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = addCommentsToWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();

				Log.e("test", "result comment : " + result);

				if (result.contains("Success")) {
					getComments();
				}
			} catch (Exception e) {
			}
		}
	}

	private String addCommentsToWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		if (!mUIDStr.trim().equals(mUserID.trim())) {
			mRepliedCount = "1";

		} else {
			mRepliedCount = "0";
		}
		String postURL = "http://54.149.99.130/ws/add-comment.php?item_id=&user_id=&comment=&replied_cnt=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", mUIDStr));
			params.add(new BasicNameValuePair("item_id", mItemID));
			params.add(new BasicNameValuePair("comment", mAddComment));
			params.add(new BasicNameValuePair("replied_cnt", mRepliedCount));

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
}
