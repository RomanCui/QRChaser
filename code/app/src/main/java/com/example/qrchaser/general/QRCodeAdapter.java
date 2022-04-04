package com.example.qrchaser.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.qrchaser.R;
import com.example.qrchaser.oop.QRCode;
import java.util.ArrayList;

/**
 * This Adapter Class is used for listview in MyQRCodeScreen and BrowseQRCodesScreen
 */
public class QRCodeAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrCodes;
    private Context context;

    public QRCodeAdapter(Context context, ArrayList<QRCode> qrCodes) {
        super(context, 0,qrCodes);
        this.qrCodes = qrCodes;
        this.context = context;
    } // end QRCodeAdapter Constructor

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qrcode_content, parent,false);
        }

        QRCode qrCode = qrCodes.get(position);

        // Assign values to views in qrcode_content
        TextView qrCodeName = view.findViewById(R.id.name_text);
        TextView qrCodeScore = view.findViewById(R.id.score_text);
        qrCodeName.setText(qrCode.getName());
        qrCodeScore.setText(qrCode.getScore() + "");

        return view;
    } // end getView

} // end QRCodeAdapter Class
