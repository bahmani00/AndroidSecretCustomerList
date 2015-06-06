package com.danielbahmani.secretcustomerlist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClientDB {

	private static final String TAG = ClientDB.class.getSimpleName();

	class DbHelper extends SQLiteOpenHelper {
		static final String TAG = "DbHelper";
		static final String DB_NAME = "secretcustomer.db";
		static final int DB_VERSION = 2;
		Context context;

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			this.context = context;
		}

		// Called only once, first time the DB is created
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table client (_id int primary key, fullname text not null, birthdate long, gender bit, picture text, address text, postalcode text, notes text)";
			db.execSQL(sql); //
			Log.d(TAG, "onCreated sql: " + sql);
		}

		// Called whenever newVersion != oldVersion
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists client"); // drops the old database
			Log.d(TAG, "onUpdated");
			onCreate(db); // run onCreate to get new database
		}
	}

	private final DbHelper dbHelper;

	public ClientDB(Context context) {
		this.dbHelper = new DbHelper(context);
		Log.i(TAG, "Initialized data");
	}

	public void close() {
		this.dbHelper.close();
	}

	public void insert(Client client) {
		Log.d(TAG, "insert on " + client.fullname);
		ContentValues values = new ContentValues();
		int id = getMaxId();
		values.put("_id", id);
		values.put("fullname", client.fullname);
		values.put("birthdate", client.birthdate);
		values.put("gender", client.gender);
		values.put("picture", client.picture);
		values.put("address", client.address);
		values.put("postalcode", client.postalcode);
		values.put("notes", client.notes);

		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertOrThrow("client", null, values);
			Log.d(TAG, "insert done " + client.fullname);
		} finally {
			db.close();
		}
	}
	public int update(Client client) {
		Log.d(TAG, "update on " + client.fullname);
		Log.d(TAG, "update on gender" + client.gender);
		ContentValues values = new ContentValues();
		values.put("_id", client.id);
		values.put("fullname", client.fullname);

		values.put("birthdate", client.birthdate);
		values.put("gender", client.gender);
		values.put("picture", client.picture);
		values.put("address", client.address);
		values.put("postalcode", client.postalcode);
		values.put("notes", client.notes);

		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			return db.update("client", values, "_id=?", new String []{String.valueOf(client.id)});
		} finally {
			db.close();
		}
	}
	public void delete(Client client)
	  {
	   SQLiteDatabase db = this.dbHelper.getWritableDatabase();
	   try {
		   db.delete("client", "_id=?", new String [] {String.valueOf(client.id)});
	   } finally {
			db.close();
		}
	  }
	
	public List<Client> getClients() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		List<Client> persons = new ArrayList<Client>();
		try {
			Cursor cursor = db.query("client", null, null, null, null, null, "fullname ASC");
			if (cursor != null) {
				while (cursor.moveToNext()) {
					persons.add(createClientFromCursor(cursor));
				}
			}
		} finally {
			db.close();
		}

		return persons;
	}

	public Client getClientById(long id) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query("client", null, "_id=" + id, null, null, null, null);
			if (cursor != null && cursor.moveToNext())					
				return createClientFromCursor(cursor);
		} finally {
			db.close();
		}
		
		return null;
	}
	
	private Client createClientFromCursor(Cursor cursor){
		Client client = new Client(cursor.getInt(0), cursor.getString(1));
		client.birthdate = cursor.getLong(2);
		client.gender = cursor.getInt(3) > 0;
		client.picture = cursor.getString(4);
		client.address = cursor.getString(5);
		client.postalcode = cursor.getString(6);
		client.notes = cursor.getString(7);
		
		return client;
	}
	

	private int getMaxId() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		int newId = 0;
		try {
			Cursor cursor = db.query("client", null, null, null, null, null,
					"_id DESC");
			try {
				if (cursor != null && cursor.moveToNext()){					
					newId = cursor.getInt(0);
					newId++;
					Log.v(TAG, "getMaxId: " + newId);
				}
				else
					newId = 0;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
		
		return newId;
	}
}

