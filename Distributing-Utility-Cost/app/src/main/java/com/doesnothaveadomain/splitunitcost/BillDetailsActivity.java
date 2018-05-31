package com.doesnothaveadomain.splitunitcost;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class BillDetailsActivity extends AppCompatActivity
{
	private static final int PERMISSION_REQUEST_ID = 1;
	private static final String PhoneOf1stFloor = "+8801557452001";
	private static final String PhoneOf2ndFloor = "+8801670868869";
	private static final String PhoneOf3rdFloor = "+8801706889400";
	private static final String PhoneOf4thFloor = "+8801631294839; +8801631348696";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_details);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// attach click send msg event for 2nd floor
		// attach click send msg event for 2nd floor
		(findViewById(R.id.buttonMsgTo1stFloor)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				OpenSmsApp(PhoneOf1stFloor, ((EditText)findViewById(R.id.editText1stFloor)).getText().toString());
			}
		});
		
		(findViewById(R.id.buttonMsgTo2ndFloor)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				OpenSmsApp(PhoneOf2ndFloor, ((EditText)findViewById(R.id.editText2ndFloor)).getText().toString());
			}
		});
		
		// attach click send msg event for 3rd floor
		(findViewById(R.id.buttonMsgTo3rdFloor)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				OpenSmsApp(PhoneOf3rdFloor, ((EditText)findViewById(R.id.editText3rdFloor)).getText().toString());
			}
		});
		
		// attach click send msg event for 4th floor
		(findViewById(R.id.buttonMsgTo4thFloor)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				OpenSmsApp(PhoneOf4thFloor, ((EditText)findViewById(R.id.editText4thFloor)).getText().toString());
			}
		});
		
		ShowDetails();
	}
	
	private void OpenSmsApp(String phoneNumber, String msg)
	{
		Uri sms_uri = Uri.parse("smsto:" + phoneNumber);
		Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
		sms_intent.putExtra("sms_body", msg);
		startActivity(sms_intent);
	}
	
	private void PrepareForMessaging()
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
	
	protected void ShowDetails()
	{
		// declare/initialize necessary variables
		Intent intentView = getIntent();
		
		TextView txtBillOfFirstMeter = findViewById(R.id.textViewBillOfFirstMeter);
		TextView txtBillOfSecondMeter = findViewById(R.id.textViewBillOfSecondMeter);
		TextView txtTotalCharge = findViewById(R.id.textViewTotalCharge);
		
		EditText editText1stFloor = findViewById(R.id.editText1stFloor);
		EditText editText2ndFloor = findViewById(R.id.editText2ndFloor);
		EditText editText3rdFloor = findViewById(R.id.editText3rdFloor);
		EditText editText4thFloor = findViewById(R.id.editText4thFloor);
		
		double unitsOf1stFloor, unitsOf1stFloorSubmeter,
				chargeOf1stFloor, electricityChargeOf1stFloorSubmeter,
				electricityBillOf1stFloor, electricityBillOf2ndFloor, electricityBillOf3rdFloor, electricityBillOf4thFloor,
				totalRawChargeOf1stFloor, vatOfCharge1, vatOfCharge2,
				motorBillPerOwner, waterBillPerFloor, gasBillSingleStove;
		int waterBill, gasBillDoubleStove;
		
		// get values
		unitsOf1stFloor = intentView.getDoubleExtra("unitsOf1stFloor", 0);
		unitsOf1stFloorSubmeter = intentView.getDoubleExtra("submeterUnitsOf1stFloor", 0);
		
		chargeOf1stFloor = intentView.getDoubleExtra("chargeOf1stFloor", 0);
		electricityChargeOf1stFloorSubmeter = intentView.getDoubleExtra("chargeOf1stFloorSubmeter", 0);
		
		electricityBillOf1stFloor = intentView.getDoubleExtra("electricityBillOf1stFloor", 0);
		electricityBillOf2ndFloor = intentView.getDoubleExtra("electricityBillOf2ndFloor", 0);
		electricityBillOf3rdFloor = intentView.getDoubleExtra("electricityBillOf3rdFloor", 0);
		electricityBillOf4thFloor = intentView.getDoubleExtra("electricityBillOf4thFloor", 0);
		
		waterBill = intentView.getIntExtra("waterBill", 0);
		gasBillDoubleStove = intentView.getIntExtra("gasBill", 0);
		
		
		
		// calculations
		totalRawChargeOf1stFloor= chargeOf1stFloor + electricityChargeOf1stFloorSubmeter;
		vatOfCharge1 = chargeOf1stFloor * 5 / 100;
		chargeOf1stFloor = chargeOf1stFloor + vatOfCharge1;
		vatOfCharge2 = electricityChargeOf1stFloorSubmeter * 5 / 100;
		electricityChargeOf1stFloorSubmeter = electricityChargeOf1stFloorSubmeter + vatOfCharge2;
		
		motorBillPerOwner = electricityChargeOf1stFloorSubmeter / 4.0;
		waterBillPerFloor = waterBill / 5.0;
		gasBillSingleStove = gasBillDoubleStove - 50;
		
		
		// set to controls
		txtBillOfFirstMeter.setText(String.format(Locale.ENGLISH, "%.2f", chargeOf1stFloor)
						+ " TK for Unit "
						+ String.format(Locale.ENGLISH, "%.2f", unitsOf1stFloor - unitsOf1stFloorSubmeter)
						+ ". Late: "
						+  String.format(Locale.ENGLISH, "%.2f", (chargeOf1stFloor+vatOfCharge1))
						+ "tk",
				TextView.BufferType.EDITABLE);
		
		txtBillOfSecondMeter.setText(String.format(Locale.ENGLISH, "%.2f", electricityChargeOf1stFloorSubmeter)
						+ " TK for Unit "
						+  String.format(Locale.ENGLISH, "%.2f", unitsOf1stFloorSubmeter)
						+ ". Late: "
						+ String.format(Locale.ENGLISH, "%.2f", electricityChargeOf1stFloorSubmeter+vatOfCharge2)
						+ "tk",
				TextView.BufferType.EDITABLE);
		
		txtTotalCharge.setText(String.format(Locale.ENGLISH, "%.02f", totalRawChargeOf1stFloor));
		
		SetDetailsBill(R.id.editText1stFloor, Math.ceil(electricityBillOf1stFloor-electricityChargeOf1stFloorSubmeter), Math.ceil(motorBillPerOwner), Math.ceil(waterBillPerFloor), gasBillDoubleStove);
		SetDetailsBill(R.id.editText2ndFloor, electricityBillOf2ndFloor, (int)motorBillPerOwner, (int)waterBillPerFloor, gasBillDoubleStove);
		SetDetailsBill(R.id.editText3rdFloor, electricityBillOf3rdFloor, (int)motorBillPerOwner, (int)waterBillPerFloor, gasBillDoubleStove);
		SetDetailsBill(R.id.editText4thFloor, electricityBillOf4thFloor, (int)motorBillPerOwner, Math.floor(waterBillPerFloor*2), gasBillDoubleStove+(gasBillSingleStove*2));
	}
	
	private void SetDetailsBill(int id, double electricityBill, double motorBill, double waterBill, double gasBill)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		
		EditText editText = findViewById(id);
		editText.setText(
				"Utility bills - "
				+ new DateFormatSymbols().getMonths()[c.get(Calendar.MONTH)]
				+ ", "
				+ c.get(Calendar.YEAR)
				+ "----"
				+ System.lineSeparator()
				+ "Electricity - "
				+ String.format(Locale.ENGLISH, "%.0f", electricityBill)
				+ System.lineSeparator()
				+ "Motor - "
				+ String.format(Locale.ENGLISH, "%.0f", motorBill)
				+ System.lineSeparator()
				+ "Water - "
				+ String.format(Locale.ENGLISH, "%.0f", waterBill)
				+ System.lineSeparator()
				+ "Gas - "
				+ String.format(Locale.ENGLISH, "%.0f", gasBill)
				+ System.lineSeparator()
				+ "Total:  "
				+ String.format(Locale.ENGLISH, "%.0f", electricityBill + motorBill + waterBill + gasBill)
			);
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
			PrepareForMessaging();
		}
		else
		{
			Toast.makeText(BillDetailsActivity.this,
					"Permission denied", Toast.LENGTH_LONG).show();
		}
		
	}
}
