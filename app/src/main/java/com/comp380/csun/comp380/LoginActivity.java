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

// This class is for if the user has a password in DB
public class LoginActivity extends ActionBarActivity {

    private Button mSubmit;
    private EditText mPassword;

    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPassword = (EditText)findViewById(R.id.textPassword);
        mSubmit = (Button)findViewById(R.id.buttonPassword);

        // set the submit button to be off initially.
        mSubmit.setEnabled(false);
        mSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        //region register a text listener
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // enable the button since the text has changed
                // and set the color.
                mSubmit.setEnabled(true);
                mSubmit.setBackgroundColor(getResources().getColor(R.color.buttons));

                // if the user tries to input a blank field disable
                // the button and change its color.
                if (mPassword.getText().toString().equals("")) {
                    mSubmit.setEnabled(false);
                    mSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //endregion
    }

    public void onLogin(View view) {
        db = new DatabaseHandler(this,null,null,1);
        EditText pw = (EditText)findViewById(R.id.textPassword);
        if (db.checkPassword(md5(pw.getText().toString()))) {
            finish();
            startActivity(new Intent(LoginActivity.this, AddExpenseActivity.class));
        }
        else {
            pw.setText("");
            Toast.makeText(getApplicationContext(), "Password Mismatch!", Toast.LENGTH_SHORT).show();
        }
    }

    //MD5 Hash Functions Section
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
}
