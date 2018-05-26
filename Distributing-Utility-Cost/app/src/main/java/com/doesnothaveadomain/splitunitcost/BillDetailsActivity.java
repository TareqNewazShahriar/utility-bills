package com.doesnothaveadomain.splitunitcost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.telephony.SmsManager;

public class BillDetailsActivity extends AppCompatActivity
{
	private static final int SMS_PERMISSION_REQUEST_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_details);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		
		
		Button buttonSendMessage = findViewById(R.id.buttonSendMsg);
		buttonSendMessage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view)
			{
				String sms = "sdfsdf";
				String phoneNum = "013456466546";
				
				if(!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum))
				{
					if(checkPermission())
					{
						//Get the default SmsManager//
						SmsManager smsManager = SmsManager.getDefault();
						
						//Send the SMS//
						smsManager.sendTextMessage(phoneNum, null, sms, null, null);
					}
					else
					{
						Toast.makeText(BillDetailsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	/*protected void Show()
	{
		EditText txtTotalUsage = (EditText) findViewById(R.id.editTextTotalUsageInput);
		EditText txtSubmeterUnitPrev = (EditText) findViewById(R.id.editTextSubmeterBillPrevInput);
		EditText txtSubmeterUnitCurrent = (EditText) findViewById(R.id.editTextSubmeterUnitCurrentInput);
		TextView txtBillOfFirstMeter = (TextView) findViewById(R.id.textViewBillOf1stMeter);
		TextView txtBillOfSecondMeter = (TextView) findViewById(R.id.textViewBillOf2ndMeter);
		TextView txtTotalCharge = (TextView) findViewById(R.id.txtTotalCharge);
		
		double totalUsage, totalUsageOf2ndMeter,
				charge1, charge2, totalCharge, vatOfCharge1, vatOfCharge2;
		
		totalUsage = Double.parseDouble(txtTotalUsage.getText().toString());
		totalUsageOf2ndMeter = Double.parseDouble(txtSubmeterUnitCurrent.getText().toString()) - Double.parseDouble(txtSubmeterUnitPrev.getText().toString());;
		
		charge1 = 0; // calc(1, totalUsage - totalUsageOf2ndMeter, totalUsage);
		charge2 = 0; //calc(totalUsage - totalUsageOf2ndMeter + 1, totalUsage, totalUsage);
		totalCharge= charge1 + charge2;
		
		vatOfCharge1 = charge1 * 5 / 100;
		vatOfCharge2 = charge2 * 5 / 100;
		charge1 = charge1 + vatOfCharge1;
		charge2 = charge2 + vatOfCharge2;
		
		txtBillOfFirstMeter.setText(String.format(Locale.ENGLISH, "%.02f", charge1)
						+ " TK for Unit " + String.format(Locale.ENGLISH, "%.0f", totalUsage - totalUsageOf2ndMeter)
						+ ". Late: " +  String.format(Locale.ENGLISH, "%.0f", (charge1+vatOfCharge1)) + "tk",
				TextView.BufferType.EDITABLE);
		
		txtBillOfSecondMeter.setText(String.format(Locale.ENGLISH, "%.02f", charge2)
						+ " TK for Unit " + totalUsageOf2ndMeter
						+ ". Late: " + String.format(Locale.ENGLISH, "%.0f", charge2+vatOfCharge2) + "tk",
				TextView.BufferType.EDITABLE);
		
		txtTotalCharge.setText(String.format(Locale.ENGLISH, "%.02f", totalCharge));
		
		// copy 2nd meter bill to clipboard
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		//clipboard.setText(String.format(Locale.ENGLISH, "%.02f", charge2));
		
		
		ClipData clipDataObj = ClipData.newPlainText("this is clipdata label",
				String.format(Locale.ENGLISH, "%.02f", charge2));
		clipboard.setPrimaryClip(clipDataObj);
		
		Toast.makeText(BillDetailsActivity.this, "2nd meter bill copied to clipboard", Toast.LENGTH_SHORT).show();
	}*/
	
	
	private boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(BillDetailsActivity.this, Manifest.permission.SEND_SMS);
		if (result == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			requestPermission();
			return false;
		}
	}
	
	private void requestPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_ID);
		
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case SMS_PERMISSION_REQUEST_ID:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					
					Toast.makeText(BillDetailsActivity.this,
							"Permission accepted", Toast.LENGTH_LONG).show();
					
				} else {
					Toast.makeText(BillDetailsActivity.this,
							"Permission denied", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
}
