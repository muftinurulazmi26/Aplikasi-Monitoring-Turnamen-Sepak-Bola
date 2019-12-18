package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHAddGoals extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView nmPlayer,menitGoals;

    public VHAddGoals(@NonNull View itemView) {
        super(itemView);

        icon = itemView.findViewById(R.id.icongoals);
        nmPlayer = itemView.findViewById(R.id.tv_GlsnmPlayer);
        menitGoals = itemView.findViewById(R.id.tv_Glsmenitgoals);
    }
}
