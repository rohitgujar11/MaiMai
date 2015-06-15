package com.nanostuffs.maimai.fragment;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.ItemDetailsActivity;
import com.nanostuffs.maimai.activity.ItemsTabActivity;
import com.nanostuffs.maimai.activity.LoginActivity;
import com.nanostuffs.maimai.activity.MeActivity;
import com.nanostuffs.maimai.activity.MeItemDetailsActivity;
import com.nanostuffs.maimai.activity.MeItemsTabActivity;
import com.nanostuffs.maimai.activity.NameItemsTabActivity;
import com.nanostuffs.maimai.activity.SellActivity;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.activity.LoginActivity.ForgotPasswordTask;
import com.nanostuffs.maimai.fragment.MeHighPriceFragment.MeItemListAdapter.LikeTask;
import com.nanostuffs.maimai.fragment.MeHighPriceFragment.MeItemListAdapter.UnLikeTask;
import com.nanostuffs.maimai.fragment.MeLowPriceFragment.MeItemListAdapter;
import com.nanostuffs.maimai.model.Item;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView.IXListViewListener;

public class NameRecentFragment extends Fragment implements IXListViewListener {
	private ArrayList<Item> mItem;
	private Handler mHandler;
	private XListView mItemListView;
	private Item item;
	private Typeface mActionBarTypeface;
	private TextView mEmpty;
	private String mUIDStr;
	private MeItemListAdapter adapter;
	public ImageView itemImg;
	private String mReason;
	private Dialog mDialog111;
	private String mItemID;
	private String mUserID;
	private String mRetractReason;
	public int offset;
	private String mNameUserID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recent_tab, container, false);
		initializeComponents(rootView);
		// getItemsInRecentCategory();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			getItemsInRecentCategory();
		} catch (Exception e) {
			// TODO: handle exception
		} 
		
		
	}

	private void getItemsInRecentCategory() {
		mItem = new ArrayList<Item>();
		try {
			if (checkInternetConnection()) {
				ProgressDialog progress = new ProgressDialog(getActivity());
				progress.setMessage("Please wait..");
				// progress.setCanceledOnTouchOutside(false);
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				new RetriveItemsInRecentCategoryTask(progress)
						.executeOnExecutor(threadPoolExecutor);
			}
		} catch (Exception e) {
		}

	}

	public class RetriveItemsInRecentCategoryTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public RetriveItemsInRecentCategoryTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			offset++;
			String resultdata = getItemsInRecentCategoryFromWeb(offset);
			return resultdata;
		}

		public void onPostExecute(String result) {
			progress.dismiss();
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getItemsInRecentCategory();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {

				Log.e("test", "result meeeeeeeeeeee  : " + result);
				try {
					JSONObject responseObj = new JSONObject(result);

					JSONArray ja = responseObj.getJSONArray("data");
					if (ja.length() == 0) {
						mEmpty.setVisibility(View.VISIBLE);
					} else {
						mItem.clear();
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
						System.out.println("list is :- "+mItem);
						System.out.println("count is :-  " +mItem.size());
						if (mItem.size() != 0) {
							mItemListView.setPullLoadEnable(true);
							mItemListView
									.setXListViewListener(NameRecentFragment.this);
							Parcelable state = mItemListView
									.onSaveInstanceState();
							mItemListView.onRestoreInstanceState(state);

							try {
								adapter = new MeItemListAdapter(getActivity(),
										mItem);
								mItemListView.setAdapter(adapter);
							} catch (Exception e) {
							}

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


	private boolean checkInternetConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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
						getActivity());
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getActivity().finish();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} catch (Exception e) {
			}
			return false;
		}
	}


	private String getItemsInRecentCategoryFromWeb(int offset) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mNameUserID = sharedPreferences.getString("name_uid", "");
		Log.e("", "mUIDStr " + mUIDStr);
		String postURL = "http://54.149.99.130/ws/get_user_item.php?userid=&flag=recent&itemid=0";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mNameUserID));
			params.add(new BasicNameValuePair("flag", "recent"));
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

	private void initializeComponents(View rootView) {
		mActionBarTypeface = Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/verdana.ttf");
		mHandler = new Handler();
		mItemListView = (XListView) rootView.findViewById(R.id.item_list);
		mItemListView.setPullLoadEnable(true);
		mItemListView.setPullRefreshEnable(true);
		mEmpty = (TextView) rootView.findViewById(R.id.empty);
		mEmpty.setTypeface(mActionBarTypeface);
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
				// getItemsInRecentCategory();
				onLoad();
			}
		}, 2000);
	}

	public class MeItemListAdapter extends ArrayAdapter<Item> {
		private Context mContext;
		private ArrayList<Item> mValues;
		private Typeface mActionBarTypeface;
		private AQuery androidAQuery;
		private String mUserId;
		private String mItemId;
		private CheckBox likeCheckBox;
		private int mPosition;
		private String mUIDStr;
		private String mShareText;
		private ImageButton deletebtn;
		private ImageButton share;
		private TextView itemNameText;
		private TextView daysAgo;
		private TextView price;
		private TextView username;
		private TextView location;
		private String mItemId1;
		private Dialog dialog;
		private ProgressBar progress;
		private Typeface mActionBarTypeface1;
		private Dialog dialog2;

		public MeItemListAdapter(Context context, ArrayList<Item> list) {
			super(context, R.layout.me_item_list_item, list);
			this.mContext = context;
			this.mValues = list;
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
		public Item getItem(int position) {
			return mValues.get(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			SharedPreferences prefs = mContext.getSharedPreferences(
					SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
			mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
			// ViewHolder viewHolder = null;
			mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/verdana.ttf");
			mActionBarTypeface1 = Typeface.createFromAsset(getActivity()
					.getAssets(), "fonts/verdana_bold.ttf");

			androidAQuery = new AQuery(mContext);

			// if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.me_item_list_item, null);
			// viewHolder = new ViewHolder();
			progress = (ProgressBar) convertView.findViewById(R.id.progress);
			share = (ImageButton) convertView.findViewById(R.id.share_img);
			deletebtn = (ImageButton) convertView
					.findViewById(R.id.delete_item);
			deletebtn.setVisibility(View.GONE);
			itemImg = (ImageView) convertView.findViewById(R.id.item_img);
			itemNameText = (TextView) convertView.findViewById(R.id.item_name);
			itemNameText.setTypeface(mActionBarTypeface);
			daysAgo = (TextView) convertView.findViewById(R.id.days_ago);
			daysAgo.setTypeface(mActionBarTypeface);
			price = (TextView) convertView.findViewById(R.id.price);
			price.setTypeface(mActionBarTypeface);
			username = (TextView) convertView.findViewById(R.id.username);
			username.setTypeface(mActionBarTypeface);
			location = (TextView) convertView.findViewById(R.id.location);
			location.setTypeface(mActionBarTypeface);
			likeCheckBox = (CheckBox) convertView
					.findViewById(R.id.like_check_box);
			likeCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// int getPosition = (Integer) buttonView.getTag();
							// mValues.get(getPosition).setSelected(
							// buttonView.isChecked());
							mItemId1 = mValues.get(position).getItemId();
							Log.e("", "mItemId1 : " + mItemId1
									+ " ::: mItemName :"
									+ mValues.get(position).getItemName());
							// if (checkInternetConnection()) {
							//
							// ProgressDialog progress = new ProgressDialog(
							// mContext);
							// progress.setMessage("Please wait..");
							// progress.setCanceledOnTouchOutside(false);
							// new LikeUnlikeTask(progress).execute();
							// }
							if (isChecked) {
								Log.e("", "tttttttttttt 111");
								if (checkInternetConnection()) {
									ProgressDialog progress = new ProgressDialog(
											mContext);
									progress.setMessage("Please wait..");
									progress.setCanceledOnTouchOutside(false);
									int corePoolSize = 60;
									int maximumPoolSize = 80;
									int keepAliveTime = 10;
									BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
											maximumPoolSize);
									Executor threadPoolExecutor = new ThreadPoolExecutor(
											corePoolSize, maximumPoolSize,
											keepAliveTime, TimeUnit.SECONDS,
											workQueue);
									new LikeTask(progress)
											.executeOnExecutor(threadPoolExecutor);
								}
							} else {
								Log.e("", "tttttttttttt 222222");
								if (checkInternetConnection()) {
									ProgressDialog progress = new ProgressDialog(
											mContext);
									progress.setMessage("Please wait..");
									progress.setCanceledOnTouchOutside(false);
									int corePoolSize = 60;
									int maximumPoolSize = 80;
									int keepAliveTime = 10;
									BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
											maximumPoolSize);
									Executor threadPoolExecutor = new ThreadPoolExecutor(
											corePoolSize, maximumPoolSize,
											keepAliveTime, TimeUnit.SECONDS,
											workQueue);
									new UnLikeTask(progress)
											.executeOnExecutor(threadPoolExecutor);
								}
							}
						}
					});
			// deletebtn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// mContext);
			// builder.setMessage("Are you sure you want to delete this item ?");
			// builder.setIcon(R.drawable.alert);
			// builder.setTitle("Delete");
			// builder.setPositiveButton("Yes",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// if (checkInternetConnection()) {
			// mPosition = position;
			// mUserId = mValues.get(mPosition)
			// .getUserId();
			// mItemId = mValues.get(mPosition)
			// .getItemId();
			// Log.e("", "mItemId : "
			// + mItemId
			// + " ::: mItemName :"
			// + mValues.get(mPosition)
			// .getItemName());
			// ProgressDialog progress = new ProgressDialog(
			// mContext);
			// progress.setMessage("Please wait..");
			// progress.setCancelable(false);
			//
			// new DeleteItemTask(progress).execute();
			// }
			// }
			// }).setNegativeButton("No",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// dialog.dismiss();
			// }
			// });
			// AlertDialog alert = builder.create();
			// alert.show();
			//
			// }
			// });

			// deletebtn.setOnClickListener(new OnClickListener() {
			//
			// private Dialog dialog1;
			//
			// @Override
			// public void onClick(View v) {
			// dialog1 = new Dialog(getActivity());
			// dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			// dialog1.setContentView(getActivity().getLayoutInflater()
			// .inflate(R.layout.dialog_delete, null));
			// TextView remove_posting = (TextView) dialog1
			// .findViewById(R.id.remove_posting);
			//
			// remove_posting.setTypeface(mActionBarTypeface1);
			// Button item_sold = (Button) dialog1
			// .findViewById(R.id.item_sold);
			// item_sold.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (dialog1 != null) {
			// dialog1.dismiss();
			// }
			// mPosition = position;
			// itemSold();
			// }
			// });
			// Button delete_item = (Button) dialog1
			// .findViewById(R.id.delete_item);
			// delete_item.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (dialog1 != null) {
			// dialog1.dismiss();
			//
			// }
			// mPosition = position;
			// retractItem(mPosition);
			// }
			//
			// });
			// Button cancel = (Button) dialog1.findViewById(R.id.cancel);
			// cancel.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (dialog1 != null) {
			// dialog1.dismiss();
			//
			// }
			// }
			// });
			// item_sold.setTypeface(mActionBarTypeface);
			// delete_item.setTypeface(mActionBarTypeface);
			// cancel.setTypeface(mActionBarTypeface);
			//
			// dialog1.show();
			// }
			// });
			String path = "http://54.149.99.130/ws/sharing_page.php?itemid="
					+ mValues.get(position).getItemId();
			path = ". Check it out @ " + path;

			mShareText = mValues.get(position).getItemName() + " "
					+ mValues.get(position).getPrice() + " at "
					+ mValues.get(position).getLocation() + path;
			share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String shareBody = mShareText;
					Intent sharingIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					sharingIntent.putExtra(
							android.content.Intent.EXTRA_SUBJECT, "MaiMai");
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							shareBody);
					mContext.startActivity(Intent.createChooser(
							sharingIntent,
							mContext.getResources().getString(
									R.string.share_this)));
				}
			});

			// convertView.setTag(viewHolder);
			// convertView
			// .setTag(R.id.like_check_box, likeCheckBox);
			// } else {
			// viewHolder = (ViewHolder) convertView.getTag();
			// }
			// likeCheckBox.setTag(position); // This line is important.
			// likeCheckBox.setChecked(mValues.get(position)
			// .isSelected());

			String imagePath = mValues.get(position).getItemImage();

			if (!imagePath.contains(",")) {
				imagePath = imagePath.replace("\\/", "/").replace("[", "")
						.replace("]", "").replace("\"", "");
				Log.e("", "imagePath : " + imagePath);
				progress.setVisibility(View.VISIBLE);
				androidAQuery
						.id(itemImg)
						.progress(progress)
						.image(imagePath, true, true, 0,
								R.drawable.defaultimage2);
			} else {
				String imgArr[] = imagePath.split(",");
				imgArr[0] = imgArr[0].replace("\\/", "/").replace("[", "")
						.replace("]", "").replace("\"", "");
				Log.e("", "imgArr[0] : " + imgArr[0]);
				progress.setVisibility(View.VISIBLE);
				androidAQuery
						.id(itemImg)
						.progress(progress)
						.image(imgArr[0], true, true, 0,
								R.drawable.defaultimage2);
			}

			itemNameText.setText(mValues.get(position).getItemName());
			price.setText(mValues.get(position).getPrice());
			location.setText(mValues.get(position).getLocation());
			username.setText(mValues.get(position).getUserName());
			String ago = mValues.get(position).getDays();
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
			daysAgo.setText(ago);

			String isLike = mValues.get(position).getIsLike();
			if (isLike.contains("1")) {
				likeCheckBox.setChecked(true);
			} else {
				likeCheckBox.setChecked(false);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("", "convertViewconvertView");
					try {
						InputMethodManager imm = (InputMethodManager) getActivity()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								NameItemsTabActivity.actionBarSearch
										.getWindowToken(), 0);
						mItemID = mItem.get(position).getItemId();
						mUserID = mItem.get(position).getUserId();
						Log.e("", "Nameeeeeeee : "
								+ mItem.get(position).getItemName());
						Activity activity = (Activity) mContext;
						startActivity(new Intent(activity,
								MeItemDetailsActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
								.putExtra("itemid", mItemID)
								.putExtra("userid", mUserID));
						activity.overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
					} catch (Exception e) {
					}
				}
			});
			return convertView;
		}

		private boolean checkInternetConnection() {
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
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
				return false;
			}
		}

		public class LikeTask extends AsyncTask<String, Void, String> {
			private ProgressDialog progress;

			public LikeTask(ProgressDialog progress) {
				this.progress = progress;
			}

			public void onPreExecute() {
				// progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String resultdata = likeFromWeb();
				return resultdata;
			}

			public void onPostExecute(String result) {
				// progress.dismiss();
				Log.e("", "tttttttttttt result like : " + result);
				// viewHolder.likeCheckBox.setChecked(true);
			}
		}

		private String likeFromWeb() {
			String postURL = "http://54.149.99.130/ws/item_like.php?itemid=1&uid=1";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("itemid", mItemId1));
				params.add(new BasicNameValuePair("uid", mUserId));
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

			public UnLikeTask(ProgressDialog progress) {
				this.progress = progress;
			}

			public void onPreExecute() {
				// progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String resultdata = unlikeFromWeb();
				return resultdata;
			}

			public void onPostExecute(String result) {
				// progress.dismiss();
				Log.e("", "tttttttttttt result unlike: " + result);
				// viewHolder.likeCheckBox.setChecked(false);
			}
		}

		private String unlikeFromWeb() {
			String postURL = "http://54.149.99.130/ws/item_unlike.php?itemid=1&uid=1";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("itemid", mItemId1));
				params.add(new BasicNameValuePair("uid", mUserId));
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

		private void retractItem(int mPosition) {
			Log.e("", "retractItem");
			dialog2 = new Dialog(getActivity());
			dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			dialog2.setContentView(getActivity().getLayoutInflater().inflate(
					R.layout.retract_item_dialog, null));
			TextView remove_posting = (TextView) dialog2
					.findViewById(R.id.remove_posting);

			remove_posting.setTypeface(mActionBarTypeface1);
			final Button keep = (Button) dialog2.findViewById(R.id.keep);
			final Button no_buyer_interest = (Button) dialog2
					.findViewById(R.id.no_buyer_interest);
			final Button gave = (Button) dialog2.findViewById(R.id.gave);

			keep.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setSelected(true);
					mRetractReason = "Keeping It for Myself";
					no_buyer_interest.setSelected(false);
					gave.setSelected(false);
				}
			});

			no_buyer_interest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setSelected(true);
					mRetractReason = "No Buyer Interest";
					keep.setSelected(false);
					gave.setSelected(false);
				}
			});

			gave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setSelected(true);
					mRetractReason = "Gave It Away";
					no_buyer_interest.setSelected(false);
					keep.setSelected(false);
				}
			});
			TextView other = (TextView) dialog2.findViewById(R.id.other);
			other.setTypeface(mActionBarTypeface);
			final EditText reason = (EditText) dialog2
					.findViewById(R.id.reason);
			reason.setTypeface(mActionBarTypeface);
			reason.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					keep.setSelected(false);
					no_buyer_interest.setSelected(false);
					keep.setSelected(false);
				}

				@Override
				public void afterTextChanged(Editable s) {
					mRetractReason = reason.getText().toString();

				}
			});
			Button remove = (Button) dialog2.findViewById(R.id.remove);
			remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (dialog2 != null) {
						dialog2.dismiss();
					}
					remove();
				}

			});
			Button cancel = (Button) dialog2.findViewById(R.id.cancel);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog2 != null) {
						dialog2.dismiss();
					}
				}
			});

			dialog2.show();
		}

		private void remove() {
			if (checkInternetConnection()) {
				mUserId = mValues.get(mPosition).getUserId();
				mItemId = mValues.get(mPosition).getItemId();
				Log.e("", "mItemId : " + mItemId + " ::: mItemName :"
						+ mValues.get(mPosition).getItemName());
				ProgressDialog progress = new ProgressDialog(mContext);
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
				new RetractItemTask(progress)
						.executeOnExecutor(threadPoolExecutor);
			}

		}

		private void itemSold() {
			Log.e("", "itemSold");

			if (checkInternetConnection()) {
				mUserId = mValues.get(mPosition).getUserId();
				mItemId = mValues.get(mPosition).getItemId();
				mReason = "Item Sold";
				Log.e("", "mItemId : " + mItemId + " ::: mItemName :"
						+ mValues.get(mPosition).getItemName());
				ProgressDialog progress = new ProgressDialog(mContext);
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
				new ItemSoldTask(progress)
						.executeOnExecutor(threadPoolExecutor);
			}
		}

		private void showCustomToast(String string) {
			// LayoutInflater inflater = getActivity().getLayoutInflater();
			// View layout = inflater.inflate(
			// R.layout.toast_background,
			// (ViewGroup) getActivity().findViewById(
			// R.id.toast_layout_root));
			//
			// TextView toast_text = (TextView) layout
			// .findViewById(R.id.toast_text);
			// toast_text.setTypeface(mActionBarTypeface);
			// toast_text.setText(string);
			// Toast toast = new Toast(getActivity());
			// toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
			// 15);
			// toast.setDuration(Toast.LENGTH_LONG);
			// toast.setView(layout);
			// toast.show();
			Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
		}

		public class LikeUnlikeTask extends AsyncTask<String, Void, String> {
			private ProgressDialog progress;

			public LikeUnlikeTask(ProgressDialog progress) {
				this.progress = progress;
			}

			public void onPreExecute() {
				// progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String resultdata = likeUnlikeFromWeb();
				return resultdata;
			}

			public void onPostExecute(String result) {
				// progress.dismiss();
			}
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
				progress.dismiss();
				Log.e("", "result delete: " + result);

				try {
					JSONObject responseObj = new JSONObject(result);
					JSONArray ja = responseObj.getJSONArray("data");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						if (jo.getString("response_msg").equalsIgnoreCase(
								"Deleted successfully")) {
							getItemsInRecentCategory();
							// Toast.makeText(getActivity(),
							// "Successfully deleted", Toast.LENGTH_SHORT)
							// .show();
							showCustomToast("Successfully deleted");
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

		public class ItemSoldTask extends AsyncTask<String, Void, String> {
			private ProgressDialog progress;

			public ItemSoldTask(ProgressDialog progress) {
				this.progress = progress;
			}

			public void onPreExecute() {
				progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String resultdata = itemSoldFromWeb();
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
								"Success")) {

							getItemsInRecentCategory();
							// Toast.makeText(getActivity(),
							// "Successfully deleted", Toast.LENGTH_SHORT)
							// .show();
							showCustomToast("Successfully deleted");
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

		public class RetractItemTask extends AsyncTask<String, Void, String> {
			private ProgressDialog progress;

			public RetractItemTask(ProgressDialog progress) {
				this.progress = progress;
			}

			public void onPreExecute() {
				progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String resultdata = retractItemFromWeb();
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
								"Success")) {
							getItemsInRecentCategory();
							// Toast.makeText(getActivity(),
							// "Successfully deleted", Toast.LENGTH_SHORT)
							// .show();
							showCustomToast("Successfully deleted");
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

		private String itemSoldFromWeb() {

			String postURL = "http://54.149.99.130/ws/item_sold.php?itemid=&reason=";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("itemid", mItemId));
				params.add(new BasicNameValuePair("reason", mReason));

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

		private String retractItemFromWeb() {

			String postURL = "http://54.149.99.130/ws/item_retract.php?itemid=&reason=";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("itemid", mItemId));
				params.add(new BasicNameValuePair("reason", mRetractReason));

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

		private String likeUnlikeFromWeb() {
			String postURL = "http://54.149.99.130/ws/item_like.php?itemid=1&uid=1";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("itemid", mItemId1));
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

		private String deleteFromWeb() {

			String postURL = "http://54.149.99.130/ws/delete_item.php?uid=57&itemid=120";
			String result = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("uid", mUIDStr));
				params.add(new BasicNameValuePair("itemid", mItemId));
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

}

// static class ViewHolder {
// ImageView itemImg;
// TextView daysAgo;
// TextView price;
// TextView username;
// TextView itemNameText;
// TextView location;
// CheckBox likeCheckBox;
// ImageButton share;
//
// }

