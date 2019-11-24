package org.linkedapplication.telemetry;

import android.os.Build;

public class DeviceKeeper {

	private static DeviceKeeper DEVICE_INSTANCE;

	public static DeviceKeeper values() {
		if (DEVICE_INSTANCE == null) {
			DEVICE_INSTANCE = new DeviceKeeper();
		}
		return DEVICE_INSTANCE;
	}

	private final String androidVersion;
	private final String phoneModel;
	private final String phoneBoard;
	private final String phoneBrand;
	private final String buildID;
	private final String hardware;
	private final String manufacturer;
	private final String serial;

	private DeviceKeeper() {
		this.androidVersion = android.os.Build.VERSION.RELEASE;
		this.phoneModel = android.os.Build.MODEL;
		this.phoneBoard = Build.BOARD;
		this.phoneBrand = Build.BRAND;
		this.buildID = Build.DISPLAY;
		this.hardware = Build.HARDWARE;
		this.manufacturer = Build.MANUFACTURER;
		this.serial = Build.SERIAL;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public String getPhoneBoard() {
		return phoneBoard;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public String getPhoneBrand() {
		return phoneBrand;
	}

	public String getBuildID() {
		return buildID;
	}

	public String getHardware() {
		return hardware;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getSerial() {
		return serial;
	}

}
