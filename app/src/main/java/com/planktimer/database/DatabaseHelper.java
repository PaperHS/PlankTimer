package com.planktimer.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.planktimer.utils.LogUtil;

/**
 * database helper
 * 
 * @author Peggy
 * @email  potm@163.com
 *
 * @date   2014年6月17日 上午10:08:03
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

	private Dao<Records, Integer> mRecordsDao = null;
	private static final String DB_NAME = "records.db";
	private static final int DB_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Records.class);
		} catch (java.sql.SQLException e) {
			LogUtil.e("[databasehelpr:]"+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVer,
			int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Records.class, true);
			onCreate(database, connectionSource);
		} catch (java.sql.SQLException e) {
			LogUtil.e("[databasehelpr:]"+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * To get the vcard data access object.
	 * 
	 * @return the dao of vcard
	 * @throws SQLException 
	 */
	public Dao<Records, Integer> getRecordsDao() throws SQLException {
		if (mRecordsDao == null) {
			mRecordsDao = getDao(Records.class);
		}
		return this.mRecordsDao;
	}
}
