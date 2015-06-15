package com.nanostuffs.maimai.adapter;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.HomeActivity;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.model.Category;

public class CategoryListAdapter extends ArrayAdapter<Category> {
	private Context mContext;
	private ArrayList<Category> mValues;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;

	public CategoryListAdapter(Context context, ArrayList<Category> list) {
		super(context, R.layout.home_category_list_item, list);
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
	public Category getItem(int position) {
		return mValues.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		androidAQuery = new AQuery(mContext);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.home_category_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.progress = (ProgressBar) convertView
					.findViewById(R.id.progress);
			viewHolder.categoryImage = (ImageView) convertView
					.findViewById(R.id.category_img);
			viewHolder.categoryName = (TextView) convertView
					.findViewById(R.id.category_name);
			viewHolder.categoryName.setTypeface(mActionBarTypeface);
			viewHolder.mViewedCount = (TextView) convertView
					.findViewById(R.id.viewed_count);
			viewHolder.mViewedCount.setTypeface(mActionBarTypeface);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.categoryName
				.setText(mValues.get(position).getCategoryName());
		if (mValues.get(position).getCategoryName()
				.equalsIgnoreCase("Following")) {
			viewHolder.mViewedCount.setText("+"
					+ mValues.get(position).getFollowigViewedCount()
					+ " NEW TODAY");
		} else if (mValues.get(position).getCategoryName()
				.equalsIgnoreCase("Near Me")) {
			if(SplashActivity.view_count_array.size()==0){
				viewHolder.mViewedCount.setText("+"
						+ mValues.get(position).getNearMeViewedCount()
						+ " NEW TODAY");
			}
			else{try {
				viewHolder.mViewedCount.setText("+"
						+ SplashActivity.view_count_array.get(SplashActivity.view_count)
						+ " NEW TODAY");
			} catch (Exception e) {
				// TODO: handle exception
				viewHolder.mViewedCount.setText("+"
						+ mValues.get(position).getNearMeViewedCount()
						+ " NEW TODAY");
			}
			
			}
			
			
		} else if (mValues.get(position).getCategoryName()
				.equalsIgnoreCase("Popular")) {
			viewHolder.mViewedCount.setText("+"
					+ mValues.get(position).getViewedCount() + " NEW TODAY");
		} else {
			viewHolder.mViewedCount.setText("+"
					+ mValues.get(position).getViewedCount() + " NEW TODAY");
		}

		String imagePath = mValues.get(position).getCategoryImage();
		imagePath = imagePath.replace("\\/", "/");
		// Log.e("", "viewHolder.categoryImage : " +
		// viewHolder.categoryImage.get);
		viewHolder.progress.setVisibility(View.VISIBLE);
		androidAQuery.id(viewHolder.categoryImage)
				.progress(viewHolder.progress)
				.image(imagePath, true, true, 0, R.drawable.default_image1);

		// viewHolder.progress.setVisibility(View.GONE);

		// new DownloadImageTask(viewHolder.categoryImage).execute(imagePath);

		// new ImageDownloaderTask(viewHolder.categoryImage).execute(imagePath);

		return convertView;
	}

	static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from "
					+ url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference imageViewReference;

		public ImageDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference(imageView);
		}

		@Override
		// Actual download method, run in the task thread
		protected Bitmap doInBackground(String... params) {
			// params comes from the execute() call: params[0] is the url.
			return downloadBitmap(params[0]);
		}

		@Override
		// Once the image is downloaded, associates it to the imageView
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = (ImageView) imageViewReference.get();
				if (imageView != null) {

					if (bitmap != null) {
						imageView.setImageBitmap(bitmap);
					} else {
						imageView.setImageDrawable(imageView.getContext()
								.getResources()
								.getDrawable(R.drawable.default_image1));
					}
				}

			}
		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	static class ViewHolder {
		ImageView categoryImage;
		TextView categoryName;
		TextView mViewedCount;
		ProgressBar progress;
	}

}
