package com.danielbahmani.secretcustomerlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;

public class ClientListActivity extends ListActivity {

	private final String TAG = "ClientListActivity2"; 

	List<Client> clients;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeListView();
	}
	
	@Override
	public void onResume() {
	    super.onResume();

	    initializeListView();
	}
	
	private void initializeListView()
	{
		final ListView listview = getListView();
		
		final ArrayList<String> list = new ArrayList<String>();		
		ClientDB clientDB = new ClientDB(getApplicationContext());
		clients = clientDB.getClients();
		clientDB.close();
		for (int i = 0; i < clients.size(); ++i) {
			list.add(clients.get(i).fullname);
		}
		
		final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int personId = clients.get(position).id;
				Intent intent = new Intent(parent.getContext(), EditAddClientActivity.class);
				intent.putExtra("id", personId);
			    startActivity(intent);
			}
		});		
	}
	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

}
