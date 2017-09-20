package com.codepath.com.nytimes.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.nytimes.R;
import com.codepath.com.nytimes.models.Stories;
import com.codepath.com.nytimes.networking.NetworkUtils;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsGridFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUERY = "param1";
    public static final String FRAGMENT_TAG = "NewsGridFragment";

    // TODO: Rename and change types of parameters
    private String mQuery;


    public NewsGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param queryString Parameter 1.
     * @return A new instance of fragment NewsGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsGridFragment newInstance(String queryString) {
        NewsGridFragment fragment = new NewsGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, queryString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(ARG_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fetchDataFromAPI();
        return inflater.inflate(R.layout.fragment_news_grid, container, false);
    }

    public void fetchDataFromAPI(){

        NetworkUtils networkUtils = new NetworkUtils(getActivity());
        networkUtils.getNewsItems(mQuery, "", "newest", null, 0, new NetworkUtils.NetworkUtilResponse() {
            @Override
            public void onSuccess(Stories stories) {
                Log.d(FRAGMENT_TAG,"Got Retrofit Response");
                Log.d(FRAGMENT_TAG,"Number of stories= "+stories.getResponse().getMeta().getHits());
            }
        });


    }
}
