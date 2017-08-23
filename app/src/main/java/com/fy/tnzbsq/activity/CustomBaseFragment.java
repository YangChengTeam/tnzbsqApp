package com.fy.tnzbsq.activity;

import com.fy.tnzbsq.view.CustomWebView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomBaseFragment extends Fragment {

	public boolean isShow;
	public CustomWebView customWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
