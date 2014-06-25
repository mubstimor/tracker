package com.ptts;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ptts.fragments.MenuListFragment;
import com.ptts.fragments.TutorialFragment;
import com.ptts.library.ZoomOutPageTransformer;

public class FirstTimeActivity extends FragmentActivity {

	private SlidingMenu menu;
	private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_screen_slide);

		setTitle(R.string.title_activity_first_time);

		// set the Above View
		//setContentView(R.layout.content_frame);
//		getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.content_frame, new SampleListFragment())
//		.commit();

		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuListFragment())
		.commit();
		
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

	        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

	        // Add either a "next" or "finish" button to the action bar, depending on which page
	        // is currently selected.
	        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
	                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
	                        ? R.string.action_finish
	                        : R.string.action_next);
	        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	                // Navigate "up" the demo structure to the launchpad activity.
	                // See http://developer.android.com/design/patterns/navigation.html for more.
	                NavUtils.navigateUpTo(this, new Intent(this, ViewRoutes.class));
	                return true;

	            case R.id.action_previous:
	                // Go to the previous step in the wizard. If there is no previous step,
	                // setCurrentItem will do nothing.
	                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
	                return true;

	            case R.id.action_next:
	                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
	                // will do nothing.
	                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
	                if(mPager.getCurrentItem()==4){
	                	//Toast.makeText(getApplicationContext(), "Load first activity", Toast.LENGTH_SHORT).show();
	                	Intent intent = new Intent(FirstTimeActivity.this, ViewRoutes.class);
	            		startActivity(intent);
	                }
	                return true;
	        }

	        return super.onOptionsItemSelected(item);
	    }

	    /**
	     * A simple pager adapter that represents 5 {@link TutorialFragment} objects, in
	     * sequence.
	     */
	    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	        public ScreenSlidePagerAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public Fragment getItem(int position) {
	            return TutorialFragment.create(position);
	        }

	        @Override
	        public int getCount() {
	            return NUM_PAGES;
	        }
	    }
	    
}
