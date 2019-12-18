package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.R;

public class VHManager extends RecyclerView.ViewHolder {
    public TextView namaManager,tinggalManager,timManager;
    public ImageView profilManager;
    public ItemClickListener mItemClickListener;
    public ProgressBar progressBar;

    public VHManager(@NonNull View itemView) {
        super(itemView);

        namaManager = itemView.findViewById(R.id.tv_namaManager);
        tinggalManager = itemView.findViewById(R.id.tv_tinggalManager);
        timManager = itemView.findViewById(R.id.tv_namaTim);
        profilManager = itemView.findViewById(R.id.profilManager);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}
