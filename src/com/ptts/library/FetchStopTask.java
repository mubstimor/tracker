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

public class FetchStopTask extends AsyncTask<String, Void, JSONArray> {

    public ItemListScreen listScreen = null;   
	private static final String TAG_JSON_ARRAY = "results";
	private static final String TAG_ROUTE_ID = "id";
	private static final String TAG_ROUTE_NAME = "route";	
	private static final String TAG_STOPS_ARRAY = "stops";
	private static final String TAG_ROUTE_STOP = "stop_name";
	private static final String TAG_STOP_ID = "id";
	private static final String TAG_ROUTE_STOP_LAT = "latitude";
	private static final String TAG_ROUTE_STOP_LNG = "longitude";
	JSONArray stops = null;

    @Override
    protected JSONArray doInBackground(String... urls) {

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        String responseBody = "{}";
        try {
            HttpResponse response = client.execute(get);
            responseBody = getResponseBody(response);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
        	
//           	JSONObject json = new JSONObject(responseBody);
        	Log.i("RESPONSE", responseBody);
           	stops = new JSONArray(responseBody);           	
            return stops;           
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(NullPointerException e){        	
        	e.printStackTrace();        	
        	Log.i("error", "can't process data");
        }
        catch(Throwable t){
        	t.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray todoJsonArray) {
        //List<BusItem> routeItems = new ArrayList<BusItem>();
        ArrayList<HashMap<String, String>> stopsList = new ArrayList<HashMap<String, String>>();
       
        try{        	
        
        for (int i = 0; i < todoJsonArray.length(); i++) {
            try {
            	HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = todoJsonArray.getJSONObject(i);
                map.put(getTagRouteId(), jsonObject.getString(getTagRouteId()));
                map.put(getTagRouteName(), jsonObject.getString(getTagRouteName()));                           
                map.put(getTagRouteStop(), jsonObject.getString(getTagRouteStop()));
                map.put(getTagRouteStopLat(), jsonObject.getString(getTagRouteStopLat()));
                map.put(getTagRouteStopLng(), jsonObject.getString(getTagRouteStopLng()));
                
             // adding HashList to ArrayList
    			stopsList.add(map);
    			
                Log.i("FETCHING DATA", jsonObject.getString(getTagStopId()));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(NullPointerException e){        	
            	e.printStackTrace();        	
            	Log.i("error", "can't process data");
            }
        }
        
 
        }catch(NullPointerException e){        	
        	e.printStackTrace();        	
        	Log.i("error", "can't process data");
        }
        
        listScreen.displayList(stopsList);
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

	public ItemListScreen getListScreen() {
		return listScreen;
	}

	public void setListScreen(ItemListScreen listScreen) {
		this.listScreen = listScreen;
	}

	public JSONArray getDrugs() {
		return stops;
	}

	public void setDrugs(JSONArray stops) {
		this.stops = stops;
	}

	public static String getTagRouteId() {
		return TAG_ROUTE_ID;
	}

	public static String getTagRouteName() {
		return TAG_ROUTE_NAME;
	}

	public static String getTagStopsArray() {
		return TAG_STOPS_ARRAY;
	}

	public static String getTagRouteStop() {
		return TAG_ROUTE_STOP;
	}

	public static String getTagStopId() {
		return TAG_STOP_ID;
	}

	public static String getTagRouteStopLat() {
		return TAG_ROUTE_STOP_LAT;
	}

	public static String getTagRouteStopLng() {
		return TAG_ROUTE_STOP_LNG;
	}

	public static String getTagJsonArray() {
		return TAG_JSON_ARRAY;
	}

}