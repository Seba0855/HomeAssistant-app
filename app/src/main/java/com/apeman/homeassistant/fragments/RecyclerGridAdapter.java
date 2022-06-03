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

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardContent> cardContentArrayList;
    private Context context;

    public RecyclerGridAdapter(ArrayList<CardContent> recyclerCardContent, Context context) {
        this.cardContentArrayList = recyclerCardContent;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout inflater
        View view;

        // if ViewType is RELAY
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relay_card, parent, false);
            return new RelayViewHolder(view);
        }

        // else make view a SENSOR (default)
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        CardContent.CardType cardType = cardContentArrayList.get(position).getCardType();

        // if CardType is RELAY return 1
        if (cardType == CardContent.CardType.RELAY) {
            return 1;
        }

        // default CardType is SENSOR, so return 0 instead
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder multipleHolder, int position) {
        // Setting the data to value indicator and description
        CardContent cardContent = cardContentArrayList.get(position);

        switch (multipleHolder.getItemViewType()) {
            case 0:
                RecyclerViewHolder holder = (RecyclerViewHolder) multipleHolder;
                holder.roomIndicator.setText(cardContent.getRoomIndicator());
                holder.valueIndicator.setText(cardContent.getValue());
                holder.description.setText(cardContent.getDescription());

                holder.icon.setImageResource(cardContent.getIcon());
                holder.line.setBackgroundResource(cardContent.getLineColor());

                holder.valueIndicator.setTextSize(cardContent.getValueTextSize());
                break;

            case 1:
                RelayViewHolder relayHolder = (RelayViewHolder) multipleHolder;
                relayHolder.relayIndicator.setText(cardContent.getRelayIndicator());
                relayHolder.powerFirst.setText(cardContent.getPowerFirst());
                relayHolder.powerSecond.setText(cardContent.getPowerSecond());

                relayHolder.powerFirstIndicator.setBackgroundResource(cardContent.getFirstIndicatorColor());
                relayHolder.powerSecondIndicator.setBackgroundResource(cardContent.getSecondIndicatorColor());

                relayHolder.relayIcon.setImageResource(cardContent.getIcon());
                break;
        }
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
        private ImageView icon;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIndicator = itemView.findViewById(R.id.roomIndicator);
            valueIndicator = itemView.findViewById(R.id.value_indicator);
            line = itemView.findViewById(R.id.line);
            description = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    public static class RelayViewHolder extends RecyclerView.ViewHolder {
        private final TextView relayIndicator;
        private final TextView powerFirst;
        private final TextView powerSecond;
        private final View powerFirstIndicator;
        private final View powerSecondIndicator;
        private ImageView relayIcon;

        RelayViewHolder(@NonNull View itemView) {
            super(itemView);
            relayIndicator = itemView.findViewById(R.id.relayIndicator);
            relayIcon = itemView.findViewById(R.id.relayIcon);
            powerFirst = itemView.findViewById(R.id.power_first);
            powerSecond = itemView.findViewById(R.id.power_second);
            powerFirstIndicator = itemView.findViewById(R.id.power_first_indicator);
            powerSecondIndicator = itemView.findViewById(R.id.power_second_indicator);
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


