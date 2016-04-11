package com.example.robien.beachbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robien on 3/18/2016.
 */
public class MemberAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public MemberAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(String object) {
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
        MemberHolder memberHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.member_row_layout, parent, false);
            memberHolder = new MemberHolder();
            memberHolder.tx_groupName = (TextView) row.findViewById(R.id.memberName);

            row.setTag(memberHolder);
        } else {
            memberHolder = (MemberHolder) row.getTag();
        }

        String email = this.getItem(position).toString();
        memberHolder.tx_groupName.setText(email);

        return row;
    }

    static class MemberHolder {
        TextView tx_groupName;
    }
}
