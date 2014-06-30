package com.planktimer.fragment;

import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hs.planktimer.R;
import com.planktimer.database.DatabaseMan;
import com.planktimer.database.Records;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import holographlibrary.Bar;
import holographlibrary.BarGraph;
import utils.DateUtil;
import utils.LogUtil;

/**
 * Created by Peggy on 2014/6/25.
 * potm@163.com
 */
public class AnalyzeFragment extends Fragment {

	@InjectView(R.id.analyze_bargraph)
	BarGraph mBarGraph;
	private DatabaseMan mDatabaseMan;
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
		mDatabaseMan = new DatabaseMan(getActivity());

		for(int i = 0; i<10; i++){
			Bar bar = new Bar();
			bar.setColor(Color.parseColor("#99CC00"));
			bar.setName(DateUtil.getDateSomeday(-i));
			List<Records> res =mDatabaseMan.getRecordsByDate(DateUtil.getDateSomeday(-i));
			if (res == null || res.size()==0){
				bar.setValue(0);
			}else if(res.size()==1){
				bar.setValue(res.get(0).getTotalplanktime());
			}else{
				int total = 0;
				for(Records re :res){
					total += re.getTotalplanktime();
				}
				bar.setValue(total);
				LogUtil.d("res = "+total);
			}
			points.add(bar);
			bar = null;
			res.clear();
			res = null;
		}
		mBarGraph.setUnit("s");
		mBarGraph.appendUnit(true);
		mBarGraph.setShowBarText(true);
		mBarGraph.setBars(points);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
