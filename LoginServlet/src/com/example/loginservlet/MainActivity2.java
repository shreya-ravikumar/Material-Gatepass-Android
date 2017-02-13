package com.example.loginservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends Activity {
    EditText e1,e2,e3;
    String flag;
    ProgressDialog dialog = null;
    TextView tv;
    //Context c;
  
    Session session;
    
    private final HttpClient httpclient = new DefaultHttpClient();

    final HttpParams params = httpclient.getParams();
    HttpResponse response;
   

    @Override
    protected void onCreate(Bundle savedInstanceState)     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=(Button)findViewById(R.id.button1);
        e1=(EditText)findViewById(R.id.editText1);
        e2=(EditText)findViewById(R.id.editText2);
        e3=(EditText)findViewById(R.id.editText3);
        session = new Session(getApplicationContext()); 
       
    }

public void onClick(View view) {
    final String s1=e1.getText().toString();
    final String s2=e2.getText().toString();
    final String s3=e3.getText().toString();
    session.createUserLoginSession(s1);
    new MyAsyncTask().execute(s1,s2,s3);
    
  }

/** @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }**/
    
    private class MyAsyncTask extends AsyncTask<String, Integer, String>{
   

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s=postData(params);
            //String o=postData2(params);
            if(!s.trim().equals("Success"))
            	return s;
            else
            {
            	String o=postData2(params);
            	return o;
            }
        }

        protected void onPostExecute(String result2){
            System.out.println(result2);
            System.out.println(session.isUserLoggedIn());
            System.out.println(session.UserLoggedIn());
            if(result2.trim().equals("Verification Successful"))
            {   
            	finish();
            	Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity2.this, Success.class);
                 startActivity(intent);
            }
            else
            {
            	Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                startActivity(intent);
            }
        }
     

        public String postData(String value[]) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://172.16.50.70:8080/material_gatepass/ValidateAndroid");
         //   HttpPost httppost = new HttpPost("http://10.0.2.2:8000/HelloAndroid/ValidateLogin");
             String origresponseText="";
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("param1",value[0]));
                nameValuePairs.add(new BasicNameValuePair("param2", value[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
         /* execute */
                HttpResponse response = httpclient.execute(httppost);
                  HttpEntity rp = response.getEntity();
                 origresponseText=readContent(response);
            } 
      catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } 
      catch (IOException e) {
                // TODO Auto-generated catch block
            }
            String responseText = origresponseText.substring(7, origresponseText.length());
            return responseText;
        }
       

    }
    String readContent(HttpResponse response)
    {
        String text = "";
        InputStream in =null;
         
        try {
            in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                  sb.append(line + "\n");
                }
                text = sb.toString();
        } catch (IllegalStateException e) {
            e.printStackTrace();
           
        } catch (IOException e) {
              e.printStackTrace();
        }
        finally {
            try {

              in.close();
            } catch (Exception ex) {
            }
            }

return text;
    }
    
    
    
    private String postData2(String value[]) {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
    // HttpPost httppost = new HttpPost("http://172.16.50.68:8000/OTPTest/VerifyOTP");
	HttpPost httppost = new HttpPost("http://172.16.50.70:8080/OTPTest/VerifyOTP");
         String origresponseText="";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("param1",value[0]));
            nameValuePairs.add(new BasicNameValuePair("otp",value[2]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
     /* execute */
            HttpResponse response = httpclient.execute(httppost);
             HttpEntity rp = response.getEntity();
             origresponseText=readContent(response);
        } 
  catch (ClientProtocolException e) {
	  e.printStackTrace();
            // TODO Auto-generated catch block
        } 
  catch (IOException e) {
	  e.printStackTrace();
            // TODO Auto-generated catch block
        }
        String responseText = origresponseText.substring(7, origresponseText.length());
        return responseText;
    }
   
}
