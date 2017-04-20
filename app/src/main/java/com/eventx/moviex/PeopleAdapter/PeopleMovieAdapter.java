package com.eventx.moviex.PeopleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieActivities.MoviesDetailsActivity;
import com.eventx.moviex.PeopleModels.MovieCredits;
import com.eventx.moviex.PeopleModels.PeopleMovieCast;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PeopleMovieAdapter extends RecyclerView.Adapter<PeopleMovieAdapter.PeopleMovieHolder>{
    private ArrayList<MovieCredits> mPeopleMovie;
    private Context mContext;
    private LayoutInflater inflater;


    public PeopleMovieAdapter(ArrayList<MovieCredits> popularPeoples, Context context){
        mPeopleMovie =popularPeoples;
        mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public PeopleMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.people_movie_item,parent,false);

        return new PeopleMovieHolder(v);
    }

    @Override
    public void onBindViewHolder(PeopleMovieHolder holder, int position) {
        holder.peopleCharacter.setText(mPeopleMovie.get(position).getCharacter());
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+ mPeopleMovie.get(position).getPoster_path()).into(holder.moviePoster);
        holder.movieName.setText(mPeopleMovie.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mPeopleMovie.size();
    }

    public class PeopleMovieHolder extends RecyclerView.ViewHolder{

        ImageView moviePoster;
        TextView movieName;
        TextView peopleCharacter;
        public PeopleMovieHolder(View itemView) {
            super(itemView);
            movieName=(TextView)itemView.findViewById(R.id.people_movie_title);
            moviePoster=(ImageView)itemView.findViewById(R.id.people_movie_poster);
            peopleCharacter=(TextView)itemView.findViewById(R.id.people_movie_character);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent movieDetailsIntent = new Intent(mContext, MoviesDetailsActivity.class);
                    movieDetailsIntent.putExtra("id", mPeopleMovie.get(getAdapterPosition()).getId());
                    movieDetailsIntent.putExtra("title", mPeopleMovie.get(getAdapterPosition()).getTitle());
                    movieDetailsIntent.putExtra("poster",mPeopleMovie.get(getAdapterPosition()).getPoster_path());
                    mContext.startActivity(movieDetailsIntent);
                    Activity act=(Activity)mContext;
                    act.overridePendingTransition(R.anim.slide_in,R.anim.no_change);

                }
            });
        }
    }
}
