package com.ottochiu.splitbill;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

class ChangeListener implements TextWatcher, OnCheckedChangeListener {
	
	public void afterTextChanged(Editable s) {
		try {
			// subtotal or tip was updated. make changes only when subtotal > 0
			if (getSubtotal() > 0)
			{
				System.out.println("subtotal > 0");
				
				// update tax percentage
				setPercent(Main.mTaxPercent, (getTax() / getSubtotal()));

				// update tip
				Main.mLastChange.updateTip();
				
				// always recalculate total
				updateTotal();
			}
		} catch (NumberFormatException e) {
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println("Check changed");
		Main.mLastChange.updateTip();
	}
	
	static void setPercent(TextView view, double value) {
		view.setText(new DecimalFormat("#0.000%").format(value));
	}
	
	static void updateTotal() {
		System.out.printf("%f %f %f\n", getSubtotal(), getTax(), getTipAmount());
		Main.mTotal.setText(NumberFormat.getCurrencyInstance().format(getSubtotal() + getTax() + getTipAmount()));
	}
	
	static double getSubtotal() {
		return Double.parseDouble(Main.mSubtotal.getText().toString());
	}
	
	static double getTax() {
		return Double.parseDouble(Main.mTax.getText().toString());
	}

	static double getTipAmount() {
		return Double.parseDouble(Main.mTipAmount.getText().toString());
	}
	
	static double getTipPercent() {
		return Double.parseDouble(Main.mTipPercent.getText().toString());
	}
	
	
	static TipListener mLastChange;
}


abstract class TipListener implements TextWatcher, OnFocusChangeListener {
	
	abstract void updateTip();
	
	public void onFocusChange(View v, boolean hasFocus) {
		mChangeCandidate = hasFocus;
	}
	
	public void afterTextChanged(Editable s) {
		if (mChangeCandidate) {
			Main.mLastChange = this;
			updateTip();
			ChangeListener.updateTotal();
		}
	}
	
	private boolean mChangeCandidate = false;
}


class PercentListener extends TipListener {
	void updateTip() {
		double tipBasis = Main.mTipOption.isChecked() ?
				ChangeListener.getSubtotal() + ChangeListener.getTax() :
					ChangeListener.getSubtotal();
					
		double amount = tipBasis * ChangeListener.getTipPercent() / 100;
		
		Main.mTipAmount.setText(new DecimalFormat("#.##").format(amount));
	}


	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}

}

class AmountListener extends TipListener {
	void updateTip() {
		double tipBasis = Main.mTipOption.isChecked() ?
				ChangeListener.getSubtotal() + ChangeListener.getTax() :
					ChangeListener.getSubtotal();
		
		ChangeListener.setPercent(
				Main.mTipPercent, 
				ChangeListener.getTipAmount() / tipBasis);
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}

}
