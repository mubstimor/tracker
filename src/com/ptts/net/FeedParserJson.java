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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class parses generic Atom feeds.
 *
 * <p>Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 *
 * <p>An example of an Atom feed can be found at:
 * http://en.wikipedia.org/w/index.php?title=Atom_(standard)&oldid=560239173#Example_of_an_Atom_1.0_feed
 */
public class FeedParserJson {
	
	 // Constants indicting JSON response nodes that we're interested in
	private static final String TAG_COUNT = "count";	
	private static final String TAG_JSON_ARRAY = "results";
	private static final String TAG_ROUTE_ID = "id";
	private static final String TAG_ROUTE_NAME = "route_name";
	private static final String TAG_ROUTE_START = "route_start";
	private static final String TAG_ROUTE_END = "route_end";
	
    JSONParser jsonParser = new JSONParser();
	private static final String FEED_URL = "http://smsme.info/android-sync/download.json";
  
    public List<Entry> parse(InputStream in)
            throws JSONException, IOException, ParseException {
        try {
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
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
                Entry entryObject = new Entry(entry.getString(TAG_ROUTE_ID), entry.getString(TAG_ROUTE_NAME), entry.getString(TAG_ROUTE_START), entry.getString(TAG_ROUTE_END));                           
 
                entries.add(entryObject);
            }
        }
        return entries;
    }

    /**
     * This class represents a single entry (post) in the XML feed.
     *
     * <p>It includes the data members "title," "link," and "summary."
     */
    public static class Entry {
        public final String id;
        public final String name;
        public final String start;
        public final String end;

        Entry(String id, String name, String start, String end) {
            this.id = id;
            this.name = name;
            this.start = start;
            this.end = end;
        }
    }
}
