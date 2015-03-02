package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by David on 2/18/2015.
 */
public class WelcomeActivity extends ActionBarActivity {

    private Button mSubmit;
    private EditText mPassword;
    private EditText mRetryPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // wire up the view and the items
        setContentView(R.layout.activity_welcome);
        mPassword = (EditText) findViewById(R.id.textWelcomePassword);
        mRetryPassword = (EditText) findViewById(R.id.textRetypePassword);
        mSubmit = (Button) findViewById(R.id.buttonCheckPassword);

        // set the submit button to be off initially.
        mSubmit.setEnabled(false);
        mSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        //region register a text listener
        mRetryPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // check to see if the retry field is empty
                // turn off the button if it is
                if(mRetryPassword.getText().toString().equals("")) {
                    mSubmit.setEnabled(false);
                    mSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
                // check to see if the passwords match
                // turn on the button if they match
                else if(mPassword.getText().toString().equals(mRetryPassword.getText().toString())){
                    mSubmit.setEnabled(true);
                    mSubmit.setBackgroundColor(getResources().getColor(R.color.buttons));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //endregion
    }

    //region Handle the Button click
    /**
     * Handles the button press.
     * Pre-Condition: A valid clicked view must be
     * sent in and assumes the password is validated.
     * Post-Condition: Initializes a new Activity.
     * @param view The view that was clicked.
     */
    public void onWelcomeEnter(View view) {
        DatabaseHandler db = new DatabaseHandler(this, null, null, 1);
        db.addPassword(md5(mPassword.getText().toString()));
        finish();
        startActivity(new Intent(WelcomeActivity.this, AddExpenseActivity.class));
    }//endregion

    //region MD5 Hash Functions Section
    private char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String byteArrayToHex(byte[] array) {
        String s = "";
        for (int i = 0; i < array.length; ++i) {
            int di = (array[i] + 256) & 0xFF; // Make it unsigned
            s = s + hextable[(di >> 4) & 0xF] + hextable[di & 0xF];
        }
        return s;
    }

    private String digest(String s, String algorithm) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return s;
        }

        try {
            m.update(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            m.update(s.getBytes());
        }
        return byteArrayToHex(m.digest());
    }

    private String md5(String s) {
        return digest(s, "MD5");
    }
    //endregion
}