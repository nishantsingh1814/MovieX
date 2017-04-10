package com.eventx.moviex.WishlistAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.R;
import com.eventx.moviex.Wishlist.MovieWishlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 4/7/2017.
 */

public class VerticalWishlistAdapter extends RecyclerView.Adapter<VerticalWishlistAdapter.VerticalWishlistHolder>{

    ArrayList<MovieWishlist> mMovie;
    LayoutInflater inflater;
    Context mContext;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;

    ListItemClickListener listItemClickListener;
    public interface ListItemClickListener{
        void itemClickListener(int clickedPosition);
    }



    public VerticalWishlistAdapter(ArrayList<MovieWishlist> movie, Context context, ListItemClickListener listener) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        listItemClickListener=listener;
    }

    class VerticalWishlistHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView movieTitle;
        private ImageView movieImage;



        public VerticalWishlistHolder(View itemView) {
            super(itemView);

            movieTitle=(TextView)itemView.findViewById(R.id.wishlist_movie_title);
            movieImage=(ImageView) itemView.findViewById(R.id.wishlist_movie_poster);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listItemClickListener.itemClickListener(getAdapterPosition());
        }
    }
    @Override
    public VerticalWishlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wishlist_item, parent, false);
        return new VerticalWishlistHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalWishlistHolder holder, int position) {
        MovieWishlist movie=mMovie.get(position);


        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+movie.getImage()).into(holder.movieImage);
        holder.movieTitle.setText(movie.getTitle());
    }



    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
