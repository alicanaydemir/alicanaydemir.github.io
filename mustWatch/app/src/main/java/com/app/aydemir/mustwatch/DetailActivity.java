package com.app.aydemir.mustwatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.aydemir.mustwatch.adapter.MovieCastAdapter;
import com.app.aydemir.mustwatch.adapter.MovieCrewAdapter;
import com.app.aydemir.mustwatch.api.Client;
import com.app.aydemir.mustwatch.api.Service;
import com.app.aydemir.mustwatch.data.FavoriteContract;
import com.app.aydemir.mustwatch.data.WatchClickDbHelper;
import com.app.aydemir.mustwatch.data.WatchDbHelper;
import com.app.aydemir.mustwatch.model.Cast;
import com.app.aydemir.mustwatch.model.Crew;
import com.app.aydemir.mustwatch.model.Movie;
import com.app.aydemir.mustwatch.model.MovieMembers;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    ImageView imageViewMovieImage_detail;
    TextView textV_title, textV_overview, textV_releaseDate, textV_vote_average;
    ImageView imageView_towatch, imageView_watched;
    WatchClickDbHelper favoriteDbHelper1;
    SQLiteDatabase Db;
    WatchDbHelper favoriteDbHelper2;
    Movie moviePicked;
    private List<Cast> movieCastList;
    private List<Crew> movieCrewList;
    RecyclerView recyclerView_cast;
    RecyclerView recyclerView_crew;
    private MovieCastAdapter movieCastAdapter;
    private MovieCrewAdapter movieCrewAdapter;
    FrameLayout frameLayout;
    Intent intent;
    Toolbar main_toolbar;
    LinearLayoutManager lim, lim2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView_cast.setAdapter(null);
        recyclerView_crew.setAdapter(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        frameLayout = findViewById(R.id.frm);

        recyclerView_cast = findViewById(R.id.recyvlerview_cast);
        recyclerView_crew = findViewById(R.id.recyclerview_crew_detail);
        lim = new LinearLayoutManager(getApplicationContext());
        lim.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_cast.setLayoutManager(lim);
        lim2 = new LinearLayoutManager(getApplicationContext());
        lim2.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_crew.setLayoutManager(lim2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main_toolbar.setTitleTextColor(Color.parseColor("#fafafa"));
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        favoriteDbHelper1 = new WatchClickDbHelper(getApplicationContext());

        imageView_towatch = findViewById(R.id.imageV_towatch_detail);
        imageView_watched = findViewById(R.id.imageV_watched_detail);
        imageViewMovieImage_detail = findViewById(R.id.imageV_detail);
        imageViewMovieImage_detail.getLayoutParams().height = height;
        textV_overview = findViewById(R.id.textV_overview_detail);
        textV_title = findViewById(R.id.textV_title_detail);
        textV_releaseDate = findViewById(R.id.textV_releaseDate);
        textV_vote_average = findViewById(R.id.textV_vote_average);

        intent = getIntent();
        if (intent.hasExtra("original_title")) {
            final int movie_id = intent.getExtras().getInt("id");
            final String image = intent.getExtras().getString("image");
            final String title = intent.getExtras().getString("original_title");
            final String overview = intent.getExtras().getString("overview");
            final String releaseDate = intent.getExtras().getString("release_date");
            final double vote_average = intent.getExtras().getDouble("vote");

            Picasso.with(this).load(image).resize(450, 625).into(imageViewMovieImage_detail);
            textV_title.setText(title);
            getSupportActionBar().setTitle(title);
            textV_overview.setText(overview);
            textV_releaseDate.setText("" + releaseDate);
            textV_vote_average.setText("" + vote_average);
            if (ExistsToWatch(movie_id)) {
                imageView_towatch.setColorFilter(Color.parseColor("#ad1457"));
            }
            if (ExistsWatched(movie_id)) {
                imageView_watched.setColorFilter(Color.parseColor("#ad1457"));
                imageView_towatch.setEnabled(false);
            }
            imageView_towatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ExistsToWatch(movie_id)) {
                        delete_towatch(movie_id);
                        delete_towatch_data(movie_id);
                        imageView_towatch.setColorFilter(Color.parseColor("#bdbdbd"));
                    } else {
                        save_towatch(movie_id);
                        moviePicked = new Movie(movie_id, title, image, overview, releaseDate, vote_average);
                        save_towatch_data(moviePicked);
                        Snackbar snackbar = Snackbar
                                .make(frameLayout, "To Watch", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#ad1457"));
                        snackbar.show();
                        imageView_towatch.setColorFilter(Color.parseColor("#ad1457"));
                    }
                }
            });
            imageView_watched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ExistsWatched(movie_id)) {
                        delete_watched(movie_id);
                        delete_watched_data(movie_id);
                        imageView_towatch.setEnabled(true);
                        imageView_watched.setColorFilter(Color.parseColor("#bdbdbd"));
                    } else {
                        delete_towatch(movie_id);
                        delete_towatch_data(movie_id);
                        Snackbar snackbar = Snackbar
                                .make(frameLayout, "Watched", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#ad1457"));
                        snackbar.show();

                        save_watched(movie_id);
                        moviePicked = new Movie(movie_id, title, image, overview, releaseDate, vote_average);
                        save_watched_data(moviePicked);

                        imageView_towatch.setColorFilter(Color.parseColor("#bdbdbd"));
                        imageView_towatch.setEnabled(false);
                        imageView_watched.setColorFilter(Color.parseColor("#ad1457"));
                    }
                }
            });
            loadCastMovieData(movie_id);
        }
    }

    public void save_towatch_data(Movie movie) {
        favoriteDbHelper2 = new WatchDbHelper(getApplicationContext());
        favoriteDbHelper2.add_towatch_data(movie);
    }

    public void delete_towatch_data(int movie_id) {
        favoriteDbHelper2 = new WatchDbHelper(getApplicationContext());
        favoriteDbHelper2.delete_towatch_data(movie_id);
    }

    public void save_watched_data(Movie movie) {
        favoriteDbHelper2 = new WatchDbHelper(getApplicationContext());
        favoriteDbHelper2.add_watched_data(movie);
    }

    public void delete_watched_data(int movie_id) {
        favoriteDbHelper2 = new WatchDbHelper(getApplicationContext());
        favoriteDbHelper2.delete_watched_data(movie_id);
    }


    //----------------------------------------------------------------------------
    public void save_towatch(int movie_id) {
        favoriteDbHelper1 = new WatchClickDbHelper(getApplicationContext());
        favoriteDbHelper1.add_towatch(movie_id);
    }

    public void delete_towatch(int movie_id) {
        favoriteDbHelper1 = new WatchClickDbHelper(getApplicationContext());
        favoriteDbHelper1.delete_towatch(movie_id);
    }

    public void save_watched(int movie_id) {
        favoriteDbHelper1 = new WatchClickDbHelper(getApplicationContext());
        favoriteDbHelper1.add_watched(movie_id);
    }

    public void delete_watched(int movie_id) {
         favoriteDbHelper1 = new WatchClickDbHelper(getApplicationContext());
        favoriteDbHelper1.delete_watched(movie_id);
    }

    public boolean ExistsToWatch(int searchItem) {
        String[] columns = {FavoriteContract.ToWatchEntry.COLUMN_MOVIEID};
        String selection = FavoriteContract.ToWatchEntry.COLUMN_MOVIEID + " =?";
        String[] selectionArgs = {String.valueOf(searchItem)};
        String limit = "1";
        Db = favoriteDbHelper1.getWritableDatabase();
        Cursor cursor = Db.query(FavoriteContract.ToWatchEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Db.close();
        return exists;
    }

    public boolean ExistsWatched(int searchItem) {
        String[] columns = {FavoriteContract.WatchedEntry.COLUMN_MOVIEID};
        String selection = FavoriteContract.WatchedEntry.COLUMN_MOVIEID + " =?";
        String[] selectionArgs = {String.valueOf(searchItem)};
        String limit = "1";
        Db = favoriteDbHelper1.getWritableDatabase();
        Cursor cursor = Db.query(FavoriteContract.WatchedEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Db.close();
        return exists;
    }

    private void loadCastMovieData(int id) {
        movieCastList = new ArrayList<>();
        movieCrewList = new ArrayList<>();

        Service service = Client.getClient().create(Service.class);
        String myThemovieDbApiToken = "eef2f60bfcf2413c0a9711b4749c0a0c";
        Call<MovieMembers> call = service.getMembersMovies(id, myThemovieDbApiToken);
        call.enqueue(new Callback<MovieMembers>() {
            @Override
            public void onResponse(Call<MovieMembers> call, Response<MovieMembers> response) {
                int i = response.body().getCast().size();
                if (i > 9) {
                    movieCastList = response.body().getCast().subList(0, 10);
                } else {
                    movieCastList = response.body().getCast();
                }
                int i2 = response.body().getCrew().size();
                if (i2 > 0) {
                    movieCrewList = response.body().getCrew().subList(0, 1);
                } else {
                    movieCrewList = response.body().getCrew();
                }
                movieCastAdapter = new MovieCastAdapter(getApplicationContext(), movieCastList);
                movieCrewAdapter = new MovieCrewAdapter(getApplicationContext(), movieCrewList);
                recyclerView_cast.setAdapter(movieCastAdapter);
                recyclerView_crew.setAdapter(movieCrewAdapter);
                movieCrewAdapter.notifyDataSetChanged();
                movieCastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieMembers> call, Throwable t) {
            }
        });

    }
}
