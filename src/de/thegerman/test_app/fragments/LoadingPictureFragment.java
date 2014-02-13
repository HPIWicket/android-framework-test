package de.thegerman.test_app.fragments;

import de.thegerman.test_app.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingPictureFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  	return inflater.inflate(R.layout.loading_picture_fragment, container, false);
  }
}
