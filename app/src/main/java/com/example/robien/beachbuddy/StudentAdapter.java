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
 * Created by Robien on 3/18/2016.
 */
public class StudentAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public StudentAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Student object) {
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
        StudentHolder studentHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.student_row_layout, parent, false);
            studentHolder = new StudentHolder();
            studentHolder.tx_name = (TextView)row.findViewById(R.id.tx_name);
            studentHolder.tx_className = (TextView)row.findViewById(R.id.tx_cName);
            studentHolder.tx_classNum = (TextView)row.findViewById(R.id.tx_cNum);
            studentHolder.tx_instructor = (TextView)row.findViewById(R.id.tx_instructor);

            row.setTag(studentHolder);
        }
        else {
            studentHolder = (StudentHolder)row.getTag();
        }

        Student student = (Student)this.getItem(position);
        studentHolder.tx_name.setText(student.getName());
        studentHolder.tx_instructor.setText(student.getInstructor());
        studentHolder.tx_className.setText(student.getClassName());
        studentHolder.tx_classNum.setText(student.getClassNum());
        return row;
    }

    static class StudentHolder {
        TextView tx_name, tx_className, tx_classNum, tx_instructor;
    }
}
