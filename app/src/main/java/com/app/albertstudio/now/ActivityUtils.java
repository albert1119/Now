package com.app.albertstudio.now;


public final class ActivityUtils {

    public static final String APPTAG = "NOW";
    public static final String ACTION = "COM.APP.ALBERTSTUDIO.NOW";
    public static final String SHOW_QUERY_RESULT_BROADCAST = ACTION + ".SHOW_QUERY_RESULT_BROADCAST";
	public static final String SHOW_QUERY_REQUEST_BROADCAST = ACTION + ".SHOW_QUERY_REQUEST_BROADCAST";
	public static final String SHOW_RECORD_RESULT_BROADCAST = ACTION + ".SHOW_RECORD_RESULT_BROADCAST";
	public static final String RECORD_REQUEST_BROATCASD = ACTION + ".RECORD_REQUEST_BROADCAST";
	public static final String SET_DATA_BROATCASD = ACTION + ".SET_DATA_BROADCAST";
	public static final String QUERY_TYPE_HOME_ADDRESS = "HOME_ADDRESS";
	public static final String QUERY_TYPE_OFFICE_ADDRESS = "OFFICE_ADDRESS";
	public static final String RECORD_TYPE_LOCATION_ADDRESS = "RECORD_TYPE_LOCATION_ADDRESS";
	public static final String RECORD_TYPE_ACTIVITY_RECOGNITION = "RECORD_TYPE_ACTIVITY_RECOGNITION";
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Constants used to establish the activity update interval
    public static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int DETECTION_INTERVAL_SECONDS = 60;

    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 60;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 30;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();
    

 // Used to track what type of geofence removal request was made.
    public enum REMOVE_TYPE {INTENT, LIST}

    // Used to track what type of request is in process
    public enum REQUEST_TYPE {ADD, REMOVE}

    
 // Intent actions
    public static final String ACTION_CONNECTION_ERROR = ACTION + ".ACTION_CONNECTION_ERROR";

    public static final String ACTION_CONNECTION_SUCCESS = ACTION + ".ACTION_CONNECTION_SUCCESS";

    public static final String ACTION_GEOFENCES_ADDED = ACTION + ".ACTION_GEOFENCES_ADDED";

    public static final String ACTION_GEOFENCES_REMOVED = ACTION + ".ACTION_GEOFENCES_DELETED";

    public static final String ACTION_GEOFENCE_ERROR = ACTION + ".ACTION_GEOFENCES_ERROR";

    public static final String ACTION_GEOFENCE_TRANSITION = ACTION + ".ACTION_GEOFENCE_TRANSITION";

    public static final String ACTION_GEOFENCE_TRANSITION_ERROR = ACTION + ".ACTION_GEOFENCE_TRANSITION_ERROR";

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String RECEIVER = ACTION + ".RECEIVER";

    public static final String RESULT_DATA_KEY = ACTION + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = ACTION + ".LOCATION_DATA_EXTRA";


}
