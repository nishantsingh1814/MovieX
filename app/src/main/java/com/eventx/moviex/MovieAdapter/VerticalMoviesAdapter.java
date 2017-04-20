package com.eventx.moviex.MovieAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.MovieModels.Movie;
import com.eventx.moviex.MovieFragments.MoviesButtonHandleFragment;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.activeandroid.Cache.getContext;

/**
 * Created by Nishant on 3/27/2017.
 */

public class VerticalMoviesAdapter extends RecyclerView.Adapter<VerticalMoviesAdapter.VerticalMoviesHolder> {
    ArrayList<Movie> mMovie;
    LayoutInflater inflater;
    Context mContext;

    MoviesButtonHandleFragment.MovieClickListener movieClickListener;

    ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void itemClickListener(int clickedPosition);
    }


    public VerticalMoviesAdapter(ArrayList<Movie> movie, Context context, ListItemClickListener listener) {
        mMovie = movie;
        inflater = LayoutInflater.from(context);
        mContext = context;
        listItemClickListener = listener;
    }

    class VerticalMoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movieTitle;
        private ImageView movieImage;
        private TextView movieRate;
        private ImageView star;
        private TextView movieOverview;
        private TextView cardMenu;

        public VerticalMoviesHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movie_title_vertical);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_poster_vertical);
            movieRate = (TextView) itemView.findViewById(R.id.movie_rating_vertical);
            movieOverview = (TextView) itemView.findViewById(R.id.brief_overview);
            star = (ImageView) itemView.findViewById(R.id.star_image);
            star.setColorFilter(Color.parseColor("#ff80ff"));
            cardMenu = (TextView) itemView.findViewById(R.id.card_menu);
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
    public void onBindViewHolder(final VerticalMoviesHolder holder, final int position) {
        Movie movie = mMovie.get(position);
        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.cardMenu);

                final MovieDbHelper helper;
                SQLiteDatabase readDb;

                helper = new MovieDbHelper(mContext);
                readDb = helper.getReadableDatabase();
                Cursor c = readDb.query(MovieDbHelper.MOVIE_WISHLIST_TABLE, null, MovieDbHelper.COLUMN_MOVIE_ID + " = " + mMovie.get(position).getMovieId(), null, null, null, null);
                if (c.getCount() == 0) {
                    popup.inflate(R.menu.card_popup);


                    Menu menu=popup.getMenu();
                    MenuItem settingsMenuItem =menu.findItem(R.id.menu_wishlist);
                    SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                    settingsMenuItem.setTitle(s);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Snackbar.make(view, "Added to you wishlist", Snackbar.LENGTH_SHORT).show();
                            SQLiteDatabase db = helper.getWritableDatabase();
                            helper.addToSqlite(db, MovieDbHelper.MOVIE_WISHLIST_TABLE, mMovie.get(position).getMovieId(), mMovie.get(position).getTitle(), mMovie.get(position).getPoster_path());
                            notifyDataSetChanged();
                            return true;
                        }
                    });
                } else {
                    popup.inflate(R.menu.wishlist_card_popup);
                    Menu menu=popup.getMenu();
                    MenuItem settingsMenuItem =menu.findItem(R.id.remove);
                    SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                    settingsMenuItem.setTitle(s);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Snackbar.make(view, "Removed from your wishlist", Snackbar.LENGTH_SHORT).show();

                            SQLiteDatabase db = helper.getWritableDatabase();
                            db.delete(MovieDbHelper.MOVIE_WISHLIST_TABLE, MovieDbHelper.COLUMN_MOVIE_ID + " = " + mMovie.get(position).getMovieId(), null);
                            notifyDataSetChanged();

                            return true;
                        }
                    });
                }
                c.close();

                popup.show();
            }
        });
        String overview = movie.getOverview();
        String[] arr = overview.split(" ");
        String newOverview = "";
        for (int i = 0; i < 15 && i < arr.length; i++) {
            newOverview += arr[i] + " ";
        }
        holder.movieOverview.setText(newOverview);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.movieImage);
        holder.movieTitle.setText(position + 1 + ". " + movie.getTitle());
        holder.movieRate.setText(movie.getVote_average() + "");
    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }
}
