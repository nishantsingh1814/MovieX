package com.eventx.moviex.Wishlist;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.WishlistAdapter.VerticalWishlistAdapter;

import java.util.ArrayList;

/**
 * Created by Nishant on 4/7/2017.
 */

public class WishlistTvFragment extends Fragment{
    RecyclerView wishlist;
    VerticalWishlistAdapter adapter;
    ArrayList<MovieWishlist> details;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.wishlist_movie_frag, container, false);
        details = new ArrayList<>();
        wishlist = (RecyclerView) v.findViewById(R.id.recycler_view);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            wishlist.setLayoutManager(new GridLayoutManager(getContext(), 5));
        }else{
            wishlist.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        adapter = new VerticalWishlistAdapter(details, getContext());
        wishlist.setAdapter(adapter);

        setUpViews();
        return v;
    }

    private void setUpViews() {
        details.clear();
        MovieDbHelper helper = new MovieDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(MovieDbHelper.TV_WISHLIST_TABLE, null, null, null, null, null, null);

        while (c.moveToNext()) {
            MovieWishlist movie = new MovieWishlist();
            movie.setTitle(c.getString(c.getColumnIndex(MovieDbHelper.COLUMN_TV_TITLE)));
            movie.setImage(c.getString(c.getColumnIndex(MovieDbHelper.COLUMN_TV_POSTER)));
            movie.setId(c.getLong(c.getColumnIndex(MovieDbHelper.COLUMN_TV_ID)));
            movie.setCategory("Tv");

            details.add(movie);
        }
        adapter.notifyDataSetChanged();

        c.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpViews();
    }


}
