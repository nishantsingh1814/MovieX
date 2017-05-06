package com.eventx.moviex.TvFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.LoginAccount.Favourite;
import com.eventx.moviex.LoginAccount.WatchList;
import com.eventx.moviex.LoginActivity;
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.Rate_value;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.SingleImageActivity;
import com.eventx.moviex.TvActivities.SeasonActivity;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvActivities.TvShowEpisodeActivity;
import com.eventx.moviex.TvAdapter.HorizontalTvAdapter;
import com.eventx.moviex.TvAdapter.TvCastAdapter;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowDetails;
import com.eventx.moviex.TvModels.TvShowDetailsSecond;
import com.eventx.moviex.YoutubeActivity;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvShowInfoFragment extends Fragment {
    TextView tvGenre;
    TextView overviewTv;
    TextView mTvRatings;
    TextView mReleaseTv;
    TextView title;

    RecyclerView tv_show_cast;
    TvCastAdapter adapter;
    ArrayList<Cast> mCast;
    ImageView poster;
    TvShowDetails mShow;
    HorizontalTvAdapter similarAdapter;
    RecyclerView mMoreLikeThis;
    ArrayList<TvShow> similar;
    Button wishlistBtn;
    Button mFavourite;

    boolean alreadyFavourite;
    boolean alreadyWatchlist;


    long tvId;


    TextView viewAllEpisodes;

    private ImageView mBackdropImage;
    private ImageButton mPlayButton;
    private AdView mAdView;

    TextView rateThis;
    SharedPreferences sp;
    String sessionId;

    private boolean alreadyRated;
    private float ratingDb;
    private long accountId;
    private TvShowDetailsSecond mShowSecond;

    MovieDbHelper helper;
    SQLiteDatabase readDb;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        postponeEnterTransition();
        View v = inflater.inflate(R.layout.tv_show_info_fragment, container, false);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/malgun.ttf");



        tvGenre = (TextView) v.findViewById(R.id.tv_genre);
        sp = getActivity().getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        sessionId = sp.getString("session", null);
        accountId = sp.getLong("account", -1);

        tvId=getActivity().getIntent().getLongExtra("id", -1);

//        mAdView = (AdView) v.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        title = (TextView) v.findViewById(R.id.show_detail_title);
        title.setTypeface(custom_font);
        overviewTv = (TextView) v.findViewById(R.id.tv_show_overview);
        mTvRatings = (TextView) v.findViewById(R.id.show_ratings);
        mReleaseTv = (TextView) v.findViewById(R.id.year_started);
        viewAllEpisodes = (TextView) v.findViewById(R.id.view_All_episode);
        wishlistBtn = (Button) v.findViewById(R.id.wishlist_btn);

        setWishlistBtn();
        mFavourite=(Button)v.findViewById(R.id.favourite);
        rateThis = (TextView) v.findViewById(R.id.rate_this);
        rateThis.setTypeface(custom_font);
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();

                if (sessionId != null) {
                    if (alreadyFavourite) {
                        Favourite favourite = new Favourite("tv", false, tvId);

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
                        Favourite favourite = new Favourite("tv", true, tvId);
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

        rateThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionId != null) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    View v = inflater.inflate(R.layout.dialog_layout, null);
                    alertDialog.setView(v);
                    ImageView image = (ImageView) v.findViewById(R.id.image);
                    TextView title = (TextView) v.findViewById(R.id.title);
                    Button save = (Button) v.findViewById(R.id.save_review);
                    final Button remove = (Button) v.findViewById(R.id.remove_review);
                    final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
                    ratingBar.setRating(ratingBar.getRating()*2);
                    final AlertDialog dialog = alertDialog.create();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Rate_value rate_value = new Rate_value();
                            if(ratingBar.getRating()==0.0){
                                Toast.makeText(getContext(),"0 Rating not allowed",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            rate_value.setValue(2 * ratingBar.getRating());
                            ApiInterface apiInterface = ApiClient.getApiInterface();
                            Call<Rate_value> raviewCall = apiInterface.rateTvShow(rate_value, tvId, sessionId);
                            raviewCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(getContext(), "Saving Rating", Toast.LENGTH_SHORT).show();
                                    rateThis.setText("Your Rating "+2*ratingBar.getRating());
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
                            Call<Rate_value> removeCall = apiInterface.deleteRatingTv(tvId, sessionId);
                            removeCall.enqueue(new Callback<Rate_value>() {
                                @Override
                                public void onResponse(Call<Rate_value> call, Response<Rate_value> response) {
                                    Toast.makeText(getContext(), "Deleting Rating", Toast.LENGTH_SHORT).show();
                                    rateThis.setText("Rate this");
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Rate_value> call, Throwable t) {

                                }
                            });
                        }
                    });
                    if(mShow!=null){
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + mShow.getPoster_path()).into(image);
                        title.setText(mShow.getName());
                    }else{
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + mShowSecond.getPoster_path()).into(image);
                        title.setText(mShowSecond.getName());
                    }
                    dialog.show();
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }


            }
        });


        viewAllEpisodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allEpisodesIntent = new Intent(getContext(), SeasonActivity.class);
                allEpisodesIntent.putExtra("id", getActivity().getIntent().getLongExtra("id", -1));
                allEpisodesIntent.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                if(mShow!=null) {
                    allEpisodesIntent.putExtra("seasons", mShow.getNumber_of_seasons());
                }else{
                    allEpisodesIntent.putExtra("seasons", mShowSecond.getNumber_of_seasons());
                }
                startActivity(allEpisodesIntent);
            }
        });

        mCast = new ArrayList<>();
        poster = (ImageView) v.findViewById(R.id.show_image);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageViewIntent=new Intent(getContext(), SingleImageActivity.class);
                if(mShow!=null) {
                    imageViewIntent.putExtra("image", mShow.getPoster_path());
                }else{
                    imageViewIntent.putExtra("image", mShowSecond.getPoster_path());
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
        adapter = new TvCastAdapter(mCast, getContext());
        tv_show_cast = (RecyclerView) v.findViewById(R.id.tv_cast_recycle_list);
        tv_show_cast.setAdapter(adapter);

        mMoreLikeThis = (RecyclerView) v.findViewById(R.id.shows_like_this);
        similar = new ArrayList<>();
        similarAdapter = new HorizontalTvAdapter(similar, getContext());

        mMoreLikeThis.setAdapter(similarAdapter);


        mBackdropImage = (ImageView) v.findViewById(R.id.backdrop_image);
        mPlayButton = (ImageButton) v.findViewById(R.id.play_trailer);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<ResultTrailer> keyResult = apiInterface.getTvTrailerKey(getActivity().getIntent().getLongExtra("id", -1));
                keyResult.enqueue(new Callback<ResultTrailer>() {
                    @Override
                    public void onResponse(Call<ResultTrailer> call, Response<ResultTrailer> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResults().size() == 0) {
                                Snackbar.make(view, "No trailer Available", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            String key = response.body().getResults().get(0).getKey();
                            Intent youtubeIntent=new Intent(getContext(), YoutubeActivity.class) ;
                            youtubeIntent.putExtra("videoKey",key);
                            startActivity(youtubeIntent);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultTrailer> call, Throwable t) {

                    }
                });
            }
        });

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();

                if (sessionId != null) {
                    if (alreadyWatchlist) {
                        WatchList watchList = new WatchList("tv",  tvId,false);

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
                        WatchList watchList = new WatchList("tv", tvId, true);
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
                    Cursor c=readDb.query(MovieDbHelper.TV_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null,null,null,null);
                    if(c.getCount()==0){
                        Snackbar.make(view,"Added to your wishlist",Snackbar.LENGTH_SHORT).show();
                        wishlistBtn.setText("Added to wishlist");
                        wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_black_24dp,0,0);
                        SQLiteDatabase db=helper.getWritableDatabase();
                        helper.addToSqlite(db,MovieDbHelper.TV_WISHLIST_TABLE,tvId,getActivity().getIntent().getStringExtra("title"),getActivity().getIntent().getStringExtra("poster"));
                    }
                    else{
                        Snackbar.make(view,"Removed from your wishlist",Snackbar.LENGTH_SHORT).show();
                        wishlistBtn.setText("Wishlist");
                        wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_border_black_24dp,0,0);
                        SQLiteDatabase db=helper.getWritableDatabase();
                        db.delete(MovieDbHelper.TV_WISHLIST_TABLE,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null);

                    }
                    c.close();
                }

            }
        });

        fetchData();

        return v;
    }





    private void fetchData() {
        final ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {
            Call<TvShowDetails> tvShowDetails = apiInterface.getSingleTvShowDetails(getActivity().getIntent().getLongExtra("id", -1),sessionId);

            tvShowDetails.enqueue(new Callback<TvShowDetails>() {
                @Override
                public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                    if (response.isSuccessful()) {
                        mShow = new TvShowDetails();

                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path()).into(poster);
                        startPostponedEnterTransition();
                        title.setText(response.body().getName());
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                        mShow = response.body();
                        for (int i = 0; i < mShow.getGenres().size(); i++) {
                            if (i < mShow.getGenres().size() - 1) {
                                tvGenre.append(mShow.getGenres().get(i).getName() + "|");
                            } else {
                                tvGenre.append(mShow.getGenres().get(i).getName());
                            }
                        }
                        overviewTv.setText(mShow.getOverview());
                        if (sessionId != null) {
                            if (mShow.getAccount_states().isWatchlist()) {
                                wishlistBtn.setText("Added to WatchList");
                                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
                                alreadyWatchlist=true;
                            } else {
                                wishlistBtn.setText("Watchlist");
                                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
                                alreadyWatchlist=false;
                            }
                            if (mShow.getAccount_states().isFavorite()) {
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
                        mTvRatings.setText(mShow.getVote_average() + " (" + mShow.getVote_count() + ")");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        if (mShow.getFirst_air_date() != null) {
                            try {
                                date = sdfd.parse(mShow.getFirst_air_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mReleaseTv.setText(sdf.format(date));
                        }

                    }else{
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<TvShowDetails> call, Throwable t) {
                    if(t.getMessage().contains("java.lang.IllegalStateException: Expected a boolean but was BEGIN_OBJECT")){
                        fetchDataRated();
                    }
                }
            });
        }
        if (getActivity() != null) {

            Call<CastResults> castDetails = apiInterface.getTvCast(getActivity().getIntent().getLongExtra("id", -1));
            castDetails.enqueue(new Callback<CastResults>() {
                @Override
                public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                    if (response.isSuccessful()) {

                        ArrayList<Cast> castJson = response.body().getCast();
                        mCast.clear();
                        mCast.addAll(castJson);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<CastResults> call, Throwable t) {

                }
            });


        }
        if (getActivity() != null) {
            Call<TvResults> similarTv = apiInterface.getTvSimilarResults(getActivity().getIntent().getLongExtra("id", -1));
            similarTv.enqueue(new Callback<TvResults>() {
                @Override
                public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                    if (response.isSuccessful()) {
                        ArrayList<TvShow> similarJson = response.body().getResults();
                        similar.clear();
                        similar.addAll(similarJson);
                        similarAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("hello", "onResponse: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<TvResults> call, Throwable t) {

                }
            });
        }




    }

    private void setWishlistBtn() {
        helper=new MovieDbHelper(getContext());
        readDb=helper.getReadableDatabase();

        Cursor c=readDb.query(MovieDbHelper.TV_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null,null,null,null);
        if(c.getCount()==0){
            wishlistBtn.setText("Wishlist");
            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_border_black_24dp,0,0);
        }else{
            wishlistBtn.setText("Added to wishlist");
            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_black_24dp,0,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setWishlistBtn();
    }

    private void fetchDataRated() {
        final ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {
            Call<TvShowDetailsSecond> tvShowDetails = apiInterface.getSingleTvShowDetailsRated(getActivity().getIntent().getLongExtra("id", -1), sessionId);

            tvShowDetails.enqueue(new Callback<TvShowDetailsSecond>() {
                @Override
                public void onResponse(Call<TvShowDetailsSecond> call, Response<TvShowDetailsSecond> response) {
                    if (response.isSuccessful()) {
                        mShowSecond = new TvShowDetailsSecond();

                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path()).into(poster);
                        startPostponedEnterTransition();
                        title.setText(response.body().getName());
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                        mShowSecond = response.body();
                        for (int i = 0; i < mShowSecond.getGenres().size(); i++) {
                            if (i < mShowSecond.getGenres().size() - 1) {
                                tvGenre.append(mShowSecond.getGenres().get(i).getName() + "|");
                            } else {
                                tvGenre.append(mShowSecond.getGenres().get(i).getName());
                            }
                        }
                        overviewTv.setText(mShowSecond.getOverview());
                        if (sessionId != null) {
                            if (mShowSecond.getAccount_states().isWatchlist()) {
                                wishlistBtn.setText("Added to WatchList");
                                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_black_24dp, 0, 0);
                                alreadyWatchlist = true;
                            } else {
                                wishlistBtn.setText("Watchlist");
                                wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_border_black_24dp, 0, 0);
                                alreadyWatchlist = false;
                            }
                            if (mShowSecond.getAccount_states().isFavorite()) {
                                mFavourite.setText("Favourite");
                                mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hearts, 0, 0);
                                alreadyFavourite = true;

                            } else {
                                mFavourite.setText("Mark as Favourite");
                                mFavourite.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_heart, 0, 0);
                                alreadyFavourite = false;
                            }
                            rateThis.setText("Your Rating " + mShowSecond.getAccount_states().getRated().getValue());
                        } else {
                            rateThis.setText("Rate this ");
                        }
                        mTvRatings.setText(mShowSecond.getVote_average() + " (" + mShowSecond.getVote_count() + ")");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        if (mShowSecond.getFirst_air_date() != null) {
                            try {
                                date = sdfd.parse(mShowSecond.getFirst_air_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mReleaseTv.setText(sdf.format(date));
                        }

                    } else {
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<TvShowDetailsSecond> call, Throwable t) {
                    if (t.getMessage().contains("java.lang.IllegalStateException: Expected a boolean but was BEGIN_OBJECT")) {
                        fetchDataRated();
                    }
                }
            });
        }
    }


}
