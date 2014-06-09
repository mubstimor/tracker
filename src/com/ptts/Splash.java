package com.ptts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class Splash extends Activity {

	private static final int TIME = 4 * 1000;// 4 seconds
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	Context context;
	String regid;

	static final String TAG = "Ptts Tracker";

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent mainIntent = new Intent(getApplicationContext(),
					ViewRoutes.class);
			startActivity(mainIntent);
			finish();
		}
	};

	private Handler first_time_handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent firstTimeIntent = new Intent(getApplicationContext(),
					FirstTimeActivity.class);
			startActivity(firstTimeIntent);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		context = getApplicationContext();
		regid = getRegistrationId(context);

		if (regid.isEmpty()) {
			storeRegistrationId(context, "YES");
			first_time_handler.sendEmptyMessageDelayed(0, TIME);			
		} else {
			handler.sendEmptyMessageDelayed(0, TIME);
		}
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getRMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getRMPreferences(Context context) {
		return getSharedPreferences(Splash.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	
	  private void storeRegistrationId(Context context, String regId) {
	        final SharedPreferences prefs = getRMPreferences(context);
	        int appVersion = getAppVersion(context);
	        Log.i(TAG, "Saving regId on app version " + appVersion);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, regId);
	        editor.putInt(PROPERTY_APP_VERSION, appVersion);
	        editor.commit();
	    }

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
}
