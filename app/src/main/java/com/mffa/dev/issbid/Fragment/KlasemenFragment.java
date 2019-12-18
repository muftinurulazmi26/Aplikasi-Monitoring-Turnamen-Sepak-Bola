package com.mffa.dev.issbid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mffa.dev.issbid.Model.Babak;
import com.mffa.dev.issbid.Model.Klasemen;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.Model.TimKlasemen;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHKlasemen;
import com.mffa.dev.issbid.ViewHolder.VHSisGugur;
import com.mffa.dev.issbid.ViewHolder.VHTimKlasemen;

public class KlasemenFragment extends Fragment {
    private FirebaseFirestore db;
    private CollectionReference table_klasemen,table_tim;
    private FirestoreRecyclerAdapter<Klasemen,VHKlasemen> adapter;
    private FirestoreRecyclerAdapter<TimKlasemen,VHTimKlasemen> adapTimKlas;
    private FirestoreRecyclerAdapter<Babak,VHSisGugur> adapBabak;
    private RecyclerView mRecyclerView,mRecyclerView2,mRecyclerView3;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_klasemen, container, false);

        getActivity().setTitle("Klasemen");

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        db = FirebaseFirestore.getInstance();
        table_klasemen = db.collection("Klasemen");
        table_tim = db.collection("Tim");

        mRecyclerView = view.findViewById(R.id.rc_kompPenuh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView2 = view.findViewById(R.id.rc_stngahKomp);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView2.setNestedScrollingEnabled(false);
        mRecyclerView3 = view.findViewById(R.id.rc_gugur);
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView3.setNestedScrollingEnabled(false);

        loadDataKompPenuh();

        loadDataStnghKomp();

        loadDataGugur();

        return view;
    }

    private void loadDataGugur() {

        Query qstnghKomp = table_klasemen.whereEqualTo("sistemKomp","Sistem Gugur");

        final FirestoreRecyclerOptions<Klasemen> options = new FirestoreRecyclerOptions.Builder<Klasemen>()
                .setQuery(qstnghKomp, Klasemen.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Klasemen, VHKlasemen>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VHKlasemen holder, int position, @NonNull final Klasemen model) {
                holder.namaKomp.setText(model.getNmKomp());

                final DocumentReference df = table_klasemen.document(model.getSistemKomp());
                final CollectionReference cdfTim = df.collection(model.getNmKomp());

                holder.rc_Tim.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.rc_Tim.setNestedScrollingEnabled(false);
                FirestoreRecyclerOptions<Babak> optionsTim = new FirestoreRecyclerOptions.Builder<Babak>()
                        .setQuery(cdfTim, Babak.class)
                        .build();

                adapBabak = new FirestoreRecyclerAdapter<Babak, VHSisGugur>(optionsTim) {
                    @Override
                    protected void onBindViewHolder(@NonNull final VHSisGugur holder, int position, @NonNull Babak model) {
                        holder.nmTim.setText(model.getNmTim());
                        holder.lawanTim.setText(model.getLawanTim());

                        Query query1 = table_tim.whereEqualTo("namatim",model.getNmTim());
                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Tim item = document.toObject(Tim.class);
                                        Glide.with(getActivity()).load(item.getImage())
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
                                        Glide.with(getActivity()).load(item.getImage())
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
                    public VHSisGugur onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_babak,viewGroup,false);

                        return new VHSisGugur(view);
                    }
                };
                adapBabak.startListening();
                holder.rc_Tim.setAdapter(adapBabak);
                holder.rc_Tim.getAdapter().notifyDataSetChanged();
            }

            @NonNull
            @Override
            public VHKlasemen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_sistemgugur,viewGroup,false);

                return new VHKlasemen(view);
            }
        };
        adapter.startListening();

        mRecyclerView3.setAdapter(adapter);
        //Animation load
        mRecyclerView3.getAdapter().notifyDataSetChanged();
        mRecyclerView3.scheduleLayoutAnimation();
    }

    private void loadDataStnghKomp() {

        Query qstnghKomp = table_klasemen.whereEqualTo("sistemKomp","Setengah Kompetisi");

        final FirestoreRecyclerOptions<Klasemen> options = new FirestoreRecyclerOptions.Builder<Klasemen>()
                .setQuery(qstnghKomp, Klasemen.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Klasemen, VHKlasemen>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VHKlasemen holder, int position, @NonNull final Klasemen model) {
                holder.namaKomp.setText(model.getNmKomp());

                final DocumentReference df = table_klasemen.document(model.getSistemKomp());
                final CollectionReference cdfTim = df.collection(model.getNmKomp());

                Query query = cdfTim.orderBy("posisi",Query.Direction.ASCENDING);

                holder.rc_Tim.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.rc_Tim.setNestedScrollingEnabled(false);
                FirestoreRecyclerOptions<TimKlasemen> optionsTim = new FirestoreRecyclerOptions.Builder<TimKlasemen>()
                        .setQuery(query, TimKlasemen.class)
                        .build();
                adapTimKlas = new FirestoreRecyclerAdapter<TimKlasemen, VHTimKlasemen>(optionsTim) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHTimKlasemen holder, int position, @NonNull final TimKlasemen model) {
                        holder.nmTim.setText(model.getNmTim());
                        holder.m.setText(model.getM());
                        holder.k.setText(model.getK());
                        holder.s.setText(model.getS());
                        holder.mg.setText(model.getMg());
                        holder.kg.setText(model.getKg());
                        holder.poin.setText(model.getPoin());
                    }

                    @NonNull
                    @Override
                    public VHTimKlasemen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_table_tim,viewGroup,false);

                        return new VHTimKlasemen(view);
                    }
                };
                adapTimKlas.startListening();
                holder.rc_Tim.setAdapter(adapTimKlas);
                holder.rc_Tim.getAdapter().notifyDataSetChanged();
            }

            @NonNull
            @Override
            public VHKlasemen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.table_skomppenuh,viewGroup,false);

                return new VHKlasemen(view);
            }
        };
        adapter.startListening();

        mRecyclerView2.setAdapter(adapter);
        //Animation load
        mRecyclerView2.getAdapter().notifyDataSetChanged();
        mRecyclerView2.scheduleLayoutAnimation();
    }

    private void loadDataKompPenuh() {

        Query qkompPenuh = table_klasemen.whereEqualTo("sistemKomp","Kompetisi Penuh");

        final FirestoreRecyclerOptions<Klasemen> options = new FirestoreRecyclerOptions.Builder<Klasemen>()
                .setQuery(qkompPenuh, Klasemen.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Klasemen, VHKlasemen>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VHKlasemen holder, int position, @NonNull final Klasemen model) {
                holder.namaKomp.setText(model.getNmKomp());

                final DocumentReference df = table_klasemen.document(model.getSistemKomp());
                final CollectionReference cdfTim = df.collection(model.getNmKomp());

                Query query = cdfTim.orderBy("posisi",Query.Direction.ASCENDING);

                holder.rc_Tim.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.rc_Tim.setNestedScrollingEnabled(false);
                FirestoreRecyclerOptions<TimKlasemen> optionsTim = new FirestoreRecyclerOptions.Builder<TimKlasemen>()
                        .setQuery(query, TimKlasemen.class)
                        .build();
                adapTimKlas = new FirestoreRecyclerAdapter<TimKlasemen, VHTimKlasemen>(optionsTim) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHTimKlasemen holder, int position, @NonNull final TimKlasemen model) {
                        holder.nmTim.setText(model.getNmTim());
                        holder.m.setText(model.getM());
                        holder.k.setText(model.getK());
                        holder.s.setText(model.getS());
                        holder.mg.setText(model.getMg());
                        holder.kg.setText(model.getKg());
                        holder.poin.setText(model.getPoin());
                    }

                    @NonNull
                    @Override
                    public VHTimKlasemen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_table_tim,viewGroup,false);

                        return new VHTimKlasemen(view);
                    }
                };
                adapTimKlas.startListening();
                holder.rc_Tim.setAdapter(adapTimKlas);
                holder.rc_Tim.getAdapter().notifyDataSetChanged();
            }

            @NonNull
            @Override
            public VHKlasemen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.table_skomppenuh,viewGroup,false);

                return new VHKlasemen(view);
            }
        };
        adapter.startListening();

        mRecyclerView.setAdapter(adapter);
        //Animation load
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }
}
