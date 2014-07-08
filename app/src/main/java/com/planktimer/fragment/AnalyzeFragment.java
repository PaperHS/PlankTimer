package com.planktimer.fragment;

import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hs.planktimer.PreferenceKey;
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
import utils.PreferenceUtils;

/**
 * Created by Peggy on 2014/6/25.
 * potm@163.com
 */
public class AnalyzeFragment extends Fragment {

	@InjectView(R.id.analyze_bargraph)
	BarGraph mBarGraph;
	@InjectView(R.id.analyze_best_time)
	TextView mBestTime;
	@InjectView(R.id.analyze_Longest_streak)
	TextView mLongestStreak;

	private DatabaseMan mDatabaseMan;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_analyze,null);
		ButterKnife.inject(this, view);
		ArrayList<Bar> points = new ArrayList<Bar>();
		mDatabaseMan = new DatabaseMan(getActivity());

		for(int i = 0; i<10; i++){
			Bar bar = new Bar();
//			bar.setColor(Color.parseColor("#99CC00"));
			bar.setName(DateUtil.getDateSomeday(-i));
			List<Records> res =mDatabaseMan.getRecordsByDate(DateUtil.getDateSomeday(-i));
			int totalTime = 0;
			if (res == null || res.size()==0){
			}else if(res.size()==1){
				totalTime = res.get(0).getTotalplanktime();
			}else{
				int total = 0;
				for(Records re :res){
					totalTime += re.getTotalplanktime();
				}
				LogUtil.d("res = "+total);
			}
			if (totalTime < 120){
				bar.setColor(getResources().getColor(R.color.plank_too_less));
			}else if(totalTime >= 120 && totalTime < 240){
				bar.setColor(getResources().getColor(R.color.plank_time_normal));
			}else if(totalTime >= 240 && totalTime < 480){
				bar.setColor(getResources().getColor(R.color.plank_time_great));
			}else if(totalTime >= 480){
				bar.setColor(getResources().getColor(R.color.plank_time_unbelieveable));
			}
			bar.setValue(totalTime);
			points.add(bar);
			res.clear();
		}
		mBarGraph.setUnit("秒");
		mBarGraph.appendUnit(true);
		mBarGraph.setShowBarText(true);
		mBarGraph.setBars(points);
		mBestTime.setText("最佳单次时间："+ DateUtil.SecToFormat(Integer.toString(PreferenceUtils.getPrefInt(getActivity(),PreferenceKey.TOP_TIME_PLANK,0))));
		mLongestStreak.setText("最长连续天数：");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
