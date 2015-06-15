package com.nanostuffs.maimai.adapter;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.NameItemsTabActivity;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.model.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {
	private Context mContext;
	private ArrayList<Item> mValues;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;
	private String mUserId;
	private String mItemId1;
	private CheckBox likeCheckBox;
	private String mShareText;
	private ViewHolder viewHolder;
	private String mUIDStr;
	private String[] itemImageArr;

	public ItemListAdapter(Context context, ArrayList<Item> list) {
		super(context, R.layout.item_list_item, list);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		androidAQuery = new AQuery(mContext);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.progress = (ProgressBar) convertView
					.findViewById(R.id.progress);
			viewHolder.share = (ImageButton) convertView
					.findViewById(R.id.share_img);
			viewHolder.itemImg = (ImageView) convertView
					.findViewById(R.id.item_img);
			viewHolder.itemNameText = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.itemNameText.setTypeface(mActionBarTypeface);
			viewHolder.daysAgo = (TextView) convertView
					.findViewById(R.id.days_ago);
			viewHolder.daysAgo.setTypeface(mActionBarTypeface);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			viewHolder.price.setTypeface(mActionBarTypeface);
			viewHolder.username = (TextView) convertView
					.findViewById(R.id.username);
			viewHolder.username.setTypeface(mActionBarTypeface);
			viewHolder.location = (TextView) convertView
					.findViewById(R.id.location);
			viewHolder.location.setTypeface(mActionBarTypeface);
			viewHolder.likeCheckBox = (CheckBox) convertView
					.findViewById(R.id.like_check_box);

			convertView.setTag(viewHolder);
			convertView.setTag(R.id.like_check_box, viewHolder.likeCheckBox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.likeCheckBox.setTag(position); // This line is important.
		// viewHolder.likeCheckBox.setChecked(mValues.get(position).isSelected());

		String imagePath = mValues.get(position).getItemImage();

		if (!imagePath.contains(",")) {
			imagePath = imagePath.replace("\\/", "/").replace("[", "")
					.replace("]", "").replace("\"", "");
			Log.e("", "imagePath : " + imagePath);
			viewHolder.progress.setVisibility(View.VISIBLE);
			androidAQuery.id(viewHolder.itemImg).progress(viewHolder.progress)
					.image(imagePath, true, true, 0, R.drawable.defaultimage2);
		} else {
			itemImageArr = imagePath.split(",");
			itemImageArr[0] = itemImageArr[0].replace("\\/", "/")
					.replace("[", "").replace("]", "").replace("\"", "");
			viewHolder.progress.setVisibility(View.VISIBLE);
			androidAQuery	
					.id(viewHolder.itemImg)
					.progress(viewHolder.progress)
					.image(itemImageArr[0], true, true, 0,
							R.drawable.defaultimage2);
		}

		viewHolder.itemNameText.setText(mValues.get(position).getItemName());
		viewHolder.price.setText(mValues.get(position).getPrice());
		viewHolder.location.setText(mValues.get(position).getLocation());
		viewHolder.username.setText(mValues.get(position).getUserName());
		viewHolder.username.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				savePreferences("name_uid", mValues.get(position).getUserId());
				Intent intent = new Intent(mContext, NameItemsTabActivity.class);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
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
		
		String isLike = mValues.get(position).getIsLike();
		if (isLike.contains("1")) {
			viewHolder.likeCheckBox.setChecked(true);
		} else {
			viewHolder.likeCheckBox.setChecked(false);
		}
		viewHolder.likeCheckBox
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mUserId = mValues.get(position).getUserId();
				mItemId1 = mValues.get(position).getItemId();
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
		viewHolder.daysAgo.setText(ago);
		SharedPreferences prefs = mContext.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		
		
//		String path = "http://54.149.99.130/ws/sharing_page.php?itemid="
//				+ mValues.get(position).getItemId();
		

		System.out.println("selected text is :-  "+mShareText+"posion is :- "+position);
		viewHolder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 String path = "http://54.149.99.130/ws/sharing_page.php?userid="
							+ mValues.get(position).getUserId()
							+ "&itemid="
							+ mValues.get(position).getItemId()
							+ "&login_user_id="
							+ mUIDStr;
				path = ". Check it out @ " + path;
				mShareText = mValues.get(position).getItemName() + " "
						+ mValues.get(position).getPrice() + " at "
						+ mValues.get(position).getLocation() + path;
				System.out.println("position is "+position);
				String shareBody = mShareText;
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"MaiMai");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				mContext.startActivity(Intent.createChooser(sharingIntent,
						mContext.getResources().getString(R.string.share_this)));

			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView itemImg;
		TextView daysAgo;
		TextView price;
		TextView username;
		TextView itemNameText;
		TextView location;
		CheckBox likeCheckBox;
		ImageButton share;
		ProgressBar progress;

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
						public void onClick(DialogInterface dialog, int which) {

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
//			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = likeFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
//				progress.dismiss();
				Log.e("", "tttttttttttt result like : " + result);
			} catch (Exception e) {
			}

			// viewHolder.likeCheckBox.setChecked(true);
		}
	}

	private String likeFromWeb() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		Log.e("", "likeeeeeeeitemid : " + mItemId1 + "mUserId : " + mUserId);
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

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public class UnLikeTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public UnLikeTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
//			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String resultdata = unlikeFromWeb();
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
//				progress.dismiss();
				Log.e("", "tttttttttttt result like : " + result);
			} catch (Exception e) {
			}
		}
	}

	private String unlikeFromWeb() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		String postURL = "http://54.149.99.130/ws/item_unlike.php?itemid=1&uid=1";
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
}
