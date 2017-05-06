package com.eventx.moviex.MovieAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class HorizontalMoviesAdapter  extends RecyclerView.Adapter<HorizontalMoviesAdapter.MoviesHolder> {
    ArrayList<Movie> mMovie;
    LayoutInflater inflater;
    Context mContext;


    public interface ListItemClickListener{
        void onListItemClick(int clickedPosition);
    }

    public HorizontalMoviesAdapter(ArrayList<Movie> movie, Context context) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView movieTitle;
        private ImageView movieImage;
        private TextView movieRating;


        public MoviesHolder(View itemView) {
            super(itemView);

            movieRating=(TextView)itemView.findViewById(R.id.rating);
            movieTitle=(TextView)itemView.findViewById(R.id.movie_title);
            movieImage=(ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Intent movieDetailsIntent = new Intent(mContext, MoviesDetailsActivity.class);
            movieDetailsIntent.putExtra("id", mMovie.get(getAdapterPosition()).getMovieId());
            movieDetailsIntent.putExtra("title", mMovie.get(getAdapterPosition()).getTitle());
            movieDetailsIntent.putExtra("poster",mMovie.get(getAdapterPosition()).getPoster_path());
            Activity act=(Activity)mContext;
            mContext.startActivity(movieDetailsIntent);
            act.overridePendingTransition(R.anim.slide_in,R.anim.no_change);


        }
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.horizontal_list_item, parent, false);

        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {
        Movie movie=mMovie.get(position);

        holder.movieRating.setText(movie.getVote_average()+"");
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(movie.getTitle());

    }

    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
