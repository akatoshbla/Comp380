package com.comp380.csun.comp380;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by David on 3/11/2015.
 */

// TODO: Need to label methods
public class BudgetReport {

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
        }
        else {
            setTotalBudget(tabName);
        }

        setBudgetMax(tabName);
        setProgressBar();
        setStatus();
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

    public String getTopFiveVendors(int position) {
        if (topFiveVendors != null && position < topFiveVendors.size()) {
            return topFiveVendors.get(position).toString();
        }
        return "";
    }

    public String getTopFiveCategories(int position) {
        if (topFiveCategories != null && position < topFiveCategories.size()) {
            return topFiveCategories.get(position).toString();
        }
        return "";
    }

    // This method will set the budgetCurrent and topFiveCategories ArrayList
    private void setTotalAllBudgets() {

        int totalBudgetSum = 0;
        topFiveCategories = new ArrayList<>();
        Cursor cursor = db.categoryByCost();
        //cursor.moveToPrevious();

            // Linear search through list to get total Budget and
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction(db.categoryToString(cursor.getInt(0)),
                        Float.parseFloat(cursor.getString(1)));
                topFiveCategories.add(transaction);
                totalBudgetSum += Float.parseFloat(cursor.getString(1));
            }

            budgetCurrent = totalBudgetSum;
            sortTransactions(topFiveCategories);
            budgetMax = 150;

        if (totalBudgetSum == 0) {
            budgetCurrent = 0;
            budgetMax = 150;
            topFiveCategories = null;
        }
    }

    // This method will setup the budgetCurrent, progressBar, and status vars
    private void setTotalBudget(String tabName) {

        int totalVendorSum = 0;
        topFiveVendors = new ArrayList<>();
        Cursor cursor = db.vendorByCost(tabName);
        //cursor.moveToPrevious();

        // Linear search through list to get total Budget and
        while (cursor.moveToNext()) {
                Transaction transaction = new Transaction(db.vendorIdToString(cursor.getInt(0)),
                        Float.parseFloat(cursor.getString(1)));
                topFiveVendors.add(transaction);
                totalVendorSum += Float.parseFloat(cursor.getString(1));
        }

        budgetCurrent = totalVendorSum;
        sortTransactions(topFiveVendors);
        budgetMax = 150;//db.getBudget(tabName);
    }

    // This method will setup the Top 5 Categories and costs in a TreeMap with a
    // Transaction object as the value.
    private void sortTransactions(ArrayList<Transaction> alist) {
        TransactionComparator comparator = new TransactionComparator();
        Collections.sort(alist, comparator);
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

        if (progressBar < 40) {
            status = R.drawable.greenprogressbar;
        } else if (progressBar >= 40 && progressBar < 60) {
            status = R.drawable.yellowprogressbar;
        } else if (progressBar >= 60 && progressBar <= 99) {
            status = R.drawable.orangeprogressbar;
        } else {
            status = R.drawable.redprogressbar;
        }
    }

    // Sets the progressBar in the fragment layout
    private void setProgressBar() {
        if (budgetMax == 0) {
            progressBar = 0;
        } else {
            progressBar = (int)((double)(budgetCurrent) / budgetMax * 100);
        }
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

    public static class TransactionComparator implements Comparator<Transaction> {

        @Override
        public int compare(Transaction tOne, Transaction tTwo) {
            Float costOne = tOne.getCost();
            Float costTwo = tTwo.getCost();

            if (costOne > costTwo) {
                return -1;
            }
            else if (costOne  < costTwo) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
