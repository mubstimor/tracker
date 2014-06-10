package com.ptts;

import android.os.Bundle;

import com.ptts.fragments.FeedbackFragment;


public class Feedback extends BaseActivity {
	
	public Feedback() {
		super(R.string.title_activity_feedback);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new FeedbackFragment())
		.commit();
		
		setSlidingActionBarEnabled(false);
	}

}
