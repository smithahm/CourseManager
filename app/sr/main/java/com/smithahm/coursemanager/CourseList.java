package com.smithahm.coursemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends ActionBarActivity {
    ListView mListView;
    CourseAdapter mAdapter;
    public final static String COURSEID = "com.smithahm.coursemanager.MESSAGE";
    public final static String REDID = "com.smithahm.coursemanager.REDID";
    TextView redId, student;
    String message, idToPass;
    ProgressDialog pd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListView = (ListView) findViewById(R.id.course_list);
        redId = (TextView)findViewById(R.id.red_id);
        student = (TextView)findViewById(R.id.student);
        mAdapter = new CourseAdapter(this, new ArrayList<Course>());
        mListView.setAdapter(mAdapter);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.REDID);
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.download));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        if(message != null) {
            updateData();
            redId.setText(message);
            idToPass = message;

        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = mAdapter.getItem(i);
                String id = course.getCourseId();
                Intent intent = new Intent(CourseList.this, CourseDetails.class);
                intent.putExtra(COURSEID,id);
                intent.putExtra(REDID, idToPass);
                startActivity(intent);
            }
        });

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         // setSupportActionBar(toolbar);
         toolbar.setTitle("Course Manager");
         toolbar.setLogo(R.drawable.ic_launcher);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.inflateMenu(R.menu.menu_main);
         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        ParseUser.logOut();
                        navigateToLogin();
                        return true;
                    case R.id.settings:
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void updateData() {
        pd.show();
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> tasks, ParseException error) {
                if (!(tasks.isEmpty())) {
                    mAdapter.clear();
                    mAdapter.addAll(tasks);
                    pd.dismiss();
                }
                else{
                    pd.dismiss();
                    student.setText(getResources().getString(R.string.noCourse) + " " + message);
                    return;
                }
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.loggedOut),Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent setIntent = new Intent(this,MainActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }
}