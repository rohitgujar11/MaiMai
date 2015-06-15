package com.nanostuffs.maimai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nanostuffs.maimai.R;

public class SpinnerCountryAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private ArrayList<String> mItemList;
	private Typeface mTypeface;

	public SpinnerCountryAdapter(Context context, int resource,
			ArrayList<String> mBikelist) {
		super(context, resource, mBikelist);
		this.mContext = context;
		this.mItemList = mBikelist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return getCustomView(position, convertView, parent);
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	private View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.spinner11, parent, false);

		TextView carName = (TextView) row.findViewById(R.id.spinner_text);
		carName.setText(mItemList.get(position));
		mTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		carName.setTypeface(mTypeface);

		return row;
	}
}
