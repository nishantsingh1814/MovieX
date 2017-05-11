package com.eventx.moviex.MovieFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eventx.moviex.LoginActivity;
import com.eventx.moviex.MainActivity;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieModels.Navigation;
import com.eventx.moviex.NavigationAdapter;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.Wishlist.WishlistAcitvity;
import com.eventx.moviex.favourite.FavouriteActivity;
import com.eventx.moviex.rated.RatingsActivity;

import java.util.ArrayList;


public class NavigationDrawerFragment extends Fragment {


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView menuList;

    SharedPreferences sp;

    NavigationAdapter adapter;
    String sessionId;

    SharedPreferences.Editor editor;

    ArrayList<Navigation> mMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences("MovieX", Context.MODE_PRIVATE);
        editor=sp.edit();

        sessionId = sp.getString("session", null);
        final View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        String[] loginArrray = {"Login/Signup", "Home", "Movies", "Tv", "Celebrities", "Watchlist","Your Favourites","Your Ratings"};
        String[] logoutArray = {"Home", "Movies", "Tv", "Celebrities", "Watchlist","Your Favourites","Your Ratings","Logout"};

        int[] loginIconArray = {R.drawable.ic_loginnav, R.drawable.ic_homenav, R.drawable.ic_clapperboardnav, R.drawable.ic_computer_screennav, R.drawable.ic_leonardo_di_caprio, R.drawable.ic_clocknav,R.drawable.ic_favorite_heart_buttonnav,R.drawable.ic_star};
        int[] logoutIconArray = {R.drawable.ic_homenav, R.drawable.ic_clapperboardnav, R.drawable.ic_computer_screennav, R.drawable.ic_leonardo_di_caprio, R.drawable.ic_clocknav,R.drawable.ic_favorite_heart_buttonnav,R.drawable.ic_star, R.drawable.ic_logoutnav};


        menuList = (ListView) v.findViewById(R.id.navigation_list);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(sessionId==null){
                   if(i==0){
                       startActivity(new Intent(getContext(), LoginActivity.class));
                   }
                   if(i==1){
                       startActivity(new Intent(getContext(), MainActivity.class));
                   }
                   if(i==2){
                       startActivity(new Intent(getContext(), MoviesActivity.class));
                   }
                   if(i==3){
                       startActivity(new Intent(getContext(), TvActivity.class));
                   }
                   if(i==4){
                       startActivity(new Intent(getContext(), PopularPeopleActivity.class));
                   }
                   if(i==5){
                       startActivity(new Intent(getContext(), WishlistAcitvity.class));
                       //startActivity(new Intent(getContext(), WishlistAcitvity.class));
                   }
                   if(i==6){
                       Snackbar.make(view,"Login First",Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               startActivity(new Intent(getContext(),LoginActivity.class));
                           }
                       }).show();

                       //startActivity(new Intent(getContext(), FavouriteActivity.class));
                   }
                   if(i==7){
                       Snackbar.make(view,"Login First",Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               startActivity(new Intent(getContext(),LoginActivity.class));
                           }
                       }).show();
                       //startActivity(new Intent(getContext(), RatingsActivity.class));
                   }
               }
               else{

                   if(i==0){
                       startActivity(new Intent(getContext(), MainActivity.class));
                   }
                   if(i==1){
                       startActivity(new Intent(getContext(), MoviesActivity.class));
                   }
                   if(i==2){
                       startActivity(new Intent(getContext(), TvActivity.class));
                   }
                   if(i==3){
                       startActivity(new Intent(getContext(), PopularPeopleActivity.class));
                   }
                   if(i==4){
                       startActivity(new Intent(getContext(), WishlistAcitvity.class));
                   }
                   if(i==5){
                       startActivity(new Intent(getContext(),FavouriteActivity.class));
                   }
                   if(i==6){
                       startActivity(new Intent(getContext(), RatingsActivity.class));
                   }
                   if(i==7){
                       editor.putString("session",null);
                       editor.putLong("account",-1);
                       editor.apply();
                       startActivity(new Intent(getContext(), MainActivity.class));
                       getActivity().finish();
                   }
               }
            }
        });


        if (sessionId == null) {

            mMenu = new ArrayList<>();
            for (int i = 0; i < loginArrray.length; i++) {
                Navigation navigation = new Navigation(loginIconArray[i], loginArrray[i]);
                mMenu.add(navigation);
            }
            adapter = new NavigationAdapter(getContext(), mMenu);
        } else {
            mMenu = new ArrayList<>();
            for (int i = 0; i < loginArrray.length; i++) {
                Navigation navigation = new Navigation(logoutIconArray[i], logoutArray[i]);
                mMenu.add(navigation);
                Log.i("hellop", "onCreateView: " + sessionId);

            }

            adapter = new NavigationAdapter(getContext(), mMenu);
        }
        menuList.setAdapter(adapter);
        return v;
    }


    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });
    }

}
