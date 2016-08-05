package com.example.techjini.signuppoc.util;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by techjini on 28/7/16.
 */
public class Utility {
    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public static boolean isPhoneNoValid(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo) && phoneNo.matches(Constant.Validation.PHONE_VALIDATION)) {
            return true;
        }

        return false;
    }
}
