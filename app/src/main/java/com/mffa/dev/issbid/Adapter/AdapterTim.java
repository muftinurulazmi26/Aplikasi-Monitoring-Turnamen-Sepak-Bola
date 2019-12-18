package com.mffa.dev.issbid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.ramotion.foldingcell.FoldingCell;

public class AdapterTim extends FirestoreRecyclerAdapter<Tim,AdapterTim.VHTim> {
    private Context context;

    public AdapterTim(@NonNull FirestoreRecyclerOptions<Tim> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final VHTim holder, int position, @NonNull Tim model) {
        holder.namaTim.setText(model.getNamatim());
        holder.julukan.setText("Julukan: "+model.getJulukan());
        holder.didirikan.setText("Didirikan: "+model.getDidirikan());
        holder.directure.setText("Direktur: "+model.getDirecture());
        holder.manager.setText("Manager: "+model.getManager());
        holder.asistenManager.setText("Asisten Manager: "+model.getAsistenmanager());
        holder.timMedis1.setText("1. "+model.getTimmedis1());
        holder.timMedis2.setText("2. "+model.getTimmedis2());
        holder.keamanan1.setText("1. "+model.getKeamanan1());
        holder.keamanan2.setText("2. "+model.getKeamanan2());
        holder.hukuman.setText("Hukuman: "+model.getHukuman());
        holder.keterangan.setText("Keterangan: "+model.getKet());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

            }
        });
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });

        Glide.with(context).load(model.getImage()).into(holder.logoTim);
        Glide.with(context).load(model.getImage()).into(holder.profilTim);
    }

    @NonNull
    @Override
    public VHTim onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.foldingcell_tim,viewGroup,false);

        return new VHTim(view);
    }

    public class VHTim extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView namaTim,julukan,didirikan,directure,manager,asistenManager
                ,timMedis1,timMedis2,keamanan1,keamanan2,hukuman,keterangan;
        public ImageView logoTim,profilTim;
        public FoldingCell foldingCell;
        public ItemClickListener itemClickListener;

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
            keterangan = itemView.findViewById(R.id.tv_keterangan);
            logoTim = itemView.findViewById(R.id.logo_Tim);
            profilTim = itemView.findViewById(R.id.img_logo);
            foldingCell = itemView.findViewById(R.id.folding_cell);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
