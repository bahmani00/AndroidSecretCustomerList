package com.danielbahmani.secretcustomerlist;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	
	public void btnGetAccessClick(View v)
	{
		
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

		String password = txtPassword.getText().toString();
		
		SharedPreferences  mPrefs = getApplicationContext().getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
		String savedPassword = mPrefs.getString("password", "");
		
		Log.i("assing2, savedPassword: ", savedPassword);
		Log.i("assing2, password: ", password);

		if((savedPassword == null || savedPassword.isEmpty()) && password.isEmpty())
		{
			Intent intent = new Intent(this, MainMenuActivity.class);
		    startActivity(intent);		
		}
		else if(savedPassword != null && !savedPassword.isEmpty() && password.equals(savedPassword))
		{
			Intent intent = new Intent(this, MainMenuActivity.class);
		    startActivity(intent);
		}
		else
		{
			showAlert("Invalid input", "Your input password is wrong!", txtPassword);
		}
	}
	
	private void showAlert(String title, String message, final EditText inputEditText){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(true)
        .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if(inputEditText.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        AlertDialog alert = builder.create();
		
        alert.setMessage(message);
        alert.show();
    }
	
}
