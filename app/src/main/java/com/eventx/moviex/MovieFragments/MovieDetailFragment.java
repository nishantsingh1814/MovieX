package com.eventx.moviex.MovieFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.MovieModels.MovieResults;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.MovieActivities.CastActivity;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieAdapter.CastAdapter;
import com.eventx.moviex.MovieAdapter.HorizontalMoviesAdapter;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.Wishlist.WishlistAcitvity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/27/2017.
 */

public class MovieDetailFragment extends Fragment implements HorizontalMoviesAdapter.ListItemClickListener {
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
    long movieId;
    TextView movieTitle;

    MovieDbHelper helper;
    SQLiteDatabase readDb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movie_details_frag, container, false);
        similar = new ArrayList<>();
        adapter = new HorizontalMoviesAdapter(similar, getContext(), this);
        genreTv = (TextView) v.findViewById(R.id.movie_genre);
        runtimeTv = (TextView) v.findViewById(R.id.movie_runtime);
        viewMoreBtn = (Button) v.findViewById(R.id.view_more);
        movieTitle = (TextView) v.findViewById(R.id.movie_detail_title);
        overviewTv = (TextView) v.findViewById(R.id.movie_overview);
        mMoviePoster = (ImageView) v.findViewById(R.id.movie_image);
        mMovieRatings = (TextView) v.findViewById(R.id.movie_rating);
        mGenreTv = (TextView) v.findViewById(R.id.genre_tv);
        mReleaseTv = (TextView) v.findViewById(R.id.movie_release_date);
        mTagline = (TextView) v.findViewById(R.id.movie_tagline);
        mMoreLikeThis = (RecyclerView) v.findViewById(R.id.movies_like_this);
        mCastList = (RecyclerView) v.findViewById(R.id.cast_recycle_list);
        wishlistBtn = (Button) v.findViewById(R.id.wishlist_btn);


        movieId = getActivity().getIntent().getLongExtra("id", -1);

        setWishlistBtn();


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
        mMovie = new MovieDetails();

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
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
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
            public void onClick(View view) {

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
        });


        fetchData();
        return v;
    }

    private void setWishlistBtn() {
        helper=new MovieDbHelper(getContext());
        readDb=helper.getReadableDatabase();

        Cursor c=readDb.query(MovieDbHelper.MOVIE_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_MOVIE_ID+" = "+movieId,null,null,null,null);
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

    private void fetchData() {

        ApiInterface apiInterface = ApiClient.getApiInterface();

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


        Call<MovieDetails> movieDetails = apiInterface.getMovieDetails(movieId);
        movieDetails.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful()) {

                    Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                    movieTitle.setText(response.body().getTitle());
                    mMovie = response.body();
                    overviewTv.setText(mMovie.getOverview());

                    int hour = mMovie.getRuntime() / 60;
                    int minute = mMovie.getRuntime() % 60;
                    runtimeTv.setText(hour + " hrs " + minute + " mins ");
                    Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + mMovie.getPoster_path()).into(mMoviePoster);
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
                Log.i("hello", "onRespon: " + t.getMessage());


            }
        });


        Call<MovieResults> similarMovies = apiInterface.getSimilarResults(movieId);
        similarMovies.enqueue(new Callback<MovieResults>() {


            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if(response.isSuccessful()) {
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


    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent movieDetailsIntent = new Intent(getActivity(), MoviesDetailsActivity.class);
        movieDetailsIntent.putExtra("id", similar.get(clickedPosition).getMovieId());
        movieDetailsIntent.putExtra("title", similar.get(clickedPosition).getTitle());
        movieDetailsIntent.putExtra("poster",similar.get(clickedPosition).getPoster_path());
        startActivity(movieDetailsIntent);
    }

}
