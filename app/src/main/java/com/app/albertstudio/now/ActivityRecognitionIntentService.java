package com.app.albertstudio.now;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;



/**
 * Service that receives ActivityRecognition updates. It receives updates
 * in the background, even if the main Activity is not visible.
 */
public class ActivityRecognitionIntentService extends IntentService 
{
	public ActivityRecognitionIntentService() {
        // Set the label for the service's background thread
        super("ActivityRecognitionIntentService");
        
    }

    /**
     * Called when a new activity detection update is available.
     */
    @Override
    protected void onHandleIntent(Intent intent) 
    {
        // If the intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) 
        {
            // Get the update
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            // Get the most probable activity from the list of activities in the update
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            // Get the confidence percentage for the most probable activity
            int confidence = mostProbableActivity.getConfidence();

            // Get the type of activity
            int activityType = mostProbableActivity.getType();
            
            Intent i = new Intent(ActivityUtils.SET_DATA_BROATCASD);
			i.putExtra("Type", ActivityUtils.RECORD_TYPE_ACTIVITY_RECOGNITION);
			i.putExtra("Query", getNameFromType(activityType));
			sendBroadcast(i);
        }
    }

    /**
     * Determine if an activity means that the user is moving.
     *
     * @param type The type of activity the user is doing (see DetectedActivity constants)
     * @return true if the user seems to be moving from one location to another, otherwise false
     */
    private boolean isMoving(int type) 
    {
        switch (type) {
            // These types mean that the user is probably not moving
            case DetectedActivity.STILL :
            case DetectedActivity.TILTING :
            case DetectedActivity.UNKNOWN :
                return false;
            default:
                return true;
        }
    }

    /**
     * Map detected activity types to strings
     *
     * @param activityType The detected activity type
     * @return A user-readable name for the type
     */
    private String getNameFromType(int activityType) 
    {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.UNKNOWN:
                return "UNKNOWN";
            case DetectedActivity.TILTING:
                return "TILTING";
        }
        return "unknown";
    }
}
