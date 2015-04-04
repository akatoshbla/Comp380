package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectStreamException;

/**
 * Created by gdfairclough on 3/21/15.
 */
public class GoalsDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText goalNameBox;
    EditText goalCostBox;
    Button createButton;


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.goalsCreateButton:
                dismiss();

                String goalNameText = goalNameBox.getText().toString();

                double goalCostNum = Double.parseDouble(goalCostBox.getText().toString());

                Goal newGoal = new Goal(goalNameText, goalCostNum);

                try {
                    ((GoalsActivity)getActivity()).onDialogPositiveClick(this,newGoal);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            case R.id.goalsCancelButton:
                dismiss();
            default:
                dismiss();
        }




    }

    //set up callback to GoalsActivity

    public interface GoalsDialogListener {

        public void onDialogPositiveClick(DialogFragment dialog, Goal newGoal) throws FileNotFoundException, ObjectStreamException;

    }

    GoalsDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //verify callback is set up in the host activity

        try {

            mListener  = (GoalsDialogListener)activity;
        }catch(ClassCastException e){

            throw new ClassCastException(activity.toString() + " must implement Goals Dialog Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Create Goal");
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_goal,container,false);
        Button cancelButton = (Button)view.findViewById(R.id.goalsCancelButton);
        cancelButton.setOnClickListener(this);
        createButton = (Button)view.findViewById(R.id.goalsCreateButton);
        createButton.setOnClickListener(this);

        //default create button to disabled
        createButton.setEnabled(false);
        createButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        //values to set up new goal
        goalNameBox =  (EditText)view.findViewById(R.id.goalNameEdit);
        goalCostBox =  (EditText)view.findViewById(R.id.goalCostEdit);

        //addTextChangedListener(goalNameBox);
        //addTextChangedListener(goalCostBox);


        //add text changed listener for goal name field
        goalNameBox.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //enable the button when the text has been changed
                createButton.setBackgroundColor(getResources().getColor
                        (R.color.buttons));
                createButton.setEnabled(true);
                if (goalNameBox.getText().toString().equals("") || goalCostBox.getText().toString().equals("")) {
                    createButton.setEnabled(false);
                    createButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        //add text changed listener for goalCost field
        goalCostBox.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                createButton.setEnabled(false);
                createButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //enable the button when the text has been changed
                createButton.setBackgroundColor(getResources().getColor
                        (R.color.buttons));
                createButton.setEnabled(true);
                if (goalCostBox.getText().toString().equals("") || goalNameBox.getText().toString().equals("")) {
                    createButton.setEnabled(false);
                    createButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        return view;
    }



}
