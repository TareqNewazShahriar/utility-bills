package com.doesnothaveadomain.splitunitcost;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AttachCalculateButtonClickEvent();
	}
	
	protected void EmptyResultantTextViews()
	{
		TextView txtBillOfFirstMeter = (TextView) findViewById(R.id.textViewBillOf1stMeter);
		TextView txtBillOfSecondMeter = (TextView) findViewById(R.id.textViewBillOf2ndMeter);
		// empty resultant textviews
		txtBillOfFirstMeter.setText(" ", TextView.BufferType.EDITABLE);
		txtBillOfSecondMeter.setText(" ", TextView.BufferType.EDITABLE);
	}
	
	protected void AttachCalculateButtonClickEvent()
	{
		Button button = (Button) findViewById(R.id.buttonCalculate);
		button.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Calculate();
				
				// hides the keyboard
				InputMethodManager imm = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
		});
	}
	
	protected void Calculate()
	{
		EditText txtTotalUsage = (EditText) findViewById(R.id.editTextTotalUsage);
		EditText txtUsageOf2ndMeter = (EditText) findViewById(R.id.editTextUsageOf2ndMeter);
		TextView txtBillOfFirstMeter = (TextView) findViewById(R.id.textViewBillOf1stMeter);
		TextView txtBillOfSecondMeter = (TextView) findViewById(R.id.textViewBillOf2ndMeter);
		TextView txtTotalCharge = (TextView) findViewById(R.id.txtTotalCharge);
		
		double totalUsage, totalUsageOf2ndMeter,
				charge1, charge2, totalCharge, vatOfCharge1, vatOfCharge2;
		
		totalUsage = Double.parseDouble(txtTotalUsage.getText().toString());
		totalUsageOf2ndMeter = Double.parseDouble(txtUsageOf2ndMeter.getText().toString());
		
		charge1 = calc(1, totalUsage - totalUsageOf2ndMeter, totalUsage);
		charge2 = calc(totalUsage - totalUsageOf2ndMeter + 1, totalUsage, totalUsage);
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
		
		Toast.makeText(MainActivity.this, "2nd meter bill copied to clipboard", Toast.LENGTH_SHORT).show();
	}
	
	double calc(double unitFrom, double unitTo, double totalUsage)
	{
		double[][] tariff = { {1, 75, 4.00}, {76, 200, 5.45}, {201, 300, 5.70},
				{301, 400, 6.02}, {401, 600, 9.30}, {601, Integer.MAX_VALUE, 10.70} };
		
		double charge = 0;
		
		if(totalUsage <= 50)
		{
			charge = (unitTo - unitFrom + 1) * 3.50;
		}
		else
		{
			for (double[] t : tariff)
			{
				if (unitTo < t[0])
					break;
				else if (unitFrom > t[1])
					continue;
				
				double adjustFrom = unitFrom > t[0] && unitFrom <= t[1] ? unitFrom - t[0] : 0;
				charge += t[2] * (unitTo >= t[0] && unitTo <= t[1] ? unitTo - (t[0] + adjustFrom) + 1 : t[1] - t[0] - adjustFrom + 1);
			}
		}

//		// 1 - 75
//		charge = 3.80 * (unitTo > 75 ? 75 : unitTo);
//		unitTo = unitTo > 75 ? unitTo - 75 : 0;
//
//		// 76 - 200
//		charge += 5.14 * (unitTo >= 125 ? 125 : unitTo);
//		unitTo = unitTo > 125 ? unitTo - 125 : 0;
//
//		// 201 - 300
//		charge += 5.36 * (unitTo >= 100 ? 100 : unitTo);
//		unitTo = unitTo > 100 ? unitTo - 100 : 0;
//
//		// 301 - 400
//		charge += 5.63 * (unitTo >= 100 ? 100 : unitTo);
//		unitTo = unitTo > 100 ? unitTo - 100 : 0;
//
//		// 401 - 600
//		charge += 8.70 * (unitTo >= 200 ? 200 : unitTo);
//		unitTo = unitTo > 200 ? unitTo - 200 : 0;
//
//		// 600 - *
//		charge += 9.98 * (unitTo >= 1 ? unitTo : 0);
		
		
		return charge;
	}
}
