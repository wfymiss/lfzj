package com.ovov.lfzj.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovov.lfzj.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p>
 * interface.
 */
public class SuggestFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String INDEX = "index";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static Bundle args;
    private int anInt;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SuggestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SuggestFragment newInstance(int columnCount, int index) {
        SuggestFragment fragment = new SuggestFragment();
        args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anInt = args.getInt(INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (anInt == 1) {
            View view = inflater.inflate(R.layout.fragment_message_item, container, false);
            // Set the adapter
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_item_list, container, false);
            // Set the adapter
            return view;
        }

    }


}
