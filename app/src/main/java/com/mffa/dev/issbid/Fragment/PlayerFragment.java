package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.Player;
import com.mffa.dev.issbid.Model.Tim;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHPlayer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class PlayerFragment extends Fragment {
    private AlertDialog mAlertDialog;
    private FirebaseFirestore db;
    private CollectionReference table_player;
    private FirestoreRecyclerAdapter<Player,VHPlayer> adapter;
    private FirestoreRecyclerAdapter<Player,VHPlayer> searchAdapter;
    private RecyclerView recyclerView;
    private MaterialSearchBar mMaterialSearchBar;
    List<String> searchsuggest = new ArrayList<>();
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        getActivity().setTitle("Player");

        mAdView = view.findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        db = FirebaseFirestore.getInstance();
        table_player = db.collection("Player");

        recyclerView = view.findViewById(R.id.rc_tim);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mMaterialSearchBar = view.findViewById(R.id.searchMenu);

        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(table_player, Player.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Player, VHPlayer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHPlayer holder, int position, @NonNull final Player model) {
                mAlertDialog.dismiss();
                holder.namaPlayer.setText(model.getNama());
                holder.ttlPlayer.setText(model.getTmpLahir()+", "+model.getTglLahir());
                holder.alamatPlayer.setText(model.getAlamat());
                holder.timPlayer.setText(model.getTim());
                holder.posisiPlayer.setText(model.getPosisi());
                holder.noPunggung.setText(model.getNoPunggung());

                Glide.with(getActivity()).load(model.getImage())
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
                        .into(holder.img_profil);
            }

            @NonNull
            @Override
            public VHPlayer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_player,viewGroup,false);

                return new VHPlayer(view);
            }
        };
        adapter.startListening();

        loadPlayer();

        loadSearchSuggest();
        mMaterialSearchBar.setTextHintColor(R.color.colorPrimaryDark);
        mMaterialSearchBar.setCardViewElevation(10);

        mMaterialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest1 = new ArrayList<String>();
                for (String search : searchsuggest){
                    if (search.toLowerCase().contains(mMaterialSearchBar.getText().toLowerCase()))
                        suggest1.add(search);
                }
                mMaterialSearchBar.setLastSuggestions(suggest1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMaterialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        return view;
    }

    private void loadSearchSuggest() {
        table_player.orderBy("nama").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Player item = document.toObject(Player.class);
                        searchsuggest.add(item.getNama());
                    }
                    //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    //Log.d("HASIL",myListOfDocuments.toString());
                }
            }
        });
    }

    private void startSearch(CharSequence text) {
        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(table_player.whereEqualTo("nama", text.toString()), Player.class)
                .build();

        searchAdapter = new FirestoreRecyclerAdapter<Player, VHPlayer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHPlayer holder, int position, @NonNull final Player model) {
                mAlertDialog.dismiss();
                holder.namaPlayer.setText(model.getNama());
                holder.ttlPlayer.setText(model.getTmpLahir()+", "+model.getTglLahir());
                holder.alamatPlayer.setText(model.getAlamat());
                holder.timPlayer.setText(model.getTim());
                holder.posisiPlayer.setText(model.getPosisi());
                holder.noPunggung.setText(model.getNoPunggung());


                Glide.with(getActivity()).load(model.getImage())
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
                        .into(holder.img_profil);
            }

            @NonNull
            @Override
            public VHPlayer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_player,viewGroup,false);

                return new VHPlayer(view);
            }
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadPlayer() {
        recyclerView.setAdapter(adapter);
        //Animation load
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void deletePlayer(final Player model){
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah kamu yakin ingin menghapusnya?")
                .setConfirmText("IYA")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        DocumentReference df = table_player.document(model.getId());
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

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
