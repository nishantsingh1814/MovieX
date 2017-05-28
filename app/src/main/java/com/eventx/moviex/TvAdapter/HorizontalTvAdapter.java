package com.eventx.moviex.TvAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
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


    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }
    public HorizontalTvAdapter(ArrayList<TvShow> mTvShow, Context context) {
        mShow=mTvShow;
        mContext=context;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public HorizontalTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.tv_horizontal_item,parent,false);
        return new HorizontalTvHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalTvHolder holder, int position) {
        TvShow show=mShow.get(position);

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+show.getPoster_path()).into(holder.tvShowImage);
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

            Intent movieDetailsIntent = new Intent(mContext, TvShowDetailsActivity.class);
            movieDetailsIntent.putExtra("id", mShow.get(getAdapterPosition()).getTvId());
            movieDetailsIntent.putExtra("title", mShow.get(getAdapterPosition()).getName());
            movieDetailsIntent.putExtra("poster",mShow.get(getAdapterPosition()).getPoster_path());
            Activity act=(Activity)mContext;
            mContext.startActivity(movieDetailsIntent);
            act.overridePendingTransition(R.anim.slide_in,R.anim.no_change);


        }
    }
}
