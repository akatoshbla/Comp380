package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that handles the application login.
 * This activity lives on its own and interfaces
 * with the database. It does input validation on
 * the password by comparing the hash of the user
 * input to the hash found on the database.
 * Once the password is valid it launches the next
 * activity.
 */

//TODO Make this activity be called upon application's onResumeS
public class LoginActivity extends ActionBarActivity {

    private Button mSubmit;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TEST open the expense viewer FOR TESTING PURPOSES ---------------------
        //create intent for ExpenseDisplayActivity
        //Intent displayExpenses = new Intent(this, AddExpenseActivity.class);

        //startActivity(displayExpenses);

        //------------------------------------------

        // set the layout, button and text field
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

    //region Check the database for the password.
    /**
     * Handles the button press.
     * Pre-Condition: A valid clicked view must be
     * sent. Assumes the password is not empty.
     * Post-Condition: Starts the AddExpenseActivity
     * @param view The view that was clicked.
     */
    public void onLogin(View view) {
        DatabaseHandler db = new DatabaseHandler(this,null,null,1);
        if (db.checkPassword(md5(mPassword.getText().toString()))) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else {
            mPassword.setText(null);
            mPassword.setError("Invalid!");
        }
    }
    //endregion

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