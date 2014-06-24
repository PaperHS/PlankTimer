package com.planktimer.database;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.planktimer.utils.LogUtil;

import android.content.Context;

/**
 * manul of database
 * 
 * @author Peggy
 * @email  potm@163.com
 *
 * @date   2014年6月17日 上午10:50:02
 */

public class DatabaseMan {
	
	private final static String TAG = "DatabaseMan";
	private DatabaseHelper mHelper;
	private Context mContext;
	private Dao<Records, Integer> mRecordsDao;
	
	public DatabaseMan(Context context) {
		this.mContext = context;
		if (mHelper != null && mHelper.isOpen()) {
			mHelper.close();
		}
		this.mHelper = new DatabaseHelper(mContext);
		
		try {
			this.mRecordsDao = mHelper.getRecordsDao();
		} catch (SQLException e) {
			LogUtil.e(TAG+":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (mHelper != null && mHelper.isOpen()) {
			mHelper.close();
		}
		super.finalize();
	}
	
	public void addRecord(Records record) {
		try {
			mRecordsDao.create(record);
		} catch (SQLException e) {
			LogUtil.e(TAG+":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void delRecord(Records record){
		try {
			mRecordsDao.delete(record);
		} catch (SQLException e) {
			LogUtil.e(TAG+":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * get all records 
	 * @return
	 */
	public List<Records> getAllRecords(){
		List<Records> records = null;
		QueryBuilder<Records, Integer> builder = mRecordsDao.queryBuilder();
		
		try {
			builder.orderByRaw("recordtime DESC" );
			records = builder.query();
		} catch (SQLException e) {
			LogUtil.e(TAG+":"+e.getMessage());
			e.printStackTrace();
		}
		return records;
	}
	
	/**
	 * delete all database
	 */
	public void delAllRecords(){
		try {
			mRecordsDao.delete(mRecordsDao.queryForAll());
		} catch (SQLException e) {
			LogUtil.e(TAG+":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * query by Fuzzy TODO
	 */
	
}
