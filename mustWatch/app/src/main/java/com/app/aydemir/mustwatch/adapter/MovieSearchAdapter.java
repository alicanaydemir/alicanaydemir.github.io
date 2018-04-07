package com.app.aydemir.mustwatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.aydemir.mustwatch.DetailActivity;
import com.app.aydemir.mustwatch.R;
import com.app.aydemir.mustwatch.model.MovieSearch;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ACER on 21.3.2018.
 */

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MyViewHolder> {

    private List<MovieSearch> movieList;
    private MovieSearch movie;
    private Context context;

    public MovieSearchAdapter(Context context, List<MovieSearch> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main_movies, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        movie = movieList.get(position);
        holder.textViewTitle.setText(movie.getTitle());
        if (!TextUtils.isEmpty(movie.getReleaseDate())) {
            holder.textViewDate.setText(movie.getReleaseDate().substring(0,4));
        }
        holder.textViewPlot.setText(movie.getOverview());
        if (movie.getPosterPath().contains("null")) {
            //holder.imageV_main.setImageResource(R.drawable.empty_img);
        }
        else{Picasso.with(context).load(movie.getPosterPath()).resize(200,300).into(holder.imageV_main);}
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageV_main;
        TextView textViewTitle, textViewDate, textViewPlot;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageV_main = itemView.findViewById(R.id.imageV_main);
            textViewTitle = itemView.findViewById(R.id.textVTitle_main);
            textViewDate = itemView.findViewById(R.id.textVDate_main);
            textViewPlot = itemView.findViewById(R.id.textVPlot_main);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MovieSearch movieClicked = movieList.get(position);
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("original_title", movieClicked.getTitle());
                        intent.putExtra("id", movieClicked.getId());
                        intent.putExtra("image", movieClicked.getPosterPath());
                        intent.putExtra("overview", movieClicked.getOverview());
                        intent.putExtra("release_date", movieClicked.getReleaseDate());
                        intent.putExtra("vote", movieClicked.getVoteAverage());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
