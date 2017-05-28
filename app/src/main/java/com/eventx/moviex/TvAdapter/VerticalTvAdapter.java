package com.eventx.moviex.TvAdapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.LoginAccount.WatchList;
import com.eventx.moviex.MovieModels.MovieDetails;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowD;
import com.eventx.moviex.TvModels.TvShowDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/28/2017.
 */

public class VerticalTvAdapter extends RecyclerView.Adapter<VerticalTvAdapter.VerticalTvHolder> {
    ArrayList<TvShow> mShow;
    LayoutInflater inflater;
    Context mContext;
    private String sessionId;
    private long accountId;
    SharedPreferences sp;


    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public VerticalTvAdapter(ArrayList<TvShow> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mShow = mTvShow;
        mContext = context;
        sp = context.getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        sessionId = sp.getString("session", null);
        accountId = sp.getLong("account", -1);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VerticalTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vertical_list_item, parent, false);

        return new VerticalTvHolder(view);
    }

    @Override
    public void onBindViewHolder(final VerticalTvHolder holder, final int position) {
        TvShow show = mShow.get(position);

        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popup = new PopupMenu(mContext, holder.cardMenu);

                if (sessionId == null) {
                    final MovieDbHelper helper;
                    SQLiteDatabase readDb;

                    helper = new MovieDbHelper(mContext);
                    readDb = helper.getReadableDatabase();
                    Cursor c = readDb.query(MovieDbHelper.TV_WISHLIST_TABLE, null, MovieDbHelper.COLUMN_TV_ID + " = " + mShow.get(position).getTvId(), null, null, null, null);
                    if (c.getCount() == 0) {
                        popup.inflate(R.menu.card_popup);
                        Menu menu = popup.getMenu();
                        MenuItem settingsMenuItem = menu.findItem(R.id.menu_wishlist);
                        SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                        settingsMenuItem.setTitle(s);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Snackbar.make(view, "Added to you wishlist", Snackbar.LENGTH_SHORT).show();
                                SQLiteDatabase db = helper.getWritableDatabase();
                                helper.addToSqlite(db, MovieDbHelper.TV_WISHLIST_TABLE, mShow.get(position).getTvId(), mShow.get(position).getName(), mShow.get(position).getPoster_path());
                                notifyDataSetChanged();
                                return true;
                            }
                        });
                    } else {
                        popup.inflate(R.menu.wishlist_card_popup);
                        Menu menu = popup.getMenu();
                        MenuItem settingsMenuItem = menu.findItem(R.id.remove);
                        SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                        settingsMenuItem.setTitle(s);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Snackbar.make(view, "Removed from your wishlist", Snackbar.LENGTH_SHORT).show();

                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.delete(MovieDbHelper.TV_WISHLIST_TABLE, MovieDbHelper.COLUMN_TV_ID + " = " + mShow.get(position).getTvId(), null);

                                notifyDataSetChanged();

                                return true;
                            }
                        });
                    }

                    c.close();
                    popup.show();
                }else{
                    final ApiInterface apiInterface = ApiClient.getApiInterface();
                    Call<TvShowD> call = apiInterface.getSingleTvShowD(mShow.get(position).getTvId(), sessionId);
                    call.enqueue(new Callback<TvShowD>() {
                        @Override
                        public void onResponse(Call<TvShowD> call, Response<TvShowD> response) {
                            if (response.isSuccessful()) {

                                if (response.body().getAccount_states().isWatchlist()) {
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

                                            WatchList watchList = new WatchList("tv", mShow.get(position).getTvId(), false);

                                            Call<WatchList> watchListCall = apiInterface.removeFromWatchList(watchList, accountId, sessionId);
                                            watchListCall.enqueue(new Callback<WatchList>() {
                                                @Override
                                                public void onResponse(Call<WatchList> call, Response<WatchList> response) {
                                                    if (response.isSuccessful()) {
                                                        Snackbar.make(view, "Removed From watchlist", Snackbar.LENGTH_SHORT).show();

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<WatchList> call, Throwable t) {

                                                }
                                            });
                                            return true;
                                        }
                                    });

                                } else {
                                    popup.inflate(R.menu.card_popup);


                                    Menu menu = popup.getMenu();
                                    MenuItem settingsMenuItem = menu.findItem(R.id.menu_wishlist);
                                    SpannableString s = new SpannableString(settingsMenuItem.getTitle());
                                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                                    settingsMenuItem.setTitle(s);
                                    popup.show();

                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            WatchList watchList = new WatchList("tv", mShow.get(position).getTvId(), true);
                                            Call<WatchList> watchListCall = apiInterface.addToWatchList(watchList, accountId, sessionId);
                                            watchListCall.enqueue(new Callback<WatchList>() {
                                                @Override
                                                public void onResponse(Call<WatchList> call, Response<WatchList> response) {
                                                    if (response.isSuccessful()) {
                                                        Snackbar.make(view, "Added to your Watchlist", Snackbar.LENGTH_SHORT).show();
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
                            }
                        }

                        @Override
                        public void onFailure(Call<TvShowD> call, Throwable t) {

                        }
                    });
                }
            }
        });
        String overview = show.getOverview();
        String[] arr = overview.split(" ");
        String newOverview = "";
        for (int i = 0; i < 15 && i < arr.length; i++) {
            newOverview += arr[i] + " ";
        }


        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + show.getPoster_path()).into(holder.tvShowImage);
        holder.tvShowTitle.setText(show.getName());
        holder.tvShowRate.setText(show.getVote_average() + "");

        holder.tvShowOverview.setText(newOverview);
    }

    @Override
    public int getItemCount() {
        return mShow.size();
    }

    class VerticalTvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvShowTitle;
        private ImageView tvShowImage;
        private TextView tvShowOverview;
        private TextView tvShowRate;
        private ImageView star;
        private TextView cardMenu;

        public VerticalTvHolder(View itemView) {
            super(itemView);

            tvShowTitle = (TextView) itemView.findViewById(R.id.movie_title_vertical);
            tvShowOverview = (TextView) itemView.findViewById(R.id.brief_overview);
            tvShowImage = (ImageView) itemView.findViewById(R.id.movie_poster_vertical);
            tvShowRate = (TextView) itemView.findViewById(R.id.movie_rating_vertical);
            star = (ImageView) itemView.findViewById(R.id.star_image);
            star.setColorFilter(Color.parseColor("#ff80ff"));
            cardMenu = (TextView) itemView.findViewById(R.id.card_menu);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
