package com.example.techjini.signuppoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class FindLocation extends Activity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap;

    private double latitude;
    private double longitude;
    private double lati, longi;

    private LatLng lat;

    private AutoCompleteTextView searchAddress;
    boolean NewJourney = false;

    String str = "", origin = "", destination = "";
    PlacesAutoCompleteAdapter placeAdapter;
    HashMap<String, String> pair;
    ArrayList<String> country;
    Context con = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_location);
        country = new ArrayList<String>();
        placeAdapter = new PlacesAutoCompleteAdapter(this, R.layout.adapter_place_auto_complete);

        pair = new HashMap<String, String>();
        String[] locales = Locale.getISOCountries();


        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            pair.put(obj.getDisplayCountry(), obj.getCountry());
            country.add(obj.getDisplayCountry());
        }

        findViewById(R.id.search).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchAddress.getWindowToken(), 0);
            }
        });

        searchAddress = (AutoCompleteTextView) findViewById(R.id.searchAddress);
        //placeAdapter=new PlacesAutoCompleteAdapter(this,R.layout.list_item);
        searchAddress.setAdapter(placeAdapter);

        searchAddress.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                   /*String[] strLoc = placeAdapter.locationList.get(position).split(",");
                    longitude = Double.parseDouble(strLoc[0]);
                    latitude = Double.parseDouble(strLoc[1]);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16));*//*
*/
                    Intent in = new Intent();
                    in.putExtra("Address", placeAdapter.resultList.get(position));
                    in.putExtra("LATITUDE", latitude);
                    in.putExtra("LONGITUDE", longitude);
                    setResult(RESULT_OK, in);
                    finish();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.okbtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent();
                in.putExtra("Address", str);
                in.putExtra("LATITUDE", latitude);
                in.putExtra("LONGITUDE", longitude);
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS && GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            Dialog d = GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0, new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FindLocation.this.finish();
                }
            });
            d.show();
        }
        initilizeMap();
    }

    private void initilizeMap() {
        if (mGoogleMap == null | latitude==0) {
            try {
                ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void centerMapOnMyLocation() {
        LatLng myLocation = null;
        GPSTracker gps = new GPSTracker(this);

        try {
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                myLocation = new LatLng(latitude, longitude);

                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition arg0) {
                        try {
                            lat = mGoogleMap.getCameraPosition().target;
                            new GetAddress().execute();
                        } catch (RejectedExecutionException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } else {
                showDialogToSettings();
            }

        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDialogToSettings() {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FindLocation.this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        if (mGoogleMap == null) {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
        if (mGoogleMap != null)
            centerMapOnMyLocation();
    }

    private class GetAddress extends AsyncTask<Void, Void, String> {
        GeocoderHelper geo;

        @Override
        protected void onPreExecute() {
            ((TextView) findViewById(R.id.address)).setText("Searching....");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            latitude = lat.latitude;
            longitude = lat.longitude;
            geo = new GeocoderHelper(FindLocation.this, latitude, longitude);

            str = geo.fetchCityName();
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            ((TextView) findViewById(R.id.address)).setText("" + str);
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed() {

        Intent in = new Intent();
        in.putExtra("Address", str);
        in.putExtra("LATITUDE", latitude);
        in.putExtra("LONGITUDE", longitude);
        setResult(RESULT_OK, in);
        super.onBackPressed();
        finish();


    }


}
