package com.nanostuffs.maimai.activity;

import static com.nanostuffs.maimai.notification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.nanostuffs.maimai.notification.CommonUtilities.EXTRA_MESSAGE;
import static com.nanostuffs.maimai.notification.CommonUtilities.SENDER_ID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nanostuffs.maimai.CustomHttpClient;
import com.nanostuffs.maimai.R;
import com.nanostuffs.maimai.notification.WakeLocker;

public class LoginActivity extends Activity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	private LoginButton mSignUpWithFacebook;
	private Session session;
	private String access_token;
	private Button mFBSignUp;
	private Button mGoogleSignUp;
	private EditText mPassword;
	private TextView mForgotPassword;
	private Button mLogin;
	private Button mNewUserSignUp;
	private TextView mBottomText1;
	private TextView mTermsOfService;
	private Typeface mActionBarTypeface;
	private EditText mEmail;
	private LinearLayout mLin;
	private Dialog dialog;
	private String mForgotEmail;
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	public static int chat_cnt;
	public static ArrayList<String> temp = new ArrayList<String>();
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;
	private boolean mIntentInProgress;
	private SignInButton mTestGoogle;
	public static final String PREFS_LOGIN = "LOGIN";
	public static final String PREFS_NAME = "NAME";

	/*
	 * Track whether the sign-in button has been clicked so that we know to
	 * resolve all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/*
	 * Store the connection result from onConnectionFailed callbacks so that we
	 * can resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private String personName;
	private String personPhotoUrl;
	private String email;
	private String location;
	private int flag;
	private byte[] bb;
	private ImageView mProfilePic;
	private String mGooglePhotoURL = "";
	private String mGooglePersonName = "";
	private String mGoogleEmail = "";
	private String mGoogleLocation = "";

	private String mFBPhotoURL = "";
	private String mFBPersonName = "";
	private String mFBEmail = "";
	private String mRegID;
	AsyncTask<Void, Void, Void> mRegisterTask;
	public static String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo("com.nanostuffs.maimai",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// mGoogleApiClient = new GoogleApiClient.Builder(this)
		// .addConnectionCallbacks(this)
		// .addOnConnectionFailedListener(this)
		// .addApi(Plus.API, null)
		// .addScope(Plus.SCOPE_PLUS_LOGIN)
		// .build();
		try {

			initializeComponents();
			

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void showConnectionTimeoutMsg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this);
		builder.setMessage("Connection Timeout ! Please try again.");
		builder.setTitle("Warning !");
		builder.setIcon(R.drawable.alert);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public class GCMTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		public GCMTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			// progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Log.e("", "doInBackground");
			try {

				GCMRegistrar.checkDevice(LoginActivity.this);

				// Make sure the manifest was properly set - comment out this
				// line
				// while developing the app, then uncomment it when it's ready.
				// GCMRegistrar.checkManifest(LoginActivity.this);

				registerReceiver(mHandleMessageReceiver, new IntentFilter(
						DISPLAY_MESSAGE_ACTION));

				// Get GCM registration id
				regId = GCMRegistrar.getRegistrationId(LoginActivity.this);

				// Check if regid already presents
				if (regId.equals("")) {
					Log.e("", "11");

					// Registration is not present, register now with GCM
					GCMRegistrar.register(LoginActivity.this, SENDER_ID);
				} else {
					Log.e("", "22");

					// Device is already registered on GCM
					if (GCMRegistrar.isRegisteredOnServer(LoginActivity.this)) {
						// Skips registration.
						GCMRegistrar.unregister(LoginActivity.this);
						GCMRegistrar.register(LoginActivity.this, SENDER_ID);
						// Toast.makeText(getApplicationContext(),
						// "Already registered with GCM",
						// Toast.LENGTH_LONG).show();
					} else {
						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						final Context context = LoginActivity.this;
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								// Register on our server
								// On server creates a new user
								// ServerUtilities.register(context, name,
								// amount, regId);
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mRegisterTask = null;
							}

						};
						mRegisterTask.execute(null, null, null);
					}
				}
				int flag = 0;
				while (regId.equals("")) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
			}
			return regId;
		}

		public void onPostExecute(String result) {
			progress.dismiss();
			Log.e("", "regId : " + regId);
			if (regId != null) {
				mRegID = regId;
				savePreferences("regId", regId);
			}

		}
	}

	private void savePreferences(String key, String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	private AlertDialog alert;

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();

	}

	protected void onStart() {
		super.onStart();
		Log.e("", "onStart");
		try {
			mGoogleApiClient.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	protected void onStop() {
		super.onStop();
		Log.e("", "onStop");

		try {
			if (mGoogleApiClient.isConnected()) {
				mGoogleApiClient.disconnect();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		Log.e("", "res");
		flag = 1;
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(LoginActivity.this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	private void initializeComponents() {

		mActionBarTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/verdana.ttf");
		try {
			mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(Plus.API)
					.addScope(Plus.SCOPE_PLUS_LOGIN).build();
			mGoogleApiClient.connect();
		} catch (Exception e) {
			Toast.makeText(LoginActivity.this, "errre is here",Toast.LENGTH_SHORT).show();
		}

		// if (!mGoogleApiClient.isConnecting()) {
		// mSignInClicked = true;
		// resolveSignInError();
		// }

		mLin = (LinearLayout) findViewById(R.id.lin);
		mFBSignUp = (Button) findViewById(R.id.fb_signup);
		mFBSignUp.setOnClickListener(this);
		mGoogleSignUp = (Button) findViewById(R.id.google_signup);
		mGoogleSignUp.setOnClickListener(this);
		mEmail = (EditText) findViewById(R.id.email);
		mPassword = (EditText) findViewById(R.id.password);
		mForgotPassword = (TextView) findViewById(R.id.forgot_pwd);
		mProfilePic = (ImageView) findViewById(R.id.profile_pic);

		mLogin = (Button) findViewById(R.id.login);
		mLogin.setOnClickListener(this);
		mNewUserSignUp = (Button) findViewById(R.id.new_user_sign_up);
		mNewUserSignUp.setOnClickListener(this);
		mBottomText1 = (TextView) findViewById(R.id.bottom_text1);
		mTermsOfService = (TextView) findViewById(R.id.terms_of_service);
		mTermsOfService.setOnClickListener(this);
		mLin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEmail.getWindowToken(), 0);
			}
		});

		mEmail.setTypeface(mActionBarTypeface);
		mPassword.setTypeface(mActionBarTypeface);
		mForgotPassword.setTypeface(mActionBarTypeface);
		mLogin.setTypeface(mActionBarTypeface);
		mNewUserSignUp.setTypeface(mActionBarTypeface);
		mBottomText1.setTypeface(mActionBarTypeface);
		mTermsOfService.setTypeface(mActionBarTypeface);

		mForgotPassword.setTextColor(Color.BLACK);
		mForgotPassword.setPaintFlags(mForgotPassword.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		mForgotPassword.setSelected(true);
		mForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				forgotPassword1();
			}
		});
		mTermsOfService.setText(" Terms of Service");
		mTermsOfService.setTextColor(Color.BLACK);
		mTermsOfService.setPaintFlags(mTermsOfService.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		mTermsOfService.setSelected(true);
		mTermsOfService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.maimai.com/terms"));
				startActivity(newIntent);
			}
		});
		if (checkInternetConnection()) {

			try {
				ProgressDialog progress = new ProgressDialog(LoginActivity.this);
				progress.setMessage("Please wait..");
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				new GCMTask(progress).executeOnExecutor(threadPoolExecutor);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fb_signup:
			try {//5jDXLOLXrZXP8GtxjVE/evhhAPU=
			//	   5jDXLOLXrZXP8GtxjVE/evhhAPU=
				//		lUPOwZT+sYA/RXvsoLkot9J+EGw=
				  try {
                      PackageInfo info = getPackageManager().getPackageInfo(
                      "com.nanostuffs.maimai",
                      PackageManager.GET_SIGNATURES);
                      for (Signature signature : info.signatures) {
                      MessageDigest md = MessageDigest.getInstance("SHA");
                      md.update(signature.toByteArray());
                      Log.d("KeyHash:", Base64.encodeToString(md.digest(),
                      Base64.DEFAULT));
                      }
                      } catch (NameNotFoundException e) {

                      } catch (NoSuchAlgorithmException e) {

                      }
			Boolean check = checkInternetConnection();
			System.out.println("aa"+check);
			if(check==true){
			Toast.makeText(LoginActivity.this, "Please wait..", Toast.LENGTH_LONG).show();
			signUpWithFacebook();
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.google_signup:
		
			Boolean check1 = checkInternetConnection();
			System.out.println("aa"+check1);
			try {
				if(check1==true){
				Log.e("", "is....");
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInError();
				}
				
			} 
			}catch (Exception e) {
				// TODO: handle exception
				Log.e("", "is...." + mGoogleApiClient.isConnected());
			}
		
			break;
		case R.id.new_user_sign_up:
			Intent intent1 = new Intent(LoginActivity.this,
					SignUpActivity.class);
			startActivity(intent1);
			// overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.login:
			login();
			break;
		default:
			break;
		}

	}

	private void forgotPassword1() {
		dialog = new Dialog(LoginActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(getLayoutInflater().inflate(R.layout.forgot_pwd,
				null));
		final EditText email = (EditText) dialog.findViewById(R.id.email);
		email.setTypeface(mActionBarTypeface);
		if (mEmail.length() != 0) {
			email.setText(mEmail.getText().toString());
		}
		Button send = (Button) dialog.findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("", "send click");
				if (isValidEmail(email.getText().toString())) {
					dialog.dismiss();
					if (checkInternetConnection()) {
						ProgressDialog progress = new ProgressDialog(
								LoginActivity.this);
						progress.setMessage("Please wait..");
						int corePoolSize = 60;
						int maximumPoolSize = 80;
						int keepAliveTime = 10;
						BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
								maximumPoolSize);
						Executor threadPoolExecutor = new ThreadPoolExecutor(
								corePoolSize, maximumPoolSize, keepAliveTime,
								TimeUnit.SECONDS, workQueue);
						new ForgotPasswordTask(progress)
								.executeOnExecutor(threadPoolExecutor);
						mForgotEmail = email.getText().toString();
					}
				} else {
					Log.e("", "alert");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							LoginActivity.this);
					alertDialogBuilder
							.setTitle("Warning !")
							.setIcon(R.drawable.alert)
							.setMessage("Please enter valid Email")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											email.setFocusableInTouchMode(true);
											email.requestFocus();
											email.setHintTextColor(Color.RED);
										}
									});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			}
		});
		dialog.show();
		send.setTypeface(mActionBarTypeface);
	}

	private void forgotPassword() {
		LayoutInflater li = LayoutInflater.from(LoginActivity.this);
		View promptsView = li.inflate(R.layout.input_prompt, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LoginActivity.this);
		alertDialogBuilder.setView(promptsView);
		// TextView reason = (TextView) promptsView.findViewById(R.id.reason);
		// reason.setTypeface(mActionBarTypeface);
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);
		userInput.setTypeface(mActionBarTypeface);
		if (mEmail.length() != 0) {
			userInput.setText(mEmail.getText().toString());
		}
		alertDialogBuilder.setCancelable(true).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						if (isValidEmail(userInput.getText().toString())) {
							dialog.dismiss();
							if (checkInternetConnection()) {
								ProgressDialog progress = new ProgressDialog(
										LoginActivity.this);
								progress.setMessage("Please wait..");
								int corePoolSize = 60;
								int maximumPoolSize = 80;
								int keepAliveTime = 10;
								BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
										maximumPoolSize);
								Executor threadPoolExecutor = new ThreadPoolExecutor(
										corePoolSize, maximumPoolSize,
										keepAliveTime, TimeUnit.SECONDS,
										workQueue);
								new ForgotPasswordTask(progress)
										.executeOnExecutor(threadPoolExecutor);
							}
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							alertDialogBuilder
									.setTitle("Warning !")
									.setIcon(R.drawable.alert)
									.setMessage("Please enter valid Email")
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													userInput
															.setFocusableInTouchMode(true);
													userInput.requestFocus();
													userInput
															.setHintTextColor(Color.RED);
												}
											});
							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();
						}
					}
				});
		alertDialogBuilder.show();
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (NullPointerException n) {
			return false;
		}
	}

	public class LoginTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public LoginTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
			Log.e("", "onPreExecute");

		}

		@Override
		protected String doInBackground(String... params) {
			Log.e("", "doInBackground login");
			if (isOnline()) {
				resultdata = loginUserToWeb();
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								login();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				try {
					Log.e("", "result :" + result);

					JSONObject responseObj = new JSONObject(result);
					JSONArray ja = responseObj.getJSONArray("data");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						if (jo.getString("response_msg").equalsIgnoreCase(
								"Login successfully.")) {

							setPreferences(LoginActivity.PREFS_NAME);

							SharedPreferences prefs = getSharedPreferences(
									SplashActivity.PREFS_UID,
									Context.MODE_PRIVATE);
							prefs.edit()
									.putString(SplashActivity.PREFS_UID,
											jo.getString("uid")).commit();

							startActivity(new Intent(LoginActivity.this,
									HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
							finish();
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							alertDialogBuilder
									.setMessage(jo.getString("response_msg"))
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void login() {
		if (mEmail.length() == 0 && mPassword.length() == 0) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter all the details")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mEmail.setFocusableInTouchMode(true);
									mEmail.requestFocus();
									mEmail.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mEmail.length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter Email")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mEmail.setFocusableInTouchMode(true);
									mEmail.requestFocus();
									mEmail.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else if (mPassword.length() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder
					.setTitle("Warning !")
					.setIcon(R.drawable.alert)
					.setMessage("Please enter Password")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									mPassword.setFocusableInTouchMode(true);
									mPassword.requestFocus();
									mPassword.setHintTextColor(Color.RED);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else {
			if (isValidEmail(mEmail.getText().toString())) {
				if (checkInternetConnection()) {
					ProgressDialog progress = new ProgressDialog(
							LoginActivity.this);
					progress.setMessage("Logging in..");
					Log.e("", "Logging in..");
					// new LoginTask(progress).execute();
					int corePoolSize = 60;
					int maximumPoolSize = 80;
					int keepAliveTime = 10;

					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
							maximumPoolSize);
					Executor threadPoolExecutor = new ThreadPoolExecutor(
							corePoolSize, maximumPoolSize, keepAliveTime,
							TimeUnit.SECONDS, workQueue);
					new LoginTask(progress)
							.executeOnExecutor(threadPoolExecutor);
				}
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						LoginActivity.this);
				alertDialogBuilder
						.setTitle("Warning !")
						.setIcon(R.drawable.alert)
						.setMessage("Please enter valid Email")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										mEmail.setFocusableInTouchMode(true);
										mEmail.requestFocus();
										mEmail.setHintTextColor(Color.RED);
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}

	}

	public class ForgotPasswordTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public ForgotPasswordTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = forgotPasswordWeb();

			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			if (result.length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginActivity.this);
				builder.setMessage("Connection Timeout ! Please try again.");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								forgotPassword1();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				try {
					Log.e("", "result :" + result);

					JSONObject responseObj = new JSONObject(result);
					JSONArray ja = responseObj.getJSONArray("data");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						if (jo.getString("response_msg").equalsIgnoreCase(
								"Password sent on your email successfully!")) {

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							alertDialogBuilder
									.setMessage(jo.getString("response_msg"))
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();

						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									LoginActivity.this);
							alertDialogBuilder
									.setMessage(jo.getString("response_msg"))
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private String forgotPasswordWeb() {
		String postURL = "http://54.149.99.130/ws/forgot-pass.php?email=rakshi.b2.9@gmail.com";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", mForgotEmail));
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

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

	private String googleRegisterUserToWeb() {
		Log.e("", " bb :" + bb);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		mRegID = sharedPreferences.getString("regId", "");
		String postURL = "http://54.149.99.130/ws/signup.php?username=sfssss&password=11a1&usertype=&email=swati1233nuna@gmail.com&city=dcds&token=";
		String result = "";
		try {
			//Toast.makeText(getApplicationContext(), mGooglePersonName+"\n"+mGoogleEmail, Toast.LENGTH_LONG).show();
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", mGooglePersonName));
			params.add(new BasicNameValuePair("password", ""));
			params.add(new BasicNameValuePair("email", mGoogleEmail));
			params.add(new BasicNameValuePair("city", mGoogleLocation));
			params.add(new BasicNameValuePair("usertype", "fb"));
			params.add(new BasicNameValuePair("token", mRegID));
			String response = "";
			if (bb == null) {
				Log.e("", "1");
				response = CustomHttpClient.executeHttpPost(postURL, params);
			} else {
				Log.e("", "2");
				response = CustomHttpClient.executeHttpPostForImg(postURL,
						params, "userfile", bb);
			}
			result = response.toString();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String fbRegisterUserToWeb() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		mRegID = sharedPreferences.getString("regId", "");
		Log.e("", " bb :" + bb);
		String postURL = "http://54.149.99.130/ws/signup.php?username=sfssss&password=11a1&usertype=&email=swati1233nuna@gmail.com&city=dcds&token=";
		String result = "";
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", mFBPersonName));
			params.add(new BasicNameValuePair("password", ""));
			params.add(new BasicNameValuePair("email", mFBEmail));
			params.add(new BasicNameValuePair("city", ""));
			params.add(new BasicNameValuePair("usertype", "fb"));
			params.add(new BasicNameValuePair("token", mRegID));
			String response = "";
			if (bb == null) {
				Log.e("", "1");
				response = CustomHttpClient.executeHttpPost(postURL, params);
			} else {
				Log.e("", "2");
				response = CustomHttpClient.executeHttpPostForImg(postURL,
						params, "userfile", bb);
			}
			result = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String loginUserToWeb() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		mRegID = sharedPreferences.getString("regId", "");
		Log.e("", "mRegID : " + mRegID);
		Log.e("", "loginUserToWeb");
		String postURL = "http://54.149.99.130/ws/login.php?email=sf&password=111&token=";
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", mEmail.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", mPassword.getText()
					.toString()));
			params.add(new BasicNameValuePair("token", mRegID));
			// params.add(new BasicNameValuePair("token", "hh"));
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

	private void setPreferences(String prefsName) {
		SharedPreferences settings = getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(LoginActivity.PREFS_LOGIN, "SUCCESS");
		editor.commit();
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	private boolean checkInternetConnection() {

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
						LoginActivity.this);
				builder.setMessage("Internet not available, Cross check your internet connectivity and try again");
				builder.setTitle("Warning !");
				builder.setIcon(R.drawable.alert);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});

				alert = builder.create();
				alert.show();

			} catch (Exception e) {
			}
			return false;
		}
	}

	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras() != null) {
				NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
						ConnectivityManager.EXTRA_NETWORK_INFO);
				if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
					// we're connected
					try {
						alert.cancel();
						try {

							initializeComponents();

						} catch (Exception e) {
						}
					} catch (Exception e) {
					}

				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkReceiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("", "test act " + Session.getActiveSession());

		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			try {
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		Toast.makeText(LoginActivity.this, "Please wait..", Toast.LENGTH_LONG).show();

	}

	private void signUpWithFacebook() {
		try {
			session = Session.getActiveSession();
			if (session == null) {
				session = new Session(LoginActivity.this);
				Session.setActiveSession(session);
				if (session.getState()
						.equals(SessionState.CREATED_TOKEN_LOADED)) {
					session.openForRead(new Session.OpenRequest(
							LoginActivity.this).setCallback(statusCallback));
					access_token = session.getAccessToken();
				}
			}
			if (!session.isOpened() && !session.isClosed()) {

				session.openForRead(new Session.OpenRequest(LoginActivity.this)
						.setCallback(statusCallback));
				access_token = session.getAccessToken();
			} else {
				Session.openActiveSession(LoginActivity.this, true,
						statusCallback);
			}
		} catch (Exception ex) {

		}

	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
	

		@SuppressWarnings("deprecation")
		public void call(Session session, SessionState state,
				Exception exception) {
			try{
			final Session session2 = Session.getActiveSession();
			if (session2 != null && session2.isOpened()) {
				// If the session is open, make an API call to get user data
				// and define a new callback to handle the response
				
				Request request = Request.newMeRequest(session2,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								// If the response is successful
								if (session2 == Session.getActiveSession()) {
									if (user != null) {
										String user_ID = user.getId();// user id
										String profileName = user.getName();// user's
																			// profile
																			// name
										// String
										// userNameViewsetText(user.getName());
									}
								}
							}
						});
				Request.executeBatchAsync(request);
				}
				
			}
			catch(Exception e){
				
			}
			try {
				
			
			if (session.isOpened()) {
			
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {

							// callback after Graph API response with user
							// object

							public void onCompleted(GraphUser user,
									Response response) {
								
									
						
								user.getId();
								user.getUsername();
								if (user != null) {
									Log.e("", "Response : " + response);
									Log.e("useried", user.getId());
									Log.e("", "Name : " + user.getName());
									Log.e("",
											"Username : " + user.getUsername());
									String profilePic = " https://graph.facebook.com/"
											+ user.getId()
											+ "/picture?type=large";
									Log.e("", "profilePic : " + profilePic);
									// String email = user.getProperty("email");

									// // String safeEmail = user.asMap()
									// .get("email").toString();
									Log.e("",
											"email : "
													+ user.getProperty("email"));
									Log.e("", "safeEmail : "
											+ user.asMap().get("email"));

									// Intent intent = new Intent(
									// LoginActivity.this,
									// SignUpActivity.class);
									// intent.putExtra("flag", "2");
									// intent.putExtra("personPhotoUrl",
									// profilePic);
									// intent.putExtra("personName",
									// user.getName());
									// startActivity(intent);
									// overridePendingTransition(
									// R.anim.slide_in_right,
									// R.anim.slide_out_left);

									mFBPersonName = user.getName();
									mFBPhotoURL = profilePic;
									mFBEmail = user.getId();
									// if
									// (!mFBPhotoURL.equalsIgnoreCase("null")) {
									new LoadProfileImage(mProfilePic)
											.execute(mFBPhotoURL);
									// }
									signupWithFB();
								}
								
							}

						});

			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

	}

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			// btnSignIn.setVisibility(View.GONE);
			// btnSignOut.setVisibility(View.VISIBLE);
			// btnRevokeAccess.setVisibility(View.VISIBLE);
			// llProfileLayout.setVisibility(View.VISIBLE);
		} else {
			// btnSignIn.setVisibility(View.VISIBLE);
			// btnSignOut.setVisibility(View.GONE);
			// btnRevokeAccess.setVisibility(View.GONE);
			// llProfileLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				personName = currentPerson.getDisplayName();
				personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				location = currentPerson.getCurrentLocation();
			
				Log.e("person info", "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl + "Location : "
						+ location);

				// txtName.setText(personName);
				// txtEmail.setText(email);

				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				if (flag == 1) {
					// Intent intent = new Intent(LoginActivity.this,
					// SignUpActivity.class);
					// intent.putExtra("flag", "1");
					// intent.putExtra("personPhotoUrl", personPhotoUrl);
					// intent.putExtra("personName", personName);
					// intent.putExtra("email", email);
					// intent.putExtra("location", location);
					// startActivity(intent);
					// overridePendingTransition(R.anim.slide_in_right,
					// R.anim.slide_out_left);
					mGooglePhotoURL = personPhotoUrl;
					if (mGooglePersonName != null) {
						mGooglePersonName = personName;
					}
					if (mGoogleEmail != null) {
						mGoogleEmail = email;
					}
					if (mGoogleLocation != null) {
						mGoogleLocation = location;
					}
					// if (!mGooglePhotoURL.equalsIgnoreCase("null")) {
					new LoadProfileImage(mProfilePic).execute(mGooglePhotoURL);
					// }
					signupWithGoogle();
					flag = 0;

				}
				// new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
				mGoogleSignUp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent intent = new Intent(LoginActivity.this,
						// SignUpActivity.class);
						// intent.putExtra("flag", "1");
						// intent.putExtra("personPhotoUrl", personPhotoUrl);
						// intent.putExtra("personName", personName);
						// intent.putExtra("email", email);
						// intent.putExtra("location", location);
						// startActivity(intent);
						// overridePendingTransition(R.anim.slide_in_right,
						// R.anim.slide_out_left);
						
						mGooglePhotoURL = personPhotoUrl;
						if (mGooglePersonName != null) {
							mGooglePersonName = personName;
						}
						if (mGoogleEmail != null) {
							mGoogleEmail = email;
						}
						if (mGoogleLocation != null) {
							mGoogleLocation = location;
						}
						// if (!mGooglePhotoURL.equalsIgnoreCase("null")) {
						new LoadProfileImage(mProfilePic)
								.execute(mGooglePhotoURL);
						// }
						signupWithGoogle();
					}
				});

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void signupWithGoogle() {
		if (checkInternetConnection()) {
			try {
				
				ProgressDialog progress = new ProgressDialog(LoginActivity.this);
				progress.setMessage("Signing up..");
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				new GoogleRegisterTask(progress)
						.executeOnExecutor(threadPoolExecutor);
			} catch (Exception e) {
			}
		}
	}

	private void signupWithFB() {
		if (checkInternetConnection()) {
			try {

				ProgressDialog progress = new ProgressDialog(LoginActivity.this);
				progress.setMessage("Signing up..");
				int corePoolSize = 60;
				int maximumPoolSize = 80;
				int keepAliveTime = 10;
				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
						maximumPoolSize);
				Executor threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.SECONDS, workQueue);
				new FBRegisterTask(progress)
						.executeOnExecutor(threadPoolExecutor);

			} catch (Exception e) {
			}
		}
	}

	public class GoogleRegisterTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public GoogleRegisterTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = googleRegisterUserToWeb();

			}
			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			try {
				Log.e("", "result :" + result);

				JSONObject responseObj = new JSONObject(result);
				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);

					if (jo.getString("response_msg").contains(
							"Login successfully")) {

						setPreferences(LoginActivity.PREFS_NAME);

						SharedPreferences prefs = getSharedPreferences(
								SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
						prefs.edit()
								.putString(SplashActivity.PREFS_UID,
										jo.getString("uid")).commit();

						startActivity(new Intent(LoginActivity.this,
								HomeActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						showCustomToast("You have logged in using Google   ");
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								LoginActivity.this);
						alertDialogBuilder
								.setMessage(jo.getString("response_msg"))
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.dismiss();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	private void showCustomToast(String string) {
		// LayoutInflater inflater = getLayoutInflater();
		// View layout = inflater.inflate(R.layout.toast_background,
		// (ViewGroup) findViewById(R.id.toast_layout_root));
		//
		// TextView toast_text = (TextView)
		// layout.findViewById(R.id.toast_text);
		// toast_text.setTypeface(mActionBarTypeface);
		// toast_text.setText(string);
		// Toast toast = new Toast(getApplicationContext());
		// toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 15);
		// toast.setDuration(Toast.LENGTH_LONG);
		// toast.setView(layout);
		// toast.show();
		Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
	}

	public class FBRegisterTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private String resultdata;

		public FBRegisterTask(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (isOnline()) {
				resultdata = fbRegisterUserToWeb();
			}

			return resultdata;
		}

		public void onPostExecute(String result) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
			try {
				Log.e("", "result :" + result);

				JSONObject responseObj = new JSONObject(result);
				JSONArray ja = responseObj.getJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					if (jo.getString("response_msg").contains(
							"Login successfully")) {

						setPreferences(LoginActivity.PREFS_NAME);

						SharedPreferences prefs = getSharedPreferences(
								SplashActivity.PREFS_UID, Context.MODE_PRIVATE);
						prefs.edit()
								.putString(SplashActivity.PREFS_UID,
										jo.getString("uid")).commit();
						showCustomToast("You have logged in using Facebook");
						startActivity(new Intent(LoginActivity.this,
								HomeActivity.class)
								.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					} else {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								LoginActivity.this);
						alertDialogBuilder
								.setMessage(jo.getString("response_msg"))
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.dismiss();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		try {
			mGoogleApiClient.connect();
			updateUI(false);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
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
			try{
			bmImage.setImageBitmap(getCroppedBitmap(result));
			BitmapDrawable drawable = (BitmapDrawable) mProfilePic
					.getDrawable();
			Bitmap bmap = drawable.getBitmap();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmap.compress(CompressFormat.PNG, 100, bos);
			bb = bos.toByteArray();
			}catch(Exception e){
				
			}
		}
	}

}