package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.R;
import com.ramotion.foldingcell.FoldingCell;

public class VHTim extends RecyclerView.ViewHolder{
    public TextView namaTim,julukan,didirikan,directure,manager,asistenManager
            ,timMedis1,timMedis2,keamanan1,keamanan2,hukuman,keterangan,terdaftar,update;
    public ImageView logoTim,profilTim,player,btn_Share;
    public FoldingCell foldingCell;
    public ItemClickListener mItemClickListener;
    public ProgressBar progressBar;

    public VHTim(@NonNull View itemView) {
        super(itemView);

        namaTim = itemView.findViewById(R.id.tv_namaTim);
        julukan = itemView.findViewById(R.id.tv_julukan_Tim);
        didirikan = itemView.findViewById(R.id.tv_didirikan_Tim);
        directure = itemView.findViewById(R.id.tv_directur_Tim);
        manager = itemView.findViewById(R.id.tv_manager_Tim);
        asistenManager = itemView.findViewById(R.id.tv_asistmanager_Tim);
        timMedis1 = itemView.findViewById(R.id.tv_tim_medias1);
        timMedis2 = itemView.findViewById(R.id.tv_tim_medis2);
        keamanan1 = itemView.findViewById(R.id.tv_keamanan_tim1);
        keamanan2 = itemView.findViewById(R.id.tv_keamanan_tim2);
        hukuman = itemView.findViewById(R.id.tv_hukuman);
        terdaftar = itemView.findViewById(R.id.tv_terdafar);
        update = itemView.findViewById(R.id.tv_update);
        keterangan = itemView.findViewById(R.id.tv_keterangan);
        btn_Share = itemView.findViewById(R.id.ic_Share);
        logoTim = itemView.findViewById(R.id.logo_Tim);
        profilTim = itemView.findViewById(R.id.img_logo);
        player = itemView.findViewById(R.id.player);
        foldingCell = itemView.findViewById(R.id.folding_cell);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}
