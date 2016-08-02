package com.example.techjini.signuppoc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    public ArrayList<String> resultList;
    public ArrayList<String> locationList;

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";
    public static String countryCode = "";
    private static final String API_KEY = "AIzaSyAwt44IWiYhx3dNIVrhOz1bUhHeapxXSeE";

    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        SharedPreferences pref = context.getSharedPreferences("com.adodis.android.TaxiApplication", Context.MODE_PRIVATE);
        countryCode = pref.getString("CountryCode", "IN");
//	        Log.e("Ayaz", ""+countryCode);
    }

    @Override
    public int getCount() {
        if (resultList != null) {
            int cnt = resultList.size();
            if (cnt > 0) {
                return resultList.size();
            } else
                return 0;
        } else
            return 0;
//	        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        try {
            return resultList.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    if (resultList != null && !resultList.equals("")) {
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    } else {
                        filterResults.values = "Unable to get results";
                        filterResults.count = 0;
                    }
                }
                return filterResults;
            }

            private ArrayList<String> autocomplete(String input) {
                ArrayList<String> resultList = null;

                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                    // sb.append("?sensor=false");

                    sb.append("?&input=" + URLEncoder.encode(input, "utf8"));
                    sb.append("&components=country:" + countryCode);
                    sb.append("&key=" + API_KEY);
                    System.out.println("URL ---------------: " + sb.toString());

                    URL url = new URL(sb.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error processing Places API URL", e);
                    return resultList;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error connecting to Places API", e);
                    return resultList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return resultList;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                try {
                    // Log.d(TAG, jsonResults.toString());

                    // Create a JSON object hierarchy from the results
                    JSONObject jsonObj = new JSONObject(jsonResults.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    // Extract the Place descriptions from the results
                    resultList = new ArrayList<String>(predsJsonArray.length());
                    locationList=new ArrayList<>(predsJsonArray.length());
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                        //locationList.add();

                    }
                } catch (JSONException e) {
                    Log.e("errorLOg", "Cannot process JSON results", e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return resultList;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                try {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        return filter;
    }
}