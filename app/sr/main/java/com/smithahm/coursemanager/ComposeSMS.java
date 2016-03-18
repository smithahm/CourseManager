package com.smithahm.coursemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class ComposeSMS extends ActionBarActivity {

    private EditText smsInputText;
    private EditText smsInputDest;
    private Button smsSend;
    private SmsManager smsManager;
    final int PICK_CONTACT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_sms);
        smsInputDest = (EditText) findViewById(R.id.contacts);
        smsInputText = (EditText) findViewById(R.id.message);
        smsSend = (Button) findViewById(R.id.send);
        this.smsManager = SmsManager.getDefault();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Course Manager");
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_before);
        toolbar.inflateMenu(R.menu.menu_compose_sm);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addRecipient:
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
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

        this.smsSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                String dest = smsInputDest.getText().toString();
               if (PhoneNumberUtils.isWellFormedSmsAddress(dest)) {
                   if(smsInputText.getText().toString().isEmpty()) {
                       Toast.makeText(ComposeSMS.this, "Message body is Empty", Toast.LENGTH_LONG).show();
                       return;
                   }
                   else{
                       smsManager.sendTextMessage(smsInputDest.getText().toString(), null, smsInputText.getText().toString(), null, null);
                       Toast.makeText(ComposeSMS.this, "SMS message sent", Toast.LENGTH_LONG).show();
                       finish();
                   }
                } else {
                    Toast.makeText(ComposeSMS.this, "SMS destination invalid - try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String cNumber;

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);

                    if (c.moveToFirst()) {
                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:"+cNumber);
                            smsInputDest.setText(cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                }
                break;
        }
    }
}
