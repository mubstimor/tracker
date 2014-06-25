package com.ptts;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ptts.fragments.MenuListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Activity for holding EntryListFragment.
 */
public class ViewRoutes extends FragmentActivity {
	
	private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        
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
    }
}
