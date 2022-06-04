package com.apeman.homeassistant.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apeman.homeassistant.CardContent;
import com.apeman.homeassistant.R;
import com.apeman.homeassistant.blynk.BlynkClient;
import com.apeman.homeassistant.blynk.BlynkRelayStatus;
import com.apeman.homeassistant.blynk.BlynkService;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardContent> cardContentArrayList;
    private Context context;
    private final String token = "d-6DGP7qHZ3ILwCWh3PUJNnI8LScfhqs";

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
                relayHolder.powerFirstLabel.setText(cardContent.getPowerFirstLabel());
                relayHolder.powerSecondLabel.setText(cardContent.getPowerSecondLabel());

                relayHolder.powerFirstIndicator.setBackgroundResource(cardContent.getFirstIndicatorColor());
                Log.d("ViewHolder", "Color: " + cardContent.getFirstIndicatorColor());
                relayHolder.powerSecondIndicator.setBackgroundResource(cardContent.getSecondIndicatorColor());
                Log.d("ViewHolder", "Color: " + cardContent.getSecondIndicatorColor());



                relayHolder.relayIcon.setImageResource(cardContent.getIcon());

                relayHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: Display options box
                        LayoutInflater inflater = (LayoutInflater) view.getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View popupView = inflater.inflate(R.layout.relay_popup, null);

                        SwitchMaterial relay1 = popupView.findViewById(R.id.relay1);
                        SwitchMaterial relay2 = popupView.findViewById(R.id.relay2);
                        relay1.setChecked(cardContent.getPowerFirstStatus());
                        relay2.setChecked(cardContent.getPowerSecondStatus());

                        // Creating popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // Showing the popup window
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                        if (popupView.getVisibility() == View.VISIBLE) {
                            Log.e("onClick relayHolder", "popup visible");


                            relay1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean relayPowered) {
                                    cardContent.setPowerFirstStatus(relayPowered);
                                    int mode = relayPowered ? 1 : 0;

                                    relay1.setChecked(relayPowered);

                                    Call<Void> call = BlynkClient.getInstance().updateFirstRelay(token, mode);
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            Log.d("updateRelays", "VirtualPin value for relay 1 successfully updated");
                                            if (mode == 1) {
                                                cardContent.setFirstIndicatorColor(R.color.light_green);
                                            } else {
                                                cardContent.setFirstIndicatorColor(R.color.red);
                                            }
                                            notifyItemChanged(4);
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Log.e("updateRelays", "first relay error");
                                            t.printStackTrace();
                                            Log.e("updateRelays", "cause" + t.getCause());
                                        }
                                    });
                                }
                            });

                            relay2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean relayPowered) {
                                    cardContent.setPowerSecondStatus(relayPowered);
                                    int mode = relayPowered ? 1 : 0;

                                    relay2.setChecked(relayPowered);

                                    Call<Void> call = BlynkClient.getInstance().updateSecondRelay(token, mode);
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            Log.d("updateRelays", "VirtualPin value for relay 2 successfully updated");
                                            if (mode == 1) {
                                                cardContent.setSecondIndicatorColor(R.color.light_green);
                                            } else {
                                                cardContent.setSecondIndicatorColor(R.color.red);
                                            }
                                            notifyItemChanged(4);
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Log.e("updateRelays", "second relay error");
                                        }
                                    });
                                }

                            });
                        }


                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
//                                view.performClick();
                                popupWindow.dismiss();
                                return true;
                            }
                        });
                    }
                });
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
        private final TextView powerFirstLabel;
        private final TextView powerSecondLabel;
        private final View powerFirstIndicator;
        private final View powerSecondIndicator;
        private ImageView relayIcon;

        RelayViewHolder(@NonNull View itemView) {
            super(itemView);
            relayIndicator = itemView.findViewById(R.id.relayIndicator);
            relayIcon = itemView.findViewById(R.id.relayIcon);
            powerFirstLabel = itemView.findViewById(R.id.power_first);
            powerSecondLabel = itemView.findViewById(R.id.power_second);
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


