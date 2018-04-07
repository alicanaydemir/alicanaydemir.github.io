package com.app.aydemir.mustwatch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.aydemir.mustwatch.adapter.MovieSearchAdapter;
import com.app.aydemir.mustwatch.api.Client;
import com.app.aydemir.mustwatch.api.Service;
import com.app.aydemir.mustwatch.data.WatchDbHelper;
import com.app.aydemir.mustwatch.model.MovieSearch;
import com.app.aydemir.mustwatch.model.MovieSearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieSearchAdapter moviesAdapter;
    private List<MovieSearch> movieSearchList;
    ImageView buttonSearch;
    EditText editTextSearch;
    String query;
    ProgressBar progressBar_search;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonSearch = view.findViewById(R.id.button_search);
        progressBar_search = view.findViewById(R.id.progressBar_search);
        editTextSearch = view.findViewById(R.id.editT_search);
        recyclerView = view.findViewById(R.id.recyclerV_search);
        final LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(lim);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_search.setVisibility(View.VISIBLE);
                query = editTextSearch.getText().toString().trim();
                if (TextUtils.isEmpty(query)) {
                    progressBar_search.setVisibility(View.INVISIBLE);
                } else {
                    loadSearchMovieData(query);
                }
            }
        });
    }

    private void loadSearchMovieData(String s) {
        Service service = Client.getClient().create(Service.class);
        String myThemovieDbApiToken = "eef2f60bfcf2413c0a9711b4749c0a0c";
        Call<MovieSearchResponse> call = service.getMovie(myThemovieDbApiToken, s);
        call.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                movieSearchList = new ArrayList<>();
                int size = response.body().getResults().size();
                if (size <= 10) {
                    movieSearchList = response.body().getResults();
                } else {
                    movieSearchList = response.body().getResults().subList(0, 10);
                }
                moviesAdapter = new MovieSearchAdapter(getActivity(), movieSearchList);
                recyclerView.setAdapter(moviesAdapter);
                moviesAdapter.notifyDataSetChanged();
                progressBar_search.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                progressBar_search.setVisibility(View.INVISIBLE);
            }
        });
    }
}
