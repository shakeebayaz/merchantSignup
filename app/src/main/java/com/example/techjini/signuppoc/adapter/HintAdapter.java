package com.example.techjini.signuppoc.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;


import java.util.List;

/**
 * Created by Shakeeb on 27/7/16.
 */
public class HintAdapter extends ArrayAdapter<String> {

    public HintAdapter(Context context, int theLayoutResID, List<String> list) {
        super(context, theLayoutResID, list);
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

}
