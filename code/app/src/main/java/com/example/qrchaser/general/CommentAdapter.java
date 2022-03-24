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

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comments> {

    private ArrayList<Comments> comments;
    private Context context;

    public CommentAdapter(@NonNull Context context, int resource, ArrayList<Comments> comments) {
        super(context, resource, comments);
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comments_content,parent,false);
        }

        Comments comment = comments.get(position);
        TextView username = view.findViewById(R.id.comment_username_textView);
        TextView commentTV = view.findViewById(R.id.comment_string_textView);

        username.setText(comment.getUsername());
        commentTV.setText(comment.getComment());

        return view;
    }
}
