package com.melvin.android;

import android.app.Activity;
import android.os.Bundle;

public class MonoActivity extends Activity 
{
	//-------------------------------------------
	private MonoController controller;
	//-------------------------------------------

	/**
	 * Gets the activity instance of this activity.
	 * 
	 * @return
	 */
	public MonoActivity getActivity() {
		return this;
	}

	/**
	 * On creation of this activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.controller = new MonoController(getActivity());
	}

	public MonoController getController() {
		return controller;
	}

	public void setController(MonoController controller) {
		this.controller = controller;
	}
}
