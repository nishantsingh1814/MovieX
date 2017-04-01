package com.eventx.moviex.MovieFragments;

import android.content.Intent;
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

    MovieDetails mMovie;
    long movieId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movie_details_frag, container, false);
        similar = new ArrayList<>();
        adapter = new HorizontalMoviesAdapter(similar, getContext(), this);
        genreTv = (TextView) v.findViewById(R.id.movie_genre);
        runtimeTv = (TextView) v.findViewById(R.id.movie_runtime);
        viewMoreBtn = (Button) v.findViewById(R.id.view_more);
        overviewTv = (TextView) v.findViewById(R.id.movie_overview);
        mMoviePoster = (ImageView) v.findViewById(R.id.movie_image);
        mMovieRatings = (TextView) v.findViewById(R.id.movie_rating);
        mGenreTv = (TextView) v.findViewById(R.id.genre_tv);
        mReleaseTv = (TextView) v.findViewById(R.id.movie_release_date);
        mTagline = (TextView) v.findViewById(R.id.movie_tagline);
        mMoreLikeThis = (RecyclerView) v.findViewById(R.id.movies_like_this);
        mCastList = (RecyclerView) v.findViewById(R.id.cast_recycle_list);

        movieId = getActivity().getIntent().getLongExtra("id", -1);

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


        fetchData();
        return v;
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
                    if(date!=null) {
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
                    Log.i("hello", "onResponse: " + response.message());
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
                ArrayList<Movie> popularJson = response.body().getResults();
                similar.clear();
                similar.addAll(popularJson);
                adapter.notifyDataSetChanged();
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
        startActivity(movieDetailsIntent);
    }

}
