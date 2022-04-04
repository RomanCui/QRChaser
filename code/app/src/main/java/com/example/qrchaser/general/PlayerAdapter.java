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
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.Player;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> players;
    private Context context;

    public PlayerAdapter(@NonNull Context context, int resource, ArrayList<Player> players) {
        super(context, resource, players);
        this.players = players;
        this.context = context;
    } // end PlayerAdapter Constructor

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.players_content, parent,false);
        }

        Player player = players.get(position);
        TextView ownerNumber = view.findViewById(R.id.player_string_textView);
        TextView username = view.findViewById(R.id.player_string_textView1);

        ownerNumber.setText("Owner " + (position + 1) +": ");
        username.setText(player.getNickname());
        return view;
    } // end getView

} // end PlayerAdapter Class
