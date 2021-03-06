package com.example.nextbrain.assessment.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nextbrain.assessment.R;
import com.example.nextbrain.assessment.activities.PlayerActivity;
import com.example.nextbrain.assessment.model.Req;

import java.io.Serializable;
import java.util.List;

public class MoviesSuggestionAdapter extends RecyclerView.Adapter<MoviesSuggestionAdapter.EmployeeViewHolder> {

    private List<Req> dataList;
    private Context context;
    LocationAdapterInterface locationAdapterInterface;


    public MoviesSuggestionAdapter(List<Req> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;

    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_sugg_row, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.description.setText(dataList.get(position).getDescription());
        Glide.with(context).load(dataList.get(position).getThumb()).into(holder.thumb);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description;
        ImageView thumb;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            thumb = itemView.findViewById(R.id.thumb);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (locationAdapterInterface != null) {
                locationAdapterInterface.selectedCountryValue(getAdapterPosition());
            }
        }
    }

    public void CallLocationAdapterInterface(LocationAdapterInterface locationAdapterInterface) {
        this.locationAdapterInterface = locationAdapterInterface;
    }

    public interface LocationAdapterInterface {
        void selectedCountryValue(int position);
    }

}

