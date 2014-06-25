package com.ptts.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ptts.R;
import com.ptts.library.BusItem;
import com.ptts.library.BusListScreen;
import com.ptts.library.FetchBusTask;

public class RouteDetails extends Activity implements BusListScreen {

	FetchBusTask fetchBusesTask = new FetchBusTask();
	TextView txtRouteId,txtRouteName,txtRouteStops;
	String route_id, route_name, route_stops;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_details);
				
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
		
		//to get buses along a route
		fetchBusesTask.listScreen = this;
		fetchBusesTask.execute("http://ptts.herokuapp.com/getbuslocations/1/?format=json");		
	}
	
	@Override
	public void displayList(List<BusItem> busItems) {		
		ArrayAdapter<BusItem> adapter = new ArrayAdapter<BusItem>(getApplicationContext(), android.R.layout.simple_list_item_1, busItems);
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        makeProgressBarDisappear();		
	}
	
	   private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }
}
