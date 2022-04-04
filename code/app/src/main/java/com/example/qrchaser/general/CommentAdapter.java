package com.example.qrchaser.general;

import static com.example.qrchaser.general.SaveANDLoad.loadData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrchaser.R;
import com.example.qrchaser.oop.Comments;
import com.example.qrchaser.oop.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comments> {
    private ArrayList<Comments> comments;
    private Context context;
    private final String TAG = "Error";
    private FirebaseFirestore db;
    private Player currentPlayer;

    public CommentAdapter(@NonNull Context context, int resource, ArrayList<Comments> comments) {
        super(context, resource, comments);
        this.comments = comments;
        this.context = context;
    } // end CommentAdapter Constructor

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comments_content, parent,false);
        }

        Comments comment = comments.get(position);
        TextView username = view.findViewById(R.id.comment_username_textView);
        TextView commentTV = view.findViewById(R.id.comment_string_textView);

        // Initialize database Access
        db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection("Accounts");
        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        // Get the desired player id in order to load the data from the database
        String currentPlayerID = comment.getUsername();
        DocumentReference myAccount = accountsRef.document(currentPlayerID);
        // Get the document, forcing the SDK to use the offline cache
        myAccount.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    currentPlayer = document.toObject(Player.class);
                    username.setText(currentPlayer.getNickname());
                    commentTV.setText(comment.getComment());

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }); // end addOnCompleteListener

        return view;
    } // end getView

} // end CommentAdapter Class
