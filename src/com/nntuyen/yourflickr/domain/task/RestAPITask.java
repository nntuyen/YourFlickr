package com.nntuyen.yourflickr.domain.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

@SuppressWarnings("deprecation")
public class RestAPITask extends AsyncTask<HttpUriRequest, Void, String> {

	private static final String TAG = "RestAPITask";
    public static final String HTTP_RESPONSE = "httpResponse";
 
    private Context mContext;
    private HttpClient mClient;
    private String mAction;
    
    public RestAPITask(Context context, String action)
    {
        mContext = context;
        mAction = action;
        mClient = new DefaultHttpClient();
    }
    
    public RestAPITask(Context context, String action, HttpClient client)
    {
        mContext = context;
        mAction = action;
        mClient = client;
    }

	@Override
	protected String doInBackground(HttpUriRequest... params) {
		try
        {
            HttpUriRequest request = params[0];
            HttpResponse serverResponse = mClient.execute(request);
            BasicResponseHandler handler = new BasicResponseHandler();
            return handler.handleResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
    protected void onPostExecute(String result)
    {
        Log.d(TAG, "RESULT = " + result);
        Intent intent = new Intent(mAction);
        intent.putExtra(HTTP_RESPONSE, result);
 
        // Broadcast the completion
        mContext.sendBroadcast(intent);
    }
	
}
