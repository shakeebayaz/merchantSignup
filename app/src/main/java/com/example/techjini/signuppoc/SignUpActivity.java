package com.example.techjini.signuppoc;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.techjini.signuppoc.fragment.StoreInformationFragment;
import com.example.techjini.signuppoc.util.Constant;
import com.example.techjini.signuppoc.util.LocationTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class SignUpActivity extends AppCompatActivity {
    private StoreInformationFragment mfragment;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationTracker mLocationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // startActivityForResult(new Intent(this, FindLocation.class), LOCATION_REQUEST);
        checkPlayServices();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Constant.IntentFlag.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    mLocationRequest = new LocationRequest();
                    LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(mLocationRequest);
                    mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
                    mGoogleApiClient.connect();
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
                    result.setResultCallback(mResultCallbackFromSettings);
                }
                break;
            case Constant.IntentFlag.REQUEST_CHECK_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    GPSTracker gpsTracker=new GPSTracker(this);
                    addFragment(gpsTracker.getLatitude(),gpsTracker.getLongitude(),true);
                } else finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {
                case Constant.Permission.COARSE_LOCATION_PERMISSION_REQUEST_CODE:
                case Constant.Permission.FINE_LOCATION_PERMISSION_REQUEST_CODE:

                    break;
            }

        }
    }

    /**
     * Method to verify google play services on the device
     */
    private void checkPlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            onActivityResult(Constant.IntentFlag.REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
        } else if (api.isUserResolvableError(code) && api.showErrorDialogFragment(this, code, Constant.IntentFlag.REQUEST_GOOGLE_PLAY_SERVICES)) {
            // wait for onActivityResult call (see below)
        } else {
            Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
        }
    }


    // The callback for the management of the user settings regarding location
    private ResultCallback<LocationSettingsResult> mResultCallbackFromSettings = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {
            final Status status = result.getStatus();
            //final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    GPSTracker gpsTracker=new GPSTracker(SignUpActivity.this);
                    addFragment(gpsTracker.getLatitude(),gpsTracker.getLongitude(),false);
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                SignUpActivity.this,
                                Constant.IntentFlag.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.e("Test", "Settings change unavailable. We have no way to fix the settings so we won't show the dialog.");
                    break;
            }
        }
    };


    private void addFragment(double lat,double lon,boolean isDelayed) {

        mfragment = StoreInformationFragment.newInstance(lat,lon,isDelayed);
        getFragmentManager().beginTransaction().add(R.id.layout, mfragment).commit();
    }


}
