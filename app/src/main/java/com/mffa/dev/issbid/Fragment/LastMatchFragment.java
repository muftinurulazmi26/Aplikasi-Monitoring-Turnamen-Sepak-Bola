package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mffa.dev.issbid.LiveBroadcaster.LiveMainActivity;
import com.mffa.dev.issbid.Model.Goals;
import com.mffa.dev.issbid.Model.NextMatch;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHAddGoals;
import com.mffa.dev.issbid.ViewHolder.VHMatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class LastMatchFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlertDialog mAlertDialog;
    private FirebaseFirestore db;
    private CollectionReference table_tim,table_match;
    private FirestoreRecyclerAdapter<NextMatch,VHMatch> adapter;
    private FirestoreRecyclerAdapter<Goals,VHAddGoals> addGoal,addLawan;
    String tglSekarang;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_match, container, false);

        getActivity().setTitle("Last Match");

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        db = FirebaseFirestore.getInstance();
        table_tim = db.collection("Tim");
        table_match = db.collection("Match");

        recyclerView = view.findViewById(R.id.rc_lastmatch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        tglSekarang = simpleDateFormat.format(calendar.getTime());
        int itglSkrng =Integer.parseInt(tglSekarang)-1;

        Query qr = table_match.whereEqualTo("kickOff", "Selesai");;

        FirestoreRecyclerOptions<NextMatch> options = new FirestoreRecyclerOptions.Builder<NextMatch>()
                .setQuery(qr, NextMatch.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<NextMatch, VHMatch>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHMatch holder, int position, @NonNull final NextMatch model) {
                mAlertDialog.dismiss();
                holder.nmPertandingan.setText(model.getPertandingan());
                holder.nmTim.setText(model.getNamaTim());
                holder.lawanTim.setText(model.getLawanTim());

                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();

                calendar.add(Calendar.DAY_OF_YEAR, -2);
                Date yesterday = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("EEE, MM/yyyy/dd");

                String todayAsString = dateFormat.format(today);
                String tomorrowAsString = dateFormat.format(tomorrow);
                String yesterdayAsString = dateFormat.format(yesterday);

                final String ambilDate = String.valueOf(model.getTanggal()+"/"+model.getRemindTime());

                holder.btn_live.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),LiveMainActivity.class);
                        intent.putExtra("nm_pertandingan",model.getId());
                        intent.putExtra("urlvid",model.getUrlvid());
                        intent.putExtra("tgl",ambilDate);
                        intent.putExtra("status",model.getKickOff());
                        startActivity(intent);
                    }
                });

                if (ambilDate.equals(todayAsString)){
                    holder.tanggal.setText("Hari ini, "+model.getJam()+" WIB");
                }
                else if (ambilDate.equals(tomorrowAsString)){
                    holder.tanggal.setText("Besok, "+model.getJam()+" WIB");
                }
                else if (ambilDate.equals(yesterdayAsString)){
                    holder.tanggal.setText("Kemarin, "+model.getJam()+" WIB");
                }
                else {
                    holder.tanggal.setText(model.getTanggal()+"/"+model.getRemindTime()+", "+model.getJam()+" WIB");
                }

                holder.tv_skortim.setText(model.getSkorTim());
                holder.tv_skorlawan.setText(model.getSkorLawan());
                if (model.getKickOff().equals("Bersiap")){
                    holder.tv_status.setText("Belum dimulai");
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_orange_light_stroke_color));
                }
                else if (model.getKickOff().equals("Mulai")){
                    holder.tv_status.setText("Sedang Berlangsung");
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_green_stroke_color));
                }
                else if (model.getKickOff().equals("Selesai")){
                    holder.tv_status.setText("Selesai");
                    holder.tv_status.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else {
                    holder.tv_status.setText(model.getKickOff());
                    holder.tv_status.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                holder.tv_menitBermain.setText("Menit Bermain: 2x"+model.getMenitBermain()+"'");

                final DocumentReference df = table_match.document(model.getId());
                final CollectionReference cdfTim = df.collection(model.getNamaTim());
                final CollectionReference cdfLawan = df.collection(model.getLawanTim());

                //memuat data tim penantang
                holder.rc_goalTim.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.rc_goalTim.setNestedScrollingEnabled(false);
                FirestoreRecyclerOptions<Goals> optionsTim = new FirestoreRecyclerOptions.Builder<Goals>()
                        .setQuery(cdfTim, Goals.class)
                        .build();

                addGoal = new FirestoreRecyclerAdapter<Goals, VHAddGoals>(optionsTim) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHAddGoals holder, int position, @NonNull Goals model) {
                        holder.nmPlayer.setText(model.getNamaPlayer());
                        holder.menitGoals.setText(model.getMenitgoals()+"'");
                        if (model.getIcon().equals("Gool")){
                            holder.icon.setImageResource(R.drawable.ball);
                        }
                        else if (model.getIcon().equals("Kartu Kuning")){
                            holder.icon.setImageResource(R.drawable.yellow_card);
                        }
                        else if (model.getIcon().equals("Kartu Merah")){
                            holder.icon.setImageResource(R.drawable.red_card);
                        }
                    }

                    @NonNull
                    @Override
                    public VHAddGoals onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_addgoalsleft,viewGroup,false);

                        return new VHAddGoals(view);
                    }
                };
                addGoal.startListening();
                holder.rc_goalTim.setAdapter(addGoal);
                holder.rc_goalTim.getAdapter().notifyDataSetChanged();

                //memuat data tim lawan
                holder.rc_lawanTim.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.rc_lawanTim.setNestedScrollingEnabled(false);
                FirestoreRecyclerOptions<Goals> optionsLawan = new FirestoreRecyclerOptions.Builder<Goals>()
                        .setQuery(cdfLawan, Goals.class)
                        .build();

                addLawan = new FirestoreRecyclerAdapter<Goals, VHAddGoals>(optionsLawan) {
                    @Override
                    protected void onBindViewHolder(@NonNull VHAddGoals holder, int position, @NonNull Goals model) {
                        holder.nmPlayer.setText(model.getNamaPlayer());
                        holder.menitGoals.setText(model.getMenitgoals()+"'");
                        if (model.getIcon().equals("Gool")){
                            holder.icon.setImageResource(R.drawable.ball);
                        }
                        else if (model.getIcon().equals("Kartu Kuning")){
                            holder.icon.setImageResource(R.drawable.yellow_card);
                        }
                        else if (model.getIcon().equals("Kartu Merah")){
                            holder.icon.setImageResource(R.drawable.red_card);
                        }
                    }

                    @NonNull
                    @Override
                    public VHAddGoals onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_addgoalsright,viewGroup,false);
                        return new VHAddGoals(view);
                    }
                };
                addLawan.startListening();
                holder.rc_lawanTim.setAdapter(addLawan);
                holder.rc_lawanTim.getAdapter().notifyDataSetChanged();
                holder.btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.tv_status.getText().toString().equals("Selesai")){
                            Intent sharmatch = new Intent(Intent.ACTION_SEND);
                            sharmatch.setType("text/plain");
                            String sharebody = "Skor akhir pertandingan antara " +model.getNamaTim()+ " "+ holder.tv_skortim.getText().toString() +
                                    " VS " + holder.tv_skorlawan.getText().toString() +" "+ model.getLawanTim()+" dalam ajang "+holder.nmPertandingan.getText().toString();
                            sharmatch.putExtra(Intent.EXTRA_TEXT,sharebody);
                            startActivity(Intent.createChooser(sharmatch,"Share Via"));
                        } else if (holder.tv_status.getText().toString().equals("Belum dimulai")){
                            Intent sharmatch = new Intent(Intent.ACTION_SEND);
                            sharmatch.setType("text/plain");
                            String sharebody = "Pertandingan antara, " +model.getNamaTim()+
                                    " melawan "+model.getLawanTim()+ " akan segera di mulai.";
                            sharmatch.putExtra(Intent.EXTRA_TEXT,sharebody);
                            startActivity(Intent.createChooser(sharmatch,"Share Via"));
                        } else if (holder.tv_status.getText().toString().equals("Sedang Berlangsung")){
                            Intent sharmatch = new Intent(Intent.ACTION_SEND);
                            sharmatch.setType("text/plain");
                            String sharebody = "Pertandingan antara, " +model.getNamaTim()+
                                    " melawan "+model.getLawanTim()+ " sedang berlangsung.";
                            sharmatch.putExtra(Intent.EXTRA_TEXT,sharebody);
                            startActivity(Intent.createChooser(sharmatch,"Share Via"));
                        }
                    }
                });
                Query query1 = table_tim.whereEqualTo("namatim",model.getNamaTim());
                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tim item = document.toObject(Tim.class);
                                Glide.with(getActivity().getApplicationContext()).load(item.getImage())
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
                                Glide.with(getActivity().getApplicationContext()).load(item.getImage())
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
            public VHMatch onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_match,viewGroup,false);

                return new VHMatch(view);
            }
        };
        adapter.startListening();

        loadLastMatch();

        return view;
    }

    private void loadLastMatch() {
        recyclerView.setAdapter(adapter);
        //Animation load
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
