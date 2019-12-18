package com.mffa.dev.issbid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mffa.dev.issbid.Common.Common;
import com.mffa.dev.issbid.Fragment.HomeFragment;
import com.mffa.dev.issbid.Fragment.LastMatchFragment;
import com.mffa.dev.issbid.Fragment.ManagerFragment;
import com.mffa.dev.issbid.Fragment.MatchFragment;
import com.mffa.dev.issbid.Fragment.NextMatchFragment;
import com.mffa.dev.issbid.Fragment.PlayerFragment;
import com.mffa.dev.issbid.Fragment.TimFragment;
import com.mffa.dev.issbid.Model.Ads;

import java.util.HashMap;
import java.util.List;

public class ActivityUtama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForceUpdateChecker.OnUpdateNeededListener, LocationListener {
    DrawerLayout drawer;
    private Target homeTarget;
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    private FirebaseFirestore db;
    private CollectionReference table_ads;
    private ImageView img_iklan;
    private TextView btnclose;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (ActivityCompat.checkSelfPermission(ActivityUtama.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityUtama.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ActivityUtama.this);
                            //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

        db = FirebaseFirestore.getInstance();
        table_ads = db.collection("Ads");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("is_force_update", false);
        remoteConfig.setDefaults(defaults);

        final Task<Void> fetch = remoteConfig.fetch(0);
        fetch.addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig.activateFetched();
                    if (remoteConfig.getBoolean("is_force_update")) {
                        showDialogUpdate();
                    }
                } else {
                    Toast.makeText(ActivityUtama.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        homeTarget = new Target() {
            @Override
            public Point getPoint() {
                // Get approximate position of home icon's center
                int actionBarSize = toolbar.getHeight();
                int x = actionBarSize / 2;
                int y = actionBarSize / 2;
                return new Point(x, y);
            }
        };

        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setContentTitle("Menu Utama")
                .setContentText("Akses berbagai fitur di menu utama ini.")
                .setTarget(homeTarget)
                .setStyle(R.style.CustomShowcaseTheme3)
                .singleShot(42)
                .build();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        DocumentReference docRef = table_ads.document("ads");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Ads item = document.toObject(Ads.class);
                        if (item.getUrlImage().isEmpty()){

                        } else {
                            sDialogAds(item);
                        }

                    } else {
                        //Log.d(TAG, "No such document");

                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        getLocation();
    }

    private void sDialogAds(Ads item) {
        final android.app.AlertDialog popupform = new android.app.AlertDialog.Builder(ActivityUtama.this).create();

        LayoutInflater inflater = this.getLayoutInflater();
        View adsItem = inflater.inflate(R.layout.dialog_iklan,null);

        img_iklan = adsItem.findViewById(R.id.img_iklan);
        btnclose = adsItem.findViewById(R.id.btnclose);
        progressbar = adsItem.findViewById(R.id.progressbar);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupform.dismiss();
            }
        });

        Glide.with(ActivityUtama.this).load(item.getUrlImage())
                .error(R.drawable.error_server)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_iklan);

        popupform.setView(adsItem);
        popupform.show();
    }

    private void getLocation() {
        if (isLocationEnabled(ActivityUtama.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Common.latitude = latitude;
                Common.longitude = longitude;
                //Toast.makeText(ActivityUtama.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

    private boolean isLocationEnabled(ActivityUtama activityUtama) {
        return true;
    }

    private void showDialogUpdate() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, Update your app to the newest version.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent urlApps = new Intent(Intent.ACTION_VIEW);
                                urlApps.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                                startActivity(urlApps);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.tim:
                fragment = new TimFragment();
                break;
            case R.id.player:
                fragment = new PlayerFragment();
                break;
            case R.id.match:
                fragment = new MatchFragment();
                break;
            case R.id.nextmatch:
                fragment = new NextMatchFragment();
                break;
            case R.id.lastmatch:
                fragment = new LastMatchFragment();
                break;
            case R.id.manager:
                fragment = new ManagerFragment();
                break;
            case R.id.privasi:
                Intent privacy = new Intent(Intent.ACTION_VIEW);
                privacy.setData(Uri.parse("https://deliscakrawala.blogspot.com/p/privacy-policy-mffa-dev-built-issb-id.html"));
                startActivity(privacy);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //Toast.makeText(ActivityUtama.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }
}
