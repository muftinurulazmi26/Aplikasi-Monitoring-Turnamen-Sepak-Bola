package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mffa.dev.issbid.Interface.ItemClickListener;
import com.mffa.dev.issbid.Model.Player;
import com.mffa.dev.issbid.R;
import com.mffa.dev.issbid.ViewHolder.VHPemain;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PemainFragment extends Fragment {
    private TextView tv_namaPlayer,tv_posisiPlayer;
    private ImageView img_profil;
    private FirebaseFirestore db;
    private CollectionReference table_player;
    private FirestoreRecyclerAdapter<Player,VHPemain> adapter;
    private AlertDialog mAlertDialog;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pemain, container, false);

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        db = FirebaseFirestore.getInstance();
        table_player = db.collection("Player");

        String namaTim = getArguments().getString("tim");
        getActivity().setTitle("Pemain "+namaTim);
        recyclerView = view.findViewById(R.id.rc_pemainTim);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(table_player.whereEqualTo("tim", namaTim), Player.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Player, VHPemain>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VHPemain holder, int position, @NonNull final Player model) {
                mAlertDialog.dismiss();
                holder.namaPlayer.setText(model.getNama());
                holder.posisiPlayer.setText(model.getPosisi());
                holder.namaPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog popupdetail = new AlertDialog.Builder(getContext()).create();

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View detailPemain = inflater.inflate(R.layout.item_detailtimpemain,null);

                        TextView tglLahir = detailPemain.findViewById(R.id.tv_tglLahir);
                        TextView alamat = detailPemain.findViewById(R.id.tv_alamatPlayer);
                        TextView noPunggung = detailPemain.findViewById(R.id.tv_noPunggung);
                        TextView usiaDlmHr = detailPemain.findViewById(R.id.tv_usiadlmhr);
                        TextView usiaDlmBln = detailPemain.findViewById(R.id.tv_usiadlmbl);
                        TextView usiaDlmThn = detailPemain.findViewById(R.id.tv_usiadlmthn);

                        String getTgl = String.valueOf(model.getTglLahir());
                        int igetTgl = Integer.parseInt(getTgl.substring(0,2));
                        String getBln = model.getTglLahir();
                        int igetBln = Integer.parseInt(getBln.substring(3,5));
                        String getThn = model.getTglLahir();
                        int igetThn = Integer.parseInt(getThn.substring(6,10));

                        Calendar calendar = Calendar.getInstance();
                        int tglSkrng = calendar.get(Calendar.DAY_OF_MONTH);
                        int blnSkrng = calendar.get(Calendar.MONDAY)+1;
                        int thnSkrng = calendar.get(Calendar.YEAR);

                        tglLahir.setText("Tempat/Tgl lahir: "+model.getTmpLahir()+", "+model.getTglLahir());
                        alamat.setText("Alamat: "+model.getAlamat());
                        noPunggung.setText("No punggung: "+model.getNoPunggung());
                        int hr = ((((thnSkrng - igetThn)*12)-(tglSkrng-igetTgl))*30);
                        int bln = (((thnSkrng - igetThn)*12)-(blnSkrng-igetBln));
                        int thn = thnSkrng-igetThn;
                        usiaDlmThn.setText("Usia dalam tahun: "+String.valueOf(thn));

                        popupdetail.setView(detailPemain);
                        popupdetail.show();
                    }
                });

                Glide.with(getActivity()).load(model.getImage())
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
                        }).into(holder.img_profil);
            }

            @NonNull
            @Override
            public VHPemain onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_timpemain,viewGroup,false);

                return new VHPemain(view);
            }
        };
        adapter.startListening();

        loadTimPemian();

        return view;
    }

    private void loadTimPemian() {
        recyclerView.setAdapter(adapter);
        //Animation load
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

}
