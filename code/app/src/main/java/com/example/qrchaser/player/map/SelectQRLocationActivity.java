package com.example.qrchaser.player.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrchaser.R;
import com.example.qrchaser.logIn.WelcomeActivity;
import com.example.qrchaser.player.CameraScannerActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class SelectQRLocationActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private CompassOverlay mapCompassOverlay;
    private MyLocationNewOverlay myLocationOverlay;
    private RotationGestureOverlay mapRotationGestureOverlay;
    private ScaleBarOverlay mapScaleBarOverlay;
    private GeoPoint QRLocation;
    private Marker QRLocationMarker = null;
    private LocationManager locationManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        // Inflate and create the map
        setContentView(R.layout.activity_select_qr_location_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        // Set default map zoom
        IMapController mapController = map.getController();
        mapController.setZoom(22.0);

        // Add touch Zoom Control
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);

        // Enable Compass Overlay
        this.mapCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), map);
        this.mapCompassOverlay.enableCompass();
        map.getOverlays().add(this.mapCompassOverlay);

        // Enable My Location overlay
        this.myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),map);
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

        // Attempt to set to current location
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint myPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        boolean setPoint = false;
        if (myPoint != null) {
            setPoint = true;
            mapController.setCenter(myPoint);
        }

        if (setPoint) {
            QRLocation = myPoint;
            // Add Marker
            QRLocationMarker = new Marker(map);
            QRLocationMarker.setId("QRLocation");
            QRLocationMarker.setPosition(QRLocation);
            QRLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(QRLocationMarker);
        } else {
            // Set to out of bounds
            QRLocation = new GeoPoint(200.0, 200.0);
        }
//            Toast.makeText(getBaseContext(),"yes", Toast.LENGTH_LONG).show();
        // Setup Click For location (if the decide to choose a new location)
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // Remove old marker
                for(int i = 0; i < map.getOverlays().size(); i++){
                    Overlay overlay = map.getOverlays().get(i);
                    if(overlay instanceof Marker && ((Marker) overlay).getId().equals("QRLocation")){
                        map.getOverlays().remove(overlay);
                    }
                }
                //Set new one
                QRLocation = p;
                QRLocationMarker = new Marker(map);
                QRLocationMarker.setId("QRLocation");
                QRLocationMarker.setPosition(QRLocation);
                QRLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(QRLocationMarker);
                return false;
            } // end singleTapConfirmedHelper
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            } // end longPressHelper
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));

        // Buttons
        final Button confirm = findViewById(R.id.button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrLocationIntent = new Intent();
                qrLocationIntent.putExtra("latitude", QRLocation.getLatitude());
                qrLocationIntent.putExtra("longitude", QRLocation.getLongitude());
                setResult(Activity.RESULT_OK, qrLocationIntent);
                finish();
            } // end onClick
        }); // end qrCode.setOnClickListener

        final Button cancel = findViewById(R.id.button_back);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // end onClick
        }); // end qrCode.setOnClickListener

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

    private void requestPermissionsIfNecessary(String[] permissions) {
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
} // end SelectQRLocationActivity class
