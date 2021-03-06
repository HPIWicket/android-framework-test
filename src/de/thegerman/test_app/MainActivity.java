package de.thegerman.test_app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;

import de.thegerman.test_app.model.ErasmusPicture;
import de.thegerman.test_app.requests.GsonRequest;

public class MainActivity extends ActionBarActivity {

    private static final String LAST_VIEWPAGER_POSITION = "LAST_VIEWPAGER_POSITION";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<String> menuItems;
    private ViewGroup mContentFrame;
    private ViewPager mViewPager;
    private GalleryViewPagerAdapter mPagerAdapter;
    private Drawable mBackgroundDrawable;
    private FadingActionBarHelper mFadingActionBarHelper;
    private View mStickyView;
    private View mPlaceholderView;
    private int mSavedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // Title and subtitle
        mToolbar.setTitle("MY toolbar");
        mToolbar.setSubtitle("Subtitle");
        setSupportActionBar(mToolbar);

        if (savedInstanceState != null) {
            mSavedPosition = savedInstanceState.getInt(LAST_VIEWPAGER_POSITION, 0);
        }

        mBackgroundDrawable = getResources().getDrawable(R.drawable.ab_background);
        mFadingActionBarHelper = new FadingActionBarHelper().actionBarBackground(mBackgroundDrawable).headerView(createHeaderView()).contentView(createContentView())
                .scrollChangedCallback(mScrollChangedCallback);
        mContentFrame = (ViewGroup) findViewById(R.id.contentFrame);
        mContentFrame.addView(mFadingActionBarHelper.createView(this));
        mFadingActionBarHelper.initActionBar(this, getSupportActionBar());
        setUpDrawer();
    }

    private View createContentView() {
        final View contentView = getLayoutInflater().inflate(R.layout.activity_scrollview, null);
        mStickyView = contentView.findViewById(R.id.sticky);
        mPlaceholderView = contentView.findViewById(R.id.placeholder);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mScrollChangedCallback.onScroll(0, 0);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        return contentView;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_VIEWPAGER_POSITION, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private View createHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.activity_header, null);
        mPagerAdapter = new GalleryViewPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) headerView.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        GsonRequest<ErasmusPicture[]> request = new GsonRequest<ErasmusPicture[]>("http://erasmus.thegerman.de/conf/getJSON.php?aktion=getAllImagesJson", ErasmusPicture[].class,
                null, createSuccessListener(), null);
        TestApplication.getRequestQueue().add(request);
        return headerView;
    }

    private Listener<ErasmusPicture[]> createSuccessListener() {
        return new Listener<ErasmusPicture[]>() {

            @Override
            public void onResponse(ErasmusPicture[] pictures) {
                mPagerAdapter.setPictures(Arrays.asList(pictures));
                mPagerAdapter.notifyDataSetChanged();
                if (pictures.length > mSavedPosition) {
                    mViewPager.setCurrentItem(mSavedPosition, false);
                }
            }
        };
    }

    protected void setUpDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.leftDrawer);
        mActionBarHeight = (int) getResources().getDimension(R.dimen.toolbar_height);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mDrawerList.getLayoutParams();
        layoutParams.setMargins(0, mActionBarHeight, 0, 0);
        menuItems = new ArrayList<String>();
        menuItems.add("Test");

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));

        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
        mDrawerLayout, /* DrawerLayout object */
        mToolbar, /* nav drawer image to replace 'Up' caret */
        R.string.drawer_open, /* "open drawer" description for accessibility */
        R.string.drawer_close /* "close drawer" description for accessibility */
                ) {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        checkActionBarVisibility(slideOffset);
                        super.onDrawerSlide(drawerView, slideOffset);
                    }

                    private void checkActionBarVisibility(float slideOffset) {
                        if (slideOffset > 0) {
                            int alphaStep = (int) ((255 - mFadingActionBarHelper.getLastAlpha()) * slideOffset);
                            mBackgroundDrawable.setAlpha(mFadingActionBarHelper.getLastAlpha() + alphaStep);
                        } else {
                            mBackgroundDrawable.setAlpha(mFadingActionBarHelper.getLastAlpha());
                        }
                    }

                    public void onDrawerClosed(View drawerView) {
                        showContentActionbar();
                        mBackgroundDrawable.setAlpha(mFadingActionBarHelper.getLastAlpha());
                    }

                    public void onDrawerOpened(View drawerView) {
                        showAppActionbar();
                    }
                };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    private OnScrollChangedCallback mScrollChangedCallback = new OnScrollChangedCallback() {
        @Override
        public void onScroll(int l, int scrollY) {
            int stickyPos = scrollY - mFadingActionBarHelper.getHeaderHeight() + mActionBarHeight;
            mStickyView.setTranslationY(Math.max(mPlaceholderView.getTop(), stickyPos));
        }
    };
    private int mActionBarHeight;
    private Toolbar mToolbar;

}
