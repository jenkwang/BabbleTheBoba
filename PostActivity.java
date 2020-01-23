package com.example.jenkwang.babbletheboba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    Button buttonAddReply, buttonUp, buttonDown;
    EditText editTextReply;
    TextView textViewScore, textViewTitle, textViewContent;
    ListView listViewReplies;

    DatabaseReference dbReplies, parentScore;

    List<Reply> replies;

    //variables for updating post score
    String score;
    int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();

        String id = intent.getStringExtra(MainActivity.POST_ID);
        score = intent.getStringExtra(MainActivity.POST_SCORE);
        dbReplies = FirebaseDatabase.getInstance().getReference().child("Message").child(id).child("Reply");
        parentScore = FirebaseDatabase.getInstance().getReference().child("Message").child(id).child("score");

        buttonAddReply = (Button) findViewById(R.id.buttonAddReply);
        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        editTextReply = (EditText) findViewById(R.id.editTextReply);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        listViewReplies = (ListView) findViewById(R.id.listViewReplies);
        textViewContent = (TextView) findViewById(R.id.textViewContent);

        replies = new ArrayList<>();

        //will keep score updated and makes sure it doesn't go back to 0 when
        //you go back to main activity
        textViewTitle.setText(intent.getStringExtra(MainActivity.POST_TITLE));
        textViewContent.setText(intent.getStringExtra(MainActivity.POST_CONTENT));
        textViewScore.setText(score);

        buttonAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReply();
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = Integer.parseInt(textViewScore.getText().toString().trim());
                s++;
                parentScore.setValue(s);
                textViewScore.setText("" + s);
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = Integer.parseInt(textViewScore.getText().toString().trim());
                s--;
                parentScore.setValue(s);
                textViewScore.setText("" + s);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbReplies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                replies.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reply reply = postSnapshot.getValue(Reply.class);
                    replies.add(reply);
                }
                ReplyList replyListAdapter = new ReplyList(PostActivity.this, replies);
                listViewReplies.setAdapter(replyListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveReply() {
        String response = editTextReply.getText().toString().trim();
        if (!TextUtils.isEmpty(response)) {
            String id  = dbReplies.push().getKey();
            Reply reply = new Reply(id, response);
            if (id != null) {
                dbReplies.child(id).setValue(reply);
            }
            Toast.makeText(this, "Response saved", Toast.LENGTH_LONG).show();
            editTextReply.setText("");
        } else {
            Toast.makeText(this, "Please enter response", Toast.LENGTH_LONG).show();
        }
    }


}
