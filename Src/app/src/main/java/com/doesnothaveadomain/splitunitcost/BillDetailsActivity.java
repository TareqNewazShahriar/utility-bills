package com.doesnothaveadomain.splitunitcost;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class BillDetailsActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_details);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
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
}
