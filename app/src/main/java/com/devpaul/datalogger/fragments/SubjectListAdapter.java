package com.devpaul.datalogger.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devpaul.circulartextview.CircularTextView;
import com.devpaul.datalogger.R;
import com.devpaul.datalogger.data.Subject;

import java.util.List;

/**
 * Created by Pauly D on 3/17/2015.
 */
public class SubjectListAdapter extends BaseAdapter {

    private static class MyViewHolder{
        TextView subjectAge;
        TextView subjectNumber;
        TextView subjectHeight;
        TextView subjectWeight;
        TextView subjectCategory;
        CircularTextView subjectCircleText;
    }

    private MyViewHolder viewHolder;
    private List<Subject> subjects;
    private LayoutInflater inflater;
    private Context context;

    public SubjectListAdapter(Context context, List<Subject> s) {
        this.subjects = s;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Subject getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Subject s) {
        subjects.add(s);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subject s = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.subject_list_item, parent, false);
            viewHolder = new MyViewHolder();
            viewHolder.subjectAge = (TextView) convertView.findViewById(R.id.subject_age);
            viewHolder.subjectCategory = (TextView) convertView.findViewById(R.id.subject_category);
            viewHolder.subjectHeight = (TextView) convertView.findViewById(R.id.subject_height);
            viewHolder.subjectWeight = (TextView) convertView.findViewById(R.id.subject_weight);
            viewHolder.subjectNumber = (TextView) convertView.findViewById(R.id.subject_title);
            viewHolder.subjectCircleText = (CircularTextView) convertView.findViewById(R.id.circluar_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.subjectAge.setText("" + s.getAge());
        viewHolder.subjectCategory.setText(s.getCategory());
        viewHolder.subjectNumber.setText("Subject " + s.getNumber());
        int inches = s.getHeight()%12;
        int feet = (s.getHeight() - inches) / 12;

        viewHolder.subjectHeight.setText(getHeightString(feet, inches));
        viewHolder.subjectWeight.setText(String.format(context.getResources()
                .getString(R.string.weight_format_string), s.getWeight()));
        viewHolder.subjectCircleText.setText("" + s.getNumber());


        return convertView;
    }


    private String getHeightString(int feet, int inches) {
        Resources res = context.getResources();
        return String.format(res.getString(R.string.height_format_string), feet, inches);
    }

}
