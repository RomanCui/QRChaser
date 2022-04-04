package com.example.qrchaser.player;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrchaser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraScannerActivity extends AppCompatActivity implements CameraXConfig.Provider {
    // Permissions
    private static final int CAMERA_REQUEST_CODE = 1;
    // UI
    private PreviewView cameraView;
    private TextView valueText;
    // General Data
    private String value;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_camera_screen);
        cameraView = findViewById(R.id.cameraView);
        valueText = findViewById(R.id.qrValueTextView);

        // Check permission
        if(checkCameraPermission()) {
            Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show();
            // Start camera
            launchCamera();
        } else {
            // Request permission
            requestCameraPermission();
        }
    } // end onCreate

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (checkCameraPermission()) {
                Toast.makeText(this,"Camera Permission Granted ", Toast.LENGTH_LONG).show();
                // Start camera
                launchCamera();
            } else {
                // Request permission
                Toast.makeText(this,"No Camera Permission", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    } // end onRequestPermissionsResult

    // Check camera permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Boolean checkCameraPermission() {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    } // end checkCameraPermission

    // Request camera permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    } // end requestCameraPermission

    private void launchCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(CameraScannerActivity.this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // TODO: display proper error handling
            }
        }, ContextCompat.getMainExecutor(this));
    } // end launchCamera

    private Camera bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Bind camera view
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());

        // Bind scanner analysis
        BarcodeScannerOptions qrFormats = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(qrFormats);

        ImageAnalysis qrAnalyser = new ImageAnalysis.Builder().build();

//        qrAnalyser.setAnalyzer(
//                Executors.newSingleThreadExecutor(),
//                { imageProxy -> processImageProxy(scanner, imageProxy) }
//        );

        qrAnalyser.setAnalyzer(Executors.newSingleThreadExecutor(), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                @SuppressLint("UnsafeOptInUsageError")
                InputImage inputImage = InputImage.fromMediaImage(
                        image.getImage(),
                        image.getImageInfo().getRotationDegrees()
                );

                scanner.process(inputImage)
                        .addOnSuccessListener( barcodes -> {
                            if(!barcodes.isEmpty()) {
                                Barcode barcode = barcodes.get(0); // Check if not null
                                value = barcode.getRawValue();
                                valueText.setText(value);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {
                        image.close();
                        if(value != null) {
                            Intent qrValueIntent = new Intent();
                            qrValueIntent.putExtra("qrValue", value);
                            setResult(Activity.RESULT_OK, qrValueIntent);
                            finish();
                        }
                    }
                });
            }
        });
        return cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, qrAnalyser);
    } // end bindPreview

    // TODO: Might not need this, try testing without this method
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    } // end CameraXConfig

} // end CameraScannerActivity Class
