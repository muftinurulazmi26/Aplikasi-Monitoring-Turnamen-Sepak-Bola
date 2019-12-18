package com.mffa.dev.issbid.LiveBroadcaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mffa.dev.issbid.DetailVidActivity;
import com.mffa.dev.issbid.Model.ConstantApp;
import com.mffa.dev.issbid.R;

import io.agora.rtc.Constants;

public class LiveMainActivity extends BaseActivity {
    private String nmPertnd,urlvid,tgl,status;
    private ImageView imgthumble;
    private TextView txtnmpertnd,txttgl;
    ProgressBar progressbar;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_main);
        MobileAds.initialize(this, "ca-app-pub-6842538229766974/3160812788");

        nmPertnd = getIntent().getStringExtra("nm_pertandingan");
        urlvid = getIntent().getStringExtra("urlvid");
        tgl = getIntent().getStringExtra("tgl");
        status = getIntent().getStringExtra("status");

        mAdView = findViewById(R.id.admob);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("TEST_EMULATOR")
                .build();
        mAdView.loadAd(adRequest);

        imgthumble = findViewById(R.id.imgthumble);
        txtnmpertnd = findViewById(R.id.txtnmpertnd);
        txttgl = findViewById(R.id.txttgl);
        progressbar = findViewById(R.id.progressbar);

        txtnmpertnd.setText(nmPertnd);
        txttgl.setText(tgl);

        imgthumble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailVidActivity.class);
                intent.putExtra("urlvid",urlvid);
                startActivity(intent);
            }
        });

        String thumb = "https://img.youtube.com/vi/"+urlvid+"/0.jpg";
        Glide.with(getApplicationContext()).load(thumb)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.error_server)
                .into(imgthumble);
    }

    @Override
    protected void initUIandEvent() {
        EditText textRoomName = (EditText) findViewById(R.id.room_name);
        textRoomName.setKeyListener(null);
        textRoomName.setEnabled(false);
        textRoomName.setText(nmPertnd);
    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                forwardToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickJoin(View view) {
        if (status.equals("Selesai")){
            Toast.makeText(getApplicationContext(),"Live Streaming sudah selesai",Toast.LENGTH_SHORT).show();
        } else {
            LiveMainActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_AUDIENCE);
        }
    }

    public void forwardToLiveRoom(int cRole) {
        final EditText v_room = (EditText) findViewById(R.id.room_name);
        v_room.setKeyListener(null);
        v_room.setEnabled(false);

        Intent i = new Intent(LiveMainActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, nmPertnd);

        startActivity(i);
    }

    public void forwardToSettings() {
        Intent i = new Intent(this, SettingActivity.class);
        startActivity(i);
    }
}
