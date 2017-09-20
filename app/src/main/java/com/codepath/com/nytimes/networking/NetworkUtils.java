package com.codepath.com.nytimes.networking;

import android.content.Context;
import android.util.Log;

import com.codepath.com.nytimes.models.ApiResponse;
import com.codepath.com.nytimes.models.Stories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshaymathur on 9/19/17.
 */

public class NetworkUtils {

    private String mBaseURL;
    private Retrofit mRetrofit;
    private final static String TAG = "NetworkUtils";
    private final String API_KEY= "6973729bd76c46819a940bb6b55c6b0d";
    private NYTimesAPI mNYTimesAPI;
    private final String filterLines = "web_url,headline,multimedia,news_desk";
    private Context mContext;
    public NetworkUtils(Context context){
        mBaseURL = "https://api.nytimes.com/svc/search/v2/";
        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNYTimesAPI = mRetrofit.create(NYTimesAPI.class);
        mContext = context;
    }

    public interface NetworkUtilResponse{
        void onSuccess(Stories stories);
    }


    public void getNewsItems(String queryString, String filterQuery, String sortOrder, String beginDate, int page, final NetworkUtilResponse networkUtilResponse){

        Call<Stories> call = mNYTimesAPI.getResultsFromPage(queryString,API_KEY,filterQuery,filterLines,page,sortOrder,beginDate);
        call.enqueue(new Callback<Stories>() {
            @Override
            public void onResponse(Call<Stories> call, Response<Stories> response) {
                int statusCode = response.code();
                Stories stories = response.body();
                Log.d(TAG,"Status code== "+statusCode);
                Log.d(TAG,"Total Stories== "+stories.getResponse().getMeta().getHits());
                networkUtilResponse.onSuccess(stories);
            }

            @Override
            public void onFailure(Call<Stories> call, Throwable t) {
                // Log error here since request failed
                Log.d(TAG,t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
