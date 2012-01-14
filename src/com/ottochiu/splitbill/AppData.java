package com.ottochiu.splitbill;

import android.app.Application;

public class AppData extends Application {
	double subtotal;
	double tax;
	double tip;
	double total;
	
	void update(
			double subtotal,
			double tax,
			double tip) {
		this.subtotal = subtotal;
		this.tax = tax;
		this.tip = tip;
		this.total = subtotal + tax + tip;
	}
}
