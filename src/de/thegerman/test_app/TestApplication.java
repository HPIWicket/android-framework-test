package de.thegerman.test_app;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.Context;

public class TestApplication extends Application {
	private static Context appContext;
	private static RequestQueue requestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
		requestQueue = Volley.newRequestQueue(appContext);
	}

	public static Context getAppContext() {
		return appContext;
	}
	
	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}
}
