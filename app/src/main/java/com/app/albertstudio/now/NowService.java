package com.app.albertstudio.now;

import static android.provider.BaseColumns._ID;
import static com.app.albertstudio.now.DbConstants.UID;
import static com.app.albertstudio.now.DbConstants.GROUP_LAT;
import static com.app.albertstudio.now.DbConstants.GROUP_LON;
import static com.app.albertstudio.now.DbConstants.LAT;
import static com.app.albertstudio.now.DbConstants.LON;
import static com.app.albertstudio.now.DbConstants.ADDRESS;
import static com.app.albertstudio.now.DbConstants.REC_HOUR;
import static com.app.albertstudio.now.DbConstants.REC_MIN;
import static com.app.albertstudio.now.DbConstants.IS_SCREENON;
import static com.app.albertstudio.now.DbConstants.REC_DATE;
import static com.app.albertstudio.now.DbConstants.GRAVITY_X;
import static com.app.albertstudio.now.DbConstants.GRAVITY_Y;
import static com.app.albertstudio.now.DbConstants.GRAVITY_Z;
import static com.app.albertstudio.now.DbConstants.ACTIVITY_RECOGNITION;
import static com.app.albertstudio.now.DbConstants.DAY_OF_WEEK;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import org.json.JSONException;
import org.json.JSONObject;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.location.Location;
import java.util.Date;
import java.util.Calendar;


/**
 * Created by albert on 15/8/26.
 */
public class NowService  extends Service  implements SensorEventListener
{
    private SensorManager mSensorManager;

    private Sensor mSensor;

    private boolean mIsScreenOn;

    private float mGravity[] = new float[3];

    private String mPreviousActivity;

    private String mPreviousAddress;

    private ActivityRecognizer mActivityRecognizer;

    private int mTickCounter;

    private String mStartDate;

    private BroadcastReceiver mQueryRequestReceiver =  new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if(ActivityUtils.RECORD_REQUEST_BROATCASD.equals(intent.getAction()))
            {

                Location l = mActivityRecognizer.getLocation();
                double lat = l.getLatitude();
                double lon = l.getLongitude();
                String Group_Lat = String.format("%.03f", lat);
                String Group_Lon = String.format("%.03f", lon);
                Calendar c = Calendar.getInstance();
                int Hour = c.get(Calendar.HOUR_OF_DAY);
                int Min = c.get(Calendar.MINUTE);
                int DayofWeek = c.get(Calendar.DAY_OF_WEEK);

                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                try
                {
                    JSONObject toSend = new JSONObject();
                    toSend.put(UID, "albert1119@gmail.com");
                    toSend.put(GROUP_LAT, Group_Lat);
                    toSend.put(GROUP_LON, Group_Lon);
                    toSend.put(LAT, String.valueOf(lat));
                    toSend.put(LON, String.valueOf(lon));
                    toSend.put(REC_HOUR, Hour);
                    toSend.put(REC_MIN, Min);
                    toSend.put(REC_DATE, date);
                    toSend.put(IS_SCREENON, mIsScreenOn);
                    toSend.put(GRAVITY_X, mGravity[0]);
                    toSend.put(GRAVITY_Y, mGravity[1]);
                    toSend.put(GRAVITY_Z, mGravity[2]);
                    toSend.put(ACTIVITY_RECOGNITION, mPreviousActivity);
                    toSend.put(DAY_OF_WEEK, DayofWeek);
                    toSend.put(ADDRESS, mPreviousAddress);

                    DBHelper dbHelper = new DBHelper();
                    dbHelper.execute(new JSONObject[]{toSend});

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            else if(ActivityUtils.SET_DATA_BROATCASD.equals(intent.getAction()))
            {
                String type = intent.getStringExtra("Type");
                if(type.equals(ActivityUtils.RECORD_TYPE_ACTIVITY_RECOGNITION) == true)
                {
                    mPreviousActivity = intent.getStringExtra("Query");
                    //Toast.makeText(getApplicationContext(), "Activity : " + mPreviousActivity, Toast.LENGTH_SHORT).show();
                }
                else if(type.equals(ActivityUtils.RECORD_TYPE_LOCATION_ADDRESS) == true)
                {
                    mPreviousAddress = intent.getStringExtra("Query");
                    //Toast.makeText(getApplicationContext(), "Address : " + mPreviousAddress, Toast.LENGTH_SHORT).show();
                }
            }
            else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction()))
            {
                mIsScreenOn = true;
            }
            else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction()))
            {
                mIsScreenOn = false;
                mPreviousActivity = "UNKNOWN";
            }
            else if(ActivityUtils.ACTION_GEOFENCES_ADDED.equals(intent.getAction()))
            {

            }
            else if(ActivityUtils.ACTION_GEOFENCES_REMOVED.equals(intent.getAction()))
            {

            }
            else if(ActivityUtils.ACTION_GEOFENCE_ERROR.equals(intent.getAction()))
            {

            }
            else if(ActivityUtils.SHOW_QUERY_RESULT_BROADCAST.equals(intent.getAction()))
            {

            }
            else if(Intent.ACTION_TIME_TICK.equals(intent.getAction()))
            {
                /*
                mTickCounter++;
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                if(mStartDate.equals(date) == false)
                {
                    Intent i = new Intent(ActivityUtils.SHOW_QUERY_REQUEST_BROADCAST);
                    i.putExtra("Type", ActivityUtils.QUERY_TYPE_HOME_ADDRESS);
                    i.putExtra("Query", "");
                    sendBroadcast(i);

                    Intent j = new Intent(ActivityUtils.SHOW_QUERY_REQUEST_BROADCAST);
                    j.putExtra("Type", ActivityUtils.QUERY_TYPE_OFFICE_ADDRESS);
                    j.putExtra("Query", "");
                    sendBroadcast(j);

                    mStartDate = date;

                    Toast.makeText(getApplicationContext(), "Set Home & Office", Toast.LENGTH_SHORT).show();
                }
                */
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        // do something when the service is created
        //android.os.Debug.waitForDebugger();
        mTickCounter = 0;
        mStartDate = "";



        mActivityRecognizer = new ActivityRecognizer(getApplicationContext());

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager.isScreenOn())
        {
            mIsScreenOn = true;
        }
        else
        {
            mIsScreenOn = false;
        }

        mPreviousActivity = "UNKNOWN";
        mPreviousAddress = "";
        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.SHOW_QUERY_RESULT_BROADCAST));

        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.RECORD_REQUEST_BROATCASD));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.SET_DATA_BROATCASD));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.ACTION_GEOFENCES_ADDED));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.ACTION_GEOFENCES_REMOVED));
        registerReceiver(mQueryRequestReceiver, new IntentFilter(ActivityUtils.ACTION_GEOFENCE_ERROR));

        PollingUtils.startSendBoardcast(this, 5000, 1200000, ActivityUtils.RECORD_REQUEST_BROATCASD, ActivityUtils.RECORD_TYPE_LOCATION_ADDRESS);

        Toast.makeText(getApplicationContext(), "NowService OnCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //android.os.Debug.waitForDebugger();
        mActivityRecognizer.Connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // If the client is connected
        mActivityRecognizer.Disconnect();
        unregisterReceiver(mQueryRequestReceiver);
        mSensorManager.unregisterListener(this);
        PollingUtils.stopSendBoardcast(this, ActivityUtils.RECORD_REQUEST_BROATCASD, ActivityUtils.RECORD_TYPE_LOCATION_ADDRESS);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // TODO Auto-generated method stub
        mGravity[0] = event.values[0];
        mGravity[1] = event.values[1];
        mGravity[2] = event.values[2];
    }
}
