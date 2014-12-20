package de.thegerman.test_app;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Cache;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import dalvik.system.DexClassLoader;
import de.thegerman.nativebitmapcache.AshmemBitmapCache;
import de.thegerman.nativebitmapcache.BitmapPool;
import de.thegerman.nativebitmapcache.MemoryFileBitmapCache;
import de.thegerman.nativebitmapcache.NativeBitmapCache;
import de.thegerman.nativebitmapcache.PicassoLruCache;

public class TestApplication extends Application {
	private static Context sAppContext;
	private static RequestQueue sRequestQueue;
	private static BitmapPool sBitmapPool;
	private static Picasso sPicasso;
	private static Cache sPicassoCache;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = getApplicationContext();
		sBitmapPool = new BitmapPool();
		sPicassoCache = loadPicassoCache(sAppContext); 
		sRequestQueue = Volley.newRequestQueue(sAppContext);
		sPicasso = new Picasso.Builder(sAppContext).
				memoryCache(sPicassoCache).
				indicatorsEnabled(true).
				loggingEnabled(true).
				build();
	}
	
	@Override
	public void onTrimMemory(int level) {
		Log.d("TrimMemory", "Trim memory for level: " + level);
		super.onTrimMemory(level);
	}
	
	private Cache loadPicassoCache(Context appContext) {
		/*
		final String libPath = Environment.getExternalStorageDirectory() + "/nativebitmapcache.jar";
    File libFile = new File(libPath);
    if (libFile.exists()) {
   		 try {
          final File tmpDir = getDir("dex", 0);
  
          final DexClassLoader classloader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), null, this.getClass().getClassLoader());
          final Class<Cache> classToLoad = (Class<Cache>) classloader.loadClass("de.thegerman.nativebitmapcache.ProxyBitmapCache");
          
          final Constructor<?> constructor = classToLoad.getConstructor(Context.class);
          final Cache proxyCache  = (Cache) constructor.newInstance(appContext);
          return proxyCache;
      } catch (Exception e) {
          e.printStackTrace();
      }
    } else {
    	Log.d("NativeBitmapCache", "Lib not found at: " + libPath);
    }
    */

		Cache picassoCache; 
		// picassoCache = new LruCache(appContext);
//		 picassoCache = new PicassoLruCache(appContext);
//		 picassoCache = new NativeBitmapCache(appContext);
		picassoCache = new AshmemBitmapCache(appContext, sBitmapPool);
//		picassoCache = new MemoryFileBitmapCache(appContext);
    return picassoCache;
	}
	
	public static void makeBitmapAvailable(Bitmap bitmap) {
		if (sPicassoCache instanceof AshmemBitmapCache) {
			sBitmapPool.addAvailableBitmap(bitmap);
		}
	}

	public static Context getAppContext() {
		return sAppContext;
	}

	public static RequestQueue getRequestQueue() {
		return sRequestQueue;
	}

	public static Picasso getPicassoInstance() {
		return sPicasso;
	}

	public static void printPicassoCacheStats() {
		Log.d("Picasso", sPicasso.getSnapshot().toString());
	}
}
