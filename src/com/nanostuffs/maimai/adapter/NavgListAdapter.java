package com.nanostuffs.maimai.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanostuffs.maimai.R;

public class NavgListAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] mValues;
	private Typeface mTypeface;

	public NavgListAdapter(Context context, String[] navString) {
		super(context, R.layout.nav_list_item);
		this.mContext = context;
		this.mValues = navString;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.nav_list_item, parent, false);
		ImageView navImage = (ImageView) rowView.findViewById(R.id.nav_image);
		switch (position) {
		case 0:
			navImage.setBackgroundResource(R.drawable.home);
			break;
		case 1:
			navImage.setBackgroundResource(R.drawable.me);
			break;
		case 2:
			navImage.setBackgroundResource(R.drawable.news);
			break;
		case 3:
			navImage.setBackgroundResource(R.drawable.logout);
			break;

		default:
			break;
		}
		TextView navText = (TextView) rowView.findViewById(R.id.nav_text);
		navText.setText(mValues[position]);
		mTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		navText.setTypeface(mTypeface);
		return rowView;
	}

	@Override
	public int getCount() {
		return mValues.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
