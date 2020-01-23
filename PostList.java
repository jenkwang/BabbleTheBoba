package com.example.jenkwang.babbletheboba;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class PostList extends ArrayAdapter<Message> {
    private Activity context;
    List<Message> posts;

    public PostList(Activity context, List<Message> posts) {
        super(context, R.layout.list_item, posts);
        this.context = context;
        this.posts = posts;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_item, parent, false); //null, true

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.TextViewtitle);
        TextView textViewContent = (TextView) listViewItem.findViewById(R.id.TextViewcontent);

        Message post = posts.get(position);
        textViewTitle.setText(post.getTitle());
        textViewContent.setText(post.getContent());

        return listViewItem;
    }
}

