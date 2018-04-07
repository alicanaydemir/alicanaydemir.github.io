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
import android.widget.Toast;
import com.app.aydemir.mustwatch.adapter.MovieWatchedAdapter;
import com.app.aydemir.mustwatch.data.WatchDbHelper;
import com.app.aydemir.mustwatch.model.Movie;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListWatchedFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieWatchedAdapter moviesAdapter;
    private List<Movie> movieList;
    WatchDbHelper favoriteDbHelper2;
    LinearLayoutManager lim;

    private int page_number = 1;
    private boolean loading;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public ListWatchedFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_watched, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerV_watched);
        favoriteDbHelper2 = new WatchDbHelper(getContext());
        loading = true;
        lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(lim);
        movieList = new ArrayList<>();
        moviesAdapter = new MovieWatchedAdapter(getActivity(), movieList);
        recyclerView.setAdapter(moviesAdapter);
    }

    private void getAll(int page_number, final int item_count) {
        movieList.clear();
        movieList.addAll(favoriteDbHelper2.getWatchedData(page_number, item_count));
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (movieList.size() <= 10) {
            getAll(1, 10);
        } else {
            getAll(1, totalItemCount);
        }
        getMoreData();
    }

    private void getMoreData() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = lim.getChildCount();
                    totalItemCount = lim.getItemCount();
                    pastVisiblesItems = lim.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (movieList.size() <= 10) {
                                page_number = 1;
                            } else {
                                page_number = movieList.size() / 10;
                            }
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            if (movieList.size() == (page_number) * 10) {
                                if (!favoriteDbHelper2.getWatchedData(page_number + 1, 10).isEmpty()) {

                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            movieList.addAll(favoriteDbHelper2.getWatchedData(page_number + 1, 10));
                                            loading = true;
                                            moviesAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    //Toast.makeText(getContext(), "Page " + (page_number + 1) + " loaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getContext(), "There is no more movie ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Toast.makeText(getContext(), "There is no more movie2 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

        });
    }
}
