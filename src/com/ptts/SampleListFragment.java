package com.ptts;

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
import android.widget.Toast;

public class SampleListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		
		//MENU ITEMS
		adapter.add(new SampleItem("View Routes", R.drawable.ic_action_locate));
		adapter.add(new SampleItem("Search For Stages", android.R.drawable.ic_menu_search));
		adapter.add(new SampleItem("Provide Feedback", R.drawable.ic_action_mail_add));
		adapter.add(new SampleItem("Product Tour", android.R.drawable.ic_menu_search));
		adapter.add(new SampleItem("Settings", R.drawable.ic_action_settings));		
				
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {		
		 Log.i("FragmentList", "Item clicked: " + id);
		Toast.makeText(getActivity(), "Item clicked: " + id, Toast.LENGTH_SHORT).show();
		Class<?> cls = null;
		if (id == 0) {
			cls = ViewRoutes.class;	
		}
		else if (id == 1) {
			cls = FirstTimeActivity.class;	
		}
		else if (id == 2) {
			cls = FirstTimeActivity.class;	
		}
		else if (id == 3) {
			cls = FirstTimeActivity.class;	
		}
		else if (id == 4) {
			cls = FirstTimeActivity.class;	
		}
		
		Intent intent = new Intent(getActivity(), cls);
		startActivity(intent);		
	}
	
	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
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
