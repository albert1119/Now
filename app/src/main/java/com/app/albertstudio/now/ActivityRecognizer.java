package com.app.albertstudio.now;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class ActivityRecognizer implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
	// Stores the PendingIntent used to send activity recognition events back to the app
    private PendingIntent mActivityRecognitionPendingIntent;

	/**
	 * Provides the entry point to Google Play services.
	 */
	protected GoogleApiClient mGoogleApiClient;

	/**
	 * Stores parameters for requests to the FusedLocationProviderApi.
	 */
	protected LocationRequest mLocationRequest;

	/**
	 * Represents a geographical location.
	 */
	protected Location mCurrentLocation;


	private Context mContext;
    
	public ActivityRecognizer(Context context)
	{
		//android.os.Debug.waitForDebugger();
    	
    	mContext =  context;
		mCurrentLocation = null;

        buildGoogleApiClient();
		createLocationRequest();
	}

	/**
	 * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
	 * ActivityRecognition API.
	 */
	protected synchronized void buildGoogleApiClient()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(mContext)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(ActivityRecognition.API)
				.addApi(LocationServices.API)
				.build();
	}

	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */
	@Override
	public void onConnected(Bundle connectionHint)
	{
		Log.i(ActivityUtils.APPTAG, "Connected to GoogleApiClient");
		if (mGoogleApiClient.isConnected())
		{
			Intent intent = new Intent(mContext, ActivityRecognitionIntentService.class);
			mActivityRecognitionPendingIntent = PendingIntent.getService(mContext, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, ActivityUtils.DETECTION_INTERVAL_MILLISECONDS, mActivityRecognitionPendingIntent);
			startLocationUpdates();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result)
	{
		// Refer to the javadoc for ConnectionResult to see what error codes might be returned in
		// onConnectionFailed.
		Log.i(ActivityUtils.APPTAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause)
	{
		// The connection to Google Play services was lost for some reason. We call connect() to
		// attempt to re-establish the connection.
		Log.i(ActivityUtils.APPTAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		mCurrentLocation = location;
		startAddressService();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();

		// Sets the desired interval for active location updates. This interval is
		// inexact. You may not receive updates at all if no location sources are available, or
		// you may receive them slower than requested. You may also receive updates faster than
		// requested if other applications are requesting location at a faster interval.
		mLocationRequest.setInterval(ActivityUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Sets the fastest rate for active location updates. This interval is exact, and your
		// application will never receive updates faster than this value.
		mLocationRequest.setFastestInterval(ActivityUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	}

	public void Disconnect()
	{
		// If the client is connected
        if(mGoogleApiClient.isConnected())
        {
			ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, mActivityRecognitionPendingIntent);
			stopLocationUpdates();
			mGoogleApiClient.disconnect();
	    }
	}
	
	public void Connect()
	{
		if (!mGoogleApiClient.isConnected())
		{
			mGoogleApiClient.connect();
		}
	}

	/**
	 * Requests location updates from the FusedLocationApi.
	 */
	public void startLocationUpdates()
	{
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}


	/**
	 * Removes location updates from the FusedLocationApi.
	 */
	public void stopLocationUpdates()
	{
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	/**
	 * Invoked by the "Get Location" button.
	 *
	 * Calls getLastLocation() to get the current location
	 *
	 */
	public Location getLocation()
	{
		return mCurrentLocation;
	}

	protected void startAddressService() {
		// Create an intent for passing to the intent service responsible for fetching the address.
		Intent intent = new Intent(mContext, FetchAddressIntentService.class);
		// Pass the location data as an extra to the service.
		intent.putExtra(ActivityUtils.LOCATION_DATA_EXTRA, getLocation());
		mContext.startService(intent);
	}
}
