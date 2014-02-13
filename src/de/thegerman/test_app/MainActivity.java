package de.thegerman.test_app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import de.thegerman.test_app.model.ErasmusPicture;
import de.thegerman.test_app.requests.GsonRequest;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private List<String> menuItems;
	private ViewGroup mContentFrame;
	private ViewPager mViewPager;
	private GalleryViewPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FadingActionBarHelper helper = new FadingActionBarHelper().actionBarBackground(R.drawable.ab_background).headerView(createHeaderView()).contentLayout(R.layout.activity_scrollview);
		mContentFrame = (ViewGroup) findViewById(R.id.contentFrame);
		mContentFrame.addView(helper.createView(this));
		helper.initActionBar(this);
		setUpDrawer();
	}

	private View createHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_header, null);
		mPagerAdapter = new GalleryViewPagerAdapter(getFragmentManager());
		mViewPager = (ViewPager) headerView.findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		GsonRequest<ErasmusPicture[]> request = new GsonRequest<ErasmusPicture[]>("http://erasmus.thegerman.de/conf/getJSON.php?aktion=getAllImagesJson", ErasmusPicture[].class, null, createSuccessListener(), null);
		TestApplication.getRequestQueue().add(request);
		return headerView;
	}

	private Listener<ErasmusPicture[]> createSuccessListener() {
		return new Listener<ErasmusPicture[]>() {

			@Override
			public void onResponse(ErasmusPicture[] pictures) {
				mPagerAdapter.setPictures(Arrays.asList(pictures));
				mPagerAdapter.notifyDataSetChanged();
			}
		};
	}

	protected void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mDrawerList = (ListView) findViewById(R.id.leftDrawer);
		menuItems = new ArrayList<String>();
		menuItems.add("Test");

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View drawerView) {
				showContentActionbar();
			}

			public void onDrawerOpened(View drawerView) {
				showAppActionbar();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mDrawerToggle != null) {
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	}

	protected void showContentActionbar() {
		invalidateOptionsMenu();
	}

	protected void showAppActionbar() {
		invalidateOptionsMenu();
	}

}
