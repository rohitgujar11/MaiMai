package com.nanostuffs.maimai.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.model.Category;
import com.nanostuffs.maimai.model.Item;

public class ItemGridViewAdapter extends ArrayAdapter<Item> {
	private Context mContext;
	private ArrayList<Item> mValues;
	private Typeface mTypeface;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;

	public ItemGridViewAdapter(Context context, ArrayList<Item> list) {
		super(context, R.layout.grid_item, list);
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
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.grid_item, parent, false);
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		androidAQuery = new AQuery(mContext);

		ImageView itemImg = (ImageView) rowView.findViewById(R.id.item_img);
		TextView likeText = (TextView) rowView.findViewById(R.id.like_text);
		likeText.setTypeface(mActionBarTypeface);
		TextView shareText = (TextView) rowView.findViewById(R.id.share_text);
		shareText.setTypeface(mActionBarTypeface);

		String imagePath = mValues.get(position).getItemImage();
		imagePath = imagePath.replace("\\/", "/");
		Log.e("", "imagePath : " + imagePath);
		androidAQuery.id(itemImg).image(imagePath, true, true, 0,
				R.drawable.default_image1);
		return rowView;
	}

}
