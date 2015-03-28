package com.comp380.csun.comp380;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;


public class GoalsActivity extends ActionBarActivity implements View.OnClickListener,GoalsDialogFragment.GoalsDialogListener, GoalsSavingsDialog.SavingsDialogListener {

    private Goal currentGoal;
    private ProgressBar progressBar;
    private int progressBarColor;
    private TextView currentGoalText;
    private TextView progressBarText;
    private Button addButton;
    private final String FILENAME = "GoalsFile";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Init ToolBar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.goalsProgressBar);
        currentGoalText = (TextView)findViewById(R.id.currentGoalTextView);
        progressBarText = (TextView)findViewById(R.id.textGoalsProgress);
        addButton = (Button)findViewById(R.id.addButton);

        //load object from file (if it exists)
        try {
            currentGoal = loadGoal("GoalsFile", getBaseContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if(currentGoal == null){

            //put up a dialog popup for creating a new goal
            showGoalDialog();

            //test value, new goal based on today's date
            //currentGoal = new Goal("MacBook", 2200, 200 , new Date());

        }else{


            setProgressBar();
            addButton.setEnabled(true);
            currentGoalText.setText(currentGoal.getName());
            //compare current date to today's date, if the month has changed, show popup
            //for entering amount to save into the goal
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH);


            if(currentMonth != currentGoal.getLastAccessedMonth() ){

                //the month has changed since last access, so prompt the user to update savings
                showUpdateSavingsDialog(true, currentGoal.getLastAccessed());

            }
            //set lastAccessedDate to today's date
            currentGoal.setLastAccessed(new Date());
        }

    }

    private void showUpdateSavingsDialog(Boolean endOfMonth, Date lastAccessed) {

        GoalsSavingsDialog savingsDialog = new GoalsSavingsDialog();

        //forward boolean to fragment

        if(endOfMonth){

            savingsDialog.setEndOfMonth(lastAccessed);

        }

        //start the fragement
        savingsDialog.show(getFragmentManager(), "Savings Dialog");

    }

    private void saveGoal(Context context, String filename, Goal goal) throws FileNotFoundException, ObjectStreamException {


        try{

            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(goal);
            os.close();
            fos.close();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException(this.toString() + " could not find " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Goal loadGoal(String filename, Context context) throws FileNotFoundException {

        try{

            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Goal goal = (Goal) ois.readObject();
            ois.close();
            fis.close();

            return goal;

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(R.id.goals);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Switch to addExpenseActivity if the plus button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setProgressBar(){

        int percent = currentGoal.getProgressPercent();
        //set the progress bar to the current percentage of the goal
        progressBar.setProgress(percent);

        if (percent < 40) {
            progressBarColor = R.drawable.redprogressbar;
        } else if (percent >= 40 && percent < 60) {
            progressBarColor = R.drawable.orangeprogressbar;
        } else if (percent >= 60 && percent <= 99) {
            progressBarColor = R.drawable.yellowprogressbar;
        } else {
            progressBarColor = R.drawable.greenprogressbar;
        }

        DecimalFormat decFormat = new DecimalFormat("#,###,##0.00");

        String currentAmountString = decFormat.format(currentGoal.getCurrentAmount());
        String targetAmountString = decFormat.format(currentGoal.getTargetAmount());

        progressBarText.setText(currentAmountString + "/" +targetAmountString);
        progressBar.setProgressDrawable(getResources().getDrawable(progressBarColor));

    }

    /**
     * method for presenting the GoalsDialog fragment for Creating a new goal
     */
    public void showGoalDialog(){

        GoalsDialogFragment goalsDialog = new GoalsDialogFragment();
        goalsDialog.show(getFragmentManager(),"Budget Picker");

    }

    //use information passed back from the goal fragment
    public void onDialogPositiveClick(DialogFragment dialog, Goal newGoal) throws FileNotFoundException, ObjectStreamException {

        currentGoal = newGoal;

        //goal is being created, so set the lastAccessedDate and dateCreated to today;
        currentGoal.setLastAccessed(new Date());
        currentGoal.setDateCreated(new Date());

        //save the goal to load next time the app runs
        saveGoal(getBaseContext(),this.FILENAME,newGoal);

        setProgressBar();
        currentGoalText.setText(currentGoal.getName());



        //enable the abilty to add to the goal;
        addButton.setEnabled(true);
    }

    //overload onDialogPositiveClick for savings dialog

    public void onDialogPositiveClick(DialogFragment dialog, double amount) throws FileNotFoundException, ObjectStreamException {

        //update the amount saved in the current goal
        currentGoal.setCurrentAmount(currentGoal.getCurrentAmount() + amount);

        //save to the file

        saveGoal(getBaseContext(), this.FILENAME, currentGoal);

        setProgressBar();



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addButton:
                showUpdateSavingsDialog(false, new Date());
                break;
            case R.id.changeGoalButton:
                showGoalDialog();
            default:

        }
    }
}
