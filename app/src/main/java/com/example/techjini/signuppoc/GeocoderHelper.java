package com.example.techjini.signuppoc;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class GeocoderHelper {
    Context ctx;
    double latitude;
    double longitude;
    private static int timeoutConnection = 30000;

    public GeocoderHelper(final Context context, final double LATITUDE, final double LONGITUDE) {
        ctx = context;
        latitude = LATITUDE;
        longitude = LONGITUDE;
    }

    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(GeocoderHelper.class.getName());

    String address = "";

    public String fetchCityName() {
        ;
        try {
            address = getCompleteAddressString(latitude, longitude, ctx);
        } catch (Exception ignored) {
            // after a while, Geocoder start to trhow "Service not availalbe"
            // exception. really weird since it was working before (same device,
            // same Android version etc..

        }

        int addss = address.length();

        if (addss > 1) // i.e., Geocoder succeed
        { // plateNo=address;
            return address;
        } else // i.e., Geocoder failed
        {
            address = fetchCityNameUsingGoogleMap();
            // plateNo=address;
            return address;
        }
    }

    // Geocoder failed :-(
    // Our B Plan : Google Map
    private String fetchCityNameUsingGoogleMap() {
        String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=false&language=en";

        try {
            JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

            JSONArray results = (JSONArray) googleMapResponse.get("results");
            JSONObject result = results.getJSONObject(0);
            address = result.getString("formatted_address");

            if (address.length() > 1) {
                return address;
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return address;
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }

                strAdd = strReturnedAddress.toString();

                Log.w("Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

}