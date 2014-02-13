package de.thegerman.test_app;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import de.thegerman.test_app.fragments.LoadingPictureFragment;
import de.thegerman.test_app.fragments.ShowPictureFragment;
import de.thegerman.test_app.model.ErasmusPicture;

public class GalleryViewPagerAdapter extends FragmentStatePagerAdapter {

	List<ErasmusPicture> pictures = new ArrayList<ErasmusPicture>();

	public GalleryViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position < pictures.size()) {
			ErasmusPicture picture = pictures.get(position);
			Fragment fragment = new ShowPictureFragment();
			Bundle args = new Bundle();
			args.putString(ShowPictureFragment.PATH, picture.path);
			args.putString(ShowPictureFragment.ALT, picture.alt);
			fragment.setArguments(args);
			return fragment;
		} else {
			return new LoadingPictureFragment();
		}
	}

	@Override
	public int getCount() {
		return pictures.isEmpty() ? 1 : pictures.size();
	}

	public void setPictures(List<ErasmusPicture> newPictures) {
		pictures.clear();
		pictures.addAll(newPictures);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
