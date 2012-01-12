package com.ottochiu.splitbill;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // Setup progress bar
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setProgress(0);
        
        TextView progressText = (TextView) findViewById(R.id.textProgressBar);
        progressText.setText("0%");
        
        EditText subtotal = (EditText) findViewById(R.id.subtotal);
        EditText tax = (EditText) findViewById(R.id.tax);
        TextView taxPercent = (TextView) findViewById(R.id.textTaxPercent);
        EditText tip = (EditText) findViewById(R.id.tip);
        CheckBox tipOption = (CheckBox) findViewById(R.id.tipOption);
        EditText tipPercent = (EditText) findViewById(R.id.tipPercent);
        TextView total = (TextView) findViewById(R.id.textTotalPrice);
        
        // Setup edit text listeners        
        BillChangeListener listener = new BillChangeListener(
        		subtotal,
        		tax, taxPercent,
        		tip, tipOption, tipPercent, 
        		total);
        
        subtotal.addTextChangedListener(listener);
        tax.addTextChangedListener(listener);
        tip.addTextChangedListener(listener);
        tip.setOnFocusChangeListener(listener);
        
        // Listen to tip option change
        tipOption.setOnCheckedChangeListener(listener);
        
        
        
        TipPercentListener tipListener = new TipPercentListener(
        		subtotal,
        		tax, taxPercent,
        		tip, tipOption, tipPercent, 
        		total);
        
        // Listen to tip percent change
        tipPercent.addTextChangedListener(tipListener);
        tipPercent.setOnFocusChangeListener(tipListener);
    }
}


abstract class ChangeListener implements OnFocusChangeListener {
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
		mChangeCandidate = false;
	}

	protected double stripDollarSymbol(String value) {
		value.replaceAll("$", "*");
		return Double.parseDouble(value);
	}
	
	protected void setPercent(TextView view, double value) {
		view.setText(new DecimalFormat("#0.000%").format(value));
	}
	
	protected double getSubtotal() {
		return stripDollarSymbol(mSubtotal.toString());
	}
	
	protected double getTax() {
		return stripDollarSymbol(mTax.toString());
	}

	protected double getTip() {
		return stripDollarSymbol(mTip.getText().toString());
	}
	
	protected void updateTotal() {
		System.out.printf("%f %f %f\n", getSubtotal(), getTax(), getTip());
		mTotal.setText(NumberFormat.getCurrencyInstance().format(getSubtotal() + getTax() + getTip()));
	}
	
	abstract protected void updateTip();

	public void onFocusChange(View v, boolean hasFocus) {
		mChangeCandidate = hasFocus;
	}
	
	static protected ChangeListener mLastChange; // need to be initialized by a subclass
	protected Editable mSubtotal;
	protected Editable mTax;
	protected TextView mTaxPercent;
	protected EditText mTip;
	protected CheckBox mTipOption;
	protected EditText mTipPercent;
	protected TextView mTotal;
	protected boolean mChangeCandidate;
}

	

class BillChangeListener extends ChangeListener implements TextWatcher, OnCheckedChangeListener
{
	public String toString() { return "BillChangeListener"; }
	
	BillChangeListener(
			EditText subtotal,
			EditText tax,
			TextView taxPercent,
			EditText tip,
			CheckBox tipOption,
			EditText tipPercent,
			TextView total) {
		super(subtotal, tax, taxPercent, tip, tipOption, tipPercent, total);
		mLastChange = this;
	}
	
	// @Override
	public void afterTextChanged(Editable s) {
		if ((s == mTip.getEditableText()) && mChangeCandidate)
		{
			mLastChange = this;
		}
		
		try {
			if (getSubtotal() > 0)
			{
				System.out.println("subtotal > 0");
				
				// update tax percentage
				setPercent(mTaxPercent, (getTax() / getSubtotal()));

				System.out.println("percent done");
				
				System.out.println(mLastChange);
				
				// update tip percentage text (this class deals with absolute amount)
				mLastChange.updateTip();
				
				System.out.println("tip done");
				
				// always recalculate total
				updateTotal();
				
				System.out.println("total done");
			}
		} catch (NumberFormatException e) {
		}
	}

	// @Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// do nothing
	}

	// @Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}
	
	// @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		try {
			// Always change tip slider for checkbox changes (assume tip amount is fixed)
			mLastChange.updateTip();
		} catch (NumberFormatException e) {
		}
	}

	// @Override
	protected void updateTip() {
		System.out.println("update tip");
		double tipBasis = mTipOption.isChecked() ? (getSubtotal() + getTax()) : getSubtotal(); 
		setPercent(mTipPercent, (getTip() / tipBasis));
		System.out.println("update tip done");
	}
}


class TipPercentListener extends ChangeListener implements TextWatcher {
	TipPercentListener(
			EditText subtotal,
			EditText tax,
			TextView taxPercent,
			EditText tip,
			CheckBox tipOption,
			EditText tipPercent,
			TextView total) {
		super(subtotal, tax, taxPercent, tip, tipOption, tipPercent, total);
	}

	public void afterTextChanged(Editable arg0) {
		System.out.println("Tip% change candidate: " + mChangeCandidate);
		
		if (mChangeCandidate) {
			try {
				// tip percent changed. update the tax amount box
				mLastChange = this;
				mLastChange.updateTip();
				updateTotal();
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// do nothing
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// do nothing
	}

	@Override
	protected void updateTip() {
		// change the tip amount and the total
		double tipBasis = mTipOption.isChecked() ? (getSubtotal() + getTax()) : getSubtotal();
		
		System.out.printf("Tip, %f %s\n", tipBasis, mTipPercent.getText().toString());
		mTip.setText(new DecimalFormat("#.##").
				format(tipBasis * Double.parseDouble(mTipPercent.getText().toString()) / 100));
	}
	
	public String toString() { return "TipListener"; }
}

