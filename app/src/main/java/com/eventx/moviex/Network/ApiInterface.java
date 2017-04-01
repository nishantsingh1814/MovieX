package com.eventx.moviex.Network;

import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.EpisodeResults;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.PeopleModels.PeopleImageResults;
import com.eventx.moviex.PeopleModels.PeopleInfo;
import com.eventx.moviex.PeopleModels.PeopleMovieCast;
import com.eventx.moviex.PeopleModels.PeopleTvCast;
import com.eventx.moviex.PeopleModels.PopularResults;
import com.eventx.moviex.TvModels.PosterResult;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nishant on 3/27/2017.
 */

public interface ApiInterface {
    @GET("movie/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getPopularResults();

    @GET("movie/{movie_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") long movie_id);

    @GET("movie/{movie_id}/similar?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getSimilarResults(@Path("movie_id") long movie_id);

    @GET("movie/{movie_id}/videos?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<ResultTrailer> getTrailerKey(@Path("movie_id") long movie_id);

    @GET("movie/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getMostPop(@Query("page") int page);

    @GET("movie/top_rated?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getTopRated(@Query("page") int i);

    @GET("movie/now_playing?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getNowShowing(@Query("page") int i);

    @GET("search/movie?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getSearchResults(@Query("query") String query,@Query("page") int i);

    @GET("movie/{movie_id}/credits?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<CastResults> getCast(@Path("movie_id") long movie_id);

    @GET("movie/now_playing?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getNowShowing();


    //Tv

    @GET("tv/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getPopularTvResults();

    @GET("tv/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getMostPopTv(@Query("page") int page);

    @GET("tv/top_rated?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTopRatedTv(@Query("page") int i);

    @GET("tv/airing_today?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTonightAir(@Query("page") int i);

    @GET("tv/{tv_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvShow> getTvShowDetails(@Path("tv_id" )long tv_id);

    @GET("tv/{tv_id}/videos?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<ResultTrailer> getTvTrailerKey(@Path("tv_id") long tv_id);

    @GET("search/tv?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTvSearchResults(@Query("query") String query,@Query("page") int i);

    @GET("tv/{tv_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvShowDetails> getSingleTvShowDetails(@Path("tv_id" )long tv_id);

    @GET("tv/{tv_id}/credits?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<CastResults> getTvCast(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/images?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PosterResult> getImages(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/similar?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTvSimilarResults(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/season/{season_number}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<EpisodeResults> getEpisodeResults(@Path("tv_id") long tv_id,@Path("season_number") long season_number);

    @GET("tv/airing_today?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTonightAir();

    //People
    @GET("person/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PopularResults> getPopularPeople(@Query("page") int page);

    @GET("search/person?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PopularResults> getSearchPeopleResults(@Query("query") String query,@Query("page" )int page);

    @GET("person/{person_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PeopleInfo> getPeopleInfo(@Path("person_id") long person_id);

    @GET("person/{person_id}/movie_credits?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PeopleMovieCast> getMovieCredits(@Path("person_id") long person_id);

    @GET("person/{person_id}/tv_credits?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PeopleTvCast> getTvCredits(@Path("person_id") long person_id);

    @GET("person/{person_id}/images?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PeopleImageResults> getPeopleImages(@Path("person_id") long person_id);



}
