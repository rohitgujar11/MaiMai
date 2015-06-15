package com.nanostuffs.maimai.adapter;

import java.util.ArrayList;

import org.w3c.dom.Comment;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.model.Comments;

public class CommentsAdapter extends ArrayAdapter<Comments> {
	private Context mContext;
	private ArrayList<Comments> mValues;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;

	public CommentsAdapter(Context context, ArrayList<Comments> mComList) {
		super(context, R.layout.comments_list_item, mComList);
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
	public Comments getItem(int position) {
		return mValues.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comments_list_item, parent,
				false);
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		androidAQuery = new AQuery(mContext);
		ImageView userImage = (ImageView) rowView.findViewById(R.id.user_image);
		TextView name = (TextView) rowView.findViewById(R.id.name);
		name.setTypeface(mActionBarTypeface);
		TextView comment = (TextView) rowView.findViewById(R.id.comment);
		comment.setTypeface(mActionBarTypeface);
		TextView timeAgo = (TextView) rowView.findViewById(R.id.time_ago);
		timeAgo.setTypeface(mActionBarTypeface);
		String ago = mValues.get(position).getTimeAgo();
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
		timeAgo.setText(ago);
		comment.setText(mValues.get(position).getItemComment());
		name.setText(mValues.get(position).getUserName());
		String userImagePath = mValues.get(position).getUserPic();
		if (userImagePath != null) {
			userImagePath = userImagePath.replace("\\/", "/");
			androidAQuery.id(userImage).image(userImagePath, false, false, 0,
					R.drawable.user_photo);
		}
		return rowView;
	}

}