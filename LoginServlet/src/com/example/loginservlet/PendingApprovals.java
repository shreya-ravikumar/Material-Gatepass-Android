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

public class PendingApprovals extends Activity {

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
		try{
			 String user_name;
			 System.out.println(session.UserLoggedIn());
			 user_name=session.UserLoggedIn();
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			 Class.forName("com.mysql.jdbc.Driver");
	         System.out.println("Set connection : URL : jdbc:mysql://172.16.50.70:3306/material_gatepass");
	         Connection con = DriverManager.getConnection(url,user,pass);
			 String result = "";
			 Statement st = con.createStatement();
			 ResultSet rs1 = st.executeQuery("select count(*) from approve_pass a, request_pass r, users u where a.approve_status='PENDING' and a.staffid=u.staffid and u.userid= '"+user_name+"' and a.material_id=r.material_id");
			 rs1.next();
			 int rowcount = rs1.getInt(1);
			 if(rowcount!=0)
			 {
			 ResultSet rs = st.executeQuery("select a.material_id, r.material_desc from approve_pass a, request_pass r, users u where a.approve_status='PENDING' and a.staffid=u.staffid and u.userid= '"+user_name+"' and a.material_id=r.material_id");
			 ResultSetMetaData rsnd = rs.getMetaData();
			 			 
			 while(rs.next()) {
				 result += " ► You have a material with material ID " + rs.getString(1) + " - " + rs.getString(2) + " waiting for approval.\n\n";		
			 }
			 rs.close();
			 }
			 else
			 { 
					result  = " No Pending Approvals";					
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
   	    
   	    	finish();
   	    	Intent myIntent = new Intent(PendingApprovals.this, Success.class);  
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
	    	return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
