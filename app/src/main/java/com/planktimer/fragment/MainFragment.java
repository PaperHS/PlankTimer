package com.planktimer.fragment;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hs.planktimer.PreferenceKey;
import com.hs.planktimer.R;
import com.planktimer.database.DatabaseMan;
import com.planktimer.database.Records;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import utils.DateUtil;
import utils.PreferenceUtils;

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
	private boolean mIsRunning = false;//是否正在计时
	private DatabaseMan mMan;
	private ArrayAdapter<String> mArrayAdapter;
	private ArrayList<String> mDataList = new ArrayList<String>();
	
	private StringBuffer mData = new StringBuffer();
	public static final int SPORTING = 1; 
	public static final int IDLE = 0; 
	
	private int mTotalTime;
	private int mTotalPlankTime;

	private int mTopTime;  //最长时间
	private int mLongestStreak;  //最长持续天数

	@InjectView(R.id.fragment_main_tv_timer) TextView mTimeText;
	@InjectView(R.id.fragment_main_listview_record) ListView mRecords;
	@InjectView(R.id.fragment_main_btn_start) Button mStartBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		mMan = new DatabaseMan(getActivity());
		mTopTime = PreferenceUtils.getPrefInt(getActivity(), PreferenceKey.TOP_TIME_PLANK, 0);
		mLongestStreak = PreferenceUtils.getPrefInt(getActivity(),
				PreferenceKey.LONGEST_TIME_STREAK, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_1, container, false);
		//init butterknife
		ButterKnife.inject(this,view);
		mArrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, mDataList);
		mRecords.setAdapter(mArrayAdapter);
		mTimeText.setText("00:00");
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
			if (mLastOrient) {
				mTotalPlankTime += mHour*3600 + mMin*60 +mSec; // 记录运动时间 单位s
			}
			mData.append(mHour*3600 + mMin*60 +mSec+";");
			mDataList.add(mTimeText.getText().toString());
			mArrayAdapter.notifyDataSetChanged();
			mMin = 0;
			mSec = 0;
			mTimeText.setText("00:00");
			if (mTotalPlankTime > mTopTime){
				PreferenceUtils.setPrefInt(getActivity(),PreferenceKey.TOP_TIME_PLANK, mTotalPlankTime);
				//TODO POP
			}
			if (mMan.getRecordsByDate(DateUtil.getDateSomeday(-1)).size() < 1){
				PreferenceUtils.setPrefInt(getActivity(),PreferenceKey.LONGEST_TIME_STREAK,1);
			}else{
				if (mMan.getRecordsByDate(DateUtil.getDateSomeday(-1)).size() == 1){
					PreferenceUtils.setPrefInt(getActivity(),
						PreferenceKey.LONGEST_TIME_STREAK,++mLongestStreak);
				}
			}

			Records record = new Records();
			record.setRecordTime(DateUtil.getCurrentTime());
			record.setRecordData(mData.toString());
			record.setTotalplanktime(mTotalPlankTime);
			record.setTotaltime(mTotalTime);
			record.setRecordDate(DateUtil.getDateSomeday(0));
			mMan.addRecord(record);
			mStartBtn.setText("开始");
		}else{
			mStartBtn.setText("结束");
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
             if (z > 5) {
            	 mGoUp = true;
             }else if (z < -5) {
				mGoUp = false;
             } 
             
             if (mGoUp != mLastOrient && mIsRunning) {
            	 //record
 				if (mLastOrient) {
					mData.append(mHour*3600 + mMin*60 +mSec+";");
					mTotalPlankTime += mHour*3600 + mMin*60 +mSec; // 记录运动时间 单位s
 				}else {
					mData.append(mHour * 3600 + mMin * 60 + mSec + ";");
				}
 				 mTotalTime += mHour*3600 + mMin*60 +mSec;	// 记录总时间
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
	Handler timeHandler = new Handler(new Handler.Callback(){
		@Override
		public boolean handleMessage(Message message) {
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
			return false;
		}
	});
}
