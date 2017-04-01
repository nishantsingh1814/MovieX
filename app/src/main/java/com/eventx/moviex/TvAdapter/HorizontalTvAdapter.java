package com.eventx.moviex.TvAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.R;
import com.eventx.moviex.TvModels.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class HorizontalTvAdapter extends RecyclerView.Adapter<HorizontalTvAdapter.HorizontalTvHolder> {
    ArrayList<TvShow> mShow;
    LayoutInflater inflater;
    Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }
    public HorizontalTvAdapter(ArrayList<TvShow> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mShow=mTvShow;
        mContext=context;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public HorizontalTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.horizontal_list_item,parent,false);

        return new HorizontalTvHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalTvHolder holder, int position) {
        TvShow show=mShow.get(position);

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+show.getPoster_path()).into(holder.tvShowImage);
        holder.tvShowTitle.setText(show.getName());
    }

    @Override
    public int getItemCount() {
        return mShow.size();
    }

    class HorizontalTvHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvShowTitle;
        private ImageView tvShowImage;

        public HorizontalTvHolder(View itemView) {
            super(itemView);

            tvShowTitle=(TextView)itemView.findViewById(R.id.movie_title);
            tvShowImage=(ImageView)itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
