package com.example.jenkwang.babbletheboba;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReplyList extends ArrayAdapter<Reply> {
    private Activity context;
    List<Reply> replies;

    public ReplyList(Activity context, List<Reply> replies) {
        super(context, R.layout.list_item, replies);
        this.context = context;
        this.replies = replies;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_item, null, true);

        TextView textViewReply = (TextView) listViewItem.findViewById(R.id.TextViewcontent);
        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.TextViewtitle);

        Reply reply = replies.get(position);
        textViewReply.setText(reply.getResponse());
        textViewTitle.setText("");

        return listViewItem;
    }
}

