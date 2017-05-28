package com.eventx.moviex.PeopleAdapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nishant on 3/30/2017.
 */

public class PopularPeopleAdapter extends RecyclerView.Adapter<PopularPeopleAdapter.PopularPeopleHolder> {

    private ArrayList<PopularPeople> mPopularPeople;
    private Context mContext;
    private LayoutInflater inflater;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public PopularPeopleAdapter(ArrayList<PopularPeople> popularPeoples,Context context,ListItemClickListener listener){
        mOnClickListener = listener;
        mPopularPeople=popularPeoples;
        mContext=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public PopularPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.popular_people_list_item,parent,false);

        return new PopularPeopleHolder(v);
    }

    @Override
    public void onBindViewHolder(PopularPeopleHolder holder, int position) {
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+mPopularPeople.get(position).getProfile_path()).into(holder.peopleProfile);
        holder.peopleName.setText(mPopularPeople.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mPopularPeople.size();
    }

    public class PopularPeopleHolder extends RecyclerView.ViewHolder{

        ImageView peopleProfile;
        TextView peopleName;
        public PopularPeopleHolder(View itemView) {
            super(itemView);
            peopleName=(TextView)itemView.findViewById(R.id.people_name);
            peopleProfile=(ImageView)itemView.findViewById(R.id.people_photo);

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
