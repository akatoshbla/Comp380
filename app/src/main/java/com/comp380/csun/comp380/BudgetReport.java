package com.comp380.csun.comp380;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by David on 3/11/2015.
 */

// TODO: Need to test once methods are finished (Cases only 1 ven or cat - 2 3 4)
public class BudgetReport  {

    private DatabaseHandler db;
    private int budgetCurrent;
    private int budgetMax;
    private int status;
    private int progressBar;
    private ArrayList<Transaction> topFiveVendors;
    private ArrayList<Transaction> topFiveCategories;

    public BudgetReport(DatabaseHandler db, String tabName) {
        this.db = db;

        if (tabName.equals("All")) {
            setTotalAllBudgets();
            setBudgetMax(tabName);
            setStatus();
            setProgressBar();
        }
        else {
            setTotalBudget(tabName);
            setBudgetMax(tabName);
            setStatus();
            setProgressBar();
        }
    }

    public int getBudgetCurrent() {
        return budgetCurrent;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public int getStatus() {
        return status;
    }

    public int getProgressBar() {
        return progressBar;
    }

    // TODO: Null if doesn't exist in ArrayList
    public String getTopFiveVendors(int position) {
        return topFiveVendors.get(position).toString();
    }

    // TODO: Null if doesn't exist in ArrayList
    public String getTopFiveCategories(int position) {
        return topFiveCategories.get(position).toString();
    }

    // This method will set the budgetCurrent and topFiveCategories ArrayList
    private void setTotalAllBudgets() {

        int totalBudgetSum = 0;
        Cursor cursor = db.getAllRows();
        cursor.moveToFirst();

        // Linear search through list to get total Budget and
        while (cursor.moveToNext()){
            Transaction transaction = new Transaction(cursor.getString(1),
                    Float.parseFloat(cursor.getString(3)));
            topFiveCategories.add(transaction);
            totalBudgetSum += Float.parseFloat(cursor.getString(3));
        }

        budgetCurrent = totalBudgetSum;
        sortTransactions(topFiveCategories);
    }

    // This method will setup the budgetCurrent, progressBar, and status vars
    private void setTotalBudget(String tabName) {

        int totalVendorSum = 0;
        // TODO: Cursor gets all vendors from db under category tabName instead of rows?
        Cursor cursor = db.getAllRows();
        cursor.moveToFirst();

        // Linear search through list to get total Budget and
        while (cursor.moveToNext()) {
            if (cursor.getString(2).equals(tabName)) {
                Transaction transaction = new Transaction(cursor.getString(2),
                        Float.parseFloat(cursor.getString(3)));
                topFiveVendors.add(transaction);
                totalVendorSum += Float.parseFloat(cursor.getString(3));
            }
        }

        budgetCurrent = totalVendorSum;
        sortTransactions(topFiveVendors);
    }

    // This method will setup the Top 5 Categories and costs in a TreeMap with a
    // Transaction object as the value.
    private void sortTransactions(ArrayList<Transaction> alist) {
        // TODO: Need to create sorting algorithm for ArrayList<Transaction>
    }

    // Access the db and sets the budgetMax based on tabName shown
    private void setBudgetMax(String incomeBudgetName) {
        if (incomeBudgetName.equals("All")) {
            // TODO: Add all the category incomes up and set to budgetMax
        } else {
            // TODO: Need a method to get user set BudgetMax from db
            //budgetMax = db.getBudgetMax(incomeBudgetName);

        }
    }

    // Sets the status color of the defCom textView in fragment layout
    private void setStatus() {
        int temp = (int)(budgetCurrent / budgetMax);
        if (temp < 40) {
            status = R.color.good;
        }
        else if (temp > 40 && temp < 60) {
            status = R.color.caution;
        }
        else if (temp > 60 && temp < 99) {
            status = R.color.danger;
        }
        else {
            status = R.color.bad;
        }
    }

    // Sets the progressBar in the fragment layout
    private void setProgressBar() {
        progressBar = (int)(budgetCurrent / budgetMax);
    }

    // Object for the Transactions that are put into an ArrayList(DataStructure)
    public static class Transaction {

        private String transactionName;
        private float transactionCost;

        public Transaction(String name, float cost) {
            transactionName = name;
            transactionCost = cost;
        }

        public float getCost()  {
            return transactionCost;
        }

        public String toString() {
            return transactionName + " " + transactionCost;
        }
    }
}
