package com.codepath.com.nytimes.fragments;


import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.nytimes.R;
import com.codepath.com.nytimes.activities.NewsDetailActivity;
import com.codepath.com.nytimes.adapters.NewsItemRecyclerViewAdapter;
import com.codepath.com.nytimes.databinding.FragmentNewsGridBinding;
import com.codepath.com.nytimes.models.Doc;
import com.codepath.com.nytimes.models.Stories;
import com.codepath.com.nytimes.networking.NetworkUtils;
import com.codepath.com.nytimes.recievers.InternetCheckReceiver;
import com.codepath.com.nytimes.utils.EndlessRecyclerViewScrollListener;
import com.codepath.com.nytimes.utils.ItemClickSupport;

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
    private int mTotalPages;
    private InternetCheckReceiver mBroadcastReceiver;
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private int retryRemaining = 5;
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
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(page<= mTotalPages && mBroadcastReceiver.isInternetAvailable()) {
                    mFragmentNewsGridBinding.swipeContainer.setRefreshing(true);
                    fetchDataFromAPI(page);
                }else{
                    Snackbar.make(mFragmentNewsGridBinding.getRoot(),getString(R.string.snackbar_text_internet_lost),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        };
        mFragmentNewsGridBinding.rvNewsGridItems.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        ItemClickSupport.addTo(mFragmentNewsGridBinding.rvNewsGridItems).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // do it
                        List<Doc> data = mNewsItemRecyclerViewAdapter.getData();
                        final Doc doc = data.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.open_with_label));
                        builder.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                openChromeTab(doc);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                                intent.putExtra(NewsDetailActivity.DOC_KEY,doc);
                                startActivity(intent);
                            }
                        });
                        builder.show();


                    }
                }
        );
        mFragmentNewsGridBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mBroadcastReceiver.isInternetAvailable()) {
                    mNewsItemRecyclerViewAdapter.clear();
                    mEndlessRecyclerViewScrollListener.resetState();
                    fetchDataFromAPI(0);
                }else{
                    mFragmentNewsGridBinding.swipeContainer.setRefreshing(false);
                    Snackbar.make(mFragmentNewsGridBinding.getRoot(),getString(R.string.snackbar_text_internet_lost),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        mFragmentNewsGridBinding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fetchDataFromAPI(0);
        return mFragmentNewsGridBinding.getRoot();
    }

    public void fetchDataFromAPI(final int page){

        Log.d(FRAGMENT_TAG,"Loading page--> "+page);
        NetworkUtils networkUtils = new NetworkUtils(getActivity());
        networkUtils.getNewsItems(mQuery,page, new NetworkUtils.NetworkUtilResponse() {
            @Override
            public void onSuccess(Stories stories) {
                Log.d(FRAGMENT_TAG,"Got Retrofit Response");
                Log.d(FRAGMENT_TAG,"Number of stories= "+stories.getResponse().getMeta().getHits());
                mTotalPages = stories.getResponse().getMeta().getHits() / 10;
                List<Doc> newData = stories.getResponse().getDocs();
                mNewsItemRecyclerViewAdapter.addData(newData);
                mFragmentNewsGridBinding.swipeContainer.setRefreshing(false);
                retryRemaining = 5;
            }

            @Override
            public void onFailure() {
                Log.d(FRAGMENT_TAG,"Retrying request for page--> "+page);
                Log.d(FRAGMENT_TAG,"Retrying count--> "+retryRemaining);
                mFragmentNewsGridBinding.swipeContainer.setRefreshing(false);
                if(retryRemaining>0){
                    retryRemaining--;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchDataFromAPI(page);

                        }
                    },5000);

                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mBroadcastReceiver = new InternetCheckReceiver(mFragmentNewsGridBinding.getRoot());
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    private void openChromeTab(Doc doc){
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        String url = doc.getWebUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        builder.addDefaultShareMenuItem();
        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }
}
