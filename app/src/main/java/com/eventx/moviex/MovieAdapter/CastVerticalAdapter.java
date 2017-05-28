package com.eventx.moviex.MovieAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.PeopleActivities.PeopleDetailsActivity;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class CastVerticalAdapter extends RecyclerView.Adapter<CastVerticalAdapter.CastVerticalHolder> {
    ArrayList<Cast> mCast;
    LayoutInflater inflater;
    Context mContext;

    public CastVerticalAdapter(ArrayList<Cast> mCast, Context mContext) {
        this.mCast = mCast;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public CastVerticalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cast_vertical_list_item, parent, false);
        return new CastVerticalHolder(view);
    }

    @Override
    public void onBindViewHolder(CastVerticalHolder holder, int position) {

        holder.castRole.setText(mCast.get(position).getCharacter());
        holder.castName.setText(mCast.get(position).getName());

        if(mCast.get(position).getProfile_path()!=null) {
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + mCast.get(position).getProfile_path()).into(holder.castImage);
        }else{
            holder.castImage.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        return mCast.size();
    }

    class CastVerticalHolder extends RecyclerView.ViewHolder{

        private TextView castName;
        private ImageView castImage;
        private TextView castRole;

        public CastVerticalHolder(View itemView) {
            super(itemView);
            castImage=(ImageView)itemView.findViewById(R.id.cast_image);
            castName=(TextView)itemView.findViewById(R.id.cast_name);
            castRole=(TextView)itemView.findViewById(R.id.cast_role);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent peopleDetailIntent = new Intent(mContext, PeopleDetailsActivity.class);
                    peopleDetailIntent.putExtra("id", mCast.get(getAdapterPosition()).getId());
                    peopleDetailIntent.putExtra("title",mCast.get(getAdapterPosition()).getName());
                    mContext.startActivity(peopleDetailIntent);
                    Activity act=(Activity)mContext;
                    act.overridePendingTransition(R.anim.slide_in,R.anim.no_change);
                }
            });
        }
    }
}
