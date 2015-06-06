package com.danielbahmani.secretcustomerlist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	}

	public void btnActionClicked(View v) {
		Intent intent = null;
		
		switch(v.getId())
		{
			case R.id.btnAccessClientList:
				intent = new Intent(this, ClientListActivity.class);
				break;
			case R.id.btnAddNewClient:
				intent = new Intent(this, EditAddClientActivity.class);
				break;
			case R.id.btnChangePassword:
				intent = new Intent(this, ChangePasswordActivity.class);
				break;
			case R.id.btnAccessClientList2:
				intent = new Intent(this, ClientListActivity2.class);
				break;
		}

		startActivity(intent);
	}
}
