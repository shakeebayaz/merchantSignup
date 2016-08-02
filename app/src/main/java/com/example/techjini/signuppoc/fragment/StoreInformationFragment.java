package com.example.techjini.signuppoc.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techjini.signuppoc.FindLocation;
import com.example.techjini.signuppoc.GPSTracker;
import com.example.techjini.signuppoc.GeocoderHelper;
import com.example.techjini.signuppoc.R;
import com.example.techjini.signuppoc.adapter.HintAdapter;
import com.example.techjini.signuppoc.databinding.FragmentStoreInformationBinding;
import com.example.techjini.signuppoc.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakeeb on 25/7/16.
 */
public class StoreInformationFragment extends Fragment implements View.OnClickListener {
    private FragmentStoreInformationBinding mBinding;
    private HintAdapter mStoreAdapter, mPartnershipAdapter;
    private boolean isDelay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_information, container, false);
        mBinding.progressbar.bringToFront();
        init();
        final Handler handler = new Handler();
    //    if(isDelay)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                GPSTracker gpsTracker=new GPSTracker(getActivity());
                new GetAddress(getActivity()).execute(gpsTracker.getLatitude(),gpsTracker.getLongitude());

            }
        }, 5000);
       /* else {
            GPSTracker gpsTracker=new GPSTracker(getActivity());
            new GetAddress(getActivity()).execute(gpsTracker.getLatitude(),gpsTracker.getLongitude());
            mBinding.progressbar.setVisibility(View.GONE);
        }*/
        return mBinding.getRoot();


    }

    public static StoreInformationFragment newInstance(/*boolean isDelay*/) {
        StoreInformationFragment fragment = new StoreInformationFragment();

         /* Bundle args = new Bundle();
        args.putBoolean(Constant.BundleExtra.isDelayed,isDelay);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            isDelay = getArguments().getBoolean(Constant.BundleExtra.isDelayed);
        }*/
    }

    private void init() {
        mBinding.setHandler(this);

        mStoreAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, getStoreCategoryList());
        mStoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.storeCategorySpinner.setAdapter(mStoreAdapter);
        mBinding.storeCategorySpinner.setSelection(mStoreAdapter.getCount());

        mPartnershipAdapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, getPartnershipList());
        mPartnershipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.partnershipSpinner.setAdapter(mPartnershipAdapter);
        mBinding.partnershipSpinner.setSelection(mPartnershipAdapter.getCount());

    }

    public List<String> getStoreCategoryList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("Category " + i);
        }
        list.add(getResources().getString(R.string.store_category));
        return list;
    }

    public List<String> getPartnershipList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("Partnership " + i);
        }
        list.add(getResources().getString(R.string.partnership));
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_btn)
            addFragment();
        else
            startActivityForResult(new Intent(getActivity(), FindLocation.class), Constant.IntentFlag.LOCATION_REQUEST); ;
    }

    public void addFragment() {
        Fragment fragment;
        fragment = ContactInfoFragment.newInstance();
        getActivity().getFragmentManager().beginTransaction().add(R.id.layout, fragment).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.IntentFlag.LOCATION_REQUEST) {
            double mLatitude = data.getDoubleExtra("LATITUDE", 0);
            double mLongitude = data.getDoubleExtra("LONGITUDE", 0);
            String mAddress = data.getStringExtra("Address");
            mBinding.storeLocationEdt.setText(mAddress);
        }
    }
    private class GetAddress extends AsyncTask<Double, Void, String> {
        private final Context mContext;
        GeocoderHelper geo;

        public GetAddress(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Double... params) {

            geo = new GeocoderHelper(mContext,params[0], params[1]);

            return geo.fetchCityName();
        }

        @Override
        protected void onPostExecute(String result) {
            mBinding.storeLocationEdt.setText("" + result);
            mBinding.progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
}
