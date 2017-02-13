package com.example.loginservlet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.sql.Statement;

import org.w3c.dom.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class MaterialApprove extends Activity {

	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Document doc = null;
	Session session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xml);
		
		ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		session = new Session(getApplicationContext()); 
		testDB();
		
	}
	
  	public void testDB() {
  		
		final TextView tv = (TextView) this.findViewById(R.id.text_view);
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
	        // Connection con = DriverManager.getConnection("jdbc:mysql://172.16.50.68:3306/gatepass","test123","test123");
	         Connection con = DriverManager.getConnection(url,user,pass);
			 String result = "";
			 Statement st = con.createStatement();
			 ResultSet rs1 = st.executeQuery("select count(*) from request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING'");
			 ResultSetMetaData rsnd1 = rs1.getMetaData();	
			 rs1.next();
			 int rowcount = rs1.getInt(1);
			 
			 if(rowcount!=0)
			 {
			     result += "There are " + rs1.getInt(1) + " materials waiting for approval\n\n";
			     ResultSet rs2 = st.executeQuery("select r.material_id, r.material_desc from request_pass r, approve_pass a where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and a.approve_status='PENDING'");
				 ResultSetMetaData rsnd2 = rs2.getMetaData();
				// doc = JDBCUtil.toDocument(rs2);
				
				 
				 
                 con.close();
			     rs2.close();
			     rs1.close();
			 }
			 else
			 {
				 result = "There are no materails to approve\n"; 
			 }
			// tv.setText(result);
			 //System.out.println(JDBCUtil.serialize(doc));
			 rs1.close();
		     con.close();
		}
				 catch (Exception e){
		  	     e.printStackTrace();
			     tv.setText(e.toString());
		       }
		
	        }


	@Override
   	public boolean onOptionsItemSelected(MenuItem item) {
   	    switch (item.getItemId()) {
   	    case R.id.home:
   	        //NavUtils.navigateUpFromSameTask(this);
   	    	finish();
   	        return true;
   	    }
   	    return super.onOptionsItemSelected(item);
   	 }
}

