package com.doesnothaveadomain.splitunitcost;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AttachCalculateButtonClickEvent();
		
		InputMethodManager imm = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(null, 0);
	}
	
	protected void AttachCalculateButtonClickEvent()
	{
		Button button = findViewById(R.id.buttonCalculate);
		button.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// hides the keyboard
				InputMethodManager imm = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
				
				Show();
			}
		});
	}
	
	protected void Show()
	{
		EditText txtTotalUsage = findViewById(R.id.editTextTotalUsageInput);
		EditText txtSubmeterUnitPrev = findViewById(R.id.editTextSubmeterBillPrevInput);
		EditText txtSubmeterUnitCurrent = findViewById(R.id.editTextSubmeterUnitCurrentInput);
		EditText txtWaterBillInput = findViewById(R.id.editTextWaterBillInput);
		EditText txtGasInput = findViewById(R.id.editTextGasInput);
		
		double totalUsage, totalUsageOf2ndMeter,
				charge1, charge2, totalCharge, vatOfCharge1, vatOfCharge2;
		int waterBill, doubleStoveGasBill;
		
		waterBill = Integer.parseInt(txtWaterBillInput.getText().toString());
		doubleStoveGasBill = Integer.parseInt(txtGasInput.getText().toString());
		
		totalUsage = Double.parseDouble(txtTotalUsage.getText().toString());
		totalUsageOf2ndMeter = Double.parseDouble(txtSubmeterUnitCurrent.getText().toString()) - Double.parseDouble(txtSubmeterUnitPrev.getText().toString());;
		
		charge1 = calc(1, totalUsage - totalUsageOf2ndMeter, totalUsage);
		charge2 = calc(totalUsage - totalUsageOf2ndMeter + 1, totalUsage, totalUsage);
		totalCharge = charge1 + charge2;
		
		vatOfCharge1 = charge1 * 5 / 100;
		vatOfCharge2 = charge2 * 5 / 100;
		charge1 = charge1 + vatOfCharge1;
		charge2 = charge2 + vatOfCharge2;
		
		sendToDetailsView(totalUsage, totalUsageOf2ndMeter, charge1, charge2, waterBill, doubleStoveGasBill);
	}
	
	private void sendToDetailsView(double totalUsage, double totalUsageOf2ndMeter, double charge1, double charge2, int waterBill, int gasBill)
	{
		Intent intent = new Intent(this, BillDetailsActivity.class);
		
		intent.putExtra("totalUsage", totalUsage);
		intent.putExtra("totalUsageOf2ndMeter", totalUsageOf2ndMeter);
		intent.putExtra("charge1", charge1);
		intent.putExtra("charge2", charge2);
		intent.putExtra("waterBill", waterBill);
		intent.putExtra("gasBill", gasBill);
		
		startActivity(intent);
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
		
		return charge;
	}
}
