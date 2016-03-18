package com.smithahm.coursemanager;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import static android.R.anim.fade_out;

public class CourseAdapter extends ArrayAdapter<Course> {
    private Context mContext;
    private List<Course> mCourses;
    TextSwitcher mTextSwitcher;

    public CourseAdapter(Context context, List<Course> objects) {
        super(context, R.layout.course_row_item, objects);
        this.mContext = context;
        this.mCourses = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.course_row_item, null);
        }
        Course course = mCourses.get(position);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.course_description);
        //TextView idView = (TextView) convertView.findViewById(R.id.course_id);
        descriptionView.setText(course.getCourseId() + ": " + course.getTitle());
        //idView.setText(course.getCourseId());

        return convertView;
    }
}
