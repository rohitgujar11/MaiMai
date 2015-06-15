package com.nanostuffs.maimai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.model.Chat;

public class AllChatsAdapter extends ArrayAdapter<Chat> {
	private Context mContext;
	private ArrayList<Chat> mValues;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;
	private String mUIDStr;
	private String mFromId;
	private Typeface mBoldTypeface;

	public AllChatsAdapter(Context context, ArrayList<Chat> mComList) {
		super(context, R.layout.all_chat_list_item, mComList);
		this.mContext = context;
		this.mValues = mComList;
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
	public Chat getItem(int position) {
		return mValues.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.all_chat_list_item, parent,
				false);
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		mBoldTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana_bold.ttf");
		androidAQuery = new AQuery(mContext);
		ImageView to_user_image = (ImageView) rowView
				.findViewById(R.id.to_user_image);
		to_user_image.setVisibility(View.GONE);

		TextView name = (TextView) rowView.findViewById(R.id.name);
		name.setTypeface(mBoldTypeface);
		TextView last_msg = (TextView) rowView.findViewById(R.id.last_msg);
		last_msg.setTypeface(mActionBarTypeface);
		TextView unread_count = (TextView) rowView
				.findViewById(R.id.unread_count);
		unread_count.setTypeface(mActionBarTypeface);

		// String to_userImagePath = mValues.get(position).getToImage();
		// to_userImagePath = to_userImagePath.replace("\\/", "/");
		// androidAQuery.id(to_user_image).image(to_userImagePath, true, true,
		// 0,
		// R.drawable.user_photo);
		SharedPreferences prefs = mContext.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		if (mUIDStr.equalsIgnoreCase(mValues.get(position).getToId())) {
			name.setText(mValues.get(position).getFromName());
		} else {
			name.setText(mValues.get(position).getToName());
		}
		if (mValues.get(position).getLastMsg().contains("admin/uploads/audio")) {
			last_msg.setText("Audio");
		} else if (mValues.get(position).getLastMsg().contains("loc:")) {
			String[] locationStr = mValues.get(position).getLastMsg()
					.split("loc:");
			if (locationStr[0].equalsIgnoreCase("null")) {
				last_msg.setText("No address found");
			} else {
				last_msg.setText("" + locationStr[0]);
			}

		} else if (mValues.get(position).getLastMsg()
				.contains("/admin/uploads/chat_image")) {
			last_msg.setText("Image");
		} else {
			last_msg.setText(mValues.get(position).getLastMsg());
		}

		if (!mValues.get(position).getUnreadCount().equalsIgnoreCase("0")) {
			unread_count.setText(mValues.get(position).getUnreadCount());
		}

		return rowView;
	}
}