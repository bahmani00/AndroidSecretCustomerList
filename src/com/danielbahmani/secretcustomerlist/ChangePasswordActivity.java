package com.danielbahmani.secretcustomerlist;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class ChangePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
	}

	public void btnChangePasswordClick(View v)
	{
		SharedPreferences  mPrefs = getApplicationContext().getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
		String savedPassword = mPrefs.getString("password", "");

	    EditText txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
		String oldPassword = txtOldPassword.getText().toString();

		boolean valid = false;
		if((savedPassword == null || savedPassword.isEmpty()) && oldPassword.isEmpty())
		{
			valid = true;
		}
		else if(savedPassword != null && !savedPassword.isEmpty() && oldPassword.equals(savedPassword))
		{
			valid = true;
		}

		if(!valid){
			showAlert("Invalid input", "Old password doesn't match!", txtOldPassword);	
		}
		else
		{
			EditText txtNewPassword1 = (EditText) findViewById(R.id.txtNewPassword1);
			EditText txtNewPassword2 = (EditText) findViewById(R.id.txtNewPassword2);
			String newPassword1 = txtNewPassword1.getText().toString();
			String newPassword2 = txtNewPassword2.getText().toString();
			if(newPassword1.equals(newPassword2))
			{				
			    SharedPreferences.Editor editor = mPrefs.edit();
			    editor.putString("password", newPassword1);
			    editor.commit();
				showAlert("Successfully Changed", "Password changed successfully!", txtNewPassword2);
				//this.finish();
			}
			else
			{
				showAlert("Invalid input", "New password entered twice doesn't match!", txtNewPassword1);
				txtNewPassword1.setText("");
				txtNewPassword2.setText("");
			}
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
