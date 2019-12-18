package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mffa.dev.issbid.Adapter.AdapterTim;
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.Manager;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHTim;
import com.theartofdev.edmodo.cropper.CropImage;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimFragment extends Fragment {
    private AlertDialog mAlertDialog;
    private FirebaseFirestore db;
    private CollectionReference table_tim;
    private String idTim;
    private FirestoreRecyclerAdapter<Tim,VHTim> adapter;
    private RecyclerView mRecyclerView;
    private AdapterTim adapterTim;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tim, container, false);

        getActivity().setTitle("Tim");

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        db = FirebaseFirestore.getInstance();
        table_tim = db.collection("Tim");

        mRecyclerView = view.findViewById(R.id.rc_tim);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);

        FirestoreRecyclerOptions<Tim> options = new FirestoreRecyclerOptions.Builder<Tim>()
                .setQuery(table_tim, Tim.class)
                .build();

        //adapterTim = new AdapterTim(options,getActivity());
        //mRecyclerView.setAdapter(adapterTim);
        //mRecyclerView.getAdapter().notifyDataSetChanged();

        adapter = new FirestoreRecyclerAdapter<Tim, VHTim>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHTim holder, int position, @NonNull final Tim model) {
                mAlertDialog.dismiss();
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
                holder.terdaftar.setText("Terdaftar: "+model.getTerdaftar());
                holder.update.setText("Update: "+model.getUpdate());
                holder.btn_Share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharmatch = new Intent(Intent.ACTION_SEND);
                        sharmatch.setType("text/plain");
                        String sharebody = "Sekarang jadwal dan hasil pertandingan dari klub " +model.getNamatim()+
                                " ada di aplikasi https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
                        sharmatch.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(sharmatch,"Share Via"));
                    }
                });
                holder.foldingCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.foldingCell.toggle(false);
                    }
                });

                holder.player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("tim",model.getNamatim());
                        Fragment fragment = null;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        fragment = new PemainFragment();
                        fragment.setArguments(bundle);
                        ft.addToBackStack(null);
                        ft.replace(R.id.frame_content,fragment).commit();
                        //MDToast.makeText(getActivity(),"Oke",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
                    }
                });

                Glide.with(getActivity()).load(model.getImage())
                        .placeholder(R.drawable.ph_tim)
                        .error(R.drawable.error_server)
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
                        }).into(holder.logoTim);
                //Glide.with(getContext()).load(model.getImage()).into(holder.profilTim);
            }

            @NonNull
            @Override
            public VHTim onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.foldingcell_tim,viewGroup,false);

                return new VHTim(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                MDToast.makeText(getActivity(),""+e.getMessage(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
            }
        };
        adapter.startListening();

        loadTim();

        return view;
    }

    private void deleteTim(final Tim model){
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah kamu yakin ingin menghapusnya?")
                .setConfirmText("IYA")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        DocumentReference df = table_tim.document(model.getId());
                        df.delete();
                        sweetAlertDialog
                                .setTitleText("Terhapus")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

    private void loadTim() {
        mRecyclerView.setAdapter(adapter);
        //Animation load
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();
        //adapterTim.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapterTim.stopListening();
        //adapter.stopListening();
    }
}
