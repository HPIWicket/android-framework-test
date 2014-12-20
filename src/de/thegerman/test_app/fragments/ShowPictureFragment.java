package de.thegerman.test_app.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import de.thegerman.test_app.R;
import de.thegerman.test_app.TestApplication;

public class ShowPictureFragment extends Fragment {

	public static final String BACKEND_PATH = "http://erasmus.thegerman.de/images/full/";
	public static final String PATH = "PICTURE_PATH";
	public static final String ALT = "PICTURE_ALT";
	private String mPath;
	private View mView;
	private ImageView mImageView;
	private Target mTarget;
	private Bitmap mBitmap;
	private Picasso mPicassoInstance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mPath = arguments.getString(PATH);
		}
		mView = inflater.inflate(R.layout.loading_picture_fragment, container, false);
		mImageView = (ImageView) mView.findViewById(R.id.image_header);
		mPicassoInstance = TestApplication.getPicassoInstance();
		if (mTarget != null) {
			mPicassoInstance.cancelRequest(mTarget);
			mTarget = null;
		}
		mTarget = new Target() {
			
			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {}
			
			@Override
			public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
				mBitmap = bitmap;
				mImageView.setImageBitmap(bitmap);
				mTarget = null;
			}
			
			@Override
			public void onBitmapFailed(Drawable errorDrawable) {}
		};
		mPicassoInstance.load(BACKEND_PATH+ mPath).into(mTarget);
		TestApplication.printPicassoCacheStats();
		return mView;
	}
	
	@Override
	public void onDestroyView() {
		if (mTarget != null) {
			mPicassoInstance.cancelRequest(mTarget);
			mTarget = null;
		}
		if (mBitmap != null) {
			TestApplication.makeBitmapAvailable(mBitmap);
		}
		super.onDestroyView();
	}
}
