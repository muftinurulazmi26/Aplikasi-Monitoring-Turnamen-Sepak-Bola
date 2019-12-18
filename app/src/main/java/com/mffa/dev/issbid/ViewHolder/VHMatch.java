package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHMatch extends RecyclerView.ViewHolder{
    public TextView nmPertandingan,nmTim,lawanTim,tanggal,tv_status,tv_skortim,tv_skorlawan,tv_menitBermain;
    public ImageView logoTim,logolawan,btn_share,btn_live;
    public ProgressBar progressBar,progressBar2;
    public RecyclerView rc_goalTim,rc_lawanTim;

    public VHMatch(@NonNull View itemView) {
        super(itemView);

        nmPertandingan = itemView.findViewById(R.id.tv_namaPertandingan);
        nmTim = itemView.findViewById(R.id.tv_namaTim);
        lawanTim = itemView.findViewById(R.id.tv_namaLawan);
        tanggal = itemView.findViewById(R.id.tv_waktu);
        tv_status = itemView.findViewById(R.id.tv_status);
        tv_skortim = itemView.findViewById(R.id.tv_skortim);
        tv_skorlawan = itemView.findViewById(R.id.tv_skorlawan);
        tv_menitBermain = itemView.findViewById(R.id.tv_menitBermain);
        logoTim = itemView.findViewById(R.id.nm_logo_tim);
        logolawan = itemView.findViewById(R.id.nm_logo_lawan);
        progressBar = itemView.findViewById(R.id.progressBar);
        progressBar2 = itemView.findViewById(R.id.progressBar2);
        btn_share = itemView.findViewById(R.id.btn_share);
        btn_live = itemView.findViewById(R.id.btn_live);
        rc_goalTim = itemView.findViewById(R.id.rc_goalTim);
        rc_lawanTim = itemView.findViewById(R.id.rc_lawanTim);
    }
}
