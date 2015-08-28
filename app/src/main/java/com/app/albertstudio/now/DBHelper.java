package com.app.albertstudio.now;

import static com.app.albertstudio.now.DbConstants.TABLE_USERRECORDDATA;
import static com.app.albertstudio.now.DbConstants.TABLE_HOMETIME;
import static com.app.albertstudio.now.DbConstants.TABLE_OFFICETIME;

import static android.provider.BaseColumns._ID;
import static com.app.albertstudio.now.DbConstants.GROUP_LAT;
import static com.app.albertstudio.now.DbConstants.GROUP_LON;
import static com.app.albertstudio.now.DbConstants.LAT;
import static com.app.albertstudio.now.DbConstants.LON;
import static com.app.albertstudio.now.DbConstants.ADDRESS;
import static com.app.albertstudio.now.DbConstants.REC_HOUR;
import static com.app.albertstudio.now.DbConstants.REC_MIN;
import static com.app.albertstudio.now.DbConstants.REC_DATE;
import static com.app.albertstudio.now.DbConstants.IS_SCREENON;
import static com.app.albertstudio.now.DbConstants.GRAVITY_X;
import static com.app.albertstudio.now.DbConstants.GRAVITY_Y;
import static com.app.albertstudio.now.DbConstants.GRAVITY_Z;
import static com.app.albertstudio.now.DbConstants.ACTIVITY_RECOGNITION;
import static com.app.albertstudio.now.DbConstants.DAY_OF_WEEK;
import static com.app.albertstudio.now.DbConstants.TIME_TYPE;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

public class DBHelper extends AsyncTask<JSONObject, JSONObject, Boolean>
{

	String url = "http://www.albert-studio.com/RecordData.php";

	@Override
	protected Boolean doInBackground(JSONObject... data)
	{
		try
		{
			JSONObject json = data[0];

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(json.names().length());
			for(int i=0;i<json.names().length();i++)
			{
				nameValuePairs.add(new BasicNameValuePair(json.names().getString(i), json.getString(json.names().getString(i))));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			httpclient.execute(httppost);
			return true;
		}
		catch (Throwable t)
		{
			return false;
		}
	}

}