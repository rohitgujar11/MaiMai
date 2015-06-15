package com.nanostuffs.maimai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.model.Category;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	AQuery androidAQuery;
	private LayoutInflater mInflater;
	// private static final int[] ids = { R.drawable.thumb, R.drawable.thumb,
	// R.drawable.thumb, R.drawable.thumb, R.drawable.thumb,
	// R.drawable.thumb, R.drawable.thumb };
	private static ArrayList<Category> arr;

	public ImageAdapter(Context context, ArrayList<Category> mCategory) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// mInflater = LayoutInflater.from(context);
		mContext = context;
		// arr.addAll(PictureGalleryFrag.slidemap.get(viewflowgal.cliclid));
		androidAQuery = new AQuery(mContext);
		arr = mCategory;
	}

	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_item, null);
		}
		AQuery androidAQuery = new AQuery(mContext);

		// androidAQuery.id(((ImageView)
		// convertView.findViewById(R.id.imgView)))
		// .image(arr.get(position), true, true, 320, R.drawable.ic_launcher);
		String image = arr.get(position).getCategoryImage();
		image = image.replace("\\/", "/").replace("[", "").replace("]", "").replace("'", "");
		String[] arr = image.split(",");
		Log.e("", "arr ...............: " + arr[0]);
		
		androidAQuery.id((ImageView) convertView.findViewById(R.id.imgView))
				.image(arr[0], true, true, 320, R.drawable.category);
		return convertView;

	}

}
