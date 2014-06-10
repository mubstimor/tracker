package com.ptts;

import com.ptts.fragments.RouteListFragment;

import android.os.Bundle;


public class ViewRoutes extends BaseActivity {
	
	public ViewRoutes() {
		super(R.string.title_activity_view_routes);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new RouteListFragment())
		.commit();
		
		setSlidingActionBarEnabled(false);
	}

}
