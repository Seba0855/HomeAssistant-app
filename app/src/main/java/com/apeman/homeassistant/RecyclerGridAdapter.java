package com.apeman.homeassistant;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return cardContentArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView roomIndicator;
        private TextView valueIndicator;
        private TextView description;
        private View line;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIndicator = itemView.findViewById(R.id.roomIndicator);
            valueIndicator = itemView.findViewById(R.id.value_indicator);
            line = itemView.findViewById(R.id.line);
            description = itemView.findViewById(R.id.name);
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
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.right = horizontalSpaceWidth;
            outRect.left = horizontalSpaceWidth;
        }
    }
}


