package com.eventx.moviex.MovieAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class VerticalMoviesAdapter extends RecyclerView.Adapter<VerticalMoviesAdapter.VerticalMoviesHolder> {
    ArrayList<Movie> mMovie;
    LayoutInflater inflater;
    Context mContext;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;

    ListItemClickListener listItemClickListener;
    public interface ListItemClickListener{
        void itemClickListener(int clickedPosition);
    }



    public VerticalMoviesAdapter(ArrayList<Movie> movie, Context context, ListItemClickListener listener) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        listItemClickListener=listener;
    }

    class VerticalMoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView movieTitle;
        private ImageView movieImage;
        private TextView movieRate;
        private ImageView star;
        private TextView movieOverview;


        public VerticalMoviesHolder(View itemView) {
            super(itemView);

            movieTitle=(TextView)itemView.findViewById(R.id.movie_title_vertical);
            movieImage=(ImageView) itemView.findViewById(R.id.movie_poster_vertical);
            movieRate=(TextView)itemView.findViewById(R.id.movie_rating_vertical);
            movieOverview=(TextView)itemView.findViewById(R.id.brief_overview);
            star=(ImageView)itemView.findViewById(R.id.star_image);
            star.setColorFilter(Color.parseColor("#ff80ff"));
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listItemClickListener.itemClickListener(getAdapterPosition());
        }
    }
    @Override
    public VerticalMoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vertical_list_item, parent, false);
        return new VerticalMoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalMoviesHolder holder, int position) {
        Movie movie=mMovie.get(position);

        String overview=movie.getOverview();
        String [] arr=overview.split(" ");
        String newOverview="";
        for(int i=0;i<15&&i<arr.length;i++){
            newOverview+=arr[i]+" ";
        }
        holder.movieOverview.setText(newOverview);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(position+1+". "+movie.getTitle());
        holder.movieRate.setText(movie.getVote_average()+"");
    }



    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
