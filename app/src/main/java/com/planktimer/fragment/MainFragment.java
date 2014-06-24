package com.planktimer.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.hs.planktimer.R;
import com.planktimer.database.DatabaseMan;
import com.planktimer.database.Records;
import com.planktimer.utils.DateUtil;

/**
 * fragment for main ; show time and record it
 * 
 * @author Peggy
 * @email  potm@163.com
 *
 * @date   2014年6月17日 上午11:56:23
 */

public class MainFragment extends Fragment{
	
	/**
	 * phoneFace sensor
	 */
	private SensorManager sm;
	private Sensor sensor;
	private Timer mTimer;
	private boolean mIsRunning = false;
	private DatabaseMan mMan;
	private ArrayAdapter<String> mArrayAdapter;
	private ArrayList<String> mDataList = new ArrayList<String>();
	
	private LinkedList<HashMap<Integer, Integer>> mData = new LinkedList<HashMap<Integer, Integer>>();
	public static final int SPORTING = 1; 
	public static final int IDLE = 0; 
	
	private int mTotalTime;
	private int mTotalPlankTime;
	
	@InjectView(R.id.fragment_main_tv_timer) TextView mTimeText;
	@InjectView(R.id.fragment_main_listview_record) ListView mRecords;
	@InjectView(R.id.fragment_main_btn_start) Button mStartBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		mMan = new DatabaseMan(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_1, container, false);
		//init butterknife
		ButterKnife.inject(this,view);
		mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDataList);
		mRecords.setAdapter(mArrayAdapter);
		return view;
	}
	
	@Override
	public void onDestroyView() {
		ButterKnife.reset(this);
		sm.unregisterListener(mPhoneFaceSensor);
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		super.onDestroyView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//set up sensor
		sm = (SensorManager)getActivity().getSystemService(Service.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
		sm.registerListener(mPhoneFaceSensor, sensor, SensorManager.SENSOR_DELAY_GAME);
		
		mTimer = new Timer();
		mTimer.schedule(mPlankTimer, 0, 1000);
		
	}
	
	@OnClick(R.id.fragment_main_btn_start) 
	public void startTimer(){
		mIsRunning = !mIsRunning;
		if (!mIsRunning) {
			Records record = new Records();
			record.setRecordTime(DateUtil.getCurrentTime());
			record.setRecordDate(mData.toString());
			record.setTotalplanktime(mTotalPlankTime);
			record.setTotaltime(mTotalTime);
			mMan.addRecord(record);
		}
	}
	
	float x,y,z;
	boolean mLastOrient =  true;
	boolean mGoUp;
	
	SensorEventListener mPhoneFaceSensor = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			 x = event.values[0];  
             y = event.values[1];  
             z = event.values[2]; 
             if (z > 8) {
            	 mGoUp = true;
             }else if (z < -8) {
				mGoUp = false;
             } 
             
             if (mGoUp != mLastOrient) {
            	 HashMap<Integer, Integer> reMap = new HashMap<Integer,Integer>();
            	 //record 
 				if (mLastOrient) {
					mTotalPlankTime += mHour*3600 + mMin*60 +mSec; // 记录运动时间 单位s
					reMap.put(SPORTING, mHour*3600 + mMin*60 +mSec);
 				}else {
 					reMap.put(IDLE, mHour*3600 + mMin*60 +mSec);
				}
 				 mTotalTime += mHour*3600 + mMin*60 +mSec;	// 记录总时间
 				 mData.add(reMap);
            	 mDataList.add(mTimeText.getText().toString());
            	 mArrayAdapter.notifyDataSetChanged();
            	 mMin = 0;
            	 mSec = 0;
            	 mLastOrient = mGoUp;
             }
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
	
	/**
	 * timetask 4 timeKeeper
	 */
	int mHour,mMin,mSec;
	String mStrMin,mStrSec;
	TimerTask mPlankTimer = new TimerTask() {
		
		@Override
		public void run() {
			if (mIsRunning) {
				mSec++;
				if (mSec > 59) {
					mSec = 0;
					mMin ++;
				}
				if (mMin > 59 ) {
					mMin = 0;
					mHour ++;
				}

				timeHandler.sendEmptyMessage(0);
			}
		}
	};
	
	/**
	 * handler 4 refresh timetext;
	 */
	@SuppressLint("HandlerLeak")
	Handler timeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if (mSec < 10) {
				mStrSec  = "0"+mSec;
			}else {
				mStrSec = ""+mSec;
			}
			if (mMin < 10) {
				mStrMin = "0"+mMin;
			}else {
				mStrMin = "" + mMin;
			}
			mTimeText.setText(mStrMin+":"+mStrSec);
		};
	};
}
