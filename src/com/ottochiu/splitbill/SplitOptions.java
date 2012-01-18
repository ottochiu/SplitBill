package com.ottochiu.splitbill;

import java.math.BigDecimal;
import java.text.NumberFormat;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

public class SplitOptions extends Activity implements TextWatcher {

	private int parties;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.split_options);

        TextView partiesView = (TextView) findViewById(R.id.parties);
        parties = Integer.parseInt(partiesView.getText().toString());
        partiesView.addTextChangedListener(this);
        afterTextChanged(partiesView.getEditableText());
	}
	
	public void splitMethodChanged(View v) {
		View perPartyCost = findViewById(R.id.perPartyCost);
		View splitUneven = findViewById(R.id.viewSplitUneven);
		
		// split evenly means a simple division.
		if (((CheckBox) v).isChecked()) {
			afterTextChanged(((TextView) findViewById(R.id.parties)).getEditableText());
			perPartyCost.setVisibility(View.VISIBLE);
			// remove items from view
			splitUneven.setVisibility(View.GONE);
		} else {
			// varying split means for each party, calculate the split.
			// add items to view			
			splitUneven.setVisibility(View.VISIBLE);			
			perPartyCost.setVisibility(View.GONE);
		}
		
	}

	public void afterTextChanged(Editable s) {
		NumberFormat f = NumberFormat.getCurrencyInstance();
		double total = ((AppData) getApplication()).total;
		
		try {
			parties = Integer.parseInt(s.toString());
		} catch (NumberFormatException e) {
			// do nothing to the parties value
		}

		// update according to whether cost is split evenly.
		if (((CheckBox) findViewById(R.id.splitEven)).isChecked()) {
			((TextView) findViewById(R.id.perPartyCost)).setText(
					String.format("%s / %d = %s",
							f.format(total), parties, 
							f.format(new BigDecimal(total / parties).setScale(2, BigDecimal.ROUND_CEILING))));
		} else {
			// calculate amount for each party
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	public void onTextChanged(CharSequence s, int start, int before, int count) { }
}
