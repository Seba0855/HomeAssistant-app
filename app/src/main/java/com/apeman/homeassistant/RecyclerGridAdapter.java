package com.apeman.homeassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.RecyclerViewHolder> {
    private ArrayList<CardContent> cardContentArrayList;
    private Context context;

    public RecyclerGridAdapter(ArrayList<CardContent> recyclerCardContent, Context context) {
        this.cardContentArrayList = recyclerCardContent;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout inflater
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Setting the data to value indicator and description
        CardContent cardContent = cardContentArrayList.get(position);
        holder.valueIndicator.setText(cardContent.getValue());
        holder.line.setBackgroundResource(cardContent.getLineColor());
        holder.description.setText(cardContent.getDescription());
    }

    @Override
    public int getItemCount() {
        return cardContentArrayList.size();
    }

    //    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View listitemView = convertView;
//        if (listitemView == null) {
//            // Layout Inflater inflates each item to be displayed in Grid View
//            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
//        }
//
//        CardContent cardContent = getItem(position);
//        TextView valueIndicator = listitemView.findViewById(R.id.value_indicator);
//        TextView description = listitemView.findViewById(R.id.name);
//
//        valueIndicator.setText(cardContent.getValue());
//        description.setText(cardContent.getDescription());
//
//        return listitemView;
//    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView valueIndicator;
        private TextView description;
        private View line;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            valueIndicator = itemView.findViewById(R.id.value_indicator);
            line = itemView.findViewById(R.id.line);
            description = itemView.findViewById(R.id.name);
        }
    }
}


