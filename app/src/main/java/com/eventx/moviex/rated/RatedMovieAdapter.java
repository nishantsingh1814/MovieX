package com.eventx.moviex.rated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

/**
 * Created by Nishant on 4/27/2017.
 */

public class RatedMovieAdapter  extends RecyclerView.Adapter<RatedMovieAdapter.VerticalWishlistHolder> {

    ArrayList<Movie> mMovie;
    LayoutInflater inflater;
    Context mContext;
    private String sessionId;
    private long accountId;
    SharedPreferences sp;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;


    public RatedMovieAdapter(ArrayList<Movie> movie, Context context) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        sp = context.getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        sessionId = sp.getString("session", null);
        accountId = sp.getLong("account", -1);
    }

    class VerticalWishlistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movieTitle;
        private ImageView movieImage;
        private TextView rating;



        public VerticalWishlistHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.wishlist_movie_title);
            movieImage = (ImageView) itemView.findViewById(R.id.wishlist_movie_poster);
            rating = (TextView) itemView.findViewById(R.id.rating);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Intent movieDetailsIntent = new Intent(mContext, MoviesDetailsActivity.class);


            movieDetailsIntent.putExtra("id", mMovie.get(getAdapterPosition()).getMovieId());
            movieDetailsIntent.putExtra("title", mMovie.get(getAdapterPosition()).getTitle());
            movieDetailsIntent.putExtra("poster", mMovie.get(getAdapterPosition()).getPoster_path());
            Activity act = (Activity) mContext;
            mContext.startActivity(movieDetailsIntent);
            act.overridePendingTransition(R.anim.slide_in, R.anim.no_change);

        }
    }

    @Override
    public VerticalWishlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rated_item, parent, false);
        return new VerticalWishlistHolder(view);
    }

    @Override
    public void onBindViewHolder(final VerticalWishlistHolder holder, final int position) {
        Movie movie = mMovie.get(position);


        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(movie.getTitle());

        holder.rating.setText(""+movie.getRating());

    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
