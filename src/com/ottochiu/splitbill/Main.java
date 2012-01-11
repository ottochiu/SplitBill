package com.ottochiu.splitbill;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
        SeekBar tipSlider = (SeekBar) findViewById(R.id.tipSlider);
        TextView total = (TextView) findViewById(R.id.textTotalPrice);
        
        // Setup edit text listeners        
        ChangeListener listener = new ChangeListener(
        		subtotal,
        		tax, taxPercent,
        		tip, tipOption, tipSlider, 
        		total);
        
        subtotal.addTextChangedListener(listener);
        tax.addTextChangedListener(listener);
        tip.addTextChangedListener(listener);
        
        // Listen to tip option change
        tipOption.setOnCheckedChangeListener(listener);
        
        // Listen to tip slider change
    }
}

class ChangeListener implements TextWatcher, OnCheckedChangeListener
{
	ChangeListener(
			EditText subtotal,
			EditText tax,
			TextView taxPercent,
			EditText tip,
			CheckBox tipOption,
			SeekBar tipSlider,
			TextView total) {
		mSubtotal = subtotal.getEditableText();
		mTax = tax.getEditableText();
		mTaxPercent = taxPercent;
		mTip = tip.getEditableText();
		mTipOption = tipOption;
		mTipSlider = tipSlider;
		mTotal = total;
	}
	
	// @Override
	public void afterTextChanged(Editable s) {
		try {
			double subtotal = getSubtotal();
			double tax = getTax();
			double tip = getTip();

			if (subtotal > 0)
			{
				// always recalculate total
				mTotal.setText(Double.toString(subtotal + tax + tip));

				// update tax percentage
				mTaxPercent.setText(new DecimalFormat("#0.000%").format(tax / subtotal));

				// update tip percentage bar (this is after text change, therefore can always change the tip slider bar)
				updateTip();
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
			updateTip();
		} catch (NumberFormatException e) {
		}
	}
	
	
	
	private double stripDollarSymbol(String value) {
		value.replaceAll("$", "*");
		return Double.parseDouble(value);
	}
	
	private double getSubtotal() {
		return stripDollarSymbol(mSubtotal.toString());
	}
	
	private double getTax() {
		return stripDollarSymbol(mTax.toString());
	}

	private double getTip() {
		return stripDollarSymbol(mTip.toString());
	}
	
	private void updateTip() {
		double tipBasis = mTipOption.isChecked() ? (getSubtotal() + getTax()) : getSubtotal(); 
		mTipSlider.setProgress((int) (getTip() * 100 / tipBasis));
	}
	
	private Editable mSubtotal;
	private Editable mTax;
	private TextView mTaxPercent;
	private Editable mTip;
	private CheckBox mTipOption;
	private SeekBar mTipSlider;
	private TextView mTotal;
}


