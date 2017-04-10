package com.eventx.moviex.TvFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventx.moviex.Database.MovieDbHelper;
import com.eventx.moviex.MovieModels.Cast;
import com.eventx.moviex.MovieModels.CastResults;
import com.eventx.moviex.MovieModels.ResultTrailer;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.R;
import com.eventx.moviex.TvActivities.TvShowDetailsActivity;
import com.eventx.moviex.TvActivities.TvShowEpisodeActivity;
import com.eventx.moviex.TvAdapter.HorizontalTvAdapter;
import com.eventx.moviex.TvAdapter.TvCastAdapter;
import com.eventx.moviex.TvModels.TvResults;
import com.eventx.moviex.TvModels.TvShow;
import com.eventx.moviex.TvModels.TvShowDetails;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 3/29/2017.
 */

public class TvShowInfoFragment extends Fragment implements HorizontalTvAdapter.ListItemClickListener {
    TextView tvGenre;
    TextView overviewTv;
    TextView mTvRatings;
    TextView mReleaseTv;
    TextView title;

    RecyclerView tv_show_cast;
    TvCastAdapter adapter;
    ArrayList<Cast> mCast;
    ImageView poster;
    TvShowDetails mShow;
    HorizontalTvAdapter similarAdapter;
    RecyclerView mMoreLikeThis;
    ArrayList<TvShow> similar;
    Button wishlistBtn;

    long tvId;

    MovieDbHelper helper;
    SQLiteDatabase readDb;

    TextView viewAllEpisodes;

    private ImageView mBackdropImage;
    private ImageButton mPlayButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tv_show_info_fragment, container, false);
        tvGenre = (TextView) v.findViewById(R.id.tv_genre);
        title = (TextView) v.findViewById(R.id.show_detail_title);
        overviewTv = (TextView) v.findViewById(R.id.tv_show_overview);
        mTvRatings = (TextView) v.findViewById(R.id.show_ratings);
        mReleaseTv = (TextView) v.findViewById(R.id.year_started);
        viewAllEpisodes = (TextView) v.findViewById(R.id.view_All_episode);
        wishlistBtn = (Button) v.findViewById(R.id.wishlist_btn);

        tvId=getActivity().getIntent().getLongExtra("id", -1);

        setWishlistBtn();

        viewAllEpisodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allEpisodesIntent = new Intent(getContext(), TvShowEpisodeActivity.class);
                allEpisodesIntent.putExtra("id", getActivity().getIntent().getLongExtra("id", -1));
                allEpisodesIntent.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                startActivity(allEpisodesIntent);
            }
        });

        mCast = new ArrayList<>();
        poster = (ImageView) v.findViewById(R.id.show_image);
        adapter = new TvCastAdapter(mCast, getContext());
        tv_show_cast = (RecyclerView) v.findViewById(R.id.tv_cast_recycle_list);
        tv_show_cast.setAdapter(adapter);
        mShow = new TvShowDetails();

        mMoreLikeThis = (RecyclerView) v.findViewById(R.id.shows_like_this);
        similar = new ArrayList<>();
        similarAdapter = new HorizontalTvAdapter(similar, getContext(), this);

        mMoreLikeThis.setAdapter(similarAdapter);


        mBackdropImage = (ImageView) v.findViewById(R.id.backdrop_image);
        mPlayButton = (ImageButton) v.findViewById(R.id.play_trailer);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiInterface apiInterface = ApiClient.getApiInterface();
                Call<ResultTrailer> keyResult = apiInterface.getTvTrailerKey(getActivity().getIntent().getLongExtra("id", -1));
                keyResult.enqueue(new Callback<ResultTrailer>() {
                    @Override
                    public void onResponse(Call<ResultTrailer> call, Response<ResultTrailer> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResults().size() == 0) {
                                Snackbar.make(view, "No trailer Available", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            String key = response.body().getResults().get(0).getKey();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultTrailer> call, Throwable t) {

                    }
                });
            }
        });

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helper=new MovieDbHelper(getContext());
                readDb=helper.getReadableDatabase();
                Cursor c=readDb.query(MovieDbHelper.TV_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null,null,null,null);
                if(c.getCount()==0){
                    Snackbar.make(view,"Added to your wishlist",Snackbar.LENGTH_SHORT).show();
                    wishlistBtn.setText("Added to wishlist");
                    wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_black_24dp,0,0);
                    SQLiteDatabase db=helper.getWritableDatabase();
                    helper.addToSqlite(db,MovieDbHelper.TV_WISHLIST_TABLE,tvId,getActivity().getIntent().getStringExtra("title"),getActivity().getIntent().getStringExtra("poster"));
                }
                else{
                    Snackbar.make(view,"Removed from your wishlist",Snackbar.LENGTH_SHORT).show();
                    wishlistBtn.setText("Wishlist");
                    wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_border_black_24dp,0,0);
                    SQLiteDatabase db=helper.getWritableDatabase();
                    db.delete(MovieDbHelper.TV_WISHLIST_TABLE,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null);

                }
                c.close();
            }
        });

        fetchData();

        return v;
    }

    private void setWishlistBtn() {
        helper=new MovieDbHelper(getContext());
        readDb=helper.getReadableDatabase();

        Cursor c=readDb.query(MovieDbHelper.TV_WISHLIST_TABLE,null,MovieDbHelper.COLUMN_TV_ID+" = "+tvId,null,null,null,null);
        if(c.getCount()==0){
            wishlistBtn.setText("Wishlist");
            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_border_black_24dp,0,0);
        }else{
            wishlistBtn.setText("Added to wishlist");
            wishlistBtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bookmark_black_24dp,0,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setWishlistBtn();
    }

    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        if (getActivity() != null) {
            Call<TvShowDetails> tvShowDetails = apiInterface.getSingleTvShowDetails(getActivity().getIntent().getLongExtra("id", -1));

            tvShowDetails.enqueue(new Callback<TvShowDetails>() {
                @Override
                public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                    if (response.isSuccessful()) {
                        title.setText(response.body().getName());
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getBackdrop_path()).into(mBackdropImage);
                        mShow = response.body();
                        for (int i = 0; i < mShow.getGenres().size(); i++) {
                            if (i < mShow.getGenres().size() - 1) {
                                tvGenre.append(mShow.getGenres().get(i).getName() + "|");
                            } else {
                                tvGenre.append(mShow.getGenres().get(i).getName());
                            }
                        }
                        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path()).into(poster);
                        overviewTv.setText(mShow.getOverview());
                        mTvRatings.setText(mShow.getVote_average() + " (" + mShow.getVote_count() + ")");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d,yyyy");

                        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        if (mShow.getFirst_air_date() != null) {
                            try {
                                date = sdfd.parse(mShow.getFirst_air_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mReleaseTv.setText(sdf.format(date));
                        }

                    }else{
                        fetchData();
                    }
                }

                @Override
                public void onFailure(Call<TvShowDetails> call, Throwable t) {

                }
            });
        }
        if (getActivity() != null) {

            Call<CastResults> castDetails = apiInterface.getTvCast(getActivity().getIntent().getLongExtra("id", -1));
            castDetails.enqueue(new Callback<CastResults>() {
                @Override
                public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                    if (response.isSuccessful()) {

                        ArrayList<Cast> castJson = response.body().getCast();
                        mCast.clear();
                        mCast.addAll(castJson);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<CastResults> call, Throwable t) {

                }
            });


        }
        if (getActivity() != null) {
            Call<TvResults> similarTv = apiInterface.getTvSimilarResults(getActivity().getIntent().getLongExtra("id", -1));
            similarTv.enqueue(new Callback<TvResults>() {
                @Override
                public void onResponse(Call<TvResults> call, Response<TvResults> response) {
                    if (response.isSuccessful()) {
                        ArrayList<TvShow> similarJson = response.body().getResults();
                        similar.clear();
                        similar.addAll(similarJson);
                        similarAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("hello", "onResponse: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<TvResults> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent tvShowDetailsIntent = new Intent(getContext(), TvShowDetailsActivity.class);
        tvShowDetailsIntent.putExtra("id", similar.get(clickedPosition).getTvId());
        tvShowDetailsIntent.putExtra("title", similar.get(clickedPosition).getName());
        tvShowDetailsIntent.putExtra("poster",similar.get(clickedPosition).getPoster_path());
        startActivity(tvShowDetailsIntent);
    }
}
