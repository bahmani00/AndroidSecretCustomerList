package com.danielbahmani.secretcustomerlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ClientListActivity2 extends ListActivity {

	private final String TAG = "ClientListActivity2"; 
	private MySimpleAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initializeListView();
		mAdapter = new MySimpleAdapter(this);

		final ListView listview = getListView();

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				int personId = ((Client)parent.getAdapter().getItem(position)).id;
				//int personId = ((Client)mAdapter.getItem(position)).id;
				Intent intent = new Intent(parent.getContext(),
						EditAddClientActivity.class);
				intent.putExtra("id", personId);
				startActivity(intent);
			}
		});

		listview.setAdapter(mAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mAdapter.getCount() == 0)
			loadData();
	}

	private void loadData() {
		ClientDB clientDB = new ClientDB(getApplicationContext());
		List<Client> clients = clientDB.getClients();
		clientDB.close();

		Iterator<Client> iterator = clients.iterator();
		while (iterator.hasNext()) {
			Client item = iterator.next();
			mAdapter.add(item);
		}
	}

	private class MySimpleAdapter extends BaseAdapter {

		private final Context mContext;
		private final List<Client> mItems = new ArrayList<Client>();

		public MySimpleAdapter(Context context) {
			mContext = context;
		}

		public void add(Client item) {
			mItems.add(item);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mItems.get(position).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Client client = mItems.get(position);

			Log.v(TAG, client.fullname);
			
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.client_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.lblAddr = (TextView) convertView.findViewById(R.id.lblAddr);
				viewHolder.lblFullName = (TextView) convertView.findViewById(R.id.lblFullName);
				viewHolder.imgView = (ImageView) convertView.findViewById(R.id.imgPicture);
				
				convertView.setTag(viewHolder);
				
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.lblFullName.setText(client.fullname);
			viewHolder.lblAddr.setText(client.address);

			if (client.picture != null && !client.picture.isEmpty()) {
				try {
					viewHolder.imgView.setImageBitmap(BitmapFactory
							.decodeFile(client.picture));
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
				}
			}

			return convertView;
		}
	}
	
	private static class ViewHolder{
		TextView lblAddr;
		TextView lblFullName;
		ImageView imgView;
	}	
}
