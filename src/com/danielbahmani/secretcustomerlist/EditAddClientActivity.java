package com.danielbahmani.secretcustomerlist;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;

public class EditAddClientActivity extends Activity {

	int clientId = -1;
	EditText txtFirstAndLastName;
	String picturePath;
	ImageButton btnClientPic;	
	RadioButton rdoMale;
	RadioButton rdoFemale;
	EditText txtAddress;
	EditText txtPostalcode;
	EditText txtNotes;
	// Birthdate
	EditText txtBirthdate;
	private int mYear = 1970;
	private int mMonth = 01;
	private int mDay = 01;
	private Date birthDate;

	
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_edit_add);

		txtFirstAndLastName = (EditText) findViewById(R.id.txtFirstAndLastName);
		txtBirthdate = (EditText) findViewById(R.id.txtBirthdate);
		txtBirthdate.setEnabled(false);
		btnClientPic = (ImageButton) findViewById(R.id.btnClientPic);
		rdoMale = (RadioButton) findViewById(R.id.rdoMale);
		rdoFemale = (RadioButton) findViewById(R.id.rdoFemale);
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		txtPostalcode = (EditText) findViewById(R.id.txtPostalcode);
		txtNotes = (EditText) findViewById(R.id.txtNotes);

		clientId = getIntent().getIntExtra("id", -1);
		Button btnAddEdit = (Button) findViewById(R.id.btnAddClient);
		Button btnBackCancel = (Button) findViewById(R.id.btnCancel);
		if(clientId == -1)
		{
			this.setTitle(R.string.title_activity_add_client);
			btnAddEdit.setText(R.string.btnAddNewClient);
			btnBackCancel.setText(R.string.btnCancel);
		}
		else
		{
			this.setTitle(R.string.title_activity_edit_client);
			btnAddEdit.setText(R.string.btnSaveChanges);
			btnBackCancel.setText(R.string.btnBack);
			LoadClientData(clientId);
		}
	}

	private void LoadClientData(int clientId)
	{
		ClientDB clientDB = new ClientDB(getApplicationContext());
		Client client = clientDB.getClientById(clientId);
		clientDB.close();

		txtFirstAndLastName.setText(client.fullname);
		if(client.birthdate > 0){
			birthDate = new Date(client.birthdate);
			mYear = birthDate.getYear();
			mMonth = birthDate.getMonth();
			mDay = birthDate.getDay();
			updateDisplay();			
		}
		
		if(client.picture != null && !client.picture.isEmpty()){
			picturePath = client.picture;
			btnClientPic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}

		//TODO:load image btnClientPic.setsrcText(client.fullname);
		Log.v("LoadClientData", "client.gender: "+ client.gender);
		
		rdoMale.setChecked(client.gender);
		rdoFemale.setChecked(!client.gender);
		txtAddress.setText(client.address);
		txtPostalcode.setText(client.postalcode);
		txtNotes.setText(client.notes);
		
	}

	public void btnPickDateClick(View v) {
		new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay).show();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		// Month is 0 based so add 1
		txtBirthdate.setText(String.format("%d-%d-%d", mMonth + 1, mDay, mYear));
	}
	
	public void rdoGenderClick(View v)
	{
		RadioButton rdo = (RadioButton)v;
		if(rdo == rdoMale)
			rdoFemale.setChecked(false);
		else
			rdoMale.setChecked(false);
	}
	public void btnClientPicClick(View v) {

		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK	&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			Log.v("RESULT_LOAD_IMAGE", "picturePath: " + picturePath);
			
			btnClientPic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}

	}
	
	public void btnCancelClick(View v) {
		this.finish();
	}
	public void btnAddClientClick(View v) {
		
		//Validation
		
		String firstAndLastName = txtFirstAndLastName.getText().toString();
		if(firstAndLastName.isEmpty() && firstAndLastName.length() < 3)
		{
			showAlert("Invalid input", "First and LastName must have more than 3 chars.", txtFirstAndLastName);
			return ;
		}

		//validate canadian postal code
		String postalcode = txtPostalcode.getText().toString();
		if(!isValidPostalCode(postalcode))
		{
			showAlert("Invalid input", "Invalid Canadian Postal Code.", txtPostalcode);
			return ;
		}


		birthDate = new Date();
		birthDate.setYear(mYear);
		birthDate.setMonth(mMonth);
		birthDate.setDate(mDay);
		
		Client client = new Client(clientId, firstAndLastName);
		client.fullname = firstAndLastName;
		client.birthdate = birthDate.getTime();
		client.gender = rdoMale.isChecked();
		client.picture = picturePath;
		client.address = txtAddress.getText().toString();
		client.postalcode = postalcode;
		client.notes = txtNotes.getText().toString();

		Log.v("assing2", "clientId: "+ clientId);
		
		ClientDB clientDB = new ClientDB(this.getApplicationContext());
		if(clientId == -1)
		{
			try{
				clientDB.insert(client);
				Toast.makeText(getApplicationContext(), "Successfully inserted!", Toast.LENGTH_LONG).show();			
			}catch(Exception e){
				//Alert that there was a error							
			}finally{
				clientDB.close();
			}
		}
		else
		{
			try{
				clientDB.update(client);
				Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_LONG).show();			
			}catch(Exception e){
				//Alert that there was a error							
			}finally{
				clientDB.close();
			}
		}

		this.finish();
		
	}

	private boolean isValidPostalCode(String code) {

		if(code == null || code.isEmpty()) return true;
		// must have 7 digits
		if (code.length() != 7)
			return false;

		// make if uppercase for not having to chack for A to Z AND a to z
		code = code.toUpperCase();
		// translate into digit
		char[] digit = code.toCharArray();

		if (digit[0] < 'A' || digit[0] > 'Z')
			return false;
		if (digit[1] < '0' || digit[1] > '9')
			return false;
		if (digit[2] < 'A' || digit[2] > 'Z')
			return false;
		if (digit[3] != ' ')
			return false;
		if (digit[4] < '0' || digit[4] > '9')
			return false;
		if (digit[5] < 'A' || digit[5] > 'Z')
			return false;
		if (digit[6] < '0' || digit[6] > '9')
			return false;

		return true;
	}
	private boolean CheckPostalCode(String pCode)
	{
		if(pCode == null || pCode.isEmpty()) return true;
		
		String pc1="([A-PR-UWYZ](([0-9](([0-9]|[A-HJKSTUW])?)?)|([A-HK-Y][0-9]([0-9]|[ABEHMNPRVWXY])?)) [0-9][ABD-HJLNP-UW-Z]{2})|GIR 0AA";
		Pattern pattern = Pattern.compile(pc1);
		Matcher  matcher = pattern.matcher(pCode.toUpperCase());
        if (matcher.find()) {
            Log.v("assign2:", matcher.group());
            return true;
        } else { 
        	Log.v("assign2","invalid PostalCode"); 
        	return false;
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
