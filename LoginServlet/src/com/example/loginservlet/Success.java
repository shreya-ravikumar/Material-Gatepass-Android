package com.example.loginservlet;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
 
public class Success extends ListActivity {
 
	static final String[] Display= new String[] { "Notifications", "Pending Approvals", "Due Items", "Approve Items"};
    Session session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    
		setListAdapter(new ListActLogin(this, Display)); 
		session = new Session(getApplicationContext());
	}
 
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
		String s = (String) getListAdapter().getItem(position);
		if (s.equals("Notifications")) {
			Intent intent = new Intent(Success.this, Not2.class);
	        startActivity(intent);
			
		} 
		else if (s.equals("Pending Approvals")) {
			Intent intent = new Intent(Success.this, PendingApprovals.class);
	        startActivity(intent);
			
		} else if (s.equals("Due Items")) {
			 Intent intent = new Intent(Success.this, DueItems.class);
		        startActivity(intent);
			
		}else if (s.equals("Approve Items")) {
			Intent intent = new Intent(Success.this, M3.class);
	        startActivity(intent);
			
		} 
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Logout) {
			finish();
			session.logoutUser();
		    Toast.makeText(getApplicationContext(),"Successfully Logged Out!", Toast.LENGTH_SHORT).show();
		    System.out.println(session.isUserLoggedIn());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
