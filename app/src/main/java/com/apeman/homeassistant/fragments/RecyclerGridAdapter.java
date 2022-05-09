package com.apeman.homeassistant.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apeman.homeassistant.CardContent;
import com.apeman.homeassistant.R;

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
        holder.roomIndicator.setText(cardContent.getRoomIndicator());
        holder.valueIndicator.setText(cardContent.getValue());
        holder.line.setBackgroundResource(cardContent.getLineColor());
        holder.description.setText(cardContent.getDescription());

        holder.valueIndicator.setTextSize(cardContent.getValueTextSize());
        //holder.icon.setImageResource(R.drawable.ic_humidity);
    }

    @Override
    public int getItemCount() {
        return cardContentArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView roomIndicator;
        private final TextView valueIndicator;
        private final TextView description;
        private final View line;
       // private ImageView icon;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIndicator = itemView.findViewById(R.id.roomIndicator);
            valueIndicator = itemView.findViewById(R.id.value_indicator);
            line = itemView.findViewById(R.id.line);
            description = itemView.findViewById(R.id.name);
            //icon = itemView.findViewById(R.id.icon);
        }
    }


    /**
     * Supporting class for setting the spacing between cards
     */
    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;
        private final int horizontalSpaceWidth;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight, int horizontalSpaceWidth) {
            this.verticalSpaceHeight = verticalSpaceHeight;
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            outRect.right = horizontalSpaceWidth;
            outRect.left = horizontalSpaceWidth;
        }
    }
}


