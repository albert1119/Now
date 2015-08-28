package com.app.albertstudio.now;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class PollingUtils
{

	public static void startPollingService(Context context, int seconds, Class<?> cls,String action)
	{
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, cls);
		intent.setAction(action);   
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		long triggerAtTime = 5000;//SystemClock.elapsedRealtime();
		manager.setRepeating(AlarmManager.RTC, triggerAtTime, seconds * 1000, pendingIntent);
	}

	public static void stopPollingService(Context context, Class<?> cls,String action)
	{
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static void startSendBoardcast(Context context, long triggerAtTime, long interval, String action, String type)
	{
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(action);
		intent.putExtra("Type", type);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, interval, pendingIntent);
	}
	
	public static void stopSendBoardcast(Context context, String action, String type)
	{
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(action);
		intent.putExtra("Type", type);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
