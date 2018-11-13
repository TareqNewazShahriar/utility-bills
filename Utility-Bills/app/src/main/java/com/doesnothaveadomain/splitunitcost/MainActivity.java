package com.doesnothaveadomain.splitunitcost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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
		EditText txtUnitsOf1stFloor = findViewById(R.id.editTextUnitsOf1stFloor);
		EditText txtSubmeterUnitPrev = findViewById(R.id.editTextSubmeterBillPrevInput);
		EditText txtSubmeterUnitCurrent = findViewById(R.id.editTextSubmeterUnitCurrentInput);
		EditText txtElectricityBillOf1stFloor = findViewById(R.id.editTextElectricityBillOf1stFloor);
		EditText txtElectricityBillOf2ndFloor = findViewById(R.id.editTextElectricityBillOf2ndFloor);
		EditText txtElectricityBillOf3rdFloor = findViewById(R.id.editTextElectricityBillOf3rdFloor);
		EditText txtElectricityBillOf4thFloor = findViewById(R.id.editTextElectricityBillOf4thFloor);
		EditText txtWaterBillInput = findViewById(R.id.editTextWaterBillInput);
		EditText txtGasInput = findViewById(R.id.editTextGasInput);
		Switch switchMoveMotorBill  = findViewById(R.id.switchThirdFloorMotorBillTo4th);
		
		if(TextUtils.isEmpty(txtUnitsOf1stFloor.getText())
				|| TextUtils.isEmpty(txtSubmeterUnitPrev.getText())
				|| TextUtils.isEmpty(txtSubmeterUnitCurrent.getText())
				|| TextUtils.isEmpty(txtElectricityBillOf1stFloor.getText())
				|| TextUtils.isEmpty(txtElectricityBillOf2ndFloor.getText())
				|| TextUtils.isEmpty(txtElectricityBillOf3rdFloor.getText())
				|| TextUtils.isEmpty(txtElectricityBillOf4thFloor.getText())
				|| TextUtils.isEmpty(txtWaterBillInput.getText())
				|| TextUtils.isEmpty(txtGasInput.getText()) )
		{
			Toast.makeText(this, "All are required fields.", Toast.LENGTH_LONG).show();
			return;
		}
		
		double unitsOf1stFloor, submeterUnitsOf1stFloor,
				electricityBillOf1stFloor,
				electricityBillOf2ndFloor, electricityBillOf3rdFloor, electricityBillOf4thFloor,
				charge1, charge2, vatOfCharge1, vatOfCharge2;
		int waterBill, doubleStoveGasBill;
		
		electricityBillOf1stFloor = Integer.parseInt(txtElectricityBillOf1stFloor.getText().toString());
		electricityBillOf2ndFloor = Integer.parseInt(txtElectricityBillOf2ndFloor.getText().toString());
		electricityBillOf3rdFloor = Integer.parseInt(txtElectricityBillOf3rdFloor.getText().toString());
		electricityBillOf4thFloor = Integer.parseInt(txtElectricityBillOf4thFloor.getText().toString());
		
		waterBill = Integer.parseInt(txtWaterBillInput.getText().toString());
		doubleStoveGasBill = Integer.parseInt(txtGasInput.getText().toString());
		
		unitsOf1stFloor = Double.parseDouble(txtUnitsOf1stFloor.getText().toString());
		submeterUnitsOf1stFloor = Double.parseDouble(txtSubmeterUnitCurrent.getText().toString()) - Double.parseDouble(txtSubmeterUnitPrev.getText().toString());
		
		charge1 = calc(1, unitsOf1stFloor - submeterUnitsOf1stFloor, unitsOf1stFloor);
		charge2 = calc(unitsOf1stFloor - submeterUnitsOf1stFloor + 1, unitsOf1stFloor, unitsOf1stFloor);
		
		sendToDetailsView(unitsOf1stFloor,
				submeterUnitsOf1stFloor,
				charge1,
				charge2,
				electricityBillOf1stFloor,
				electricityBillOf2ndFloor,
				electricityBillOf3rdFloor,
				electricityBillOf4thFloor,
				waterBill,
				doubleStoveGasBill,
				switchMoveMotorBill.isChecked());
	}
	
	private void sendToDetailsView(double unitsOf1stFloor,
	                               double submeterUnitsOf1stFloor,
	                               double chargeOf1stFloor,
	                               double chargeOf1stFloorSubmeter,
	                               double electricityBillOf1stFloor,
	                               double electricityBillOf2ndFloor,
	                               double electricityBillOf3rdFloor,
	                               double electricityBillOf4thFloor,
	                               int waterBill,
	                               int gasBill,
	                               boolean moveMotorBill)
	{
		Intent intent = new Intent(this, BillDetailsActivity.class);
		
		intent.putExtra("unitsOf1stFloor", unitsOf1stFloor);
		intent.putExtra("submeterUnitsOf1stFloor", submeterUnitsOf1stFloor);
		intent.putExtra("chargeOf1stFloor", chargeOf1stFloor);
		intent.putExtra("chargeOf1stFloorSubmeter", chargeOf1stFloorSubmeter);
		
		intent.putExtra("electricityBillOf1stFloor", electricityBillOf1stFloor);
		intent.putExtra("electricityBillOf2ndFloor", electricityBillOf2ndFloor);
		intent.putExtra("electricityBillOf3rdFloor", electricityBillOf3rdFloor);
		intent.putExtra("electricityBillOf4thFloor", electricityBillOf4thFloor);
		
		intent.putExtra("waterBill", waterBill);
		intent.putExtra("gasBill", gasBill);
		
		intent.putExtra("Move3rdFloorMotorBillTo4th", moveMotorBill);
		
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
