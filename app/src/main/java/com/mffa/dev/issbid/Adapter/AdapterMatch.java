package com.mffa.dev.issbid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.NextMatch;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;

public class AdapterMatch extends FirestoreRecyclerAdapter<NextMatch,AdapterMatch.VHTim> {
    private Context context;
    private FirebaseFirestore db;
    private CollectionReference table_nextmatch,table_tim;

    public AdapterMatch(@NonNull FirestoreRecyclerOptions<NextMatch> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final VHTim holder, int position, @NonNull NextMatch model) {
        holder.nmPertandingan.setText(model.getPertandingan());
        holder.nmTim.setText(model.getNamaTim());
        holder.lawanTim.setText(model.getLawanTim());
        holder.tanggal.setText(model.getTanggal()+", "+model.getJam()+" WIB");
        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Query query1 = table_tim.whereEqualTo("namatim",model.getNamaTim());
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tim item = document.toObject(Tim.class);
                        Glide.with(context).load(item.getImage())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        holder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        holder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .error(R.drawable.error_server)
                                .into(holder.logoTim);
                    }
                    //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    //Log.d("HASIL",myListOfDocuments.toString());
                }
            }
        });

        Query query2 = table_tim.whereEqualTo("namatim",model.getLawanTim());
        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tim item = document.toObject(Tim.class);
                        Glide.with(context).load(item.getImage())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        holder.progressBar2.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        holder.progressBar2.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .error(R.drawable.error_server)
                                .into(holder.logolawan);
                    }
                    //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    //Log.d("HASIL",myListOfDocuments.toString());
                }
            }
        });
    }

    @NonNull
    @Override
    public VHTim onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_nextmatch,viewGroup,false);

        db = FirebaseFirestore.getInstance();
        table_nextmatch = db.collection("Next Match");
        table_tim = db.collection("Tim");

        return new VHTim(view);
    }

    public class VHTim extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nmPertandingan,nmTim,lawanTim,tanggal;
        public ImageView logoTim,logolawan,btn_share;
        public ItemClickListener mItemClickListener;
        public ProgressBar progressBar,progressBar2;

        public VHTim(@NonNull View itemView) {
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

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
