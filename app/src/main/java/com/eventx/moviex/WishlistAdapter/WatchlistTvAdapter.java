package com.eventx.moviex.WishlistAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.LoginAccount.WatchList;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 4/25/2017.
 */

public class WatchlistTvAdapter extends RecyclerView.Adapter<WatchlistTvAdapter.VerticalWishlistHolder> {

    ArrayList<TvShow> mMovie;
    LayoutInflater inflater;
    Context mContext;
    private String sessionId;
    private long accountId;
    SharedPreferences sp;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;


    public WatchlistTvAdapter(ArrayList<TvShow> movie, Context context) {
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


            movieDetailsIntent.putExtra("id", mMovie.get(getAdapterPosition()).getTvId());
            movieDetailsIntent.putExtra("title", mMovie.get(getAdapterPosition()).getName());
            movieDetailsIntent.putExtra("poster", mMovie.get(getAdapterPosition()).getPoster_path());
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
        TvShow movie = mMovie.get(position);


        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(movie.getName());

        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.cardMenu);

                popup.inflate(R.menu.wishlist_card_popup);
                Menu menu = popup.getMenu();
                MenuItem settingsMenuItem = menu.findItem(R.id.remove);
                SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                settingsMenuItem.setTitle(s);
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        WatchList watchList = new WatchList("tv", mMovie.get(position).getTvId(), false);
                        ApiInterface apiInterface= ApiClient.getApiInterface();
                        Call<WatchList> watchListCall = apiInterface.removeFromWatchList(watchList, accountId, sessionId);
                        watchListCall.enqueue(new Callback<WatchList>() {
                            @Override
                            public void onResponse(Call<WatchList> call, Response<WatchList> response) {
                                if (response.isSuccessful()) {
                                    Snackbar.make(view, "Removed From watchlist", Snackbar.LENGTH_SHORT).show();
                                    mMovie.remove(position);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<WatchList> call, Throwable t) {

                            }
                        });
                        return true;
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
