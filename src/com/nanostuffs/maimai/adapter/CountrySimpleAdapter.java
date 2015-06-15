package com.nanostuffs.maimai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleAdapter;

public class CountrySimpleAdapter extends SimpleAdapter implements Filterable {

	private ArrayList<HashMap<String, String>> mAllData, mDataShown;

	public CountrySimpleAdapter(Context context, List data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
		
		mDataShown = (ArrayList<HashMap<String, String>>) data;
		if (mDataShown != null) {
			mAllData = (ArrayList<HashMap<String, String>>) mDataShown.clone();
		}
	}

	@Override
	public Filter getFilter() {
		Filter nameFilter = new Filter() {

			@Override
			public String convertResultToString(Object resultValue) {
				return ((HashMap<String, String>) (resultValue))
						.get("description");
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					ArrayList<HashMap<String, String>> tmpAllData = mAllData;
					ArrayList<HashMap<String, String>> tmpDataShown = mDataShown;
					tmpDataShown.clear();
					for (int i = 0; i < tmpAllData.size(); i++) {
						Log.v("Location::MY",
								tmpAllData.get(i).get("description").toString());
						if (tmpAllData
								.get(i)
								.get("description")
								.toLowerCase()
								.startsWith(constraint.toString().toLowerCase())) {
							tmpDataShown.add(tmpAllData.get(i));
						}
					}

					FilterResults filterResults = new FilterResults();
					filterResults.values = tmpDataShown;
					filterResults.count = tmpDataShown.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
			}
		};

		return nameFilter;
	}
}
