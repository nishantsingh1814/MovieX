package com.eventx.moviex.TvAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.Episodes;
import com.eventx.moviex.TvModels.TvShow;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/29/2017.
 */

public class EpisodeAdapter extends RecyclerView.Adapter <EpisodeAdapter.EpisodeHolder>{

    ArrayList<Episodes> mEpisode;
    LayoutInflater inflater;
    Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }
    public EpisodeAdapter(ArrayList<Episodes> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mEpisode =mTvShow;
        mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.episode_list_item,parent,false);
        return new EpisodeHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, int position) {
        Episodes episode=mEpisode.get(position);

        holder.episodeNumber.setText("S"+episode.getSeason_number()+" E"+episode.getEpisode_number()+" "+episode.getName());
    }

    @Override
    public int getItemCount() {
        return mEpisode.size();
    }

    class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView episodeNumber;

        public EpisodeHolder(View itemView) {
            super(itemView);
            episodeNumber=(TextView)itemView.findViewById(R.id.episode_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition=getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
