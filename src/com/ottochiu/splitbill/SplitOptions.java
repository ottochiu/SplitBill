package com.ottochiu.splitbill;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplitOptions extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.split_options);
        
        // Setup progress bar
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(30);
        ((TextView) findViewById(R.id.textProgressBar)).setText("30%");
	}
}
