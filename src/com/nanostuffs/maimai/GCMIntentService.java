package com.nanostuffs.maimai;

import static com.nanostuffs.maimai.notification.CommonUtilities.SENDER_ID;
import static com.nanostuffs.maimai.notification.CommonUtilities.displayMessage;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.nanostuffs.maimai.activity.AllChatsActivity;
import com.nanostuffs.maimai.activity.HomeActivity;
import com.nanostuffs.maimai.activity.LoginActivity;
import com.nanostuffs.maimai.notification.ServerUtilities;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static SoundPool sp;
	private static MediaPlayer mMediaPlayer;
	private static String soundfile;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "Your device registred with GCM");
		LoginActivity.regId = registrationId;
		ServerUtilities.register(context, "a", "b", registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message");
		Log.e("", "message : " + message);
		displayMessage(context, message);
		// notifies user
		// if (message
		// .contains("Your Post will be posted onto the network within the next 60 minutes From Mai Mai"))
		// {
		// generateNotification1(context, message);
		//
		// // } else if (message.contains("New Passenger Request")) {
		// // // generateNotification2(context, message);
		// //
		// // } else if (message.contains("Accepted By Carpooler")) {
		// // // generateNotification3(context, message);
		// //
		// // } else if (message.contains("Confirm By Carpooler")) {
		// // // generateNotification3(context, message);
		// //
		// // } else if (message.contains("Reject Passenger Post")) {
		// // // generateNotification4(context, message);
		// //
		// // } else if (message.contains("Remove CarPool")) {
		// // // generateNotification5(context, message);
		// //
		// // } else if (message.contains("Request confirmed by passenger")) {
		// // // generateNotification6(context, message);
		//
		// } else {
		if (message.contains("You have received one message")) {
			generateNotification1(context, message);
		} else {
			generateNotification(context, message);

		}

		// }
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mMediaPlayer = new MediaPlayer();

		// int soundIds[] = new int[1];
		// soundIds[0] = sp.load(context, R.raw.win_game_sound, 1);
		// sp.play(soundIds[0], 100F, 100F, 1, 0, 1.0F);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		// boolean sound1Bool = sharedPreferences.getBoolean(
		// SettingsFragment.PREFS_SOUND1, false);
		// if (sound1Bool) {
		// mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
		// }
		// boolean sound2Bool = sharedPreferences.getBoolean(
		// SettingsFragment.PREFS_SOUND2, false);
		// if (sound2Bool) {
		// mMediaPlayer = MediaPlayer.create(context, R.raw.tick);
		// }
		// boolean sound3Bool = sharedPreferences.getBoolean(
		// SettingsFragment.PREFS_SOUND3, false);
		// if (sound3Bool) {
		// mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
		// }
		// Log.e("", "sound1Bool :" + sound1Bool + "sound2Bool : " + sound2Bool
		// + "sound3Bool" + sound3Bool);
		// mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// mMediaPlayer.start();
		// mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		// // When audio is done will change pause to play
		// public void onCompletion(MediaPlayer mp) {
		// mMediaPlayer.stop();
		// }
		// });

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);
		// Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
		Intent notificationIntent = new Intent(context, HomeActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// String sound1 = sharedPreferences.getString(
		// SettingsFragment.PREFS_SOUND1, "");
		// if (sound1.length() == 0) {
		// notification.defaults |= Notification.DEFAULT_SOUND;
		//
		// } else {
		// Log.e("", "sound : " + sound1);
		// // notification.sound = Uri.parse("android.resource://"
		// // + context.getPackageName() + sound1);
		// notification.sound = Uri.parse(sound1);
		// }
		// // Play default notification sound
		//
		// // Vibrate if vibrate is enabled
		// boolean onBool = sharedPreferences.getBoolean(
		// SettingsFragment.PREFS_VIBRATE_ON, false);
		// boolean offBool = sharedPreferences.getBoolean(
		// SettingsFragment.PREFS_VIBRATE_OFF, false);
		//
		// if (onBool) {
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		// } else if (offBool) {
		//
		// } else {
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		//
		// }

		notificationManager.notify(0, notification);

	}

	private static void generateNotification1(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mMediaPlayer = new MediaPlayer();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, AllChatsActivity.class);
		notificationIntent.putExtra("GCM", "getnotification");
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

	// private static void generateNotification2(Context context, String
	// message) {
	// String[] arr = message.split(",");
	//
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// mMediaPlayer = new MediaPlayer();
	//
	// // int soundIds[] = new int[1];
	// // soundIds[0] = sp.load(context, R.raw.win_game_sound, 1);
	// // sp.play(soundIds[0], 100F, 100F, 1, 0, 1.0F);
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	//
	// // boolean sound1Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND1, false);
	// // if (sound1Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // boolean sound2Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND2, false);
	// // if (sound2Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.tick);
	// // }
	// // boolean sound3Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND3, false);
	// // if (sound3Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // Log.e("", "sound1Bool :" + sound1Bool + "sound2Bool : " + sound2Bool
	// // + "sound3Bool" + sound3Bool);
	// // mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// // mMediaPlayer.start();
	// // mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	// // // When audio is done will change pause to play
	// // public void onCompletion(MediaPlayer mp) {
	// // mMediaPlayer.stop();
	// // }
	// // });
	//
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, arr[0], when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// CarPoolApplicantsActivity.class);
	// notificationIntent.putExtra("postid", arr[1]);
	// // set intent so it does not start a new activity
	// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// PendingIntent intent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, title, arr[0], intent);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// String sound1 = sharedPreferences.getString(
	// SettingsFragment.PREFS_SOUND1, "");
	// if (sound1.length() == 0) {
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// } else {
	// Log.e("", "sound : " + sound1);
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + sound1);
	// notification.sound = Uri.parse(sound1);
	// }
	// // Play default notification sound
	// // notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + "your_sound_file_name.mp3");
	// // Vibrate if vibrate is enabled
	// boolean onBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_ON, false);
	// boolean offBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_OFF, false);
	//
	// if (onBool) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// } else if (offBool) {
	//
	// } else {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	//
	// }
	// notificationManager.notify(0, notification);
	//
	// }
	//
	// private static void generateNotification3(Context context, String
	// message) {
	// String[] arr = message.split(",");
	//
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// mMediaPlayer = new MediaPlayer();
	//
	// // int soundIds[] = new int[1];
	// // soundIds[0] = sp.load(context, R.raw.win_game_sound, 1);
	// // sp.play(soundIds[0], 100F, 100F, 1, 0, 1.0F);
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	//
	// // boolean sound1Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND1, false);
	// // if (sound1Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // boolean sound2Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND2, false);
	// // if (sound2Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.tick);
	// // }
	// // boolean sound3Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND3, false);
	// // if (sound3Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // Log.e("", "sound1Bool :" + sound1Bool + "sound2Bool : " + sound2Bool
	// // + "sound3Bool" + sound3Bool);
	// // mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// // mMediaPlayer.start();
	// // mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	// // // When audio is done will change pause to play
	// // public void onCompletion(MediaPlayer mp) {
	// // mMediaPlayer.stop();
	// // }
	// // });
	//
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, arr[0], when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// CarpoolNotificationActivity.class);
	// Log.e("", "test ........arr[1] :" + arr[1]);
	// notificationIntent.putExtra("postid", arr[1]);
	// // set intent so it does not start a new activity
	// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// PendingIntent intent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, title, arr[0], intent);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// String sound1 = sharedPreferences.getString(
	// SettingsFragment.PREFS_SOUND1, "");
	// if (sound1.length() == 0) {
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// } else {
	// Log.e("", "sound : " + sound1);
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + sound1);
	// notification.sound = Uri.parse(sound1);
	// }
	// // Play default notification sound
	// // notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + "your_sound_file_name.mp3");
	// // Vibrate if vibrate is enabled
	// boolean onBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_ON, false);
	// boolean offBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_OFF, false);
	//
	// if (onBool) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// } else if (offBool) {
	//
	// } else {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	//
	// }
	// notificationManager.notify(0, notification);
	//
	// }
	//
	// private static void generateNotification4(Context context, String
	// message) {
	// String[] arr = message.split(",");
	//
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// mMediaPlayer = new MediaPlayer();
	//
	// // int soundIds[] = new int[1];
	// // soundIds[0] = sp.load(context, R.raw.win_game_sound, 1);
	// // sp.play(soundIds[0], 100F, 100F, 1, 0, 1.0F);
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	//
	// // boolean sound1Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND1, false);
	// // if (sound1Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // boolean sound2Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND2, false);
	// // if (sound2Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.tick);
	// // }
	// // boolean sound3Bool = sharedPreferences.getBoolean(
	// // SettingsFragment.PREFS_SOUND3, false);
	// // if (sound3Bool) {
	// // mMediaPlayer = MediaPlayer.create(context, R.raw.win_game_sound);
	// // }
	// // Log.e("", "sound1Bool :" + sound1Bool + "sound2Bool : " + sound2Bool
	// // + "sound3Bool" + sound3Bool);
	// // mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// // mMediaPlayer.start();
	// // mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	// // // When audio is done will change pause to play
	// // public void onCompletion(MediaPlayer mp) {
	// // mMediaPlayer.stop();
	// // }
	// // });
	//
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, arr[0], when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// CarpoolNotificationActivity.class);
	// notificationIntent.putExtra("postid", arr[1]);
	// // set intent so it does not start a new activity
	// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// PendingIntent intent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, title, arr[0], intent);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// String sound1 = sharedPreferences.getString(
	// SettingsFragment.PREFS_SOUND1, "");
	// if (sound1.length() == 0) {
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// } else {
	// Log.e("", "sound : " + sound1);
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + sound1);
	// notification.sound = Uri.parse(sound1);
	// }
	// // Play default notification sound
	// // notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + "your_sound_file_name.mp3");
	// // Vibrate if vibrate is enabled
	// boolean onBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_ON, false);
	// boolean offBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_OFF, false);
	//
	// if (onBool) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// } else if (offBool) {
	//
	// } else {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	//
	// }
	// notificationManager.notify(0, notification);
	//
	// }
	//
	// private static void generateNotification5(Context context, String
	// message) {
	// String[] arr = message.split(",");
	//
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// mMediaPlayer = new MediaPlayer();
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, arr[0], when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// CarPoolApplicantsActivity.class);
	// notificationIntent.putExtra("postid", arr[1]);
	// // set intent so it does not start a new activity
	// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// PendingIntent intent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, title, arr[0], intent);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// String sound1 = sharedPreferences.getString(
	// SettingsFragment.PREFS_SOUND1, "");
	// if (sound1.length() == 0) {
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// } else {
	// Log.e("", "sound : " + sound1);
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + sound1);
	// notification.sound = Uri.parse(sound1);
	// }
	// // Play default notification sound
	// // notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + "your_sound_file_name.mp3");
	// // Vibrate if vibrate is enabled
	// boolean onBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_ON, false);
	// boolean offBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_OFF, false);
	//
	// if (onBool) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// } else if (offBool) {
	//
	// } else {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	//
	// }
	// notificationManager.notify(0, notification);
	//
	// }
	//
	// private static void generateNotification6(Context context, String
	// message) {
	// String[] arr = message.split(",");
	//
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// mMediaPlayer = new MediaPlayer();
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, arr[0], when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// CarPoolApplicantsActivity.class);
	// notificationIntent.putExtra("postid", arr[1]);
	// // set intent so it does not start a new activity
	// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// PendingIntent intent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, title, arr[0], intent);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// String sound1 = sharedPreferences.getString(
	// SettingsFragment.PREFS_SOUND1, "");
	// if (sound1.length() == 0) {
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// } else {
	// Log.e("", "sound : " + sound1);
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + sound1);
	// notification.sound = Uri.parse(sound1);
	// }
	// // Play default notification sound
	// // notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://"
	// // + context.getPackageName() + "your_sound_file_name.mp3");
	// // Vibrate if vibrate is enabled
	// boolean onBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_ON, false);
	// boolean offBool = sharedPreferences.getBoolean(
	// SettingsFragment.PREFS_VIBRATE_OFF, false);
	//
	// if (onBool) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// } else if (offBool) {
	//
	// } else {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	//
	// }
	// notificationManager.notify(0, notification);
	//
	// }
}
