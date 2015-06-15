package com.nanostuffs.maimai.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nanostuffs.maimai.GPSTracker;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.activity.HomeActivity;
import com.nanostuffs.maimai.activity.ItemDetailsActivity;
import com.nanostuffs.maimai.activity.ItemsTabActivity;
import com.nanostuffs.maimai.activity.SplashActivity;
import com.nanostuffs.maimai.adapter.ItemListAdapter;
import com.nanostuffs.maimai.model.Item;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView;
import com.nanostuffs.maimai.pulltorefereshlistview.XListView.IXListViewListener;

public class HighPriceFragment extends Fragment implements IXListViewListener {
	private String mCatName;
	private ArrayList<Item> mItem;
	private Handler mHandler;
	private XListView mItemListView;
	private Item item;
	private Typeface mActionBarTypeface;
	private TextView mEmpty;
	private String mUIDStr;
	private String mItemID;
	private String mUserID;
	public int offset;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recent_tab, container, false);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mCatName = sharedPreferences.getString("mCatName", "");
		initializeComponents(rootView);
		// getItemsInRecentCategory();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			getItemsInRecentCategory();

		} catch (Exception e) {
		}
	}

	private void getItemsInRecentCategory() {
		mItem = new ArrayList<Item>();
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(getActivity());
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new RetriveItemsInRecentCategoryTask(progress)
					.executeOnExecutor(threadPoolExecutor);
			// if (mCatName.equalsIgnoreCase("Following")) {
			// new RetriveItemsInRecentCategoryTaskLike(progress)
			// .executeOnExecutor(threadPoolExecutor);
			// }
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}

	public class RetriveItemsInRecentCategoryTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public RetriveItemsInRecentCategoryTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// offset++;
			if (isOnline()) {
				resultdata = getItemsInRecentCategoryFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
				progress = null;

				if (result.length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("Connection Timeout ! Please try again.");
					builder.setTitle("Warning !");
					builder.setIcon(R.drawable.alert);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getItemsInRecentCategory();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();
				} else {
					// if (mCatName.equalsIgnoreCase("Following")) {
					// item = new Item();
					//
					// try {
					// Log.e("test", "resulttttttttttt  : " + result);
					//
					// JSONObject responseObj = new JSONObject(result);
					//
					// JSONArray ja = responseObj.getJSONArray("data");
					// if (ja.length() == 0) {
					// mEmpty.setVisibility(View.VISIBLE);
					// } else {
					// mEmpty.setVisibility(View.GONE);
					// for (int i = 0; i < ja.length(); i++) {
					// JSONObject jo = (JSONObject) ja.get(i);
					//
					// item.setItemId(jo.getString("Id"));
					// item.setItemImage(jo.getString("Image"));
					// item.setItemName(jo.getString("Name"));
					// item.setUserId(jo.getString("UserId"));
					// item.setUserName(jo.getString("Username"));
					// // item.setDescription(jo.getString("Description"));
					// item.setPrice(jo.getString("Price"));
					// item.setLocation(jo.getString("Locations"));
					// item.setDays(jo.getString("NoOfDays"));
					// item.setIsLike(jo.getString("user_likes"));
					// // if
					// // (mCatName.equalsIgnoreCase("Following"))
					// // {
					// // if (!mItem.contains(item)) {
					// // mItem.add(item);
					// // }
					// // } else {
					// // Log.e("", "")
					// // mItem.add(item);
					// // }
					// mItem.add(item);
					// }
					// if (mItem.size() != 0) {
					//
					// mItemListView.setPullLoadEnable(true);
					// mItemListView
					// .setXListViewListener(HighPriceFragment.this);
					// Parcelable state = mItemListView
					// .onSaveInstanceState();
					// mItemListView.onRestoreInstanceState(state);
					// final ItemListAdapter adapter = new ItemListAdapter(
					// getActivity(), mItem);
					// mItemListView.setAdapter(adapter);
					// // if (mCatName.equalsIgnoreCase("Following")) {
					// // try {
					// // Log.e("", "FollowingFollowing");
					// // @SuppressWarnings("unchecked")
					// // ArrayList<Item> newList = new ArrayList(
					// // new HashSet(mItem));
					// // ArrayList<Item> list = new ArrayList<Item>(
					// // newList);
					// // final ItemListAdapter adapter = new ItemListAdapter(
					// // getActivity(), list);
					// // mItemListView.setAdapter(adapter);
					// // } catch (Exception e) {
					// // }
					// // } else {
					// // try {
					// // Log.e("", "FollowingFollowing 2222");
					// // final ItemListAdapter adapter = new ItemListAdapter(
					// // getActivity(), mItem);
					// // mItemListView.setAdapter(adapter);
					// // } catch (Exception e) {
					// // }
					// // }
					//
					// }
					//
					// }
					// // mCategoryListView
					// // .setOnItemClickListener(new OnItemClickListener()
					// // {
					// //
					// // @Override
					// // public void onItemClick(AdapterView<?> arg0,
					// // View arg1, int position, long arg3) {
					// // mCatName = mCategory.get(position - 1)
					// // .getCategoryName();
					// // savePreferences("mCatName", mCatName);
					// // Intent intent = new Intent(HomeActivity.this,
					// // ItemsTabActivity.class);
					// // // intent.putExtra("mCatName", mCatName);
					// // startActivity(intent);
					// // overridePendingTransition(
					// // R.anim.slide_in_right,
					// // R.anim.slide_out_left);
					// //
					// // }
					// // });
					// try {
					//
					// mItemListView
					// .setOnItemClickListener(new OnItemClickListener() {
					//
					// @Override
					// public void onItemClick(
					// AdapterView<?> arg0,
					// View arg1, int position,
					// long arg3) {
					// try {
					//
					// InputMethodManager imm = (InputMethodManager)
					// getActivity()
					// .getSystemService(
					// Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(
					// ItemsTabActivity.actionBarSearch
					// .getWindowToken(),
					// 0);
					// Log.e("",
					// "onItemClick......");
					// mItemID = mItem.get(
					// position - 1)
					// .getItemId();
					// mUserID = mItem.get(
					// position - 1)
					// .getUserId();
					// Log.e("",
					// "Nameeeeeeee : "
					// + mItem.get(
					// position - 1)
					// .getItemName());
					// Log.e("", "mUserID : "
					// + mUserID);
					// incrementViewedCount();
					// startActivity(new Intent(
					// getActivity(),
					// ItemDetailsActivity.class)
					// .setFlags(
					// Intent.FLAG_ACTIVITY_CLEAR_TOP)
					// .putExtra("itemid",
					// mItemID)
					// .putExtra("userid",
					// mUserID));
					// getActivity()
					// .overridePendingTransition(
					// R.anim.slide_in_right,
					// R.anim.slide_out_left);
					// } catch (Exception e) {
					// }
					// }
					//
					// });
					// } catch (Exception e) {
					// }
					// } catch (JSONException e) {
					// e.printStackTrace();
					// }
					// } else {

					try {
						Log.e("test", "resulttttttttttt  : " + result);

						JSONObject responseObj = new JSONObject(result);
						try {
							
							
							if(mCatName.equalsIgnoreCase("Near Me")){
								System.out.println(""+responseObj.getString("Items_count"));
								SplashActivity.view_count_array.add(1, responseObj.getString("Items_count"));
							}
							} catch (Exception e) {
								// TODO: handle exception
							}
						JSONArray ja = responseObj.getJSONArray("data");
						if (ja.length() == 0) {
							mEmpty.setVisibility(View.VISIBLE);
						} else {
							mEmpty.setVisibility(View.GONE);
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo = (JSONObject) ja.get(i);
								item = new Item();

								item.setItemId(jo.getString("Id"));
								item.setItemImage(jo.getString("Image"));
								item.setItemName(jo.getString("Name"));
								item.setUserId(jo.getString("UserId"));
								item.setUserName(jo.getString("Username"));
								// item.setDescription(jo.getString("Description"));
								item.setPrice(jo.getString("Price"));
								item.setLocation(jo.getString("Locations"));
								item.setDays(jo.getString("NoOfDays"));
								item.setIsLike(jo.getString("user_likes"));
							
								// if
								// (mCatName.equalsIgnoreCase("Following"))
								// {
								// if (!mItem.contains(item)) {
								// mItem.add(item);
								// }
								// } else {
								// Log.e("", "")
								// mItem.add(item);
								// }
								if (!mItem.contains(item)) {
									mItem.add(item);
								} else {
									mItem.add(item);
								}
							}
							if (mItem.size() != 0) {

								mItemListView.setPullLoadEnable(true);
								mItemListView
										.setXListViewListener(HighPriceFragment.this);
								Parcelable state = mItemListView
										.onSaveInstanceState();
								mItemListView.onRestoreInstanceState(state);
								if (mCatName.equalsIgnoreCase("Following")) {
									try {
										Log.e("", "FollowingFollowing");
										// HashSet hs = new HashSet();
										// hs.addAll(mItem);
										// mItem.clear();
										// mItem.addAll(hs);

										// ArrayList<Item> newList = new
										// ArrayList(
										// new HashSet(mItem));
										// ArrayList<Item> list = new
										// ArrayList<Item>(
										// newList);
										// final ItemListAdapter adapter = new
										// ItemListAdapter(
										// getActivity(), list);
										final ItemListAdapter adapter = new ItemListAdapter(
												getActivity(), mItem);
										mItemListView.setAdapter(adapter);
									} catch (Exception e) {
									}
								} else {
									try {
										Log.e("", "FollowingFollowing 2222");
										final ItemListAdapter adapter = new ItemListAdapter(
												getActivity(), mItem);
										mItemListView.setAdapter(adapter);
									} catch (Exception e) {
									}
								}

							}

						}
						// mCategoryListView
						// .setOnItemClickListener(new OnItemClickListener()
						// {
						//
						// @Override
						// public void onItemClick(AdapterView<?> arg0,
						// View arg1, int position, long arg3) {
						// mCatName = mCategory.get(position - 1)
						// .getCategoryName();
						// savePreferences("mCatName", mCatName);
						// Intent intent = new Intent(HomeActivity.this,
						// ItemsTabActivity.class);
						// // intent.putExtra("mCatName", mCatName);
						// startActivity(intent);
						// overridePendingTransition(
						// R.anim.slide_in_right,
						// R.anim.slide_out_left);
						//
						// }
						// });
						try {

							mItemListView
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int position, long arg3) {
											InputMethodManager imm = (InputMethodManager) getActivity()
													.getSystemService(
															Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													ItemsTabActivity.actionBarSearch
															.getWindowToken(),
													0);
											Log.e("", "onItemClick......");
											mItemID = mItem.get(position - 1)
													.getItemId();
											mUserID = mItem.get(position - 1)
													.getUserId();
											Log.e("", "Nameeeeeeee : "
													+ mItem.get(position - 1)
															.getItemName());
											Log.e("", "mUserID : " + mUserID);
											incrementViewedCount();
											startActivity(new Intent(
													getActivity(),
													ItemDetailsActivity.class)
													.setFlags(
															Intent.FLAG_ACTIVITY_CLEAR_TOP)
													.putExtra("itemid", mItemID)
													.putExtra("userid", mUserID));
											getActivity()
													.overridePendingTransition(
															R.anim.slide_in_right,
															R.anim.slide_out_left);
										}

									});
						} catch (Exception e) {
						}
					} catch (JSONException e) {
						e.printStackTrace();
						// }
					}catch (Exception e) {
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkInternetConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
			return haveConnectedWifi || haveConnectedMobile;
		} else {
			try {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getActivity().finish();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} catch (Exception e) {
			}
			return false;
		}
	}

	public class RetriveItemsInRecentCategoryTaskLike extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public RetriveItemsInRecentCategoryTaskLike(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// offset++;
			if (isOnline()) {
				resultdata = getLikeFollowingFromWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			// try {
			// progress.dismiss();
			// progress = null;
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			Log.e("test", "resulttttttttttt  : ");
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getItemsInRecentCategory();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {

				try {
					JSONObject responseObj = new JSONObject(result);

					JSONArray ja = responseObj.getJSONArray("data");
					if (ja.length() == 0) {
						mEmpty.setVisibility(View.VISIBLE);
					} else {
						mEmpty.setVisibility(View.GONE);
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = (JSONObject) ja.get(i);
							item = new Item();
							item.setItemId(jo.getString("Id"));
							item.setItemImage(jo.getString("Image"));
							item.setItemName(jo.getString("Name"));
							item.setUserId(jo.getString("UserId"));
							item.setUserName(jo.getString("Username"));
							// item.setDescription(jo.getString("Description"));
							item.setPrice(jo.getString("Price"));
							item.setLocation(jo.getString("Locations"));
							item.setDays(jo.getString("NoOfDays"));
							item.setIsLike(jo.getString("user_likes"));

							mItem.add(item);

						}
						if (mItem.size() != 0) {

							mItemListView.setPullLoadEnable(true);
							mItemListView
									.setXListViewListener(HighPriceFragment.this);
							Parcelable state = mItemListView
									.onSaveInstanceState();
							mItemListView.onRestoreInstanceState(state);

							try {
								final ItemListAdapter adapter = new ItemListAdapter(
										getActivity(), mItem);
								mItemListView.setAdapter(adapter);
							} catch (Exception e) {
							}

						}

					}
					// mCategoryListView
					// .setOnItemClickListener(new OnItemClickListener() {
					//
					// @Override
					// public void onItemClick(AdapterView<?> arg0,
					// View arg1, int position, long arg3) {
					// mCatName = mCategory.get(position - 1)
					// .getCategoryName();
					// savePreferences("mCatName", mCatName);
					// Intent intent = new Intent(HomeActivity.this,
					// ItemsTabActivity.class);
					// // intent.putExtra("mCatName", mCatName);
					// startActivity(intent);
					// overridePendingTransition(
					// R.anim.slide_in_right,
					// R.anim.slide_out_left);
					//
					// }
					// });
					try {

						mItemListView
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int position, long arg3) {
										InputMethodManager imm = (InputMethodManager) getActivity()
												.getSystemService(
														Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(
												ItemsTabActivity.actionBarSearch
														.getWindowToken(), 0);
										Log.e("", "onItemClick......");
										mItemID = mItem.get(position - 1)
												.getItemId();
										mUserID = mItem.get(position - 1)
												.getUserId();
										Log.e("",
												"Nameeeeeeee : "
														+ mItem.get(
																position - 1)
																.getItemName());
										Log.e("", "mUserID : " + mUserID);
										incrementViewedCount();
										startActivity(new Intent(getActivity(),
												ItemDetailsActivity.class)
												.setFlags(
														Intent.FLAG_ACTIVITY_CLEAR_TOP)
												.putExtra("itemid", mItemID)
												.putExtra("userid", mUserID));
										getActivity()
												.overridePendingTransition(
														R.anim.slide_in_right,
														R.anim.slide_out_left);
									}

								});
					} catch (Exception e) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private String getItemsInRecentCategoryFromWeb() {
		SharedPreferences prefs = getActivity().getSharedPreferences(
				SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
		mUIDStr = prefs.getString(SplashActivity.PREFS_UID, "");
		if (mCatName.equalsIgnoreCase("Following")) {
			String postURL = "http://54.149.99.130/ws/get_folowing_items.php?userid=&flag=highprice";
			String result = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("catename", mCatName));
				params.add(new BasicNameValuePair("flag", "highprice"));
				params.add(new BasicNameValuePair("userid", mUIDStr));
				// params.add(new BasicNameValuePair("offset", Integer
				// .toString(offset)));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		} else if (mCatName.equalsIgnoreCase("Popular")) {
			String postURL = "http://54.149.99.130/ws/get_populer_items.php?flag=&login_uid=";
			String result = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("catename", mCatName));
				params.add(new BasicNameValuePair("flag", "highprice"));
				params.add(new BasicNameValuePair("login_uid", mUIDStr));
				// params.add(new BasicNameValuePair("offset", Integer
				// .toString(offset)));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		} else if (mCatName.equalsIgnoreCase("Near Me")) {
		     // create class object
		
			String postURL = "http://54.149.99.130/ws/get_nearme_items.php?";
			String result = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("catename", mCatName));
				params.add(new BasicNameValuePair("flag", "highprice"));
				params.add(new BasicNameValuePair("userid", mUIDStr));
				params.add(new BasicNameValuePair("lat", HomeActivity.lat));
				params.add(new BasicNameValuePair("long", HomeActivity.lng));
				// params.add(new BasicNameValuePair("offset", Integer
				// .toString(offset)));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		} else {
			String postURL = "http://54.149.99.130/ws/get_item.php?catename=&flag=highprice&login_uid=";
			String result = "";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(postURL);
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("catename", mCatName));
				params.add(new BasicNameValuePair("flag", "highprice"));
				params.add(new BasicNameValuePair("login_uid", mUIDStr));
				// params.add(new BasicNameValuePair("offset", Integer
				// .toString(offset)));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		}

	}

	private String getLikeFollowingFromWeb() {

		String postURL = "http://54.149.99.130/ws/get_like_folowing_items.php?flag=highprice&userid=";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", mUIDStr));
			params.add(new BasicNameValuePair("flag", "highprice"));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void initializeComponents(View rootView) {
		mActionBarTypeface = Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/verdana.ttf");
		mHandler = new Handler();
		mItemListView = (XListView) rootView.findViewById(R.id.item_list);
		mItemListView.setPullLoadEnable(true);
		mItemListView.setPullRefreshEnable(true);
		mEmpty = (TextView) rootView.findViewById(R.id.empty);
		mEmpty.setTypeface(mActionBarTypeface);
	}

	private void onLoad() {
		mItemListView.stopRefresh();
		mItemListView.stopLoadMore();
		mItemListView.setRefreshTime("");

	}

	public class IncrementCategoriesViewedCountTask extends
			AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public IncrementCategoriesViewedCountTask(ProgressDialog progress) {
			// this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = incrementViewedCountToWeb();
			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			Log.e("", "increment result b" + result);
		}
	}

	private void incrementViewedCount() {
		if (checkInternetConnection()) {
			ProgressDialog progress = new ProgressDialog(getActivity());
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;
			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
					maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			new IncrementCategoriesViewedCountTask(progress)
					.executeOnExecutor(threadPoolExecutor);
		}
	}

	private String incrementViewedCountToWeb() {
		String postURL = "http://54.149.99.130/ws/item_view_count.php?itemid=2";
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", mItemID));
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// ++curpage;
				mEmpty.setVisibility(View.GONE);
				mItemListView.startProgress();
				// getItemsInRecentCategory();
				onLoad();
			}
		}, 2000);
	}

}
