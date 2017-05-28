package com.eventx.moviex;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;

public class SingleImageActivity extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        Slidr.attach(this);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        image = (ImageView) findViewById(R.id.image);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + getIntent().getStringExtra("image")).into(image);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallpaper,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        if(item.getItemId()==R.id.menu_wallpaper){
            image.buildDrawingCache();
            Bitmap bmap=image.getDrawingCache();
            WallpaperManager m=WallpaperManager.getInstance(this);

            try {
                m.setBitmap(bmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
