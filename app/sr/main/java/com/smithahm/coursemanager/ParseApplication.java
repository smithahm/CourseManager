package com.smithahm.coursemanager;

import android.app.Application;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ParseApplication extends Application{

    @Override
    public void onCreate() {
        Parse.initialize(this, "TN08zCxvlHqQAOMFsWLNRXWI8tGMP1rwtwamZGhl", "plWmNF4HVYMmSFHN5b7KjkmAOs9I74Ck4XTfidfo");

        String userNameTxt = "818485649";
        String passwordtxt = "818485649";
        ParseUser user = new ParseUser();
        user.setUsername(userNameTxt);
        user.setPassword(passwordtxt);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up, please log in.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sign up Error", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Course t = new Course();
        t.setCourseId("CS645");
        t.setTitle("Advanced Web development");
        t.setOwner(ParseUser.getCurrentUser());
        t.saveEventually();


        String dateString = "08/12/15";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),
                date.toString(),
                Toast.LENGTH_LONG).show();

        byte[] data = ("Assignment 1 - Life Cycle Events\n" +
                "App description\n\t" +
                "You are going to create an app that logs the standard life cycle activity methods (onCreate, " +
                "onRestart, onStart, onPause, onSaveInstanceState, onRestoreInstanceState and onResume) " +
                "are called. Create an app with one activity and implement the methods onCreate, onRestart, " +
                "onStart, on Pause, onSaveInstanceState, onRestoreInstanceState and onResume in the activ" +
                "ity. Look up the methods in the documentation for their signatures. (Which ones require you " +
                "call super?) Each time one of the method () is called you are to do two things: \n" +
                "1 Write the name of the method to the log (LogCat) \n" +
                "2 Append the name of the method as a new line of text at the end of a TextView in the interface \n" +
                "of the app. ").getBytes();
        ParseFile file = new ParseFile("assignment1.txt", data);
        file.saveInBackground();

        Details d = new Details();
        d.setCourseId("CS646");
        d.setContent("Assignment #1");
        d.setSubDate(date);
        d.setPartner(true);
        d.setFile(file);
        d.saveInBackground();

        ParseQuery<Details> query = ParseQuery.getQuery(Details.class);
        query.getInBackground("tJCraP5tRU", new GetCallback<Details>() {
            @Override
            public void done(Details details, ParseException e) {
                details.setFileName("assignment4.txt");
                details.saveInBackground();
            }
        });
    }
}