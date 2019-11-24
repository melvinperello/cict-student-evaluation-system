package com.jhmvin.linked;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.jhmvin.commons.Commons;

public class CodeScanner extends Activity implements OnQRCodeReadListener {

	private QRCodeReaderView qrCodeReaderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this); // full screen
		setContentView(R.layout.activity_code_scanner);

		qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
		qrCodeReaderView.setOnQRCodeReadListener(this);

		// Use this function to enable/disable decoding
		qrCodeReaderView.setQRDecodingEnabled(true);

		// Use this function to change the auto focus interval (default is 5
		// secs)
		qrCodeReaderView.setAutofocusInterval(5000L);

		// Use this function to enable/disable Torch
		qrCodeReaderView.setTorchEnabled(false);

		// Use this function to set front camera preview
		// qrCodeReaderView.setFrontCamera();

		// Use this function to set back camera preview
		qrCodeReaderView.setBackCamera();
	}

	@Override
	public void onQRCodeRead(String text, PointF[] points) {
		// TODO Auto-generated method stub
		qrCodeReaderView.stopCamera();
		sendResults(text);
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		qrCodeReaderView.startCamera();
	}

	@Override
	protected void onPause() {
		super.onPause();
		qrCodeReaderView.stopCamera();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// do nothing on back pressed
	}

	private void sendResults(String text) {
		Intent data = new Intent();
		data.putExtra("qr_string", text);
		setResult(RESULT_OK, data);
		finish();
	}

}
