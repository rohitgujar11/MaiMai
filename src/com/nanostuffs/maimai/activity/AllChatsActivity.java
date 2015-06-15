package com.nanostuffs.maimai.activity;

import java.util.ArrayList;
import java.util.Collections;
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

import android.R.integer;
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
import android.nfc.Tag;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.HomeActivity.RetriveChatTask;
import com.nanostuffs.maimai.adapter.AllChatsAdapter;
import com.nanostuffs.maimai.adapter.NavgListAdapter;
import com.nanostuffs.maimai.adapter.SearchAdapter;
import com.nanostuffs.maimai.model.Chat;
import com.nanostuffs.maimai.model.SearchItem;

public class AllChatsActivity extends Activity {
	private String[] navString = new String[] { "Home", "Me", "News", "Logout" };
	final String[] mFragments = {
			"com.nanostuffs.maimai.activity.HomeActivity",
			"com.nanostuffs.maimai.activity.MeActivity",
			"com.nanostuffs.maimai.activity.NewsActivity" };
	private ImageButton mNavMenu;
	private DrawerLayout drawer;
	private Typeface mActionBarTypeface;
	private String mUIDStr;
	private Handler mHandler;
	private String mCatID;
	private String mCatName;
	private AutoCompleteTextView actionBarSearch;
	private ArrayList<SearchItem> mItemList;
	private String list;
	private ProgressDialog progress;
	private SearchItem mItem;
	private ArrayList<Chat> mChatList;
	private ListView mChatListView;
	private Chat mChat;
	private AllChatsAdapter mChatAdapter;
	TextView chat_view;
	private Handler mHandler1 = new Handler();
	private AlertDialog alert;
	ArrayList<String> store_data_allchat = new ArrayList<String>();
	ArrayList<String> temp_store_data_allchat = new ArrayList<String>();
	ArrayList<String> store_data_count = new ArrayList<String>();
	ArrayList<String> temp_store_data_count = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_chats);
		
		actionBarDetails();
		try {
			initializeComponents();
		} catch (Exception e) {
		}
		
    
		getChatMessages();  
		mHandler1.postDelayed(mRunnable, 3000);

	}
	

	
	
	private void getChatMessages1() {
		try {
			if (checkInternetConnection()) {
				ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage("Please wait..");
				progress.setCanceledOnTouchOutside(false);
				
				new RetriveChatTask2(progress).execute();
			}
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
	}

	public class RetriveChatTask2 extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String ago;
		private String resultdata;

		public RetriveChatTask2(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = getChatsFromWeb1();
			}

			return resultdata;
		}

		public void onPostExecute(String result1) {
			// progress.dismiss();
			Log.e("test", "result newwwwwwwwwwww : " + result1);
			try {
				JSONObject responseObj = new JSONObject(result1);
				LoginActivity.temp.clear();
				JSONArray ja = responseObj.getJSONArray("data");
				if (!(ja.length() == 0)) {
					
					LoginActivity.chat_cnt=0;
					LoginActivity.temp.clear();
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
					
						LoginActivity.temp.add(i,jo.getString("ChatCount"));
					
					}
					
					for (int cnt = 0; cnt < LoginActivity.temp.size(); cnt++) {
						System.out.println("chat count is"+LoginActivity.temp.get(cnt));
						LoginActivity.chat_cnt = LoginActivity.chat_cnt + Integer.parseInt(LoginActivity.temp.get(cnt)); 
					}
					Collections.reverse(LoginActivity.temp);
					System.out.println("After Reverse Order, ArrayList Contains : " + LoginActivity.temp);
					
					if(LoginActivity.chat_cnt>0){
						String get_count = String.valueOf(LoginActivity.chat_cnt);
						chat_view.setVisibility(View.VISIBLE);
						chat_view.setText(String.valueOf(LoginActivity.chat_cnt));
					}
					else if(LoginActivity.chat_cnt==0)
		        		chat_view.setVisibility(View.INVISIBLE);

								
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	private String getChatsFromWeb1() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/get_from_messages.php?fromid=";
		String result1 = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("fromid", mUIDStr));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result1 = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result1;
	}
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}

	private final Runnable mRunnable = new Runnable() {

		public void run() {
			getChatMessages();
			mHandler1.postDelayed(mRunnable, 3000);
		}
	};
	private TextView mNoConv;

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

	public class RetriveChatTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String ago;
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
		@Override
		public void onPostExecute(String result) {
			// progress.dismiss();
			try {
				store_data_allchat.clear();
				store_data_count.clear();
				JSONObject responseObj = new JSONObject(result);
			

			
				JSONArray ja = responseObj.getJSONArray("data");
			
			if (!(ja.length() == 0)) {
				
			
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					store_data_count.add(i,jo.getString("ChatCount"));
					store_data_allchat.add(i,jo.getString("ChatId"));
				
				}
			}
		}
		 catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
			int flag = 0;
			
				if(temp_store_data_allchat.size()!=0){
					flag=0;
					System.out.println("temp store :- "+temp_store_data_count);
					System.out.println("store :- "+store_data_count);
					for (int i = 0; i < temp_store_data_allchat.size(); i++) {
					
						for (int j = 0; j < store_data_allchat.size(); j++) {
							if((store_data_allchat.get(j).toString()).equalsIgnoreCase((temp_store_data_allchat.get(i).toString())) &&
									(store_data_count.get(j).toString()).equalsIgnoreCase((temp_store_data_count.get(i).toString())))
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
				Boolean abc =store_data_allchat.equals(result);
				System.out.println("temp store :- "+temp_store_data_allchat);
				Log.i("value return",abc.toString());
				System.out.println("store :- "+store_data_allchat);
				temp_store_data_allchat.clear();
				temp_store_data_count.clear();
				for (int i = 0; i < store_data_allchat.size(); i++) {
					temp_store_data_allchat.add(i, store_data_allchat.get(i));
					temp_store_data_count.add(i, store_data_count.get(i));
				}
				Log.e("test", "result newwwwwwwwwwww : " + result);
				try {
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
						mChat.setToName(jo.getString("To_name"));
						mChat.setFromName(jo.getString("From_name"));
						mChat.setLastMsg(jo.getString("Message"));
						mChat.setUnreadCount(jo.getString("ChatCount"));
						mChat.setToId(jo.getString("To_id"));
						mChat.setFromId(jo.getString("From_id"));
						mChat.setFromImage(jo.getString("From_image"));
						mChat.setToImage(jo.getString("To_image"));
						mChat.setUnreadCount(jo.getString("ChatCount"));
						mChat.setTimeAgo(jo.getString("Datecreate"));
						mChat.setmLastMsg_id(jo.getString("ChatId"));
						mChatList.add(0, mChat);
					}
					
					mChatAdapter = new AllChatsAdapter(AllChatsActivity.this,
							mChatList);
					mChatListView.setAdapter(mChatAdapter);

					
				} else {
					mNoConv.setVisibility(View.VISIBLE);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
			mChatListView
			.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0,
						View arg1, int position, long arg3) {
					try {
						
					
					System.out.println("count before is := "+position );
					for(int jk=0;jk<LoginActivity.temp.size();jk++)
						System.out.println("values are := "+LoginActivity.temp.get(jk));
					LoginActivity.chat_cnt = LoginActivity.chat_cnt - Integer.parseInt(LoginActivity.temp.get(position)); 
					System.out.println("count after is := "+LoginActivity.chat_cnt );
					ago = mChatList.get(position).getTimeAgo();
					
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

					
					Intent intent = new Intent(
							AllChatsActivity.this,
							MessagingSellerDirectly.class);
					Log.e("", "gggg all chats : frrom "
							+ mChatList.get(position)
									.getFromId() + " ..to : "
							+ mChatList.get(position).getToId());
					intent.putExtra("to",
							mChatList.get(position).getToId());
					intent.putExtra("to1",
							mChatList.get(position).getFromId());
					intent.putExtra("from", mUIDStr);
					intent.putExtra("userImage",
							mChatList.get(position)
									.getFromImage());
					if (mUIDStr.equalsIgnoreCase(mChatList.get(
							position).getToId())) {
						intent.putExtra("name",
								mChatList.get(position)
										.getFromName());
					} else {
						intent.putExtra("name",
								mChatList.get(position)
										.getToName());
					}

					intent.putExtra("ago", ago);
					intent.putExtra("fromid", mChatList.get(position).getFromId());
					intent.putExtra("toid", mChatList.get(position).getToId());
					intent.putExtra("test", "0");
					startActivity(intent);
					overridePendingTransition(
							R.anim.slide_in_right,
							R.anim.slide_out_left);
				} catch (Exception e) {
					// TODO: handle exception
				}
				}
			});
			
		}
			
	}
	
	
	private String getChatsFromWeb() {
		SharedPreferences prefs = this.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/get_from_messages.php?fromid=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("fromid", mUIDStr));

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
		mChatList = new ArrayList<Chat>();
		mChatListView = (ListView) findViewById(R.id.list_chats);
		mNoConv = (TextView) findViewById(R.id.no_conv);
		mNoConv.setTypeface(mActionBarTypeface);
	}

	public void actionBarDetails() {
		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		mNavMenu = (ImageButton) addView.findViewById(R.id.menu);
		chat_view = (TextView) addView.findViewById(R.id.msg_count);
	

		//if(chat_cnt>0)
		//{
			
	//	}
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
							startActivity(new Intent(AllChatsActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 1:
							startActivity(new Intent(AllChatsActivity.this,
									MeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							break;
						case 2:
							startActivity(new Intent(AllChatsActivity.this,
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
												AllChatsActivity.this,
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
				startActivity(new Intent(AllChatsActivity.this,
						SellActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		// ImageView message = (ImageView) addView.findViewById(R.id.message);
		// message.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(actionBarSearch.getWindowToken(), 0);
		// startActivity(new Intent(AllChatsActivity.this,
		// AllChatsActivity.class)
		// .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
		// }
		// });

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
							getChatMessages();
							mHandler1.postDelayed(mRunnable, 3000);
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
		 String text = "";
			Intent intent = getIntent();
	        if(null != intent)
	        	text= intent.getStringExtra("GCM");
	        if(text == null)
	        	text = "";
	        if(text.equalsIgnoreCase("getnotification")){
	        	getChatMessages1();
	        }
	        else{
	        	if(LoginActivity.chat_cnt>0){
					chat_view.setVisibility(View.VISIBLE);
					chat_view.setText(String.valueOf(LoginActivity.chat_cnt));
				}
	        	else if(LoginActivity.chat_cnt==0)
	        		chat_view.setVisibility(View.INVISIBLE);

	        }
	    	
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
						AllChatsActivity.this);
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
									AllChatsActivity.this,
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
			ProgressDialog progress = new ProgressDialog(AllChatsActivity.this);
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
						AllChatsActivity.this);
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
				AllChatsActivity.this);
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
						startActivity(new Intent(AllChatsActivity.this,
								LoginActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						finish();
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								AllChatsActivity.this);
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
