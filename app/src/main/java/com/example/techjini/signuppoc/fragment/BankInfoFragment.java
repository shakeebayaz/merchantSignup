package com.example.techjini.signuppoc.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techjini.signuppoc.R;
import com.example.techjini.signuppoc.databinding.FragmentBankInfoBinding;

/**
 * Created by Shakeeb on 27/7/16.
 */
public class BankInfoFragment extends Fragment implements View.OnClickListener {
    private FragmentBankInfoBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_info, container, false);
        mBinding.setHandler(this);
        return mBinding.getRoot();
    }

    public static BankInfoFragment newInstance() {

        BankInfoFragment fragment = new BankInfoFragment();
       /* Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA, rechargeDetailModel);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onClick(View v) {
        addFragment();
    }

    public void addFragment() {
        Fragment fragment;
        fragment = DocumentInfoFragment.newInstance();
        getActivity().getFragmentManager().beginTransaction().add(R.id.layout, fragment).commit();

    }
}
