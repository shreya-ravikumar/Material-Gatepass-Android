package com.example.loginservlet;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DueItemsTable extends Activity {

	private static final String url="jdbc:mysql://172.16.50.70:3306/material_gatepass";
	private static final String user="root";
	private static final String pass="Walnut01";
	Session session;
	String [][] result1;
	GridView grid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_view);
		
		ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		session = new Session(getApplicationContext()); 
		testDB();
		grid = (GridView)findViewById(R.id.grid);
        ListAdapter adapter = null;
		grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 
        @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
			}
        });
 
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
			 result1 = new String[rowcount][3];
			 if(rowcount!=0)
			 {
				 ResultSet rs2 = st.executeQuery("select r.material_id, timestampdiff(day,sysdate(),returndate) as 'days_until_return', r.material_desc from request_pass r, users u where u.userid= '"+user_name+"' and u.staffid=r.staffid and r.m_type='returnable' and r.material_id IN (select material_id from approve_pass where approve_status='YES')");
			     ResultSetMetaData rsnd = rs2.getMetaData();		 
			     int i=0;
			     while(rs2.next()) {
			    	 result1[i][0]=rs2.getString(1);
			    	 result1[i][1]=rs2.getString(3);
			    	 result1[i][2]=Integer.toString(rs2.getInt(2));
				     result += "You have a material " + rs2.getString(1) + ", " + rs2.getString(3) +" pending and is due for return in  ";
				     result += rs2.getInt(2) + " days \n\n";		
				     i++;
			    }
			    tv.setText(result);
		        rs2.close();
			 }
			 else
			 { 
				result  = " No Due Items";
			 }
			 tv.setText(result);
			 //rs2.close();
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
   	    case android.R.id.home:
   	        //NavUtils.navigateUpFromSameTask(this);
   	    	finish();
   	        return true;
   	    }
   	    return super.onOptionsItemSelected(item);
   	 }
}
