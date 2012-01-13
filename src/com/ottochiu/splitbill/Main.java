package com.ottochiu.splitbill;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
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
        
        mSubtotal = (EditText) findViewById(R.id.subtotal);
        mTax = (EditText) findViewById(R.id.tax);
        mTaxPercent = (TextView) findViewById(R.id.textTaxPercent);
        mTipAmount = (EditText) findViewById(R.id.tip);
        mTipOption = (CheckBox) findViewById(R.id.tipOption);
        mTipPercent = (EditText) findViewById(R.id.tipPercent);
        mTotal = (TextView) findViewById(R.id.textTotalPrice);
        
        // Setup edit text listeners        
        ChangeListener listener = new ChangeListener();
        
        mSubtotal.addTextChangedListener(listener);
        mTax.addTextChangedListener(listener);
        
        // Listen to tip option change
        mTipOption.setOnCheckedChangeListener(listener);

        
        // Tip amount and percent listeners
        
        TipListener amountListener = new AmountListener();
        mTipAmount.addTextChangedListener(amountListener);
        mTipAmount.setOnFocusChangeListener(amountListener);
        
        mLastChange = amountListener;
        
        TipListener percentListener = new PercentListener();
        mTipPercent.addTextChangedListener(percentListener);
        mTipPercent.setOnFocusChangeListener(percentListener);
    }
    
    static TipListener mLastChange;
    
   	static EditText mSubtotal;
   	static EditText mTax;
   	static TextView mTaxPercent;
   	static EditText mTipAmount;
   	static EditText mTipPercent;
   	static CheckBox mTipOption;
   	static TextView mTotal;
}

