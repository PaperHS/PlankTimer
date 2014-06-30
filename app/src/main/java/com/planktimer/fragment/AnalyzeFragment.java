package com.planktimer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hs.planktimer.R;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import holographlibrary.Bar;
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
		Random r = new Random(20);
		View view = inflater.inflate(R.layout.fragment_analyze, container, false);
		ButterKnife.inject(this, view);
		ArrayList<Bar> points = new ArrayList<Bar>();

		for(int i = 0; i<10; i++){
			Bar bar = new Bar();
			bar.setColor(Color.parseColor("#99CC00"));
			bar.setName("Test"+i);
			bar.setValue(Math.abs(r.nextInt()));
			points.add(bar);
		}
		mBarGraph.appendUnit(false);
		mBarGraph.setShowBarText(false);
		mBarGraph.setBars(points);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
