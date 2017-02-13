package com.example.loginservlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListActLogin extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public ListActLogin(Context context, String[] values) {
		super(context, R.layout.loginlist, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.loginlist, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = values[position];
 
		System.out.println(s);
 
		if (s.equals("Notifications")) {
			imageView.setImageResource(R.drawable.not1);
		} else if (s.equals("Pending Approvals")) {
			imageView.setImageResource(R.drawable.pend);
		} else if (s.equals("Due Items")) {
			imageView.setImageResource(R.drawable.duei);
		}else if (s.equals("Approve Items")) {
			imageView.setImageResource(R.drawable.due);
		}
		return rowView;
	}
	
	
}