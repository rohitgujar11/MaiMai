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
import com.nanostuffs.maimai.model.Category;

public class SpinnerCategoryAdapter extends ArrayAdapter<Category> {
	private Context mContext;
	private ArrayList<Category> mItemList;
	private Typeface mTypeface;

	public SpinnerCategoryAdapter(Context context, int resource,
			ArrayList<Category> mList) {
		super(context, resource, mList);
		this.mContext = context;
		this.mItemList = mList;
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
		View row = inflater.inflate(R.layout.category_spinner_listitem, parent,
				false);
		mTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		TextView categoryName = (TextView) row.findViewById(R.id.spinner_text);
		categoryName.setText(mItemList.get(position).getCategoryName());
		categoryName.setTypeface(mTypeface);

		return row;
	}
}
