package com.example.qrchaser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
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

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
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

public class MapActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private CompassOverlay mapCompassOverlay;
    private MyLocationNewOverlay myLocationOverlay;
    private RotationGestureOverlay mapRotationGestureOverlay;
    private ScaleBarOverlay mapScaleBarOverlay;
    private Button button1,button2,button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle permissions first, before map is created. Is this done?

        // Load/initialize the osmdroid configuration, this can be done
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        // Inflate and create the map
        setContentView(R.layout.activity_map);

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

        // Enable Compass Overlay
        this.mapCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), map);
        this.mapCompassOverlay.enableCompass();
        map.getOverlays().add(this.mapCompassOverlay);

        // Enable My Location overlay
        this.myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),map);
        this.myLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.myLocationOverlay);


        // Attempt to set to current location
        // *****************I Am Unable to test this right now, my emulator always gives null**************************
        GeoPoint myPoint = this.myLocationOverlay.getMyLocation();
//        Log.d("lat", "" + myPoint.getLatitude());
//        Log.d("long", "" + myPoint.getLongitude());
        if (myPoint == null) {
            // Since no current location exist, attempt with last known location
            Location location = this.myLocationOverlay.getLastFix();
            if (location != null) {
                myPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            } else {
                // Since no current nor last-know location exist, default to 0, 0!
                myPoint =  new GeoPoint(0.0, 0.0);
            }
        }
        mapController.setCenter(myPoint);

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

        // *************************** Test Points ************************

        // Create 10k labelled points, in most cases, there will be no problems of displaying >100k points
        List<IGeoPoint> points = new ArrayList<>();
        // This is where you need to import all of the QR codes from the database:
        // The Latitude, Longitude, and ID are all needed
        for (int i = 0; i < 10000; i++) {
            points.add(new LabelledGeoPoint(37 + Math.random() * 5, -8 + Math.random() * 5, "Point #" + i));
        }

        // Wrap them in a theme
        SimplePointTheme pointTheme = new SimplePointTheme(points, true);

        // Create label style
        Paint textStyle = new Paint();
        textStyle.setStyle(Paint.Style.FILL);
        textStyle.setColor(Color.parseColor("#0000ff"));
        textStyle.setTextAlign(Paint.Align.CENTER);
        textStyle.setTextSize(24);

        // Set some visual options for the overlay
        // Use MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
        SimpleFastPointOverlayOptions pointOverlayOptions = SimpleFastPointOverlayOptions.getDefaultStyle()
                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                .setRadius(7).setIsClickable(true).setCellSize(15).setTextStyle(textStyle);

        // Create the overlay with the theme
        final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pointTheme, pointOverlayOptions);

        // onClick callback
        sfpo.setOnClickListener(new SimpleFastPointOverlay.OnClickListener() {
            @Override
            public void onClick(SimpleFastPointOverlay.PointAdapter points, Integer point) {
                // Will Bring up the QRCode Info here**
                Toast.makeText(map.getContext()
                        , "You clicked " + ((LabelledGeoPoint) points.get(point)).getLabel()
                        , Toast.LENGTH_SHORT).show();
            } // end onClick
        }); // end sfpo.setOnClickListener

        // Add overlay
        map.getOverlays().add(sfpo);

        // ************************** Page Selection ****************************************
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button4 = findViewById(R.id.button4);

        // Head to My QR Code Screen
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MyQRCodeScreenActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button1.setOnClickListener

        // Head to Browse Players
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button2.setOnClickListener

        // Head to Player Profile
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, PlayerProfileActivity.class);
                startActivity(intent);
                finish();
            } // end onClick
        }); // end button4.setOnClickListener
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
} // end MapActivity Class