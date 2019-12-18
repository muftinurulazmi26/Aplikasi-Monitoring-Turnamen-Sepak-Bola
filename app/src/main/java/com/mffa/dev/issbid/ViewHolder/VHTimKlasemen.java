package com.mffa.dev.issbid.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mffa.dev.issbid.R;

public class VHTimKlasemen extends RecyclerView.ViewHolder {
    public TextView nmTim,m,k,s,mg,kg,poin;
    public VHTimKlasemen(@NonNull View itemView) {
        super(itemView);

        nmTim = itemView.findViewById(R.id.tv_namaTim);
        m = itemView.findViewById(R.id.tv_M);
        k = itemView.findViewById(R.id.tv_K);
        s = itemView.findViewById(R.id.tv_S);
        mg = itemView.findViewById(R.id.tv_MG);
        kg = itemView.findViewById(R.id.tv_KG);
        poin = itemView.findViewById(R.id.tv_Poin);
    }
}
