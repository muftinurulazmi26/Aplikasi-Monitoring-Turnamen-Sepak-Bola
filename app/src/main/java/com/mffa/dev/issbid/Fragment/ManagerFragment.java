package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.Manager;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class ManagerFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlertDialog mAlertDialog;
    private FirebaseFirestore db;
    private CollectionReference table_manager;
    private FirestoreRecyclerAdapter<Manager,VHManager> adapter;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager, container, false);

        getActivity().setTitle("Manager");

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        db = FirebaseFirestore.getInstance();
        table_manager = db.collection("Manager");

        recyclerView = view.findViewById(R.id.rc_manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        FirestoreRecyclerOptions<Manager> options = new FirestoreRecyclerOptions.Builder<Manager>()
                .setQuery(table_manager, Manager.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Manager, VHManager>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHManager holder, int position, @NonNull final Manager model) {
                mAlertDialog.dismiss();
                holder.namaManager.setText(model.getNamaManager());
                holder.timManager.setText(model.getTimManager());
                holder.tinggalManager.setText(model.getTinggalManager());

                Glide.with(getContext()).load(model.getProfil())
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
                        }).into(holder.profilManager);
            }

            @NonNull
            @Override
            public VHManager onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_manager,viewGroup,false);

                return new VHManager(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                MDToast.makeText(getActivity(),""+e.getMessage(),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
            }

        };
        adapter.startListening();

        loadManager();

        return view;
    }

    private void deleteManager(final Manager model) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah kamu yakin ingin menghapusnya?")
                .setConfirmText("IYA")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        DocumentReference df = table_manager.document(model.getIdManager());
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

    private void loadManager() {
        recyclerView.setAdapter(adapter);
        //Animation load
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

}
