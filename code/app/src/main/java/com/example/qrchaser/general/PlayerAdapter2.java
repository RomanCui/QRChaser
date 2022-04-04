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
import com.example.qrchaser.oop.Player;

import java.util.ArrayList;

/**
 * This Adapter Class is used for listview in the BrowsePlayersScreen (getTotalScore())
 */
public class PlayerAdapter2 extends ArrayAdapter<Player> {
    private ArrayList<Player> players;
    private Context context;

    public PlayerAdapter2(Context context, ArrayList<Player> players) {
        super(context, 0, players);
        this.players = players;
        this.context = context;
    } // end PlayerAdapter2 Constructor

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qrcode_content, parent,false);
        }

        Player player = players.get(position);

        // Assign values to views in qrcode_content
        TextView qrCodeName = view.findViewById(R.id.name_text);
        TextView qrCodeScore = view.findViewById(R.id.score_text);
        qrCodeName.setText(player.getNickname());
        qrCodeScore.setText(String.valueOf(player.getTotalScore()));

        return view;
    } // end getView

} // end PlayerAdapter2 Class
