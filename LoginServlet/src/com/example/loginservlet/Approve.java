package com.example.loginservlet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

public class Approve extends Activity {

	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Session session;
	private RadioGroup radioSGroup;
	private RadioButton radioselectButton;
	private Button button1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approval);
        
        session = new Session(getApplicationContext()); 
		testDB();
		
	}
	
  	public void testDB() {
		TextView tv = (TextView) this.findViewById(R.id.text_view);
		Typeface face= Typeface.createFromAsset(getAssets(), "fonts/formal.ttf");
	    tv.setTypeface(face);
		try{
			 Intent intent = getIntent();
	         Bundle bundle = intent.getExtras();
	         final String selectedvalue= bundle.getString("SV");
			 String user_name;
			 System.out.println(session.UserLoggedIn());
			 user_name=session.UserLoggedIn();
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			 Class.forName("com.mysql.jdbc.Driver");
	         System.out.println("Set connection : URL : jdbc:mysql://172.16.50.70:3306/material_gatepass");
	         final Connection con = DriverManager.getConnection(url,user,pass);
			 String result = "";
			 final Statement st = con.createStatement();
		     ResultSet rs = st.executeQuery("select u.firstname,u.lastname, r.material_id, r.source, r.destination, r.m_type, r.returndate, r.currentdate, r.material_desc from request_pass r, approve_pass a, users u where r.staffid IN (select u.staffid from users u, division_details d where u.divisionid=d.divisionid and d.staffid_dh IN (select u.staffid from users u where u.userid='"+user_name+"')) and r.material_id=a.material_id and r.material_id='"+selectedvalue+"'and a.approve_status='PENDING' and u.staffid=r.staffid");
					  System.out.println("Set connection : URL");
			          while(rs.next())
				      {
			          result += "Name of Intender : " + rs.getString(1) + " " + rs.getString(2) + "\n";	
				      result += "Material ID : " + rs.getString(3) + "\n";
				      result += "Description : " + rs.getString(9) + "\n";
				      result += "Source : " + rs.getString(4) + "\n";
				      result += "Destination : " + rs.getString(5) + "\n";
				      result += "Material type : " + rs.getString(6) + "\n";
				      result += "Request date : " + rs.getString(8) + "\n";
				      result += "Return date : " + rs.getString(7) + "\n\n";
				      
				        radioSGroup = (RadioGroup) findViewById(R.id.radioGroup);
						button1 = (Button) findViewById(R.id.button1);
						button1.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
    					        // get selected radio button from radioGroup
								int selectedId = radioSGroup.getCheckedRadioButtonId();
     							// find the radiobutton by returned id
							    radioselectButton = (RadioButton) findViewById(selectedId);
                                if(radioselectButton.getText().equals("Approve"))
                                {
                                	Toast.makeText(Approve.this,"Item Approved", Toast.LENGTH_SHORT).show();
                                	try {
                                		System.out.println(selectedvalue);
                                		String query="update approve_pass set approve_status='YES' where material_id='"+selectedvalue+"'";
										PreparedStatement ps = con.prepareStatement(query);
										ps.executeUpdate();
										
												
									} catch (SQLException e) {
										
										e.printStackTrace();
									} 
                                	Intent intent = new Intent(Approve.this, M3.class);
                                    startActivity(intent);	
                               }
                                else
                                { 
                                	Toast.makeText(Approve.this,"Item Declined", Toast.LENGTH_SHORT).show(); 
                                	try {
                                		System.out.println(selectedvalue);
                                		String query="update approve_pass set approve_status='NO' where material_id='"+selectedvalue+"'";
										PreparedStatement ps = con.prepareStatement(query);
										ps.executeUpdate();
										
									} catch (SQLException e) {
										
										e.printStackTrace();
									} 
                                	Intent intent = new Intent(Approve.this, M3.class);
                                    startActivity(intent);
                                }
						    }
						});
			        
			        tv.setText(result);
			       } 
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
   	    	Intent myIntent = new Intent(Approve.this, Success.class);  
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
