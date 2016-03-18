package com.smithahm.coursemanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailsAdapter extends ArrayAdapter<Details> {
    private Context mContext;
    private List<Details> mCourses;
    ImageButton file;

    public DetailsAdapter(Context context, List<Details> objects) {
        super(context, R.layout.details_row_item, objects);
        this.mContext = context;
        this.mCourses = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.details_row_item, null);
        }
        final Details course = mCourses.get(position);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.assignment_id);
        TextView idView = (TextView) convertView.findViewById(R.id.date_id);
        file = (ImageButton)convertView.findViewById(R.id.file);

        file.setOnClickListener(new View.OnClickListener() {

    //Files will be store in Device Internal storage
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Downloading attachment", Toast.LENGTH_LONG).show();
                ParseFile file = course.getFile();
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            String filename = course.getFileName();
                            File myFile = new File(Environment.getExternalStorageDirectory(), filename);

                            if (!myFile.exists())
                                try {
                                    myFile.createNewFile();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(myFile);
                                fos.write(bytes);
                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                Toast.makeText(mContext, "Downloaded", Toast.LENGTH_LONG).show();
            }
        });
        descriptionView.setText(course.getContent() + " - ");
        Calendar c = Calendar.getInstance();
        Date dateString = course.getSubDate();
        c.setTime(dateString);
        c.add(Calendar.DATE, -1);
        dateString = c.getTime();
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yy");
        String formattedDate = outputFormat.format(dateString);
        idView.setText("Due: "+formattedDate);
        convertView.setOnCreateContextMenuListener(null);
        return convertView;
    }


}