package com.ottochiu.splitbill;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

class ChangeListener implements TextWatcher, OnCheckedChangeListener {
	ChangeListener(
			EditText subtotal,
			EditText tax,
			TextView taxPercent,
			EditText tip,
			CheckBox tipOption,
			EditText tipPercent,
			TextView total) {
		mSubtotal = subtotal.getEditableText();
		mTax = tax.getEditableText();
		mTaxPercent = taxPercent;
		mTip = tip;
		mTipOption = tipOption;
		mTipPercent = tipPercent;
		mTotal = total;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		try {
			// subtotal or tip was updated. make changes only when subtotal > 0
			if (getSubtotal() > 0)
			{
				System.out.println("subtotal > 0");
				
				// update tax percentage
				setPercent(mTaxPercent, (getTax() / getSubtotal()));

				// update tip
//				TODO
				
				
				// always recalculate total
				updateTotal();
			}
		} catch (NumberFormatException e) {
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// TODO
		System.out.println("Check changed");
	}
	
	
	
	
	
	
	
	static void setPercent(TextView view, double value) {
		view.setText(new DecimalFormat("#0.000%").format(value));
	}
	
	void updateTotal() {
		System.out.printf("%f %f %f\n", getSubtotal(), getTax(), getTip());
		mTotal.setText(NumberFormat.getCurrencyInstance().format(getSubtotal() + getTax() + getTip()));
	}
	
	double getSubtotal() {
		return Double.parseDouble(mSubtotal.toString());
	}
	
	double getTax() {
		return Double.parseDouble(mTax.toString());
	}

	double getTip() {
		return Double.parseDouble(mTip.getText().toString());
	}
	
	
	
	
	static TipListener mLastChange; 
	private Editable mSubtotal;
	private Editable mTax;
	private TextView mTaxPercent;
	private EditText mTip;
	private CheckBox mTipOption;
	private EditText mTipPercent;
	private TextView mTotal;
}


abstract class TipListener implements TextWatcher, OnFocusChangeListener {
	
	abstract void updateTip();
	
	public void onFocusChange(View v, boolean hasFocus) {
		mChangeCandidate = hasFocus;
	}
	
	protected boolean mChangeCandidate = false;
}


class PercentListener extends TipListener {
	void updateTip() {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}

class AmountListener extends TipListener {
	void updateTip() {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}


	

//class BillChangeListener extends ChangeListener implements TextWatcher, OnCheckedChangeListener
//{
//
//	// @Override
//	protected void updateTip() {
//		System.out.println("update tip");
//		double tipBasis = mTipOption.isChecked() ? (getSubtotal() + getTax()) : getSubtotal(); 
//		setPercent(mTipPercent, (getTip() / tipBasis));
//		System.out.println("update tip done");
//	}
//}
//
//
//class TipPercentListener extends ChangeListener implements TextWatcher {
//
//	public void afterTextChanged(Editable arg0) {
//		System.out.println("Tip% change candidate: " + mChangeCandidate);
//		
//		if (mChangeCandidate) {
//			try {
//				// tip percent changed. update the tax amount box
//				mLastChange = this;
//				mLastChange.updateTip();
//				updateTotal();
//			} catch (NumberFormatException e) {
//				// do nothing
//			}
//		}
//	}
//
//	@Override
//	protected void updateTip() {
//		// change the tip amount and the total
//		double tipBasis = mTipOption.isChecked() ? (getSubtotal() + getTax()) : getSubtotal();
//		
//		System.out.printf("Tip, %f %s\n", tipBasis, mTipPercent.getText().toString());
//		mTip.setText(new DecimalFormat("#.##").
//				format(tipBasis * Double.parseDouble(mTipPercent.getText().toString()) / 100));
//	}
//	
//}
//
