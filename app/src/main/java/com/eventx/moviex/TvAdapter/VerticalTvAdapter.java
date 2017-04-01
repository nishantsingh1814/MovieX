package com.eventx.moviex.TvAdapter;

import android.content.Context;
import android.graphics.Color;
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
 * Created by Nishant on 3/28/2017.
 */

public class VerticalTvAdapter  extends RecyclerView.Adapter<VerticalTvAdapter.VerticalTvHolder>{
    ArrayList<TvShow> mShow;
    LayoutInflater inflater;
    Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }
    public VerticalTvAdapter(ArrayList<TvShow> mTvShow, Context context, ListItemClickListener listener) {
        mOnClickListener = listener;
        mShow=mTvShow;
        mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public VerticalTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.vertical_list_item,parent,false);

        return new VerticalTvHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalTvHolder holder, int position) {
        TvShow show=mShow.get(position);

        String overview=show.getOverview();
        String [] arr=overview.split(" ");
        String newOverview="";
        for(int i=0;i<15&&i<arr.length;i++){
            newOverview+=arr[i]+" ";
        }

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+show.getPoster_path()).into(holder.tvShowImage);
        holder.tvShowTitle.setText(show.getName());
        holder.tvShowRate.setText(show.getVote_average()+"");

        holder.tvShowOverview.setText(newOverview);
    }

    @Override
    public int getItemCount() {
        return mShow.size();
    }

    class VerticalTvHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvShowTitle;
        private ImageView tvShowImage;
        private TextView tvShowOverview;
        private TextView tvShowRate;
        private ImageView star;

        public VerticalTvHolder(View itemView) {
            super(itemView);

            tvShowTitle=(TextView)itemView.findViewById(R.id.movie_title_vertical);
            tvShowOverview=(TextView)itemView.findViewById(R.id.brief_overview);
            tvShowImage=(ImageView)itemView.findViewById(R.id.movie_poster_vertical);
            tvShowRate=(TextView)itemView.findViewById(R.id.movie_rating_vertical);
            star=(ImageView)itemView.findViewById(R.id.star_image);
            star.setColorFilter(Color.parseColor("#ff80ff"));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
