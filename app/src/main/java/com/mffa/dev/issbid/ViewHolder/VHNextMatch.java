package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHNextMatch extends RecyclerView.ViewHolder{
    public TextView nmPertandingan,nmTim,lawanTim,tanggal;
    public ImageView logoTim,logolawan,btn_share;
    public ProgressBar progressBar,progressBar2;
    public CardView cd_nextmatch;

    public VHNextMatch(@NonNull View itemView) {
        super(itemView);

        nmPertandingan = itemView.findViewById(R.id.tv_namaPertandingan);
        nmTim = itemView.findViewById(R.id.tv_namaTim);
        lawanTim = itemView.findViewById(R.id.tv_namaLawan);
        tanggal = itemView.findViewById(R.id.tv_waktu);
        logoTim = itemView.findViewById(R.id.nm_logo_tim);
        logolawan = itemView.findViewById(R.id.nm_logo_lawan);
        progressBar = itemView.findViewById(R.id.progressBar);
        progressBar2 = itemView.findViewById(R.id.progressBar2);
        btn_share = itemView.findViewById(R.id.btn_share);
        cd_nextmatch = itemView.findViewById(R.id.cd_nextmatch);
    }
}
