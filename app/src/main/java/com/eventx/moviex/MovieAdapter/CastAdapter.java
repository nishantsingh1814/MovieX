package com.eventx.moviex.MovieAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.PeopleActivities.PeopleDetailsActivity;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/27/2017.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {
    ArrayList<Cast> mCast;
    LayoutInflater inflater;
    Context mContext;

    public CastAdapter(ArrayList<Cast> mCast, Context mContext) {
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
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500" + cast.getProfile_path()).into(holder.castImage);
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
                }
            });
        }
    }
}
