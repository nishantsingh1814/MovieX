package com.eventx.moviex.PeopleAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.PeopleModels.MovieCredits;
import com.eventx.moviex.PeopleModels.TvCredits;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleTvAdapter extends RecyclerView.Adapter <PeopleTvAdapter.PeopleTvHolder>{
    private ArrayList<TvCredits> mPeopleMovie;
    private Context mContext;
    private LayoutInflater inflater;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public PeopleTvAdapter(ArrayList<TvCredits> popularPeoples, Context context, ListItemClickListener listener){
        mOnClickListener = listener;
        mPeopleMovie =popularPeoples;
        mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public PeopleTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.people_movie_item,parent,false);

        return new PeopleTvHolder(v);
    }

    @Override
    public void onBindViewHolder(PeopleTvHolder holder, int position) {
        holder.peopleCharacter.setText(mPeopleMovie.get(position).getCharacter());
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+ mPeopleMovie.get(position).getPoster_path()).into(holder.moviePoster);
        holder.movieName.setText(mPeopleMovie.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mPeopleMovie.size();
    }

    public class PeopleTvHolder extends RecyclerView.ViewHolder{

        ImageView moviePoster;
        TextView movieName;
        TextView peopleCharacter;
        public PeopleTvHolder(View itemView) {
            super(itemView);
            movieName=(TextView)itemView.findViewById(R.id.people_movie_title);
            moviePoster=(ImageView)itemView.findViewById(R.id.people_movie_poster);
            peopleCharacter=(TextView)itemView.findViewById(R.id.people_movie_character);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition=getAdapterPosition();
                    mOnClickListener.onListItemClick(clickedPosition);
                }
            });
        }
    }
}
