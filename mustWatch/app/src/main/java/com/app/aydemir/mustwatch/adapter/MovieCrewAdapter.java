package com.app.aydemir.mustwatch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.aydemir.mustwatch.R;
import com.app.aydemir.mustwatch.model.Crew;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieCrewAdapter extends RecyclerView.Adapter<MovieCrewAdapter.MyViewHolder> {

    private List<Crew> movieList;
    private Context context;

    public MovieCrewAdapter(Context context, List<Crew> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieCrewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Crew movieCrew = movieList.get(position);
        holder.textViewName.setText(movieCrew.getName());
        holder.textViewCharacterName.setText(movieCrew.getJob());
        if (movieCrew.getProfilePath().contains("null")) {
            //holder.imageview.setImageResource(R.drawable.empty_img);
        } else {
            Picasso.with(context).load(movieCrew.getProfilePath()).resize(120, 180).into(holder.imageview);
        }

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
        TextView textViewName, textViewCharacterName;
        CircleImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageV_cast);
            textViewCharacterName = itemView.findViewById(R.id.textV_character_name);
            textViewName = itemView.findViewById(R.id.textV_name);

        }
    }
}
