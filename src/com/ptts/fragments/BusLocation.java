package com.ptts.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ptts.R;
import com.ptts.library.ConnectionDetector;
import com.ptts.library.DirectionsJSONParser;
import com.ptts.library.FetchBusTask;

public class BusLocation extends SherlockFragmentActivity implements LocationListener, LocationSource{

    GoogleMap mGoogleMap;
    double mLatitude=0;
    double mLongitude=0;
    LocationManager locationManager;
    TextView txtBusId,txtBusTime;
    String bus_id;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showProgressBar();        

        txtBusId = (TextView)findViewById(R.id.bus_plate);
		txtBusTime = (TextView)findViewById(R.id.time_label);
						
        Intent in = getIntent();
        cd = new ConnectionDetector(getApplicationContext());

        bus_id = in.getStringExtra(FetchBusTask.getKeyBusid());        
        String latitude = in.getStringExtra(FetchBusTask.getKeyLatitude());
        String longitude = in.getStringExtra(FetchBusTask.getKeyLongitude());
        
        bus_id = bus_id.substring(6).replace("/", "");
        
        txtBusId.setText("Bus "+bus_id);
		//txtBusTime.setText(latitude);
        if (!cd.isConnectingToInternet()){	            
            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
            return;
        }else{
        	//to get bus plate number
        	ServerConnection connect=new ServerConnection();
    		connect.execute(new String[]{bus_id});

        }
        
        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Google Map
            mGoogleMap = fragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            if(provider != null){

                // Getting Current Location From GPS
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            }

        }//end else

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude) ,Double.parseDouble(longitude))).title(bus_id).snippet(latitude));
        
        //find the distance between the two
    	LatLng origin =new LatLng(mLatitude, mLongitude);
		LatLng dest = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);				
		
		DownloadTask downloadTask = new DownloadTask();
		
		// Start downloading json data from Google Directions API
		downloadTask.execute(url);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            }
            else
            {
                //Show an error dialog that GPS is disabled...
               Log.i("NO GPS", "GPS is off");
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        // mLatitude, mLongitude

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void activate(OnLocationChangedListener arg0) {}

    @Override
    public void deactivate() {}
    
	private String getDirectionsUrl(LatLng origin,LatLng dest){
		
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			String distance = "";
			String duration = "";
			
			
			
			if(result.size()<1){
				Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
				
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	
					
					if(j==0){	// Get distance from the list
						distance = (String)point.get("distance");						
						continue;
					}else if(j==1){ // Get duration from the list
						duration = (String)point.get("duration");
						continue;
					}
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);	
				
			}
			
			txtBusTime.setText("Distance:"+distance + ", Duration:"+duration);
			
			// Drawing polyline in the Google Map for the i-th route
			mGoogleMap.addPolyline(lineOptions);							
		}			
    }   

    private class ServerConnection extends AsyncTask<String,Void,String>{
		String result=null;		
		String url="http://ptts.herokuapp.com/buses/"+bus_id+"/?format=json";//URL HERE
		InputStream is=null;
		int TIMEOUT_MILLISEC=50000;
		@Override
		protected String doInBackground(String... params) {
			
			try {				
				HttpParams httpParams = new BasicHttpParams();			
				HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
				HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				HttpClient client = new DefaultHttpClient(httpParams);
				JSONObject json=new JSONObject();
				HttpGet request = new HttpGet(url);
				request.setHeader("json", json.toString());
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();

				is = entity.getContent();
			}catch (ClientProtocolException e) {
				result="No Internet Connection";
			} catch (IOException e) {
				Log.v("Error while parsing the response","true");

			}

			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder builder = new StringBuilder();
				String line=null;
				while ((line = reader.readLine()) != null) {
					builder.append(line + "\n");
				}
				is.close();
				result=builder.toString();
				Log.v("Server response", result);
			}catch(Exception i){

			}


			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result !=null){
				makeProgressBarDisappear();
					try {
					JSONArray jsonResponse=new JSONArray(result);
					JSONObject	jobject = jsonResponse.getJSONObject(0);
					String busPlate=jobject.getString("license_number");
					txtBusId.setText(busPlate);
					Toast.makeText(getApplicationContext(), "License "+busPlate, Toast.LENGTH_LONG).show();
										
					} catch (JSONException e) {
						e.printStackTrace();
					}

				
			}else{
				Toast.makeText(getApplicationContext(), "Bus not found", Toast.LENGTH_SHORT).show();
			}

		}
	}
    
    @Override
	   protected void onResume() 
	   {
	       super.onResume();
	       getSupportActionBar().show();  
	       
	   }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
	 private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }
	 
	 private void showProgressBar() {
	        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.VISIBLE);
	    }
}

