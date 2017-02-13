package com.example.loginservlet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Not2 extends Activity {

	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Session session;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xml);
		
		/*ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        
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
			 
			 ResultSet r1 = st.executeQuery("select count(*) from users u, approve_pass a, request_pass r  where u.staffid IN (select d.staffid_dh from users u, approve_pass a, division_details d where a.approve_status='YES' and a.staffid=u.staffid and u.userid= '"+user_name+"' and u.divisionid=d.divisionid) and a.staffid IN (select distinct a.staffid from approve_pass a, users u where u.userid= '"+user_name+"' and a.staffid=u.staffid) and a.approve_status='YES' and a.material_id=r.material_id");
			 r1.next();
			 int rowcount1 = r1.getInt(1);
			 
			 ResultSet r2 = st.executeQuery("select count(*) from users u, request_pass r, approve_pass a  where u.staffid IN (select d.staffid_dh from users u, approve_pass a, division_details d where a.approve_status='NO' and a.staffid=u.staffid and u.userid= '"+user_name+"' and u.divisionid=d.divisionid) and a.staffid IN (select distinct a.staffid from approve_pass a, users u where u.userid= '"+user_name+"' and a.staffid=u.staffid) and a.approve_status='NO' and a.material_id=r.material_id");
			 r2.next();
			 int rowcount2 = r2.getInt(1);
			 
			 ResultSet r3 = st.executeQuery("select count(*) from users u, request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING' and r.staffid=u.staffid");
			 r3.next();
			 int rowcount3 = r3.getInt(1);
			 
			 if( rowcount1>0 || rowcount2 >0 || rowcount3>0)
			 {
			 ResultSet rs1 = st.executeQuery("select u.firstname, u.lastname, a.material_id, a.approval_id, r.material_desc from users u, approve_pass a, request_pass r  where u.staffid IN (select d.staffid_dh from users u, approve_pass a, division_details d where a.approve_status='YES' and a.staffid=u.staffid and u.userid= '"+user_name+"' and u.divisionid=d.divisionid) and a.staffid IN (select distinct a.staffid from approve_pass a, users u where u.userid= '"+user_name+"' and a.staffid=u.staffid) and a.approve_status='YES' and a.material_id=r.material_id");
			 ResultSetMetaData rsnd = rs1.getMetaData();
			 			 
			 while(rs1.next()) {
				 result += " ► Your material  " + rs1.getString(3) + ",  " + rs1.getString(5) + " with approval ID ";
				 result += rs1.getString(4) + " is approved by " ;
				 result +=  rs1.getString(1) + " " + rs1.getString(2) +  "\n\n";		
			 }
			 
			 
			 ResultSet rs2 = st.executeQuery("select u.firstname, u.lastname, a.material_id, a.approval_id, r.material_desc from users u, request_pass r, approve_pass a  where u.staffid IN (select d.staffid_dh from users u, approve_pass a, division_details d where a.approve_status='NO' and a.staffid=u.staffid and u.userid= '"+user_name+"' and u.divisionid=d.divisionid) and a.staffid IN (select distinct a.staffid from approve_pass a, users u where u.userid= '"+user_name+"' and a.staffid=u.staffid) and a.approve_status='NO' and a.material_id=r.material_id");
			 while(rs2.next()) {
				 result += " ► The material " + rs2.getString(3) + ",  " + rs2.getString(5) + " with approval ID ";
				 result += rs2.getString(4) + " requested by you for approval has not been approved by " ;
				 result +=  rs2.getString(1) + " " + rs2.getString(2) +  "\n\n";			
			 }
			 
			 ResultSet rs3 = st.executeQuery("select u.firstname, u.lastname, r.material_id, a.approval_id, r.material_desc from users u, request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING' and r.staffid=u.staffid");
			 while(rs3.next()) {
				 result += " ► A material  " + rs3.getString(3) + ",  " + rs3.getString(5) + " with approval ID ";
				 result += rs3.getString(4) + " is yet to be approved which has been requested by " ;
				 result += rs3.getString(1) + " " + rs3.getString(2) + "\n\n";		
			 }
			  
			 tv.setText(result);
			 rs3.close();
			 rs2.close();
			 rs1.close();
			}
			 else
			 {
					result  = " No Notifications";
					tv.setText(result);
			 }
			 r3.close();
			 r2.close();
			 r1.close();
			 con.close();
		}
		catch (Exception e){
			e.printStackTrace();
			tv.setText(e.toString());
		}
		
	}
   	
   	/**@Override
   	public boolean onOptionsItemSelected(MenuItem item) {
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
   	    	Intent myIntent = new Intent(Not2.this, Success.class);  
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
