package com.ptts;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.ptts.fragments.StopLocation;
import com.ptts.library.ConnectionDetector;
import com.ptts.library.FetchStopTask;
import com.ptts.library.ItemListScreen;
import com.ptts.library.StopListAdapter;

public class SearchActivity extends SherlockActivity implements ItemListScreen {

	FetchStopTask fetchStopsTask;
	ConnectionDetector cd; // Internet detector
	EditText search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_list);
		makeProgressBarDisappear();

		cd = new ConnectionDetector(getApplicationContext());

		ActionBar actionBar = getSupportActionBar();
		// add the custom view to the action bar
		actionBar.setCustomView(R.layout.activity_search);
		search = (EditText) actionBar.getCustomView().findViewById(
				R.id.searchfield);
		search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Toast.makeText(SearchActivity.this, "Search triggered",
						Toast.LENGTH_LONG).show();
				String stop = search.getText().toString();
				showProgressBar();
				downloadData(stop);
				return false;
			}
		});
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
	}

	private void downloadData(String stopName) {
		if (!cd.isConnectingToInternet()) {
			Toast.makeText(getApplicationContext(),
					"Connect to Internet First", Toast.LENGTH_LONG).show();
			return;
		} else {
			fetchStopsTask = new FetchStopTask();
			fetchStopsTask.listScreen = this;
			fetchStopsTask.execute("http://ptts.herokuapp.com/getstop/"
					+ stopName.trim() + "/?format=json");
			Log.i("REQUEST for search", "http://ptts.herokuapp.com/getstop/"
					+ stopName.trim() + "/?format=json");
		}
	}

	private void showProgressBar() {
		ProgressBar progressBar = (ProgressBar) this
				.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void makeProgressBarDisappear() {
		ProgressBar progressBar = (ProgressBar) this
				.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void displayList(final ArrayList<HashMap<String, String>> stopItems) {
		StopListAdapter adapter = new StopListAdapter(this, stopItems);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		makeProgressBarDisappear();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(getApplicationContext(),
						StopLocation.class);
				i.putExtra(
						FetchStopTask.getTagRouteStop(),
						stopItems.get(position).get(
								FetchStopTask.getTagRouteStop()));
				i.putExtra(
						FetchStopTask.getTagRouteStopLat(),
						stopItems.get(position).get(
								FetchStopTask.getTagRouteStopLat()));
				i.putExtra(
						FetchStopTask.getTagRouteStopLng(),
						stopItems.get(position).get(
								FetchStopTask.getTagRouteStopLng()));
				startActivity(i);

			}
		});
	}

}
