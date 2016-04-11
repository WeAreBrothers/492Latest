package com.example.robien.beachbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DarthMerl on 4/7/2016.
 */

/**
 * Created by Robien on 3/18/2016.
 */
public class MsgAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public MsgAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Message object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        MsgHolder msgHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.message_view_layout, parent, false);
            msgHolder = new MsgHolder();
            msgHolder.tx_name = (TextView)row.findViewById(R.id.tx_name);


            row.setTag(msgHolder);
        }
        else {
            msgHolder = (MsgHolder)row.getTag();
        }

        Message msg = (Message)this.getItem(position);
        msgHolder.tx_name.setText(msg.getSender());

        return row;
    }

    static class MsgHolder {
        TextView tx_name;
    }
}

