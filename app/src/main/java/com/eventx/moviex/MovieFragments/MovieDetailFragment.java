package com.eventx.moviex.MovieFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.LoginAccount.AccountState;
import com.eventx.moviex.LoginAccount.Favourite;
import com.eventx.moviex.LoginAccount.WatchList;
import com.eventx.moviex.LoginActivity;
import com.eventx.moviex.MovieAdapter.ReviewAdapter;
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.MovieDetailsSecond;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieModels.Rate_value;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.MovieActivities.CastActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieAdapter.CastAdapter;
import com.eventx.moviex.MovieAdapter.HorizontalMoviesAdapter;
import com.eventx.moviex.MovieModels.Review;
import com.eventx.moviex.MovieModels.ReviewResults;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.SingleImageActivity;
import com.eventx.moviex.Wishlist.WishlistAcitvity;
import com.eventx.moviex.YoutubeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.Api;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.label;
import static android.R.attr.maxWidth;
import static android.R.attr.rating;
import static android.R.attr.typeface;
import static android.content.ContentValues.TAG;
import static com.squareup.picasso.Picasso.with;

/**
 * Created by Nishant on 3/27/2017.
 */

public class MovieDetailFragment extends Fragment {
    TextView genreTv;
    TextView runtimeTv;
    TextView overviewTv;
    ImageView mMoviePoster;
    TextView mMovieRatings;

    Button wishlistBtn;
    TextView mGenreTv;
    TextView mReleaseTv;
    TextView mTagline;
    RecyclerView mMoreLikeThis;

    RecyclerView mCastList;
    CastAdapter castAdapter;
    ArrayList<Cast> cast;

    ArrayList<Movie> similar;
    Button viewMoreBtn;


    HorizontalMoviesAdapter adapter;

    ImageButton mPlayTrailer;
    ImageView mBackdropImage;
    MovieDetails mMovie;
    MovieDetailsSecond mMovieDetailsSecond;
    long movieId;
    TextView movieTitle;

    MovieDbHelper helper;
    SQLiteDatabase readDb;

    RecyclerView reviewList;
    Button viewReviews;

    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviews = new ArrayList<>();

    TextView rateThis;
    SharedPreferences sp;
    String sessionId;
    long accountId;

    float ratingDb;

    SQLiteDatabase db;

    boolean alreadyFavourite;
    boolean alreadyWatchlist;


    Button mFavourite;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        postponeEnterTransition();
        View v = inflater.inflate(R.layout.movie_details_frag, container, false);


        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/malgun.ttf");


        movieId = getActivity().getIntent().getLongExtra("id", -1);
        helper = new MovieDbHelper(getContext());
        readDb = helper.getReadableDatabase();
        db = helper.getWritableDatabase();
        sp = getActivity().getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        sessionId = sp.getString("session", null);
        accountId = sp.getLong("account", -1);
        Log.i("poil", "onCreateView: " + sessionId);
        Log.i("poil", "onCreateView: " + accountId);


        similar = new ArrayList<>();
        adapter = new HorizontalMoviesAdapter(similar, getContext());
        genreTv = (TextView) v.findViewById(R.id.movie_genre);
        runtimeTv = (TextView) v.findViewById(R.id.movie_runtime);
        viewMoreBtn = (Button) v.findViewById(R.id.view_more);
        movieTitle = (TextView) v.findViewById(R.id.movie_detail_title);
        movieTitle.setTypeface(custom_font);
        overviewTv = (TextView) v.findViewById(R.id.movie_overview);
        mMoviePoster = (ImageView) v.findViewById(R.id.movie_image);
        viewReviews = (Button) v.findViewById(R.id.view_reviews);
        reviewAdapter = new ReviewAdapter(getContext(), reviews);
        rateThis = (TextView) v.findViewById(R.id.rate_this);
        rateThis.setTypeface(custom_font);
        mFavourite = (Button) v.findViewById(R.id.favourite);
        // setRating();

        rateThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionId != null) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    View v = inflater.inflate(R.layout.dialog_layout, null);
                    alertDialog.setView(v);
                    ImageView image = (ImageView) v.findViewById(R.id.image);
                    TextView title = (TextView) v.findViewById(R.id.title);

                    if(mMovie!=null){
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+mMovie.getPoster_path()).into(image);
                        title.setText(mMovie.getTitle());
                    }else{
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+mMovieDetailsSecond.getPoster_path()).into(image);

                        title.setText(mMovieDetailsSecond.getTitle());

                    }
                    Button save = (Button) v.findViewById(R.id.save_review);
                    final Button remove = (Button) v.findViewById(R.id.remove_review);
                    final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
                    ratingBar.setRating(ratingDb / 2);
                    final AlertDialog dialog = alertDialog.create();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Rate_value rate_value = new Rate_value();
                            if (ratingBar.getRating() == 0.0) {
                                Toast.makeText(getContext(), "0 Rating not allowed", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            rate_value.setValue(2 * ratingBar.getRating());
                            ApiInterface apiInterface = ApiClient.getApiInterface();
                            Call<Rate_value> raviewCall = apiInterface.rateMovie(rate_value, movieId, sessionId);
                            raviewCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(getContext(), "Saving Rating", Toast.LENGTH_SHORT).show();
                                    rateThis.setText("Your Rating "+2*ratingBar.getRating());
                                    //setRating();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Rate_value> call, Throwable t) {

                                }
                            });
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ApiInterface apiInterface = ApiClient.getApiInterface();
                            Call<Rate_value> removeCall = apiInterface.deleteRating(movieId, sessionId);
                            removeCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(getContext(), "Deleting Rating", Toast.LENGTH_SHORT).show();
                                    rateThis.setText("Rate this");
                                    //setRating();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Rate_value> call, Throwable t) {

                                }
                            });
                        }
                    });

                    dialog.show();
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }


            }
        });

        reviewList = (RecyclerView) v.findViewById(R.id.review_list);
        reviewList.setAdapter(reviewAdapter);
        reviewList.setNestedScrollingEnabled(false);
        viewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviewList.getVisibility() == View.GONE) {
                    fetchReviews();
                    viewReviews.setText("Hide Reviews");
                } else {
                    reviews.clear();
                    reviewList.setVisibility(View.GONE);
                    viewReviews.setText("See Reviews");
                }

            }
        });
        mMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageViewIntent = new Intent(getContext(), SingleImageActivity.class);
                if(mMovie!=null) {
                    imageViewIntent.putExtra("image", mMovie.getPoster_path());
                }else{
                    imageViewIntent.putExtra("image", mMovieDetailsSecond.getPoster_path());
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), view, "trans");
                    startActivity(imageViewIntent, options.toBundle());
                } else {
                    startActivity(imageViewIntent);
                }
            }
        });
        mMovieRatings = (TextView) v.findViewById(R.id.movie_rating);
        mGenreTv = (TextView) v.findViewById(R.id.genre_tv);
        mReleaseTv = (TextView) v.findViewById(R.id.movie_release_date);
        mTagline = (TextView) v.findViewById(R.id.movie_tagline);
        mMoreLikeThis = (RecyclerView) v.findViewById(R.id.movies_like_this);
        mCastList = (RecyclerView) v.findViewById(R.id.cast_recycle_list);
        wishlistBtn = (Button) v.findViewById(R.id.wishlist_btn);


        viewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent castIntent = new Intent(getActivity(), CastActivity.class);
                castIntent.putExtra("id", movieId);
                startActivity(castIntent);
            }
        });
        cast = new ArrayList<>();
        castAdapter = new CastAdapter(cast, getActivity());
        mCastList.setAdapter(castAdapter);

        mMoreLikeThis.setAdapter(adapter);


        mPlayTrailer = (ImageButton) v.findViewById(R.id.movie_trailer);
        mBackdropImage = (ImageView) v.findViewById(R.id.backdrop_image);
        mPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<ResultTrailer> keyResult = apiInterface.getTrailerKey(getActivity().getIntent().getLongExtra("id", -1));
                keyResult.enqueue(new Callback<ResultTrailer>() {
                    @Override
                    public void onResponse(Call<ResultTrailer> call, Response<ResultTrailer> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResults().size() == 0) {
                                Snackbar.make(v, "No trailer Available", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            String key = response.body().getResults().get(0).getKey();
                            Intent youtubeIntent = new Intent(getContext(), YoutubeActivity.class);
                            youtubeIntent.putExtra("videoKey", key);
                            startActivity(youtubeIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultTrailer> call, Throwable t) {
                    }
                });
            }
        });

        if(sessionId==null){
            setWishlistBtn();
        }
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();

                if (sessionId != null) {
                    if (alreadyFavourite) {
                        Favourite favourite = new Favourite("movie", false, movieId);

                        Call<Favourite> watchListCall = apiInterface.removeFromFavourite(favourite, accountId, sessionId);
                        watchListCall.enqueue(new Callback<Favourite>() {
                            @Override
                            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                                if (response.isSuccessful()) {
                                    Snackbar.make(view, "Removed from favourites", Snackbar.LENGTH_SHORT).show();

                                    mFavourite.setText("Mark as favourite");
                                    mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_heart, 0, 0);
                                    alreadyFavourite = false;

                                }
                            }

                            @Override
                            public void onFailure(Call<Favourite> call, Throwable t) {

                            }
                        });

                    } else {
                        Favourite favourite = new Favourite("movie", true, movieId);
                        Call<Favourite> watchListCall = apiInterface.addToFavourite(favourite, accountId, sessionId);
                        watchListCall.enqueue(new Callback<Favourite>() {
                            @Override
                            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                                if (response.isSuccessful()) {
                                    Snackbar.make(view, "Marked as favourite", Snackbar.LENGTH_SHORT).show();
                                    mFavourite.setText("Favourite");
                                    mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hearts, 0, 0);
                                    alreadyFavourite = true;
                                }

                            }

                            @Override
                            public void onFailure(Call<Favourite> call, Throwable t) {

                            }
                        });
                    }
                }else{
                    Snackbar.make(view,"Login First",Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getContext(),LoginActivity.class));
                        }
                    }).show();
                }
            }
        });
        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();

                if (sessionId != null) {
                    if (alreadyWatchlist) {
                        WatchList watchList = new WatchList("movie",  movieId,false);

                        Call<WatchList> watchListCall = apiInterface.removeFromWatchList(watchList, accountId, sessionId);
                        watchListCall.enqueue(new Callback<WatchList>() {
                            @Override
                            public void onResponse(Call<WatchList> call, Response<WatchList> response) {
                                if (response.isSuccessful()) {
                                    Snackbar.make(view, "Removed From watchlist", Snackbar.LENGTH_SHORT).show();

                                    wishlistBtn.setText("Watchlist");
                                    wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
                                    alreadyWatchlist = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<WatchList> call, Throwable t) {

                            }
                        });

                    } else {
                        WatchList watchList = new WatchList("movie", movieId, true);
                        Call<WatchList> watchListCall = apiInterface.addToWatchList(watchList, accountId, sessionId);
                        watchListCall.enqueue(new Callback<WatchList>() {
                            @Override
                            public void onResponse(Call<WatchList> call, Response<WatchList> response) {
                                if (response.isSuccessful()) {
                                    Snackbar.make(view, "Added to your Watchlist", Snackbar.LENGTH_SHORT).show();
                                    wishlistBtn.setText("Added to watchlist");
                                    wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
                                    alreadyWatchlist= true;
                                }

                            }

                            @Override
                            public void onFailure(Call<WatchList> call, Throwable t) {

                            }
                        });
                    }
                }else{

                    helper=new MovieDbHelper(getContext());
                    readDb=helper.getReadableDatabase();
                    Cursor c=readDb.query(MovieDbHelper.MOVIE_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_MOVIE_ID+" = "+movieId,null,null,null,null);
                    if(c.getCount()==0){
                        Snackbar.make(view,"Added to you wishlist",Snackbar.LENGTH_SHORT).show();
                        wishlistBtn.setText("Added to wishlist");
                        wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_black_24dp,0,0);
                        SQLiteDatabase db=helper.getWritableDatabase();
                        helper.addToSqlite(db,MovieDbHelper.MOVIE_WISHLIST_TABLE,movieId,getActivity().getIntent().getStringExtra("title"),getActivity().getIntent().getStringExtra("poster"));
                    }
                    else{
                        Snackbar.make(view,"Removed from your wishlist",Snackbar.LENGTH_SHORT).show();
                        wishlistBtn.setText("Wishlist");
                        wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_border_black_24dp,0,0);
                        SQLiteDatabase db=helper.getWritableDatabase();
                        db.delete(MovieDbHelper.MOVIE_WISHLIST_TABLE,MovieDbHelper.COLUMN_MOVIE_ID+" = "+movieId,null);

                    }
                    c.close();
                }

            }
        });


        fetchData();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setWishlistBtn();
    }

//    private void setRating() {
//        ApiInterface apiInterface=ApiClient.getApiInterface();
//        Call<AccountState> accountStateCall=apiInterface.getAccountStateMovie(movieId,sessionId);
//        accountStateCall.enqueue(new Callback<AccountState>() {
//            @Override
//            public void onResponse(Call<AccountState> call, Response<AccountState> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<AccountState> call, Throwable t) {
//                Log.i("opli", "onR: "+t.getMessage());
//
//            }
//        });
//
//    }

    private void fetchReviews() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ReviewResults> reviewsJson = apiInterface.getReviews(movieId);
        reviewsJson.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                if (response.isSuccessful()) {

                    reviews.addAll(response.body().getResults());
                    if (reviews.size() == 0) {
                        viewReviews.setText("No Reviews Available");
                        viewReviews.setEnabled(false);
                        return;
                    }
                    reviewList.setVisibility(View.VISIBLE);
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {

            }
        });
    }

    private void setWishlistBtn() {


        if (sessionId == null) {
            Cursor c = readDb.query(MovieDbHelper.MOVIE_WISHLIST_TABLE, null, MovieDbHelper.COLUMN_MOVIE_ID + " = " + movieId, null, null, null, null);
            if (c.getCount() == 0) {
                wishlistBtn.setText("Wishlist");
                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
            } else {
                wishlistBtn.setText("Added to wishlist");
                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
            }
        }
    }




    private void fetchData() {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<MovieDetails> movieDetails = apiInterface.getMovieDetails(movieId, sessionId);
        movieDetails.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {


                if (response.isSuccessful()) {
                    mMovie = new MovieDetails();


                    with(getActivity()).load("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path()).into(mMoviePoster);

                    with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                    movieTitle.setText(response.body().getTitle());
                    mMovie = response.body();
                    overviewTv.setText(mMovie.getOverview());
                    if (sessionId != null) {
                        if (mMovie.getAccount_states().isWatchlist()) {
                            wishlistBtn.setText("Added to WatchList");
                            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
                            alreadyWatchlist=true;
                        } else {
                            wishlistBtn.setText("Watchlist");
                            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
                            alreadyWatchlist=false;
                        }
                        if (mMovie.getAccount_states().isFavorite()) {
                            mFavourite.setText("Favourite");
                            mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hearts, 0, 0);
                            alreadyFavourite = true;

                        } else {
                            mFavourite.setText("Mark as Favourite");
                            mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_heart, 0, 0);
                            alreadyFavourite=false;
                        }
                    } else {
                        rateThis.setText("Rate this ");
                    }

                    int hour = mMovie.getRuntime() / 60;
                    int minute = mMovie.getRuntime() % 60;
                    runtimeTv.setText(hour + " hrs " + minute + " mins ");

                    mTagline.setText(mMovie.getTagline());
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                    SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdfd.parse(mMovie.getRelease_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        mReleaseTv.setText(sdf.format(date));
                    }
                    mMovieRatings.setText(mMovie.getVote_average() + "/10 " + "\n" + mMovie.getVote_count());
                    for (int i = 0; i < mMovie.getGenres().size(); i++) {
                        if (i < mMovie.getGenres().size() - 1) {
                            genreTv.append(mMovie.getGenres().get(i).getName() + "|");
                            mGenreTv.append(mMovie.getGenres().get(i).getName() + "|");
                        } else {
                            genreTv.append(mMovie.getGenres().get(i).getName());
                            mGenreTv.append(mMovie.getGenres().get(i).getName());
                        }
                    }

                } else {
                    fetchData();
                }

            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                if(t.getMessage().contains("java.lang.IllegalStateException: Expected a boolean but was BEGIN_OBJECT")){
                   fetchDataRated();
                }
                if(t.getMessage().equals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BOOLEAN at line 1 column 1263 path $.account_states.rated")){
                }
                if(t.getMessage().equals("Unable to resolve host \"api.themoviedb.org\": No address associated with hostname")){
                }


            }
        });


        Call<MovieResults> similarMovies = apiInterface.getSimilarResults(movieId);
        similarMovies.enqueue(new Callback<MovieResults>() {


            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> popularJson = response.body().getResults();
                    similar.clear();
                    similar.addAll(popularJson);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
        Call<CastResults> movieCast = apiInterface.getCast(movieId);
        movieCast.enqueue(new Callback<CastResults>() {

            @Override
            public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                if (response.isSuccessful()) {
                    ArrayList<Cast> castJson = response.body().getCast();
                    cast.clear();
                    cast.addAll(castJson);
                    castAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CastResults> call, Throwable t) {

            }
        });

    }

    private void fetchDataRated() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<MovieDetailsSecond> movieCall=apiInterface.getMovieDetailsRated(movieId,sessionId);
        movieCall.enqueue(new Callback<MovieDetailsSecond>() {
            @Override
            public void onResponse(Call<MovieDetailsSecond> call, Response<MovieDetailsSecond> response) {
                if (response.isSuccessful()) {

                    mMovieDetailsSecond=new MovieDetailsSecond();
                    mMovieDetailsSecond=response.body();
                    with(getActivity()).load("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path()).into(mMoviePoster);

                    with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                    movieTitle.setText(response.body().getTitle());
                    overviewTv.setText(mMovieDetailsSecond.getOverview());
                    if (sessionId != null) {
                        if (mMovieDetailsSecond.getAccount_states().isWatchlist()) {
                            wishlistBtn.setText("Added to WatchList");
                            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
                            alreadyWatchlist=true;
                        } else {
                            wishlistBtn.setText("Watchlist");
                            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
                            alreadyWatchlist=false;
                        }
                        if (mMovieDetailsSecond.getAccount_states().isFavorite()) {
                            mFavourite.setText("Favourite");
                            mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hearts, 0, 0);
                            alreadyFavourite = true;

                        } else {
                            mFavourite.setText("Mark as Favourite");
                            mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_heart, 0, 0);
                            alreadyFavourite=false;
                        }
                        rateThis.setText("Your Rating "+mMovieDetailsSecond.getAccount_states().getRated().getValue());
                    }

                    int hour = mMovieDetailsSecond.getRuntime() / 60;
                    int minute = mMovieDetailsSecond.getRuntime() % 60;
                    runtimeTv.setText(hour + " hrs " + minute + " mins ");

                    mTagline.setText(mMovieDetailsSecond.getTagline());
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                    SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdfd.parse(mMovieDetailsSecond.getRelease_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        mReleaseTv.setText(sdf.format(date));
                    }
                    mMovieRatings.setText(mMovieDetailsSecond.getVote_average() + "/10 " + "\n" + mMovieDetailsSecond.getVote_count());
                    for (int i = 0; i < mMovieDetailsSecond.getGenres().size(); i++) {
                        if (i < mMovieDetailsSecond.getGenres().size() - 1) {
                            genreTv.append(mMovieDetailsSecond.getGenres().get(i).getName() + "|");
                            mGenreTv.append(mMovieDetailsSecond.getGenres().get(i).getName() + "|");
                        } else {
                            genreTv.append(mMovieDetailsSecond.getGenres().get(i).getName());
                            mGenreTv.append(mMovieDetailsSecond.getGenres().get(i).getName());
                        }
                    }

                } else {
                    fetchData();
                }

            }

            @Override
            public void onFailure(Call<MovieDetailsSecond> call, Throwable t) {

            }
        });
    }


}
