package com.jhmvin.commons;

import com.dlazaro66.qrcodereaderview.QRCodeHelper;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jhmvin.linked.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Commons {
	/**
	 * Request the activity to be full screen
	 * 
	 * @param activity
	 */
	public static void setActivityFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 
	 * @param v
	 */
	public static void startGradientBackground(View v) {
		AnimationDrawable ad = (AnimationDrawable) v.getBackground();
		ad.setExitFadeDuration(5000);
		ad.setExitFadeDuration(2000);
		ad.start();

	}

	/**
	 * Only reads the IMEI of the first SIM
	 * 
	 * @param telephony
	 * @return
	 */
	public static String getIMEI(Object telephony) {

		TelephonyManager telephonyManager = (TelephonyManager) telephony;
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	public static String deviceImei(Activity a) {
		Object telephony = a.getSystemService(Context.TELEPHONY_SERVICE);
		return getIMEI(telephony);
	}

	/**
	 * 
	 * @param a
	 * @param link
	 */
	public static void launchBrowser(Activity a, String link) {
		String url = link;
		Uri webpage = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, webpage);
		if (launchBrowser.resolveActivity(a.getPackageManager()) != null) {
			a.startActivity(launchBrowser);
		}
	}

	public static Bitmap generateQR(String text, Activity a) {
		try {
			Bitmap icon = BitmapFactory.decodeResource(a
					.getApplicationContext().getResources(),
					R.drawable.cict_logo);
			QRCodeHelper qrHelper = QRCodeHelper.newInstance();

			qrHelper.setContent(text);

			qrHelper.setErrorCorrectionLevel(ErrorCorrectionLevel.Q);
			qrHelper.setWidthAndHeight(200, 200);
			qrHelper.setLogo(icon);
			qrHelper.setMargin(1);
			Bitmap qrImage = qrHelper.generate();
			return qrImage;
			// img_view.setImageBitmap(qrImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("Error", e.toString());
			return null;
		}

	}
}
