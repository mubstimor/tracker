package com.ptts.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class FetchBusTask extends AsyncTask<String, Void, JSONArray> {

    public BusListScreen listScreen = null;
    private static final String KEY_ID = "num";
	private static final String KEY_BUSID = "bus_id";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE= "longitude";
	static final String KEY_ROUTEID= "route_id";

    @Override
    protected JSONArray doInBackground(String... urls) {

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        String responseBody = "{}";
        try {
            HttpResponse response = client.execute(get);
            responseBody = getResponseBody(response);
        }
        catch(NullPointerException e){
        	e.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            return new JSONArray(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(Throwable t){
        	t.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray todoJsonArray) {
        //List<BusItem> routeItems = new ArrayList<BusItem>();
        ArrayList<HashMap<String, String>> busList = new ArrayList<HashMap<String, String>>();
       
        try{
    	   
       
        for (int i = 0; i < todoJsonArray.length(); i++) {
            try {
            	HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = todoJsonArray.getJSONObject(i);
                map.put(getKeyId(), (i+1)+"");
                map.put(getKeyBusid(), jsonObject.getString(getKeyBusid()));
                map.put(getKeyLatitude(), jsonObject.getString(getKeyLatitude()));
                map.put(getKeyLongitude(), jsonObject.getString(getKeyLongitude()));
                map.put(KEY_ROUTEID, jsonObject.getString(KEY_ROUTEID));
                
             // adding HashList to ArrayList
    			busList.add(map);
    			
          //      routeItems.add(new BusItem(jsonObject.getString("bus_id"),jsonObject.getString("latitude"),jsonObject.getString("longitude")));
                Log.i("FETCHING DATA", jsonObject.getString("bus_id"));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        }catch(NullPointerException e){        	
        	e.printStackTrace();        	
        	Log.i("error", "can't process data");
        }
        
        listScreen.displayList(busList);
    }

    private String getResponseBody(HttpResponse response) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream body = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
	public static String getKeyId() {
		return KEY_ID;
	}

	public static String getKeyBusid() {
		return KEY_BUSID;
	}

	public static String getKeyLatitude() {
		return KEY_LATITUDE;
	}

	public static String getKeyLongitude() {
		return KEY_LONGITUDE;
	}
}