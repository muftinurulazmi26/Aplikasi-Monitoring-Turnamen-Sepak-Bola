package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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
import com.mffa.dev.issbid.Model.NextMatch;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHNextMatch;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class NextMatchFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlertDialog mAlertDialog;
    private FirebaseFirestore db;
    private CollectionReference table_nextmatch,table_tim,table_match;
    private FirestoreRecyclerAdapter<NextMatch,VHNextMatch> adapter;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_match, container, false);

        getActivity().setTitle("Next Match");

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        db = FirebaseFirestore.getInstance();
        table_nextmatch = db.collection("Next Match");
        table_match = db.collection("Match");
        table_tim = db.collection("Tim");

        recyclerView = view.findViewById(R.id.rc_nextmatch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        FirestoreRecyclerOptions<NextMatch> options = new FirestoreRecyclerOptions.Builder<NextMatch>()
                .setQuery(table_nextmatch, NextMatch.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<NextMatch, VHNextMatch>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHNextMatch holder, int position, @NonNull final NextMatch model) {
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

                String ambilDate = String.valueOf(model.getTanggal()+"/"+model.getRemindTime());

                if (ambilDate.equals(todayAsString)){
                    holder.tanggal.setText("Hari ini, "+model.getJam()+" WIB");
                    DocumentReference docRef = table_match.document(model.getNamaTim()+"vs"+model.getLawanTim());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                } else {
                                    //Log.d(TAG, "No such document");
                                    String idMatch = model.getNamaTim()+"vs"+model.getLawanTim();
                                    NextMatch uploadMatch = new NextMatch(
                                            idMatch,
                                            model.getPertandingan(),
                                            model.getNamaTim(),
                                            model.getLawanTim(),
                                            model.getTanggal(),
                                            model.getJam(),
                                            model.getRemindTime(),
                                            model.getMenitBermain(),
                                            model.getSkorTim(),
                                            model.getSkorLawan(),
                                            model.getKickOff(),
                                            model.getUrlvid()
                                    );

                                    table_match.document(idMatch).set(uploadMatch)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                }
                            } else {
                                //Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
                else if (ambilDate.equals(tomorrowAsString)){
                    holder.tanggal.setText("Besok, "+model.getJam()+" WIB");
                }
                else if (ambilDate.equals(yesterdayAsString)){
                    holder.tanggal.setText("Kemarin, "+model.getJam()+" WIB");
                    DocumentReference df = table_nextmatch.document(model.getId());
                    df.delete();
                }
                else {
                    holder.tanggal.setText(model.getTanggal()+"/"+model.getRemindTime()+", "+model.getJam()+" WIB");
                }

                holder.btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharmatch = new Intent(Intent.ACTION_SEND);
                        sharmatch.setType("text/plain");
                        String sharebody = "Jangan lupa saksikanlah pertandingan sepak bola antara, " +model.getNamaTim()+
                                " melawan "+model.getLawanTim()+" "+holder.tanggal.getText().toString()+", Download aplikasinya di https://play.google.com/store/apps/details?id="+getActivity().getPackageName()+", untuk melihat jadwal dan hasil pertandingan.";
                        sharmatch.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(sharmatch,"Share Via"));
                    }
                });
                Query query1 = table_tim.whereEqualTo("namatim",model.getNamaTim());
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
            public VHNextMatch onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_nextmatch,viewGroup,false);

                return new VHNextMatch(view);
            }
        };
        adapter.startListening();

        loadNextMatch();

        return view;
    }

    private void loadNextMatch() {
        recyclerView.setAdapter(adapter);
        //Animation load
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
