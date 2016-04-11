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
public class GroupAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public GroupAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Group mGroup) {
        super.add(mGroup);
        list.add(mGroup);
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
        GroupHolder groupHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.group_row_layout, parent, false);
            groupHolder = new GroupHolder();
            groupHolder.tx_groupName = (TextView)row.findViewById(R.id.groupRowName);
            groupHolder.tx_groupID = (TextView)row.findViewById(R.id.groupRowID);

            row.setTag(groupHolder);
        }
        else {
            groupHolder = (GroupHolder)row.getTag();
        }

        Group myGroup = (Group)this.getItem(position);
        groupHolder.tx_groupName.setText(myGroup.getGroupType());
        groupHolder.tx_groupID.setText(" " + myGroup.getGroupID() + " Group");
        return row;
    }

    static class GroupHolder {
        TextView tx_groupName, tx_groupID;
    }
}