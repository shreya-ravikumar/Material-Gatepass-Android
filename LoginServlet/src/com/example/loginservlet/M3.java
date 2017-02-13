package com.example.loginservlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ActionBar;
import android.app.Activity;
public class M3 extends ListActivity {
 
	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Session session;
	String [] Android1;
	
	
	String[] testDB(){
	//static final String[] Android =	new String[] { "CupCake", "Donut", "Froyo", "GingerBread","HoneyComb","Ice-Cream Sandwich","Jelly-Bean"};
	
	try{
		 String user_name;
		
		System.out.println(session.UserLoggedIn());
		 user_name=session.UserLoggedIn();
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		 Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Set connection : URL : jdbc:mysql://172.16.50.70:3306/material_gatepass");
         Connection con = DriverManager.getConnection(url,user,pass);
		 Statement st = con.createStatement();
		
		 ResultSet rs1 = st.executeQuery("select count(*) from request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING'");
		 rs1.next();
		 int rowcount = rs1.getInt(1);
		 System.out.println(rowcount);
		 
		 if(rowcount>0)
		 {   String  [] Android = new String[rowcount];
		     ResultSet rs2 = st.executeQuery("select r.material_id from request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING'");
			 int i=0;
			 while(rs2.next())
			 { Android[i]=rs2.getString(1);
			   System.out.println(Android[i]);
			  i++;
			 }
            con.close();
		     rs2.close();
		     rs1.close();
		     return Android;
		 }
		 else
		 {   String  [] Android = new String[1];
			 Android[0] = "No Approvals"; 
			 rs1.close();
		     con.close();
		     return Android;
		 }
		
		 
	}
			 catch (Exception e){
	  	     e.printStackTrace();
		  
	       }
	return null;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());
        String [] Android1=testDB();
        setListAdapter(new ListAct(this, Android1));
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		String selectedValue = (String) getListAdapter().getItem(position);
		
		if(selectedValue.equals("No Approvals"))
		{ Intent myIntent = new Intent(M3.this, LoginSuccess.class);   
       	  startActivity(myIntent);
		}
		else
		{ Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		  Intent myIntent = new Intent(M3.this, Approve.class);   
		  myIntent.putExtra("SV", selectedValue);    
		  startActivity(myIntent);
		}
	}
	
	/**public boolean onOptionsItemSelected(MenuItem item) {
   	    switch (item.getItemId()) {
   	    case android.R.id.home:
   	        //NavUtils.navigateUpFromSameTask(this);
   	    	finish();
   	        return true;
   	    }
   	    return super.onOptionsItemSelected(item);
   	 }**/
	
	public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
   	    case R.id.home:
   	       	finish();
   	    	Intent myIntent = new Intent(M3.this, Success.class);  
   	    	startActivity(myIntent);
   	        return true;
   	  
   	   case R.id.Logout:    
			finish();
			session.logoutUser();
		    Toast.makeText(getApplicationContext(),"Successfully Logged Out!", Toast.LENGTH_SHORT).show();
		    System.out.println(session.isUserLoggedIn());
			return true;
			
   	   case R.id.back:
	    	finish();
	    	Intent myIntent2 = new Intent(M3.this, Success.class);  
   	    	startActivity(myIntent2);
	    	return true;

		}
		return super.onOptionsItemSelected(item);
	}
}
