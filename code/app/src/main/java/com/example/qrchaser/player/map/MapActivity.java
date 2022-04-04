package com.example.qrchaser.player.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrchaser.QRCodeInfoActivity;
import com.example.qrchaser.R;
import com.example.qrchaser.player.browse.BrowseQRActivity;
import com.example.qrchaser.player.myQRCodes.MyQRCodeScreenActivity;
import com.example.qrchaser.player.profile.PlayerProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This Activity Class allows the user to view QR Codes in a Map
 */
public class MapActivity extends AppCompatActivity {
    // UI
    private BottomNavigationView bottomNavigationView;
    // Permissions
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    // Map & Map Overlays
    private MapView map = null;
    private CompassOverlay mapCompassOverlay;
    private MyLocationNewOverlay myLocationOverlay;
    private RotationGestureOverlay mapRotationGestureOverlay;
    private ScaleBarOverlay mapScaleBarOverlay;
    // Database
    private final String TAG = "Sample";
    private FirebaseFirestore db;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle permissions first, before map is created. Is this done? -> I think so

        // Load/initialize the osmdroid configuration, this can be done
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        // setting this before the layout is inflated is a good idea
        // it 'should' ensure that the map has a writable location for the map cache, even without permissions
        // if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        // see also StorageUtils
        // note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        // tile servers will get you banned based on this string

        // Inflate and create the map
        setContentView(R.layout.activity_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        // Set default map zoom
        IMapController mapController = map.getController();
        mapController.setZoom(22.0);
        map.setMinZoomLevel(4.0);

        // Add touch Zoom Control
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);

        // Enable Compass Overlay
        this.mapCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), map);
        this.mapCompassOverlay.enableCompass();
        map.getOverlays().add(this.mapCompassOverlay);

        // Enable My Location overlay
        this.myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        this.myLocationOverlay.enableMyLocation();
        this.myLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.myLocationOverlay);

        // Enable rotation gestures
        mapRotationGestureOverlay = new RotationGestureOverlay(map);
        mapRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(this.mapRotationGestureOverlay);

        // Enable Map Scale bar overlay
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mapScaleBarOverlay = new ScaleBarOverlay(map);
        mapScaleBarOverlay.setCentred(true);
        // Play around with these values to get the location on screen in the right place for your application
        mapScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(this.mapScaleBarOverlay);

        List<IGeoPoint> points = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        // Get a top level reference to the collection
        final CollectionReference QRCodesReference = db.collection("QRCodes");

        db.collection("QRCodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                try {
                                    String tempID = document.getString("hash");
                                    Double tempLat = document.getDouble("latitude");
                                    Double tempLon = document.getDouble("longitude");
                                    if (tempLat < 200 && tempLon < 200) {
                                        points.add(new LabelledGeoPoint(tempLat, tempLon, tempID));
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    } // end onComplete
                }); // db.collection("QRCodes").get().addOnCompleteListener

        // *************************** Test Points ************************
        // Create 10k labelled points, in most cases, there will be no problems of displaying >100k points
//        for (int i = 0; i < 10000; i++) {
//            points.add(new LabelledGeoPoint(37 + Math.random() * 5, -8 + Math.random() * 5, "Point #" + i));
//        }
        // *************************** End Test Points ************************

        // Wrap them in a theme
        SimplePointTheme pointTheme = new SimplePointTheme(points, true);

        // Create label style (Hidden)
        Paint textStyle = new Paint();
        textStyle.setStyle(Paint.Style.FILL);
        textStyle.setColor(Color.parseColor("#000000"));
        textStyle.setTextAlign(Paint.Align.CENTER);
        textStyle.setTextSize(0);

        // Create Point style
        Paint pointStyle = new Paint();
        pointStyle.setStyle(Paint.Style.FILL);
        pointStyle.setColor(Color.parseColor("#e94335"));
        // Set some visual options for the overlay
        SimpleFastPointOverlayOptions pointOverlayOptions = SimpleFastPointOverlayOptions.getDefaultStyle()
                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MEDIUM_OPTIMIZATION)
                .setRadius(15).setIsClickable(true).setCellSize(5).setTextStyle(textStyle).setPointStyle(pointStyle).setSymbol(SimpleFastPointOverlayOptions.Shape.CIRCLE);

        // Create the overlay with the theme
        final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pointTheme, pointOverlayOptions);

        // onClick callback
        sfpo.setOnClickListener(new SimpleFastPointOverlay.OnClickListener() {
            @Override
            public void onClick(SimpleFastPointOverlay.PointAdapter points, Integer point) {
                Intent intent = new Intent(MapActivity.this, QRCodeInfoActivity.class);
                intent.putExtra("qrHash", ((LabelledGeoPoint) points.get(point)).getLabel());
                startActivity(intent);
            } // end onClick
        }); // end sfpo.setOnClickListener

        // Add overlay
        map.getOverlays().add(sfpo);

        // Attempt to set to current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getApplication().getMainExecutor(), new Consumer<Location>() {
                @Override
                public void accept(Location location) {
                    GeoPoint myPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    if (myPoint != null) {
                        mapController.setCenter(myPoint);
                    }
                }
            });
        } else {
            GeoPoint myPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            if (myPoint != null){
                mapController.setCenter(myPoint);
            }
        }


        // ************************** Page Selection ****************************************
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.map);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_qr_code:
                        startActivity(new Intent(getApplicationContext(),MyQRCodeScreenActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.browse_qr:
                        startActivity(new Intent(getApplicationContext(), BrowseQRActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.self_profile:
                        startActivity(new Intent(getApplicationContext(),PlayerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            } // end onNavigationItemSelected
        }); // end  bottomNavigationView.setOnItemSelectedListener
    } // end onCreate

    @Override
    public void onResume() {
        super.onResume();
        // This will refresh the osmdroid configuration on resuming.
        // If you make changes to the configuration, use
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume();
    } // end onResume

    @Override
    public void onPause() {
        super.onPause();
        // This will refresh the osmdroid configuration on resuming.
        // If you make changes to the configuration, use
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Configuration.getInstance().save(this, prefs);
        map.onPause();
    } // end onPause

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    } // end onRequestPermissionsResult

    public void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    } // end requestPermissionsIfNecessary

} // end MapActivity Class
