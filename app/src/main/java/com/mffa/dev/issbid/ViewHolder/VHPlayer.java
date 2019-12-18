package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VHPlayer extends RecyclerView.ViewHolder {
    public TextView namaPlayer,ttlPlayer,alamatPlayer,timPlayer,posisiPlayer,noPunggung;
    public CircleImageView img_profil;
    public ItemClickListener mItemClickListener;
    public ProgressBar progressBar;

    public VHPlayer(@NonNull View itemView) {
        super(itemView);

        namaPlayer = itemView.findViewById(R.id.tv_namaPlayer);
        ttlPlayer = itemView.findViewById(R.id.tv_ttlPlayer);
        alamatPlayer = itemView.findViewById(R.id.tv_alamatPlayer);
        timPlayer = itemView.findViewById(R.id.tv_timPlayer);
        posisiPlayer = itemView.findViewById(R.id.tv_posisiPlayer);
        noPunggung = itemView.findViewById(R.id.tv_nopunggungPlayer);
        img_profil = itemView.findViewById(R.id.img_profil);
        progressBar = itemView.findViewById(R.id.progressBar);
    }

}
