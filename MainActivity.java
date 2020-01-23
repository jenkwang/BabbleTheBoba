package com.example.jenkwang.babbletheboba;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String POST_TITLE = "posttitle";
    public static final String POST_CONTENT = "postcontent";
    public static final String POST_ID = "postid";
    public static final String POST_SCORE = "postscore";

    //view objects
    EditText editTextTitle;
    EditText editTextContent;
    Button buttonAddPost;
    ListView listViewPosts;

    //a list to store all posts from database
    List<Message> posts;

    //database reference object
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference for Message (post)
        db = FirebaseDatabase.getInstance().getReference().child("Message");

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextContent = (EditText) findViewById(R.id.editTextContent);
        listViewPosts = (ListView) findViewById(R.id.listViewPosts);

        buttonAddPost = (Button) findViewById(R.id.buttonAddPost);

        posts = new ArrayList<>();

        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message post = posts.get(i);

                Intent intent = new Intent(MainActivity.this, PostActivity.class);

                intent.putExtra(POST_ID, post.getPostid());
                intent.putExtra(POST_TITLE, post.getTitle());
                intent.putExtra(POST_CONTENT, post.getContent());
                String score = "" + post.getScore();
                intent.putExtra(POST_SCORE, score);

                //opens up new activity
                startActivity(intent);
            }
        });

        //when post entry is held on you can delete it
        listViewPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message post = posts.get(i);
                showDeleteDialog(post.getPostid(), post.getTitle());
                return true;
            }
        });
    }

    private void addPost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (!TextUtils.isEmpty(title)) {
            String id = db.push().getKey();

            Message post = new Message(id, title, content, 0);

            //Saving the Post
            if (id != null) {
                //db.setValue(post);
                db.child(id).setValue(post);
            }

            //setting edittext to blank again
            editTextTitle.setText("");
            editTextContent.setText("");

            Toast.makeText(this, "Post added", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous post list
                posts.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message post = postSnapshot.getValue(Message.class);
                    posts.add(post);
                }

                //sorts posts by score
                Collections.sort(posts);

                //creating adapter
                PostList postAdapter = new PostList(MainActivity.this, posts);
                //attaching adapter to the listview
                listViewPosts.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //delete dialog for when user long clicks a post entry
    private void showDeleteDialog(final String postId, String postTitle) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle(postTitle);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        //cancel button if user changes mind
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        //deletes the post and all the replies
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(postId);
                b.dismiss();
            }
        });
    }

    private boolean deletePost(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Message").child(id);

        //removes post
        dR.removeValue();

        DatabaseReference drReplies = FirebaseDatabase.getInstance().getReference("Reply").child(id);

        //removes all replies
        drReplies.removeValue();
        Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
}