package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by David on 2/18/2015.
 */
public class WelcomeActivity extends ActionBarActivity {

    DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onWelcomeEnter(View view) {
        db = new DatabaseHandler(this, null, null, 1);
        EditText pw = (EditText) findViewById(R.id.textWelcomePassword);
        EditText rpw = (EditText) findViewById(R.id.textRetypePassword);
        if (pw.getText().toString().equals(rpw.getText().toString())) {
            db.addPassword(md5(pw.getText().toString()));
            Toast.makeText(getApplicationContext(), "Password has been saved!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(WelcomeActivity.this, AddExpenseActivity.class));
        }
        else {
            pw.setText("");
            rpw.setText("");
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

