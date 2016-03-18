package com.smithahm.coursemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetails extends ActionBarActivity{
    String message, redID;
    ListView mListView;
    DetailsAdapter mAdapter;
    Date givenDateString;
    Boolean partnerRequired = false;
    private Details d;
    ContextMenu menuTosave;
    View viewTosave;
    ProgressDialog pd;
    TextView redId,descriptionView, tapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mListView = (ListView) findViewById(R.id.details_list);
        mAdapter = new DetailsAdapter(this, new ArrayList<Details>());

        Intent intent = getIntent();
        message = intent.getStringExtra(CourseList.COURSEID);
        redID = intent.getStringExtra(CourseList.REDID);
        descriptionView = (TextView)findViewById(R.id.id_details);
        tapView = (TextView)findViewById(R.id.tap);
        redId = (TextView)findViewById(R.id.red_id);
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.download));
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        if(message != null) {
            updateDetails();
            descriptionView.setText(getResources().getString(R.string.assignment) + " "+  message);
            redId.setText(redID);
        }
       mListView.setAdapter(mAdapter);
       mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < parent.getChildCount(); i++)
                {
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                d =((DetailsAdapter) mListView.getAdapter()).getItem(position);
                view.setBackgroundColor(Color.LTGRAY);
                return false;
            }
        });

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       toolbar.setTitle("Course Manager");
       toolbar.setLogo(R.drawable.ic_launcher);
       toolbar.setTitleTextColor(Color.WHITE);
       toolbar.setNavigationIcon(R.drawable.ic_before);
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
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        registerForContextMenu(mListView);
    }

    public void updateDetails() {
        pd.show();
        ParseQuery<Details> query = ParseQuery.getQuery(Details.class);
        query.whereEqualTo("courseId", message);
        query.findInBackground(new FindCallback<Details>() {
            @Override
            public void done(List<Details> tasks, ParseException error) {
                if (!(tasks.isEmpty())) {
                    pd.dismiss();
                    mAdapter.clear();
                    mAdapter.addAll(tasks);
                }else{
                    pd.dismiss();
                    descriptionView.setText(getResources().getString(R.string.noAssignment) + " " + message);
                    tapView.setText("");
                }
            }
        });
    }

    public void scheduleAlarm() {
        Log.i("rew", "Alarm called");
        ParseQuery<Details> query = ParseQuery.getQuery(Details.class);
        query.whereEqualTo("courseId", message);
        query.getFirstInBackground(new GetCallback<Details>() {
            @Override
            public void done(Details details, ParseException e) {
                givenDateString = details.getSubDate();
                setAlarm();
            }
        });
    }


    public void setAlarm(){
        this.finish();
        Long startTime;
        Long endTime;
        Calendar cal = Calendar.getInstance();
        cal.setTime(givenDateString);
        cal.add(Calendar.DATE, -1);
        int years = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(years, month, day, 10, 30);
        startTime = beginCal.getTimeInMillis();
        Calendar endCal = Calendar.getInstance();
        endCal.set(years, month, day, 11, 30);
        endTime = endCal.getTimeInMillis();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("allDay", false);
        intent.putExtra("title", d.getContent());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
        intent.putExtra(CalendarContract.Events.STATUS, true);
        intent.putExtra(CalendarContract.Events.VISIBLE, 0);
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, Color.MAGENTA);
        intent.putExtra(CalendarContract.Events.DISPLAY_COLOR, Color.MAGENTA);
        intent.putExtra(CalendarContract.Events.HAS_ALARM, true);
        startActivity(intent);
    }

    public void sendSMS(){
        /** Creating an intent to initiate view action */
        Intent intent = new Intent(this,ComposeSMS.class);
        startActivity(intent);
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.add(0, v.getId(), 0, getResources().getString(R.string.reminder));
        menuTosave = menu;
        viewTosave = v;

        ParseQuery<Details> query1 = ParseQuery.getQuery(Details.class);
        query1.whereEqualTo("content", d.getContent());
        query1.getFirstInBackground(new GetCallback<Details>() {
            @Override
            public void done(Details details, ParseException e) {
                pd.dismiss();
                if(e == null) {
                    partnerRequired = details.getPartner();
                    if(partnerRequired){
                        partnerRequired = false;
                        menuTosave.add(0, viewTosave.getId(), 0, getResources().getString(R.string.partner));
                    }
                }
            }
        });
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(getResources().getString(R.string.reminder))) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            scheduleAlarm();
        }
        else if(item.getTitle().equals(getResources().getString(R.string.partner))) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            sendSMS();
        }
        else {
            return false;
        }
        return true;
    }


    private void navigateToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.loggedOut),Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}

