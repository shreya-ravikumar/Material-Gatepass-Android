package com.example.loginservlet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DueItems extends Activity {

	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Session session;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xml);
		
        
		session = new Session(getApplicationContext()); 
		testDB();
		
	}
	
   	public void testDB() {
		TextView tv = (TextView) this.findViewById(R.id.text_view);
		Typeface face= Typeface.createFromAsset(getAssets(), "fonts/formal.ttf");
	    tv.setTypeface(face);
	    //ResultSet rs2 = null;
		try{
			 //SystemDate due = new SystemDate();
			 //System.out.println(due);
			 String user_name;
			 System.out.println(session.UserLoggedIn());
			 user_name=session.UserLoggedIn();
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			 Class.forName("com.mysql.jdbc.Driver");
	         System.out.println("Set connection : URL : jdbc:mysql://172.16.50.70:3306/material_gatepass");
	        // Connection con = DriverManager.getConnection("jdbc:mysql://172.16.50.68:3306/gatepass","test123","test123");
	         Connection con = DriverManager.getConnection(url,user,pass);
			 String result = "";
			 Statement st = con.createStatement();
			 ResultSet rs1 = st.executeQuery("select count(*) from request_pass r, users u where u.userid= '"+user_name+"' and u.staffid=r.staffid and r.m_type='returnable' and r.material_id IN (select material_id from approve_pass where approve_status='YES')");
			 rs1.next();
			 int rowcount = rs1.getInt(1);
			 if(rowcount!=0)
			 {
				 ResultSet rs2 = st.executeQuery("select r.material_id, timestampdiff(day,sysdate(),returndate) as 'days_until_return', r.material_desc from request_pass r, users u where u.userid= '"+user_name+"' and u.staffid=r.staffid and r.m_type='Returnable' and r.material_id IN (select material_id from approve_pass where approve_status='YES')");
			     ResultSetMetaData rsnd = rs2.getMetaData();		 
			     while(rs2.next()) {
				 result += " ► You have a material " + rs2.getString(1) + ", " + rs2.getString(3) +" pending and is due for return in  ";
				 result += rs2.getInt(2) + " days \n\n";		
			    }
			    tv.setText(result);
		        rs2.close();
			 }
			 else
			 { 
				result  = " No Due Items";
			 }
			 tv.setText(result);
			 rs1.close();
			 con.close();
		  }
		catch (Exception e){
			e.printStackTrace();
			tv.setText(e.toString());
		}
		
	}
   	
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
   	        //NavUtils.navigateUpFromSameTask(this);
   	    	finish();
   	    	Intent myIntent = new Intent(DueItems.this, Success.class);  
   	    	startActivity(myIntent);
   	        return true;
   	        
   	   case R.id.back:
	    	finish();
	    	return true;
   	          
   	    case R.id.Logout:    
			finish();
			session.logoutUser();
		    Toast.makeText(getApplicationContext(),"Successfully Logged Out!", Toast.LENGTH_SHORT).show();
		    System.out.println(session.isUserLoggedIn());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
