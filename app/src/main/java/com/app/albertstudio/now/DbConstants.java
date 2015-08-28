package com.app.albertstudio.now;

import android.provider.BaseColumns;

public interface DbConstants extends BaseColumns 
{
	public static final String TABLE_USERRECORDDATA = "RecordData";
	public static final String TABLE_HOMETIME = "HomeTime";
	public static final String TABLE_OFFICETIME = "OfficeTime";

	public static final String UID = "UID";
	public static final String GROUP_LAT = "GROUP_LAT";
	public static final String GROUP_LON = "GROUP_LON";
	public static final String LAT = "LAT";
	public static final String LON = "LON";
	public static final String ADDRESS = "ADDRESS";
	public static final String REC_HOUR = "REC_HOUR";
	public static final String REC_MIN = "REC_MIN";
	public static final String REC_DATE = "REC_DATE";
	public static final String IS_SCREENON = "IS_SCREENON";
	public static final String GRAVITY_X = "GRAVITY_X";
	public static final String GRAVITY_Y = "GRAVITY_Y";
	public static final String GRAVITY_Z = "GRAVITY_Z";
	public static final String ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION";
	public static final String DAY_OF_WEEK = "DAY_OF_WEEK";
	
	public static final String TIME_TYPE = "TIME_TYPE";
	
}