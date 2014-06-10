package com.ptts;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class SearchActivity extends SherlockActivity {

	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.route_list);
	    makeProgressBarDisappear();

	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.activity_search);
	    EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
	    search.setOnEditorActionListener(new OnEditorActionListener() {

	      @Override
	      public boolean onEditorAction(TextView v, int actionId,
	          KeyEvent event) {
	        Toast.makeText(SearchActivity.this, "Search triggered",
	            Toast.LENGTH_LONG).show();
	        showProgressBar();
	        return false;
	      }
	    });
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
	        | ActionBar.DISPLAY_SHOW_HOME);
	  }

	  private void showProgressBar(){
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.VISIBLE);
	    }
	  
	  private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }
}
