package com.example.techjini.signuppoc.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techjini.signuppoc.R;
import com.example.techjini.signuppoc.databinding.AdapterDocInfoBinding;
import com.example.techjini.signuppoc.model.DocumentInfo;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Shakeeb on 28/7/16.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private Context mContext;
    private List<DocumentInfo> mList;

    public DocumentAdapter(Context context, List<DocumentInfo> docList) {
        mList = docList;
        mContext = context;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDocInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_doc_info, parent, false);
        return new DocumentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        holder.mBinding.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDocInfoBinding mBinding;

        public DocumentViewHolder(AdapterDocInfoBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
