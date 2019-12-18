package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHSisGugur extends RecyclerView.ViewHolder{
    public TextView nmTim,lawanTim;
    public ImageView logoTim,logolawan;
    public ProgressBar progressBar,progressBar2;

    public VHSisGugur(@NonNull View itemView) {
        super(itemView);

        nmTim = itemView.findViewById(R.id.tv_namaTim);
        lawanTim = itemView.findViewById(R.id.tv_namaLawan);
        logoTim = itemView.findViewById(R.id.nm_logo_tim);
        logolawan = itemView.findViewById(R.id.nm_logo_lawan);
        progressBar = itemView.findViewById(R.id.progressBar);
        progressBar2 = itemView.findViewById(R.id.progressBar2);
    }
}
