package com.example.qrchaser.player.profile;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrchaser.R;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/* Resource Used:
From: geeksforgeeks.org
URL: https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
Author: https://auth.geeksforgeeks.org/user/chaitanyamunje
 */

public class GeneratedQRCodeActivity extends AppCompatActivity {
    private ImageView qrCodeIV;
    private String QRData = "Something Went Wrong";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_qrcode);
        QRData = getIntent().getStringExtra("qrData");

        qrCodeIV = findViewById(R.id.idIVQrcode);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();

        // The point which is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        // Generate dimension from width and height.
        int dimension = width < height ? width : height;
        dimension = dimension * 3 / 4;

        qrgEncoder = new QRGEncoder(QRData, null, QRGContents.Type.TEXT, dimension);
        try {
            // Get the qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }
    } // end onCreate
} // end GeneratedQRCodeActivity Class