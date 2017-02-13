package com.example.loginservlet;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LoginSuccess extends Activity {
	 Session session;
	 Button imageButton1;
	 Button imageButton2;
	 Button imageButton3;
	 Button imageButton4;
	 Button imageButton5;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.successlogin);
	  //imageButton1 = (Button) findViewById(R.id.button1);
	 //imageButton1.setFocusableInTouchMode(true);
	  addListenerOnButton();
	  session = new Session(getApplicationContext());
	 }
	 

	 public void addListenerOnButton() {

	  imageButton1 = (Button) findViewById(R.id.button1);
	  imageButton2 = (Button) findViewById(R.id.button2);
	  imageButton3 = (Button) findViewById(R.id.button3);
	  imageButton4 = (Button) findViewById(R.id.button4);
	  imageButton5 = (Button) findViewById(R.id.button5);
	  
	   
	  imageButton1.setOnClickListener(new OnClickListener() {
	   @Override
	   public void onClick(View arg0) {
	    imageButton1.setFocusableInTouchMode(false);
	    imageButton1.setFocusable(false);
	    Intent intent = new Intent(LoginSuccess.this, Not2.class);
        startActivity(intent);
	   }
	  });
	  
	  imageButton2.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
		    imageButton2.setFocusableInTouchMode(false);
		    imageButton2.setFocusable(false);
		    Intent intent = new Intent(LoginSuccess.this, PendingApprovals.class);
	        startActivity(intent);
		   }
		  });
	  
	  imageButton3.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
		    imageButton3.setFocusableInTouchMode(false);
		    imageButton3.setFocusable(false);
		    Intent intent = new Intent(LoginSuccess.this, DueItems.class);
	        startActivity(intent);
		   }
		  });
	  
	  imageButton4.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
		    imageButton4.setFocusableInTouchMode(false);
		    imageButton4.setFocusable(false);
		    Intent intent = new Intent(LoginSuccess.this, M3.class);
	        startActivity(intent);
		   }
		  });
	  
	  imageButton5.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
		    imageButton5.setFocusableInTouchMode(false);
		    imageButton5.setFocusable(false);
		    session.logoutUser();
		    Toast.makeText(getApplicationContext(),"Successfully Logged Out!", Toast.LENGTH_SHORT).show();
		    System.out.println(session.isUserLoggedIn());
		   }
		  });
	 }

	}