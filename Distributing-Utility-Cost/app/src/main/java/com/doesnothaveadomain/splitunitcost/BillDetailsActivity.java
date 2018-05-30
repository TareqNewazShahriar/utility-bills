package com.doesnothaveadomain.splitunitcost;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import java.util.Locale;

public class BillDetailsActivity extends AppCompatActivity
{
	private static final int PERMISSION_REQUEST_ID = 1;
	private static final String PHoneOf2ndFloor = "+8801670868869";
	private static final String PHoneOf3rdFloor = "+8801706889400";
	private static final String PHoneOf4thFloor = "+8801631294839; +8801631348696";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_details);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		Button buttonSendMessage = findViewById(R.id.buttonMsgTo3rdFloor);
		buttonSendMessage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Uri sms_uri = Uri.parse("smsto:+88016549869654");
				Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
				sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
				startActivity(sms_intent);
			}
		});
	}
	
	private void SmsQueue()
	{
		if(checkPermissions())
		{
			String msg = "This test message automatically sent by the app\n'Distributing Utility Cost'.";
			String phoneNum = "+8801558960472";
			sendSms(phoneNum, msg);
		}
	}
	
	private void sendSms(String phoneNum, String msg)
	{
		if(!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(phoneNum))
		{
			//Get the default SmsManager//
			SmsManager smsManager = SmsManager.getDefault();
			
			//Send the SMS//
			smsManager.sendTextMessage(phoneNum, null, msg, null, null);
		}
	}
	
	private void sendSms2(String phoneNumber, String message)
	{
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				new Intent(this, BillDetailsActivity.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
	}
	
	private void sendSms3(String phoneNumber, String message)
	{
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);
		
		//---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(SENT));
		
		//---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered",
								Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
	
	protected void Show()
	{
		EditText txtTotalUsage = null;
		EditText txtSubmeterUnitPrev = null;
		EditText txtSubmeterUnitCurrent = null;
		TextView txtBillOfFirstMeter = null;
		TextView txtBillOfSecondMeter = null;
		TextView txtTotalCharge = null;
		
		double totalUsage, totalUsageOf2ndMeter,
				charge1, charge2, totalCharge, vatOfCharge1, vatOfCharge2;
		
		totalUsage = 0;
		totalUsageOf2ndMeter = 0;
		
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
		
	}
	
	
	private boolean checkPermissions()
	{
		boolean ok = true;
		for (String item: new String[]{ Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE })
		{
			int result = ContextCompat.checkSelfPermission(BillDetailsActivity.this, item);
			
			if (result == PackageManager.PERMISSION_GRANTED)
				ok = ok & true;
			else
			{
				requestPermission(item);
				ok = ok & false;
			}
		}
		
		return ok;
	}
	
	private void requestPermission(String permission)
	{
		ActivityCompat.requestPermissions(this, new String[]{ permission }, PERMISSION_REQUEST_ID);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
		{
			Toast.makeText(BillDetailsActivity.this,
					"Permission accepted", Toast.LENGTH_LONG).show();
			SmsQueue();
		}
		else
		{
			Toast.makeText(BillDetailsActivity.this,
					"Permission denied", Toast.LENGTH_LONG).show();
		}
		
	}
}
