package com.ptts.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ptts.R;
import com.ptts.library.BusAdapter;
import com.ptts.library.BusListScreen;
import com.ptts.library.ConnectionDetector;
import com.ptts.library.FetchBusTask;

public class RouteDetails extends Activity implements BusListScreen {

	FetchBusTask fetchBusesTask = new FetchBusTask();
	TextView txtRouteId,txtRouteName,txtRouteStops;
	String route_id, route_name, route_stops;
	ConnectionDetector cd; 		// Internet detector
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_details);
		
		cd = new ConnectionDetector(getApplicationContext());
		txtRouteId = (TextView)findViewById(R.id.route_id);
		txtRouteName = (TextView)findViewById(R.id.route_name);
		txtRouteStops = (TextView)findViewById(R.id.route_stops);
		
		Intent intent = getIntent();
		route_id = intent.getStringExtra("route_id");
		route_name= intent.getStringExtra("route_name");
		route_stops = intent.getStringExtra("route_stops");
		
		if(route_stops.length()==0){
			route_stops = "None registered so far";
		}
		
		txtRouteId.setText("Route :");
		txtRouteName.setText(route_name);
		txtRouteStops.setText(route_stops);

	}
	
	@Override
	public void displayList(final ArrayList<HashMap<String, String>> busItems) {		
		BusAdapter adapter = new BusAdapter(this, busItems);
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        makeProgressBarDisappear();		
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {							
				Toast.makeText(getApplicationContext(), "clicked list item "+busItems.get(position).get(FetchBusTask.getKeyBusid()), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), BusLocation.class);
		        i.putExtra("bus_id", busItems.get(position).get(FetchBusTask.getKeyBusid()));
		        i.putExtra("latitude", busItems.get(position).get(FetchBusTask.getKeyLatitude()));
		        i.putExtra("longitude",busItems.get(position).get(FetchBusTask.getKeyLongitude()));
		        startActivity(i);
 
			}
		});	
	}
	
	   private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }
	   
	   @Override
	   protected void onResume() 
	   {
	       super.onResume();
	       if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }
	   }
	   
	   @Override
	    public void onStart() {
	        super.onStart();
	        if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{
	        	//to get buses along a route
	      		fetchBusesTask.listScreen = this;
	      		fetchBusesTask.execute("http://ptts.herokuapp.com/getbuslocations/"+route_id.trim()+"/?format=json");
	        }
	   }
	   
}
