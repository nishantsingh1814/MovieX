package com.eventx.moviex.Network;

import com.eventx.moviex.LoginAccount.Account;
import com.eventx.moviex.LoginAccount.AccountState;
import com.eventx.moviex.LoginAccount.Favourite;
import com.eventx.moviex.LoginAccount.WatchList;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.EpisodeResults;
import com.eventx.moviex.MovieModels.MovieD;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.MovieDetailsSecond;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieModels.Rate_value;
import com.eventx.moviex.MovieModels.RequestToken;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.MovieModels.ReviewResults;
import com.eventx.moviex.MovieModels.SessionId;
import com.eventx.moviex.MovieModels.ValidRequest;
import com.eventx.moviex.PeopleModels.PeopleImageResults;
import com.eventx.moviex.PeopleModels.PeopleInfo;
import com.eventx.moviex.PeopleModels.PeopleMovieCast;
import com.eventx.moviex.PeopleModels.PeopleTvCast;
import com.eventx.moviex.PeopleModels.PopularResults;
import com.eventx.moviex.TvModels.PosterResult;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowD;
import com.eventx.moviex.TvModels.TvShowDetails;
import com.eventx.moviex.TvModels.TvShowDetailsSecond;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nishant on 3/27/2017.
 */

public interface ApiInterface {
    @GET("movie/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getPopularResults();

    @GET("movie/{movie_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") long movie_id, @Query("session_id")String session_id);

    @GET("movie/{movie_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<MovieD> getMovieD(@Path("movie_id") long movie_id, @Query("session_id")String session_id);

    @GET("movie/{movie_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<MovieDetailsSecond> getMovieDetailsRated(@Path("movie_id") long movie_id, @Query("session_id")String session_id);

    @GET("movie/{movie_id}/similar?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getSimilarResults(@Path("movie_id") long movie_id);

    @GET("movie/{movie_id}/videos?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<ResultTrailer> getTrailerKey(@Path("movie_id") long movie_id);

    @GET("movie/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getMostPop(@Query("page") int page);

    @GET("movie/top_rated?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getTopRated();

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

    @GET("movie/upcoming?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getUpcoming(@Query("region") String m);

    @GET("movie/upcoming?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getUpcoming(@Query("page") int i,@Query("region") String m);


    @GET("movie/{movie_id}/images?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PosterResult> getMovieImages(@Path("movie_id") long movie_id);

    @GET("movie/{movie_id}/reviews?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<ReviewResults> getReviews(@Path("movie_id") long movie_id);

    //Tv

    @GET("tv/popular?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getPopularTvResults();

    @GET("tv/top_rated?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTopRatedTv();

    @GET("tv/airing_today?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTonightAir();

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

    @GET("tv/{tv_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<TvShowDetails> getSingleTvShowDetails(@Path("tv_id" )long tv_id,@Query("session_id")String session_id);

    @GET("tv/{tv_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<TvShowDetailsSecond> getSingleTvShowDetailsRated(@Path("tv_id" )long tv_id, @Query("session_id")String session_id);

    @GET("tv/{tv_id}/credits?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<CastResults> getTvCast(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/images?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<PosterResult> getImages(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/similar?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getTvSimilarResults(@Path("tv_id") long tv_id);

    @GET("tv/{tv_id}/season/{season_number}?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<EpisodeResults> getEpisodeResults(@Path("tv_id") long tv_id,@Path("season_number") long season_number);







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



    //Login

    @GET("authentication/token/new?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<RequestToken> getRequestToken();

    @GET("account?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Account> getAccountId(@Query("session_id")String session);

    @GET("authentication/token/validate_with_login?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<ValidRequest> validateRequest(@Query("username") String username,@Query("password")String password,@Query("request_token") String requestToken);

    @GET("authentication/session/new?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<SessionId> getSessionId(@Query("request_token") String requestToken);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("movie/{movie_id}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> rateMovie(@Body Rate_value rate_value,@Path("movie_id") long movie_id ,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @DELETE("movie/{movie_id}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> deleteRating(@Path("movie_id") long movie_id ,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("tv/{tv_id}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> rateTvShow(@Body Rate_value rate_value,@Path("tv_id") long tv_id,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @DELETE("tv/{tv_id}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> deleteRatingTv(@Path("tv_id") long tv_id ,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("tv/{tv_id}/season/{season_number}/episode/{episode_number}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> rateTvShowEpisode(@Body Rate_value rate_value,@Path("tv_id") long tv_id,@Path("season_number") int season_number,@Path("episode_number") int episode_number,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @DELETE("tv/{tv_id}/season/{season_number}/episode/{episode_number}/rating?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Rate_value> deleteRatingTvEpidode(@Path("tv_id") long tv_id ,@Path("season_number") int season_number,@Path("episode_number") int episode_number,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("account/{account_id}/watchlist?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<WatchList> addToWatchList(@Body WatchList watchList,@Path("account_id") long account_id,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("account/{account_id}/watchlist?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<WatchList> removeFromWatchList(@Body WatchList watchList,@Path("account_id") long account_id,@Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("account/{account_id}/favorite?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Favourite> addToFavourite(@Body Favourite favourite, @Path("account_id") long account_id, @Query("session_id")String session);

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("account/{account_id}/favorite?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<Favourite> removeFromFavourite(@Body Favourite favourite,@Path("account_id") long account_id,@Query("session_id")String session);


    @GET("tv/{tv_id}/account_states?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<AccountState> getAccountStateTv(@Path("tv_id") long tv_id,@Query("session_id")String session);

    @GET("movie/{movie_id}/account_states?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<AccountState> getAccountStateMovie(@Path("movie_id") long movie_id,@Query("session_id")String session);

    @GET("account/{account_id}/watchlist/movies?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getWatchedListMovie(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("account/{account_id}/watchlist/tv?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getWatchedListTV(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("account/{account_id}/favorite/movies?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getFavoriteMovie(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("account/{account_id}/favorite/tv?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getFavoriteTV(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("account/{account_id}/rated/movies?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<MovieResults> getRatedMovie(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("account/{account_id}/rated/tv?api_key=3721a26d1b2c217fa0206eb8f4a368b8")
    Call<TvResults> getRatedTV(@Path("account_id") long account_id,@Query("session_id")String session,@Query("page" )int page);

    @GET("tv/{tv_id}?api_key=3721a26d1b2c217fa0206eb8f4a368b8&append_to_response=account_states")
    Call<TvShowD> getSingleTvShowD(@Path("tv_id" )long tv_id,@Query("session_id")String session_id);
}
