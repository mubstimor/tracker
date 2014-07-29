/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ptts.net;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class parses the received JSON Data.
  *
 */
public class FeedParserJson {
	
	 // Constants indicating JSON response nodes that we're interested in
	private static final String TAG_COUNT = "count";	
	private static final String TAG_JSON_ARRAY = "results";
	private static final String TAG_ROUTE_ID = "id";
	private static final String TAG_ROUTE_NAME = "route_name";
	private static final String TAG_ROUTE_START = "route_start";
	private static final String TAG_ROUTE_END = "route_end";
	private static final String TAG_STOPS_ARRAY = "stops";
	private static final String TAG_ROUTE_STOP = "stop_name";
	
    JSONParser jsonParser = new JSONParser();
	private static final String FEED_URL = "http://ptts.herokuapp.com/dstops";
  
    public List<Entry> parse(InputStream in)
            throws JSONException, IOException, ParseException {
        try {
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("format", "json"));
            
			JSONObject json = jsonParser.makeHttpRequest(FEED_URL, "GET", params);
			
			Log.i("JSON RESPONSE", json.toString());

            return readFeed(json);
        } finally {
            in.close();
        }
    }

  
    private List<Entry> readFeed(JSONObject jsonObject)
            throws JSONException, IOException, ParseException {
        List<Entry> entries = new ArrayList<Entry>();

        if (Integer.parseInt(jsonObject.getString(TAG_COUNT)) > 0) {
            JSONArray posts = jsonObject.getJSONArray(TAG_JSON_ARRAY);
          
 
            for (int i = 0; i < posts.length(); i++) {
                JSONObject entry = (JSONObject) posts.getJSONObject(i);
                String routeString = "";
                
                JSONArray routeStops = entry.getJSONArray(TAG_STOPS_ARRAY);
                for (int j = 0; j < routeStops.length(); j++){
                    JSONObject routeStop = routeStops.getJSONObject(j);  
                    if(j != (routeStops.length()-1)){
                    	routeString += routeStop.getString(TAG_ROUTE_STOP)+",";                    	
                    }else{
                    routeString += routeStop.getString(TAG_ROUTE_STOP);
                    }
                }
                
                Log.i("FOUND STOP", routeString);
                
                Entry entryObject = new Entry(entry.getString(TAG_ROUTE_ID), entry.getString(TAG_ROUTE_NAME), entry.getString(TAG_ROUTE_START), entry.getString(TAG_ROUTE_END),routeString);                           
 
                entries.add(entryObject);
            }
        }
        return entries;
    }

 
    public static class Entry {
        public final String id;
        public final String name;
        public final String start;
        public final String end;
        public final String stops;

        Entry(String id, String name, String start, String end, String stops) {
            this.id = id;
            this.name = name;
            this.start = start;
            this.end = end;
            this.stops = stops;
        }
    }
}
