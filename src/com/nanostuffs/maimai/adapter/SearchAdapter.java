package com.nanostuffs.maimai.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.ItemDetailsActivity;
import com.nanostuffs.maimai.activity.SearchedItemListActivity;
import com.nanostuffs.maimai.model.SearchItem;

public class SearchAdapter extends ArrayAdapter<SearchItem> {
	private Context mContext;
	private List<SearchItem> mValues;
	private Typeface mActionBarTypeface;
	private LayoutInflater mInflater;
	private String mItemName;

	public SearchAdapter(Context context, List<SearchItem> listWithoutDuplicates) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_list_item, null);
		}
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		TextView item = (TextView) convertView.findViewById(R.id.name);
		item.setTypeface(mActionBarTypeface);
		item.setText(mValues.get(position).getItemName());
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mItemName = mValues.get(position).getItemName();
				Activity activity = (Activity) mContext;
				activity.startActivity(new Intent(mContext,
						SearchedItemListActivity.class).setFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("itemname",
						mItemName));
				activity.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				return false;
			}
		});
		return convertView;
	}

}
