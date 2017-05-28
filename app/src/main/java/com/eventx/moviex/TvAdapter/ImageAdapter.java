package com.eventx.moviex.TvAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.transition.Fade;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.R;
import com.eventx.moviex.SingleImageActivity;
import com.eventx.moviex.TvModels.Poster;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/29/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<Poster> mPosters;

    public ImageAdapter(ArrayList<Poster> posters, Context context){
        this.mPosters=posters;
        this.mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+mPosters.get(position).getFile_path()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return mPosters.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{

        private ImageView poster;
        public ImageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent imageViewIntent=new Intent(mContext, SingleImageActivity.class);
                    imageViewIntent.putExtra("image",mPosters.get(getAdapterPosition()).getFile_path());

                    if (Build.VERSION.SDK_INT >= 21) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, view, "trans");
                        mContext.startActivity(imageViewIntent, options.toBundle());
                    } else {
                        mContext.startActivity(imageViewIntent);
                    }
                }
            });
            poster=(ImageView)itemView.findViewById(R.id.image_tv_show);
        }
    }


}
