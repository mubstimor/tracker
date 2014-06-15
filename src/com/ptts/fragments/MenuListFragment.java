package com.ptts.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ptts.EntryListActivity;
import com.ptts.Feedback;
import com.ptts.FirstTimeActivity;
import com.ptts.R;
import com.ptts.SearchActivity;
import com.ptts.SettingsActivity;
import com.ptts.ViewRoutes;

public class MenuListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuAdapter adapter = new MenuAdapter(getActivity());
		
		//MENU ITEMS
		adapter.add(new MenuItem("View Routes", R.drawable.ic_action_locate));
		adapter.add(new MenuItem("Search For Stages", android.R.drawable.ic_menu_search));
		adapter.add(new MenuItem("Provide Feedback", R.drawable.ic_action_mail_add));
		adapter.add(new MenuItem("Product Tour", android.R.drawable.ic_menu_search));
		adapter.add(new MenuItem("Settings", R.drawable.ic_action_settings));
		adapter.add(new MenuItem("Syncing", android.R.drawable.ic_menu_search));	
				
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {		
		 Log.i("FragmentList", "Item clicked: " + id);
		//Toast.makeText(getActivity(), "Item clicked: " + id, Toast.LENGTH_SHORT).show();
		Class<?> cls = null;
		if (id == 0) {
			cls = ViewRoutes.class;	
		}
		else if (id == 1) {
			cls = SearchActivity.class;	
		}
		else if (id == 2) {
			cls = Feedback.class;	
		}
		else if (id == 3) {
			cls = FirstTimeActivity.class;	
		}
		else if (id == 4) {
			cls = SettingsActivity.class;	
		}
		else if (id == 5) {
			cls = EntryListActivity.class;	
		}
		
		Intent intent = new Intent(getActivity(), cls);
		startActivity(intent);		
	}
	
	private class MenuItem {
		public String tag;
		public int iconRes;
		public MenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class MenuAdapter extends ArrayAdapter<MenuItem> {

		public MenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
}
