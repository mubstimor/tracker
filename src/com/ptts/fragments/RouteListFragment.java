package com.ptts.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ptts.R;
import com.ptts.library.FetchRouteTask;
import com.ptts.library.RouteItem;
import com.ptts.library.RouteListScreen;

public class RouteListFragment extends ListFragment implements RouteListScreen {
	
	FetchRouteTask fetchRoutesTask = new FetchRouteTask();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.route_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		fetchRoutesTask.listScreen = this;
		fetchRoutesTask.execute("https://todo-list-app-dan.herokuapp.com/api/tasks");		
	}



	@Override
	public void displayList(List<RouteItem> routeItems) {
		ArrayAdapter<RouteItem> adapter = new ArrayAdapter<RouteItem>(getActivity(), android.R.layout.simple_list_item_1, routeItems);
        ListView listView = (ListView)getView().findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        makeProgressBarDisappear();		
	}
	
	   private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }

}
