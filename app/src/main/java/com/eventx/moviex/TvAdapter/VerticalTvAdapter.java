package com.eventx.moviex.TvAdapter;

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
import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/28/2017.
 */

public class VerticalTvAdapter extends RecyclerView.Adapter<VerticalTvAdapter.VerticalTvHolder> {
    ArrayList<TvShow> mShow;
    LayoutInflater inflater;
    Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public VerticalTvAdapter(ArrayList<TvShow> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mShow = mTvShow;
        mContext = context;
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
                PopupMenu popup = new PopupMenu(mContext, holder.cardMenu);
                final MovieDbHelper helper;
                SQLiteDatabase readDb;

                helper = new MovieDbHelper(mContext);
                readDb = helper.getReadableDatabase();
                Cursor c = readDb.query(MovieDbHelper.TV_WISHLIST_TABLE, null, MovieDbHelper.COLUMN_TV_ID + " = " + mShow.get(position).getTvId(), null, null, null, null);
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
                            helper.addToSqlite(db, MovieDbHelper.TV_WISHLIST_TABLE, mShow.get(position).getTvId(), mShow.get(position).getName(), mShow.get(position).getPoster_path());
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

                            SQLiteDatabase db=helper.getWritableDatabase();
                            db.delete(MovieDbHelper.TV_WISHLIST_TABLE,MovieDbHelper.COLUMN_TV_ID+" = "+mShow.get(position).getTvId(),null);

                            notifyDataSetChanged();

                            return true;
                        }
                    });
                }
                c.close();
                popup.show();
            }
        });
        String overview = show.getOverview();
        String[] arr = overview.split(" ");
        String newOverview = "";
        for (int i = 0; i < 15 && i < arr.length; i++) {
            newOverview += arr[i] + " ";
        }


        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + show.getPoster_path()).into(holder.tvShowImage);
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
