package com.nanostuffs.maimai.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.LocationActivity;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.activity.TouchImageView;
import com.nanostuffs.maimai.model.Chat;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChatAdapter extends ArrayAdapter<Chat> {
	private Context mContext;
	private ArrayList<Chat> mValues;
	private Typeface mActionBarTypeface;
	private AQuery androidAQuery;
	private String mUIDStr;
	private String mFromId;
	public Bitmap mBitmap;
	private CheckBox from_play_pause;
	private CheckBox from_blink_mic;
	private CheckBox to_play_pause;
	private CheckBox to_blink_mic;
	private Handler mHandler = new Handler();
	private boolean playPause;
	private MediaPlayer mediaPlayer;
	private boolean intialStage = true;
	private Dialog dialog2;

	public ChatAdapter(Context context, ArrayList<Chat> mComList) {
		super(context, R.layout.chat_list_item, mComList);
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
		View rowView = inflater.inflate(R.layout.chat_list_item, parent, false);
		mActionBarTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/verdana.ttf");
		androidAQuery = new AQuery(mContext);
		ImageView speaker = (ImageView) rowView.findViewById(R.id.speaker);
		ImageView speaker_to = (ImageView) rowView
				.findViewById(R.id.speaker_to);
		final ImageView img = (ImageView) rowView.findViewById(R.id.img);
		ImageView img_to = (ImageView) rowView.findViewById(R.id.img_to);
		ImageView loc = (ImageView) rowView.findViewById(R.id.loc);
		ImageView loc_to = (ImageView) rowView.findViewById(R.id.loc_to);
		ImageView to_user_image = (ImageView) rowView
				.findViewById(R.id.to_user_image);
		ImageView from_user_image = (ImageView) rowView
				.findViewById(R.id.from_user_image);
		ProgressBar progress = (ProgressBar) rowView
				.findViewById(R.id.progress);
		ProgressBar progress_to = (ProgressBar) rowView
				.findViewById(R.id.progress_to);
		TextView to_message = (TextView) rowView.findViewById(R.id.to_message);
		to_message.setTypeface(mActionBarTypeface);
		TextView from_message = (TextView) rowView
				.findViewById(R.id.from_message);
		from_message.setTypeface(mActionBarTypeface);
		from_play_pause = (CheckBox) rowView.findViewById(R.id.from_play_pause);
		from_blink_mic = (CheckBox) rowView.findViewById(R.id.from_blink_mic);
		to_play_pause = (CheckBox) rowView.findViewById(R.id.to_play_pause);
		to_blink_mic = (CheckBox) rowView.findViewById(R.id.to_blink_mic);

		String from_userImagePath = mValues.get(position).getFromImage();
		from_userImagePath = from_userImagePath.replace("\\/", "/");
		Log.e("", "from_userImagePath : " + from_userImagePath);

		String to_userImagePath = mValues.get(position).getToImage();
		to_userImagePath = to_userImagePath.replace("\\/", "/");
		final String toUserName = mValues.get(position).getToName();
		// if (mBitmap != null) {
		// to_user_image.setImageBitmap(mBitmap);
		// } else {
		// new DownloadImageTask(to_user_image).execute(to_userImagePath);
		// }
		Log.e("", "getChatsFromWeb : f " + mValues.get(position).getFromId()
				+ " t " + mValues.get(position).getToId());
		SharedPreferences prefs = mContext.getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		Log.e("", "mUIDStr " + mUIDStr);
		mFromId = mValues.get(position).getFromId();
		if (mValues.get(position).getMessage().contains(".jpg")) {
			Log.e("", "Message eeeeeeeeee: "
					+ mValues.get(position).getMessage());
		}

		from_play_pause.setVisibility(View.GONE);
		from_blink_mic.setVisibility(View.GONE);
		from_user_image.setVisibility(View.GONE);

		to_blink_mic.setVisibility(View.GONE);
		to_play_pause.setVisibility(View.GONE);
		to_user_image.setVisibility(View.GONE);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {

			if (mValues.get(position).getMessage()
					.contains("/admin/uploads/audio/")) {
				Log.e("", "audioooooooo :" + mValues.get(position).getMessage());
				// Audio Message
				String s = mValues.get(position).getMessage()
						.replace("/admin/uploads/audio/", "-");
				String str[] = s.split("-");
				Log.e("", "audioooooooo : " + str[1]);
				final String audio_path = "http://2mai.mobi/maimai/admin/uploads/audio/"
						+ str[1];
				if (!mUIDStr.trim().equals(mFromId.trim())) {
					Log.e("",
							"audioooooooo to : mUIDStr.trim() :"
									+ mUIDStr.trim() + " mFromId.trim() :"
									+ mFromId.trim());
					// To
					androidAQuery.id(to_user_image).image(to_userImagePath,
							false, false, 0, R.drawable.user_photo);
					from_message.setVisibility(View.GONE);
					to_message.setVisibility(View.VISIBLE);
					from_user_image.setVisibility(View.GONE);
					to_user_image.setVisibility(View.VISIBLE);
					speaker_to.setVisibility(View.VISIBLE);
					to_message.setText(mValues.get(position).getRecTime());
					to_message.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (intialStage) {
								Log.e("", "aaaaudddio :" + audio_path.trim());
								new Player().execute(audio_path.trim());
							} else {
								if (!mediaPlayer.isPlaying())
									mediaPlayer.start();
							}
							playPause = true;
						}
					});
					speaker_to.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (intialStage) {
								Log.e("", "aaaaudddio :" + audio_path.trim());
								new Player().execute(audio_path.trim());
							} else {
								if (!mediaPlayer.isPlaying())
									mediaPlayer.start();
							}
							playPause = true;
						}
					});

				} else {
					// From
					androidAQuery.id(from_user_image).image(from_userImagePath,
							false, false, 0, R.drawable.user_photo);
					Log.e("",
							"audioooooooo from : mUIDStr.trim() :"
									+ mUIDStr.trim() + " mFromId.trim() :"
									+ mFromId.trim());
					from_message.setVisibility(View.VISIBLE);
					to_message.setVisibility(View.GONE);
					to_user_image.setVisibility(View.GONE);
					from_user_image.setVisibility(View.VISIBLE);
					speaker.setVisibility(View.VISIBLE);
					from_message.setText(mValues.get(position).getRecTime());
					from_message.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (intialStage) {
								Log.e("", "aaaaudddio :" + audio_path.trim());
								new Player().execute(audio_path.trim());
							} else {
								if (!mediaPlayer.isPlaying())
									mediaPlayer.start();
							}
							playPause = true;
						}
					});
					speaker.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (intialStage) {
								Log.e("", "aaaaudddio :" + audio_path.trim());
								new Player().execute(audio_path.trim());
							} else {
								if (!mediaPlayer.isPlaying())
									mediaPlayer.start();
							}
							playPause = true;
						}
					});

				}
			} else if (mValues.get(position).getMessage()
					.contains("/admin/uploads/chat_image")) {
				String str[] = mValues.get(position).getMessage().split("-");
				Log.e("", "chatttttttttttt : " + str[1]);
				final String image_path = "http://2mai.mobi/maimai/admin/uploads/chat_image/chat-"
						+ str[1];

				final String thumb_image_path = "http://2mai.mobi/maimai/admin/uploads/chat_image_thumb/chat-thumb-"
						+ str[1];

				Log.e("", "image_path : " + image_path);
				Log.e("", "thumb_image_path : " + thumb_image_path);

				if (!mUIDStr.equals(mFromId)) {
					// To
					androidAQuery.id(to_user_image).image(to_userImagePath,
							false, false, 0, R.drawable.user_photo);
					from_message.setVisibility(View.GONE);
					to_message.setVisibility(View.GONE);
					from_user_image.setVisibility(View.GONE);
					to_user_image.setVisibility(View.VISIBLE);
					img_to.setVisibility(View.VISIBLE);
					// androidAQuery
					// .id(img)
					// .image(image_path, true, true, 0,
					// R.drawable.default_image1)
					// .progress(progress_to);
					androidAQuery
							.id(img_to)
							.image(thumb_image_path, false, false, 0,
									R.drawable.default_image1)
							.progress(progress_to);
					img_to.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							viewImage(image_path);
						}
					});
				} else {
					// From
					androidAQuery
							.id(from_user_image)
							.image(from_userImagePath, false, false, 0,
									R.drawable.user_photo).progress(progress);

					from_message.setVisibility(View.GONE);
					to_message.setVisibility(View.GONE);
					to_user_image.setVisibility(View.GONE);
					from_user_image.setVisibility(View.VISIBLE);
					img.setVisibility(View.VISIBLE);
					// androidAQuery.id(img).image(image_path, true, true, 0,
					// R.drawable.default_image1);
					androidAQuery.id(img).image(thumb_image_path, false, false,
							0, R.drawable.default_image1);
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							viewImage(image_path);
						}
					});
				}
			} else if (mValues.get(position).getMessage().contains("loc:")) {

				// Location
				String[] locationStr = mValues.get(position).getMessage()
						.split("loc:");

				String locArr[] = locationStr[1].split(",");
				final String latitude = locArr[0].trim();
				final String longitude = locArr[1].trim();

				if (!mUIDStr.equals(mFromId)) {
					// To
					androidAQuery.id(to_user_image).image(to_userImagePath,
							false, false, 0, R.drawable.user_photo);
					from_message.setVisibility(View.GONE);
					to_message.setVisibility(View.VISIBLE);
					if(locationStr[0].equalsIgnoreCase("null")){
						to_message.setText("No address found");
					}else{
						to_message.setText(locationStr[0]);
					}
					
					from_user_image.setVisibility(View.GONE);
					to_user_image.setVisibility(View.VISIBLE);
					loc_to.setVisibility(View.VISIBLE);
					to_message.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// show location on map
							Intent intent = new Intent(mContext,
									LocationActivity.class);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							intent.putExtra("name", toUserName);
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
					});
					loc_to.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// show location on map
							Intent intent = new Intent(mContext,
									LocationActivity.class);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							intent.putExtra("name", toUserName);
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
					});

				} else {
					// From
					androidAQuery.id(from_user_image).image(from_userImagePath,
							false, false, 0, R.drawable.user_photo);

					from_message.setVisibility(View.VISIBLE);
					if (locationStr[0].equalsIgnoreCase("null")) {
						from_message.setText("No address found");
					} else {
						from_message.setText(locationStr[0]);
					}
					to_message.setVisibility(View.GONE);
					to_user_image.setVisibility(View.GONE);
					from_user_image.setVisibility(View.VISIBLE);
					loc.setVisibility(View.VISIBLE);
					from_message.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// show location on map
							Intent intent = new Intent(mContext,
									LocationActivity.class);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							intent.putExtra("name", toUserName);
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
					});
					loc.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// show location on map
							Intent intent = new Intent(mContext,
									LocationActivity.class);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							intent.putExtra("name", toUserName);
							mContext.startActivity(intent);
							((Activity) mContext).overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
					});

				}
			} else {
				// Text Message
				from_play_pause.setVisibility(View.GONE);
				from_blink_mic.setVisibility(View.GONE);
				to_play_pause.setVisibility(View.GONE);
				to_play_pause.setVisibility(View.GONE);

				Log.e("",
						"hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
				if (!mUIDStr.equals(mFromId)) {
					androidAQuery.id(to_user_image).image(to_userImagePath,
							false, false, 0, R.drawable.user_photo);

					to_message.setText(mValues.get(position).getMessage());
					from_message.setVisibility(View.GONE);
					to_message.setVisibility(View.VISIBLE);
					from_user_image.setVisibility(View.GONE);
					to_user_image.setVisibility(View.VISIBLE);
				} else {
					androidAQuery.id(from_user_image).image(from_userImagePath,
							false, false, 0, R.drawable.user_photo);
					from_message.setVisibility(View.VISIBLE);
					to_message.setVisibility(View.GONE);
					from_message.setText(mValues.get(position).getMessage());
					to_user_image.setVisibility(View.GONE);
					from_user_image.setVisibility(View.VISIBLE);
				}
			}

		} catch (Exception e) {
		}
		return rowView;
	}

	protected void viewImage(String path) {
		dialog2 = new Dialog(mContext);
		dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(((Activity) mContext).getLayoutInflater()
				.inflate(R.layout.dialog_chat_image, null));
		final TouchImageView sell_image = (TouchImageView) dialog2
				.findViewById(R.id.sell_image_final);
		final TouchImageView sell_image1 = (TouchImageView) dialog2
				.findViewById(R.id.temp_image);
		sell_image1.setVisibility(View.VISIBLE);
		final ProgressBar pB = (ProgressBar) dialog2.findViewById(R.id.progress);
		pB.setVisibility(View.VISIBLE);
		ImageLoader imageLoader;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		
	FailReason failReason;
	imageLoader.displayImage(path, sell_image, null, new ImageLoadingListener() {
	    @Override
	    public void onLoadingStarted(String imageUri, View view) {
	    	
	    }
	    @Override
	    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
	    	
	    	pB.setVisibility(View.INVISIBLE);
	    	sell_image1.setVisibility(View.INVISIBLE);
	    }
	    @Override
	    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	    	pB.setVisibility(View.INVISIBLE);
	    	sell_image1.setVisibility(View.INVISIBLE);
	    	
	    }
	    @Override
	    public void onLoadingCancelled(String imageUri, View view) {
	    	
	    }
	}, new ImageLoadingProgressListener() {
	    @Override
	    public void onProgressUpdate(String imageUri, View view, int current, int total) {
	    	
	    }
	});
		//androidAQuery.id(sell_image).progress(pB)
		//		.image(path, true, true, 0, R.drawable.default_image1);
		dialog2.show();
	}

	public Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	class Player extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog progress;

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean prepared;
			Log.e("", "111111111111111111111111111111");
			Log.e("", "111111111111111111111111111111 params[0] :" + params[0]);
			try {

				mediaPlayer.setDataSource(params[0]);

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						intialStage = true;
						playPause = false;
						to_play_pause.setChecked(false);
						mediaPlayer.stop();
						mediaPlayer.reset();
					}
				});
				mediaPlayer.prepare();
				prepared = true;
			} catch (IllegalArgumentException e) {
				Log.d("IllegarArgument", e.getMessage());
				prepared = false;
				e.printStackTrace();
			} catch (SecurityException e) {
				prepared = false;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				prepared = false;
				e.printStackTrace();
			} catch (IOException e) {
				prepared = false;
				e.printStackTrace();
			}
			return prepared;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (progress.isShowing()) {
				progress.cancel();
			}
			Log.e("aaaaPrepared", "aaaaPrepared//" + result);
			mediaPlayer.start();
			intialStage = false;
		}

		public Player() {
			progress = new ProgressDialog(mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progress.setMessage("Buffering...");
			this.progress.show();

		}
	}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		// final Rect rect = new Rect(0, 0, bitmap.getWidth(),
		// bitmap.getHeight());
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		final RectF rectF = new RectF(0, 0, bitmap.getWidth(),
				bitmap.getHeight());
		// canvas.drawRoundRect(rectF, 20, 20, paint);

		canvas.drawRoundRect(rectF, 5, 5, paint);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		// bitmap.getWidth() / 2, paint);
		// canvas.drawRect(rect, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 120, 120, false);
		Bitmap _bmp = Bitmap.createScaledBitmap(output, 300, 300, false);
		return _bmp;
		// return output;
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
			Log.e("", "onPostExecute");
			mBitmap = result;
			bmImage.setImageBitmap(result);
		}
	}
}