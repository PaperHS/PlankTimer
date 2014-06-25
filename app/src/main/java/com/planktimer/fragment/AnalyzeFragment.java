package com.planktimer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hs.planktimer.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import holographlibrary.BarGraph;

/**
 * Created by Peggy on 2014/6/25.
 * potm@163.com
 */
public class AnalyzeFragment extends Fragment {

	@InjectView(R.id.analyze_bargraph)
	BarGraph mBarGraph;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_analyze, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
