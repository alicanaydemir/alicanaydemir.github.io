package com.app.aydemir.mustwatch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.aydemir.mustwatch.adapter.MoviesAdapter;
import com.app.aydemir.mustwatch.api.Client;
import com.app.aydemir.mustwatch.api.Service;
import com.app.aydemir.mustwatch.model.Movie;
import com.app.aydemir.mustwatch.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movieList;


    public PopularFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerV_main);
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(lim);
        movieList = new ArrayList<>();
        loadPopularMoviesData();
    }

    private void loadPopularMoviesData() {

        Service service = Client.getClient().create(Service.class);
        String myThemovieDbApiToken = "eef2f60bfcf2413c0a9711b4749c0a0c";
        Call<MovieResponse> call = service.getPopularMovies(myThemovieDbApiToken);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieList = response.body().getResults();
                moviesAdapter = new MoviesAdapter(getActivity(), movieList);
                recyclerView.setAdapter(moviesAdapter);
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
            }
        });
    }

}
