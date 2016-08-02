package com.example.techjini.signuppoc.util;

/**
 * Created by techjini on 28/7/16.
 */
public class Constant {

    public interface BundleExtra {
String isDelayed="isDelayed";
    }

    public interface IntentFlag {
        int GALLERY_PICTURE = 1;
        int CAMERA_REQUEST = 2;
        int LOCATION_REQUEST =3;
        int REQUEST_CHECK_SETTINGS=4;
        int REQUEST_GOOGLE_PLAY_SERVICES=5;
    }
    public interface Permission{
         int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 1;
         int FINE_LOCATION_PERMISSION_REQUEST_CODE = 2;
    }
}
