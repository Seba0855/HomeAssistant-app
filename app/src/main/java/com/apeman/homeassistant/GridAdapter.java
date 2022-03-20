package com.apeman.homeassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<CardContent> {
    public GridAdapter(@NonNull Context context, ArrayList<CardContent> cardContents) {
        super(context, 0, cardContents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in Grid View
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        CardContent cardContent = getItem(position);
        TextView valueIndicator = listitemView.findViewById(R.id.value_indicator);
        TextView description = listitemView.findViewById(R.id.name);

        valueIndicator.setText(cardContent.getValue());
        description.setText(cardContent.getDescription());

        return listitemView;
    }
}
