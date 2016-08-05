package com.example.techjini.signuppoc.util;

/**
 * Created by techjini on 28/7/16.
 */
public class Constant {


    public interface BundleExtra {
        String isDelayed = "isDelayed";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    public interface IntentFlag {
        int GALLERY_PICTURE = 1;
        int CAMERA_REQUEST = 2;
        int LOCATION_REQUEST = 3;
        int REQUEST_CHECK_SETTINGS = 4;
        int REQUEST_GOOGLE_PLAY_SERVICES = 5;
    }

    public interface Permission {
        int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 1;
        int FINE_LOCATION_PERMISSION_REQUEST_CODE = 2;
    }

    public interface Validation {

        String OPERATOR_TYPE = "RECHARGE_TYPE";
        String OPERATOR_TITLE = "RECHARGE_TITLE";
        String PHONE_VALIDATION = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
        String AMOUNT_VALIDATION = "[1-9][0-9]*";
    }
}
