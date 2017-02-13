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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OTPVerification extends Activity {
	EditText e1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.successlogin);
		e1=(EditText)findViewById(R.id.editText1);
		
}
	
	
	public void validate(View view)
	{    
		final String s1=e1.getText().toString();
		MyAsyncTask task = new MyAsyncTask();
		//Toast.makeText(getApplicationContext(), "TESTING...!", Toast.LENGTH_LONG).show();
		task.execute(new String[] {s1});
	}
	
	private class MyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try
			{
			String s=null;
			for (String param : params)
			{ s=postData(param);
			}
			return s;
			}
			catch(Exception e) {
			
			e.printStackTrace();
			}
			return null;
		}
				
		private String postData(String string) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.16.50.70:8080/material_gatepass/VerifyOTP");
             String origresponseText="";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("otp",string));
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

		private String readContent(HttpResponse response) {
			// TODO Auto-generated method stub
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
		            	ex.printStackTrace();
		              }
		            }

		return text;
	}
			
		protected void onPostExecute(String result) {
	        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
	    }
		
	}
	
	/**@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Logout) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}**/
}
