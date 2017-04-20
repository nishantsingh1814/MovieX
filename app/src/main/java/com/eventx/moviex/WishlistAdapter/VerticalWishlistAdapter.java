package com.eventx.moviex.WishlistAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.Wishlist.MovieWishlist;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.activeandroid.Cache.getContext;

/**
 * Created by Nishant on 4/7/2017.
 */

public class VerticalWishlistAdapter extends RecyclerView.Adapter<VerticalWishlistAdapter.VerticalWishlistHolder> {

    ArrayList<MovieWishlist> mMovie;
    LayoutInflater inflater;
    Context mContext;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;


    public VerticalWishlistAdapter(ArrayList<MovieWishlist> movie, Context context) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    class VerticalWishlistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movieTitle;
        private ImageView movieImage;
        private TextView cardMenu;


        public VerticalWishlistHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.wishlist_movie_title);
            movieImage = (ImageView) itemView.findViewById(R.id.wishlist_movie_poster);
            cardMenu = (TextView) itemView.findViewById(R.id.card_menu);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Intent movieDetailsIntent = new Intent(mContext, TvShowDetailsActivity.class);

            if (mMovie.get(getAdapterPosition()).getCategory().equals("Movie")) {
                movieDetailsIntent = new Intent(mContext, MoviesDetailsActivity.class);
            }
            movieDetailsIntent.putExtra("id", mMovie.get(getAdapterPosition()).getId());
            movieDetailsIntent.putExtra("title", mMovie.get(getAdapterPosition()).getTitle());
            movieDetailsIntent.putExtra("poster", mMovie.get(getAdapterPosition()).getImage());
            Activity act = (Activity) mContext;
            mContext.startActivity(movieDetailsIntent);
            act.overridePendingTransition(R.anim.slide_in, R.anim.no_change);

        }
    }

    @Override
    public VerticalWishlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wishlist_item, parent, false);
        return new VerticalWishlistHolder(view);
    }

    @Override
    public void onBindViewHolder(final VerticalWishlistHolder holder, final int position) {
        MovieWishlist movie = mMovie.get(position);

        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.cardMenu);
                popup.inflate(R.menu.wishlist_card_popup);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Snackbar.make(view, "Removed from your wishlist", Snackbar.LENGTH_SHORT).show();

                        MovieDbHelper helper;
                        helper = new MovieDbHelper(mContext);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if(mMovie.get(position).getCategory().equals("Movie")) {
                            db.delete(MovieDbHelper.MOVIE_WISHLIST_TABLE, MovieDbHelper.COLUMN_MOVIE_ID + " = " + mMovie.get(position).getId(), null);
                        }else{
                            db.delete(MovieDbHelper.TV_WISHLIST_TABLE,MovieDbHelper.COLUMN_TV_ID+" = "+mMovie.get(position).getId(),null);
                        }
                        mMovie.remove(position);
                        notifyDataSetChanged();

                        return true;
                    }
                });
                popup.show();
            }
        });

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(holder.movieImage);
        holder.movieTitle.setText(movie.getTitle());


    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
