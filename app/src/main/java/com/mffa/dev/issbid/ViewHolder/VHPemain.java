package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VHPemain extends RecyclerView.ViewHolder{
    public TextView namaPlayer,posisiPlayer;
    public CircleImageView img_profil;
    public ItemClickListener mItemClickListener;
    public ProgressBar progressBar;

    public VHPemain(@NonNull View itemView) {
        super(itemView);

        namaPlayer = itemView.findViewById(R.id.tv_namaPlayer);
        posisiPlayer = itemView.findViewById(R.id.tv_posisiPlayer);
        img_profil = itemView.findViewById(R.id.img_profil);
        progressBar = itemView.findViewById(R.id.progressBar);
    }

}
