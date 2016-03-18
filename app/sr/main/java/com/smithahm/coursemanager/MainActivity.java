package com.smithahm.coursemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    Button loginbutton;
    ProgressDialog pd;
    EditText username, password;
    String usernametxt, passwordtxt;
    public final static String REDID = "com.smithahm.coursemanager.REDID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseObject.registerSubclass(Course.class);
        ParseObject.registerSubclass(Details.class);
        Parse.initialize(this, "TN08zCxvlHqQAOMFsWLNRXWI8tGMP1rwtwamZGhl", "plWmNF4HVYMmSFHN5b7KjkmAOs9I74Ck4XTfidfo");

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.loggedIn));
        pd.setMessage(getResources().getString(R.string.wait));
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("Course Manager");
        toolbar.setTitleTextColor(Color.WHITE);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginbutton = (Button) findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                pd.show();
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                if(username.getText().length()==0)
                {
                    pd.dismiss();
                    username.setError(getResources().getString(R.string.noField));
                    return;
                }
                else if(password.getText().length()==0)
                {
                    pd.dismiss();
                    password.setError(getResources().getString(R.string.noField));
                    return;
                }
                else {
                    username.setError(null);
                    password.setError(null);
                }
                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {

                            public void done(ParseUser user, ParseException e) {
                               if (user != null) {
                                    Intent intent = new Intent(MainActivity.this,CourseList.class);
                                    intent.putExtra(REDID,usernametxt);
                                    startActivity(intent);
                                    pd.dismiss();
                                } else {
                                   pd.dismiss();
                                   Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

}
