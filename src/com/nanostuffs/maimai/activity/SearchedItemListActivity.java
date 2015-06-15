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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.adapter.ItemListAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.model.Item;
import com.nanostuffs.maimai.model.SearchItem;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView.IXListViewListener;

public class SearchedItemListActivity extends Activity implements
		IXListViewListener {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private XListView mItemListView;
	private Handler mHandler;
	private AutoCompleteTextView actionBarSearch;
	private ArrayList<SearchItem> mItemList;
	private String list;
	private ProgressDialog progress;
	private SearchItem mSearchedItem;
	private TextView mEmpty;
	private String mKeyword;
	private Item item;
	private ArrayList<Item> mItem;
	private String mItemID;
	private String mUserID;
	public int offset;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searched_item_list);
		actionBarDetails();
		
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
		try {
			initializeComponents();
			if (getIntent() != null) {
				actionBarSearch.setText(getIntent().getExtras().getString(
						"itemname"));
				getSearchedList(getIntent().getExtras().getString("itemname"));
			}
		} catch (Exception e) {
		}

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkReceiver);
	}

	private void initializeComponents() {
		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");
		mHandler = new Handler();
		mItemListView = (XListView) findViewById(R.id.item_list);
		mItemListView.setPullLoadEnable(true);
		mItemListView.setPullRefreshEnable(true);
		mEmpty = (TextView) findViewById(R.id.empty);
		mEmpty.setTypeface(mActionBarTypeface);
		mItemList = new ArrayList<SearchItem>();
		mItem = new ArrayList<Item>();
	}

	private void getSearchedList(String search) {
		mKeyword = search;
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(this);
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			new RetriveSearchListTask1(progress).execute(mKeyword);
		}
	}

	public class RetriveSearchListTask1 extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public RetriveSearchListTask1(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = getSearchDetailsFromWeb1();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchedItemListActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getSearchedList(getIntent().getExtras()
										.getString("itemname"));
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {

				Log.e("test", "resulttttttttttt  : ");
				if (mItem != null) {
					mItem.clear();
				}
				try {
					JSONObject responseObj = new JSONObject(result);

					JSONArray ja = responseObj.getJSONArray("data");
					if (ja.length() == 0) {
						mEmpty.setVisibility(View.VISIBLE);
					} else {
						mEmpty.setVisibility(View.GONE);

						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = (JSONObject) ja.get(i);
							item = new Item();
							item.setItemId(jo.getString("Id"));
							item.setItemImage(jo.getString("Image"));
							item.setItemName(jo.getString("Name"));
							item.setUserId(jo.getString("UserId"));
							item.setUserName(jo.getString("Username"));
							// item.setDescription(jo.getString("Description"));
							item.setPrice(jo.getString("Price"));
							item.setLocation(jo.getString("Locations"));
							item.setDays(jo.getString("NoOfDays"));
							item.setIsLike(jo.getString("user_likes"));

							mItem.add(item);
						}
						if (mItem.size() != 0) {
							mItemListView.setPullLoadEnable(true);
							mItemListView
									.setXListViewListener(SearchedItemListActivity.this);
							Parcelable state = mItemListView
									.onSaveInstanceState();
							mItemListView.onRestoreInstanceState(state);
							final ItemListAdapter adapter = new ItemListAdapter(
									SearchedItemListActivity.this, mItem);
							mItemListView.setAdapter(adapter);
						}

					}

					mItemListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									Log.e("", "onItemClick......");
									Log.e("",
											"Nameeeee : "
													+ mItem);
									mItemID = mItem.get(position - 1)
											.getItemId();
									mUserID = mItem.get(position - 1)
											.getUserId();
									Log.e("",
											"Nameeeee : "
													+ mItem.get(position - 1)
															.getItemName());
									incrementViewedCount();
									startActivity(new Intent(
											SearchedItemListActivity.this,
											ItemDetailsActivity.class)
											.setFlags(
													Intent.FLAG_ACTIVITY_CLEAR_TOP)
											.putExtra("itemid", mItemID)
											.putExtra("activity","search")
											.putExtra("userid", mUserID));
									overridePendingTransition(
											R.anim.slide_in_right,
											R.anim.slide_out_left);
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

	public class IncrementCategoriesViewedCountTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public IncrementCategoriesViewedCountTask(ProgressDialog progress) {
			// this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = incrementViewedCountToWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			Log.e("", "increment result b" + result);
		}
	}

	private void incrementViewedCount() {
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
			new IncrementCategoriesViewedCountTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	private String incrementViewedCountToWeb() {
		String postURL = "http://54.149.99.130/ws/item_view_count.php?itemid=2";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
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

	private String getSearchDetailsFromWeb1() {
		String postURL = "http://54.149.99.130/ws/search_item.php?keyword=he";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("keyword", mKeyword));
			// params.add(new BasicNameValuePair("offset", Integer
			// .toString(offset)));
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
									SearchedItemListActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(
									SearchedItemListActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(
									SearchedItemListActivity.this,
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
					if (checkInternetConnection()) {
						new RetriveSearchListTask().execute(actionBarSearch
								.getText().toString());
					}
				}

				actionBarSearch
						.setOnEditorActionListener(new OnEditorActionListener() {
							@Override
							public boolean onEditorAction(TextView v,
									int actionId, KeyEvent event) {
								if (actionId == EditorInfo.IME_ACTION_SEARCH) {
									if (actionBarSearch.length() >= 1) {
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(
												actionBarSearch
														.getWindowToken(), 0);
										getSearchedList(actionBarSearch
												.getText().toString());

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
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		ImageView sell = (ImageView) addView.findViewById(R.id.camera);
		sell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
				startActivity(new Intent(SearchedItemListActivity.this,
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
				startActivity(new Intent(SearchedItemListActivity.this,
						AllChatsActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		getActionBar().setCustomView(addView);
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
						mSearchedItem = new SearchItem();
						mSearchedItem.setItemName(jo.getString("Name"));
						mItemList.add(mSearchedItem);
					}
					if (mItemList.size() != 0) {
						List<SearchItem> listWithoutDuplicates = new ArrayList<SearchItem>(
								mItemList);
						final SearchAdapter1 adapter = new SearchAdapter1(
								SearchedItemListActivity.this,
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
					SearchedItemListActivity.this);
			progress.setMessage("Logging out...");
			new LogoutTask(progress).execute();
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
						SearchedItemListActivity.this);
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
						startActivity(new Intent(SearchedItemListActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								SearchedItemListActivity.this);
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

	private void onLoad() {
		mItemListView.stopRefresh();
		mItemListView.stopLoadMore();
		mItemListView.setRefreshTime("");

	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// ++curpage;
				mEmpty.setVisibility(View.GONE);
				mItemListView.startProgress();
				onLoad();
			}

		}, 2000);
	}

	public class SearchAdapter1 extends ArrayAdapter<SearchItem> {
		private Context mContext;
		private List<SearchItem> mValues;
		private Typeface mActionBarTypeface;
		private LayoutInflater mInflater;
		private String mItemName;

		public SearchAdapter1(Context context,
				List<SearchItem> listWithoutDuplicates) {
			super(context, R.layout.search_list_item, listWithoutDuplicates);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mContext = context;
			this.mValues = listWithoutDuplicates;
		}

		@Override
		public int getCount() {
			return mValues.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public SearchItem getItem(int position) {
			return mValues.get(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.search_list_item, null);
			}
			mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/verdana.ttf");
			TextView item = (TextView) convertView.findViewById(R.id.name);
			item.setTypeface(mActionBarTypeface);
			item.setText(mValues.get(position).getItemName());
			convertView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							actionBarSearch.getWindowToken(), 0);
					mItemName = mValues.get(position).getItemName();
					getSearchedList(mItemName);
					return false;
				}
			});
			return convertView;
		}

	}
}
