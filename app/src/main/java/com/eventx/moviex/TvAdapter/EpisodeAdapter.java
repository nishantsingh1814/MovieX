package com.eventx.moviex.TvAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.Episodes;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.format;

/**
 * Created by Nishant on 3/29/2017.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

    ArrayList<Episodes> mEpisode;
    LayoutInflater inflater;
    Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public EpisodeAdapter(ArrayList<Episodes> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mEpisode = mTvShow;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.episode_list_item, parent, false);
        return new EpisodeHolder(view);
    }

    @Override
    public void onBindViewHolder(final EpisodeHolder holder, int position) {
        final Episodes episode = mEpisode.get(position);

        if (episode.getEpisode_number() < 10) {
            holder.episodeName.setText("0" + episode.getEpisode_number() + ": " + episode.getName());
        } else {
            holder.episodeName.setText(episode.getEpisode_number() + ": " + episode.getName());
        }

        final MovieDbHelper helper;
        final SQLiteDatabase readDb;
        helper = new MovieDbHelper(mContext);
        String date = episode.getAir_date();
        Date date1 = null;
        SimpleDateFormat format = new SimpleDateFormat("y-M-d");
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {

        }
        SimpleDateFormat format1 = new SimpleDateFormat("E,d MMM,yyyy");

        date = format1.format(date1);

        holder.episodeDate.setText(date);
        readDb = helper.getReadableDatabase();
        final Cursor c = readDb.query(MovieDbHelper.EPISODE_WATCHED_TABLE, null, MovieDbHelper.COLUMN_EPISODE_ID + " = " + episode.getId(), null, null, null, null);

        if (c.getCount() != 0) {
            holder.watchImage.setImageResource(R.drawable.correct_white);
            holder.watchImage.setBackgroundColor(Color.parseColor("#607D8B"));
        } else {
            holder.watchImage.setImageResource(R.drawable.ic_noun_15005);
            holder.watchImage.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.watchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = readDb.query(MovieDbHelper.EPISODE_WATCHED_TABLE, null, MovieDbHelper.COLUMN_EPISODE_ID + " = " + episode.getId(), null, null, null, null);
                if (c.getCount() != 0) {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.delete(MovieDbHelper.EPISODE_WATCHED_TABLE, MovieDbHelper.COLUMN_EPISODE_ID + " = " + episode.getId(), null);
                    holder.watchImage.setImageResource(R.drawable.ic_noun_15005);
                    holder.watchImage.setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    helper.addToEpisode(db, MovieDbHelper.EPISODE_WATCHED_TABLE, episode.getId());
                    holder.watchImage.setImageResource(R.drawable.correct_white);
                    holder.watchImage.setBackgroundColor(Color.parseColor("#607D8B"));
                }
                c.close();
            }
        });
        c.close();
//        holder.episodeNumber.setText("Episode "+episode.getEpisode_number());
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + episode.getStill_path()).into(holder.episodeImage);
    }

    @Override
    public int getItemCount() {
        return mEpisode.size();
    }

    class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView episodeDate;
        private TextView episodeName;
        private ImageView episodeImage;
        private ImageView watchImage;

        public EpisodeHolder(View itemView) {
            super(itemView);
            watchImage = (ImageView) itemView.findViewById(R.id.watched);
            episodeDate = (TextView) itemView.findViewById(R.id.episode_date);
//            episodeNumber=(TextView)itemView.findViewById(R.id.episode_number);
            episodeImage = (ImageView) itemView.findViewById(R.id.episode_image);
            episodeName = (TextView) itemView.findViewById(R.id.episode_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
