package com.eventx.moviex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieModels.Navigation;
import com.google.android.youtube.player.internal.v;

import java.util.ArrayList;

import static android.R.attr.resource;

/**
 * Created by Nishant on 4/23/2017.
 */

public class NavigationAdapter extends ArrayAdapter {

    ArrayList<Navigation> mMenu;



    Context mContext;


    public NavigationAdapter(@NonNull Context context, ArrayList<Navigation> menu) {
        super(context, 0);
        mContext = context;
        mMenu = menu;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater
                    .from(mContext);
            convertView = inflater.inflate( R.layout.navigation_item, parent,false);
        }

        final TextView menuTitle = (TextView) convertView.findViewById(R.id.menu_title);
        final ImageView menuIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        menuIcon.setColorFilter(Color.parseColor("#8c000000"));

        menuIcon.setImageResource(mMenu.get(position).getIcon());
        menuTitle.setText(mMenu.get(position).getMenuItem());


        return convertView;
    }

    @Override
    public int getCount() {
        return mMenu.size();
    }
}
