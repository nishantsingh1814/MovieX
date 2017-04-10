package com.eventx.moviex.PeopleFragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eventx.moviex.MainActivity;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleActivities.PeopleDetailsActivity;
import com.eventx.moviex.PeopleActivities.PeopleSearchResultsActivity;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.PeopleAdapter.PopularPeopleAdapter;
import com.eventx.moviex.PeopleModels.PopularPeople;
import com.eventx.moviex.PeopleModels.PopularResults;
import com.eventx.moviex.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 4/1/2017.
 */

public class PeopleFragment extends Fragment implements PopularPeopleAdapter.ListItemClickListener {
    PopularPeopleAdapter adapter;
    RecyclerView popularPeopleList;
    ArrayList<PopularPeople> peoples;
    Button viewMore;
    private int page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = 1;
        View v = inflater.inflate(R.layout.people_fragment, container, false);

        popularPeopleList = (RecyclerView) v.findViewById(R.id.popular_people_list);
        peoples = new ArrayList<>();
        peoples.clear();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            popularPeopleList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        else{
            popularPeopleList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        adapter = new PopularPeopleAdapter(peoples, getContext(), this);
        popularPeopleList.setAdapter(adapter);
        viewMore = (Button) v.findViewById(R.id.view_more);
        viewMore.setVisibility(View.INVISIBLE);

        popularPeopleList.setNestedScrollingEnabled(false);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(page++);
            }
        });
        fetchData(page++);

        setHasOptionsMenu(true);
        return v;
    }

    private void fetchData(final int i) {

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<PopularResults> popularPeop = apiInterface.getPopularPeople(i);
        popularPeop.enqueue(new Callback<PopularResults>() {
            @Override
            public void onResponse(Call<PopularResults> call, Response<PopularResults> response) {
                if (response.isSuccessful()) {

                    peoples.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    if (i == 1)
                        viewMore.setVisibility(View.VISIBLE);

                } else {
                    viewMore.setVisibility(View.INVISIBLE);
                    return;
                }

            }

            @Override
            public void onFailure(Call<PopularResults> call, Throwable t) {

            }
        });
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent peopleDetailIntent = new Intent(getContext(), PeopleDetailsActivity.class);
        peopleDetailIntent.putExtra("id", peoples.get(clickedPosition).getId());
        peopleDetailIntent.putExtra("title", peoples.get(clickedPosition).getName());
        startActivity(peopleDetailIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search for Actors...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchResultIntent = new Intent(getContext(), PeopleSearchResultsActivity.class);
                searchResultIntent.putExtra("query", query);
                startActivity(searchResultIntent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
