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
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.PeopleActivities.PeopleDetailsActivity;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvCastAdapter extends RecyclerView.Adapter<TvCastAdapter.CastHolder> {
    ArrayList<Cast> mCast;
    LayoutInflater inflater;
    Context mContext;

    public TvCastAdapter(ArrayList<Cast> mCast, Context mContext) {
        this.mCast = mCast;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public CastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cast_list_item, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(CastHolder holder, int position) {

        Cast cast = mCast.get(position);

        holder.castName.setText(cast.getName());
        if(cast.getProfile_path()!=null) {
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + cast.getProfile_path()).into(holder.castImage);
        }else{
            holder.castImage.setImageResource(R.drawable.profile);
        }

    }

    @Override
    public int getItemCount() {
        return mCast.size();
    }

    class CastHolder extends RecyclerView.ViewHolder {
        private TextView castName;
        private ImageView castImage;

        public CastHolder(View itemView) {
            super(itemView);

            castName = (TextView) itemView.findViewById(R.id.cast_title);
            castImage = (ImageView) itemView.findViewById(R.id.cast_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent peopleDetailIntent = new Intent(mContext, PeopleDetailsActivity.class);
                    peopleDetailIntent.putExtra("id", mCast.get(getAdapterPosition()).getId());
                    peopleDetailIntent.putExtra("title",mCast.get(getAdapterPosition()).getName());
                    mContext.startActivity(peopleDetailIntent);
                    Activity act=(Activity)mContext;
                    act.overridePendingTransition(R.anim.slide_right,R.anim.no_change);
                }
            });
        }
    }
}
