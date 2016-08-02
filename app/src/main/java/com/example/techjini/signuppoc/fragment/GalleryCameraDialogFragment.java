package com.example.techjini.signuppoc.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.techjini.signuppoc.util.Constant;
import com.example.techjini.signuppoc.util.Utility;

import java.io.IOException;

/**
 * Created by Shakeeb on 28/7/16.
 */
public class GalleryCameraDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures");
        myAlertDialog.setMessage("How do you want to get picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                Intent.ACTION_GET_CONTENT, null);
                        pictureActionIntent.setType("image/*");
                        pictureActionIntent.putExtra("return-data", true);
                        startActivityForResult(pictureActionIntent,
                                Constant.IntentFlag.GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureActionIntent,
                                Constant.IntentFlag.CAMERA_REQUEST);

                    }
                });
        return myAlertDialog.create();
    }

    public static GalleryCameraDialogFragment newInstance() {
        GalleryCameraDialogFragment fragment = new GalleryCameraDialogFragment();
       /* Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA, rechargeDetailModel);
        fragment.setArguments(args);*/
        return fragment;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == Constant.IntentFlag.GALLERY_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancelled",
                    Toast.LENGTH_SHORT).show();

        } else if (requestCode == Constant.IntentFlag.CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("data")) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else if (data.getExtras() == null) {
                    Toast.makeText(getActivity(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (bitmap != null) {
            String mImageBase64String = Utility.getEncoded64ImageStringFromBitmap(bitmap);
            mImageBase64String.trim();
        }
    }
}
