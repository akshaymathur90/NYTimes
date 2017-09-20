package com.codepath.com.nytimes.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.nytimes.R;
import com.codepath.com.nytimes.adapters.NewsItemRecyclerViewAdapter;
import com.codepath.com.nytimes.databinding.FragmentNewsGridBinding;
import com.codepath.com.nytimes.models.Doc;
import com.codepath.com.nytimes.models.Stories;
import com.codepath.com.nytimes.networking.NetworkUtils;

import java.util.ArrayList;
import java.util.List;


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
    private FragmentNewsGridBinding mFragmentNewsGridBinding;
    NewsItemRecyclerViewAdapter mNewsItemRecyclerViewAdapter;
    private List<Doc> mDocList;

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
        mFragmentNewsGridBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_grid,container,false);
        mDocList = new ArrayList<>();
        mNewsItemRecyclerViewAdapter= new NewsItemRecyclerViewAdapter(getActivity(),mDocList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mFragmentNewsGridBinding.rvNewsGridItems.setLayoutManager(staggeredGridLayoutManager);
        mFragmentNewsGridBinding.rvNewsGridItems.setAdapter(mNewsItemRecyclerViewAdapter);
        fetchDataFromAPI();
        return mFragmentNewsGridBinding.getRoot();
    }

    public void fetchDataFromAPI(){

        NetworkUtils networkUtils = new NetworkUtils(getActivity());
        networkUtils.getNewsItems(mQuery,0, new NetworkUtils.NetworkUtilResponse() {
            @Override
            public void onSuccess(Stories stories) {
                Log.d(FRAGMENT_TAG,"Got Retrofit Response");
                Log.d(FRAGMENT_TAG,"Number of stories= "+stories.getResponse().getMeta().getHits());
                List<Doc> newData = stories.getResponse().getDocs();
                mNewsItemRecyclerViewAdapter.addData(newData);
            }
        });


    }
}
