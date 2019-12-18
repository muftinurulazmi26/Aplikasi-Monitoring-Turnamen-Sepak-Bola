package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHKlasemen extends RecyclerView.ViewHolder {
    public TextView namaKomp;
    public RecyclerView rc_Tim;

    public VHKlasemen(@NonNull View itemView) {
        super(itemView);

        namaKomp = itemView.findViewById(R.id.tv_namaKomp);
        rc_Tim = itemView.findViewById(R.id.rc_Tim);
    }

}
