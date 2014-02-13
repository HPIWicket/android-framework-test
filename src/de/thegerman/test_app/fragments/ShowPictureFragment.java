package de.thegerman.test_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.thegerman.test_app.R;

public class ShowPictureFragment extends Fragment {

	public static final String BACKEND_PATH = "http://erasmus.thegerman.de/images/full/";
	public static final String PATH = "PICTURE_PATH";
	public static final String ALT = "PICTURE_ALT";
	private String mPath;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mPath = arguments.getString(PATH);
		}
		mView = inflater.inflate(R.layout.loading_picture_fragment, container, false);
		ImageView imageView = (ImageView) mView.findViewById(R.id.image_header);
		Picasso.with(getActivity()).load(BACKEND_PATH+ mPath).into(imageView);
		return mView;
	}
}
