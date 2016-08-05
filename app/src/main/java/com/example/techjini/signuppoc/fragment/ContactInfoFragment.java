package com.example.techjini.signuppoc.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techjini.signuppoc.R;
import com.example.techjini.signuppoc.databinding.FragmentContactInfoContainerBinding;
import com.example.techjini.signuppoc.util.Utility;

/**
 * Created by Shakeeb on 27/7/16.
 */
public class ContactInfoFragment extends Fragment implements View.OnClickListener {


    private FragmentContactInfoContainerBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_info_container, container, false);
        init();
        getPrimaryPhoneNo();
        return mBinding.getRoot();
    }

    public void init() {
        mBinding.setHandler(this);
        mBinding.primaryEmailEdt.setText(getPrimaryEmail());
    }

    public String getPrimaryPhoneNo() {
        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (!TextUtils.isEmpty(mPhoneNumber))
            return mPhoneNumber;
        /*else{
            String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"};
            Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
                    main_data, "mimetype=?",
                    new String[]{"vnd.android.cursor.item/phone_v2"},
                    "is_primary DESC");
            if (cursor != null) {
                do {
                    if (!(cursor.moveToNext()))
                        break;
                    mPhoneNumber = (cursor.getString(4));
                } while (true);
                cursor.close();
            }
        }*/
        System.out.print(mPhoneNumber);
        return null;
    }

    private boolean validatePhoneNo() {
        if (!Utility.isPhoneNoValid(mBinding.primaryPhoneNoEdt.getText().toString())) {
            mBinding.primaryPhoneNoWrapper.setErrorEnabled(true);
            mBinding.primaryPhoneNoWrapper.setError(getResources().getString(R.string.enter_valid_phone));
            return false;
        }
        return true;
    }

    private String getPrimaryEmail() {
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        String primaryEmail = null;
        if (accounts != null && accounts.length > 0) {
            for (Account account : accounts) {
                if (account.type.equals("com.google")) {
                    primaryEmail = account.name;
                }
            }
        }
        if (!TextUtils.isEmpty(primaryEmail)) {
            return String.valueOf(primaryEmail);
        }
        return null;
    }

    public static ContactInfoFragment newInstance() {

        ContactInfoFragment fragment = new ContactInfoFragment();
       /* Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA, rechargeDetailModel);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onClick(View v) {
        Log.e("test", "Clicked");
        if(validatePhoneNo())
        addFragment();
    }

    private void addFragment() {
        Fragment fragment;
        fragment = BankInfoFragment.newInstance();
        getActivity().getFragmentManager().beginTransaction().add(R.id.layout, fragment).commit();

    }
}
