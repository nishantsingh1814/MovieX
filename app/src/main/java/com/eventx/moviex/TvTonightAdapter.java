package com.eventx.moviex;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/30/2017.
 */

class TvTonightAdapter extends RecyclerView.Adapter<TvTonightAdapter.TvTonightHolder> {
    ArrayList<TvShow> mMovie;
    LayoutInflater inflater;
    Context mContext;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;

    ListItemClickListener listItemClickListener;
    public interface ListItemClickListener{
        void itemClickListener(int clickedPosition);
    }



    public TvTonightAdapter(ArrayList<TvShow> movie, Context context, ListItemClickListener listener) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        listItemClickListener=listener;
    }

    class TvTonightHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private ImageView movieImage;
        public TvTonightHolder(View itemView) {
            super(itemView);
            movieImage=(ImageView) itemView.findViewById(R.id.now_showing_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listItemClickListener.itemClickListener(getAdapterPosition());
        }
    }
    @Override
    public TvTonightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.now_showing_list_item, parent, false);
        return new TvTonightHolder(view);
    }

    @Override
    public void onBindViewHolder(TvTonightHolder holder, int position) {
        TvShow movie=mMovie.get(position);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(holder.movieImage);
    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
