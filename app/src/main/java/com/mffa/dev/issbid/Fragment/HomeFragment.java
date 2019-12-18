package com.mffa.dev.issbid.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mffa.dev.issbid.Api.ApiServices;
import com.mffa.dev.issbid.Api.RetrofitClient;
import com.mffa.dev.issbid.Common.Common;
import com.mffa.dev.issbid.Model.WeatherResponse;
import com.mffa.dev.issbid.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private TextView tv_JmlTim,tv_JmlPemain;
    private FirebaseFirestore db;
    private CollectionReference table_tim,table_player;
    private CardView cd_klasemen;
    private TextView tv_pagi,tv_desc,tv_loc,tv_int,tv_kelembaban,tv_kecepatan,tv_minmax,tv_barat;
    private ImageView img_bg,img_awan;
    CompositeDisposable compositeDisposable;
    ApiServices apiServices;
    private AlertDialog mAlertDialog;

    public HomeFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getClient();
        apiServices = retrofit.create(ApiServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("ISSB ID");

        mAlertDialog = new SpotsDialog(getActivity());
        mAlertDialog.show();

        db = FirebaseFirestore.getInstance();
        table_tim = db.collection("Tim");
        table_player = db.collection("Player");

        tv_JmlTim = view.findViewById(R.id.tv_jmlTim);
        tv_JmlPemain = view.findViewById(R.id.tv_jmlPemain);
        cd_klasemen = view.findViewById(R.id.cd_klasemen);
        tv_pagi = view.findViewById(R.id.tv_pagi);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_loc = view.findViewById(R.id.tv_loc);
        tv_int = view.findViewById(R.id.tv_int);
        tv_kelembaban = view.findViewById(R.id.tv_kelembaban);
        tv_kecepatan = view.findViewById(R.id.tv_kecepatan);
        tv_minmax = view.findViewById(R.id.tv_minmax);
        tv_barat = view.findViewById(R.id.tv_barat);
        img_bg = view.findViewById(R.id.img_bg);
        img_awan = view.findViewById(R.id.img_awan);

        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay <10){
            img_bg.setImageResource(R.drawable.sunrise);
            tv_pagi.setText("Pagi");
            tv_desc.setText("Awali aktivitasmu dengan senyuman.");
        }
        else if (timeOfDay >= 10 && timeOfDay < 15){
            img_bg.setImageResource(R.drawable.siang);
            tv_pagi.setText("Siang");
            tv_desc.setText("Mempelajari sejarah sangat mudah tetapi membuat sejarah sangat sulit. Buatlah sejarah tentang dirimu sendiri kemudian buat orang lain mempelajarinya.");
        }
        else if (timeOfDay >=15 && timeOfDay < 18){
            img_bg.setImageResource(R.drawable.sunrise);
            tv_pagi.setText("Sore");
            tv_desc.setText("Semoga sore harimu menyenangkan dan semua mimpimu menjadi kenyataan.");
        }
        else if (timeOfDay >= 18 && timeOfDay < 19){
            img_bg.setImageResource(R.drawable.night);
            tv_pagi.setText("Petang");
            tv_desc.setText("Biarkan kegagalan di sore hari menginspirasimu untuk menjadi lebih baik di pagi hari.");
        }
        else if (timeOfDay >= 19 && timeOfDay < 24){
            img_bg.setImageResource(R.drawable.goodnight);
            tv_pagi.setText("Malam");
            tv_desc.setText("Meskipun aku tidak bersamamu, aku harap malammu sama indahnya seperti saat kita bersama.");
        }

        table_tim.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                        tv_JmlTim.setText(String.valueOf(count));
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });

        table_player.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                        tv_JmlPemain.setText(String.valueOf(count));
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });

        cd_klasemen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                fragment = new KlasemenFragment();
                ft.addToBackStack(null);
                ft.replace(R.id.frame_content,fragment).commit();
            }
        });

        getWeatherInformation();

        return view;
    }

    private void getWeatherInformation() {

        compositeDisposable.add(apiServices.getWeatherbyLatLang(String.valueOf(Common.latitude),
                String.valueOf(Common.longitude),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Exception {
                        mAlertDialog.dismiss();
                        Picasso.with(getActivity()).load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResponse.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_awan);

                        tv_loc.setText(weatherResponse.getName());
                        tv_int.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getTemp())).toString());
                        tv_barat.setText(new StringBuilder(String.valueOf(weatherResponse.getWeather().get(0).getDescription())).toString());
                        tv_minmax.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getTempMin())).append("/").append(weatherResponse.getMain().getTempMax()).toString());
                        tv_kecepatan.setText(new StringBuilder(String.valueOf(weatherResponse.getWind().getSpeed())).append(" m/s").toString());
                        tv_kelembaban.setText(new StringBuilder(String.valueOf("Kelembaban "+weatherResponse.getMain().getHumidity())).append("%").toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

}
