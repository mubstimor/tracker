package com.ptts.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ptts.R;

public class FeedbackFragment extends Fragment {
	
	EditText textMessage, textSubject;
	Button btnSubmit;
	static final String to = "info@tracker.com";
	
	public FeedbackFragment() {	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_feedback, container, false);
		textMessage = (EditText)rootView.findViewById(R.id.txtMessage);
		textSubject = (EditText)rootView.findViewById(R.id.txtSubject);		
		btnSubmit = (Button)rootView.findViewById(R.id.btnSubmit);
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
								
				String subject = textSubject.getText().toString();
				String message = textMessage.getText().toString();
								
				Intent email = new Intent(Intent.ACTION_SEND);
				  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});				 
				  email.putExtra(Intent.EXTRA_SUBJECT, subject);
				  email.putExtra(Intent.EXTRA_TEXT, message);

				  //need this to prompts email client only
				  email.setType("message/rfc822");
				  
				  startActivity(Intent.createChooser(email, "Choose an Email client :"));
				  
				  //Toast.makeText(getActivity(), "sending ....", Toast.LENGTH_SHORT).show();
			}
		});
	}
		
}
