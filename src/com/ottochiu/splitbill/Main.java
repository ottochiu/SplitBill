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
        ChangeListener listener = new ChangeListener(
        		subtotal,
        		tax, taxPercent,
        		tip, tipOption, tipPercent, 
        		total);
        
        subtotal.addTextChangedListener(listener);
        tax.addTextChangedListener(listener);
        
        // Listen to tip option change
        tipOption.setOnCheckedChangeListener(listener);

        
        // Tip amount and percent listeners
        
        TipListener amountListener = new AmountListener();
        tip.addTextChangedListener(amountListener);
        tip.setOnFocusChangeListener(amountListener);
        
        
        TipListener percentListener = new PercentListener();
        tipPercent.addTextChangedListener(percentListener);
        tipPercent.setOnFocusChangeListener(percentListener);
    }
}

