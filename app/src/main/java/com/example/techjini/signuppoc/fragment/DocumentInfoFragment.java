package com.example.techjini.signuppoc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.techjini.signuppoc.R;
import com.example.techjini.signuppoc.adapter.DocumentAdapter;
import com.example.techjini.signuppoc.databinding.FragmentDocInfoBinding;
import com.example.techjini.signuppoc.model.DocumentInfo;
import com.example.techjini.signuppoc.util.Constant;
import com.example.techjini.signuppoc.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakeeb on 28/7/16.
 */
public class DocumentInfoFragment extends Fragment {
    private FragmentDocInfoBinding mBinding;
    private String mImageBase64String;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_doc_info, container, false);
        init();
        //startDialog();
        return mBinding.getRoot();
    }

    public void init() {
        mBinding.docRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.docRecycler.setAdapter(new DocumentAdapter(getActivity(), getDocList()));
     /*   Intent pictureActionIntent = new Intent(
                Intent.ACTION_GET_CONTENT, null);
        pictureActionIntent.setType("image*//*");
        pictureActionIntent.putExtra("return-data", true);
        startActivityForResult(pictureActionIntent,
                Constant.IntentFlag.GALLERY_PICTURE);*/
    }

    public List<DocumentInfo> getDocList() {
        List<DocumentInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DocumentInfo doc = new DocumentInfo();
            doc.setDocName("Document " + i);
            list.add(doc);
        }
        return list;
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
            mImageBase64String = Utility.getEncoded64ImageStringFromBitmap(bitmap);
        }


    }

    private void startDialog() {
        GalleryCameraDialogFragment fragment = GalleryCameraDialogFragment.newInstance();
        fragment.show(getActivity().getFragmentManager(), "vv");
    }

    public static DocumentInfoFragment newInstance() {

        DocumentInfoFragment fragment = new DocumentInfoFragment();
       /* Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA, rechargeDetailModel);
        fragment.setArguments(args);*/
        return fragment;
    }



}
