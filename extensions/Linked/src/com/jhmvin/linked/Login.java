package com.jhmvin.linked;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhmvin.actors.Authenticator;
import com.jhmvin.actors.Linked;
import com.jhmvin.commons.ApplicationCache;
import com.jhmvin.commons.Commons;
import com.jhmvin.commons.SharedPrefManager;

/**
 * Controls the login layout of this application.
 * 
 * @author Jhon Melvin
 * 
 */
public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_login);

		//
		/**
		 * Set IP.
		 */
		// get ip from shared pref
		String ip = SharedPrefManager.getIP(this);
		// assign it to application cache
		ApplicationCache.view().APP_IP = ip;
		//
		initialize();
		eventHandling();
	}

	private void DEVELOPER_OPTIONS(String command) {
		String[] exe = command.split(" ");
		if (exe.length == 2) {
			if (exe[0].equalsIgnoreCase("setip")) {
				SharedPrefManager.setIp(this, exe[1]);
				Toast.makeText(getApplicationContext(), "IP CHANGED",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						SharedPrefManager.getIP(this), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Declare widgets
	 */
	private Button btn_login;
	private EditText txt_user, txt_pass;
	private TextView btn_about, btn_forgot;

	/**
	 * Initialization of all components
	 */
	private void initialize() {
		View pnl = findViewById(R.id.pnl_login);
		Commons.startGradientBackground(pnl);
		/**
		 * Initialize widgets
		 */
		btn_login = (Button) findViewById(R.id.btn_login);
		txt_user = (EditText) findViewById(R.id.txt_user);
		txt_pass = (EditText) findViewById(R.id.txt_pass);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Authenticator auth = new Authenticator(this);
		// auth.checkWifi();
		Linked.instance().authenticator(this);
	}

	/**
	 * Event related
	 */
	private void eventHandling() {
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickedLogin();
			}
		});
	}

	private void onClickedLogin() {
		
		
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setTitle("Incomplete Details");
		dlgAlert.setCancelable(true);
		dlgAlert.setPositiveButton("Okay! I'll try again.", null);

		String username = txt_user.getText().toString().trim();
		String password = txt_pass.getText().toString().trim();

		/**
		 * Enter Dev Mode.
		 */
		if (password.equalsIgnoreCase("developer_mode")) {
			this.DEVELOPER_OPTIONS(username);
			return;
		}
		if (username.isEmpty()) {
			dlgAlert.setMessage("Hey ! You didn't enter your USERNAME. Please try again.");
			dlgAlert.show();
			return;
		}

		if (password.isEmpty()) {
			dlgAlert.setMessage("Hey ! You didn't enter your PASSWORD. Please try again.");
			dlgAlert.show();
			return;
		}

		/**
		 * Login Routine
		 */
		Authenticator login = Linked.instance().createAuthenticator(this);
		login.authUsername = username;
		login.authPassword = password;
		login.authIMEI = Commons
				.getIMEI(getSystemService(Context.TELEPHONY_SERVICE));
		login.authIsSkipped = 0;
		login.authenticate();
	}
}
