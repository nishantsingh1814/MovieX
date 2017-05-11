package com.eventx.moviex.MovieAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eventx.moviex.MovieModels.Review;
import com.eventx.moviex.R;

import java.util.ArrayList;

/**
 * Created by Nishant on 4/23/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    Context mContext;
    ArrayList<Review> mReviews;
    LayoutInflater inflater;
    public ReviewAdapter(Context context, ArrayList<Review> reviews){
        mContext=context;
        mReviews=reviews;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ReviewHolder(inflater.inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        holder.author.setText(mReviews.get(position).getAuthor());
        holder.content.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
    public class ReviewHolder extends RecyclerView.ViewHolder{

        public ReviewHolder(View itemView) {
            super(itemView);
            author=(TextView)itemView.findViewById(R.id.author_tv);
            content=(TextView)itemView.findViewById(R.id.content_tv);
        }
        TextView author;
        TextView content;
    }
}

