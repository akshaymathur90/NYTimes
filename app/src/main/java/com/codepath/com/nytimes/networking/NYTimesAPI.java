package com.codepath.com.nytimes.networking;

import com.codepath.com.nytimes.models.Stories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akshaymathur on 9/19/17.
 */

public interface NYTimesAPI {

    @GET("articlesearch.json")
    Call<Stories> getResults(@Query("q") String query,@Query("api-key") String apiKey,
                          @Query("fq") String filterQuery,@Query("fl") String filterLines,
                             @Query("sort") String sortOrder, @Query("begin_date") String beginDate);

    @GET("articlesearch.json")
    Call<Stories> getResultsFromPage(@Query("q") String query,@Query("api-key") String apiKey,
                             @Query("fq") String filterQuery,@Query("fl") String filterLines,
                                     @Query("page") int page,@Query("sort") String sortOrder,
                                     @Query("begin_date") String beginDate);

}
