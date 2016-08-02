package com.example.techjini.signuppoc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

public class HttpUtils {
	
	private static Application app;
	private static int response_code;
	private static int timeoutConnection = 30000;

	public static void init(Application app) {
		HttpUtils.app = app;
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

		
	public static String doGet(String api_url){
		
		InputStream is = null;
		String result = "";
		response_code = 0;
		
		if (!api_url.trim().equals("")) {
			//http get
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(api_url);
				
				//set connection TTL
		        HttpParams httpParameters = new BasicHttpParams();
		        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		        
		        httpget.setParams(httpParameters);
				
				HttpResponse response = httpclient.execute(httpget);
				response_code = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			    
			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result=sb.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getValueFromJObj(JSONObject jb, String key) throws JSONException
	{
		String returnstring = "";
		if (!jb.isNull(key)){
			returnstring = jb.getString(key);
		}
		return returnstring;
	}
	
	public static String getValueFromMap(HashMap<String, String> map, String key)  
	{
		String returnstring = "";
		if (map.containsKey(key)){
			returnstring = map.get(key);
		}
		return returnstring;
	}
	
	public static int getResponseCode() {
		return response_code;
	}
}