package com.eventx.moviex.MovieAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedPosition);
    }

    public HorizontalMoviesAdapter(ArrayList<Movie> movie, Context context,ListItemClickListener listener) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        mOnClickListener = listener;
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView movieTitle;
        private ImageView movieImage;


        public MoviesHolder(View itemView) {
            super(itemView);

            movieTitle=(TextView)itemView.findViewById(R.id.movie_title);
            movieImage=(ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
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

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(position +1+". "+movie.getTitle());

    }

    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
