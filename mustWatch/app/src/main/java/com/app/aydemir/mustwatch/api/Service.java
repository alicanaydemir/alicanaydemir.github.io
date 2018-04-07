package com.app.aydemir.mustwatch.api;

import com.app.aydemir.mustwatch.model.MovieMembers;
import com.app.aydemir.mustwatch.model.MovieResponse;
import com.app.aydemir.mustwatch.model.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ACER on 21.3.2018.
 */

public interface Service {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key")String apiKey);
    @GET("search/movie")
    Call<MovieSearchResponse> getMovie(@Query("api_key")String apiKey,@Query("query")String query);
    @GET("movie/{movie_id}/credits")
    Call<MovieMembers> getMembersMovies(@Path("movie_id") int movie_id, @Query("api_key")String apiKey);

}
